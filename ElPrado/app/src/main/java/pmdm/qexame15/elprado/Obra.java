package pmdm.qexame15.elprado;

import android.content.ContentValues;
import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Obra {
	private static String taboa = "obra";
	private static String[] columnas = new String[] {"_id", "titulo", "ano", "autor", "tecnica", "ancho", "alto", "idcoleccion", };

	private long id;
	private String titulo;
	private int ano;
	private String autor;
	private String tecnica;
	private int ancho;
	private int alto;
	private long idColeccion;

	public Obra(long id, String titulo, int ano, String autor, String tecnica, int ancho, int alto, long idColeccion) {
		this.id = id;
		this.titulo = titulo;
		this.ano = ano;
		this.autor = autor;
		this.tecnica = tecnica;
		this.ancho = ancho;
		this.alto = alto;
		this.idColeccion = idColeccion;
	}

	public static Cursor buscarObraColeccionCursor(long idColeccion) {
		return MainActivity.getDb().query(taboa, columnas, "idcoleccion=?", new String[] {String.valueOf(idColeccion)}, null, null, "autor");
	}

	public static void crearDendeXML(Document xmlObra) {
		Element raiz = xmlObra.getDocumentElement();
		long idColeccion = Long.valueOf(raiz.getAttribute("id_coleccion"));

		NodeList nl = raiz.getElementsByTagName("obra");

		for(int i=0; i<nl.getLength(); i++) {
			NamedNodeMap atributos = nl.item(i).getAttributes();

			long id = Long.valueOf(atributos.getNamedItem("id").getTextContent());
			String titulo = atributos.getNamedItem("titulo").getTextContent();
			int ano = Integer.valueOf(atributos.getNamedItem("ano").getTextContent());
			int ancho = Integer.valueOf(atributos.getNamedItem("ancho").getTextContent());
			int alto = Integer.valueOf(atributos.getNamedItem("alto").getTextContent());
			String autor = atributos.getNamedItem("autor").getTextContent();
			String tecnica = atributos.getNamedItem("tecnica").getTextContent();

			Obra a = new Obra(id, titulo, ano, autor, tecnica, ancho, alto, idColeccion);
			a.gardar();
		}
	}

	public static Obra buscarObraPorId(long idObra) {
		Obra obra = null;

		Cursor c = MainActivity.getDb().query(taboa, columnas, "_id = ?", new String []{String.valueOf(idObra)}, null, null, null);
		if(c.moveToFirst()) {
			obra = new Obra(idObra, c.getString(c.getColumnIndex("titulo")),
					c.getInt(c.getColumnIndex("ano")),
					c.getString(c.getColumnIndex("autor")),
					c.getString(c.getColumnIndex("tecnica")),
					c.getInt(c.getColumnIndex("ancho")),
					c.getInt(c.getColumnIndex("alto")),
					c.getLong(c.getColumnIndex("idcoleccion")));
		}

		return obra;
	}

	private void gardar() {
		ContentValues cv = new ContentValues();

		cv.put(columnas[0], this.id);
		cv.put(columnas[1], this.titulo);
		cv.put(columnas[2], this.ano);
		cv.put(columnas[3], this.autor);
		cv.put(columnas[4], this.tecnica);
		cv.put(columnas[5], this.ancho);
		cv.put(columnas[6], this.alto);
		cv.put(columnas[7], this.idColeccion);

		MainActivity.getDb().insert(taboa, null, cv);
	}

	public long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getAno() {
		return ano;
	}

	public String getAutor() {
		return autor;
	}

	public String getTecnica() {
		return tecnica;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public long getIdColeccion() {
		return idColeccion;
	}
	
}
