package br.com.viniciussls.synthesis;

import br.com.viniciussls.analysis.LexicalAnalysis;
import br.com.viniciussls.analysis.Symbol;
import br.com.viniciussls.analysis.Token;
import br.com.viniciussls.synthesis.command.CommandContext;
import br.com.viniciussls.utils.FileUtils;

import java.util.*;
import java.util.stream.Stream;

public class SynthesisExecution {
    private static List<PairCommand> commands = new ArrayList<>();

    private static final ListMemmory listMemmory = new ListMemmory();

    private final LexicalAnalysis lexicalAnalysis;

    private final CommandContext commandContext;

    public SynthesisExecution() {
        LexicalAnalysis  lexicalAnalysis = new LexicalAnalysis(FileUtils.getFileMapToString());
        this.commandContext = new CommandContext(lexicalAnalysis);
        this.lexicalAnalysis = lexicalAnalysis;
    }

    public void run() {
        lexicalAnalysis.nextToken();
        while(lexicalAnalysis.getCurrentToken().getSymbol() != Symbol.END) {
            jumpToken();
            commandContext.parseCommand();
        }
        GoToRedirect.updateGotos(commands);
        insertVariables();
        System.out.println("finalizou");
    }

    private void jumpToken() {
        Token previousToken = lexicalAnalysis.getPreviousToken();

        if (previousToken == null) {
            lexicalAnalysis.nextToken();
            return;
        }

        if(previousToken.getSymbol() == Symbol.ENTER) {
            lexicalAnalysis.nextToken();
        }
    }

    private void insertVariables() {
        List<PairCommand> listAux = new ArrayList<>();
        listMemmory.getMemmoryList().forEach(item -> {
            listAux.add(new PairCommand(item));
            commands.forEach(pair -> {
                if(Objects.equals(pair.getMemmoryPosition(), item.getPositionAux())) {
                    pair.setMemmoryPosition(commands.size() - 1);
                }
            });
        });
        commands = Stream.concat(commands.stream(), listAux.stream()).toList();
    }

    public static void addToCommandList(PairCommand pair) {
        commands.add(pair);
    }

    public static ListMemmory getListMemmory() {
        return listMemmory;
    }

    public List<PairCommand> getCommands() {
        return commands;
    }
}
