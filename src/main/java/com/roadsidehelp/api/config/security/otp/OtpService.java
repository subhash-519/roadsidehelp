package com.roadsidehelp.api.config.security.otp;

import com.roadsidehelp.api.config.exception.ApiException;
import com.roadsidehelp.api.config.exception.ErrorCode;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> ops;
    private final Random random = new Random();

    // OTP key prefix
    private static final String OTP_PREFIX = "otp:";

    // default TTL 5 minutes
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    public OtpService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ops = redisTemplate.opsForValue();
    }

    public String generateOtp(String key) {
        // key could be phone:<number> or email:<email>
        String otp = String.format("%06d", random.nextInt(1_000_000));
        ops.set(OTP_PREFIX + key, otp, DEFAULT_TTL);
        return otp;
    }

    public boolean validate(String key, String code) {
        String stored = ops.get(OTP_PREFIX + key);
        if (stored == null) {
            throw new ApiException(ErrorCode.OTP_EXPIRED, "OTP expired or not found");
        }
        if (!stored.equals(code)) {
            throw new ApiException(ErrorCode.OTP_INVALID, "Invalid OTP");
        }
        // delete after successful validation
        redisTemplate.delete(OTP_PREFIX + key);
        return true;
    }
}
