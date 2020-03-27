package pmdm.qexame15.elprado;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TarefaDescargaXML extends AsyncTask<URL, Void, Document> {
    private final int tipoDescarga;
    private Cliente cliente;

    public TarefaDescargaXML(Cliente cliente, int tipoDescarga) {
        this.cliente = cliente;
        this.tipoDescarga = tipoDescarga;
    }

    @Override
    protected Document doInBackground(URL... urls) {
        Document resultado = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            resultado = db.parse(urls[0].openStream());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(Document resultado) {
        cliente.recibirDocumento(resultado, tipoDescarga);
    }


    interface Cliente {
        void recibirDocumento(Document resultado, int tipoDescarga);
    }
}
