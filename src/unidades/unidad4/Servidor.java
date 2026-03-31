package unidades.unidad4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;


public class Servidor {
	
	private static final int PUERTO = 12345;
	private static final String CONFIG_STOCK = "config_stock.properties";
	private static HashMap<String, Integer> stock = new HashMap<>();
	private static final String LOG = "pedidos.log";
	
	public static void main(String[] args) {
		//Cargo los datos del properties
	    cargarStock();
	    
	    try(ServerSocket servidor = new ServerSocket(PUERTO)) {
	    	System.out.println("Servidor iniciado en el puerto " + PUERTO + "...");
	    	
	    	while(true) {
	    		//Con accept() bloqueo el programa hasta que un cliente se conecta
	            Socket sCliente = servidor.accept(); 
	            //Con getInetAddress obtengo la IP del cliente que se conecta
	            System.out.println("Almacén conectado: " + sCliente.getInetAddress());
	            
	            //Creo un hilo para el cliente
	            new Thread (new ManejarPedidos(sCliente)).start();
	    	}
	    	
	    } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
	    
	}
	
	//Metodo para guardar cambios en el archivo
	private static void actualizarStock() {
		Properties prop = new Properties();
		// Bucle para pasar del HashMap al objeto Properties
		for (String clave : stock.keySet()) {
			prop.setProperty(clave, String.valueOf(stock.get(clave)));
		}
		try(FileOutputStream fos = new FileOutputStream(CONFIG_STOCK)){
			prop.store(fos, "Actualización de stock");
		} catch (IOException e) {
            System.err.println("Error guardando el stock en archivo: " + e.getMessage());
        }
	}
	
	//Metodo para los logs
	private static void escribirLog(String ip, String producto, int cantidad, String estado) {
		String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMensaje = String.format("[%s] PEDIDO %s - Almacén: %s - Producto: %s - Cantidad: %d",
                fecha, estado, ip, producto, cantidad);

		try(FileWriter fw = new FileWriter (LOG, true);
		PrintWriter pw = new PrintWriter (fw)) {
			pw.println(logMensaje);
		} catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }			
	}
	
	//Método que solo valida si los datos tienen sentido
	private static boolean validarPedido(String producto, int cantidad) {
	    //El producto no puede ser nulo/vacío y la cantidad debe ser positiva 
	    return producto != null && !producto.trim().isEmpty() && cantidad > 0;
	}

	//Método principal coordinado (Sincronizado)
	public static synchronized boolean procesarPedido(String producto, int cantidad, String ip) {
	    //Valido la entrada
	    if (!validarPedido(producto, cantidad)) {
	        escribirLog(ip, producto, cantidad, "RECHAZADO (Datos inválidos)");
	        return false;
	    }

	    //Compruebo existencia
	    if (!stock.containsKey(producto)) {
	        escribirLog(ip, producto, cantidad, "RECHAZADO (Producto no existe)"); 
	        return false;
	    }

	    //Delego la modificación del stock 
	    return ejecutarTransaccion(producto, cantidad, ip);
	}

	//Método que solo toca el stock y los archivos
	private static boolean ejecutarTransaccion(String producto, int cantidad, String ip) {
	    int stockActual = stock.get(producto);

	    if (stockActual >= cantidad) {
	        stock.put(producto, stockActual - cantidad); 
	        actualizarStock(); 
	        escribirLog(ip, producto, cantidad, "ACEPTADO");
	        return true;
	    } else {
	        escribirLog(ip, producto, cantidad, "RECHAZADO (Stock insuficiente)"); 
	        return false;
	    }
	}
	
	//Metodo para cargar los datos del archivo en un HashMap
    private static void cargarStock() {
        System.out.println("Cargando archivo config_stock.properties...");
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_STOCK)) {
            prop.load(fis);
            for (String producto : prop.stringPropertyNames()) {
            	stock.put(producto, Integer.parseInt(prop.getProperty(producto)));
            }
            System.out.println("Productos cargados: " + stock.size());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error cargando el stock: " + e.getMessage());
        }
    }
}

