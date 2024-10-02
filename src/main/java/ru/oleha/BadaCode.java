package ru.oleha;

import java.util.ArrayList;

public class BadaCode {
    private final ArrayList<Integer> code;
    private final int codeSize;
    private final int controlNumber;
    public BadaCode(String string) {
        code = getCode(string);
        codeSize = calcSize(code);
        controlNumber = getControlNumber(code);
    }
    private int calcSize(ArrayList<Integer> code) {
        int codeSize = code.size();
        if (code.size() == 14) {
            codeSize -= 1;
        }
        return codeSize;
    }
    private int getControlNumber(ArrayList<Integer> integers) {
        return integers.get(integers.size()-1);
    }
    private ArrayList<Integer> getCode(String string) {
        ArrayList<Integer> integers = new ArrayList<>();
        if (string.length() == 13) {
            integers.add(-1);
        }
        for (char aChar : string.toCharArray()) {
            try {
                integers.add(Integer.parseInt(String.valueOf(aChar)));
            } catch (NumberFormatException ignore) {
                integers.add(-1);
            }
        }
        return integers;
    }
    private int oneCalc(ArrayList<Integer> integers) {
        int result = 0;
        for (int i = 0; i < integers.size(); i++) {
            int value = integers.get(i);
            if (value != -1 && value != 0) {
                if (i % 2 == 0) {
                    result += value;
                }
            }
        }
        return result;
    }
    private String oneCalcS(ArrayList<Integer> integers) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < integers.size(); i++) {
            int value = integers.get(i);
            if (value != -1 && value != 0) {
                if (i % 2 == 0) {
                    s.append(value).append(" + ");
                }
            }
        }
        s.delete(s.length() - 3,s.length());
        return "1) " + s + " = " + oneCalc(integers);
    }
    private int twoCalc(int i) {
        return i * 3;
    }
    private String twoCalcS(int i) {
        return "2) " + i +" * 3" + " = " + twoCalc(i);
    }
    private int threeCalc(ArrayList<Integer> integers) {
        int result = 0;
        for (int i = 0; i < integers.size(); i++) {
            if (i != integers.size() - 1) {
                if (i % 2 != 0) {
                    result += integers.get(i);
                }
            }
        }
        return result;
    }
    private String threeCalcS(ArrayList<Integer> integers) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < integers.size(); i++) {
            if (i != integers.size() - 1) {
                if (i % 2 != 0) {
                    if (integers.get(i) != 0) {
                        s.append(integers.get(i)).append(" + ");
                    }
                }
            }
        }
        s.delete(s.length() - 3,s.length());
        return "3) " + s + " = " + threeCalc(integers);
    }
    private int fourCalc(int i,int i2) {
        return i + i2;
    }
    private String fourCalcS(int i,int i2) {
        return "4) " +  i + " + " + i2 + " = " + (i + i2);
    }
    private int fiveCalc(int i) {
        int result = 0;
        for (int j = i; j < i + 10; j++) {
            if (j % 10 == 0) {
                result = j;
                break;
            }
        }
        return result;
    }
    private String fiveCalcS(int i) {
        int result = 0;
        for (int j = i; j < i + 10; j++) {
            if (j % 10 == 0) {
                result = j;
                break;
            }
        }
        if (controlNumber == -1) {
            return "5) " + "x = " + result + " - "  + i + " = " + (result - i);
        }
        if (i + controlNumber % 10 != 0) {
            return "5) " + i  + " + " + controlNumber + " = " + (i + controlNumber);
        }
        return "5) " + i  + " + " + (result - i) + " = " + result;
    }
    private String getType(int size) {
        if (size == 8) {
            return "EAN-8";
        }
        if (size == 12) {
            return "UPC-12";
        }
        if (size == 13) {
            return "EAN-13";
        }
        return "unknown code";
    }
    private String validCode(int i,int i2) {
        if ((i - i2) == controlNumber) {
            return "Штрихкод верный";
        } else if (controlNumber == -1) {
            return "Штрих код имеет контрольный разряд, равный " + (i - i2);
        } else if ((i - i2) != controlNumber) {
            return "Штрихкод неверный";
        }
        return "unknown validCode";
    }
    public String getString() {
        int oneCalc = oneCalc(code);
        int twoCalc = twoCalc(oneCalc);
        int threeCalc = threeCalc(code);
        int fourCalc = fourCalc(twoCalc,threeCalc);
        int fiveCalc = fiveCalc(fourCalc);
        String s = "Вид штрихкода: " + getType(codeSize) + "\n"
                + "Контрольное число: " + (controlNumber == -1 ? "X":controlNumber) + "\n"
                + oneCalcS(code) + "\n"
                + twoCalcS(oneCalc) + "\n"
                + threeCalcS(code) + "\n"
                + fourCalcS(twoCalc,threeCalc) + "\n"
                + fiveCalcS(fourCalc) + "\n"
                + validCode(fiveCalc,fourCalc);
        return s;
    }
}
