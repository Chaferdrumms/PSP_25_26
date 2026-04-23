package unidades.unidad6;

import java.io.IOException;
import java.util.logging.*;


public class GestorLogs {
	
	private static final Logger logger = Logger.getLogger("SeguridadLog");

    public static void inicializar() {
        try {
            //Configuro el archivo de salida (append = true)
            FileHandler fh = new FileHandler("actividad.log", true);
            fh.setFormatter(new SimpleFormatter()); //Formato texto plano 
            logger.addHandler(fh);
            
            //Evito que los logs salgan duplicados por la consola
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL); 
            
        } catch (IOException e) {
            System.err.println("Error crítico: No se pudo crear el archivo de log.");
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}