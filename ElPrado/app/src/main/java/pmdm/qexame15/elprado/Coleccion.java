package pmdm.qexame15.elprado;

import android.content.ContentValues;
import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Coleccion {
	private static String taboa = "coleccion";
	private static String[] columnas = new String[] {"_id", "nome", };

	private long id;
	private String nome;
	
	public Coleccion(long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public static Cursor getAllCursor() {
		return MainActivity.getDb().query(taboa, columnas, null, null, null, null, null);
	}

	public static void crearDendeXML(Document xmlColeccions) {
		Element raiz = xmlColeccions.getDocumentElement();

		NodeList nl = raiz.getElementsByTagName("coleccion");

		Coleccion coleccion;

		for(int i=0; i<nl.getLength(); i++) {
			NamedNodeMap atributos = nl.item(i).getAttributes();

			long id = Long.parseLong(atributos.getNamedItem("id").getNodeValue());
			String nome = atributos.getNamedItem("nome").getNodeValue();

			coleccion = new Coleccion(id, nome);
			coleccion.gardar();
		}
	}

	private void gardar() {
		ContentValues cv = new ContentValues();

		cv.put(columnas[0], this.id);
		cv.put(columnas[1], this.nome);

		MainActivity.getDb().insert(taboa, null, cv);
	}


	public long getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
}
