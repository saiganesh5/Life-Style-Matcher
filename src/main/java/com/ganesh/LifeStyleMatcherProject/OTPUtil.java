package com.ganesh.LifeStyleMatcherProject;

import java.util.Random;

public class OTPUtil {

    public static String generateOTP() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < otpLength; i++) {
            otp.append(rand.nextInt(10));  // random digit from 0-9
        }

        return otp.toString();
    }
}
