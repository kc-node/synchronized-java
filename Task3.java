import java.util.*;
import java.util.concurrent.*;

public class Task3 {
    public static final int POISON_PILL = -1;

    static class Producer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        // Do not change constructor
        public Producer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> producedPizzas = new ArrayList<>(); // Producer makes pizzas and puts them in the queue

            int pizzas = 10; //she said to generate 10 pizzas
            for (int i = 0; i < pizzas; i++) {
                queue.put(i);
                producedPizzas.add(i);
                Thread.sleep(200); // simulate making the pizzaassss
            }

            queue.put(POISON_PILL); // Poison consumers so they can stop

            return producedPizzas;
        }
    }

    static class Consumer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        public Consumer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> pizzaReady = new ArrayList<>(); // Consumer collects pizzas until poison pill is received

            while (true) {
                int pizza = queue.take();

                if (pizza == POISON_PILL) {
                    break;                  // Stop consuming when poison pill is found
                }

                pizzaReady.add(pizza);
                Thread.sleep(200); // collecting the pizzas
            }

            return pizzaReady;
        }
    }
}
