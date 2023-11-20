package br.com.viniciussls.synthesis;

public class PairCommand {

    private static Integer commandSize = 0;

    private final Integer lineNumber;

    private Operation operation;

    private Integer memmoryPosition;

    private String variable;

    private PairVariable pairVariable;

    public PairCommand(Operation operation, String variable) {
        this.operation = operation;
        this.variable = variable;
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
        String memmoryPositionFormat = getMemmoryPosition() == null ? null : String.format("%02d", getMemmoryPosition());
        String signal = "+";
        return signal.concat(operation.getValue().toString()).concat(memmoryPositionFormat == null ? "00": memmoryPositionFormat);
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

    public String getVariable() {
        return variable;
    }
}
