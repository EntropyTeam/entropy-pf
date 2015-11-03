/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.reportes;

import backend.auxiliares.Mensajes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author Denise
 */
public class Impresora {

    public static void imprimirPDF(File fileToPrint) throws FileNotFoundException, PrintException {
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
        FileInputStream psStream = null;
        psStream = new FileInputStream(fileToPrint);
        if (services.length > 0) {
            DocPrintJob printJob = services[0].createPrintJob();
            Doc document = new SimpleDoc(psStream, flavor, null);
            printJob.print(document, null);
            Mensajes.mostrarExito("Documento enviado a impresora.");
        } else {
            Mensajes.mostrarError("No existe una impresora PDF disponible.");
        }
    }
}
