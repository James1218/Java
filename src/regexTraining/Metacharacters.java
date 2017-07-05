package regexTraining;
import java.util.regex.Pattern;

/*
The metacharacters supported by this API are: <([{\^-=$!|]})?*+.>

.	Any character (. matches anything but a line terminator)
\d	Any digits, short of [0-9]
\D	Any non-digit, short for [^0-9]
\s	Any whitespace character, short for [\t\n\x0B\f\r]
\S	Any non-whitespace character, short for [^\s]
\w	Any word character, short for [a-zA-Z_0-9]
\W	Any non-word character, short for [^\w]
\b	A word boundary
\B	A non word boundary
\Q	which starts the quote
\E	which ends it
\\	metacharacters

There are two ways to force a metacharacter to be treated as an ordinary character:

precede the metacharacter with a backslash, or
enclose it within \Q (which starts the quote) and \E (which ends it).

 */
public class Metacharacters {

	public static void main(String[] args) {
		
		System.out.println("metacharacters d....");//\\d means digit  
		System.out.println(Pattern.matches("\\d", "abc"));//false (non-digit)  
		System.out.println(Pattern.matches("\\d", "1"));//true (digit and comes once)  
		  
		System.out.println("metacharacters D....");//\\D means non-digit  
		  
		System.out.println(Pattern.matches("\\D", "abc"));//false (non-digit but comes more than once)   
		System.out.println(Pattern.matches("\\D", "m"));//true (non-digit and comes once)  
		  
		System.out.println("metacharacters D with quantifier....");  
		System.out.println(Pattern.matches("\\D*", "mak"));//true (non-digit and may come 0 or more times) 
		
		System.out.println("metacharacters enclose....");  
		//same as Pattern.quote(regex)
		System.out.println(Pattern.matches("\\Q"+"5*"+"\\E", "5*"));//true, match metacharacter itself
		System.out.println(Pattern.matches("5\\*", "5*"));//true, match metacharacter itself
		System.out.println(Pattern.matches("5*", "5*"));//false



	}

}
