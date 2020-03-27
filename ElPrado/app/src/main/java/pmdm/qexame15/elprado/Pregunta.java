package pmdm.qexame15.elprado;

class Pregunta {
    long id;
    String texto;

    public Pregunta(long id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }
}
