package unidades.unidad1.ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class LimpiarYOrdenarLecturas {
	
	public static void main(String[] args) {

        //Creo una lista para guardar solo las líneas que sean válidas
        List<String> lineasValidas = new ArrayList<>();

        //Para "leer" uso un try-with-resources (para que cierre el Bufer al terminar el try)
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            
            String linea; // Una variable para guardar cada línea que lea
            
            //Formateo de fecha
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //Un while para leer todas las líneas siempre que no sean nulas
            while ((linea = br.readLine()) != null) {
                
                //Divido en las 3 partes la línea (sensores, timestamp y valor)                
                //Uso split para dividir cada parte desde el ";"
                String[] partes = linea.split(";");
                
                //Valido que hay 3 partes (que no faltan)	
                if (partes.length == 3) {
                    
                    //Comprobacion de las partes
                    try {
                    	//La primera parte o parte 0 es un String normal, por lo que la dejamos tal cual
                    	//Compruebo que el timestamp sea correcto parseando la fecha y pasándole el formato
                        LocalDateTime.parse(partes[1], dtf);
                        
                        //Compruebo que el valor sea correcto y lo parseo 
                        double valor = Double.parseDouble(partes[2]);
                        
                        //Valido el rango para el valor
                        if (valor >= -20.0 && valor <= 60.0) {
                            lineasValidas.add(linea);
                        }
                        
                    } catch (NumberFormatException | DateTimeParseException e) {
                        // Si fallan las conversiones (parse) o no está en rango, es una línea mal formada.
                        // En principio la descartamos sin más (en silencio).
                    }
                }
                
            } 
            
        } catch (IOException e) {
            System.err.println("Error al leer en: " + e.getMessage());
        }

        //Ordeno lineasValidas usando el metodo sort de las ArrayList
        /*Con 'null' uso el orden natural/alfabético, ya que el formato de fecha usado es ordenable,
         de forma que no necesito comparar la parte de sensores con la de fechas.*/
        lineasValidas.sort(null); 

        //Imprimir el resultado ya ordenado
        for (String lV : lineasValidas) {
            System.out.println(lV);
        }
    }
}
