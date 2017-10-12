/** Free */
package com.rtzan.camel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class MessageCounter {

    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public Integer increment(String counterName) {
        AtomicInteger counter = counters.computeIfAbsent("counterName", s -> new AtomicInteger(1));
        return counter.getAndIncrement();
    }

    public Integer get(String counterName) {
        AtomicInteger counter = counters.getOrDefault("counterName", new AtomicInteger(1));
        return counter.get();
    }

}
