/*
 * Much of this code was influenced and adapted from the code on D2L from Jalal Kawash
 */

public interface StackInterface
{
    public boolean isEmpty();
    public boolean isFull();
    public void push(Cell element);
    public Cell pop();
    public Cell peek();
}