package pe.edu.pucp.economix.operaciones.boi;

import pe.edu.pucp.economix.operaciones.ibo.ICicloCajaBO;
import pe.edu.pucp.economix.operaciones.ibo.IRendicionBO;
import pe.edu.pucp.economix.operaciones.idao.IRendicionDAO;
import pe.edu.pucp.economix.operaciones.daoi.RendicionDAOImpl;
import pe.edu.pucp.economix.operaciones.model.CicloCajaChica;
import pe.edu.pucp.economix.operaciones.model.Rendicion;

import java.util.Date;
import java.util.List;

public class RendicionBOImpl implements IRendicionBO {

    private final IRendicionDAO rendicionDAO;
    private final ICicloCajaBO cicloCajaBO;
    public RendicionBOImpl(){
        rendicionDAO = new RendicionDAOImpl();
        cicloCajaBO =new CicloCajaBOImpl();
    }
    @Override
    public int insertar(Rendicion rendicion) throws Exception {
        validar(rendicion,false);
        return rendicionDAO.insertar(rendicion);
    }

    @Override
    public int modificar(Rendicion rendicion) throws Exception {
        validar(rendicion,true);
        return rendicionDAO.modificar(rendicion);
    }

    @Override
    public int eliminar(int id) throws Exception {

        return rendicionDAO.eliminar(id);
    }

    @Override
    public Rendicion buscarPorId(int id) throws Exception {
        return rendicionDAO.buscarPorId(id);
    }

    @Override
    public List<Rendicion> listarTodas() throws Exception {
        return rendicionDAO.listarTodas();
    }

    public void validar(Rendicion rendicion, boolean esModificacion) throws Exception{
        if(rendicion == null ){
            throw new Exception("La rendicion no puede ser nula.");
        }
        if(esModificacion && rendicion.getIdRendicion()<=0){
             throw new Exception("El id de la redencion es obligatorio para la modificación.");
        }

        validarCicloCajaChica(rendicion.getCicloCajaChica());
        validarMontos(rendicion);
    }

    public void calcularTotales(Rendicion rendicion) throws Exception {
        rendicion.calcularTotalDeclarado();
        if(rendicion.getCicloCajaChica().getTotalGastado()==0)
            cicloCajaBO.calcularTotalGastado(rendicion.getCicloCajaChica());
        rendicion.calcularTotalAprobado(); // usa CICLOCAJA
        modificar(rendicion);
    }




    public void validarMontos(Rendicion rendicion) throws Exception {
        CicloCajaChica ciclo = rendicion.getCicloCajaChica();

        if (rendicion.getTotalDeclarado() < 0) {
            throw new Exception("El monto total declarado debe ser valido.");
        }

        if (rendicion.getTotalDeclarado() > ciclo.getSaldoInicial()) {
            throw new Exception("El monto rendido (" + rendicion.getTotalDeclarado() +
                    ") excede el saldo inicial del ciclo (" + ciclo.getSaldoInicial() + ").");
        }

        if (rendicion.getTotalAprobado() > rendicion.getTotalDeclarado()) {
            throw new Exception("El monto aprobado no puede ser mayor al monto declarado.");
        }
    }

    public void validarFechaPresentacion(Date fechaPresentacion)throws Exception {
        if(fechaPresentacion==null){
            throw new Exception("La rendicion tiene que tener una fecha de presentacion.");
        }
    }

    public void validarCicloCajaChica(CicloCajaChica cicloCajaChica) throws Exception{
        if(cicloCajaChica==null){
            throw new Exception("La rendicion tiene que tener un ciclo de caja chica asociado.");
        }

        if(cicloCajaBO.buscarPorId(cicloCajaChica.getIdCicloCaja())==null) {
            throw new Exception("El ciclo caja chica asignado no existe.");
        }


    }
}
