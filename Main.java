import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Punto de entrada del programa. Lee un archivo de codigo C (simplificado)
 * pasado como argumento y usa el Lexer para imprimir la lista de tokens
 * en la salida estandar, con el formato pedido en el enunciado:
 *   Token: TIPO "lexema"
 *
 * Uso:
 *   java Main archivo.c
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Main <archivo.c>");
            return;
        }

        String source;
        try {
            source = Files.readString(Path.of(args[0]));
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo: " + args[0]);
            return;
        }

        Lexer lexer = new Lexer(source);
        int errores = 0;

        while (true) {
            Token tok = lexer.nextToken();
            if (tok.getType() == TokenType.EOF) {
                break;
            }
            if (tok.getType() == TokenType.ERROR) {
                System.err.println("Error lexico en linea " + tok.getLine()
                        + ": simbolo no reconocido \"" + tok.getLexeme() + "\"");
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
