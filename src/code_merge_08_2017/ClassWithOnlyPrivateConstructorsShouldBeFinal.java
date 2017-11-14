package code_merge_08_2017;

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

public class ClassWithOnlyPrivateConstructorsShouldBeFinal {
	private static int countGlobal = 0;

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\ClassWithOnlyPrivateConstructorsShouldBeFinal"};
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
				helper(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println(countGlobal);
	}
	
	private static void helper(String filePath){
		if (!filePath.endsWith("java")) return;
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			
			//group 1 : class name
			String regex = "(?m)[^\\S\\n]*?public class (\\w+)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			String className = "";
			if (matcher.find()){
				className = matcher.group(1);
			}
//			System.out.println("---------------------------------------");
//			System.out.println(className);

			//group 1 : constructor type
			String regex_constructor = "(?m)^[^\\S\\n]*?(private|protected|public)?[^\\S\\n]*?" + className + "[^\\S\\n]*?\\(";
			Pattern pattern_constructor = Pattern.compile(regex_constructor);
			Matcher matcher_constructor = pattern_constructor.matcher(input);
			while (matcher_constructor.find()){
				String modifier = matcher_constructor.group(1);
//				System.out.println("************");
//				System.out.println(modifier);
				//private constructor
				if (modifier != null && modifier.equals("private")){
					needOverWrite = true;
				}
				else{
					needOverWrite = false;
					break;
				}
			}

			if (needOverWrite){
				input = input.replaceFirst("(?m)[^\\S\\n]*?public class", "public final class");
//				System.out.println(input);
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(input.getBytes());
					fileOutputStream.close();
				}
				countGlobal++;
				countLocal++;
				System.out.println(filePath);
				System.out.println(countLocal);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(filePath);
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
