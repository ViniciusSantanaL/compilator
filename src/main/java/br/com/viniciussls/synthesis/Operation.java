package br.com.viniciussls.synthesis;

public enum Operation {
    READ(10),
    WRITE(11),
    LOAD(20),
    STORE(21),
    ADD(30),
    SUBTRACT(31),
    DIVIDE(32),
    MULTIPLY(33),
    MODULE(34),
    BRANCH(40),
    BRANCHNEG(41),
    BRANCHZERO(42),
    HALT(43);


    private final Integer value;

    Operation(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
