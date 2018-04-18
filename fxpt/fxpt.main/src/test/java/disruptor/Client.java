package disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client  {
    public static void main(String[] args) throws Exception {

        //1.配置并获得Disruptor
        ExecutorService executor = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        // 设置RingBuffer大小, 需为2的N次方(能将求模运算转为位运算提高效率 ), 否则影响性能
        int ringBufferSize = 1024 * 1024;

        //创建disruptor, 泛型参数:传递的事件的类型
        // 第一个参数: 产生Event的工厂类, Event封装生成-消费的数据
        // 第二个参数: RingBuffer的缓冲区大小
        // 第三个参数: 线程池
        // 第四个参数: SINGLE单个生产者, MULTI多个生产者
        // 第五个参数: WaitStrategy 当消费者阻塞在SequenceBarrier上, 消费者如何等待的策略.
        //BlockingWaitStrategy 使用锁和条件变量, 效率较低, 但CPU的消耗最小, 在不同部署环境下性能表现比较一致
        //SleepingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度; 多次调度后仍不成功, 睡眠纳秒级别的时间再尝试. 平衡了延迟和CPU资源占用, 但延迟不均匀.
        //YieldingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度. 平衡了延迟和CPU资源占用, 延迟也比较均匀.
        //BusySpinWaitStrategy 自旋等待，类似自旋锁. 低延迟但同时对CPU资源的占用也多.
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        // 注册事件消费处理器, 也即消费者. 可传入多个EventHandler ...
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动
        disruptor.start();

        //2.将数据装入RingBuffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        // 创建生产者, 以下方式一使用原始api, 方式二使用新API
        //LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(8); // 这里只是笔者实验, 不是必须要用ByteBuffer保存long数据
        for(int i = 0; i < 100; ++i){
            byteBuffer.putLong(0, i);
            producer.produceData(byteBuffer);
        }

        disruptor.shutdown(); //关闭 disruptor  阻塞直至所有事件都得到处理
        executor.shutdown(); // 需关闭 disruptor使用的线程池, 上一步disruptor关闭时不会连带关闭线程池
    }
}
