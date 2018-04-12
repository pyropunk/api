/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ProductAggregator.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public class ProductAggregator<T extends Product<R>, K, R> implements Aggregator<T, K, R> {

    Function<T, K> converter;
    List<Class<? extends Aggregate<R>>> aggregates;

    Map<K, List<Aggregate<R>>> grouped = new HashMap<>();

    /**
     * 
     */
    public ProductAggregator(Function<T, K> converter, List<Class<? extends Aggregate<R>>> aggregates) {

        this.converter = converter;
        this.aggregates = aggregates;
    }

    @Override
    public void put(T value) {

        try {
            
            final K key = converter.apply(value);
            
            if ( !grouped.containsKey(key)) {

                final List<Aggregate<R>> list = new ArrayList<>();
                for (Class<? extends Aggregate<R>> ac : aggregates) {
                    final Aggregate<R> a = ac.newInstance();
                    a.put(value.getAmount());
                    list.add(a);
                }
                grouped.put(key, list);
            }
            else {
                List<Aggregate<R>> list = grouped.get(key);
                for (Aggregate<R> a : list) {
                    a.put(value.getAmount());
                }
            }
        }
        catch (InstantiationException | IllegalAccessException a) {
            throw new SecurityException(a);
        }
    }

    @Override
    public Map<K, List<R>> get() {

        final Map<K, List<R>> results = new HashMap<>();

        for (Map.Entry<K, List<Aggregate<R>>> entry : grouped.entrySet()) {
            
            final List<R> r = new ArrayList<>();
            for (Aggregate<R> a : entry.getValue()) {
                r.add(a.get());
            }
            results.put(entry.getKey(), r);
        }
        return results;
    }
}
