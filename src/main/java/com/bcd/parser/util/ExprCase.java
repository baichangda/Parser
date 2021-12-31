package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;

public final class ExprCase {
    /**
     * 1、x
     * 2、b
     * 3、x+b
     * 4、x*a
     * 5、x/a
     * 6、(x+b)*a
     * 7、(x+b)/a
     * 8、x*a+b
     * 9、x/a+b
     */
    public final int mode;
    public final boolean xPositive;
    public final int a;
    public final int b;

    private ExprCase(int mode, boolean xPositive, int a, int b) {
        this.mode = mode;
        this.xPositive = xPositive;
        this.a = a;
        this.b = b;
    }

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

    public double calc_double(final double x) {
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

    public double deCalc_double(final double y) {
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

    private static ExprCase case1(boolean xPositive) {
        return new ExprCase(1, xPositive, 0, 0);
    }

    private static ExprCase case2(int b) {
        return new ExprCase(2, false, 0, b);
    }

    private static ExprCase case3(boolean xPositive, int b) {
        return new ExprCase(3, xPositive, 0, b);
    }

    private static ExprCase case4(boolean xPositive, int a) {
        return new ExprCase(4, xPositive, a, 0);
    }

    private static ExprCase case5(boolean xPositive, int a) {
        return new ExprCase(5, xPositive, a, 0);
    }

    private static ExprCase case6(boolean xPositive, int a, int b) {
        return new ExprCase(6, xPositive, a, b);
    }

    private static ExprCase case7(boolean xPositive, int a, int b) {
        return new ExprCase(7, xPositive, a, b);
    }

    private static ExprCase case8(boolean xPositive, int a, int b) {
        return new ExprCase(8, xPositive, a, b);
    }

    private static ExprCase case9(boolean xPositive, int a, int b) {
        return new ExprCase(9, xPositive, a, b);
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
                        return ExprCase.case4(true, xPositive ? num : -num);
                    }
                    case "/": {
                        return ExprCase.case5(true, xPositive ? num : -num);
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
                        return ExprCase.case8(true, xPositive ? a : -a, b);
                    }
                    case "/": {
                        String var = rpn[0];
                        int a = Integer.parseInt(rpn[1]);
                        int b = Integer.parseInt(rpn[3]);
                        boolean xPositive = var.charAt(0) != '-';
                        if (rpn[4].equals("-")) {
                            b = -b;
                        }
                        return ExprCase.case9(true, xPositive ? a : -a, b);
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
    }
}