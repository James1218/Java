/*
 * Delete Junk like "_$$_jvst1fb_1c8" and "___-_-__jvst243__398" by using Regex
 * @author : 1381317
 */
package task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteJunk_TestUtils {

	public static void main(String[] args){

		String filePath = "gov.state.uim.wc.service.WeeklyCertService.saveWeeklyCertDetailsFromWcAck2017-07-03_0.xml";
		deleteJunk(filePath);
	}

	/*
	 * Delete Junk like "_$$_jvst1fb_1c8" and "___-_-__jvst243__398" by using Regex
	 * @author : 1381317
	 */
	public static void deleteJunk(String filePath) {

		//regex represents "_$$_xxx" and "___-_-__xxx"
		String[] replacements = {"", ""};
		String[] regexs = {"(_\\$\\$_.*?)(\")", "(___\\-_\\-__.*?)([\\s\\>\\]])"};
		int[] groups = {1, 1};

		//declare closable object in order to close it in the finally block to avoid memory leak
		BufferedReader bufferedReader =null;
		FileOutputStream fileOutputStream  = null;
		FileReader fileReader= null;

		try {
			fileReader = new FileReader(filePath);
			bufferedReader= new BufferedReader(fileReader);
			StringBuffer stringBuffer  = new StringBuffer();//used to save the modified output content
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				line = replaceLine(line, regexs, replacements, groups);
				stringBuffer .append(line);
			}

			bufferedReader.close();
			fileOutputStream = new FileOutputStream(filePath);// write the new String with the replaced line OVER the same file
			fileOutputStream.write(stringBuffer .toString().getBytes());
			fileOutputStream.close();
			stringBuffer .setLength(0);//clean StringBuffer

			System.out.println("Junk Deleted");

		} catch (Exception e) {
			System.out.println("Problem reading file or wrong regex.");
		}
//		finally {
//			try {
//				bufferedReader.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
//			try {
//				fileOutputStream.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
//			try {
//				fileReader.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
//		}
	}

	/*
	 * helper method for "deleteJunk" method
	 * @author : 1381317
	 */
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
