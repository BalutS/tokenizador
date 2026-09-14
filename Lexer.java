import java.util.HashMap;
import java.util.Map;

/**
 * Escaner (analizador lexico) para el lenguaje C simplificado.
 *
 * IMPLEMENTACION: Automata Finito Determinista (AFD) implementado como una
 * TABLA DE TRANSICIONES explicita: table[estado][clase_de_caracter] = estado
 * siguiente (o -1 si no existe transicion, es decir "estado muerto").
 *
 * No se usa Lex/Flex ni expresiones regulares del lenguaje: la tabla se
 * construye a mano en el metodo buildTable(), y el reconocimiento de cada
 * token se hace recorriendo la tabla con la tecnica clasica de simulacion
 * de un AFD con "maximal munch" (coincidencia mas larga) y retroceso
 * (rollback) hasta el ultimo estado de aceptacion visitado.
 */
public class Lexer {

    // ================= Clases de caracteres (columnas de la tabla) =================
    private static final int CLS_LETTER = 0;   // letras y '_'
    private static final int CLS_DIGIT = 1;    // 0-9
    private static final int CLS_QUOTE = 2;    // "
    private static final int CLS_SQUOTE = 3;   // '
    private static final int CLS_LBRACE = 4;   // {
    private static final int CLS_RBRACE = 5;   // }
    private static final int CLS_LBRACK = 6;   // [
    private static final int CLS_RBRACK = 7;   // ]
    private static final int CLS_LPAREN = 8;   // (
    private static final int CLS_RPAREN = 9;   // )
    private static final int CLS_SEMI = 10;    // ;
    private static final int CLS_COMMA = 11;   // ,
    private static final int CLS_DOT = 12;     // .
    private static final int CLS_PLUS = 13;    // +
    private static final int CLS_MINUS = 14;   // -
    private static final int CLS_STAR = 15;    // *
    private static final int CLS_SLASH = 16;   // /
    private static final int CLS_AMP = 17;     // &
    private static final int CLS_PIPE = 18;    // |
    private static final int CLS_CARET = 19;   // ^
    private static final int CLS_TILDE = 20;   // ~
    private static final int CLS_BANG = 21;    // !
    private static final int CLS_EQUAL = 22;   // =
    private static final int CLS_LT = 23;      // <
    private static final int CLS_GT = 24;      // >
    private static final int CLS_BACKSLASH = 25; // \
    private static final int CLS_OTHER = 26;   // cualquier otro caracter
    private static final int NUM_CLASSES = 27;

    // ================= Estados (filas de la tabla) =================
    private static final int DEAD = -1;        // estado muerto: no hay transicion
    private static final int START = 0;
    private static final int ID_STATE = 1;
    private static final int NUM_STATE = 2;
    private static final int STRING_BODY = 3;
    private static final int STRING_ESCAPE = 4;
    private static final int STRING_END = 5;
    private static final int CHAR_BODY = 6;
    private static final int CHAR_ESCAPE = 7;
    private static final int CHAR_CLOSE_PENDING = 8;
    private static final int CHAR_END = 9;
    private static final int LBRACE_END = 10;
    private static final int RBRACE_END = 11;
    private static final int LBRACK_END = 12;
    private static final int RBRACK_END = 13;
    private static final int LPAREN_END = 14;
    private static final int RPAREN_END = 15;
    private static final int SEMI_END = 16;
    private static final int COMMA_END = 17;
    private static final int DOT_END = 18;
    private static final int PLUS_END = 19;
    private static final int MUL_END = 20;
    private static final int SLASH_END = 21;   // DIV_OP
    private static final int CARET_END = 22;
    private static final int TILDE_END = 23;
    private static final int MINUS1 = 24;      // acepta MINUS, puede seguir a ARROW
    private static final int ARROW_END = 25;
    private static final int AMP1 = 26;        // acepta AND_OP, puede seguir a ANDAND
    private static final int ANDAND_END = 27;
    private static final int PIPE1 = 28;       // acepta OR_OP, puede seguir a OROR
    private static final int OROR_END = 29;
    private static final int BANG1 = 30;       // acepta NOT_OP, puede seguir a NOTEQ
    private static final int NOTEQ_END = 31;
    private static final int EQ1 = 32;         // acepta ASSIGN, puede seguir a EQ
    private static final int EQEQ_END = 33;
    private static final int LT1 = 34;         // acepta LT, puede seguir a LTEQ/SHL
    private static final int LTEQ_END = 35;
    private static final int SHL_END = 36;
    private static final int GT1 = 37;         // acepta GT, puede seguir a GTEQ/SHR
    private static final int GTEQ_END = 38;
    private static final int SHR_END = 39;
    private static final int NUM_STATES = 40;

    // table[estado][clase] = estado siguiente, o DEAD si no hay transicion
    private static final int[][] table = new int[NUM_STATES][NUM_CLASSES];
    // accept[estado] = tipo de token que se acepta en ese estado, o null si no es de aceptacion
    private static final TokenType[] accept = new TokenType[NUM_STATES];

    static {
        buildTable();
    }

    /** Construye la tabla de transiciones y marca los estados de aceptacion. */
    private static void buildTable() {
        // Inicializar toda la tabla como "muerta" (-1)
        for (int[] row : table) {
            java.util.Arrays.fill(row, DEAD);
        }

        // ---- Estado START: primera transicion segun el primer caracter ----
        table[START][CLS_LETTER] = ID_STATE;
        table[START][CLS_DIGIT] = NUM_STATE;
        table[START][CLS_QUOTE] = STRING_BODY;
        table[START][CLS_SQUOTE] = CHAR_BODY;
        table[START][CLS_LBRACE] = LBRACE_END;
        table[START][CLS_RBRACE] = RBRACE_END;
        table[START][CLS_LBRACK] = LBRACK_END;
        table[START][CLS_RBRACK] = RBRACK_END;
        table[START][CLS_LPAREN] = LPAREN_END;
        table[START][CLS_RPAREN] = RPAREN_END;
        table[START][CLS_SEMI] = SEMI_END;
        table[START][CLS_COMMA] = COMMA_END;
        table[START][CLS_DOT] = DOT_END;
        table[START][CLS_PLUS] = PLUS_END;
        table[START][CLS_MINUS] = MINUS1;
        table[START][CLS_STAR] = MUL_END;
        table[START][CLS_SLASH] = SLASH_END;
        table[START][CLS_AMP] = AMP1;
        table[START][CLS_PIPE] = PIPE1;
        table[START][CLS_CARET] = CARET_END;
        table[START][CLS_TILDE] = TILDE_END;
        table[START][CLS_BANG] = BANG1;
        table[START][CLS_EQUAL] = EQ1;
        table[START][CLS_LT] = LT1;
        table[START][CLS_GT] = GT1;

        // ---- IN_ID: [letra|_] ( [letra|digito|_] )*  (self-loop) ----
        table[ID_STATE][CLS_LETTER] = ID_STATE;
        table[ID_STATE][CLS_DIGIT] = ID_STATE;
        accept[ID_STATE] = TokenType.ID; // luego se corrige a keyword si aplica

        // ---- IN_NUM: [digito]+  (self-loop) ----
        table[NUM_STATE][CLS_DIGIT] = NUM_STATE;
        accept[NUM_STATE] = TokenType.INT_NUM;

        // ---- Cadenas "..." : cualquier caracter hasta la comilla de cierre ----
        for (int cls = 0; cls < NUM_CLASSES; cls++) table[STRING_BODY][cls] = STRING_BODY;
        table[STRING_BODY][CLS_QUOTE] = STRING_END;
        table[STRING_BODY][CLS_BACKSLASH] = STRING_ESCAPE;
        for (int cls = 0; cls < NUM_CLASSES; cls++) table[STRING_ESCAPE][cls] = STRING_BODY;
        accept[STRING_END] = TokenType.STRING_LIT;

        // ---- Caracter literal '...' : un solo caracter (o escape) entre comillas ----
        for (int cls = 0; cls < NUM_CLASSES; cls++) table[CHAR_BODY][cls] = CHAR_CLOSE_PENDING;
        table[CHAR_BODY][CLS_BACKSLASH] = CHAR_ESCAPE;
        table[CHAR_BODY][CLS_SQUOTE] = DEAD; // '' vacio no es valido
        for (int cls = 0; cls < NUM_CLASSES; cls++) table[CHAR_ESCAPE][cls] = CHAR_CLOSE_PENDING;
        table[CHAR_CLOSE_PENDING][CLS_SQUOTE] = CHAR_END;
        accept[CHAR_END] = TokenType.CHAR_LIT;

        // ---- Simbolos de un solo caracter (estados terminales, sin salida) ----
        accept[LBRACE_END] = TokenType.LBRACE;
        accept[RBRACE_END] = TokenType.RBRACE;
        accept[LBRACK_END] = TokenType.LSQUARE;
        accept[RBRACK_END] = TokenType.RSQUARE;
        accept[LPAREN_END] = TokenType.LPAR;
        accept[RPAREN_END] = TokenType.RPAR;
        accept[SEMI_END] = TokenType.SEMI;
        accept[COMMA_END] = TokenType.COMMA;
        accept[DOT_END] = TokenType.DOT;
        accept[PLUS_END] = TokenType.PLUS;
        accept[MUL_END] = TokenType.MUL_OP;
        accept[SLASH_END] = TokenType.DIV_OP;
        accept[CARET_END] = TokenType.XOR_OP;
        accept[TILDE_END] = TokenType.NOT_BIT;

        // ---- Operadores de uno o dos caracteres ----
        accept[MINUS1] = TokenType.MINUS;
        table[MINUS1][CLS_GT] = ARROW_END;
        accept[ARROW_END] = TokenType.ARROW;

        accept[AMP1] = TokenType.AND_OP;
        table[AMP1][CLS_AMP] = ANDAND_END;
        accept[ANDAND_END] = TokenType.ANDAND;

        accept[PIPE1] = TokenType.OR_OP;
        table[PIPE1][CLS_PIPE] = OROR_END;
        accept[OROR_END] = TokenType.OROR;

        accept[BANG1] = TokenType.NOT_OP;
        table[BANG1][CLS_EQUAL] = NOTEQ_END;
        accept[NOTEQ_END] = TokenType.NOTEQ;

        accept[EQ1] = TokenType.ASSIGN;
        table[EQ1][CLS_EQUAL] = EQEQ_END;
        accept[EQEQ_END] = TokenType.EQ;

        accept[LT1] = TokenType.LT;
        table[LT1][CLS_EQUAL] = LTEQ_END;
        table[LT1][CLS_LT] = SHL_END;
        accept[LTEQ_END] = TokenType.LTEQ;
        accept[SHL_END] = TokenType.SHL_OP;

        accept[GT1] = TokenType.GT;
        table[GT1][CLS_EQUAL] = GTEQ_END;
        table[GT1][CLS_GT] = SHR_END;
        accept[GTEQ_END] = TokenType.GTEQ;
        accept[SHR_END] = TokenType.SHR_OP;
    }

    /** Clasifica un caracter de entrada en una de las columnas de la tabla. */
    private static int classify(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') return CLS_LETTER;
        if (c >= '0' && c <= '9') return CLS_DIGIT;
        switch (c) {
            case '"': return CLS_QUOTE;
            case '\'': return CLS_SQUOTE;
            case '{': return CLS_LBRACE;
            case '}': return CLS_RBRACE;
            case '[': return CLS_LBRACK;
            case ']': return CLS_RBRACK;
            case '(': return CLS_LPAREN;
            case ')': return CLS_RPAREN;
            case ';': return CLS_SEMI;
            case ',': return CLS_COMMA;
            case '.': return CLS_DOT;
            case '+': return CLS_PLUS;
            case '-': return CLS_MINUS;
            case '*': return CLS_STAR;
            case '/': return CLS_SLASH;
            case '&': return CLS_AMP;
            case '|': return CLS_PIPE;
            case '^': return CLS_CARET;
            case '~': return CLS_TILDE;
            case '!': return CLS_BANG;
            case '=': return CLS_EQUAL;
            case '<': return CLS_LT;
            case '>': return CLS_GT;
            case '\\': return CLS_BACKSLASH;
            default: return CLS_OTHER;
        }
    }

    // Tabla de palabras reservadas: se consulta solo cuando el AFD termina
    // de reconocer un lexema en el estado ID_STATE.
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("main", TokenType.MAIN);
        KEYWORDS.put("void", TokenType.VOID);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("scanf", TokenType.READ);
        KEYWORDS.put("printf", TokenType.WRITE);
        KEYWORDS.put("auto", TokenType.AUTO);
        KEYWORDS.put("double", TokenType.DOUBLE);
        KEYWORDS.put("struct", TokenType.STRUCT);
        KEYWORDS.put("long", TokenType.LONG);
        KEYWORDS.put("switch", TokenType.SWITCH);
        KEYWORDS.put("case", TokenType.CASE);
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("register", TokenType.REGISTER);
        KEYWORDS.put("typedef", TokenType.TYPEDEF);
        KEYWORDS.put("char", TokenType.CHAR);
        KEYWORDS.put("extern", TokenType.EXTERN);
        KEYWORDS.put("union", TokenType.UNION);
        KEYWORDS.put("const", TokenType.CONST);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("short", TokenType.SHORT);
        KEYWORDS.put("unsigned", TokenType.UNSIGNED);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("signed", TokenType.SIGNED);
        KEYWORDS.put("goto", TokenType.GOTO);
        KEYWORDS.put("sizeof", TokenType.SIZEOF);
        KEYWORDS.put("volatile", TokenType.VOLATILE);
        KEYWORDS.put("static", TokenType.STATIC);
        KEYWORDS.put("default", TokenType.DEFAULT);
    }

    // ================= Estado del escaner sobre el codigo fuente =================
    private final char[] src;
    private int pos;
    private int line;
    private final int length;

    public Lexer(String source) {
        this.src = source.toCharArray();
        this.length = src.length;
        this.pos = 0;
        this.line = 1;
    }

    private char peek() {
        return pos < length ? src[pos] : '\0';
    }

    private char peekNext() {
        return (pos + 1 < length) ? src[pos + 1] : '\0';
    }

    private char advance() {
        char c = src[pos++];
        if (c == '\n') line++;
        return c;
    }

    private boolean isAtEnd() {
        return pos >= length;
    }

    /**
     * Elimina espacios en blanco y comentarios (// y /.../) antes de invocar
     * la tabla de transiciones. Al igual que en la mayoria de escaneres reales,
     * estos elementos se descartan en una etapa previa porque no generan
     * tokens del lenguaje.
     */
    private void skipWhitespaceAndComments() {
        while (!isAtEnd()) {
            char c = peek();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                advance();
            } else if (c == '/' && peekNext() == '/') {
                while (!isAtEnd() && peek() != '\n') advance();
            } else if (c == '/' && peekNext() == '*') {
                advance(); advance();
                while (!isAtEnd() && !(peek() == '*' && peekNext() == '/')) advance();
                if (!isAtEnd()) { advance(); advance(); }
            } else {
                break;
            }
        }
    }

    /** Reconoce y devuelve el proximo token, o EOF si no quedan caracteres. */
    public Token nextToken() {
        skipWhitespaceAndComments();
        if (isAtEnd()) {
            return new Token(TokenType.EOF, "", line);
        }
        int startLine = line;
        return scanWithTable(startLine);
    }

    /**
     * Recorre la tabla de transiciones caracter a caracter a partir de
     * START, aplicando "maximal munch": sigue avanzando mientras exista una
     * transicion valida y recuerda el ULTIMO estado de aceptacion visitado.
     * Cuando ya no hay transicion posible, retrocede (rollback) hasta ese
     * ultimo estado de aceptacion y construye el Token correspondiente.
     */
    private Token scanWithTable(int startLine) {
        int start = pos;
        int curState = START;
        int curPos = pos;

        int lastAcceptState = DEAD;
        int lastAcceptPos = -1;

        while (curPos < length) {
            int cls = classify(src[curPos]);
            int next = table[curState][cls];
            if (next == DEAD) break;
            curPos++;
            curState = next;
            if (accept[curState] != null) {
                lastAcceptState = curState;
                lastAcceptPos = curPos;
            }
        }

        if (lastAcceptState == DEAD) {
            // Ningun estado de aceptacion fue alcanzado: caracter no reconocido
            char bad = advance();
            return new Token(TokenType.ERROR, String.valueOf(bad), startLine);
        }

        // Confirmar el avance real del cursor (manteniendo el conteo de lineas)
        while (pos < lastAcceptPos) advance();

        String lexeme = new String(src, start, lastAcceptPos - start);
        TokenType type = accept[lastAcceptState];
        if (lastAcceptState == ID_STATE) {
            type = KEYWORDS.getOrDefault(lexeme, TokenType.ID);
        }
        return new Token(type, lexeme, startLine);
    }
}
