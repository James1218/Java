package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
^	The beginning of a line
$	The end of a line
\b	A word boundary
\B	A non-word boundary
\A	The beginning of the input
\G	The end of the previous match
\Z	The end of the input but for the final terminator, if any
\z	The end of the input
 */
public class BoundaryMatchers {

	public static void main(String[] args) {
		
		System.out.println("^ begining of a line");
		String regex = "^abc";//match the beginning of the line
		String target = "abcabcabc";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
		}
		
		
		System.out.println("$ end of a line");
		regex = "abc$";//match the end of the line
		target = "abcabcabc";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
		}
		
		System.out.println("word boundary");
		regex = "\\bdog\\b";
		target = "I have a dog";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
			System.out.println(matcher.end());
		}
		
		System.out.println("word && non-word boundary");
		regex = "\\bdog\\B";
		target = "I have a doggie";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
			System.out.println(matcher.end());
		}
		
		System.out.println("match to occur only at the end of the previous match, use \\G:");
		regex = "\\Gdog";
		target = "dog dog";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
			System.out.println(matcher.end());
		}
		target = "I have a dog dog";
		System.out.println(Pattern.matches(regex, target));
	}

}
