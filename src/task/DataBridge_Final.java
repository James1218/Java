/*
Already surrounded with the correct comment : ignore it if possible
Already surrounded with the incorrect comment : ignore it

import gov.state.uim.domain.data.ClaimApplicationEmployerDataBridge;


ClaimApplicationEmployerDataBridge claimAppEmpData = 
							(ClaimApplicationEmployerDataBridge) factFindingData
							.getClaimAppEmpData();


ClaimantData clmtData = (ClaimantDataBridge) objAssembly
				.getFirstComponent(ClaimantDataBridge.class);


The code is in comment : ignore it
// objectAssembly.getFirstComponent(ClaimantDataBridge.class);
//ClaimantData claimantData = (ClaimantData)objAssembly.getFirstComponent(ClaimantDataBridge.class);


ClaimApplicationEmployerDataBridge appEmployerData = (ClaimApplicationEmployerDataBridge) iterator
					.next();


ClaimApplicationEmployerDataBridge clmAppEmprData = null;  ignore it


ClaimantData cData = new ClaimantDataBridge();

	 impactPoint="Start")
				ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
						.fetchORCreate(ClaimApplicationClaimantDataBridge.class);


Not Exist:
ClaimantData clmtData
 = (ClaimantDataBridge) objAssembly

 */

package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.FormatStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataBridge_Final {

	private static int classCount = 0;
	private static int statementCount = 0;

	public static void main(String[] args) {

		String [] directoryList = {"C:\\NewWorkSpace\\Benefits", "C:\\NewWorkSpace\\BenefitsWY", "C:\\NewWorkSpace\\Framework", 
				"C:\\NewWorkSpace\\Tax", "C:\\NewWorkSpace\\TaxWY"};

		for (String directory : directoryList){
			listFilesAndFilesSubDirectories(directory);
		}

		
//		String directoryName = "C:\\NewWorkSpace2\\JavaTraining\\target";
//		listFilesAndFilesSubDirectories(directoryName);
	}

	/**
	 * List all files from a directory and its subdirectories
	 */
	private static void listFilesAndFilesSubDirectories(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile()){
				addAnnotation(file.getAbsolutePath());
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

	private static void addAnnotation(String filePath){

		//only .java file add annotation
		if (!filePath.endsWith("java"))	return;

		//Already surrounded with comment
		String comment = "((?m)^[\\t ]+//.*?\"Start\"\\)[\\t ]*?\n)?";
		//"DataBridge()" or "(DataBridge)" is at the first line of the match
		String oneLineRegex = "((?m)^[\\t ]+\\w.*?=.*?DataBridge\\s*?[\\(\\)\\>][^;]*?;[\\t ]*?)";
		//"DataBridge()" or "(DataBridge)" is at the second line of the match
		String twoLineRegex = "((?m)^[\\t ]+\\w.*?=\\s*?\n[\\t ]+.*?DataBridge\\s*?[\\(\\)\\>][^;]*?;[\\t ]*?)";

		String regex = comment + "(" + oneLineRegex + "|" + twoLineRegex + ")";
		
		String start = "//@cif_wy(storyNumber = \"P1-FRM-000\" , requirementId = \"Data Bridge Changes\",designDocName = \"Data Bridge Changes\",designDocSection = \"\", dcrNo = \"\", mddrNo = \"\", impactPoint = \"Start\")";
		String end = "//@cif_wy(storyNumber = \"P1-FRM-000\" , requirementId = \"Data Bridge Changes\",designDocName = \"Data Bridge Changes\",designDocSection = \"\", dcrNo = \"\", mddrNo = \"\", impactPoint = \"End\")";
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){

			String file = br.lines().collect(Collectors.joining("\n")); //convert java file to one string
			StringBuffer sb = new StringBuffer();
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(file);
			boolean foundChange = false;

			while(m.find()){
				if (m.group(1) == null){//there is no comment surrounded
					m.appendReplacement(sb, start + "\n" +m.group()+"\n" + end);
					foundChange = true;
					statementCount++;
				}else{//there is comment surrounded
					m.appendReplacement(sb, m.group());
				}
			}
			m.appendTail(sb);

			if (foundChange){
				try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);){
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
					System.out.println(filePath);
					System.out.println("file generated Successfully : " + ++classCount );
					System.out.println("comment added : " + statementCount);
				}
			}
			sb.setLength(0);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


