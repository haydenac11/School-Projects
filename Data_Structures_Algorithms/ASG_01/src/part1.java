import java.math.BigInteger;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class part1 {

	public static void main(String[] args) {

	try {
		BufferedWriter f_writer = new BufferedWriter(new FileWriter("/Users/haydenchurch/Documents/fib_data.txt"));
		long start;
		long end;
		long delta;
		BigInteger value;
		int max = 53;
		
		for(int i = 0; i <= max; i++) {
			start = System.nanoTime();
			value = Fib1(i);
			end = System.nanoTime();
			delta = end - start;
			System.out.println(value.toString() + "\t" + Long.toString(delta) + "\n");
			f_writer.write(value.toString() + "\t" + Long.toString(delta) + "\n");
		}
		
		f_writer.write("\n");
		
		for(int i = 0; i <= max; i++) {
			start = System.nanoTime();
			value = Fib2(i);
			end = System.nanoTime();
			delta = end - start;
			System.out.println(value.toString() + "\t" + Long.toString(delta) + "\n");
			f_writer.write(value.toString() + "\t" + Long.toString(delta) + "\n");
		}
		
		f_writer.write("\n");
		
		BigInteger f[] = new BigInteger[55];
		
		for(int i = 0; i <= max; i++) {
			Arrays.fill(f, BigInteger.valueOf(0));
			f[1] = BigInteger.valueOf(1);
			f[2] = BigInteger.valueOf(1);
			start = System.nanoTime();
			value = Fib3(i, f);
			end = System.nanoTime();
			delta = end - start;
			System.out.println(value.toString() + "\t" + Long.toString(delta) + "\n");
			f_writer.write(value.toString() + "\t" + Long.toString(delta) + "\n");
		}
		
		f_writer.close();
				
	}
	
	catch (IOException e) {
		System.out.print(e.getMessage());
	}
	
		
	try {
		BufferedWriter f_writer = new BufferedWriter(new FileWriter("/Users/haydenchurch/Documents/fib_5000_data.txt"));
		long start;
		long end;
		long delta;
		BigInteger value;
		int max = 4999;
		
		
		for(int i = 0; i <= max; i++) {
			start = System.nanoTime();
			value = Fib2(i);
			end = System.nanoTime();
			delta = end - start;
			//System.out.println(value.toString() + "\t" + Long.toString(delta) + "\n");
			f_writer.write(value.toString() + "\t" + Long.toString(delta) + "\n");
		}
		
		f_writer.write("\n");
		
		BigInteger f[] = new BigInteger[5001];
		
		for(int i = 0; i <= max; i++) {
			Arrays.fill(f, BigInteger.valueOf(0));
			f[1] = BigInteger.valueOf(1);
			f[2] = BigInteger.valueOf(1);
			start = System.nanoTime();
			value = Fib3(i, f);
			end = System.nanoTime();
			delta = end - start;
			//System.out.println(value.toString() + "\t" + Long.toString(delta) + "\n");
			f_writer.write(value.toString() + "\t" + Long.toString(delta) + "\n");
		}
		
		f_writer.close();
				
	}
	
	catch (IOException e) {
		System.out.print(e.getMessage());
	}
	
	

	}

	
	public static BigInteger Fib1(int n) {
		if (n < 2) {
			BigInteger big_n = BigInteger.valueOf(n);
			return big_n;
		}
		else return Fib1(n-1).add(Fib1(n-2));
	}
	
	public static BigInteger Fib2(int n) {
		BigInteger i = BigInteger.valueOf(1);
		BigInteger j = BigInteger.valueOf(0);
		for(int k = 1; k <= n; k ++) {
			j = i.add(j);
			i = j.subtract(i);
		}
		return j;
	}
	
	public static BigInteger Fib3(int n, BigInteger f[]) {
		int k;
		if (n < 3) return f[n];
		if (!f[n].equals(BigInteger.valueOf(0))) return f[n];
		if ((n % 2) != 0) {
			k = (n + 1) / 2;
		}
		else {
			k = n / 2;
		}
		if ((n % 2) != 0) {
			f[n] = (Fib3(k, f).pow(2)).add(Fib3(k - 1, f).pow(2));
		}
		else {
			f[n] = ((Fib3(k-1, f).multiply(BigInteger.valueOf(2))).add(Fib3(k, f))).multiply(Fib3(k, f));
		}
		return f[n];
	}
	
}
