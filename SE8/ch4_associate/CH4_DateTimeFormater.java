package ch4_associate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CH4_DateTimeFormater {

	public static void main(String[] args) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);


		LocalDate localDate = LocalDate.of(2014, 04, 04);

		String s = dateTimeFormatter.format(localDate);
		System.out.println(s);

		s = localDate.format(dateTimeFormatter);
		System.out.println(s);

		s = localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
		System.out.println(s);

		dateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE;
		s = localDate.format(dateTimeFormatter);
		System.out.println(s);

		dateTimeFormatter = DateTimeFormatter.ofPattern("YY-MM dd ");
		s = localDate.format(dateTimeFormatter);
		System.out.println(s);


		dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
		LocalTime localTime = LocalTime.of(5, 5);
		s = dateTimeFormatter.format(localTime);
		System.out.println(s);
		
		DateTimeFormatter d1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse("2057-01-29", d1 );
		System.out.println(date);

	}

}
