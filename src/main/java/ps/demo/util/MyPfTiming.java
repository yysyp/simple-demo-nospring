package ps.demo.util;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MyPfTiming {

    long start;

    private MyPfTiming() {
        this.reset();
    }

    public static MyPfTiming start() {
        return new MyPfTiming();
    }

    public MyPfTiming reset() {
        this.start = System.nanoTime();
        return this;
    }

    public long time() {
        long end = System.nanoTime();
        return end - start;
    }

    public long time(TimeUnit timeUnit) {
        return timeUnit.convert(time(), TimeUnit.NANOSECONDS);
    }

    public long timeMs() {
        return time(TimeUnit.MILLISECONDS);
    }

}
