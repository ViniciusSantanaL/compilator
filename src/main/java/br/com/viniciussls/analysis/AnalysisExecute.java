package br.com.viniciussls.analysis;

import br.com.viniciussls.exception.ErrorHanlder;
import br.com.viniciussls.utils.FileUtils;

public class AnalysisExecute {

    private final LexicalAnalysis lexicalAnalysis;

    private final SyntacticAnalysis syntacticAnalysis;

    private final SemanticAnalysis semanticAnalysis;

    public AnalysisExecute() {
        String sourceCode = FileUtils.getFileMapToString();
        this.lexicalAnalysis = new LexicalAnalysis(sourceCode.toLowerCase());
        this.syntacticAnalysis = new SyntacticAnalysis(this.lexicalAnalysis);
        this.semanticAnalysis = new SemanticAnalysis(this.lexicalAnalysis);
    }
    public void run() {
        doLexicalAnalysis();
        lexicalAnalysis.reset();

        doSyntacticAnalysis();
        lexicalAnalysis.reset();

        doSemanticAnalysis();

        System.out.println("-------ANÁLISE FINALIZADA-------");
    }

    private void doLexicalAnalysis() {
        System.out.println("Iniciando Léxica");

        while (lexicalAnalysis.nextToken()) {
            Token token = lexicalAnalysis.getCurrentToken();
            System.out.println("Token identificado: " + token.getSymbol() + " com valor: " + token.getValue());
        }

        if(ErrorHanlder.hasError()) {
            System.out.println("-------PROGRAMA FINALIZADO-------");
            System.out.println(ErrorHanlder.printErrors());
            throw new RuntimeException();
        }
    }

    private void doSyntacticAnalysis() {
        System.out.println("-------------------------");
        System.out.println("Iniciando Sintática");
        try {
            syntacticAnalysis.parse();
            System.out.println("Análise sintática concluída com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("-------PROGRAMA FINALIZADO-------");
            System.out.println(ErrorHanlder.printErrors());
            throw new RuntimeException();
        }
    }

    private void doSemanticAnalysis() {
        System.out.println("-------------------------");
        System.out.println("Iniciando Semantica");

        try {
            semanticAnalysis.analyze();
        } catch (RuntimeException e) {
            System.out.println("-------PROGRAMA FINALIZADO-------");
            System.out.println(ErrorHanlder.printErrors());
            throw new RuntimeException();
        }

        System.out.println("Análise semântica concluída com sucesso!");
    }
}
