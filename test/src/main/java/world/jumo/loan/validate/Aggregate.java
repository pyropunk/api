/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Aggregate.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

/**
 * Interface for all aggregating functions
 * 
 * @param <T> the type that is being aggregated over
 */
public interface Aggregate<T> {

    /**
     * Add a value to the aggregate
     * 
     * @param value
     */
    void put(T value);

    /**
     * Retrieve the result of the aggregate function
     * 
     * @return a value of type T
     */
    T get();
}
