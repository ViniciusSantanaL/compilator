package br.com.viniciussls;

import java.util.Arrays;
import java.util.List;

public class LexicalAnalysis {

    private static final List<Character> ignoreChars = Arrays.asList(' ', '\r', '\t');

    private static final List<Character> operators = Arrays.asList('-','=', '+', '*', '/', '<', '>', '%', '\n');

    private final String code;

    private final int codeLength;

    private int currentIndex;

    private Token currentToken;

    private Token previousToken;

    public LexicalAnalysis(String codigo) {
        this.code = codigo;
        this.currentIndex = 0;
        this.codeLength = codigo.length();
    }

    public boolean nextToken() {
        while(!isFinish()) {
            this.previousToken = this.currentToken;
            final char actualChar = code.charAt(currentIndex);
            if(ignoreChars.contains(actualChar)) {
                skipBlankSpaces();
                continue;
            } else if(actualChar == 'r' && (currentIndex + 3 < codeLength) && code.startsWith("rem", currentIndex)) {
                currentToken = new Token(Symbol.REM, "rem");
                currentIndex += 4;
                skipComment();
            } else if ((Character.isLetter(actualChar) && Character.isLowerCase(actualChar)) || operators.contains(actualChar)) {
                Symbol symbol = readSymbol();
                if (symbol == null) {
                    currentToken = new Token(Symbol.VARIABLE, String.valueOf(code.charAt(currentIndex)));
                    currentIndex++;
                } else {
                    currentToken = new Token(symbol, symbol.getSymbol());
                }
            } else if (Character.isDigit(actualChar)) {
                currentToken = new Token(Symbol.INTEGER, readInteger());
            } else {
                ErrorHanlder.addMessage(ErrorType.LEXICAL,"Token não mapeado: " + actualChar);
                return false;
            }
            return true;
        }
        return false;
    }

    public Token peekNextToken() {
        int tempIndex = currentIndex;
        Token tempCurrentToken = currentToken;
        Token tempPreviousToken = previousToken;

        nextToken();
        Token next = currentToken;

        currentIndex = tempIndex;
        currentToken = tempCurrentToken;
        previousToken = tempPreviousToken;

        return next;
    }

    private Symbol readSymbol() {
        if(operators.contains(code.charAt(currentIndex))) {
            return readOperatorSymbol();
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 5 && currentIndex + i < codeLength; i++) {
                sb.append(code.charAt(currentIndex + i));
                Symbol symbol = Symbol.parse(sb.toString());
                if(symbol != null) {
                    currentIndex += (i + 1);
                    return symbol;
                }
            }
            return null;
        }
    }

    private Symbol readOperatorSymbol() {
        char currentChar = code.charAt(currentIndex);
        String potentialDoubleOperator = String.valueOf(currentChar);

        if (currentIndex + 1 < codeLength && operators.contains(code.charAt(currentIndex + 1))) {
            char nextChar = code.charAt(currentIndex + 1);
            potentialDoubleOperator += nextChar;
            Symbol doubleOpSymbol = Symbol.parse(potentialDoubleOperator);

            if (doubleOpSymbol != null) {
                currentIndex += 2;
                return doubleOpSymbol;
            }
        }
        currentIndex++;
        return Symbol.parse(String.valueOf(currentChar));
    }


    private String readInteger() {
        StringBuilder sb = new StringBuilder();
        char currentChar = code.charAt(currentIndex);

        while (!isFinish() && Character.isDigit(currentChar)) {
            sb.append(currentChar);
            currentIndex++;
            if (isFinish()) break;
            currentChar = code.charAt(currentIndex);
        }
        return sb.toString();
    }

    public void skipComment() {
        while(!isFinish() && code.charAt(currentIndex) != '\n') {
            currentIndex++;
        }
    }

    public void skipBlankSpaces() {
        while(!isFinish()) {
            if(ignoreChars.contains(code.charAt(currentIndex))){
                this.currentIndex++;
            } else {
                break;
            }
        }
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token getPreviousToken() {
        return previousToken;
    }

    public boolean isFinish() {
        return currentIndex >= codeLength;
    }

}
