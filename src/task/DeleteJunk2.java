package task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteJunk2 {

	public static void main(String[] args){

		String filePath = "gov.state.uim.wc.service.WeeklyCertService.saveWeeklyCertDetailsFromWcAck2017-07-03_0.xml";
		String[] replacements = {"", ""};
		String[] regexs = {"(_\\$\\$_.*?)(\")", "(___\\-_\\-__.*?)([\\s\\>\\]])"};
		int[] groups = {1, 1};
		

		replaceSelected(filePath, regexs, replacements, groups);


	}

	public static void replaceSelected(String filePath, String[] regexs, String[] replacements, int[] groups) {

		if (regexs.length != replacements.length){
			System.err.println("Arguments invalid");
			return;
		}
		try (BufferedReader file = new BufferedReader(new FileReader(filePath));)// input the file content to the StringBuffer "input"
		{
			StringBuffer sb = new StringBuffer();//used to save the modified output content
			String line;

			while ((line = file.readLine()) != null) {
				line = replaceLine(line, regexs, replacements, groups);
				sb.append(line);
				//fileOut.write(line.getBytes());
			}

			file.close();
			FileOutputStream fileOut = new FileOutputStream(filePath);// write the new String with the replaced line OVER the same file
			fileOut.write(sb.toString().getBytes());
			fileOut.close();
			
			System.out.println("Junk Deleted");
			
		} catch (Exception e) {
			System.out.println("Problem reading file or wrong regex.");
		}
	}

	public static String replaceLine(String line, String [] regexs, String [] replacements, int[] groups){

		//iterate through all regex
		for (int i = 0; i < regexs.length; i++){
			
			boolean found = false;//if found the match, do it again; otherwise, just check the match once
			String regex = regexs[i];
			String replacement = replacements[i];
			int group = groups[i];
			Pattern pattern = Pattern.compile(regex);
			StringBuffer stringBuffer = new StringBuffer();
			do{		
				Matcher matcher = pattern.matcher(line); //make a new matcher because the length of input string changes after modify
				if (matcher.find()){
					stringBuffer.setLength(0);//resue same stringbuffer to increase 25% performance
					line = stringBuffer.append(line).replace(matcher.start(group), matcher.end(group), replacement).toString();
				}
			} while(found);
		}

		return line + System.lineSeparator();//line + "/n" doesn't fit Linux system since the Linux line terminator is "\r\n"
	}


}
