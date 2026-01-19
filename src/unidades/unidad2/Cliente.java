package unidades.unidad2;

import java.util.concurrent.Semaphore;

public class Cliente implements Runnable {

	private String id;
	private Material material;
	private Semaphore semaforo;
	private Impresora[] impresoras;
private RegistrosLogs registroLog;
	
	
	public Cliente (String id, Material material, Semaphore semaforo, Impresora[] impresoras, RegistrosLogs registroLog) {
		this.id = id;
		this.material = material;
		this.semaforo = semaforo;
		this.impresoras = impresoras;
		this.registroLog = registroLog;
	}
	
	@Override
	public void run() {
		
		//Log de llegada 
		registroLog.log("[COLA] El cliente " + id + " está esperando para imprimir " + material + ".");
		
		try {
			//Tomar permiso (Aquí se quedan bloqueados si hay 3 imprimiendo)
			semaforo.acquire(); 
			
			Impresora impresora = null;
			
			//Uso synchronized para que solo un hilo busque en el array al buscar una impresora libre
			synchronized (impresoras) {
	            for (Impresora imp : impresoras) {
	                if (!imp.getOcupada()) {
	                    imp.setOcupada(true); 
	                    impresora = imp;
	                    break; 
	                }
	            }
	        }
			
			//Log de asignación - Imprimir 
			if (impresora != null) {
				registroLog.log("[ASIGNACIÓN] El cliente " + id + " ha sido asignado a IMPRESORA-" + impresora.getId());
				
				//Le asigno directamente el tiempo que requiere cada material
				Thread.sleep(material.getTiempo()); 
				
				//Agrego al registro final la impresión actual
				impresora.registrarTrabajo(material);		
				
				//Log final
				registroLog.log("[FINALIZADO] IMPRESORA-" + impresora.getId() + " ha terminado su pieza de " + material + " para el cliente " + id);
				
				//Libero la impresora
	            synchronized (impresoras) {
	                impresora.setOcupada(false);
	            }
			}
		} catch (InterruptedException e) {
			System.err.println("El cliente " + id + " fue interrumpido.");
		}finally {
			semaforo.release();
		}
	}
}