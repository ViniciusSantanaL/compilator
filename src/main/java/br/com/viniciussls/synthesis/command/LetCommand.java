package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.analysis.Symbol;
import br.com.viniciussls.analysis.Token;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.Operation;
import br.com.viniciussls.synthesis.PairCommand;
import br.com.viniciussls.synthesis.SynthesisExecution;

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
            addToCommandList(new PairCommand(Operation.LOAD, rightVariableMemPos));
            addToCommandList(new PairCommand(Operation.STORE, leftVariableMemPos));
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
                    addToCommandList(new PairCommand(Operation.LOAD, variableMemmoryPosition));
                    addToCommandList(new PairCommand(Operation.ADD, memmoryPosition));
                    addToCommandList(new PairCommand(Operation.STORE, variableMemmoryPosition));
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
        generateArithmeticCode(operator, firstOperandPos, secondOperandPos, leftVariableMemPos);

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

    private void generateArithmeticCode(Symbol operator, Integer firstOperandPos, Integer secondOperandPos, Integer resultPos) {
        SynthesisExecution.addToCommandList(new PairCommand(Operation.LOAD, firstOperandPos));

        switch (operator) {
            case ADD:
                SynthesisExecution.addToCommandList(new PairCommand(Operation.ADD, secondOperandPos));
                break;
            case SUBTRACT:
                SynthesisExecution.addToCommandList(new PairCommand(Operation.SUBTRACT, secondOperandPos));
                break;
            case MULTIPLY:
                SynthesisExecution.addToCommandList(new PairCommand(Operation.MULTIPLY, secondOperandPos));
                break;
            case DIVIDE:
                SynthesisExecution.addToCommandList(new PairCommand(Operation.DIVIDE, secondOperandPos));
                break;
            case MODULO:
                SynthesisExecution.addToCommandList(new PairCommand(Operation.MODULE, secondOperandPos));
                break;
            default:
                throw new IllegalStateException("Unsupported arithmetic operation: " + operator);
        }

        SynthesisExecution.addToCommandList(new PairCommand(Operation.STORE, resultPos));
    }


    public static LetCommand getInstance() {
        if(instance == null) {
            instance = new LetCommand();
        }
        return instance;
    }
}
