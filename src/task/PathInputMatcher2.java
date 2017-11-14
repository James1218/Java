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



public class PathInputMatcher2 {

	private static int strutsActionXmlCount = 0;
	private static int misMatchCount = 0;
	private static int tilesDefsXmlCount = 0;
	private static List<String> list = new LinkedList<>();
	//	private static List<String> strutsActionXmlList = new LinkedList<>();
	//	private static List<String> tilesDefsXmlList = new LinkedList<>();
	private static String pathGlobal = null, inputGlobal = null;
	private static String pathGlobalPrevious = null, inputGlobalPrevious = null;

	public static void main(String[] args) {

//		String [] directoryList = {"C:\\Workspace_Feng_Yang\\NewWorkSpace2\\JavaTraining\\TestFile\\PathInputMatcher"};
		String [] directoryList = {"C:\\Workspace_Feng_Yang\\UI_1.2\\Benefits", 
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
				
				if (pathGlobal != null && !pathGlobal.equals(pathGlobalPrevious)){
					for (String directoryPath2 : directoryList){
						List<File> list2 = listAllFiles(directoryPath2);
						Iterator<File> iterator2 = list2.iterator();
						while (iterator2.hasNext()){
							String path = iterator2.next().getAbsolutePath();
							struts_action_xml(path);
							tiles_defs_xml(path);
							changeOtherAction(path);
						}
					}
				}
				
				pathGlobalPrevious = pathGlobal;
				inputGlobalPrevious = inputGlobal;
			}
		}

		System.out.println("Done");
		System.out.println("total mismatch found : "+misMatchCount);
		System.out.println("struts-action.xml changes : "+strutsActionXmlCount);
		System.out.println("tiles-defs.xml changes : "+tilesDefsXmlCount);
		System.out.println("relative action classes changes : " + list.size());
		System.out.println("----------------------------");
		list.forEach(System.out::println);
	}

	private static void changeOtherAction(String filePath) {
		if (!filePath.endsWith("Action.java")) return;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			boolean needOverWrite = false;


			
			String regex = "@struts\\.action-forward\\s+name\\s*?=\\s*?\""
					+ pathGlobal
					+ "\".*?path\\s*?=\\s*?\"\\.(\\w+)\"[\\S\\s]*?(?:@struts\\.action|\\*/)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			if(matcher.find()){
				String group = matcher.group();
				String group1 = matcher.group(1);
				//				System.out.println("-------------------Found-----------------");
				//				System.out.println(group);
				//				System.out.println("-------------------Group 1-----------------");
				//				System.out.println(group1);


				if (!group1.equals(pathGlobal)){
					System.out.println("change relative action class :  "+filePath.substring(filePath.lastIndexOf("\\")+1));
					list.add(filePath.substring(filePath.lastIndexOf("\\")+1));
					needOverWrite = true;
				}

				matcher.appendReplacement(sb, "");
				sb.append(input.substring(matcher.start(), matcher.start(1)))
					.append(pathGlobal)
					.append(input.substring(matcher.end(1), matcher.end()));
//				sb.append(group.replace(group1, pathGlobal));
			}
			matcher.appendTail(sb);
//			System.out.println(sb.toString());
			if (needOverWrite){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
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

	private static void tiles_defs_xml(String filePath) {
		if (!filePath.endsWith("tiles-defs.xml")) return;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			boolean needOverWrite = false;


			String regex = "definition\\s+name\\s*?=\\s*?\"\\."
					+ inputGlobal
					+ "\"";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);

			if(matcher.find()){
				String group = matcher.group();
//				System.out.println("-------------------Found-----------------");
//				System.out.println(group);

				tilesDefsXmlCount++;
				needOverWrite = true;

				matcher.appendReplacement(sb, "");
				sb.append(group.replace(inputGlobal, pathGlobal));
			}
			matcher.appendTail(sb);


			//			System.out.println(sb.toString());

			if (needOverWrite){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
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

	private static void struts_action_xml(String filePath) {
		if (!filePath.endsWith("struts-actions.xml")) return;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			boolean needOverWrite = false;


			//group 1 : path
			String regex = "forward\\s+name\\s*?=\\s*?\""
					+ pathGlobal
					+ "\"\\s+path\\s*?=\\s*?\"\\.(\\w+)\"";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);

			if(matcher.find()){
				String group = matcher.group();
				String group1 = matcher.group(1);
				//				System.out.println("-------------------Found-----------------");
				//				System.out.println(group);
				//				System.out.println("-------------------Group 1-----------------");
				//				System.out.println(group1);

				if (!group1.equals(pathGlobal)){
					strutsActionXmlCount++;
					needOverWrite = true;

				}
				matcher.appendReplacement(sb, "");
				sb.append(input.substring(matcher.start(), matcher.start(1)))
					.append(pathGlobal)
					.append(input.substring(matcher.end(1), matcher.end()));
			}
			matcher.appendTail(sb);

			//			System.out.println(sb.toString());

			if (needOverWrite){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
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

	private static void excute(String filePath){
		if (!filePath.endsWith("Action.java")) return;

		try (FileReader fileReader = new FileReader(filePath);
				BufferedReader bf = new BufferedReader(fileReader)){

			String input = bf.lines().collect(Collectors.joining("\n"));
			StringBuffer sb = new StringBuffer();
			boolean needOverWrite = false;


			//group 1 : path value, group 2 : input value
			String regex = "@struts\\.action[\\S\\s]*?path\\s*?=\\s*?\"/(\\w+)\"[\\S\\s]*?input\\s*?=\\s*?\"\\.(\\w+)\"[\\S\\s]*?(?:@struts\\.action|\\*/)";
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
//				System.out.println(matcher.start(2));
//				System.out.println(matcher.end(2));

				if (!group1.equals(group2)){
					System.out.println(filePath.substring(filePath.lastIndexOf("\\")+1));
					misMatchCount++;
					needOverWrite = true;
					pathGlobal = group1;
					inputGlobal = group2;
				}

				matcher.appendReplacement(sb, "");
				sb.append(input.substring(matcher.start(), matcher.start(2)))
					.append(group1)
					.append(input.substring(matcher.end(2), matcher.end()));
			}
			matcher.appendTail(sb);

			if (needOverWrite){
//				System.out.println(sb.toString());
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
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
