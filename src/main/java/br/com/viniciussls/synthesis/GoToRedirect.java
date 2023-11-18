package br.com.viniciussls.synthesis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoToRedirect {

    private static final Map<Integer, Integer> SIMPLE_TO_SML_LINE_MAPPING = new HashMap<>();

    private static final Map<Integer, Integer> GOTOS_FOR_UPDATE = new HashMap<>();


    public static void registerLineNumber(Integer simpleLineNumber, Integer smlLineNumber) {
        SIMPLE_TO_SML_LINE_MAPPING.put(simpleLineNumber, smlLineNumber);
    }

    public static void addGotoForUpdate(Integer smlGoToLine, Integer redirectGotoNumber) {
        GOTOS_FOR_UPDATE.put(smlGoToLine, redirectGotoNumber);
    }

    public static void updateGotos(List<PairCommand> listMemmory) {
        for(Map.Entry<Integer, Integer> entry: GOTOS_FOR_UPDATE.entrySet()) {
            int goToLineNumber = entry.getKey();
            int oldRedirectLineNumber = entry.getValue();
            System.out.println("GoTo da linha sml" + goToLineNumber + ", que redireciona para linha do SIMPLE" + oldRedirectLineNumber);
            int newRedirectLineNumber = SIMPLE_TO_SML_LINE_MAPPING.get(oldRedirectLineNumber);
            PairCommand pairCommand = listMemmory.stream()
                                    .filter(item -> item.getLineNumber() == goToLineNumber)
                                    .findFirst()
                                    .orElseThrow();
            pairCommand.setMemmoryPosition(newRedirectLineNumber);
        }
    }
}
