package test;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * 写操作：在线程数目增加时CopyOnWriteArrayList的写操作性能下降非常严重，而Collections.synchronizedList虽然有性能的降低，但下降并不明显。
 * 读操作：在多线程进行读时，Collections.synchronizedList和CopyOnWriteArrayList均有性能的降低，
 *          但是Collections.synchronizedList的性能降低更加显著。
 *
 * 结论
 *          CopyOnWriteArrayList，发生修改时候做copy，新老版本分离，保证读的高性能，适用于以读为主，读操作远远大于写操作的场景中使用，比如缓存。
 *          而Collections.synchronizedList则可以用在CopyOnWriteArrayList不适用，但是有需要同步列表的地方，读写操作都比较均匀的地方。
 * \
 */
public class CopyOnWriteArrayListTest {
    private int NUM = 10000;
    private int THREAD_COUNT = 32;

    @Test
    public void testAdd() throws Exception {
        List<Integer> list1 = new CopyOnWriteArrayList<Integer>();
        List<Integer> list2 = Collections.synchronizedList(new ArrayList<Integer>());
        Vector<Integer> v  = new Vector<Integer>();

        CountDownLatch add_countDownLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        int add_copyCostTime = 0;
        int add_synchCostTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            add_copyCostTime += executor.submit(new AddTestTask(list1, add_countDownLatch)).get();
        }
        System.out.println("CopyOnWriteArrayList add method cost time is " + add_copyCostTime);

        for (int i = 0; i < THREAD_COUNT; i++) {
            add_synchCostTime += executor.submit(new AddTestTask(list2, add_countDownLatch)).get();
        }
        System.out.println("Collections.synchronizedList add method cost time is " + add_synchCostTime);


    }

    @Test
    public void testGet() throws Exception {
        List<Integer> list = initList();

        List<Integer> list1 = new CopyOnWriteArrayList<Integer>(list);
        List<Integer> list2 = Collections.synchronizedList(list);

        int get_copyCostTime = 0;
        int get_synchCostTime = 0;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch get_countDownLatch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            get_copyCostTime += executor.submit(new GetTestTask(list1, get_countDownLatch)).get();
        }
        System.out.println("CopyOnWriteArrayList add method cost time is " + get_copyCostTime);

        for (int i = 0; i < THREAD_COUNT; i++) {
            get_synchCostTime += executor.submit(new GetTestTask(list2, get_countDownLatch)).get();
        }
        System.out.println("Collections.synchronizedList add method cost time is " + get_synchCostTime);

    }


    private List<Integer> initList() {
        List<Integer> list = new ArrayList<Integer>();
        int num = new Random().nextInt(10000);
        for (int i = 0; i < NUM; i++) {
            list.add(num);
        }
        return list;
    }

    class AddTestTask implements Callable<Integer> {
        List<Integer> list;
        CountDownLatch countDownLatch;

        AddTestTask(List<Integer> list, CountDownLatch countDownLatch) {
            this.list = list;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Integer call() throws Exception {
            int num = new Random().nextInt(1000);
            long start = System.currentTimeMillis();
            for (int i = 0; i < NUM; i++) {
                list.add(num);
            }
            long end = System.currentTimeMillis();
            countDownLatch.countDown();
            return (int) (end - start);
        }
    }

    class GetTestTask implements Callable<Integer> {
        List<Integer> list;
        CountDownLatch countDownLatch;

        GetTestTask(List<Integer> list, CountDownLatch countDownLatch) {
            this.list = list;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Integer call() throws Exception {
            int pos = new Random().nextInt(NUM);
            long start = System.currentTimeMillis();
            for (int i = 0; i < NUM; i++) {
                list.get(pos);
            }
            long end = System.currentTimeMillis();
            countDownLatch.countDown();
            return (int) (end - start);
        }
    }
}
