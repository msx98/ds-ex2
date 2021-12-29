package il.ac.tau.cs.ds.proj2;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    HeapNode minNode;   //Pointer for the minimum node of the forest
    int size, markedNodes, numOfTrees;
    public HeapNode head = minNode;
    public HeapNode tail;

    public FibonacciHeap() {
        this.size = 0;
        this.markedNodes = 0;
        this.numOfTrees = 0;
        this.minNode = null;
    }
    /**
     * public boolean isEmpty()
     *
     * Returns true if and only if the heap is empty.
     * @complexity == O(1)
     */
    public boolean isEmpty()
    {
        return this.minNode == null; // should be replaced by student code
    }

    /**
     * public HeapNode insert(int key)
     *
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     *
     * Returns the newly created node.
     *
     * @complexity == O(1)
     */
    public HeapNode insert(int key)
    {
        HeapNode node = new HeapNode(key);
        this.size++;
        this.numOfTrees++;

        // If the heap is empty,
        if (this.isEmpty()) {
            this.minNode = node;
            return node;
        }

        // If we're adding a new root for the forest
        HeapNode temp = minNode.next;
        node.setNext(temp);
        node.setPrev(minNode);
        minNode.setNext(node);
        temp.setPrev(node);

        // updating the minimum pointer in case we added new minimal key
        if (minNode.key > key) {
            this.minNode = node;
        }

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
        return; // should be replaced by student code

    }

    /**
     * public HeapNode findMin()
     *
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     *
     * @complexity == O(1)
     */
    public HeapNode findMin()
    {
        return this.minNode;// should be replaced by student code
    }

    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Melds heap2 with the current heap.
     *
     */
    public void meld (FibonacciHeap heap2)
    {
        return; // should be replaced by student code
    }

    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     * @complexity == O(1)
     */
    public int size()
    {
        return this.size; // should be replaced by student code
    }

    /**
     * public int[] countersRep()
     *
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
     *
     */
    public int[] countersRep()
    {
        int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }

    /**
     * public void delete(HeapNode x)
     *
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     *
     */
    public void delete(HeapNode x)
    {
        return; // should be replaced by student code
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta)
    {
        return; // should be replaced by student code
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
     * @complexity == O(1)
     */
    public int potential()
    {
        return this.numOfTrees + 2*this.markedNodes; // should be replaced by student code
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
        return 222; // should be replaced by student code
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
        return -456; // should be replaced by student code
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
        int[] arr = new int[100];
        return arr; // should be replaced by student code
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
            this.parent = null;
            this.child = null;
            this.next = null;
            this.prev = null;
            this.mark = false;
        }

        /**
        * all get methods
        *
        * @complexity == O(1) for all
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
        * @complexity == O(1) for all
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

	public static void logd(String s) {
		if (FLAG_VERBOSE)
			System.out.println(s);
	}
	
	public static int ASSERTION_TRIGGERS  = 0;
	public static int TOTAL_INSERTIONS    = 0;
	public static int TOTAL_DELETIONS     = 0;
	public static int TOTAL_SPLITS        = 0;
	public static int TOTAL_JOINS         = 0;
	public static int TOTAL_BALANCE       = 0;
}