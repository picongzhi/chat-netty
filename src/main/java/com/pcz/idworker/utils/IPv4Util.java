package com.pcz.idworker.utils;

public class IPv4Util {
    public static long toLong(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new IllegalArgumentException("ip address cannot be null or empty");
        }

        String[] octets = ipAddress.split(java.util.regex.Pattern.quote("."));
        if (octets.length != 4) {
            throw new IllegalArgumentException("invalid ip address");
        }

        long ip = 0;
        for (int i = 3; i >= 0; i--) {
            long octet = Long.parseLong(octets[3 - i]);
            if (octet > 255 || octet < 0) {
                throw new IllegalArgumentException("invalid ip address");
            }
            ip |= octet << (i * 8);
        }

        return ip;
    }

    public static String toString(long ip) {
        if (ip > 4294967295l || ip < 0) {
            throw new IllegalArgumentException("invalid ip");
        }

        StringBuilder ipAddress = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int shift = i * 8;
            ipAddress.append((ip & (0xff << shift)) >> shift);
            if (i > 0) {
                ipAddress.append(".");
            }
        }

        return ipAddress.toString();
    }
}