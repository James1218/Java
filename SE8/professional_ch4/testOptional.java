package professional_ch4;

import java.util.Optional;

public class testOptional {

	public static void main(String[] args) throws Exception {
		
		int [] scores = {90,100 };
		Optional<Double> optional = average(scores);
		optional.ifPresent(System.out::println);
		optional.ifPresent(x -> System.out.println(x));
		System.out.println(optional.orElse(Math.random()));
		System.out.println(optional.orElseGet(Math::random));
		System.out.println(optional.orElseThrow(()->new Exception("My Exception")));

	}
	
	private static Optional<Double> average (int [] scores){
		if (scores.length == 0){
			return Optional.empty();
		}
		int sum = 0;
		for (int score : scores){
			sum += score;
		}
		return Optional.of((double)sum / scores.length);
	}

}
