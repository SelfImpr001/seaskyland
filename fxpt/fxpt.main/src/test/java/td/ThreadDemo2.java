package td;


public class ThreadDemo2 {
    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.start();
        thread.start();
        for(int i = 0; i < 3; ++i)
            System.out.println("bb");

    }

}


class MyRunnable implements Runnable{

    @Override
    public void run() {
        for(int i = 0; i < 3; ++i)
            System.out.println("aaaaaaaaaaaaaaaaa");
    }
}
