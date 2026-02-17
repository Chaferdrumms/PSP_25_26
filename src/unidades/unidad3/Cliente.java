package unidades.unidad3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		//Intentar conectarse al servidor (IP + Puerto)
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner teclado = new Scanner(System.in)) {

            System.out.println("Conectado al servidor en 127.0.0.1:5000...");
            String opcion = "";
            
            //Menú
            while (!opcion.equals("5")) {
                imprimirMenu();
                opcion = teclado.nextLine();
                String comando = "";

                switch (opcion) {
                    case "1": 
                    	comando = "LISTA"; 
                    	break;
                    case "2": 
                        System.out.print("Introduzca código: ");
                        comando = "STOCK " + teclado.nextLine();
                        break;
                    case "3":
                        System.out.print("Introduzca código: ");
                        comando = "PRECIO " + teclado.nextLine();
                        break;
                    case "4":
                        System.out.print("Introduzca código: ");
                        comando = "INFO " + teclado.nextLine();
                        break;
                    case "5": 
                    	comando = "FIN"; 
                    	break;
                    default: 
                    	System.out.println("Opción no válida."); 
                    	continue;
                }
                
                //Envio el comando al servidor
                salida.println(comando); 
                
                //Leo la respuesta del servidor
                String respuesta = entrada.readLine();
                System.out.println("<< Respuesta del servidor:\n" + respuesta.replace("##", "\n"));
                
                if (comando.equals("FIN")) break;
            }

        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        }
    }

    private static void imprimirMenu() {
        System.out.println("------------------------------------");
        System.out.println("   MENÚ DE CONSULTA DE PRODUCTOS");
        System.out.println("------------------------------------");
        System.out.println("1. Ver lista de productos");
        System.out.println("2. Consultar stock por código");
        System.out.println("3. Consultar precio por código");
        System.out.println("4. Información completa de un producto");
        System.out.println("5. Salir");
        System.out.println("------------------------------------");
        System.out.print("Seleccione una opción: ");
    }
}
