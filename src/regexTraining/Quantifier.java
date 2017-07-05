package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
X?	X occurs once or not at all
X+	X occurs once or more times
X*	X occurs zero or more times
X{n}	X occurs n times only
X{n,}	X occurs n or more times
X{y,z}	X occurs at least y times but less than z times

zero-length match
.*	Greedy
.*?	Reluctant
.*+	Possessive

 */


public class Quantifier {

	public static void main(String[] args) {
		System.out.println("? quantifier ....");  
		System.out.println(Pattern.matches("[amn]?", "a"));//true 
		  
		System.out.println("+ quantifier ....");  
		System.out.println(Pattern.matches("[amn]+", "a"));//true 
		  
		System.out.println("* quantifier ....");  
		System.out.println(Pattern.matches("[amn]*", "ammmna"));//true 
		
		System.out.println("X{n,} X{y,z} quantifier ....");  
		System.out.println(Pattern.matches("[amn]{3,}", "ammmna"));//true 
		System.out.println(Pattern.matches("[amn]{3,5}", "ammmna"));//false, appear 6 times 
		System.out.println();
		
		
		//zero-length match
		System.out.println("* zero-length match");
		String regex = "a*";
		String target = "aaa";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("group: " + group);
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);	
		}
		System.out.println();
		
		
		//zero-length match
		System.out.println("? zero-length match");
		regex = "a?";
		target = "abaab";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("group: " + group);
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);	
		}
		System.out.println();
		
		//https://docs.oracle.com/javase/tutorial/essential/regex/quant.html
		//Greedy	
		System.out.println("Greedy");
		regex = ".*foo";
		target = "xfooxxxxxxfoo";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("group: " + group);
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);	
		}
		System.out.println();
		
		//Reluctant	
		System.out.println("Reluctant");
		regex = ".*?foo";
		target = "xfooxxxxxxfoo";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("group: " + group);
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);	
		}
		System.out.println();
		
		//Possessive
		System.out.println("Possessive");
		regex = ".*+foo";
		target = "xfooxxxxxxfoo";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(target);
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("group: " + group);
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);	
		}
		System.out.println();
		
		
		
	}

}
