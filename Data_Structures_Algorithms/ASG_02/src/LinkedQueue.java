/*
 * Much of this code was influenced and adapted from the code on D2L from Jalal Kawash
 */



public class LinkedQueue implements QueueInterface {
    private class Node {
        private Cell value;
        private Node next;
        private Node prev;
    }

    private Node front;
    private Node rear;

    public LinkedQueue() {
        front = null;
        rear = null;
    }

    public boolean isEmpty() {
        return (front == null);
    }

    public boolean isFull() {
        return false;
    }

    public void enqueue(Cell item) throws OverflowException {
        Node newNode = new Node();
        newNode.value = item;

        if (isEmpty()) {
            newNode.next = newNode;
            newNode.prev = newNode;
            front = newNode; //if empty then the front and rear are both the item we are eneueing
            rear = newNode;
        } else {
            newNode.next = front; //reset the pointer to the front
            newNode.prev = rear; //reset the pointer to the rear
            rear.next = newNode; //change the rear cells next pointer to the new node 
            front.prev = newNode; //change the front cells prev pointer to the new node (rear of the queue because circular)A
            rear = newNode; //now make our new now the rear of the queue FIFO = LILO
        }
    }

    public Cell dequeue() throws UnderflowException {
        if (!isEmpty()) {
            Cell tmp = front.value;

            if (front == rear) {
                front = null; //if empty then pointers shouldnt point to anything
                rear = null;
            } else {
                front = front.next; // change front pointer to point at the next item in queue
                rear.next = front; //change the rears next pointer to point at the front of the queue (circular)
                front.prev = rear; // change the fronts prev pointer to point at the rear of queue (circular)
            }

            return tmp;
        } else {
            throw new UnderflowException("Cannot dequeue from an empty queue");
        }
    }

    public Cell peek() throws UnderflowException {
        if (!isEmpty()) {
            return front.value;
        } else {
            throw new UnderflowException("Cannot peek an empty queue");
        }
    }

}

