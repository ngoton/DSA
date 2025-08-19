package it.ngoton;

/**
 * Swapping two numbers for learning purposes.
 */
public class MySwap {
    private final int a;
    private final int b;

    private MySwap(int a, int b) {
        this.a = a;
        this.b = b;
    }

    /**
     * <p>The bitwise XOR operator to swap the values without using a temporary variable.
     * @param a
     * @param b
     * @return Swap
     */
    public static MySwap swapXOR(int a, int b) {
        //          a = 0 0 0 0 1 0 1 0     10
        //          b = 0 0 0 1 1 1 1 0     30
        a = a ^ b; // = 0 0 0 1 0 1 0 0     20
        b = a ^ b; // = 0 0 0 0 1 0 1 0     10
        a = a ^ b; // = 0 0 0 1 1 1 1 0     30
        return new MySwap(a, b);
    }

    /**
     * Arithmetic operations to swap values without using a temporary variable (but it may gets overflow)
     * @param a
     * @param b
     * @return Swap
     */
    public static MySwap swapAdd(int a, int b) {
        //          a = 10
        //          b = 30
        a = a + b; // = 40
        b = a - b; // = 40 - 30 = 10
        a = a - b; // = 40 - 10 = 30
        return new MySwap(a, b);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
