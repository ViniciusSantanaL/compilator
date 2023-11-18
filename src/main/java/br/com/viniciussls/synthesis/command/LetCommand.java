package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.analysis.Symbol;
import br.com.viniciussls.analysis.Token;
import br.com.viniciussls.synthesis.*;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;
import static br.com.viniciussls.synthesis.SynthesisExecution.getListMemmory;

public class LetCommand implements Command {

    private static LetCommand instance;

    private LetCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());

        lexicalAnalysis.nextToken(); // Pula 'let'
        String leftVariable = lexicalAnalysis.getCurrentToken().getValue();

        lexicalAnalysis.nextToken(); // Pula o nome da variável
        lexicalAnalysis.nextToken(); // Pula o '='

        // Verifica se o lado direito é uma constante e lida com ela.
        boolean isConst = constValue(lexicalAnalysis, leftVariable, simpleLine);
        if (isConst) {
            return; // Se for uma constante, o método constValue já lidou com isso.
        }

        // Pega a posição da memória para a variável à esquerda.
        Integer leftVariableMemPos = getListMemmory().allocVariable(leftVariable, 0);

        // Verifica se o próximo token é uma variável ou uma expressão.
        Token rightSideToken = lexicalAnalysis.getCurrentToken();
        Symbol nextSymbol = lexicalAnalysis.peekNextToken().getSymbol();

        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        if (rightSideToken.getSymbol() == Symbol.VARIABLE && nextSymbol == Symbol.ENTER) {
            // Se o lado direito é apenas outra variável.
            Integer rightVariableMemPos = getListMemmory().allocVariable(rightSideToken.getValue(), 0);
            // Gera código para carregar o valor da variável direita e armazenar na esquerda.
            addToCommandList(StackOperation.push(Operation.LOAD, rightVariableMemPos));
            addToCommandList(StackOperation.push(Operation.STORE, leftVariableMemPos));

        } else {
            // Se o lado direito é uma expressão aritmética.
            processArithmeticExpression(lexicalAnalysis, leftVariableMemPos);
        }
    }

    private boolean constValue(LexicalAnalysis lexicalAnalysis, String variable, Integer simpleLine) {
        if(lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.INTEGER) {
            Symbol nextSymbol = lexicalAnalysis.peekNextToken().getSymbol();
            if(nextSymbol == Symbol.ENTER) {
                Integer constant = Integer.valueOf(lexicalAnalysis.getCurrentToken().getValue()); // get default value
                if(!getListMemmory().variableExist(variable)) {
                    getListMemmory().allocVariable(variable, constant); // alloc new variable with default value
                } else {
                    GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
                    Integer memmoryPosition = getListMemmory().allocConst(constant);
                    Integer variableMemmoryPosition = getListMemmory().allocVariable(variable, 0);
                    addToCommandList(StackOperation.push(Operation.LOAD, variableMemmoryPosition));
                    addToCommandList(StackOperation.push(Operation.ADD, memmoryPosition));
                    addToCommandList(StackOperation.push(Operation.STORE, variableMemmoryPosition));
                }
                lexicalAnalysis.nextToken(); // jump integer
                return true;
            }
        }
        return false;
    }

    private void processArithmeticExpression(LexicalAnalysis lexicalAnalysis, Integer leftVariableMemPos) {
        // O primeiro operando já foi lido (é uma constante ou uma variável).
        Integer firstOperandPos = getOperandPosition(lexicalAnalysis.getCurrentToken());

        // Avança para o operador.
        lexicalAnalysis.nextToken();
        Symbol operator = lexicalAnalysis.getCurrentToken().getSymbol();

        // Avança para o segundo operando.
        lexicalAnalysis.nextToken();
        Integer secondOperandPos = getOperandPosition(lexicalAnalysis.getCurrentToken());

        // Agora, gera o código SML com base no operador.
        generateArithmeticCode(operator, firstOperandPos, secondOperandPos, leftVariableMemPos, lexicalAnalysis);

        // Avança para o próximo token após a expressão aritmética.
        lexicalAnalysis.nextToken();
    }

    private Integer getOperandPosition(Token token) {
        // Se o token for um número, aloca uma constante.
        if (token.getSymbol() == Symbol.INTEGER) {
            Integer value = Integer.valueOf(token.getValue());
            return getListMemmory().allocConst(value);
        } else {
            // Se for uma variável, recupera sua posição de memória.
            return getListMemmory().allocVariable(token.getValue(), 0);
        }
    }

    private void generateArithmeticCode(Symbol operator, Integer firstOperandPos, Integer secondOperandPos, Integer resultPos, LexicalAnalysis lexicalAnalysis) {
        addToCommandList(StackOperation.push(Operation.LOAD, firstOperandPos));
        switch (operator) {
            case ADD:
                addToCommandList(StackOperation.push(Operation.ADD, secondOperandPos));
                break;
            case SUBTRACT:
                addToCommandList(StackOperation.push(Operation.SUBTRACT, secondOperandPos));
                break;
            case MULTIPLY:
                addToCommandList(StackOperation.push(Operation.MULTIPLY, secondOperandPos));
                break;
            case DIVIDE:
                addToCommandList(StackOperation.push(Operation.DIVIDE, secondOperandPos));
                break;
            case MODULO:
                addToCommandList(StackOperation.push(Operation.MODULE, secondOperandPos));
                break;
            default:
                throw new IllegalStateException("Unsupported arithmetic operation: " + operator + " token anterior: " + lexicalAnalysis.peekNextToken().getSymbol());
        }
        addToCommandList(StackOperation.push(Operation.STORE, resultPos));
    }


    public static LetCommand getInstance() {
        if(instance == null) {
            instance = new LetCommand();
        }
        return instance;
    }
}
