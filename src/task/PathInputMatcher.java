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



public class PathInputMatcher {

	private static int count = 0;
	private static int fileCount = 0;

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\PathInputMatcher"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
									"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
		
		for (String directoryPath : directoryList){
			List<File> list = listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				excute(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println("total mismatch found : "+fileCount);
//		System.out.println("total input missing found : "+count);
	}
	
	private static void excute(String filePath){
		if (!filePath.endsWith("Action.java")) return;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			
 
			//group 1 : path value, group 2 : input value
			String regex = "@struts\\.action[\\S\\s]*?path=\"/(\\w+)\"[\\S\\s]*?input=\"\\.(\\w+)\"[\\S\\s]*?(?:@struts\\.action|\\*/)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			if(matcher.find()){
				String group = matcher.group();
				String group1 = matcher.group(1);
				String group2 = matcher.group(2);
//				System.out.println("-------------------Found-----------------");
//				System.out.println(group);
//				System.out.println("-------------------Group 1-----------------");
//				System.out.println(group1);
//				System.out.println("-------------------Group 2-----------------");
//				System.out.println(group2);

				if (!group1.equals(group2)){
					System.out.println(filePath.substring(filePath.lastIndexOf("\\")+1));
					fileCount++;
				}
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
