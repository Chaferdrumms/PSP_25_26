package unidades.unidad4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejarPedidos implements Runnable {
	private Socket socket;
	
	public ManejarPedidos(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		//Flujos de entrada y salida
        try(BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {
        	
        	String linea;
        	while((linea = entrada.readLine()) != null) {
        		String[] datos = linea.split(",");
                if (datos.length != 2) {
                	System.err.println("Formato inválido. Use: producto,cantidad");
                    continue;
                }
                String producto = datos[0].trim();
                int cantidad;
                try {
                    cantidad = Integer.parseInt(datos[1].trim());
                } catch (NumberFormatException e) {
                	System.err.println("Cantidad inválida.");
                    continue;
                }

        		boolean exito = Servidor.procesarPedido(producto, cantidad, socket.getInetAddress().toString());
        		if (exito) {
        			salida.println("Pedido aceptado: Producto: " + producto + " - Cantidad: " + cantidad);
        		} else {
        			salida.println("Pedido rechazado: Stock insuficiente.");
        		}
        	}
        	socket.close();
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
	}
}