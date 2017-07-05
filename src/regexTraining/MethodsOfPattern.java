package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodsOfPattern {

	public static void main(String[] args) {

		/* appendReplacement and appendTail: append previous match to current match
		 * Output: gog gay gaughter gaut gig go gone gate  */
		String joke = "dog day daughter daut did do done date";
		String regEx = "d";

		Pattern doggone = Pattern.compile(regEx);
		Matcher m = doggone.matcher(joke);

		StringBuffer newJoke = new StringBuffer();
		while (m.find())
			m.appendReplacement(newJoke, "g");
		m.appendTail(newJoke);
		System.out.println(newJoke.toString());



		// using Matcher.replaceFirst() and replaceAll() methods
		System.out.println("replace mathed");
		Pattern pattern = Pattern.compile("1*2");
		Matcher matcher = pattern.matcher("11234512678");
		System.out.println("Using replaceAll: " + matcher.replaceAll("_"));
		System.out.println("Using replaceFirst: " + matcher.replaceFirst("_"));
		System.out.println();

		//split method
		System.out.println("split method");
		pattern = Pattern.compile("\\d");
		String [] items = pattern.split("one3two5three2four0five");
		for (String s : items){
			System.out.println(s);
		}
		System.out.println();

		//quote method
		System.out.println("quote method");
		System.out.println(Pattern.matches(Pattern.quote("\\n"), "\\n"));//regex matches "\n" instead of newLine, return true
		System.out.println(Pattern.matches("\\n", "\\n"));//regex matches new line, return false
		System.out.println();

		//lookingat method: only check the find in the beginning
		System.out.println("lookingat method");
		String str1 = "J2SE is the only one for me";
		String str2 = "For me, it's J2SE, or nothing at all";
		String str3 = "J2SEistheonlyoneforme";

		matcher = pattern.matcher(str1);
		String msg = str1 + ": matches?: ";
		System.out.println(msg + matcher.lookingAt());

		matcher.reset(str2);
		msg = str2 + ": matches?: ";
		System.out.println(msg + matcher.lookingAt());

		matcher.reset(str3);
		msg = str3 + ": matches?: ";
		System.out.println(msg + matcher.lookingAt());

	}

}
