package nl.hkstwk.calculationmodule.exceptions;

public class RateLimitExceededException extends RuntimeException {
    long retryAfterSeconds;

    public RateLimitExceededException(long retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
