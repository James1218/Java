/*
Already surrounded with the correct comment : ignore it if possible
Already surrounded with the incorrect comment : ignore it

import gov.state.uim.domain.data.ClaimApplicationEmployerDataBridge;


ClaimApplicationEmployerDataBridge claimAppEmpData = (ClaimApplicationEmployerDataBridge) factFindingData
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





 */

package task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class DataBridge {

	public static void main(String[] args) {
		
		String [] lines = {"\t\t private static void test(){", "ClaimantDataBridge claimantDataBridge) { "};
		for (String line : lines){
			System.out.println(validLine(line));
		}
		
	}
	
	private static String getStatement(String filePath){
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath));){
			String line = null;
			String preLine = null;//previous line
			String statement = null;
			while ((line = br.readLine()) != null){
				if (!validLine(line)){
					preLine = null;
					continue;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static void addAnotation(String statement){
		System.out.println(statement + "\n");
//		String regex = ".*?\\=";
	}
	
	private static boolean validLine(String line){
		String regex = "(\\w*(public|private|protected))|(.*DataBridge.*\\{.*)";
		return !Pattern.compile(regex).matcher(line).find();
	}

}
