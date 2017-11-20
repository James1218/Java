package ocp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class CH5_Locale {

	public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {

		Locale locale1 = Locale.getDefault();
		System.out.println(locale1);
		System.out.println(Locale.GERMAN);
		System.out.println(Locale.GERMANY);
		System.out.println(new Locale("fr"));            // fr
		System.out.println(new Locale("hi", "IN"));      // hi_IN
		System.out.println();
		
		Locale locale2 = new Locale.Builder()
						.setRegion("US")
						.setLanguage("en")
						.build();
		System.out.println(locale2);
		System.out.println();
		
		Locale.setDefault(locale2);
		ResourceBundle rb = ResourceBundle.getBundle("resource/Zoo", locale1);
		System.out.println(rb.getString("hello"));
		//variable
		String s = rb.getString("variable");
		System.out.println(s);
		s = MessageFormat.format(s, "Feng", 123);
		System.out.println(s);
		Set<String> keys = rb.keySet();
		keys.stream().map(k -> k + " " + rb.getString(k))
		             .forEach(System.out::println);
		System.out.println();
		
		Properties properties = new Properties();
		properties.load(new BufferedReader(new FileReader("se8/resource/Zoo_en.properties")));
		System.out.println(properties.getProperty("hello","123"));
		System.out.println(properties.getProperty("not exist","123"));
		System.out.println();
		
		
		BigDecimal money = new BigDecimal(3_333_333);
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		System.out.println(numberFormat.format(money));
		numberFormat = NumberFormat.getInstance(Locale.GERMANY);
		System.out.println(numberFormat.format(money));
		numberFormat = NumberFormat.getInstance(Locale.CANADA_FRENCH);
		System.out.println(numberFormat.format(money));
		System.out.println();
		
		numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
		System.out.println(numberFormat.format(money));
		numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		System.out.println(numberFormat.format(money));
		numberFormat = NumberFormat.getCurrencyInstance(Locale.CANADA_FRENCH);
		System.out.println(numberFormat.format(money));
		System.out.println();
		
		//parse: convert money to number
		numberFormat = NumberFormat.getCurrencyInstance();
		Number number = numberFormat.parse("$3,333,333");
		System.out.println(number);
		System.out.println();
		
		numberFormat = NumberFormat.getInstance();
		String one = "456abc";
		String two = "-2.5165x10";
		String three = "x85.3";
		number = numberFormat.parse(one);
		System.out.println(numberFormat.parse(one));  // 456
		System.out.println(numberFormat.parse(two));  // -2.5165
//		System.out.println(nf.parse(three));// throws ParseException
		System.out.println();
		
		
		

		
		
	}

}
