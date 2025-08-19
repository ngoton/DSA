package it.ngoton;

/**
 * Reverse a number or a text for learning purposes.
 */
public class MyReverse {

    public static int reverse(int num) {
        /**
         *      num = 1234
         * reversed =    0
         * ---------------
         *      num = 123   (num / 10)
         *    digit =    4  (num % 10)
         * reversed =    4  (*10 + digit)
         * ---------------
         *      num =  12
         *    digit =    3
         * reversed =   43
         * ---------------
         *      num =   1
         *    digit =    2
         * reversed =  432
         * ---------------
         *      num =    0
         *    digit =    1
         * reversed = 4321
         */
        int reversed = 0;
        while (num != 0) {
            int digit = num % 10; // Extract the last digit
            reversed = reversed * 10 + digit; // Append the digit to the reversed number
            num /= 10; // Remove the last digit from the original number
        }
        return reversed;
    }

    public static String reverse(String text) {
        char[] array = text.toCharArray();
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            char temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
        return String.valueOf(array);
    }
}
