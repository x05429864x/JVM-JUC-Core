package thread;

/**
 * 1.两个普通同步方法,两个线程,标准打印     //one two
 * 2.新增Thread.sleep()给getOne()       //one two
 * 3.新增普通方法getThree()              //three one two
 * 4.两个普通同步方法,两个Number对象        //two one
 * 5.修改给getOne()为静态方法               //two one
 * 6.修改两个方法均为静态方法,一个Number对象    //one two
 * 7.一个静态同步方法,一个非静态同步方法 两个Numer对象   //two one
 * 8.另个静态同步方法,两个Number对象    //one two
 */
public class TestThread8Monitor {
    public static void main(String[] args) {
        Number number = new Number();
        Number number1 = new Number();
        new Thread(new Runnable() {
            @Override
            public void run() {
                number.getOne();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                number.getTwo();
//                number1.getTwo();
            }
        }).start();

        new Thread(()->{
            number.getThree();
        }).start();
    }
}

class Number{
    public synchronized void getOne(){//静态方法锁Number.class
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){

        }
        System.out.println("one");
    }

    public synchronized void getTwo(){//非静态方法锁 this
        System.out.println("two");
    }

    public void getThree(){
        System.out.println("three");
    }
}