package unidades.unidad6;

import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainSolicitarDatos {
	public static void main(String[] args) {
		
        //Inicio servicios
        GestorLogs.inicializar();
        Logger log = GestorLogs.getLogger();
        
        try (Scanner sc = new Scanner(System.in)) {
        	log.info("Aplicación iniciada.");

            //Solicitar Login
            String login = "";
            boolean esValido = false;
            
            while(!esValido) {
            	System.out.println("Introduce login (8 letras minúsculas):");
                login = sc.nextLine();

                if (Validador.loginValido(login)) {
                	esValido = true;
                	log.info("Login exitoso para el usuario: " + login);
                    System.out.println("¡Bienvenido, " + login + "!");
                }else {
                	log.warning("Intento de acceso con login inválido: " + login);
                    System.err.println("Error: El login debe tener exactamente 8 minúsculas. Prueba de nuevo.");
                }
            }       

            //Solicitar Fichero
            String fichero = "";
            boolean ficheroValido = false;
            
            while(!ficheroValido) {
            	System.out.println("Introduce el nombre del fichero (ej: pablo.txt):");
                fichero = sc.nextLine();
                
                if (Validador.nombreFicheroValido(fichero)) {
                	//Compruebo si el fichero existe antes de darlo por válido
                    File archivo = new File(fichero);
                    if (archivo.exists()) {
                        ficheroValido = true;
                        log.info("Fichero validado y encontrado: " + fichero);
                    } else {
                    	log.severe("Fichero no encontrado en disco: " + fichero); 
                        System.err.println("Error: El archivo '" + fichero + "' no existe. Prueba de nuevo.");
                    }
                }else {
                	log.warning("Usuario " + login + " intentó usar nombre de fichero inválido: " + fichero);
                    System.err.println("Error: Formato incorrecto (máx 8 letras + punto + 3 letras). Prueba de nuevo.");
                }
            }
            //Mostrar contenido del fichero
            leerArchivo(fichero, log);
        }
        
        log.info("Aplicación finalizada correctamente.");
    }

    private static void leerArchivo(String nombre, java.util.logging.Logger log) {
        File file = new File(nombre);
        
        //Try-with-resources
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            log.info("Leyendo contenido del fichero: " + nombre);
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "Fichero no encontrado: " + nombre, e);
            System.err.println("Error: El archivo no existe.");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error de E/S al leer: " + nombre, e);
            System.err.println("Error al leer el archivo.");
        }
    }
}