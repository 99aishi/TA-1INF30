package pe.edu.pucp.economix.operaciones.bo;

import pe.edu.pucp.economix.operaciones.boi.IComprobantePagoBO;
import pe.edu.pucp.economix.operaciones.dao.IComprobantePagoDAO;
import pe.edu.pucp.economix.operaciones.implement.ComprobantePagoImplement;
import pe.edu.pucp.economix.operaciones.model.ComprobantePago;

import java.util.List;

public class ComprobantePagoBOImpl implements IComprobantePagoBO {
    private final IComprobantePagoDAO comprobantePagoDAO;

    public ComprobantePagoBOImpl(){
        comprobantePagoDAO= new ComprobantePagoImplement();
    }
    @Override
    public int insertar(ComprobantePago comprobante) throws Exception {
        return comprobantePagoDAO.insertar(comprobante);
    }

    @Override
    public int modificar(ComprobantePago comprobante) throws Exception {
        return comprobantePagoDAO.modificar(comprobante);
    }

    @Override
    public int eliminar(int id) throws Exception {
        return comprobantePagoDAO.eliminar(id);
    }

    @Override
    public ComprobantePago buscarPorId(int id) throws Exception {
        return comprobantePagoDAO.buscarPorId(id);
    }

    @Override
    public List<ComprobantePago> listarTodas() throws Exception {
        return comprobantePagoDAO.listarTodas();
    }
}
