package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.*;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;

public class EndCommand implements Command {

    private static EndCommand instance;

    private EndCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        addToCommandList(StackOperation.push(Operation.HALT, 0));
    }

    public static EndCommand getInstance() {
        if(instance == null) {
            instance = new EndCommand();
        }
        return instance;
    }
}
