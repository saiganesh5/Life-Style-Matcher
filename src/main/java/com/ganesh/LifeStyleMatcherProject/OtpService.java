package com.ganesh.LifeStyleMatcherProject;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {
    private final Map<String, String> otpStore = new HashMap<>();

    public void saveOtp(String email, String otp) {
        otpStore.put(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        return otpStore.containsKey(email) && otpStore.get(email).equals(otp);
    }

    public void clearOtp(String email) {
        otpStore.remove(email);
    }
}
