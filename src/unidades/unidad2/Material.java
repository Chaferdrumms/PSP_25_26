package unidades.unidad2;

public enum Material {
	PLA(1800), PETG(2200), ABS(2600);
	
	private final int tiempo;
	
	Material (int tiempo){
		this.tiempo = tiempo;
	}
	
	public int getTiempo() {
		return tiempo;
	}
}