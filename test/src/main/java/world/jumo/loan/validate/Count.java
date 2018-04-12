/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Count.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.math.BigDecimal;

/**
 *
 */
public class Count implements Aggregate<BigDecimal> {

    BigDecimal result = BigDecimal.ZERO;

    public Count() {

        super();
    }

    public void put(BigDecimal value) {

        result = result.add(BigDecimal.ONE);
    }

    public BigDecimal get() {

        return result;
    }

}
