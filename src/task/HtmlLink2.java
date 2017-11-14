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



public class HtmlLink2 {

	private static int count = 0;
	private static int fileCount = 0;
	private static final String LINE = "<c:param name=\"SKEY\" value=\"${SKEY}\" />";

	public static void main(String[] args) {
		
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\jsp"};
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
//									"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
//									"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
//									"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
//									"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
//									"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
		
		for (String directoryPath : directoryList){
			List<File> list = listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				excute(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println("total files changed : "+fileCount);
		System.out.println("total places changed : "+count);
	}
	
	private static void excute(String filePath){
		if (!filePath.endsWith("jsp")) return;
		if (filePath.endsWith("template.jsp") || filePath.endsWith("templatenomenu.jsp") ||
				filePath.endsWith("templatenomenunoheadernofooter.jsp") ){
			return;
		}
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
 
//			String test = "</c:url> \n <html:link";
//			String reg = "(?)([^\\S\\n]*)\\</c:url\\>\\s*?\\<html:link";
////			String reg = "<table[^\\S\\n]*</table>";
//			Matcher m = Pattern.compile(reg).matcher(test);
//			if (m.find()){
//				System.out.println(m.group());
//				System.out.println(m.group(1));
//			}
			
			//group 1 : first half <c:url ... before </c:url>; group 2 : variable, need to match; 
			//group 3 : whitespace, use to make alignment; group 4 : second half 
			String regex = "(?)(\\<\\s*c\\s*:\\s*url[\\S\\s]*?var\\s*=\\s*\"\\s*(\\w+)\\s*\"[\\S\\s]*?([^\\S\\n]*))(\\<\\s*/\\s*c\\s*:\\s*url\\s*\\>\\s*\\<\\s*html\\s*:\\s*link[\\S\\s]*?\\2[\\S\\s]*?\\<\\s*/\\s*html\\s*:\\s*link\\s*\\>)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			
			while(matcher.find()){
//				System.out.println("-------------------Found-----------------");
//				System.out.println(matcher.group());
//				System.out.println("-------------------1-----------------");
//				System.out.println(matcher.group(1));
//				System.out.println("-------------------2-----------------");
//				System.out.println(matcher.group(2));
//				System.out.println("-------------------3-----------------");
//				System.out.println(matcher.group(3));
//				System.out.println("-------------------4-----------------");
//				System.out.println(matcher.group(4));
				String group = matcher.group();
				if (group.contains(LINE)){
					continue;
				}
				needOverWrite = true;
				String group1 = matcher.group(1);
				String group2 = matcher.group(2);
				String group3 = matcher.group(3);
				String group4 = matcher.group(4);
				matcher.appendReplacement(sb, "");
				sb.append(group1).append("\t").append(LINE).append("\n").append(group3).append(group4);
				count++;
				countLocal++;
//				System.out.println(group1+"\t"+LINE+"\n"+group3+group4);
			}
			matcher.appendTail(sb);
//			System.out.println(sb.toString());
			if (needOverWrite){
//				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
//					fileOutputStream.write(sb.toString().getBytes());
//					fileOutputStream.close();
//				}
				System.out.println(filePath);
				System.out.println("local change : "+countLocal);
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
