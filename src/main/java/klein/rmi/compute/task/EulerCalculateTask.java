package klein.rmi.compute.task;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * EulerCalculateTask is a task for calculating euler's number
 * from: http://stackoverflow.com/a/1481788/3554016
 */
public class EulerCalculateTask implements Task<BigDecimal> {
    private int precision;

    /**
     * Creates a new EulerCalculateTask instance
     * @param digits calculation precision
     */
    public EulerCalculateTask(int digits) {
        this.precision = digits;
    }

    /**
     * Calculate e.
     * @return approximation of euler's number
     */
    @Override
    public BigDecimal execute() {
        return calculateEuler(precision);
    }

    /**
     * Calculate euler's number with the given precision.
     * @param precision calculation precision (number of iterations)
     * @return approximation of euler's number
     */
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
