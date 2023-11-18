package br.com.viniciussls.synthesis;

import java.util.HashSet;
import java.util.Set;

public class StackOperation {

    private final static Set<Operation> stack = new HashSet<>();

    public static PairCommand push(Operation operation, Integer memmoryPosition) {
        if(stack.contains(operation)) {
            return null;
        } else {
            if(operation == Operation.STORE) {
                stack.clear();
            }
            return new PairCommand(operation, memmoryPosition);
        }
    }
}
