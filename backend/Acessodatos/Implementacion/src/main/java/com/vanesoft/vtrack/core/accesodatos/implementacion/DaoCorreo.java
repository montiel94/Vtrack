package com.vanesoft.vtrack.core.accesodatos.implementacion;
import com.vanesoft.vtrack.core.accesodatos.contratos.IDaoParametro;
import com.vanesoft.vtrack.core.accesodatos.contratos.IDaoPlantilla;
import com.vanesoft.vtrack.core.accesodatos.contratos.IDaoCorreo;
import com.vanesoft.vtrack.core.accesodatos.contratos.IDaoUsuario;
import com.vanesoft.vtrack.core.entidades.*;
import com.vanesoft.vtrack.core.utilidades.propiedades.Armador;
import com.vanesoft.vtrack.core.utilidades.propiedades.CifrarDescifrar;
import com.vanesoft.vtrack.core.utilidades.propiedades.PropiedadesAccesoDatos;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;
/**
 * Sistema:             Vtrack
 * Nombre:              IDaoCorreo
 * Descripcion:         Contrato del dao dedicado a las operaciones con correos de notifiacion
 *
 * @author montda
 * @version 1.0
 * @since 04/02/2017
 */
public class DaoCorreo implements IDaoCorreo{

    /**
     * Descripcion: metodo que envia correo finalmente
     * @params usuario : usuario a enviar correo
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */

    public void enviarCorreo(Correo correoEnviando)
    {
        Properties props = new Properties();
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPPAUTH,
                PropiedadesAccesoDatos.CONFIG_MAILSMTPAUTHVALOR);
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPSTARTTLSENABLE,
                PropiedadesAccesoDatos.CONFIG_MAILSMTPSTARTTLSENABLEVALOR);
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPHOST,
                PropiedadesAccesoDatos.CONFIG_MAILSMTPHOSTVALOR);
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPPORT,
                PropiedadesAccesoDatos.CONFIG_MAILSMTPPORTVALOR);
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPMAILSENDER,
                correoEnviando.getUser());
        props.put(PropiedadesAccesoDatos.CONFIG_MAILSMTPUSER,correoEnviando.getUser());
        Session session = Session.getDefaultInstance(props);
        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(correoEnviando.getUser()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correoEnviando.getTo()));
            message.setSubject(correoEnviando.getAsunto());
            message.setContent(correoEnviando.getMensaje(),
                    PropiedadesAccesoDatos.CONFIG_SETCONTENT);
            Transport t = session.getTransport(PropiedadesAccesoDatos.CONFIG_MAILGETTRANSPORTE);
            t.connect((String)props.get(PropiedadesAccesoDatos.CONFIG_MAILSMTPUSER),
                    correoEnviando.getUserPassword() );
            t.sendMessage(message, message.getAllRecipients());
            t.close();


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Descripcion: metodo que generar password nueva
     *
     * @params usuario : usuario a enviar correo
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */
    public void envioCorreoUsuarioXIntentosLogin (usuario usuarioEnviarCorreo)
    {
        try {
            String nuevaContrasena = "";
            String nuevaContrasenaEncriptada = "";
            IDaoUsuario daoUsuario = FabricaDao.obtenerDaoUsuario();
            IDaoPlantilla daoPlantilla = FabricaDao.obtenerDaoPlantilla();
            Plantilla plantillaEnBd = daoPlantilla.consultarPlantilla(TipoPlantilla.usuariobloqueadointentoslogin);
            IDaoParametro daoParametro = FabricaDao.obtenerDaoParametro();
            nuevaContrasena = generarContrasenaProvisional(Integer.valueOf(PropiedadesAccesoDatos.CONFIG_TAMANO_CONTRASENA));
            nuevaContrasenaEncriptada = CifrarDescifrar.cifrar(nuevaContrasena);
            ArrayList parametrosPlantillaEnBd = daoParametro.consultarParametrosXPlantilla(TipoPlantilla.usuariobloqueadointentoslogin);
            Hashtable<String, String> valores = new Hashtable<String, String>();
            valores.put(ParametroMensaje.nombreParametroNombreUsuario, usuarioEnviarCorreo.getNombreempresa());
            valores.put(ParametroMensaje.NombreParametroPassword, nuevaContrasena);
            String MensajeFinal = Armador.armarMensaje(plantillaEnBd.getTexto(), parametrosPlantillaEnBd, valores);
            Correo correoEnviando = new Correo(PropiedadesAccesoDatos.CONFIG_CORREO_REMINENTE,
                    usuarioEnviarCorreo.getUsername(), PropiedadesAccesoDatos.CONFIG_CONTRASENA_REMINENTE, plantillaEnBd.getTitulo(),
                    MensajeFinal);
            enviarCorreo(correoEnviando);
            daoUsuario.modificarContrasenaUsuario(usuarioEnviarCorreo, nuevaContrasenaEncriptada);
        }catch (Exception e)
        {

        }
    }
    /**
     * Descripcion: metodo que arma el correo cuando el usuario olvida su contrasena
     *
     * @params usuario : usuario a enviar correo
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */
    private boolean armarCorreoUsuarioOlvidoContrasena(usuario usuarioEnviarCorreo)
    {
        boolean exito = false;
        IDaoUsuario  daoUsuario = FabricaDao.obtenerDaoUsuario();
        IDaoPlantilla daoPlantilla = FabricaDao.obtenerDaoPlantilla();
        Plantilla plantillaEnBd = daoPlantilla.consultarPlantilla(TipoPlantilla.usuarioolvidocontrasena);
        IDaoParametro daoParametro = FabricaDao.obtenerDaoParametro();
        String nuevaContrasena = generarContrasenaProvisional(Integer.valueOf(PropiedadesAccesoDatos.CONFIG_TAMANO_CONTRASENA));
        String nuevaContrasenaEncriptada = CifrarDescifrar.cifrar(nuevaContrasena);
        ArrayList parametrosPlantillaEnBd = daoParametro.consultarParametrosXPlantilla(TipoPlantilla.usuarioolvidocontrasena);
        Hashtable<String, String> valores = new Hashtable<String, String>();
        valores.put(ParametroMensaje.nombreParametroNombreUsuario, usuarioEnviarCorreo.getNombreempresa());
        valores.put(ParametroMensaje.NombreParametroPassword, nuevaContrasena);
        String MensajeFinal = Armador.armarMensaje(plantillaEnBd.getTexto(), parametrosPlantillaEnBd, valores);
        Correo correoEnviando = new Correo(PropiedadesAccesoDatos.CONFIG_CORREO_REMINENTE,
                usuarioEnviarCorreo.getUsername(), PropiedadesAccesoDatos.CONFIG_CONTRASENA_REMINENTE, plantillaEnBd.getTitulo(),
                MensajeFinal);
        enviarCorreo(correoEnviando);
        daoUsuario.modificarContrasenaUsuario(usuarioEnviarCorreo, nuevaContrasenaEncriptada);

        exito = true;
        return exito;
    }
    /**
     * Descripcion: metodo que arma el correo cuando el pedido cambia de estado
     *
     * @params usuario : usuario a enviar correo, pedido pedido a detallar, estado : estado del pedido
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */
    public boolean armarCorreoPedidoEstado( usuario usuarioEnviarCorreo, Pedido pedido, String estado)
    {
        boolean exito = false;
        IDaoPlantilla daoPlantilla = FabricaDao.obtenerDaoPlantilla();
        Plantilla plantillaEnBd = daoPlantilla.consultarPlantilla(TipoPlantilla.pedidofinalizollenado);
        IDaoParametro daoParametro = FabricaDao.obtenerDaoParametro();
        ArrayList parametrosPlantillaEnBd = daoParametro.consultarParametrosXPlantilla(TipoPlantilla.pedidofinalizollenado);
        Hashtable<String, String> valores = new Hashtable<String, String>();
        valores.put(ParametroMensaje.nombreParametroNombreUsuario, usuarioEnviarCorreo.getNombreempresa());
        valores.put(ParametroMensaje.nombreParametroCodigoPedido, String.valueOf(pedido.getCodigoPedido()));
        valores.put(ParametroMensaje.nombreHoraFinLlenado,pedido.getFin());
        valores.put(ParametroMensaje.nombreHoraInicioLlenado,pedido.getInicio());
        valores.put(ParametroMensaje.nombreChofer,pedido.getChofer());
        valores.put(ParametroMensaje.nombreCola,pedido.getCola());
        valores.put(ParametroMensaje.nombreCabezote,pedido.getCabezote());
        valores.put(ParametroMensaje.nombreEstadoPedido,estado);
        String MensajeFinal = Armador.armarMensaje(plantillaEnBd.getTexto(), parametrosPlantillaEnBd, valores);
        Correo correoEnviando = new Correo(PropiedadesAccesoDatos.CONFIG_CORREO_REMINENTE,
                usuarioEnviarCorreo.getUsername(), PropiedadesAccesoDatos.CONFIG_CONTRASENA_REMINENTE, plantillaEnBd.getTitulo(),
                MensajeFinal);
        enviarCorreo(correoEnviando);

        exito = true;
        return exito;
    }
    /**
     * Descripcion: metodo que envia correo parametrizadamente
     * @params usuario : usuario a enviar correo
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */
    public Boolean envioCorreoUsuarioParametrizado (usuario usuarioEnviarCorreo,String tipoPlantillaa)
    {
        Boolean exito = false;
        Correo correoAEnviar = null;
        try
        {
            if (tipoPlantillaa.equals(TipoPlantilla.usuarioolvidocontrasena))
                 armarCorreoUsuarioOlvidoContrasena(usuarioEnviarCorreo);
            exito = true;

        }catch (Exception e)
        {

        }
        return exito;
    }

    /**
     * Descripcion: metodo que generar password nueva
     *
     * @params usuario : usuario a enviar correo
     * @version 1.0
     * @author montda
     * @since 04/02/2017
     */
    public String generarContrasenaProvisional(int longitud)
    {
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i < longitud){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                cadenaAleatoria += c;
                i ++;
            }
        }
        return cadenaAleatoria;
    }

}
