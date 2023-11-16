package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.PairCommand;

public class PrintCommand implements Command {

    private static PrintCommand instance;

    private PrintCommand() { }
    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        lexicalAnalysis.nextToken();
    }

    public static PrintCommand getInstance() {
        if(instance == null) {
            instance = new PrintCommand();
        }
        return instance;
    }
}
