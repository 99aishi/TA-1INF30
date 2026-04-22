package pe.edu.pucp.economix.tesoreria.test;

import pe.edu.pucp.economix.rrhh.model.Empleado;
import pe.edu.pucp.economix.tesoreria.implement.EntregaARendirImplement;
import pe.edu.pucp.economix.tesoreria.model.EntregaARendir;
import pe.edu.pucp.economix.tesoreria.model.EstadoEntregaARendir;
import pe.edu.pucp.economix.tesoreria.model.EstadoFondo;

import java.util.List;

public class EntregaARendirTest {
    private static EntregaARendirImplement entregaARendirDAO = new EntregaARendirImplement();

    public static List<EntregaARendir> pruebaInsercion(Empleado jefe, Empleado emp1, Empleado emp2){

        // --- ENTREGA 1: Solicitada por emp1 al empJefe ---
        EntregaARendir entrega1 = new EntregaARendir();
        entrega1.setNombre("Viaje Buenos Aires");
        entrega1.setMotivo("Compra de suministros de oficina urgentes");
        entrega1.setMontoSolicitado(250.50);
        entrega1.setEstado(EstadoFondo.Activo);
        entrega1.setSolicitante(emp1);
        entrega1.setAprobador(jefe);
        entrega1.setEstado(EstadoEntregaARendir.Pendiente);
        entrega1.setIdFondo(entregaARendirDAO.insertar(entrega1));



        EntregaARendir entrega2 = new EntregaARendir();
        entrega2.setNombre("Viaje Roma");
        entrega2.setMotivo("Viáticos para mantenimiento de servidores");
        entrega2.setMontoSolicitado(500.00);
        entrega2.setEstado(EstadoFondo.Activo);
        entrega2.setSolicitante(emp2);
        entrega2.setAprobador(jefe);
        entrega2.setEstado(EstadoEntregaARendir.Pendiente);
        entrega2.setIdFondo(entregaARendirDAO.insertar(entrega2));



        List<EntregaARendir> entregas = entregaARendirDAO.listarTodas();
        for(EntregaARendir ent : entregas)
            System.out.println(ent);
        System.out.println();
        return  entregas;
    }
}
