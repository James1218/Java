package ocp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CH3_Comparator {

	public static void main(String[] args) {

		Squirrel squirrel = new Squirrel("a");
		squirrel.setWeight(11);
		Squirrel squirrel2 = new Squirrel("a");
		squirrel2.setWeight(2);
		List<Squirrel> list = new ArrayList<>();
		list.add(squirrel);
		list.add(squirrel2);
		//descending
		System.out.println(list);
		//ascending
		Collections.sort(list, new SquirrelComparator());
		System.out.println(list);
		//descending
		Collections.sort(list, (s1, s2)->(s2.getWeight() - s1.getWeight()));
		System.out.println(list);
		//ascending
		Collections.sort(list, (Squirrel s1, Squirrel s2)->{
			Comparator<Squirrel> c = Comparator.comparing(s->s.getSpecies());
			c = c.thenComparing(s->s.getWeight());
			return	c.compare(s1, s2);
		});
		System.out.println(list);
		//descending
		Collections.sort(list, 
				Comparator.comparing((Squirrel s) -> s.getSpecies())
				.thenComparing(s->s.getWeight()).reversed());
		System.out.println(list);
		//ascending
		Collections.sort(list, 
				Comparator.comparing(Squirrel::getSpecies)
				.thenComparing(Squirrel::getWeight));
		System.out.println(list);
	}

}

class SquirrelComparator implements Comparator<Squirrel>{

	@Override
	public int compare(Squirrel o1, Squirrel o2) {
		Comparator<Squirrel> c = Comparator.comparing(s->s.getSpecies());
		c = c.thenComparing(s->s.getWeight());
		return c.compare(o1, o2);
	}

}

class Squirrel {
	private int weight;
	private String species;
	public Squirrel(String theSpecies) {
		if (theSpecies == null) throw new IllegalArgumentException();
		species = theSpecies;
	}
	public int getWeight() { return weight; }
	public void setWeight(int weight) { this.weight = weight; }
	public String getSpecies() { return species; }
	@Override
	public String toString(){
		return getSpecies() + ":" + getWeight();
	}
}
