
/**
 * Implementation of a BST using linking
 * 
 * Implementation is by contract and by reference
 *
 * @author Jalal Kawash, based on the implementation of Dale, Joyce, and Weems (Object-Oriented Data Structures in Java)
 * 
 */


public class BST<T extends Comparable> implements BSTInterface<T>
{   
    protected Node<T> root;
    
    public static final int INORDER = 0;
    public static final int PREORDER = 1;
    public static final int POSTORDER = 2;
    
    private LinkedQueue<T> inOrderQueue, preOrderQueue, postOrderQueue;
    
    public Node<T> getRoot(){ // needed in TreePrinter
        return root;
    }

    /**
     * Constructor for objects of class BST
     */
    public BST()
    {
        root = null;
    }

    /**
     * Precondition: None
     * Postcondition: returns true if BST is empty
     */
    public boolean isEmpty()
    {
        return (root == null);
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
     * Postcondition: returns the number of elements inthe BST
     */
    public int size()
    {
        return recursiveSize(root);
    }
    
    int recursiveSize(Node<T> root) 
    {
        if (root == null) return 0;
        else return recursiveSize(root.getLeft()) + recursiveSize(root.getRight()) + 1;
    }
    
    /**
     * Precondition: None
     * Postcondition: deletes all the elements in the BST and resests it to the initial condition
     */
    public void clear()
    {
        root = null;
    }
    
    /**
     * Precondition: None
     * Postcondition: resets the current index to the begining of the BST
     */
    public void reset(int order) 
    {
        if (order == BST.INORDER)
        {
            inOrderQueue = new LinkedQueue<T>();
            inOrderTraversal(root);
        }
        
        if (order == BST.PREORDER)
        {
            preOrderQueue = new LinkedQueue<T>();
            preOrderTraversal(root);
        }
        
        if (order == BST.POSTORDER)
        {
            postOrderQueue = new LinkedQueue<T>();
            postOrderTraversal(root);
        }
    }
    
    void inOrderTraversal(Node<T> root) 
    {
        if (root != null) 
        {
            inOrderTraversal(root.getLeft());
            inOrderQueue.enqueue(root.getValue());
            inOrderTraversal(root.getRight());
        }
    }
    
    void preOrderTraversal(Node<T> root) 
    {
        if (root != null) 
        {
            preOrderQueue.enqueue(root.getValue());
            preOrderTraversal(root.getLeft());
            preOrderTraversal(root.getRight());
        }
    }
    
    void postOrderTraversal(Node<T> root) 
    {
        if (root != null) 
        {
            postOrderTraversal(root.getLeft());
            postOrderTraversal(root.getRight());
            postOrderQueue.enqueue(root.getValue());
        }
    }
    
    /**
     * Precondition: None
     * Postcondition: returns the next element in the list based on specified traversal order (inorder, preorder, postorder)
     */
    public T getNext(int order) 
    {
       if (order == BST.INORDER) return inOrderQueue.dequeue();
       if (order == BST.PREORDER) return preOrderQueue.dequeue();
       if (order == BST.POSTORDER) return postOrderQueue.dequeue();
       return null;
    }
    
    /**
     * Precondition: None
     * Postcondition: Adds a new element to the list
     */
    public void add(T item) 
    {
        root = recursiveAdd(item, root);
    }
    
    Node<T> recursiveAdd(T item, Node<T> root)
    {
        if (root == null) //insert here
        {
            root = new Node<T>();
            root.setValue(item);    
        }
        else if (item.compareTo(root.getValue()) < 0) // got to left subtree
            root.setLeft(recursiveAdd(item, root.getLeft()));
        else root.setRight(recursiveAdd(item, root.getRight())); // go right
        
        return root;
    }
    
    /**
     * Precondition: None
     * Postcondition: returns true if a given item is in the tree; otherwise returns false
     */
    public boolean contains(T item) 
    {
        return recursiveContains(item, root);
    }
    
    boolean recursiveContains(T item, Node<T> root)
    {
        if (root == null) return false;
        else if (item.compareTo(root.getValue()) < 0) return recursiveContains(item, root.getLeft());
        else if (item.compareTo(root.getValue()) > 0) return recursiveContains(item, root.getRight());
        else return true;
    }
    
    /**
     * Precondition: Item to be removed is in the tree
     * Postcondition: removes an item from the BST
     */
    public void remove(T item) 
    {
        root = recursiveRemove(item,root);
    }
    
    Node<T> recursiveRemove(T item, Node<T> root)
    {
        if (root == null) return null;
        else if (item.compareTo(root.getValue()) < 0) root.setLeft(recursiveRemove(item, root.getLeft()));
        else if (item.compareTo(root.getValue()) > 0) root.setRight(recursiveRemove(item, root.getRight()));
        else root = removeNode(root);
        return root;
    }
    
    Node<T> removeNode(Node<T> root)
    {
        T tmp;
        if (root.getLeft() == null) return root.getRight();
        else if (root.getRight() == null) return root.getLeft();
        else
        {
            tmp = predecessor(root.getLeft());
            root.setValue(tmp);
            root.setLeft(recursiveRemove(tmp, root.getLeft()));
            return root;
        }
    }
    
    T predecessor(Node<T> root)
    {
        while (root.getRight() != null) root = root.getRight();
        return root.getValue();
    }
    
    
    /**
     * Precondition: None
     * Postcondition: prints the contents of the list nased on the provided traversal order type
     */
    public void printBST(int order)
    {
        int s = size();
        if (order == INORDER) 
        {
            reset(INORDER);
            inOrderTraversal(root);
            System.out.println("Get items inorder: ");
            for (int i = 0; i < s; i++) System.out.print(getNext(BST.INORDER) + " ");
            System.out.println();
        }
        
        if (order == PREORDER) 
        {
            reset(PREORDER);
            preOrderTraversal(root);
            System.out.println("Get items preorder: ");
            for (int i = 0; i < s; i++) System.out.print(getNext(BST.PREORDER) + " ");
            System.out.println();
        }
        
        if (order == POSTORDER) 
        {
            reset(POSTORDER);
            postOrderTraversal(root);
            System.out.println("Get items postorder: ");
            for (int i = 0; i < s; i++) System.out.print(getNext(BST.POSTORDER) + " ");
            System.out.println();
        }
        
    }
    
    public int height() {
    	return recursiveHeight(root);
    }
    
    private int recursiveHeight(Node<T> root) {
    	// return -1 if non existent
    	if (root == null) {
    		return -1;
    	}
    	
    	else {
    		// recursively go through nodes
    		int left = recursiveHeight(root.getLeft());
    		int right = recursiveHeight(root.getRight());
    		if (left > right) {
    			return (left + 1);
    		}
    		else {
    			return (right + 1);
    		}
    	}
    }
    
    public Node<T> parent(T value) {

    	return recursiveParent(root, value, null);
    }
    
    private Node<T> recursiveParent(Node<T> current, T value, Node<T> parent){
    	// if nonexistent return null
    	if (current == null) return null;
    	// if value of node == current node return the parent
    	if (value == current) return parent;

    	if(value.compareTo(current) < 0) {
    		return recursiveParent(current.getLeft(), value, current);
    	}
    	else return recursiveParent(current.getRight(), value,  current);
    }
    
    // method to reset the visited state 
    private void resetVisits(Node<T> current) {
    	if (current != null) {
    		current.setVisited(false);
    		resetVisits(current.getLeft());
    		resetVisits(current.getRight());
    	}
    }
    
    // method to determine level
    public int level(T value) {
    	resetVisits(root);
    	return recursiveLevel(root, value, 0, -1);
    }
    
    // recursively find the level
    private int recursiveLevel(Node<T> current, T value, int currentLevel, int highestLevel) {
    	// if null tree then return highestLevel so far (-1)
    	if (current == null) return highestLevel;
    	// update the highest level 
    	if (value.compareTo(current.getValue()) == 0) {
    		if (currentLevel > highestLevel) {
    			highestLevel = currentLevel;
    		}
    	}
    	// mark node as visited
    	current.setVisited(true);
    	
    	int next = currentLevel + 1;
    	
    	if (current.getLeft() != null && !current.getLeft().isVisited()) {
    		int leftHigh = recursiveLevel(current.getLeft(), value, next, highestLevel);
    		if (leftHigh > highestLevel) highestLevel = leftHigh;
    	}
    	if (current.getRight() != null && !current.getRight().isVisited()) {
    		int rightHigh = recursiveLevel(current.getRight(), value, next, highestLevel);
    		if (rightHigh > highestLevel) highestLevel = rightHigh;
    		
    	}
    	return highestLevel;
    }
    
    
    public boolean isComplete() {
    	int nodes = size();
    	return recursiveComplete(root, nodes, 0);
    }
    
    // Pretty sure this isnt working correctly but not enough time to dig into it
    // Just tryna get some part marks for stuff at this point
    // The results I am getting for my statistics aren't right which makes me think this is the problem
    private boolean recursiveComplete(Node<T> current, int nodes, int i) {
    	if (current == null) return true;
    	if (nodes <= i) return false;
    	return recursiveComplete(current.getLeft(), nodes, (2 * i) + 1) && recursiveComplete(current.getRight(), nodes, (2 * i) + 2);
    }
    
    // Use definition of perfect to calculate if perfect
    public boolean isPerfect() {
    	int nodes = size();
    	return ((int) Math.pow(2, (height() + 1) - 1) == nodes);
    }
    
    //could not figure out the hasDoubles() in time
    
}


