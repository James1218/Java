package ocp;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

public class CH5_Locale {

	public static void main(String[] args) throws ParseException {

		Locale locale1 = Locale.getDefault();
		System.out.println(locale1);

		System.out.println(Locale.GERMAN);
		System.out.println(Locale.GERMANY);
		
		Locale locale2 = new Locale.Builder()
						.setRegion("US")
						.setLanguage("en")
						.build();
		System.out.println(locale2);
		
		Locale.setDefault(locale2);
		
		ResourceBundle rb = ResourceBundle.getBundle("Zoo", locale1);
		System.out.println(rb.getString("hello"));
		
		NumberFormat nf = NumberFormat.getInstance();
		String one = "456abc";
		String two = "-2.5165x10";
		String three = "x85.3";
		Number number = nf.parse(one);
		System.out.println(nf.parse(one));  // 456
		System.out.println(nf.parse(two));  // -2.5165
//		System.out.println(nf.parse(three));// throws ParseException
		
		String m1 = Duration.of(1, ChronoUnit.MINUTES).toString();
		String m2 = Duration.ofMinutes(1).toString();
		String s = Duration.of(60, ChronoUnit.SECONDS).toString();
		 
		String d = Duration.ofDays(1).toString();
		String p = Period.ofDays(1).toString();
		
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(s);
		System.out.println(d);
		System.out.println(p);
		
		
	}

}
