package pe.edu.pucp.economix.operaciones.test;

import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.boi.ComprobantePagoBOImpl;
import pe.edu.pucp.economix.operaciones.ibo.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.daoi.ComprobantePagoDAOImpl;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.enums.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.enums.TipoComprobante;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class ComprobantePagoTest {
    private static final ComprobantePagoDAOImpl comprobanteDAO = new ComprobantePagoDAOImpl();

    private static final IComprobantePagoBO comprobantePagoBO = new ComprobantePagoBOImpl();
    public static List<ComprobantePago> pruebaInsercion(SolicitudGasto solicitudAprobada, Moneda moneda) throws Exception {

        ComprobantePago factura = new ComprobantePago();

        // Seteamos las relaciones (Claves Foráneas)
        factura.setSolicitud(solicitudAprobada);
        factura.setMoneda(moneda);

        factura.setFechaEmision(new Date());
        factura.setEstado(EstadoComprobante.PorRevisar);
        factura.setTipoDocumento(TipoComprobante.Factura);
        factura.setNumeroSerial("921236767");
        factura.setRUCProveedor("11119990001");
        factura.setRazonSocial("Pepitos SAC");
        factura.setSubtotal(100);
        factura.setIgv(18);
        factura.setTotal(118);
        factura.setIdComprobante(comprobantePagoBO.insertar(factura));

        // Listar y verificar
        List<ComprobantePago> comprobantes = comprobantePagoBO.listarTodas();
        for (ComprobantePago cp : comprobantes) {
            System.out.println(cp.toString());
        }
        System.out.println();
        return comprobantes;
    }
}
