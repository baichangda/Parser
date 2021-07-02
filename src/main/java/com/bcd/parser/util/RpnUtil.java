package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;
import java.util.*;

public class RpnUtil {

    /**
     * 处理rpn表达式集合
     * 字符串变量 --> char
     * 将数字字符串 --> double
     *
     * @param rpn rpn表达式集合
     * @return
     */
    public static Object[] doWithRpnList_char_double(String[] rpn) {
        return Arrays.stream(rpn).map(e -> {
            try {
                return Double.parseDouble(e);
            } catch (NumberFormatException ex) {
                return e.charAt(0);
            }
        }).toArray();
    }

    /**
     * 处理rpn表达式集合
     * 字符串变量 --> char
     * 将数字字符串 --> int
     *
     * @param rpn rpn表达式集合
     * @return
     */
    public static Object[] doWithRpnList_char_int(String[] rpn) {
        return Arrays.stream(rpn).map(e -> {
            try {
                return Integer.parseInt(e);
            } catch (NumberFormatException ex) {
                return e.charAt(0);
            }
        }).toArray();
    }

    /**
     * list中变量定义必须是char 支持 a-z A-Z
     * <p>
     * A-Z --> 65-90
     * a-z --> 97-122
     * 所以char数组长度为 65-122 长度为58
     * 同时需要进行偏移量计算也就是 字符-65
     *
     * @param rpn  rpn表达式集合,其中变量必须是char,常量必须是int
     * @param vals 变量对应值数组,取值规则为 vals[int(char)]
     * @return
     */
    public static int calcRPN_char_int(Object[] rpn, int[] vals) {
        int stackIndex = -1;
        final int[] stack = new int[rpn.length];
        for (Object s : rpn) {
            if (s instanceof Integer) {
                stack[++stackIndex] = (int) s;
            } else {
                switch ((char) s) {
                    case '+': {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] + stack[stackIndex + 1];
                        break;
                    }
                    case '-': {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] - stack[stackIndex + 1];
                        break;
                    }
                    case '*': {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] * stack[stackIndex + 1];
                        break;
                    }
                    case '/': {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] / stack[stackIndex + 1];
                        break;
                    }
                    default: {
                        stack[++stackIndex] = vals[(char) s];
                        break;
                    }
                }
            }
        }
        return stack[0];
    }


    /**
     * 将算数字符串转换成逆波兰表达式
     * 算数支持 + - * / ( ) 符号
     *
     * @return
     */
    public static String[] parseArithmeticToRPN(String str) {
        List<String> output = new ArrayList<>();
        int stackIndex = -1;
        char[] stack = new char[str.length()];
        char[] arr = str.toCharArray();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i <= arr.length - 1; i++) {
            if (arr[i] == '+' || arr[i] == '-' || arr[i] == '*' || arr[i] == '/') {
                if (temp.length() > 0) {
                    output.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                if (stackIndex >= 0) {
                    while (stack[stackIndex] != '(' && getSymbolPriority(stack[stackIndex]) >= getSymbolPriority(arr[i])) {
                        output.add(String.valueOf(stack[stackIndex--]));
                        if (stackIndex == -1) {
                            break;
                        }
                    }
                }
                stack[++stackIndex] = arr[i];
            } else if (arr[i] == '(') {
                stack[++stackIndex] = arr[i];
            } else if (arr[i] == ')') {
                if (temp.length() > 0) {
                    output.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                while (stackIndex >= 0) {
                    char c = stack[stackIndex--];
                    if (c == '(') {
                        break;
                    }
                    output.add(String.valueOf(c));
                }
            } else {
                temp.append(arr[i]);
            }
        }

        if (temp.length() > 0) {
            output.add(temp.toString());
        }

        for (int i = stackIndex; i >= 0; i--) {
            output.add(String.valueOf(stack[i]));
        }

        return output.toArray(new String[0]);
    }

    /**
     * 获取字符优先级
     *
     * @param c
     * @return
     */
    private static int getSymbolPriority(char c) {
        switch (c) {
            case '+':
            case '-': {
                return 1;
            }
            case '*':
            case '/': {
                return 2;
            }
            default: {
                throw BaseRuntimeException.getException("symbol[" + c + "] not support");
            }
        }
    }

    public static String parseRPNToArithmetic(Object[] rpn) {
        if (rpn.length == 1) {
            return rpn[0].toString();
        } else {
            String[] stack = new String[rpn.length];
            int[] symbolPriority = new int[rpn.length];
            int index = -1;
            for (Object o : rpn) {
                String s = o.toString();
                if (s.equals("+") ||
                        s.equals("-") ||
                        s.equals("*") ||
                        s.equals("/")) {
                    int index2 = index--;
                    int index1 = index--;
                    String s1 = stack[index1];
                    String s2 = stack[index2];
                    int p1 = symbolPriority[index1];
                    int p2 = symbolPriority[index2];
                    int curSymbolPriority = getSymbolPriority(s.charAt(0));
                    if (p1 != -1 && p1 < curSymbolPriority) {
                        s1 = "(" + s1 + ")";
                    }
                    if (p2 != -1 && p2 < curSymbolPriority) {
                        s2 = "(" + s2 + ")";
                    }
                    int curIndex = ++index;
                    stack[curIndex] = s1 + s + s2;
                    symbolPriority[curIndex] = curSymbolPriority;
                } else {
                    int curIndex = ++index;
                    stack[curIndex] = s;
                    symbolPriority[curIndex] = -1;
                }
            }
            return stack[0];
        }
    }

    /**
     * 解析表达式 y=x/a+b 中的a、b
     * 顺序可以调整、可以加上括号
     *
     * @param expr
     * @return [a, b]
     */
    public static int[] parseSimpleExpr(String expr) {
        int a = 1;
        int b = 0;
        String[] rpn = parseArithmeticToRPN(expr);
        int stackIndex = -1;
        final Object[] stack = new Object[expr.length()];
        for (String s : rpn) {
            try {
                int v = Integer.parseInt(s);
                stack[++stackIndex] = v;
            } catch (NumberFormatException ex) {
                switch (s) {
                    case "+": {
                        stackIndex--;
                        if (stack[stackIndex] instanceof String) {
                            b = (int) stack[stackIndex + 1];
                            stack[stackIndex] = stack[stackIndex] + "+" + stack[stackIndex + 1];
                        } else if (stack[stackIndex + 1] instanceof String) {
                            b = (int) stack[stackIndex];
                            stack[stackIndex] = stack[stackIndex] + "+" + stack[stackIndex + 1];
                        } else {
                            stack[stackIndex] = (int) stack[stackIndex] + (int) stack[stackIndex + 1];
                        }
                        break;
                    }
                    case "-": {
                        stackIndex--;
                        if (stackIndex == -1) {
                            if (stack[stackIndex + 1] instanceof String) {
                                a = -a;
                                stack[0] = "-" + stack[stackIndex + 1];
                            } else {
                                stack[0] = -(int) stack[stackIndex + 1];
                            }
                            stackIndex = 0;
                        } else {
                            if (stack[stackIndex] instanceof String) {
                                b = -(int) stack[stackIndex + 1];
                                stack[stackIndex] = stack[stackIndex] + "-" + stack[stackIndex + 1];
                            } else if (stack[stackIndex + 1] instanceof String) {
                                b = (int) stack[stackIndex];
                                a = -a;
                                stack[stackIndex] = stack[stackIndex] + "-" + stack[stackIndex + 1];
                            } else {
                                stack[stackIndex] = (int) stack[stackIndex] - (int) stack[stackIndex + 1];
                            }
                        }
                        break;
                    }
                    case "/": {
                        stackIndex--;
                        if (stack[stackIndex] instanceof String) {
                            a = (int) stack[stackIndex + 1];
                            stack[stackIndex] = stack[stackIndex] + "*" + stack[stackIndex + 1];
                        } else if (stack[stackIndex + 1] instanceof String) {
                            a = (int) stack[stackIndex];
                            stack[stackIndex] = stack[stackIndex] + "*" + stack[stackIndex + 1];
                        } else {
                            stack[stackIndex] = (int) stack[stackIndex] * (int) stack[stackIndex + 1];
                        }
                        break;
                    }
                    default: {
                        stack[++stackIndex] = s;
                        break;
                    }
                }
            }
        }

        //处理特殊情况、y=b时候
        if (stack[0] instanceof Integer) {
            a = 0;
            b = (int) stack[0];
        }

        return new int[]{a, b};
    }


    /**
     * 计算 y=x/a+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static byte calc_byte(int[] arr, byte x) {
        return (byte) (x / arr[0] + arr[1]);
    }

    /**
     * 计算 y=x/a+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static long calc_long(int[] arr, long x) {
        return x / arr[0] + arr[1];
    }

    /**
     * 计算 y=x/a+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static int calc_int(int[] arr, int x) {
        return x / arr[0] + arr[1];
    }

    /**
     * 计算 y=x/a+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static short calc_short(int[] arr, short x) {
        return (short) (x / arr[0] + arr[1]);
    }

    /**
     * 计算x=(y-b)*a
     *
     * @param arr
     * @param y
     * @return
     */
    public static byte deCalc_byte(int[] arr, byte y) {
        return (byte) ((y - arr[1]) * arr[0]);
    }

    /**
     * 计算x=(y-b)*a
     *
     * @param arr
     * @param y
     * @return
     */
    public static long deCalc_long(int[] arr, long y) {
        return (y - arr[1]) * arr[0];
    }

    /**
     * 计算x=(y-b)*a
     *
     * @param arr
     * @param y
     * @return
     */
    public static int deCalc_int(int[] arr, int y) {
        return (y - arr[1]) * arr[0];
    }

    /**
     * 计算x=(y-b)/a
     *
     * @param arr
     * @param y
     * @return
     */
    public static short deCalc_short(int[] arr, short y) {
        return (short) ((y - arr[1]) * arr[0]);
    }

    /**
     * 计算 y=ax+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static double calc_double(int[] arr, double x) {
        return arr[0] / x + arr[1];
    }

    /**
     * 计算 y=ax+b
     *
     * @param arr
     * @param x
     * @return
     */
    public static float calc_float(int[] arr, float x) {
        return arr[0] / x + arr[1];
    }


    /**
     * 计算x=(y-b)/a
     *
     * @param arr
     * @param y
     * @return
     */
    public static double deCalc_double(int[] arr, double y) {
        return (y - arr[1]) * arr[0];
    }

    /**
     * 计算x=(y-b)/a
     *
     * @param arr
     * @param y
     * @return
     */
    public static double deCalc_float(int[] arr, float y) {
        return (y - arr[1]) * arr[0];
    }

}
