package compute.task;

import compute.SolutionCallback;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * from: http://www.java2s.com/Book/Java/Essential-Classes/Calculating_Eulers_number_e_with_BigDecimal.htm
 */
public class EulerCalculateTask implements Task<BigDecimal>, Serializable {
    private int digits;
    private SolutionCallback<BigDecimal> callback;

    public EulerCalculateTask(int digits, SolutionCallback<BigDecimal> callback) {
        this.digits = digits;
        this.callback = callback;
    }

    @Override
    public SolutionCallback<BigDecimal> getCallback() {
        return callback;
    }

    @Override
    public BigDecimal execute() {
        MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i <= digits; i++) {
            BigDecimal factorial = factorial(new BigDecimal(i));
            BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
            result = result.add(res);
        }
        return result;
    }

    private BigDecimal factorial(BigDecimal n) {
        if (n.equals(BigDecimal.ZERO))
            return BigDecimal.ONE;
        else
            return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
    }
}
