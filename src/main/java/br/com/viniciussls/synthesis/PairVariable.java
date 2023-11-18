package br.com.viniciussls.synthesis;

public class PairVariable {

    private Integer initialValue;

    private final Integer positionAux;

    public PairVariable(Integer positionAux, Integer initialValue) {
        this.positionAux = positionAux;
        this.initialValue = initialValue;
    }

    public String getVariableExpression() {
        if(initialValue >= 0) {
            return  '+' + String.format("%04d", initialValue);
        } else {
            return String.format("%05d", initialValue);
        }
    }

    public void changeInitialValue(Integer value) {
        this.initialValue = value;
    }

    public Integer getPositionAux() {
        return positionAux;
    }
}
