package br.com.viniciussls;

import java.util.HashSet;
import java.util.Set;

public class SemanticAnalysis {

    private final LexicalAnalysis lexicalAnalysis;
    private final Set<String> declaredVariables;
    private final Set<Integer> lineNumbers;
    private final Set<Integer> gotoTargets;
    private int lastLineNumber = 0;

    public SemanticAnalysis(LexicalAnalysis lexicalAnalysis) {
        this.lexicalAnalysis = lexicalAnalysis;
        this.declaredVariables = new HashSet<>();
        this.lineNumbers = new HashSet<>();
        this.gotoTargets = new HashSet<>();
    }

    public void analyze() {
        lexicalAnalysis.nextToken();
        while (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.END) {
            System.out.println(String.format("Analisando token: %s", lexicalAnalysis.getCurrentToken().getSymbol()));
            lineNumber();
            command();
        }
        verifyGotoTargets();
    }

    private void lineNumber() {
        if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.INTEGER) {
            ErrorHanlder.addMessage("Esperado número de linha no início da expressão.");
            throw new RuntimeException();
        }

        int currentLineNumber = Integer.parseInt(lexicalAnalysis.getCurrentToken().getValue());
        if (currentLineNumber <= lastLineNumber) {
            ErrorHanlder.addMessage("Número de linha " + currentLineNumber + " não está em ordem crescente ou foi repetido.");
            throw new RuntimeException();
        }
        if (lineNumbers.contains(currentLineNumber)) {
            ErrorHanlder.addMessage("Número de linha " + currentLineNumber + " repetido.");
            throw new RuntimeException();
        }
        lineNumbers.add(currentLineNumber);

        lastLineNumber = currentLineNumber;
        lexicalAnalysis.nextToken();
    }

    private void command() {
        System.out.printf("Analisando expressão semântica: %s%n", lexicalAnalysis.getCurrentToken().getSymbol());
        switch (lexicalAnalysis.getCurrentToken().getSymbol()) {
            case INPUT:
                lexicalAnalysis.nextToken();
                declareVariable(lexicalAnalysis.getCurrentToken().getValue());
                lexicalAnalysis.nextToken();
                break;
            case LET:
                lexicalAnalysis.nextToken();
                declareVariable(lexicalAnalysis.getCurrentToken().getValue());
                lexicalAnalysis.nextToken();
                lexicalAnalysis.nextToken(); // Skip ASSIGNMENT
                expression();
                break;
            case IF:
                lexicalAnalysis.nextToken();
                condition();
                if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.GOTO) {
                    ErrorHanlder.addMessage("Esperado GOTO após condição.");
                    throw new RuntimeException();
                }
                lexicalAnalysis.nextToken();
                if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.INTEGER) {
                    ErrorHanlder.addMessage("Número de linha esperado após GOTO.");
                    throw new RuntimeException();
                }
                gotoTargets.add(Integer.parseInt(lexicalAnalysis.getCurrentToken().getValue()));
                lexicalAnalysis.nextToken();
                break;
            case GOTO:
                lexicalAnalysis.nextToken();
                if (lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.INTEGER) {
                    ErrorHanlder.addMessage("Número de linha esperado após GOTO.");
                    throw new RuntimeException();
                }
                gotoTargets.add(Integer.parseInt(lexicalAnalysis.getCurrentToken().getValue()));
                lexicalAnalysis.nextToken();
                break;
            case PRINT:
                lexicalAnalysis.nextToken();
                useVariable(lexicalAnalysis.getCurrentToken().getValue());
                lexicalAnalysis.nextToken();
                break;
            case END:
                // Não faz nada.
                break;
            default:
                ErrorHanlder.addMessage("Comando não reconhecido.");
                throw new RuntimeException();
        }
        System.out.printf("Expressão semântica %s analisada e válida!%n", lexicalAnalysis.getCurrentToken().getSymbol());
    }

    private void expression() {
        if(lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.SUBTRACT) {
            lexicalAnalysis.nextToken();
        }
        term();
        while (isArithmeticOperator(lexicalAnalysis.getCurrentToken().getSymbol())) {
            lexicalAnalysis.nextToken();
            term();
        }
    }

    private boolean isArithmeticOperator(Symbol symbol) {
        return symbol == Symbol.ADD || symbol == Symbol.SUBTRACT ||
                symbol == Symbol.MULTIPLY || symbol == Symbol.DIVIDE;
    }

    private void term() {
        factor();
        while (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.MULTIPLY ||
                lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.DIVIDE) {
            lexicalAnalysis.nextToken();
            factor();
        }
    }
    private void relationalOperator() {
        Symbol currentSymbol = lexicalAnalysis.getCurrentToken().getSymbol();
        if (currentSymbol == Symbol.GT || currentSymbol == Symbol.LT ||
                currentSymbol == Symbol.EQ || currentSymbol == Symbol.NE ||
                currentSymbol == Symbol.GE || currentSymbol == Symbol.LE) {
            lexicalAnalysis.nextToken();
        } else {
            ErrorHanlder.addMessage("Operador de comparação esperado.");
            throw new RuntimeException();
        }
    }


    private void factor() {
        if (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.VARIABLE) {
            useVariable(lexicalAnalysis.getCurrentToken().getValue());
            lexicalAnalysis.nextToken(); // Avance o token após a VARIABLE
        } else if (lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.INTEGER) {
            lexicalAnalysis.nextToken(); // Avance o token após o INTEGER
        } else {
            ErrorHanlder.addMessage("Token inesperado em factor: " + lexicalAnalysis.getCurrentToken().getSymbol());
            throw new RuntimeException();
        }
    }


    private void condition() {
        expression();
        relationalOperator(); // Operador de comparação
        expression(); // Segunda expressão
    }

    private void declareVariable(String varName) {
        declaredVariables.add(varName);
    }

    private void useVariable(String varName) {
        if (!declaredVariables.contains(varName)) {
            ErrorHanlder.addMessage("Variável " + varName + " não foi declarada.");
            throw new RuntimeException();
        }
    }

    private void verifyGotoTargets() {
        for (int target : gotoTargets) {
            if (!lineNumbers.contains(target)) {
                ErrorHanlder.addMessage("GOTO para linha " + target + " que não existe.");
                throw new RuntimeException();
            }
        }
    }
}
