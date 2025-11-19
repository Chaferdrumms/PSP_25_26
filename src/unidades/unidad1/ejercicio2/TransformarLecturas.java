package unidades.unidad1.ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransformarLecturas {
	
	public static void main(String[] args) {
		
		/* Compruebo que tengo los argumentos correctos. Si no hay al menos 2 argumentos (entrada y salida), 
		 * imprime mensaje de error mostrando al usuario como debe escribir el comando.*/
        if (args.length < 2) {
            System.err.println("Uso: java TransformarLecturas <fichero_entrada> <fichero_salida>");
            return; // Salir del programa
        }
        
        //Variable para el --minAlert
        double alerta = 30.0; //Valor por defecto
        
        //Si hay un tercer argumento, compruebo su formato
        if (args.length == 3) {
        	
        	//Compruebo que el argumento empieza "--minAlert="
        	if(args[2].startsWith("--minAlert=")) {
        		
        		//Lo proceso dentro de un try/catch
        		try {
        			//Divido el argumento en dos para poder obtener el valor de alerta que deseo cambiar
                	String[] umbral = args[2].split("=");
                	
                	//Modifico alerta parseando el string a double
                	alerta = Double.parseDouble(umbral[1]);
                	
            	}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            		//Ya sea un error en el formato del valor o un mal split
            		System.err.println("ADVERTENCIA: Valor de --minAlert no válido. Usando 30.0 por defecto.");
            	}
            } else {
            	//Si el 3er argumento no es "--minAlert="
            	System.err.println("ADVERTENCIA: Tercer argumento desconocido. Usando 30.0 por defecto.");
            }
        }	

        //Anido un try-with-resources dentro de otro (uno para escribir y otro para leer)
        //Con el "try" externo abro el escritor (PrintWriter)
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])))) {
            
            //El 'try' interno abre el lector (BufferedReader)
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                
            	//Variable para almacenar cada línea que lea en el bucle
                String linea;
                
                //Formateo de fecha
        		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        		
                //Un bucle para la lectura
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
                            
                            //Variable para agregar la "transformacion"
                            String nuevaLinea;
                            
                            //Valido el rango para el valor
                            if (valor >= alerta) {
                                nuevaLinea = linea + ";ALERTA";
                            } else {
                            	nuevaLinea = linea + ";OK";
                            }      
                            
                            //Escribo cada línea con el objeto de escritor creado en el primer try-with-resources
                            pw.println(nuevaLinea);
                            
                        } catch (NumberFormatException | DateTimeParseException e) {
                        	//Informo de que hay un error en el formato de esa línea
                            pw.println(linea + ";ERROR_FORMATO");
                        }
                    }else {
                        //Si la línea no tiene 3 partes
                        pw.println(linea + ";ERROR_ESTRUCTURA");
                    }           
                }
                
            } catch (IOException e) {
                System.err.println("Error al leer el fichero de entrada: " + e.getMessage());
            }     
            
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero de salida: " + e.getMessage());
        }
    }
}