Especificación de Flujo: Cierre, Apertura y Transacciones Automáticas

Este documento define las reglas de negocio, el comportamiento del sistema y los escenarios de excepción para el proceso de Cierre de Ciclo, la Apertura de Lunes y el Generador Automático de Transacciones en el módulo de Caja Chica.

1. El Flujo de Cierre, Excepciones y Reapertura Flexible

El ciclo operativo no se congela de forma abrupta ni paraliza la empresa. Transiciona de forma controlada durante el fin de semana bajo la supervisión del Jefe de Tesorería, quien posee el control total y discrecional sobre los estados contables.

[Viernes 5:00 PM] ──> [Bloqueo de Nuevos Vales] ──> [Anulación de Pendientes] ──> [Revisión de Tesorería]
                                                                                       │
             ┌─────────────────────────────────────────────────────────────────────────┤
             ▼ (Observado)                                                             ▼ (Aprobado / Pérdida)
   [Ventana de Excepción]                                                      [Cierre Contable / Archivado]
   (Empleado edita comprobantes                                                        │
    y sube vouchers de devolución)                                                     ▼
             │                                                                 [Lunes 8:00 AM: Nuevo Ciclo]
             └─> [Empleado subsana] ──> [Re-evaluación]                        (Coexiste con ciclo anterior)


A. Corte Automático del Fin de Semana (Viernes 5:00 PM)

Bloqueo de Solicitudes: Llegado el viernes a las 5:00 PM, el sistema bloquea automáticamente la creación de nuevas solicitudes de gasto para el ciclo de la semana vigente.

Anulación Automática de Vales Pendientes: Toda solicitud que permanezca en estado PENDIENTE (que no haya sido evaluada ni aprobada por el Jefe de Área) es anulada y eliminada de forma automática por el sistema, liberando inmediatamente el saldo virtual retenido.

Congelamiento de Rendiciones: Las solicitudes que ya fueron PAGADO (dinero entregado) o están en proceso de rendición pasan a un estado de auditoría para la revisión de tesorería.

B. Revisión, Auditoría y Decisiones de Tesorería

El Jefe de Tesorería consolida los datos y puede dictaminar sobre cada caja chica:

Aprobar el Cierre (Con o sin Diferencias): * Si la rendición cuadra al centavo, el ciclo pasa a LIQUIDADO.

Tratamiento de Pérdidas Contables: Si existen diferencias o descuadres que el jefe decide no observar (ej. dinero que el empleado no sustentó ni devolvió), el Jefe de Tesorería puede forzar el cierre. El sistema registrará de forma automática la diferencia como una Pérdida Contable en la contabilidad general de la empresa. Si esto es recurrente, las medidas disciplinarias o denuncias contra el custodio ocurren por fuera del sistema.

Observar y Habilitar Ventana de Excepción:

Si el tesorero detecta inconsistencias, marca el ciclo o solicitudes específicas como OBSERVADO.

Esto habilita una Ventana de Excepción para que el empleado interactúe con el sistema durante el fin de semana.

C. La Ventana de Excepción (Reglas de Oro)

Durante el fin de semana, el empleado puede ingresar para corregir los puntos observados bajo estrictas restricciones:

Modificación Única de Sustentos: El empleado únicamente puede subir, eliminar o editar la grilla de ComprobantePago (vouchers, facturas, boletas) o registrar su transacción de devolución (DEVOLUCION_SOBRANTE).

Inmutabilidad Absoluta del Vale: Las solicitudes de gasto (SolicitudGasto) son inmodificables por diseño. Jamás se edita un monto solicitado, motivo o medio de pago una vez que fue evaluado. Si hubo un error en la solicitud, el flujo exige que se elimine por completo y se cree una nueva (acción solo permitida cuando el ciclo del lunes está abierto).

D. Reapertura de Lunes a las 8:00 AM y Coexistencia de Ciclos

El lunes a las 8:00 AM se inicializa automáticamente un nuevo ciclo semanal para la caja chica, restableciendo su saldo inicial con el montoTecho (fondo máximo).

Coexistencia de Ciclos Activos: Para evitar que la operación de la empresa se detenga por demoras en la auditoría, el sistema permite que coexistan dos ciclos de una misma caja chica al mismo tiempo:

El ciclo de la semana anterior puede seguir en estado de EVALUACION o EN_CORRECCION bajo auditoría del Jefe de Tesorería.

Paralelamente, el ciclo de la semana en curso ya se encuentra ABIERTO y operativo para recibir nuevas solicitudes de dinero de los empleados.

2. Generador Automático de Transacciones

Para garantizar que cada movimiento de dinero esté respaldado y sea auditable, el sistema genera de forma automática Transacciones Pendientes de Ejecución basándose en los hitos del flujo. Toda transacción debe ser liquidada mediante un comprobante/voucher digital o justificada físicamente:

Trigger 1: Aprobación de Solicitud (Egreso de la Empresa)

Disparador: El Jefe de Área o el Tesorero aprueba una solicitud de gasto menor (Estado: APROBADO).

Acción Automática: Dentro de una transacción atómica (DBManager), el sistema:
1. Cambia la solicitud a estado APROBADO y registra al jefe aprobador.
2. Crea una transacción de tipo DESEMBOLSO en estado REGISTRADA desde la cuenta corriente corporativa de la Caja Chica hacia el empleado.
3. Vincula ambas filas mediante FKs: `ope_solicitud_gasto.id_transaccion` ↔ `ope_transaccion.id_solicitud_gasto`.

El tesorero/jefe debe ejecutar el pago real (Yape, Plin o Transferencia), seleccionar/confirmar el medio y registrar el código de operación para pasar la transacción a COMPLETADA y la solicitud a PAGADO.

Trigger 2: Expiración de Tiempo / Cierre de Rendición (Ingreso a la Empresa)

Disparador: El empleado cierra su grilla de comprobantes y la ecuación de cuadratura detecta un sobrante (gastó menos de lo que se le entregó), o transcurre el tiempo límite parametrizado desde el desembolso.

Acción Automática: El sistema genera una transacción de tipo DEVOLUCION_SOBRANTE en estado PENDIENTE del Empleado hacia la Caja Chica del área. El empleado debe depositar físicamente o transferir el sobrante a la cuenta de la empresa y registrar el voucher en el sistema.

Trigger 3: Reposición de Fondo de Caja Chica (Ingreso a la Caja)

Disparador: Se apertura el nuevo ciclo el lunes a las 8:00 AM o se cierra definitivamente un ciclo anterior (LIQUIDADO).

Acción Automática: Para asegurar que la caja chica no se quede sin liquidez real, el sistema genera de forma automática una transacción de tipo REPOSICION_FONDO en estado PENDIENTE dirigida al Jefe de Tesorería.

Ejecución Flexible: El Jefe de Tesorería debe ejecutar esta reposición y registrar su código de operación en el sistema. Puede realizar esta transferencia interbancaria durante el fin de semana pasado o los lunes antes de las 8:00 AM para garantizar que la cuenta tenga dinero real al iniciar la semana.

### Auto-Cálculo de Totales al Cerrar Ciclo (SP Layer)

Cuando el sistema cierra un ciclo de caja chica (estado `CERRADO` o `LIQUIDADO`), el stored procedure `pa_modificar_ciclo_caja` ejecuta automáticamente:

1. **Recalcular `totalGastado`**: Suma todos los `monto_solicitado` de solicitudes en estado `APROBADO` del ciclo.
2. **Crear rendición automática** (si no existe previamente):
   - `totalDeclarado` = suma de `monto_total` de comprobantes no anulados del ciclo.
   - `totalAprobado` = mismo valor del `totalGastado` calculado.
   - `saldoFinal` = `saldoInicial - totalAprobado`.
   - `estado` = `EN_ESPERA`, `fechaPresentacion` = fecha actual.
3. **Vincular FK**: El `id_rendicion` del ciclo se actualiza con la nueva rendición.

Todas las lecturas (`pa_buscar_ciclo_caja_por_id`, listados) devuelven valores calculados en vivo mediante subconsultas, no valores almacenados estáticos. Esto garantiza que la UI siempre muestre los totales correctos independientemente de modificaciones manuales en la BD.

### Evento automático de cierre semanal (viernes 23:00)

El evento `ev_cierre_semanal_caja_chica` ejecuta el cierre de forma desatendida:

1. Selecciona los ciclos `ABIERTO` cuya `fecha_cierre <= CURDATE()`.
2. Para cada uno llama `pa_generar_rendicion_de_ciclo`, que:
   - Calcula `totalDeclarado` (comprobantes no anulados) y `totalAprobado` (solicitudes aprobadas).
   - Inserta una rendición en estado `EN_ESPERA`.
   - Marca el ciclo como `EN_EXCEPCION`.
3. Crea el ciclo de la siguiente semana (lunes–domingo) con `saldoInicial = montoTecho` y `estado = ABIERTO`.

Esto implementa literalmente el paso "[Viernes 5:00 PM]" del flujo: el ciclo vigente pasa a auditoría y el lunes ya existe un nuevo ciclo operativo.

### Ventana de excepción y reenvío

Si tesorería observa la rendición (`OBSERVADO`), el ciclo queda `EN_EXCEPCION` y el empleado puede:
- Editar o eliminar los comprobantes de sus solicitudes (no la solicitud).
- Presionar **Reenviar rendición** para devolver la rendición a `EN_ESPERA` y el ciclo a `CERRADO`, quedando lista para nueva revisión de tesorería.

3. Nuevas Reglas de Negocio Incorporadas

Límite de Solicitud del 40%: Un empleado solo puede solicitar como máximo el $40\%$ del saldo actual disponible de la caja chica en una única solicitud. Esto evita el desabastecimiento inmediato del fondo.

Límite de Sustentación de Comprobantes: Los comprobantes de pago presentados y las Declaraciones Juradas excepcionales acumuladas solo pueden sustentar de forma máxima un porcentaje equivalente al monto total solicitado originalmente, impidiendo sobre-declaraciones de gasto para forzar reembolsos indebidos.

Sustento Obligatorio de Transacciones: Toda transacción (devolución, reembolso o reposición) requiere el registro de un comprobante visual (voucher) o, en su defecto, la confirmación explícita de recepción en efectivo justificada ante tesorería.

4. Unhappy Paths Detallados

UP 1: El "Efecto Embudo" por Transacciones de Devolución Pendientes

Escenario: El sistema genera la transacción de devolución automática para un empleado que gastó S/ 60 de los S/ 100 entregados. El empleado no realiza el depósito del saldo sobrante (S/ 40) a la cuenta de la empresa y se queda con el dinero.

Control del Sistema: El sistema añade al empleado a una lista de deudores de rendición. Mientras tenga una transacción de tipo DEVOLUCION_SOBRANTE en estado PENDIENTE, el sistema le bloquea la creación de cualquier nueva solicitud de gasto en el ciclo del lunes recién abierto, obligándolo a liquidar su deuda antes de volver a operar.

UP 2: Descalce Operativo por Falta de Fondos en Reposición de Caja Chica (Lunes 8:00 AM)

Escenario: Se apertura el ciclo de la semana y se genera la transacción pendiente de REPOSICION_FONDO para que el Jefe de Tesorería transfiera dinero a la caja. No obstante, la cuenta principal de la empresa se queda sin liquidez para transferir.

Control del Sistema: Aunque el sistema muestra virtualmente un saldo inicial equivalente al techo de la caja chica en la interfaz para no detener la operación, las transacciones de desembolso reales de la semana comenzarán a rebotar en el banco. El sistema mostrará una etiqueta visual de advertencia ("Fondo sin Respaldo Líquido") en el panel de control de tesorería hasta que la transacción de reposición pendiente cambie a estado EJECUTADA.

UP 3: Evasión del Límite del 40% mediante Fraccionamiento de Vales ("Pitufeo")

Escenario: Un empleado necesita S/ 400 de una caja chica que tiene S/ 500 de saldo disponible (Supera el límite del $40\% = \text{S/ } 200$). Para evadir la regla, el empleado registra de forma consecutiva tres solicitudes de S/ 130 con minutos de diferencia.

Control del Sistema: El backend de la aplicación debe validar que la suma de solicitudes en estado PENDIENTE o APROBADO de un mismo empleado en un lapso de 24 horas no supere acumuladamente el $40\%$ del saldo disponible de la caja chica al inicio de ese día. Si el acumulado supera el límite, el sistema bloquea el guardado de la nueva solicitud.

UP 4: Intentos de Sobregiro en la Ventana de Excepción

Escenario: Durante el fin de semana, aprovechando la excepción abierta, el empleado intenta cargar comprobantes cuyos montos convertidos acumulados superan con creces el monto original que le fue desembolsado en la solicitud, intentando forzar un reembolso automático de dinero de la empresa.

Control del Sistema: El sistema restringe en caliente que la suma de los comprobantes ingresados durante la ventana de excepción no exceda bajo ninguna circunstancia el monto desembolsado de la solicitud, a menos que el Jefe de Tesorería apruebe de forma explícita una adenda de ampliación de gasto en su panel de control.
