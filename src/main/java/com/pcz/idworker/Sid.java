package com.pcz.idworker;

import com.pcz.idworker.strategy.DefaultWorkerIdStrategy;
import com.pcz.idworker.strategy.WorkerIdStrategy;
import com.pcz.idworker.utils.Util;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author picongzhi
 */
@Component
public class Sid {
    private static WorkerIdStrategy workerIdStrategy;
    private static IdWorker idWorker;

    static {
        configure(DefaultWorkerIdStrategy.instance);
    }

    public static synchronized void configure(WorkerIdStrategy strategy) {
        if (workerIdStrategy != null) {
            workerIdStrategy.release();
        }

        workerIdStrategy = strategy;
        idWorker = new IdWorker(workerIdStrategy.availableWorkerId()) {
            @Override
            public long getEpoch() {
                return Util.midnightMillis();
            }
        };
    }

    public static String next() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + String.format("%014d", id);
    }

    public String nextShort() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + Util.padLeft(Util.encode(id), 10, '0');
    }

    public static void main(String[] args) {
        String aa = new Sid().nextShort();
        String bb = new Sid().next();

        System.out.println(aa);
        System.out.println(bb);
    }
}
