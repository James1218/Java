package regexTraining;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {

		String regex = "impactPoint=\"Start\")\n(((?m)^[\\t ]+\\w.*?=.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?)|((?m)^[\\t ]+\\w.*?=\\s*?\n[\\t ]+.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?))";

		String oneLineRegex = "((?m)^[\\t ]+\\w.*?=.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?)";

		//"DataBridge()" or "(DataBridge)" is at the second line of the match
		String twoLineRegex = "((?m)^[\\t ]+\\w.*?=\\s*?\n[\\t ]+.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?)";

		String filePath = "CinService.java";
		addAnnotation(filePath);




	}

	private static void addAnnotation(String filePath){

		//Already surrounded with comment
		String comment = "((?m)^[\\t ]+//.*?\"Start\"\\)[\\t ]*?\n)?";
		//"DataBridge()" or "(DataBridge)" is at the first line of the match
		String oneLineRegex = "((?m)^[\\t ]+\\w.*?=.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?)";

		//"DataBridge()" or "(DataBridge)" is at the second line of the match
		String twoLineRegex = "((?m)^[\\t ]+\\w.*?=\\s*?\n[\\t ]+.*?DataBridge\\s*?[\\(\\)][^;]*?;[\\t ]*?)";

		String regex = comment + "(" + oneLineRegex + "|" + twoLineRegex + ")";

		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){

			String file = br.lines().collect(Collectors.joining("\n")); //convert java file to one string
			StringBuffer sb = new StringBuffer();


			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(file);
			System.out.println("Regex");
			int count = 0;
			while(m.find()){
				System.out.println("find match "+ ++count);
//				System.out.println(m.group());
				if (m.group(1) == null){
//					System.out.println("Add comment only if no comment surrounded");
//					System.out.println(m.group(2));
				}else{
					System.out.println(m.group());
				}
				
				System.out.println();
			}
			m.appendTail(sb);
			System.out.println("Total Match is : " + count);


			//			p = Pattern.compile(oneLineRegex); 
			//			m = p.matcher(file);
			//			System.out.println("oneLineRegex");
			//			count = 0;
			//			while(m.find()){
			//				System.out.println("find match 2_"+ ++count);
			//				System.out.println(m.group());
			//				System.out.println();
			//			}
			//			
			//			count = 0;
			//			System.out.println("twoLineRegex");
			//			p = Pattern.compile(twoLineRegex);
			//			m = p.matcher(file);
			//			while(m.find()){
			//				System.out.println("find match 3_"+ ++count);
			//				System.out.println(m.group());
			//				System.out.println();
			//			}


			//			try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);){
			//				fileOutputStream.write(sb.toString().getBytes());
			//				fileOutputStream.close();
			//				System.out.println("file generated Successfully");
			//			}

			System.out.println("addAnotation End");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
