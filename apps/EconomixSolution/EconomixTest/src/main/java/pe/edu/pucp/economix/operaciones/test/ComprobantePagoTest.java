package pe.edu.pucp.economix.operaciones.test;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.economix.operaciones.implement.ComprobantePagoImplement;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;
import pe.edu.pucp.economix.operaciones.model.EstadoComprobante;
import pe.edu.pucp.economix.operaciones.model.SolicitudGasto;
import pe.edu.pucp.economix.operaciones.model.TipoComprobante;
import pe.edu.pucp.economix.tesoreria.model.Moneda;

public class ComprobantePagoTest {
    private static final ComprobantePagoImplement comprobanteDAO = new ComprobantePagoImplement();

    public static List<ComprobantePago> pruebaInsercion(SolicitudGasto solicitudAprobada, Moneda moneda) throws SQLException{

        ComprobantePago factura = new ComprobantePago();

        // Seteamos las relaciones (Claves Foráneas)
        factura.setSolicitud(solicitudAprobada);
        factura.setMoneda(moneda);
        factura.setEstado(EstadoComprobante.PorRevisar);
        factura.setTipoDocumento(TipoComprobante.Factura);
        factura.setIdComprobante(comprobanteDAO.insertar(factura));

        // Listar y verificar
        List<ComprobantePago> comprobantes = comprobanteDAO.listarTodas();
        for (ComprobantePago cp : comprobantes) {
            System.out.println(cp.toString());
        }
        System.out.println();
        return comprobantes;
    }
}
