package regexTraining;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarmUp {

	public static void main(String[] args) {

		//1st way: match entire string
		Pattern p = Pattern.compile(".s");//. represents single character
		Matcher m = p.matcher("as");
		boolean b = m.matches();

		//2nd way: match entire string
		boolean b2=Pattern.compile(".s").matcher("as").matches();

		//3rd way: match entire string
		boolean b3 = Pattern.matches(".s", "as");
		
		//4th way: match partial String
		boolean b4 = Pattern.compile(".s").matcher("asss").find();
		
		System.out.println(b+" "+b2+" "+b3 + " " + b4);
		
		//match digit
		String regex = "\\d\\d";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher("123456");
		while (matcher.find()){
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			String group = matcher.group();
			System.out.println("startIndex: "+startIndex);
			System.out.println("endIndex: " + endIndex);
			System.out.println("group: " + group);
			
		}

	}

}
