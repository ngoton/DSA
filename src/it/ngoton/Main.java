package it.ngoton;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------- Testing My Math --------------");
        System.out.println(MyMath.sqrt(9));

        System.out.println("---------- Testing My BigDecimal --------------");
        System.out.println(new MyBigDecimal("-0.12345").add(new MyBigDecimal("0.345")));
        System.out.println(new BigDecimal("-0.12345").add(new BigDecimal("0.345")));
        System.out.println(new MyBigDecimal("-0.12345").subtract(new MyBigDecimal("-0.345")));
        System.out.println(new BigDecimal("-0.12345").subtract(new BigDecimal("-0.345")));
        System.out.println(new MyBigDecimal("12345").multiply(new MyBigDecimal("-0.345")));
        System.out.println(new BigDecimal("12345").multiply(new BigDecimal("-0.345")));
        System.out.println(new BigDecimal("123.45").divide(new BigDecimal("-2.3451212121211"), 10, RoundingMode.DOWN));
        System.out.println(new MyBigDecimal("123.45").divide(new MyBigDecimal("-2.3451212121211")));


        System.out.println("---------- Testing My BigInteger --------------");
        System.out.println(new BigInteger("10000000000000").sqrt());
        System.out.println(new BigInteger("-10").mod(new BigInteger("3")));

        testMyExpressions();

        System.out.println("---------- Testing Factorial --------------");
        for(int i = 80; i < 100 ; i++)
            System.out.println("fact("+i+"):= " + MyBigInteger.factorial(i).toString());

        System.out.println("Testing Recursive Power");
        for(int i = 0; i < 300; i += 10 )
        {
            String result = new MyBigInteger(2).pow(i).toString();
            System.out.println("2 ^ " + i + " = " + result);

        }
    }
    private static void testMyExpressions() {
        String[] expressions = {
                "0 - 5",
                "-3 - 5",
                "-3 - -5",
                "-20 + -5",
                "-20 + 5",
                "20 + -5",
                "20 + 5",
                "-12 * 4",
                "-10 / 3",
                "-2 / 3",
                "20 / -3",
                "123450000000000 / 2345",
                "-10 % 3",
                "102 ^ -2",
                "71045943470 * 41564635484",
                "316227766 * 316227766",
                "-12345 + 34500",
                MyBigInteger.factorial(20) + " * 10",
                MyBigInteger.factorial(19) + " + 121",
                MyBigInteger.factorial(35) + " * 2"
        };
        for(String exp : expressions){
            System.out.print(exp + " = ");
            System.out.println(MyBigInteger.calculate(exp));
        }
    }

}
