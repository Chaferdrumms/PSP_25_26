package unidades.unidad6;

import java.util.regex.Pattern;

public class Validador {
	
	//Valida login: exactamente 8 letras minúsculas
    public static boolean loginValido(String login) {
        return Pattern.matches("^[a-z]{8}$", login); 
    }

    //Valida nombre de fichero: máx 8 caracteres + punto + 3 de extensión
    public static boolean nombreFicheroValido(String nombre) {
        //Explicación: ^[a-zA-Z0-9]{1,8} (Nombre 1-8) + \\. (Punto) + [a-zA-Z0-9]{3}$ (Ext 3)
        String regex = "^[a-zA-Z0-9]{1,8}\\.[a-zA-Z0-9]{3}$";
        return Pattern.matches(regex, nombre);
    }
}