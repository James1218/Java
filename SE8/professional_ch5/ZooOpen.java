package professional_ch5;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author feng
 *
 */
public class ZooOpen {

	public static void main(String[] args) {


		Locale us = new Locale("en", "US");
		Locale france = new Locale("fr", "FR");
		//Locale builder pattern
		Locale englishCanada = new Locale.Builder().setLanguage("en").setRegion("CA").build();
		//Resource Bundle BaseName
		String resourceBundleBaseName = "professional_ch5.Zoo";
		
		//get value from key
		printProperties(france, resourceBundleBaseName);
		System.out.println();
		printProperties(us, resourceBundleBaseName);
		
		//get all keys
		printAllPairs(us, resourceBundleBaseName);
		
		

	}
	
	
	
	/**
	 * print all keys in Resource Bundle
	 */
	private static void printAllPairs(Locale locale, String resourceBundleBaseName) {
		System.out.println();
		System.out.println("-------------printAllPairs-----------");
		ResourceBundle rb = ResourceBundle.getBundle(resourceBundleBaseName, locale);
		Set<String> keySet = rb.keySet();
		Stream<String> keyStream = keySet.stream();
		Stream<String> pairStream = keyStream.map(x->x + " : " + rb.getString(x));
		pairStream.forEach(System.out::println);
	}



	/**
	 * get value from key in Resource Bundle
	 */
	private static void printProperties(Locale locale, String resourceBundleBaseName) {
		ResourceBundle rb = ResourceBundle.getBundle(resourceBundleBaseName, locale);
		String hello = rb.getString("hello");
		String open = rb.getString("open");
		System.out.println(hello);
		System.out.println(open);
	}

}
