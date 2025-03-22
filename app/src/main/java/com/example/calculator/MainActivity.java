package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView screen;
    private StringBuilder exp = new StringBuilder();
    private int openParentheses = 0;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        screen = findViewById(R.id.screen);
        ArrayList<Button> nums = new ArrayList<>();

        int[] numIds = {R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9};
        for (int id : numIds) {
            Button btn = findViewById(id);
            nums.add(btn);
            btn.setOnClickListener(view -> addToExp(btn.getText().toString()));
        }

        Button decimal = findViewById(R.id.decimal);
        Button reset = findViewById(R.id.reset);
        Button del = findViewById(R.id.del);
        Button par = findViewById(R.id.par);
        Button percent = findViewById(R.id.percent);
        Button div = findViewById(R.id.div);
        Button mul = findViewById(R.id.mul);
        Button sub = findViewById(R.id.sub);
        Button add = findViewById(R.id.add);
        Button equal = findViewById(R.id.equal);
        Button negate = findViewById(R.id.negate);

        decimal.setOnClickListener(view -> addDecimal());
        reset.setOnClickListener(view -> resetExpression());
        del.setOnClickListener(view -> deleteLastCharacter());
        par.setOnClickListener(view -> handleParentheses());
        percent.setOnClickListener(view -> {
            int len = exp.length();
            if (len>0 && (Character.isDigit(exp.charAt(len-1)) || exp.charAt(len-1) == ')' || exp.charAt(len-1) == '!'))
                addToExp("%");
        });
        div.setOnClickListener(view -> addToExpOperatorCheck("÷"));
        mul.setOnClickListener(view -> addToExpOperatorCheck("×"));
        sub.setOnClickListener(view ->addToExpOperatorCheck("-"));
        add.setOnClickListener(view -> addToExpOperatorCheck("+"));
        equal.setOnClickListener(view -> calculateResult());
        negate.setOnClickListener(view -> applyNegate());

        Button pow = findViewById(R.id.pow);
        if (pow != null) {
            pow.setOnClickListener(view -> addToExpOperatorCheck("^"));
        }

        Button factorial = findViewById(R.id.factorial);
        if (factorial != null) {
            factorial.setOnClickListener(view -> addToExpOperatorCheck("!"));
        }
        Button reciprocal = findViewById(R.id.reciprocal);
        if (reciprocal != null) {
            reciprocal.setOnClickListener(view -> applyReciprocal());
        }

        Button sin = findViewById(R.id.sin);
        if (sin != null) {
            sin.setOnClickListener(view -> addToExpParFunc("sin("));
        }

        Button cos = findViewById(R.id.cos);
        if (cos != null) {
            cos.setOnClickListener(view -> addToExpParFunc("cos("));
        }

        Button tan = findViewById(R.id.tan);
        if (tan != null) {
            tan.setOnClickListener(view -> addToExpParFunc("tan("));
        }
        Button sqrt = findViewById(R.id.sqrt);
        if (sqrt != null) {
            sqrt.setOnClickListener(view -> addToExpParFunc("√("));
        }
        Button abs = findViewById(R.id.abs);
        if (abs != null) {
            abs.setOnClickListener(view -> addToExpParFunc("abs("));
        }
        Button log = findViewById(R.id.log);
        if (log != null) {
            log.setOnClickListener(view -> addToExpParFunc("log("));
        }
        Button ln = findViewById(R.id.ln);
        if (ln != null) {
            ln.setOnClickListener(view -> addToExpParFunc("ln("));
        }

    }
    private void revertDefaultViewState(){
        screen.setTextColor(getResources().getColor(R.color.white));
        isResultDisplayed = false;
    }
    private void addToExp(String value) {
        if (isResultDisplayed) {
            if (Character.isDigit(value.charAt(0))) {
                exp.setLength(0);
            }
            revertDefaultViewState();
        }

        exp.append(value);
        screen.setText(exp.toString());
    }
    private void addToExpParFunc(String value){
            if (openParentheses == 0) {
                if (isResultDisplayed){
                    revertDefaultViewState();
                    exp.setLength(0);
                }
                addToExp(value);
                openParentheses++;
            }
    }
    private void addToExpOperatorCheck(String value) {
        int len = exp.length();
        if (len>0 && ((Character.isDigit(exp.charAt(len-1)) || exp.charAt(len-1) == ')'|| exp.charAt(len-1) == '%') || exp.charAt(len-1) == '!'))
            addToExp(value);
    }
    private void addDecimal(){
        if (exp.length()==0){
            addToExp("0.");
        }
        else {
            int len = exp.length();
            if (Character.isDigit(exp.charAt(len-1))) {
                for (int i = len-2;i>=0; i--) {
                    if(!Character.isDigit(exp.charAt(i))){
                        if (exp.charAt(i) == '.')
                            return;
                        break;
                    }
                }
                addToExp(".");
            }
            else if ("+-×÷^".contains(Character.toString(exp.charAt(len - 1)))){
                addToExp("0.");
            }
        }

    }

    private void resetExpression() {
        screen.setTextColor(getResources().getColor(R.color.white));
        exp.setLength(0);
        openParentheses = 0;
        screen.setText("0");
    }

    private void deleteLastCharacter() {
        if (exp.length() > 0) {
            char lastChar = exp.charAt(exp.length() - 1);
            exp.deleteCharAt(exp.length() - 1);
            if (lastChar == '(') openParentheses--;
            if (lastChar == ')') openParentheses++;
            screen.setText(exp.length() > 0 ? exp.toString() : "0");
        }
    }

    private void handleParentheses() {
        if (exp.length()==0){
            addToExp("(");
            openParentheses++;
            return;
        }
        char last = exp.charAt(exp.length() - 1);
        if (openParentheses == 0 && ((exp.length() == 0) || (exp.length() > 0 && "+-×÷^".contains(Character.toString(last))))) {
            addToExp("(");
            openParentheses++;
        }else if(openParentheses==0 && Character.isDigit(last)){
            addToExp("×(");
            openParentheses++;
        }
        else if (openParentheses > 0 && last!='(') {
            addToExp(")");
            openParentheses--;
        }
    }
    private void applyNegate() {
        if (openParentheses==1)
            return;
        if (exp.length() == 0 || "+-×÷^".contains(Character.toString(exp.charAt(exp.length() - 1)))) {
            addToExp("(-");
            openParentheses++;
        } else {
            int i = exp.length() - 1;
            while (i >= 0 && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.')) {
                i--;
            }
            exp.insert(i + 1, "(-");
            exp.append(")");
            openParentheses++;
        }
        screen.setText(exp.toString());
    }
    private void applyReciprocal() {
        if (exp.length() == 0 || "+-×÷^".contains(Character.toString(exp.charAt(exp.length() - 1)))) {
            addToExp("1÷");
        }
        screen.setText(exp.toString());
    }

    private void calculateResult() {
        try {
            if (exp.length()>0 && openParentheses==1){
                openParentheses--;
                addToExp(")");
            }
            double result = evaluateExp(exp.toString());

            DecimalFormat df = new DecimalFormat("#.##");
            String formattedResult = df.format(result);

            if (formattedResult.endsWith(".0")) {
                formattedResult = formattedResult.substring(0, formattedResult.length() - 2);
            }
            screen.setTextColor(getResources().getColor(R.color.green));
            screen.setText(formattedResult);

            exp.setLength(0);
            exp.append(result);
            isResultDisplayed = true;

        } catch (Exception e) {
            screen.setText("error");
            exp.setLength(0);
            isResultDisplayed = false;
        }
    }

    private List<String> getPostFix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        StringBuilder number = new StringBuilder();
        boolean lastWasOperator = true;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                number.append(c);
                lastWasOperator = false;
            }
            else {
                if (number.length() > 0) {
                    output.add(number.toString());
                    number.setLength(0);
                }

                if (c == '-') {
                    if (lastWasOperator) {
                        number.append(c);
                        continue;
                    }
                }

                if (c == '%') {
                    output.add("%");
                } else if (c == '(') {
                    operators.push(c);
                    lastWasOperator = true;
                } else if (c == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        output.add(String.valueOf(operators.pop()));
                    }
                    if (!operators.isEmpty() && operators.peek() == '(') {
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 's') {
                        output.add("sin");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 'c') {
                        output.add("cos");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 't') {
                        output.add("tan");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 'a') {
                        output.add("abs");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == '√') {
                        output.add("√");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 'l') {
                        output.add("log");
                        operators.pop();
                    }
                    if (!operators.isEmpty() && operators.peek() == 'n') {
                        output.add("ln");
                        operators.pop();
                    }
                }
                else if (expr.startsWith("sin", i)) {
                    operators.push('s');
                    i += 2;
                }
                else if (expr.startsWith("cos", i)) {
                    operators.push('c');
                    i += 2;
                }
                else if (expr.startsWith("tan", i)) {
                    operators.push('t');
                    i += 2;
                }
                else if (expr.startsWith("abs", i)) {
                    operators.push('a');
                    i += 2;
                }
                else if (expr.startsWith("√", i)) {
                    operators.push('√');
                }
                else if (expr.startsWith("log", i)) {
                    operators.push('l');
                    i+=2;
                }
                else if (expr.startsWith("ln", i)) {
                    operators.push('n');
                    i++;
                }
                else {
                    while (!operators.isEmpty() && getPrecedence(operators.peek()) >= getPrecedence(c)) {
                        output.add(String.valueOf(operators.pop()));
                    }
                    operators.push(c);
                    lastWasOperator = true;
                }
            }
        }

        if (number.length() > 0) output.add(number.toString());
        while (!operators.isEmpty()) output.add(String.valueOf(operators.pop()));
        return output;
    }


    private int getPrecedence(char op) {
        switch (op) {
            case '^': return 3;
            case '!': return 2;
            case '×':
            case '÷':
                return 1;
            case '+':
            case '-':
                return 0;
            default:
                return -1;
        }
    }

    private double evaluateExp(String expr) {
        List<String> postfix = getPostFix(expr);
        Stack<Double> stack = new Stack<>();

        Toast toast = Toast.makeText(this, postfix.toString(), Toast.LENGTH_LONG);
        toast.show();

        for (String token : postfix) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("%")) {
                stack.push(stack.pop() / 100.0);
            } else if (token.equals("^")) {
                double exponent = stack.pop();
                double base = stack.pop();
                stack.push(Math.pow(base, exponent));
            } else if (token.equals("!")) {
                double number = stack.pop();
                stack.push(factorial(number));
            }
            else if (token.equals("sin")) {
                double n = stack.pop();
                stack.push(Math.sin(Math.toRadians(n)));
            }
            else if (token.equals("cos")) {
                double n = stack.pop();
                stack.push(Math.cos(Math.toRadians(n)));
            }
            else if (token.equals("tan")) {
                double n = stack.pop();
                stack.push(Math.tan(Math.toRadians(n)));
            }
            else if (token.equals("abs")) {
                double n = stack.pop();
                stack.push(Math.abs(n));
            }
            else if (token.equals("√")) {
                double n = stack.pop();
                stack.push(Math.sqrt(n));
            }
            else if (token.equals("log")) {
                double n = stack.pop();
                stack.push(Math.log10(n));
            }
            else if (token.equals("ln")) {
                double n = stack.pop();
                stack.push(Math.log(n));
            }
            else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '×': stack.push(a * b); break;
                    case '÷': stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }

    private double factorial(double num) {
        if (num == 0) return 1;
        return num * factorial(num - 1);
    }
}