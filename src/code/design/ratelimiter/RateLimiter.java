package code.design.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    private int windowSecond;
    private int maxCallsAllowed;
    private Map<Integer, RateLimiterConfig> configMap;

    public RateLimiter(int windowSecond, int maxCallsAllowed) {
        this.windowSecond = windowSecond;
        this.maxCallsAllowed = maxCallsAllowed;
        this.configMap = new HashMap<>();
    }

    boolean rateLimit(int customerId) {
        RateLimiterConfig config = getCustomerConfig(customerId);
        long currentTime = System.currentTimeMillis();
        int diff = diffInSeconds(config.getTimestamp(), currentTime);
        if (diff < windowSecond) {
            if (config.getNoOfAllowedCalls() < this.maxCallsAllowed) {
                //allowed
                RateLimiterConfig newConfig = new RateLimiterConfig(config.getNoOfAllowedCalls() + 1, config.getCreditRequest(), config.getTimestamp());
                setCustomerConfig(customerId, newConfig);
                return true;
            } else if (config.getCreditRequest() > 0) {
                //allowed
                RateLimiterConfig newConfig = new RateLimiterConfig(config.getNoOfAllowedCalls() + 1, config.getCreditRequest() - 1, config.getTimestamp());
                setCustomerConfig(customerId, newConfig);
                return true;
            } else {
                //not allowed
                return false;
            }
        } else {
            //refresh the window
            int newCredit = config.getNoOfAllowedCalls() < this.maxCallsAllowed ? this.maxCallsAllowed - config.getNoOfAllowedCalls() : 0;
            RateLimiterConfig newConfig = new RateLimiterConfig(1, newCredit, currentTime);
            setCustomerConfig(customerId, newConfig);
            return true;
        }
    }

    private RateLimiterConfig getCustomerConfig(int customerId) {
        return this.configMap.getOrDefault(customerId, new RateLimiterConfig(0, 0, System.currentTimeMillis()));
    }

    private void setCustomerConfig(int customerId, RateLimiterConfig config) {
        this.configMap.put(customerId, config);
    }

    private int diffInSeconds(long timestamp1, long timestamp2) {
        return (int) Math.abs(timestamp1 - timestamp2) / 1000;
    }
}
