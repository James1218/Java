package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
(?i), equivalent with Pattern.CASE_INSENSITIVE
(?x), equivalent with Pattern.COMMENTS
(?m), equivalent with Pattern.MULTILINE : 
		tells Java to accept the anchors ^ and $ to match at the start and end of each line 
		(otherwise they only match at the start/end of the entire string).
(?s), equivalent with Pattern.DOTTAL
		tells Java to allow the dot to match newline characters, too.
(?u), equivalent with Pattern.UNICODE_CASE
(?d), equivalent with Pattern.UNIX_LINES
 */
public class Flag {

	public static void main(String[] args) {
		
		String regex;
        String text = "the quick brown fox jumps over a lazy dog";
        
        //(?i)
        System.out.println(Pattern.compile("(?i)the").matcher(text).find());
        //CASE_INSENSITIVE
        System.out.println(Pattern.compile("the", Pattern.CASE_INSENSITIVE).matcher(text).find());
        
        Pattern pattern = Pattern.compile("(?i)the");        
        Matcher matcher = pattern.matcher(text);

        //(?i)
        while (matcher.find()) {
            System.out.format("Group \"%s\" found at %d to %d.%n",
                    matcher.group(), matcher.start(), 
                    matcher.end());
        }
        System.out.println();
        
        //(?x) : increase the readability of the regex by ignore the whitespace
        //and ignore embedded comments starting with # are ignored until the end of a line
        System.out.println("(?x)");
        regex = "\\{ user_id \\s : \\s \\d+ \\}";
        text = "{user_id : 0}";
        pattern = Pattern.compile(regex, Pattern.COMMENTS);
        matcher = pattern.matcher(text);
        System.out.println(matcher.find());
        
        

	}

}
