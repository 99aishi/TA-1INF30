# Economix — AGENTS.md

## Project overview

Two-codebase architecture under `TA-1INF30/`:
- **Java (`apps/EconomixSolution`)**: Jakarta REST (JAX-RS) backend → MySQL. Maven multi-module (7 modules), Java 25. WAR deployed to GlassFish/Payara.
- **.NET (`web/EconomixSolution`)**: Blazor Interactive Server frontend + WS proxy layer. Cookie auth with Claims, no JWT.

Java WS (port 8080) → .NET WS proxy → Blazor UI.

## Architecture layers (Java)

EconomixWS → EconomixNegocio (BO/BOI) → EconomixPersistencia (DAO/DAOI) → EconomixDBManager → MySQL.
- `EconomixModel` — domain POJOs (`rrhh`, `tesoreria`, `operaciones`, `auditoria`)
- `EconomixNegocio` — `ibo/` (interfaces), `boi/` (implementations). **BOs only call DAOs; BOs never call other BOs.**
- `EconomixPersistencia` — `idao/` (interfaces), `daoi/` (impls), stored procedure calls via `DBManager`. **DAOs only call stored procedures; DAOs never call other DAOs.**
- `EconomixDBManager` — `DBManager` singleton, reads `datosBD.properties` from classpath
- `EconomixTest` — JUnit/integration test classes
- `Economix` — launcher with `Main.java`

Note: `datosBD.properties` exists in **both** `EconomixWS/src/main/resources/` and `Economix/src/main/resources/`.

## Build commands

```bash
# Java (from apps/EconomixSolution)
mvn compile                 # 0 errors expected
mvn clean package           # WAR in EconomixWS/target/

# .NET (from web/EconomixSolution)
dotnet build                # 0 errors expected
# Blazor dev server:
dotnet run --project EconomixWA   # http://localhost:5247
```

## Java REST endpoints (`@Path("XWS")`)

Base path: `@ApplicationPath("webresources")` in `RestAplication.java`.
Dev URL: `http://localhost:8080/EconomixWS-1.0-SNAPSHOT/webresources/`

Convention per endpoint class in `pe.edu.pucp.economix.economixws.{domain}.ws/`:
- `UsuarioWS` — `POST /login` (returns `Usuario` or `null`)
- `AreaWS` — `ListarAreas`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`, `ListarActivas`, `Recuperar`
- `RolWS` — `ListarRoles`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `EmpleadoWS` — `ListarEmpleados`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `AdministradorWS` — `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `CajaChicaWS` — `ListarCajasChicas`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `CuentaBancariaWS` — `ListarCuentas`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `MonedaWS` — `ListarMonedas`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`, `ListarActivas`, `Recuperar`
- `CicloCajaWS` — `ListarCiclos`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`, `CerrarCiclo`
- `SolicitudGastoWS` — `Listar`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`, `Evaluar`, `EjecutarDesembolso`, `HorarioHabilitado`
- `ComprobantePagoWS` — `ListarComprobantes`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `RendicionWS` — `Listar`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`, `GenerarRendicionDeCiclo`, `ObservarRendicion`, `AceptarRendicion`, `DenegarRendicion`, `ReEnviarRendicion`, `ListarPorArea`
- `TransaccionWS` — `ListarTransacciones`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `AuditoriaWS` — `ListarLog`, `InsertarLog` (returns audit logs)

Password hashing: Argon2 via `spring-security-crypto` (`Usuario.setPassword()`).

## .NET solution (`web/EconomixSolution/EconomixSolution.slnx`)

3 projects: `EconomixModel`, `EconomixWS`, `EconomixWA`. Targets `net10.0`. Central package management via `Directory.Packages.props`.

WS proxy layer (`EconomixWS/{domain}/` — subdirectories `rrhh/`, `tesoreria/`, `operaciones/`, and `auditoria/`):
- `IWS/IWS.cs` — generic `IWS<T>` with `insertar`, `actualizar`, `eliminar(int id)`, `listar`
- `IWS/I{Entity}WS.cs` — domain-specific interface extending `IWS<T>`
- `WSImpl/{Entity}WSImpl.cs` — `HttpClient` implementation using sync-over-async (`.GetAwaiter().GetResult()`)

## Blazor UI (`EconomixWA/`)

- `BlazorDisableThrowNavigationException=true` in csproj
- **Auth**: Cookie-based via `Program.cs` `/auth/login` endpoint. 8h expiry, sliding, `IsPersistent=true`.
- **Role mapping**: Role claim is mapped using `DeterminarRolEmpleado` based on Area and `RolFlujo` (Administrador, Jefe, Empleado, Tesoreria).
- **Session user**: Reconstructed from claims via `IUsuarioWS.GetCurrentUser()`.
- **Generic Component Pattern**: 
  Each entity module (e.g. Area, Moneda, Empleado) follows a standard layout structure:
  - `{Entidad}Page.razor` (Main route container)
  - `{Entidad}Item.razor` (List row or card representation)
  - `{Entidad}Detalle.razor` (Form side-panel for View/Create/Edit)
  - `{Entidad}SearchBar.razor` (Encapsulated filters emitting search events)

- **Two layouts**: `LoginLayout.razor` vs `MainLayout.razor` (full app chrome).

## Database

MySQL. Stored procedure files in `database/{domain}/` (named `{entidad}StoreProcedure.sql`). Module prefixes: `rrhh_`, `tes_`, `ope_`.
Audit trail is maintained dynamically via triggers in `{entidad}TriggersAuditoria.sql` mapping changes to `log_auditoria`.

## Business Rules — Auto-Calculations (SP Layer)

All calculations for cycle/rendición totals happen at the database SP level, not in Java or .NET:

### Cycle (`CicloCajaStoreProcedure.sql`)
- **`pa_insertar_ciclo_caja`**: If `p_numero_semana ≤ 0`, auto-calculates `WEEK(fecha_apertura, 1)` using MySQL's `WEEK()`.
- **`pa_modificar_ciclo_caja`** (close/liquidate):
  1. **Auto `totalGastado`**: Overrides any passed value with `SUM(monto_solicitado)` of `APROBADO` solicitudes in the cycle.
  2. **Auto rendición**: If status changes to `CERRADO`/`LIQUIDADO` and no rendición exists, creates one with:
     - `totalDeclarado` = `SUM(cp.monto_total)` of non-`ANULADO` comprobantes across all solicitudes in the cycle
     - `totalAprobado` = same calculated `totalGastado`
     - `saldoFinal` = `saldoInicial - totalAprobado`
     - `estado` = `EN_ESPERA`, `fechaPresentacion` = `CURDATE()`
     - Cycle's `id_rendicion` FK updated to the new row.
- **All read SPs**: Return live calculated values via LEFT JOIN subqueries (`COALESCE(calculated, stored_value)`) — always reflects current state regardless of stored data.

### Rendición (`RendicionStoreProcedure.sql`)
- **`pa_insertar_rendicion`**: Added `p_id_ciclo_caja` (was missing from table insert). Auto-calculates `totalDeclarado`, `totalAprobado`, `saldoFinal` when totals are 0 and a valid cycle ID is provided.
- **`pa_modificar_rendicion`**: Same auto-calculation logic. Falls back to the stored `id_ciclo_caja` from the existing row if not provided.
- **All read SPs**: Live calculated values via subqueries (same pattern as cycles).

### Solicitud → comprobantes chain
- `totalDeclarado` (rendición) = `SUM(cp.monto_total)` where `cp.estado_comprobante != 'ANULADO'` for solicitudes in the cycle.
- `totalGastado` (cycle) / `totalAprobado` (rendición) = `SUM(sg.monto_solicitado)` where `sg.estado_solicitud = 'APROBADO'` in the cycle.

### File locations
- `database/Operaciones/CicloCajaStoreProcedure.sql`
- `database/Operaciones/RendicionStoreProcedure.sql`
- `apps/EconomixSolution/EconomixPersistencia/src/main/java/.../daoi/RendicionDAOImpl.java`

## Business Rules — Edit Validation / Permission System (Comprobantes)

When a `ope_comprobante_pago` is modified, the database enforces whether the edit is allowed based on the parent cycle's state.

### Cycle states and edit permission
- **`ABIERTO`**: free edit allowed.
- **`CERRADO`**, **`LIQUIDADO`**, **`EN_EXCEPCION`**: edit requires an active 48-hour permission (`ope_permiso_edicion`).

### Database enforcement
- `ope_ciclo_caja.estado_ciclo` enum includes `EN_EXCEPCION`.
- `BEFORE UPDATE` trigger on `ope_comprobante_pago` (`ComprobantePagoTriggersAuditoria.sql`):
  1. Resolves the cycle via `comprobante → solicitud → ciclo`.
  2. If cycle is not `ABIERTO`, looks for an active permission for that comprobante where the current modifier is either the `id_usuario_solicitante` or the `id_usuario_autorizador`.
  3. If found, marks the permission `USADO` and allows the update.
  4. If not found, raises `SIGNAL SQLSTATE '45000'` with message `El ciclo de caja chica no permite ediciones. Solicite autorizacion a Jefe o Tesorero (48h).`

### Permission lifecycle (`ope_permiso_edicion`)
1. **Employee requests** (`pa_solicitar_permiso_edicion`): inserts row with `id_usuario_autorizador = NULL`, `estado = 'ACTIVO'`, `fecha_expiracion = NOW() + 48h`.
2. **Jefe/Tesorero grants** (`pa_otorgar_permiso_edicion`): sets `id_usuario_autorizador`, `motivo_autorizacion`, and resets `fecha_expiracion = NOW() + 48h`.
3. **Revoke** (`pa_revocar_permiso_edicion`): sets `estado = 'REVOCADO'`.
4. **Use**: trigger consumes the permission on first successful edit (`USADO`).

### UI/UX
- `ComprobanteDePagoDetalle.razor` / `ComprobanteDePagoDatos.razor`: read-only mode when cycle is closed/liquidated/in-exception. Inputs disabled, no save button, shows "Solicitar autorización" modal.
- `PermisosPendientes.razor`: review page for Jefe/Tesorero with two tabs:
  - **Pendientes**: requested + granted-but-unused permissions.
  - **Excepción (fin de semana)**: comprobantes in cycles with `estado_ciclo = 'EN_EXCEPCION'`.
- Menu item `Permisos Pendientes` added to both `Jefe` and `Tesoreria` sidebars.

### Endpoints
- Java: `PermisoEdicionWS` (`Solicitar`, `Otorgar`, `Revocar`, `ListarPendientes`, `ListarEnExcepcion`).
- .NET proxy: `IPermisoEdicionWS` / `PermisoEdicionWSImpl`.

### File locations
- `database/General/Creacion.sql` — `ope_permiso_edicion` table + `EN_EXCEPCION` enum.
- `database/Operaciones/PermisoEdicionStoreProcedure.sql`
- `database/Operaciones/PermisoEdicionTriggersAuditoria.sql`
- `database/Operaciones/ComprobantePagoTriggersAuditoria.sql`
- `apps/.../operaciones/model/PermisoEdicion.java`, `idao/IPermisoEdicionDAO.java`, `daoi/PermisoEdicionDAOImpl.java`, `ibo/IPermisoEdicionBO.java`, `boi/PermisoEdicionBOImpl.java`, `economixws/operaciones/ws/PermisoEdicionWS.java`
- `web/.../EconomixModel/Model/operaciones/PermisoEdicion.cs`
- `web/.../EconomixWS/operaciones/IWS/IPermisoEdicionWS.cs`, `WSImpl/PermisoEdicionWSImpl.cs`
- `web/.../EconomixWA/Components/Pages/PermisosPendientes.razor`
- `web/.../EconomixWA/Components/Pages/ComprobanteDePago/ComprobanteDePagoDetalle.razor`
- `web/.../EconomixWA/Components/Pages/ComprobanteDePago/ComprobanteDePagoDatos.razor`
- `web/.../EconomixWA/Components/Pages/ComprobanteDePago/ComprobanteDePagoSustento.razor`
- `web/.../EconomixWA/Components/Layout/NavMenu.razor`

## Business Rules — Caja Chica First Cycle Auto-Creation

When a new `CajaChica` is inserted, the Java BO atomically creates the first `CicloCajaChica` for the current week inside a single transaction:
- `fecha_apertura = today`
- `numero_semana = Calendar.WEEK_OF_YEAR`
- `saldo_inicial = cajaChica.monto_techo`
- `total_gastado = 0`
- `estado = ABIERTO`

### Transaction orchestration
1. `CajaChicaBOImpl.insertar()` validates the business object (null checks, default `estado = ACTIVO`, valid cuenta bancaria/moneda, positive `montoTecho`) and starts a `DBManager` transaction.
2. `CajaChicaDAOImpl.insertar()` executes `pa_insertar_fondo` and `pa_insertar_caja_chica` using `DBManager.ejecutarProcedimientoTransaccion()`.
3. `CicloCajaChicaDAOImpl.insertarEnTransaccion()` executes `pa_insertar_ciclo_caja` using the same transactional connection.
4. BO validates both generated IDs and commits; any exception triggers `cancelarTransaccion()`.

This avoids the previous bug where creating a Caja Chica from the bank-account page could leave a Caja Chica without its first cycle.

### Validation rules
- Caja Chica cannot be null.
- `CuentaBancaria` is required and must exist.
- `Moneda` is required and must exist.
- `Nombre` is required (non-empty).
- `MontoTecho` must be > 0.
- `Estado` defaults to `ACTIVO` when null.

### File locations
- `apps/.../tesoreria/boi/CajaChicaBOImpl.java`
- `apps/.../tesoreria/daoi/CajaChicaDAOImpl.java`
- `apps/.../operaciones/idao/ICicloCajaChicaDAO.java`
- `apps/.../operaciones/daoi/CicloCajaChicaDAOImpl.java`

## UI — Cuenta Bancaria

- `CuentaBancariaPage` keeps the account list on the left.
- Selecting an account opens the right panel with:
  - Account editor at the top.
  - **List of Cajas Chicas** belonging to that account below, using the new `CajaChicaItemSimple` style (clean card, main info only).
- A "+ Agregar Caja Chica" button inside the Cajas Chicas section opens `CajaChicaDetalle` as a component for creation.
- `CajaChicaItemSimple` actions:
  - **Ver ciclos** navigates to `/Caja Chica?cajaId={id}`.
  - **Editar** opens `CajaChicaDetalle` for editing.
  - **Eliminar** deletes the caja chica (visible when `ACTIVO`).
  - **Recuperar** reactivates the caja chica (visible when `INACTIVO`).
- When editing an existing account, the **owner type** (`Empleado`/`Área`) and the selected owner are **disabled/locked** to prevent switching an account from one owner type to another.

### File locations
- `web/.../EconomixWA/Components/Pages/CuentaBancaria/CuentaBancariaPage.razor`
- `web/.../EconomixWA/Components/Pages/CuentaBancaria/CuentaBancariaItem.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaItemSimple.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaDetalle.razor`

## UI — Caja Chica / Ciclos

The `/Caja Chica` route now shows **list of cajas chicas** (funds management):
- Filters: Caja Chica name, Área, Cuenta bancaria, Estado.
- List item (`CajaChicaItemSimple`) shows fund info:
  - Fund name, area name, monto techo.
  - **Badge**: Only shows for non-ACTIVO states (INACTIVO shows gray badge). ACTIVO items have no badge (default state).
  - Actions: "Ver ciclos" navigates to `/Caja Chica/Ciclos?cajaId={id}`, "Ver" opens detail.
- Detail side-panel (`CajaChicaDetalle`) supports view/edit/create; only Tesoreria can modify.

The `/Caja Chica/Ciclos` route (also accessible as `/Ciclos`) shows **all cycles**:
- Filters: Caja Chica name, Área, Cuenta bancaria, Estado, Fecha apertura desde/hasta.
- List item (`CicloCajaItem`) shows cycle info plus a summary of its expenses/requests:
  - Caja Chica name, week number, estado badge (always shown), fechas.
  - Solicitudes count + approved count.
  - Comprobantes count.
  - Techo, disponible.
- Detail side-panel (`CicloCajaDetalle`) supports view/edit/create; only Tesoreria can modify.
- State can be set to `ABIERTO`, `CERRADO`, `LIQUIDADO`, or `EN_EXCEPCION`.
- When a cycle is `EN_EXCEPCION`, Tesoreria sees a "Marcar como revisado" button to switch it back to `ABIERTO`.
- "Cerrar Ciclo" action calls `CicloCajaWS.cerrarCiclo()`.
- Query params: `?cajaId=X` pre-filters cycles by caja chica.
- Routes:
  - `/Caja Chica` — list of cajas chicas (funds).
  - `/Caja Chica/Ciclos` or `/Ciclos` — all cycles.
  - `/Caja Chica/Detalle` — view/edit cycle.
  - `/Caja Chica/Crear` — create cycle.
- NavMenu: Admin sees both "Caja Chica" (funds) and "Ciclos" (cycles); other roles see only "Caja Chica" (which shows cycles for them).

### File locations
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaPage.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CicloCajaItem.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CicloCajaDetalle.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CicloCajaSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaItem.razor` (kept for DashboardTesoreria)
- `web/.../EconomixWA/Components/Layout/NavMenu.razor`

## UI — Dashboard Administrador

The administrator dashboard uses a responsive 6-6 two-column layout with reusable card components:
- `SolicitudRecienteItem`: clickable card that navigates to `/Mis Solicitudes De Gasto/Detalle/{id}`.
- `ActividadDashboardItem`: read-only activity card with **color-coded icons and entity badges** using custom CSS classes from `app.css`:
  - **Icon color by action type** (`TipoEvento`) via `.actividad-icon-*` classes:
    - `INSERT` (Creó): green `.actividad-icon-success`
    - `UPDATE` (Modificó): blue `.actividad-icon-primary`
    - `DELETE` (Eliminó): red `.actividad-icon-danger`
    - `LOGIN_SUCCESS`: cyan `.actividad-icon-info`
    - `LOGIN_FAILED`: yellow `.actividad-icon-warning`
  - **Entity badge color by module** (`EntidadNombre`):
    - RRHH (Empleado, Usuario, Área, Rol): blue `bg-primary text-white`
    - Tesorería (Caja chica, Cuenta bancaria, Moneda): green `bg-success text-white`
    - Operaciones (Solicitud, Comprobante, Rendición, Transacción): orange `bg-warning text-dark`
- Layout: `col-12 col-lg-6` columns inside `row g-3 flex-fill overflow-hidden`.
- **Bottom clipping fix**: The outer container uses `pb-4` and the stat cards row uses `flex-shrink-0` to prevent card bottom borders and rounded corners from being clipped by `overflow-hidden`.

### File locations
- `web/.../EconomixWA/Components/Pages/MainDashBoard/DashboardAdministrador.razor`
- `web/.../EconomixWA/Components/Pages/MainDashBoard/SolicitudRecienteItem.razor`
- `web/.../EconomixWA/Components/Pages/MainDashBoard/ActividadDashboardItem.razor`

## UI — Dashboard Jefe: Inline Evaluation + Activity

The Jefe dashboard (`DashboardJefe.razor`) uses the same 6-6 two-column layout as the admin dashboard:
- **KPI cards**: Saldo de Caja Chica, Pendientes de Aprobación (count + urgentes), Gastos del Ciclo, Solicitudes Aprobadas.
- **Left column**: Pending solicitudes list via `SolicitudPendienteJefeItem`.
- **Right column**: Activity reciente filtered by the jefe's area (client-side). Loads 50 global logs, filters by employee IDs in the jefe's area via `EmpleadoWS.listarPorAreaAsync`, takes 10.

### `SolicitudPendienteJefeItem` component
Each pending solicitud card shows:
- Solicitud info: SG-Id, solicitante + area, fecha, motivo, monto.
- **3-state evaluation** (no navigation):
  1. **Idle**: 3 buttons — Aprobar (green), Observar (yellow), Rechazar (red).
  2. **Observando**: textarea appears for comment + "Confirmar Observación" + "Cancelar".
  3. **Rechazando**: textarea appears for comment (required) + "Confirmar Rechazo" + "Cancelar".
- **Aprobar**: immediate action, defaults comment to "Aprobado" if empty.
- **Observar/Rechazar**: requires clicking confirm after entering comment. Rechazar requires a comment (validation error if empty).
- After successful evaluation, the item is removed from the list and KPI counters update in real-time.

### File locations
- `web/.../EconomixWA/Components/Pages/MainDashBoard/DashboardJefe.razor`
- `web/.../EconomixWA/Components/Pages/MainDashBoard/SolicitudPendienteJefeItem.razor`

## Business Rules — SolicitudGasto ↔ Transaccion Linkage

### Schema
- `ope_solicitud_gasto.id_transaccion` — nullable FK to `ope_transaccion(id_transaccion)`.
- `ope_transaccion.id_solicitud_gasto` — nullable FK to `ope_solicitud_gasto(id_solicitud_gasto)`.
- `ope_solicitud_gasto.medio_desembolso` was removed; the payment method belongs to `Transaccion`.

### Approval atomic transaction
`SolicitudGastoBOImpl.evaluar()` runs inside a `DBManager` transaction:
1. Updates `SolicitudGasto` to `APROBADO` and sets `jefeAprobador`.
2. Creates a `Transaccion` of type `DESEMBOLSO` in state `REGISTRADA`.
3. Updates `SolicitudGasto.idTransaccion` with the generated transaction ID.
4. Commits or rolls back as a unit.

### Disbursement execution
`SolicitudGastoBOImpl.ejecutarDesembolso()` also runs inside a `DBManager` transaction:
1. Locates the pending `Transaccion` directly via `SolicitudGasto.idTransaccion`.
2. Sets `medioPago`, `cuentaDestino`, and `numeroOperacionBancaria`.
3. Marks the transaction `COMPLETADA` and the solicitud `PAGADO`.
4. Recalculates the cycle's `totalGastado`.

### UI behaviour
- Boss list (`SolicitudDeGastoRecibidaPage`) lists every solicitud except `ANULADO`, ordered by cycle and status.
- Boss detail (`SolicitudDeGastoRecibidaDetalle`) displays the linked transaction data (monto, moneda, cuenta origen, beneficiario, estado) when the solicitud is `APROBADO`/`PAGADO`/`RENDIDO`.
- Employee detail (`MiSolicitudDeGastoDetalle`) shows the transaction summary when `IdTransaccion > 0`.
- Transaction detail (`TransaccionDetalle`) is now a read-only data panel loaded from the selected transaction.

### File locations
- `apps/.../operaciones/model/SolicitudGasto.java`
- `apps/.../operaciones/model/Transaccion.java`
- `apps/.../operaciones/boi/SolicitudGastoBOImpl.java`
- `apps/.../economixws/operaciones/ws/SolicitudGastoWS.java`
- `web/.../EconomixModel/Model/operaciones/SolicitudGasto.cs`
- `web/.../EconomixModel/Model/operaciones/Transaccion.cs`
- `web/.../EconomixWS/operaciones/IWS/ISolicitudGastoWS.cs`
- `web/.../EconomixWS/operaciones/WSImpl/SolicitudGastoWSImpl.cs`
- `web/.../EconomixWA/Components/Pages/SolicitudDeGasto/SolicitudRecibida/SolicitudDeGastoRecibidaPage.razor`
- `web/.../EconomixWA/Components/Pages/SolicitudDeGasto/SolicitudRecibida/SolicitudDeGastoRecibidaDetalle.razor`
- `web/.../EconomixWA/Components/Pages/SolicitudDeGasto/MiSolicitudesDeGasto/MiSolicitudDeGastoDetalle.razor`
- `web/.../EconomixWA/Components/Pages/Transaccion/TransaccionDetalle.razor`
- `database/General/Creacion.sql`
- `database/Operaciones/SolicitudGastoStoreProcedure.sql`
- `database/Operaciones/TransaccionStoreProcedure.sql`
- `database/Operaciones/ComprobantePagoStoreProcedure.sql`
- `database/Operaciones/SolicitudGastoTriggersAuditoria.sql`

## Business Rules — Rendiciones & Friday Auto-Close

### Rendición lifecycle
- `EN_ESPERA` → `ACEPTADO` / `DENEGADO` / `OBSERVADO` / `ANULADO`
- `OBSERVADO` → `EN_ESPERA` (employee re-submits) / `ACEPTADO` / `DENEGADO` / `ANULADO`
- `ACEPTADO` and `ANULADO` are terminal; `pa_cambiar_estado_rendicion` rejects further changes.

### Cycle state side-effects (`pa_cambiar_estado_rendicion`)
- `OBSERVADO` → cycle becomes `EN_EXCEPCION`.
- `ACEPTADO` → cycle becomes `LIQUIDADO`.
- `EN_ESPERA` (via re-enviar from `OBSERVADO`) → cycle becomes `CERRADO`.

### Employee exception window (OBSERVADO)
When a cycle's rendición is `OBSERVADO`, the employee can only edit/delete the `ComprobantePago` rows of their solicitudes. The `SolicitudGasto` itself stays immutable. After correcting the vouchers, the employee clicks **Reenviar rendición**, which transitions the rendición back to `EN_ESPERA` and the cycle to `CERRADO` for treasury review.

### Friday auto-close event
MySQL event `ev_cierre_semanal_caja_chica` runs every Friday at 23:00:
1. Finds every `ABIERTO` cycle whose `fecha_cierre <= CURDATE()`.
2. Calls `pa_generar_rendicion_de_ciclo` (creates `EN_ESPERA` rendición and marks cycle `EN_EXCEPCION`).
3. Creates the next week's cycle (Monday–Sunday) with `saldoInicial = cajaChica.montoTecho` and `estado = ABIERTO`.

The event scheduler is enabled automatically by the script.

### Role-based access
- **Tesorería**: can `observar`, `aceptar`, `denegar`, and `generarRendicionDeCiclo`.
- **Jefe de Área**: sees only rendiciones of their area (`ListarPorArea`).
- **Administrador**: read-only view of all rendiciones.
- **Empleado**: interacts only through the OBSERVADO exception window in `MiSolicitudDeGastoDetalle`.

### File locations
- `database/General/EventosCicloRendicion.sql`
- `database/Operaciones/RendicionStoreProcedure.sql`
- `database/General/Creacion.sql` — `ope_rendicion.estado_rendicion` now includes `OBSERVADO`.
- `apps/.../operaciones/model/enums/EstadoRendicion.java`
- `apps/.../operaciones/boi/RendicionBOImpl.java`
- `apps/.../operaciones/daoi/RendicionDAOImpl.java`
- `apps/.../economixws/operaciones/ws/RendicionWS.java`
- `web/.../EconomixModel/Model/operaciones/Rendicion.cs`
- `web/.../EconomixWS/operaciones/IWS/IRendicionWS.cs`, `WSImpl/RendicionWSImpl.cs`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionPage.razor`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionDetalle.razor`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionItem.razor`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/SolicitudDeGasto/MiSolicitudesDeGasto/MiSolicitudDeGastoDetalle.razor`

## TO-DO / Pending Tasks

- [ ] **Login Auditing**: When a login is attempted (either successful or failed), the attempt should be logged to the `log_auditoria` database table via `AuditoriaWS`.

## Business Rules — Date/Time Handling & ISO 8601 Standard

### Overview
All date/time fields across the application now use full `DATETIME` precision (not `DATE`) and are serialized as **ISO 8601** strings (`2024-01-15T14:30:00Z`) across the entire stack. This ensures accurate relative time display ("hace 3h" instead of "ahora" for 3-hour-old events) and consistent time handling between Java and .NET.

### Database Migration
- `database/Alter_FechaTimestamp.sql` — one-time migration script that changes all `DATE` columns to `DATETIME` and updates stored procedure parameters.

**Tables modified:**
| Table | Columns | Before | After |
|---|---|---|---|
| `ope_solicitud_gasto` | `fecha_solicitud` | `DATE` | `DATETIME` |
| `ope_comprobante_pago` | `fecha_emision` | `DATE` | `DATETIME` |
| `ope_ciclo_caja` | `fecha_apertura`, `fecha_cierre` | `DATE` | `DATETIME` |
| `ope_rendicion` | `fecha_presentacion`, `fecha_aprobacion` | `DATE` | `DATETIME` |
| `tes_tipo_cambio` | `fecha_tipo_cambio` | `DATE` | `DATETIME` |

**Not modified (already DATETIME):** `ope_transaccion.momento_operacion`, `ope_permiso_edicion` dates, `log_auditoria.creado_at`.

### Java Backend — ISO 8601 Serialization (No DTOs)
Jackson is configured globally via `JacksonConfig.java` (`@Provider` `ContextResolver<ObjectMapper>`) to serialize all date fields as ISO 8601 strings:
- `WRITE_DATES_AS_TIMESTAMPS` disabled — outputs strings, not epoch arrays.
- `JavaTimeModule` registered — handles both `java.util.Date` and `java.time.LocalDateTime`.

This means:
- `java.util.Date` fields → `"2024-01-15T14:30:00.000+0000"` (with timezone offset)
- `LocalDateTime` fields → `"2024-01-15T14:30:00"` (ISO 8601)

No `@JsonFormat` annotations are needed on any model class.

### Java DAO Layer — Timestamp Precision
All DAO `rs.getDate()` calls changed to `rs.getTimestamp()` to preserve time information:
- `SolicitudGastoDAOImpl.java` — `fecha_solicitud`, `fecha_apertura`, `fecha_cierre`
- `ComprobantePagoDAOImpl.java` — `fecha_emision`, `fecha_solicitud`
- `RendicionDAOImpl.java` — `fecha_presentacion`, `fecha_aprobacion`, `fecha_apertura`, `fecha_cierre`
- `CicloCajaChicaDAOImpl.java` — `fecha_apertura`, `fecha_cierre`, `fecha_presentacion`, `fecha_aprobacion`
- `TipoCambioDAOImpl.java` — `fecha_tipo_cambio`

All `new java.sql.Date()` calls changed to `new java.sql.Timestamp()` when passing dates to stored procedures.

### .NET Frontend — Timezone-Aware Deserialization
`UnixDateTimeConverter.cs` updated to:
- **Read**: Parse ISO 8601 strings with `RoundtripKind`, default `Unspecified` to `Local`, convert UTC to local. Strips Java `ZonedDateTime.toString()` suffixes like `[UTC]`, `[America/Lima]` before parsing (Java serializes `ZonedDateTime` with zone ID in brackets).
- **Write**: Always serialize as local ISO 8601 with milliseconds and timezone offset (`yyyy-MM-ddTHH:mm:ss.fffK`), matching Java's `SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")` pattern exactly.

### Dashboard Relative Time Display
`ActividadDashboardItem.razor` now correctly shows relative times because `creado_at` was already `TIMESTAMP` and `CreadoAt` is a full `DateTime`:
- `< 1 min` → "ahora"
- `< 60 min` → "hace Xm"
- `< 24 h` → "hace Xh"
- `< 7 days` → "hace Xd"
- Otherwise → `dd/MM/yyyy`

### File Locations
- `database/Alter_FechaTimestamp.sql`
- `apps/.../economixws/RestAplication.java`
- `apps/.../economixws/JacksonConfig.java`
- `apps/.../operaciones/daoi/SolicitudGastoDAOImpl.java`
- `apps/.../operaciones/daoi/ComprobantePagoDAOImpl.java`
- `apps/.../operaciones/daoi/RendicionDAOImpl.java`
- `apps/.../operaciones/daoi/CicloCajaChicaDAOImpl.java`
- `apps/.../tesoreria/daoi/TipoCambioDAOImpl.java`
- `web/.../EconomixModel/Converters/UnixDateTimeConverter.cs`
- `web/.../EconomixWA/Components/Pages/MainDashBoard/ActividadDashboardItem.razor`

## Business Rules — SolicitudGasto Business-Hour Validation

### Overview
Employees can only register new `SolicitudGasto` requests during business hours:
- **Days**: Monday to Friday (excludes weekends)
- **Hours**: 8:00 AM to 6:00 PM

### Java Backend — `SolicitudGastoBOImpl.validarHorarioLaboral()`
Called from `validarFecha()` during both insert and update. Validates:
1. Day is not Saturday or Sunday.
2. Current time is between 08:00 and 18:00 (checked via `HOUR_OF_DAY` and `MINUTE`).

Throws `Exception` with descriptive message if validation fails.

### Java Backend — `GET /horario-habilitado` Endpoint
`SolicitudGastoWS.HorarioHabilitado()` returns a JSON response:
```json
{
  "habilitado": true|false,
  "mensaje": "Horario habilitado..." | "Las solicitudes solo pueden registrarse..."
}
```
The .NET frontend calls this endpoint on page load to proactively disable the form.

### .NET Frontend — `MiSolicitudDeGastoDetalle.razor`
- On `OnInitializedAsync()`, calls `SolicitudGastoWS.obtenerHorarioHabilitadoAsync()`.
- `PuedeEditar` now includes `&& HorarioHabilitado` — disables all form fields outside business hours.
- A yellow warning banner shows the reason when the horario is not enabled.

### File Locations
- `apps/.../operaciones/boi/SolicitudGastoBOImpl.java` — `validarHorarioLaboral()`, `validarFecha()`
- `apps/.../economixws/operaciones/ws/SolicitudGastoWS.java` — `HorarioHabilitado()` endpoint
- `web/.../EconomixWS/operaciones/IWS/ISolicitudGastoWS.cs` — `obtenerHorarioHabilitadoAsync()`
- `web/.../EconomixWS/operaciones/WSImpl/SolicitudGastoWSImpl.cs` — implementation
- `web/.../EconomixWA/Components/Pages/SolicitudDeGasto/MiSolicitudesDeGasto/MiSolicitudDeGastoDetalle.razor` — UI

## UI — Real-Time Area & Employee Search Filters

### Overview
All search bars that previously used `<select>` dropdowns for Área filtering now use a **real-time search** component (`AreaSearchFilter.razor`). This provides type-to-search with autocomplete results and selected chips, replacing static dropdowns.

### `AreaSearchFilter` Component
Shared component at `web/.../EconomixWA/Components/Shared/AreaSearchFilter.razor`:
- **Parameters**: `Areas` (full list), `Seleccionadas` (selected list, bindable), `SeleccionadasChanged`, `OnCambio`
- **Behavior**: Type 2+ characters → dropdown shows matching areas (max 10). Click to add → shown as removable badge chip. `LimpiarBusqueda()` clears the text input.
- **Layout**: Does NOT include column wrapper — each search bar wraps it in its own `col-md-*` to preserve layout dimensions.

### Search Bars Updated
| Search Bar | Page | Column Width |
|---|---|---|
| `CuentaBancariaSearchBar` | CuentaBancariaPage | `col-6 col-md-2` |
| `CajaChicaSearchBar` | CajaChicaPage | `col-6 col-md-2` |
| `CicloCajaSearchBar` | CicloCajaPage | `col-12 col-md-2` |
| `UsuarioSearchBar` | UsuariosPage | `col-4 col-md-2` |
| `RolSearchBar` | RolPage | `col-6 col-md-2` |
| `RendicionSearchBar` | RendicionPage | `col-12 col-md-3` (conditional) |

### CuentaBancariaSearchBar — Employee Search
The CuentaBancaria search bar also has a **real-time employee search** (replaced the old "Tipo de Dueño" dropdown):
- **Parameters**: `Empleados` (full list), `EmpleadosSeleccionados` (selected list, bindable), `EmpleadosSeleccionadosChanged`
- **Behavior**: Same as area search — type name → dropdown → click to add → chips with X to remove
- **Filtering logic**: `CuentasFiltradas` filters accounts where `c.EmpleadoAdministrador?.UsuarioID` matches any selected employee

### CuentasBancariasDisponibles — Relational Dropdown Filtering

When a search bar has both an **Área** multi-select filter and a **Cuenta bancaria** dropdown, the CuentaBancaria dropdown should show only accounts belonging to the selected areas. This prevents selecting a bank account from a non-selected area.

**Pattern** (CajaChicaSearchBar / CicloCajaSearchBar):
```csharp
private IEnumerable<CuentaBancaria> CuentasBancariasDisponibles =>
    CuentasBancarias
        .Where(cb => !AreasSeleccionadas.Any()
            || AreasSeleccionadas.Any(a => a.AreaID == cb.AreaAdministradora?.AreaID));
```

Then use `CuentasBancariasDisponibles` in the `<select>`:
```razor
<select class="form-select bg-light rounded-4" @bind="FiltroCuentaBancariaId">
    <option value="0">Todas</option>
    @foreach (var cuenta in CuentasBancariasDisponibles)
    {
        <option value="@cuenta.IdCuenta">@cuenta.NombreBanco - @cuenta.NumeroBancario</option>
    }
</select>
```

Also reset `FiltroCuentaBancariaId = 0` when adding/removing areas (the previously selected account may not belong to the filtered set).

### BuildAreaLookup Timing

When filtering CajaChica or CicloCaja pages by area via `_areaIdByCajaChicaId`, `BuildAreaLookup()` must be called **after** `ListarCajasChicas()` has populated `CajasChicas`. Calling it before will result in an empty dictionary and **no results shown** for area filters.

```csharp
// Correct ordering:
await CargarCatalogos();    // loads Areas, CuentasBancarias
await ListarCajasChicas();  // loads CajasChicas first
BuildAreaLookup();           // now CajasChicas is populated
```

### Badge Color Scheme for Multi-Select Filter Chips

Each filter type in multi-select search bars uses distinct badge colors:

| Filter | Badge Classes |
|--------|---------------|
| Área | `bg-success-subtle text-success-emphasis` (green) |
| Empleado | `bg-primary-subtle text-primary-emphasis` (blue) |
| Moneda | `bg-info-subtle text-info-emphasis` (teal) |
| Rol | `bg-warning-subtle text-warning-emphasis` (amber) |
| Estado | `bg-danger-subtle text-danger-emphasis` (red) |

### Parent Page Pattern
Each parent page follows the same pattern:
1. Replace `FiltroAreaId` (int) with `AreasSeleccionadas` (List\<Area\>)
2. Update search bar binding: `@bind-FiltroAreaId` → `@bind-AreasSeleccionadas`
3. Update `HayFiltrosActivos`: `FiltroAreaId > 0` → `AreasSeleccionadas.Any()`
4. Update filtering: `FiltroAreaId <= 0 || entity.Area?.AreaID == FiltroAreaId` → `!AreasSeleccionadas.Any() || AreasSeleccionadas.Any(a => a.AreaID == entity.Area?.AreaID)`
5. Update `LimpiarFiltros`: `FiltroAreaId = 0` → `AreasSeleccionadas = new()`

### File Locations
- `web/.../EconomixWA/Components/Shared/AreaSearchFilter.razor`
- `web/.../EconomixWA/Components/Pages/CuentaBancaria/CuentaBancariaSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/CuentaBancaria/CuentaBancariaPage.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CajaChicaPage.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CicloCajaSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/CajaChica/CicloCajaPage.razor`
- `web/.../EconomixWA/Components/Pages/Usuario/UsuarioSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/Usuario/UsuariosPage.razor`
- `web/.../EconomixWA/Components/Pages/Rol/RolSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/Rol/RolPage.razor`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionSearchBar.razor`
- `web/.../EconomixWA/Components/Pages/Rendicion/RendicionPage.razor`

## Page-Level Authorization (`[Authorize]` Attributes)

All routable Blazor pages now have `[Authorize]` / `[Authorize(Roles = "...")]` or `[AllowAnonymous]`:
- `@attribute [AllowAnonymous]` — Login, Logout, NotFound, Error
- `@attribute [Authorize]` — Home (any authenticated user)
- `@attribute [Authorize(Roles = "Administrador")]` — Area, AreaDetalle, Rol, Usuarios, UsuarioDetalle, CuentaBancaria, Moneda, MonedaDetalle, Reportes
- `@attribute [Authorize(Roles = "Administrador, Tesoreria")]` — CajaChica, CicloCaja, CicloCajaDetalle
- `@attribute [Authorize(Roles = "Administrador, Jefe, Tesoreria")]` — SolicitudesDeGastoRecibidas, Transacciones, Rendiciones, DashboardTesoreria
- `@attribute [Authorize(Roles = "Administrador, Jefe, Empleado, Tesoreria")]` — ComprobantesDePago, ComprobanteDePagoDetalle
- `@attribute [Authorize(Roles = "Empleado, Jefe")]` — MisSolicitudesDeGasto, MiSolicitudDeGastoDetalle
- `@attribute [Authorize(Roles = "Empleado")]` — MisComprobantesDePago
- `@attribute [Authorize(Roles = "Jefe, Tesoreria")]` — PermisosPendientes

The `using Microsoft.AspNetCore.Authorization` is in `_Imports.razor` (global).
`AuthorizeRouteView` in `Routes.razor` handles unauthorized users: unauthenticated → redirect to login, authenticated but wrong role → "Acceso Denegado" message.

**Important**: The 4 auth policies in `Program.cs` (`IsAdministrador`, `IsJefe`, `IsEmpleado`, `IsTesoreria`) are defined but **never referenced** — role enforcement is done entirely via `[Authorize(Roles = "...")]` attributes on each page.
