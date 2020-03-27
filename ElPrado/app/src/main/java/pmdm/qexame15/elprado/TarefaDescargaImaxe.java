package pmdm.qexame15.elprado;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class TarefaDescargaImaxe extends AsyncTask<URL, Void, Bitmap> {

    private final ImageView ivDestino;
    private final Cliente cliente;
    private final int tipoImaxe;
    private final String nome; //O nome asociado á imaxe que se descarga

    public TarefaDescargaImaxe(ImageView ivDestino) {
        this.ivDestino = ivDestino;
        this.cliente = null;
        this.tipoImaxe = -1;
        this.nome = null;
    }


    public TarefaDescargaImaxe(Cliente cliente, int tipoImaxe, String nome) {
        this.ivDestino = null;
        this.cliente = cliente;
        this.tipoImaxe = tipoImaxe;
        this.nome = nome;
    }

    public TarefaDescargaImaxe(Cliente cliente, int tipoImaxe, String nome, ImageView ivDestino) {
        this.ivDestino = ivDestino;
        this.cliente = cliente;
        this.tipoImaxe = tipoImaxe;
        this.nome = nome;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap resultado = null;

        try {
            resultado = BitmapFactory.decodeStream(urls[0].openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

 /*
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        return resultado;
    }


    @Override
    protected void onPostExecute(Bitmap resultado) {
        if(ivDestino != null) {
            ivDestino.setImageBitmap(resultado);
        }

        if(cliente != null) {
            cliente.recibirImaxe(resultado, tipoImaxe, nome);
        }
    }
	
	public interface Cliente {
        public void recibirImaxe(Bitmap resultado, int tipoImaxe, String nomeImaxe);
    }
}
