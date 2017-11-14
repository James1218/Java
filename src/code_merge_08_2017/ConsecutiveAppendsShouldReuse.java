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

public class ConsecutiveAppendsShouldReuse {
	private static int count = 0;

	public static void main(String[] args) {
		
//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\CodeMerge_08_2018\\ConsecutiveLiteralAppends"};
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
		System.out.println(count);
	}
	
	private static void helper(String filePath){
		if (!filePath.endsWith("java")) return;
		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			
			int countLocal = 0;
			StringBuffer sb = new StringBuffer();
			String input = bf.lines().collect(Collectors.joining("\n"));
			boolean needOverWrite = false;
			
			StringBuffer sb_between_append = new StringBuffer();
			List<String> list = new LinkedList<>();
			//group 1 : stringbuffer variable
			String regex = "(?m)^[^\\S\\n]*(\\w+)\\.append[^\\S\\n]*\\([\\S\\s]*?\\)[^\\S\\n]*;";
			String stringbuffer = "";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			while (matcher.find()){
//				System.out.println("---------------------------");
//				System.out.println(matcher.group());
				//from last match to the start of this match
				matcher.appendReplacement(sb_between_append, "");
				//first append match, sb appends until start of match
				if (list.size() == 0){
//					System.out.println("size 0");
					sb.append(sb_between_append.toString());
					list.add(matcher.group());
					stringbuffer = matcher.group(1);
//					System.out.println(list);
				}
				//possible second append match
				else{
					//empty or comment between two appends, valid consecutive appends, add comment to the last append
					if (stringbuffer.equals(matcher.group(1))
							&& RegexUtil.isCodeEmptyOrCommentOut(sb_between_append.toString())){
						String lastAppend = list.get(list.size() - 1) + sb_between_append.toString();
						list.set(list.size() - 1, lastAppend);
//						System.out.println("empty betwwen two appends");
						list.add(matcher.group());
//						System.out.println(list.size());
//						System.out.println(list);
						
					}
					//code not empty between two appends, not consecutive appends
					else{
						//only one append in the list, don't make the change
						if (list.size() == 1){
//							System.out.println("list size = 1");
//							System.out.println(list);
							sb.append(list.get(0));
							sb.append(sb_between_append.toString());
							list.clear();
							list.add(matcher.group());
							stringbuffer = matcher.group(1);
//							System.out.println(list);
						}
						//two appends in the list, make change
						else{
//							System.out.println("list size = 2");
//							System.out.println(list);
							needOverWrite = true;
							//first append
							String firstAppend = list.get(0).replaceFirst("\\)[^\\S\\n]*;", ")");
							sb.append(firstAppend);
							//middle append
							for (int i = 1; i < list.size() - 1; i++){
								String middleAppend = list.get(i).replaceFirst(stringbuffer, "").replaceFirst("\\)[^\\S\\n]*;", ")");
								sb.append(middleAppend);
								countLocal++;
								count++;
							}
							//last append
							String lastAppend = list.get(list.size() - 1).replaceFirst(stringbuffer, "");
							sb.append(lastAppend);
							sb.append(sb_between_append.toString());
							countLocal++;
							count++;
							list.clear();
							list.add(matcher.group());
							stringbuffer = matcher.group(1);
//							System.out.println(list);
						}
					}
					
				}
				//clean up
				sb_between_append.setLength(0);
			}//end while
			
//			System.out.println("While end");
//			System.out.println(list.size());
			if (list.size() == 0){
				//do nothing
			}
			//only one append in the list, don't make the change
			else if (list.size() == 1){
				sb.append(list.get(0));
			}
			//two appends in the list, make change
			else{
				needOverWrite = true;
				//first append
				String firstAppend = list.get(0).replaceFirst("\\)[^\\S\\n]*;", ")");
				sb.append(firstAppend);
				//middle append
				for (int i = 1; i < list.size() - 1; i++){
					String middleAppend = list.get(i).replaceFirst(stringbuffer, "").replaceFirst("\\)[^\\S\\n]*;", ")");
					sb.append(middleAppend);
					countLocal++;
					count++;
				}
				//last append
				String lastAppend = list.get(list.size() - 1).replaceFirst(stringbuffer, "");
				sb.append(lastAppend);
				countLocal++;
				count++;
			}
			list.clear();
			
			matcher.appendTail(sb);
			input = sb.toString();
//			System.out.println("\n\n****************************");
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
