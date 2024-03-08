
/**
 * Generates a random graph with "empty" vertices using the adjacency matrix implementation
 * The vertices only have natural number (0 to size-1) ids (the records are empty)
 *
 * @author Jalal Kawash
 * 
 */

import java.util.Random;

public class RandomAdjMatrixGraph extends AdjMatrixGraph
{   
    private int numEdges = 0;
    
    /**
     * Constructor for objects of class RandomAdjMatrixGraph
     * 
     * @precond: density is between 0 and 10. A more dense (higher density value) graph will have more edges
     * 
     */
    public RandomAdjMatrixGraph(int size, int density)
    {
        super(size);
        Random rnd = new Random();
        if (density <= 0) return; // results in a graph with no edges
        if (density >= 11) density = 10;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) 
            {
                for (int k = 0; k < density; k++) // for higher densities more edges betwen i and j may be added
                {
                    if (rnd.nextInt(size)%(size*3) == 3) // random choice to add a vertex
                    {
                        this.addNode(new Vertex(j), rnd.nextFloat(size), i, j);
                        numEdges++;
                    }
                    
                }
            }         
    }
    
    public int getNumEdges()
    {
        return numEdges;
    }
    
    
    
}
