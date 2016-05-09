package test.compute.task;

import client.ComputePi;
import compute.task.PiCalculateTask;
import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 * Test the calculation of pi.
 */
public class PiCalculateTaskTest {
    /**
     * Test the calculation of the first 10 digits of pi.
     * @throws Exception
     */
    @org.junit.Test
    public void testComputePi4Digits() throws Exception {
        assertEquals(BigDecimal.valueOf(3.1415926536), PiCalculateTask.computePi(10));
    }
}