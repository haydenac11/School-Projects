import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
public class ASG_05 {

	public static void main(String[] args) {
		
		try {
			
			BufferedWriter f_writer = new BufferedWriter(new FileWriter("/Users/haydenchurch/Documents/asg_05_p1.csv"));
			long start;
			long end;
			long delta1; //floyd
			long delta2; //dijkstra
			for (int i = 500; i <= 1000; i+=10) {
				RandomAdjMatrixGraph graph = new RandomAdjMatrixGraph(i,5);
				start = System.nanoTime();
				float floyd[][] = floyd(graph);
				end = System.nanoTime();
				delta1 = end - start;
				
				start = System.nanoTime();
				for (int j = 0; j < i; j++) {
					//System.out.println("Entering DYK " + j);
					float dik[] = dijkstras(graph, j);
				}
				end = System.nanoTime();
				delta2 = end - start;
				f_writer.write(i + "," + delta1 + "," + delta2);
				f_writer.newLine();
				
				//System.out.println("NUMBER " + i);
			}
			
			f_writer.close();
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
		System.out.println("DONE PART 1");
		try {
			BufferedWriter f_writer = new BufferedWriter(new FileWriter("/Users/haydenchurch/Documents/asg_05_p2.csv"));
			long start;
			long end;
			long delta1; //floyd
			long delta2; //dijkstra
			
			for (int i = 1; i < 10; i++) {
				RandomAdjMatrixGraph graph = new RandomAdjMatrixGraph(5000, i);
				
				start = System.nanoTime();
				float floyd[][] = floyd(graph);
				end = System.nanoTime();
				delta1 = end - start;
				
				start = System.nanoTime();
				for (int j = 0; j < 5000; j++) {
					//System.out.println("Entering DYK " + j);
					float dik[] = dijkstras(graph, j);
				}
				end = System.nanoTime();
				delta2 = end - start;
				f_writer.write(i + "," + delta1 + "," + delta2);
				f_writer.newLine();
				
				//System.out.println("NUMERO " + i);
			}
			
			f_writer.close();
			
			
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		
		System.out.println("Done");
		
	}
	
	// Dijkstras algorithm
	public static float[] dijkstras(RandomAdjMatrixGraph G, int s){
		//get size of the graph
		int V = G.size;
		
		// create array with infinities at unreachable nodes
		float[][] AM = addInfinities(G);
		
		// distance array
		float[] D = new float[V];
		
		//array to see if vertex has been visited yet
		boolean[] visited = new boolean[V];
		
		// update distance array with relevant adj matrix information
		for (int v = 0; v < V; v++) {
			D[v] = AM[s][v];
		}
		
		// hash set to quickly check if a vertex is in set S
		HashSet<Integer> visitedSet = new HashSet<Integer>();
		
		// add the vertex to S
		visitedSet.add(s);
		visited[s] = true;
		
		
		float min;
		
		// main body of dijkstras algorithm
		for(int i = 0; i < V - 1; i++) {
			min = Float.POSITIVE_INFINITY;
		
			// select the minimum 
			int tracker = -1;
			for (int w = 0; w < V; w++) {
				// if not visited and path is less than current minimum
				if ((!visited[w] && (D[w] < min))) {
					//update this path to the minimum distance for connected vertices
					min = D[w];
					tracker = w;
				}
			}
			// if no connected vertices continue
			if (tracker == -1) continue;
			
			// add shortest path vertex to S
			visitedSet.add(tracker);
			visited[tracker] = true;
			
			// check if path is shorter than just going there directly from starting vertex
			for (int v = 0; v < V; v++) {
				if (visitedSet.contains(v)) continue;
				
				D[v] = Math.min(D[v], D[tracker] + AM[tracker][v]);
			}
			
			
		}
		return D;
		
	}
	
	
	// Adds infinities and copies the Random matrix to a float distance matrix
	public static float[][] addInfinities(RandomAdjMatrixGraph graph) {
		float[][] D = new float[graph.size][graph.size];
		for (int i = 0; i < graph.size; i++) {
			for (int j = 0; j < graph.size; j ++) {
				if (graph.getNode(i,j) == null) {
					D[i][j] = Float.POSITIVE_INFINITY;
				}
				else {
					D[i][j] = graph.getNode(i, j).getWeight();
				}
			}
			
		}
		return D;
	}
	
	
	// Code adapted from code on D2L by Jalal Kawash
	public static float[][] floyd(RandomAdjMatrixGraph G){
		
		int V = G.size;
		
		// initialize distance array 
		float[][] D = addInfinities(G);
		

		
		//set main diagonal to zeros
		for (int i = 0; i < V; i++) {
			D[i][i] = 0;
		}
		
		
		
		for (int k = 0; k < V; k++) {
			for (int i = 0; i < V; i ++) {
				for (int j = 0; j < V; j ++) {
					if ((D[i][k] + D[k][j]) < D[i][j]) D[i][j] = D[i][k] + D[k][j];
				}
				
			}
			
		}
		
		return D;
			
	}

}
