package br.com.viniciussls.synthesis;

public class PairVariable {

    private Integer initialValue;

    private final Integer positionAux;

    public PairVariable(Integer positionAux, Integer initialValue) {
        this.positionAux = positionAux;
        this.initialValue = initialValue;
    }

    public String getVariableExpression() {
        Character signal = initialValue >= 0 ? '+' : '-';
        String valueFormat = String.format("%04d", initialValue);
        return signal + valueFormat;
    }

    public void changeInitialValue(Integer value) {
        this.initialValue = value;
    }

    public Integer getPositionAux() {
        return positionAux;
    }
}
