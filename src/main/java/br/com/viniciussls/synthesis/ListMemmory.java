package br.com.viniciussls.synthesis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListMemmory {

    private final Map<String, PairVariable> memmoryPositions;

    public ListMemmory() {
        memmoryPositions =  new HashMap<>();
    }

    public Integer allocVariable(String variable, Integer initialValue) {
        if(memmoryPositions.containsKey(variable)){
            return memmoryPositions.get(variable).getPositionAux();
        }
        int memmoryPositionTemporary = memmoryPositions.size();
        memmoryPositions.put(variable, new PairVariable(memmoryPositionTemporary, initialValue));
        return memmoryPositionTemporary;
    }

    public Integer allocConst(Integer constant) {
        if(memmoryPositions.containsKey(constant.toString())) {
            return memmoryPositions.get(constant.toString()).getPositionAux();
        }
        int memmoryPositionTemporary = memmoryPositions.size();
        memmoryPositions.put(constant.toString(), new PairVariable(memmoryPositionTemporary, constant));
        return memmoryPositionTemporary;
    }

    public Integer getMemmoryPosition(String variable) {
        return memmoryPositions.get(variable).getPositionAux();
    }

    public boolean variableExist(String variable) {
        return memmoryPositions.containsKey(variable);
    }

    public void updateInitialValue(String variable, Integer value) {
        PairVariable pairVariable = memmoryPositions.get(variable);
        pairVariable.changeInitialValue(value);
    }


    public Collection<PairVariable> getMemmoryList() {
        return this.memmoryPositions.values();
    }
}
