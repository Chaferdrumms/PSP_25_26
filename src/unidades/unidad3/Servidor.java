package unidades.unidad3;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Servidor {
	//Defino el puerto para que el cliente sepa dónde llamar (siempre será el mismo)
	private static final int PUERTO = 5000;
	
	//El inventario es static para que sea accesible desde el main directamente
    private static HashMap<String, Producto> inventario = new HashMap<>();

    public static void main(String[] args) {
    	//Cargo datos del csv
        cargarProductos();
          
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO + "...");
            
            //Con accept() bloqueo el programa hasta que un cliente se conecta
            Socket sCliente = servidor.accept(); 
            //Con getInetAddress obtengo la IP del cliente que se conecta
            System.out.println("Cliente conectado desde: " + sCliente.getInetAddress());  

            //Flujos de entrada y salida
            try(BufferedReader entrada = new BufferedReader(new InputStreamReader(sCliente.getInputStream()));
            PrintWriter salida = new PrintWriter(sCliente.getOutputStream(), true)) { //true = auto-envío

	            String lineaRecibida;
	
	        	//Bucle para procesar comandos hasta que el cliente diga "FIN"
	            while ((lineaRecibida = entrada.readLine()) != null) {
	                System.out.println("[COMANDO] " + lineaRecibida);
	                
	                //Proceso lógica para obtener la respuesta
	                String respuesta = procesarComando(lineaRecibida);              
	                salida.println(respuesta); // Envio la respuesta al cliente
	
	                if (lineaRecibida.equalsIgnoreCase("FIN")) break;
	            }
            }    
            
            //El socket del cliente se cierrra automaticamente al terminar el try-with-resources
            System.out.println("Conexión con el cliente cerrada.");
            System.out.println("Servidor finalizado.");

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void cargarProductos() {
        System.out.println("Cargando archivo productos.csv...");
        try (BufferedReader br = new BufferedReader(new FileReader("unidades/unidad3/productos.csv"))) {
            br.readLine(); // Saltar cabecera
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                inventario.put(datos[0], new Producto(datos[0], datos[1], 
                               Integer.parseInt(datos[2]), Double.parseDouble(datos[3])));
            }
            System.out.println("Productos cargados: " + inventario.size());
        } catch (IOException e) {
            System.err.println("Error al cargar CSV: " + e.getMessage());
        }
    }

    private static String procesarComando(String peticion) {
    	//Divido el texto donde haya un espacio con split
        String[] partes = peticion.split(" "); 
        String comando = partes[0].toUpperCase();

        if (comando.equals("FIN")) return "Conexión finalizada. Gracias.";
        if (comando.equals("LISTA")) {
            StringBuilder sb = new StringBuilder();
            for (Producto p : inventario.values()) {
                sb.append(p.toString()).append("##"); // Añadimos cada producto seguido de ##
            }
            return sb.toString(); //Envío la lista
        }
        
        //Necesitamos si o si el código
        if (partes.length < 2) return "ERROR: Falta el código del producto.";
        
        String codigo = partes[1].toUpperCase();
        Producto p = inventario.get(codigo);
        
        if (p == null) return "ERROR: El producto " + codigo + " no existe.";
        
        //Devuelvo la información segú el comando solicitado
        switch (comando) {
            case "STOCK": 
            	return "Stock disponible de " + codigo + ": " + p.getStock() + " unidades.";
            case "PRECIO": 
            	return "Precio de " + codigo + ": " + p.getPrecio() + "€";
            case "INFO": 
            	String info = "Código: " + p.getCodigo() + "##Nombre: " + p.getNombre() + "##Stock: " + p.getStock() + "##Precio: " + p.getPrecio() + "€";
            	return info;
            default: 
            	return "ERROR: Comando no reconocido.";
        }
    }
}
