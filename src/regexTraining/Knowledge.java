package regexTraining;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Knowledge {

	public static void main(String[] args) {
		
		/*
		 * White space but not new line
		 * [^\\S\\n]
		 */
		String input = "\n\nif\n\n";
		String regex = "(?m)^[^\\S\\n]*if\\s*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}

}
