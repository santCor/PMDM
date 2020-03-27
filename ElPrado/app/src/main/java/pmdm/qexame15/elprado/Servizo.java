package pmdm.qexame15.elprado;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Servizo {

    private static String urlBase = "http://172.16.0.56/dam2_pmdm/elprado/";

    public static URL urlLogin(String login, String password) {
        String cadeaUrl = urlBase + "login.php?usuario=" + encode(login) + "&password=" + encode(password);

        //System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlDescargaColecciones() {
        String cadeaUrl = urlBase + "coleccions.php";

        //System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlDescargaObras(long idColeccion) {
        String cadeaUrl = urlBase + "obras.php?id_coleccion=" + idColeccion;

//        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlDescargaMiniaturaObra(long idObra) {
        String cadeaUrl = urlBase + "miniatura.php?id_obra=" + idObra;

//        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlDescargaFotoObra(long idObra) {
        String cadeaUrl = urlBase + "foto.php?id_obra=" + idObra;

//        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlEnquisa() {
        String cadeaUrl = urlBase + "enquisa.php";

        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    public static URL urlContestarEnquisa(long id, long id_respuesta) {
        String cadeaUrl = urlBase + "contestar_enquisa.php?id_enquisa=" + id + "&id_resposta=" + id_respuesta;

        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    private static String encode(String valor) {
        try {
            return URLEncoder.encode(valor, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static URL cadea2Url(String cadeaUrl) {
        try {
            return new URL(cadeaUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
