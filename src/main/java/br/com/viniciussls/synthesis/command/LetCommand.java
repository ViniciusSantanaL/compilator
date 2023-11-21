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
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());

        lexicalAnalysis.nextToken(); // Pula 'let'
        Token leftVariable = lexicalAnalysis.getCurrentToken();

        lexicalAnalysis.nextToken(); // Pula o nome da variável
        lexicalAnalysis.nextToken(); // Pula o '='

        // Verifica se o lado direito é uma constante e lida com ela.
        boolean isConst = constValue(lexicalAnalysis, leftVariable);
        if (isConst) {
            return; // Se for uma constante, o método constValue já lidou com isso.
        }

        // Pega a posição da memória para a variável à esquerda.
        getListMemmory().allocVariable(leftVariable);

        // Verifica se o próximo token é uma variável ou uma expressão.
        Token rightSideToken = lexicalAnalysis.getCurrentToken();
        Symbol nextSymbol = lexicalAnalysis.peekNextToken().getSymbol();

        if (rightSideToken.getSymbol() == Symbol.VARIABLE && nextSymbol == Symbol.ENTER) {
            // Se o lado direito é apenas outra variável.
            getListMemmory().allocVariable(rightSideToken);
            // Gera código para carregar o valor da variável direita e armazenar na esquerda.
            addToCommandList(StackOperation.push(Operation.LOAD, rightSideToken.getValue()));
            addToCommandList(StackOperation.push(Operation.STORE, leftVariable.getValue()));

        } else {
            // Se o lado direito é uma expressão aritmética.
            processArithmeticExpression(lexicalAnalysis, leftVariable.getValue());
        }
    }

    private boolean constValue(LexicalAnalysis lexicalAnalysis, Token variable) {
        if(lexicalAnalysis.getCurrentToken().getSymbol() == Symbol.INTEGER) {
            Symbol nextSymbol = lexicalAnalysis.peekNextToken().getSymbol();
            if(nextSymbol == Symbol.ENTER) {
                Token constant = lexicalAnalysis.getCurrentToken(); // get default value
                getListMemmory().allocVariable(variable);
                getListMemmory().allocVariable(constant);
                addToCommandList(StackOperation.push(Operation.LOAD, variable.getValue()));
                addToCommandList(StackOperation.push(Operation.ADD, constant.getValue()));
                addToCommandList(StackOperation.push(Operation.STORE, variable.getValue()));

                lexicalAnalysis.nextToken(); // jump integer
                return true;
            }
        }
        return false;
    }

    private void processArithmeticExpression(LexicalAnalysis lexicalAnalysis, String leftVariable) {
        // O primeiro operando já foi lido (é uma constante ou uma variável).
        getListMemmory().allocVariable(lexicalAnalysis.getCurrentToken());
        String firstOperandPos = lexicalAnalysis.getCurrentToken().getValue();

        // Avança para o operador.
        lexicalAnalysis.nextToken();
        Symbol operator = lexicalAnalysis.getCurrentToken().getSymbol();

        // Avança para o segundo operando.
        lexicalAnalysis.nextToken();
        getListMemmory().allocVariable(lexicalAnalysis.getCurrentToken());
        String secondOperandPos = lexicalAnalysis.getCurrentToken().getValue();

        // Agora, gera o código SML com base no operador.
        generateArithmeticCode(operator, firstOperandPos, secondOperandPos, leftVariable, lexicalAnalysis);

        // Avança para o próximo token após a expressão aritmética.
        lexicalAnalysis.nextToken();
    }

    private void generateArithmeticCode(Symbol operator, String firstOperandPos, String secondOperandPos, String resultPos, LexicalAnalysis lexicalAnalysis) {
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
