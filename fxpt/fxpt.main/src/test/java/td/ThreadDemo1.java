package td;

public class ThreadDemo1 {
    public static void main(String[] args) {
        MyThread mt = new MyThread();
        mt.start();  // 注意调用的start()方法，而不是run()方法
        mt.start();  // 注意调用的start()方法，而不是run()方法
        for(int i = 0; i < 3; ++i)
            System.out.println("222");
    }
}

class MyThread extends Thread{    // 继承Thread类

    @Override
    public void run() {          // 重写run方法
        for(int i = 0; i < 3; ++i)    // 将将要执行的代码写到run方法中
            System.out.println("1111111111");

    }
}
