package com.vanesoft.vtrack.demonios.servicios.consultor.DAO.Contratos;

import com.vanesoft.vtrack.demonios.servicios.consultor.EntidadesDemonio.Pedido;
import com.vanesoft.vtrack.demonios.servicios.consultor.EntidadesDemonio.usuario;

/**
 * Created by Daniel jose on 09/04/2017.
 */
public interface IDAOCorreo {

    public boolean armarCorreoPedidoEstado(usuario usuarioEnviarCorreo, Pedido pedido, String estado);

}
