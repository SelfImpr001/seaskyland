package disruptor;

// Event封装要传递的数据
public class LongEvent {
    private long value;
    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }
}
