package task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteJunkTest {

	public static void main(String[] args) {
		
		String filePath = "gov.state.uim.wc.service.WeeklyCertService.saveWeeklyCertDetailsFromWcAck2017-07-03_0.xml";
		String replacement = "";
		String regex = "(___\\-_\\-__.*?)([\\s\\>\\]])";
		int groups = 1;
		String input = "<gov.state.uim.domain.data.IssueData___-_-__jvst243__1bd>reference=";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		System.out.println(matcher.find());
	}

}
