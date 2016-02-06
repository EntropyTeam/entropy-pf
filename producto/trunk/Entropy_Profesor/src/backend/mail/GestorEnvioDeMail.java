package backend.mail;


import backend.dao.resoluciones.DAOResolucion;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Jose Ruiz
 */
public class GestorEnvioDeMail {

    private Message msg;

    public GestorEnvioDeMail() {

    }

    public boolean enviarMail(Email nuevoMail) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", nuevoMail.getSmtpServ());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465"); // puede que el puerto sea 587
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            this.msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(nuevoMail.getFrom()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(nuevoMail.getTo(), false));
            msg.setSubject(nuevoMail.getSubject());
            msg.setContent(nuevoMail.getMultiParte());
            msg.setHeader("MyMail", "Mr. XYZ");
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("El mensaje a " + nuevoMail.getTo() + " a sido enviado correctamente");
            return true;
        } catch (MessagingException ex) {
            System.err.println("El mensaje no pudo enviado a " + nuevoMail.getTo());
            System.err.println("Exception " + ex);
            return false;
        }
    }

    //No va ser usado
    public void enviarMuchosDestinatarios(Email mail, ArrayList<String> destinatarios) {
        for (String destinatario : destinatarios) {
            mail.setTo(destinatario);
            enviarMail(mail);
        }
    }

    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "equipo.entropy@gmail.com";
            String password = "teamentropy";
            return new PasswordAuthentication(username, password);
        }
    }
    
    public boolean setResolucionEnviada(int idResolucion, boolean fueEnviada){
        return new DAOResolucion().setFueEnviadaPorEmail(idResolucion, fueEnviada);
    }
}
