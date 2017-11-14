package code_merge_08_2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	public static String addImport(String input, String importStatement){
		StringBuffer sb = new StringBuffer();
		String regex_package = "package.*?;";
		Pattern pattern = Pattern.compile(regex_package);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()){
			matcher.appendReplacement(sb, "");
			sb.append(matcher.group());
			sb.append("\n\n" + importStatement);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static String replaceLast(String input, String oldString, String replacement) throws Exception{
		int i = input.lastIndexOf(oldString);
		if(i >=0){
			input = new StringBuilder(input).replace(i, i + replacement.length(), replacement).toString();
		}else{
			throw new Exception("RegexUtil replaceLast method : index < 0, target string does't exist");
		}
		return input;
		    
	}
	
	public static List<File> listAllFiles (String directoryPath){
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
	
//	//check if the index of the match start is in the comment range
//	public static boolean isInComment(List<Integer> commentRangeList, int matchStartIndex){
//		boolean isInComment = false;
//		for (int i = 0; i < commentRangeList.size(); i += 2){
//			int start = commentRangeList.get(i);
//			int end = commentRangeList.get(i + 1);
//			if (start < matchStartIndex && matchStartIndex < end){
//				isInComment = true;
//				break;
//			}
//		}
//		return isInComment;
//	}
//
//	//find comment  /*    xxxx   */ range
//	//Exception 1, more opening comment sign than closing comment sign
//	/** 
//	 * /**
//	 */
//	//Exception 2,  invalid closing /*/
//	/*//data fix -manipulation for daily migration of account status */
//	//Exception 3, invalid opening ///*
//	//Exception 4, //***********************************************************************************
//	public static List<Integer> commentRange(String input) throws Exception{
//		List<Integer> list = new LinkedList<>();
//		Pattern pattern_open = Pattern.compile("(?m)^[^\\S\\n]*?(/)?.*?(/\\*)");
//		Matcher matcher_open = pattern_open.matcher(input);
//		Pattern pattern_close = Pattern.compile("\\*/");
//		Matcher matcher_close = pattern_close.matcher(input);
//		int pre_close = 0;
//		int cur_open = 0;
//		while (matcher_open.find()){
//			//Exception 3
//			if (matcher_open.group(1) != null){
//				continue;
//			}
//			//Exception 1
//			if (matcher_open.start(2) < pre_close){
//				continue;
//			}
//			list.add(matcher_open.start());
//			cur_open = matcher_open.end() - 1;
//			//once find opening comment, look for closing comment
//			while (matcher_close.find()){
//				//Exception 2, /*/  invalid closing */
//				if (matcher_close.start() <= cur_open){
//					continue;
//				}
//				list.add(matcher_close.end() - 1);
//				pre_close = matcher_close.end() - 1;
//				break;
//			}
//			System.out.println("----------------------");
//			System.out.println(input.substring(matcher_open.start(2), matcher_close.end()));
//		}
//		System.out.println(list.size());
//		if (list.size() % 2 != 0){
//			throw new Exception("RegexUtil method commentRange : range is not valid");
//		}
//		return list;
//	}
	
	public static String addLoggerDeclaration(String input){
		Pattern pattern = Pattern.compile("(?ms)^[^\\S\\n]*?public (?:abstract )?class (\\w+)\\s*?.*?\\{");
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		if (matcher.find()){
			String loggerDeclaration = matcher.group().replaceFirst("\\{", "\\{\n\tprivate static final Logger LOGGER = Logger.getLogger("+matcher.group(1)+".class);\n");
			matcher.appendReplacement(sb, loggerDeclaration);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static String commentOut(String input) throws IOException{
		BufferedReader br = new BufferedReader(new StringReader(input));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null){
			sb.append("//").append(line).append("\n");
		}
		return sb.toString();
	}
	
	public static String addLoggerError(String exception){
		String loggerStatement = "\n\t\t\tif (LOGGER.isEnabledFor(Level.ERROR)) {\n"
				+ "\t\t\t\tLOGGER.error(\"error\", "
				+ exception
				+ ");\n"
				+ "\t\t\t}\n";
		return loggerStatement;
	}
	
	//search until next } or to the end of the code
	public static boolean isCodeAfterCurlyBraceEmpty(String codeAfterCurlyBrace) throws IOException{
		BufferedReader br = new BufferedReader(new StringReader(codeAfterCurlyBrace));
		String line;
		boolean isEmptyOrOnlyHasComment = true;
		//comment out by /* xxx */
		boolean comment_out = false;
		while ((line = br.readLine()) != null){
			if (comment_out){
				if (line.matches(".*?\\*/\\s*")){
					comment_out = false;
				}
				else if (line.matches(".*?\\*/.*?")){
					comment_out = false;
					isEmptyOrOnlyHasComment = false;
				}
				continue;
			}
			//comment out by /* xxx */
			if (line.matches("\\s*(/\\*).*?")){
				comment_out = true;
				continue;
			}
			//line is commented out
			if (line.matches("\\s*(//|/\\*|\\*).*?")){
				continue;
			}
			//line only has empty whitespace
			else if (line.matches("\\s*")){
				continue;
			}
			//line reach the end of the while block
			else if (line.matches("\\s*\\}.*?")){
				break;
			}
			//line has code
			else{
				isEmptyOrOnlyHasComment = false;
				break;
			}
		}
		return isEmptyOrOnlyHasComment;
	}
	
	public static boolean isCodeEmptyOrCommentOut(String input) throws IOException{
		BufferedReader br = new BufferedReader(new StringReader(input));
		String line;
		boolean isEmptyOrOnlyHasComment = true;
		//comment out by /* xxx */
		boolean comment_out = false;
		while ((line = br.readLine()) != null){
			if (comment_out){
				if (line.matches(".*?\\*/\\s*")){
					comment_out = false;
				}
				else if (line.matches(".*?\\*/.*?")){
					comment_out = false;
					isEmptyOrOnlyHasComment = false;
				}
				continue;
			}
			//comment out by /* xxx */
			if (line.matches("\\s*(/\\*).*?")){
				comment_out = true;
				continue;
			}
			//line is commented out
			if (line.matches("\\s*(//|/\\*|\\*).*?")){
				continue;
			}
			//line only has empty whitespace
			if (line.matches("\\s*")){
				continue;
			}
			//line has code
			else{
				isEmptyOrOnlyHasComment = false;
				break;
			}
		}
		return isEmptyOrOnlyHasComment;
	}
	
}
