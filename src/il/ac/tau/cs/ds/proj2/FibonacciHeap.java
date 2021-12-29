package il.ac.tau.cs.ds.proj2;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	int numOfNodes, markedNodes, numOfTrees;
	HeapNode minNode; // this is the node with the minimum key
	HeapNode first; // on insertion, this shall be the newest
	
	private void assertNumOfTrees() {
		// FIXME - missing implementation
	}
	
	private void assertIsEmpty() {
		Logger.assertd_iff(this.isEmpty(), this.size() == 0, this.numOfNodes==0,
				this.numOfTrees==0, this.minNode==null, this.first==null);
	}
	
	private void assertSanity() {
		assertIsEmpty();
		assertNumOfTrees();
	}

	public FibonacciHeap() {
		this.numOfNodes = 0;
		this.markedNodes = 0;
		this.numOfTrees = 0;
		this.minNode = null;
		Logger.assertd_iff(true, true, true, true);
	}
	/**
	 * public boolean isEmpty()
	 *
	 * Returns true if and only if the heap is empty.
	 * @complexity: O(1)
	 */
	public boolean isEmpty()
	{
		boolean result = this.minNode == null;
		
		Logger.assertd_iff(result, this.size() == 0, this.numOfNodes==0,
				this.numOfTrees==0, this.minNode==null, this.first==null);
		
		return result;
	}

	/**
	 * public HeapNode insert(int key)
	 *
	 * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
	 * The added key is assumed not to already belong to the heap.
	 *
	 * Returns the newly created node.
	 *
	 * @complexity: O(1)
	 */
	public HeapNode insert(int key)
	{
		HeapNode node = new HeapNode(key);

		if (this.isEmpty()) {
			this.minNode = node;
			this.first   = node;
		} else {
			// add to leftmost
			
			HeapNode last = this.getLast();
			node.setPrev(last);
			last.setNext(node);
			
			node.setNext(this.first);
			this.first.setPrev(node);
			
			this.first = node;
			
			// update min pointer accordingly
			if (this.minNode.getKey() > node.getKey()) {
				this.minNode = node;
			}
		}

		// update object parameters
		numOfNodes++; // added a new node 
		numOfTrees += 1; // this is technically a new tree
		
		return node;
	}

	/**
	 * public void deleteMin()
	 *
	 * Deletes the node containing the minimum key.
	 *
	 */
	public void deleteMin()
	{
		// FIXME - missing implementation
		return;

	}

	/**
	 * public HeapNode findMin()
	 *
	 * Returns the node of the heap whose key is minimal, or null if the heap is empty.
	 *
	 * @complexity: O(1)
	 */
	public HeapNode findMin()
	{
		return this.minNode;
	}

	/**
	 * public void meld (FibonacciHeap heap2)
	 *
	 * (lazy) melds heap2 with the current heap in order:
	 * 		this -> heap2
	 *
	 */
	public void meld (FibonacciHeap heap2)
	{
		if (heap2 == null) return;
		
		if (this.isEmpty() && heap2.isEmpty()) {
			// both are empty
			// do nothing
		} else if (this.isEmpty() && !heap2.isEmpty()) {
			// only this is empty
			// simply update all object properties
			heap2.copyTo(this);
		} else if (!this.isEmpty() && heap2.isEmpty()) {
			// only heap2 is empty
			// simply update all object properties
			this.copyTo(heap2);
		} else {
			// neither are empty
			this.minNode = this.findMin().getKey() < heap2.findMin().getKey() ?
					this.minNode
					: heap2.minNode;
			
			HeapNode thisFirst = this.getFirst();
			HeapNode thisLast = this.getLast();
			HeapNode secFirst = heap2.getFirst();
			HeapNode secLast = heap2.getLast();
			
			thisFirst.setPrev(secLast);
			secLast.setNext(thisFirst);
			thisLast.setNext(secFirst);
			secFirst.setPrev(thisLast);
			
			this.numOfNodes  += heap2.numOfNodes;
			this.numOfTrees  += heap2.numOfTrees;
			this.markedNodes += heap2.markedNodes;
			
			this.copyTo(heap2);
		}
	}

	/**
	 * public int size()
	 *
	 * Returns the number of elements in the heap.
	 *
	 * @complexity: O(1)
	 */
	public int size()
	{
		return this.numOfNodes;
	}

	/**
	 * public int[] countersRep()
	 *
	 * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
	 * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
	 * No requirements on complexity
	 *
	 * @complexity: O(n)
	 */
	public int[] countersRep()
	{
		if (this.isEmpty()) return new int[0];

		// get max rank first
		int maxRank = 0;
		HeapNode p = this.first;
		do {
			maxRank = Math.max(maxRank, p.getRank());
			p = p.getNext();
		} while (p != this.first); 
		int[] arr = new int[maxRank+1];
		for (int i=0; i<=maxRank; i++) arr[i] = 0;
		
		// now fill array
		p = this.first;
		do {
			arr[p.getRank()]++;
			p = p.getNext();
		} while (p != this.first);
		
		return arr;
	}

	/**
	 * public void delete(HeapNode x)
	 *
	 * Deletes the node x from the heap.
	 * It is assumed that x indeed belongs to the heap.
	 *
	 */
	public void delete(HeapNode x) // FIXME - verify @complexity
	{
		Logger.assertd(this.minNode != null);
		if (this.minNode == null) return;
		
		// delete() will never be called with MIN_VALUE in the heap
		// so don't test for this
		Logger.assertd(this.minNode.getKey() != Integer.MIN_VALUE);
		// still, just in case...
		if (this.minNode.getKey() == Integer.MIN_VALUE) {
			// FIXME - this is a dirty solution and I'm not sure it'll work
			//         but this case will supposedly not be tested anyway
			this.minNode = x;
		}
		
		x.key = Integer.MIN_VALUE; // OMG! NO LONGER VALID! QUICK, DECREASEKEY!!! AAAHHH
		decreaseKey(x, 0);
		deleteMin();
	}

	/**
	 * public void decreaseKey(HeapNode x, int delta)
	 *
	 * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
	 * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
	 */
	public void decreaseKey(HeapNode x, int delta)
	{
		// FIXME - missing implementation
		Logger.assertd(delta >= 0);
		Logger.assertd(x.key-delta >= Integer.MIN_VALUE);
		
		// implement this
		return;
	}

	/**
	 * public int potential()
	 *
	 * This function returns the current potential of the heap, which is:
	 * Potential = #trees + 2*#marked
	 *
	 * In words: The potential equals to the number of trees in the heap
	 * plus twice the number of marked nodes in the heap.
	 *
	 * @complexity: O(1)
	 */
	public int potential()
	{
		return this.numOfTrees + 2*this.markedNodes;
	}

	/**
	 * public static int totalLinks()
	 *
	 * This static function returns the total number of link operations made during the
	 * run-time of the program. A link operation is the operation which gets as input two
	 * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
	 * tree which has larger value in its root under the other tree.
	 */
	public static int totalLinks()
	{
		return Logger.COUNT_LINKS; // FIXME - might not wanna use Logger
	}

	/**
	 * public static int totalCuts()
	 *
	 * This static function returns the total number of cut operations made during the
	 * run-time of the program. A cut operation is the operation which disconnects a subtree
	 * from its parent (during decreaseKey/delete methods).
	 */
	public static int totalCuts()
	{
		return Logger.COUNT_CUTS; // FIXME - might not wanna use Logger
	}

	/**
	 * public static int[] kMin(FibonacciHeap H, int k)
	 *
	 * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
	 * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
	 *
	 * ###CRITICAL### : you are NOT allowed to change H.
	 */
	public static int[] kMin(FibonacciHeap H, int k)
	{
		// FIXME - missing implementation
		int[] arr = new int[100];
		return arr;
	}
	
	/**
	 * private HeapNode getFirst()
	 * 
	 * Returns pointer to first node in heap
	 * @complexity: O(1)
	 */
	private HeapNode getFirst()
	{
		if (this.isEmpty()) return null;
		else                return this.first;
	}
	
	/**
	 * private HeapNode getLast()
	 * 
	 * Returns pointer to last node in heap
	 * @complexity: O(1)
	 */
	private HeapNode getLast()
	{
		if (this.isEmpty()) return null;
		else                return this.getFirst().getPrev();
	}
	
	/**
	 * private void copyFrom(FibonacciHeap H)
	 * 
	 * Copies all properties of H into this
	 * @complexity: O(1)
	 */
	private void copyFrom(FibonacciHeap H)
	{
		Logger.assertd(H != null);
		if (H == null) return;
		this.minNode     = H.minNode;
		this.first       = H.first;
		this.numOfNodes  = H.numOfNodes;
		this.numOfTrees  = H.numOfTrees;
		this.markedNodes = H.markedNodes;
	}
	
	/**
	 * private void copyTo(FibonacciHeap H)
	 * 
	 * Copies all properties of this into H
	 * @complexity: O(1)
	 */
	private void copyTo(FibonacciHeap H)
	{
		Logger.assertd(H != null);
		if (H == null) return;
		H.copyFrom(this);
	}
	
	/**
	 * private static void consolidate(FibonacciHeap H)
	 * 
	 * This function consolidates H, altering it
	 * @complexity: O(n) // FIXME - is this the complexity?
	 */
	private static void consolidate(FibonacciHeap H)
	{
		// FIXME - missing implementation
		if (H == null || H.isEmpty()) return;
		
		// we don't need to be precise with maxRank, we just need this to be LARGER than maxRank
		// and this does it in O(1) without needing to maintain maxRank
		int maxRank = (int) Math.ceil(Math.log10(H.numOfTrees)/Math.log10(2));
		HeapNode rootInRank[] = new HeapNode[maxRank+1];
		for (int i=0; i<=maxRank; i++) rootInRank[i] = null;
		
		HeapNode p = H.getFirst();
		HeapNode pNext;
		HeapNode pLast = H.getLast();
		do {
			
		} while (p != pLast);
	}
	
	/**
	 * private static HeapNode joinTwoRoots(HeapNode node1, HeapNode node2)
	 * 
	 * @pre: node1.rank == node2.rank
	 * @pre: node*.prev == node*.next == node*.parent == null 
	 * @pre: node* != null
	 * @post: new root
	 */
	private static HeapNode joinTwoRoots(HeapNode node1, HeapNode node2) {
		Logger.assertd(node1.rank == node2.rank);
		Logger.assertd(node1.prev == null && node1.next == null && node1.parent == null);
		Logger.assertd(node2.prev == null && node2.next == null && node2.parent == null);
		Logger.assertd(node1 != null && node2 != null);
		
		HeapNode newRoot  = (node1.getKey() < node2.getKey())? node1 :node2;
		HeapNode newChild = (node1.getKey() < node2.getKey())? node2 :node1;
		
		if (newRoot.getChild() == null) {
			
		}
		
		return newRoot;
	}
	
	/**
	 * private static double log2(double x)
	 * 
	 * Returns log(x) in base 2
	 */
	private static double log2(double x) {
		return Math.log10(x)/Math.log10(2);
	}

	/**
	 * public class HeapNode
	 *
	 * If you wish to implement classes other than FibonacciHeap
	 * (for example HeapNode), do it in this file, not in another file.
	 *
	 */
	public static class HeapNode {

		int key, rank;
		HeapNode parent, child, next, prev;
		boolean mark;

		public HeapNode(int key) {
			this.key = key;
			this.rank = 0;
			this.next = this;
			this.prev = this;
			this.parent = null;
			this.child = null;
			this.mark = false;
		}

		/**
		* all get methods
		*
		* @complexity: O(1) for all
		* */

		public HeapNode getParent() {
			return this.parent;
		}

		public HeapNode getChild() {
			return this.child;
		}

		public HeapNode getNext() {
			return this.next;
		}

		public HeapNode getPrev() {
			return this.prev;
		}

		public int getKey() {
			return this.key;
		}

		public int getRank() {
			return rank;
		}

		/**
		* all set methods
		*
		* @complexity: O(1) for all
		* */

		public void setParent(HeapNode parent) {
			this.parent = parent;
		}

		public void setChild(HeapNode child) {
			this.child = child;
		}

		public void setNext(HeapNode next) {
			this.next = next;
		}

		public void setPrev(HeapNode prev) {
			this.prev = prev;
		}
	}

}


final class Logger {
	private Logger() {}
	public static final boolean FLAG_DEBUG   = true; // FIXME - change to false before assignment
	public static final boolean FLAG_VERBOSE = true; // FIXME - change to false before assignment
	
	public static void assertd(boolean condition) {
		if (FLAG_DEBUG) {
			if (!condition) {
				Logger.ASSERTION_TRIGGERS += 1;
				System.out.println("About to throw assertion");
				assert (condition);
			}
		}
	}
	
	public static void assertd_iff(boolean ... conditions) {
		if (!FLAG_DEBUG) return;
		for (int i=1; i<conditions.length; i++) {
			assertd((conditions[0]&&conditions[i]) || (!conditions[0] && !conditions[i]));
		}
	}

	public static void logd(String s) {
		if (FLAG_VERBOSE)
			System.out.println(s);
	}
	
	public static int ASSERTION_TRIGGERS  = 0;
	public static int COUNT_INSERTIONS	  = 0; // FIXME - never updated
	public static int COUNT_DELETIONS	  = 0; // FIXME - never updated
	public static int COUNT_LINKS         = 0; // FIXME - never updated (and required)
	public static int COUNT_CUTS          = 0; // FIXME - never updated (and required)
}