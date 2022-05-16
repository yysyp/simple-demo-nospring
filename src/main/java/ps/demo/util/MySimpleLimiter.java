package ps.demo.util;

import java.util.concurrent.TimeUnit;

public class MySimpleLimiter {

    long next = System.nanoTime();
    long interval = 1000000000;

    public MySimpleLimiter(long intervalNano) {
        this.interval = intervalNano;
    }

    synchronized long reserve(long now) {
        if (now > next) {
            next = now;
        }
        long at = next;
        next += interval;
        return Math.max(at, 0L);
    }

    public void acquire() {
        long now = System.nanoTime();
        long at = reserve(now);
        long waitTime = Math.max(at - now, 0L);
        if (waitTime > 0) {
            try {
                TimeUnit.NANOSECONDS
                        .sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    long storedPermits = 0;
//
//    long maxPermits = 3;
//
//    long interval = 1000000000; // 1 seconds
//
//    long next = System.nanoTime();
//
//    public MySimpleLimiter(long interval, long maxPermits) {
//        this.interval = interval;
//        this.maxPermits = maxPermits;
//    }
//
//    synchronized long reserve(long now) {
//        resync(now);
//
//        long at = next;
//        long fb = Math.min(1, storedPermits);
//        long nr = 1-fb;
//        next = next + nr*interval;
//        this.storedPermits -= fb;
//        return at;
//
//    }
//
//    void acquire() {
//        long now = System.nanoTime();
//
//        long at = reserve(now);
//        long waitTime = Math.max(at-now, 0L);
//        if (waitTime > 0) {
//            try {
//                Thread.sleep(waitTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    void resync(long now) {
//        if (now > next) {
//            long newPermits = (now - next)/interval;
//            storedPermits = Math.min(maxPermits, storedPermits+newPermits);
//            next = now;
//        }
//
//    }

}
