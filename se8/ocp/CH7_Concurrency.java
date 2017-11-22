package ocp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CH7_Concurrency {

	private static int counter = 0;

	public static void main(String[] args) {

		new Thread( ()->{for (int i = 0; i < 10; i++) {System.out.println(i);}	}).start();
		System.out.println(10000);

		new Thread(() -> {for(int i=0; i<500; i++) counter++;}).start();
		while(counter<100) {
			System.out.println("Not reached yet");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Reached!");
		System.out.println();

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
				System.out.println();
			}
		}

		try{
			executorService = Executors.newSingleThreadExecutor();
			Future<Integer> future =  executorService.submit((Callable<Integer>)()->11+12);
			try {
				System.out.println(future.get(1, TimeUnit.SECONDS));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
		finally{
			if (executorService != null){
				executorService.shutdown();
				System.out.println();
			}
		}
		if(executorService != null) {
			try {
				executorService.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Check whether all tasks are finished
			if(executorService.isTerminated())
				System.out.println("All tasks finished");
			else
				System.out.println("At least one task is still running");
		}
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

		Runnable task1 = () -> System.out.println("Hello Zoo");
		Callable<String> task2 = () -> "Monkey";

		Future<?> result1 = service.schedule(task1, 10, TimeUnit.SECONDS);
		Future<?> result2 = service.schedule(task2, 8, TimeUnit.MINUTES);
	}

}
