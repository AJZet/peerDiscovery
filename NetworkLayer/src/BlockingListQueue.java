import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Basic implementation of synchronized queue with a LinkedList.
 *
 */


public class BlockingListQueue {
    private final LinkedList<String> queue;
    private final int limit;
    private Lock lock = new ReentrantLock();
    private Condition isFullCondition = lock.newCondition();
    private Condition isEmptyCondition = lock.newCondition();

    /**
     * Initialize the queue with a specific size.
     *
     * @param sizeLimit
     *              size of the queue
     */
    public BlockingListQueue(int sizeLimit) {
        queue = new LinkedList<String>();
        limit = sizeLimit;
    }

    /**
     * Check if the queue is empty
     *
     */
    public boolean isEmpty() { return queue.size() == 0; }

    /**
     * Check if the queue is full
     *
     */
    public boolean isFull() { return queue.size() == limit; }

    /**
     * Get current number of elements in the queue
     *
     */
    public int getSize() { return queue.size(); }

    /**
     * Add a message if the queue is not full, and wait for a
     * signalAll from isEmptyCondition if otherwise
     *
     * @param message
     *              a message to be added in the queue
     */
    public void enqueue(String message) {
        lock.lock();
        try {
            while(isFull()) {
                try {
                    isFullCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.add(message);
            isEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get a message from the queue if it is not empty, and wait for a
     * signalAll from isFullCondition if otherwise
     *
     */
    public String dequeue() {
        lock.lock();
        String msg;
        try {
            while(isEmpty()) {
                try {
                    isEmptyCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            msg = queue.remove();
            isFullCondition.signalAll();
        } finally {
            lock.unlock();
        }
        return msg;
    }

}