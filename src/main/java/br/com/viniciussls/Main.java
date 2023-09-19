package br.com.viniciussls;

public class Main {

    public static void main(String[] args) {
        // Código fonte exemplo para ser analisado
        String sourceCode = FileUtils.getFileMapToString("");

        // Criando uma instância da análise léxica
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(sourceCode);
        System.out.println("Iniciando Léxica");
        // Realizando a análise léxica
        while (lexicalAnalysis.nextToken()) {
            Token token = lexicalAnalysis.getCurrentToken();
            System.out.println("Token identificado: " + token.getSymbol() + " com valor: " + token.getValue());
        }

        if(ErrorHanlder.hasError()) {
            System.out.println(ErrorHanlder.printErrors());
            return;
        }
        // Resetando o índice para realizar a análise sintática
        lexicalAnalysis = new LexicalAnalysis(sourceCode);

        // Criando uma instância da análise sintática
        SyntacticAnalysis syntaxAnalysis = new SyntacticAnalysis(lexicalAnalysis);

        // Realizando a análise sintática
        System.out.println("-------------------------");
        System.out.println("Iniciando Sintática");
        try {
            syntaxAnalysis.parse();
            System.out.println("Análise sintática concluída com sucesso.");
        } catch (RuntimeException e) {
            System.out.println(ErrorHanlder.printErrors());
            return;
        }

        System.out.println("-------------------------");
        System.out.println("Iniciando Semantica");
        lexicalAnalysis = new LexicalAnalysis(sourceCode.toLowerCase());

        // Executar a análise semântica.
        try {
            SemanticAnalysis semanticAnalysis = new SemanticAnalysis(lexicalAnalysis);
            semanticAnalysis.analyze();
            System.out.println("Análise semântica concluída com sucesso!");
        } catch (RuntimeException e) {
            System.out.println(ErrorHanlder.printErrors());
        }
    }
}
