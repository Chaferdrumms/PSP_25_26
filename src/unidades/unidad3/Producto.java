package unidades.unidad3;

public class Producto {
	private String codigo;
    private String nombre;
    private int stock;
    private double precio;

    public Producto(String codigo, String nombre, int stock, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }

    //Getters 
    public String getCodigo() { 
    	return codigo; 
    	}
    public String getNombre() {
    	return nombre; 
    	}
    public int getStock() { 
    	return stock; 
    	}
    public double getPrecio() { 
    	return precio; 
    	}

    @Override
    public String toString() {
    	return "Código: " + codigo + " | Nombre: " + nombre + " | Stock: " + stock + " | Precio: " + precio + "€";
    }
}
