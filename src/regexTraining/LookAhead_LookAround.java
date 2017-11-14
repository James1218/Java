/*
 * 

(?=foo)		Lookahead	Asserts that what immediately follows the current position in the string is foo
(?<=foo)	Lookbehind	Asserts that what immediately precedes the current position in the string is foo
(?!foo)		Negative 	Lookahead	Asserts that what immediately follows the current position in the string is not foo
(?<!foo)	Negative 	Lookbehind	Asserts that what immediately precedes the current position in the string is not foo

 */


package regexTraining;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookAhead_LookAround {

	public static void main(String[] args) {
		String regex = "John (?!Smith)[A-Z]\\w+";
	    Pattern pattern = Pattern.compile(regex);

	    String candidate = "I think that John Smith ";
	    candidate += "is a fictional character. His real name ";
	    candidate += "might be John Jackson, John Westling, ";
	    candidate += "or John Holmes for all we know.";

	    Matcher matcher = pattern.matcher(candidate);

	    String tmp = null;

	    while (matcher.find()) {
	      tmp = matcher.group();
	      System.out.println("MATCH:" + tmp);
	    }
	}

}
