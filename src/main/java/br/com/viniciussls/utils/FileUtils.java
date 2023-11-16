package br.com.viniciussls.utils;

import br.com.viniciussls.synthesis.PairCommand;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static final String CODE_FILE = "src/main/resources/codigo.txt";

    public static final String RESULT_FILE = "src/main/resources/result.txt";

    public static String getFileMapToString() {
        try (BufferedReader buffer = new BufferedReader(new FileReader(CODE_FILE))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeResult(List<PairCommand> listCommand) {
        try {
            FileWriter fileWriter = new FileWriter(RESULT_FILE, false);
            for(PairCommand command : listCommand) {
                fileWriter.write(command.getExpression().concat("\n"));
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
