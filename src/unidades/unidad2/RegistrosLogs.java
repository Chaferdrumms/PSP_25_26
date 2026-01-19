package unidades.unidad2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistrosLogs {
	// Formato de fecha y hora para el log
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    // El método es synchronized para que los mensajes no se mezclen en la consola
    public synchronized void log(String mensaje) {
        String horaActual = LocalDateTime.now().format(dtf);
        System.out.println("[" + horaActual + "] " + mensaje);
    }
}