
/**
 * Rrepresents a vertex in a graph
 * A vertes has an id and a generic object, which we call the record
 *
 * @author Jalal Kawash
 * 
 */
public class Vertex<T>
{
    private int id;
    private T record;

    /**
     * Constructor for objects of class Vertex
     */
    public Vertex(int id)
    {
        this.id = id;
    }

    /**
     * get the vertex id
     *
     * @postcod: returns the vertex id
     * 
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * get the vertex record
     *
     * @postcod: returns the vertex record
     * 
     */
    public T getrec()
    {
        return record;
    }
    
    /**
     * set the vertex record
     *
     * @postcod: returns the vertex id
     * 
     */
    public void setRec(T record)
    {
        this.record = record;
    }
}

