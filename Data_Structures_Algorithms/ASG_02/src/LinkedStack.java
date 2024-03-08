
/*
 * Much of this code was influenced and adapted from the code on D2L from Jalal Kawash
 */

public class LinkedStack implements StackInterface
{

	private class Node
    {

		private Cell value;
		public Node next;
		public Node prev;
    }
        private Node topIndex;

    public LinkedStack()
    {
        topIndex = null;
    }

 
    public boolean isEmpty()
    {
        return (topIndex == null);
    }
    
 
    public boolean isFull()
    {
        return false;
    }
    
    
    public void push(Cell item) throws OverflowException{
    	if (!isFull()) {
    	Node newNode = new Node();
    	newNode.value = item;
    	newNode.next = topIndex;
    	if (topIndex != null) {
    		topIndex.prev = newNode; // this is the double linking piece
    	}
    	topIndex = newNode;
    	}
    	else throw new OverflowException("Cannot push to a full stack");
    	
    }
    
        

    public Cell pop() throws UnderflowException{
    	if (!isEmpty()) {
    		Cell tmp = topIndex.value;
    		topIndex = topIndex.next;
    		if (topIndex != null) {
    			topIndex.prev = null; //double linking piece
    		}
    		return tmp;
    	}
    	else throw new UnderflowException("Cannot pop from an empty stack");
    }
    

    public Cell peek() throws UnderflowException{
    	if (!isEmpty()) 
    		return topIndex.value;
    	else throw new UnderflowException("Cannot peek to an empty stack");
    	
    }
    

}
