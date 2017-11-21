package ocp;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/*
 *  Callable can throw a checked Exception, Runnable not
 *  
 */
public class CH7_AmbiguousLambdaSample {

	public static void useCallable(Callable<Integer> expression) {}
	public static void useSupplier(Supplier<Integer> expression) {}

	public static void use(Supplier<Integer> expression) {}
	public static void use(Callable<Integer> expression) {}
	public static void main(String[] args) {
		useCallable(() -> {throw new IOException();});  // COMPILES
		/*
		 * The use() method is overloaded to take both Callable and Supplier parameters. 
		 * The compiler does not take into account the fact that the body of the lambda expression happens to throw an exception; 
		 * therefore, it does not know how to tell them apart. As you might have already guessed, 
		 * when the compiler doesn’t know what to do, it reports an error and does not compile. 
		 * When the compiler is unable to assign a functional interface to a lambda expression, 
		 * it is referred to as an ambiguous lambda expression.
		 * Note that the ambiguity can be resolved with an explicit cast. 
		 */
		use((Callable<Integer>)() -> {throw new IOException("");});  // COMPILES
		//Supplier does not support checked exceptions.
//		useSupplier(() -> {throw new IOException();});  // DOES NOT COMPILE
//		use(() -> {throw new IOException();});          // DOES NOT COMPILE
	}


}
