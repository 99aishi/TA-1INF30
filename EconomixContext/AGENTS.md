# Economix — AGENTS.md

## Project overview

Two-codebase architecture under `TA-1INF30/`:
- **Java (`apps/EconomixSolution`)**: Jakarta REST (JAX-RS) backend → MySQL. Maven multi-module (7 modules), Java 25. WAR deployed to GlassFish/Payara.
- **.NET (`web/EconomixSolution`)**: Blazor Interactive Server frontend + WS proxy layer. Cookie auth with Claims, no JWT.

Java WS (port 8080) → .NET WS proxy → Blazor UI.

## Architecture layers (Java)

EconomixWS → EconomixNegocio (BO/BOI) → EconomixPersistencia (DAO/DAOI) → EconomixDBManager → MySQL.
- `EconomixModel` — domain POJOs (`rrhh`, `tesoreria`, `operaciones`, `auditoria`)
- `EconomixNegocio` — `ibo/` (interfaces), `boi/` (implementations)
- `EconomixPersistencia` — `idao/` (interfaces), `daoi/` (impls), stored procedure calls via `DBManager`
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
- `CicloCajaWS` — `ListarCiclos`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `SolicitudGastoWS` — `ListarSolicitudes`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `ComprobantePagoWS` — `ListarComprobantes`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
- `RendicionWS` — `ListarRendiciones`, `BuscarPorId`, `Insertar`, `Actualizar`, `Eliminar`
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

## TO-DO / Pending Tasks

- [ ] **Login Auditing**: When a login is attempted (either successful or failed), the attempt should be logged to the `log_auditoria` database table via `AuditoriaWS`.
