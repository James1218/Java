package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteJunk {

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
		try (BufferedReader file = new BufferedReader(new FileReader(filePath));// input the file content to the StringBuffer "input"
				FileOutputStream fileOut = new FileOutputStream("temp.xml");// write the new String with the replaced line OVER the same file
			){
			
			String line;

			while ((line = file.readLine()) != null) {
				line = replaceLine(line, regexs, replacements, groups);
				fileOut.write(line.getBytes());
			}

			file.close();
			fileOut.close();
			
			//logic: create a temperate file to store the modified content, then rename it to the original file name
			File oldFile = new File(filePath);
			oldFile.delete();
			File newFile = new File("temp.xml");
			newFile.renameTo(oldFile);
			System.out.println("END");
		} catch (Exception e) {
			System.out.println("Problem reading file or wrong regex.");
		}
	}

	public static String replaceLine(String line, String [] regexs, String [] replacements, int[] groups){

		//iterate through all regex
		for (int i = 0; i < regexs.length; i++){
			
			boolean found = false; //if found the match, do it again
			String regex = regexs[i];
			String replacement = replacements[i];
			int group = groups[i];
			Pattern pattern = Pattern.compile(regex);
			do{		
				Matcher matcher = pattern.matcher(line); //make a new matcher because the length of input string changes after modify
				if (matcher.find()){
					line = new StringBuffer(line).replace(matcher.start(group), matcher.end(group), replacement).toString();
				}
			} while(found);
		}

		return line + System.lineSeparator();//line + "/n" doesn't fit Linux system since the Linux line terminator is "\r\n"
	}

}
