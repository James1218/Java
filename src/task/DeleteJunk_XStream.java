package task;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeleteJunk_XStream {

	/*
	 * Delete Junk like "_$$_jvst1fb_1c8" and "___-_-__jvst243__398" by using Regex
	 * @author : 1381317
	 */
	private String deleteJunk(String xml){
		
		//regex represents "_$$_xxx" and "___-_-__xxx"
		String[] replacements = {"", ""};
		String[] regexs = {"(_\\$\\$_.*?)(\")", "(___\\-_\\-__.*?)([\\s\\>\\]])"};
		int[] groups = {1, 1};
		
		StringBuffer sb = new StringBuffer();

		//iterate through all regex
		for (int i = 0; i < regexs.length; i++){

			String regex = regexs[i];
			String replacement = replacements[i];
			int group = groups[i];
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(xml);
			while (matcher.find()){
				matcher.appendReplacement(sb, "");//copy previous match to current match to stringbuffer
				for (int j = 1; j <= matcher.groupCount(); j++){//copy each group to stringbuffer
					if (j == group){
						sb.append(replacement);//only change the content of the target group
					} else {
						sb.append(matcher.group(j));//copy rest of the group
					}
				}
			}
			matcher.appendTail(sb);//copy the last non-match
			xml = sb.toString();
			sb.setLength(0);//clean stringbuffer
		}
		return xml;
	}
	
}
