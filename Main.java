import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Main <archivo.c>");
            return;
        }

        String fuente;
        try {
            fuente = Files.readString(Path.of(args[0]));
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo: " + args[0]);
            return;
        }

        Lexer lexer = new Lexer(fuente);
        int errores = 0;

        while (true) {
            Token tok = lexer.siguienteToken();
            if (tok.obtenerTipo() == TokenType.EOF) {
                break;
            }
            if (tok.obtenerTipo() == TokenType.ERROR) {
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
