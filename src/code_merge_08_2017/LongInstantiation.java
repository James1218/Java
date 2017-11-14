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


/*
 .
public class Foo {
	private Long i = new Long(0); // change to Long i =Long.valueOf(0);
	
	if ((appealData.getDocketNumber() == null) || ((new Long(0).compareTo(appealData.getDocketNumber())) > 0)) {
	
	long temp = (new Long(currentForm.getAppealPartyIdTwo())).longValue();
	
	data.setEmployerPartnerId(new Long(rs.getLong("EMPLOYER_PARTNER_ID")));
	
	new Long(
								GlobalConstants.NUMERIC_THREE));
}
 */


public class LongInstantiation {

	private static int count = 0;

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\LongInstantiation"};
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
				longInstantiation(iterator.next().getAbsolutePath());
			}
		}
		System.out.println("Done");
		System.out.println(count);
	}
	
	private static void longInstantiation(String filePath){
		if (!filePath.endsWith("java")) return;
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			//group 1 : code inside Long ( xxx ) 
			String regex = "(?s)new\\b\\s*?Long\\s*?\\(.*?[;\\{]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			//private Long i = new Long(0); // change to Long i =Long.valueOf(0);
			//(new Long(0).compareTo(appealData.getDocketNumber())) > 0)) {
			while(matcher.find()){
				needOverWrite = true;
				int lastParentheses = getLastParentheses(matcher.group());
				int firstParentheses = matcher.group().indexOf("(");
				String codeInsideLong = matcher.group().substring(firstParentheses + 1, lastParentheses);
				matcher.appendReplacement(sb, "");
				sb.append("Long.valueOf(" + codeInsideLong + matcher.group().substring(lastParentheses));
				count++;
				countLocal++;
			}
			matcher.appendTail(sb);
//			System.out.println(sb.toString());
			if (needOverWrite){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
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
	
	private static int getLastParentheses(String input){
		
		int left = 0, right = 0, i = -1;
		while (!(left == right && left != 0)){
			i++;
			char temp = input.charAt(i);
			if (temp == '('){
				left++;
			}
			if (temp == ')'){
				right++;
			}
		}
		return i;
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
