import java.util.Random;

public class part2 {

	public static void main(String[] args) {
		int first = 500;
		int second = 1000;
		int third = 5000;
		int amount = 100;
		double heightFirst = 0.0;
		double completeFirst = 0.0;
		double perfectFirst = 0.0;
		double heightSecond = 0.0;
		double completeSecond = 0.0;
		double perfectSecond = 0.0;
		double heightThird = 0.0;
		double completeThird = 0.0;
		double perfectThird = 0.0;
		
		
		for (int i = 1; i <= amount; i ++) {
			BST<Integer> treeFirst = randomTree(first);
			heightFirst += treeFirst.height();
			if (treeFirst.isComplete()) completeFirst++;
			if (treeFirst.isPerfect()) perfectFirst++;
			
			BST<Integer> treeSecond = randomTree(second);
			
			heightSecond += treeSecond.height();
			if (treeSecond.isComplete()) completeSecond++;
			if (treeSecond.isPerfect()) perfectSecond++;
			
			BST<Integer> treeThird = randomTree(third);
			heightThird += treeThird.height();
			if (treeThird.isComplete()) completeThird++;
			if (treeThird.isPerfect()) perfectThird++;
		}
		
		heightFirst = heightFirst / amount;
		completeFirst = completeFirst / amount;
		perfectFirst = perfectFirst / amount;
		
		heightSecond = heightSecond /amount;
		completeSecond = completeSecond / amount;
		perfectThird = perfectThird / amount;
		
		heightThird = heightThird / amount;
		completeThird = completeThird / amount;
		perfectThird = perfectThird / amount;
		
		System.out.println("Average Height for " + first + " nodes: " + heightFirst);
		System.out.println("Average Height for " + second + " nodes: " + heightSecond);
		System.out.println("Average Height for " + third + " nodes: " + heightThird);
		System.out.println();
		System.out.println("Average number of complete for " + first + " nodes: " + completeFirst);
		System.out.println("Average number of complete for " + second + " nodes: " + completeSecond);
		System.out.println("Average number of complete for " + third + " nodes: " + completeThird);
		System.out.println();
		System.out.println("Average number of perfect for " + first + " nodes: " + perfectFirst);
		System.out.println("Average number of perfect for " + second + " nodes: " + perfectSecond);
		System.out.println("Average number of perfect for " + third + " nodes: " + perfectThird);
		


	}
	
	public static BST<Integer> randomTree(int size){
		//System.out.println(size);
		BST<Integer> tree = new BST<>();
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			int nodeValue = rand.nextInt(size);
			tree.add(nodeValue);
		}
		return tree;
		
	}

}
