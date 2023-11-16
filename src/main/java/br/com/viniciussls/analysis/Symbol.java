package br.com.viniciussls.analysis;

import java.util.HashMap;
import java.util.Map;

public enum Symbol {
    ASSIGNMENT ("="),
    ADD ("+"),
    SUBTRACT ("-"),
    MULTIPLY ("*"),
    DIVIDE ("/"),
    MODULO ("%"),
    EQ ("=="),
    NE ("!="),
    GT (">"),
    LT ("<"),
    GE (">="),
    LE ("<="),
    VARIABLE (""),
    INTEGER (""),
    REM ("rem"),
    INPUT ("input"),
    LET ("let"),
    PRINT ("print"),
    GOTO ("goto"),
    IF ("if"),
    ENTER("\n"),
    END ("end"),
    ERROR ("");

    private static final Map<String, Symbol> type = new HashMap<>();

    static {
        for(Symbol symbol : Symbol.values()) {
            type.put(symbol.getSymbol(), symbol);
        }
    }

    private final String symbol;

    Symbol(String symbol)
    {
        this.symbol = symbol;
    }

    public static Symbol parse(final String character) {
        return type.get(character);
    }

    public String getSymbol() {
        return symbol;
    }
}
