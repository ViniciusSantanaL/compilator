package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.analysis.Symbol;
import br.com.viniciussls.analysis.Token;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.Operation;
import br.com.viniciussls.synthesis.PairCommand;
import br.com.viniciussls.synthesis.StackOperation;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;
import static br.com.viniciussls.synthesis.SynthesisExecution.getListMemmory;

public class IfCommand implements Command {

    private static IfCommand instance;

    private IfCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        // A sintaxe esperada é: if x <condição> y goto z
        // onde <condição> é um operador relacional.
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        // Pula o 'if'
        lexicalAnalysis.nextToken();
        Integer leftVariableMemPos = getOperandPosition(lexicalAnalysis.getCurrentToken());

        // Avança para o operador relacional
        lexicalAnalysis.nextToken();
        Symbol condition = lexicalAnalysis.getCurrentToken().getSymbol();

        // Avança para a segunda variável da condição
        lexicalAnalysis.nextToken();
        Integer rightVariableMemPos = getOperandPosition(lexicalAnalysis.getCurrentToken());

        // Avança para 'goto'
        lexicalAnalysis.nextToken();

        // Avança para o número da linha para o salto
        lexicalAnalysis.nextToken();
        Integer goToLineNumber = Integer.valueOf(lexicalAnalysis.getCurrentToken().getValue());

        // Aqui é onde geramos o código SML para a condição.
        generateConditionalCode(condition, leftVariableMemPos, rightVariableMemPos, goToLineNumber);

        // Avança para o próximo token após o 'if'
        lexicalAnalysis.nextToken();
    }

    private void generateConditionalCode(Symbol condition, Integer leftMemPos, Integer rightMemPos, Integer goToLine) {
        switch (condition) {
            case EQ: // ==
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine));
                break;
            case NE: // !=
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                int branchOverLine = PairCommand.getLineCount() + 2; // Próxima linha depois do BRANCHZERO.
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, branchOverLine));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCH, goToLine));
                break;
            case GT: // >
                addToCommandList(StackOperation.push(Operation.LOAD, rightMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, leftMemPos));
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine));
                break;
            case LT: // < ;
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine));
                break;
            case GE: // >=
                addToCommandList(StackOperation.push(Operation.LOAD, rightMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, leftMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine));
                break;
            case LE: // <=
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine));
                break;
            default:
                throw new IllegalStateException("Unsupported relational operation: " + condition);
        }
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

    public static IfCommand getInstance() {
        if(instance == null) {
            instance = new IfCommand();
        }
        return instance;
    }
}
