package disruptor;

import com.lmax.disruptor.*;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientForEventProcessor {
    public static void main(String[] args) throws Exception {
        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;

        // 这里直接获得RingBuffer. createSingleProducer创建一个单生产者的RingBuffer

        // 第一个参数为EventFactory，产生数据Trade的工厂类
        // 第二个参数是RingBuffer的大小，需为2的N次方
        // 第三个参数是WaitStrategy, 消费者阻塞时如何等待生产者放入Event
        final RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade(UUID.randomUUID().toString());
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());

        //SequenceBarrier, 协调消费者与生产者, 消费者链的先后顺序. 阻塞后面的消费者(没有Event可消费时)
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建事件处理器 (消费者): 处理ringBuffer, 用TradeHandler的方法处理(实现EventHandler), 用sequenceBarrier协调生成-消费
        //如果存在多个消费者(老api, 可用workpool解决) 那重复执行 创建事件处理器-注册进度-提交消费者的过程, 把其中TradeHandler换成其它消费者类
        BatchEventProcessor<Trade> transProcessor = new BatchEventProcessor<Trade>(ringBuffer, sequenceBarrier, new TradeHandler());
        //把消费者的消费进度情况注册给RingBuffer结构(生产者)    !如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(transProcessor.getSequence());

        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //把消费者提交到线程池, !说明EventProcessor实现了callable接口
        executors.submit(transProcessor);

        // 生产者, 这里新建线程不是必要的
        Future<?> future= executors.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    seq = ringBuffer.next();
                    ringBuffer.get(seq).setPrice(Math.random() * 9999);
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });

        Thread.sleep(1000); //等上1秒，等待消费完成
        transProcessor.halt(); //通知事件处理器  可以结束了（并不是马上结束!）
        executors.shutdown();
    }
}
