package it.ngoton;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------- Testing My Math --------------");
        System.out.println(MyMath.sqrt(9));


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
                "-20 + -5",
                "-12 * 4",
                "-10 / 3",
                "-10 % 3",
                "102 ^ -2",
                "71045943470 * 41564635484",
                "316227766 * 316227766",
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
