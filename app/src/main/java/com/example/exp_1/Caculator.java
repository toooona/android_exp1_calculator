package com.example.exp_1;


import java.math.BigDecimal;
import java.util.Stack;

public class Caculator {
    private String str;
    private boolean negative = false;   //判断正负数
    private Stack<Character> optr = new Stack<>();      //操作符栈
    private Stack<BigDecimal> opnd = new Stack<>();         //操作数栈
    //BigDecimal 解决double精度不准确问题

    public Caculator(String str){
        this.optr.push('#');//栈底元素
        this.str = str;
    }


    public String result(){
//        evaluate();//筛选
        String show = "";
        char charStr[] = str.toCharArray();
        for (int i = 0; i < str.length(); ++i) {
            if (digit(charStr[i]))              //判断数字
            {
                String num = String.valueOf(charStr[i]);
                while (digit(charStr[i + 1]) || charStr[i + 1] == '.'){
                    num += String.valueOf(charStr[i+1]);
                    i++;
                }
                if(negative) {  //负数
                    num = "-" + num;
                    negative = false;
                }
                opnd.push( new BigDecimal(num) );   //入操作数栈

            }
            else//判断运算符
            {
                if(charStr[i] == '-'){
                    if(i == 0 || !digit(charStr[i-1])) {
                        negative = true;        //负数
                        continue;
                    }
                }
                if (error(optr.peek(), charStr[i]))//判断是否输入错误
                {
                    show = "error";
                    break;
                }
                if (optr.peek() == '#' && charStr[i] == '=')//运算结束
                {
                    if(opnd.empty())
                        show = "error";
                    else
                        show = String.valueOf(opnd.peek());     //弹出最后结果
                    break;
                }
                else if (lp(optr.peek()) < rp(charStr[i]))  //θ2的优先级高
                {
                    optr.push(charStr[i]);      //θ2入optr栈，继续扫描
                }
                else if (lp(optr.peek()) == rp(charStr[i])) //优先级相等（即左右括号相遇）
                {
                    if (charStr[i + 1] == '(')  //若后一字符为左括号，左右括号相反，输入错误
                    {
                        show = "error";
                        break;
                    }
                    optr.pop();     //输入正确，退出optr栈顶元素‘(’，继续扫描
                }
                else      //若θ2的优先级低，则从optr栈退出θ1 ，从opnd栈退出两个操作数b和a ，作运算a θ1 b后将结果压入opnd栈。
                {
                    BigDecimal x, y;
                    if (opnd.empty())       //操作数栈为空
                    {
                        show = "error";     //输入错误
                        break;
                    }
                    else {
                        y = opnd.peek();    //获取操作数y
                        opnd.pop();
                    }

                    if (opnd.empty())       //操作数栈为空
                    {
                        show = "error";     //输入错误
                        break;
                    } else {
                        x = opnd.peek();    //获取操作数x
                        opnd.pop();
                    }
                    opnd.push(operate(optr.peek(), x, y));//压计算结果入操作数栈
                    optr.pop();
                    i--;
                }
            }
        }
        return show;
    }


    private int lp(char op){    //左端优先级
        int level = 0;
        switch(op)
        {
            case '+':
            case '-':
                level = 3;
                break;
            case '×':
            case '÷':
                level = 5;
                break;
            case '(':
                level = 1;
                break;
            case ')':
                level = 6;
                break;
            case '#':
                level = 0;
                break;
        }
        return level;
    }

    private int rp(char op){    //右端优先级
        int level = 0;
        switch(op)
        {
            case '+':
            case '-':
                level = 2;
                break;
            case '×':
            case '÷':
                level = 4;
                break;
            case '(':
                level = 6;
                break;
            case ')':
                level = 1;
                break;
            case '=':
                level = 0;
                break;
        }
        return level;
    }

    private boolean digit(char ch){    //判断是否为数字
        return ch >= '0' && ch <= '9';
    }

    private boolean error(char op, char str){  //判断是否输入错误
        return (lp(op) == 1 && rp(str) == 0) ||     //op='(' , rp='=' ---没有右括号
                (lp(op) == 6 && rp(str) == 6) ||    //左右括号相反
                (lp(op) == 0 && rp(str) == 1);      //op='#' , rp=')' ---没有左括号
    }

    private BigDecimal operate(char theta, BigDecimal x, BigDecimal y){//根据当前运算符进行相应的计算
        BigDecimal result;
        switch(theta)
        {
            case '+':
                result = x.add(y);
                break;
            case '-':
                result = x.subtract(y);
                break;
            case '×':
                result = x.multiply(y);
                break;
            case '÷':
                result = x.divide(y,15,BigDecimal.ROUND_HALF_UP);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + theta);
        }
        return result;
    }


}
