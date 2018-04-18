package disruptor;

import com.lmax.disruptor.EventFactory;

// 产生Event的工厂
public class LongEventFactory implements EventFactory {
    @Override
    public Object newInstance() {
        return new LongEvent();
    }
}
