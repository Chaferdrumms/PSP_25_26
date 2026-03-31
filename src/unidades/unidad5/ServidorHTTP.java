package unidades.unidad5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorHTTP {
	private static final int PUERTO = 8066;

	public static void main(String[] args) {
		try (ServerSocket servidor = new ServerSocket(PUERTO)) { 
            System.out.println("Servidor HTTP escuchando en el puerto " + PUERTO); 

            while (true) { 
            	Socket cliente = servidor.accept(); 
            	System.out.println("Cliente conectado desde: " + cliente.getInetAddress()); 
            
            	//Lanzo un hilo nuevo para cada petición 
            	new Thread(new ManejarPeticiones(cliente)).start();
            }
		} catch (IOException e) {
			System.err.println("Error en el servidor: " + e.getMessage());
		}
	}
}