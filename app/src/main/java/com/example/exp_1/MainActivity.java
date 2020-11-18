package com.example.exp_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Stack;
import java.math.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sign;       //正负号
    private Button point;       //.

    private Button clear;       //清空
    private Button percent;     //百分数
    private Button back;        //回退
    private Button divide;      //除
    private Button multiply;    //乘
    private Button sub;         //减
    private Button plus;        //加
    private Button equal;       //等于

    private Button l_BKT;       //（
    private Button r_BKT;        // ）
    private Button square_Rt;   //平方根

    private TextView et_showExp;
    private TextView et_showIn;
    private String click_op = "";           //记录上一次输入的操作符
    private String oldNum = "";             //记录输入数字
    private int clear_Count = 0;            //点击 C 次数
    private boolean error = false;          //判断上一计算是否出错
    private boolean negative = false;       //判断是否为负数
    private boolean click_pt = false;       //判断是否为点击小数点


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();    //隐藏系统自带的标题栏
        if(actionBar != null){
            actionBar.hide();
        }

        //findViewById---获取布局文件中定义的元素，返回View对象
        //强制转型为Button
        et_showExp = (TextView) findViewById(R.id.et_showExp);
        et_showIn = (TextView) findViewById(R.id.et_showIn);
        //数字1~9，0
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button button0 = (Button) findViewById(R.id.button0);
        sign = (Button) findViewById(R.id.sign);
        point = (Button) findViewById(R.id.point);

        clear = (Button) findViewById(R.id.clear);
        percent = (Button) findViewById(R.id.percent);
        back = (Button) findViewById(R.id.back);
        divide = (Button) findViewById(R.id.divide);
        multiply = (Button) findViewById(R.id.multiply);
        sub = (Button) findViewById(R.id.sub);
        plus = (Button) findViewById(R.id.plus);
        equal = (Button) findViewById(R.id.equal);

        l_BKT = (Button) findViewById(R.id.l_BKT);
        r_BKT = (Button) findViewById(R.id.r_BKT);
        square_Rt = (Button) findViewById(R.id.square_Rt);


        //setOnClickListener---注册监听器
        et_showExp.setOnClickListener(this);
        et_showIn.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);
        sign.setOnClickListener(this);
        point.setOnClickListener(this);

        clear.setOnClickListener(this);
        percent.setOnClickListener(this);
        back.setOnClickListener(this);
        divide.setOnClickListener(this);
        multiply.setOnClickListener(this);
        sub.setOnClickListener(this);
        plus.setOnClickListener(this);
        equal.setOnClickListener(this);

        l_BKT.setOnClickListener(this);
        r_BKT.setOnClickListener(this);
        square_Rt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String str_Exp = et_showExp.getText().toString();
        String str_In = et_showIn.getText().toString();

        switch (v.getId()) {
            case R.id.button0:
                pressNum("0");
                break;

            case R.id.button1:
                pressNum("1");
                break;

            case R.id.button2:
                pressNum("2");
                break;

            case R.id.button3:
                pressNum("3");
                break;

            case R.id.button4:
                pressNum("4");
                break;
            case R.id.button5:
                pressNum("5");
                break;
            case R.id.button6:
                pressNum("6");
                break;

            case R.id.button7:
                pressNum("7");
                break;

            case R.id.button8:
                pressNum("8");
                break;

            case R.id.button9:
                pressNum("9");
                break;

            case R.id.sign:     //  +/-
                if (!click_op.equals("="))   //不能更改上一计算结果
                    pressNum("sign");
                break;

            case R.id.point:
                if(!oldNum.contains(".") && !oldNum.equals(""))
                    pressNum(".");

                break;

            case R.id.clear:
                clear();
                break;

            case R.id.percent:
                if(!click_op.equals("="))   //不能更改上一计算结果
                    pressNum("%");
                break;

            case R.id.back:
                if(click_op.equals("=") || oldNum.equals(""))
                    //显示计算结果 || 尚未输入
                    break;

                oldNum =oldNum.substring(0, oldNum.length()-1);
                et_showIn.setText(oldNum);
                break;

            case R.id.l_BKT:
                pressOp("(");
                break;

            case R.id.r_BKT:
                pressOp(")");
                break;

            case R.id.square_Rt:
                if(!oldNum.equals("") && !negative)
                    sqrt();
                else if(negative){
                    clear();
                    et_showIn.setText("负数不可进行平方根运算");
                }
                break;

            case R.id.divide:
                pressOp("÷");
                break;

            case R.id.multiply:
                pressOp("×");
                break;

            case R.id.sub:
                pressOp("-");
                break;

            case R.id.plus:
                pressOp("+");
                break;

            case R.id.equal:
                pressOp("=");
                break;

        }
    }

    public void pressNum(String num){
        clear_Count = 0;
        if(click_op.equals("="))
            clear();        //清空上一次的计算痕迹

        if(error)       //显示栏报错，重新输入
            error = false;


        if (num.equals("%")) {               //点击%
            if(!oldNum.equals("")){
                BigDecimal x = new BigDecimal(oldNum);
                x = x.divide(new BigDecimal("100"));
                oldNum = x.toString();
            }
        }
        else if (num.equals("sign")) {    //点击 +/-
            if(!oldNum.equals("")){
                if (negative) {        //负数改为正数
                    oldNum = oldNum.substring(1);
                    negative = false;
                } else {              //正数改为负数
                    oldNum = "-" + oldNum;
                    negative = true;
                }
            }
        }
        else
            oldNum += num;

        et_showIn.setText(oldNum);     //更新输入栏信息
    }

    public void pressOp(String op){
        boolean save = negative;
        negative = false;   //初始化下一输入的数字
        if(error)           //前一次计算出错，需先输入数字重新计算
            return;

        String str = "";
        if(click_op.equals("÷") && !oldNum.equals("")) {                //上一个操作符位为除法
            if(Double.valueOf(oldNum) == 0) {     //除数为0
                str = "除数不能为0";
                oldNum = "";
                et_showIn.setText(str);
                error = true;
                return;
            }
        }

        if(click_op.equals("=")){       //接上一计算结果，继续计算
            str = et_showIn.getText().toString() + op;
        }
        else
            str = et_showExp.getText().toString() + oldNum + op;

        if(op.equals("(")){
            str = et_showExp.getText().toString() + op;
            negative = save;
            et_showExp.setText(str);
            return;
        }


        et_showExp.setText(str);

        if(op.equals("=")) {        //计算结果
            Caculator c = new Caculator(str);
            str = c.result();

            if(!str.equals("error")){       //输入算式正确
                if(str.contains("-"))
                    negative = true;
                BigDecimal r =new BigDecimal(str);      //获取结果
                str = r.stripTrailingZeros().toPlainString().toString();    //去除小数中的无效0

            }
            else
                error = true;

            et_showIn.setText(str);     //显示计算结果
        }


        click_op = op;
        click_pt = false;   //初始化下一输入的数字
        oldNum = "";        //操作符后的数字
    }

    public void clear(){
        click_pt = false;
        if(click_op.equals("=")){
            et_showIn.setText("");
            oldNum = "";
            click_op = "";
            et_showExp.setText("");
            clear_Count = 0;
            return;
        }
        if(clear_Count == 0){
            oldNum = "";
            et_showIn.setText(oldNum);
            clear_Count++;
            return;
        }
        else if(clear_Count == 1){
            click_op = "";
            et_showExp.setText("");
            clear_Count--;
            return;
        }
    }

    public void sqrt(){     //计算平方根
        int scale = 15;  //保留小数位数
        BigDecimal value = new BigDecimal(oldNum);
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;    //操作使用的数字个数；结果舍入到此精度
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);  //RoundingMode.HALF_UP：四舍五入
       //封装上下文设置的不可变对象，它描述数字运算符的某些规则（BigDecimal）
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            //（Xn+a/Xn）/2
            cnt++;
        }
        oldNum = deviation.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();

        BigDecimal r =new BigDecimal(oldNum);      //获取结果
        oldNum = r.stripTrailingZeros().toPlainString().toString();    //去除小数中的无效0

        et_showIn.setText(oldNum);
    }
}
