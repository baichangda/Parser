package com.bcd.parser.util;

import com.bcd.parser.exception.BaseRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RpnUtil {
    /**
     * 处理rpn表达式集合
     * 字符串变量 --> char
     * 将数字字符串 --> double
     * @param rpn rpn表达式集合
     * @return
     */
    public static Object[] doWithRpnList_char_double(String[] rpn){
        return Arrays.stream(rpn).map(e->{
            try {
                return Double.parseDouble(e);
            }catch (NumberFormatException ex){
                return e.charAt(0);
            }
        }).toArray();
    }

    /**
     * 处理rpn表达式集合
     * 字符串变量 --> char
     * 将数字字符串 --> int
     * @param rpn rpn表达式集合
     * @return
     */
    public static Object[] doWithRpnList_char_int(String[] rpn){
        return Arrays.stream(rpn).map(e->{
            try {
                return Integer.parseInt(e);
            }catch (NumberFormatException ex){
                return e.charAt(0);
            }
        }).toArray();
    }

    /**
     * list中变量定义必须是char 支持 a-z A-Z
     *
     * A-Z --> 65-90
     * a-z --> 97-122
     * 所以char数组长度为 65-122 长度为58
     * 同时需要进行偏移量计算也就是 字符-65
     *
     * @param rpn rpn表达式集合,其中变量必须是char,常量必须是int
     * @param vals 变量对应值数组,取值规则为 vals[int(char)-offset]
     * @param offset 代表char对应的数字在vals的偏移量
     * @return
     */
    public static int calcRPN_char_int(Object[] rpn, int[] vals,int offset){
        int stackIndex=-1;
        int[] stack=new int[2];
        for (Object s : rpn) {
            if(s instanceof Integer){
                stack[++stackIndex]=(int)s;
            }else {
                switch ((char)s) {
                    case '+': {
                        int num2 = stack[stackIndex--];
                        int num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 + num2;
                        break;
                    }
                    case '-': {
                        int num2 = stack[stackIndex--];
                        int num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 - num2;
                        break;
                    }
                    case '*': {
                        int num2 = stack[stackIndex--];
                        int num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 * num2;
                        break;
                    }
                    case '/': {
                        int num2 = stack[stackIndex--];
                        int num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 / num2;
                        break;
                    }
                    default: {
                        int val = vals[(char)s-offset];
                        stack[++stackIndex] = val;
                        break;
                    }
                }
            }
        }
        return stack[0];
    }

    /**
     * 计算逆波兰表达式
     * 只有一个变量
     * @param rpn 逆波兰表达式集合,其中变量必须是char,常量必须是double
     * @param var 变量值
     * @param precision 精度
     * @return
     */
    public static double calcRPN_char_double_singleVar(Object[] rpn, double var,int precision){
        int stackIndex=-1;
        double[] stack=new double[2];
        for (Object o : rpn) {
            if(o instanceof Double){
                stack[++stackIndex] = (double)o;
            }else {
                switch ((char)o) {
                    case '+': {
                        double num2 = stack[stackIndex--];
                        double num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 + num2;
                        break;
                    }
                    case '-': {
                        double num2 = stack[stackIndex--];
                        double num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 - num2;
                        break;
                    }
                    case '*': {
                        double num2 = stack[stackIndex--];
                        double num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 * num2;
                        break;
                    }
                    case '/': {
                        double num2 = stack[stackIndex--];
                        double num1 = stack[stackIndex--];
                        stack[++stackIndex] = num1 / num2;
                        break;
                    }
                    default: {
                        stack[++stackIndex] = var;
                        break;
                    }
                }
            }
        }
        if(stack[0]>0){
            double pow=Math.pow(10,precision);
            return Math.round(stack[0]*pow)/pow;
        }else if(stack[0]<0){
            double pow=Math.pow(10,precision);
            return -Math.round(-stack[0]*pow)/pow;
        }else{
            return stack[0];
        }
    }

    /**
     * 将算数字符串转换成逆波兰表达式
     * 算数支持 + - * / ( ) 符号
     * @return
     */
    public static String[] parseArithmeticToRPN(String str){
        List<String> output=new ArrayList<>();
        int stackIndex=-1;
        char[] stack=new char[str.length()];
        char[] arr= str.toCharArray();
        StringBuilder temp=new StringBuilder();
        for(int i=0;i<=arr.length-1;i++){
            if(arr[i]=='+'||arr[i]=='-'||arr[i]=='*'||arr[i]=='/'){
                if(temp.length()>0) {
                    output.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                if(stackIndex>=0){
                    while(stack[stackIndex]!='('&&get(stack[stackIndex])>=get(arr[i])){
                        output.add(String.valueOf(stack[stackIndex--]));
                        if(stackIndex==-1){
                            break;
                        }
                    }
                }
                stack[++stackIndex]=arr[i];
            }else if(arr[i]=='('){
                stack[++stackIndex]=arr[i];
            }else if(arr[i]==')'){
                if(temp.length()>0) {
                    output.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                while(stackIndex>=0){
                    char c= stack[stackIndex--];
                    if(c=='('){
                        break;
                    }
                    output.add(String.valueOf(c));
                }
            }else{
                temp.append(arr[i]);
            }
        }

        if(temp.length()>0){
            output.add(temp.toString());
        }

        for(int i=stackIndex;i>=0;i--){
            output.add(String.valueOf(stack[i]));
        }

        return output.toArray(new String[0]);
    }

    /**
     * 获取字符优先级
     * @param c
     * @return
     */
    private static int get(char c){
        switch (c){
            case '+':{
                return 1;
            }
            case '-':{
                return 1;
            }
            case '*':{
                return 2;
            }
            case '/':{
                return 2;
            }
            default:{
                throw BaseRuntimeException.getException("symbol["+c+"] not support");
            }
        }
    }

    /**
     * 解y=ax+b
     * 转换成x=(y-b)/a
     * 其中a、b皆为常量 a!=0
     * example:
     * y=0.1x+100 --> x=(y-100)/0.1 此时rpn长度为5
     * y=0.1x --> x=y/0.1 此时rpn长度为3
     * y=x --> x=y 此时rpn长度为1
     *
     * @param rpn
     * @return
     */
    public static String[] reverseSimpleRPN(String[] rpn){
        switch (rpn.length){
            case 1:{
                return new String[]{rpn[0]};
            }
            case 3:{
                String[] res=new String[3];
                res[0]=rpn[0];
                res[1]=rpn[1];
                res[2]=reverseSymbol(rpn[2]);
                return res;
            }
            case 5:{
                String[] res=new String[5];
                res[0]=rpn[0];
                res[1]=rpn[3];
                res[2]=reverseSymbol(rpn[4]);
                res[3]=rpn[1];
                res[4]=reverseSymbol(rpn[2]);
                return res;
            }
            default:{
                throw BaseRuntimeException.getException("rpn[{0}] not support",Arrays.toString(rpn));
            }
        }
    }

    private static String reverseSymbol(String symbol){
        switch (symbol){
            case "+":{
                return "-";
            }
            case "-":{
                return "+";
            }
            case "*":{
                return "/";
            }
            case "/":{
                return "*";
            }
            default:{
                throw BaseRuntimeException.getException("reverseSymbol[{0}] not support",symbol);
            }
        }
    }

    public static void main(String[] args) {
        String[] res1=parseArithmeticToRPN("x*0.01-1000");
        System.out.println(Arrays.toString(res1));
        System.out.println(Arrays.toString(reverseSimpleRPN(res1)));
        String[] res2=parseArithmeticToRPN("(y+1000)/0.01");
        System.out.println(Arrays.toString(res2));
    }
}
