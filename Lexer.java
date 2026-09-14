import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private static final int CLS_LETRA = 0;
    private static final int CLS_DIGITO = 1;
    private static final int CLS_COMILLA = 2;
    private static final int CLS_COMILLA_SIMPLE = 3;
    private static final int CLS_LLAVE_IZQ = 4;
    private static final int CLS_LLAVE_DER = 5;
    private static final int CLS_CORCHETE_IZQ = 6;
    private static final int CLS_CORCHETE_DER = 7;
    private static final int CLS_PAR_IZQ = 8;
    private static final int CLS_PAR_DER = 9;
    private static final int CLS_PUNTO_Y_COMA = 10;
    private static final int CLS_COMA = 11;
    private static final int CLS_PUNTO = 12;
    private static final int CLS_MAS = 13;
    private static final int CLS_MENOS = 14;
    private static final int CLS_ASTERISCO = 15;
    private static final int CLS_BARRA = 16;
    private static final int CLS_AMPERSAND = 17;
    private static final int CLS_PLECA = 18;
    private static final int CLS_CIRCUNFLEJO = 19;
    private static final int CLS_TILDE = 20;
    private static final int CLS_EXCLAMACION = 21;
    private static final int CLS_IGUAL = 22;
    private static final int CLS_MENOR = 23;
    private static final int CLS_MAYOR = 24;
    private static final int CLS_BARRA_INVERSA = 25;
    private static final int CLS_OTRO = 26;
    private static final int NUM_CLASES = 27;

    private static final int ESTADO_MUERTO = -1;
    private static final int ESTADO_INICIAL = 0;
    private static final int ESTADO_ID = 1;
    private static final int ESTADO_NUM = 2;
    private static final int CUERPO_CADENA = 3;
    private static final int ESCAPE_CADENA = 4;
    private static final int FIN_CADENA = 5;
    private static final int CUERPO_CARACTER = 6;
    private static final int ESCAPE_CARACTER = 7;
    private static final int CIERRE_PENDIENTE_CARACTER = 8;
    private static final int FIN_CARACTER = 9;
    private static final int FIN_LLAVE_IZQ = 10;
    private static final int FIN_LLAVE_DER = 11;
    private static final int FIN_CORCHETE_IZQ = 12;
    private static final int FIN_CORCHETE_DER = 13;
    private static final int FIN_PAR_IZQ = 14;
    private static final int FIN_PAR_DER = 15;
    private static final int FIN_PUNTO_Y_COMA = 16;
    private static final int FIN_COMA = 17;
    private static final int FIN_PUNTO = 18;
    private static final int FIN_MAS = 19;
    private static final int FIN_MULTIPLICACION = 20;
    private static final int FIN_DIVISION = 21;
    private static final int FIN_CIRCUNFLEJO = 22;
    private static final int FIN_TILDE = 23;
    private static final int MENOS1 = 24;
    private static final int FIN_FLECHA = 25;
    private static final int AMPERSAND1 = 26;
    private static final int FIN_ANDAND = 27;
    private static final int PLECA1 = 28;
    private static final int FIN_OROR = 29;
    private static final int EXCLAMACION1 = 30;
    private static final int FIN_DIFERENTE = 31;
    private static final int IGUAL1 = 32;
    private static final int FIN_IGUALIGUAL = 33;
    private static final int MENOR1 = 34;
    private static final int FIN_MENORIGUAL = 35;
    private static final int FIN_DESPLAZAMIENTO_IZQ = 36;
    private static final int MAYOR1 = 37;
    private static final int FIN_MAYORIGUAL = 38;
    private static final int FIN_DESPLAZAMIENTO_DER = 39;
    private static final int NUM_ESTADOS = 40;

    private static final int[][] tabla = new int[NUM_ESTADOS][NUM_CLASES];
    private static final TokenType[] aceptacion = new TokenType[NUM_ESTADOS];

    static {
        construirTabla();
    }

    private static void construirTabla() {
        for (int i = 0; i < NUM_ESTADOS; i++) {
            for (int j = 0; j < NUM_CLASES; j++) {
                tabla[i][j] = ESTADO_MUERTO;
            }
        }

        tabla[ESTADO_INICIAL][CLS_LETRA] = ESTADO_ID;
        tabla[ESTADO_INICIAL][CLS_DIGITO] = ESTADO_NUM;
        tabla[ESTADO_INICIAL][CLS_COMILLA] = CUERPO_CADENA;
        tabla[ESTADO_INICIAL][CLS_COMILLA_SIMPLE] = CUERPO_CARACTER;
        tabla[ESTADO_INICIAL][CLS_LLAVE_IZQ] = FIN_LLAVE_IZQ;
        tabla[ESTADO_INICIAL][CLS_LLAVE_DER] = FIN_LLAVE_DER;
        tabla[ESTADO_INICIAL][CLS_CORCHETE_IZQ] = FIN_CORCHETE_IZQ;
        tabla[ESTADO_INICIAL][CLS_CORCHETE_DER] = FIN_CORCHETE_DER;
        tabla[ESTADO_INICIAL][CLS_PAR_IZQ] = FIN_PAR_IZQ;
        tabla[ESTADO_INICIAL][CLS_PAR_DER] = FIN_PAR_DER;
        tabla[ESTADO_INICIAL][CLS_PUNTO_Y_COMA] = FIN_PUNTO_Y_COMA;
        tabla[ESTADO_INICIAL][CLS_COMA] = FIN_COMA;
        tabla[ESTADO_INICIAL][CLS_PUNTO] = FIN_PUNTO;
        tabla[ESTADO_INICIAL][CLS_MAS] = FIN_MAS;
        tabla[ESTADO_INICIAL][CLS_MENOS] = MENOS1;
        tabla[ESTADO_INICIAL][CLS_ASTERISCO] = FIN_MULTIPLICACION;
        tabla[ESTADO_INICIAL][CLS_BARRA] = FIN_DIVISION;
        tabla[ESTADO_INICIAL][CLS_AMPERSAND] = AMPERSAND1;
        tabla[ESTADO_INICIAL][CLS_PLECA] = PLECA1;
        tabla[ESTADO_INICIAL][CLS_CIRCUNFLEJO] = FIN_CIRCUNFLEJO;
        tabla[ESTADO_INICIAL][CLS_TILDE] = FIN_TILDE;
        tabla[ESTADO_INICIAL][CLS_EXCLAMACION] = EXCLAMACION1;
        tabla[ESTADO_INICIAL][CLS_IGUAL] = IGUAL1;
        tabla[ESTADO_INICIAL][CLS_MENOR] = MENOR1;
        tabla[ESTADO_INICIAL][CLS_MAYOR] = MAYOR1;

        tabla[ESTADO_ID][CLS_LETRA] = ESTADO_ID;
        tabla[ESTADO_ID][CLS_DIGITO] = ESTADO_ID;
        aceptacion[ESTADO_ID] = TokenType.ID;

        tabla[ESTADO_NUM][CLS_DIGITO] = ESTADO_NUM;
        aceptacion[ESTADO_NUM] = TokenType.INT_NUM;

        for (int cls = 0; cls < NUM_CLASES; cls++) {
            tabla[CUERPO_CADENA][cls] = CUERPO_CADENA;
        }
        tabla[CUERPO_CADENA][CLS_COMILLA] = FIN_CADENA;
        tabla[CUERPO_CADENA][CLS_BARRA_INVERSA] = ESCAPE_CADENA;
        for (int cls = 0; cls < NUM_CLASES; cls++) {
            tabla[ESCAPE_CADENA][cls] = CUERPO_CADENA;
        }
        aceptacion[FIN_CADENA] = TokenType.STRING_LIT;

        for (int cls = 0; cls < NUM_CLASES; cls++) {
            tabla[CUERPO_CARACTER][cls] = CIERRE_PENDIENTE_CARACTER;
        }
        tabla[CUERPO_CARACTER][CLS_BARRA_INVERSA] = ESCAPE_CARACTER;
        tabla[CUERPO_CARACTER][CLS_COMILLA_SIMPLE] = ESTADO_MUERTO;
        for (int cls = 0; cls < NUM_CLASES; cls++) {
            tabla[ESCAPE_CARACTER][cls] = CIERRE_PENDIENTE_CARACTER;
        }
        tabla[CIERRE_PENDIENTE_CARACTER][CLS_COMILLA_SIMPLE] = FIN_CARACTER;
        aceptacion[FIN_CARACTER] = TokenType.CHAR_LIT;

        aceptacion[FIN_LLAVE_IZQ] = TokenType.LBRACE;
        aceptacion[FIN_LLAVE_DER] = TokenType.RBRACE;
        aceptacion[FIN_CORCHETE_IZQ] = TokenType.LSQUARE;
        aceptacion[FIN_CORCHETE_DER] = TokenType.RSQUARE;
        aceptacion[FIN_PAR_IZQ] = TokenType.LPAR;
        aceptacion[FIN_PAR_DER] = TokenType.RPAR;
        aceptacion[FIN_PUNTO_Y_COMA] = TokenType.SEMI;
        aceptacion[FIN_COMA] = TokenType.COMMA;
        aceptacion[FIN_PUNTO] = TokenType.DOT;
        aceptacion[FIN_MAS] = TokenType.PLUS;
        aceptacion[FIN_MULTIPLICACION] = TokenType.MUL_OP;
        aceptacion[FIN_DIVISION] = TokenType.DIV_OP;
        aceptacion[FIN_CIRCUNFLEJO] = TokenType.XOR_OP;
        aceptacion[FIN_TILDE] = TokenType.NOT_BIT;

        aceptacion[MENOS1] = TokenType.MINUS;
        tabla[MENOS1][CLS_MAYOR] = FIN_FLECHA;
        aceptacion[FIN_FLECHA] = TokenType.ARROW;

        aceptacion[AMPERSAND1] = TokenType.AND_OP;
        tabla[AMPERSAND1][CLS_AMPERSAND] = FIN_ANDAND;
        aceptacion[FIN_ANDAND] = TokenType.ANDAND;

        aceptacion[PLECA1] = TokenType.OR_OP;
        tabla[PLECA1][CLS_PLECA] = FIN_OROR;
        aceptacion[FIN_OROR] = TokenType.OROR;

        aceptacion[EXCLAMACION1] = TokenType.NOT_OP;
        tabla[EXCLAMACION1][CLS_IGUAL] = FIN_DIFERENTE;
        aceptacion[FIN_DIFERENTE] = TokenType.NOTEQ;

        aceptacion[IGUAL1] = TokenType.ASSIGN;
        tabla[IGUAL1][CLS_IGUAL] = FIN_IGUALIGUAL;
        aceptacion[FIN_IGUALIGUAL] = TokenType.EQ;

        aceptacion[MENOR1] = TokenType.LT;
        tabla[MENOR1][CLS_IGUAL] = FIN_MENORIGUAL;
        tabla[MENOR1][CLS_MENOR] = FIN_DESPLAZAMIENTO_IZQ;
        aceptacion[FIN_MENORIGUAL] = TokenType.LTEQ;
        aceptacion[FIN_DESPLAZAMIENTO_IZQ] = TokenType.SHL_OP;

        aceptacion[MAYOR1] = TokenType.GT;
        tabla[MAYOR1][CLS_IGUAL] = FIN_MAYORIGUAL;
        tabla[MAYOR1][CLS_MAYOR] = FIN_DESPLAZAMIENTO_DER;
        aceptacion[FIN_MAYORIGUAL] = TokenType.GTEQ;
        aceptacion[FIN_DESPLAZAMIENTO_DER] = TokenType.SHR_OP;
    }

    private static int clasificar(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
            return CLS_LETRA;
        }
        if (c >= '0' && c <= '9') {
            return CLS_DIGITO;
        }
        switch (c) {
            case '"': return CLS_COMILLA;
            case '\'': return CLS_COMILLA_SIMPLE;
            case '{': return CLS_LLAVE_IZQ;
            case '}': return CLS_LLAVE_DER;
            case '[': return CLS_CORCHETE_IZQ;
            case ']': return CLS_CORCHETE_DER;
            case '(': return CLS_PAR_IZQ;
            case ')': return CLS_PAR_DER;
            case ';': return CLS_PUNTO_Y_COMA;
            case ',': return CLS_COMA;
            case '.': return CLS_PUNTO;
            case '+': return CLS_MAS;
            case '-': return CLS_MENOS;
            case '*': return CLS_ASTERISCO;
            case '/': return CLS_BARRA;
            case '&': return CLS_AMPERSAND;
            case '|': return CLS_PLECA;
            case '^': return CLS_CIRCUNFLEJO;
            case '~': return CLS_TILDE;
            case '!': return CLS_EXCLAMACION;
            case '=': return CLS_IGUAL;
            case '<': return CLS_MENOR;
            case '>': return CLS_MAYOR;
            case '\\': return CLS_BARRA_INVERSA;
            default: return CLS_OTRO;
        }
    }

    private static final Map<String, TokenType> PALABRAS_RESERVADAS = new HashMap<>();
    static {
        PALABRAS_RESERVADAS.put("int", TokenType.INT);
        PALABRAS_RESERVADAS.put("main", TokenType.MAIN);
        PALABRAS_RESERVADAS.put("void", TokenType.VOID);
        PALABRAS_RESERVADAS.put("break", TokenType.BREAK);
        PALABRAS_RESERVADAS.put("do", TokenType.DO);
        PALABRAS_RESERVADAS.put("else", TokenType.ELSE);
        PALABRAS_RESERVADAS.put("if", TokenType.IF);
        PALABRAS_RESERVADAS.put("while", TokenType.WHILE);
        PALABRAS_RESERVADAS.put("return", TokenType.RETURN);
        PALABRAS_RESERVADAS.put("scanf", TokenType.READ);
        PALABRAS_RESERVADAS.put("printf", TokenType.WRITE);
        PALABRAS_RESERVADAS.put("auto", TokenType.AUTO);
        PALABRAS_RESERVADAS.put("double", TokenType.DOUBLE);
        PALABRAS_RESERVADAS.put("struct", TokenType.STRUCT);
        PALABRAS_RESERVADAS.put("long", TokenType.LONG);
        PALABRAS_RESERVADAS.put("switch", TokenType.SWITCH);
        PALABRAS_RESERVADAS.put("case", TokenType.CASE);
        PALABRAS_RESERVADAS.put("enum", TokenType.ENUM);
        PALABRAS_RESERVADAS.put("register", TokenType.REGISTER);
        PALABRAS_RESERVADAS.put("typedef", TokenType.TYPEDEF);
        PALABRAS_RESERVADAS.put("char", TokenType.CHAR);
        PALABRAS_RESERVADAS.put("extern", TokenType.EXTERN);
        PALABRAS_RESERVADAS.put("union", TokenType.UNION);
        PALABRAS_RESERVADAS.put("const", TokenType.CONST);
        PALABRAS_RESERVADAS.put("float", TokenType.FLOAT);
        PALABRAS_RESERVADAS.put("short", TokenType.SHORT);
        PALABRAS_RESERVADAS.put("unsigned", TokenType.UNSIGNED);
        PALABRAS_RESERVADAS.put("continue", TokenType.CONTINUE);
        PALABRAS_RESERVADAS.put("for", TokenType.FOR);
        PALABRAS_RESERVADAS.put("signed", TokenType.SIGNED);
        PALABRAS_RESERVADAS.put("goto", TokenType.GOTO);
        PALABRAS_RESERVADAS.put("sizeof", TokenType.SIZEOF);
        PALABRAS_RESERVADAS.put("volatile", TokenType.VOLATILE);
        PALABRAS_RESERVADAS.put("static", TokenType.STATIC);
        PALABRAS_RESERVADAS.put("default", TokenType.DEFAULT);
    }

    private final char[] fuente;
    private int posicion;
    private int linea;
    private final int longitud;

    public Lexer(String codigoFuente) {
        this.fuente = codigoFuente.toCharArray();
        this.longitud = fuente.length;
        this.posicion = 0;
        this.linea = 1;
    }

    private char observar() {
        if (posicion < longitud) {
            return fuente[posicion];
        } else {
            return '\0';
        }
    }

    private char observarSiguiente() {
        if (posicion + 1 < longitud) {
            return fuente[posicion + 1];
        } else {
            return '\0';
        }
    }

    private char avanzar() {
        char c = fuente[posicion];
        posicion++;
        if (c == '\n') {
            linea++;
        }
        return c;
    }

    private boolean estaAlFinal() {
        return posicion >= longitud;
    }

    private void omitirEspaciosYComentarios() {
        while (!estaAlFinal()) {
            char c = observar();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                avanzar();
            } else if (c == '/' && observarSiguiente() == '/') {
                while (!estaAlFinal() && observar() != '\n') {
                    avanzar();
                }
            } else if (c == '/' && observarSiguiente() == '*') {
                avanzar();
                avanzar();
                while (!estaAlFinal() && !(observar() == '*' && observarSiguiente() == '/')) {
                    avanzar();
                }
                if (!estaAlFinal()) {
                    avanzar();
                    avanzar();
                }
            } else {
                break;
            }
        }
    }

    public Token siguienteToken() {
        omitirEspaciosYComentarios();
        if (estaAlFinal()) {
            return new Token(TokenType.EOF, "", linea);
        }
        int lineaInicial = linea;
        return escanearConTabla(lineaInicial);
    }

    private Token escanearConTabla(int lineaInicial) {
        int inicio = posicion;
        int estadoActual = ESTADO_INICIAL;
        int posicionActual = posicion;

        int ultimoEstadoAceptacion = ESTADO_MUERTO;
        int ultimaPosicionAceptacion = -1;

        while (posicionActual < longitud) {
            int cls = clasificar(fuente[posicionActual]);
            int siguiente = tabla[estadoActual][cls];
            if (siguiente == ESTADO_MUERTO) {
                break;
            }
            posicionActual++;
            estadoActual = siguiente;
            if (aceptacion[estadoActual] != null) {
                ultimoEstadoAceptacion = estadoActual;
                ultimaPosicionAceptacion = posicionActual;
            }
        }

        if (ultimoEstadoAceptacion == ESTADO_MUERTO) {
            char malCaracter = avanzar();
            return new Token(TokenType.ERROR, String.valueOf(malCaracter), lineaInicial);
        }

        while (posicion < ultimaPosicionAceptacion) {
            avanzar();
        }

        String lexema = new String(fuente, inicio, ultimaPosicionAceptacion - inicio);
        TokenType tipo = aceptacion[ultimoEstadoAceptacion];
        if (ultimoEstadoAceptacion == ESTADO_ID) {
            if (PALABRAS_RESERVADAS.containsKey(lexema)) {
                tipo = PALABRAS_RESERVADAS.get(lexema);
            } else {
                tipo = TokenType.ID;
            }
        }
        return new Token(tipo, lexema, lineaInicial);
    }
}
