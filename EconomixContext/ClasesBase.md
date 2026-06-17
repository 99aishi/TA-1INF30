Modelo de Clases y Enums: Módulo de Caja Chica

Este documento detalla la estructura y relaciones de las clases de dominio (POJOs) y tipos enumerados (Enums) para el módulo de Caja Chica, alíneandose con las reglas y flujos del negocio financiero de la empresa.

1. Diagrama de Relaciones de Dominio (Conceptual)

Para garantizar un tipado fuerte y evitar el uso de identificadores numéricos sueltos (IDs), las entidades se relacionan directamente mediante referencias de objetos y tipos enumerados en lugar de cadenas de texto crudas:

[Empleado] ──(1) pertenece a ──> [Area] ──(1..*) posee ──> [CuentaBancaria]
   │                                                         │
   │ (0..*) posee                                            │ (1) financia
   ▼                                                         ▼
[CuentaBancaria]                                        [CajaChica] (Asociada a divisa de Cuenta)
   │ (Asociada a un Empleado titular)                        │
   │                                                         ▼ (1..*)
   │                                                    [CicloCajaChica] (Saldo inicial = Techo de Caja)
   │                                                         │
   │                                                         ▼ (1..*)
   └─────────────────────────────────────────┐          [SolicitudGasto] <──(1..*) [ComprobantePago]
                                             │               │ (Usa Enums para Estados y Medios)
                                             ▼               ▼
                                           [Transaccion] ◄───┘ (Desembolso/Devolución/Reembolso)



2. Tipos Enumerados (Enums del Sistema)

Para evitar la repetición de cadenas propensas a errores de digitación y estandarizar los flujos lógicos, utilizaremos los siguientes Enums en las clases del modelo:

Enum RolFlujo

Define la jerarquía funcional y de aprobación del empleado dentro del flujo financiero de su área.

public enum RolFlujo {
    EMPLEADO,       // Registra solicitudes y comprobantes
    JEFE_AREA       // Aprueba las solicitudes de su personal subordinado
}



Enum MedioPago

Representa las pasarelas o canales transaccionales para transferir o rendir fondos, incluyendo las billeteras digitales habilitadas.

public enum MedioPago {
    YAPE,
    PLIN,
    TRANSFERENCIA,
    EFECTIVO
}



Enum EstadoSolicitud

Representa los estados oficiales por los que transita una solicitud de gasto menor dentro del ciclo de vida del dinero.

public enum EstadoSolicitud {
    PENDIENTE,      // Creado por el empleado
    APROBADO,       // Evaluado y validado por la jefatura inmediata
    PAGADO,         // Tesorería realizó el desembolso real
    RENDIDO,        // Comprobantes cargados, validados y cuadrícula cerrada
    RECHAZADO       // Denegado por el jefe o tesorero
}



Enum TipoDocumento

Clasificación de los comprobantes para sustento tributario o declaraciones de excepción.

public enum TipoDocumento {
    FACTURA,
    BOLETA,
    DJ_EXCEPCIONAL   // Declaración jurada para gastos excepcionales sin comprobante
}



Enum EstadoCiclo

Maneja el estado contable del corte temporal semanal de caja chica.

public enum EstadoCiclo {
    ABIERTO,        // Permite registrar solicitudes del ciclo vigente
    CERRADO,        // Ciclo cerrado para el fin de semana. No acepta solicitudes nuevas
    LIQUIDADO       // Cuentas cuadradas y listas para el reporte analítico consolidado
}



Enum TipoTransaccion

Clasifica las operaciones físicas de entrada y salida de fondos bancarios del área.

public enum TipoTransaccion {
    DESEMBOLSO,           // Salida de dinero para pagar un vale aprobado
    DEVOLUCION_SOBRANTE,  // Ingreso de dinero del empleado a la empresa por saldos no utilizados
    REEMBOLSO_DEFICIT,    // Salida de dinero de la empresa al empleado por gastos mayores aprobados
    REPOSICION_FONDO      // Ingreso de fondos de tesorería para restablecer el techo de la caja chica
}



3. Definición de Clases de Dominio (Modelos POJO)

3.1. Catálogos Base y Seguridad

Clase Moneda

public class Moneda {
    private int idMoneda;
    private String nombre;        // Ej. "Soles", "Dólares"
    private String simbolo;       // Ej. "S/", "$"
    private String codigoISO;     // Ej. "PEN", "USD"
    private boolean estaActivo;
    
    // Getters, Setters y Constructores
}



Clase Rol

Define el nivel de perfil administrativo en el sistema corporativo (RBAC).

public class Rol {
    private int rolID;
    private String titulo;        // Ej. "Jefe de Área", "Empleado", "Tesorero"
    private String descripcion;
    private boolean estaActivo;
    
    // Getters, Setters y Constructores
}



3.2. Estructura Organizacional

Clase Area

public class Area {
    private int idArea;
    private String nombre;                  // Ej. "Tesorería y Finanzas", "Logística"
    private String descripcion;
    private boolean estaActivo;
    private Empleado jefe;                  // Jefe directo de este departamento (Jefe de Área)
    private List<CuentaBancaria> cuentasBancarias; // Cuentas de la empresa asignadas a esta área
    private List<CajaChica> cajasChicas;    // Cajas chicas configuradas para esta área
    
    // Getters, Setters y Constructores
}



Clase Usuario (Abstracta)

public abstract class Usuario {
    protected int usuarioID;
    protected String nombres;
    protected String apellidoPaterno;
    protected String apellidoMaterno;
    protected String password;              // Hash Argon2id
    protected String correo;
    protected boolean estaActivo;
    
    // Getters, Setters y Constructores
}



Clase Empleado (Hereda de Usuario)

public class Empleado extends Usuario {
    private String numeroCelular;            // Número telefónico para billeteras digitales (Yape / Plin)
    private Rol rol;                         // Rol asignado para el control de accesos (RBAC)
    private RolFlujo rolFlujo;               // Enum: Rol funcional de aprobación en Caja Chica (JEFE_AREA / EMPLEADO)
    private Area area;                       // Área a la que pertenece
    private Empleado jefeDirecto;            // Supervisor inmediato (Para árbol recursivo de aprobaciones)
    private List<CuentaBancaria> cuentasPersonales; // Cuentas bancarias personales del empleado
    private List<SolicitudGasto> solicitudesEnviadas;
    
    // Getters, Setters y Constructores
}



Clase Administrador (Hereda de Usuario)

Clase concreta para los usuarios administradores del sistema, sin relación directa con el flujo operativo diario de aprobaciones de dinero de caja chica.

public class Administrador extends Usuario {   

}



3.3. Gestión de Cuentas y Cajas Chicas

Clase CuentaBancaria

Representa exclusivamente los datos de identificación de una cuenta de banco. No posee saldo disponible computable, ya que actúa como catálogo informativo y de conciliación para transferencias o billeteras digitales.

public class CuentaBancaria {
    private int idCuentaBancaria;
    private String numeroCuenta;
    private String cci;
    private String banco;
    private Moneda moneda;                  // Divisa de la cuenta (PEN, USD, etc.)
    private Area areaAsignada;              // Nulo si la cuenta pertenece a un empleado
    private Empleado titular;               // Empleado dueño de la cuenta (Nulo si es cuenta corriente corporativa de un Área)
    private boolean estaActivo;
    
    // Getters, Setters y Constructores
}



Clase CajaChica

public class CajaChica {
    private int idCajaChica;
    private String nombre;                  // Ej. "Caja Chica Soles - Logística"
    private double montoTecho;              // Monto límite/máximo establecido para el fondo fijo
    private Moneda moneda;                  // Moneda base fija (heredada de la cuenta origen)
    private CuentaBancaria cuentaOrigen;    // Cuenta corporativa del área que financia la caja
    private Area area;                      // Área propietaria de la caja
    private boolean estaActivo;
    private List<CicloCajaChica> ciclos;    // Cortes semanales históricos
    
    // Getters, Setters y Constructores
}



3.4. Ciclo Operativo, Vales y Comprobantes

Clase CicloCajaChica

Corte semanal temporal. Al aperturarse, su saldo inicial se restablece de forma inalterable con el monto máximo (el montoTecho de la CajaChica asociada).

public class CicloCajaChica {
    private int idCicloCaja;
    private String nombreSemana;            // Ej. "Semana 24-2026"
    private Date fechaApertura;
    private Date fechaCierre;               // Nulo si se encuentra en estado ABIERTO
    private double montoSaldoInicial;       // Se inicializa siempre con el montoTecho (fondo máximo) de la CajaChica
    private double montoSaldoActual;        // Saldo neto restante para gastar en este ciclo
    private double montoTotalGastado;       // Sumatoria total de los comprobantes validados en el ciclo
    private EstadoCiclo estadoCiclo;        // Enum: "ABIERTO", "CERRADO", "LIQUIDADO"
    private CajaChica cajaChica;            // Caja chica asignada a este corte
    private List<SolicitudGasto> solicitudes; // Solicitudes adscritas a este ciclo semanal
    
    // Getters, Setters y Constructores
}



Clase SolicitudGasto

public class SolicitudGasto {
    private int idSolicitudGasto;
    private Date fechaSolicitud;
    private double montoOriginal;           // Monto digitado originalmente por el empleado
    private Moneda monedaOriginal;          // Divisa seleccionada del gasto original solicitado
    private double tipoCambio;              // Tipo de cambio (1.0 si coincide con la moneda base de la Caja Chica)
    private double montoConvertido;         // Descuento computable recalculado en la moneda base de la Caja Chica
    private String motivoSolicitud;
    private MedioPago medioDesembolso;      // Enum: Medio deseado por el trabajador (YAPE, PLIN, etc.)
    private EstadoSolicitud estadoSolicitud;// Enum: Estado del vale (PENDIENTE, APROBADO, PAGADO, etc.)
    private Empleado solicitante;           // Trabajador que pide el dinero
    private Empleado jefeAprobador;         // Jefe inmediato que pre-aprueba el vale
    private Empleado tesoreroAprobador;     // Jefe de Tesorería (visto bueno final y cierre)
    private CicloCajaChica cicloCaja;       // Ciclo semanal vigente de Caja Chica al que afecta
    private List<ComprobantePago> comprobantes; // Comprobantes que sustentan el gasto real
    private Transaccion transaccionDesembolso;  // Registro de la operación de pago
    
    // Getters, Setters y Constructores
}



Clase ComprobantePago

public class ComprobantePago {
    private int idComprobante;
    private TipoDocumento tipoDocumento;    // Enum: "FACTURA", "BOLETA", "DJ_EXCEPCIONAL"
    private String rucProveedor;            // Nulo si es DJ_EXCEPCIONAL
    private String razonSocial;             // Nulo si es DJ_EXCEPCIONAL
    private String numeroSerie;             // Nulo si es DJ_EXCEPCIONAL
    private Date fechaEmision;
    private Moneda monedaComprobante;       // Moneda en la que se emitió físicamente el comprobante
    private double montoOriginal;           // Importe bruto expresado en el papel
    private double tipoCambio;              // Tipo de cambio aplicado en la fecha del comprobante
    private double montoConvertido;         // Equivalente calculado para descontar en la moneda de la Caja Chica
    private double subtotal;                // Base imponible
    private double igv;                     // Impuestos aplicados
    private double montoTotal;              // subtotal + igv en moneda original
    private String nombreArchivoComprobante;// Nombre del archivo digitalizado (imagen o PDF)
    private SolicitudGasto solicitud;       // Solicitud de gasto asociada
    
    // Getters, Setters y Constructores
}



Clase Transaccion

public class Transaccion {
    private int idTransaccion;
    private TipoTransaccion tipoTransaccion;// Enum: "DESEMBOLSO", "DEVOLUCION_SOBRANTE", etc.
    private double monto;                   // Monto neto de la operación
    private Moneda moneda;                  // Moneda de la transacción
    private Date fecha;
    private MedioPago medioPago;            // Enum: "YAPE", "PLIN", "TRANSFERENCIA", "EFECTIVO"
    private String codigoOperacion;         // Alfanumérico obligatorio si se usó billetera digital o transferencia
    private CuentaBancaria cuentaOrigen;    // Cuenta emisora (nulo si es efectivo físico)
    private CuentaBancaria cuentaDestino;   // Cuenta receptora (nulo si es retiro en efectivo)
    private Empleado beneficiario;          // Colaborador que entrega o recibe el dinero
    
    // Getters, Setters y Constructores
}


