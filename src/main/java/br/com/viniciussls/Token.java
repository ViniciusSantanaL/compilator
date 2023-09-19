package br.com.viniciussls;

public class Token {

    private final Symbol symbol;

    private String value;

    public Token(Symbol symbol, String value) {
        this.symbol = symbol;
        this.value = value;
    }

    public Token(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }
}
