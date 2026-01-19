package unidades.unidad2;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FabLab {
	public static void main(String[] args) {
		
		//Configuro el número de impresoras y clientes
		int numImpresoras = 3;
		int numClientes = 10; 
		
		//Limito a 3 hilos concurrentes a través del número de impresoras
		Semaphore semaforo = new Semaphore(numImpresoras);
		
		//Objeto random para asignar los materiales
		Random random = new Random();
		
		//Creo un objeto de logs para sacar los registros
		RegistrosLogs rl = new RegistrosLogs();
				
		//Creo las impresoras
		Impresora[] impresoras = new Impresora[numImpresoras];
		for (int i = 0; i < numImpresoras; i++) {
			impresoras[i] = new Impresora (i + 1);
		}
				
		//Creo una lista para guardar los hilos
		List<Thread> hilosClientes = new ArrayList<>();
				
		rl.log("=== INICIO DEL FABLAB AUTOMATIZADO ===");
				
		//Llegan los clientes
		for(int i = 1; i <= numClientes; i++) {
			String idCliente = "CLI-" + String.format("%03d", i);
			//Elegir material: Uso el objeto random para asignar un material al azar
			Material mat = Material.values()[random.nextInt(Material.values().length)];
					
			//Creo el hilo del cliente
			Cliente cliente = new Cliente (idCliente, mat, semaforo, impresoras, rl);
			Thread hilo = new Thread(cliente);
					
			//Guardo el hilo en la lista antes de lanzarlo
			hilosClientes.add(hilo);
			hilo.start();
		}
				
		//Espero a que todos los hilos terminen con join()
		for (Thread t : hilosClientes) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
		//Resumen final, con join me he asegurado que todos los hilos terminen, por lo que los datos son reales
		rl.log("=== Resumen final de trabajos realizados ===");
		for (Impresora imp : impresoras) {
			imp.mostrarResumen(rl);
		}
		rl.log("=== Cierre del FabLab ===");
	}
}