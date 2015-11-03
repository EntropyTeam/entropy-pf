package backend.mail;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author Jose Ruiz
 */
public class Email {

    private String to;
    private String from;
    private String message;
    private String subject;
    private String smtpServ;
    private MimeMultipart multiParte;

    public Email() {
        this.to = "equipo.entropy@gmail.com";
        this.from = "Java.Mail.CA@gmail.com";
        this.smtpServ = "smtp.gmail.com";
        this.message = "";
        multiParte = new MimeMultipart();

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public String getSmtpServ() {
        return smtpServ;
    }

    public void setSmtpServ(String smtpServ) {
        this.smtpServ = smtpServ;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MimeMultipart getMultiParte() {
        return multiParte;
    }

    public void setMultiParte(MimeMultipart multiParte) {
        this.multiParte = multiParte;
    }

    public void setMessage(String mensaje) {
        this.message = mensaje;
        BodyPart texto = new MimeBodyPart();
        try {
            texto.setText(this.getMessage());
            multiParte.addBodyPart(texto);
        } catch (MessagingException e) {
            System.err.println("Ocurrio un error al crear la mutiparte del texto del mensaje");
            System.err.println(e.toString());
        }
    }

    public void setAdjunto(String nombreArchivo, byte[] bytes) {
        try {
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(nombreArchivo + ".pdf");
            this.getMultiParte().addBodyPart(pdfBodyPart);
        } catch (MessagingException e) {
            System.err.println("Ocurrio un error al crear la mutiparte del adjunto mensaje");
            System.err.println(e.toString());

        }
    }
}
