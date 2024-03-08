
/**
 * Queue ADT operations
 *
 * @author Jalal Kawash
 */

public interface QueueInterface <T extends Comparable>
{
    public boolean isEmpty();
    public boolean isFull();
    public void enqueue (T element);
    public T dequeue();
}
