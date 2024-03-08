import java.io.IOException;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class ASG_04 {

	public static void main(String[] args) {
		try {
			
			BufferedWriter f_writer = new BufferedWriter(new FileWriter("/Users/haydenchurch/Documents/asg_04_values.csv"));
			long start;
			long end;
			long delta1; //linear
			long delta2; //binary
			long delta3; //hashtable
			
			//generate the elements
			int[] elements = generateElements();

			// variables 
			boolean found;
			int foundBinary;
			
			// create instance of hash function
			HashFunction1 hasher = new HashFunction1();
			
			// for loop for incrementing array size
			for (int i = 1000; i < 1000000; i += 1000) {
				// generate the search list
				int[] search = generateSearch(i);
				
				// create hashtable
				HashTableSC hashtable = new HashTableSC(hasher, 9973);
	
				
				// measure time for linear search
				start = System.nanoTime();
				for (int k = 0; k < elements.length; k++) {
					found = sequential(elements[k], search);
				}
				end = System.nanoTime();
				delta1 = end - start;
				
				
				// sort the data
				mergeSort(search);
				
				// add all the data to the hash table
				for (int k = 0; k < search.length; k++) {
					HashableObject temp = new HashableObject(search[k]);
					hashtable.add(temp);
				}
				
				// measure time for binary search
				start = System.nanoTime();
				for (int k = 0; k < elements.length; k++) {
					foundBinary = binarySearch(search, elements[k], 0, i-1);
				}
				end = System.nanoTime();
				delta2 = end - start;
				start = System.nanoTime();
				
				
				// measure time for hash table search
				for (int k = 0; k < elements.length; k++) {
					found = hashtable.contains(new HashableObject(elements[k]));
				}
				end = System.nanoTime();
				delta3 = end - start;
				
				
				// write data into a csv
				f_writer.write(delta1 + "," + delta2 + "," + delta3);
				f_writer.newLine();
				
				// dereference search array and hashtable
				search = null;
				hashtable = null;
			}
			
			f_writer.close();
			
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
				
			System.out.println("DONE");	

	}
	
	// linear search
	public static boolean sequential(int element, int[] search) {
		int i = 0;
		while (i < search.length) {
			if (element == search[i]) return true;
			i++;
		}
		return false;
	}
	
	
    
    // The merge sort implementation has been adapted from the code given on D2L by Jalal Kawash
    public static void mergeSort(int[] array) {
        if (array.length <= 1) {
            return;
        } else {
            int n = array.length;
            int[] tempArray = new int[n];
            for (int i = 0; i < n; i++) {
                tempArray[i] = array[i];
            }
            recMergeSort(tempArray, array, 0, n - 1);
        }
    }

    private static void recMergeSort(int[] tempArray, int[] array, int low, int high) {
        if (low < high) {
            int midPoint = (low + high) / 2;
            recMergeSort(tempArray, array, low, midPoint);
            recMergeSort(tempArray, array, midPoint + 1, high);
            merge(tempArray, array, low, high, midPoint);
        }
    }

    private static void merge(int[] tempArray, int[] array, int low, int high, int mid) {
        int n1 = mid - low + 1;
        int n2 = high - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        
        for (int i = 0; i < n1; i++) {
            leftArray[i] = tempArray[low + i];
        }

        
        for (int i = 0; i < n2; i++) {
            rightArray[i] = tempArray[mid + 1 + i];
        }

        int i = 0, j = 0, k = low;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                tempArray[k] = leftArray[i];
                i++;
            } else {
                tempArray[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            tempArray[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            tempArray[k] = rightArray[j];
            j++;
            k++;
        }

        
        for (int m = low; m <= high; m++) {
            array[m] = tempArray[m];
        }
    }



    //  binary search
    public static int binarySearch(int[] search, int element, int low, int high) {
    	if (low > high) return -1;
    	int mid = (high + low) / 2;
    	
    	if (search[mid] == element) {
    		return mid;
    	} else if (search[mid] < element) {
    		return binarySearch(search, element, mid + 1, high);
    	} else {
    		return binarySearch(search, element, low, mid - 1);
    	}
    			
    }
    
    // generate the search list
	public static int[] generateSearch(int size) {
		int search[] = new int[size];
		Random rand = new Random();
		
		for (int i = 0; i < size; i++) {
			search[i] = rand.nextInt(5001);
		}
		return search;
	}
	
	
	// generate the elements list
	public static int[] generateElements() {
		
		int elements[] = new int[100];
		Random rand = new Random();
		
		for(int i = 0; i < 99; i++) {
			elements[i] = rand.nextInt(5001);
		}
		elements[99] = 5001;
		return elements;
	}

}
