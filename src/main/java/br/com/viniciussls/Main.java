package br.com.viniciussls;

import br.com.viniciussls.analysis.AnalysisExecute;
import br.com.viniciussls.synthesis.SynthesisExecution;
import br.com.viniciussls.utils.FileUtils;

public class Main {

    public static void main(String[] args) {
        AnalysisExecute analysisExecute = new AnalysisExecute();
        analysisExecute.run();

        SynthesisExecution synthesisExecution = new SynthesisExecution();
        synthesisExecution.run();

        FileUtils.writeResult(synthesisExecution.getCommands());

    }
}
