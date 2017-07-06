package ch4_associate;

import java.time.LocalDate;

public class CH4_LocalDate {

	public static void main(String[] args) {
		
		//of
		LocalDate localDate = LocalDate.of(2017, 7, 5);
		System.out.println(localDate);
		
		//now
		System.out.println(LocalDate.now());
		
		//parse
		localDate = LocalDate.parse("2017-07-05");
		System.out.println(localDate);
		
		//getXXX
		System.out.println(localDate.getDayOfMonth());
		System.out.println(localDate.getDayOfWeek());
		System.out.println(localDate.getDayOfYear());
		System.out.println(localDate.getMonth());
		System.out.println(localDate.getMonthValue());
		System.out.println(localDate.getYear());
		
		//isBefore, isAfter
		LocalDate localDate2 = LocalDate.of(2016, 7, 5);
		System.out.println(localDate.isAfter(localDate2));
		System.out.println(localDate.isBefore(localDate2));
		
		//plusXXX, minusXXX
		LocalDate localDate3 = LocalDate.of(2017, 12, 31);
		System.out.println(localDate3.plusDays(1));
		System.out.println(localDate3.plusMonths(2));
		System.out.println(localDate3.minusWeeks(4));
		
		//withXXX
		System.out.println(localDate.withDayOfMonth(30));
	}

}
