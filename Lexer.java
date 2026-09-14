import java.util.Arrays;
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
    private static final int CLS_PAREN_IZQ = 8;
    private static final int CLS_PAREN_DER = 9;
    private static final int CLS_PUNTO_Y_COMA = 10;
    private static final int CLS_COMA = 11;
    private static final int CLS_PUNTO = 12;
    private static final int CLS_MAS = 13;
    private static final int CLS_MENOS = 14;
    private static final int CLS_ASTERISCO = 15;
    private static final int CLS_BARRA = 16;
    private static final int CLS_AMPERSAND = 17;
    private static final int CLS_BARRA_VERTICAL = 18;
    private static final int CLS_ACENTO_CIRCUNFLEJO = 19;
    private static final int CLS_TILDE = 20;
    private static final int CLS_EXCLAMACION = 21;
    private static final int CLS_IGUAL = 22;
    private static final int CLS_MENOR = 23;
    private static final int CLS_MAYOR = 24;
    private static final int CLS_BARRA_INVERTIDA = 25;
    private static final int CLS_OTRO = 26;
    private static final int NUM_CLASES = 27;

    private static final int MUERTO = -1;
    private static final int INICIO = 0;
    private static final int ESTADO_ID = 1;
    private static final int ESTADO_NUM = 2;
    private static final int CUERPO_CADENA = 3;
    private static final int ESCAPE_CADENA = 4;
    private static final int FIN_CADENA = 5;
    private static final int CUERPO_CARACTER = 6;
    private static final int ESCAPE_CARACTER = 7;
    private static final int CARACTER_CIERRE_PENDIENTE = 8;
    private static final int FIN_CARACTER = 9;
    private static final int FIN_LLAVE_IZQ = 10;
    private static final int FIN_LLAVE_DER = 11;
    private static final int FIN_CORCHETE_IZQ = 12;
    private static final int FIN_CORCHETE_DER = 13;
    private static final int FIN_PAREN_IZQ = 14;
    private static final int FIN_PAREN_DER = 15;
    private static final int FIN_PUNTO_Y_COMA = 16;
    private static final int FIN_COMA = 17;
    private static final int FIN_PUNTO = 18;
    private static final int FIN_MAS = 19;
    private static final int FIN_MULTIPLICACION = 20;
    private static final int FIN_DIVISION = 21;
    private static final int FIN_XOR = 22;
    private static final int FIN_TILDE = 23;
    private static final int MENOS1 = 24;
    private static final int FIN_FLECHA = 25;
    private static final int AMP1 = 26;
    private static final int FIN_AND_LOGICO = 27;
    private static final int BARRA_VERTICAL1 = 28;
    private static final int FIN_OR_LOGICO = 29;
    private static final int EXCLAMACION1 = 30;
    private static final int FIN_DIFERENTE = 31;
    private static final int IGUAL1 = 32;
    private static final int FIN_IGUALDAD = 33;
    private static final int MENOR1 = 34;
    private static final int FIN_MENOR_IGUAL = 35;
    private static final int FIN_DESPLAZAR_IZQ = 36;
    private static final int MAYOR1 = 37;
    private static final int FIN_MAYOR_IGUAL = 38;
    private static final int FIN_DESPLAZAR_DER = 39;
    private static final int NUM_ESTADOS = 40;

    private static final int[][] tabla = new int[NUM_ESTADOS][NUM_CLASES];
    private static final TipoToken[] aceptacion = new TipoToken[NUM_ESTADOS];

    static {
        construirTabla();
    }

    private static void construirTabla() {
        for (int[] fila : tabla) {
            Arrays.fill(fila, MUERTO);
        }

        tabla[INICIO][CLS_LETRA] = ESTADO_ID;
        tabla[INICIO][CLS_DIGITO] = ESTADO_NUM;
        tabla[INICIO][CLS_COMILLA] = CUERPO_CADENA;
        tabla[INICIO][CLS_COMILLA_SIMPLE] = CUERPO_CARACTER;
        tabla[INICIO][CLS_LLAVE_IZQ] = FIN_LLAVE_IZQ;
        tabla[INICIO][CLS_LLAVE_DER] = FIN_LLAVE_DER;
        tabla[INICIO][CLS_CORCHETE_IZQ] = FIN_CORCHETE_IZQ;
        tabla[INICIO][CLS_CORCHETE_DER] = FIN_CORCHETE_DER;
        tabla[INICIO][CLS_PAREN_IZQ] = FIN_PAREN_IZQ;
        tabla[INICIO][CLS_PAREN_DER] = FIN_PAREN_DER;
        tabla[INICIO][CLS_PUNTO_Y_COMA] = FIN_PUNTO_Y_COMA;
        tabla[INICIO][CLS_COMA] = FIN_COMA;
        tabla[INICIO][CLS_PUNTO] = FIN_PUNTO;
        tabla[INICIO][CLS_MAS] = FIN_MAS;
        tabla[INICIO][CLS_MENOS] = MENOS1;
        tabla[INICIO][CLS_ASTERISCO] = FIN_MULTIPLICACION;
        tabla[INICIO][CLS_BARRA] = FIN_DIVISION;
        tabla[INICIO][CLS_AMPERSAND] = AMP1;
        tabla[INICIO][CLS_BARRA_VERTICAL] = BARRA_VERTICAL1;
        tabla[INICIO][CLS_ACENTO_CIRCUNFLEJO] = FIN_XOR;
        tabla[INICIO][CLS_TILDE] = FIN_TILDE;
        tabla[INICIO][CLS_EXCLAMACION] = EXCLAMACION1;
        tabla[INICIO][CLS_IGUAL] = IGUAL1;
        tabla[INICIO][CLS_MENOR] = MENOR1;
        tabla[INICIO][CLS_MAYOR] = MAYOR1;

        tabla[ESTADO_ID][CLS_LETRA] = ESTADO_ID;
        tabla[ESTADO_ID][CLS_DIGITO] = ESTADO_ID;
        aceptacion[ESTADO_ID] = TipoToken.IDENTIFICADOR;

        tabla[ESTADO_NUM][CLS_DIGITO] = ESTADO_NUM;
        aceptacion[ESTADO_NUM] = TipoToken.NUMERO_ENTERO;

        for (int cls = 0; cls < NUM_CLASES; cls++) tabla[CUERPO_CADENA][cls] = CUERPO_CADENA;
        tabla[CUERPO_CADENA][CLS_COMILLA] = FIN_CADENA;
        tabla[CUERPO_CADENA][CLS_BARRA_INVERTIDA] = ESCAPE_CADENA;
        for (int cls = 0; cls < NUM_CLASES; cls++) tabla[ESCAPE_CADENA][cls] = CUERPO_CADENA;
        aceptacion[FIN_CADENA] = TipoToken.LITERAL_CADENA;

        for (int cls = 0; cls < NUM_CLASES; cls++) tabla[CUERPO_CARACTER][cls] = CARACTER_CIERRE_PENDIENTE;
        tabla[CUERPO_CARACTER][CLS_BARRA_INVERTIDA] = ESCAPE_CARACTER;
        tabla[CUERPO_CARACTER][CLS_COMILLA_SIMPLE] = MUERTO;
        for (int cls = 0; cls < NUM_CLASES; cls++) tabla[ESCAPE_CARACTER][cls] = CARACTER_CIERRE_PENDIENTE;
        tabla[CARACTER_CIERRE_PENDIENTE][CLS_COMILLA_SIMPLE] = FIN_CARACTER;
        aceptacion[FIN_CARACTER] = TipoToken.LITERAL_CARACTER;

        aceptacion[FIN_LLAVE_IZQ] = TipoToken.LLAVE_IZQ;
        aceptacion[FIN_LLAVE_DER] = TipoToken.LLAVE_DER;
        aceptacion[FIN_CORCHETE_IZQ] = TipoToken.CORCHETE_IZQ;
        aceptacion[FIN_CORCHETE_DER] = TipoToken.CORCHETE_DER;
        aceptacion[FIN_PAREN_IZQ] = TipoToken.PAREN_IZQ;
        aceptacion[FIN_PAREN_DER] = TipoToken.PAREN_DER;
        aceptacion[FIN_PUNTO_Y_COMA] = TipoToken.PUNTO_Y_COMA;
        aceptacion[FIN_COMA] = TipoToken.COMA;
        aceptacion[FIN_PUNTO] = TipoToken.PUNTO;
        aceptacion[FIN_MAS] = TipoToken.MAS;
        aceptacion[FIN_MULTIPLICACION] = TipoToken.MULTIPLICACION;
        aceptacion[FIN_DIVISION] = TipoToken.DIVISION;
        aceptacion[FIN_XOR] = TipoToken.XOR_BIT;
        aceptacion[FIN_TILDE] = TipoToken.NOT_BIT;

        aceptacion[MENOS1] = TipoToken.MENOS;
        tabla[MENOS1][CLS_MAYOR] = FIN_FLECHA;
        aceptacion[FIN_FLECHA] = TipoToken.FLECHA;

        aceptacion[AMP1] = TipoToken.AND_BIT;
        tabla[AMP1][CLS_AMPERSAND] = FIN_AND_LOGICO;
        aceptacion[FIN_AND_LOGICO] = TipoToken.AND_LOGICO;

        aceptacion[BARRA_VERTICAL1] = TipoToken.OR_BIT;
        tabla[BARRA_VERTICAL1][CLS_BARRA_VERTICAL] = FIN_OR_LOGICO;
        aceptacion[FIN_OR_LOGICO] = TipoToken.OR_LOGICO;

        aceptacion[EXCLAMACION1] = TipoToken.NOT_LOGICO;
        tabla[EXCLAMACION1][CLS_IGUAL] = FIN_DIFERENTE;
        aceptacion[FIN_DIFERENTE] = TipoToken.DIFERENTE;

        aceptacion[IGUAL1] = TipoToken.ASIGNACION;
        tabla[IGUAL1][CLS_IGUAL] = FIN_IGUALDAD;
        aceptacion[FIN_IGUALDAD] = TipoToken.IGUALDAD;

        aceptacion[MENOR1] = TipoToken.MENOR;
        tabla[MENOR1][CLS_IGUAL] = FIN_MENOR_IGUAL;
        tabla[MENOR1][CLS_MENOR] = FIN_DESPLAZAR_IZQ;
        aceptacion[FIN_MENOR_IGUAL] = TipoToken.MENOR_IGUAL;
        aceptacion[FIN_DESPLAZAR_IZQ] = TipoToken.DESPLAZAR_IZQ;

        aceptacion[MAYOR1] = TipoToken.MAYOR;
        tabla[MAYOR1][CLS_IGUAL] = FIN_MAYOR_IGUAL;
        tabla[MAYOR1][CLS_MAYOR] = FIN_DESPLAZAR_DER;
        aceptacion[FIN_MAYOR_IGUAL] = TipoToken.MAYOR_IGUAL;
        aceptacion[FIN_DESPLAZAR_DER] = TipoToken.DESPLAZAR_DER;
    }

    private static int clasificar(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') return CLS_LETRA;
        if (c >= '0' && c <= '9') return CLS_DIGITO;
        switch (c) {
            case '"': return CLS_COMILLA;
            case '\'': return CLS_COMILLA_SIMPLE;
            case '{': return CLS_LLAVE_IZQ;
            case '}': return CLS_LLAVE_DER;
            case '[': return CLS_CORCHETE_IZQ;
            case ']': return CLS_CORCHETE_DER;
            case '(': return CLS_PAREN_IZQ;
            case ')': return CLS_PAREN_DER;
            case ';': return CLS_PUNTO_Y_COMA;
            case ',': return CLS_COMA;
            case '.': return CLS_PUNTO;
            case '+': return CLS_MAS;
            case '-': return CLS_MENOS;
            case '*': return CLS_ASTERISCO;
            case '/': return CLS_BARRA;
            case '&': return CLS_AMPERSAND;
            case '|': return CLS_BARRA_VERTICAL;
            case '^': return CLS_ACENTO_CIRCUNFLEJO;
            case '~': return CLS_TILDE;
            case '!': return CLS_EXCLAMACION;
            case '=': return CLS_IGUAL;
            case '<': return CLS_MENOR;
            case '>': return CLS_MAYOR;
            case '\\': return CLS_BARRA_INVERTIDA;
            default: return CLS_OTRO;
        }
    }

    private static final Map<String, TipoToken> PALABRAS_RESERVADAS = new HashMap<>();
    static {
        PALABRAS_RESERVADAS.put("int", TipoToken.INT);
        PALABRAS_RESERVADAS.put("main", TipoToken.MAIN);
        PALABRAS_RESERVADAS.put("void", TipoToken.VOID);
        PALABRAS_RESERVADAS.put("break", TipoToken.BREAK);
        PALABRAS_RESERVADAS.put("do", TipoToken.DO);
        PALABRAS_RESERVADAS.put("else", TipoToken.ELSE);
        PALABRAS_RESERVADAS.put("if", TipoToken.IF);
        PALABRAS_RESERVADAS.put("while", TipoToken.WHILE);
        PALABRAS_RESERVADAS.put("return", TipoToken.RETURN);
        PALABRAS_RESERVADAS.put("scanf", TipoToken.READ);
        PALABRAS_RESERVADAS.put("printf", TipoToken.WRITE);
        PALABRAS_RESERVADAS.put("auto", TipoToken.AUTO);
        PALABRAS_RESERVADAS.put("double", TipoToken.DOUBLE);
        PALABRAS_RESERVADAS.put("struct", TipoToken.STRUCT);
        PALABRAS_RESERVADAS.put("long", TipoToken.LONG);
        PALABRAS_RESERVADAS.put("switch", TipoToken.SWITCH);
        PALABRAS_RESERVADAS.put("case", TipoToken.CASE);
        PALABRAS_RESERVADAS.put("enum", TipoToken.ENUM);
        PALABRAS_RESERVADAS.put("register", TipoToken.REGISTER);
        PALABRAS_RESERVADAS.put("typedef", TipoToken.TYPEDEF);
        PALABRAS_RESERVADAS.put("char", TipoToken.CHAR);
        PALABRAS_RESERVADAS.put("extern", TipoToken.EXTERN);
        PALABRAS_RESERVADAS.put("union", TipoToken.UNION);
        PALABRAS_RESERVADAS.put("const", TipoToken.CONST);
        PALABRAS_RESERVADAS.put("float", TipoToken.FLOAT);
        PALABRAS_RESERVADAS.put("short", TipoToken.SHORT);
        PALABRAS_RESERVADAS.put("unsigned", TipoToken.UNSIGNED);
        PALABRAS_RESERVADAS.put("continue", TipoToken.CONTINUE);
        PALABRAS_RESERVADAS.put("for", TipoToken.FOR);
        PALABRAS_RESERVADAS.put("signed", TipoToken.SIGNED);
        PALABRAS_RESERVADAS.put("goto", TipoToken.GOTO);
        PALABRAS_RESERVADAS.put("sizeof", TipoToken.SIZEOF);
        PALABRAS_RESERVADAS.put("volatile", TipoToken.VOLATILE);
        PALABRAS_RESERVADAS.put("static", TipoToken.STATIC);
        PALABRAS_RESERVADAS.put("default", TipoToken.DEFAULT);
    }

    private final char[] fuente;
    private int posicion;
    private int linea;
    private final int longitud;

    public Lexer(String fuente) {
        this.fuente = fuente.toCharArray();
        this.longitud = this.fuente.length;
        this.posicion = 0;
        this.linea = 1;
    }

    private char echarVistazo() {
        return posicion < longitud ? fuente[posicion] : '\0';
    }

    private char echarVistazoSiguiente() {
        return (posicion + 1 < longitud) ? fuente[posicion + 1] : '\0';
    }

    private char avanzar() {
        char c = fuente[posicion++];
        if (c == '\n') linea++;
        return c;
    }

    private boolean estaAlFinal() {
        return posicion >= longitud;
    }

    private void omitirEspaciosYComentarios() {
        while (!estaAlFinal()) {
            char c = echarVistazo();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                avanzar();
            } else if (c == '/' && echarVistazoSiguiente() == '/') {
                while (!estaAlFinal() && echarVistazo() != '\n') avanzar();
            } else if (c == '/' && echarVistazoSiguiente() == '*') {
                avanzar(); avanzar();
                while (!estaAlFinal() && !(echarVistazo() == '*' && echarVistazoSiguiente() == '/')) avanzar();
                if (!estaAlFinal()) { avanzar(); avanzar(); }
            } else {
                break;
            }
        }
    }

    public Token siguienteToken() {
        omitirEspaciosYComentarios();
        if (estaAlFinal()) {
            return new Token(TipoToken.FIN_ARCHIVO, "", linea);
        }
        int lineaInicio = linea;
        return escanearConTabla(lineaInicio);
    }

    private Token escanearConTabla(int lineaInicio) {
        int inicio = posicion;
        int estadoActual = INICIO;
        int posActual = posicion;

        int ultimoEstadoAceptacion = MUERTO;
        int ultimaPosAceptacion = -1;

        while (posActual < longitud) {
            int cls = clasificar(fuente[posActual]);
            int siguiente = tabla[estadoActual][cls];
            if (siguiente == MUERTO) break;
            posActual++;
            estadoActual = siguiente;
            if (aceptacion[estadoActual] != null) {
                ultimoEstadoAceptacion = estadoActual;
                ultimaPosAceptacion = posActual;
            }
        }

        if (ultimoEstadoAceptacion == MUERTO) {
            char malo = avanzar();
            return new Token(TipoToken.ERROR, String.valueOf(malo), lineaInicio);
        }

        while (posicion < ultimaPosAceptacion) avanzar();

        String lexema = new String(fuente, inicio, ultimaPosAceptacion - inicio);
        TipoToken tipo = aceptacion[ultimoEstadoAceptacion];
        if (ultimoEstadoAceptacion == ESTADO_ID) {
            tipo = PALABRAS_RESERVADAS.getOrDefault(lexema, TipoToken.IDENTIFICADOR);
        }
        return new Token(tipo, lexema, lineaInicio);
    }
}
