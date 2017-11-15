package ocp;

import java.util.function.Predicate;

public class CH1_Predicate {

	public static void main(String[] args) {
		Panda.main(args);

	}

}

class Panda {
	int age;
	public static void main(String[] args) {
		Panda p1 = new Panda();
		p1.age = 1;
		boolean result = check(p1, p -> p.age < 5);  
		System.out.println(result);
	}
	private static boolean check(Panda panda, Predicate<Panda> pred) { 
		boolean result = pred.test(panda) ;
		return result;
	}
}
