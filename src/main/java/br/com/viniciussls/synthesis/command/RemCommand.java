package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;

public class RemCommand implements Command {

    private static RemCommand instance;

    private RemCommand() { }

    @Override
    public void interpreteCommand(LexicalAnalysis lexicalAnalysis) {
        lexicalAnalysis.nextToken();
    }

    public static RemCommand getInstance() {
        if(instance == null) {
            instance = new RemCommand();
        }
        return instance;
    }
}
