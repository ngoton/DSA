package it.ngoton;

import java.util.Arrays;
import java.util.Objects;

/**
 * A custom BigInteger for learning purposes.
 * <p>Handling arbitrary-precision integers, which exceed the capacity of primitive data types like {@code int}
 * <p>{@code int} base <code>2<sup>32</sup></code>, stores numbers from <code>-2<sup>31</sup></code> to <code>2<sup>31</sup>-1</code>
 * */
public class MyBigInteger extends Number implements Comparable<MyBigInteger> {
    public static final MyBigInteger ZERO = new MyBigInteger(new int[]{0}, 0);
    public static final MyBigInteger ONE = new MyBigInteger(new int[]{1}, 1);
    /**
     * Use an array of {@code int} to store the digits or segments of the large number.
     * Each element in the array represents a part of the number in a chosen base (e.g., base <code>2<sup>32</sup></code> if using int arrays).
     * Store digits in reverse order (least significant, most significant).
     * */
    private int[] digits;
    /**
     * Maintain a sign variable (e.g., 1 for positive, -1 for negative, 0 for zero).
     * */
    private int signum;

    public MyBigInteger(String value) {
        int signum = 1;
        if (value.startsWith("-")) {
            signum = -1;
            value = value.substring(1);
        }
        int[] digits = new int[value.length()];
        for (int i = 0; i < digits.length; i++) {
            // The least significant part is stored at magnitude[0]
            digits[i] = value.charAt(value.length() - 1 - i) - '0'; // Subtracts ASCII value of '0' (48) char -> int
        }
        init(digits, signum);
    }

    public MyBigInteger(int value) {
        this(Integer.toString(value));
    }

    private MyBigInteger(int[] digits, int signum) {
        init(digits, signum);
    }

    private void init(int[] digits, int signum) {
        digits = removeLeadingZeros(digits);
        if (digits.length == 0) {
            this.digits = new int[]{0};
            this.signum = 0;
        }
        else {
            this.digits = digits;
            this.signum = signum;
        }
    }

    private int[] removeLeadingZeros(int[] digits) {
        if (digits.length == 0) {
            return digits;
        }
        // Remove leading zeros
        int FIRST_NON_ZERO = digits.length - 1;
        while (FIRST_NON_ZERO >= 0 && digits[FIRST_NON_ZERO] == 0) {
            FIRST_NON_ZERO--; // Count number of zeros
        }
        if (FIRST_NON_ZERO < 0) {
            return new int[0];
        }
        return Arrays.copyOfRange(digits, 0, FIRST_NON_ZERO+1);
    }

    /**
     * Algebraic Operations (Các phép toán đại số)
     * <p>The basic operations covered in algebra are addition, subtraction, multiplication, and division.
     * @param expression
     * @return
     */
    public static MyBigInteger calculate(String expression) {
        String[] xOperatorY = expression.split(" ");
        char operator = xOperatorY[1].charAt(0);
        MyBigInteger x = new MyBigInteger(xOperatorY[0]);
        MyBigInteger y = new MyBigInteger(xOperatorY[2]);

        switch(operator) {
            case '*':
                return x.multiply(y);
            case '+':
                return x.add(y);
            case '-':
                return x.subtract(y);
            case '/':
                return x.divide(y);
            case '%':
                return x.mod(y);
            case '^':
                return x.pow(y.intValue());
            default : throw new IllegalArgumentException("Operation not supported!");
        }
    }

    public MyBigInteger add(MyBigInteger other) {
        if (this.signum == 0) return other;
        if (other.signum == 0) return this;
        if (this.signum != other.signum) {
            if (this.signum == 1)
                return this.subtract(other.abs()); // Positive + Negative
            return other.subtract(this.abs()); // Negative + Positive
        }
        return new MyBigInteger(plus(this.digits, other.digits), this.signum);
    }

    public MyBigInteger subtract(MyBigInteger other) {
        if (this.signum == 0) return other.negate();
        if (other.signum == 0) return this;
        if (this.signum != other.signum) // Positive - negative || Negative - positive
            return this.add(other.abs().negate());

        int compare = compareMagnitude(this.digits, other.digits);
        if (compare == 0)
            return ZERO;
        if (compare == -1)
            return other.subtract(this).negate();

        return new MyBigInteger(minus(this.digits, other.digits), this.signum);
    }

    public MyBigInteger multiply(MyBigInteger other) {
        if (this.signum == 0 || other.signum == 0) return ZERO;
        return new MyBigInteger(times(this.digits, other.digits), this.signum * other.signum);
    }

    public MyBigInteger divide(MyBigInteger other) {
        if (this.signum == 0) return ZERO; // Quotient is 0 if dividend = 0
        if (other.signum == 0) throw new ArithmeticException("BigInteger divide by zero");

        int compare = compareMagnitude(this.digits, other.digits);
        if (compare == 0)
            return ONE; // Quotient is 1 if dividend = divisor
        if (compare == -1)
            return ZERO; // Quotient is 0 if dividend < divisor

        return new MyBigInteger(divideBy(this.digits, other.digits), this.signum * other.signum);
    }

    public MyBigInteger mod(MyBigInteger other) {
        MyBigInteger quotient = this.divide(other);
        MyBigInteger remainder = this.subtract(quotient.multiply(other));
        return remainder;
    }

    /**
     * The pow(n) method, which calculates number x raised to the power of n. (<code>x<sup>n</sup></code>)
     * <p>The exponent of a number tells us how many times we should multiply the base. (cơ số)
     * <p> <code>5<sup>3</sup> = 5 * 5 * 5</code>
     * <p> <code>5<sup>-3</sup> = 1/5 * 1/5 * 1/5</code>
     * @param exponent
     * @return <code>x<sup>n</sup></code>
     */
    public MyBigInteger pow(int exponent) {
        if (exponent == 0) return ONE;

        MyBigInteger result = ONE;
        MyBigInteger base = this;
        // Handle negative exponents
        // a^-n = 1/a^n
        if (exponent < 0) {
            base = result.divide(base);
            exponent = -exponent;
        }

        while (exponent > 0) {
            result = result.multiply(base);
            exponent--;
        }
        return result;
    }

    /**
     * The factorial method, denoted by an exclamation mark (!),
     * calculates the product of all positive integers less than or equal to a given number (n).
     * <p>{@code n! = n * (n-1) * (n-2) * ... * 2 * 1}
     * <p>{@code 5! = 5 * 4 * 3 * 2 * 1}
     * @param n
     * @return {@code n!}
     */
    public static MyBigInteger factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative factorial not supported");
        MyBigInteger result = ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(new MyBigInteger(i));
        }
        return result;
    }

    /**
     * Method returns the square root of a number.
     * @return √n
     */
    public MyBigInteger sqrt() {
        MyBigInteger result = ONE;
        MyBigInteger low = ONE, high = this;

        while (low.compareTo(high) <= 0) {
            MyBigInteger middle = low.add(high.subtract(low)).divide(new MyBigInteger(2));

            // if square of mid is less than or equal to n
            // update the result and search in upper half
            if (middle.pow(2).compareTo(this) <= 0){
                result = middle;
                low = middle.add(ONE);
            }

            // if square of mid exceeds n,
            // search in the lower half
            else {
                high = middle.subtract(ONE);
            }
        }

        return result;
    }

    /**
     * Method returns a MyBigInteger whose value is the absolute value of the MyBigInteger with which it is used.
     * @return {@code digits} itself and {@code signum = 1 positive}
     */
    public MyBigInteger abs() {
        return new MyBigInteger(this.digits, 1);
    }

    /**
     * Method returns a MyBigInteger whose value is the negated value of the MyBigInteger with which it is used.
     * @return {@code digits} itself and its opposite {@code -signum}
     */
    public MyBigInteger negate() {
        return new MyBigInteger(this.digits, -this.signum);
    }

    /**
     *        1234
     *   + 1234567
     *   = -------
     *     1235801
     *            a = [4 3 2 1]
     *            b = [7 6 5 4 3 2 1]
     *        carry = 0
     * -------------------------------0
     *  sum = carry = 0
     * sum += a + b = 11
     *       res[0] = 11 % 10 = 1
     *          res = [1 0 0 0 0 0 0]
     *        carry = 11 / 10 = 1
     * ------------------------------1
     *  sum = carry = 1
     * sum += a + b = 1 + 3 + 6 = 10
     *       res[1] = 10 % 10 = 0
     *          res = [1 0 0 0 0 0 0]
     *        carry = 10 / 10 = 1
     * ------------------------------2
     *  sum = carry = 1
     * sum += a + b = 1 + 2 + 5 = 8
     *       res[2] = 8 % 10 = 8
     *          res = [1 0 8 0 0 0 0]
     *        carry = 8 / 10 = 0
     * ------------------------------n
     *          res = [1 0 8 5 3 2 1]
     *
     * @param a
     * @param b
     * @return {@code int[]} a+b
     */
    private int[] plus(int[] a, int[] b) {
        int len = Math.max(a.length, b.length);
        int[] result = new int[len + 1];
        int carry = 0;
        for (int i = 0; i < len; i++) {
            int sum = carry;
            if (i < a.length) sum += a[i];
            if (i < b.length) sum += b[i];
            result[i] = sum % 10;
            carry = sum / 10;
        }
        if (carry > 0) result[len] = carry;
        return result;
    }

    /**
     *     1234
     *   -  567
     *   = ----
     *      667
     *            a = [4 3 2 1]
     *            b = [7 6 5]
     *       borrow = 0
     * -------------------------------0
     * diff = a[0] - borrow - b[0] = 4 - 0 - 7 = -3
     * diff < 0:
     *  -> borrow = 1
     *  -> res[0] = -3 + 10 = 7
     * ------------------------------1
     * diff = 3 - 1 - 6 = -4
     * borrow = 1
     * res[1] = -4 + 10 = 6
     * ------------------------------2
     * diff = 2 - 1 - 5 = -4
     * borrow = 1
     * res[2] = -4 + 10 = 6
     * ------------------------------3
     * diff = 1 - 1 - 0 = 0
     * borrow = 0
     * res[3] = 0
     * ------------------------------n
     *          res = [7 6 6 0]
     *
     * @param a
     * @param b
     * @return {@code int[]} a-b
     */
    private int[] minus(int[] a, int[] b) {
        int[] result = new int[a.length];
        int borrow = 0;

        for (int i = 0; i < a.length; i++) {
            int diff = a[i] - borrow - (i < b.length ? b[i] : 0);
            if (diff < 0) {
                diff += 10;
                borrow = 1;
            }
            else {
                borrow = 0;
            }
            result[i] = diff;
        }
        return result;
    }

    /**
     *       123
     *    *   45
     *    = ----
     *       135
     *    +  90
     *    + 45
     *    = ----
     *      5535
     *            a = [3 2 1]
     *            b = [5 4]
     *          res = [0 0 0 0 0]
     * -------------------------------0
     * carry = 0
     * prod  = res[0] + a[0] * b[0] + carry = 0 + 3 * 5 + 0 = 15
     * res[0] = prod % 10 = 5                                           res = [5 0 0 0 0]
     * carry = prod / 10 = 1
     *      carry = 1
     *      prod  = res[1] + a[0] * b[1] + carry = 0 + 3 * 4 + 1 = 13
     *      res[1] = prod % 10 = 3                                      res = [5 3 0 0 0]
     *      carry = prod / 10 = 1
     * - carry > 0:
     * -> res[0 + 2] += carry = 1                                       res = [5 3 1 0 0]
     * ------------------------------1
     * carry = 0
     * prod  = res[1] + a[1] * b[0] + carry = 3 + 2 * 5 + 0 = 13
     * res[1] = prod % 10 = 3                                           res = [5 3 1 0 0]
     * carry = prod / 10 = 1
     *      carry = 1
     *      prod  = res[2] + a[1] * b[1] + carry = 1 + 2 * 4 + 1 = 10
     *      res[2] = prod % 10 = 0                                      res = [5 3 0 0 0]
     *      carry = prod / 10 = 1
     * - carry > 0:
     * -> res[1 + 2] += carry = 1                                       res = [5 3 0 1 0]
     * ------------------------------2
     * carry = 0
     * prod  = res[2] + a[2] * b[0] + carry = 0 + 1 * 5 + 0 = 5
     * res[2] = prod % 10 = 5                                           res = [5 3 5 1 0]
     * carry = prod / 10 = 0
     *      carry = 0
     *      prod  = res[3] + a[2] * b[1] + carry = 1 + 1 * 4 + 0 = 5
     *      res[3] = prod % 10 = 5                                      res = [5 3 5 5 0]
     *      carry = prod / 10 = 0
     *                                                                  res = [5 3 5 5 0]
     * ------------------------------n
     *          res = [5 3 5 5 0]
     *
     * @param a
     * @param b
     * @return {@code int[]} a*b
     */
    private int[] times(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];

        for (int i = 0; i < a.length; i++) {
            int carry = 0;
            for (int j = 0; j < b.length; j++) {
                int product = result[i + j] + a[i] * b[j] + carry;
                result[i + j] = product % 10;
                carry = product / 10;
            }
            if (carry > 0) {
                result[i + b.length] += carry;
            }
        }
        return result;
    }

    /**
     *  1234 : 45 = 27       1 / 45 =  0
     * -0┊┊┊                 0 * 45 =  0
     * ──┊┊┊
     *  12┊┊                12 / 45 =  0
     *  -0┊┊                 0 * 45 =  0
     *  ──┊┊
     *  123┊               123 / 45 =  2
     *  -90┊                 2 * 45 =  90
     *   ──┊
     *   33┊                33 / 45 =  0
     *  - 0┊                 0 * 45 =  0
     *   ──┊
     *   334               334 / 45 =  7
     *  -315                 7 * 45 = 315
     *   ───
     *    19                19 < 45
     *    ==> quotient 27, remainder 19.
     *
     *           a = [4 3 2 1]
     *           b = [5 4]
     *    quotient = []
     *   remainder = []
     * -------------------------------0
     * remainder = [0]
     * remainder = remainder + a[0] = [1]
     * remainder < b
     * ------------------------------1
     * remainder = [0 1]
     * remainder = remainder + a[1] = [2 1]
     * remainder < b
     * ------------------------------2
     * remainder = [0 2 1]
     * remainder = remainder + a[2] = [3 2 1]
     * remainder > b
     *      result = 0
     *      remainder = remainder - b = [3 2 1] - [5 4] = [8 7]
     *      result = 1
     *      remainder = remainder - b = [8 7] - [5 4] = [3 3]
     *      result = 2
     * quotient = [2]
     * ------------------------------3
     * remainder = [0 3 3]
     * remainder = remainder + a[3] = [4 3 3]
     * remainder > b
     *      result = 0
     *      remainder = remainder - b = [4 3 3] - [5 4] = [9 8 2]
     *      result = 1
     *      remainder = remainder - b = [9 8 2] - [5 4] = [4 4 2]
     *      result = 2
     *      remainder = remainder - b = [4 4 2] - [5 4] = [9 9 1]
     *      result = 3
     *      ...
     *      remainder = remainder - b = [4 6] - [5 4] = [9 1]
     *      result = 7
     * quotient = [7 2]
     * ------------------------------n
     * quotient = [7 2]
     *
     * @param a
     * @param b
     * @return {@code int[]} a/b
     */
    private int[] divideBy(int[] a, int[] b) {
        /**
         * Division and subtraction are closely related.
         * Division can be thought of as repeated subtraction.
         * When we divide one number by another,
         * we are trying to find out how many times the dividend can be subtracted from the divisor to get 0
         * (or a remainder smaller than the divisor).
         * Ex: 12/3 = 12 - 3 - 3 - 3 = 0
         *     12/5 = 12 - 5 - 5 = 2 (remainder < divisor)
         */
        int[] quotient = {};
        int[] remainder = {};
        for (int i = a.length - 1; i >= 0; i--) {            //             [....]
            remainder = addSize(remainder, 1);     // Shift left [0,....]
            remainder = plus(remainder, new int[] {a[i]}); // Append to   [*,....]
            remainder = removeLeadingZeros(remainder);

            int result = 0;
            while (compareMagnitude(remainder, b) >= 0) { // remainder >= divisor
                remainder = minus(remainder, b);
                remainder = removeLeadingZeros(remainder);
                result++; // How many times the subtraction is performed is the result of the division.
            }
            if (result > 0) {
                int[] resultDigits = new MyBigInteger(result).digits; // Convert int -> int[]
                quotient = addSize(quotient, 1);
                quotient = plus(quotient, resultDigits);
            }
        }
        return quotient;
    }

    /**
     * With ideas like Bit shift left operation {@code <<} to increase the size of array
     * <p>  5<<2
     *       101
     *     --
     *     10100
     * <p> Ex:
     *    increase = 2
     *    original =      [4 3 2 1]
     * destination = [0 0 4 3 2 1]
     * @param original
     * @param increase
     * @return original's size + {@code increase}
     */
    private int[] addSize(int[] original, int increase) {
        int[] result = new int[original.length + increase];
        System.arraycopy(original, 0, result, increase, original.length);
        return result;
    }

    /**
     * Compares the magnitude array of this BigInteger with the specified BigInteger's.
     * This is the version of compareTo ignoring sign.
     * @param m1
     * @param m2
     * @return -1, 0 or 1 as this magnitude array is less than, equal to or greater than the other.
     */
    private int compareMagnitude(int[] m1,  int[] m2) {
        int len1 = m1.length;
        int len2 = m2.length;

        if (len1 < len2)
            return -1;
        if (len1 > len2)
            return 1;

        for (int i = 0; i < len1; i++) {
            int a = m1[i];
            int b = m2[i];
            if (a != b) {
                return a > b ? 1 : -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.signum == -1) {
            builder.append("-");
        }
        for (int i = this.digits.length - 1; i >= 0; i--) {
            builder.append(this.digits[i]);
        }
        return builder.toString();
    }

    @Override
    public int compareTo(MyBigInteger other) {
        if (this.signum == other.signum) {
            if (this.signum >= 0)
                return compareMagnitude(this.digits, other.digits);
            else
                return compareMagnitude(other.digits, this.digits);
        }
        return this.signum > other.signum ? 1 : -1;
    }

    @Override
    public int intValue() {
        long result = longValue();
        if (result < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        if (result > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (int) result;
    }

    @Override
    public long longValue() {
        long result = 0;
        for (int i = this.digits.length - 1; i >= 0; i--) {
            result = result * 10 + this.digits[i];
        }
        return result * this.signum;
    }

    @Override
    public float floatValue() {
        long result = longValue();
        if (result < Float.MIN_VALUE)
            return Float.MIN_VALUE;
        if (result > Float.MAX_VALUE)
            return Float.MAX_VALUE;
        return (float) result;
    }

    @Override
    public double doubleValue() {
        long result = longValue();
        if (result < Double.MIN_VALUE)
            return Double.MIN_VALUE;
        if (result > Double.MAX_VALUE)
            return Double.MAX_VALUE;
        return (double) result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(digits), signum);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MyBigInteger)) return false;
        MyBigInteger other = (MyBigInteger) obj;
        return this.signum == other.signum && compareMagnitude(this.digits, other.digits) == 0;
    }
}
