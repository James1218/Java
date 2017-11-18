package oca;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CH4_LocalTime {

	public static void main(String[] args) {

		LocalTime localTime = LocalTime.of(13, 13, 13, 13);
		System.out.println(localTime);
		
		localTime = LocalTime.now();
		System.out.println(localTime);
		
		localTime = LocalTime.parse("12:12:12");
		System.out.println(localTime);
		
		System.out.println(localTime.getHour());
		System.out.println(localTime.getNano());
		
		LocalDateTime localDateTime = localTime.atDate(LocalDate.of(2017, 3, 3));
		System.out.println(localDateTime);
	}

}
