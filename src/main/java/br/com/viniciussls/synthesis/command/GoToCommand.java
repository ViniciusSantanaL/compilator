package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.Operation;
import br.com.viniciussls.synthesis.PairCommand;
import br.com.viniciussls.synthesis.StackOperation;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;

public class GoToCommand implements Command {

    private static GoToCommand instance;

    private GoToCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        lexicalAnalysis.nextToken();

        Integer goToLineNumber = Integer.valueOf(lexicalAnalysis.getCurrentToken().getValue());

        GoToRedirect.addGotoForUpdate(PairCommand.getLineCount(), goToLineNumber);
        addToCommandList(StackOperation.push(Operation.BRANCH, goToLineNumber));
    }

    public static GoToCommand getInstance() {
        if(instance == null) {
            instance = new GoToCommand();
        }
        return instance;
    }
}
