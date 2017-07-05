package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 
((a)(bc)) contains 3 capturing groups; ((a)(bc)), (a) and (bc)

 Backreference in regular expression with backslash (\) and then the number of group to be recalled.
 
 */
public class CapturingGroup {

	public static void main(String[] args) {
		
		/*
		 * In the first example, at runtime first capturing group is (\w\d) .
		 * which evaluates to “a2″ when matched with the input String “a2a2″ and saved in memory. 
		 * So \1 is referring to “a2″ and hence it returns true. 
		 * Due to same reason second statement prints false
		 */
		
		System.out.println(Pattern.matches("(\\w\\d)\\1", "a2a2")); //true
		System.out.println(Pattern.matches("(\\w\\d)\\1", "a2b2")); //false	
		System.out.println(Pattern.matches("(AB)(B\\d)\\2\\1", "ABB2B2AB")); //true	
		System.out.println(Pattern.matches("(AB)(B\\d)\\2\\1", "ABB2B3AB")); //false
		System.out.println();
		
		String regex = "((a)(b(c)))";
		String target = "abc";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		while (matcher.find()){
			int groupCount = matcher.groupCount();
			System.out.println("groupCount: " + groupCount);//groupCount doesn't include group 0	
			for (int i = 0; i <= groupCount; i++){
				int startIndex = matcher.start(i);
				int endIndex = matcher.end(i);
				String group = matcher.group(i);			
				System.out.println("group " + i + " : "+group);
				System.out.println("startIndex: "+startIndex);
				System.out.println("endIndex: " + endIndex);
				for (int j = 1; j <= matcher.groupCount(); j++){
					System.out.println(matcher.group(j));
				}
			}
				
		}
		System.out.println();
	}

}
