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
import java.util.PrimitiveIterator.OfDouble;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class Display_table {

	private static int check = 0;
	private static int count = 0;
	private static int fileChangeCount = 0;
	private static int csrf_delete_count = 0;
	private static int addCUrlCount = 0;
	private static int modifyExcludedParamsValueCount = 0;
	private static int addExcludedParamsCount = 0;
	private static int modifyURI = 0;
	private static int addURI = 0;
	private static final String LINE_FIRST = "<c:url var=\"displaytagsecurity\" value=\"\">";
	private static final String LINE_SECOND = "<c:param name=\"SKEY\" value=\"${SKEY}\" />";
	private static final String LINE_THIRD = "</c:url>";
	private static final String LINE_REQUESTURI = "requestURI=\"${fn:replace(displaytagsecurity,'&','&amp;')}\"";
	private static final String LINE_excludedParams = "excludedParams=\"SKEY\"";

	public static void main(String[] args) {

//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\Display_table"};
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
		System.out.println("-------------Done------------");
		System.out.println("total add <c:url> : "+addCUrlCount);
		System.out.println("total add requestURI   : "+addURI);
		System.out.println("total modify requestURI  : "+modifyURI);
		System.out.println("total add excludedParams attribute  : "+addExcludedParamsCount);
		System.out.println("total modify excludedParams attribute  : "+modifyExcludedParamsValueCount);
		System.out.println("total files changed : "+fileChangeCount);
	}

	private static void excute(String filePath){
		if (!filePath.endsWith("jsp")) return;
		String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
//		if (filePath.endsWith("template.jsp") || filePath.endsWith("templatenomenu.jsp") ||
//				filePath.endsWith("templatenomenunoheadernofooter.jsp") ){
//			return;
//		}

		int tableFound = 0;
		int formFound = 0;
		int csrfDeleted = 0;
		int countLocal = 0;
		boolean needOverWrite = false;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){
			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			
//			if (input.contains("<display:table")){
//				if (!input.contains("prefix=\"c\"")){
//					System.out.println(filePath);
//					check++;
//				}
//			}

			String regex; Pattern pattern; Matcher matcher;
			
			//if <c:url var=\displaytagsecurity\ not exist, add it
			if (!input.contains("<c:url var=\"displaytagsecurity\"")){
//				System.out.println("not contain <c:url var=\"displaytagsecurity\" : " + fileName);
				
				//find <display:table, add <c:url...
				//group 1 : white space on the line of <display:table>, group 2 : other code before <display:table>
				//group 3 : <display:table ..... until the end of the line
				regex = "([^\\S\\n]*)(.*?)(\\<display:table.*)";
				pattern = Pattern.compile(regex);
				matcher = pattern.matcher(input);
				
				if(matcher.find()){
					String group = matcher.group();
					String group1 = matcher.group(1);
					String group2 = matcher.group(2);
					String group3 = matcher.group(3);
					String indentation;
					if (group2.contains("/>") || group2.contains("</") ||group2.length() == 0){
						indentation = group1;
					}
					else{
						indentation = group1 + "\t";
					}
//					System.out.println("-------------------Found-----------------");
//					System.out.println(group);
//					System.out.println("-------------------group 1-----------------");
//					System.out.println(group1);
//					System.out.println("-------------------group 2-----------------");
//					System.out.println(group2);

					needOverWrite = true;
					matcher.appendReplacement(sb, "");
					sb.append(group1).append(group2).append("\n")
						.append(indentation).append(LINE_FIRST).append("\n")
						.append(indentation).append("\t").append(LINE_SECOND).append("\n")
						.append(indentation).append(LINE_THIRD).append("\n")
						.append(indentation).append(group3);
					count++;
					countLocal++;
				}
				matcher.appendTail(sb);
				input = sb.toString();
//				System.out.println(input);
				if (needOverWrite){
					System.out.println(fileName+ " :  add <c:url> ");
//					System.out.println("csrf found : "+tableFound);
//					System.out.println("csrf deleted : "+csrfDeleted);
//					System.out.println("csrf added : "+countLocal);
					addCUrlCount++;
				}
			}

			//if <display:table> has requestURI, replace it with new one. 
			//if excludedParams="SKEY" not exist, add it
			//if excludedParams="xxx" exist, but value is not SKEY, change it to excludedParams="xxx,SKEY"
			//group 1 : <display:table, group 2 : excludedParams="xxx", group 3 : value inside excludedParams attribute
			regex = "(\\<display:table)[\\S\\s]*?\\>";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(input);
			sb.setLength(0);
			
			if(matcher.find()){
				String group = matcher.group();
				String group1 = matcher.group(1);
				needOverWrite = true;
//				String group2 = matcher.group(2);
//				String group3 = matcher.group(3);
//				System.out.println("-------------------Found-----------------");
//				System.out.println(group);
//				System.out.println("-------------------group 1-----------------");
//				System.out.println(group1);
//				System.out.println("-------------------group 2-----------------");
//				System.out.println(group2);
//				System.out.println("-------------------group 3-----------------");
//				System.out.println(group3);
				
				
				if (group.contains("requestURI")){
					String uri = getURI(group);
					if (!uri.equals(LINE_REQUESTURI)){
						System.out.println(fileName+ " :  modify requestURI ");
						modifyURI++;
					}
//					System.out.println("-------------------uri-----------------");
//					System.out.println(uri);
					group = group.replace(uri, LINE_REQUESTURI);
				}
				else{
					group = group.replace(group1, group1 + " " + LINE_REQUESTURI);
					System.out.println(fileName+ " :  add requestURI ");
					addURI++;
				}
//				System.out.println("-------------------New Group 1-----------------");
//				System.out.println(group);
				
				if (group.contains("excludedParams")){
//					System.out.println("Contain excludedParams");
					String skey = getExcludedParams(group);
//					System.out.println(skey);
					if (!skey.contains("SKEY")){
						if (skey.contains("*")){
							//don't modify excludedParams="*" 
						}
						else{
							group = group.replace(skey, skey.substring(0, skey.length()-1) + ",SKEY\"");
							System.out.println(fileName+ " :  modify excludedParams");
							modifyExcludedParamsValueCount++;
						}
						
					}
				}
				else{
					group = group.replace(LINE_REQUESTURI, LINE_REQUESTURI + " " + LINE_excludedParams);
					System.out.println(fileName+ " :  add excludedParams");
					addExcludedParamsCount++;
				}

//				System.out.println("-------------------New Group 2-----------------");
//				System.out.println(group);
				
				needOverWrite = true;
				matcher.appendReplacement(sb, "");
				sb.append(group);
				count++;
				countLocal++;
			}
			matcher.appendTail(sb);
			input = sb.toString();
//			System.out.println(input);
			if (needOverWrite){
//				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
//					fileOutputStream.write(input.getBytes());
//					fileOutputStream.close();
//				}
				fileChangeCount++;
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
	
	private static String getURI(String s){
		int requestURI_index = s.indexOf("requestURI");
		int parenthesesFirst = s.indexOf("\"", requestURI_index+1);
		int parenthesesSecond = s.indexOf("\"", parenthesesFirst+1);
		s = s.substring(requestURI_index, parenthesesSecond+1);
		return s;
	}
	
	private static String getExcludedParams(String s){
		int requestURI_index = s.indexOf("excludedParams");
		int parenthesesFirst = s.indexOf("\"", requestURI_index+1);
		int parenthesesSecond = s.indexOf("\"", parenthesesFirst+1);
		s = s.substring(requestURI_index, parenthesesSecond+1);
		return s;
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
