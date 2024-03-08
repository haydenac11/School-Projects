
/**
 * Implementation of an unbounded queue ADT using linking
 * 
 * Implementation is by contract and by reference
 * 
 *
 * @author Jalal Kawash
 */


public class LinkedQueue<T extends Comparable> implements QueueInterface<T>
{
    private class Node<T extends Comparable>
    {
        private T value;
        private Node<T> next;
    }
    private Node<T> front, rear;
    
    /**
     * Constructor for objects of class LinkedQueue
     */
    public LinkedQueue()
    {
        rear = null;
        front = null;
    }

    /**
     * Precondition: None
     * Postcondition: returns true if queue is empty
     */
    public boolean isEmpty()
    {
        return (front == null);
    }
    
    /**
     * Precondition: None
     * Postcondition: returns false
     */
    public boolean isFull()
    {
        return false;
    }
    
    
    /**
     * Precondition: None
     * Postcondition: Adds a new element to the queue
     */
    public void enqueue(T item) 
    {
        Node<T> newNode = new Node<T>();
        newNode.value = item;
        newNode.next = rear;
        rear = newNode;
        if (front == null) front = rear;
    }
    
        
    /**
     * Precondition: Qeueue is not empty
     * Postcondition: removes and retuens the front item from the queue
     */
    public T dequeue()
    {
            T tmp = front.value;
            if (front == rear) // singleton
            {
                front = null;
                rear = null;
            }
            else {
                Node<T> tmpN = rear;
                while (tmpN.next != front) tmpN = tmpN.next;
                front = tmpN;
                front.next = null;
            }
            return tmp;
    } 
                   
    /**
     * Precondition: None
     * Postcondition: prints the contents of the queue
     */
    public void printQueue()
    {
       System.out.print("r->");
       Node<T> i = rear;
       if (front == rear && front != null) System.out.print(i.value); // singleton queue
       else 
           while (i != null) 
       {
            System.out.print(i.value);
            if (i.next != null) System.out.print(",");
            i = i.next;
       }
       System.out.println("<-f");
       }
    }
