package com.carrental.app.businesslayer;

import java.util.List;

// Builds the next readable public id from the ids that already exist,
// e.g. existing CAR-0001 ... CAR-0012  ->  next is CAR-0013.
public final class PublicIdGenerator {

    private PublicIdGenerator() {
    }

    public static String next(String prefix, List<String> existingIds) {
        int highest = existingIds.stream()
                .filter(id -> id != null && id.startsWith(prefix + "-"))
                .mapToInt(id -> Integer.parseInt(id.substring(prefix.length() + 1)))
                .max()
                .orElse(0);
        return String.format("%s-%04d", prefix, highest + 1);
    }
}
