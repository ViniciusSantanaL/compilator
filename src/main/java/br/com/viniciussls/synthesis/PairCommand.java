package br.com.viniciussls.synthesis;

public class PairCommand {

    private static Integer commandSize = 0;

    private final Integer lineNumber;

    private Operation operation;

    private Integer memmoryPosition;

    private PairVariable pairVariable;

    public PairCommand(Operation operation, Integer memmoryPosition) {
        this.operation = operation;
        this.memmoryPosition = memmoryPosition;
        this.lineNumber = commandSize;
        commandSize++;
    }

    public PairCommand(PairVariable pairVariable) {
        this.pairVariable = pairVariable;
        this.lineNumber = commandSize;
        commandSize++;
    }

    public String getExpression() {
        return pairVariable == null ? getCommandExpression() : pairVariable.getVariableExpression();
    }
    private String getCommandExpression() {
        String memmoryPositionFormat = String.format("%02d", getMemmoryPosition());
        String signal = "+";
        return signal.concat(operation.getValue().toString()).concat(memmoryPositionFormat);
    }

    public void setMemmoryPosition(Integer memmoryPosition) {
        this.memmoryPosition = memmoryPosition;
    }

    public Integer getMemmoryPosition() {
        return memmoryPosition;
    }

    public static Integer getLineCount() {
        return commandSize;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }
}
