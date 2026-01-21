import java.util.concurrent.locks.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Task2 {
    public interface MyBlockingQueue<T> {
        void enqueue(T item) throws InterruptedException;
        T dequeue() throws InterruptedException;
        int size();
        int capacity();
    }

    @SuppressWarnings("unchecked")
    public static class CoarseGrainedBlockingQueue<T> implements MyBlockingQueue<T> {
        private final Object[] queue;
        private int head = 0, tail = 0, count = 0;

        //add any lock and conditions here
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        public CoarseGrainedBlockingQueue(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
            this.queue = new Object[capacity];
        }

        @Override
        public void enqueue(T item) throws InterruptedException {
            //TODO: Implement the function
            lock.lock();
            try {
                while (count == queue.length) {
                    notFull.await();
                }
                
                queue[tail] = item; //add item to queue
                tail = (tail + 1) % queue.length;
                count++;

                notEmpty.signal(); //signal that queue is not empty now
            } finally{
                lock.unlock();
            }
        }

        @Override
        public T dequeue() throws InterruptedException {
            //TODO: Implement the function
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }

                T item = (T) queue[head];
                head = (head + 1) % queue.length;
                count--;

                notFull.signal();
                return item;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int size() {
            lock.lock();
            try { return count; } finally { lock.unlock(); }
        }

        @Override
        public int capacity() { return queue.length; }
    }

    @SuppressWarnings("unchecked")
    public static class FineGrainedBlockingQueue<T> implements MyBlockingQueue<T> {
        private final Object[] queue;
        private volatile int head = 0, tail = 0; 
        private final AtomicInteger size = new AtomicInteger(0);

        //add any lock and conditions here
        private final Lock enqueueLock = new ReentrantLock();
        private final Lock dequeueLock = new ReentrantLock();
        
        //for waiting 
        private final Condition notFull = enqueueLock.newCondition();
        private final Condition notEmpty = dequeueLock.newCondition();
        
        public FineGrainedBlockingQueue(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
            this.queue = new Object[capacity];
        }

        @Override
        public void enqueue(T item) throws InterruptedException {
            //TODO: Implement the function
            enqueueLock.lock();
            try {
                while (size.get() == queue.length) {
                    notFull.await();
                }

                queue[tail] = item;
                tail = (tail + 1) % queue.length;
                int prevSize = size.incrementAndGet();

                if (prevSize == 1){
                    dequeueLock.lock();
                    try {
                        notEmpty.signal(); 
                    } finally {
                        dequeueLock.unlock();
                    }
                }
            } finally {
                enqueueLock.unlock();
            }
        }

        @Override
        public T dequeue() throws InterruptedException {
            //TODO: Implement the function
            T item;
            dequeueLock.lock();
            try{
                while(size.get() == 0){
                    notEmpty.await();
                }

                item = (T) queue[head];
                head = (head + 1) % queue.length;
                int prevSize = size.getAndDecrement();

                if ( prevSize == queue.length){
                    enqueueLock.lock();
                    try{
                        notFull.signal();
                    } finally {
                        enqueueLock.unlock();
                    }
                }
            } finally {
                dequeueLock.unlock();
            }

            return item;

        }

        @Override
        public int size() { return size.get(); }

        @Override
        public int capacity() { return queue.length; }
    }
}