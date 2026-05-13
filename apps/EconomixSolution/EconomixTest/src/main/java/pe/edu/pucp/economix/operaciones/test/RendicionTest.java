package pe.edu.pucp.economix.operaciones.test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pe.edu.pucp.economix.operaciones.bo.RendicionBOImpl;
import pe.edu.pucp.economix.operaciones.boi.IRendicionBO;
import pe.edu.pucp.economix.operaciones.implement.RendicionImplement;
import pe.edu.pucp.economix.operaciones.model.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

public class RendicionTest {
    private static final RendicionImplement rendicionDAO = new RendicionImplement();
    private static final IRendicionBO rendicionBO = new RendicionBOImpl();
    public static List<Rendicion> probarInsercion() throws Exception {
        Rendicion r1 = new Rendicion();
        r1.setFechaPresentacion(new Date());
        r1.setTotalDeclarado(1500.00);
        r1.setEstado(EstadoRendicion.EnEspera);
        r1.setComentario("Rendición de caja chica Finanzas - Semana 1");
        r1.setIdRendicion(rendicionBO.insertar(r1));

        Rendicion r2 = new Rendicion();
        r2.setFechaPresentacion(new Date());
        r2.setTotalDeclarado(850.00);
        r2.setEstado(EstadoRendicion.EnEspera);
        r2.setComentario("Viáticos viaje comercial Norte");
        r2.setIdRendicion(rendicionBO.insertar(r2));

        List<Rendicion> rendiciones = rendicionBO.listarTodas();
        for (Rendicion r : rendiciones) {
            System.out.println(r);
        }
        System.out.println();
        return rendiciones;
    }
}
