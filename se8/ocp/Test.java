package ocp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Test {

	public static void main(String[] args) {

		LocalDate date = LocalDate.parse("2017-11-15");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
		String dateStr = formatter.format(date);
		System.out.println(dateStr);
		
	}

}
