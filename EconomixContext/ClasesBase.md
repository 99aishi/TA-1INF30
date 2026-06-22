Modelo de Clases y Enums: Módulo de Caja Chica

Este documento detalla la estructura y relaciones de las clases de dominio (POJOs) y tipos enumerados (Enums) para el módulo de Caja Chica, alíneandose con las reglas y flujos del negocio financiero de la empresa.

1. Diagrama de Relaciones de Dominio (Conceptual)

Para garantizar un tipado fuerte y evitar el uso de identificadores numéricos sueltos (IDs), las entidades se relacionan directamente mediante referencias de objetos y tipos enumerados en lugar de cadenas de texto crudas:

```
[Empleado] ──(1) pertenece a ──> [Area] ──(1..*) posee ──> [CuentaBancaria]
   │                                                         │
   │ (0..*) posee                                            │ (1..*) financia
   ▼                                                         ▼
[CuentaBancaria]                                        [CajaChica]
   │ (Asociada a un Empleado titular)                        │
   │                                                         ▼ (1..*)
   │                                                    [CicloCajaChica]
   │                                                         │
   │                                                         ▼ (1..*)
   └─────────────────────────────────────────┐          [SolicitudGasto] <──(1..*) [ComprobantePago]
                                             │               │
                                             ▼               ▼
                                           [Transaccion] ◄───┘
```

**Flujo de Relaciones de Fondos**:
- Cada **Área** puede tener cero o múltiples cuentas bancarias corporativas (`CuentaBancaria`). No es obligatorio que un área tenga una cuenta desde su creación.
- Cada **Cuenta Bancaria** corporativa de la empresa asociada al área puede financiar múltiples fondos fijos de **Caja Chica**.

---

2. Tipos Enumerados (Enums del Sistema)

Para evitar la repetición de cadenas propensas a errores de digitación y estandarizar los flujos lógicos, se definen los siguientes Enums en las clases del modelo:

### Enum EstadoUsuario
```java
public enum EstadoUsuario {
    ACTIVO,
    INACTIVO
}
```

### Enum RolFlujo
Define la jerarquía funcional y de aprobación del empleado dentro del flujo financiero de su área.
```java
public enum RolFlujo {
    EMPLEADO,       // Registra solicitudes y comprobantes
    JEFE_AREA       // Aprueba las solicitudes de su personal subordinado
}
```

### Enum MedioPago
Representa las pasarelas o canales transaccionales para transferir o rendir fondos.
```java
public enum MedioPago {
    YAPE,
    PLIN,
    TRANSFERENCIA,
    EFECTIVO
}
```

### Enum EstadoSolicitudGasto
Representa los estados oficiales por los que transita una solicitud de gasto menor dentro del ciclo de vida del dinero.
```java
public enum EstadoSolicitudGasto {
    PENDIENTE,      // Creado por el empleado
    APROBADO,       // Evaluado y validado por la jefatura inmediata
    PAGADO,         // Tesorería realizó el desembolso real
    RENDIDO,        // Comprobantes cargados, validados y cuadrícula cerrada
    RECHAZADO,      // Denegado por el jefe o tesorero
    ANULADO         // Cancelado o retirado
}
```

### Enum TipoComprobante
Clasificación de los comprobantes para sustento tributario o declaraciones de excepción.
```java
public enum TipoComprobante {
    FACTURA,
    BOLETA,
    DJ_EXCEPCIONAL   // Declaración jurada para gastos excepcionales sin comprobante
}
```

### Enum EstadoCicloCaja
Maneja el estado contable del corte temporal semanal de caja chica.
```java
public enum EstadoCicloCaja {
    ABIERTO,        // Permite registrar solicitudes del ciclo vigente
    CERRADO,        // Ciclo cerrado para el fin de semana. No acepta solicitudes nuevas
    LIQUIDADO       // Cuentas cuadradas y listas para el reporte analítico consolidado
}
```

### Enum TipoTransaccion
Clasifica las operaciones físicas de entrada y salida de fondos bancarios del área.
```java
public enum TipoTransaccion {
    DESEMBOLSO,           // Salida de dinero para pagar un vale aprobado
    DEVOLUCION_SOBRANTE,  // Ingreso de dinero del empleado a la empresa por saldos no utilizados
    REEMBOLSO_DEFICIT,    // Salida de dinero de la empresa al empleado por gastos mayores aprobados
    REPOSICION_FONDO      // Ingreso de fondos de tesorería para restablecer el techo de la caja chica
}
```

### Enum EstadoTransaccion
```java
public enum EstadoTransaccion {
    REGISTRADA,
    COMPLETADA,
    ANULADA
}
```

3. Definición de Clases de Dominio (Modelos POJO)

Todas las clases del modelo Java se implementan bajo el paquete base `pe.edu.pucp.economix.{domain}.model`, mientras que las de .NET se implementan con propiedades PascalCase decoradas con `[JsonPropertyName]` para el mapeo JSON síncrono.
- `Usuario`
- `Empleado`
- `Administrador`
- `Area`
- `Rol`
- `Moneda`
- `CuentaBancaria`
- `CajaChica`
- `CicloCajaChica`
- `SolicitudGasto`
- `ComprobantePago`
- `Transaccion`
