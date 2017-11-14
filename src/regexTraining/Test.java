package regexTraining;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws IOException {

		String input = 
					"/*Employer employer = Employer.findByPrimaryKey(appealPartyData.getEmployerData().getEmployerId());\n"
				+ "appealPartyData.setEmployerData(employer.getEmployerData());*/\n"
				+ "}";
		BufferedReader br = new BufferedReader(new StringReader(input));
		String line;
		boolean isEmptyOrOnlyHasComment = true;
		//comment out by /* xxx */
		boolean comment_out = false;
		while ((line = br.readLine()) != null){
			System.out.println("**********");
//			System.out.println(line);
			if (comment_out){
				if (line.contains("*/")){
					comment_out = false;
					System.out.println(comment_out);
				}
				continue;
			}
			//comment out by /* xxx */
			if (line.matches("\\s*?(/\\*).*?")){
				comment_out = true;
				System.out.println(comment_out);
				continue;
			}
			//line is commented out
			if (line.matches("\\s*?(//|/\\*|\\*).*?")){
				continue;
			}
			//line only has empty whitespace
			else if (line.matches("\\s*")){
				continue;
			}
			//line reach the end of the while block
			else if (line.matches("\\s*\\}.*?")){
				System.out.println(line);
				break;
			}
			//line has code
			else{
				isEmptyOrOnlyHasComment = false;
				System.out.println(line);
				break;
			}
		}
	}

}
