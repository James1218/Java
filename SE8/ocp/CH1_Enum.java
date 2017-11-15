package ocp;

public class CH1_Enum {

	public static void main(String[] args) {

		Season1 s1 = Season1.SPRING;
		s1.printExpectedVisitors();
		s1.expectedVisitors = "hello";
		s1.printExpectedVisitors();

		Season2 s2 = Season2.WINTER;
		s2.printHours();
		
		Season3 s3 = Season3.WINTER;
		s3.printHours();
		
		System.out.println(null instanceof Object);

	}

}

enum Season1 {
	WINTER("Low"), SPRING("Medium"), SUMMER("High"), FALL("Medium");
//	private String expectedVisitors; //also ok, but not able to access outside
	public String expectedVisitors;
	private Season1(String expectedVisitors) {
		this.expectedVisitors = expectedVisitors;
	}
	public void printExpectedVisitors() {
		System.out.println(expectedVisitors);
	} 
}

enum Season2 {
	WINTER {
		public void printHours() { System.out.println("9am-3pm"); }
	}, SPRING {
		public void printHours() { System.out.println("9am-5pm"); }
	}, SUMMER {
		public void printHours() { System.out.println("9am-7pm"); }
	}, FALL {
		public void printHours() { System.out.println("9am-5pm"); }
	};
	//abstract method, every value must implement it, or use default method instead
	public abstract void printHours();
}

enum Season3 {
	WINTER {
		public void printHours() { System.out.println("short hours"); }
	}, SUMMER {
		public void printHours() { System.out.println("long hours"); }
	}, SPRING, FALL;
	public void printHours() { System.out.println("default hours"); }
}