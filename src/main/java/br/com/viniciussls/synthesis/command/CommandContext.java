package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;

public class CommandContext {

    private Command actualCommand;

    private final LexicalAnalysis lexicalAnalysis;

    public CommandContext(LexicalAnalysis lexicalAnalysis) {
        this.lexicalAnalysis = lexicalAnalysis;
    }

    public void parseCommand() {
        switch (lexicalAnalysis.getCurrentToken().getSymbol()) {
            case REM:
                execute(RemCommand.getInstance(), lexicalAnalysis);
                break;
            case INPUT:
                execute(InputCommand.getInstance(), lexicalAnalysis);
                break;
            case LET:
                execute(LetCommand.getInstance(), lexicalAnalysis);
                break;
            case PRINT:
                execute(PrintCommand.getInstance(), lexicalAnalysis);
                break;
            case GOTO:
                execute(GoToCommand.getInstance(), lexicalAnalysis);
                break;
            case IF:
                execute(IfCommand.getInstance(), lexicalAnalysis);
                break;
            case END:
                execute(EndCommand.getInstance(), lexicalAnalysis);
                return;
        }
        lexicalAnalysis.nextToken();
    }

    private void execute(Command command, LexicalAnalysis lexicalAnalysis) {
        setActualCommand(command);
        actualCommand.interpreteCommand(lexicalAnalysis);
    }

    private void setActualCommand(Command actualCommand) {
        this.actualCommand = actualCommand;
    }
}
