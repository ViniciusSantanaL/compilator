package br.com.viniciussls;

import java.util.ArrayList;
import java.util.List;

public class ErrorHanlder {

    private static final String BASE_TEXT = "------------ERROS-----------------\n";
    private static final List<String> errosLits = new ArrayList<>();

    public static void addMessage(String message) {
        errosLits.add(message);
    }

    public static String printErrors() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_TEXT);
        final int[] index = {1};
        errosLits.forEach(item -> {
            sb.append(index[0]).append(".").append(" ").append(item).append("\n");
            index[0]++;
        });
        return sb.toString();
    }

    public static boolean hasError() {
        return !errosLits.isEmpty();
    }
}
