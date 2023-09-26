package br.com.viniciussls;

public class SyntacticAnalysis {

    private final LexicalAnalysis lexicalAnalysis;

    public SyntacticAnalysis(LexicalAnalysis lexicalAnalysis) {
        this.lexicalAnalysis = lexicalAnalysis ;
    }

    public void parse() {
        lexicalAnalysis.nextToken();
        while (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.END) {
            System.out.printf("Analisando token: %s%n", lexicalAnalysis.getCurrentToken().getSymbol());
            if (isPreviousTokenEnterOrStart()) {
                lineNumber();  // Garantir que o começo de cada expressão tem um número de linha.
            }
            command();
        }
    }
    public boolean isPreviousTokenEnterOrStart() {
        Token previousToken = lexicalAnalysis.getPreviousToken();

        if (previousToken == null) {
            return true;
        }

        return previousToken.getSymbol() == Symbol.ENTER;
    }

    private void lineNumber() {
        if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.INTEGER) {
            ErrorHanlder.addMessage(ErrorType.SYNTACTIC,"Esperado número de linha no início da expressão.");
            throw new RuntimeException();
        }
        lexicalAnalysis.nextToken();
    }

    private void command() {
        System.out.printf("Analisando expressão: %s%n", lexicalAnalysis.getCurrentToken().getSymbol());
        switch (lexicalAnalysis.getCurrentToken().getSymbol()) {
            case REM:
                lexicalAnalysis.nextToken();
                break;
            case INPUT:
                lexicalAnalysis.nextToken();
                expect(Symbol.VARIABLE);
                break;
            case LET:
                lexicalAnalysis.nextToken();
                expect(Symbol.VARIABLE);
                expect(Symbol.ASSIGNMENT);
                expression();
                break;
            case PRINT:
                lexicalAnalysis.nextToken();
                expect(Symbol.VARIABLE);
                break;
            case GOTO:
                lexicalAnalysis.nextToken();
                expect(Symbol.INTEGER);
                break;
            case IF:
                lexicalAnalysis.nextToken();
                condition();
                expect(Symbol.GOTO);
                expect(Symbol.INTEGER);
                break;
            case END:

                return;
            default:
                ErrorHanlder.addMessage(ErrorType.SYNTACTIC,"Comando não reconhecido, numero de linha vazio: " + lexicalAnalysis.getCurrentToken().getSymbol());
                throw new RuntimeException();
        }
        System.out.printf("Expressão %s analisada e válida!%n", lexicalAnalysis.getCurrentToken().getSymbol());
        expect(Symbol.ENTER);

    }

    private void expression() {
        term();
        while (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.ADD ||
                lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.SUBTRACT) {
            lexicalAnalysis.nextToken();
            term();
        }
    }

    private void term() {
        factor();
        while (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.MULTIPLY ||
                lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.DIVIDE ||
                lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.MODULO) {
            lexicalAnalysis.nextToken();
            factor();
        }
    }

    private void factor() {
        if (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.VARIABLE ||
                lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.INTEGER) {
            lexicalAnalysis.nextToken();
        } else if (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.SUBTRACT) {
            lexicalAnalysis.nextToken();
            if (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.INTEGER) {
                lexicalAnalysis.nextToken();
            } else {
                ErrorHanlder.addMessage(ErrorType.SYNTACTIC,"Número esperado após o sinal '-'.");
                throw new RuntimeException();
            }
        } else {
            ErrorHanlder.addMessage(ErrorType.SYNTACTIC,
                    "Fator não reconhecido. Esperado: VARIABLE, INTEGER ou '-' seguido por INTEGER. Encontrado: " + lexicalAnalysis.getCurrentToken().getSymbol() + " com valor: " + lexicalAnalysis.getCurrentToken().getValue() + ".");
            throw new RuntimeException();
        }
    }

    private void condition() {
        expression();
        switch (lexicalAnalysis.getCurrentToken().getSymbol()) {
            case GT:
            case GE:
            case LT:
            case LE:
            case EQ:
            case NE:
                lexicalAnalysis.nextToken();
                expression();
                break;
            default:
                ErrorHanlder.addMessage(ErrorType.SYNTACTIC,
                        "Operador condicional não reconhecido. Encontrado: " + lexicalAnalysis.getCurrentToken().getSymbol() + " com valor: " + lexicalAnalysis.getCurrentToken().getValue() +
                                ". Operadores condicionais válidos são: >, >=, <, <=, ==, !=.");
                throw new RuntimeException();
        }
    }

    private void expect(Symbol expectedSymbol) {
        if (lexicalAnalysis.getCurrentToken().getSymbol() != expectedSymbol) {
            ErrorHanlder.addMessage(ErrorType.SYNTACTIC,"Token esperado: " + expectedSymbol + ", mas foi encontrado: " + lexicalAnalysis.getCurrentToken().getSymbol());
            throw new RuntimeException();
        }

        lexicalAnalysis.nextToken();
    }
}
