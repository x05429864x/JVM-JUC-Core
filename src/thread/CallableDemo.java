package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        FutureTask<Integer> futureTask1 = new FutureTask<>(new MyThread());
        new Thread(futureTask, "AA").start();
        new Thread(futureTask1, "BB").start();
        int result01 = 100;

        while (!futureTask.isDone()){
            System.out.println("while...");
        }
        int result02 = futureTask.get();
        System.out.println("result=" + (result01 + result02));

    }
}

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread().getName()+" callable come in ...");
        return 1024;
    }
}
