package br.com.viniciussls;

public enum ErrorType {
    LEXICAL("Erro Léxico"),
    SYNTACTIC("Erro Sintático"),
    SEMANTIC("Erro Semântico");

    private final String description;

    ErrorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}