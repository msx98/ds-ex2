package il.ac.tau.cs.ds.proj2;

/*
 * Instructions:
 * The constants need to match HeapNode's fields. If something was modeled differently (different types, etc) then the
 * accessor functions need to be changed some more.
 * For example, if your mark field is an `int` instead of `bool`, you'll need to change `getMark`, and cast its result
 * to an int, and convert it to a boolean somehow\.
    final static String CHILD_FIELD = "child";
    final static String NEXT_FIELD = "next";
    final static String PARENT_FILED = "parent";
    final static String MARK_FILED = "mark";
    final static String RANK_FILED = "rank";
 * The tester will raise an exception if something is wrong - you can catch it in the debugger and see what went wrong.
 * It takes a couple of minutes for it to complete, don't worry about the noise :)
 */

import java.lang.reflect.Field;
import java.util.*;

class TestFail extends RuntimeException {
    public TestFail(String message) {
        super(message);
    }
}

public class TestFibonacciHeap {
    final static String CHILD_FIELD = "child";
    final static String NEXT_FIELD = "next";
    final static String PARENT_FILED = "parent";
    final static String MARK_FILED = "mark";
    final static String RANK_FILED = "rank";

    static Random rnd;
    static boolean alwaysVerify = true;

    private static ArrayList<Integer> generateInputSizes() {
        ArrayList<Integer> l = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            l.add(i);
        }

        l.add((int)Math.pow(2, 10));
        l.add((int)Math.pow(2, 15));
        l.add((int)Math.pow(2, 18));

        return l;
    }
    
    public static boolean isOrdered(int[] inputs) {
    	for (int i=0; i<inputs.length; i++) {
    		if (inputs[i] != i) return false;
    	}
    	return true;
    }
    
    public static boolean isReversed(int[] inputs) {
    	for (int i=0; i<inputs.length; i++) {
    		if (inputs[i] != inputs.length-1-i) return false;
    	}
    	return true;
    }
    
    public static boolean isShuffled(int[] inputs) {
    	return !isOrdered(inputs) && !isReversed(inputs);
    }

    public static void main(String[] args) {
        initRandom(42);
        for (int i : generateInputSizes()) {
            alwaysVerify = i < 1000;
            int[] ordered = createOrderedArray(i);
            int[] reversed = createReversedArray(i);
            int[] shuffled = createRandomArray(i);
            testInserts(shuffled);
            System.out.printf("Inserts for i=%d passed%n", i);
            insertionDeleMinTester(shuffled);
            System.out.printf("Inserts/DeleteMins shuffled for i=%d passed%n", i);
            insertionDeleMinTester(ordered);
            System.out.printf("Inserts/DeleteMins oredered for i=%d passed%n", i);
            insertionDeleMinTester(reversed);
            System.out.printf("Inserts/DeleteMins reversed for i=%d passed%n", i);
            insertionDeletionTester(reversed);
            System.out.printf("Inserts/Deletes shuffled for i=%d passed%n", i);
            insertionDeletionTester(ordered);
            System.out.printf("Inserts/Deletes ordered for i=%d passed%n", i);
            insertionDeletionTester(reversed);
            System.out.printf("Inserts/Deletes reversed for i=%d passed%n", i);
        }
    }

    private static void failIf(boolean cond, String message) {
        if (cond)
            throw new TestFail(message);
    }

    public static void initRandom(int seed) {
        rnd = new Random(seed);
    }

    public static void shuffle(int[] array) {
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, rnd.nextInt(i));
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static int[] createOrderedArray(int n) {
        int[] out = new int[n];

        for (int i = 0; i < n; i++) {
            out[i] = i;
        }

        return out;
    }

    public static int[] createRandomArray(int n) {
        int[] out = createOrderedArray(n);
        shuffle(out);
        return out;
    }

    public static int[] createReversedArray(int n) {
        int[] out = new int[n];

        for (int i = 0; i < n; i++) {
            out[n - 1 - i] = i;
        }

        return out;
    }

    public static FibonacciHeap heapFromArray(int[] arr) {
        FibonacciHeap h = new FibonacciHeap();
        for (int k: arr) {
            h.insert(k);
        }
        return h;
    }

    private static int minUntil(int[] arr, int count) {
        int min = arr[0];
        for (int i = 0; i < count; i++) {
            if (arr[i] < min)
                min = arr[i];
        }

        return min;
    }

    static void testInserts(int[] inputs) {
        FibonacciHeap h = new FibonacciHeap();
        int min = inputs[0];
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] < min) {
                min = inputs[i];
            }
            verifiedInsert(h, inputs[i]);
            failIf(min != h.findMin().getKey(), "Min calculation wrong");
            failIf(h.size() != i + 1, "Size wrong");
        }
    }

    private static Object getHeapNodeField(FibonacciHeap.HeapNode node, String name) {
        Field field;
        try {
            field = node.getClass().getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("No field " + name);
        }

        try {
            return field.get(node);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static FibonacciHeap.HeapNode getNext(FibonacciHeap.HeapNode node) {
        return (FibonacciHeap.HeapNode)getHeapNodeField(node, NEXT_FIELD);
    }

    private static FibonacciHeap.HeapNode getChild(FibonacciHeap.HeapNode node) {
        return (FibonacciHeap.HeapNode)getHeapNodeField(node, CHILD_FIELD);
    }

    private static FibonacciHeap.HeapNode getParent(FibonacciHeap.HeapNode node) {
        return (FibonacciHeap.HeapNode)getHeapNodeField(node, PARENT_FILED);
    }

    private static int getRank(FibonacciHeap.HeapNode node) {
        return (Integer)getHeapNodeField(node, RANK_FILED);
    }

    private static boolean getMark(FibonacciHeap.HeapNode node) {
        return (Boolean)getHeapNodeField(node, MARK_FILED);
    }

    private static int countNodes(FibonacciHeap.HeapNode node) {
        if (node == null) {
            return 0;
        }

        int count = 0;
        FibonacciHeap.HeapNode current = node;
        do {
            count += countNodes(getChild(current));
            count++;
            current = getNext(current);
        } while (current != node);

        return count;
    }

    private int findMaxRank(FibonacciHeap.HeapNode node) {
        if (node == null) {
            return 0;
        }

        FibonacciHeap.HeapNode current = node;
        int maxRank = 0;
        do {
            maxRank = Math.max(maxRank, getRank(current));
            current = getNext(current);
        } while (current != node);

        return maxRank;
    }

    private static int numMarkedChildren(FibonacciHeap.HeapNode node) {
        node = getChild(node);
        if (node == null) {
            return 0;
        }

        FibonacciHeap.HeapNode current = node;
        int marked = 0;
        do {
            if (getMark(current)) {
                marked++;
            }
            current = getNext(current);
        } while (current != node);

        return marked;
    }

    private static HashMap<Integer, Integer> countersDict(int[] counters) {
        HashMap<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < counters.length; i++) {
            int ctr = counters[i];
            if (ctr != 0) {
                m.put(i, ctr);
            }
        }

        return m;
    }

    private static HashMap<Integer, Integer> postDeleteMinCounters(HeapStats s1, FibonacciHeap.HeapNode node) {
        HashMap<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < s1.counters.length; i++) {
            int ctr = s1.counters[i];
            if (getRank(node) == i) {
                ctr--;
            }
            if (ctr != 0) {
                m.put(i, ctr);
            }
        }

        FibonacciHeap.HeapNode child = getChild(node);
        if (child == null) {
            return m;
        }

        FibonacciHeap.HeapNode current = child;
        do {
            int rank = getRank(current);
            int ctr = m.getOrDefault(rank, 0);
            m.put(rank, ctr + 1);
            current = getNext(current);
        } while (current != child);

        return m;
    }

    private static int[] trimCounters(int[] counters) {
        int maxRank = 0;
        for (int i = 0; i < counters.length; i++) {
            if (counters[i] != 0) {
                maxRank = i;
            }
        }

        return Arrays.copyOf(counters, maxRank + 1);
    }

    private static IntAndCounters consolidateCounters(int[] counters) {
        int totalLinks = 0;

        int consolidateSum = 0;
        for (int i = 0; i < counters.length; i++) {
            consolidateSum += counters[i] * (1 << i);
        }
        int maxPossibleRank = (int)Math.ceil(Math.log(consolidateSum) / Math.log(2)) + 1;
        int[] newCounters = Arrays.copyOf(counters, maxPossibleRank + 1);
        for (int i = 0; i < maxPossibleRank; i++) {
            int preConsolidateVal = newCounters[i];
            int links = preConsolidateVal / 2; // Floor division
            newCounters[i] = preConsolidateVal % 2;
            newCounters[i + 1] += links;
            totalLinks += links;
        }

        return new IntAndCounters(totalLinks, trimCounters(newCounters));
    }

    private static int[] mapToCounters(HashMap<Integer, Integer> m) {
        int newMaxRank = m.keySet().stream().mapToInt(i -> i).max().orElse(-1);
        int[] newInterimCounters = new int[newMaxRank + 1];
        for(Map.Entry<Integer, Integer> e : m.entrySet()) {
            newInterimCounters[e.getKey()] = e.getValue();
        }

        return newInterimCounters;
    }

    private static HeapStats getExpectedDeleteMinStats(FibonacciHeap.HeapNode toBeDeleted, HeapStats s1) {
        FibonacciHeap.HeapNode node = toBeDeleted;
        HashMap<Integer, Integer> m = postDeleteMinCounters(s1, node);
        int[] newInterimCounters = mapToCounters(m);

        IntAndCounters pair;
        if (newInterimCounters.length == 0) {
            pair = new IntAndCounters(0, new int[0]);
        } else {
            pair = consolidateCounters(newInterimCounters);
        }

        int[] newCounters = pair.counters;
        int newTreesCount = Arrays.stream(newCounters).sum();
        int oldTreesCount = Arrays.stream(s1.counters).sum();
        int numMarkedChildren = numMarkedChildren(node);
        int newPotential = s1.potential - numMarkedChildren * 2 + newTreesCount - oldTreesCount;

        HeapStats s2 = new HeapStats(s1);
        s2.counters = newCounters;
        s2.potential = newPotential;
        s2.totalLinks += pair.i;

        return s2;
    }

    private static void verifiedDeleteMin(FibonacciHeap h) {
        HeapStats s1 = new HeapStats(h);
        FibonacciHeap.HeapNode node = h.findMin();
        HeapStats expectedS2 = getExpectedDeleteMinStats(node, s1);
        h.deleteMin();
        HeapStats s2 = new HeapStats(h);
        if (!s2.equals(expectedS2)) {
        	System.out.println("PROBLEMO!");
        }
        failIf(!s2.equals(expectedS2), String.format("verifiedDeleteMin - Error in stats\nstats=%s\nexpected=%s",s2.toString(),expectedS2.toString()));
    }

    private static void verifiedDeleteMin(FibonacciHeap h, boolean verify) {
        if (verify) {
            verifiedDeleteMin(h);
        } else {
            h.deleteMin();
        }
    }

    private static void modCounterAt(HashMap<Integer, Integer> m, int key, int delta) {
            int ctr = m.getOrDefault(key, 0);
            ctr += delta;
            m.put(key, ctr);
    }

    private static HeapStats getExpectedDecreaseKeyStats(FibonacciHeap.HeapNode node, HeapStats s1) {
        HeapStats s2 = new HeapStats(s1);
        if (getParent(node) == null) {
            return s2;
        }

        HashMap<Integer, Integer> m = countersDict(s2.counters);
        FibonacciHeap.HeapNode current = node;
        int rankMod = 0;
        do {
            s2.totalCuts++;
            modCounterAt(m, getRank(current) + rankMod, 1);
            rankMod = -1;
            s2.potential++;
            if (getMark(current)) {
                s2.potential -= 2;
            }
            current = getParent(current);
            if (getParent(current) == null) {
                modCounterAt(m, getRank(current), -1);
                modCounterAt(m, getRank(current) - 1, 1);
            }
            if (getParent(current) != null && !getMark(current)) {
                s2.potential += 2;
            }
        } while (getMark(current));

        s2.counters = trimCounters(mapToCounters(m));
        return s2;
    }

    private static void verifiedDelete(FibonacciHeap h, FibonacciHeap.HeapNode node) {
        HeapStats s1 = new HeapStats(h);
        HeapStats expectedS2 = getExpectedDecreaseKeyStats(node, s1);
        h.decreaseKey(node, Integer.MAX_VALUE);
        HeapStats s2 = new HeapStats(h);
        failIf(!s2.equals(expectedS2), "verifiedDelete - Error in stats");
        verifiedDeleteMin(h);
    }

    private static void verifiedDelete(FibonacciHeap h, FibonacciHeap.HeapNode node, boolean verify) {
        if (verify) {
            verifiedDelete(h, node);
        } else {
            h.delete(node);
        }
    }

    private static FibonacciHeap.HeapNode verifiedInsert(FibonacciHeap h, int key) {
        int size = h.size();
        int oldMin = Integer.MAX_VALUE;
        if (size != 0) {
            oldMin = h.findMin().getKey();
        }
        FibonacciHeap.HeapNode node = h.insert(key);
        failIf(key < oldMin && h.findMin().getKey() != key, "Min not updated");
        failIf(h.size() - 1 != size, "Size should have increased");
        checkSize(h);
        return node;
    }

    private static boolean shouldVerify(FibonacciHeap h) {
        if(alwaysVerify) {
            return true;
        }

        return rnd.nextInt(h.size() / 100 + 1) == 10;
    }

    private static void checkSize(FibonacciHeap h) {
        if (!shouldVerify(h)) {
            return;
        }
        int counted = countNodes(h.findMin());
        int counted2 = countNodes(h.findMin());
        failIf(h.size() != counted, "Wrong size");
    }

    private static ArrayList<DictCommand> genInsDelCommands(int[] inputs) {
         int n = inputs.length;
        ArrayList<DictCommand> commands = new ArrayList<>();
        ArrayListSet s = new ArrayListSet();
        int removed = 0;
        int consumed = 0;

        while (consumed < n || removed < n) {
            int r = rnd.nextInt(n - removed);
            if (r + removed >= consumed) {
                int key = inputs[consumed];
                commands.add(new DictCommand(true, key));
                s.add(key);
                consumed += 1;
            } else {
                 int r2 = rnd.nextInt(s.size());
                 int key = s.removeAt(r2);
                 commands.add(new DictCommand(false, key));
                 removed += 1;
            }
        }

        return commands;
    }

    public static void insertionDeletionTester(int[] inputs) {
         int n = inputs.length;
        FibonacciHeap h = new FibonacciHeap();
        ArrayList<DictCommand> commands = genInsDelCommands(inputs);
        HashMap<Integer, FibonacciHeap.HeapNode> nodeByKey = new HashMap<>();

        failIf(commands.size() != 2 * n, "Bug in command creation");
        
        for (int i=0; i<commands.size(); i++) {
        	DictCommand c = commands.get(i);
        	//System.out.println(String.format("%d: %s %d", i, c.insert?"INS":"DEL", c.key));
        }

        int maxHeapSize = 0;
        for (int i = 0; i < commands.size(); i++) {
            DictCommand c = commands.get(i);
            //System.out.println("Executing command " + String.valueOf(i));
            if (c.insert) {
                FibonacciHeap.HeapNode node = verifiedInsert(h, c.key);
                maxHeapSize = Math.max(maxHeapSize, h.size());
                nodeByKey.put(c.key, node);
            } else {
                FibonacciHeap.HeapNode node = nodeByKey.get(c.key);
                verifiedDelete(h, node, shouldVerify(h));
                checkSize(h);
            }
        }
        System.out.println("Insertion/deletion max heap size: " + maxHeapSize);
    }

    public static void insertionDeleMinTester(int[] inputs) {
         int n = inputs.length;
        FibonacciHeap h = new FibonacciHeap();
        ArrayList<DictCommand> commands = genInsDelCommands(inputs);
        HashMap<Integer, FibonacciHeap.HeapNode> nodeByKey = new HashMap<>();

        failIf(commands.size() != 2 * n, "Bug in command creation");

        int maxHeapSize = 0;
        for (int i = 0; i < commands.size(); i++) {
            DictCommand c = commands.get(i);
            if (c.insert) {
                FibonacciHeap.HeapNode node = verifiedInsert(h, c.key);
                maxHeapSize = Math.max(maxHeapSize, h.size());
                nodeByKey.put(c.key, node);
            } else {
                FibonacciHeap.HeapNode node = nodeByKey.get(c.key);
                verifiedDeleteMin(h, shouldVerify(h));
                checkSize(h);
            }
        }
        System.out.println("Insertion/deleteMin max heap size: " + maxHeapSize);
    }
}

class DictCommand {
    public boolean insert;
    public int key;

    DictCommand(boolean insert, int key) {
        this.insert = insert;
        this.key = key;
    }
}

class HeapStats {
    public int totalCuts;
    public int totalLinks;
    public int potential;
    public int[] counters;

    HeapStats(FibonacciHeap h) {
        totalCuts = FibonacciHeap.totalCuts();
        totalLinks = FibonacciHeap.totalLinks();
        potential = h.potential();
        counters = h.countersRep();
    }

    public HeapStats(HeapStats s) {
        this.totalLinks = s.totalLinks;
        this.totalCuts = s.totalCuts;
        this.potential = s.potential;
        this.counters = Arrays.copyOf(s.counters, s.counters.length);
    }

    public HeapStats(int totalCuts, int totalLinks, int potential, int[] counters) {
        this.totalCuts = totalCuts;
        this.totalLinks = totalLinks;
        this.potential = potential;
        this.counters = counters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeapStats heapStats = (HeapStats) o;
        return totalCuts == heapStats.totalCuts && totalLinks == heapStats.totalLinks && potential == heapStats.potential && Arrays.equals(counters, heapStats.counters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(totalCuts, totalLinks, potential);
        result = 31 * result + Arrays.hashCode(counters);
        return result;
    }
    
    @Override
    public String toString() {
    	String s = "";
    	s += String.format("totalCuts=%d, totalLinks=%d, potential=%s, counters=%s", totalCuts, totalLinks, potential, Arrays.toString(counters));
    	return s;
    }
}

class IntAndCounters {
    public int i;
    public int[] counters;

    public IntAndCounters(int i, int[] counters) {
        this.i = i;
        this.counters = counters;
    }
}

class ArrayListSet {
    ArrayList<Integer> l;
    ArrayListSet() {
        l = new ArrayList<>();
    }

    public void add(int n) {
        l.add(n);
    }

    public int removeAt(int i) {
        int res = l.get(i);
        l.set(i, l.get(l.size() - 1));
        l.remove(l.size() - 1);
        return res;
    }

    int size() {
        return l.size();
    }
}