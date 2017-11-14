package professional_ch4;

import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) {
		Stream<String> stream = Stream.of("w", "o", "l", "f");
		String word = stream.reduce("123 ", (s, c) -> s + c);
		System.out.println(word);    // wolf
		
		Stream<Integer> stream2= Stream.of(1, 2, 3);
		System.out.println(stream2.reduce(10, (a, b) -> a*b));
		
		BinaryOperator<Integer> op = (a, b) -> a * b;
		Stream<Integer> empty = Stream.empty();
		Stream<Integer> oneElement = Stream.of(3);
		Stream<Integer> threeElements = Stream.of(3, 5, 6);
		 
		empty.reduce(op).ifPresent(System.out::println); // no output
		oneElement.reduce(op).ifPresent(System.out::println); // 3
		threeElements.reduce(op).ifPresent(System.out::println); // 90

	}

}
