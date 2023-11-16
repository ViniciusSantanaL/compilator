package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.PairCommand;

public class EndCommand implements Command {

    private static EndCommand instance;

    private EndCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
    }

    public static EndCommand getInstance() {
        if(instance == null) {
            instance = new EndCommand();
        }
        return instance;
    }
}
