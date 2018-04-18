package disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

//生产者实现一
public class LongEventProducer {
    // 生产者持有RingBuffer的引用
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void produceData(ByteBuffer bb){
        // 获得下一个Event槽的下标
        long sequence = ringBuffer.next();
        try {
            // 给Event填充数据
            LongEvent event = ringBuffer.get(sequence);
            event.setValue(bb.getLong(0));
        } finally {
            // 发布Event, 激活观察者去消费, 将sequence传递给该消费者
            //publish应该放在 finally块中以确保一定会被调用->如果某个事件槽被获取但未提交, 将会堵塞后续的publish动作。
            ringBuffer.publish(sequence);
        }
    }
}