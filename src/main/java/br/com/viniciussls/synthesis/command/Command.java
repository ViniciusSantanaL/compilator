package br.com.viniciussls.synthesis.command;

import br.com.viniciussls.analysis.LexicalAnalysis;

public interface Command {
    void interpreteCommand(final LexicalAnalysis lexicalAnalysis);
}
