package pmdm.qexame15.elprado;

import android.content.ContentProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnItemTouchListenerPMDM.OnItemTouchActionListener, TarefaDescargaXML.Cliente {

    private static final int LOGIN = 1;
    private static final int DESCARGA_COLECCIONES = 2;
    private static final int DESCARGA_OBRAS = 3;
    private static SQLiteDatabase db;
    private SharedPreferences sp;
    private Spinner spColeccions;
    private RecyclerView rvObras;
    private File cartafolCache;
    private static File cartafolMiniaturas;
    private static File cartafolFotos;
    private int ENQUISA = 4;
    private Enquisa enquisa;
    private int RESPOSTA_ENQUISA = 5;

    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, ENQUISA);
                tdx.execute(Servizo.urlEnquisa());
            }
        });


        // Abrimos a base de datos. Isto fai que se cree se é a primeira vez
        // que se executa o programa
        ElPradoOpenHelper edb = new ElPradoOpenHelper(this);
        if(db == null) {
            db = edb.getWritableDatabase();
        }

        // Abrimos as SharedPreferences, que usaremos para gardar as credenciais do usuario
        sp = getSharedPreferences("elprado", MODE_PRIVATE);

        // Establecemos o CookieManager
        CookieManager.setDefault(new CookieManager());

        // Establecemos os valores dos obxectos File que apuntan ós cartafois que usaremos
        // como caché de fotos, e creamos os cartafois se aínda non existen
        cartafolCache = getDir("cache", MODE_PRIVATE);
        if( ! cartafolCache.exists()) {
            cartafolCache.mkdir();
        }

        cartafolMiniaturas = new File(cartafolCache, "miniaturas");
        if( ! cartafolMiniaturas.exists()) {
            cartafolMiniaturas.mkdir();
        }

        cartafolFotos = new File(cartafolCache, "fotos");

        if( ! cartafolFotos.exists()) {
            cartafolFotos.mkdir();
        }


        //buscar las referencias al sp y rv
        spColeccions = (Spinner) findViewById(R.id.spColeccions);
        spColeccions.setOnItemSelectedListener(this);

        rvObras = (RecyclerView) findViewById(R.id.rvObras);
        rvObras.setLayoutManager(new LinearLayoutManager(this));

        //Poñemos un OnItemTouchListenerPMDM para o rvObras
        rvObras.addOnItemTouchListener(new OnItemTouchListenerPMDM(this, rvObras, this));

        login();
    }

    public static File getCartafolMiniaturas() {
        return cartafolMiniaturas;
    }

    public static File getCartafolFotos() {
        return cartafolFotos;
    }


    private void login() {

        String login = sp.getString("login", null);
        boolean credenciaisValidadas = sp.getBoolean("credenciaisValidadas", false);

        // Se login é null ou as credenciais non son válidas
        // (porque se fixo con elas un intento fallido de login)
        // teño que pedir nome de usuario e password ó usuario
        // lanzando o correspondente AlertDialog

        if(login == null || ! credenciaisValidadas) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Login");
            View v = getLayoutInflater().inflate(R.layout.dialog_login, null);
            adb.setView(v);
            adb.setCancelable(false);

            adb.setPositiveButton(R.string.adBotonOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialog ad = (AlertDialog) dialogInterface;

                    EditText etLoginUsuario = (EditText) ad.findViewById(R.id.etLoginUsuario);
                    EditText etLoginPassword = (EditText) ad.findViewById(R.id.etLoginPassword);

                    TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, LOGIN);
                    String login = etLoginUsuario.getText().toString();
                    String password = etLoginPassword.getText().toString();

                    //Gardamos en SharedPreferences o login e a password co que facemos este intento de login
                    sp.edit().putString("login", login)
                            .putString("password", password)
                            .putBoolean("credenciaisValidadas", false).commit();

                    tdx.execute(Servizo.urlLogin(login, password));
                }
            });

            adb.show();
        } else {
            // Inicio sesión co login e password que están en SharedPreferences
            String password = sp.getString("password", null);
            TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, LOGIN);
            tdx.execute(Servizo.urlLogin(login, password));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if(adapterView.getId() == spColeccions.getId()) {
            cargarObras(id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View item, int itemPosition, long id) {
        if(recyclerView.getId() == rvObras.getId()) {
            Intent intent = new Intent(this, DetallesObraActivity.class);
            // "Cargamos" o intent co id da obra como extra
            intent.putExtra("idObra", id);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(RecyclerView recyclerView, View item, int itemPosition, long id) {

    }

    @Override
    public void recibirDocumento(Document resultado, int tipoDescarga) {
        //Comprobar que resultado non é null, se o é avisamos ó usuario e non continuamos
        if(resultado == null) {
            Toast.makeText(this, "Problemas de conexión", Toast.LENGTH_SHORT).show();
            return;
        }

        Element raiz = resultado.getDocumentElement();

        if(tipoDescarga == LOGIN) {

            if (raiz.getTagName().equalsIgnoreCase("resposta")) {
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();

                if (raiz.getTextContent().equalsIgnoreCase("Login correcto")) {

                    sp.edit().putBoolean("credenciaisValidadas", true).commit();

                    cargarColeccions();

                } else {
                    login();
                }
            }
        } else if(tipoDescarga ==  DESCARGA_COLECCIONES){
            //Comprobar se a resposta do servidor corresponde a unha situación de erro
            if(raiz.getTagName().equalsIgnoreCase("resposta")) {
                // O contido do elemento raíz da resposta é unha mensaxe para o usuario
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();
                // O contido do atributo "mensaxe" do elemento raíz da resposta é unha mensaxe
                // con info da causa do erro para o programador
                System.out.println(raiz.getAttribute("mensaxe_debug"));
                return;
            }
            // Se o resultado é un éxito, usamos a información recibida para meter os módulos
            // na bd local, e despois xa podemos facer a consulta e a carga de spModulos.
            Coleccion.crearDendeXML(resultado);
            Cursor c = Coleccion.getAllCursor();
            encherSpColecciones(c);
        } else if (tipoDescarga == DESCARGA_OBRAS) {
            //Comprobar se a resposta do servidor corresponde a unha situación de erro
            if(raiz.getTagName().equalsIgnoreCase("resposta")) {
                // O contido do elemento raíz da resposta é unha mensaxe para o usuario
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();
                // O contido do atributo "mensaxe" do elemento raíz da resposta é unha mensaxe
                // con info da causa do erro para o programador
                System.out.println(raiz.getAttribute("mensaxe_debug"));
                return;
            }

            // Se o resultado é un éxito, usamos a información recibida para meter os alumnos
            // na bd local, e despois xa podemos facer a consulta e a carga de rvAlumnos.
            Obra.crearDendeXML(resultado);
            Cursor c = Obra.buscarObraColeccionCursor(spColeccions.getSelectedItemId());
            encherRvObras(c);
        } else  if(tipoDescarga == ENQUISA) {
            // Comprobamos se a resposta do servidor é un erro, e nese caso
            // mostramos a mensaxe ó usuario e saímos da función
            if(raiz.getTagName().equalsIgnoreCase("resposta")) {
                // O contido do elemento raíz da resposta é unha mensaxe para o usuario
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();
                // O contido do atributo "mensaxe" do elemento raíz da resposta é unha mensaxe
                // con info da causa do erro para o programador
                System.out.println(raiz.getAttribute("mensaxe_debug"));
                return;
            }

            enquisa = null;
            enquisa = Enquisa.crearDendeXML(resultado);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Enquisa");
            final View v = getLayoutInflater().inflate(R.layout.dialog_enquisa, null);
            adb.setView(v);

            TextView tvEnquisaTitulo = (TextView) v.findViewById(R.id.tvEnquisaTitulo);

            final RadioButton[] botones = {(RadioButton) v.findViewById(R.id.rbEnquisaResposta1),
                                    (RadioButton)v.findViewById(R.id.rbEnquisaResposta2),
                                    (RadioButton)v.findViewById(R.id.rbEnquisaResposta3),
                                    (RadioButton)v.findViewById(R.id.rbEnquisaResposta4)};


            tvEnquisaTitulo.setText(enquisa.getTitulo());
            for(int j = 0; j < 4; j++){
                botones[j].setText(enquisa.getPreguntaAt(j).getTexto());
            }
            adb.setCancelable(false);

            adb.setNegativeButton("Cancelar", null);
            adb.setPositiveButton("Responder", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    long id_respuesta = 0;
                    for(int k = 0; k < 4; k++){
                        if(botones[k].isChecked()){
                            id_respuesta = enquisa.getPreguntaAt(k).getId();
                        }
                    }

                    TarefaDescargaXML tdx = new TarefaDescargaXML(MainActivity.this, RESPOSTA_ENQUISA);
                    tdx.execute(Servizo.urlContestarEnquisa(enquisa.getId(), id_respuesta));

                }
            });

            adb.show();
        } else  if(tipoDescarga == RESPOSTA_ENQUISA){
            //Comprobar se a resposta do servidor corresponde a unha situación de erro
            if(raiz.getTagName().equalsIgnoreCase("resposta")) {
                // O contido do elemento raíz da resposta é unha mensaxe para o usuario
                Toast.makeText(this, raiz.getTextContent(), Toast.LENGTH_LONG).show();
                // O contido do atributo "mensaxe" do elemento raíz da resposta é unha mensaxe
                // con info da causa do erro para o programador
                System.out.println(raiz.getAttribute("mensaxe_debug"));
                return;
            }
        }
    }

    private void cargarColeccions() {
        Cursor c = Coleccion.getAllCursor();
        if(c.getCount() == 0) {
            // Se o cursor está baleiro lanzamos a descarga dos módulos
            TarefaDescargaXML tdx = new TarefaDescargaXML(this, DESCARGA_COLECCIONES);
            tdx.execute(Servizo.urlDescargaColecciones());
        } else {
            // Se os módulos xa están na bd local, enchemos con eles spModulos
            encherSpColecciones(c);
        }
    }

    private void encherSpColecciones(Cursor c) {
        // Creamos un adapter e aplicámosllo
        String[] from = new String[] {"nome"};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, from, to ,0);
        spColeccions.setAdapter(sca);
    }

    private void cargarObras(long idColeccion) {
        Cursor c = Obra.buscarObraColeccionCursor(idColeccion);
        if(c.getCount() == 0) {
            // Se o cursor está baleiro lanzamos a descarga das obras
            TarefaDescargaXML tdx = new TarefaDescargaXML(this, DESCARGA_OBRAS);
            tdx.execute(Servizo.urlDescargaObras(idColeccion));
        } else {
            // Se as obras xa están na bd local, enchemos con eles rvObras
            encherRvObras(c);
        }
    }

    private void encherRvObras(Cursor c) {
        ObraCursorAdapter oca = (ObraCursorAdapter) rvObras.getAdapter();
        // Comprobamos se rvObras está baleiro, é dicir, se oca é null
        if(oca == null) {
            //Creamos un ObraCursorAdapter e aplicámolo a rvObra
            oca = new ObraCursorAdapter(this, c);
            rvObras.setAdapter(oca);
        } else {
            oca.swapCursor(c).close();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        Cursor c = Obra.buscarObraColeccionCursor(spColeccions.getSelectedItemId());
        encherRvObras(c);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Pechamos os cursores que estean aínda abertos antes de saír do programa
        if(spColeccions.getAdapter() != null) {
            ((SimpleCursorAdapter)spColeccions.getAdapter()).getCursor().close();
        }

        if(rvObras.getAdapter() != null) {
            ((ObraCursorAdapter)rvObras.getAdapter()).getCursor().close();
        }
    }
}
