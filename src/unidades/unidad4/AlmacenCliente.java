package unidades.unidad4;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class AlmacenCliente {
	private static final String HOST = "localhost";
	private static final int PUERTO = 12345;
	
	public static void main(String[] args) {

        //Defino una lista de pedidos automática
		Random r = new Random();
		String[] productos = {"producto1", "producto2", "producto3", "producto4", "producto5"};

        System.out.println("Cliente - Almacén iniciando...");

        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor de pedidos.");
            
            for (int i = 0; i < 10; i++) {
            	String producto = productos[r.nextInt(productos.length)];
            	int cantidad = r.nextInt(20) + 1;
            	
            	String pedido = producto + "," + cantidad;
            	System.out.println("Enviando: " + pedido);
            	
            	salida.println(pedido); // Envio al servidor
            	
            	//Lee la respuesta del servidor
            	String respuesta = entrada.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);

                Thread.sleep(1000); 
            }

            System.out.println("Simulación finalizada. Cerrando conexión.");

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}