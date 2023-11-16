package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.synthesis.GoToRedirect;
import br.com.viniciussls.synthesis.Operation;
import br.com.viniciussls.synthesis.PairCommand;
import br.com.viniciussls.synthesis.SynthesisExecution;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;

public class InputCommand implements Command {

    private static InputCommand instance;

    private InputCommand() {}

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        lexicalAnalysis.nextToken();
        String variable = lexicalAnalysis.getCurrentToken().getValue();
        int memmoryPosition =  SynthesisExecution.getListMemmory().allocVariable(variable, 0);
        addToCommandList(new PairCommand(Operation.READ, memmoryPosition));
        lexicalAnalysis.nextToken();
    }

    public static InputCommand getInstance() {
        if(instance == null) {
            instance = new InputCommand();
        }
        return instance;
    }
}
