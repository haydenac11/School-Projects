
/**
 *
 *
 *  HashFunction given on D2L
 *
 */

public class HashFunction1 extends HashFunction{

	public int hash(int x) {
		x = ((x >>> 16) ^ x) * 0x45d9f3b;
		x = ((x >>> 16) ^ x) * 0x45d9f3b;
		x = (x >>> 16) ^ x;
		return Math.abs(x) % 9973;
	}
}
