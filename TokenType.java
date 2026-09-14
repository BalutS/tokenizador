/**
 * Enumeracion de todos los tipos de tokens reconocidos por el escaner
 * del lenguaje C simplificado.
 */
public enum TokenType {
    // ---- Palabras reservadas (subconjunto pedido en el enunciado, 2.1) ----
    INT, MAIN, VOID, BREAK, DO, ELSE, IF, WHILE, RETURN, READ, WRITE,

    // ---- Palabras reservadas adicionales de C estandar (tabla del anexo) ----
    AUTO, DOUBLE, STRUCT, LONG, SWITCH, CASE, ENUM, REGISTER, TYPEDEF,
    CHAR, EXTERN, UNION, CONST, FLOAT, SHORT, UNSIGNED, CONTINUE, FOR,
    SIGNED, GOTO, SIZEOF, VOLATILE, STATIC, DEFAULT,

    // ---- Simbolos especiales / operadores (2.2) ----
    LBRACE, RBRACE, LSQUARE, RSQUARE, LPAR, RPAR, SEMI,
    PLUS, MINUS, MUL_OP, DIV_OP,
    AND_OP, OR_OP, NOT_OP, XOR_OP, NOT_BIT,
    ASSIGN, LT, GT, SHL_OP, SHR_OP,
    EQ, NOTEQ, LTEQ, GTEQ, ANDAND, OROR,
    COMMA, DOT, ARROW,

    // ---- Literales ----
    INT_NUM, ID, STRING_LIT, CHAR_LIT,

    // ---- Fin de archivo / error ----
    EOF, ERROR
}
