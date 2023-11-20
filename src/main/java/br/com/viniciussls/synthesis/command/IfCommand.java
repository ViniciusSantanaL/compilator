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
        getListMemmory().allocVariable(lexicalAnalysis.getCurrentToken());
        String leftVariableMemPos = lexicalAnalysis.getCurrentToken().getValue();

        // Avança para o operador relacional
        lexicalAnalysis.nextToken();
        Symbol condition = lexicalAnalysis.getCurrentToken().getSymbol();

        // Avança para a segunda variável da condição
        lexicalAnalysis.nextToken();
        getListMemmory().allocVariable(lexicalAnalysis.getCurrentToken());
        String rightVariableMemPos = lexicalAnalysis.getCurrentToken().getValue();

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

    private void generateConditionalCode(Symbol condition, String leftMemPos, String rightMemPos, Integer goToLine) {
        switch (condition) {
            case EQ: // ==
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine.toString()));
                break;
            case NE: // !=
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                int branchOverLine = PairCommand.getLineCount() + 2; // Próxima linha depois do BRANCHZERO.
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, String.valueOf(branchOverLine)));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCH, goToLine.toString()));
                break;
            case GT: // >
                addToCommandList(StackOperation.push(Operation.LOAD, rightMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, leftMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine.toString()));
                break;
            case LT: // < ;
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine.toString()));
                break;
            case GE: // >=
                addToCommandList(StackOperation.push(Operation.LOAD, rightMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, leftMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine.toString()));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine.toString()));
                break;
            case LE: // <=
                addToCommandList(StackOperation.push(Operation.LOAD, leftMemPos));
                addToCommandList(StackOperation.push(Operation.SUBTRACT, rightMemPos));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHNEG, goToLine.toString()));
                GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLine);
                addToCommandList(StackOperation.push(Operation.BRANCHZERO, goToLine.toString()));
                break;
            default:
                throw new IllegalStateException("Unsupported relational operation: " + condition);
        }
    }

    public static IfCommand getInstance() {
        if(instance == null) {
            instance = new IfCommand();
        }
        return instance;
    }
}
