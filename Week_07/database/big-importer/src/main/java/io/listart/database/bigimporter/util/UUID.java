package io.listart.database.bigimporter.util;

public final class UUID {
    public static String newUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
