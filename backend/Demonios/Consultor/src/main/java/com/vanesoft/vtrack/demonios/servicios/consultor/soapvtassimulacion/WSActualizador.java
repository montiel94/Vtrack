
package com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "WSActualizador", targetNamespace = "http://implementacion.servicioactualizador.webservice.vtrack.vanesoft.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WSActualizador {


    /**
     * 
     * @return
     *     returns java.util.List<com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.Pedido>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllPedidos", targetNamespace = "http://implementacion.servicioactualizador.webservice.vtrack.vanesoft.com/", className = "com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.GetAllPedidos")
    @ResponseWrapper(localName = "getAllPedidosResponse", targetNamespace = "http://implementacion.servicioactualizador.webservice.vtrack.vanesoft.com/", className = "com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.GetAllPedidosResponse")
    public List<Pedido> getAllPedidos();

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.util.List<com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.Pedido>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "actualizarEstado", targetNamespace = "http://implementacion.servicioactualizador.webservice.vtrack.vanesoft.com/", className = "com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.ActualizarEstado")
    @ResponseWrapper(localName = "actualizarEstadoResponse", targetNamespace = "http://implementacion.servicioactualizador.webservice.vtrack.vanesoft.com/", className = "com.vanesoft.vtrack.demonios.servicios.consultor.soapvtassimulacion.ActualizarEstadoResponse")
    public List<Pedido> actualizarEstado(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

}
