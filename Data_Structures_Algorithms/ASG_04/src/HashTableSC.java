
/**
 *
 *
 *  Adapted from D2L code by Jalal Kawash
 *
 */


import java.lang.reflect.Array;
import java.util.LinkedList;

public class HashTableSC<T extends Hashable> implements HashTableInterface<T>
{
    private LinkedList<T>[] hashTable;
    private HashFunction f;

    /**
     * Constructor for objects of class HashTableSC
     */
    public HashTableSC(HashFunction f, int maxSize)
    {
        hashTable = new LinkedList[maxSize];
        this.f = f;
    }

    /**
     * clears the hashtable
     *
     * @preconsidtion: none
     * @postcondition: hash table becomes empty
     * 
     */
    public void clear()
    {
        for (int i = 0; i < hashTable.length; i++) hashTable[i] = null;
    }
    
    /**
     * adds an item to the hashtable
     *
     * @preconsidtion: none
     * @postcondition: item is added to the appropriat chain
     * 
     */
    public void add(T item)
    {
        int i = f.hash(item.key());
        if (hashTable[i] == null) {
        	hashTable[i] = new LinkedList<T>();
        	
        } 
        hashTable[i].add(item);
        
    }
    
    /**
     * removes an item to the hashtable
     *
     * @preconsidtion: none
     * @postcondition: item is deleted from the hashtable
     * 
     */
    public void remove(T item)
    {
        hashTable[f.hash(item.key())].remove(item.key());
    }
    
    /**
     * looks for an item in a hash table 
     *
     * @preconsidtion: none
     * @postcondition: returns true if item is in the table; false otherwise
     * 
     */
    public boolean contains(T item)
    {
        int i = f.hash(item.key());
        if (hashTable[i] == null) {
        	return false;
        }
        else {
        	return true;
        }
    }
    
    
}
