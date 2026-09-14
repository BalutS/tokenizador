import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] argumentos) {
        if (argumentos.length < 1) {
            System.out.println("Uso: java Main <archivo.c>");
            return;
        }

        String fuente;
        try {
            fuente = Files.readString(Path.of(argumentos[0]));
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo: " + argumentos[0]);
            return;
        }

        Lexer lexico = new Lexer(fuente);
        int errores = 0;

        while (true) {
            Token tok = lexico.siguienteToken();
            if (tok.obtenerTipo() == TipoToken.FIN_ARCHIVO) {
                break;
            }
            if (tok.obtenerTipo() == TipoToken.ERROR) {
                System.err.println("Error lexico en linea " + tok.obtenerLinea()
                        + ": simbolo no reconocido \"" + tok.obtenerLexema() + "\"");
                errores++;
                continue;
            }
            System.out.println(tok);
        }

        if (errores > 0) {
            System.err.println("Total de errores lexicos: " + errores);
        }
    }
}
