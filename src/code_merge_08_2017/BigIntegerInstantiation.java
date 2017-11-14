package code_merge_08_2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * BigInteger B_16 = new BigInteger("0");
 * BigDecimal totalWages = new BigDecimal(0);
 */

public class BigIntegerInstantiation {
	private static int count = 0;
	private static Map<String, String> map = new HashMap<>();

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\BigIntegerInstantiation"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
									"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
									"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
		
		map.put("BigInteger0", "BigInteger.ZERO");
		map.put("BigInteger1", "BigInteger.ONE");
		map.put("BigInteger10", "BigInteger.TEN");
		map.put("BigDecimal0", "BigDecimal.ZERO");
		map.put("BigDecimal1", "BigDecimal.ONE");
		map.put("BigDecimal10", "BigDecimal.TEN");
		
		
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
			//group 1 : BigInteger or BigDecimal, group 2 : number inside new BigInteger (" xxx ") 
			String regex = "new\\b\\s*?(BigInteger|BigDecimal)\\s*?\\(\\s*?\"?\\s*?(0|1|10)\\s*?\"?\\s*?\\)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			boolean needOverWrite = false;
			//BigInteger B_16 = new BigInteger("0"); ->  BigInteger B_16 = BigInteger.ZERO;
			//BigDecimal totalWages = new BigDecimal(0); -> BigDecimal.ZERO
			while(matcher.find()){
				needOverWrite = true;
				matcher.appendReplacement(sb, "");
				sb.append(map.get(matcher.group(1)+matcher.group(2)));
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
