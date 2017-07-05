package regexTraining;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {

		String regex = "ab+c?";
		String input = "abbb";
		
		System.out.println(Pattern.matches(regex, input));

	}

}
