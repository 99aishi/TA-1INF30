# Economix — Class Map

> Canonical reference of every class, interface, and enum across both codebases.

---

## 1. Java Backend (`apps/EconomixSolution/`)

Base package: `pe.edu.pucp.economix`

### 1.1 Domain Model (`EconomixModel`)

#### Inheritance hierarchy

```
Usuario (abstract)
  int usuarioID, String nombres, String apellidoPaterno, String apellidoMaterno
  String password, String correo, EstadoUsuario estado
  setPassword(String) — Argon2-hashes via spring-security-crypto
  boolean validarPassword(String)

  ├── Administrador (concrete, no extra fields)
  │
  └── Empleado (concrete)
        String numeroCelular
        Rol rol, Area area, Empleado jefeDirecto, RolFlujo rolFlujo
        List<SolicitudGasto> solicitudesRecibidas, solicitudesEnviadas
        List<CuentaBancaria> cuentas
        List<BilleteraDigital> billeteras

Fondo (concrete)
  int idFondo, String nombre, EstadoFondo estado

  └── CajaChica (concrete)
        double montoTecho, Area areaAsignada
        List<CicloCajaChica> ciclos

Area (standalone)
  int idArea, String nombre, String descripcion, boolean estaActivo
  Empleado jefe, CajaChica cajaChica, List<CuentaBancaria> cuentasBancarias

Rol (standalone)
  int rolID, String titulo, String descripcion, boolean estaActivo

Moneda (standalone)
  int idMoneda, String codigoISO, String simbolo, String nombre, String descripcion, boolean activa

CuentaBancaria (standalone)
  int idCuenta, String nombreBanco, String numeroBancario, String cci
  Moneda moneda, Empleado empleadoAdministrador, Area areaAdministradora

BilleteraDigital (standalone)
  int idBilletera, String numeroCelular, Empleado titular, boolean activa

SolicitudGasto (standalone)
  int idSolicitudGasto, Date fechaSolicitud, double montoSolicitado
  String motivoSolicitud, EstadoSolicitudGasto estado, String comentarioDecision
  Empleado solicitante, Empleado destinatario, CicloCajaChica ciclo, MedioPago medioDesembolso
  List<ComprobantePago> comprobantes

CicloCajaChica (standalone)
  int idCicloCaja, int numeroSemana, Date fechaApertura, Date fechaCierre
  double saldoInicial, double totalGastado, EstadoCicloCaja estado
  CajaChica cajaChica, Rendicion rendicion
  List<SolicitudGasto> solicitudesDeGasto

Rendicion (standalone)
  int idRendicion, Date fechaPresentacion, Date fechaAprobacion
  double totalDeclarado, double totalAprobado, double saldoFinal
  EstadoRendicion estado, String comentario, CicloCajaChica cicloCajaChica

ComprobantePago (standalone)
  int idComprobante, TipoComprobante tipoDocumento
  String RUCProveedor, String razonSocial, String numeroSerial, Date fechaEmision
  double montoTotal, double subtotal, double igv, double total
  EstadoComprobante estado, SolicitudGasto solicitud, Moneda moneda

Transaccion (standalone)
  int idTransaccion, TipoTransaccion tipoTransaccion, Date fecha, double monto
  String numeroOperacionBancaria, MedioPago medioPago, double tipoCambio, EstadoTransaccion estadoTransaccion
  CuentaBancaria cuentaOrigen, cuentaDestino, Moneda moneda, Empleado beneficiario

AuditoriaLogDto (standalone)
  int idLog, String nombreTabla, String accion, int idRegistroAfectado, String valorAnterior, String valorNuevo, int idUsuarioAccion, Date momentoCambio
```

#### Enums (`pe.edu.pucp.economix.model.enums` / `rrhh.model`)

| Enum | Values |
|------|--------|
| `EstadoUsuario` | `ACTIVO, INACTIVO` |
| `RolFlujo` | `EMPLEADO, JEFE_AREA` |
| `EstadoFondo` | `ACTIVO, INACTIVO` |
| `EstadoSolicitudGasto` | `PENDIENTE, APROBADO, PAGADO, RENDIDO, RECHAZADO, ANULADO` |
| `EstadoCicloCaja` | `ABIERTO, CERRADO, LIQUIDADO` |
| `EstadoComprobante` | `POR_REVISAR, ANULADO, APROBADO, OBSERVADO` |
| `EstadoRendicion` | `ACEPTADO, EN_ESPERA, DENEGADO, ANULADO` |
| `EstadoTransaccion` | `REGISTRADA, COMPLETADA, ANULADA` |
| `TipoComprobante` | `FACTURA, BOLETA, DJ_EXCEPCIONAL` |
| `TipoTransaccion` | `DESEMBOLSO, DEVOLUCION_SOBRANTE, REEMBOLSO_DEFICIT, REPOSICION_FONDO` |
| `MedioPago` | `YAPE, PLIN, TRANSFERENCIA, EFECTIVO` |

---

### 1.2 Business Layer (`EconomixNegocio`)

#### Generic base

```
IBaseBO<T>                          (interface)
  + int insertar(T)
  + int modificar(T)
  + int eliminar(int)
  + T buscarPorId(int)
  + List<T> listarTodas()
```

#### BO interfaces (`.../ibo/`)

| Interface | Module | Extends | Own methods |
|-----------|--------|---------|-------------|
| `IAreaBO` | `rrhh.ibo` | `IBaseBO<Area>` | `listarActivas()`, `recuperar(int)` |
| `IRolBO` | `rrhh.ibo` | `IBaseBO<Rol>` | — |
| `IUsuarioBO` | `rrhh.ibo` | **(none)** | `validarUsuario(String,String)` |
| `IAdministradorBO` | `rrhh.ibo` | `IBaseBO<Administrador>` | — |
| `IEmpleadoBO` | `rrhh.ibo` | `IBaseBO<Empleado>` | `listarPorNombreApellido(String)` |
| `IMonedaBO` | `tesoreria.ibo` | `IBaseBO<Moneda>` | `listarMonedas_X_codigoISO_nombre_simbolo(String)`, `listarMonedas_X_estado(boolean)`, `listarActivas()`, `recuperar(int)` |
| `ICajaChicaBO` | `tesoreria.ibo` | `IBaseBO<CajaChica>` | — |
| `ICuentaBancariaBO` | `tesoreria.ibo` | `IBaseBO<CuentaBancaria>` | — |
| `ICicloCajaBO` | `operaciones.ibo` | `IBaseBO<CicloCajaChica>` | `calcularTotalGastado(CicloCajaChica)` |
| `ISolicitudGastoBO` | `operaciones.ibo` | `IBaseBO<SolicitudGasto>` | `listarPorSolicitante(int)`, `listarPendientesJefe(int)`, `listarPorCiclo(int)` |
| `IComprobantePagoBO` | `operaciones.ibo` | `IBaseBO<ComprobantePago>` | `listarPorSolicitud(int)` |
| `IRendicionBO` | `operaciones.ibo` | `IBaseBO<Rendicion>` | `calcularTotales(Rendicion)`, `generarRendicionDeCiclo(int)` |
| `ITransaccionBO` | `operaciones.ibo` | `IBaseBO<Transaccion>` | — |
| `IAuditoriaBO` | `auditoria.ibo` | **(none)** | `listarLog()`, `insertarLog(AuditoriaLogDto)` |

---

### 1.3 Persistence Layer (`EconomixPersistencia`)

#### Generic base

```
IDAO<T>                             (interface)
  + int insertar(T)
  + int modificar(T)
  + int eliminar(int)
  + T buscarPorId(int)
  + List<T> listarTodas()
```

#### DAO interfaces (`.../idao/`)

| Interface | Module | Extends | Own methods |
|-----------|--------|---------|-------------|
| `IAreaDAO` | `rrhh.idao` | `IDAO<Area>` | `asignarJefe(Area,Empleado)`, `listarActivas()`, `recuperar(int)` |
| `IRolDAO` | `rrhh.idao` | `IDAO<Rol>` | — |
| `IUsuarioDAO` | `rrhh.idao` | **(none)** | `buscarPorCorreo(String)` |
| `IAdministradorDAO` | `rrhh.idao` | `IDAO<Administrador>` | — |
| `IEmpleadoDAO` | `rrhh.idao` | `IDAO<Empleado>` | `listarPorNombreApellido(String)` |
| `IMonedaDAO` | `tesoreria.idao` | `IDAO<Moneda>` | `listarMonedas_X_codigoISO_nombre_simbolo(String)`, `listarMonedas_X_estado(boolean)`, `listarActivas()`, `recuperar(int)` |
| `ICajaChicaDAO` | `tesoreria.idao` | `IDAO<CajaChica>` | — |
| `ICuentaBancariaDAO` | `tesoreria.idao` | `IDAO<CuentaBancaria>` | — |
| `ICicloCajaChicaDAO` | `operaciones.idao` | `IDAO<CicloCajaChica>` | — |
| `ISolicitudGastoDAO` | `operaciones.idao` | `IDAO<SolicitudGasto>` | `listarPorSolicitante(int)`, `listarPendientesJefe(int)`, `listarPorCiclo(int)` |
| `IComprobantePagoDAO` | `operaciones.idao` | `IDAO<ComprobantePago>` | `listarPorSolicitud(int)` |
| `IRendicionDAO` | `operaciones.idao` | `IDAO<Rendicion>` | — |
| `ITransaccionDAO` | `operaciones.idao` | `IDAO<Transaccion>` | — |
| `IAuditoriaDAO` | `auditoria.idao` | **(none)** | `listarLog()`, `insertarLog(AuditoriaLogDto)` |

---

### 1.4 REST Layer (`EconomixWS`)

All follow `@Path("XWS")` on base package `pe.edu.pucp.economix.economixws.{domain}.ws/`.

| WS class | Path | Endpoints |
|----------|------|-----------|
| `UsuarioWS` | `UsuarioWS` | `POST /login` |
| `AreaWS` | `AreaWS` | `listarAreas`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `RolWS` | `RolWS` | `listarRoles`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `EmpleadoWS` | `EmpleadoWS` | `listarEmpleados`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `CajaChicaWS` | `CajaChicaWS` | `listarCajasChicas`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `CuentaBancariaWS` | `CuentaBancariaWS` | `listarCuentas`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `MonedaWS` | `MonedaWS` | `listarMonedas`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `CicloCajaWS` | `CicloCajaWS` | `listarCiclos`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `SolicitudGastoWS` | `SolicitudGastoWS` | `listarSolicitudes`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `ComprobantePagoWS` | `ComprobantePagoWS` | `listarComprobantes`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `RendicionWS` | `RendicionWS` | `listarRendiciones`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `TransaccionWS` | `TransaccionWS` | `listarTransacciones`, `buscarPorId`, `insertar`, `actualizar`, `eliminar` |
| `AuditoriaWS` | `AuditoriaWS` | `listarLog`, `insertarLog` |

---

## 2. .NET Frontend (`web/EconomixSolution/`)

### 2.1 Domain Model (`EconomixModel`, namespace `EconomixModel.Model`)

Mirror properties with `[JsonPropertyName("snake_case")]` decorators.
Classes structured with standard C# PascalCase properties matching Java models:
- `Usuario` (abstract)
- `Empleado` : `Usuario`
- `Administrador` : `Usuario`
- `Area`
- `Rol`
- `Moneda`
- `CajaChica`
- `CuentaBancaria`
- `SolicitudGasto`
- `CicloCajaChica`
- `ComprobantePago`
- `Transaccion`
- `AuditLogEntry`

### 2.2 WS Proxy Layer (`EconomixWS`)

Proxies extend `IWS<T>` providing sync-over-async implementations calling JAX-RS endpoints:
- `IUsuarioWS` / `UsuarioWS`
- `IAreaWS` / `AreaWSImpl`
- `IRolWS` / `RolWSImpl`
- `IEmpleadoWS` / `EmpleadoWSImpl`
- `IMonedaWS` / `MonedaWSImpl`
- `ICajaChicaWS` / `CajaChicaWSImpl`
- `ICuentaBancariaWS` / `CuentaBancariaWSImpl`
- `ISolicitudGastoWS` / `SolicitudGastoWSImpl`
- `IComprobantePagoWS` / `ComprobantePagoWSImpl`
- `ICicloCajaWS` / `CicloCajaWSImpl`
- `ITransaccionWS` / `TransaccionWSImpl`
- `IAuditoriaWS` / `AuditoriaWSImpl`
