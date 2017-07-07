package task;

public class Test {

	/*
	 * Test case 1 : invalid statement
	 */
	import gov.state.uim.domain.data.ClaimApplicationEmployerDataBridge;
	
	objectAssembly.getFirstComponent(ClaimantDataBridge.class);
	
	//ClaimantData claimantData = (ClaimantData)objAssembly.getFirstComponent(ClaimantDataBridge.class);
	
	ClaimApplicationEmployerDataBridge clmAppEmprData = null;
	
	Calendar basePeriodStartDate = new GregorianCalendar();
	
	// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

	
	/*
	 * Test case 2 : one line statement, "DataBridge()" or "(DataBridge)" is at the first line of the match
	 */
	
	ClaimantData cData_Test_2_1 = new ClaimantDataBridge()  ;
	
	ClaimantData clmtData_Test_2_2 = (ClaimantDataBridge) objAssembly
			.getFirstComponent(ClaimantDataBridge.class);
	
	ClaimApplicationEmployerDataBridge appEmployerData_Test_2_3 = (ClaimApplicationEmployerDataBridge) iterator
			.next();
			
			
		ClaimApplicationDataBridge claimAppData = null;
		if (ClaimApplicationTypeEnum.AIC.getName().equals(
				clmAppEmpData.getClaimApplicationData().getClaimAppType())) {
			claimAppData_Test_2_4 = (ClaimApplicationDataBridge) ClaimApplication
					.findClaimAppByPkId(
							clmAppEmpData.getClaimApplicationData().getPkId())
					.getClaimApplicationData();
			clmAppEmpData.setClaimApplicationData(claimAppData);
		} else if (null != clmAppEmpData.getClaimApplicationData()) {
			ClaimApplication claimApplicationBO = new ClaimApplication(
					clmAppEmpData.getClaimApplicationData());
			claimApplicationBO.saveOrUpdate();
		}
		
		
		@SuppressWarnings("unchecked")
		List<ClaimantDataBridge> claimantList_Test_2_5 = (List<ClaimantDataBridge>) super
				.executeQueryAsDynamicHandler(pstmt, this, "getResultsOffList");
		return claimantList;
	
	/*
	 * Test case 3 : two line statement, "DataBridge()" or "(DataBridge)" is at the second line of the match
	 */
	
	 ClaimApplicationEmployerDataBridge claimAppEmpData_Test_3_1 = 
			(ClaimApplicationEmployerDataBridge) factFindingData
			.getClaimAppEmpData()    ; 
	
	
	claimAppData_Test_3_2 = 
					(ClaimApplicationDataBridge) ClaimApplication
					.findClaimAppByPkId(
							clmAppEmpData.getClaimApplicationData().getPkId())
					.getClaimApplicationData();
	
	

	
	

	
	

	

	
	

	
	
}
