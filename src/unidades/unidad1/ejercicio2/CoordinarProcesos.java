package unidades.unidad1.ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CoordinarProcesos {
	
	public static void main(String[] args) {
		
		//Compruebo los argumentos mínimos
        if (args.length < 3) {
            System.err.println("Uso correcto: java CoordinarProcesos <entrada> <N_Procesos> <salida> [--minAlert=VALOR]");
            return; //Salir del programa
        }
        
        //Guardo los argumentos obligatorios
        String ficheroEntrada = args[0];
        String ficheroSalida = args[2];
        
        //Para el número de procesos, creo una variable integer
        int numProcesos = 0; 
        
        //Uso un try/catch para parsear N_Procesos (que es un String), a la variable int creada
        try {
        	numProcesos = Integer.parseInt(args[1]);
        	
        } catch (NumberFormatException e) {
        	System.err.println("ADVERTENCIA: Segundo argumento desconocido. Debe introducir un número entero.");
            return; //Salir del programa
        }

        //Manejo el argumento opcional (--minAlert)
        String umbralOpcional = null; // Por defecto no hay
        if (args.length == 4) {
            umbralOpcional = args[3];
        }
        
     // Creo una lista donde guardar todas las líneas del proceso que se van leyendo
        List<String> lineas = new ArrayList<>();
        
        //Abro/leo fichero de ENTRADA
        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada))) {
            String linea; 
            while ((linea = br.readLine()) != null) {
                lineas.add(linea); // <-- Solo se lee y se guarda
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero de entrada: " + e.getMessage());
            return; //Si no se puede leer, no podemos continuar
        }
        
        //Calculo el total de lineas que se han guardado
        int totalLineas = lineas.size();
        System.out.println("Fichero de entrada leído. Total líneas: " + totalLineas);
        
        if (totalLineas == 0) {
            System.err.println("El fichero de entrada está vacío. No hay nada que procesar.");
            return; //Si no hay líneas, no se puede continuar
        }
        
        //Calculo la cantidad de líneas que debe llevar cada proceso
        int lineasPorProceso = totalLineas / numProcesos;
        
        //Hago un bucle 'for' para crear los N trozos y repartirlos
        for (int i = 0; i < numProcesos; i++) {
            String nombreTramo = "tramo_" + i + ".csv";
            
            //Calculo los índices del ArrayList
            int indiceInicio = i * lineasPorProceso;
            
            //El último tramo coge hasta el final
            int indiceFin;
            //Si es el último tramo
            if (i == numProcesos - 1) {
                indiceFin = totalLineas;
            } else {
                //Si no es el último tramo
                indiceFin = (i + 1) * lineasPorProceso;
            }

            //Abro/escribo un fichero de SALIDA
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(nombreTramo)))) {      
            	
                //Recorro cada tramo del ArrayList que he separado anteriormente en índices.
                for (int j = indiceInicio; j < indiceFin; j++) {
                    pw.println(lineas.get(j));
                }
                
            } catch (IOException e) {
                System.err.println("Error al escribir el tramo " + nombreTramo + ": " + e.getMessage());
            }
        }
        
        System.out.println("Reparto de tareas completado. " + numProcesos + " ficheros de tramo creados.");
        
        //Creo una lista para guardar una referencia de cada proceso lanzado
        List<Process> procesosLanzados = new ArrayList<>();
        
        //Bucle para lanzar los procesos
        for (int k = 0; k < numProcesos; k++) {
        	
        	//Creo un array donde construyo paso a paso el comando que luego se lanzará 
        	ArrayList<String> tramos = new ArrayList<>();
        	tramos.add("java"); //El comando base
        	tramos.add("unidades.unidad1.ejercicio2.TransformarLecturas"); //La clase a ejecutar
        	tramos.add("tramo_" + k + ".csv"); //La entrada que le toca a cada tramo
        	tramos.add("salida_parcial_" + k + ".csv"); //La salida que le toca a cada tramo
        	
        	//En caso de que haya un --minAlert
        	if (umbralOpcional != null) {
        		tramos.add(umbralOpcional);
        	}
        	
        	try {
        		//Clase para manejar los procesos
        		ProcessBuilder pb = new ProcessBuilder(tramos);
        		Process proceso = pb.start(); //Se lanza cada proceso y se ejecuta en paralelo al programa
        		procesosLanzados.add(proceso); //Guardo cada proceso en la lista creada anteriormente
        		
        	} catch (IOException e) { 
        		e.printStackTrace(); 
        	}		
        }
        System.out.println("INFO: " + numProcesos + " procesos lanzados. Esperando a que terminen.");
        
        try {
        	//Recorro la lista de procesos uno a uno
        	for (Process p : procesosLanzados) {
        		//Bloqueo la ejecución de los procesos lanzados hasta que hayan acabado todos
        		p.waitFor();	
        	} 
        }catch (InterruptedException e) {
    		e.printStackTrace(); 
    	}
        
        //Una vez que han terminado todos los procesos
        System.out.println("INFO: Todos los procesos han terminado.");

        //Abro el fichero de salida final
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ficheroSalida)))) {
            
            //Recorro cada fichero parcial
            for (int l = 0; l < numProcesos; l++) {
                String ficheroParcial = "salida_parcial_" + l + ".csv";
                
                //Abro el fichero parcial para leerlo
                try (BufferedReader br = new BufferedReader(new FileReader(ficheroParcial))) {
                    
                    String linea;
                    //Vuelco cada línea del fichero parcial en el fichero final
                    while ((linea = br.readLine()) != null) {
                        pw.println(linea);
                    }                  
                } catch (IOException e) {
                    System.err.println("Error al leer el fichero parcial " + ficheroParcial);
                }             
            }
            
        } catch (IOException e) {
            System.err.println("Error FATAL al escribir el fichero de salida final.");
            e.printStackTrace();
        }
        //Final
        System.out.println("¡TAREA COMPLETADA! Fichero final '" + ficheroSalida + "' creado.");
	}
}
