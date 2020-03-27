package pmdm.qexame15.elprado;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Enquisa {
    long id;
    String titulo;
    Pregunta[] preguntas;

    public Enquisa(long id, String titulo, Pregunta[] preguntas) {
        this.id = id;
        this.titulo = titulo;
        this.preguntas = preguntas;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Pregunta getPreguntaAt(int position) {
        return this.preguntas[position];
    }

    public static Enquisa crearDendeXML (Document xmlEnquisa) {
        Enquisa enquisa = null;
        Element raiz = xmlEnquisa.getDocumentElement();
        final long idEnquisa = Long.parseLong(raiz.getAttribute("id_enquisa"));

        NodeList nl = raiz.getElementsByTagName("titulo");
        String titulo = nl.item(0).getTextContent();

        nl = raiz.getElementsByTagName("resposta");

        Pregunta[] preguntas = new Pregunta[nl.getLength()];

        for(int i=0; i<nl.getLength(); i++) {
            NamedNodeMap atributos = nl.item(i).getAttributes();

            preguntas[i] = new Pregunta(Long.parseLong(atributos.getNamedItem("id_resposta").getNodeValue()),  nl.item(i).getTextContent());
        }

        enquisa = new Enquisa(idEnquisa, titulo, preguntas);

        return enquisa;
    }
}
