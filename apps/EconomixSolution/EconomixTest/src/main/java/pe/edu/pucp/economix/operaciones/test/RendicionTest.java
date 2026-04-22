package pe.edu.pucp.economix.operaciones.test;

import pe.edu.pucp.economix.operaciones.implement.RendicionImplement;
import pe.edu.pucp.economix.operaciones.model.EstadoRendicion;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.util.Date;
import java.util.List;

public class RendicionTest {
    public static List<Rendicion> probarInsercion(){
        RendicionImplement rendicionDAO = new RendicionImplement();

        Rendicion r1 = new Rendicion();
        r1.setFechaPresentacion(new Date());
        r1.setTotalDeclarado(1500.00);
        r1.setEstado(EstadoRendicion.EnEspera);
        r1.setComentario("Rendición de caja chica Finanzas - Semana 1");
        r1.setIdRendicion(rendicionDAO.insertar(r1));

        Rendicion r2 = new Rendicion();
        r2.setFechaPresentacion(new Date());
        r2.setTotalDeclarado(850.00);
        r2.setEstado(EstadoRendicion.EnEspera);
        r2.setComentario("Viáticos viaje comercial Norte");
        r2.setIdRendicion(rendicionDAO.insertar(r2));

        List<Rendicion> rendiciones = rendicionDAO.listarTodas();
        for (Rendicion r : rendiciones) {
            System.out.printf("ID: %d | Estado: %-10s | Monto: %-8s | Comentario: %s%n",
                    r.getIdRendicion(), r.getEstado(), r.getTotalDeclarado(), r.getComentario());
        }
        return rendiciones;
    }
}
