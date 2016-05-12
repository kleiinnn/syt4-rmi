package klein.rmi.compute.task;

import klein.rmi.compute.SolutionCallback;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * from: http://stackoverflow.com/a/1481788/3554016
 */
public class EulerCalculateTask implements Task<BigDecimal> {
    private int precision;

    public EulerCalculateTask(int digits) {
        this.precision = digits;
    }

    @Override
    public BigDecimal execute() {
        return calculateEuler(precision);
    }

    public static BigDecimal calculateEuler(int precision) {
        BigDecimal e = BigDecimal.ONE;
        BigDecimal fact = BigDecimal.ONE;

        for(int i=1;i<=precision;i++) {
            fact = fact.multiply(new BigDecimal(i));

            e = e.add(BigDecimal.ONE.divide(fact, new MathContext(10000, RoundingMode.HALF_UP)));
        }
        return e;
    }
}
