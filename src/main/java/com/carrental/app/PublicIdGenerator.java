package com.carrental.app;

import java.util.List;

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
