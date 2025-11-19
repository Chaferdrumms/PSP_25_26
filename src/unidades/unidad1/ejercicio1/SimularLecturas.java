package unidades.unidad1.ejercicio1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimularLecturas {
	
	public static void main(String[] args) {
		
		//Uso un array con 5 sensores para las lecturas
		String[] sensorIds = {"S1", "S2", "S3", "S4", "S5"};
		
		//Formateo de fecha
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		/*Para que la fecha no sea siempre la misma, he decidido hacer que salga una fecha aleatoria entre los 
		últimos 30 días.*/
		LocalDateTime ldt = LocalDateTime.now();
		// Restar 30 días
        LocalDateTime min = ldt.minusDays(30);
        
        //Convierto en long las fechas de inicio y fin para poder generarlas aleatoriamente.
        long inicio = min.toEpochSecond(java.time.ZoneOffset.UTC);
        long fin = ldt.toEpochSecond(java.time.ZoneOffset.UTC);

        //Variable valor
		double valor;
		
		//Creo un objeto de la clase Random para hacer aleatorias las salidas de los sensores y valores.
		Random r = new Random();
		
		//Formateo la salida de decimales para que salga el punto y no la coma.
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.'); // <-- Fuerza el punto
        DecimalFormat df = new DecimalFormat("#.00", symbols);	
		
		//Bucle para generar las 50 lecturas.
		for (int i = 0; i < 50; i++) {
			//Uso los longs de las fechas anteriores para generar un aleatorio en ese rango
			long fechaRandom = ThreadLocalRandom.current().nextLong(inicio, fin);
			//Convierto el long en formato fecha
			LocalDateTime random = LocalDateTime.ofEpochSecond(fechaRandom, 0, ZoneOffset.UTC);
			//Formateo la fecha
			String timestamp = random.format(dtf);
			//Genero un valor aleatorio
			valor = -30.0 + r.nextDouble() * 100.0; // Genera desde -30.0 a 69.99 (rango de 100)

			System.out.println(sensorIds[r.nextInt(sensorIds.length)] + ";" + timestamp + ";" + df.format(valor));
		}
	}
}