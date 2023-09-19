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
            if (isNextTokenACommand()) {
                lineNumber();  // Garantir que o começo de cada expressão tem um número de linha.
            }
            command();
        }
    }

    private boolean isNextTokenACommand() {
        Symbol nextSymbol = lexicalAnalysis.peekNextToken().getSymbol();
        return nextSymbol == Symbol.REM || nextSymbol == Symbol.INPUT || nextSymbol == Symbol.LET ||
                nextSymbol == Symbol.PRINT || nextSymbol == Symbol.GOTO || nextSymbol == Symbol.IF ||
                nextSymbol == Symbol.END;
    }

    private void lineNumber() {
        if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.INTEGER) {
            ErrorHanlder.addMessage("Esperado número de linha no início da expressão.");
            throw new RuntimeException();
        }
        lexicalAnalysis.nextToken();
    }

    private void command() {
        System.out.printf("Analisando expressão: %s%n", lexicalAnalysis.getCurrentToken().getSymbol());
        switch (lexicalAnalysis.getCurrentToken().getSymbol()) {
            case REM:
                // Pula o comentário
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
                // Não faz nada, apenas termina
                break;
            default:
                ErrorHanlder.addMessage("Comando não reconhecido, numero de linha vazio: " + lexicalAnalysis.getCurrentToken().getSymbol());
                throw new RuntimeException();
        }
        System.out.printf("Expressão %s analisada e válida!%n", lexicalAnalysis.getCurrentToken().getSymbol());
        lexicalAnalysis.nextToken();
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
                ErrorHanlder.addMessage("Número esperado após o sinal '-'.");
                throw new RuntimeException();
            }
        } else {
            ErrorHanlder.addMessage("Fator não reconhecido.");
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
                ErrorHanlder.addMessage("Operador condicional não reconhecido.");
                throw new RuntimeException();
        }
    }

    private void expect(Symbol expectedSymbol) {
        if (lexicalAnalysis.getCurrentToken().getSymbol() != expectedSymbol) {
            ErrorHanlder.addMessage("Token esperado: " + expectedSymbol + ", mas foi encontrado: " + lexicalAnalysis.getCurrentToken().getSymbol());
            throw new RuntimeException();
        }

        lexicalAnalysis.nextToken();
    }
}
