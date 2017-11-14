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



public class TagLib_Struts_Html {

	private static int totalJSP = 0;
	private static int totalJSP_miss_html = 0;
	private static int count = 0;
	private static int fileCount = 0;
	private static final String LINE = "<%@ taglib uri=\"/taglib/Owasp.CsrfGuard\" prefix=\"csrf\" %>";

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\jsp"};
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
		System.out.println("Done");
//		System.out.println("total files changed : "+fileCount);
//		System.out.println("total places changed : "+count);
		System.out.println("totalJSP : "+totalJSP);
		System.out.println("totalJSP_miss_html : "+totalJSP_miss_html);
	}
	
	private static void excute(String filePath){
		if (!filePath.endsWith("jsp")) return;
//		if (filePath.endsWith("template.jsp") || filePath.endsWith("templatenomenu.jsp") ||
//				filePath.endsWith("templatenomenunoheadernofooter.jsp") ){
//			return;
//		}
		totalJSP++;
		
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
 
			if (input.contains(LINE)){
				return;
			}
			
			//<%@ taglib uri="/taglib/struts-html" prefix="html"%>
			String regex = "\\<%@\\s*taglib[\\S\\s]*?uri\\s*=\\s*\"/taglib/struts-html\"[\\S\\s]*?%\\>";

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			
			if(matcher.find()){
//				System.out.println("-------------------Found-----------------");
//				System.out.println(matcher.group());
				String group = matcher.group();
				needOverWrite = true;
				matcher.appendReplacement(sb, "");
				sb.append(group).append("\n").append(LINE);
				count++;
				countLocal++;
			}
			else{
				totalJSP_miss_html++;
				System.out.println(filePath);
			}
//			matcher.appendTail(sb);
//			System.out.println(sb.toString());
//			if (needOverWrite){
//				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
//					fileOutputStream.write(sb.toString().getBytes());
//					fileOutputStream.close();
//				}
//				System.out.println(filePath);
//				System.out.println("local change : "+countLocal);
//				fileCount++;
//			}
			
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
