/*
 * Author : 1381317
 * Add "styleId=next" for next button, "styleId=back" for buck button, "styleId=submit" for submit button
 * 

 Pattern 1: add styleId="submit" to submit button (make sure if styleId is already there, don't double add)
 <html:submit property="method" styleClass="formbutton"
					onmouseover="this.className='formbutton formbuttonhover'"
					onmouseout="this.className='formbutton'">
					<bean:message key="access.submit" />
				</html:submit>


Pattern 2: add styleId="back" to back button (make sure if styleId is already there, don't double add)
				<td class="buttontdright">	<access:nonvalidatesubmit
	property="method" styleClass="formbutton" styleId="back"
	onmouseover="this.className='formbutton formbuttonhover'"
	onmouseout="this.className='formbutton'">
	<bean:message key="access.back" />
</access:nonvalidatesubmit>.


Pattern 3: add styleId="next" to next button (make sure if styleId is already there, don't double add)
<html:submit property="method" styleClass="formbutton" styleId="next"
onmouseover="this.className='formbutton formbuttonhover'"
onmouseout="this.className='formbutton'">
<bean:message key="access.continue" />
</html:submit></td>

 */

package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NextBackSubmitButton {

	private static int backKeyCount = 0;
	private static int backKeyClass = 0;
	private static int continueKeyCount = 0;
	private static int continueKeyClass = 0;
	private static int submitKeyCount = 0;
	private static int submitKeyClass = 0;
	private static String className = "";

	public static void main(String[] args) {



//		String [] directoryList = {"C:\\NewWorkSpace2\\JavaTraining\\TestFile"};
		
		String [] directoryList = {"C:\\NewWorkSpace_MO\\Benefits"};
		
//		String [] directoryList = {"C:\\NewWorkSpace\\Benefits", "C:\\NewWorkSpace\\BenefitsWY", 
//				"C:\\NewWorkSpace\\Tax"};

		for (String directory : directoryList){
			listFilesAndFilesSubDirectories(directory);
		}
		//output the report to console
		System.out.println("\n\n################### Total Count ###############");
		System.out.println("Back Key Added : " + backKeyCount);
		System.out.println("Back Key Class Modified: " + backKeyClass);
		System.out.println("Continue Key Added : " + continueKeyCount);
		System.out.println("Continue Key Class Modified: " + continueKeyClass);
		System.out.println("Submit Key Added : " + submitKeyCount);
		System.out.println("Submit Key Class Modified: " + submitKeyClass);
	}
	
	private static void addSubmitKey(String filePath){

		if (!filePath.endsWith("jsp"))	return;//only change jsp file

		try(FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr)){

			String file = br.lines().collect(Collectors.joining("\n")); //convert java file to one string
			StringBuffer sb = new StringBuffer();

			String regex_block = "(?s)\\<html:submit.*?\\</html:submit\\>";//regex for the all submit block
			String regex_valid = "(?s)^(?!.*?styleId)^(?=.*?access\\.submit).*?styleClass";//regex for valid submit black
			Pattern p_block = Pattern.compile(regex_block);
			Matcher m_block = p_block.matcher(file);

			boolean found = false;
			while(m_block.find()){
				m_block.appendReplacement(sb, "");//add content between previous match and current match
				Pattern p_styleId = Pattern.compile(regex_valid);
				Matcher m_styleId = p_styleId.matcher(m_block.group()); 
				if (m_styleId.find()){ 
					found = true;
					submitKeyCount++;
					//don't use matcher appead method to avoid back reference in regex
					//for example, "${xxx} in the block will throw an exception since no back reference is used
					sb.append(m_block.group().replaceAll("styleClass", " styleId=\"submit\" styleClass"));
				} 
				else{ 
					sb.append(m_block.group());
				}
			}
			m_block.appendTail(sb);//able to use matcher appean method because all the back reference ${xxx} are in the matching block

			//when there are changes, overwrite the original file
			if (found){
				try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
					sb.setLength(0);
					if (className != filePath){
						className = filePath;
						System.out.println(className);
					}
					System.out.println("Submit Key file generated Successfully : " + ++submitKeyClass );
					System.out.println("Submit key added : " + submitKeyCount);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addContinueKey(String filePath){

		if (!filePath.endsWith("jsp"))	return;

		try(FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr)){

			String file = br.lines().collect(Collectors.joining("\n")); //convert java file to one string
			StringBuffer sb = new StringBuffer();

			String regex_block = "(?s)\\<html:submit.*?\\</html:submit\\>";
			String regex_valid = "(?s)^(?!.*?styleId)^(?=.*?access\\.continue).*?styleClass";
			Pattern p_block = Pattern.compile(regex_block);
			Matcher m_block = p_block.matcher(file);

			boolean found = false;
			while(m_block.find()){

				m_block.appendReplacement(sb, "");//add content between previous match and current match
				Pattern p_styleId = Pattern.compile(regex_valid);
				Matcher m_styleId = p_styleId.matcher(m_block.group()); 
				if (m_styleId.find()){ 
					found = true;
					continueKeyCount++;
					sb.append(m_block.group().replaceAll("styleClass", " styleId=\"next\" styleClass"));
				} 
				else{ 
					sb.append(m_block.group());
				}
			}
			m_block.appendTail(sb);

			if (found){
				try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
					sb.setLength(0);
					if (className != filePath){
						className = filePath;
						System.out.println(className);
					}
					System.out.println("Continue Key file generated Successfully : " + ++continueKeyClass );
					System.out.println("Continue key added : " + continueKeyCount);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addBackKey(String filePath){

		if (!filePath.endsWith("jsp"))	return;

		try(FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr)){

			String file = br.lines().collect(Collectors.joining("\n")); //convert java file to one string
			StringBuffer sb = new StringBuffer();

			String regex_block = "(?s)\\<access:nonvalidatesubmit.*?\\</access:nonvalidatesubmit\\>";
			String regex_valid = "(?s)^(?!.*?styleId)^(?=.*?access\\.back).*?styleClass";
			Pattern p_block = Pattern.compile(regex_block);
			Matcher m_block = p_block.matcher(file);

			boolean found = false;
			while(m_block.find()){
				m_block.appendReplacement(sb, "");//add content between previous match and current match
				Pattern p_styleId = Pattern.compile(regex_valid);
				Matcher m_styleId = p_styleId.matcher(m_block.group()); 
				if (m_styleId.find()){ 
					
					found = true;
					backKeyCount++;
					sb.append(m_block.group().replaceAll("styleClass", " styleId=\"back\" styleClass"));
				} else{ 
					sb.append(m_block.group());
				}
			}
			m_block.appendTail(sb);

			if (found){	
				try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
					sb.setLength(0);
					className = filePath;
					System.out.println(className);
					System.out.println("Back key file generated Successfully : " + ++backKeyClass );
					System.out.println("Back key added : " + backKeyCount);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * List all files from a directory and its subdirectories
	 */
	private static void listFilesAndFilesSubDirectories(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile()){
				addBackKey(file.getAbsolutePath());
				addContinueKey(file.getAbsolutePath());
				addSubmitKey(file.getAbsolutePath());
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

}
