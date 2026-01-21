import java.util.*;
import java.util.concurrent.locks.*;

public class Task1 {
    static class ReusableBarrier {
        private final int friends; //number of threads
        private final Runnable task; //what to do once all friedns are done
        
        private int completed = 0; // how many have arrived
        private int current_pizza = 0; //nth of Pizzas 

        //add lock here
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();

        public ReusableBarrier(int friends) { this(friends, null); }

        public ReusableBarrier(int friends, Runnable task) {
            if (friends <= 0) throw new IllegalArgumentException("friends must be > 0");
            this.friends = friends;
            this.task = task;
        }

        public void await() throws InterruptedException {
            //TODO: Implement function
            lock.lock();
            try {
                int nthPizzaRound = current_pizza; // the current round of pizza
                completed++;

                if ( completed == friends){ // the last thread arrived 
                    if (task != null){
                        task.run(); // now we can bake
                    }

                    current_pizza++; // next pizza
                    completed = 0;
                    condition.signalAll(); //tell the friend to bake another pizza
                } 

                else{
                    while(nthPizzaRound == current_pizza){
                        condition.await();
                    }
                }
            } finally {
               lock.unlock();
            }
        }

    }
    
}

