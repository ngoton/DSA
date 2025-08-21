package it.ngoton;

/**
 * A custom BigDecimal for learning purposes.
 * <p>Handling arbitrary-precision floating-point, which exceed the capacity of primitive data types like {@code float, double}
 * <p>{@code float} base <code>2<sup>32</sup></code>, stores numbers IEEE 754 Standard
 * (<code>sign<sup>1bit</sup> exponent<sup>8bit</sup> Mantissa / Significand<sup>23bit</sup></code>)
 * <p>{@code double} base <code>2<sup>64</sup></code>, stores numbers IEEE 754 Standard
 * (<code>sign<sup>1bit</sup> exponent<sup>11bit</sup> Mantissa / Significand<sup>52bit</sup></code>)
 * */
public class MyBigDecimal {

    private final MyBigInteger unscaledValue;
    private final int scale;

    private MyBigDecimal(MyBigInteger unscaledValue, int scale) {
        this.unscaledValue = unscaledValue;
        this.scale = scale;
    }

    public MyBigDecimal(String value) {
        int dotIndex = value.indexOf('.');
        if (dotIndex == -1) {
            this.unscaledValue = new MyBigInteger(value);
            this.scale = 0;
        }
        else {
            String digits = value.replace(".", "");
            this.unscaledValue = new MyBigInteger(digits);
            this.scale = value.length() - 1 - dotIndex;
        }
    }

    public MyBigDecimal(float value) {
        this(Float.toString(value));
    }

    public MyBigDecimal(double value) {
        this(Double.toString(value));
    }

    public MyBigDecimal add(MyBigDecimal other) {
        // 123.45           scale = 2
        //+ 56.789          scale = 3
        // -------
        // 123450           12345 * 10^(3-2)
        //+ 56789           56789 * 10^(3-3)
        // ------
        // 180239
        // 180.239          scale = 3

        // Bring to the same scale
        int maxScale = Math.max(this.scale, other.scale);
        MyBigInteger a = this.unscaledValue.multiply(new MyBigInteger(10).pow(maxScale - this.scale));
        MyBigInteger b = other.unscaledValue.multiply(new MyBigInteger(10).pow(maxScale - other.scale));
        // Sum like whole numbers
        MyBigInteger sum = a.add(b);
        return new MyBigDecimal(sum, maxScale);
    }

    public MyBigDecimal subtract(MyBigDecimal other) {
        return this.add(new MyBigDecimal(other.unscaledValue.negate(), other.scale));
    }

    public MyBigDecimal multiply(MyBigDecimal other) {
        // 123.45           scale = 2
        //* 56.789          scale = 3
        // -------
        //  12345
        //* 56789
        // ------
        // 701060205
        // 7010.60205       scale = 2+3 = 5

        // Ignore the decimal point, multiply like whole numbers
        MyBigInteger product = this.unscaledValue.multiply(other.unscaledValue);
        int newScale = this.scale + other.scale;
        return new MyBigDecimal(product, newScale);
    }

    public MyBigDecimal divide(MyBigDecimal other) {
        // 12.34 : 2.345
        // 123.4 : 23.45
        // 1234  : 234.5
        // 12340 : 2345           12340 / 2345 = 5
        //-11725       = 5        5 * 2345     = 11725
        // ─────
        //   615┊                  615 / 2345   = 0
        //   6150                  6150 / 2345  = 2
        //  -4690      = 5.2       2 * 2345     = 4690
        //   ────
        //   1460┊                 1460 / 2345  = 0
        //   14600                 14600 / 2345 = 6
        //  -14070     = 5.26

        // 123.45      : 2.345
        // [5,4,3,2,1] : [5,4,3,2]                      scale = 2   scale = 3
        // [0,0,0,0,0,0,0,0,0,0,5,4,3,2,1] : [5,4,3,2]
        // quotient = [0,4,2,3,2,9,3,4,6,2,5]           scale = 10 - (divisor - dividend) = 10-(3-2) = 9
        // => 52.643923240

        // Mở rộng độ chính xác lên 10 số
        int precision = other.scale + 10;

        MyBigInteger dividend = this.unscaledValue.multiply(new MyBigInteger(10).pow(precision));
        MyBigInteger quotient = dividend.divide(other.unscaledValue);
        int diffScale = other.scale - this.scale;
        int newScale = precision - diffScale;

        // TODO: Handle rounding mode
        return new MyBigDecimal(quotient, newScale);
    }

    @Override
    public String toString() {
        if (scale == 0) {
            return unscaledValue.toString();
        }
        String s = unscaledValue.abs().toString(); // get unscaled value without signum
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() <= scale) { // if number is between 0 and 1. ex: 0.12345 -> s=12345 length=5 scale=5
            builder.insert(0, '0');
        }
        builder.insert(builder.length() - scale, '.');
        if (unscaledValue.isNegative()) {
            builder.insert(0, '-');
        }
        return builder.toString();
    }
}
