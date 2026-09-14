public class Token {
    private final TipoToken tipo;
    private final String lexema;
    private final int linea;

    public Token(TipoToken tipo, String lexema, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
    }

    public TipoToken obtenerTipo() {
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
