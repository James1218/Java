package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
1	[abc]	a, b, or c (simple class)
2	[^abc]	Any character except a, b, or c (negation)
3	[a-zA-Z]	a through z or A through Z, inclusive (range)
4	[a-d[m-p]]	a through d, or m through p: [a-dm-p] (union)
5	[a-z&&[def]]	d, e, or f (intersection)
6	[a-z&&[^bc]]	a through z, except for b and c: [ad-z] (subtraction)
7	[a-z&&[^m-p]]	a through z, and not m through p: [a-lq-z](subtraction)

8 	^xxx	Matches xxx regex at the beginning of the line
9 	xxx$	Matches regex xxx at the end of the line	
10 	xx|yy	Matches regex xx or yy	

 */
public class CharacterClass {

	public static void main(String[] args) {
		
		System.out.println(Pattern.matches("[amn]", "abcd"));//false (not a or m or n)  
		System.out.println(Pattern.matches("[amn]", "a"));//true (among a or m or n)  
		System.out.println(Pattern.matches("[amn]", "ammmna"));//false (m and a comes more than once)
		
		String regex = "^abc";//match the beginning of the line
		String target = "abcabcabc";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
		}
		
		regex = "abc$";//match the end of the line
		target = "abcabcabc";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while(matcher.find()){
			System.out.println(matcher.start());
		}
		
		System.out.println(Pattern.matches("xxx|y", "xxy"));

	}

}
