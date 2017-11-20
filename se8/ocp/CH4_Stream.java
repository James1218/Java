package ocp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CH4_Stream {

	public static void main(String[] args) {
		Stream<Integer> streamInteger = Stream.of(1, 2, 3);
		streamInteger = Stream.generate(()->1);
		streamInteger = Stream.iterate(1, n -> n + 2);
		int a = streamInteger.limit(1).reduce(10, (x, y) -> x + y);
		System.out.println(a);
		
		streamInteger = Stream.of(1, 2, 3);
		Optional<Integer> optional = streamInteger.findAny();
		System.out.println(optional.orElse(111));
		
		Stream<String> stream = Stream.of("w", "o", "l", "f");
		//supplier, accumulator, combiner
		StringBuffer sb = stream.collect(()->new StringBuffer(), (x, y) -> x.append(y), (x, y) -> x.append(y));
		System.out.println(sb.toString());
		
		stream = Stream.of("w", "o", "l", "f");
		Set<String> set = stream.collect(()->new HashSet<>(), (x, y)->x.add(y), (x,y)->x.addAll((y)));
		System.out.println(set);
		
		stream = Stream.of("w", "o", "l", "f");
		set = stream.collect(Collectors.toSet());
		System.out.println(set);
		
		stream = Stream.of("w", "o", "l", "f");
		stream = stream.filter( x->x.equals("f"));
		set = stream.collect(Collectors.toSet());
		System.out.println(set);
		
		stream = Stream.of("w", "o", "l", "f");
		streamInteger = stream.map(x -> x.length());
		streamInteger.forEach(x->System.out.print(x));
		System.out.println();
		
		List<String> zero = Arrays.asList();
		List<String> one = Arrays.asList("Bonobo");
		List<String> two = Arrays.asList("Mama Gorilla", "Baby Gorilla");
		Stream<List<String>> animals = Stream.of(zero, one, two);
		stream = animals.flatMap(list->list.stream());
		
		IntStream intStream = IntStream.range(1, 6);
		streamInteger = intStream.mapToObj(x->x);
		streamInteger.forEach((x)->System.out.print(x));
		System.out.println();
		
		intStream = IntStream.rangeClosed(1, 10);
		OptionalDouble optionalDouble = intStream.average();
		System.out.println(optionalDouble.getAsDouble());
		
		Stream<String> ohMy = Stream.of("lions", "tigers", "bears");
		Map<String, Integer> map = ohMy.collect(Collectors.toMap(s -> s, s->s.length()));
		System.out.println(map); // {lions=5, bears=5, tigers=6}
		
		ohMy = Stream.of("lions", "tigers", "bears");
		Map<Integer, String> map2 = ohMy.collect(Collectors.toMap(
		    String::length, k -> k, (s1, s2) -> s1 + "," + s2));
		System.out.println(map2);  // {5=lions,bears, 6=tigers}
		System.out.println(map2.getClass());  // class. java.util.HashMap
		
		ohMy = Stream.of("lions", "tigers", "bears", "A");
		//toMap(Function k, Function v, BinaryOperator m, Supplier s)
		TreeMap<Integer, String> treeMap = ohMy.collect(Collectors.toMap(s->s.length(), s->s, (s1, s2)->s1+","+s2, ()->new TreeMap<>()));
		System.out.println(treeMap);
		
		ohMy = Stream.of("lions", "tigers", "bears", "B", "A");
		Map<Integer, List<String>> map3 = ohMy.collect(Collectors.groupingBy(x->x.length()));
		System.out.println(map3);
		System.out.println(map3.getClass());
		
		ohMy = Stream.of("lions", "tigers", "bears", "B", "A", "A");
		Map<Integer, Set<String>> map4 = ohMy.collect(Collectors.groupingBy(x->x.length(), Collectors.toCollection(TreeSet::new)));
		System.out.println("Collects.groupingBy : "+map4);
		
		ohMy = Stream.of("lions", "tigers", "bears", "B", "A", "A");
		//classifier, mapFactory, downstream
		map4 = ohMy.collect(Collectors.groupingBy(x->x.length(), ()->new TreeMap<>(), Collectors.toCollection(TreeSet::new)));
		System.out.println(map4);
		System.out.println(map4.getClass());
		
		ohMy = Stream.of("lions", "tigers", "bears", "B", "A", "A");
		Map<Boolean, List<String>> map5 = ohMy.collect(Collectors.partitioningBy(s->s.length() > 4));
		System.out.println(map5);
		
		ohMy = Stream.of("lions", "tigers", "bears", "B", "A", "A");
		Map<Integer, Long> map6 = ohMy.collect(Collectors.groupingBy(s->s.length(), Collectors.counting()));
		System.out.println(map6);

		ohMy = Stream.of("lions", "tigers", "bears");
		//classifier, downstream
		Map<Integer, Optional<Character>> map7 = ohMy.collect(
				Collectors.groupingBy(String::length, Collectors.mapping(s -> s.charAt(0), Collectors.minBy(Comparator.naturalOrder()))));
		System.out.println(map7); // {5=Optional[b], 6=Optional[t]}
		
		Function<String, String> f = String::new;
	}

}
