package ocp;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

public class CH5_Date {

	public static void main(String[] args) throws InterruptedException {
		LocalDate localDate = LocalDate.now();
		localDate = LocalDate.of(2017, Month.JANUARY, 1);
		System.out.println("localDate "+localDate);
		LocalTime localTime = LocalTime.now();
		localTime = LocalTime.of(1, 2, 3, 4);
		System.out.println("localTime" +localTime);
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = LocalDateTime.of(localDate, localTime);
		System.out.println("localDateTime " + localDateTime);
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		ZoneId zoneId = ZoneId.of("US/Eastern");
		zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
		System.out.println("zonedDateTime "+zonedDateTime);
		System.out.println();

		//list all avaiable zoneId
		ZoneId.getAvailableZoneIds().stream().filter(s -> s.contains("US")).forEach(System.out::println);
		System.out.println();

		long epochDay = localDate.toEpochDay();
		System.out.println(epochDay);
		System.out.println();

		Period period = Period.of(1, 1, 1);
		System.out.println(period);
		System.out.println(Period.of(0, 20, 47));
		System.out.println(Period.ofWeeks(3));
		System.out.println(localDate.plus(period));
		System.out.println();

		Duration daily = Duration.ofDays(1);                // PT24H
		Duration hourly = Duration.ofHours(1);              // PT1H
		Duration everyMinute = Duration.ofMinutes(1);       // PT1M
		Duration everyTenSeconds = Duration.ofSeconds(10);  // PT10S
		Duration everyMilli = Duration.ofMillis(1);         // PT0.001S
		Duration everyNano = Duration.ofNanos(1);           // PT0.000000001S

		daily = Duration.of(1, ChronoUnit.DAYS);
		hourly = Duration.of(1, ChronoUnit.HOURS);
		everyMinute = Duration.of(1, ChronoUnit.MINUTES);
		everyTenSeconds = Duration.of(10, ChronoUnit.SECONDS);
		everyMilli = Duration.of(1, ChronoUnit.MILLIS);
		everyNano = Duration.of(1, ChronoUnit.NANOS);
		
		LocalTime one = LocalTime.of(5, 15);
		LocalTime two = LocalTime.of(6, 30);
		LocalDate date = LocalDate.of(2016, 1, 20);
		
		System.out.println(Duration.between(one, two)); //PT1H15M
		System.out.println(ChronoUnit.HOURS.between(one, two));   // 1
		System.out.println(ChronoUnit.MINUTES.between(one, two)); // 75
		/*System.out.println(ChronoUnit.MINUTES.between(one, date)); // DateTimeException*/
		System.out.println();

		Instant now = Instant.now();
		Thread.sleep(1000);
		Instant later = Instant.now();
		Duration duration = Duration.between(now, later);
		System.out.println(duration.toMillis());
		System.out.println();
		
		//The Instant class represents a specific moment in time in the GMT time zone
		Instant instant = zonedDateTime.toInstant(); // 2015–05–25T15:55:00Z
		System.out.println(zonedDateTime); // 2015–05–25T11:55–04:00[US/Eastern]
		System.out.println(instant); // 2015–05–25T15:55:00Z
		System.out.println();

		long epochSecond = zonedDateTime.toEpochSecond();
		System.out.println(zonedDateTime);
		System.out.println(epochSecond);
		instant = Instant.ofEpochSecond(epochSecond);
		System.out.println(instant);
		System.out.println();
		
		Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
		System.out.println(nextDay); // 2015–05–26T15:55:00Z
		Instant nextHour = instant.plus(1, ChronoUnit.HOURS);
		System.out.println(nextHour); // 2015–05–25T16:55:00Z
		System.out.println();
		
		//Java is smart enough to know that there is no 2:30 a.m. that night and switches over to the appropriate GMT offset.
		LocalDate date1 = LocalDate.of(2016, Month.MARCH, 13);
		LocalTime time = LocalTime.of(2, 30);
		ZoneId zone = ZoneId.of("US/Eastern");
		ZonedDateTime dateTime = ZonedDateTime.of(date1, time, zone);
		System.out.println(dateTime);  // 2016–03–13T03:30–04:00[US/Eastern]
		System.out.println();
		
		System.out.println(date.getDayOfWeek());     // WEDNESDAY
		System.out.println(date.getMonth());          // JANUARY
		System.out.println(date.getYear());          // 2016
		System.out.println(date.getDayOfYear());     // 20
		System.out.println();
		
		System.out.println(date);
		System.out.println(time);
		System.out.println(dateTime);
		//ISO is a standard for dates
		System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
		System.out.println(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
		System.out.println(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		System.out.println();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		System.out.println(dateTimeFormatter.format(localDate));
		System.out.println(dateTimeFormatter.format(localDateTime));
		System.out.println(zonedDateTime.format(dateTimeFormatter));
		//DateTimeFormatter.ofLocalizedDate, cannot be used for localtime
//		System.out.println(dateTimeFormatter.format(localTime));//UnsupportedTemporalTypeException
		System.out.println();
		
		dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		//ofLocalizedDateTime cannot be used for localdate
//		System.out.println(dateTimeFormatter.format(localDate));
		System.out.println(dateTimeFormatter.format(localDateTime));
		System.out.println(zonedDateTime.format(dateTimeFormatter));
		//ofLocalizedDateTime cannot be used for localtime
//		System.out.println(dateTimeFormatter.format(localTime));//UnsupportedTemporalTypeException
		System.out.println();
		
		dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
		//ofLocalizedTime cannot be used for localdate, localdatetime
//		System.out.println(dateTimeFormatter.format(localDate));
//		System.out.println(dateTimeFormatter.format(localDateTime));
//		System.out.println(zonedDateTime.format(dateTimeFormatter));
		System.out.println(dateTimeFormatter.format(localTime));//UnsupportedTemporalTypeException
		System.out.println();
		
		dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
		System.out.println(dateTimeFormatter.format(localDate));
		System.out.println(dateTimeFormatter.format(localDateTime));
		System.out.println(zonedDateTime.format(dateTimeFormatter));
//		System.out.println(dateTimeFormatter.format(localTime));//UnsupportedTemporalTypeException
		System.out.println();
		
		dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-YY-MMMM-MMM-MM-DD  hh mm ss nn");
		System.out.println(localDateTime.format(dateTimeFormatter));
		System.out.println();
		
	}

}
