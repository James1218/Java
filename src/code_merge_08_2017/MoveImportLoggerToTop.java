package code_merge_08_2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MoveImportLoggerToTop {

	private static int count = 0;

	public static void main(String[] args) {

		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\revert"};
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
//				"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
//				"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
//				"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
//				"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
//				"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};

		for (String directoryPath : directoryList){
			List<File> list = listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				moveImportLoggerToTop(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println(count);
	}

	private static void moveImportLoggerToTop(String filePath){
		if (!filePath.endsWith("java")) return;


		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			StringBuffer sb = new StringBuffer();			
			String input = bf.lines().collect(Collectors.joining("\n"));

			String regex_import = "import org\\.apache\\.log4j\\.Logger;\\s*(public (?:abstract )?class)";
			Pattern pattern = Pattern.compile(regex_import);
			Matcher matcher = pattern.matcher(input);
			boolean findImport = false;
			while (matcher.find()){
				findImport = true;
//								System.out.println(matcher.group());
				matcher.appendReplacement(sb, "");
				sb.append(matcher.group(1));
			}
			if (!findImport){
				return;
			}
			matcher.appendTail(sb);
			input = sb.toString();
			sb.setLength(0);
			String regex_package = "package.*?;";
			pattern = Pattern.compile(regex_package);
			matcher = pattern.matcher(input);
			while (matcher.find()){
				matcher.appendReplacement(sb, "");
				sb.append(matcher.group());
				sb.append("\n\nimport org.apache.log4j.Logger;\n");
			}
			matcher.appendTail(sb);
//			System.out.println(sb.toString());
			try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
				fileOutputStream.write(sb.toString().getBytes());
				fileOutputStream.close();
				sb.setLength(0);
				System.out.println(filePath);
				count++;
			}



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
