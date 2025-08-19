package it.ngoton;

/**
 * Custom Mathematical operations for learning purposes.
 */
public class MyMath {

    /**
     * Function to add two numbers without using arithmetic operators
     * <p>The approach is to add two numbers using bitwise operations. Let's first go through some observations:
     * <p>- {@code a & b} (AND) will have only those bits set which are set in both a and b.
     * <p>- {@code a ^ b} (XOR) will have only those bits set which are set in either a or b but not in both.
     * <p>If we want to calculate the sum of a and b such that a and b has no common set bit,
     * then {@code a ^ b} is same as {@code a + b}. So, we can say that a + b without carry = a ^ b. (tổng không có nhớ)
     * <p>To calculate the carry, we know that <b>carry will only have the common set bits of a and b</b>, shifted 1 place to the left.
     * So, we can say that carry = (a & b) << 1. (số nhớ)
     * */
    public static int sum(int a, int b) {
        //                a = 0 0 0 0 1 0 1 0     10
        //                b = 0 0 0 1 1 1 1 0     30
        //       (*)  a & b = 0 0 0 0 1 0 1 0     10
        // carry = (*) << 1 = 0 0 0 1 0 1 0 0     20
        //---------------------------------------
        //        a = a ^ b = 0 0 0 1 0 1 0 0     20
        //        b = carry = 0 0 0 1 0 1 0 0     20
        //       (*)  a & b = 0 0 0 1 0 1 0 0     20
        // carry = (*) << 1 = 0 0 1 0 1 0 0 0     40
        //---------------------------------------
        //        a = a ^ b = 0 0 0 0 0 0 0 0     0
        //        b = carry = 0 0 1 0 1 0 0 0     40
        //       (*)  a & b = 0 0 0 0 0 0 0 0     0
        // carry = (*) << 1 = 0 0 0 0 0 0 0 0     0
        //---------------------------------------
        //        a = a ^ b = 0 0 1 0 1 0 0 0     40
        //        b = carry = 0 0 0 0 0 0 0 0     0
        //          sum = a = 40

        // Iterate till there is no carry
        while (b != 0) {

            // carry contains common set bits of a and b, left shifted by 1
            int carry = (a & b) << 1;

            // Update a with (a + b without carry)
            a = a ^ b;

            // Update b with carry
            b = carry;
        }
        return a;
    }

    /**
     * Given a positive integer {@code n}, find its square root. If {@code n} is not a perfect square, then return floor of {@code √n}.
     * <p>Using a loop - O(sqrt(n)) Time and O(1) Space
     * @param n
     * @return {@code √n}
     */
    public static int loopSqrt(int n) {
        // res = 1          n = 11
        // res * res = 1 <= n
        // =>  res++ = 2
        // res * res = 4 <= n
        // =>  res++ = 3
        // res * res = 9 <= n
        // =>  res++ = 4
        // res * res = 16 > n
        // res = res - 1 = 4-1 = 3

        // start iteration from 1 until the
        // square of a number exceeds n
        int res = 1;
        while (res * res <= n) {
            res++;
        }

        // return the largest integer whose
        // square is less than or equal to n
        return res - 1;
    }

    /**
     * Given a positive integer {@code n}, find its square root. If {@code n} is not a perfect square, then return floor of {@code √n}.
     * <p>Using Binary Search - O(log(n)) Time and O(1) Space
     * @param n
     * @return {@code √n}
     */
    public static int binarySqrt(int n) {
        // res = 1         n = 11
        // low = 1  high = n = 11
        // middle = (low+high)/2 = 6
        // middle * middle = 36 > n
        // => high = middle - 1 = 5    res = 1
        // middle = (1+5)/2 = 3
        // middle * middle = 9 <= n
        // => low = middle + 1 = 4     res = 3
        // middle = (4+5)/2 = 4
        // middle * middle = 16 > n
        // => high = middle - 1 = 3    res = 3
        // high > low
        // res = 3

        // initial search space
        int lo = 1, hi = n;
        int res = 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            // if square of mid is less than or equal to n
            // update the result and search in upper half
            if (mid * mid <= n){
                res = mid;
                lo = mid + 1;
            }

            // if square of mid exceeds n,
            // search in the lower half
            else {
                hi = mid - 1;
            }
        }

        return res;
    }

    /**
     * Given a positive integer {@code n}, find its square root. If {@code n} is not a perfect square, then return floor of {@code √n}.
     * <p>Using Formula Used by Pocket Calculators - O(1) Time and O(1) Space
     * <p><code>√n = e <sup>1/2 * log(n)</sup></code>
     * <p>Let's say square root of n is x:
     * => x = √n
     * Squaring both the sides:
     * => x^2 = n
     * Taking log on both the sides:
     * => log(x^2) = log(n)
     * => 2 × log(x) = log(n)
     * => log(x) = 1/2 × log(n)
     * To isolate x, exponentiate both sides with base e:
     * => x = e ^ 1/2 * log(n)
     * x is the square root of n:
     * So, √n = e ^ 1/2 × log(n)
     * @param n
     * @return {@code √n}
     */
    public static int sqrt(int n) {
        // calculating square root using
        // mathematical formula
        int res = (int)Math.exp(0.5 * Math.log(n));

        // If square of res + 1 is less than or equal to n
        // then, it will be our answer
        if ((res + 1) * (res + 1) <= n) {
            res++;
        }

        return res;
    }
}
