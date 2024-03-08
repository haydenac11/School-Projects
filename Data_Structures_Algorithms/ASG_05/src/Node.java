
/**
 * A node in a grph represents a vertex plus the weight of the incoming edge
 *
 * @author Jalal Kawash
 *
 */
public class Node<T>
{
   private Vertex<T> vertex; // vertex contained in this node
   private float edgeWeight; // weight of the incoming edge to this vertex
        
   public Node(Vertex vertex, float edgeWeight)
   {
       this.vertex = vertex;
       this.edgeWeight = edgeWeight;
   }
        
    public float getWeight()
    {
       return edgeWeight;
    }
        
   public Vertex getVertex()
   {
       return vertex;
   }
}
