package ocp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CH7_Concurrency {
	
	private static int counter = 0;

	public static void main(String[] args) {

		new Thread(
				()->{
					for (int i = 0; i < 10; i++) {
						System.out.println(i);
					}
				})
		.start();
		System.out.println(10000);
		
		
		 new Thread(() -> {
	         for(int i=0; i<500; i++) counter++;
	      }).start();

	      while(counter<100) {
	         System.out.println("Not reached yet");
	         try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	      }
	      System.out.println("Reached!");
	      
	      ExecutorService executorService = null;
	      try{
	    	  executorService = Executors.newSingleThreadExecutor();
	    	  executorService.execute(()->System.out.println("first"));
	    	  executorService.execute(()->System.out.println("second"));
	    	  executorService.execute(()->System.out.println("third"));
	    	  System.out.println("fourth");
	      }
	      finally {
			if (executorService != null){
				//failing to call shutdown() will result in your application never terminating.
				executorService.shutdown();
				executorService.shutdownNow();
			}
		}
	}

}
