package pmdm.qexame15.elprado;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DetallesObraActivity extends AppCompatActivity implements TarefaDescargaImaxe.Cliente {

    private static final int DESCARGA_FOTO = 1;
    private TextView tvTituloObra;
    private TextView tvNomeAutorObra;
    private TextView tvAnoObra;
    private TextView tvTecnicaObra;
    private TextView tvAnchoObra;
    private TextView tvAltoObra;
    private ImageView ivFotoObra;

    private Obra obra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_obra);

        tvTituloObra = (TextView) findViewById(R.id.tvTituloObra);
        tvNomeAutorObra =(TextView) findViewById(R.id.tvNomeAutorObra);
        tvAnoObra = (TextView) findViewById(R.id.tvAnoObra);
        tvTecnicaObra = (TextView) findViewById(R.id.tvTecnicaObra);
        tvAnchoObra = (TextView) findViewById(R.id.tvAnchoObra);
        tvAltoObra = (TextView) findViewById(R.id.tvAltoObra);

        ivFotoObra = (ImageView) findViewById(R.id.ivFotoObra);

        // long id, String titulo, int ano, String autor, String tecnica, int ancho, int alto, long idColeccion
        long idObra = getIntent().getLongExtra("idObra", -1);
        obra = Obra.buscarObraPorId(idObra);

        tvTituloObra.setText(obra.getTitulo());
        tvNomeAutorObra.setText(obra.getAutor());
        tvAnoObra.setText(obra.getAno() + "");
        tvTecnicaObra.setText(obra.getTecnica());
        tvAnchoObra.setText(obra.getAncho() + "");
        tvAltoObra.setText(obra.getAlto() +"");

        // Mostrar a foto do alumno, buscando primeiro se está na caché
        File arquivoFoto = new File(MainActivity.getCartafolFotos(), obra.getId() + ".png");
        if(arquivoFoto.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(arquivoFoto.getAbsolutePath());
            ivFotoObra.setImageBitmap(bmp);
        } else {
            TarefaDescargaImaxe tdi = new TarefaDescargaImaxe(this, DESCARGA_FOTO, obra.getId() + ".png", ivFotoObra);
            tdi.execute(Servizo.urlDescargaFotoObra(obra.getId()));
        }
    }


    @Override
    public void recibirImaxe(Bitmap resultado, int tipoImaxe, String nomeImaxe) {
        if(resultado == null) {
            return;
        }

        //Gardamos a imaxe recibida na caché de fotos
        File ficheiroFoto =new File(MainActivity.getCartafolFotos(), nomeImaxe);
        try {
            resultado.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(ficheiroFoto.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
