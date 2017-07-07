package task;

public class Test {

	/*
	 * Test case 1 : invalid statement
	 */
	import gov.state.uim.domain.data.ClaimApplicationEmployerDataBridge;
	
	objectAssembly.getFirstComponent(ClaimantDataBridge.class);
	
	//ClaimantData claimantData = (ClaimantData)objAssembly.getFirstComponent(ClaimantDataBridge.class);
	
	ClaimApplicationEmployerDataBridge clmAppEmprData = null;
	
	
	/*
	 * Test case 2 : one line statement, "DataBridge()" or "(DataBridge)" is at the first line of the match
	 */
	
	 ClaimantData cData = new ClaimantDataBridge()  ;
	
	ClaimantData clmtData = (ClaimantDataBridge) objAssembly
			.getFirstComponent(ClaimantDataBridge.class);
	
	ClaimApplicationEmployerDataBridge appEmployerData = (ClaimApplicationEmployerDataBridge) iterator
			.next();
	
	/*
	 * Test case 3 : two line statement, "DataBridge()" or "(DataBridge)" is at the second line of the match
	 */
	
	ClaimApplicationEmployerDataBridge claimAppEmpData = 
			(ClaimApplicationEmployerDataBridge) factFindingData
			.getClaimAppEmpData();
	
	

	
	

	
	

	

	
	

	
	
}
