//package code_merge_08_2017;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//public class ConsecutiveAppendsShouldReuse_ReadLine {
//	private static int count = 0;
//
//	public static void main(String[] args) {
//		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\ConsecutiveLiteralAppends"};
////		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
////									"C:\\Workspace_Feng_Yang\\UI_1.2\\BenefitsWY",
////									"C:\\Workspace_Feng_Yang\\UI_1.2\\Framework",
////									"C:\\Workspace_Feng_Yang\\UI_1.2\\Tax",
////									"C:\\Workspace_Feng_Yang\\UI_1.2\\TaxWY",
////									"C:\\Workspace_Feng_Yang\\UI_1.2\\UIWorkflowListener"};
//		
//		
//		for (String directoryPath : directoryList){
//			List<File> list = listAllFiles(directoryPath);
//			Iterator<File> iterator = list.iterator();
//			while (iterator.hasNext()){
//				helper(iterator.next().getAbsolutePath());
//			}
//		}
//		System.out.println("Done");
//		System.out.println(count);
//	}
//	
//	private static void helper(String filePath){
//		if (!filePath.endsWith("java")) return;
//		try (FileReader fileReader = new FileReader(filePath);
//				BufferedReader bf = new BufferedReader(fileReader)){
//			
//			int countLocal = 0;
//			StringBuffer sb = new StringBuffer();
//			
//			boolean needOverWrite = false;
//			
//			//group 1 : 
//			String regex = "(?m)^[^\\S\\n]*(\\w+)\\.append[^\\S\\n]*\\(.*?\\)[^\\S\\n]*;[^\\S\\n]*$";
//			Pattern pattern = Pattern.compile(regex);
//			String line, stringbuffer = "";
//			List<String> list = new LinkedList<>();
//			while((line = bf.readLine()) != null){
//				Matcher matcher = pattern.matcher(line);
//				//find append statement
//				if (matcher.find()){
//					//first append
//					if (list.size() == 0){
//						list.add(line + "\n");
//						stringbuffer = matcher.group(1);
//					}
//					//second or more append
//					else{
//						//same stringbuffer
//						if (stringbuffer.equals(matcher.group(1))){
//							list.add(line + "\n");
//						}
//						//different stringbuffer
//						else{
//							//only one append before
//							if (list.size() == 1){
//								sb.append(list.get(0));
//							}
//							//more than one append
//							else if (list.size() > 1){
//								needOverWrite = true;
//								sb.append(list.get(0).replaceFirst("(?m);(\\s*)$", "$1"));
//								for (int i = 1; i < list.size() - 1; i++){
//									sb.append(list.get(i).replaceFirst(stringbuffer, "").replaceFirst("(?m);(\\s*)$", "$1"));
//									countLocal++;
//									count++;
//								}
//								//last append keep ;
//								sb.append(list.get(list.size() - 1).replaceFirst(stringbuffer, ""));
//								countLocal++;
//								count++;
//							}
//							//clean up
//							list.clear();
//							list.add(line + "\n");
//							stringbuffer = matcher.group(1);
//						}
//					}
//				}
//				//not find append statement
//				else{
//					//this line is empty or comment out
//					if (list.size() > 0 && RegexUtil.isCodeEmptyOrCommentOut(line)){
//						list.set(list.size() - 1, list.get(list.size() - 1) + line + "\n");
//						continue;
//					}
//					//only one append before
//					if (list.size() == 1){
//						sb.append(list.get(0));
//					}
//					//more than one append
//					else if (list.size() > 1){
//						needOverWrite = true;
//						sb.append(list.get(0).replaceFirst("(?m);(\\s*)$", "$1"));
//						for (int i = 1; i < list.size() - 1; i++){
//							sb.append(list.get(i).replaceFirst(stringbuffer, "").replaceFirst("(?m);(\\s*)$", "$1"));
//							countLocal++;
//							count++;
//						}
//						//last append keep ;
//						sb.append(list.get(list.size() - 1).replaceFirst(stringbuffer, ""));
//						countLocal++;
//						count++;
//					}
//					sb.append(line + "\n");
//					//clean up
//					list.clear();
//					stringbuffer = "";
//				}
//			}
//			
//			
//			String input = sb.toString();
//			System.out.println(input);
//			if (needOverWrite){
////				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
////					fileOutputStream.write(input.getBytes());
////					fileOutputStream.close();
////				}
//				System.out.println(filePath);
//				System.out.println(countLocal);
//			}
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private static List<File> listAllFiles (String directoryPath){
//		List<File> list = new LinkedList<>();
//		File directory = new File(directoryPath);
//		File [] fileList = directory.listFiles();
//		for (File file : fileList){
//			if (file.isFile()){
//				list.add(file);
//			}
//			else if (file.isDirectory()){
//				list.addAll(listAllFiles(file.getAbsolutePath()));
//			}
//		}
//		return list;
//	}
//}
