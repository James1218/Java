package ch4_associate;

import java.util.ArrayList;
import java.util.List;

public class Test {

	static class Sparrow extends Bird { }
	static class Bird { }

	public static void main(String[] args) {



		String s = "a";
		Object o = "a";
		System.out.println(o.equals(s));
		System.out.println(s.equals(o));
	}

}



