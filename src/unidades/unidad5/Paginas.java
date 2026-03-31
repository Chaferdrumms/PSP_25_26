package unidades.unidad5;

import java.util.Random;

public class Paginas {
	public static final String BIENVENIDA = """
		    <html>
                <head>
                    <title>Servidor de Curiosidades</title>
                </head>
                <body>
                    <h1>Bienvenidos al Servidor de Curiosidades</h1>
                    <p>Selecciona una opción para navegar:</p>
                    <ul>
                        <li><a href="/curiosidad">Ver una curiosidad aleatoria</a></li>
                        <li><a href="/contacto">Información de contacto</a></li>
                        <li><a href="/noexiste">Probar un enlace roto (Error 404)</a></li>
                    </ul>
                </body>
            </html>
	    """;
	public static final String CONTACTO = "<html><body><h1>Contacto</h1><p>Email: info@curiosidades.com</p></body></html>"; 
	public static final String ERROR_404 = "<html><body><h1>404 - Página no encontrada</h1></body></html>"; 

	public static String curiosidadAleatoria() { 
		String[] datos = {
				"<html><body><h1>¿Sabías que...?</h1><p>Las abejas pueden ver el color ultravioleta.</p></body></html>",
		        "<html><body><h1>¿Sabías que...?</h1><p>El pulpo tiene tres corazones.</p></body></html>",
		        "<html><body><h1>¿Sabías que...?</h1><p>Java nació originalmente para televisores interactivos.</p></body></html>",
		        "<html><body><h1>¿Sabías que...?</h1><p>Aproximadamente el 80% del océano aún no ha sido explorado ni cartografiado.</p></body></html>",
		        "<html><body><h1>¿Sabías que...?</h1><p>La sangre de las arañas es azul.</p></body></html>",
		        "<html><body><h1>¿Sabías que...?</h1><p>Las hormigas no duermen de corrido, sino que toman siestas de pocos minutos varias veces al día.</p></body></html>"
		};
		return datos[new Random().nextInt(datos.length)]; 
	}
}