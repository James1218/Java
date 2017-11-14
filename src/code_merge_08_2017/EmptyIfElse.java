package code_merge_08_2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmptyIfElse {

	private static int count = 0;

	public static void main(String[] args) {
		
		

//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\EmptyIfElse"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
				"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
				"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
				"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
				"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
				"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
		
		
		for (String directoryPath : directoryList){
			List<File> list = RegexUtil.listAllFiles(directoryPath);
			Iterator<File> iterator = list.iterator();
			while (iterator.hasNext()){
				applyRegexChange(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println(count);
	}

	private static void applyRegexChange(String filePath){
		if (!filePath.endsWith("java")) return;
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();


			//group 1 : comment sign, group 2 : { , group : if statement, from the beginning of the line to {
			String regex = "(?m)^[^\\S\\n]*?(//|/\\*|\\*)?[^\\S\\n]*?(?:\\})?[^\\S\\n]*?(?:if|else)[\\S\\s]*?(\\{)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			boolean isEmptyOrOnlyHasComment;
			String loggerStatement = "\n\t\t\tif (LOGGER.isDebugEnabled()) {\n"
									+ "\t\t\t\tLOGGER.debug(\"Empty if/else Statement\");\n"
									+ "\t\t\t}\n";
			while(matcher.find()){
//				System.out.println("********************************");
//				System.out.println(matcher.group());
//				System.out.println("----------------------------");
//				System.out.println(matcher.group(1));
//				System.out.println("____________________");
//				System.out.println(matcher.group(2));
				if (matcher.group(1) != null){
					continue;
				}
				isEmptyOrOnlyHasComment  = RegexUtil.isCodeAfterCurlyBraceEmpty(input.substring(matcher.end()));
//				System.out.println(isEmptyOrOnlyHasComment);
				if (isEmptyOrOnlyHasComment){
					matcher.appendReplacement(sb, "");
					sb.append(matcher.group());
					sb.append(matcher.group(2).replace("{", loggerStatement));
					needOverWrite = true;
					count++;
					countLocal++;
				}
			}
			matcher.appendTail(sb);
			input = sb.toString();

			if (needOverWrite){
				String accessLogger = "import gov.state.uim.framework.logging.log4j.AccessLogger;";
				String apacheLogger = "import org.apache.log4j.Logger;";
//				String utilLooger = "java.util.logging.Logger;";
				//import java.util.logging.Logger;
				if (!input.contains(accessLogger) && !input.contains(apacheLogger)){
					input = RegexUtil.addImport(input, apacheLogger);
					input = RegexUtil.addLoggerDeclaration(input);
				}
				//
				if (Pattern.compile("(Logger\\s+logger\\s+\\=)").matcher(input).find()){
					input = input.replace("LOGGER", "logger");
				}
				
//				System.out.println(input);
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
}
