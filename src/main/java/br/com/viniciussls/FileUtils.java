package br.com.viniciussls;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class FileUtils {

    public static final String BASE_PATH = "src/main/resources/codigo.txt";
    public static String getFileMapToString(String fileName) {
        try (BufferedReader buffer = new BufferedReader(new FileReader(BASE_PATH.concat(fileName)))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
