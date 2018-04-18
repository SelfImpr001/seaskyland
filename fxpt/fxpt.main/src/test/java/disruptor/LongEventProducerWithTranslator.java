package disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

//生产者实现二
public class LongEventProducerWithTranslator {

    // 使用EventTranslator, 封装 获取Event的过程
    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
        @Override
        public void translateTo(LongEvent event, long sequeue, ByteBuffer buffer) {
            event.setValue(buffer.getLong(0));
        }
    };

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void produceData(ByteBuffer buffer){
        // 发布
        ringBuffer.publishEvent(TRANSLATOR, buffer);
    }
}
