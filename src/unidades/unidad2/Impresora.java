package unidades.unidad2;

import java.util.HashMap;

public class Impresora {
	
	private final int id;
	
	//Para saber si la impresora está en uso
	private boolean ocupada = false; 
	
	//Mapa para guardar el resumen con la cuenta de trabajos por material 
	private final HashMap<Material, Integer> resumen = new HashMap<>();
	
	public Impresora(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	//Método sincronizado para ir anotando el trabajo hecho
	public synchronized void registrarTrabajo(Material material) {
		resumen.put(material, resumen.getOrDefault(material, 0) + 1);
	}
	
	//Método para imprimir el resumen final del programa
	public void mostrarResumen(RegistrosLogs registrosLogs) {
		registrosLogs.log("Impresora-" + id + " resumen: " + resumen);
	}
	
	public boolean getOcupada() { 
		return ocupada; 
	}
    
	public void setOcupada(boolean ocupada) { 
		this.ocupada = ocupada; 
	}
}