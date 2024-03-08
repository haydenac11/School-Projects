import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/* 
 * Hayden Church
 * 30133957
 */
public class Runner {

	public static void main(String[] args) {
		stackImplementation();
		queueImplementation();
	}
	
	
	
	
	public static void queueImplementation() {
		//variables to keep track of starting positions
        int cheeseX = 0;
        int cheeseY = 0;
        int mouseX = 0;
        int mouseY = 0;
		try {
			//create array to store the map
			Cell[][] grid = new Cell[50][50];
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/haydenchurch/Documents/maze.txt")));

			// variables to loop over the map we read in
            int charCode;
            int row;
            int col;
            int tally = 0;

            while ((charCode = bufferedReader.read()) != -1) {
                char character = (char) charCode;
                row = tally / 50; // y
                col = tally % 50; // x
                
                // check that the character read in isnt a newline and then create a cell for it
                if (character == 'm' || character == 'c' || character == '1' || character == '0') {
                	Cell cell = new Cell(row, col, character, false);
		        	grid[row][col] = cell;
		        	
		        	tally += 1;
		        	if (cell.cellType == 'm') {
		        		mouseX = col;
		        		mouseY = row;

		        	
		        	}
		        	if (cell.cellType == 'c') {
		        		cheeseX = col;
		        		cheeseY = row;
		        	}
		        }
                
          
            }
            
            // Algorithm provided
            LinkedQueue trailQueue = new LinkedQueue();
            LinkedQueue queue = new LinkedQueue();
            
            queue.enqueue(grid[mouseY][mouseX]);
            
            boolean moreToSearch = true;
            
            while ((!queue.isEmpty()) && moreToSearch) {
            	
            	Cell j = queue.dequeue();
            	j.visited = true;
            	trailQueue.enqueue(j);
            	
            	if (j.row == cheeseY && j.col == cheeseX) {
            		moreToSearch = false;
            	}
            	else {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            // Skip the center and corners
                            if (x == 0 && y == 0) continue;
                            if (x != 0 && y != 0) continue;
                            
                            int neighbourX = j.col + x;
                            int neighbourY = j.row + y;
                            
                            // Check in map
                            if (neighbourX >= 0 && neighbourX < 50 && neighbourY >= 0 && neighbourY < 50) {
                                Cell neighbour = grid[neighbourY][neighbourX];
                                
                                // Check it isnt a wall and hasnt been visitedd
                                if (neighbour.cellType != '1' && !neighbour.visited) {
                                    queue.enqueue(neighbour); 
                                }
                            }
                        }
                    }
            		
            	}
            }
            
            String version = "Queue Version";
            String file = "/Users/haydenchurch/Documents/maze_output_queue.txt";
            printMap(trailQueue, mouseY, mouseX, grid, moreToSearch, version, file);
            
            
            
            bufferedReader.close();
		}
		catch (IOException e) {
		System.out.print(e.getMessage());
		}
	
	}
	
	public static void stackImplementation() {
		//variables to keep track of intitial positions
        int cheeseX = 0;
        int cheeseY = 0;
        int mouseX = 0;
        int mouseY = 0;
		try {
			//create array to keep track of map
			Cell[][] grid = new Cell[50][50];
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/haydenchurch/Documents/maze.txt")));

			
            int charCode;
            int row;
            int col;
            int tally = 0;

            //read in the file 
            while ((charCode = bufferedReader.read()) != -1) {
                char character = (char) charCode;
                row = tally / 50; //y
                col = tally % 50; //x
                
                // check that the chracter read in isnt a newline character and then create a cell for it
                if (character == 'm' || character == 'c' || character == '1' || character == '0') {
                	Cell cell = new Cell(row, col, character, false);
		        	grid[row][col] = cell;
		        	
		        	tally += 1;
		        	if (cell.cellType == 'm') {
		        		mouseX = col;
		        		mouseY = row;

		        	
		        	}
		        	if (cell.cellType == 'c') {
		        		cheeseX = col;
		        		cheeseY = row;
		        	}
		        }
                
          
            }
            
            // algorithm provided
            LinkedQueue trailQueue = new LinkedQueue();
            LinkedStack stack = new LinkedStack();
            
            stack.push(grid[mouseY][mouseX]);
            
            boolean moreToSearch = true;
            
            while ((!stack.isEmpty()) && moreToSearch) {
            	
            	Cell j = stack.pop();
            	j.visited = true;
            	trailQueue.enqueue(j);
            	
            	if (j.row == cheeseY && j.col == cheeseX) {
            		moreToSearch = false;

            	}
            	else {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            // Skip the center and the corners
                            if (x == 0 && y == 0) continue;
                            if (x != 0 && y != 0) continue;
                            
                            int neighbourX = j.col + x;
                            int neighbourY = j.row + y;
                            
                            // Check if within map
                            if (neighbourX >= 0 && neighbourX < 50 && neighbourY >= 0 && neighbourY < 50) {
                                Cell neighbour = grid[neighbourY][neighbourX];
                                
                                // Check that it isnt a wall and that it hasnt been visited
                                if (neighbour.cellType != '1' && !neighbour.visited) {
                                    stack.push(neighbour); 
                                }
                            }
                        }
                    }
            		
            	}
            }
            
            String version = "Stack Version";
            String file = "/Users/haydenchurch/Documents/maze_output_stack.txt";
            printMap(trailQueue, mouseY, mouseX, grid, moreToSearch, version, file);
            
            
            
            bufferedReader.close();
		}
		catch (IOException e) {
		System.out.print(e.getMessage());
		}
	
	}
	
	public static void printMap(LinkedQueue trailQueue, int mouseY, int mouseX, Cell grid[][], boolean moreToSearch, String version, String file) {
		
		try {
		BufferedWriter f_writer = new BufferedWriter(new FileWriter(file));
		f_writer.write(version + "\n\n");
		if (!moreToSearch) {
			f_writer.write("CHEESE FOUND\n\n");
		}
		
        Cell rem;
        while (!trailQueue.isEmpty()) {
        	rem = trailQueue.dequeue(); //dequeue from trailQueue
        		
        		
        		if (rem.row > mouseY) { //if mouse moved down
        			if (rem.cellType != 'c' && rem.cellType != 'm') {
        				rem.cellType = 'v';
        			}
        			grid[rem.row][rem.col] = rem;
        		
        			mouseY = rem.row;
        		 
        		} else if (rem.row < mouseY) { //if mouse moved up
        			
        			if (rem.cellType != 'c' && rem.cellType != 'm') {
        				rem.cellType = '^';
        			}
        			grid[rem.row][rem.col] = rem;
        		
        			mouseY = rem.row;
        		
        		} else if (rem.col > mouseX) { //if mouse moved right
        			if (rem.cellType != 'c' && rem.cellType != 'm') {
        				rem.cellType = '>';
        			}
        			grid[rem.row][rem.col] = rem;
        		
        			mouseX = rem.col;
        	
        		} else if (rem.col < mouseX) { // if mouse moved left
        			if (rem.cellType != 'c' && rem.cellType != 'm') {
        				rem.cellType = '<';
        			}
        			grid[rem.row][rem.col] = rem;
        		
        			mouseX = rem.col;
        		}
        		
        		// for each cell in the 50 x 50 map, find the cell at that point and print out its type
    			for (int r = 0; r < 50; r++) {
    			    for (int c = 0; c < 50; c++) {
    			       rem = grid[r][c];
    			        
    			        f_writer.write(rem.cellType);
    			        
    			    }
    			    
    			    f_writer.write("\n");
    			}

    			f_writer.write("\n");
    			f_writer.write("\n");
    			f_writer.write("\n");
        		
        }
        

		f_writer.close();
		}
			catch (IOException e) {
				System.out.print(e.getMessage());
		}
	}

}
