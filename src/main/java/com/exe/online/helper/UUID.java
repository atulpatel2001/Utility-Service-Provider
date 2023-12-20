package com.exe.online.helper;

import java.util.concurrent.ThreadLocalRandom;

public class UUID {

    public static long generateUniqueID() {
        long timestamp = System.currentTimeMillis();
        long randomNum = ThreadLocalRandom.current().nextLong();
        return timestamp + randomNum;
    }

    public static long getSixDigitID(long uniqueID) {
        // Make sure the ID is positive (remove the sign) to avoid negative results
        long positiveID = Math.abs(uniqueID);

        // Reduce the number to 6 digits using modulo
        long sixDigitID = positiveID % 1_000_000;

        // If the number is less than 6 digits, add leading zeros
        String sixDigitString = String.format("%06d", sixDigitID);

        // Parse the result back to long and return
        return Long.parseLong(sixDigitString);
    }
}
