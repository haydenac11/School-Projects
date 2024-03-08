
/**
 * Implements a graph using an adjacency matrix
 * The grpah is directed and weighed
 *
 * @author Jalal Kawash
 * 
 */

import java.util.Stack;

public class AdjMatrixGraph<T>
{
    Node[][] adjMatrix;
    int size; // number of vertices |V|

    /**
     * Constructor for objects of class AdjMatrixGraph
     */
    public AdjMatrixGraph(int size)
    {
        adjMatrix = new Node[size][size];
        this.size = size;
    }
    
    /**
     * Add a new node to the graph
     */
    public void addNode(Vertex v, float edgeWeight, int i, int j)
    {
        adjMatrix[i][j] = new Node(v,edgeWeight);
    }
    
    /**
     * Return a node
     */
    public Node getNode(int i, int j)
    {
        return adjMatrix[i][j];
    }
    
    /**
     * returns a queue of DFS walk of vertices from startVertex
     */
    public LinkedQueue oneSourceDFS(int startVertex)
    {
        Stack<Integer> s = new Stack();
        LinkedQueue<Integer> w = new LinkedQueue();
        boolean[] visited = new boolean[size];
        for (int i = 0; i < size; i++) visited[i] = false;
        s.push(startVertex);
        
        while (!s.isEmpty())
        {
            int currentVertex = s.pop();
            w.enqueue(currentVertex);
            visited[currentVertex] = true;
            
            for (int i = 0; i < size; i++)
                    if (!visited[i] && adjMatrix[currentVertex][i] != null) 
                     s.push(i);
        }
        return w;
    }
    
    /**
     * returns a queue of BFS walk of vertices from startVertex
     */
    public LinkedQueue oneSourceBFS(int startVertex)
    {
        LinkedQueue<Integer> q = new LinkedQueue();
        LinkedQueue<Integer> w = new LinkedQueue();
        boolean[] visited = new boolean[size];
        for (int i = 0; i < size; i++) visited[i] = false;
        q.enqueue(startVertex);
        
        while (!q.isEmpty())
        {
            int currentVertex = q.dequeue();
            w.enqueue(currentVertex);
            visited[currentVertex] = true;
            
            for (int i = 0; i < size; i++)
                    if (!visited[i] && adjMatrix[currentVertex][i] != null) 
                      q.enqueue(i);
        }
        return w;
    }
    
    /**
     * returns a 2D array representing the reachability of vertices from each other
     */
    public boolean[][] getReachMatrix()
    {
        boolean[][] reachMatrix = new boolean[size][size];
        
        LinkedQueue<Integer>[] allQs = new LinkedQueue[size];
        for (int i = 0; i < size; i++) 
        {   
            allQs[i] = new LinkedQueue<Integer>();
            allQs[i] = oneSourceDFS(i); //get all reachable vertices from i
            
            for (int j = 0; j < size; j++) { // initialize the matrix
                 if (i == j) reachMatrix[i][j] = true;
                else reachMatrix[i][j] = false;
            }
        }
        
        for (int i = 0; i < size; i++)
        {
            while (!allQs[i].isEmpty()) 
            {
                int k = (Integer) allQs[i].dequeue();
                reachMatrix[i][k] = true;
            }
        }
        
        return reachMatrix;
    }
    
    /**
     * Checks if the graph is connected. A directed graph G is strongly connected if there is a path between every pair
     * of vertices in the G
     */
    public boolean isStronglyConnected() 
    {
        boolean[][] rm = getReachMatrix();
        int[] reachableCount = new int[size];
        for (int i = 0; i < size; i++) 
            reachableCount[i]= 0;
        
        for (int i = 0; i < size; i++) 
            for(int j = 0; j < size; j++) 
                if (rm[i][j]) reachableCount[i]++;
                
        for (int i = 0; i < size; i++) 
            if (reachableCount[i] != size) return false;
        
            return true;
    }
    
    public void printGraph()
    {
        System.out.println("Graph Adjacency Matrix");
        for (int i = 0; i < size; i++) 
        {
             System.out.printf("[%d]", i);
             for (int j = 0; j < size; j++)
             {
                if (this.getNode(i,j) != null)
                    System.out.printf("(%d,%.2f) ", this.getNode(i,j).getVertex().getId(), this.getNode(i,j).getWeight());
                else System.out.printf("(  ,   ) ");
             }
             System.out.println();
        }   
    
    }
    
}
