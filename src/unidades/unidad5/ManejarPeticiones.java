package unidades.unidad5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejarPeticiones implements Runnable{
	private Socket socket;

    public ManejarPeticiones(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            //Leo solo la primera línea de la petición GET 
            String peticion = entrada.readLine();
            if (peticion == null) return;

            System.out.println("Petición: " + peticion);
            
            //Obtengo la ruta 
            String[] partes = peticion.split(" ");
            String ruta;
            if (partes.length > 1) {
                //Si el array tiene más de un elemento, obtengo lo que hay tras el "GET"
                ruta = partes[1]; 
            } else {
                //Si por algún motivo el array es corto, asigno la raíz por defecto
                ruta = "/"; 
            }
            enviarRespuesta(ruta, salida);

        } catch (IOException e) {
            System.err.println("Error procesando petición: " + e.getMessage());
        } finally {
            try {
                socket.close(); //Cierro socket
            } catch (IOException e) {
                System.err.println("Error al cerrar socket: " + e.getMessage());
            }
        }
    }

    //Método enviarRespuesta
    private void enviarRespuesta(String ruta, PrintWriter salida) {
        switch (ruta) {
            case "/":
                salida.print(Mensajes.OK + Paginas.BIENVENIDA); 
                break;
            case "/curiosidad":
                salida.print(Mensajes.OK + Paginas.curiosidadAleatoria()); 
                break;
            case "/contacto":
                salida.print(Mensajes.OK + Paginas.CONTACTO); 
                break;
            default:
                salida.print(Mensajes.NOT_FOUND + Paginas.ERROR_404); 
                System.out.println("Respuesta enviada: 404 Not Found"); 
                break;
        }
        salida.flush(); //Limpio el bufer
    }
}