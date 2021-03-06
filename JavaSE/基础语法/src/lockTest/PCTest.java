package lockTest;

//测试：生产者消费者模式-->利用缓冲区：管程法
//生产者，消费者，产品，缓冲区
public class PCTest {
    public static void main(String[] args) {
        SynContainer container = new SynContainer();

        new Productor(container).start();
        new Consumer(container).start();
    }
}

//生产者
class Productor extends Thread {
    SynContainer container;

    public Productor(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            container.pusp(new Chicken(i));
            System.out.println("生产了" + i + "只鸡");
        }
    }
}

//消费者
class Consumer extends Thread {
    SynContainer container;

    public Consumer(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("消费了---》" + container.pop().id + "只鸡");
        }
    }
}

//产品
class Chicken {
    int id;//产品编号

    public Chicken(int id) {
        this.id = id;
    }
}

//缓冲区
class SynContainer {
    //需要一个容器大小
    Chicken[] chickens = new Chicken[10];
    //    容器计数器
    int count = 0;

    //生产者放入产品
    public synchronized void pusp(Chicken chicken) {
        if (count == chickens.length) {
            //通知消费者消费，生产者等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果没有满，则需要丢入产品
        chickens[count] = chicken;
        count++;
        //可以通知消费者消费了
        this.notifyAll();

    }

    //消费者消费产品
    public synchronized Chicken pop() {
        //判断能否消费
        if (this.count == 0) {
            //消费者等待生产，或者通知生产者生成
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //消费
        count--;
        Chicken chicken = chickens[count];
        //通知生产者生产
        this.notifyAll();
        return chicken;
    }


}
