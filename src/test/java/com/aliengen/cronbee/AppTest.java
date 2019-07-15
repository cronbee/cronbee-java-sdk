package com.aliengen.cronbee;

import static org.junit.Assert.assertTrue;

import com.aliengen.cronbee.annotation.CronbeeMonitor;
import org.junit.Test;

/**
 * Unit test for Cronbee SDK.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        this.testCronSuccess();

        try {
            this.testCronFailure();
            assertTrue(false);
        } catch (Exception e) {
        }

        assertTrue( true );
    }

    @CronbeeMonitor(uuid = "test-monitor-method")
    public void testCronSuccess() {

    }

    @CronbeeMonitor(uuid = "test-monitor-method")
    public void testCronFailure() throws Exception {
        throw new Exception("Failure!");
    }
}
