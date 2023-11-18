package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.*;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;

public class PrintCommand implements Command {

    private static PrintCommand instance;

    private PrintCommand() { }
    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        lexicalAnalysis.nextToken();

        String variable = lexicalAnalysis.getCurrentToken().getValue();
        int memmoryPosition =  SynthesisExecution.getListMemmory().allocVariable(variable, 0);
        addToCommandList(StackOperation.push(Operation.WRITE, memmoryPosition));
        lexicalAnalysis.nextToken();
    }

    public static PrintCommand getInstance() {
        if(instance == null) {
            instance = new PrintCommand();
        }
        return instance;
    }
}
