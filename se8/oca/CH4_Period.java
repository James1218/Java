package oca;

import java.time.LocalDate;
import java.time.Period;

public class CH4_Period {

	public static void main(String[] args) {

		Period period = Period.ofDays(-155);
		System.out.println(period);
		
		period = Period.of(-12, -12, 12);
		System.out.println(period);
		
		period = Period.parse("-P-5y");//positive!!!
		System.out.println(period);
		
		period = Period.between(LocalDate.of(2017, 07, 11), LocalDate.of(2017, 01, 01));
		System.out.println(period);
		
		period = period.multipliedBy(2);
		System.out.println(period);
		
		int days = period.getDays();
		System.out.println(days);
		
		long months = period.toTotalMonths();
		System.out.println(months);
		
		
	}

}
