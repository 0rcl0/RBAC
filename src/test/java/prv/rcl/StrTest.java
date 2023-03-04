package prv.rcl;


import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StrTest {

    private static AtomicInteger atomicInteger = new AtomicInteger();

    @Test
    public void incre(){

        Thread [] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()-> {
                for (int j = 0; j < 10000; j++) {
                    increase();
                }
            },"thread---"+i);

            threads[i].start();
        }
        // 当前活动线程数
        while (Thread.activeCount() > 1) {
            // 有活动线程就让出
            Thread.yield();
        }

        System.out.println(atomicInteger);
    }

    public static void increase() {
        atomicInteger.incrementAndGet();
    }

}
