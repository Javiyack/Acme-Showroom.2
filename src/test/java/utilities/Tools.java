package utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tools {

    public static void main(String[] args) {
        List <String> Lines = getFileLines("src/main/resources/files/nombres-propios-es.txt");
        for (String line : Lines) {
            String nombre = getNombreAleatorio();
            String apellidos = getApellidosAleatorios();
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(getFechaNacimientoMayorDeEdadAleatorio());

            System.out.println(apellidos
                    + ", " + nombre
                    + ", " + getEmailFromFullName(nombre, apellidos)
                    + ", " + date
                    + ", " + getCreditCardNumber()
                    + ", " + getUrlAleatoria()
                    + ", " + getAddress()
                    + ", " + generateBussinesName());
        }
    }

    public static List <String> getFileLines(String filename) {
        List <String> result = new ArrayList <>();
        Charset charset = Charset.forName("ISO-8859-1");
        try {
            result = Files.readAllLines(Paths.get(filename), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCreditCardNumber() {
        String res;
        Integer n = BasicosAleatorios.getNumeroAleatorio(1, 4);
        res = "1111-2222-3333-4444";

        return res;
    }

    public static String getWrongCreditCardNumber() {
        String res;
        res = "AAAA-2222-333-4444";

        return res;
    }

    public static String getPastDate() {
        String res;
        res = "AAAA-2222-333-4444";

        return res;
    }


    public static Date getFechaNacimientoMayorDeEdadAleatorio() {
        Calendar calendar = Calendar.getInstance();
        Integer edadMinima = 18;
        Integer edadTope = BasicosAleatorios.getNumeroAleatorio(100);
        if (edadTope < 5)
            edadTope = 120;
        else if (edadTope < 35)
            edadTope = 60;
        else if (edadTope < 60)
            edadTope = 50;
        else if (edadTope < 100)
            edadTope = 35;
        calendar.add(Calendar.YEAR, -BasicosAleatorios.getNumeroAleatorio(edadMinima, edadTope));
        calendar.add(Calendar.MONTH, -BasicosAleatorios.getNumeroAleatorio(12));
        calendar.add(Calendar.DATE, -BasicosAleatorios.getNumeroAleatorio(31));
        Date date = calendar.getTime();

        return calendar.getTime();
    }

    public static String getEmailAleatorio() {
        String mail;
        mail = getNombreAleatorio().substring(0, 2).toLowerCase()
                + getApellidoAleatorio().substring(0, 2).toLowerCase()
                + getApellidoAleatorio().substring(0, 2).toLowerCase() + "@"
                + getDominioAleatorio();

        return mail;
    }

    public static String getWrongEmailAleatorio() {
        String mail;
        mail = getNombreAleatorio().substring(0, 2).toLowerCase()
                + getApellidoAleatorio().substring(0, 2).toLowerCase()
                + getApellidoAleatorio().substring(0, 2).toLowerCase() + ""
                + getDominioAleatorio();

        return mail;
    }


    public static String getEmailFromFullName(String nombre, String apellidos) {
        String mail;
        mail = nombre.substring(0, 3).toLowerCase();
        String[] apellidosSplit = apellidos.split(" ");
        mail += apellidosSplit[0].substring(0, 3).toLowerCase()
                + apellidosSplit[1].substring(0, 3).toLowerCase() + "@"
                + getDominioAleatorio();
        return mail;
    }

    public static String getwWrongEmailFromFullName(String nombre, String apellidos) {
        String mail;
        mail = nombre.substring(0, 3).toLowerCase();
        String[] apellidosSplit = apellidos.split(" ");
        mail += apellidosSplit[0].substring(0, 3).toLowerCase()
                + apellidosSplit[1].substring(0, 3).toLowerCase() + ""
                + getDominioAleatorio();
        return mail;
    }


    public static String getNombreAleatorio() {
        String nombre = "";
        String fichero = "src/main/resources/files/nombres-propios-es.txt";
        List <String> nombres = getFileLines(fichero);
        if (BasicosAleatorios.getNumeroAleatorio(3) > 0)
            nombre += nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size()));
        else {
            nombre = nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size())) + " "
                    + nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size()));
        }
        return nombre;

    }

    public static String getApellidosAleatorios() {
        String apellido = "";
        String fichero = "src/main/resources/files/apellidos-es.txt";
        List <String> nombres = getFileLines(fichero);
        apellido = nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size())) + " "
                + nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size()));
        return apellido;

    }

    public static String getApellidoAleatorio() {
        String apellido = "";
        String fichero = "src/main/resources/files/apellidos-es.txt";
        List <String> nombres = getFileLines(fichero);
        apellido += nombres.get(BasicosAleatorios.getNumeroAleatorio(nombres.size()));
        return apellido;

    }

    public static String getDominioAleatorio() {
        String domain;
        String filePath = "src/main/resources/files/dominios.txt";
        List <String> domains = getFileLines(filePath);
        domain = domains.get(BasicosAleatorios.getNumeroAleatorio(domains.size()));
        return domain;

    }


    public static String getUrlAleatoria() {
        String url = "http://www.";
        url += BasicosAleatorios.getCodigoAlfabeticoAleatorioEnNinusculas();
        url += ".com";
        return url;

    }


    public static String getBusinessName() {
        String businessName;
        String filePath = "src/main/resources/files/razon-social.txt";
        List <String> businessNames = getFileLines(filePath);
        businessName = businessNames.get(BasicosAleatorios.getNumeroAleatorio(businessNames.size()));
        return businessName;
    }

    public static String generateBussinesName() {
        String result = "";
        String vocales = "aeiou";
        String consonantes = "bcdfghjklmnñpqrstvwxyz";
        for (int i = 0; i < BasicosAleatorios.getNumeroAleatorio(1, 3); i++) {
            int si = BasicosAleatorios.getNumeroAleatorio(4);
            if (si == 0) {
                result += consonantes.charAt(BasicosAleatorios.getNumeroAleatorio(22));
                result += vocales.charAt(BasicosAleatorios.getNumeroAleatorio(5));
            } else if(si==1){
                result += consonantes.charAt(BasicosAleatorios.getNumeroAleatorio(22));
                result += vocales.charAt(BasicosAleatorios.getNumeroAleatorio(5));
                result += consonantes.charAt(BasicosAleatorios.getNumeroAleatorio(22));
            }
            else if(si==2){
                result += vocales.charAt(BasicosAleatorios.getNumeroAleatorio(5));
                result += consonantes.charAt(BasicosAleatorios.getNumeroAleatorio(22));
            }else if(si==3){
                result += vocales.charAt(BasicosAleatorios.getNumeroAleatorio(5));
                result += consonantes.charAt(BasicosAleatorios.getNumeroAleatorio(22));
                result += vocales.charAt(BasicosAleatorios.getNumeroAleatorio(5));
            }
        }
        String c = result.substring(0,1);
        return result.replaceFirst(c,c.toUpperCase());
    }

    public static String generateDescription(){
        String result = "";
        for (int i = 0; i < BasicosAleatorios.getNumeroAleatorio(40, 150); i++) {
            result += generateBussinesName().toLowerCase();
            result += " ";
        }
        result = result.trim() + ".";
        String c = result.substring(0,1);
        return result.replaceFirst(c,c.toUpperCase());
    }

    public static String getAddress() {
        String addressName;
        String filePath = "src/main/resources/files/razon-social.txt";
        List <String> addressNames = getFileLines(filePath);
        addressName = addressNames.get(BasicosAleatorios.getNumeroAleatorio(addressNames.size()));
        addressName += ", " + BasicosAleatorios.getNumeroAleatorio(1, 150);
        return addressName;
    }

    public static String getDescription() {
        String res = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus vitae finibus mauris. " +
                "Quisque malesuada, est eget placerat faucibus, nulla erat feugiat ligula, ut viverra orci arcu pretium odio. " +
                "Maecenas vulputate elit nec molestie fringilla turpis duis.";
        return res;

    }

    public static String getBlankText() {
        String res = "";
        return res;

    }

    public static String getNullkText() {
        String res = null;
        return res;

    }


}