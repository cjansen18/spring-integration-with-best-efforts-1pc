package com.chrisjansen.simulator;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;

/**
* Simulates a database exception.
 *
* @author Chris Jansen
 */
@Aspect
public class DatabaseFailureSimulator {

    public void simulateBusinessProcessingFailure() {
        throw new DataIntegrityViolationException("Simulated database failure.");
    }

    @AfterReturning("execution(* *..*Builder+.build(String)) && args(msg)")
    public void maybeFail(String msg) {
        if (msg.contains("fail")) {
                simulateBusinessProcessingFailure();
            }
        }
}
