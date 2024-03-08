/*
 * Much of this code was influenced and adapted from the code on D2L from Jalal Kawash
 */
public interface QueueInterface {
    public boolean isEmpty();
    public boolean isFull();
    public void enqueue(Cell element) throws OverflowException;
    public Cell dequeue() throws UnderflowException;
    public Cell peek() throws UnderflowException;
}
