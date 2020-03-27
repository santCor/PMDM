package pmdm.qexame15.elprado;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ObraCursorAdapter extends CursorAdapterPMDM<ObraCursorAdapter.ObraViewHolder> implements TarefaDescargaImaxe.Cliente {
    private static final int DESCARGA_MINIATURA = 1;

    public ObraCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @NonNull
    @Override
    public ObraCursorAdapter.ObraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_obra, parent,false);
        return new ObraViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ObraCursorAdapter.ObraViewHolder holder, int position) {
        c.moveToPosition(position);

        holder.tvItemObraTitulo.setText(c.getString(c.getColumnIndex("titulo")));
        holder.tvItemObraAutor.setText(c.getString(c.getColumnIndex("autor")));

        //Cargamos a foto miniatura da obra.
        //Antes de nada, miramos se a foto xa está na caché
        File ficheiroMiniatura = new File(MainActivity.getCartafolMiniaturas(), c.getLong(c.getColumnIndex("_id")) +".png");
        if(ficheiroMiniatura.exists()) {
            // Se a foto existe na caché, lemos o ficheiro para crear un bitmap, e enchemos
            // con el o iv
            Bitmap miniatura = BitmapFactory.decodeFile(ficheiroMiniatura.getAbsolutePath());
            holder.ivItemObraMiniatura.setImageBitmap(miniatura);
        } else {
            // Se a foto non existe na caché, lanzamos a súa descarga
            // Isto xa fai que se mostre a foto en ivItemAlumnoFoto
            // Faltará só gardar a imaxe recibida na caché
            TarefaDescargaImaxe tdi = new TarefaDescargaImaxe(this, DESCARGA_MINIATURA, ficheiroMiniatura.getName(), holder.ivItemObraMiniatura);
            tdi.execute(Servizo.urlDescargaMiniaturaObra(c.getLong(c.getColumnIndex("_id"))));
        }
    }

    @Override
    public void recibirImaxe(Bitmap resultado, int tipoImaxe, String nomeImaxe) {
        if(resultado == null) {
            return;
        }

        if(tipoImaxe == DESCARGA_MINIATURA) {
            // Gargamos a imaxe recibida na caché de miniaturas
            File miniatura = new File(MainActivity.getCartafolMiniaturas(), nomeImaxe);
            try {
                resultado.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(miniatura.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    class ObraViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemObraTitulo;
        private final TextView tvItemObraAutor;
        private final ImageView ivItemObraMiniatura;

        public ObraViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemObraTitulo = (TextView) itemView.findViewById(R.id.tvItemObraTitulo);
            tvItemObraAutor = (TextView) itemView.findViewById(R.id.tvItemObraAutor);
            ivItemObraMiniatura = (ImageView) itemView.findViewById(R.id.ivItemObraMiniatura);
        }
    }
}
