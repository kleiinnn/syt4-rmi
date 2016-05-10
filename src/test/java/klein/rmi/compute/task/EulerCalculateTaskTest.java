package klein.rmi.compute.task;

import klein.rmi.compute.task.EulerCalculateTask;
import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Test class for the calculation of euler's number.
 */
public class EulerCalculateTaskTest {

    /**
     * Test the calculation of euler's number.
     * @throws Exception
     */
    @Test
    public void testCalculateEuler() throws Exception {
        BigDecimal calculatedEuler = EulerCalculateTask.calculateEuler(100).round(new MathContext(10));
        assertEquals(BigDecimal.valueOf(2.718281828), calculatedEuler);
    }
}