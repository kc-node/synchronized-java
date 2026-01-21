import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws Exception {
        //TODO: Test your code
        int friends = 3;
        Task1.ReusableBarrier barrier = new Task1.ReusableBarrier(friends);

        ExecutorService exec1 = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= friends; i++) {
            final int id = i;
            exec1.submit(() -> {
                try {
                    Thread.sleep(200 * id);
                    System.out.println("Friend " + id + " is waiting.");
                    barrier.await();
                    System.out.println("Friend " + id + " is eating the pizza! it is so YUM YUMMY YUM");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        exec1.shutdown(); //this says dont ccept new tasks
        exec1.awaitTermination(5, TimeUnit.SECONDS); // wait up to 5 seconds for running tasks
        
        
    }
}


