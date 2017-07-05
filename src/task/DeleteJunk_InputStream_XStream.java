package task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DeleteJunk_InputStream_XStream {

	public static void main(String[] args) {

		String filePath = "gov.state.uim.wc.service.WeeklyCertService.saveWeeklyCertDetailsFromWcAck2017-07-03_0.xml";
		//regex represents "_$$_xxx" and "___-_-__xxx"
		String[] replacements = {"", ""};
		String[] regexs = {"(_\\$\\$_.*?)(\")", "(___\\-_\\-__.*?)([\\s\\>\\]])"};
		int[] groups = {1, 1};

		try (InputStream inputStream = new FileInputStream(filePath);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) 
		{


			String xml = bufferedReader.lines().collect(Collectors.joining("\n"));//convert xml file to a string


			//iterate through all regex
			for (int i = 0; i < regexs.length; i++){

				String regex = regexs[i];
				String replacement = replacements[i];
				int group = groups[i];
				Pattern pattern = Pattern.compile(regex);
				StringBuffer sb = new StringBuffer();
				Matcher matcher = pattern.matcher(xml);
				while (matcher.find()){
					matcher.appendReplacement(sb, "");
					for (int j = 1; j <= matcher.groupCount(); j++){
						if (j == group){
							sb.append(replacement);
						} else {
							sb.append(matcher.group(j));
						}
					}
				}
				matcher.appendTail(sb);
				xml = sb.toString();
			}

			FileOutputStream fileOutputStream = new FileOutputStream("temp.xml");
			fileOutputStream.write(xml.getBytes());
			fileOutputStream.close();
			System.out.println("Junk Deleted");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

}
