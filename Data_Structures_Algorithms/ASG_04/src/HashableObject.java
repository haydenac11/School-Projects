
/**
 *
 *
 *  Adapted from D2L code by Jalal Kawash
 *
 */

public class HashableObject implements Hashable{

	private int index;
	
	public HashableObject(int i) {
		index = i;
	}
	
	public int key() {
		return index;
	}
}
