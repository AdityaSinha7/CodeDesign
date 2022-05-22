package code.design.ratelimiter;

public class RateLimiterConfig {
    private int noOfAllowedCalls;
    private int creditRequest;
    private long timestamp;

    public RateLimiterConfig(int noOfAllowedCalls, int creditRequest, long timestamp) {
        this.noOfAllowedCalls = noOfAllowedCalls;
        this.creditRequest = creditRequest;
        this.timestamp = timestamp;
    }

    public int getNoOfAllowedCalls() {
        return noOfAllowedCalls;
    }

    public int getCreditRequest() {
        return creditRequest;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
