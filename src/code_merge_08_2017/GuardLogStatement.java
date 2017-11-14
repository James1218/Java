package code_merge_08_2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuardLogStatement {
	
	private static int count = 0;
	private static Map<String, String> map = new HashMap<>();

	
	public static void main (String [] args){
		
		
//		String [] filePathArr = GuardLogStatement_FilePath.filePathArr;
//		for (String filePath : filePathArr){
//			guardLogStatement(filePath);
//		}
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\Benefits"};
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\GuardLogStatement"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
									"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
		
		map.put("trace", "isTraceEnabled()");
		map.put("debug", "isDebugEnabled()");
		map.put("info", "isInfoEnabled()");
		map.put("warn", "isEnabledFor(Level.WARN)");
		map.put("error", "isEnabledFor(Level.ERROR)");
		map.put("fatal", "isEnabledFor(Level.FATAL)");

		
		for (String directoryPath : directoryList){
			List<File> list = listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				guardLogStatement(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println(count);
		
		
		
	}
	
	private static void guardLogStatement(String filePath){
		if (!filePath.endsWith("java")) return;
		
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();

			//white space but no new line [^\\S\\n]
			//group 1 : if statement, group 2: comment-out logger statement
			//group 3 : logger variable, group 4 : logger function called
			//group 5 : if logger.enable has multiple logger statements
			String regex_block = "(?m)(^[^\\S\\n]*?if.*?(?:LOGGER|Logger|logger)\\s*?\\.\\s*?(?:isDebugEnabled|isInfoEnabled|isTraceEnabled|isEnabledFor)[\\S\\s]*?\\{)?"
					+ "\\s*?^[^\\S\\n]*?(//|/\\*|\\*)?[^\\S\\n]*?(LOGGER|Logger|logger)\\s*?\\.\\s*?(\\w+)[\\S\\s]*?\\)\\s*?;";

			
			Pattern pattern = Pattern.compile(regex_block);
			Matcher matcher = pattern.matcher(input);
			String logger, method;
			boolean needImport = false;
			boolean needOverWrite = false;
			while (matcher.find()){
				//surrounded by if
				if (matcher.group(1) != null){
					continue;
				}
				if (matcher.group(2) != null){
					continue;
				}

				logger = matcher.group(3);
				method = matcher.group(4);
				//not a logger regular method, for example
				//logger.batchRecordProcess(taxReportDetailID.toString(),"T_TAX_REPORT_DETAIL");
				if (!map.containsKey(method)){
					continue;
				}
//				System.out.println(matcher.group());
				needOverWrite = true;

				matcher.appendReplacement(sb, "");
				sb.append("\n\t\tif (" + logger +"." + map.get(method) + ") {");
				sb.append(matcher.group());
				sb.append("\n\t\t}");
				if (method.equals("warn") | method.equals("error") | method.equals("fatal")){
					needImport = true;
				}
//				System.out.println(sb.toString());
				countLocal++;
				count++;
			}
			matcher.appendTail(sb);
			input = new String(sb.toString());
			if (needImport){
				input = input.replaceFirst("import", "import org.apache.log4j.Level;\nimport");
			}
//			System.out.println(input);
			if (needOverWrite){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(input.getBytes());
					fileOutputStream.close();
				}
				System.out.println(filePath);
				System.out.println(countLocal);
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
