public class Token {
    private final TokenType tipo;
    private final String lexema;
    private final int linea;

    public Token(TokenType tipo, String lexema, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
    }

    public TokenType obtenerTipo() {
        return tipo;
    }

    public String obtenerLexema() {
        return lexema;
    }

    public int obtenerLinea() {
        return linea;
    }

    @Override
    public String toString() {
        return "Token: " + tipo + " \"" + lexema + "\"";
    }
}
