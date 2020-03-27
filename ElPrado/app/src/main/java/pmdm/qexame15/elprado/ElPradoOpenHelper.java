package pmdm.qexame15.elprado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ElPradoOpenHelper extends SQLiteOpenHelper {
    public ElPradoOpenHelper(@Nullable Context context) {
        super(context, "elprado.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE coleccion (_id INT NOT NULL PRIMARY KEY, nome TEXT NOT NULL);");
        db.execSQL("CREATE TABLE obra (_id INT NOT NULL PRIMARY KEY, titulo TEXT NOT NULL, ano INT NOT NULL,autor TEXT NOT NULL, tecnica TEXT NOT NULL, ancho INT NOT NULL, alto INT NOT NULL, idcoleccion INT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
