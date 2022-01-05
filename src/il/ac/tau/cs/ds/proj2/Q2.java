package il.ac.tau.cs.ds.proj2;

public class Q2 {
	public static void run() {
		System.out.println("Q2.run() - START");
		
		run_for((int)(Math.pow(3,6)-1));
		System.out.println();
		run_for((int)(Math.pow(3,8)-1));
		System.out.println();
		run_for((int)(Math.pow(3,10)-1));
		System.out.println();
		run_for((int)(Math.pow(3,12)-1));
		System.out.println();
		run_for((int)(Math.pow(3,14)-1));
		
		System.out.println("Q2.run() - END");
	}
	
	public static void run_for(int m) {
		System.out.println("Q2.run_for("+String.valueOf(m)+") - START");
		
		int k, i;
		int three_quart_m = (int)Math.ceil((3*m)/4)+1;
		Timer timer = new Timer();
		FibonacciHeap H = new FibonacciHeap();
		
		Logger.reset();
		timer.reset();
		
		for (k=0; k<=m; k++) {
			H.insert(k);
		}
		
		int linkPrev = 0;
		int linkTot = 0;
		for (i=1; i<2; i++) {
			H.deleteMin();
			linkTot += H.totalLinks() - linkPrev;
			linkPrev = H.totalLinks();
		}
		
		
		
		long pace = timer.split();
		System.out.println(String.format("Q2.run_for(%d) - \n\tRuntime = %d ms\n\ttotalLinks = %d\n\ttotalCuts=%d\n\tPotential=%d", m, pace, H.totalLinks(), H.totalCuts(), H.potential()));

		Logger.reset();
		timer.reset();
		
		System.out.println("Q2.run_for("+String.valueOf(m)+") - END\n");
	}
}
