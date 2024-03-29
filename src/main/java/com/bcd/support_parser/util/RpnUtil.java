package com.bcd.support_parser.util;


import com.bcd.support_parser.exception.BaseRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RpnUtil {

    private final static double[] pows_double = new double[10];
    private final static float[] pows_float = new float[10];

    static {
        for (int i = 0; i < pows_double.length; i++) {
            pows_double[i] = Math.pow(10, i);
            pows_float[i] = (float) pows_double[i];
        }
    }

    public static long format_double_long(final double v) {
        if (v > 0) {
            return Math.round(v);
        } else if (v <= 0) {
            return -Math.round(-v);
        } else {
            return 0L;
        }
    }

    public static int format_float_int(final float v) {
        if (v > 0) {
            return Math.round(v);
        } else if (v <= 0) {
            return -Math.round(-v);
        } else {
            return 0;
        }
    }


    public static double format_double_double(final double v, final int precision) {
        if (v > 0) {
            final double pow = pows_double[precision];
            return Math.round(v * pow) / pow;
        } else if (v <= 0) {
            final double pow = pows_double[precision];
            return -Math.round(-v * pow) / pow;
        } else {
            return 0d;
        }
    }

    public static float format_float_float(final float v, final int precision) {
        if (v > 0) {
            final float pow = pows_float[precision];
            return Math.round(v * pow) / pow;
        } else if (v <= 0) {
            final float pow = pows_float[precision];
            return -Math.round(-v * pow) / pow;
        } else {
            return 0f;
        }
    }

    public final static class Ele_int {
        public final int type;
        public final int val;

        public Ele_int(int type, int val) {
            this.type = type;
            this.val = val;
        }
    }

    public final static class Ele_double {
        /**
         * 1: 数字常量
         * 2: 正数字变量
         * 3: 负数字变量
         * 4: +
         * 5: -
         * 6: *
         * 7: /
         */
        public final int type;
        public final double val_double;
        public final int val_int;

        public Ele_double(int type, double val_double) {
            this.type = type;
            this.val_double = val_double;
            this.val_int = (int) val_double;
        }

        public Ele_double(int type, int val_int) {
            this.type = type;
            this.val_int = val_int;
            this.val_double = val_int;
        }
    }

    /**
     * 处理rpn表达式集合、不同类型值转换为不同对象
     *
     * @param rpn rpn表达式集合
     * @return
     */
    public static Ele_double[] to_ele_double(String[] rpn) {
        return Arrays.stream(rpn).map(e -> {
            try {
                return new Ele_double(1, Double.parseDouble(e));
            } catch (NumberFormatException ex) {
                if (e.length() == 1) {
                    char c = e.charAt(0);
                    switch (c) {
                        case '+' : {
                            return new Ele_double(4, 0);
                        }
                        case '-' : {
                            return new Ele_double(5, 0);
                        }
                        case '*' : {
                            return new Ele_double(6, 0);
                        }
                        case '/' : {
                            return new Ele_double(7, 0);
                        }
                        default : {
                            return new Ele_double(2, c);
                        }
                    }
                } else {
                    return new Ele_double(3, e.charAt(1));
                }
            }
        }).toArray(Ele_double[]::new);
    }

    /**
     * 处理rpn表达式集合、不同类型值转换为不同对象
     *
     * @param rpn rpn表达式集合
     * @return
     */
    public static Ele_int[] to_ele_int(final String[] rpn) {
        return Arrays.stream(rpn).map(e -> {
            try {
                return new Ele_int(1, Integer.parseInt(e));
            } catch (NumberFormatException ex) {
                if (e.length() == 1) {
                    final char c = e.charAt(0);
                    switch (c) {
                        case '+' : {
                            return new Ele_int(4, 0);
                        }
                        case '-' : {
                            return new Ele_int(5, 0);
                        }
                        case '*' : {
                            return new Ele_int(6, 0);
                        }
                        case '/' : {
                            return new Ele_int(7, 0);
                        }
                        default : {
                            return new Ele_int(2, c);
                        }
                    }
                } else {
                    return new Ele_int(3, e.charAt(1));
                }
            }
        }).toArray(Ele_int[]::new);
    }

    /**
     * 计算rpn表达式
     *
     * @param rpn  rpn表达式集合
     * @param vals 变量对应值数组,取值规则为 vals[int(char)]
     * @return
     */
    public static int calc_int(final Ele_int[] rpn, final int[] vals) {
        if (rpn.length == 1) {
            final Ele_int first = rpn[0];
            switch (first.type) {
                case 1 : {
                    return first.val;
                }
                case 2 : {
                    return vals[first.val];
                }
                case 3 : {
                    return -vals[first.val];
                }
                default : {
                    throw BaseRuntimeException.getException("error single type[{}] val[{}]", first.type, first.val);
                }
            }
        } else {
            int stackIndex = -1;
            final int[] stack = new int[rpn.length];
            for (Ele_int e : rpn) {
                switch (e.type) {
                    case 1 : {
                        stack[++stackIndex] = e.val;
                        break;
                    }
                    case 2 : {
                        stack[++stackIndex] = vals[e.val];
                        break;
                    }
                    case 3 : {
                        stack[++stackIndex] = -vals[e.val];
                        break;
                    }
                    case 4 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] + stack[stackIndex + 1];
                        break;
                    }
                    case 5 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] - stack[stackIndex + 1];
                        break;
                    }
                    case 6 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] * stack[stackIndex + 1];
                        break;
                    }
                    case 7 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] / stack[stackIndex + 1];
                        break;
                    }
                }
            }
            return stack[0];
        }
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
    public static double calc_double(final Ele_double[] rpn, final double[] vals) {
        if (rpn.length == 1) {
            final Ele_double first = rpn[0];
            switch (first.type) {
                case 1 : {
                    return first.val_double;
                }
                case 2 : {
                    return vals[first.val_int];
                }
                case 3 : {
                    return -vals[first.val_int];
                }
                default : {
                    throw BaseRuntimeException.getException("error single type[{}] val_int[{}] val_double[{}]", first.type, first.val_int, first.val_double);
                }
            }
        } else {
            int stackIndex = -1;
            final double[] stack = new double[rpn.length];
            for (Ele_double e : rpn) {
                switch (e.type) {
                    case 1 : {
                        stack[++stackIndex] = e.val_double;
                        break;
                    }
                    case 2 : {
                        stack[++stackIndex] = vals[e.val_int];
                        break;
                    }
                    case 3 : {
                        stack[++stackIndex] = -vals[e.val_int];
                        break;
                    }
                    case 4 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] + stack[stackIndex + 1];
                        break;
                    }
                    case 5 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] - stack[stackIndex + 1];
                        break;
                    }
                    case 6 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] * stack[stackIndex + 1];
                        break;
                    }
                    case 7 : {
                        stackIndex--;
                        stack[stackIndex] = stack[stackIndex] / stack[stackIndex + 1];
                        break;
                    }
                }
            }
            return stack[0];
        }
    }


    /**
     * 将算数字符串转换成逆波兰表达式
     * 算数支持 + - * / ( ) 符号
     *
     * @return
     */
    public static String[] toRpn(final String str) {
        List<String> output = new ArrayList<>();
        int stackIndex = -1;
        final char[] stack = new char[str.length()];
        final char[] arr = str.toCharArray();
        final StringBuilder temp = new StringBuilder();
        boolean nextNegative = false;
        for (int i = 0; i <= arr.length - 1; i++) {
            char cur = arr[i];
            if (cur == '+' || cur == '-' || cur == '*' || cur == '/') {
                /**
                 * 判断是否有num或者var
                 */
                if (temp.length() > 0) {
                    output.add(temp.toString());
                    temp.delete(0, temp.length());
                    /**
                     * 开始回溯符号栈、将优先级高于当前符号的符号弹出、视为优先运算
                     */
                    if (stackIndex >= 0) {
                        while (stack[stackIndex] != '(' && getSymbolPriority(stack[stackIndex]) >= getSymbolPriority(cur)) {
                            output.add(String.valueOf(stack[stackIndex--]));
                            if (stackIndex == -1) {
                                break;
                            }
                        }
                    }
                    stack[++stackIndex] = cur;
                } else {
                    /**
                     * 没有num和var、说明运算符左边没有变量、此时有两种情况
                     * 1: 左边的变量是一个()包住的表达式
                     * 2: 这个符号为-、是对下一个值取负
                     * 其他情况说明异常
                     */
                    if (i > 0 && arr[i - 1] == ')') {
                        /**
                         * 没有num和var且当前符号不为负号、则说明可能刚结束了一个()导致变量被清空
                         * 开始回溯符号栈、将优先级高于当前符号的符号弹出、视为优先运算
                         */
                        if (stackIndex >= 0) {
                            while (stack[stackIndex] != '(' && getSymbolPriority(stack[stackIndex]) >= getSymbolPriority(cur)) {
                                output.add(String.valueOf(stack[stackIndex--]));
                                if (stackIndex == -1) {
                                    break;
                                }
                            }
                        }
                        stack[++stackIndex] = cur;
                    } else {
                        if (cur == '-') {
                            /**
                             * 没有num和var且当前符号为负号、说明此时是对下一个数取负
                             */
                            if (nextNegative) {
                                //负负得正
                                nextNegative = false;
                            } else {
                                nextNegative = true;
                            }
                        } else {
                            throw BaseRuntimeException.getException("parse error on index[{}]、operator[{}] left has no var or value", i, cur);
                        }
                    }

                }
            } else if (cur == '(') {
                /**
                 * 如果遇到()、则将其中的表达式视为独立
                 * 截取出其中的字符串、递归调用
                 * 最后添加到结果中
                 */
                int count = 0;
                int end = -1;
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] == '(') {
                        count++;
                    } else {
                        if (arr[j] == ')') {
                            if (count == 0) {
                                end = j;
                                break;
                            } else {
                                count--;
                            }
                        }
                    }
                }
                String[] curRes = toRpn(new String(arr, i + 1, end - i - 1));
                /**
                 * 如果括号外面为负号则
                 * -num -> num
                 * num -> -num
                 * -var -> var
                 * var -> -var
                 */
                if (nextNegative) {
                    for (String s : curRes) {
                        switch (s) {
                            case "+":
                            case "-":
                            case "*":
                            case "/": {
                                output.add(s);
                                break;
                            }
                            default: {
                                if (s.charAt(0) == '-') {
                                    output.add(s.substring(1));
                                } else {
                                    output.add("-" + s);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    output.addAll(Arrays.asList(curRes));
                }
                i = end;
            } else {
                /**
                 * 如果有负数标识、则先打上负号
                 */
                if (nextNegative) {
                    temp.append("-");
                    nextNegative = false;
                }
                temp.append(cur);
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
    private static int getSymbolPriority(final char c) {
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

    /**
     * 将rpn转换为数学表达式
     *
     * @param rpn
     * @return
     */
    public static String toExpr(final String[] rpn) {
        if (rpn.length == 1) {
            return rpn[0];
        } else {
            String[] stack = new String[rpn.length];
            int[] symbolPriority = new int[rpn.length];
            int index = -1;
            for (String s : rpn) {
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
                    if ((p2 != -1 && p2 < curSymbolPriority) ||
                            s.equals("-") && s2.charAt(0) != '(' && (s2.contains("+") || s2.contains("-"))
                    ) {
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


    public static void main(String[] args) {
//        System.err.println(parseRPNToArithmetic(parseArithmeticToRPN("-(a-(b+(c)))")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("-(a-(b+(c)))")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN(parseRPNToArithmetic(parseArithmeticToRPN("-(a-(b+(c)))")))));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("---4")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("--(a/-3-4)--d")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("1-4")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("(a-(b+(c)))")));
//        System.err.println(Arrays.toString(parseArithmeticToRPN("(a-(b+(c)))")));
        System.out.println(ExprCase.from("(x+100)/1000"));
        System.out.println(ExprCase.from("(-x+100)/1000"));
        System.out.println(ExprCase.from("(x-100)/1000"));
        System.out.println(ExprCase.from("(100+x)*1000"));
        System.out.println(ExprCase.from("(100-x)*1000"));
        System.out.println(ExprCase.from("x+100"));
        System.out.println(ExprCase.from("x-100"));
        System.out.println(ExprCase.from("x*100"));
        System.out.println(ExprCase.from("x/100"));
        System.out.println(ExprCase.from("-x"));
        System.out.println(ExprCase.from("x"));
        System.out.println(ExprCase.from("100"));
        System.out.println(ExprCase.from("-100"));
        System.out.println(ExprCase.from("(100-x)*-1000"));
        System.out.println(ExprCase.from("-1-x"));
    }


}
