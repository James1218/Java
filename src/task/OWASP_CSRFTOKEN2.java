package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class OWASP_CSRFTOKEN2 {

	private static int count = 0;
	private static int fileCount = 0;
	private static int csrf_delete_count = 0;
	private static final String LINE = "<tr><td><input type=\"hidden\" name=\"<csrf:token-name/>\" value=\"<csrf:token-value uri=\"protect.html\"/>\"/></td></tr>";
	
	public static void main(String[] args) {

//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\OWASP_CSRFTOKEN"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\Benefits", 
				"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\BenefitsWY",
				"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\Framework",
				"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\Tax",
				"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\TaxWY",
				"C:\\Workspace_Feng_Yang\\UI_1.2_Tax\\UIWorkflowListener"};

		for (String directoryPath : directoryList){
			List<File> list = listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				excute(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("-------------Done------------");
		System.out.println("total files changed : "+fileCount);
		System.out.println("total csrf deleted : "+csrf_delete_count);
		System.out.println("total csrf added : "+count);
	}

	private static void excute(String filePath){
		if (!filePath.endsWith("jsp")) return;
		if (filePath.endsWith("template.jsp") || filePath.endsWith("templatenomenu.jsp") ||
				filePath.endsWith("templatenomenunoheadernofooter.jsp") ){
			return;
		}

		int csrfFound = 0;
		int csrfDeleted = 0;
		int countLocal = 0;
		boolean needOverWrite = false;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){


			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();

			/*delete existing csrf start*/ 	
			if (input.contains("csrf:token-name")){
				csrfFound++;
			}

			//<tr><td><input type="hidden" name="<csrf:token-name/>"
			String regex = "\\<tr\\>\\s*\\<td\\>\\s*\\<input type=\"hidden\" name=\"<csrf:token\\-name.*?\\</tr\\>";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			while(matcher.find()){
				String group = matcher.group();
//				System.out.println("-------------------Delete-----------------");
//				System.out.println(group);
				needOverWrite = true;
				matcher.appendReplacement(sb, "");
				csrfDeleted++;
				csrf_delete_count++;
			}
			matcher.appendTail(sb);
			if (csrfDeleted != csrfFound){
				throw new Exception("csrf:token-name more than one");
			}

			input = sb.toString();
//			System.out.println(input);
			/*delete existing csrf end*/ 	


			sb = new StringBuffer();
			//group 1 : white space before </table>, used to format; 
			//group 2 : </table>, in case no white space is found 
			regex = "\\<html:form[\\S\\s]*?([^\\S\\n]*)(\\</table\\>)[\\S\\s]*?\\</html:form";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(input);

			if(matcher.find()){
				String group = matcher.group();
				if (group.contains("csrf:token-name")){
					throw new Exception("csrf:token-name hasn't been deleted yet");
				}
				String group1 = matcher.group(1);
				if (group1 == null){
					throw new Exception("No space befoer </table>");
				}
				String group2 = matcher.group(2);
//				System.out.println("-------------------Found-----------------");
//				System.out.println(group1);
//				System.out.println(group1 + group2);
				needOverWrite = true;
				matcher.appendReplacement(sb, "");
				sb.append(group.replaceFirst(group2, "\t"+LINE+"\n"+group1+group2));
				count++;
				countLocal++;
			}
			matcher.appendTail(sb);
			input = sb.toString();
//			System.out.println(input);
			if (needOverWrite){
//				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
//					fileOutputStream.write(input.getBytes());
//					fileOutputStream.close();
//				}
				System.out.println(filePath);
				System.out.println("csrf found : "+csrfFound);
				System.out.println("csrf deleted : "+csrfDeleted);
				System.out.println("csrf added : "+countLocal);
				fileCount++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static List<File> listAllFiles (String directoryPath){
		List<File> list = new LinkedList<>();
		File directory = new File(directoryPath);
		File [] fileList = directory.listFiles();
		for (File file : fileList){
			if (file.isFile()){
				list.add(file);
			}
			else if (file.isDirectory()){
				list.addAll(listAllFiles(file.getAbsolutePath()));
			}
		}
		return list;
	}

}
