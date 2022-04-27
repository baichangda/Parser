package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;

public record ExprCase(int mode, boolean xPositive, int a, int b) {

    public int calc_int(final int x) {
        switch (mode) {
            case 1: {
                return xPositive ? x : -x;
            }
            case 2: {
                return b;
            }
            case 3: {
                return xPositive ? x + b : -x + b;
            }
            case 4: {
                return x * a;
            }
            case 5: {
                return x / a;
            }
            case 6: {
                return xPositive ? (x + b) * a : (-x + b) * a;
            }
            case 7: {
                return xPositive ? (x + b) / a : (-x + b) / a;
            }
            case 8: {
                return x * a - b;
            }
            case 9: {
                return x / a - b;
            }
            default: {
                throw BaseRuntimeException.getException("calc_int mode[{}] not support", mode);
            }
        }
    }


    public long calc_long(final long x) {
        switch (mode) {
            case 1: {
                return xPositive ? x : -x;
            }
            case 2: {
                return b;
            }
            case 3: {
                return xPositive ? x + b : -x + b;
            }
            case 4: {
                return x * a;
            }
            case 5: {
                return x / a;
            }
            case 6: {
                return xPositive ? (x + b) * a : (-x + b) * a;
            }
            case 7: {
                return xPositive ? (x + b) / a : (-x + b) / a;
            }
            case 8: {
                return x * a - b;
            }
            case 9: {
                return x / a - b;
            }
            default: {
                throw BaseRuntimeException.getException("calc_long mode[{}] not support", mode);
            }
        }
    }

    public float calc_float(final float x, final int precision) {
        switch (mode) {
            case 1: {
                return RpnUtil.format_float_float(xPositive ? x : -x, precision);
            }
            case 2: {
                return RpnUtil.format_float_float(b, precision);
            }
            case 3: {
                return RpnUtil.format_float_float(xPositive ? x + b : -x + b, precision);
            }
            case 4: {
                return RpnUtil.format_float_float(x * a, precision);
            }
            case 5: {
                return RpnUtil.format_float_float(x / a, precision);
            }
            case 6: {
                return RpnUtil.format_float_float(xPositive ? (x + b) * a : (-x + b) * a, precision);
            }
            case 7: {
                return RpnUtil.format_float_float(xPositive ? (x + b) / a : (-x + b) / a, precision);
            }
            case 8: {
                return RpnUtil.format_float_float(x * a - b, precision);
            }
            case 9: {
                return RpnUtil.format_float_float(x / a - b, precision);
            }
            default: {
                throw BaseRuntimeException.getException("calc_float mode[{}] not support", mode);
            }
        }
    }

    public double calc_double(final double x, final int precision) {
        switch (mode) {
            case 1: {
                return RpnUtil.format_double_double(xPositive ? x : -x, precision);
            }
            case 2: {
                return RpnUtil.format_double_double(b, precision);
            }
            case 3: {
                return RpnUtil.format_double_double(xPositive ? x + b : -x + b, precision);
            }
            case 4: {
                return RpnUtil.format_double_double(x * a, precision);
            }
            case 5: {
                return RpnUtil.format_double_double(x / a, precision);
            }
            case 6: {
                return RpnUtil.format_double_double(xPositive ? (x + b) * a : (-x + b) * a, precision);
            }
            case 7: {
                return RpnUtil.format_double_double(xPositive ? (x + b) / a : (-x + b) / a, precision);
            }
            case 8: {
                return RpnUtil.format_double_double(x * a - b, precision);
            }
            case 9: {
                return RpnUtil.format_double_double(x / a - b, precision);
            }
            default: {
                throw BaseRuntimeException.getException("calc_double mode[{}] not support", mode);
            }
        }
    }

    public int deCalc_int(final int y) {
        switch (mode) {
            case 1: {
                return xPositive ? y : -y;
            }
            case 2: {
                return b;
            }
            case 3: {
                return xPositive ? y - b : b - y;
            }
            case 4: {
                return y / a;
            }
            case 5: {
                return y * a;
            }
            case 6: {
                return xPositive ? y / a - b : b - y / a;
            }
            case 7: {
                return xPositive ? y * a - b : b - y * a;
            }
            case 8: {
                return (y - b) / a;
            }
            case 9: {
                return (y - b) * a;
            }
            default: {
                throw BaseRuntimeException.getException("deCalc_int mode[{}] not support", mode);
            }
        }
    }

    public long deCalc_long(final long y) {
        switch (mode) {
            case 1: {
                return xPositive ? y : -y;
            }
            case 2: {
                return b;
            }
            case 3: {
                return xPositive ? y - b : b - y;
            }
            case 4: {
                return y / a;
            }
            case 5: {
                return y * a;
            }
            case 6: {
                return xPositive ? y / a - b : b - y / a;
            }
            case 7: {
                return xPositive ? y * a - b : b - y * a;
            }
            case 8: {
                return (y - b) / a;
            }
            case 9: {
                return (y - b) * a;
            }
            default: {
                throw BaseRuntimeException.getException("deCalc_long mode[{}] not support", mode);
            }
        }
    }

    public int deCalc_float(final float y) {
        switch (mode) {
            case 1: {
                return RpnUtil.format_float_int(xPositive ? y : -y);
            }
            case 2: {
                return RpnUtil.format_float_int(b);
            }
            case 3: {
                return RpnUtil.format_float_int(xPositive ? y - b : b - y);
            }
            case 4: {
                return RpnUtil.format_float_int(y / a);
            }
            case 5: {
                return RpnUtil.format_float_int(y * a);
            }
            case 6: {
                return RpnUtil.format_float_int(xPositive ? y / a - b : b - y / a);
            }
            case 7: {
                return RpnUtil.format_float_int(xPositive ? y * a - b : b - y * a);
            }
            case 8: {
                return RpnUtil.format_float_int((y - b) / a);
            }
            case 9: {
                return RpnUtil.format_float_int((y - b) * a);
            }
            default: {
                throw BaseRuntimeException.getException("deCalc_int mode[{}] not support", mode);
            }
        }
    }

    public long deCalc_double(final double y) {
        switch (mode) {
            case 1: {
                return RpnUtil.format_double_long(xPositive ? y : -y);
            }
            case 2: {
                return b;
            }
            case 3: {
                return RpnUtil.format_double_long(xPositive ? y - b : b - y);
            }
            case 4: {
                return RpnUtil.format_double_long(y / a);
            }
            case 5: {
                return RpnUtil.format_double_long(y * a);
            }
            case 6: {
                return RpnUtil.format_double_long(xPositive ? y / a - b : b - y / a);
            }
            case 7: {
                return RpnUtil.format_double_long(xPositive ? y * a - b : b - y * a);
            }
            case 8: {
                return RpnUtil.format_double_long((y - b) / a);
            }
            case 9: {
                return RpnUtil.format_double_long((y - b) * a);
            }
            default: {
                throw BaseRuntimeException.getException("deCalc_long mode[{}] not support", mode);
            }
        }
    }


    private static ExprCase case1(boolean xPositive) {
        return new ExprCase(1, xPositive, 0, 0);
    }

    private static ExprCase case2(int b) {
        return new ExprCase(2, false, 0, b);
    }

    private static ExprCase case3(boolean xPositive, int b) {
        return new ExprCase(3, xPositive, 0, b);
    }

    private static ExprCase case4(int a) {
        return new ExprCase(4, true, a, 0);
    }

    private static ExprCase case5(int a) {
        return new ExprCase(5, true, a, 0);
    }

    private static ExprCase case6(boolean xPositive, int a, int b) {
        return new ExprCase(6, xPositive, a, b);
    }

    private static ExprCase case7(boolean xPositive, int a, int b) {
        return new ExprCase(7, xPositive, a, b);
    }

    private static ExprCase case8(int a, int b) {
        return new ExprCase(8, true, a, b);
    }

    private static ExprCase case9(int a, int b) {
        return new ExprCase(9, true, a, b);
    }

    public String toString() {
        return mode + "," + xPositive + "," + a + "," + b;
    }

    public static ExprCase from(String expr) {
        String[] rpn = RpnUtil.toRpn(expr);
        final int length = rpn.length;
        switch (length) {
            case 1: {
                try {
                    final int v = Integer.parseInt(rpn[0]);
                    //为常量
                    return ExprCase.case2(v);
                } catch (NumberFormatException ex) {
                    if (rpn[0].charAt(0) == '-') {
                        return ExprCase.case1(false);
                    } else {
                        return ExprCase.case1(true);
                    }
                }
            }
            case 3: {
                boolean varFirst;
                String var;
                int num;
                try {
                    num = Integer.parseInt(rpn[1]);
                    var = rpn[0];
                    varFirst = true;
                } catch (NumberFormatException ex) {
                    num = Integer.parseInt(rpn[0]);
                    var = rpn[1];
                    varFirst = false;
                }
                boolean xPositive;
                if (varFirst) {
                    xPositive = var.charAt(0) != '-';
                } else {
                    xPositive = !rpn[2].equals("-");
                }
                if (varFirst && rpn[2].equals("-")) {
                    num = -num;
                }
                switch (rpn[2]) {
                    case "+":
                    case "-": {
                        return ExprCase.case3(xPositive, num);
                    }
                    case "*": {
                        return ExprCase.case4(xPositive ? num : -num);
                    }
                    case "/": {
                        return ExprCase.case5(xPositive ? num : -num);
                    }
                    default: {
                        throw BaseRuntimeException.getException("toExprVar[{}] not support", expr);
                    }
                }
            }
            case 5: {
                switch (rpn[2]) {
                    case "+":
                    case "-": {
                        boolean varFirst;
                        String var;
                        int b;
                        int a = Integer.parseInt(rpn[3]);
                        try {
                            b = Integer.parseInt(rpn[1]);
                            var = rpn[0];
                            varFirst = true;
                        } catch (NumberFormatException ex) {
                            b = Integer.parseInt(rpn[0]);
                            var = rpn[1];
                            varFirst = false;
                        }
                        boolean xPositive;
                        if (varFirst) {
                            xPositive = var.charAt(0) != '-';
                        } else {
                            xPositive = !rpn[2].equals("-");
                        }
                        if (varFirst && rpn[2].equals("-")) {
                            b = -b;
                        }
                        switch (rpn[4]) {
                            case "*": {
                                return ExprCase.case6(xPositive, a, b);
                            }
                            case "/": {
                                return ExprCase.case7(xPositive, a, b);
                            }
                            default: {
                                throw BaseRuntimeException.getException("toExprVar[{}] not support", expr);
                            }
                        }
                    }
                    case "*": {
                        String var = rpn[0];
                        int a = Integer.parseInt(rpn[1]);
                        int b = Integer.parseInt(rpn[3]);
                        boolean xPositive = var.charAt(0) != '-';
                        if (rpn[4].equals("-")) {
                            b = -b;
                        }
                        return ExprCase.case8(xPositive ? a : -a, b);
                    }
                    case "/": {
                        String var = rpn[0];
                        int a = Integer.parseInt(rpn[1]);
                        int b = Integer.parseInt(rpn[3]);
                        boolean xPositive = var.charAt(0) != '-';
                        if (rpn[4].equals("-")) {
                            b = -b;
                        }
                        return ExprCase.case9(xPositive ? a : -a, b);
                    }
                    default: {
                        throw BaseRuntimeException.getException("toExprVar[{}] not support", expr);
                    }
                }
            }
            default: {
                throw BaseRuntimeException.getException("toExprVar[{}] not support", expr);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(ExprCase.from("(x+100)/1000"));
        System.out.println(ExprCase.from("(-x+100)/1000"));
        System.out.println(ExprCase.from("(x-100)/1000"));
        System.out.println(ExprCase.from("(100+x)*1000"));
        System.out.println(ExprCase.from("(100-x)*1000"));
        System.out.println(ExprCase.from("x+100"));
        System.out.println(ExprCase.from("x-100"));
        System.out.println(ExprCase.from("-x*100"));
        System.out.println(ExprCase.from("x/100"));
        System.out.println(ExprCase.from("-x"));
        System.out.println(ExprCase.from("x"));
        System.out.println(ExprCase.from("100"));
        System.out.println(ExprCase.from("-100"));
        System.out.println(ExprCase.from("(100-x)*-1000"));
        System.out.println(ExprCase.from("-1-x"));
        System.out.println(ExprCase.from("x*100-1"));
        System.out.println(ExprCase.from("-x/100-1"));
        System.out.println(ExprCase.from("(-x/100)+10"));
        System.out.println((int) ExprCase.from("(x/10)").deCalc_double(381.3d));
    }
}