package jvm;


/**
 * \* User: x
 * \* Date: 2021/2/24
 * \* Time: 17:03
 * \* Description:
 * \
 */
public class HelloGC {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("****HelloGC");
        System.out.println(1111);
//        byte[] ints = new byte[1024 * 1024 * 50];
        Thread.sleep(Integer.MAX_VALUE);
    }
}
