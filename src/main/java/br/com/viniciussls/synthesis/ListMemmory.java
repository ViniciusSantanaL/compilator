package br.com.viniciussls.synthesis;

import br.com.viniciussls.analysis.Symbol;
import br.com.viniciussls.analysis.Token;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListMemmory {

    private final Map<String, PairVariable> memmoryPositions;

    public ListMemmory() {
        memmoryPositions =  new HashMap<>();
    }

    public void allocVariable(Token tokenVariable) {
        String variable = tokenVariable.getValue();
        if(!memmoryPositions.containsKey(variable)) {
            if(tokenVariable.getSymbol() == Symbol.INTEGER) {
                memmoryPositions.put(variable, new PairVariable(variable, Integer.valueOf(variable)));
            } else {
                memmoryPositions.put(variable, new PairVariable(variable, 0));
            }
        }
    }

    public void allocVariableWithInitialValue(Token tokenVariable, Integer initialValue) {
        String variable = tokenVariable.getValue();
        if(!memmoryPositions.containsKey(variable)) {
            if(tokenVariable.getSymbol() == Symbol.INTEGER) {
                memmoryPositions.put(variable, new PairVariable(variable,Integer.valueOf(variable)));
            } else {
                memmoryPositions.put(variable, new PairVariable(variable, initialValue));
            }
        }
    }


    public boolean variableExist(String variable) {
        return memmoryPositions.containsKey(variable);
    }



    public Collection<PairVariable> getMemmoryList() {
        return this.memmoryPositions.values();
    }
}
