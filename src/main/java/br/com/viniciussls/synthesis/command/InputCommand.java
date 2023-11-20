package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.analysis.Token;
import br.com.viniciussls.synthesis.*;

import static br.com.viniciussls.synthesis.SynthesisExecution.addToCommandList;

public class InputCommand implements Command {

    private static InputCommand instance;

    private InputCommand() {}

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        Integer simpleLine = Integer.parseInt(lexicalAnalysis.getPreviousToken().getValue());
        GoToRedirect.registerLineNumber(simpleLine, PairCommand.getLineCount());
        lexicalAnalysis.nextToken();

        Token variable = lexicalAnalysis.getCurrentToken();
        SynthesisExecution.getListMemmory().allocVariable(variable);
        addToCommandList(StackOperation.push(Operation.READ, variable.getValue()));
        lexicalAnalysis.nextToken();
    }

    public static InputCommand getInstance() {
        if(instance == null) {
            instance = new InputCommand();
        }
        return instance;
    }
}
