package gov.state.uim.cin.service;

import gov.state.uim.ApplicationProperties;
import gov.state.uim.GlobalConstants;
import gov.state.uim.TransientSessionConstants;
import gov.state.uim.ViewConstants;
import gov.state.uim.WorkflowProcessTemplateConstants;
import gov.state.uim.WorkflowProcessTemplateConstants.Monetary;
import gov.state.uim.app.AppealConstants;
import gov.state.uim.batch.BatchConstants;
import gov.state.uim.batch.cin.data.BatchCheckAlternateBasePeriodEligibilityBean;
import gov.state.uim.batch.cin.data.BatchPerformEUCProgramOptionCheckBean;
import gov.state.uim.common.ServiceLocator;
import gov.state.uim.dao.IClaimApplicationDAO;
import gov.state.uim.dao.IClaimDAO;
import gov.state.uim.dao.IClaimantDAO;
import gov.state.uim.dao.IClaimantPaymentDAO;
import gov.state.uim.dao.IGenericInquiryDAO;
import gov.state.uim.dao.IInsDAO;
import gov.state.uim.dao.IJdbcEmployerChargeDetailsDAO;
import gov.state.uim.dao.IJdbcQuestionnaireDAO;
import gov.state.uim.dao.IJdbcTransferEmployerDAO;
import gov.state.uim.dao.IProcessedWeeklyCertDAO;
import gov.state.uim.dao.IUnemploymentRateDao;
import gov.state.uim.dao.IWeeklyCertificationJrnlDAO;
import gov.state.uim.dao.factory.DAOFactory;
import gov.state.uim.dao.hibernate.EmployerContactDAO;
import gov.state.uim.domain.Appeal;
import gov.state.uim.domain.ApprissResponse;
import gov.state.uim.domain.BackDateRequest;
import gov.state.uim.domain.CWCOption;
import gov.state.uim.domain.ChargeInquiryTransactionBO;
import gov.state.uim.domain.Chargeability;
import gov.state.uim.domain.Claim;
import gov.state.uim.domain.ClaimApplication;
import gov.state.uim.domain.ClaimApplicationClaimant;
import gov.state.uim.domain.ClaimApplicationEmployer;
import gov.state.uim.domain.ClaimApplicationIb1;
import gov.state.uim.domain.ClaimWorkSearchContact;
import gov.state.uim.domain.Claimant;
import gov.state.uim.domain.Correspondence;
import gov.state.uim.domain.DocumentImageIndexBO;
import gov.state.uim.domain.DuaApplication;
import gov.state.uim.domain.DuaDeclaration;
import gov.state.uim.domain.DynamicBizConstant;
import gov.state.uim.domain.ES931Response;
import gov.state.uim.domain.EbDeclaration;
import gov.state.uim.domain.Employee;
import gov.state.uim.domain.Employer;
import gov.state.uim.domain.EmployerAccount;
import gov.state.uim.domain.EmployerUser;
import gov.state.uim.domain.FcccRequest;
import gov.state.uim.domain.FcccResponse;
import gov.state.uim.domain.FederalEmployer;
import gov.state.uim.domain.FicDestination;
import gov.state.uim.domain.FicLocation;
import gov.state.uim.domain.Form525B;
import gov.state.uim.domain.Hearing;
import gov.state.uim.domain.Ib4Request;
import gov.state.uim.domain.Ib4Response;
import gov.state.uim.domain.Ib5;
import gov.state.uim.domain.IbiqRequestBO;
import gov.state.uim.domain.Issue;
import gov.state.uim.domain.LateAppeal;
import gov.state.uim.domain.MassLayoff;
import gov.state.uim.domain.MilitaryEmployerBO;
import gov.state.uim.domain.MstBankDetails;
import gov.state.uim.domain.MultiClaimantEmployer;
import gov.state.uim.domain.NmonReconsiderationRequest;
import gov.state.uim.domain.ProcessedWeeklyCertBO;
import gov.state.uim.domain.RegisteredEmployer;
import gov.state.uim.domain.RegularClaim;
import gov.state.uim.domain.SharedWeeklyCert;
import gov.state.uim.domain.SharedWorkEmployee;
import gov.state.uim.domain.SharedWorkEmployer;
import gov.state.uim.domain.SidResponse;
import gov.state.uim.domain.SsnForMassLayoffs;
import gov.state.uim.domain.TaxInterceptAppeal;
import gov.state.uim.domain.TeucClaim;
import gov.state.uim.domain.TeucDeclaration;
import gov.state.uim.domain.TeucReachback;
import gov.state.uim.domain.UI512AResponse;
import gov.state.uim.domain.User;
import gov.state.uim.domain.Wage;
import gov.state.uim.domain.WeeklyCert;
import gov.state.uim.domain.WeeklyCertificationJrnlBO;
import gov.state.uim.domain.WingsReferralResult;
import gov.state.uim.domain.WorkSearchDetail;
import gov.state.uim.domain.bean.AddressBean;
import gov.state.uim.domain.bean.AvailableJobBean;
import gov.state.uim.domain.bean.BackDateRequestBean;
import gov.state.uim.domain.bean.CalendarBean;
import gov.state.uim.domain.bean.CinRequalificationCriteriaBean;
import gov.state.uim.domain.bean.ClaimantPeriodWageBean;
import gov.state.uim.domain.bean.DuaCrBbean;
import gov.state.uim.domain.bean.FlowBean;
import gov.state.uim.domain.bean.GeneralBean;
import gov.state.uim.domain.bean.IssueBean;
import gov.state.uim.domain.bean.JobTitleBean;
import gov.state.uim.domain.bean.LexisNexisIdentityVerificationOperationInputBean;
import gov.state.uim.domain.bean.LexisNexisPageFlowBean;
import gov.state.uim.domain.bean.LexisNexisRequestBean;
import gov.state.uim.domain.bean.LexisNexisResponseBean;
import gov.state.uim.domain.bean.MassLayoffWeeksClaimedBean;
import gov.state.uim.domain.bean.MessageHolderBean;
import gov.state.uim.domain.bean.MsEmployerInfoBean;
import gov.state.uim.domain.bean.MsEmployerPredicessorBean;
import gov.state.uim.domain.bean.QtrBean;
import gov.state.uim.domain.bean.RedeterminationOfClaimWageBean;
import gov.state.uim.domain.bean.ReverseDecisionBean;
import gov.state.uim.domain.bean.TaxInterceptInfoBean;
import gov.state.uim.domain.bean.VerifyEmployerBean;
import gov.state.uim.domain.bean.WeeklyCertJournalBean;
import gov.state.uim.domain.bean.WeeklyCertificationValidationDataBean;
import gov.state.uim.domain.data.AddressComponentData;
import gov.state.uim.domain.data.AicReopenData;
import gov.state.uim.domain.data.AppealAlternateAddressData;
import gov.state.uim.domain.data.AppealData;
import gov.state.uim.domain.data.AppealPartyData;
import gov.state.uim.domain.data.AverageUnemploymentRateData;
import gov.state.uim.domain.data.BackDateRequestData;
import gov.state.uim.domain.data.BusInterruptRenewalData;
import gov.state.uim.domain.data.CWCOptionData;
import gov.state.uim.domain.data.ChargeabilityData;
import gov.state.uim.domain.data.ClaimAlienData;
import gov.state.uim.domain.data.ClaimApplicationClaimantData;
import gov.state.uim.domain.data.ClaimApplicationClaimantDataBridge;
import gov.state.uim.domain.data.ClaimApplicationClaimantOSOCData;
import gov.state.uim.domain.data.ClaimApplicationData;
import gov.state.uim.domain.data.ClaimApplicationDataBridge;
import gov.state.uim.domain.data.ClaimApplicationEmployerData;
import gov.state.uim.domain.data.ClaimApplicationEmployerDataBridge;
import gov.state.uim.domain.data.ClaimApplicationIb1Data;
import gov.state.uim.domain.data.ClaimData;
import gov.state.uim.domain.data.ClaimWorkSearchContactData;
import gov.state.uim.domain.data.ClaimantAddressData;
import gov.state.uim.domain.data.ClaimantData;
import gov.state.uim.domain.data.ClaimantDataBridge;
import gov.state.uim.domain.data.ClaimantGeographicalPreferenceData;
import gov.state.uim.domain.data.ClaimantMatchingJobRequestData;
import gov.state.uim.domain.data.ClaimantOSOCData;
import gov.state.uim.domain.data.ClaimantPaymentData;
import gov.state.uim.domain.data.CommentData;
import gov.state.uim.domain.data.CorrWageInfoData;
import gov.state.uim.domain.data.CorrespondenceData;
import gov.state.uim.domain.data.DocumentImageIndexData;
import gov.state.uim.domain.data.DuaApplicationData;
import gov.state.uim.domain.data.DuaApplicationDataBridge;
import gov.state.uim.domain.data.DuaDeclarationData;
import gov.state.uim.domain.data.ES931ResponseData;
import gov.state.uim.domain.data.EbDeclarationData;
import gov.state.uim.domain.data.EmployeeData;
import gov.state.uim.domain.data.EmployerContactData;
import gov.state.uim.domain.data.EmployerData;
import gov.state.uim.domain.data.EmployerTransferData;
import gov.state.uim.domain.data.FCCCRequestData;
import gov.state.uim.domain.data.FCCCResponseData;
import gov.state.uim.domain.data.FederalEmployerData;
import gov.state.uim.domain.data.FicDestinationData;
import gov.state.uim.domain.data.Form525BData;
import gov.state.uim.domain.data.FormMa843Data;
import gov.state.uim.domain.data.HearingData;
import gov.state.uim.domain.data.Ib4RequestData;
import gov.state.uim.domain.data.Ib5Data;
import gov.state.uim.domain.data.IbiqRequestData;
import gov.state.uim.domain.data.IbiqResponseData;
import gov.state.uim.domain.data.InsResponseAuthData;
import gov.state.uim.domain.data.InsResponseData;
import gov.state.uim.domain.data.IssueData;
import gov.state.uim.domain.data.LateAppealData;
import gov.state.uim.domain.data.MassLayoffData;
import gov.state.uim.domain.data.MassLayoffWeeksData;
import gov.state.uim.domain.data.MiscAppealData;
import gov.state.uim.domain.data.MstAppDeterReasonCodeMaster;
import gov.state.uim.domain.data.MstBankDetailsData;
import gov.state.uim.domain.data.MstHandBook;
import gov.state.uim.domain.data.MstIssueMaster;
import gov.state.uim.domain.data.MstOsocTitle;
import gov.state.uim.domain.data.MultiClaimantEmployerData;
import gov.state.uim.domain.data.NmonReconsiderationRequestData;
import gov.state.uim.domain.data.OtherStateEmployerData;
import gov.state.uim.domain.data.PotentialIssueData;
import gov.state.uim.domain.data.PotentialNBYClaimData;
import gov.state.uim.domain.data.ProcessedWeeklyCertData;
import gov.state.uim.domain.data.RegisteredEmployerData;
import gov.state.uim.domain.data.RegularClaimData;
import gov.state.uim.domain.data.SharedWeeklyCertData;
import gov.state.uim.domain.data.SharedWorkEmployeeData;
import gov.state.uim.domain.data.SharedWorkEmployerData;
import gov.state.uim.domain.data.SidResponseData;
import gov.state.uim.domain.data.SsnForMassLayoffsData;
import gov.state.uim.domain.data.TaxAppealData;
import gov.state.uim.domain.data.TaxInterceptAppealData;
import gov.state.uim.domain.data.TeucDeclarationData;
import gov.state.uim.domain.data.TeucReachbackData;
import gov.state.uim.domain.data.UI512AResponseData;
import gov.state.uim.domain.data.UserData;
import gov.state.uim.domain.data.WageData;
import gov.state.uim.domain.data.WaitingWeekControlData;
//Cif_00323:End
import gov.state.uim.domain.data.WeeklyCertBackweekData;
import gov.state.uim.domain.data.WeeklyCertData;
import gov.state.uim.domain.data.WeeklyCertDataBridge;
import gov.state.uim.domain.data.WingsReferralResultData;
import gov.state.uim.domain.data.WorkitemDetailData;
import gov.state.uim.domain.decision.appeals.AppealsDecisionLettersParagraphBuilder;
import gov.state.uim.domain.enums.AddressTypeEnum;
import gov.state.uim.domain.enums.AlienStatusEnum;
import gov.state.uim.domain.enums.AppealPartyTypeEnum;
import gov.state.uim.domain.enums.AppealStatusEnum;
import gov.state.uim.domain.enums.AppealTypeEnum;
import gov.state.uim.domain.enums.ApplicationStatusEnum;
import gov.state.uim.domain.enums.CertificationModeEnum;
import gov.state.uim.domain.enums.CinRequalificationCriteriaEnum;
import gov.state.uim.domain.enums.ClaimAppFlowTypeEnum;
import gov.state.uim.domain.enums.ClaimApplicationTypeEnum;
import gov.state.uim.domain.enums.ClaimEmploymentChargeStatusEnum;
import gov.state.uim.domain.enums.ClaimEntitlementTypeEnum;
import gov.state.uim.domain.enums.ClaimStatusEnum;
import gov.state.uim.domain.enums.ClaimTypeEnum;
import gov.state.uim.domain.enums.CorrespondenceCodeEnum;
import gov.state.uim.domain.enums.CorrespondenceCodeTaxEnum;
import gov.state.uim.domain.enums.CorrespondenceDirectionEnum;
import gov.state.uim.domain.enums.CorrespondenceModeEnum;
import gov.state.uim.domain.enums.CorrespondenceParameterDescriptionEnum;
import gov.state.uim.domain.enums.CorrespondenceParameterEnum;
import gov.state.uim.domain.enums.DecisionCodeEnum;
import gov.state.uim.domain.enums.DismissalReasonEnum;
import gov.state.uim.domain.enums.ES931ReasonOfSeparationEnum;
import gov.state.uim.domain.enums.EmployerRegistrationStatusEnum;
import gov.state.uim.domain.enums.EmployerTypeEnum;
import gov.state.uim.domain.enums.EmployerUnitStatusEnum;
import gov.state.uim.domain.enums.EntitlementTypeEnum;
import gov.state.uim.domain.enums.Form525BTypeEnum;
import gov.state.uim.domain.enums.FunctionsEnum;
import gov.state.uim.domain.enums.InterceptRefundTypeEnum;
import gov.state.uim.domain.enums.IssueCategoryEnum;
import gov.state.uim.domain.enums.IssueSourceEnum;
import gov.state.uim.domain.enums.IssueSubCategoryEnum;
import gov.state.uim.domain.enums.IssueTypeEnum;
import gov.state.uim.domain.enums.JobListingDegreeCodeEnum;
import gov.state.uim.domain.enums.LexisNexisFlowEnum;
import gov.state.uim.domain.enums.LexisNexisTransactionResultType;
import gov.state.uim.domain.enums.MiscAppIssueCategoryEnum;
import gov.state.uim.domain.enums.MiscAppealLevelEnum;
import gov.state.uim.domain.enums.MiscAppealTypeEnum;
import gov.state.uim.domain.enums.MonetaryRedeterminationReasonEnum;
import gov.state.uim.domain.enums.MstDeterminationTypeEnum;
import gov.state.uim.domain.enums.NmonReconsiderationStatusEnum;
import gov.state.uim.domain.enums.ProgramTypeEnum;
import gov.state.uim.domain.enums.QuestionnaireEnum;
import gov.state.uim.domain.enums.ReasonCodeEnum;
import gov.state.uim.domain.enums.ReasonForSeperationEnum;
import gov.state.uim.domain.enums.SIDProcessNameEnum;
import gov.state.uim.domain.enums.SeparationReasonEnum;
import gov.state.uim.domain.enums.SsaStatusEnum;
import gov.state.uim.domain.enums.TeucClaimTypeEnum;
import gov.state.uim.domain.enums.UserTypeEnum;
import gov.state.uim.domain.enums.WeekProcessingReasonEnum;
import gov.state.uim.domain.enums.WeeklyTransactionTypeEnum;
import gov.state.uim.domain.enums.WingsReferralResultTypeEnum;
import gov.state.uim.domain.enums.WorkFlowOperationsEnum;
import gov.state.uim.domain.enums.WorkSearchReasonEnum;
import gov.state.uim.domain.enums.YesNoTypeEnum;
import gov.state.uim.domain.rules.fact.claim.MassLayoffRuleResponse;
import gov.state.uim.domain.rules.fact.claim.MasslayoffclaimFact;
import gov.state.uim.framework.cache.CacheUtility;
import gov.state.uim.framework.cache.HolidayCache;
import gov.state.uim.framework.exception.BaseApplicationException;
import gov.state.uim.framework.exception.BaseRunTimeException;
import gov.state.uim.framework.logging.log4j.AccessLogger;
import gov.state.uim.framework.reqtrace.cif_wy;
import gov.state.uim.framework.rule.module.IFactModel;
import gov.state.uim.framework.rule.module.IFactResponseModel;
import gov.state.uim.framework.search.GenericSearchBean;
import gov.state.uim.framework.search.GenericSearchService;
import gov.state.uim.framework.service.BaseService;
import gov.state.uim.framework.service.Context;
import gov.state.uim.framework.service.IObjectAssembly;
import gov.state.uim.framework.webservices.WebServicesConstants;
import gov.state.uim.framework.workflow.WorkflowConstants;
import gov.state.uim.framework.workflow.bean.GenericWorkflowSearchBean;
import gov.state.uim.framework.workflow.bean.WorkflowItemBean;
import gov.state.uim.interfaces.InterfaceFactory;
import gov.state.uim.interfaces.autocoder.Autocoder;
import gov.state.uim.interfaces.autocoder.AutocoderRequestBean;
import gov.state.uim.interfaces.autocoder.ReturnWS;
import gov.state.uim.interfaces.ins.IIns;
import gov.state.uim.interfaces.jobListing.JobListing;
import gov.state.uim.interfaces.lexisnexis.webservice.LexisNexisWebServiceInvoker;
import gov.state.uim.interfaces.service.IconService;
import gov.state.uim.interfaces.wings.ClaimantJobRequestMessage;
import gov.state.uim.interfaces.wings.data.ClaimantJobRequestData;
import gov.state.uim.multistate.BaseOrStateEnum;
import gov.state.uim.multistate.MultiStateClassFactory;
import gov.state.uim.payment.PaymentConstants;
import gov.state.uim.qe.domain.QuestionnaireBO;
import gov.state.uim.rules.instance.GetDroolsInstance;
import gov.state.uim.users.service.ClaimantRegService;
import gov.state.uim.util.BenefitsConstants;
import gov.state.uim.util.lang.DateFormatUtility;
import gov.state.uim.util.lang.DateUtility;
import gov.state.uim.util.lang.StringUtility;
import gov.state.uim.wc.service.BIWeeklyCertService;
import gov.state.uim.wc.service.WeeklyCertService;
import gov.state.uim.workflow.transaction.WorkflowTransactionService;

import java.math.BigDecimal;
import java.math.BigInteger;
//CIF_00323:Start
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//CIF_00323:End
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jobsmogov.JobSearchResultsStruct;
//CIF_00323:Start




import org.apache.commons.lang.StringUtils;

/**
 * This class provides methods related to Claim Intake process.
 * 
 * @author Tata Consultancy Services
 * @version 1.0
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class CinService extends BaseService implements BICinService {
	private static final AccessLogger LOGGER = ServiceLocator.instance
			.getLogger(CinService.class);

	/*
	 * Object stateSpecificBO = MultiStateClassFactory.getObject(this.getClass()
	 * .getName(), BaseOrStateEnum.STATE, null, null, Boolean.TRUE);
	 */

	public CinService() {
		super();
	}

	/**
	 * This method works with the table T_CLAIM_APPLICATION; It takes
	 * ObjectAssembly with ClaimantApplicationData as input and returns the
	 * ObjectAssembly with ClaimApplicationData object matching the SSN, or if
	 * there is no record for given SSN a newly created record will be returned.
	 * 
	 * @param objAssembly
	 *            with SSN
	 * @return objAssembly with ClaimantApplicationData where the Claimant's SSN
	 *         match or new object
	 */

	public IObjectAssembly getOrCreateClaimApp(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getOrCreateClaimApp");
		}

		// Modified 20060421 by ben to allow for claimants to bypass the
		// EnterSsn screen. If nothing is passed in for the SSN, it means
		// the user is not a claimant, and she/he is on the ClaimAdvisement
		// screen. Otherwise, the user is either on the EnterSSN
		// screen or she/he is Claimant on the ClaimAdvisement screen,
		// retrieving corresponding ClaimApplicationData object.
		if (StringUtility.isNotBlank(objAssembly.getSsn())) {
			String ssn = objAssembly.getSsn();

			// To create and retrieve ClaimApplicationData object
			ClaimApplication claimAppBO = null;
			try {
				DuaCrBbean duabean = (DuaCrBbean) objAssembly
						.getFirstBean(DuaCrBbean.class);
				// If the claimant has indicated that he is affected by disaster
				// and the CSR as not selected
				// interstate filing mode and the count of employment or county
				// of residence prior to disaster was
				// part of DUA declaration, set the claim effective date as the
				// DUA Start DAte.
				if (duabean != null
						&& ViewConstants.YES.equals(duabean
								.getAffetcedByDisaster())
						&& (!ViewConstants.DUA_FILING_MODE_INTERSTATE
								.equals(duabean.getFilingMode()))
						&& duabean.isBackDateDua()) {

					Date wkStartDate = Claim.determineClaimEffectiveDate()
							.getTime();
					if (UserTypeEnum.MDES.getName().equals(
							objAssembly.getUserContext().getUserType())) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(wkStartDate);
						cal.add(Calendar.DATE, -7);
						wkStartDate = cal.getTime();
					}
					// CIF_03330 || Defect_5147
					claimAppBO = ClaimApplication.createOrFindClaimAppBySsn(
							ssn, Claim.determineClaimEffectiveDate().getTime(),
							wkStartDate, objAssembly);
				} else {
					Date wkStartDate = Claim.determineClaimEffectiveDate()
							.getTime();
					if (UserTypeEnum.MDES.getName().equals(
							objAssembly.getUserContext().getUserType())) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(wkStartDate);
						cal.add(Calendar.DATE, -7);
						wkStartDate = cal.getTime();
					}

					// Check for TEUC declaration for a new claim. Not for NBY
					// claim.
					boolean teucFlag = false;
					boolean ebFlag = false;
					if (!YesNoTypeEnum.NumericYes.getName().equals(
							objAssembly.getData("NBY_INDICATOR"))
							&& !YesNoTypeEnum.NumericYes.getName().equals(
									objAssembly.getData("MASSLAYOFF_FLAG"))) {
						TeucDeclaration teucDecln = TeucDeclaration
								.getDeclaration();
						if (teucDecln != null) {
							TeucDeclarationData teucDeclnData = teucDecln
									.getTeucDeclarationData();
							if (teucDeclnData != null) {
								// Check if SSN present in reach back table
								TeucReachback reachbackBO = TeucReachback
										.findTeucReachbackBySsn(
												ssn,
												teucDeclnData
														.getTeucDeclarationId(),
												TeucClaimTypeEnum.TIER_3
														.getName());
								if (reachbackBO != null) {
									TeucReachbackData reachbackData = reachbackBO
											.getTeucReachbackData();
									// Check if filing date is not past current
									// date
									if (reachbackData.getFilingEndDate().after(
											new Date())) {
										Date teucTier3EffectiveDate = Claim
												.getBizConstant()
												.getValueAsDate(
														"TIER_3_EFFECTIVE_DATE");
										claimAppBO = ClaimApplication
												.createOrFindClaimAppBySsn(ssn,
														teucTier3EffectiveDate,
														wkStartDate,
														objAssembly);
										objAssembly.addComponent(teucDeclnData,
												true);
										teucFlag = true;
									}
								}
							}
						}

						if (!teucFlag) {
							// Check for EB declaration for a new claim. Not for
							// NBY claim.
							EbDeclaration ebDecln = EbDeclaration
									.getDeclaration();
							if (ebDecln != null) {
								EbDeclarationData ebcDeclnData = ebDecln
										.getEbDeclarationData();
								if (ebcDeclnData != null) {
									// Check if SSN present in reachback table
									TeucReachback reachbackBO = TeucReachback
											.findReachbackBySsn(
													ssn,
													ebcDeclnData
															.getEbDeclarationId());
									if (reachbackBO != null) {
										TeucReachbackData reachbackData = reachbackBO
												.getTeucReachbackData();
										// Check if filing date is not past
										// current date
										if (reachbackData.getFilingEndDate()
												.after(new Date())) {
											claimAppBO = ClaimApplication
													.createOrFindClaimAppBySsn(
															ssn,
															ebcDeclnData
																	.getEbStartDate(),
															wkStartDate,
															objAssembly);
											objAssembly.addComponent(
													ebcDeclnData, true);
											ebFlag = true;
										}
									}
								}
							}
						}
					}

					if (!ebFlag && !teucFlag) {
						if (YesNoTypeEnum.NumericYes.getName().equals(
								objAssembly.getData("NBY_INDICATOR"))) {
							Date nbyEffDateFromMF = (Date) objAssembly
									.getData("NBYEFFECTIVEDATE");
							claimAppBO = ClaimApplication
									.createOrFindClaimAppBySsn(ssn,
											nbyEffDateFromMF, wkStartDate,
											objAssembly);
						} else if (YesNoTypeEnum.NumericYes.getName().equals(
								objAssembly.getData("MASSLAYOFF_FLAG"))) {
							MassLayoffData massLayoffData = (MassLayoffData) objAssembly
									.getFirstComponent(MassLayoffData.class);
							claimAppBO = ClaimApplication
									.createOrFindClaimAppBySsn(
											ssn,
											massLayoffData
													.getMassLayoffClaimEffDate(),
											wkStartDate, objAssembly);
						} else {
							claimAppBO = ClaimApplication
									.createOrFindClaimAppBySsn(ssn, Claim
											.determineClaimEffectiveDate()
											.getTime(), wkStartDate,
											objAssembly);
						}
					}
				}
			} catch (BaseApplicationException bae) {
				Object[] obj = new Object[1];
				obj[0] = ssn;
				String message = bae.getMessage();
				if (YesNoTypeEnum.NumericYes.getName().equals(
						objAssembly.getData("MASSLAYOFF_FLAG"))) {
					if (message
							.equalsIgnoreCase("error.access.getorcreateclaimapp.pendingclaimexists")) {
						message = "error.access.getorcreateclaimapp.pendingclaimexists.massLayOff";
					} else if (message
							.equalsIgnoreCase("error.access.getorcreateclaimapp.zerooroneweektobefilled")) {
						message = "error.access.getorcreateclaimapp.zerooroneweektobefilled.massLayOff";
					} else if (message
							.equalsIgnoreCase("error.access.getorcreateclaimapp.invalidssn")) {
						message = "error.access.getorcreateclaimapp.invalidssn.massLayOff";
					}
				}
				objAssembly.addBusinessError(message, obj);
			}

			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
			// mddrNo="", impactPoint="Start")
			objAssembly.removeComponent(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
			// mddrNo="", impactPoint="End")
			if (claimAppBO != null) {
				ClaimApplicationData claimAppData = claimAppBO
						.getClaimApplicationData();

				objAssembly.addComponent(claimAppData);
				if (claimAppData.getClaimApplicationClaimantData() != null) {
					objAssembly.addComponent(claimAppData
							.getClaimApplicationClaimantData());
				}
				if (claimAppData.getDuaApplicationData() != null) {
					// to lad lazy values.
					claimAppData.getDuaApplicationData().getCountyWork();
					DuaApplication duaAppBO = DuaApplication
							.findDuaAppByClaimAppId(claimAppData.getPkId());
					objAssembly.addComponent(duaAppBO.getDuaApplicationData(),
							true);
				}
			}
		}

		// objAssembly = getDuaDeclaration(objAssembly);//Added for CR

		return objAssembly;
	}

	/**
	 * 
	 * @param objAssembly
	 * @return
	 */
	public IObjectAssembly getDuaDeclarationListForToday(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDuaDeclarationListForToday");
		}
		// CIF_01889
		// Defect_2105
		List duaList = null;
		if (getUserContext().getUserType().equals(UserTypeEnum.CLMT.getName())) {
			duaList = DuaDeclaration.findDuaDeclarationForDate(new Date());// find
																			// list
																			// for
																			// current
																			// date
		} else {
			// CSR
			duaList = DuaDeclaration
					.findDuaDeclarationForDate(new Date(), true);
		}
		if (duaList != null && !duaList.isEmpty()) {
			objAssembly.addComponentList(duaList, true);
		}
		return objAssembly;

	}

	/**
	 * 
	 */
	public IObjectAssembly createClaimAppForClaimFiling(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createClaimAppForClaimFiling");
		}// seperate method for clarity

		// add User Data for updating email address now moved to LDAP
		User userBO = User.findByUserId(objAssembly.getUserContext()
				.getUserId());
		if (userBO != null) {
			objAssembly.addComponent(userBO.getUserData(), true);
		}

		// For claimant login we need check the previous claims so going to call
		// separate method for clarity
		String clmtVerifyPreviousClaim = (String) objAssembly
				.getData("CLMT_VERIFY_PREVIOUS_CLAIM");
		if (GlobalConstants.ANSWER_YES
				.equalsIgnoreCase(clmtVerifyPreviousClaim)) {
			return findPreviousClaimDetails(objAssembly);
		}

		return getOrCreateClaimApp(objAssembly);

	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input and
	 * and saves it, or updates it if it is already exists.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateClaimApp(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClaimApp");
		}

		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class, true);

		/*
		 * Payment Defect_41 start If coming from activate claim=>interveing
		 * employment screen claimapp data will not be null but the object will
		 * not have data in it
		 */
		// boolean fromActivateClaim = false;
		if (claimAppData != null && claimAppData.getSsn() == null) {
			WeeklyCertificationValidationDataBean weekCertValidationBean = (WeeklyCertificationValidationDataBean) objAssembly
					.getFirstBean(WeeklyCertificationValidationDataBean.class);

			claimAppData = (ClaimApplicationDataBridge) weekCertValidationBean
					.getClaimData().getClaimApplicationData();
			// fromActivateClaim = true;
		}

		ClaimApplication claimAppBO = new ClaimApplication(claimAppData);
		claimAppBO.saveOrUpdate();

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationIb1Data as input
	 * and and saves it, or updates it if it is already exists
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationIb1Data
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateClaimAppIb1(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClaimAppIb1");
		}

		ClaimApplicationIb1Data claimAppIb1Data = (ClaimApplicationIb1Data) objAssembly
				.fetchORCreate(ClaimApplicationIb1Data.class);

		ClaimApplicationIb1 claimAppIb1BO = new ClaimApplicationIb1(
				claimAppIb1Data);
		claimAppIb1BO.saveOrUpdate();

		// CorrespondenceData corrData = (CorrespondenceData)
		// objAssembly.getFirstComponent(CorrespondenceData.class);

		// Correspondence corrBO = new Correspondence(corrData);

		// corrBO.saveOrUpdate();

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SSN and ClaimApplicationData
	 * object, saves ClaimApplicationData object, and searching for SSN in mass
	 * layoff lists and setting corresponding flag.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with MASSLAYOFF_FLAG, and OtherStateEmployerData
	 */
	public IObjectAssembly processDetermineState(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processDetermineState");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// To save or update ClaimApplicationData
		objAssembly = this.saveOrUpdateClaimApp(objAssembly);

		// To save or update ClaimApplicationData,
		// or delete DuaApplicationData
		if (ViewConstants.YES.equals(clmAppData.getClaimTypeDua())) {
			// There is no need to save the dua app as it is already save in
			// previous screens.
			// objAssembly = this.saveOrUpdateDuaApplication(objAssembly);
		} else {
			objAssembly = this.deleteDuaAppByClmAppId(objAssembly);
		}

		// To check the existance of corresponding MassLayoffData
		if (ViewConstants.YES.equals(clmAppData.getClaimTypeReturnToWork())) {
			objAssembly = this.getSsnForMassLayoffsDataBySsn(objAssembly);
		} else {
			objAssembly.addData("MASSLAYOFF_FLAG", "0");
		}

		// To get OtherStateEmployerData, if flow is IB-1
		/*
		 * FlowBean flowBean = (FlowBean) objAssembly
		 * .fetchORCreateBean(FlowBean.class);
		 * 
		 * if (flowBean.isDisplayIb1()) { Object[] array =
		 * clmAppData.getClaimTypeStates().toArray(); Employer emprBO = Employer
		 * .findOsaEmployerByState((String) array[0]);
		 * 
		 * objAssembly.removeComponent(OtherStateEmployerData.class); if (emprBO
		 * != null) { objAssembly.addComponent((OtherStateEmployerData) emprBO
		 * .getEmployerData()); } }
		 * 
		 * boolean msFlag = false; if ((clmAppData.getClaimTypeStates() != null)
		 * && (clmAppData.getClaimTypeStates().size()==2)){ for (Iterator i =
		 * clmAppData.getClaimTypeStates().iterator(); i.hasNext();){ String
		 * state = (String)i.next(); if
		 * (ViewConstants.HOME_STATE.equals(state)){ msFlag = true; break; } } }
		 * if (msFlag ){ if ((clmAppData.getClaimTypeStates() != null) &&
		 * (clmAppData.getClaimTypeStates().size()==2)){ for (Iterator i =
		 * clmAppData.getClaimTypeStates().iterator(); i.hasNext();){ String
		 * state = (String)i.next(); if
		 * (!ViewConstants.HOME_STATE.equals(state)){ OtherState otherState =
		 * OtherState.getStateInfoById(state);
		 * objAssembly.addComponent((OtherStateEmployerData) otherState
		 * .getEmployerData(),true); break; } } } }
		 */
		// To save or update ClaimApplicationData
		objAssembly = this.loadBaseperiodWage(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SSN, searches for MassLayoffData,
	 * and sets corresponding flag based on the search.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with MASSLAYOFF_FLAG
	 */
	public IObjectAssembly getSsnForMassLayoffsDataBySsn(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSsnForMassLayoffsDataBySsn");
		}

		String ssn = objAssembly.getSsn();
		MassLayoff mlBO = MassLayoff.getMassLayoffsDataBySsn(ssn);

		if (mlBO != null) {
			objAssembly.addData("MASSLAYOFF_FLAG", "1");
		} else {
			objAssembly.addData("MASSLAYOFF_FLAG", "0");
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with pkId and SSN, searches for
	 * ClaimantData based on pkId or SSN,and returns ClaimantData key exist then
	 * this is the perferred method
	 * 
	 * @param objAssembly
	 *            with pkId and SSN
	 * @return objAssembly with ClaimantData
	 */

	public IObjectAssembly getClaimantData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimantData");
		}

		Long pkId = (Long) objAssembly.getPrimaryKey();
		Claimant claimantBO = null;

		if (pkId != null) {
			claimantBO = Claimant.findByPrimaryKey(pkId);
		} else {
			String ssn = objAssembly.getSsn();
			claimantBO = Claimant.findBySsn(ssn);
		}

		objAssembly.removeComponent(ClaimantDataBridge.class);
		if (claimantBO != null) {
			objAssembly.addComponent(claimantBO.getClaimantData());
		}

		return objAssembly;
	}

	/**
	 * This takes ObjectAssembly with primary key of ClaimData object as input
	 * and returns ObjectAssembly with ClaimData retrieved from database.
	 * 
	 * @param objAssembly
	 *            with primary key of ClaimData
	 * @return objAssembly with ClaimData object
	 */
	public IObjectAssembly getClaimData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimData");
		}

		Long pkId = objAssembly.getPrimaryKeyAsLong(ClaimData.class);

		objAssembly.removeComponent(ClaimData.class);

		if ((pkId != null) && (pkId.longValue() > 0)) {
			Claim claimBO = Claim.findByPrimaryKey(pkId);

			if (claimBO != null) {
				objAssembly.addComponent(claimBO.getClaimData(), true);
			}
		}

		return objAssembly;
	}

	/**
	 * This takes ObjectAssembly with OtherStateEmployerData object as input and
	 * returns ObjectAssembly with EmployerContactData object retrieved from
	 * database.
	 * 
	 * @param objAssembly
	 *            with OtherStateEmployerData object
	 * @return objAssembly with EmployerContactData object
	 */
	public IObjectAssembly getEmployerContactDataByState(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getEmployerContactDataByState");
		}

		OtherStateEmployerData empData = (OtherStateEmployerData) objAssembly
				.fetchORCreate(OtherStateEmployerData.class);

		EmployerContactData data = Employer
				.getEmployerContactDataByState(empData.getState());

		objAssembly.removeComponent(EmployerContactData.class);
		if (data != null) {
			objAssembly.addComponent(data);
		}

		return objAssembly;
	}

	/**
	 * This takes ObjectAssembly with ClaimApplicationData object as input and
	 * returns ObjectAssembly with ClaimApplicationIb1Data object retrieved from
	 * database.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData object
	 * @return objAssembly with ClaimApplicationIb1Data object
	 */
	public IObjectAssembly getClaimAppIb1(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimAppIb1");
		}

		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		ClaimApplicationIb1 claimAppIb1BO = ClaimApplicationIb1
				.getClaimAppIb1(claimAppData.getPkId());

		objAssembly.removeComponent(ClaimApplicationIb1Data.class);
		if (claimAppIb1BO != null) {
			objAssembly
					.addComponent(claimAppIb1BO.getClaimApplicationIb1Data());
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationClaimantData and
	 * ClaimApplicationData as input and and saves them, or updates them if they
	 * already exist.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationClaimantData, and ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateClmAppClmnt(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClmAppClmnt");
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		Object lObject = MultiStateClassFactory.getObject(this.getClass()
				.getName(), BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

		if (null != lObject) {

			((CinService) lObject).setStateSpecificClmAppClmnt(clmAppClmntData,
					clmAppData);
		} else {

			setStateSpecificClmAppClmnt(clmAppClmntData, clmAppData);

		}
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		clmAppBO.saveOrUpdate();

		return objAssembly;
	}

	/*
	 * 
	 * <H3>Code is refactored, to set values -</H3>
	 * 
	 * 
	 * @author 341231
	 * 
	 * @param clmAppClmntData
	 * 
	 * @param clmAppData
	 */
	@cif_wy(storyNumber = "P1-CM-005", requirementId = "FR_1614", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.8.4", dcrNo = "DCR_MDDR_9, DCR_MDDR_10", mddrNo = "", impactPoint = "")
	protected void setStateSpecificClmAppClmnt(
			ClaimApplicationClaimantDataBridge clmAppClmntData,
			ClaimApplicationData clmAppData) {
		clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input and
	 * returns ObjectAssembly with list of employers for base period
	 * (missingFlag == 0) based on employee's SSN and Last Name.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with list of ClaimApplicationEmployerData
	 */
	public IObjectAssembly getBaseEmployers(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getBaseEmployers");
		}
		Calendar basePeriodStartDate = new GregorianCalendar();
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// to return the list of Wage Data object for particular person
		// (based on SSN)
		// CQ R4UAT00015390 on 06/06/2011
		if (ClaimApplicationTypeEnum.AIC.getName().equals(
				clmAppData.getClaimAppType())) {
			// changing the below date to next qtr
			// basePeriodStartDate = clmAppData.getBpStartDateDisplay();
			basePeriodStartDate = clmAppData.getBpStartDateDisplayOnScreen();
		} else {
			basePeriodStartDate = clmAppData.getBpStartDate();
		}

		List wageList = Employer.findNotUsedWageListForBasePeriod(
				objAssembly.getSsn(), basePeriodStartDate,
				new GregorianCalendar());

		List regWageList = new ArrayList();

		if (wageList != null && !wageList.isEmpty()) {
			for (Iterator itr = wageList.iterator(); itr.hasNext();) {
				WageData wageData = (WageData) itr.next();
				Employee empBO = Employee.findByEmployeeId(wageData
						.getEmployeeData().getEmployeeId());
				if (EmployerTypeEnum.REG.getName().equals(
						empBO.getEmployeeData().getEmployerData()
								.getEmployerType())) {
					regWageList.add(wageData);
				}
			}
		}

		objAssembly.removeComponent(WageData.class);
		if ((regWageList != null) && (regWageList.size() > 0)) {
			objAssembly.addComponentList(regWageList);
		}

		// to create the list of Claim Application Employer Data objects
		List tempList = Employer.getBasePeriodClaimAppEmprList(regWageList,
				clmAppData);

		List clmAppEmpList = new ArrayList();
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="Start")
		if ((tempList != null) && (tempList.size() > 0)) {
			for (Iterator i = tempList.iterator(); i.hasNext();) {
				ClaimApplicationEmployerDataBridge data = (ClaimApplicationEmployerDataBridge) i
						.next();
				if (!ViewConstants.YES.equals(data.getMissingFlag())) {
					clmAppEmpList.add(data);
				}
			}
		}
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="End")

		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);
		if ((clmAppEmpList != null) && (clmAppEmpList.size() > 0)) {
			objAssembly.addComponentList(clmAppEmpList);
		}

		// to create the list of Wage Data bens for the specified Claim
		// Application Employers
		this.getBasePeriodWageList(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input and
	 * returns ObjectAssembly with ClaimApplicationEmployerData list based on
	 * ClaimApplicationData
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with list of ClaimApplicationEmployerData
	 */
	public IObjectAssembly getAllEmployers(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getAllEmployers");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		List empList = ClaimApplicationEmployer
				.findClmAppEmpListByClmAppId(clmAppData.getPkId());

		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);
		if ((empList != null) && (empList.size() > 0)) {
			objAssembly.addComponentList(empList);
		}
		objAssembly.addData("HB_LAW_ENABLED",
				DynamicBizConstant.isHb150SevLawEnabled(new Date()));
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with the list of
	 * ClaimApplicationEmployerData objects as input (with only set the value of
	 * primary keys), and returns ObjectAssembly with
	 * ClaimApplicationEmployerData list based on primary keys.
	 * 
	 * @param objAssembly
	 *            with list of ClaimApplicationEmployerData
	 * @return objAssembly with list of ClaimApplicationEmployerData
	 */
	public IObjectAssembly getClaimAppEmpListById(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimAppEmpListById");
		}

		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="Start")
		List listEmp = objAssembly
				.getComponentList(ClaimApplicationEmployerDataBridge.class);

		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="End")

		if ((listEmp == null)) {
			return objAssembly;
		}

		List listRet = new ArrayList();

		for (Iterator i = listEmp.iterator(); i.hasNext();) {

			// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
			// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
			// designDocSection="1.2", dcrNo="DCR_MDDR_6",
			// mddrNo="",impactPoint="Start")

			ClaimApplicationEmployerDataBridge clmAppEmp = (ClaimApplicationEmployerDataBridge) i
					.next();
			// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
			// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
			// designDocSection="1.2", dcrNo="DCR_MDDR_6",
			// mddrNo="",impactPoint="End")

			ClaimApplicationEmployer clmAppEmpBO = ClaimApplicationEmployer
					.findByPrimaryKey(clmAppEmp.getPkId());

			if (clmAppEmpBO != null) {
				listRet.add(clmAppEmpBO.getClaimAppEmpData());
			}
		}

		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);
		if ((listRet != null) && (listRet.size() > 0)) {
			objAssembly.addComponentList(listRet);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData object as
	 * input, and returns ObjectAssembly with list of DuaDeclarationData, and
	 * PotentialIssueData.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with DuaApplicationData, list of DuaDeclarationData,
	 *         and list of PotentialIssueData
	 */
	public IObjectAssembly getDuaDeclaration(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDuaDeclaration");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// To search for corresponding DuaApplicationData
		DuaApplication duaAppBO = DuaApplication
				.findDuaAppByClaimAppId(clmAppData.getPkId());

		objAssembly.removeComponent(DuaApplicationDataBridge.class);
		if (duaAppBO != null) {
			objAssembly.addComponent(duaAppBO.getDuaApplicationData());
		}

		// To search for list of DuaDeclarationData
		objAssembly.removeComponent(DuaDeclarationData.class);
		Date curDate = clmAppData.getClaimEffectiveDate();
		List list = DuaDeclaration.findDuaDeclarationForDate(curDate);
		if ((list != null) && (list.size() > 0)) {
			objAssembly.addComponentList(list);
		}

		// To search for list of PotentialIssueData
		List listPotentialIssue = ClaimApplication
				.getPotentialIssueListByClaimAppId(clmAppData.getPkId());

		objAssembly.removeComponent(PotentialIssueData.class);
		objAssembly.addComponentList(listPotentialIssue);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData object as
	 * input, and returns ObjectAssembly with ClaimApplicationClaimantdata.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with ClaimApplicationClaimantData
	 */
	public IObjectAssembly getClaimAppClaimant(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimAppClaimant");
		}

		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		ClaimApplicationClaimant claimAppClmntBO = ClaimApplicationClaimant
				.findByClaimAppId(claimAppData.getPkId());

		Claimant clmtData = Claimant.findBySsn(objAssembly.getSsn());

		if (clmtData != null) {
			objAssembly.addComponent(clmtData.getClaimantData());
		}

		objAssembly.removeComponent(ClaimApplicationClaimantDataBridge.class);
		if (claimAppClmntBO != null) {
			objAssembly.addComponent(claimAppClmntBO
					.getClaimApplicationClaimantData());
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SSN as input, retrieves or creates
	 * corresponding ClaimApplicationData, and returns ObjectAssembly with
	 * ClaimApplicationData.
	 * 
	 * @param objAssembly
	 *            with SSN
	 * @return objAssembly with ClaimApplicationData
	 */
	public IObjectAssembly getEsClaimAppClaimant(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getEsClaimAppClaimant");
		}

		// modified 20060421 by ben to allow for claimants to bypass the
		// EsEnterSsn screen.
		// if nothing is passed in for the ssn, it means they're not a claimant
		// and they're
		// in the LaunchEsAction flow. else they're either on the EsEnterSsn
		// screen or
		// they're claimants in the LaunchEsAction flow, retrieving their
		// ClaimApplicationClaimantData object.

		if (StringUtility.isNotBlank(objAssembly.getSsn())) {

			Date wkStartDate = Claim.determineClaimEffectiveDate().getTime();
			if (UserTypeEnum.MDES.getName().equals(
					objAssembly.getUserContext().getUserType())) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(wkStartDate);
				cal.add(Calendar.DATE, -7);
				wkStartDate = cal.getTime();
			}

			ClaimApplication claimApp = ClaimApplication
					.createOrFindEsClaimAppBySsn(objAssembly.getSsn(),
							wkStartDate);

			if (claimApp != null) {
				objAssembly.addComponent(claimApp.getClaimApplicationData());
			}// claimant
		}
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationEmployerData (only
	 * primary key is set) as input, and returns ObjectAssembly with
	 * ClaimApplicationEmployerData.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly with ClaimApplicationEmployerData
	 */
	public IObjectAssembly getClaimApplicationEmployer(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimApplicationEmployer");
		}

		ClaimApplicationEmployer clmAppEmpBO = ClaimApplicationEmployer
				.findByPrimaryKey(objAssembly.getPrimaryKeyAsLong());

		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);
		if (clmAppEmpBO != null) {
			objAssembly.addComponent(clmAppEmpBO.getClaimAppEmpData());
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with a ClaimApplicationEmployerData
	 * object as input to save or update it.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly
	 * @throws BaseApplicationException
	 */
	public IObjectAssembly saveOrUpdateClmAppEmp(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClmAppEmp");
		}
		// cq R4UAT00015324 on 06/02/2011
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="Start")
		ClaimApplicationEmployerDataBridge clmAppEmpData = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="End")
		if (clmAppEmpData != null
				&& clmAppEmpData.getClaimApplicationData() != null
				&& clmAppEmpData.getClaimApplicationData().getBpStartDate() != null
				&& clmAppEmpData.getEndDate() != null
				&& (clmAppEmpData.getEndDate().before(
						clmAppEmpData.getClaimApplicationData()
								.getBpStartDate().getTime()) || (clmAppEmpData
						.getEndDate().compareTo(
								clmAppEmpData.getClaimApplicationData()
										.getBpStartDate().getTime()) == 0 && !DateUtility
						.isSameDay(clmAppEmpData.getEndDate(), clmAppEmpData
								.getClaimApplicationData().getBpStartDate()
								.getTime())))) {
			throw new BaseApplicationException(
					"error.access.cin.addmilitaryemployer.workenddate.not.valid");
		}
		// added for Last employer check CIF_01312 starts
		List<ClaimApplicationEmployerData> employeeList = null;
		if (StringUtility.isNotBlank(clmAppEmpData.getLastEmployerFlag())
				&& clmAppEmpData.getLastEmployerFlag().equalsIgnoreCase(
						ViewConstants.YES)) {
			employeeList = ClaimApplicationEmployer
					.findClmAppEmpListByClmAppId(clmAppEmpData
							.getClaimApplicationData().getPkId());
			if (null != employeeList && !employeeList.isEmpty()) {
				for (ClaimApplicationEmployerData claimApplicationEmployerData : (List<ClaimApplicationEmployerData>) employeeList) {
					if ((null != claimApplicationEmployerData)
							&& (null != claimApplicationEmployerData
									.getLastEmployerFlag())
							&& claimApplicationEmployerData
									.getLastEmployerFlag().equalsIgnoreCase(
											ViewConstants.YES)
							&& (clmAppEmpData.getPkId() == null || !claimApplicationEmployerData
									.getPkId().equals(clmAppEmpData.getPkId()))) {
						throw new BaseApplicationException(
								"error.access.cin.addfederalemployer.lastemployer.not.valid");
					}
				}
			}
		}
		// CIF_01312 ends
		if (EmployerTypeEnum.MIL.getName().equals(
				clmAppEmpData.getEmployerType())) {
			MilitaryEmployerBO milEmprBO = MilitaryEmployerBO
					.findMilitaryEmployerByBranchOfService(clmAppEmpData
							.getMilitaryServiceBranch());

			if (milEmprBO != null) {
				clmAppEmpData.setEmployerData(milEmprBO.getEmployerData());
			}
		} else if (EmployerTypeEnum.FED.getName().equals(
				clmAppEmpData.getEmployerType())) {
			// Condition to check already last employer exists or not.
			FederalEmployer fedEmpr = FederalEmployer
					.findFederalEmployerByFicCode(clmAppEmpData.getFicCode());
			// CIF_00050 START
			// to null pointer CHECK ADDED
			if (null != fedEmpr
					&& null != fedEmpr.getFederalEmployerData()
					&& null != fedEmpr.getFederalEmployerData()
							.getAddressLine3()) {
				clmAppEmpData.setAddressLine3(fedEmpr.getFederalEmployerData()
						.getAddressLine3());
			}
			// CIF_00050 END
		}
		// CIF_INT_01087
		ClaimApplicationDataBridge claimAppData = null;
		if (ClaimApplicationTypeEnum.AIC.getName().equals(
				clmAppEmpData.getClaimApplicationData().getClaimAppType())) {
			claimAppData = (ClaimApplicationDataBridge) ClaimApplication
					.findClaimAppByPkId(
							clmAppEmpData.getClaimApplicationData().getPkId())
					.getClaimApplicationData();
			clmAppEmpData.setClaimApplicationData(claimAppData);
		} else if (null != clmAppEmpData.getClaimApplicationData()) {
			ClaimApplication claimApplicationBO = new ClaimApplication(
					clmAppEmpData.getClaimApplicationData());
			claimApplicationBO.saveOrUpdate();
		}
		ClaimApplicationEmployer claimAppEmpBO = new ClaimApplicationEmployer(
				clmAppEmpData);

		claimAppEmpBO.saveOrUpdate();

		if (null != claimAppData
				&& ClaimApplicationTypeEnum.AIC.getName().equals(
						claimAppData.getClaimAppType())) {
			objAssembly.addComponent(claimAppData, true);
			Claimant claimant = Claimant.fetchClaimantBySsn(claimAppData
					.getSsn());
			List<PotentialIssueData> listPotentialData = claimant
					.initializeQuestionnaireRenew(objAssembly);
			objAssembly.removeComponent(PotentialIssueData.class);
			if ((listPotentialData != null) && (listPotentialData.size() > 0)) {
				objAssembly.addComponentList(listPotentialData);
			}
			List<ClaimApplicationEmployerData> list = new ArrayList<ClaimApplicationEmployerData>();
			list.add(clmAppEmpData);
			claimAppData.getClaimApplicationEmployerData().retainAll(list);
			ClaimApplication claimApplicationBO = new ClaimApplication(
					claimAppData);
			claimApplicationBO.saveOrUpdate();
		}
		// @cif_wy(storyNumber = "P1-BAD-014", requirementId =
		// "FR_471, designDocName = "03 - Monetary Redetermination and
		// Reconsideration.docx", designDocSection = "10.2.4.4", dcrNo = "", mddrNo = "", impactPoint = "Start")
		CinService lObject = (CinService) MultiStateClassFactory.getObject(this
				.getClass().getName(), BaseOrStateEnum.STATE, null, null,
				Boolean.FALSE);
		WorkflowItemBean bean = objAssembly
				.getFirstBean(WorkflowItemBean.class);

		if (null != bean
				&& bean.getWorkItemName().equalsIgnoreCase("WageCreditFreeze")) {
			objAssembly = ((CinService) lObject)
					.saveIb4RequestForTTDOutOfState(objAssembly, clmAppEmpData);
		}
		// @cif_wy(storyNumber = "P1-BAD-014", requirementId = "FR_471",
		// designDocName =
		// "03 - Monetary Redetermination and Reconsideration.docx",
		// designDocSection = "10.2.4.4", dcrNo = "", mddrNo = "", impactPoint =
		// "End")
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationEmployerData - only
	 * pkId is valid (and EmployerData) as input and returns
	 * ClaimApplicationEmployerData if this pkId is different than null or
	 * create appropriate ClaimApplicationEmployerData based on EmployerData
	 * (which consists data for FederalEmployer), if pkId is null.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly with ClaimApplicationEmployerData
	 */
	public IObjectAssembly getFederalEmployer(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getFederalEmployer");
		}

		VerifyEmployerBean dataBean = (VerifyEmployerBean) objAssembly
				.getFirstBean(VerifyEmployerBean.class);

		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="Start")
		ClaimApplicationEmployerDataBridge clmAppEmp = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="End")

		if (clmAppEmp.getPkId() != null) {
			objAssembly.setPrimaryKey(clmAppEmp.getPkId());
			objAssembly = this.getClaimApplicationEmployer(objAssembly);
		}
		if (dataBean != null) {
			if (dataBean.getFicLocationData() != null) {
				FicLocation ficLocation = FicLocation
						.getFicLocationDataByPrimaryKey(dataBean
								.getFicLocationData().getFicLocationId());
				clmAppEmp.setMailAddress(ficLocation.getFicLocationData()
						.getAddress());
				clmAppEmp.setEmployerName(ficLocation.getFicLocationData()
						.getFederalEmployerData().getEmployerName());
				clmAppEmp.setEmployerData(ficLocation.getFicLocationData()
						.getFederalEmployerData());
				dataBean.setFicLocationData(ficLocation.getFicLocationData());
			} else {
				FicDestination ficDestBO = FicDestination
						.getFicDestinationDataByPrimaryKey(dataBean
								.getFicDestinationData().getFicDestinationId());
				clmAppEmp.setMailAddress(ficDestBO.getFicDestinationData()
						.getAddress());
				clmAppEmp.setAddressLine3(ficDestBO.getFicDestinationData()
						.getAddressLine3());
				clmAppEmp.setEmployerName(ficDestBO.getFicDestinationData()
						.getFederalEmployerData().getEmployerName());
				FederalEmployerData empData = null;
				Employer employer = Employer
						.findFedEmployerByPrimaryKey(ficDestBO
								.getFicDestinationData()
								.getFederalEmployerData().getEmployerId());
				if (employer != null) {
					empData = (FederalEmployerData) employer.getEmployerData();
				}
				clmAppEmp.setEmployerData(empData);
				dataBean.setFicDestinationData(ficDestBO
						.getFicDestinationData());
			}
		}
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationEmployerData - only
	 * pkId is valid (and EmployerData) as input and returns
	 * ClaimApplicationEmployerData if this pkId is different than null or
	 * create appropriate ClaimApplicationEmployerData based on EmployerData
	 * (which consists data for RegularEmployer), if pkId is null.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly with ClaimApplicationEmployerData
	 */
	public IObjectAssembly getRegularEmployer(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRegularEmployer");
		}
		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="Start")
		ClaimApplicationEmployerDataBridge clmAppEmp = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);

		// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
		// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
		// designDocSection="1.2", dcrNo="DCR_MDDR_6",
		// mddrNo="",impactPoint="End")
		if (clmAppEmp.getPkId() != null) {
			objAssembly.setPrimaryKey(clmAppEmp.getPkId());
			objAssembly = this.getClaimApplicationEmployer(objAssembly);
		} else if (clmAppEmp.getEmployerData().getEmployerId() != null) {
			RegisteredEmployer regEmprBO = RegisteredEmployer
					.fetchRegEmprDataWithEmprAccountDataByPkId(clmAppEmp
							.getEmployerData().getEmployerId());

			RegisteredEmployerData empData = null;
			if (regEmprBO != null) {
				empData = regEmprBO.getRegisteredEmployer();
			}

			if (empData != null) {
				RegisteredEmployerData regEmpData = (RegisteredEmployerData) empData;

				clmAppEmp.setEmployerName(regEmpData.getTradeName());

				AddressComponentData addressData = new AddressComponentData();
				if (!StringUtility.isBlank(regEmpData.getMailAddressData()
						.getLine1())) {
					addressData.setLine1(regEmpData.getMailAddressData()
							.getLine1());
					addressData.setLine2(regEmpData.getMailAddressData()
							.getLine2());
					addressData.setCity(regEmpData.getMailAddressData()
							.getCity());
					addressData.setState(regEmpData.getMailAddressData()
							.getState());
					addressData
							.setZip(regEmpData.getMailAddressData().getZip());
					addressData.setCountry(regEmpData.getMailAddressData()
							.getCountry());
					addressData.setValidatedFlag(regEmpData
							.getMailAddressData().getValidatedFlag());
					addressData.setPostalBarCode(regEmpData
							.getMailAddressData().getPostalBarCode());
				} else {
					if (!StringUtility.isBlank(regEmpData
							.getPhysicalAddressData().getLine1())) {
						addressData.setLine1(regEmpData
								.getPhysicalAddressData().getLine1());
						addressData.setLine2(regEmpData
								.getPhysicalAddressData().getLine2());
						addressData.setCity(regEmpData.getPhysicalAddressData()
								.getCity());
						addressData.setState(regEmpData
								.getPhysicalAddressData().getState());
						addressData.setZip(regEmpData.getPhysicalAddressData()
								.getZip());
						addressData.setCountry(regEmpData
								.getPhysicalAddressData().getCountry());
						addressData.setValidatedFlag(regEmpData
								.getPhysicalAddressData().getValidatedFlag());
						addressData.setPostalBarCode(regEmpData
								.getPhysicalAddressData().getPostalBarCode());
					}
				}
				clmAppEmp.setMailAddress(addressData);

				if (StringUtility.isNotBlank(empData.getTradeName())) {
					clmAppEmp.setEmployerName(empData.getTradeName());
				} else {
					clmAppEmp.setEmployerName(empData.getEmployerAccountData()
							.getEntityName());
				}
			} else {
				// This condition should never reached.
				clmAppEmp.getEmployerData().setEmployerId(null);
			}
		}
		// @cif_wy(storyNumber = "P1-CM-003", requirementId = "FR_1570",
		// designDocName = "03 UCFE,UCX,CWC,IB1,Missing Wages", designDocSection
		// = "1.2", dcrNo = "DCR_MDDR_6", mddrNo = "", impactPoint = "Start")
		Object lObject = MultiStateClassFactory.getObject(this.getClass()
				.getName(), BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

		if (null != lObject) {

			((CinService) lObject)
					.includeStateSpecificClmAppEmpDataObject(clmAppEmp);
		}
		// @cif_wy(storyNumber = "P1-CM-003", requirementId = "FR_1570",
		// designDocName = "03 UCFE,UCX,CWC,IB1,Missing Wages", designDocSection
		// = "1.2", dcrNo = "DCR_MDDR_6", mddrNo = "", impactPoint = "End")
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with the list of
	 * ClaimApplicationEmployerData as input and and saves, or updates these
	 * objects.
	 * 
	 * @param objAssembly
	 *            with the list of ClaimApplicationEmployerData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateClaimAppEmprList(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClaimAppEmprList");
		}

		List list = objAssembly
				.getComponentList(ClaimApplicationEmployerDataBridge.class);
		ClaimApplicationEmployerData clmAppEmpData = null;
		String currentEmpId = (String) objAssembly.getData("currentEmployer");
		// CIF_01373 starts
		for (Iterator i = list.iterator(); i.hasNext();) {
			clmAppEmpData = (ClaimApplicationEmployerData) i.next();
			if (currentEmpId.equalsIgnoreCase(clmAppEmpData.getEmployerData()
					.getEmployerId().toString())) {
				break;
			}

		}
		List<ClaimApplicationEmployerData> employeeList = null;
		if (StringUtility.isNotBlank(clmAppEmpData.getLastEmployerFlag())
				&& clmAppEmpData.getLastEmployerFlag().equalsIgnoreCase(
						ViewConstants.YES)) {
			employeeList = ClaimApplicationEmployer
					.findClmAppEmpListByClmAppId(clmAppEmpData
							.getClaimApplicationData().getPkId());
			if (null != employeeList && !employeeList.isEmpty()) {
				for (ClaimApplicationEmployerData claimApplicationEmployerData : (List<ClaimApplicationEmployerData>) employeeList) {
					if ((null != claimApplicationEmployerData)
							&& (null != claimApplicationEmployerData
									.getLastEmployerFlag())
							&& claimApplicationEmployerData
									.getLastEmployerFlag().equalsIgnoreCase(
											ViewConstants.YES)
							&& (clmAppEmpData.getPkId() == null || !claimApplicationEmployerData
									.getPkId().equals(clmAppEmpData.getPkId()))) {
						objAssembly
								.addBusinessError("error.access.cin.addfederalemployer.lastemployer.not.valid");
						// throw new
						// BaseApplicationException("error.access.cin.addfederalemployer.lastemployer.not.valid");
						return objAssembly;
					}
				}
			}
		}
		// CIF_01373 ends
		for (Iterator i = list.iterator(); i.hasNext();) {
			clmAppEmpData = (ClaimApplicationEmployerData) i.next();
			// CIF_INT_03953
			if (clmAppEmpData.getEmployerData() != null
					&& null != clmAppEmpData.getEmployerData()
							.getAddressDataByType(AddressTypeEnum.MA.getName())) {
				clmAppEmpData.setMailAddress(clmAppEmpData.getEmployerData()
						.getAddressDataByType(AddressTypeEnum.MA.getName()));
			}
			ClaimApplicationEmployer claimAppEmpBO = new ClaimApplicationEmployer(
					clmAppEmpData);

			claimAppEmpBO.saveOrUpdate();
		}

		return objAssembly;
	}

	// CIF_00534
	/*	*//**
	 * 
	 * This method check whether if the back date or post date is more than
	 * a Week and create a workiteam .
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * 
	 * @return objAssembly
	 * 
	 */
	/*
	 * public Boolean requestBackandPostDate(ClaimApplicationData clmAppData)
	 * throws BaseApplicationException { if(LOGGER.isDebugEnabled()){
	 * LOGGER.debug("Start of method requestBackandPostDate"); }
	 * //ClaimApplicationData clmAppData = (ClaimApplicationData) objAssembly
	 * .fetchORCreate(ClaimApplicationData.class); Calendar currentEffectiveDate
	 * = new GregorianCalendar(); Calendar effectiveDate = new
	 * GregorianCalendar(); boolean backDate=false;
	 * currentEffectiveDate.setTime(clmAppData.getClaimEffectiveDate());
	 * effectiveDate.setTime(clmAppData.getBackDate());
	 * 
	 * if(effectiveDate.before(currentEffectiveDate)){
	 * effectiveDate.add(Calendar.DATE, 7);
	 * if(effectiveDate.before(currentEffectiveDate)){ backDate=true; }
	 * 
	 * } if(effectiveDate.after(currentEffectiveDate)) {
	 * effectiveDate.add(Calendar.DATE,-7);
	 * if(effectiveDate.after(currentEffectiveDate)){ backDate=true; } } //
	 * creating workiteam if the back date or post date is more than a week
	 * if(backDate){ IBaseWorkflowDAO baseWorkflowDAO =
	 * DAOFactory.instance.getBaseWorkflowDAO(); BaseAccessDsContainerBean bean
	 * = new BaseAccessDsContainerBean(); GlobalDsContainerBean globalBean =
	 * bean.getNewGlobalDsContainerBean();
	 * globalBean.setBusinessKey(clmAppData.getPkId().toString());
	 * globalBean.setSsneanfein(clmAppData.getSsn());
	 * //globalBean.setName(clmAppData.get); globalBean.setTypeAsSsn();
	 * baseWorkflowDAO
	 * .createAndStartProcessInstance(NonMonetary.REQUEST_BACK_DATE, bean);
	 * 
	 * // BaseAccessDsContainerBean baseBean = new BaseAccessDsContainerBean();
	 * // GlobalDsContainerBean globalBean =
	 * baseBean.getNewGlobalDsContainerBean();
	 * 
	 * Map<String,Object> mapValues = new HashMap<String,Object>();
	 * //Map<String,Object> globalbeanMap = new HashMap<String,Object>();
	 * 
	 * //globalBean.setTypeAsSsn(); mapValues.put(WorkflowConstants.TYPE,
	 * WorkflowConstants.BUSINESS_TYPE.SSN);
	 * //globalBean.setSsneanfein(clmAppData.getSsn());
	 * mapValues.put(WorkflowConstants.SSN_EAN_FEIN, clmAppData.getSsn());
	 * 
	 * //globalBean.setName(sharedWorkEmployeeData.getFirstName()+" "+
	 * sharedWorkEmployeeData.getLastName());
	 * //globalBean.setBusinessKey(clmAppData.getPkId().toString());
	 * mapValues.put(WorkflowConstants.BUSINESS_KEY,
	 * clmAppData.getPkId().toString()); //
	 * globalBean.setZipCode(sharedWorkEmployeeData.getZip());
	 * 
	 * // baseBean.setGlobalDs(globalBean); // //
	 * globalbeanMap.put("globalBean", globalBean); //
	 * globalbeanMap.put("businessKey", globalBean.getBusinessKey()); //
	 * globalbeanMap.put("businessData",globalBean.getBusinessData()); //
	 * globalbeanMap.put("name",globalBean.getName()); //
	 * globalbeanMap.put("ssneanfein",globalBean.getSsneanfein()); //
	 * globalbeanMap.put("type",globalBean.getType()); //
	 * globalbeanMap.put("zipCode",globalBean.getZipCode()); //
	 * globalbeanMap.put("docketNo",globalBean.getDocketNo()); // // //*3.set
	 * the baseBean to the mapValues. //
	 * mapValues.put("userId",baseBean.getUserId()); //
	 * mapValues.put("supervisorUserId",baseBean.getSupervisorUserId()); //
	 * mapValues.put("holdDate",baseBean.getHoldDate()); //
	 * mapValues.put("firstNotificationDueTime"
	 * ,baseBean.getFirstNotificationDueTime()); //
	 * mapValues.put("forward",baseBean.getForward()); //
	 * mapValues.put("nextActivity",baseBean.getNextActivity()); //
	 * mapValues.put("globalDs",globalbeanMap); //
	 * mapValues.put("dolTimeLapse",baseBean.getDolTimeLapse()); //
	 * mapValues.put("comment",baseBean.getComment());
	 * 
	 * //BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
	 * 
	 * //workflowDAO.createAndStartProcessInstance(WorkflowProcessTemplateConstants
	 * .Claims.SHARED_WORK_EMPLOYEE_REMOVED,mapValues);
	 * mapValues.put(WorkflowConstants.PROCESS_NAME,
	 * NonMonetary.REQUEST_BACK_DATE); WorkflowTransactionService
	 * wfTransactionService = new WorkflowTransactionService();
	 * wfTransactionService
	 * .invokeWorkFlowOperation(WorkFlowOperationsEnum.CreateWorkItem.getName(),
	 * mapValues);
	 * 
	 * 
	 * 
	 * 
	 * } return backDate ; }
	 */
	/**
	 * This method takes ObjectAssembly with list ClaimApplicationEmployerData
	 * as input and and saves, or updates these objects.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateBasePeriodEmprList(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateBasePeriodEmprList");
		}

		// to determine 8X
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// CIF_00534 Start
		// check the back date falg if yes call the method .
		/*
		 * if(YesNoTypeEnum.NumericYes.getName().equals(clmAppData.getBackDateFlag
		 * ())){ requestBackandPostDate(objAssembly); }
		 */

		// CIF_00534 Start

		// 7748
		List claimAppEmprList = new ArrayList();
		if (objAssembly
				.getComponentList(ClaimApplicationEmployerDataBridge.class) != null) {
			claimAppEmprList = (List) objAssembly
					.getComponentList(ClaimApplicationEmployerDataBridge.class);
		}
		if (ClaimApplicationTypeEnum.NEW.getName().equals(
				clmAppData.getClaimAppType())) {
			if (!claimAppEmprList.isEmpty()) {
				for (Iterator itr = claimAppEmprList.iterator(); itr.hasNext();) {
					ClaimApplicationEmployerData clmAppEmprData = (ClaimApplicationEmployerData) itr
							.next();
					if (EmployerTypeEnum.REG.getName().equals(
							clmAppEmprData.getEmployerType())) {
						if (StringUtility.isNotBlank(clmAppEmprData
								.getMissingFlag())) {
							if (YesNoTypeEnum.NumericYes.getName().equals(
									clmAppEmprData.getMissingFlag())) {
								if (clmAppEmprData.getStartDate().before(
										clmAppData.getBpEndDate().getTime())
										&& clmAppEmprData.getEndDate().after(
												clmAppData.getBpStartDate()
														.getTime())) {
									if (clmAppEmprData.getForm525BData() == null
											|| clmAppEmprData.getForm525BData()
													.isEmpty()) {
										String[] emprName = new String[] { clmAppEmprData
												.getEmployerName() };
										// uncommented this line of code as
										// earlier this was commented for work
										// flow purposes
										throw new BaseApplicationException(
												"error.access.employmentsummary.incomplete.525b",
												emprName);
									}
								}
							}
						}
					}
				}
			}
		}// End 7748

		List lst = Issue.processSeparationIssue8xNotSatisfied(
				clmAppData.getSsn(), clmAppData.getClaimEffectiveDate());
		if (lst.size() >= 1) {
			objAssembly.addData(TransientSessionConstants.CLM_SEP_ISSUE_OPEN,
					lst.get(0));
		}
		if (lst.size() >= 2) {
			objAssembly.addData(TransientSessionConstants.CLM_SEP_ISSUE_LIFTED,
					lst.get(1));
		}
		// List otherIssues = Issue.getNonSepAndAllowedSepIssue(
		// clmAppData.getSsn());
		// objAssembly.addData(TransientSessionConstants.CLM_SEP_ISSUE_ALLOWED_DECISION,otherIssues);

		if (!objAssembly
				.isComponentPresent(ClaimApplicationEmployerDataBridge.class)) {
			objAssembly.addData(TransientSessionConstants.CLM_8X_AMT,
					GlobalConstants.BIGDECIMAL_ZERO);
			objAssembly.addData(TransientSessionConstants.CLMEMP_EVIDENT_8X,
					null);
			objAssembly.addData(TransientSessionConstants.CLMEMP_8X, null);
			// return objAssembly;
		}

		// to get the list of Claim Application Employer Data
		List tempList = new ArrayList();
		if (objAssembly
				.getComponentList(ClaimApplicationEmployerDataBridge.class) != null) {
			tempList = new ArrayList(
					objAssembly
							.getComponentList(ClaimApplicationEmployerDataBridge.class));
		}

		// to get the list of Claim Application Employer Data
		// with Worked or Missing status
		List temp2List = new ArrayList();
		for (Iterator i = tempList.iterator(); i.hasNext();) {
			ClaimApplicationEmployerData clmAppEmprData = (ClaimApplicationEmployerData) i
					.next();
			if ((GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
					.getWorkFlag()))
					|| (GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
							.getMissingFlag()))) {
				temp2List.add(clmAppEmprData);
			}
		}
		if (temp2List.isEmpty()) {
			objAssembly.addData(TransientSessionConstants.CLM_8X_AMT,
					GlobalConstants.BIGDECIMAL_ZERO);
			objAssembly.addData(TransientSessionConstants.CLMEMP_EVIDENT_8X,
					null);
			objAssembly.addData(TransientSessionConstants.CLMEMP_8X, null);
			// return objAssembly;
		}
		List clmAppEmprList = new ArrayList();
		if (temp2List != null && temp2List.size() > 0) {
			// to sort the list of Claim Application Employers by end dates
			Collections.sort(temp2List, Collections.reverseOrder());

			ClaimApplicationEmployerData lstClmAppEmp = (ClaimApplicationEmployerData) temp2List
					.get(0);

			for (Iterator i = temp2List.iterator(); i.hasNext();) {
				ClaimApplicationEmployerData data = (ClaimApplicationEmployerData) i
						.next();
				if (((!EmployerTypeEnum.MIL.getName().equals(
						data.getEmployerType()) && (StringUtility
						.isNotBlank(data.getReasonSeparation()) && (!(ReasonForSeperationEnum.LWLO
						.getName().equals(data.getReasonSeparation())
				// || "DSTR".equals(data.getReasonSeparation())
				|| ReasonForSeperationEnum.WRPT.getName().equals(
						data.getReasonSeparation()))
				// ||"VACD".equals(data.getReasonSeparation())
				))))) {
					clmAppEmprList.add(data);
				}
			}
			if ((clmAppEmprList.isEmpty())) {
				objAssembly.addData(TransientSessionConstants.CLM_8X_AMT,
						GlobalConstants.BIGDECIMAL_ZERO);
				objAssembly.addData(
						TransientSessionConstants.CLMEMP_EVIDENT_8X, null);
				objAssembly.addData(TransientSessionConstants.CLMEMP_8X, null);
				// return objAssembly;
			}

			// remove the last employer from the list if present.
			if (lstClmAppEmp != null) {
				clmAppEmprList.remove(lstClmAppEmp);
			}
		}
		List wageList = null;
		BigDecimal amount8X = null;

		// to get all wages for specified claimant and time period
		List wageList1 = (List) objAssembly.getComponentList(WageData.class);
		if (wageList1 == null) {
			wageList1 = new ArrayList();
		}
		Set wageBpSet = new HashSet();
		for (Iterator i = wageList1.iterator(); i.hasNext();) {
			WageData wageData = (WageData) i.next();
			QtrBean qtrBean = new QtrBean(wageData.getQuarter().intValue(),
					wageData.getYear().intValue());

			if (!qtrBean.getEndDate()
					.after(clmAppData.getBpEndDate().getTime())) {
				wageBpSet.add(wageData);
			}
		}

		amount8X = (Claim.calculateWba(wageBpSet,
				clmAppData.getClaimEffectiveDate()))
				.multiply(new BigDecimal(5));

		BigDecimal whatsTheWBA = Claim.calculateWba(wageBpSet,
				clmAppData.getClaimEffectiveDate());

		Set wageSet = new HashSet();
		wageSet.addAll(wageList1);

		// to select Claim Application Employers for which the issue can be
		// lifted - removed
		List evidentClmAppEmprList = new ArrayList();

		// commented two lines and the amount8x calculation above as 8Xs is
		// computed differently
		/*
		 * if ((clmAppEmprList != null) && (!clmAppEmprList.isEmpty()) &&
		 * (amount8X.compareTo(new BigDecimal(0)) > 0)) {
		 */
		if ((clmAppEmprList != null) && (!clmAppEmprList.isEmpty())) {
			for (int i = clmAppEmprList.size() - 1; i >= 0; i--) {
				ClaimApplicationEmployerData data = (ClaimApplicationEmployerData) clmAppEmprList
						.get(i);
				// get the end date of employments and call 8x method.
				// this will find the claim where the date falls within. if it
				// return null then
				// look at current info. if it's wba is zero then make 8x max
				// amount.
				amount8X = Issue.getRequalifyingAmountForSpecificDate(
						data.getEndDate(), clmAppData.getSsn());
				if (amount8X == null) {
					if (whatsTheWBA.compareTo(new BigDecimal(0)) < 1) {
						BigDecimal maxWba = Claim
								.getDynamicBozConstantAsBigDecimal("MAX_WBA",
										data.getEndDate());
						amount8X = maxWba
								.multiply(new BigDecimal(
										gov.state.uim.framework.BenefitsConstants.NUMBER_FIVE));
					} else {
						amount8X = whatsTheWBA
								.multiply(new BigDecimal(
										gov.state.uim.framework.BenefitsConstants.NUMBER_FIVE));
					}
				}
				objAssembly.addData(data.getPkId().toString(),
						amount8X.setScale(2));
				Calendar issueStartDt = new GregorianCalendar();
				issueStartDt.setTime(data.getEndDate());
				if (wageList == null) {
					wageList = Wage.getBpWageList(clmAppData.getSsn(),
							issueStartDt, new GregorianCalendar());
				}
				if (amount8X.compareTo(Wage.addWages(wageList, issueStartDt,
						new GregorianCalendar())) <= 0) {
					data.setRequalificationStatus(ViewConstants.REQ8X_SYSTEM_EVIDENT);
					evidentClmAppEmprList.add(data);
				} else if (ViewConstants.REQ8X_SYSTEM_EVIDENT.equals(data
						.getRequalificationStatus())) {
					data.setRequalificationStatus(null);
				}
			}
			clmAppEmprList.removeAll(evidentClmAppEmprList);
		}

		if (!evidentClmAppEmprList.isEmpty()) {
			objAssembly.addData(TransientSessionConstants.CLMEMP_EVIDENT_8X,
					evidentClmAppEmprList);
			objAssembly = this.saveOrUpdateEvidentClaimAppEmprList(objAssembly);
		} else {
			objAssembly.addData(TransientSessionConstants.CLMEMP_EVIDENT_8X,
					null);
		}

		if (!clmAppEmprList.isEmpty()) {
			objAssembly.addData(TransientSessionConstants.CLMEMP_8X,
					clmAppEmprList);
		} else {
			objAssembly.addData(TransientSessionConstants.CLMEMP_8X, null);
		}

		return objAssembly;
	}

	/**
	 * This method processes the initial claim application. (send request to SSA
	 * for SSN validation; create the appropriate requests to other states
	 * and/or FCCC; save or update ClaimantData object; save or update ClaimData
	 * object; save or update ClaimApplicationData object; save or update
	 * ClaimApplicationClaimantData object)
	 * 
	 * @param objAssembly
	 *            with ClaimaApplicationData, ClaimApplicationClaimantData,
	 *            ClaimantData, ClaimApplicationEmployerData object
	 * @return objAssembly
	 * @throws BaseApplicationException
	 */
	public IObjectAssembly processInitialClaim(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processInitialClaim");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		List claimAppEmpList = null;
		if (objAssembly
				.getComponentList(ClaimApplicationEmployerDataBridge.class) != null) {
			claimAppEmpList = new ArrayList(
					objAssembly
							.getComponentList(ClaimApplicationEmployerDataBridge.class));
		}
		Set claimAppEmpSet = clmAppData.getClaimApplicationEmployerData();
		if (claimAppEmpSet == null) {
			claimAppEmpSet = new HashSet();
		}
		if (claimAppEmpList != null && !claimAppEmpList.isEmpty()) {
			claimAppEmpSet.addAll(claimAppEmpList);
			clmAppData.setClaimApplicationEmployerData(claimAppEmpSet);
		}
		// CIF_01351 Start
		String skippedWeek = (String) objAssembly
				.getData(GlobalConstants.SKIPPED_WEEK);
		String skippedWeekInBetween = (String) objAssembly
				.getData(GlobalConstants.SKIPPED_WEEK_IN_BETWEEN);
		if (StringUtility.isBlank(skippedWeek)
				&& StringUtility.isBlank(skippedWeekInBetween)) {
			clmAppData.setSkippedWeek(false);
		} else {
			clmAppData.setSkippedWeek(true);
		}
		// CIF_01351 End
		ClaimApplication clmAppFromDB = ClaimApplication
				.findClaimAppByPkId(clmAppData.getPkId());
		if (clmAppFromDB != null) {
			IClaimApplicationDAO clmAppDAO = DAOFactory.instance
					.getClaimApplicationDAO();
			clmAppDAO.evict(clmAppFromDB.getClaimApplicationData());
			if (ApplicationStatusEnum.COMPLETED.getName().equals(
					clmAppFromDB.getClaimApplicationData().getClaimAppStatus())
					|| ApplicationStatusEnum.PENDING.getName().equals(
							clmAppFromDB.getClaimApplicationData()
									.getClaimAppStatus())) {
				objAssembly.addData("INCORRECTCLMPROCESS",
						YesNoTypeEnum.NumericYes.getName());
				return objAssembly;
			}
		}

		// Claim will be held where issues next to last and bp employers are
		// detected not 8x evident or
		// prior 8x issues exists (not 8x evident) or carry over issues exists.
		String pendingScreenForwardFlag = (String) objAssembly
				.getData("claimantpendingscreen");
		if (YesNoTypeEnum.NumericYes.getName().equals(pendingScreenForwardFlag)) {
			return objAssembly;
		}

		FlowBean flowBean = (FlowBean) objAssembly
				.fetchORCreateBean(FlowBean.class);

		ClaimApplication clmAppBO = new ClaimApplication(clmAppData);

		// Check for TEUC reach back claimant
		TeucDeclarationData teucDecData = (TeucDeclarationData) objAssembly
				.getFirstComponent(TeucDeclarationData.class);

		// Check for EB claim
		EbDeclarationData ebDecData = (EbDeclarationData) objAssembly
				.getFirstComponent(EbDeclarationData.class);

		if (teucDecData != null) {
			// to get the list of Claim Application Employer Data
			List tempList = new ArrayList();
			if (objAssembly
					.getComponentList(ClaimApplicationEmployerDataBridge.class) != null) {
				tempList = new ArrayList(
						objAssembly
								.getComponentList(ClaimApplicationEmployerDataBridge.class));
			}

			// to get the list of Claim Application Employer Data
			// with Worked or Missing status
			List temp2List = new ArrayList();
			for (Iterator i = tempList.iterator(); i.hasNext();) {
				ClaimApplicationEmployerData clmAppEmprData = (ClaimApplicationEmployerData) i
						.next();
				if ((GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
						.getWorkFlag()))
						|| (GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
								.getMissingFlag()))) {
					temp2List.add(clmAppEmprData);
				}
			}

			if (temp2List != null && temp2List.size() > 0) {
				// to sort the list of Claim Application Employers by end dates
				Collections.sort(temp2List, Collections.reverseOrder());

				ClaimApplicationEmployerData lstClmAppEmp = (ClaimApplicationEmployerData) temp2List
						.get(0);

				// If last day worked is after the TEUC effective date, prior
				// sunday to last day worked
				// will be the claim effective date
				Date teucTier3EffectiveDate = Claim.getBizConstant()
						.getValueAsDate("TIER_3_EFFECTIVE_DATE");
				if (lstClmAppEmp.getEndDate().after(teucTier3EffectiveDate)) {
					Calendar effectiveDate = new GregorianCalendar();
					effectiveDate.setTime(lstClmAppEmp.getEndDate());

					int dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
					int sunday = Calendar.SUNDAY;

					while (dayOfWeek != sunday) {
						effectiveDate.add(Calendar.DATE, -1);
						dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
					}
					clmAppData.setClaimEffectiveDate(effectiveDate.getTime());
					clmAppBO.setValuesForUiDiasterorEBClaimApp(objAssembly);
				}
			}
			/*
			 * Commented as per MDES request. 11/10/2009
			 * if(!ClaimApplicationTypeEnum
			 * .AIC.getName().equals(clmAppData.getClaimAppType())){ //8745
			 * //8820 Set wageSet = new HashSet();
			 * wageSet.addAll(Wage.getBpWageList(clmAppData.getSsn(),
			 * clmAppData.getBpStartDate(), clmAppData.getBpEndDate()));
			 * if(BigDecimal.valueOf(0).compareTo(Claim.calculateWba(wageSet,
			 * clmAppData.getClaimEffectiveDate())) != 0){
			 * clmAppData.setClaimEffectiveDate
			 * (Claim.determineClaimEffectiveDate().getTime()); } }
			 */
			flowBean.setFlowTEUC(true);
		} else if (ebDecData != null) {
			// to get the list of Claim Application Employer Data
			List tempList = new ArrayList();
			if (objAssembly
					.getComponentList(ClaimApplicationEmployerDataBridge.class) != null) {
				tempList = new ArrayList(
						objAssembly
								.getComponentList(ClaimApplicationEmployerDataBridge.class));
			}

			// to get the list of Claim Application Employer Data
			// with Worked or Missing status
			List temp2List = new ArrayList();
			for (Iterator i = tempList.iterator(); i.hasNext();) {
				ClaimApplicationEmployerData clmAppEmprData = (ClaimApplicationEmployerData) i
						.next();
				if ((GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
						.getWorkFlag()))
						|| (GlobalConstants.DB_ANSWER_YES.equals(clmAppEmprData
								.getMissingFlag()))) {
					temp2List.add(clmAppEmprData);
				}
			}

			if (temp2List != null && temp2List.size() > 0) {
				// to sort the list of Claim Application Employers by end dates
				Collections.sort(temp2List, Collections.reverseOrder());

				ClaimApplicationEmployerData lstClmAppEmp = (ClaimApplicationEmployerData) temp2List
						.get(0);

				// If last day worked is after the TEUC effective date, prior
				// sunday to last day worked
				// will be the claim effective date
				if (lstClmAppEmp.getEndDate().after(ebDecData.getEbStartDate())) {
					Calendar effectiveDate = new GregorianCalendar();
					effectiveDate.setTime(lstClmAppEmp.getEndDate());

					int dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
					int sunday = Calendar.SUNDAY;

					while (dayOfWeek != sunday) {
						effectiveDate.add(Calendar.DATE, -1);
						dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
					}
					clmAppData.setClaimEffectiveDate(effectiveDate.getTime());
					clmAppBO.setValuesForUiDiasterorEBClaimApp(objAssembly);
				}
			}
			if (!ClaimApplicationTypeEnum.AIC.getName().equals(
					clmAppData.getClaimAppType())) {
				// 8745 //8820
				Set wageSet = new HashSet();
				wageSet.addAll(Wage.getBpWageList(clmAppData.getSsn(),
						clmAppData.getBpStartDate(), clmAppData.getBpEndDate()));
				if (BigDecimal.valueOf(0).compareTo(
						Claim.calculateWba(wageSet,
								clmAppData.getClaimEffectiveDate())) != 0) {
					clmAppData.setClaimEffectiveDate(Claim
							.determineClaimEffectiveDate().getTime());
				}
			}
			flowBean.setFlowEB(true);
		}

		if (flowBean.isDisplayDua()) {
			//@cif_wy(storyNumber = "", requirementId = "DEFECT_215", designDocName = "", designDocSection = "", dcrNo = "", mddrNo = "")
			DuaApplicationDataBridge duaAppData = (DuaApplicationDataBridge) objAssembly
					.getFirstComponent(DuaApplicationDataBridge.class);
			DuaDeclarationData duaDeclarationData = (DuaDeclarationData) objAssembly
					.getFirstComponent(DuaDeclarationData.class);

			if (duaAppData.getLastDate().after(
					duaDeclarationData.getStartDate())) {

				Calendar effectiveDate = new GregorianCalendar();
				effectiveDate.setTime(duaAppData.getLastDate());

				int dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
				int sunday = Calendar.SUNDAY;

				while (dayOfWeek != sunday) {
					effectiveDate.add(Calendar.DATE, -1);
					dayOfWeek = effectiveDate.get(Calendar.DAY_OF_WEEK);
				}

				clmAppData.setClaimEffectiveDate(effectiveDate.getTime());

				clmAppBO.setValuesForUiDiasterorEBClaimApp(objAssembly);
			}
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		// Commented below block as the version of the
		// claimapplicationemployerdata in the object assembly is
		// different than the version attached to claimapplicationdata. this was
		// caught while doing testing for versioning added
		// for defect R4UAT00005244

		List tempList = null;
		if (clmAppData.getClaimApplicationEmployerData() != null
				&& !clmAppData.getClaimApplicationEmployerData().isEmpty()) {
			tempList = new ArrayList(
					clmAppData.getClaimApplicationEmployerData());
			Set clmAppEmprSet = clmAppData.getClaimApplicationEmployerData();
			// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
			// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
			// designDocSection="1.2", dcrNo="DCR_MDDR_6",
			// mddrNo="",impactPoint="Start")
			for (Iterator clmAppEmprSetIterator = clmAppEmprSet.iterator(); clmAppEmprSetIterator
					.hasNext();) {
				ClaimApplicationEmployerDataBridge claimApplicationEmployerData = (ClaimApplicationEmployerDataBridge) clmAppEmprSetIterator
						.next();
				// @cif_wy(storyNumber="P1-CM-003", requirementId="FR_1570",
				// designDocName="03 UCFE,UCX,CWC,IB1,Missing Wages",
				// designDocSection="1.2", dcrNo="DCR_MDDR_6",
				// mddrNo="",impactPoint="End")
				if (EmployerTypeEnum.FED.getName().equals(
						claimApplicationEmployerData.getEmployerType())) {
					if (StringUtility.isBlank(claimApplicationEmployerData
							.getFicDestinationCode())) {
						List destinationList = FicDestination
								.getDestinationListByEmployerAndFic(
										claimApplicationEmployerData
												.getEmployerData()
												.getEmployerId(),
										claimApplicationEmployerData
												.getFicCode());
						if (destinationList != null
								&& destinationList.size() > 0) {
							FicDestinationData ficDestinationData = (FicDestinationData) destinationList
									.get(0);
							claimApplicationEmployerData
									.setFicDestinationCode(ficDestinationData
											.getDestinationCode());
						}
					}
					if (claimApplicationEmployerData.getMailAddress() == null) {
						FederalEmployer federalEmployer = FederalEmployer
								.fetchFedEmprByPkId(claimApplicationEmployerData
										.getEmployerData().getEmployerId());
						claimApplicationEmployerData
								.setMailAddress(federalEmployer
										.getFederalEmployerData()
										.getContactAddress());
						claimApplicationEmployerData
								.setAddressLine3(federalEmployer
										.getFederalEmployerData()
										.getAddressLine3());
					}

				}
			}
			clmAppData.setClaimApplicationEmployerData(clmAppEmprSet);
		}
		// CIF_01453 code commented as this was changing the ssa Status to
		// verified
		/*
		 * if (!ApplicationProperties.SSA_VALIDATION_FLAG) { // this code is
		 * added for testing purpose in UAT // it will be removed later String[]
		 * validSsnList= new String[] { "001442401", "001504700", "001566109",
		 * "001580405", "001583009", "001705603", "001745101", "001744500",
		 * "002323705", "002324608", "002589302", "002308601", "002345408",
		 * "002467806", "004946703", "001442401", "001504700", "001566109",
		 * "001580405", "001583009", "001705603", "001745101", "001744500",
		 * "002323705", "002324608", "002589302", "002308601", "002345408",
		 * "002467806", "004946703", "001400600", "001500706", "001560500",
		 * "001705703", "001766101", "002093000", "002222409", "002425203",
		 * "002428300", "002444102", "002483804", "002484105", "006507809",
		 * "004802808", "004804204", "005501000", "005929208", "006284804",
		 * "006681903", "006704301", "002267202", "004702700", "004746201",
		 * "004842007", "004926903", "006381906", "001427703", "001442501",
		 * "001622900", "002362007", "002408409", "002488207", "004508609",
		 * "002265701", "002362007", "004588804", "004625101", "004642404",
		 * "004702105" };
		 * 
		 * if (StringUtility.isValuePresent(validSsnList, objAssembly.getSsn()))
		 * { // do nothing } else {
		 * clmAppClmntData.setSsaStatus(SsaStatusEnum.V.getName()); } } else {
		 * // For production it will directly go here
		 * if(!SsaStatusEnum.I.getName
		 * ().equals(clmAppClmntData.getSsaStatus())){
		 * clmAppClmntData.setSsaStatus(SsaStatusEnum.V.getName()); } }
		 */
		// end of test code
		// to mark the claim application pending and set File Date
		clmAppData.setClaimAppStatus(ApplicationStatusEnum.PENDING.getName());
		clmAppData.setFileDate(new Date());

		clmAppBO = new ClaimApplication(clmAppData);
		clmAppBO.saveOrUpdate();
		clmAppBO.flush();

		// to process DUA claim, if necessary
		if (flowBean.isDisplayDua()) {
			// @cif_wy(storyNumber="P1-BSP-001", requirementId="FR_1206",
			// designDocName=""02 File DUA Claim.docx",designDocSection="1.3",
			// dcrNo="DCR_MDDR_32", mddrNo="",
			// impactPoint="Start")
			DuaApplicationDataBridge duaAppData = (DuaApplicationDataBridge) objAssembly
					.fetchORCreate(DuaApplicationDataBridge.class);

			// @cif_wy(storyNumber="P1-BSP-001", requirementId="FR_1206",
			// designDocName=""02 File DUA Claim.docx",designDocSection="1.3",
			// dcrNo="DCR_MDDR_32", mddrNo="",
			// impactPoint="End")

			duaAppData.setStatusFlag(ApplicationStatusEnum.PENDING.getName());
			DuaApplication duaAppBO = new DuaApplication(duaAppData);
			duaAppBO.saveOrUpdate();
			duaAppBO.flush();
		}

		// to validate SSN status
		// CIF_00972:START
		// commented out this code ad per the new rule Claim will establish when
		// ssn validation fails
		/*
		 * if (SsaStatusEnum.I.getName().equals(clmAppClmntData.getSsaStatus()))
		 * {
		 * objAssembly.addBusinessError("error.access.establishclaim.ssn.invalid"
		 * ); flowBean.setDisplayDetermination(false);
		 */
		// CIF_00972:END

		// // to generate correspondence
		// CorrespondenceData corrData = new CorrespondenceData();
		// corrData.setCorrespondenceCode(CorrespondenceCodeEnum.SSN_INVALID.getName());
		// corrData.setClaimAppClmntData(clmAppClmntData);
		// corrData.setDirection(CorrespondenceDirectionEnum.OUTGOING.getName());
		//
		// //Parameter 1 is set to ssn for saving in ClaimantDmsBean
		// corrData.setParameter1(clmAppClmntData.getSsn());
		//
		// corrData.setMailingAddress(clmAppClmntData.getMailAddress());
		// Correspondence correspondenceBO = new Correspondence(corrData);
		// correspondenceBO.saveOrUpdate();

		// return objAssembly;
		// }
		// else if
		// ((SsaStatusEnum.P.getName().equalsIgnoreCase(clmAppClmntData.getSsaStatus())
		// ||
		// (SsaStatusEnum.N.getName().equalsIgnoreCase(clmAppClmntData.getSsaStatus()))))
		// {
		// objAssembly
		// .addBusinessError("error.access.establishclaim.ssn.pending");
		// flowBean.setDisplayDetermination(false);
		// return objAssembly;
		// }

		// to process ClaimantData object
		Claimant claimantBO = Claimant.fetchClaimantBySsn(clmAppClmntData
				.getSsn());
		ClaimantData clmntData;
		if (claimantBO != null) {
			clmntData = claimantBO.getClaimantData();
			/*
			 * Setting the SSA status in the claim app claimant data with the
			 * actual SSA status of the claimant
			 */
			if (StringUtility.isBlank(clmAppClmntData.getSsaStatus())) {
				if (StringUtility.isNotBlank(clmntData.getSsaStatus())) {
					clmAppClmntData.setSsaStatus(clmntData.getSsaStatus());
				} else {
					// in real time scenario this will never get excuted
					clmAppClmntData.setSsaStatus(SsaStatusEnum.N.getName());
				}
			}

		} else {
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "Start")
			clmntData = new ClaimantDataBridge();
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "End")
		}

		claimantBO = new Claimant(clmntData);
		/*
		 * R4UAT00002985 - For NBY Claims do NOT update the T_CLAIMANT_ADDRESS
		 * (with address from old Claim Application)
		 */
		if (!ClaimTypeEnum.NEW_BENEFIT_YEAR.getName().equals(
				clmAppData.getClaimType())
				&& !ClaimTypeEnum.AUTOMATED_INITIAL_CLAIM.getName().equals(
						clmAppData.getClaimType())) {
			claimantBO = claimantBO.processClaimant(clmAppClmntData);
		}

		if (StringUtility.isNotBlank(clmAppData.getAddessIssueComment())) {
			claimantBO.createBusinessProcessEventComment("FCIN",
					"ADDRESS_ISSUE",
					new String[] { clmAppData.getAddessIssueComment() },
					clmntData, true);
		}
		claimantBO.saveOrUpdate();
		claimantBO.flush();

		objAssembly.removeComponent(ClaimantDataBridge.class);
		objAssembly.addComponent(claimantBO.getClaimantData());
		if (StringUtility.isNotBlank(getUserContext().getUserType())) {
			// payment defect_41
			objAssembly.setPrimaryKey(claimantBO.getClaimantData().getPkId());
			this.createClaimantJobRequestAndSendMessage(objAssembly);
		} else {
			objAssembly.setPrimaryKey(claimantBO.getClaimantData().getPkId());
			objAssembly = this.createClaimantJobRequest(objAssembly);
			objAssembly = this.findClaimantMatchingJobs(objAssembly);
		}

		// to generate UI_501 Correspondence
		// if the claim is NBY do not generate UI-501

		// CIF_01273 Start Commented this correspondence related code as this is
		// MS specific.
		RegularClaim rcBO = new RegularClaim(null);

		// CIF_01273 End

		// if the page flow is from paper menu then it has to create issues
		// for creating issues
		String filePaperClaim = (String) objAssembly.getData("filePaperClaim");
		if (filePaperClaim != null) {
			objAssembly = this.initializeQuestionnaire(objAssembly);
		}
		// to generate business correspondence for the DUA application

		// 6124 defect fix to check if the claim is not AIC and DUA is filed
		// then only insert a comment.
		// for AIC claim the comment will be inserted through
		// processAdditionalClaim method,this fix is done to avoid the
		// duplication
		// of comments
		if (clmAppData.getDuaApplicationData() != null) {
			if (!ClaimApplicationTypeEnum.AIC.getName().equals(
					clmAppData.getClaimAppType())) {
				Object[] messageArguments = new Object[1];
				messageArguments[0] = clmAppData.getDuaApplicationData()
						.getDuaDeclarationData().getFemaNumber();
				clmAppBO.createBusinessProcessEventComment("FCIN", "DUA_FLD",
						messageArguments, clmntData);
			}
			/** Defect 5787 Generate DUA12 report Added 2007-11-07 **/
			// sort it in reverse order
			ArrayList clmAppEmpList = null;
			if (tempList != null && tempList.size() > 0) {
				clmAppEmpList = new ArrayList(tempList);
				Collections.sort(clmAppEmpList, Collections.reverseOrder());
			}

			// defect 6471 generate DUA1 added on 8/27/2007
			// CIF_01486 Commented on 11/20/2014 because this is not necessary
			// for view purpose as we have seperate jsp and action.
			// ClaimApplication.generateDUA1Report(clmntData, clmAppData,
			// clmAppEmpList, rcBO);

			// CIF_01486 Commented on 11/20/2014 because this is not necessary
			// for view purpose as we have seperate jsp and action.
			/*
			 * if(StringUtility.isNotBlank(clmAppData.getDuaApplicationData().
			 * getSelfEmployedFlag()) &&
			 * YesNoTypeEnum.NumericYes.getName().equals
			 * (clmAppData.getDuaApplicationData().getSelfEmployedFlag())){
			 * ClaimApplication.generateDUA12Report(clmntData, clmAppData,
			 * clmAppEmpList, rcBO); }
			 */
		}

		DuaCrBbean duabean = (DuaCrBbean) objAssembly
				.getFirstBean(DuaCrBbean.class);
		if (duabean != null
				&& ViewConstants.YES.equals(duabean.getAffetcedByDisaster())
				&& (!duabean.isBackDateDua())) {
			clmAppBO.getClaimApplicationData().setClaimAppStatus(
					ApplicationStatusEnum.DUA_PENDING.getName());
			DuaApplication duaAppBo = new DuaApplication(
					clmAppData.getDuaApplicationData());
			duaAppBo.createDuaWorkItem(clmAppData);
			return objAssembly;
		}
		try {
			final String claimType = (String) objAssembly.getData("claimType");
			if (StringUtility.isNotBlank(claimType)) {
				clmAppData.setClaimTypeSelected(claimType);
			}

			// payment defect_41
			BusInterruptRenewalData busIntrRenewData = objAssembly
					.getFirstComponent(BusInterruptRenewalData.class);
			if (busIntrRenewData != null) {
				clmAppData.setBusIntrRenewData(busIntrRenewData);
			}
			Set ibiqStates = (Set) objAssembly.getData("IBIQSTATES");
			if (null != ibiqStates) {
				clmAppData.setIbiqStates(ibiqStates);
			}

			// @cif_wy(storyNumber="P1-CM-036,P1-CM-057",requirementId="FR_1614",designDocName="02
			// Questionnaire.docx",designDocSection="1.1.4",dcrNo="DCR_MDDR_62", mddrNo="",impactPoint="Start")
			if (GlobalConstants.DB_ANSWER_YES
					.equals(clmAppData.getAnaMedical())) {
				Object lObject = MultiStateClassFactory.getObject(this
						.getClass().getName(), BaseOrStateEnum.STATE, null,
						null, Boolean.TRUE);
				if (null != lObject) {
					((CinService) lObject).checkMedicalQuestionaire(
							objAssembly, clmAppData);
				}

			}
			// @cif_wy(storyNumber="P1-CM-036,P1-CM-057",requirementId="FR_1614",designDocName="02
			// Questionnaire.docx",designDocSection="1.1.4",dcrNo="DCR_MDDR_62", mddrNo="",impactPoint="End")
			ClaimData clmData = ClaimApplication
					.processInitialClaimApplication(clmAppData, tempList,
							flowBean, clmntData);
			if (clmData != null) {
				objAssembly.addComponent(clmData, true);
			}
			// If the DUA_FLAG='1' then insert record in the
			// T_WAITING_WEEK_CONTROL table
			// with the latest DUA claim declaration data

			// Commented below as part of 3864, NBY should not insert waiting
			// week control record
			/*
			 * if(objAssembly.getData("DUAFLAG")!= null &&
			 * objAssembly.getData("DUAFLAG"
			 * ).equals(GlobalConstants.DB_ANSWER_YES)){ RegularClaim claimBO =
			 * new RegularClaim(clmData); claimantBO.flush(); DuaClaimData
			 * duaclaimdata =
			 * Claim.findLatestDuaClaimBySSN(objAssembly.getSsn());
			 * WaitingWeekControlData waitingweekcontroldata = new
			 * WaitingWeekControlData();
			 * waitingweekcontroldata.setClaimData(clmData);
			 * 
			 * waitingweekcontroldata.setDuaDeclarationData(duaclaimdata.
			 * getClaimApplicationData
			 * ().getDuaApplicationData().getDuaDeclarationData());
			 * waitingweekcontroldata
			 * .setEndDate(DateFormatUtility.parse(GlobalConstants
			 * .INDEFINITE_DATE));
			 * 
			 * Set waitingweekcontrolset = clmData.getWaitingWeekControls();
			 * if(waitingweekcontrolset == null) { waitingweekcontrolset = new
			 * HashSet(); } waitingweekcontrolset.add(waitingweekcontroldata);
			 * clmData.setWaitingWeekControls(waitingweekcontrolset);
			 * 
			 * claimBO.saveOrUpdate(); }
			 */
		} catch (BaseApplicationException e) {
			/*
			 * added as part of termination of work items if some transaction
			 * errors occur
			 */
			GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
					.fetchORCreateBean(GenericWorkflowSearchBean.class);
			bean.setGlobalContainerMemberLike("ssneanfein", clmntData.getSsn());
			bean.setGlobalContainerMember("type", "ssn");
			bean.setReceivedTimeBetween(new Date(),
					DateUtility.getNextDay(new Date()));
			/*
			 * WorkItemTerminatingFromClaimBean workItemBean = new
			 * WorkItemTerminatingFromClaimBean();
			 * workItemBean.setDateComp(bean.getComparator());
			 * workItemBean.setDueDate(bean.getDueDate());
			 * workItemBean.setFilter(bean.getQuery());
			 * workItemBean.setQueryResultThreshold
			 * (bean.getQueryResultThreshold());
			 * workItemBean.setSortCriteria(bean.getSortCriteria());
			 */
			RegularClaim claimBO = null;
			claimBO = new RegularClaim(null);
			claimBO.terminateWorkItems(bean);
			objAssembly.addBusinessError(e.getMessage());
			flowBean.setDisplayDetermination(false);
			throw new BaseRunTimeException(e);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with MassLayoffData as input and updates
	 * appropriate record in database
	 * 
	 * @param objAssembly
	 *            with MassLayoffData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateMassLayoffsData(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateMassLayoffsData");
		}

		MassLayoffData massLayoffsData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);

		MassLayoff layoffBO = new MassLayoff(massLayoffsData);
		layoffBO.saveOrUpdateMassLayoffsData();

		return objAssembly;
	}

	/**
	 * @throws ParseException
	 *             This method takes ObjectAssembly with data for questionnaires
	 *             as input and saves/updates appropriate record in database
	 * 
	 * @param objAssembly
	 *            with data from questionnaires
	 * @return objAssembly
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws
	 */
	public IObjectAssembly saveOrUpdateQuestionnaire(IObjectAssembly objAssembly)
			throws IllegalArgumentException, IllegalAccessException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateQuestionnaire");
		}

		QuestionnaireBO qnrBO = new QuestionnaireBO();
		/* CIF_00323:Start */
		DateFormat formatter;
		Date date = null;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		qnrBO.createOrUpdateResponse(objAssembly);
		qnrBO.updatePotentialIssue(objAssembly);
		// CIF_02901 Start: Defect_4046 - EB work search will not be populated
		// from questionnire.
		/*
		 * List<EBWorksearchBean> newBeanList =
		 * objAssembly.getBeanList(EBWorksearchBean.class); String ssn=
		 * objAssembly.getSsn(); if(newBeanList!=null && newBeanList.size()>0){
		 * Claimant claimantBO = Claimant.findBySsn(ssn); Long pkID =
		 * claimantBO.getClaimantData().getPkId(); WeeklyCertData wCertData
		 * =WeeklyCert.getCertifiedWeeklyCertForClaimantAndrefusedJob(pkID);
		 * 
		 * Long weeklyCertID = wCertData.getWeeklyCertId(); for (Iterator
		 * iterator = newBeanList.iterator(); iterator.hasNext();) {
		 * EBWorksearchBean ebWorksearchBean = (EBWorksearchBean) iterator
		 * .next(); WcEbWorkSearchData wcEbWorkSrc = new WcEbWorkSearchData();
		 * wcEbWorkSrc.setWeeklyCertData(wCertData);
		 * wcEbWorkSrc.setResults(ebWorksearchBean.getResults()); String
		 * dt=String.valueOf(ebWorksearchBean.getContactDate()); try { date=
		 * formatter.parse(dt); } catch (ParseException e) { // TODO
		 * Auto-generated catch block LOGGER.error("Message ",e); }
		 * wcEbWorkSrc.setSearchDate(date);
		 * wcEbWorkSrc.setTypeOfContact(ebWorksearchBean.getContactType());
		 * wcEbWorkSrc.setTypeOfWork(ebWorksearchBean.getWorkType());
		 * wcEbWorkSrc.setCompanyName(ebWorksearchBean.getCompanyDetails());
		 * wcEbWorkSrc.setContactName(ebWorksearchBean.getPersonName()); //
		 * saveOrUpdate(wcEbWorkSrc); WeeklyCertDAO dao = new WeeklyCertDAO();
		 * 
		 * dao.saveOrUpdate(wcEbWorkSrc);
		 * 
		 * 
		 * } }
		 */
		// CIF_02901 End: Defect_4046
		/* CIF_00323:End */
		return objAssembly;
	}

	public IObjectAssembly getDataFOrMassLayoff(IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDataFOrMassLayoff");
		}
		Long pkVal = objectAssembly.getPrimaryKeyAsLong();
		if (null != pkVal) {
			MassLayoff massLayoffBo = new MassLayoff(null);
			massLayoffBo = MassLayoff.findByPrimaryKey(pkVal);
			MassLayoffData layoffData = massLayoffBo.getMassLayoffData();
			objectAssembly.addComponent(layoffData, true);

		}
		return objectAssembly;
	}

	public IObjectAssembly updateManagersDecision(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateManagersDecision");
		}
		MassLayoffData massLayoffData = objAssembly
				.getFirstComponent(MassLayoffData.class);
		MassLayoff massLayoffBO = new MassLayoff(massLayoffData);
		String persistenceOid = objAssembly.getData("WorkFlowId").toString();
		String userId = (String) objAssembly.getData("userId");
		massLayoffBO.saveOrUpdateMassLayoffsData();
		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowConstants.TASK_ID, persistenceOid);
		map.put(WorkflowConstants.USER_ID, userId);
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(), map);
		// BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
		// workflowDAO.startAndCompleteWorkitem(persistenceOid, userId);
		// workflowDAO.completeWorkitem(persistenceOid, userId);
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with objAssembly as input and
	 * saves/updates appropriate record in database
	 * 
	 * @param objAssembly
	 *            with MassLayoffData, MultiClaimantEmployerData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateReasonForLayoffsData(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateReasonForLayoffsData");
		}

		MassLayoffData massLayoffData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);

		MassLayoff massLayoffBO = new MassLayoff(massLayoffData);
		Calendar lastWorkDate = Calendar.getInstance();
		lastWorkDate.setTime(massLayoffData.getLastDateOfWork());
		Calendar recallDate = Calendar.getInstance();
		recallDate.setTime(massLayoffData.getReturnDate());
		Calendar claimEffectDate = Calendar.getInstance();
		claimEffectDate.setTime(massLayoffData.getMassLayoffClaimEffDate());

		Integer laidoffWeeks = DateUtility.getWeeksBetween(lastWorkDate,
				recallDate);
		Integer notifWeeks = DateUtility.getWeeksBetween(claimEffectDate,
				Calendar.getInstance());
		if (claimEffectDate.getTime().before(Calendar.getInstance().getTime())) {
			notifWeeks = (-1 * notifWeeks);
		}

		/*
		 * KnowledgeBase knowledgeBase = GetDroolsInstance.getKnowledgeBase();
		 * StatefulKnowledgeSession ksession = knowledgeBase
		 * .newStatefulKnowledgeSession();
		 */

		// CIF_01208 start
		IFactModel mass = new MasslayoffclaimFact();
		((MasslayoffclaimFact) mass).setClaimantLaidOffWeeks(laidoffWeeks);
		((MasslayoffclaimFact) mass).setNotificationWeeks(notifWeeks);
		((MasslayoffclaimFact) mass).setNumberofWorkers(Integer
				.valueOf(massLayoffData.getNumberInvolved()));

		IFactResponseModel response = new MassLayoffRuleResponse();
		((MassLayoffRuleResponse) response).setIsEligible(false);

		// execute the business rule
		GetDroolsInstance.executeBusinessRule(mass, response);
		// CIF_01208 end

		LOGGER.info("Employer eligibility: ----  "
				+ ((MassLayoffRuleResponse) response).getIsEligible());
		// RuleFactResponse responseValue = (RuleFactResponse)
		// responseList.get(0);

		Boolean isEligible = ((MassLayoffRuleResponse) response)
				.getIsEligible();
		if (isEligible) {
			massLayoffData.setStatusFlag("1");
		} else {
			massLayoffData.setStatusFlag("0");
		}
		massLayoffBO.saveOrUpdateMassLayoffsData();

		if (objAssembly.getFirstComponent(MultiClaimantEmployerData.class) != null) {
			MultiClaimantEmployerData multiClmntEmprData = (MultiClaimantEmployerData) objAssembly
					.getFirstComponent(MultiClaimantEmployerData.class);
			MultiClaimantEmployer multiClmntEmprBO = new MultiClaimantEmployer(
					multiClmntEmprData);
			multiClmntEmprBO.saveOrUpdate();
		}

		if (!isEligible) {
			this.createWorkFLow(objAssembly);
		}
		// System.out.println("After MassLayoff execution == " +
		// response.getIsEligible());
		objAssembly.addData("workflowInitiated",
				!((MassLayoffRuleResponse) response).getIsEligible());
		return objAssembly;
	}

	private void createWorkFLow(IObjectAssembly objectAssembly) {
		RegisteredEmployerData regEmpData = objectAssembly
				.getFirstComponent(RegisteredEmployerData.class);
		MassLayoffData layoffData = objectAssembly
				.getFirstComponent(MassLayoffData.class);

		// BaseAccessDsContainerBean bean = new BaseAccessDsContainerBean();
		// GlobalDsContainerBean globalBean =
		// bean.getNewGlobalDsContainerBean();
		/*
		 * if(StringUtility.isNotBlank(regEmpData.getEan())) {
		 * globalBean.setTypeAsEan();
		 * globalBean.setSsneanfein(regEmpData.getEan()); }
		 * globalBean.setName(regEmpData.getEmployerName());
		 * globalBean.setBusinessKey(layoffData.getPkId().toString());
		 * 
		 * Set set = regEmpData.getEmployerContactData(); for (Object object :
		 * set) { EmployerContactData empcontactData = (EmployerContactData)
		 * object; globalBean.setZipCode(empcontactData.getZip()); break; }
		 * BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
		 * 
		 * workflowDAO.createAndStartProcessInstance(
		 * WorkflowProcessTemplateConstants
		 * .Claims.MASS_LAYOFF_INELIGIBILITY,bean);
		 */

		// BaseAccessDsContainerBean baseBean = new BaseAccessDsContainerBean();
		// GlobalDsContainerBean globalBean =
		// baseBean.getNewGlobalDsContainerBean();

		Map<String, Object> mapValues = new HashMap<String, Object>();
		// Map<String,Object> globalbeanMap = new HashMap<String,Object>();

		// globalBean.setTypeAsEan();
		mapValues.put(WorkflowConstants.TYPE,
				WorkflowConstants.BUSINESS_TYPE.EAN);
		// globalBean.setSsneanfein(regEmpData.getEan());
		mapValues.put(WorkflowConstants.SSN_EAN_FEIN, regEmpData.getEan());

		// globalBean.setName(regEmpData.getEmployerName());
		mapValues.put(WorkflowConstants.NAME, regEmpData.getEmployerName());
		// globalBean.setBusinessKey(layoffData.getPkId().toString());
		mapValues.put(WorkflowConstants.BUSINESS_KEY, layoffData.getPkId()
				.toString());
		Set set = regEmpData.getEmployerContactData();
		for (Object object : set) {
			EmployerContactData empcontactData = (EmployerContactData) object;
			// globalBean.setZipCode(empcontactData.getZip());
			mapValues.put(WorkflowConstants.ZIP_CODE, empcontactData.getZip());
			break;
		}

		// / baseBean.setGlobalDs(globalBean);

		// globalbeanMap.put("globalBean", globalBean);
		// globalbeanMap.put("businessKey", globalBean.getBusinessKey());
		// globalbeanMap.put("businessData",globalBean.getBusinessData());
		// globalbeanMap.put("name",globalBean.getName());
		// globalbeanMap.put("ssneanfein",globalBean.getSsneanfein());
		// globalbeanMap.put("type",globalBean.getType());
		// globalbeanMap.put("zipCode",globalBean.getZipCode());

		// *3.set the baseBean to the mapValues.
		// mapValues.put("userId",baseBean.getUserId());
		// mapValues.put("supervisorUserId",baseBean.getSupervisorUserId());
		// mapValues.put("holdDate",baseBean.getHoldDate());
		// mapValues.put("firstNotificationDueTime",baseBean.getFirstNotificationDueTime());
		// mapValues.put("forward",baseBean.getForward());
		// mapValues.put("nextActivity",baseBean.getNextActivity());
		// mapValues.put("globalDs",globalbeanMap);
		// mapValues.put("dolTimeLapse",baseBean.getDolTimeLapse());
		// mapValues.put("comment",baseBean.getComment());
		mapValues
				.put(WorkflowConstants.PROCESS_NAME,
						WorkflowProcessTemplateConstants.Claims.MASS_LAYOFF_INELIGIBILITY);
		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.CREATE_WORKITEM.getName(), mapValues);

	}

	public IObjectAssembly saveOrUpdateAutoIssueCutoffDate(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateAutoIssueCutoffDate");
		}

		MassLayoffData massLayoffData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);

		MassLayoff massLayoffBO = new MassLayoff(massLayoffData);
		massLayoffBO.saveOrUpdateMassLayoffsData();

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with GenericSearchBean as input and
	 * returns a list of DynaBean objects
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean
	 * @return objAssembly
	 */
	public IObjectAssembly getSsnForMassLayoffs(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSsnForMassLayoffs");
		}

		GenericSearchService search = new GenericSearchService();
		search.getSearchResult(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SsnForMassLayoffsData as input and
	 * saves, or updates appropriate record(s) in database
	 * 
	 * @param objAssembly
	 *            with ReasonForLayoffsData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateSsnForMassLayoffsData(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSsnForMassLayoffsData");
		}

		MassLayoffData layoffData = (MassLayoffData) objAssembly
				.fetchORCreate(MassLayoffData.class);

		MassLayoff layoffBO = new MassLayoff(layoffData);
		// the the set of ssn beans from the transfer object
		Set ssnSet = (Set) objAssembly.getData("ssnSet");
		layoffBO.saveOrUpdateSsnForMassLayoffsData(ssnSet);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SsnForMassLayoffsData as input and
	 * saves, or updates appropriate record(s) in database
	 * 
	 * @param objAssembly
	 *            with ReasonForLayoffsData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateAddSsnForMassLayoffsData(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateAddSsnForMassLayoffsData");
		}

		MassLayoffData layoffData = (MassLayoffData) objAssembly
				.fetchORCreate(MassLayoffData.class);
		MassLayoff layoffBO = new MassLayoff(layoffData);
		// the the set of ssn beans from the transfer object
		List ssnBeanList = (ArrayList) objAssembly.getData("ssnBeanList");

		layoffBO.saveOrUpdateAddSsnForMassLayoffsData(ssnBeanList);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with GenericSearchBean as input and
	 * returns a list of DynaBean objects
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean
	 * @return objAssembly
	 */
	public IObjectAssembly getMassLayoffDataForEmployer(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getMassLayoffDataForEmployer");
		}

		GenericSearchService search = new GenericSearchService();
		search.getSearchResult(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with GenericSearchBean or a pkId for
	 * MassLayoffData as input and returns a list of DynaBean objects or a
	 * MassLayoffData object
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean
	 * @return objAssembly
	 */
	public IObjectAssembly getInfoForNotifyMassLayoffs(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getInfoForNotifyMassLayoffs");
		}

		Long pk = objAssembly.getPrimaryKeyAsLong();
		MassLayoffData layoffData;
		if (pk != null) {
			layoffData = MassLayoff.getMassLayoffsDataByPk(pk);
		} else {
			layoffData = new MassLayoffData();
		}
		MassLayoff layoffBO = new MassLayoff(layoffData);
		if (layoffBO.isNew()
				&& objAssembly.isBeanPresent(GenericSearchBean.class)) {
			GenericSearchService search = new GenericSearchService();
			search.getSearchResult(objAssembly);
		} else {
			objAssembly.removeComponent(MassLayoffData.class);
			objAssembly.addComponent(layoffData);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with a ssn as input and returns a
	 * ClaimantData object that contains info from the database or only the ssn
	 * 
	 * @param objAssembly
	 *            with ssn
	 * @return objAssembly with ClaimantData object
	 */
	public IObjectAssembly getClaimantDataForMassLayoffs(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimantDataForMassLayoffs");
		}

		String ssn = objAssembly.getSsn();
		Claimant claimantBO = Claimant.findBySsn(ssn);

		objAssembly.removeComponent(ClaimantDataBridge.class);
		objAssembly.removeBusinessError();
		if (claimantBO != null) {
			objAssembly.addComponent(claimantBO.getClaimantData());
		} else {
			// //@cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "Start")
			ClaimantData claimantData = new ClaimantDataBridge();
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "End")
			claimantData.setSsn(ssn);
			objAssembly.addComponent(claimantData);
		}

		// If ClaimApplicationData object does NOT exist,
		// check status of active Claim, The following method returns the active
		// claim in descending order of BYE date.
		List claimList = Claim.fetchClaimBySsn(ssn);
		ClaimData claimData = null;
		if (claimList != null) {
			for (int i = 0; i < claimList.size(); i++) {
				ClaimData data = (ClaimData) claimList.get(i);
				if (data instanceof RegularClaimData) {
					claimData = data;
					break;
				}
			}
		}
		// If there is claimData with status is equal to Active and valid BYE

		if (claimData != null) {
			Claim claim = Claim.getClaimBoByClaimData(claimData);
			if (!claim.isByeExpired(new Date())) {
				int numWeeks = 0;
				try {
					WeeklyCertificationValidationDataBean wcBean = WeeklyCert
							.determineEligibilityToFileWeeklyCertification(ssn,
									null);

					if (wcBean.isCurrentWeek()) {
						numWeeks = 1;
					} else if (wcBean.isTwoWeeksPending()) {
						numWeeks = 2;
					} else if (wcBean.isMorethanTwoWeeks()) {
						numWeeks = 3;
					}
				} catch (BaseApplicationException e) {

				}

				if (numWeeks <= 2) {
					objAssembly
							.addBusinessError("error.access.getorcreateclaimapp.zerooroneweektobefilled");
				}
				// objAssembly
				// .addBusinessError("error.access.enterssnformasslayoff.activeclaim");
				// }
			}
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with a ClaimantData object as input and
	 * saves or updates it
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateFileMassLayoffsInfo(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateFileMassLayoffsInfo");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		MassLayoffData layoffData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);
		RegisteredEmployerData regEmprData = (RegisteredEmployerData) objAssembly
				.getFirstComponent(RegisteredEmployerData.class);

		// Set the effective date based on the masslayoff claim effective date.
		// Compute the base period.
		clmAppData
				.setClaimEffectiveDate(layoffData.getMassLayoffClaimEffDate());
		Calendar tempCal = new GregorianCalendar();
		tempCal.setTime(clmAppData.getClaimEffectiveDate());
		Calendar[] qDate = RegularClaim.determineBasePeriod(tempCal);
		clmAppData.setBpStartDate(qDate[0]);
		clmAppData.setBpEndDate(qDate[1]);
		// to mark the claim application pending and set File Date
		clmAppData.setClaimAppStatus(ApplicationStatusEnum.PENDING.getName());
		clmAppData.setFileDate(new Date());

		clmAppClmntData.setClaimApplicationData(clmAppData);
		clmAppData.setClaimApplicationClaimantData(clmAppClmntData);

		ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		// cq R4UAT00014119
		// clmAppBO.saveOrUpdate();
		// clmAppBO.flush();

		Set massLayoffWeeks = layoffData.getMassLayoffWeeks();
		boolean designatedVacnReportedFlag = false;
		// CIF_02910 adding null check
		if (null != massLayoffWeeks && !massLayoffWeeks.isEmpty()) {
			for (Iterator itr = massLayoffWeeks.iterator(); itr.hasNext();) {
				MassLayoffWeeksData massLayoffWeeksData = (MassLayoffWeeksData) itr
						.next();
				if ("VACD".equals(massLayoffWeeksData.getReason())) {
					designatedVacnReportedFlag = true;
					break;
				}
			}
		}

		boolean leadCaseExists = false;
		if (designatedVacnReportedFlag) {
			List masslayoffAicReopenList = clmAppBO
					.getMasslayoffAicReopens(layoffData.getPkId());

			if (masslayoffAicReopenList != null
					&& !masslayoffAicReopenList.isEmpty()) {
				for (Iterator i = masslayoffAicReopenList.iterator(); i
						.hasNext();) {
					AicReopenData aicReopenData = (AicReopenData) i.next();
					List multiIssueList = Issue
							.getMultiClaimantIssueByEmprIdandClmId(regEmprData
									.getEmployerId(), aicReopenData
									.getClaimData().getClaimId());
					if (multiIssueList != null && !multiIssueList.isEmpty()) {
						for (Iterator j = multiIssueList.iterator(); j
								.hasNext();) {
							IssueData issueData = (IssueData) j.next();
							if (YesNoTypeEnum.NumericYes.getName().equals(
									issueData.getLeadCaseFlag())) {
								leadCaseExists = true;
								break;
							}
						}
					}
				}
			}
			if (!leadCaseExists) {
				List masslayoffClaimList = Claim
						.getClaimsByMasslayoffId(layoffData.getPkId());
				if (masslayoffClaimList != null
						&& !masslayoffClaimList.isEmpty()) {
					for (Iterator i = masslayoffClaimList.iterator(); i
							.hasNext();) {
						ClaimData claimData = (ClaimData) i.next();
						List multiIssueList = Issue
								.getMultiClaimantIssueByEmprIdandClmId(
										regEmprData.getEmployerId(),
										claimData.getClaimId());
						if (multiIssueList != null && !multiIssueList.isEmpty()) {
							for (Iterator j = multiIssueList.iterator(); j
									.hasNext();) {
								IssueData issueData = (IssueData) j.next();
								if (YesNoTypeEnum.NumericYes.getName().equals(
										issueData.getLeadCaseFlag())) {
									leadCaseExists = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		List weekList = objAssembly
				.getBeanList(MassLayoffWeeksClaimedBean.class);
		MassLayoff layoffBO = new MassLayoff(layoffData);
		RegularClaimData regClaimData = layoffBO.processMassLayoffClaim(
				clmAppData, clmAppClmntData, weekList);
		// CIF_02957 || Jira : UIM-4138
		/*
		 * if(!ClaimApplicationTypeEnum.NEW.getName().equals(clmAppData.
		 * getClaimAppType())){ regClaimData =
		 * Claim.findLastActiveRegularClaimDataBySsn(objAssembly.getSsn()); }
		 */
		// CIF_01273 Start Commented block of code as this MS specific.

		/*
		 * if((regClaimData != null &&
		 * ClaimApplicationTypeEnum.NEW.getName().equals
		 * (clmAppData.getClaimAppType())) ||
		 * !ClaimApplicationTypeEnum.NEW.getName
		 * ().equals(clmAppData.getClaimAppType())){
		 */

		// CIF_01158 Code has been commented to Issue Category and Issue Sub
		// Category has been removed from the Enum
		/*
		 * if(StringUtility.isNotBlank(clmAppData.getMassLayoffMultiClaimantFlag(
		 * ))){ IssueBean issueBean = new IssueBean();
		 * issueBean.setClaimantSsn(objAssembly.getSsn());
		 * issueBean.setDateIssueDetected(new Date());
		 * issueBean.setEan(regEmprData.getEan());
		 * issueBean.setEmployerId(regEmprData.getEmployerId());
		 * issueBean.setEmployerName(regEmprData.getDisplayName());
		 * issueBean.setInformationProvidedBy("CLMT");
		 * issueBean.setInformationProvidedHow("PHCL");
		 * issueBean.setIssueDescription
		 * (IssueCategoryEnum.MULTI_CLAIMANT_EMPLOYER.getName());
		 * issueBean.setIssueDetails
		 * (IssueSubCategoryEnum.DESIGNATED_VACATION.getName());
		 * issueBean.setIssueSource(IssueSourceEnum.MASS_LAYOFF.getName());
		 * 
		 * Date lastCWEDate = (Date)objAssembly.getData("lastCWEDate");
		 * issueBean.setIssueEndDate(lastCWEDate);
		 * 
		 * Date firstCWE = (Date)objAssembly.getData("firstCWEDate"); Calendar
		 * calFirstCWE = new GregorianCalendar(); calFirstCWE.setTime(firstCWE);
		 * Calendar multiClaimantIssueStDate =
		 * DateUtility.getFirstDateOfWeek(calFirstCWE);
		 * 
		 * issueBean.setIssueStartDate(multiClaimantIssueStDate.getTime());
		 * 
		 * issueBean.setMasslayoffLastDayWorked(layoffData.getLastDateOfWork());
		 * 
		 * if(YesNoTypeEnum.NumericYes.getName().equals(regClaimData.
		 * getMonEligibleFlag()) && !leadCaseExists &&
		 * YesNoTypeEnum.NumericYes.getName
		 * ().equals(clmAppData.getMassLayoffMultiClaimantFlag())){
		 * 
		 * issueBean.setLeadCaseFlag(YesNoTypeEnum.NumericYes.getName());
		 * issueBean
		 * .setMultiClaimantIssueFlag(YesNoTypeEnum.NumericYes.getName());
		 * }else{ if(YesNoTypeEnum.NumericYes.getName().equals(clmAppData.
		 * getMassLayoffMultiClaimantFlag())){
		 * issueBean.setLeadCaseFlag(YesNoTypeEnum.NumericNo.getName());
		 * issueBean
		 * .setMultiClaimantIssueFlag(YesNoTypeEnum.NumericYes.getName());
		 * }else{ issueBean.setLeadCaseFlag(YesNoTypeEnum.NumericNo.getName());
		 * issueBean
		 * .setMultiClaimantIssueFlag(YesNoTypeEnum.NumericNo.getName()); } } //
		 * CQ 8203 issueBean.setClaimid(regClaimData.getClaimId());
		 * Issue.createIssue(issueBean); }
		 */

		// Create Future Issue - Return to work
		/*
		 * IssueBean futureIssueBean = new IssueBean();
		 * futureIssueBean.setClaimantSsn(objAssembly.getSsn());
		 * futureIssueBean.setDateIssueDetected(new Date());
		 * futureIssueBean.setEan(regEmprData.getEan());
		 * futureIssueBean.setEmployerId(regEmprData.getEmployerId());
		 * futureIssueBean.setEmployerName(regEmprData.getDisplayName());
		 * futureIssueBean.setInformationProvidedBy("CLMT");
		 * futureIssueBean.setInformationProvidedHow("PHCL");
		 * futureIssueBean.setIssueDescription
		 * (IssueCategoryEnum.FUTURE_ISSUE.getName());
		 * futureIssueBean.setIssueDetails
		 * (IssueSubCategoryEnum.RETURN_TO_WORK.getName());
		 * futureIssueBean.setIssueEndDate
		 * (DateFormatUtility.parse(GlobalConstants.INDEFINITE_DATE));
		 * futureIssueBean
		 * .setMasslayoffLastDayWorked(layoffData.getLastDateOfWork());
		 * futureIssueBean
		 * .setIssueSource(IssueSourceEnum.MASS_LAYOFF.getName()); Calendar
		 * futureIssueStartDate = new GregorianCalendar();
		 * futureIssueStartDate.setTime(layoffData.getReturnDate());//changed
		 * for CIF_00033 futureIssueStartDate.add(Calendar.DATE,1);
		 * futureIssueBean.setIssueStartDate(futureIssueStartDate.getTime()); //
		 * CQ 8203 futureIssueBean.setClaimid(regClaimData.getClaimId());
		 * Issue.createIssue(futureIssueBean);
		 * 
		 * CorrespondenceData corr501MData = new CorrespondenceData();
		 * corr501MData
		 * .setCorrespondenceCode(CorrespondenceCodeEnum.UI_501M.getName());
		 * corr501MData.setParameter1(objAssembly.getSsn());
		 * corr501MData.setParameter6(clmAppData.getPkId().toString());
		 * corr501MData
		 * .setDirection(CorrespondenceDirectionEnum.OUTGOING.getName());
		 * Correspondence corr501MBO = new Correspondence(corr501MData);
		 * corr501MBO.saveOrUpdate();
		 * 
		 * //To generate BRI Statement correspondence self mailer
		 * CorrespondenceData briStatementdata = new CorrespondenceData();
		 * briStatementdata.setClaimData(null);
		 * briStatementdata.setClaimantData(regClaimData.getClaimantData());
		 * briStatementdata
		 * .setCorrespondenceCode(CorrespondenceCodeEnum.BRI_STATEMENT
		 * .getName()); briStatementdata.setEmployerData(new
		 * RegisteredEmployerData());
		 * briStatementdata.setClaimAppEmployerData(new
		 * ClaimApplicationEmployerData());
		 * briStatementdata.setClaimAppClmntData
		 * (clmAppData.getClaimApplicationClaimantData());
		 * briStatementdata.setDirection
		 * (CorrespondenceDirectionEnum.OUTGOING.getName());
		 * briStatementdata.setMailingAddress
		 * (clmAppData.getClaimApplicationClaimantData().getMailAddress());
		 * briStatementdata
		 * .setParameter1(DateFormatUtility.format(clmAppData.getClaimEffectiveDate
		 * ()));
		 * briStatementdata.setParameter6(clmAppData.getPkId().toString());
		 * 
		 * // insert the correspondence into the database IJdbcClaimDAO
		 * jdbcClaimDAO = DAOFactory.instance.getJdbcClaimDAO();
		 * jdbcClaimDAO.saveCorrespondence(briStatementdata); }
		 */
		// CIF_01273 End
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationClaimantData as
	 * input, request INS verification, if it is necessay, and saves
	 * ClaimApplicationClaimantData, or updates it, if it already exists.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationClaimantData
	 * @return objAssembly with ClaimApplicationClaimantData
	 */
	public IObjectAssembly processClaimant(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processClaimant");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class, true);
		// to save Claim Application Claimant
		if (ViewConstants.YES.equals(clmAppData.getClaimTypeDua())) {
			objAssembly = this.saveOrUpdateDuaApplication(objAssembly);
		} else {
			this.saveOrUpdateClmAppClmnt(objAssembly);
		}

		if (UserTypeEnum.MDES.getName().equals(
				super.getUserContext().getUserType())) {
			Claimant claimant = Claimant.fetchClaimantBySsn(objAssembly
					.getSsn());
			if (claimant != null) {
				ClaimantData claimantData = claimant.getClaimantData();
				if (StringUtility.isNotBlank(claimantData.getUserId())) {
					objAssembly.addData(
							GlobalConstants.SHOW_EMAIL_IN_CORRESPONDENCE_MODE,
							Boolean.TRUE);
				} else {
					objAssembly.addData(
							GlobalConstants.SHOW_EMAIL_IN_CORRESPONDENCE_MODE,
							Boolean.FALSE);
				}
			}
		} else {
			objAssembly.addData(
					GlobalConstants.SHOW_EMAIL_IN_CORRESPONDENCE_MODE,
					Boolean.TRUE);
		}

		// to generate request for verification to INS, if necessary

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge data = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		if (GlobalConstants.DB_ANSWER_NO.equals(data.getCitizenship())) {
			ClaimApplicationClaimant claimAppClmnt = new ClaimApplicationClaimant(
					data);
			claimAppClmnt.processInsRequest();

		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input, and
	 * returns ClaimApplicationClaimantData based on ClaimApplicationData.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with ClaimApplicationClaimantData
	 */
	public IObjectAssembly getClmAppClmnt(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClmAppClmnt");
		}

		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		ClaimApplicationClaimant claimAppClmntBO = null;
		// To retrieve ClaimApplicationClaimant object based on foreign key
		// from ClaimApplicationData
		if (claimAppData.getPkId() != null) {
			claimAppClmntBO = ClaimApplicationClaimant
					.findByClaimAppId(claimAppData.getPkId());
		}
		objAssembly.removeComponent(ClaimApplicationClaimantDataBridge.class);
		if (claimAppClmntBO != null) {
			objAssembly.addComponent(claimAppClmntBO
					.getClaimApplicationClaimantData());
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input, gets
	 * the base period start and end date so the the quarters can be calculated,
	 * sets these values in RegularClaimData, and returns RegularClaimData.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly with RegularClaimData
	 */
	public IObjectAssembly getBasePeriod(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getBasePeriod");
		}

		ClaimApplicationDataBridge claimApp = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		RegularClaimData claimData = (RegularClaimData) objAssembly
				.fetchORCreate(RegularClaimData.class);

		Calendar startDate = new GregorianCalendar();
		startDate.setTime(claimApp.getClaimEffectiveDate());
		Calendar[] qDate = RegularClaim.determineBasePeriod(startDate);
		claimData.setBasePeriodStartDate(qDate[0].getTime());
		claimData.setBasePeriodEndDate(qDate[1].getTime());

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ReconsiderationRequestData and
	 * ClaimApplicationEmployerData as input and saves it, or updates it if it
	 * is already exists.
	 * 
	 * @param objAssembly
	 *            with ReconsiderationRequestData, and
	 *            ClaimApplicationEmployerData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateWageReconsiderationAppData(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateWageReconsiderationAppData");
		}

		Form525BData form525BData = (Form525BData) objAssembly
				.fetchORCreate(Form525BData.class);
		ClaimApplicationEmployerDataBridge empData = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);

		form525BData.setClmAppEmpData(empData);

		/*
		 * escon 77: workflow related Stuff Commented Out.
		 */

		// CIF_00158
		// Start:uncommented this line
		Form525B form525BO = new Form525B(form525BData);
		form525BO.saveOrUpdate();
		// CIF_00158:End
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimantApplicationData and list of
	 * ClaimApplicationEmployerData as input, initialize appropriate
	 * questionnaires as PotentialIssueData.
	 * 
	 * @param objAssembly
	 *            with ClaimantApplicationData, and list of
	 *            ClaimApplicationEmployerData
	 * @return objAssembly with PotentialIssueData
	 */
	public IObjectAssembly initializeQuestionnaire(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method initializeQuestionnaire");
		}
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		List clmAppEmpList = ClaimApplicationEmployer
				.findClmAppEmpListByClmAppId(clmAppData.getPkId());

		ClaimApplication claimAppBO = new ClaimApplication(clmAppData);
		// List clmAppEmpListQuestionnaire = new ArrayList();

		List clmAppNo8x = (List) objAssembly.getData("CLMEMP_8X");
		List requalificationBeanList = objAssembly
				.getBeanList(CinRequalificationCriteriaBean.class);
		// Remove the claim application employers where 8 time
		// except for last employer.

		List list = claimAppBO.initializeQuestionnaire(clmAppEmpList,
				clmAppNo8x, requalificationBeanList);

		objAssembly.removeComponent(PotentialIssueData.class);
		if ((list != null) && (list.size() > 0)) {
			objAssembly.addComponentList(list);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimantApplicationData as input,
	 * sets appropriate flow, and saves/updates ClaimantApplicationData.
	 * 
	 * @param objAssembly
	 *            with ClaimantApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly processClaimEffectiveDate(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processClaimEffectiveDate");
		}

		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		ClaimApplication claimAppBO = ClaimApplication
				.determineBackDate(claimAppData);

		// If ClaimApplicationData object does NOT exist,
		// check status of active Claim, The following method returns the active
		// claim in descending order of BYE date.
		List claimList = Claim.fetchClaimBySsn(objAssembly.getSsn());
		ClaimData claimData = null;

		if (claimList != null) {
			for (int i = 0; i < claimList.size(); i++) {
				ClaimData data = (ClaimData) claimList.get(i);
				if (data instanceof RegularClaimData) {
					claimData = data;
					break;
				}
			}
		}

		// If there is claimData with status is equal to Active and valid BYE

		if (claimData != null) {
			Claim claim = Claim.getClaimBoByClaimData(claimData);
			if (!claim.isByeExpired(new Date())) {
				// If there is no break or break is less than is allowed
				// set status as "Active Claim"
				if (WeeklyCert.determineBreak() == 0) {
					claimAppData
							.setFlowType(GlobalConstants.CIN_CLAIM_STATUS_ACC);
				} else {
					claimAppData
							.setFlowType(ClaimAppFlowTypeEnum.INT.getName());
				}
				// If there is no claimData with status is equal to Active and
				// valid
				// BYE
			}
		}

		if (ClaimAppFlowTypeEnum.UIT.getName().equals(
				claimAppData.getFlowType())) {
			Calendar startDate = new GregorianCalendar();
			startDate.setTime(claimAppData.getClaimEffectiveDate());
			Calendar[] qDate = RegularClaim.determineBasePeriod(startDate);
			claimAppData.setBpStartDate(qDate[0]);
			claimAppData.setBpEndDate(qDate[1]);
		} else if ((ClaimApplicationTypeEnum.AIC.getName().equals(
				claimAppData.getClaimAppType()) || (ClaimAppFlowTypeEnum.INT
				.getName().equals(claimAppData.getFlowType())))) {

			// Set the base period start date as the last claim effective date
			// so that rest of the application works properly.
			// CQ R4UAT00015390 on 06/06/2011
			Calendar startDate = new GregorianCalendar();
			List aicList = Claim.findAicReopenByClaimId(claimData.getClaimId());
			if (aicList != null && aicList.size() > 0) {
				AicReopenData aicReopenData = (AicReopenData) aicList.get(0);
				startDate.setTime(aicReopenData.getEffectiveDate());
				startDate.setTime(claimData.getEffectiveDate());
				Calendar[] qDate = RegularClaim.determineBasePeriod(startDate);
				claimAppData.setBpStartDate(qDate[0]);
				claimAppData.setBpEndDate(qDate[1]);
				Calendar calBp = Calendar.getInstance();
				calBp.setTime(aicReopenData.getEffectiveDate());
				Calendar nextQt = DateUtility.determineNextQuarter(calBp);
				claimAppData.setBpStartDateDisplay(nextQt);
			} else {
				// CQ R4UAT00015390 on 06/06/2011
				startDate.setTime(claimData.getEffectiveDate());
				claimAppData.setBpStartDate(startDate);
				Calendar[] qDate = RegularClaim.determineBasePeriod(startDate);
				claimAppData.setBpStartDate(qDate[0]);
				claimAppData.setBpEndDate(qDate[1]);
				Calendar calBp = Calendar.getInstance();
				calBp.setTime(claimData.getEffectiveDate());
				Calendar nextQt = DateUtility.determineNextQuarter(calBp);
				claimAppData.setBpStartDateDisplay(nextQt);
			}
			// Calendar endDate = new GregorianCalendar();
			// endDate.setTime(claimAppData.getClaimEffectiveDate());
			// claimAppData.setBpEndDate(endDate);

			// cq R4UAT00015324 on 06/02/2011
			// Calendar startDate = new GregorianCalendar();
			// startDate.setTime(claimData.getEffectiveDate());
			// Calendar[] qDate = RegularClaim.determineBasePeriod(startDate);
			// claimAppData.setBpStartDate(qDate[0]);
			// claimAppData.setBpEndDate(qDate[1]);

		}

		objAssembly = this.saveOrUpdateClaimApp(objAssembly);
		objAssembly.removeComponent(ClaimApplicationDataBridge.class);
		if (claimAppBO != null) {
			ClaimApplicationData claimApp = claimAppBO
					.getClaimApplicationData();
			objAssembly.addComponent(claimApp);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimantApplicationData as input
	 * and returns the ObjectAssembly with ClaimApplication matching the SSN.
	 * 
	 * @param objAssembly
	 *            with SSN
	 * @return objAssembly with ClaimantApplicationData where the claimant's ssn
	 *         match.
	 */
	public IObjectAssembly getClaimApp(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimApp");
		}

		Date wkStartDate = Claim.determineClaimEffectiveDate().getTime();
		if (UserTypeEnum.MDES.getName().equals(
				objAssembly.getUserContext().getUserType())) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(wkStartDate);
			cal.add(Calendar.DATE, -7);
			wkStartDate = cal.getTime();
		}

		ClaimApplication claimAppBO = ClaimApplication.findClaimAppBySsn(
				objAssembly.getSsn(), wkStartDate);

		objAssembly.removeComponent(ClaimApplicationDataBridge.class);
		if (claimAppBO != null) {
			ClaimApplicationData claimAppData = claimAppBO
					.getClaimApplicationData();
			objAssembly.addComponent(claimAppData);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with GenericSearchBean as input and
	 * returns a list of DynaBean objects
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean, EbDeclarationData primary key if
	 *            available
	 * @return objAssembly
	 */
	public IObjectAssembly getEbDeclarationData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getEbDeclarationData");
		}
		Long ebDeclarationId = objAssembly
				.getPrimaryKeyAsLong(EbDeclarationData.class);

		if (ebDeclarationId != null) {
			EbDeclarationData ebData = EbDeclaration
					.getEbDeclarationDataByPk(ebDeclarationId);
			if (ebData != null) {
				objAssembly.addComponent(ebData, true);
			}
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with EbDeclarationData as input and
	 * saves it, or updates it if it is already exists.
	 * 
	 * @param objAssembly
	 *            with EbDeclarationData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateEbDeclaration(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateEbDeclaration");
		}

		EbDeclarationData data = (EbDeclarationData) objAssembly
				.getFirstComponent(EbDeclarationData.class);

		EbDeclaration ebDeclarationBO = new EbDeclaration(data);
		ebDeclarationBO.saveOrUpdate();

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationClaimantData as
	 * input, send request for SSA validation, and saves/updates
	 * ClaimApplicationClaimantData.
	 * 
	 * @param objAssembly
	 *            with ClaimantApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly sendSsaValidation(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendSsaValidation");
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")
		ClaimantData clmntData = (ClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimantDataBridge.class);

		ClaimApplicationClaimant clmAppClmntBO = new ClaimApplicationClaimant(
				clmAppClmntData);

		// to send ssn validation request in case that request has not been
		// sent yet, or the first attempt get invalid result
		if ((StringUtility.isBlank(clmAppClmntData.getSsaStatus()))
				|| (SsaStatusEnum.N.getName().equals(clmAppClmntData
						.getSsaStatus()))
				|| (SsaStatusEnum.I1.getName().equals(clmAppClmntData
						.getSsaStatus()))) {
			try {
				clmAppClmntBO = clmAppClmntBO.processSsnRequest(clmntData);
			} catch (BaseApplicationException e) {
				objAssembly.addBusinessError(e.getMessage());
			}
		}

		this.saveOrUpdateClmAppClmnt(objAssembly);

		return objAssembly;
	}

	/**
	 * This method saves ClaimApplicationData, and initializes questionnaires.
	 * 
	 * @param objAssembly
	 * @return objAssembly
	 */
	public IObjectAssembly processAbleDetails(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processAbleDetails");
		}
		// Clear Claim Application Employer Data.
		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);

		// Create Claim Application Data From object Assembly
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// Get List of Claim Application Employer and set in Claim app data
		List clmAppEmpList = ClaimApplicationEmployer
				.findClmAppEmpListByClmAppId(clmAppData.getPkId());
		if (clmAppEmpList != null) {
			Set s = new HashSet();
			Iterator itr2 = clmAppEmpList.iterator();
			while (itr2.hasNext()) {
				s.add((ClaimApplicationEmployerData) itr2.next());
			}
			clmAppData.setClaimApplicationEmployerData(s);
		}

		// To save ClaimApplicationData
		objAssembly = this.saveOrUpdateClaimApp(objAssembly);

		// to avoid questionaire coming from File Paper Claim Menu item
		String filePaperClaim = (String) objAssembly.getData("filePaperClaim");

		// To initialize Questionnaire
		if (filePaperClaim == null) {
			objAssembly = this.initializeQuestionnaire(objAssembly);
		}

		// Add Claim Application Employer back in session
		if (clmAppEmpList != null) {
			objAssembly.addComponentList(clmAppEmpList);
		}

		return objAssembly;
	}

	/**
	 * This method calls processClaimEffectiveDate if it is necessary, and
	 * retrieves ClaimApplicationClaimantData based on ClaimApplicationData.
	 * 
	 * @param objAssembly
	 * @return objAssembly
	 */
	public IObjectAssembly loadClaimant(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method loadClaimant");
		}

		if (UserTypeEnum.CLMT.getName().equals(
				objAssembly.getUserContext().getUserType())) {
			objAssembly = this.processClaimEffectiveDate(objAssembly);
		}

		objAssembly = this.getClmAppClmnt(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ClaimApplicationEmployerData, and returns
	 * ReconsiderationRequestData.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationEmployerData
	 * @return objAssembly with ReconsiderationRequestData
	 */
	public IObjectAssembly getReconsiderationRequestDataByClaimAppEmpId(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getReconsiderationRequestDataByClaimAppEmpId");
		}

		ClaimApplicationEmployerDataBridge clmAppEmpData = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);

		ClaimApplicationEmployer clmAppEmpBO = ClaimApplicationEmployer
				.getReconsiderationRequestDataByClaimAppEmpId(clmAppEmpData
						.getPkId());

		objAssembly.removeComponent(ClaimApplicationEmployerDataBridge.class);
		objAssembly.removeComponent(Form525BData.class);
		if (clmAppEmpBO != null) {

			if (clmAppEmpBO.getClaimAppEmpData() != null) {
				objAssembly.addComponent(clmAppEmpBO.getClaimAppEmpData());

				if (clmAppEmpBO.getClaimAppEmpData().getForm525BData() != null) {
					Set form525BSet = clmAppEmpBO.getClaimAppEmpData()
							.getForm525BData();
					for (Iterator it = form525BSet.iterator(); it.hasNext();) {
						Form525BData form525BData = (Form525BData) it.next();
						if (Form525BTypeEnum.Missing.getName().equals(
								form525BData.getForm525B_Type())) {
							objAssembly.addComponent(form525BData);
							break;
						}
					}

				}
			}
		}

		return objAssembly;
	}

	/**
	 * This method will return the physical address from the employer contact
	 * table using the employer's id.
	 */
	public IObjectAssembly getPhysicalEmployerContactByEmployerID(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getPhysicalEmployerContactByEmployerID");
		}

		ClaimApplicationEmployerDataBridge clmAppEmpData = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);
		EmployerContactData empContactData = null;
		if ((clmAppEmpData.getEmployerData() != null)
				&& (clmAppEmpData.getEmployerData().getEmployerId() != null)) {
			EmployerContactDAO empContactDAO = new EmployerContactDAO();
			empContactData = empContactDAO
					.getPhysicalEmployerContactByEmployerID(clmAppEmpData
							.getEmployerData().getEmployerId());

			if ((empContactData != null)
					&& StringUtility.isBlank(empContactData.getAddressLine1())) {
				empContactData = empContactDAO
						.getMailingEmployerContactByEmployerID(clmAppEmpData
								.getEmployerData().getEmployerId());
			}
		}
		objAssembly.addData("empContactData", empContactData);

		return objAssembly;
	}

	/**
	 * This method calls getBasePeriod, and
	 * getReconsiderationRequestDataByClaimAppEmpId to load appropriate data.
	 * 
	 * @param objAssembly
	 * @return objAssembly
	 */
	public IObjectAssembly loadWageReconsideration(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method loadWageReconsideration");
		}

		objAssembly = this.getBasePeriod(objAssembly);

		objAssembly = this
				.getReconsiderationRequestDataByClaimAppEmpId(objAssembly);

		objAssembly = this.getPhysicalEmployerContactByEmployerID(objAssembly);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with EAN as input and returns the
	 * ObjectAssembly with EmployerData matching the EAN.
	 * 
	 * @param objAssembly
	 *            with EAN
	 * @return objAssembly with EmployerData
	 */
	public IObjectAssembly getRegisteredEmployerByEan(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRegisteredEmployerByEan");
		}
		objAssembly.removeComponent(EmployerData.class);
		Employer employer = null;
		employer = Employer.getEmployerByEan(objAssembly.getEan());
		// The following statement is executed to load lazy properties.
		String employerName = employer.getEmployerData().getDisplayName();
		LOGGER.debug(employerName);

		if (employer != null) {
			objAssembly.addComponent(employer.getEmployerData());
		}

		return objAssembly;
	}

	// CIF_00011
	/**
	 * This method takes ObjectAssembly with EAN as input and returns the
	 * ObjectAssembly with EmployerData matching the EAN also with uncompleted
	 * shared plan data
	 * 
	 * @param objAssembly
	 *            with EAN
	 * @return objAssembly with EmployerData and sharedEmployerData
	 */
	public IObjectAssembly getSharedRegisteredEmployerByEan(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedRegisteredEmployerByEan");
		}
		objAssembly.removeComponent(EmployerData.class);
		Employer employer = null;
		try {
			employer = Employer.getEmployerByEan(objAssembly.getEan());
		} catch (BaseApplicationException e) {
			throw new BaseApplicationException("errors.ean",
					new Object[] { "EAN" });
		}
		// The following statement is executed to load lazy properties.
		String employerName = employer.getEmployerData().getDisplayName();
		LOGGER.debug(employerName);

		if (employer != null) {
			objAssembly.addComponent(employer.getEmployerData());
			SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
			sharedEmployer = sharedEmployer.getSharedEmployerData(objAssembly
					.getEan());
			if (null != sharedEmployer) {
				objAssembly.addComponent(sharedEmployer
						.getSharedWorkEmployerData());
			}
		}

		return objAssembly;
	}

	/**
	 * This method behaves differently. this will bring other state employer
	 * instead of US Senate This method takes ObjectAssembly with EAN as input
	 * and returns the ObjectAssembly with EmployerData matching the EAN.
	 * 
	 * 
	 * @param objAssembly
	 *            with EAN
	 * @return objAssembly with EmployerData
	 */
	public IObjectAssembly fetchEmployerDataByEan(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method fetchEmployerDataByEan");
		}
		objAssembly.removeComponent(EmployerData.class);
		Employer employer = null;
		employer = Employer
				.fetchEmployerDataForChargeBackInquiryByEan(objAssembly
						.getEan());
		if (employer == null) {
			throw new BaseApplicationException("error.access.ean.not.found");
		}
		// The following statement is executed to load lazy properties.
		String employerName = employer.getEmployerData().getDisplayName();
		LOGGER.debug(employerName);

		if (employer != null) {
			objAssembly.addComponent(employer.getEmployerData());
		}

		return objAssembly;
	}

	public IObjectAssembly fetchEmployerDataByEmployerUserId(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method fetchEmployerDataByEmployerUserId");
		}
		objAssembly.removeComponent(EmployerData.class);
		EmployerUser employerUser = null;
		employerUser = EmployerUser.fetchEmployerUserByUserId(super
				.getUserContext().getUserId());
		if (employerUser == null) {
			throw new BaseApplicationException("error.access.ean.not.found");
		}
		// The following statement is executed to load lazy properties.
		Long empAccountID = employerUser.getEmployerUser()
				.getEmployerAccountId();
		EmployerAccount employerAccount = EmployerAccount.findByPrimaryKey(
				empAccountID, false);
		objAssembly.setEan(employerAccount.getEmployerAccountData().getEan());
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with DuaApplicationData as input and and
	 * saves it, or updates it if it is already exists.
	 * 
	 * @param objAssembly
	 *            with DuaApplicationData
	 * @return objAssembly
	 */

	public IObjectAssembly saveOrUpdateDuaApplication(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateDuaApplication");
		}

		DuaApplicationDataBridge data = (DuaApplicationDataBridge) objAssembly
				.getFirstComponent(DuaApplicationDataBridge.class, true);

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class, true);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		Object lObject = MultiStateClassFactory.getObject(this.getClass()
				.getName(), BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

		if (null != lObject) {

			((CinService) lObject).duaApplicatioDataObject(data);
		}

		if (data == null) {// If not present in objAssembly, then just check
							// whether present in duaApplicationData
			data = (DuaApplicationDataBridge) clmAppData
					.getDuaApplicationData();
		}
		if (data != null) {
			data.setClaimApplicationData(clmAppData);
			DuaApplication duaAppBO = new DuaApplication(data);
			duaAppBO.saveOrUpdate();
		}

		clmAppData.setDuaApplicationData(data);
		Set cliamAppEmplrList = clmAppData.getClaimApplicationEmployerData();
		objAssembly = this.saveOrUpdateClaimApp(objAssembly);

		return objAssembly;
	}

	/**
	 * This method generates DuaClaimData based on all already entered data.
	 * 
	 * @param objAssembly
	 *            with DuaApplicationData
	 * @return objAssembly DuaClaimData
	 */
	public IObjectAssembly processDuaClaim(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processDuaClaim");
		}

		// to set status of DUA application
		DuaApplicationDataBridge duaAppData = (DuaApplicationDataBridge) objAssembly
				.fetchORCreate(DuaApplicationDataBridge.class);

		duaAppData.setStatusFlag(ApplicationStatusEnum.PENDING.getName());
		duaAppData.setDuaFileDate(new Date());
		DuaApplication duaAppBO = new DuaApplication(duaAppData);
		duaAppBO.saveOrUpdate();
		duaAppBO.flush();

		// to generate business comment in case of ROC claim
		ClaimantData clmntData = (ClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class, true);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		Object[] messageArguments = new Object[1];
		messageArguments[0] = duaAppData.getDuaDeclarationData()
				.getFemaNumber();
		RegularClaim regClaimBO = new RegularClaim(null);
		Set clmAppEmplrSet = clmAppData.getClaimApplicationEmployerData();
		ArrayList tempList = new ArrayList(clmAppEmplrSet);
		if (tempList != null && tempList.size() > 0) {
			Collections.sort(tempList, Collections.reverseOrder());
		}
		// CIF_01486 Commented on 11/20/2014 because this is not necessary for
		// view purpose as we have seperate jsp and action.
		/*
		 * if(StringUtility.isNotBlank(clmAppData.getDuaApplicationData().
		 * getSelfEmployedFlag()) &&
		 * YesNoTypeEnum.NumericYes.getName().equals(clmAppData
		 * .getDuaApplicationData().getSelfEmployedFlag())){
		 * ClaimApplication.generateDUA12Report(clmntData, clmAppData, tempList,
		 * regClaimBO); }
		 */

		// defect 6471 generate DUA1 added on 8/27/2008
		// CIF_01486 Commented on 11/20/2014 because this is not necessary for
		// view purpose as we have seperate jsp and action.
		// ClaimApplication.generateDUA1Report(clmntData, clmAppData, tempList,
		// regClaimBO);

		ClaimData claimData = null;
		// DUA enhancement 09/05/08
		if (ClaimApplicationTypeEnum.NEW.getName().equals(
				clmAppData.getClaimAppType())) {
			claimData = Claim
					.findActiveClaimDataByClaimApplicationId(clmAppData
							.getPkId());
		} else if (ClaimApplicationTypeEnum.AIC.getName().equals(
				clmAppData.getClaimAppType())) {
			AicReopenData aicData = Claim.findAicByClaimAppId(clmAppData
					.getPkId());
			claimData = aicData.getClaimData();
		}

		// 2282
		if (claimData != null) {
			RegularClaim claimBO = new RegularClaim(claimData);
			WaitingWeekControlData waitingweekcontroldata = new WaitingWeekControlData();
			waitingweekcontroldata.setClaimData(claimData);

			waitingweekcontroldata.setDuaDeclarationData(duaAppData
					.getDuaDeclarationData());
			waitingweekcontroldata.setEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));

			Set waitingweekcontrolset = claimData.getWaitingWeekControls();
			if (waitingweekcontrolset == null) {
				waitingweekcontrolset = new HashSet();
			}
			waitingweekcontrolset.add(waitingweekcontroldata);
			claimData.setWaitingWeekControls(waitingweekcontrolset);

			claimBO.saveOrUpdate();
		}

		// R4UAT00015098 - for Re-opens, due to above code, claimData is null.
		// So try to query it from DB.
		Long claimId = null;
		if (claimData != null) {
			claimId = claimData.getClaimId();
		} else {
			AicReopenData aicData = Claim.findAicByClaimAppId(clmAppData
					.getPkId());
			if (aicData != null) {
				claimId = aicData.getClaimData().getClaimId();
			}
		}
		// R4UAT00014877 - Cancel the waitingPeriod if that is AFTER the dua eff
		// date.
		if (claimId != null) {
			ProcessedWeeklyCertData processedWeeklyCertData = ProcessedWeeklyCertBO
					.findLatestWaitingWeekDataByClaimId(claimId);
			Date duaEffDate = duaAppData.getDuaDeclarationData().getStartDate();
			if (processedWeeklyCertData != null
					&& processedWeeklyCertData.getTransactionType().equals(
							WeeklyTransactionTypeEnum.WAITING_PERIOD_WEEK
									.getName())) {
				if (processedWeeklyCertData.getWeeklyCertData()
						.getClaimWeekEndDate().after(duaEffDate)) {
					ProcessedWeeklyCertBO
							.insertCancelWaitingWeekData(processedWeeklyCertData);
					processedWeeklyCertData.getClaimData()
							.getClaimInformationData()
							.setWaitingPeriodFlag(GlobalConstants.DB_ANSWER_NO);
					IWeeklyCertificationJrnlDAO dao = DAOFactory.instance
							.getWeeklyCertificationJrnlDAO();
					dao.saveOrUpdate(processedWeeklyCertData.getClaimData()
							.getClaimInformationData());
				}
			}
		}

		// change done on Feb 25th 2009
		// for AIC and Reopen claims , the claim data will be null and in this
		// scenario the system need not send any UI-21A for any employers.
		// cq 2282

		// CIF_01273 Start . commented some lines of code which are MS specific.
		/*
		 * if(claimData != null){
		 * if(YesNoTypeEnum.NumericNo.getName().equals(claimData
		 * .getMonEligibleFlag())){ //Generate UI-21A to last employer
		 * if(tempList != null && tempList.size() > 0) {
		 * ClaimApplicationEmployerData lastClmAppEmprData =
		 * (ClaimApplicationEmployerData)tempList.get(0); if
		 * (EmployerTypeEnum.REG
		 * .getName().equals(lastClmAppEmprData.getEmployerType())) {
		 * CorrespondenceData corrData = new CorrespondenceData();
		 * corrData.setCorrespondenceCode
		 * (CorrespondenceCodeEnum.UI_21A.getName()); if
		 * (ClaimApplicationTypeEnum
		 * .AIC.getName().equals(clmAppData.getClaimAppType())) {
		 * corrData.setParameter1(GlobalConstants.DB_ANSWER_YES);
		 * corrData.setParameter1Desc("This is an AIC"); }
		 * corrData.setClaimData(claimData);
		 * corrData.setClaimantData(clmntData); EmployerData empData =
		 * lastClmAppEmprData.getEmployerData(); /* R4UAT00015056 - unnecessary
		 * object initializtion throws org.hibernate.TransientObjectException:
		 * object references an unsaved transient instance - save the transient
		 * instance before flushing: gov.state.uim.domain.data.EmployerData if
		 * (empData == null) { empData = new RegisteredEmployerData(); }
		 */
		// //CIF_01273 End

		// if registered employer, get the claim address of the employer. If the
		// claim address is not
		// available get the mailing address.
		// CIF_01273 Start commented some lines of code which are MS specific.
		/*
		 * if
		 * (EmployerTypeEnum.REG.getName().equals(lastClmAppEmprData.getEmployerType
		 * ())){ EmployerData employerData =
		 * lastClmAppEmprData.getEmployerData(); if (employerData != null) {
		 * Employer employer =
		 * Employer.getEmployerDataWithChildrenByPkId(employerData
		 * .getEmployerId()); if (employer == null){
		 * corrData.setMailingAddress(lastClmAppEmprData.getMailAddress()); }
		 * Set employerContactDataSet = null; if ((employer != null) &&
		 * (employer.getEmployerData() != null)) { employerContactDataSet =
		 * employer.getEmployerData().getEmployerContactData(); } if
		 * (employerContactDataSet != null &&
		 * !employerContactDataSet.isEmpty()){ AddressComponentData
		 * mailingAddress = null; AddressComponentData claimAddress = null;
		 * for(Iterator i = employerContactDataSet.iterator(); i.hasNext();){
		 * EmployerContactData employerContactData =
		 * (EmployerContactData)i.next();
		 * if(AddressTypeEnum.CL.getName().equals(
		 * employerContactData.getType())){ claimAddress = new
		 * AddressComponentData();
		 * claimAddress.setLine1(employerContactData.getAddressLine1());
		 * claimAddress.setLine2(employerContactData.getAddressLine2());
		 * claimAddress.setCity(employerContactData.getCity());
		 * claimAddress.setState(employerContactData.getState());
		 * claimAddress.setZip(employerContactData.getZip());
		 * claimAddress.setValidatedFlag
		 * (employerContactData.getValidatedFlag());
		 * claimAddress.setPostalBarCode(employerContactData.getBarcode());
		 * break; }else
		 * if(AddressTypeEnum.MA.getName().equals(employerContactData
		 * .getType())){ mailingAddress = new AddressComponentData();
		 * mailingAddress.setLine1(employerContactData.getAddressLine1());
		 * mailingAddress.setLine2(employerContactData.getAddressLine2());
		 * mailingAddress.setCity(employerContactData.getCity());
		 * mailingAddress.setState(employerContactData.getState());
		 * mailingAddress.setZip(employerContactData.getZip());
		 * mailingAddress.setValidatedFlag
		 * (employerContactData.getValidatedFlag());
		 * mailingAddress.setPostalBarCode(employerContactData.getBarcode()); }
		 * } //Set the claim address if available, else set the mailing address
		 * if(claimAddress != null){ corrData.setMailingAddress(claimAddress);
		 * }else if(mailingAddress != null){
		 * corrData.setMailingAddress(mailingAddress); } }else{
		 * corrData.setMailingAddress(lastClmAppEmprData.getMailAddress()); }
		 * }else{ //if employer id is not available, get the address from the
		 * claim application employer
		 * corrData.setMailingAddress(lastClmAppEmprData.getMailAddress()); }
		 * }else{ //if other state employer, get the address from the claim
		 * application employer
		 * corrData.setMailingAddress(lastClmAppEmprData.getMailAddress()); }
		 * 
		 * if (empData != null) { //R4UAT00015056 - if null dont set it
		 * corrData.setEmployerData(empData); }
		 * corrData.setClaimAppEmployerData(lastClmAppEmprData);
		 * corrData.setClaimAppClmntData
		 * (clmAppData.getClaimApplicationClaimantData());
		 * corrData.setDirection(
		 * CorrespondenceDirectionEnum.OUTGOING.getName());
		 * corrData.setUpdatedBy(super.getContextUserId()); ICorrespondenceDAO
		 * corrDAO = DAOFactory.instance.getCorrespondenceDAO();
		 * corrDAO.saveOrUpdate(corrData); } } } }
		 */
		// CIF_01273 End
		// End DUA enhancement 09/05/08
		ClaimApplication.fileWeeklyCertsForDUAretroWeeks(clmAppData, clmntData,
				true);

		regClaimBO.createBusinessProcessEventComment("FCIN", "DUA_FLD",
				messageArguments, clmntData);
		/*
		 * CIF_00117 - Start Below line of code is to create MakeDUADecision
		 * workflow.
		 * 
		 * Map<String , Object> mapValues = new HashMap<String , Object>();
		 * GlobalDsContainerBean globalBean = new GlobalDsContainerBean();
		 * BaseAccessDsContainerBean baseBean = new BaseAccessDsContainerBean();
		 * Map<String,Object> globalbeanMap = new HashMap<String,Object>();
		 * 
		 * 
		 * //1.Get the datas from objAssembly and set all the required parameter
		 * to the Basebean & GlobalBean.
		 * globalBean.setBusinessKey(claimData.getClaimApplicationData
		 * ().getPkId().toString());
		 * globalBean.setSsneanfein(claimData.getClaimantData().getSsn());
		 * globalBean.setType("ssn");
		 * 
		 * baseBean.setComment("Please take care of this work");
		 * baseBean.setDolTimeLapse(2334545);
		 * baseBean.setFirstNotificationDueTime(123233232);
		 * baseBean.setForward("FCRequest"); baseBean.setGlobalDs(globalBean);
		 * baseBean.setHoldDate("12/12/12");
		 * baseBean.setNextActivity("MisiingMO");
		 * baseBean.setSupervisorUserId("Abhishek"); baseBean.setUserId("abhi");
		 * 
		 * //2.set the globalBean to the globalmap.
		 * globalbeanMap.put("globalBean", globalBean);
		 * globalbeanMap.put("businessKey", globalBean.getBusinessKey());
		 * globalbeanMap.put("name",globalBean.getName());
		 * globalbeanMap.put("ssneanfein",globalBean.getSsneanfein());
		 * globalbeanMap.put("type",globalBean.getType());
		 * 
		 * 
		 * //*3.set the baseBean to the mapValues.
		 * mapValues.put("userId",baseBean.getUserId());
		 * mapValues.put("supervisorUserId",baseBean.getSupervisorUserId());
		 * mapValues.put("holdDate",baseBean.getHoldDate());
		 * mapValues.put("firstNotificationDueTime"
		 * ,baseBean.getFirstNotificationDueTime());
		 * mapValues.put("forward",baseBean.getForward());
		 * mapValues.put("nextActivity",baseBean.getNextActivity());
		 * mapValues.put("globalDs",globalbeanMap);
		 * mapValues.put("dolTimeLapse",baseBean.getDolTimeLapse());
		 * mapValues.put("comment",baseBean.getComment());
		 * 
		 * 
		 * BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
		 * 
		 * 
		 * 
		 * //Calling DUA CLaim 1 workflowDAO.createAndStartProcessInstance(
		 * WorkflowProcessTemplateConstants.DUAClaim.MAKE_DUA_DECISION,
		 * mapValues);
		 * 
		 * //Calling ADJUDICATE_WEEKLY_DUA_CLAIM 5
		 * workflowDAO.createAndStartProcessInstance
		 * (WorkflowProcessTemplateConstants
		 * .DUAClaim.ADJUDICATE_WEEKLY_DUA_CLAIM, mapValues);
		 * 
		 * // Calling DUA_QUIT_OR_DISCARGE_TERMINATED 6
		 * workflowDAO.createAndStartProcessInstance
		 * (WorkflowProcessTemplateConstants
		 * .DUAClaim.DUA_QUIT_OR_DISCARGE_TERMINATED, mapValues);
		 * 
		 * // Calling PROCESS_DUA_PROOF 4
		 * workflowDAO.createAndStartProcessInstance
		 * (WorkflowProcessTemplateConstants.DUAClaim.PROCESS_DUA_PROOF,
		 * mapValues);
		 * 
		 * // Calling PROCESS_EMPLOYMENT_WAGES_PROOF 3
		 * workflowDAO.createAndStartProcessInstance
		 * (WorkflowProcessTemplateConstants
		 * .DUAClaim.PROCESS_EMPLOYMENT_WAGES_PROOF, mapValues);
		 * 
		 * //Calling DUA ADJUDICATE_REGULAR_CLAIM_DUA_ON_FILE
		 * workflowDAO.createAndStartProcessInstance
		 * (WorkflowProcessTemplateConstants
		 * .DUAClaim.ADJUDICATE_REGULAR_CLAIM_DUA_ON_FILE, mapValues);
		 * 
		 * //CIF_00117 - End
		 *//*
			 * RegularClaim regClaimBO = new RegularClaim(null);
			 * regClaimBO.generateDuaClaimBusinessComment(clmntData);
			 */
		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with SSN as input and returns
	 * ObjectAssembly with Boolean object called EXISTING_FLAG, true if record
	 * with given SSN exist, and false if it does not exist
	 * 
	 * @param objAssembly
	 *            with SSN
	 * @return objAssembly with Boolean EXISTING_FLAG
	 */
	public IObjectAssembly checkMassLayoffsDataBySsn(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkMassLayoffsDataBySsn");
		}
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		String ssn = objAssembly.getSsn();
		Date effDate = clmAppData.getClaimEffectiveDate();

		boolean flag = MassLayoff.checkMassLayoffsDataBySsn(ssn, effDate);

		objAssembly.addData("EXISTING_FLAG", new Boolean(flag));

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input, and
	 * removes it as well as all its children objects.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly deleteClaimApp(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method deleteClaimApp");
		}

		// ClaimApplicationData clmAppData = (ClaimApplicationData)
		// objAssembly.fetchORCreate(ClaimApplicationData.class);
		//
		// if (clmAppData.getPkId() != null &&
		// clmAppData.getClaimApplicationClaimantData().getPkId() != null)
		// {
		// objAssembly.removeAllBean();
		// objAssembly.removeAllComponent();
		// ClaimApplication.deleteClaimAppByPkId(clmAppData.getPkId(),
		// clmAppData.getClaimApplicationClaimantData().getPkId());
		// }

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		if (clmAppData.getPkId() != null) {
			clmAppData.setClaimAppStatus(ApplicationStatusEnum.CANCELLED
					.getName());
			ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
			clmAppBO.saveOrUpdate();
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with DuaApplicationData as input, and
	 * removes it as well as all its children objects.
	 * 
	 * @param objAssembly
	 *            with DuaApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly deleteDuaApp(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method deleteDuaApp");
		}

		FlowBean flowBean = (FlowBean) objAssembly
				.fetchORCreateBean(FlowBean.class);

		if (flowBean.isFlowDua()) {
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
			// mddrNo="", impactPoint="Start")
			ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
			// mddrNo="", impactPoint="End")
			ClaimApplication.deleteDuaAppByClmAppId(clmAppData.getPkId());
		} else {
			this.deleteClaimApp(objAssembly);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationData as input,
	 * searching for corresponding DuaApplicationData, and removes it (as well
	 * as its children) if DuaApplicationData exists.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly deleteDuaAppByClmAppId(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method deleteDuaAppByClmAppId");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		objAssembly.removeComponent(DuaApplicationDataBridge.class);

		DuaApplication duaAppBO = DuaApplication
				.findDuaAppByClaimAppId(clmAppData.getPkId());

		if (duaAppBO != null) {
			duaAppBO.delete();
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with MassLayoffData as input, and
	 * removes it as well as all its children objects.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly deleteMasslayoff(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method deleteMasslayoff");
		}

		MassLayoffData masslayoffData = (MassLayoffData) objAssembly
				.fetchORCreate(MassLayoffData.class);

		MassLayoff masslayoffBO = new MassLayoff(masslayoffData);
		masslayoffBO.delete();

		return objAssembly;
	}

	public IObjectAssembly myTest(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method myTest");
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with
	 * 
	 * @param objAssembly
	 *            with GenericSearchBean
	 * @return objAssembly
	 */
	public IObjectAssembly getFormMa843DataByClmAppEmprId(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getFormMa843DataByClmAppEmprId");
		}

		ClaimApplicationEmployerDataBridge clmAppEmprData = (ClaimApplicationEmployerDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationEmployerDataBridge.class);

		FormMa843Data formMa843Data = ClaimApplicationEmployer
				.findFormMa843DataByClmAppEmprId(clmAppEmprData.getPkId());

		if (formMa843Data != null) {
			clmAppEmprData.setFormMa843Data(formMa843Data);
		}

		return objAssembly;
	}

	public IObjectAssembly getClaimantAndActiveClaimBySsn(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimantAndActiveClaimBySsn");
		}

		Claimant clmntBO = Claimant.fetchClaimantBySsn(objAssembly.getSsn());
		if (clmntBO != null) {
			ClaimantData clmntData = clmntBO.getClaimantData();
			objAssembly.addComponent(clmntData, true);

			if ((clmntData.getClaimData() != null)
					&& (clmntData.getClaimData().size() > 0)) {
				Set claimSet = clmntData.getClaimData();

				// 9189 Check if the Regular claim after the EUC claim then
				// allow backdating the Regular Claim.
				List claims = new ArrayList(claimSet);
				Collections.sort(claims, Collections.reverseOrder());
				Date latestUIClaimEffectiveDate = null;
				for (Iterator clmItr = claims.iterator(); clmItr.hasNext();) {

					ClaimData claimData = (ClaimData) clmItr.next();
					if (ClaimStatusEnum.CANCEL.getName().equals(
							claimData.getStatus())) {
						continue;
					}

					if (EntitlementTypeEnum.REGULAR.getName().equals(
							claimData.getEntitlementType())) {
						// get the effective date of the latest UI claim.
						latestUIClaimEffectiveDate = claimData
								.getEffectiveDate();
						break;
					}
				}

				// This loop is for checking whether we have a TUEC claim in
				// effect comparing with the
				// current date, if there is a TEUC claim in effect throw

				// Commenting the below to not check for TEUC
				/*
				 * for (Iterator i = claims.iterator(); i.hasNext(); ) {
				 * ClaimData clmData = (ClaimData) i.next();
				 * 
				 * Date currDate = new Date();
				 * 
				 * if(ClaimStatusEnum.CANCEL.getName().equals(clmData.getStatus()
				 * )) { continue; }
				 * 
				 * if (EntitlementTypeEnum.TEUC.getName().equals(clmData.
				 * getEntitlementType())){ TeucClaimData teucClaimData =
				 * (TeucClaimData)clmData; boolean isTeucClaimInEffect =
				 * DateUtility.isBetweenDates(currDate,
				 * teucClaimData.getEffectiveDate(),
				 * teucClaimData.getByeDate());
				 * 
				 * if(isTeucClaimInEffect &&
				 * !ClaimStatusEnum.CANCEL.getName().equals
				 * (teucClaimData.getStatus())){ if(latestUIClaimEffectiveDate
				 * != null) { if(latestUIClaimEffectiveDate.after(teucClaimData.
				 * getEffectiveDate())) { break; } } throw new
				 * BaseApplicationException
				 * ("error.access.cin.teuc.claim.ineffect"); } } }
				 */
				List claimList = new ArrayList();
				List aicList = new ArrayList();

				for (Iterator i = claims.iterator(); i.hasNext();) {
					ClaimData clmData = (ClaimData) i.next();

					if (null != clmData.getClaimApplicationData()) {
						clmData.getClaimApplicationData().getBpStartDate();
					}
					if (EntitlementTypeEnum.REGULAR.getName().equals(
							clmData.getEntitlementType())
							&& !ClaimStatusEnum.CANCEL.getName().equals(
									clmData.getStatus())) {
						Calendar cal = new GregorianCalendar();
						// R4UAT00017152
						// on 09/23/2011
						cal.add(Calendar.YEAR, -3);
						if (cal.getTime().compareTo(clmData.getEffectiveDate()) <= 0) {
							claimList.add(clmData);
						}

						if (clmData.getAicReopen() != null
								&& clmData.getAicReopen().size() > 0) {
							Set aicSet = clmData.getAicReopen();
							for (Iterator j = aicSet.iterator(); j.hasNext();) {
								AicReopenData aicData = (AicReopenData) j
										.next();
								aicData.getClaimData();
								if (aicData.getClaimApplicationData() != null
										&& (cal.getTime().compareTo(
												aicData.getEffectiveDate()) <= 0)) {
									aicList.add(aicData);
								}
							}
						}
					}
				}

				if ((claimList == null || claimList.size() == 0)
						&& (aicList == null || aicList.size() == 0)) {
					throw new BaseApplicationException(
							"error.access.cin.backdate.no.claims.found");
				}

				if (claimList != null && claimList.size() > 0) {
					Collections.sort(claimList, Collections.reverseOrder());
					objAssembly.addComponentList(claimList, true);
				}

				if (aicList != null && aicList.size() > 0) {
					Collections.sort(aicList, Collections.reverseOrder());
					objAssembly.addComponentList(aicList, true);
				}
			} else {
				throw new BaseApplicationException(
						"error.access.cin.backdate.claim.not.found");
			}
		} else {
			throw new BaseApplicationException(
					"error.access.cin.backdate.claimant.not.found");
		}

		return objAssembly;
	}

	/*
	 * public IObjectAssembly getBackDateRequest(IObjectAssembly objAssembly) {
	 * if(LOGGER.isDebugEnabled()){
	 * LOGGER.debug("Start of method getBackDateRequest"); }
	 * 
	 * RegularClaimData clmData = (RegularClaimData) objAssembly
	 * .fetchORCreate(RegularClaimData.class);
	 * 
	 * BackDateRequest bo = BackDateRequest
	 * .getBackDateRequestDataByClaimId(clmData.getClaimId());
	 * 
	 * objAssembly.removeComponent(BackDateRequestData.class); if (bo != null) {
	 * objAssembly.addComponent(bo.getBackDateRequestData()); }
	 * 
	 * return objAssembly; }
	 */

	public IObjectAssembly saveOrUpdateBackDateRequest(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateBackDateRequest");
		}

		BackDateRequestData data = (BackDateRequestData) objAssembly
				.fetchORCreate(BackDateRequestData.class);

		BackDateRequest bo = new BackDateRequest(data);

		bo.saveOrUpdate();

		return objAssembly;
	}

	/**
	 * @param objAssembly
	 *            IObjectAssembly
	 * @throws BaseApplicationException
	 * @return objAssembly IObjectAssembly
	 */
	public IObjectAssembly processBackDateRequest(
			final IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processBackDateRequest");
		}

		final BackDateRequestData backDateRequestData = (BackDateRequestData) objAssembly
				.getFirstComponent(BackDateRequestData.class);

		final ClaimantData claimantData = (ClaimantDataBridge) objAssembly
				.getFirstComponent(ClaimantDataBridge.class);

		final ClaimData clmData = Claim.getRegularIWClaimBetweenGivenDate(
				claimantData.getSsn(),
				backDateRequestData.getRequestedEffectiveDate());
		if (clmData != null) {
			throw new BaseApplicationException(
					"error.access.backdaterequest.cannot.backdate.iwclaim");
		}
		final List objBackDateRequestList = BackDateRequest
				.getAllBackdateRequestBySSNAndOldEffectiveDate(
						claimantData.getSsn(),
						backDateRequestData.getOldEffectiveDate());
		if (objBackDateRequestList != null) {
			for (int i = 0; i < objBackDateRequestList.size(); i++) {
				final BackDateRequestData objBackDateRequestData = (BackDateRequestData) objBackDateRequestList
						.get(i);
				if (objBackDateRequestData != null) {
					if (objBackDateRequestData.getDecisionMadeDate() != null
							&& DateUtility.isSameDay(objBackDateRequestData
									.getDecisionMadeDate(), new Date())) {
						if (objBackDateRequestData.getAllowedBackDate() != null
								&& objBackDateRequestData
										.getRequestedEffectiveDate() != null
								&& DateUtility.isSameDay(objBackDateRequestData
										.getRequestedEffectiveDate(),
										objBackDateRequestData
												.getAllowedBackDate())) {
							throw new BaseApplicationException(
									"error.access.backdaterequest.cannot.backdate.decisionmadedate");
						}
					}
				}
			}
		}

		final BackDateRequest backDateRequestBO = new BackDateRequest(
				backDateRequestData);

		final Calendar clmEffDate = new GregorianCalendar();

		if (backDateRequestData.getAicReopenData() != null) {
			clmEffDate.setTime(backDateRequestData.getAicReopenData()
					.getEffectiveDate());
		} else {
			clmEffDate.setTime(backDateRequestData.getClaimData()
					.getEffectiveDate());
		}
		// to calculate & save Weekly Cert Back Weeks
		final List weeklyCertBackweekDataList = backDateRequestBO
				.calculateWeklyCertBackWeekList(
						backDateRequestData.getClaimData(), clmEffDate);
		if (null != weeklyCertBackweekDataList
				&& !weeklyCertBackweekDataList.isEmpty()) {
			// Storing to save at the end
			objAssembly.addComponentList(weeklyCertBackweekDataList, true);
		}
		final ArrayList cweList = new ArrayList();
		for (int i = 0; i < weeklyCertBackweekDataList.size(); i++) {
			final WeeklyCertBackweekData obj = (WeeklyCertBackweekData) weeklyCertBackweekDataList
					.get(i);
			cweList.add(obj.getClaimWeekEndDate());
		}

		final WeeklyCertificationValidationDataBean weekCertBean = WeeklyCert
				.determineBWEligibilityForReqBackDateCertification(
						claimantData.getSsn(), cweList);
		weekCertBean.setClaimantData(claimantData);
		// Comment the following as we donot want it to be saved again..
		// used in WeeklyCertService.java while saving
		// weekCertBean.setBackDateRequestFlow(true);
		objAssembly.addBean(weekCertBean);

		/** ALL THESE NEED TO BE PERFORMED AT THE END OF THE FLOW **/
		// PotentialIssueData createdPotIssueData=
		// backDateRequestBO.createIssueForBackDateRequest();//Added for change
		// req 08/27/07

		// backDateRequestData.setPotentialIssueData(createdPotIssueData);//Save
		// the potentialIssueId in the T_BACK_DATE_REQUEST
		// to save the BackDateRequestData object
		// CIF_00562 START
		// Backdate Work Item
		// Work Item has to be created if the backdate is more than one week.

		this.saveOrUpdateBackDateRequest(objAssembly);
		if (backDateRequestData.getAicReopenData() == null) {

			Calendar effectiveDate = new GregorianCalendar();
			effectiveDate.setTime(backDateRequestData
					.getRequestedEffectiveDate());
			Calendar[] backDateBasePeriod = RegularClaim
					.determineBasePeriod(effectiveDate);
			Calendar backDateBPStartDate = backDateBasePeriod[0];
			if (weeklyCertBackweekDataList.size() > gov.state.uim.framework.BenefitsConstants.NUMBER_ONE
					|| (DateUtility.getWeeksBetween(backDateRequestData
							.getRequestedEffectiveDate(), backDateRequestData
							.getClaimData().getEffectiveDate()) > gov.state.uim.framework.BenefitsConstants.NUMBER_ONE || (!DateUtility
							.isSameDay(backDateBPStartDate, backDateRequestData
									.getClaimData().getClaimApplicationData()
									.getBpStartDate())))) {
				backDateRequestBO.createBackDateWorkItem(claimantData);
			}// CIF_01668 Start
				// If workitem not generated, the SYSTEM_ALLOWED_DATE must be
				// populated with REQUESTED_BACK_DATE
			else {
				backDateRequestData.setSystemAllowedDate(backDateRequestData
						.getRequestedEffectiveDate());
				backDateRequestData.setDecisionMadeDate(new Date());
				backDateRequestData.setAllowedBackDate(backDateRequestData
						.getRequestedEffectiveDate());
				objAssembly.addComponent(backDateRequestData, true);
			}
			// CIF_03292
		} else {
			if (DateUtility.getWeeksBetween(
					backDateRequestData.getRequestedEffectiveDate(),
					backDateRequestData.getAicReopenData().getEffectiveDate()) > gov.state.uim.framework.BenefitsConstants.NUMBER_ONE) {
				backDateRequestBO.createBackDateWorkItem(claimantData);
			} else {
				backDateRequestData.setSystemAllowedDate(backDateRequestData
						.getRequestedEffectiveDate());
				backDateRequestData.setDecisionMadeDate(new Date());
				backDateRequestData.setAllowedBackDate(backDateRequestData
						.getRequestedEffectiveDate());
				objAssembly.addComponent(backDateRequestData, true);
				BackDateRequest bDateBO = new BackDateRequest(
						backDateRequestData);
				bDateBO.createBusinessEvenComments(null);
			}
		}

		// CIF_01668 End
		// CIF_00158 for gov.state.uim.claim.BackPostDate workflow
		// START
		// BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
		// Map<String, String> map = new HashMap<String, String>();
		// workflowDAO.createAndStartProcessInstance(WorkflowProcessTemplateConstants.Claims.BACK_POST_DATE,
		// map);
		// CIF_00158 End
		// CIF_00562 END
		// to create businessevent
		final Object[] effDate = new Object[] { backDateRequestData
				.getRequestedEffectiveDate() };
		backDateRequestBO.createBusinessProcessEventComment("NMON",
				"BACK_DATE_PENDING", effDate, claimantData);

		return objAssembly;
	}

	/**
	 * 
	 */
	public IObjectAssembly getDuaSelectDisaster(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDuaSelectDisaster");
		}
		ClaimApplicationData clmAppData = null;
		objAssembly.removeAllComponent();
		/*
		 * objAssembly.removeComponent(ClaimantDataBridge.class);
		 * objAssembly.removeComponent(ClaimData.class);
		 * objAssembly.removeComponent(ClaimApplicationData.class);
		 * objAssembly.removeComponent(ClaimApplicationClaimantData.class);
		 * objAssembly.removeComponent(DuaApplicationData.class);
		 * objAssembly.removeComponent(DuaDeclarationData.class);
		 */

		// to get DuaApplicationData, if exists
		ClaimApplication clmAppBO = ClaimApplication
				.findInitiatedOrPendingClaimAppAndDuaAppBySsn(objAssembly
						.getSsn());
		RegularClaimData regClmData = null;
		if (clmAppBO != null) {
			// If claim application is in Initiated status, throw business error
			if (ApplicationStatusEnum.INITIATED.getName().equalsIgnoreCase(
					clmAppBO.getClaimApplicationData().getClaimAppStatus())) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.initiatedclaimapp.message");
				return objAssembly;
			}

			// If claim application is in pending status, use the claim
			// application to take DUA Application.
			if (ApplicationStatusEnum.PENDING.getName().equalsIgnoreCase(
					clmAppBO.getClaimApplicationData().getClaimAppStatus())) {
				clmAppData = clmAppBO.getClaimApplicationData();
			}
		} else {
			// find lates not cancelled regular claim.
			List list = Claim.findActiveRegularClaimListBySsn(objAssembly
					.getSsn());
			if (list == null || list.isEmpty()) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.noclaim.message");
				return objAssembly;
			}

			regClmData = (RegularClaimData) list.get(0);
			Claim.getClaimBoByClaimData(regClmData);
			// As per 8212 the BYE check should not be performed.
			/*
			 * if (claim.isByeExpired(new Date())) {
			 * objAssembly.addBusinessError
			 * ("access.cin.duaselectdisaster.noclaim.message"); return
			 * objAssembly; }
			 */

			// to handle incomplete data due to migration
			if ((regClmData.getClaimApplicationData() == null)
					|| (regClmData.getClaimApplicationData()
							.getClaimApplicationClaimantData() == null)) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.duanotcompleteddata.message");
				return objAssembly;
			}

			ClaimantData clmntData = regClmData.getClaimantData();
			clmAppData = regClmData.getClaimApplicationData();

			if (clmAppData == null) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.noclaim.message");
				return objAssembly;
			}

			// to find AicReopen claims if exists
			List aicReopenList = Claim.findAicReopenByClaimId(regClmData
					.getClaimId());
			if ((aicReopenList != null) && (!aicReopenList.isEmpty())) {
				ClaimApplicationData newClmAppData = ((AicReopenData) aicReopenList
						.get(0)).getClaimApplicationData();
				if (newClmAppData != null) {// Fix for defect R4UAT00000858
					clmAppData = newClmAppData;
				}
			}

			objAssembly.addComponent(clmntData, true);
		}
		// If claim application is pending or initiated then find claimant if
		// present
		if (clmAppData != null) {
			Claimant claimant = Claimant
					.fetchClaimantBySsn(clmAppData.getSsn());
			if (claimant != null) {
				ClaimantData claimantData = claimant.getClaimantData();
				objAssembly.addComponent(claimantData, true);
			}
		}

		DuaApplicationData duaAppData = clmAppData.getDuaApplicationData();
		if (duaAppData != null) {
			if (ApplicationStatusEnum.PENDING.getName().equals(
					duaAppData.getStatusFlag())) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.duapending.message");
				return objAssembly;
			} else if (ApplicationStatusEnum.COMPLETED.getName().equals(
					duaAppData.getStatusFlag())) {
				DuaApplication duaAppBO = DuaApplication
						.findDuaAppByClaimAppId(clmAppData.getPkId());
				duaAppData = duaAppBO.getDuaApplicationData();
				if (DateUtility.isAfterDate(new Date(), duaAppData
						.getDuaDeclarationData().getEndDate())) {
					objAssembly
							.addBusinessError("access.cin.duaselectdisaster.duacompleted.message");
					return objAssembly;
				} else {
					objAssembly
							.addBusinessError("access.cin.duaselectdisaster.duaexpired.message");
					return objAssembly;
				}
			}
		}

		Date clmEffDate;
		// CIF_03362 Start
		/*
		 * if (regClmData != null &&
		 * (ClaimApplicationTypeEnum.NEW.getName().equals
		 * (clmAppData.getClaimAppType()))) { clmEffDate =
		 * regClmData.getEffectiveDate(); } else { clmEffDate =
		 * clmAppData.getClaimEffectiveDate(); }
		 */
		// CIF_03362 End
		ClaimApplicationClaimantData clmAppClmntData = clmAppData
				.getClaimApplicationClaimantData();

		List allDuaDecList = new ArrayList();
		if ((duaAppData == null)
				|| (ApplicationStatusEnum.INITIATED.getName().equals(duaAppData
						.getStatusFlag()))) {// CIF_00563 Start
												// CIF_01360
												// SIT Defect_292 Fix -
												// UserTypeEnum.CLMT changed to
												// UserTypeEnum.CLMT,getName()
			if (getUserContext().getUserType().equals(
					UserTypeEnum.CLMT.getName())) {
				// CIF_04186 - As per the new change in the business rule , we
				// are not going with the claim effective date but with the
				// current date
				clmEffDate = new Date();
				allDuaDecList = DuaDeclaration
						.findDuaDeclarationForDate(clmEffDate);
			} else {
				// CIF_04186 - As per the new change in the business rule , we
				// are not going with the claim effective date but with the
				// current date
				clmEffDate = new Date();
				allDuaDecList = DuaDeclaration.findDuaDeclarationForDate(
						clmEffDate, true);
			}// CIF_00563 End.
				// Added as per req from peggy. Allow to file even if some DUA
				// is effect today.
			/**
			 * 6234 The below logic is now handled in the
			 * DuaDeclaration.findDuaDeclarationForDate() method itself
			 ***/
			/*
			 * List currDuaDecList =
			 * DuaDeclaration.findDuaDeclarationForDate(new Date());
			 * if(currDuaDecList!=null){ for(int i=0; i<currDuaDecList.size();
			 * i++){ DuaDeclarationData duaDecData =
			 * (DuaDeclarationData)currDuaDecList.get(i);
			 * if(!allDuaDecList.contains(duaDecData)){
			 * allDuaDecList.add(duaDecData); //Add if it is NOT already present
			 * } } }
			 */

			if (allDuaDecList.isEmpty()) {
				objAssembly
						.addBusinessError("access.cin.duaselectdisaster.noduadeclaration.message");
				return objAssembly;
			} else {
				for (Iterator i = allDuaDecList.iterator(); i.hasNext();) {
					DuaDeclarationData duaDecData = (DuaDeclarationData) i
							.next();
					LOGGER.info("Disasters found  : "
							+ duaDecData.getDescription());
				}
			}
			if (duaAppData != null) {
				objAssembly.addComponent(duaAppData, true);
			}
		}
		objAssembly.addComponent(clmAppData);
		objAssembly.addComponent(clmAppClmntData);
		objAssembly.addComponentList(allDuaDecList);

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with DuaApplicationData as an input to
	 * extract the value of ClaimAppClmntId from it. This method returns the
	 * ObjectAssembly with DuaApplicationData matching the value of
	 * ClaimAppClmntId, or newly created DuaApplicationData object if there is
	 * no corresponding record.
	 * 
	 * @param objAssembly
	 *            (with DuaApplicationData)
	 * @return objAssembly (with DuaApplicationData - new object or already
	 *         existing where ClaimAppClmntId match)
	 */
	public IObjectAssembly getDuaApplication(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDuaApplication");
		}
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		DuaApplication duaAppBO = DuaApplication
				.findDuaAppByClaimAppId(claimAppData.getPkId());

		objAssembly.removeComponent(DuaApplicationDataBridge.class);
		objAssembly.removeComponent(DuaDeclarationData.class);
		if (duaAppBO != null) {
			if (duaAppBO.getDuaApplicationData() != null) {
				objAssembly.addComponent(duaAppBO.getDuaApplicationData());
				if (duaAppBO.getDuaApplicationData().getDuaDeclarationData() != null) {
					objAssembly.addComponent(duaAppBO.getDuaApplicationData()
							.getDuaDeclarationData());
				}
			}
		}

		return objAssembly;
	}

	public IObjectAssembly batchEstablishClaim(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchEstablishClaim");
		}
		LOGGER.batchRecordProcess(objAssembly.getPrimaryKeyAsLong().toString(),
				"T_CLAIM_APPLICATION");
		// cq 9231 the batch establish claim is the only batch which has to
		// establish claim if there are
		// any pending 525b issues and after the resolution of the issues the
		// will establish the claim
		// In case of Federal or Miliatary employer the system checks with the
		// latest claim with claimeffective
		// date and ssn and the fccc response is processed and donot have any
		// 525b issues then the system will
		// establish claim till then the batch will daily pick up the claim
		// application.
		// In case of missing employer the system will check for two day from
		// the claim filing date and if
		// the missing employer is not resolved the system will establish the
		// claim
		// change implemented on sep 29th 2008
		// to retireve Claim Application Data
		ClaimApplicationData clmAppData = null;
		ClaimApplicationClaimantData clmAppClmntData = null;
		ClaimApplication clmAppBO = ClaimApplication
				.findClaimAppByPkId(objAssembly.getPrimaryKeyAsLong());
		if (clmAppBO != null) {

			clmAppData = clmAppBO.getClaimApplicationData();
			if (clmAppBO.getClaimApplicationData()
					.getClaimApplicationClaimantData() != null) {
				clmAppClmntData = clmAppBO.getClaimApplicationData()
						.getClaimApplicationClaimantData();
			}
		}

		LOGGER.debug("SSN UNDER PROCESS= " + clmAppData.getSsn());

		// to check for duplicate claim
		List clmsList = Claim.fetchClaimBySsn(clmAppData.getSsn());
		try {
			if ((clmsList != null) && (!clmsList.isEmpty())) {
				for (Iterator i = clmsList.iterator(); i.hasNext();) {
					ClaimData clmsData = (ClaimData) i.next();
					if ((clmsData.getEffectiveDate().equals(clmAppData
							.getClaimEffectiveDate()))
							&& (!clmsData.getStatus().equals(
									ClaimStatusEnum.CANCEL.getName()))) {
						objAssembly
								.addBusinessError("error.dup.claim.onsameday");
						break;
					}

				}
			}
		} catch (Exception e) {
			LOGGER.debug("There is an exception for SSN " + clmAppData.getSsn()
					+ "This is the error", e);
			LOGGER.busError(
					"There is an exception for current SSN. This is the error",
					e);
			return objAssembly;
		}
		boolean isLawEnabled = DynamicBizConstant.isHb150LawEnabled(clmAppData
				.getClaimEffectiveDate());
		boolean isRateCalculated = Boolean.TRUE;
		if (isLawEnabled) {
			IUnemploymentRateDao avgRateDao = DAOFactory.instance
					.getUnemloymentRateDao();
			Calendar cal = Calendar.getInstance();
			cal.setTime(clmAppData.getClaimEffectiveDate());
			int[] quarterYear = DateUtility.getQuarterAndYear(cal);
			AverageUnemploymentRateData rateData = avgRateDao
					.getUnemploymetRateDataByYearPeriod(quarterYear[1],
							quarterYear[0] > 2 ? GlobalConstants.TWO
									: GlobalConstants.ONE);
			if (null == rateData) {
				isRateCalculated = Boolean.FALSE;
				objAssembly.addData("HB_LAW_FAILURE", !isRateCalculated);
				return objAssembly;
			}

		}
		// cq R4UAT00004033
		// change done on 07/24/2009
		List wageList = Wage.getAllWages(clmAppData.getSsn(),
				clmAppClmntData.getLastName(),
				clmAppClmntData.getOtherLastName(),
				clmAppData.getBpStartDate(), clmAppData.getBpEndDate());

		// R4UAT00006300 fix
		// on 08/28/2009
		try {
			if (wageList != null && wageList.size() > 0) {
				for (Iterator wageListIterator = wageList.iterator(); wageListIterator
						.hasNext();) {
					WageData wageData = (WageData) wageListIterator.next();
					boolean isvalidEmployer = false;
					if (EmployerTypeEnum.FED.getName().equals(
							wageData.getEmployeeData().getEmployerData()
									.getEmployerType())
							|| EmployerTypeEnum.MIL.getName().equals(
									wageData.getEmployeeData()
											.getEmployerData()
											.getEmployerType())) {
						FcccRequest fcccRequest = FcccRequest
								.findbySsnAndEffectiveDate(clmAppData.getSsn(),
										clmAppData.getClaimEffectiveDate());
						if (fcccRequest == null
								|| fcccRequest.getFcccRequestData() == null) {
							objAssembly
									.addBusinessError("error.no.fccctype1.request");
							break;
						} else {
							// R4UAT00005880
							// change done on 08/18/2009
							for (Iterator emprIt = clmAppBO
									.getClaimApplicationData()
									.getClaimApplicationEmployerData()
									.iterator(); emprIt.hasNext();) {
								ClaimApplicationEmployerData claimAppEmprData = (ClaimApplicationEmployerData) emprIt
										.next();
								if (EmployerTypeEnum.FED.getName().equals(
										claimAppEmprData.getEmployerType())
										|| EmployerTypeEnum.MIL.getName()
												.equals(claimAppEmprData
														.getEmployerType())) {
									if (claimAppEmprData
											.getEmployerData()
											.getEmployerId()
											.equals(wageData.getEmployeeData()
													.getEmployerData()
													.getEmployerId())) {
										isvalidEmployer = true;
									}
								}
							}
							if (!isvalidEmployer) {
								objAssembly
										.addBusinessError("error.not.having.claimapplication.employer.entry");
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.debug("There is an exception for SSN " + clmAppData.getSsn()
					+ "This is the error", e);
			LOGGER.busError(
					"There is an exception for SSN with claim_application_id as"
							+ objAssembly.getPrimaryKeyAsLong()
							+ ". This is the error", e);
			return objAssembly;
		}

		// to check if there is name mismatch issue
		boolean isHavingNameMismatch = false;
		boolean isHavingIncorrectWage = false;
		boolean isHavingMissingEmployerConstraint = false;
		boolean notHavingAny525bIssues = false;
		boolean isHavingFederalEmployments = false;
		// CIF_01416 starts
		// boolean fcccResponseReceived = false;
		// flags added to check whether it is fed(F) or military
		boolean isFederalEmployer = false;
		boolean isMilitaryEmployer = false;
		boolean notInsuredWorker = false;
		boolean notHavingBackDateWI = true;
		boolean fcccMilResponseReceived = false;
		boolean fcccFedResponseReceived = false;
		// CIF_01416 ends
		// Getting all the 525b issues basing on claim application id
		List list = Form525B.find525BDataByClmAppId(clmAppData.getPkId());
		if (null != list && !list.isEmpty()) {
			for (Iterator i = list.iterator(); i.hasNext();) {
				Form525BData data = (Form525BData) i.next();
				// check for Name Mismatch
				if (Form525BTypeEnum.NameMismatch.getName().equals(
						(data.getForm525B_Type()))
						&& (StringUtility.isBlank(data.getProcessedFlag()) || YesNoTypeEnum.NumericNo
								.getName().equals(data.getProcessedFlag()))) {
					// if having a NameMismatch and is not processed donot
					// process further
					isHavingNameMismatch = true;
					break;
				}
				// check for incorrect wage
				if (Form525BTypeEnum.Incorrect.getName().equals(
						(data.getForm525B_Type()))
						&& (StringUtility.isBlank(data.getProcessedFlag()) || YesNoTypeEnum.NumericNo
								.getName().equals(data.getProcessedFlag()))) {
					// if having a incorrect wage and is not processed donot
					// process further
					isHavingIncorrectWage = true;
					break;
				}
				// check for MissingEmployer
				if (Form525BTypeEnum.Missing.getName().equals(
						(data.getForm525B_Type()))
						&& (StringUtility.isBlank(data.getProcessedFlag()) || YesNoTypeEnum.NumericNo
								.getName().equals(data.getProcessedFlag()))) {
					// if having a incorrect wage and is not processed donot
					// process further
					isHavingMissingEmployerConstraint = true;
					Calendar cal = new GregorianCalendar();
					Calendar fileDate = new GregorianCalendar();
					fileDate.setTime(clmAppBO.getClaimApplicationData()
							.getFileDate());
					// check whether the difference between the current and file
					// date is greatee than two days
					if (DateUtility.diffDays(fileDate, cal) > 2) {
						isHavingMissingEmployerConstraint = false;
						break;
					}
				}
			}
		}
		// check for backDateWI during claims intake
		// getWorkItemDetailsBySSNAndWorkItemName
		WorkSearchDetail wsDetail = new WorkSearchDetail();
		String[] workItemNameAr = WorkflowProcessTemplateConstants.Claims.REQUEST_BACK_POSTDATE_CLAIM_INTAKE
				.split("\\" + GlobalConstants.FILE_DOT);
		String workItemName = workItemNameAr[workItemNameAr.length - 1];
		WorkitemDetailData wiDetailData = wsDetail
				.getWorkItemDetailsBySSNAndWorkItemName(clmAppData.getSsn(),
						workItemName);
		if (null != wiDetailData
				&& !wiDetailData.getWorkitemStatusCode().equalsIgnoreCase(
						"COMP")) {
			notHavingBackDateWI = Boolean.FALSE;
		}

		// if the claimapplication is not having any 525B issues
		if (!isHavingNameMismatch && !isHavingIncorrectWage
				&& !isHavingMissingEmployerConstraint) {
			notHavingAny525bIssues = true;
		}
		// check for any federal Employments in from claim application employer
		for (Iterator emprIt = clmAppBO.getClaimApplicationData()
				.getClaimApplicationEmployerData().iterator(); emprIt.hasNext();) {
			ClaimApplicationEmployerData claimAppEmprData = (ClaimApplicationEmployerData) emprIt
					.next();
			if ((claimAppEmprData != null)
					&& ((claimAppEmprData.getEmployerType()
							.equals(EmployerTypeEnum.FED.getName())) || (claimAppEmprData
							.getEmployerType().equals(EmployerTypeEnum.MIL
							.getName())))) {
				isHavingFederalEmployments = true;
				if (claimAppEmprData.getEmployerType().equals(
						EmployerTypeEnum.FED.getName())) {
					isFederalEmployer = true;
				} else if (claimAppEmprData.getEmployerType().equals(
						EmployerTypeEnum.MIL.getName())) {
					isMilitaryEmployer = true;
				}

				break;
			}
		}
		// this block is added for requirement so as to keep the claim in
		// pending state if the claimant is NIW on missouri wages and
		// fed/mil/cwc wages are pending
		// CIF_01305 starts
		Set wageSet = new HashSet();
		wageSet.addAll(wageList);
		BigDecimal wba = Claim.calculateWba(wageSet,
				clmAppData.getClaimEffectiveDate());
		BigDecimal mba = new BigDecimal(BigInteger.ZERO, 2);
		// CIF_INT_00413 ||Law HB150
		mba = Claim.calculateMba(wba, wageSet,
				clmAppData.getClaimEffectiveDate());
		if (mba.compareTo(BigDecimal.ZERO) < 1) {
			notInsuredWorker = true;
		}
		// CIF_01305 ends
		ES931ResponseData es931ResponseData = null;
		if (isHavingFederalEmployments) {
			// *************************************************************************
			// This change has been added for CQ 1721 - 02/13/2009
			// CIF_01416 starts
			// As per new requirement valid response will be Type 1 response.
			if (isFederalEmployer) {
				// check verify federal employer WI
				boolean federalEmployerWi = Boolean.TRUE;
				wsDetail = new WorkSearchDetail();
				workItemNameAr = Monetary.WORKITEM_FEX.split("\\"
						+ GlobalConstants.FILE_DOT);
				workItemName = workItemNameAr[workItemNameAr.length - 1];
				wiDetailData = wsDetail.getWorkItemDetailsBySSNAndWorkItemName(
						clmAppData.getSsn(), workItemName);
				if (null != wiDetailData
						&& !wiDetailData.getWorkitemStatusCode()
								.equalsIgnoreCase("COMP")) {
					federalEmployerWi = Boolean.FALSE;
				}
				if (federalEmployerWi) {
					// CIF_02195 starts checking es931 response also to
					// establish claim
					FcccRequest fcccRequest = FcccRequest
							.findbySsnEffectiveDateAndProgrammeTypeForType1(
									clmAppBO.getClaimApplicationData().getSsn(),
									clmAppBO.getClaimApplicationData()
											.getClaimEffectiveDate(),
									BatchConstants.REDETERMINATION_UCFE);
					if (fcccRequest != null) {

						FCCCResponseData fcccResponse = FcccResponse
								.fetchRecentFcccResponseWithSSNandEffectiveDateAndProgramType(
										clmAppBO.getClaimApplicationData()
												.getSsn(), clmAppBO
												.getClaimApplicationData()
												.getClaimEffectiveDate(), "F",
										fcccRequest.getFcccRequestData()
												.getTransmissionDate());
						if (fcccResponse != null
								&& GlobalConstants.DB_ANSWER_YES
										.equals(fcccResponse.getProcessedFlag())) {
							es931ResponseData = ES931Response
									.getEs931ResponseDataBySSNandEffectiveDate(
											clmAppBO.getClaimApplicationData()
													.getSsn(), clmAppBO
													.getClaimApplicationData()
													.getClaimEffectiveDate(),
											fcccResponse.getTransmissionDate());
							if (null != es931ResponseData
									&& GlobalConstants.DB_ANSWER_YES
											.equals(es931ResponseData
													.getResponseProcessed())) {
								fcccFedResponseReceived = true;
							}
						}

					}
				}
				// CIF_02195 ends
			}
			if (isMilitaryEmployer) {
				FcccResponse fcccResponse = FcccResponse
						.findbySsnAndEffectiveDateAndProgramTypeForType1(
								clmAppBO.getClaimApplicationData().getSsn(),
								clmAppBO.getClaimApplicationData()
										.getClaimEffectiveDate(), "X");
				if (fcccResponse != null
						&& GlobalConstants.DB_ANSWER_YES.equals(fcccResponse
								.getFcccResponseData().getProcessedFlag())) {
					fcccMilResponseReceived = true;
				}
			}

			/*
			 * FcccResponse fcccResponse =
			 * FcccResponse.findLatestbySsnAndEffectiveDateForType1
			 * (clmAppBO.getClaimApplicationData
			 * ().getSsn(),clmAppBO.getClaimApplicationData
			 * ().getClaimEffectiveDate()); // check whether for the fccc
			 * response with process flag if(fcccResponse != null &&
			 * GlobalConstants
			 * .DB_ANSWER_YES.equals(fcccResponse.getFcccResponseData
			 * ().getProcessedFlag())){ fcccResponseReceived = true; }else{
			 * fcccResponseReceived = false; }
			 */
			// CIF_01416 ends
		}
		RegularClaim regClmBO = null;
		// @cif_wy(storyNumber = "P1-CM-055", requirementId = "FR_1614",
		// designDocName = "01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection = "1.26.4", dcrNo = "", mddrNo = "", impactPoint
		// = "Start")
		// establish claim if 525b issue exits and it is insured worker
		if ((notHavingAny525bIssues && !notInsuredWorker && notHavingBackDateWI)
				|| (notHavingAny525bIssues && !notInsuredWorker)) {
			// @cif_wy(storyNumber = "P1-CM-055", requirementId = "FR_1614",
			// designDocName = "01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection = "1.26.4", dcrNo = "", mddrNo = "", impactPoint
			// = "End")
			ClaimantData clmntData = null;
			Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
			if (clmntBO != null) {
				clmntData = clmntBO.getClaimantData();
			} else {
				clmntData = new ClaimantDataBridge();
			}
			// code commented removed after investigation 3146 which was before
			// commented as a fix for 1171
			clmntBO = new Claimant(clmntData);
			clmntBO = clmntBO.processClaimant(clmAppClmntData);
			clmntBO.saveOrUpdate();
			clmntBO.flush();

			// to establish Regular Claim
			regClmBO = new RegularClaim(null);
			try {
				regClmBO.doEstablishClaim(clmntData, clmAppData,
						clmAppClmntData);
			} catch (BaseApplicationException e) {
				/*
				 * added as part of termination of work items if some
				 * transaction errors occur
				 */
				GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
						.fetchORCreateBean(GenericWorkflowSearchBean.class);
				bean.setGlobalContainerMemberLike("ssneanfein",
						objAssembly.getSsn());
				bean.setGlobalContainerMember("type", "ssn");
				bean.setReceivedTimeBetween(new Date(),
						DateUtility.getNextDay(new Date()));
				regClmBO.terminateWorkItems(bean);
				LOGGER.error("error while establishing claim" + e);
				objAssembly.addBusinessError(e.getMessage());
				objAssembly.addData("error", e);
				return objAssembly;
			}
		} else if (isHavingFederalEmployments) {
			// CIF_01416 starts
			if (fcccMilResponseReceived && notHavingAny525bIssues
					&& isMilitaryEmployer && notHavingBackDateWI) {
				// CIF_01416 ends
				ClaimantData clmntData = null;
				Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
				if (clmntBO != null) {
					clmntData = clmntBO.getClaimantData();
				} else {
					clmntData = new ClaimantDataBridge();
				}
				// code commented removed after investigation 3146 which was
				// before commented as a fix for 1171
				clmntBO = new Claimant(clmntData);
				clmntBO = clmntBO.processClaimant(clmAppClmntData);
				clmntBO.saveOrUpdate();
				clmntBO.flush();
				// to establish Regular Claim
				regClmBO = new RegularClaim(null);
				try {
					regClmBO.doEstablishClaim(clmntData, clmAppData,
							clmAppClmntData);
				} catch (BaseApplicationException e) {
					/*
					 * added as part of termination of work items if some
					 * transaction errors occur
					 */
					GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
							.fetchORCreateBean(GenericWorkflowSearchBean.class);
					bean.setGlobalContainerMemberLike("ssneanfein",
							objAssembly.getSsn());
					bean.setGlobalContainerMember("type", "ssn");
					bean.setReceivedTimeBetween(new Date(),
							DateUtility.getNextDay(new Date()));
					regClmBO.terminateWorkItems(bean);
					LOGGER.error("error while establishing claim" + e);
					objAssembly.addBusinessError(e.getMessage());
					objAssembly.addData("error", e);
					return objAssembly;
				}
			}

			/*
			 * CQ 1721 - 02/13/2009 - Claim is to be established in case of
			 * military even though any FCCC response is not recieved with in 2
			 * days of claim filing. CQ 1721 - 02/13/2009
			 */
			// CIF_01416 starts
			/*
			 * else if(!fcccResponseReceived && notHavingAny525bIssues &&
			 * notHavingBackDateWI) { if(isMilitaryEmployer) { Calendar cal =
			 * new GregorianCalendar(); Calendar fileDate = new
			 * GregorianCalendar();
			 * fileDate.setTime(clmAppBO.getClaimApplicationData
			 * ().getFileDate()); // check whether the difference between the
			 * current and file date is greatee than two days
			 * if(DateUtility.diffDays(fileDate,cal) > 2) { ClaimantData
			 * clmntData = null; Claimant clmntBO =
			 * Claimant.findBySsn(objAssembly.getSsn()); if (clmntBO != null) {
			 * clmntData = clmntBO.getClaimantData(); } else { clmntData = new
			 * ClaimantData(); } // change done on 3257 clmntBO = new
			 * Claimant(clmntData); clmntBO =
			 * clmntBO.processClaimant(clmAppClmntData); clmntBO.saveOrUpdate();
			 * clmntBO.flush(); // to establish Regular Claim RegularClaim
			 * regClmBO = new RegularClaim(null); try {
			 * regClmBO.doEstablishClaim(clmntData, clmAppData,
			 * clmAppClmntData); } catch (BaseApplicationException e) { added as
			 * part of termination of work items if some transaction errors
			 * occur GenericWorkflowSearchBean bean =
			 * (GenericWorkflowSearchBean)
			 * objAssembly.fetchORCreateBean(GenericWorkflowSearchBean.class);
			 * bean.setGlobalContainerMemberLike("ssneanfein",
			 * objAssembly.getSsn()); bean.setGlobalContainerMember("type",
			 * "ssn"); bean.setReceivedTimeBetween(new Date(),
			 * DateUtility.getNextDay(new Date()));
			 * regClmBO.terminateWorkItems(bean);
			 * objAssembly.addBusinessError(e.getMessage()); return objAssembly;
			 * } }
			 * 
			 * } }
			 */
			// CIF_01416 ends
			// In case of Federal (not military) system will not check for
			// federal response.
			// system will only check for WI resolved flag.
			// If WI is resolved system will establish claim will available
			// wages in system.
			// CIF_02195 starts expecting ES931 response and federal response to
			// establish claim
			else if (isFederalEmployer && notHavingAny525bIssues
					&& notHavingBackDateWI && fcccFedResponseReceived) {
				ClaimantData clmntData = null;
				Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
				if (clmntBO != null) {
					clmntData = clmntBO.getClaimantData();
				} else {
					// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
					// requirementId = "FR_1614", designDocName =
					// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
					// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo =
					// "", impactPoint = "Start")
					clmntData = new ClaimantDataBridge();
					// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
					// requirementId = "FR_1614", designDocName =
					// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
					// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo =
					// "", impactPoint = "End")
				}
				// change done on 3257
				clmntBO = new Claimant(clmntData);
				clmntBO = clmntBO.processClaimant(clmAppClmntData);
				clmntBO.saveOrUpdate();
				clmntBO.flush();
				// to establish Regular Claim
				regClmBO = new RegularClaim(null);
				try {
					regClmBO.doEstablishClaim(clmntData, clmAppData,
							clmAppClmntData);
				} catch (BaseApplicationException e) {
					/*
					 * added as part of termination of work items if some
					 * transaction errors occur
					 */
					GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
							.fetchORCreateBean(GenericWorkflowSearchBean.class);
					bean.setGlobalContainerMemberLike("ssneanfein",
							objAssembly.getSsn());
					bean.setGlobalContainerMember("type", "ssn");
					bean.setReceivedTimeBetween(new Date(),
							DateUtility.getNextDay(new Date()));
					regClmBO.terminateWorkItems(bean);
					LOGGER.error("error while establishing claim" + e);
					objAssembly.addBusinessError(e.getMessage());
					objAssembly.addData("error", e);
					return objAssembly;
				}

			} else if (notHavingAny525bIssues && notHavingBackDateWI) {
				List ib4RequestList = Ib4Request
						.findBySsnAndEffectiveDate(clmAppData.getSsn(),
								clmAppData.getClaimEffectiveDate());
				if (null != ib4RequestList && !ib4RequestList.isEmpty()) {
					List ib4ResponseList = Ib4Response
							.findIncomingBySsnEffectiveDate(
									clmAppData.getSsn(),
									clmAppData.getClaimEffectiveDate());
					if (null != ib4ResponseList && !ib4ResponseList.isEmpty()) {
						ClaimantData clmntData = null;
						Claimant clmntBO = Claimant.findBySsn(objAssembly
								.getSsn());
						if (clmntBO != null) {
							clmntData = clmntBO.getClaimantData();
						} else {
							clmntData = new ClaimantDataBridge();
						}
						clmntBO = new Claimant(clmntData);
						clmntBO = clmntBO.processClaimant(clmAppClmntData);
						clmntBO.saveOrUpdate();
						clmntBO.flush();

						// to establish Regular Claim
						regClmBO = new RegularClaim(null);
						try {
							regClmBO.doEstablishClaim(clmntData, clmAppData,
									clmAppClmntData);
						} catch (BaseApplicationException e) {
							/*
							 * added as part of termination of work items if
							 * some transaction errors occur
							 */
							GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
									.fetchORCreateBean(GenericWorkflowSearchBean.class);
							bean.setGlobalContainerMemberLike("ssneanfein",
									objAssembly.getSsn());
							bean.setGlobalContainerMember("type", "ssn");
							bean.setReceivedTimeBetween(new Date(),
									DateUtility.getNextDay(new Date()));
							regClmBO.terminateWorkItems(bean);
							LOGGER.error("error while establishing claim" + e);
							objAssembly.addBusinessError(e.getMessage());
							objAssembly.addData("error", e);
							return objAssembly;
						}
					}
				}
			}
			// CIF_01305 starts
		} else if (notInsuredWorker && notHavingAny525bIssues
				&& notHavingBackDateWI) {
			List ib4RequestList = Ib4Request.findBySsnAndEffectiveDate(
					clmAppData.getSsn(), clmAppData.getClaimEffectiveDate());
			if (null != ib4RequestList && !ib4RequestList.isEmpty()) {
				List ib4ResponseList = Ib4Response
						.findIncomingBySsnEffectiveDate(clmAppData.getSsn(),
								clmAppData.getClaimEffectiveDate());
				if (null != ib4ResponseList && !ib4ResponseList.isEmpty()) {
					ClaimantData clmntData = null;
					Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
					if (clmntBO != null) {
						clmntData = clmntBO.getClaimantData();
					} else {
						// @cif_wy(storyNumber =
						// "P1-CM-012, P1-CM-013,P1-CM-014", requirementId =
						// "FR_1614", designDocName =
						// "01 Regular UI Claim,EUCC,EB.docx", designDocSection
						// = "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83",
						// mddrNo = "", impactPoint = "Start")
						clmntData = new ClaimantDataBridge();
						// @cif_wy(storyNumber =
						// "P1-CM-012, P1-CM-013,P1-CM-014", requirementId =
						// "FR_1614", designDocName =
						// "01 Regular UI Claim,EUCC,EB.docx", designDocSection
						// = "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83",
						// mddrNo = "", impactPoint = "End")
					}
					clmntBO = new Claimant(clmntData);
					clmntBO = clmntBO.processClaimant(clmAppClmntData);
					clmntBO.saveOrUpdate();
					clmntBO.flush();

					// to establish Regular Claim
					regClmBO = new RegularClaim(null);
					try {
						regClmBO.doEstablishClaim(clmntData, clmAppData,
								clmAppClmntData);
					} catch (BaseApplicationException e) {
						/*
						 * added as part of termination of work items if some
						 * transaction errors occur
						 */
						GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
								.fetchORCreateBean(GenericWorkflowSearchBean.class);
						bean.setGlobalContainerMemberLike("ssneanfein",
								objAssembly.getSsn());
						bean.setGlobalContainerMember("type", "ssn");
						bean.setReceivedTimeBetween(new Date(),
								DateUtility.getNextDay(new Date()));
						regClmBO.terminateWorkItems(bean);
						LOGGER.error("error while establishing claim" + e);
						objAssembly.addBusinessError(e.getMessage());
						objAssembly.addData("error", e);
						return objAssembly;
					}
				}
			} else {
				ClaimantData clmntData = null;
				Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
				if (clmntBO != null) {
					clmntData = clmntBO.getClaimantData();
				} else {
					clmntData = new ClaimantDataBridge();
				}
				clmntBO = new Claimant(clmntData);
				clmntBO = clmntBO.processClaimant(clmAppClmntData);
				clmntBO.saveOrUpdate();
				clmntBO.flush();

				// to establish Regular Claim
				regClmBO = new RegularClaim(null);
				try {
					regClmBO.doEstablishClaim(clmntData, clmAppData,
							clmAppClmntData);
				} catch (BaseApplicationException e) {
					/*
					 * added as part of termination of work items if some
					 * transaction errors occur
					 */
					GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
							.fetchORCreateBean(GenericWorkflowSearchBean.class);
					bean.setGlobalContainerMemberLike("ssneanfein",
							objAssembly.getSsn());
					bean.setGlobalContainerMember("type", "ssn");
					bean.setReceivedTimeBetween(new Date(),
							DateUtility.getNextDay(new Date()));
					regClmBO.terminateWorkItems(bean);
					LOGGER.error("error while establishing claim" + e);
					objAssembly.addBusinessError(e.toString());
					objAssembly.addData("error", e);
					return objAssembly;
				}
			}
		}

		if (null != regClmBO
				&& null != regClmBO.getClaimData()
				&& null != es931ResponseData
				&& ES931ReasonOfSeparationEnum.ES93X_QUIT.getDescription()
						.equals(es931ResponseData.getSeperationReason())
				&& ES931ReasonOfSeparationEnum.ES93X_DISCHARGED
						.getDescription().equals(
								es931ResponseData.getSeperationReason())
				&& ES931ReasonOfSeparationEnum.ES93X_LABOR_DISPUTE
						.getDescription().equals(
								es931ResponseData.getSeperationReason())
				&& ES931ReasonOfSeparationEnum.ES93X_RETIRMENT.getDescription()
						.equals(es931ResponseData.getSeperationReason())
				&& ES931ReasonOfSeparationEnum.ES93X_OTHER.getDescription()
						.equals(es931ResponseData.getSeperationReason())) {
			es931ResponseData.setClaimData(regClmBO.getClaimData());
			ES931Response es931Response = new ES931Response(es931ResponseData);

			es931Response.createIncomingEmployersResponseWIFromES931Response();
		}
		// CIF_01305 ends
		return objAssembly;
	}

	/**
	 * This is the private method called during Batch Establish Claim.
	 * 
	 * @param objAssembly
	 * @return
	 */
	private IObjectAssembly establishClaim(IObjectAssembly objAssembly) {
		// to retireve Claim Application Data
		ClaimApplicationData clmAppData = null;
		ClaimApplicationClaimantData clmAppClmntData = null;
		ClaimApplication clmAppBO = ClaimApplication
				.findClaimAppByPkId(objAssembly.getPrimaryKeyAsLong());

		if (clmAppBO != null) {
			clmAppData = clmAppBO.getClaimApplicationData();
			if (clmAppBO.getClaimApplicationData()
					.getClaimApplicationClaimantData() != null) {
				clmAppClmntData = clmAppBO.getClaimApplicationData()
						.getClaimApplicationClaimantData();
			}
		}

		ClaimantData clmntData = null;
		Claimant clmntBO = Claimant.findBySsn(objAssembly.getSsn());
		if (clmntBO != null) {
			clmntData = clmntBO.getClaimantData();
		} else {
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "Start")
			clmntData = new ClaimantDataBridge();
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "End")
		}

		RegularClaim regClmBO = new RegularClaim(null);

		try {
			regClmBO.doEstablishClaim(clmntData, clmAppData, clmAppClmntData);
		} catch (BaseApplicationException e) {
			/*
			 * added as part of termination of work items if some transaction
			 * errors occur
			 */
			GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
					.fetchORCreateBean(GenericWorkflowSearchBean.class);
			bean.setGlobalContainerMemberLike("ssneanfein",
					objAssembly.getSsn());
			bean.setGlobalContainerMember("type", "ssn");
			bean.setReceivedTimeBetween(new Date(),
					DateUtility.getNextDay(new Date()));

			regClmBO.terminateWorkItems(bean);
			objAssembly.addBusinessError(e.getMessage());
			return objAssembly;
		}

		return objAssembly;
	}

	/**
	 * save the 512a informaiton for proof submitted by the claimant.
	 */
	public IObjectAssembly saveUi512AResponse(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveUi512AResponse");
		}
		List lstUi512A = objAssembly.getComponentList(UI512AResponseData.class);
		if (lstUi512A != null) {
			for (Iterator it = lstUi512A.iterator(); it.hasNext();) {
				UI512AResponseData ui512AData = (UI512AResponseData) it.next();
				UI512AResponse ui512ABO = new UI512AResponse(ui512AData);
				ui512ABO.saveOrUpdate();
			}
		}
		return objAssembly;
	}

	/**
	 * Process the Address Issue screen close any previous issue if the CSR has
	 * said that 8x is evident or proof submitted.
	 */
	public IObjectAssembly processPreviousIssues(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processPreviousIssues");
		}
		List beanList = (List) objAssembly
				.getBeanList(CinRequalificationCriteriaBean.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		List clmEmpList = objAssembly
				.getComponentList(ClaimApplicationEmployerData.class);
		if (beanList != null) {
			for (Iterator it = beanList.iterator(); it.hasNext();) {
				CinRequalificationCriteriaBean bean = (CinRequalificationCriteriaBean) it
						.next();
				if (StringUtility.isNotBlank(bean.getIssueId())
						&& (CinRequalificationCriteriaEnum.EVIDENT.getName()
								.equals(bean.getActionTaken()) || ViewConstants.REQ8X_SYSTEM_EVIDENT
								.equals(bean.getActionTaken()))) {
					Calendar cal = new GregorianCalendar();
					cal.setTime(clmAppData.getClaimEffectiveDate());
					cal.add(Calendar.DAY_OF_YEAR, -1);
					Issue.updateIssueEndDate(Long.valueOf(bean.getIssueId()),
							cal.getTime());
				} else if (StringUtility.isNotBlank(bean
						.getClaimAppEmployerId())) {

					for (Iterator clmEmpIt = clmEmpList.iterator(); clmEmpIt
							.hasNext();) {
						ClaimApplicationEmployerData clmEmpData = (ClaimApplicationEmployerData) clmEmpIt
								.next();
						if (clmEmpData.getPkId().equals(
								Long.valueOf(bean.getClaimAppEmployerId()))) {
							clmEmpData.setRequalificationStatus(bean
									.getActionTaken());
							ClaimApplicationEmployer claimAppEmpBO = new ClaimApplicationEmployer(
									clmEmpData);
							claimAppEmpBO.saveOrUpdate();
						}
					}
				}
			}
		}

		// objAssembly =
		// this.saveOrUpdateNotEvidentClaimAppEmprList(objAssembly);

		// objAssembly = this.saveOrUpdateEvidentClaimAppEmprList(objAssembly);

		return objAssembly;
	}

	/**
	 * This method generates the EbNoticition correspondances for the input
	 * claim ids
	 * 
	 * @param objAssembly
	 * @return
	 */
	public IObjectAssembly genEbNotification(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method genEbNotification");
		}
		ClaimantData claimantData = null;
		long[] arr = (long[]) objAssembly.getData("EBCLAIMANTDTLS");
		Long claimId = Long.valueOf(arr[0]);
		// Long claimantId = Long.valueOf(arr[1]);
		Claim claimBO = Claim.findByPrimaryKey(claimId);
		claimantData = claimBO.getClaimData().getClaimantData();
		if (claimantData != null) {
			objAssembly.removeAll();
			objAssembly.addComponent(claimantData);
			// to generate correspondence
			CorrespondenceData corrData = new CorrespondenceData();
			corrData.setCorrespondenceCode(CorrespondenceCodeEnum.EB_NOTIFICATION
					.getName());
			corrData.setClaimantData(claimantData);
			corrData.setDirection(CorrespondenceDirectionEnum.OUTGOING
					.getName());
			Correspondence correspondenceBO = new Correspondence(corrData);
			correspondenceBO.saveOrUpdate();
			objAssembly.addData("Status_Message",
					"Correspondence Successfully saved");
		} else {
			objAssembly.addData("Status_Message", "No Matching Records found");
		}
		return objAssembly;
	}

	public IObjectAssembly batchInsVerification(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchInsVerification");
		}

		ClaimApplicationClaimant clmAppClmntBO = ClaimApplicationClaimant
				.findByPrimaryKey(objAssembly.getPrimaryKeyAsLong());

		if (clmAppClmntBO != null) {
			clmAppClmntBO.processInsRequest();
			ClaimApplicationClaimantData clmAppClmntData = clmAppClmntBO
					.getClaimApplicationClaimantData();
			// Check claim is established and create issue if necessary
			if (AlienStatusEnum.V.getName().equals(
					clmAppClmntData.getInsStatus())) {
				ClaimData claimData = clmAppClmntData.getClaimApplicationData()
						.getClaimData();
				if (null != claimData) {
					InsResponseData insResponseData = clmAppClmntBO
							.getInsResponseData();
					Set<ClaimAlienData> claimAlienDataList = new HashSet<ClaimAlienData>();
					Boolean createAlienIssue = Boolean.FALSE;
					if (null != insResponseData) {
						Set<InsResponseAuthData> respAuthDataSet = insResponseData
								.getInsResponseAuthData();

						if (null != respAuthDataSet
								&& !respAuthDataSet.isEmpty()) {
							// Work Authorization Found , This data is to be
							// saved in T_CLAIM_ALIEN
							ClaimAlienData claimAlienData = null;
							for (InsResponseAuthData insResponseAuthData : respAuthDataSet) {
								claimAlienData = new ClaimAlienData();
								claimAlienData.setClaimData(claimData);
								claimAlienData.setStartDate(insResponseAuthData
										.getStartDate());
								claimAlienData
										.setExpirationDate(insResponseAuthData
												.getExpirationDate());
								claimAlienData.setProvCode(insResponseAuthData
										.getProvLawCode());
								// jdbcClaimDAO.createClaimAlien(claimAlienData);
								claimAlienDataList.add(claimAlienData);
							}
						} else {
							// No Authorization is received check for entry date
							// and admitted to date
							// if admitted to date is not received then check
							// admitted to text
							// if entry date is null even then create issue and
							// do not prorate
							if (null != insResponseData.getEntryDate()) {
								ClaimAlienData claimAlienData = new ClaimAlienData();
								claimAlienData.setClaimData(claimData);
								claimAlienData.setStartDate(insResponseData
										.getEntryDate());

								if (null != insResponseData.getAdmittedTo()) {
									claimAlienData
											.setExpirationDate(insResponseData
													.getAdmittedTo());
								} else if (StringUtility
										.isNotBlank(insResponseData
												.getAdmittedToText())
										&& "INDEFINITE"
												.equalsIgnoreCase(insResponseData
														.getAdmittedToText())) {
									claimAlienData
											.setExpirationDate(DateFormatUtility
													.parse(GlobalConstants.INDEFINITE_DATE));
								} else {
									// dates inconclusive create Issue for CSR
									// action
									claimAlienData = null;
									createAlienIssue = Boolean.TRUE;
								}
								if (null != claimAlienData) {
									// jdbcClaimDAO.createClaimAlien(claimAlienData);
									claimAlienDataList.add(claimAlienData);
								}
							} else {
								createAlienIssue = Boolean.TRUE;
							}
						}
						if (null != claimAlienDataList
								&& !claimAlienDataList.isEmpty()) {
							claimData.setClaimAlienData(claimAlienDataList);
							RegularClaim claim = new RegularClaim(claimData);
							claim.saveOrUpdate();
						}
					}
					if (!createAlienIssue) {
						List<IssueData> issueList = Issue
								.getIssueByClaimIdCategoryAndSubcategory(
										claimData.getClaimId(),
										IssueCategoryEnum.ABLE_AND_AVAILABLE
												.getName(),
										IssueSubCategoryEnum.ALIEN_STATUS
												.getName());
						if (null != issueList && !issueList.isEmpty()) {
							IssueData issueData = issueList.get(0);
							Date date = issueData.getIssueStartDate();
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							cal.add(Calendar.DAY_OF_MONTH,
									GlobalConstants.NUMERIC_MINUS_ONE);
							issueData.setIssueEndDate(cal.getTime());
							Issue issue = new Issue(issueData);
							issue.saveOrUpdate();
						} else {
							RegularClaim regClaim = new RegularClaim(claimData);
							// CIF_03370 setting rede bean for redetermination
							Set<WageData> claimWageDataSet = claimData
									.getWages();
							List<RedeterminationOfClaimWageBean> wageBeanList = new ArrayList<RedeterminationOfClaimWageBean>();
							if (null != claimWageDataSet
									&& !claimWageDataSet.isEmpty()) {
								RedeterminationOfClaimWageBean redeWageBean = null;
								for (WageData claimWageData : claimWageDataSet) {
									redeWageBean = new RedeterminationOfClaimWageBean();
									if (ViewConstants.YES.equals(claimWageData
											.getUsedFlag())) {
										redeWageBean
												.setUseEmployerWage(Boolean.TRUE);
										redeWageBean
												.setUseClaimWage(Boolean.FALSE);
										redeWageBean.setWageId(claimWageData
												.getWageId());
										wageBeanList.add(redeWageBean);
									}
								}
							}
							// CIF_INT_02244
							regClaim.processRedetermination(
									null,
									null,
									MonetaryRedeterminationReasonEnum.REASON_TAX
											.getName(), null, null,
									wageBeanList, Boolean.FALSE, Boolean.FALSE);
						}
					}
				}
			}
			/*
			 * if(ApplicationStatusEnum.COMPLETED.getName().equals(clmAppClmntData
			 * .getClaimApplicationData().getClaimAppStatus())){
			 * if(AlienStatusEnum
			 * .P.getName().equals(clmAppClmntData.getInsStatus()) ||
			 * AlienStatusEnum
			 * .I.getName().equals(clmAppClmntData.getInsStatus())){ //Create
			 * Alien Status Issue IssueBean alienStatusIssue = new IssueBean();
			 * alienStatusIssue.setClaimantSsn(clmAppClmntData.getSsn());
			 * alienStatusIssue.setDateIssueDetected(new Date());
			 * alienStatusIssue.setInformationProvidedBy("");
			 * alienStatusIssue.setInformationProvidedHow("");
			 * alienStatusIssue.setIssueDescription
			 * (IssueCategoryEnum.ABLE_AND_AVAILABLE.getName());
			 * alienStatusIssue
			 * .setIssueDetails(IssueSubCategoryEnum.ALIEN_STATUS.getName());
			 * alienStatusIssue
			 * .setIssueEndDate(DateFormatUtility.parse(GlobalConstants
			 * .INDEFINITE_DATE));
			 * alienStatusIssue.setIssueSource(IssueSourceEnum
			 * .COMMONINTAKE.getName()); alienStatusIssue.setIssueStartDate(new
			 * Date()); Issue.createIssue(alienStatusIssue); Claimant claimantBO
			 * = Claimant.findBySsn(clmAppClmntData.getSsn()); ClaimantData
			 * clmntData = claimantBO.getClaimantData();
			 * claimantBO.createBusinessProcessEventComment("PINF",
			 * "ALIEN_SEC_REQ", clmntData); } }
			 */
		}

		return objAssembly;

	}

	public IObjectAssembly getRegClaimByClaimAppId(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRegClaimByClaimAppId");
		}

		objAssembly.removeComponent(RegularClaimData.class);

		RegularClaimData regClmData = Claim
				.findActiveOrPendingRegularClaimDataBySsn(objAssembly.getSsn());
		if (regClmData != null) {
			objAssembly.addComponent(regClmData);
		}

		return objAssembly;
	}

	public IObjectAssembly batchSsnVerification(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchSsnVerification");
		}

		ClaimApplicationClaimant clmAppClmntBO = ClaimApplicationClaimant
				.findByPrimaryKey(objAssembly.getPrimaryKeyAsLong());

		if (clmAppClmntBO == null) {
			return objAssembly;
		}

		// to send ssn validation request
		try {
			clmAppClmntBO = clmAppClmntBO.processSsnRequestBatch();
			clmAppClmntBO.saveOrUpdate();
			clmAppClmntBO.flush();
		} catch (BaseApplicationException e) {
			objAssembly.addBusinessError(e.getMessage());
		}

		return objAssembly;
	}

	public IObjectAssembly getIssueList(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getIssueList");
		}

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")
		RegularClaimData regClaimData = null;

		if (objAssembly.isComponentPresent(RegularClaimData.class)) {
			regClaimData = (RegularClaimData) objAssembly
					.getFirstComponent(RegularClaimData.class);
		}

		// to get monetary issues
		List listMonetaryIssue = new ArrayList();

		// to get Form525BData
		List tempList = Form525B.find525BDataByClmAppId(clmAppData.getPkId());

		if (tempList != null) {
			for (Iterator i = tempList.iterator(); i.hasNext();) {
				Form525BData form525BData = (Form525BData) i.next();

				GeneralBean bean = new GeneralBean();

				if (Form525BTypeEnum.Incorrect.getName().equals(
						form525BData.getForm525B_Type())) {
					bean.setParam1("Did Not Work");
				} else if (Form525BTypeEnum.Missing.getName().equals(
						form525BData.getForm525B_Type())) {
					bean.setParam1("Missing Employer");
				} else if (Form525BTypeEnum.Missing.getName().equals(
						form525BData.getForm525B_Type())) {
					bean.setParam1("Name Mismatch");
				} else {
					bean.setParam1("Unknown");
				}

				bean.setParam2(form525BData.getEmployerName());

				listMonetaryIssue.add(bean);
			}
		}

		// to get FcccRequestData
		FcccRequest fcccRequestBO = FcccRequest.findbySsnAndEffectiveDate(
				objAssembly.getSsn(), clmAppData.getClaimEffectiveDate());

		if (fcccRequestBO != null) {
			GeneralBean bean = new GeneralBean();
			bean.setParam1("FCCC Request");
			if ("F".equals(fcccRequestBO.getFcccRequestData().getProgramType())) {
				bean.setParam2("Federal");
			} else if ("X".equals(fcccRequestBO.getFcccRequestData()
					.getProgramType())) {
				bean.setParam2("Military");
			} else {
				bean.setParam2("Joint");
			}
			listMonetaryIssue.add(bean);
		}

		// to get Ib4RequestData
		tempList = Ib4Request.findBySsnAndEffectiveDate(objAssembly.getSsn(),
				clmAppData.getClaimEffectiveDate());

		if (tempList != null) {
			for (Iterator i = tempList.iterator(); i.hasNext();) {
				Ib4RequestData data = (Ib4RequestData) i.next();
				GeneralBean bean = new GeneralBean();
				bean.setParam1("IB-4 Request");
				bean.setParam2(data.getTransferringState());
				listMonetaryIssue.add(bean);
			}
		}

		// to add data to objAssembly
		if ((listMonetaryIssue != null) && (listMonetaryIssue.size() > 0)) {
			objAssembly.addData(TransientSessionConstants.MONETARY_LIST,
					listMonetaryIssue);
		}

		// to get Non-Monetary Issue list
		List listNonMonetaryIssue = new ArrayList();

		if (regClaimData != null) {
			List issueList = Issue
					.getListOfIssuesForClaimFromClaimsIntake(regClaimData
							.getClaimId());
			if ((issueList != null) && (issueList.size() > 0)) {
				for (Iterator i = issueList.iterator(); i.hasNext();) {
					IssueData data = (IssueData) i.next();

					GeneralBean bean = new GeneralBean();

					bean.setParam1((String) CacheUtility.getCachePropertyValue(
							"T_MST_ISSUE_CATEGORY", "key", data
									.getMstIssueMaster().getIssueCategory(),
							"description"));

					if ((data.getPotentialIssueData() != null)
							&& (data.getPotentialIssueData().getClaimAppEmp() != null)) {
						bean.setParam2(data.getPotentialIssueData()
								.getClaimAppEmp().getEmployerName());
					} else {
						bean.setParam2("N/A");
					}

					bean.setParam3(DateFormatUtility.format(data
							.getIssueStartDate()));

					listNonMonetaryIssue.add(bean);
				}
			}
		} else {
			Set potIssueSet = clmAppData.getPotentialIssueData();
			if ((potIssueSet != null) && (potIssueSet.size() > 0)) {
				for (Iterator i = potIssueSet.iterator(); i.hasNext();) {
					PotentialIssueData data = (PotentialIssueData) i.next();

					GeneralBean bean = new GeneralBean();

					if (QuestionnaireEnum.SELF_EMP.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.MEDICAL.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.REFUSAL.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.SCHOOL.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.CHILD_CARE.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.TRANSPORTATION.getName()
							.equals(data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.FAMILY_REASON.getName()
							.equals(data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());
					} else if (QuestionnaireEnum.DISCHARGE.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.DISCHARGE
								.getDescription());
					} else if (QuestionnaireEnum.PLANNING_SCHOOL.getName()
							.equals(data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.ABLE_AND_AVAILABLE
								.getDescription());

					} else if ("CIN_VOL_QUIT".equals(data
							.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.VOLUNTARY_LEAVING
								.getDescription());
					} else if (QuestionnaireEnum.PENSION.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.PENSION
								.getDescription());
					} else if (QuestionnaireEnum.PREGNANCY.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.FUTURE_ISSUE
								.getDescription());
					} else if (QuestionnaireEnum.DUA_CLAIM.getName().equals(
							data.getQuestionnaireType())) {
						bean.setParam1(IssueCategoryEnum.DISASTER_UA
								.getDescription());
					} else if (QuestionnaireEnum.TEMP_MASSLAYOFF.getName()
							.equals(data.getQuestionnaireType())) {
						bean.setParam1("Unknown");
					} else {
						bean.setParam1("Unknown");
					}

					if (data.getClaimAppEmp() != null) {
						bean.setParam2(data.getClaimAppEmp().getEmployerName());
					} else {
						bean.setParam2("N/A");
					}

					bean.setParam3("");

					listNonMonetaryIssue.add(bean);
				}
			}

		}

		// to add data to objAssembly
		if ((listNonMonetaryIssue != null) && (listNonMonetaryIssue.size() > 0)) {
			objAssembly.addData(TransientSessionConstants.NON_MONETARY_LIST,
					listNonMonetaryIssue);
		}

		return objAssembly;
	}

	public IObjectAssembly getBasePeriodWageList(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getBasePeriodWageList");
		}

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")
		List clmAppEmprList = objAssembly
				.getComponentList(ClaimApplicationEmployerData.class);

		List wageList = objAssembly.getComponentList(WageData.class);

		if ((clmAppEmprList == null) || (clmAppEmprList.isEmpty())) {
			return objAssembly;
		}

		Calendar cal = new GregorianCalendar();

		if (ClaimApplicationTypeEnum.AIC.getName().equals(
				clmAppData.getClaimAppType())) {
			// CQ R4UAT00015390 on 06/06/2011
			// changing this to nextqtr date
			// cal.setTime(clmAppData.getBpStartDateDisplay().getTime());
			cal.setTime(clmAppData.getBpStartDateDisplayOnScreen().getTime());
		} else {
			cal.setTime(clmAppData.getBpStartDate().getTime());
		}

		QtrBean qtrBean1 = new QtrBean(cal);

		cal.add(Calendar.MONTH, 3);
		QtrBean qtrBean2 = new QtrBean(cal);

		cal.add(Calendar.MONTH, 3);
		QtrBean qtrBean3 = new QtrBean(cal);

		cal.add(Calendar.MONTH, 3);
		QtrBean qtrBean4 = new QtrBean(cal);

		cal.add(Calendar.MONTH, 3);
		QtrBean qtrBean5 = new QtrBean(cal);

		List beanList = new ArrayList();

		for (Iterator i = clmAppEmprList.iterator(); i.hasNext();) {
			ClaimApplicationEmployerData data = (ClaimApplicationEmployerData) i
					.next();
			GeneralBean bean = new GeneralBean();
			if (data.getEmployerData() != null) {
				Employer employer = Employer.findByPrimaryKey(data
						.getEmployerData().getEmployerId());
				String displayName = employer.getEmployerData()
						.getDisplayName();
				if (displayName.length() > 30) {
					displayName = displayName.substring(0, 30);
				}
				bean.setParam1(displayName);

				bean.setParam2(employer.getEmployerData().getEmployerType());

				bean.setParam4(GlobalConstants.BIGDECIMAL_ZERO.toString());

				bean.setParam5(GlobalConstants.BIGDECIMAL_ZERO.toString());

				bean.setParam6(GlobalConstants.BIGDECIMAL_ZERO.toString());

				bean.setParam7(GlobalConstants.BIGDECIMAL_ZERO.toString());

				bean.setParam9(GlobalConstants.BIGDECIMAL_ZERO.toString());

				bean.setParam8(data.getEmployerData().getEmployerId()
						.toString());

				beanList.add(bean);
			}
		}
		if (wageList != null) {
			for (Iterator i = wageList.iterator(); i.hasNext();) {
				WageData wageData = (WageData) i.next();
				EmployeeData employeeData = wageData.getEmployeeData();
				EmployerData employerData = wageData.getEmployeeData()
						.getEmployerData();

				for (Iterator j = beanList.iterator(); j.hasNext();) {

					// to check if data are transferred
					if (EmployerTypeEnum.REG.getName().equals(
							employerData.getEmployerType())) {
						RegisteredEmployer regEmprBO = RegisteredEmployer
								.getTransferredWageData(employerData
										.getEmployerId());
						if (regEmprBO != null) {
							employerData = regEmprBO.getRegisteredEmployer();
						}
					}

					GeneralBean bean = (GeneralBean) j.next();
					if (bean.getParam8().equals(
							employerData.getEmployerId().toString())) {
						bean.setParam2(employerData.getEmployerType());

						String clmntName = employeeData.getFirstName() + " "
								+ employeeData.getLastName();
						if (clmntName.length() > 20) {
							clmntName = clmntName.substring(0, 20);
						}
						bean.setParam3(clmntName);

						QtrBean qtrBean = new QtrBean(wageData.getQuarter()
								.intValue(), wageData.getYear().intValue());

						if (qtrBean1.equals(qtrBean)) {
							bean.setParam4((new BigDecimal(bean.getParam4())
									.add(wageData.getWageAmount())).toString());
						} else if (qtrBean2.equals(qtrBean)) {
							bean.setParam5((new BigDecimal(bean.getParam5())
									.add(wageData.getWageAmount())).toString());
						} else if (qtrBean3.equals(qtrBean)) {
							bean.setParam6((new BigDecimal(bean.getParam6())
									.add(wageData.getWageAmount())).toString());
						} else if (qtrBean4.equals(qtrBean)) {
							bean.setParam7((new BigDecimal(bean.getParam7())
									.add(wageData.getWageAmount())).toString());
						} else if (qtrBean5.equals(qtrBean)) {
							bean.setParam9((new BigDecimal(bean.getParam9())
									.add(wageData.getWageAmount())).toString());
						}
					}
				}
			}

			objAssembly.removeBean(GeneralBean.class);
			if ((beanList != null) && (beanList.size() > 0)) {
				objAssembly.addBeanList(beanList);
			}
		}

		return objAssembly;
	}

	public IObjectAssembly saveInterveningEmploymentInfo(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveInterveningEmploymentInfo");
		}

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17",
		// mddrNo="", impactPoint="End")

		/*
		 * Payment Defect_41 start If coming from activate claim=>interveing
		 * employment screen claimapp data will not be null but the object will
		 * not have data in it
		 */
		// boolean fromActivateClaim = false;
		if (clmAppData != null && clmAppData.getSsn() == null) {
			WeeklyCertificationValidationDataBean weekCertValidationBean = (WeeklyCertificationValidationDataBean) objAssembly
					.getFirstBean(WeeklyCertificationValidationDataBean.class);

			clmAppData = (ClaimApplicationDataBridge) weekCertValidationBean
					.getClaimData().getClaimApplicationData();
			// fromActivateClaim = true;
		}

		ClaimApplication clmAppFromDB = ClaimApplication
				.findClaimAppByPkId(clmAppData.getPkId());
		if (clmAppFromDB != null) {
			IClaimApplicationDAO clmAppDAO = DAOFactory.instance
					.getClaimApplicationDAO();
			clmAppDAO.evict(clmAppFromDB.getClaimApplicationData());
			/*
			 * Payment defect_41 commented as not sure why its throwing error on
			 * pending and completed status factually pending status is when a
			 * claim is not established so there is no active claim and
			 * cpmpleted status is when claim is established. One can file
			 * certification on active claim and without active claim one can't
			 * reach to this flow. uncommented always the claim status will be
			 * initial claim as new claim application is being build
			 */

			if (ApplicationStatusEnum.COMPLETED.getName().equals(
					clmAppFromDB.getClaimApplicationData().getClaimAppStatus())
					|| ApplicationStatusEnum.PENDING.getName().equals(
							clmAppFromDB.getClaimApplicationData()
									.getClaimAppStatus())) {

				throw new BaseApplicationException(
						"error.access.cin.claim.reopen.claimapplication");

			}
		}
		this.saveOrUpdateClaimApp(objAssembly);

		if (ClaimApplicationTypeEnum.ROC.getName().equals(
				clmAppData.getClaimAppType())) {
			Claimant claimantBO = Claimant.fetchClaimantBySsn(clmAppData
					.getSsn());
			ClaimantData clmntData = claimantBO.getClaimantData();

			/*
			 * CIF_01915 as per missouri it should be reopened on all type of
			 * claim types ClaimData claimData =
			 * Claim.findLatestNonCancelledRegClaimBySsn(clmntData.getSsn());
			 */
			WeeklyCertificationValidationDataBean weekCertValidationBean = (WeeklyCertificationValidationDataBean) objAssembly
					.getFirstBean(WeeklyCertificationValidationDataBean.class);
			Claim claimBO = Claim.getClaimBoByClaimData(weekCertValidationBean
					.getClaimData());
			claimBO.processReopenClaim(clmntData, clmAppData);
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with the list of
	 * ClaimApplicationEmployerData as input and and saves, or updates these
	 * objects.
	 * 
	 * @param objAssembly
	 *            with the list of ClaimApplicationEmployerData
	 * 
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateEvidentClaimAppEmprList(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateEvidentClaimAppEmprList");
		}

		List list = (ArrayList) objAssembly
				.getData(TransientSessionConstants.CLMEMP_EVIDENT_8X);

		if (list == null) {
			return objAssembly;
		}

		for (Iterator i = list.iterator(); i.hasNext();) {
			ClaimApplicationEmployerData clmAppEmpData = (ClaimApplicationEmployerData) i
					.next();

			ClaimApplicationEmployer claimAppEmpBO = new ClaimApplicationEmployer(
					clmAppEmpData);

			claimAppEmpBO.saveOrUpdate();
		}

		return objAssembly;
	}

	/**
	 * This method takes ObjectAssembly with the list of
	 * ClaimApplicationEmployerData as input and and saves, or updates these
	 * objects.
	 * 
	 * @param objAssembly
	 *            with the list of ClaimApplicationEmployerData
	 * 
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateNotEvidentClaimAppEmprList(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateNotEvidentClaimAppEmprList");
		}

		List list = (ArrayList) objAssembly
				.getData(TransientSessionConstants.CLMEMP_8X);

		if (list == null) {
			return objAssembly;
		}

		for (Iterator i = list.iterator(); i.hasNext();) {
			ClaimApplicationEmployerData clmAppEmpData = (ClaimApplicationEmployerData) i
					.next();

			ClaimApplicationEmployer claimAppEmpBO = new ClaimApplicationEmployer(
					clmAppEmpData);

			claimAppEmpBO.saveOrUpdate();
		}

		return objAssembly;
	}

	// This method will fetch some more information about the MS employer
	// and its predicessor
	public IObjectAssembly getMsEmployerDetails(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getMsEmployerDetails");
		}

		Long msEmployerId = objAssembly.getPrimaryKeyAsLong();
		RegisteredEmployer msEmployerBO = RegisteredEmployer
				.fetchRegEmprDataWithEmprAccountDataByPkId(msEmployerId);
		MsEmployerInfoBean newBean = new MsEmployerInfoBean();
		newBean.setEmployerEan(msEmployerBO.getRegisteredEmployer().getEan());
		if (msEmployerBO.getRegisteredEmployer().getEmployerAccountData()
				.getEntityName() != null) {
			newBean.setEntityName(msEmployerBO.getRegisteredEmployer()
					.getEmployerAccountData().getEntityName());
		} else {
			newBean.setEntityName(GlobalConstants.NA);
		}
		if (msEmployerBO.getRegisteredEmployer().getTradeName() != null) {
			newBean.setTradeName(msEmployerBO.getRegisteredEmployer()
					.getTradeName());
		} else {
			newBean.setTradeName(GlobalConstants.NA);
		}
		List employerTransferDetailsList = RegisteredEmployer
				.getEmployerTransferDetails(msEmployerId);
		if (employerTransferDetailsList != null
				&& employerTransferDetailsList.size() > 0) {
			List predicessorDetailsList = new ArrayList();
			for (int i = 0; i < employerTransferDetailsList.size(); i++) {
				EmployerTransferData employerTransferData = (EmployerTransferData) employerTransferDetailsList
						.get(i);
				Long msEmployerData = employerTransferData
						.getFromEmployerData();
				RegisteredEmployer msPredicessorEmployerBO = RegisteredEmployer
						.fetchRegEmprDataWithEmprAccountDataByPkId(msEmployerData);
				MsEmployerPredicessorBean bean = new MsEmployerPredicessorBean();
				bean.setPredicessorEmployerEan(msPredicessorEmployerBO
						.getRegisteredEmployer().getEan());
				if (msPredicessorEmployerBO.getRegisteredEmployer()
						.getTradeName() != null) {
					bean.setPredicessorEmployerTradeName(msPredicessorEmployerBO
							.getRegisteredEmployer().getTradeName());
				} else {
					bean.setPredicessorEmployerTradeName(GlobalConstants.NA);
				}
				if (msPredicessorEmployerBO.getRegisteredEmployer()
						.getEmployerAccountData().getEntityName() != null) {
					bean.setPredicessorEmployerEntityName(msPredicessorEmployerBO
							.getRegisteredEmployer().getEmployerAccountData()
							.getEntityName());
				} else {
					bean.setPredicessorEmployerEntityName(GlobalConstants.NA);
				}
				predicessorDetailsList.add(bean);
				newBean.setPredicessorYesFlag(ViewConstants.YES);// predicessor
																	// is
																	// there,hence
																	// flag is
																	// having 1
				objAssembly.addData("PREDICESSORDETAILSLIST",
						predicessorDetailsList);
			}
		} else {
			newBean.setPredicessorYesFlag(ViewConstants.NO);// this means no
															// predicessor
															// record is
															// there,so it is 0
		}
		objAssembly.addBean(newBean, true);
		return objAssembly;
	}

	public IObjectAssembly get512Wages(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method get512Wages");
		}
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		List ui512List = UI512AResponse.populate512ABeanForSixMonths(clmAppData
				.getSsn());
		if (ui512List != null) {
			objAssembly.addBeanList(ui512List, true);
		}
		return objAssembly;
	}

	/**
	 * This method is invoked from the ImportNBYBatch. It takes the SSN as input
	 * in the objectAssembly and invokes the service methods to create new
	 * ClaimApplicationData and ClaimApplicationClaimantData as well as
	 * ClaimApplicationEmployerData. Finally, it invokes the method
	 * processInitialClaim with flag as NBY in order to establish Claim.
	 * 
	 * @param objAssembly
	 * 
	 * @return objAssembly
	 * 
	 * @throws BaseApplicationException
	 */
	public IObjectAssembly batchEstablishNBYClaims(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchEstablishNBYClaims");
		}
		LOGGER.batchRecordProcess("Processing record for SSN as "
				+ objAssembly.getSsn());

		// R4UAT00003638 Fix.
		//
		objAssembly = checkFederalAndMonetaryIssuesForNBY(objAssembly);

		if (objAssembly.hasBusinessError()) {
			LOGGER.busError("Claim has not been established for SSN: "
					+ objAssembly.getSsn() + ". due to "
					+ objAssembly.getFirstBusinessErrorInfo().getErrorCode());
			objAssembly.removeBean(GeneralBean.class);
			return objAssembly;
		}

		// to fetch or create ClaimApplicationData object and
		// to save it along with ClaimApplicationClaimantData object
		LOGGER.info("System is creating ClaimApplicationData for SSN: "
				+ objAssembly.getSsn());

		objAssembly.addData("NBY_INDICATOR", "1");

		objAssembly = this.getOrCreateClaimApp(objAssembly);

		if (objAssembly.hasBusinessError()) {
			LOGGER.info("Business error while creating Claim Application.");
			LOGGER.info("Claim has not been established for SSN: "
					+ objAssembly.getSsn() + ".");
			LOGGER.busError(objAssembly.getFirstBusinessErrorInfo()
					.getErrorCode());
			objAssembly.removeBean(GeneralBean.class);
			return objAssembly;
		}

		// to set values in Claim Application for NBY Claim
		// in case that previous claim does not exist, it will throw business
		// error
		LOGGER.info("System is finding previous ClaimData for SSN: "
				+ objAssembly.getSsn());

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimAppDataObj = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class, true);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		try {
			setValuesForNBY(claimAppDataObj, objAssembly.getSsn());
		} catch (BaseApplicationException bE) {
			objAssembly.addBusinessError(bE.getMessage());
		}

		if (objAssembly.hasBusinessError()) {
			LOGGER.info("Business error while trying to find previous Claim.");
			LOGGER.info("Claim has not been established for SSN: "
					+ objAssembly.getSsn() + ".");
			LOGGER.busError(objAssembly.getFirstBusinessErrorInfo()
					.getErrorCode());
			return objAssembly;
		}

		// to process Employer Data and create Claim Application Employer Data
		// objects
		LOGGER.info("System is processing Employer Data for SSN: "
				+ objAssembly.getSsn());
		// CIF_INT_00309 starts
		/*
		 * this will get the lexis nexis status from potential data and set the
		 * corresponding data in claim application table
		 */
		PotentialNBYClaimData potentialData = (PotentialNBYClaimData) DAOFactory.instance
				.getClaimApplicationDAO().get(PotentialNBYClaimData.class,
						(Long) objAssembly.getData("potentialNBYClaimId"));
		claimAppDataObj.setLnStatus(potentialData.getLnStatus());
		claimAppDataObj.setLnFailReason(potentialData.getLnFailReason());
		claimAppDataObj.setLnTransactionId(potentialData.getLnTransactionId());
		// CIF_INT_00309 ends
		objAssembly = this.getBaseEmployers(objAssembly);

		List employerList = objAssembly
				.getComponentList(ClaimApplicationEmployerData.class);

		if ((employerList != null) && (!employerList.isEmpty())) {
			LOGGER.info("There are " + employerList.size()
					+ " employers for SSN = " + objAssembly.getSsn());
			for (Iterator i = employerList.iterator(); i.hasNext();) {
				ClaimApplicationEmployerData emprdata = (ClaimApplicationEmployerData) i
						.next();
				emprdata.setWorkFlag(GlobalConstants.DB_ANSWER_YES);
				emprdata.setReasonSeparation("LWLO"); // Hardcoded. As per
														// Ashish/Darko - It
														// does not have an Enum
				emprdata.setPensionFlag(GlobalConstants.DB_ANSWER_NO);
				emprdata.setClaimApplicationData(claimAppDataObj);

				// to save/update the CLaim APplication Employer Data
				ClaimApplicationEmployer claimAppEmp = new ClaimApplicationEmployer(
						emprdata);
				claimAppEmp.saveOrUpdate();
			}
			objAssembly.addComponentList(employerList, true);
		} else {
			LOGGER.info("There are no employers for SSN = "
					+ objAssembly.getSsn());
		}

		// to establish NBY - Transitional Claim
		LOGGER.info("Establishing NBY Claim for SSN : " + objAssembly.getSsn());
		objAssembly = this.processInitialClaim(objAssembly);

		if (objAssembly.hasBusinessError()) {
			LOGGER.info("Business error while establishing Claim.");
			LOGGER.info("Claim has not been established for SSN: "
					+ objAssembly.getSsn() + ".");
			LOGGER.busError(objAssembly.getFirstBusinessErrorInfo()
					.getErrorCode());
			return objAssembly;
		}

		LOGGER.info("NBY Claim Successfully established for SSN : "
				+ objAssembly.getSsn());
		// CIF_02651 mississippi related correspondence and issue not required
		// in MISSOURI
		// if the latest week processed is not regular, then create future issue
		/*
		 * if(objAssembly.getFirstComponent(ClaimData.class)!=null){ ClaimData
		 * claimData =
		 * (ClaimData)objAssembly.getFirstComponent(ClaimData.class);
		 * WeeklyCertData weeklyCertData =
		 * WeeklyCert.getLatestCertifiedWeekForClaimant
		 * (claimData.getClaimantData().getPkId()); ProcessedWeeklyCertBO
		 * processedWeeklyCertBO =
		 * ProcessedWeeklyCertBO.findLatestByWeeklycertificationId
		 * (weeklyCertData.getWeeklyCertId());
		 * if(!EntitlementTypeEnum.REGULAR.getName
		 * ().equals(processedWeeklyCertBO
		 * .getProcessedWeeklyCertData().getClaimData().getEntitlementType())){
		 * IssueBean issueBean = new IssueBean();
		 * issueBean.setClaimantSsn(claimData.getClaimantData().getSsn());
		 * issueBean.setDateIssueDetected(new Date());
		 * issueBean.setIssueStartDate(claimData.getEffectiveDate());
		 * issueBean.setIssueDescription
		 * (IssueCategoryEnum.FUTURE_ISSUE.getName());
		 * //issueBean.setIssueDetails(IssueSubCategoryEnum.SWET.getName());
		 * Issue.createIssue(issueBean, false); } }
		 */

		// checkAndInsertMailClaimMessageForNBYstop(objAssembly);
		objAssembly.removeBean(GeneralBean.class);
		objAssembly.addData("SID_PROCESS_NAME",
				SIDProcessNameEnum.NBYC.getName());
		objAssembly = sendSIDRequest(objAssembly);
		return objAssembly;
	}

	/**
	 * 
	 * @param objAssembly
	 */
	private void checkAndInsertMailClaimMessageForNBYstop(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkAndInsertMailClaimMessageForNBYstop");
		}
		ClaimData claimData = (ClaimData) objAssembly.getFirstComponent(
				ClaimData.class, true);
		if (claimData == null
				|| !(ClaimTypeEnum.NEW_BENEFIT_YEAR.getName().equals(
						claimData.getClaimType()) || ClaimTypeEnum.AUTOMATED_INITIAL_CLAIM
						.getName().equals(claimData.getClaimType()))) {
			return; // Not an NBY claim.
		}

		List issue715StopList = Issue.getIssueFor715ForTeucClaims(claimData
				.getClaimId());
		if (issue715StopList == null || issue715StopList.isEmpty()) {
			return; // If NO 715 stop, return
		}

		Date bye = claimData.getByeDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bye);
		String param1 = DateFormatUtility.format(DateUtility.addBusinessDays(
				cal, 1, HolidayCache.instance.getHolidayList()));

		// Generate Mail Claim message
		ProcessedWeeklyCertBO.generateWCcorrespondenceRecord(
				PaymentConstants.CORR_NBY_STOP, null,
				claimData.getClaimantData());
	}

	/**
	 * Sets the BpStartDate, BpEndDate and ClaimEffectiveDate values for NBY
	 * 
	 * @param claimAppDataObj
	 */
	private void setValuesForNBY(ClaimApplicationData claimAppDataObj,
			String ssn) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method setValuesForNBY");
		}

		RegularClaimData previousRegularClaimData = Claim
				.findLastActiveRegularClaimDataBySsn(ssn);

		if ((previousRegularClaimData == null)
				|| (previousRegularClaimData.getByeDate() == null)) {
			throw new BaseApplicationException(
					"error.access.findlastactiveregularclaim.noclaimfound");
		}

		Claimant claimantBO = Claimant.fetchClaimantBySsn(ssn);
		ClaimantData claimantData = null;
		if (claimantBO != null) {
			claimantData = claimantBO.getClaimantData();
		}

		claimAppDataObj.setFlowType(ClaimAppFlowTypeEnum.NBY.getName());

		Calendar bYEcal = Calendar.getInstance();
		bYEcal.setTime(previousRegularClaimData.getByeDate());
		// Should be always Next sunday
		Calendar nextSundaytoBYE = DateUtility.getNextSunday(bYEcal);
		// check for any existing regular claim greater than the new effective
		// date
		RegularClaimData regularClaimData = RegularClaim
				.findRegularClaimAfterEffectiveDate(ssn,
						nextSundaytoBYE.getTime());
		// if(DateUtility.compareDates(nextSundaytoBYE.getTime(),new
		// Date())>=0){
		if (regularClaimData != null) {
			throw new BaseApplicationException(
					"error.access.regularclaim.alreadyexists.neweffectivedate");
		}

		// R4- UAT 0360 fix. if the future date is being set as effective date
		// of the claim, throw business error.
		Calendar today = Calendar.getInstance();
		if (nextSundaytoBYE.after(today)) {
			throw new BaseApplicationException(
					"error.access.effectivedate.infuture");
		}

		claimAppDataObj.setClaimEffectiveDate(nextSundaytoBYE.getTime());

		Calendar[] qDate = RegularClaim.determineBasePeriod(nextSundaytoBYE);
		claimAppDataObj.setBpStartDate(qDate[0]);
		claimAppDataObj.setBpEndDate(qDate[1]);
		// R4UAT00002940
		RegularClaimData latestRegularClaimData = RegularClaim
				.getLatestRegularClaimWithEffDateBeforeGivenDate(
						claimantData.getSsn(),
						claimAppDataObj.getClaimEffectiveDate());
		if (latestRegularClaimData != null) {
			RegularClaim regularClaimBO = new RegularClaim(
					latestRegularClaimData);
			if (regularClaimBO.isClaimEligibleForPayment()) {
				claimAppDataObj.setClaimType(ClaimTypeEnum.NEW_BENEFIT_YEAR
						.getName());
			} else {
				claimAppDataObj
						.setClaimType(ClaimTypeEnum.AUTOMATED_INITIAL_CLAIM
								.getName());
			}
		}
		// claimAppDataObj.setClaimType(ClaimTypeEnum.NEW_BENEFIT_YEAR.getName());//goes
		// into T_CLAIM and T_CLAIM_APPLICATION tables
		if (claimantData != null) {
			claimAppDataObj.getClaimApplicationClaimantData().setLocalOffice(
					claimantData.getClaimantProfileData().getLocalOffice());
		}

		// Get the commuter flag (Live other state flag) from the latest claim
		// application completed
		ClaimApplication latestClmAppBOFromDB = ClaimApplication
				.getLatestClaimApplicationBySSN(ssn);
		if (latestClmAppBOFromDB != null) {
			claimAppDataObj.setLiveOtherStateFlag(latestClmAppBOFromDB
					.getClaimApplicationData().getLiveOtherStateFlag());
		}

		ClaimApplication clmAppBO = new ClaimApplication(claimAppDataObj);
		clmAppBO.saveOrUpdate();
		clmAppBO.flush();

		return;
	}

	public IObjectAssembly loadEnterSsnForMassLayoffs(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method loadEnterSsnForMassLayoffs");
		}

		Long pkId = objAssembly.getPrimaryKeyAsLong(MassLayoffData.class);

		MassLayoff bo = MassLayoff.findByPrimaryKey(pkId);

		if (bo != null) {
			objAssembly.addComponent(bo.getMassLayoffData());
		}
		return objAssembly;
	}

	public IObjectAssembly WorkItemForIssueAssignedProcess(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method WorkItemForIssueAssignedProcess");
		}

		String workItemFlagTobeAssigned = (String) objAssembly
				.getData("WORKITEMFLAG");

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		if (GlobalConstants.DB_ANSWER_YES.equals(workItemFlagTobeAssigned)) {// take
																				// the
																				// assignibility
			// GenericWorkflowSearchBean bean =
			// (GenericWorkflowSearchBean)objAssembly.fetchORCreateBean(GenericWorkflowSearchBean.class);
			// bean.setGlobalContainerMemberLike("ssneanfein",
			// clmAppClmntData.getSsn());
			// bean.setGlobalContainerMember("type", "ssn");
			// bean.setReceivedTimeBetween(new Date(),
			// DateUtility.getNextDay(new Date()));
			List issueWorkItemsList = (List) objAssembly
					.getData("ISSUERELATEDLIST");
			for (int i = 0; i < issueWorkItemsList.size(); i++) {
				WorkflowItemBean workFlowItemBean = (WorkflowItemBean) issueWorkItemsList
						.get(i);
				RegularClaim.reassignWorkItems(
						workFlowItemBean.getPersistentOid(),
						clmAppClmntData.getUpdatedBy());
			}
		}
		return objAssembly;
	}

	public IObjectAssembly checkForInvestigatorRoles(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkForInvestigatorRoles");
		}
		String monetaryEligibleFlag = (String) objAssembly
				.getData("MONETARYELIGIBLE");
		boolean isInvestigator = false;
		if (GlobalConstants.DB_ANSWER_YES.equals(monetaryEligibleFlag)) {// if
																			// the
																			// claim
																			// is
																			// monetarily
																			// eligible

			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="Start")
			ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="End")

			// getting and setting the ssn and assigned date to find the list of
			// work items relarted to issue
			GenericWorkflowSearchBean bean = (GenericWorkflowSearchBean) objAssembly
					.fetchORCreateBean(GenericWorkflowSearchBean.class);
			bean.setGlobalContainerMemberLike("ssneanfein",
					clmAppClmntData.getSsn());
			bean.setGlobalContainerMember("type", "ssn");
			bean.setReceivedTimeBetween(new Date(),
					DateUtility.getNextDay(new Date()));
			List issueRelatedWorkItemsList = RegularClaim
					.issueRelatedWorkItemsList(bean);// list containing issue
														// related work items
			if (issueRelatedWorkItemsList != null
					&& issueRelatedWorkItemsList.size() > 0) {
				if (GlobalConstants.DB_ANSWER_YES.equals(objAssembly
						.getData("MONETARYELIGIBLE"))) {
					List funcList = Arrays.asList(super.getUserContext()
							.getFunctions());
					if (funcList.contains(FunctionsEnum.NONSEP_INVESTIGATION
							.getName())
							|| funcList
									.contains(FunctionsEnum.SEP_INVESTIGATION
											.getName())) {
						isInvestigator = true;
					}
					if (isInvestigator) {
						objAssembly.addData("askOwnership", Boolean.TRUE);
						objAssembly.addData("ISSUERELATEDLIST",
								issueRelatedWorkItemsList);
					}
				}
			}
		}
		if (isInvestigator == false) {
			objAssembly.addData("askOwnership", Boolean.FALSE);
		}
		return objAssembly;
	}

	public IObjectAssembly findPreviousClaimDetails(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findPreviousClaimDetails");
		}
		List activeClaimList = Claim
				.fetchAllActiveRegularClaimsBySsn(objAssembly.getSsn());
		if (activeClaimList != null && !activeClaimList.isEmpty()) {
			RegularClaimData claimData = (RegularClaimData) activeClaimList
					.get(0);
			Claim claimBO = new RegularClaim(claimData);
			// Check whether any monetary issue pending in the prvious claim
			objAssembly = checkPendingMonetaryIssue(objAssembly);
			List messageList = (List) objAssembly
					.getData(TransientSessionConstants.MESSAGE_LIST);
			if (messageList != null && !messageList.isEmpty()) {
				objAssembly
						.addBusinessError("error.acess.claimant.monissue.pending.previousclaim");
				return objAssembly;
			}

			// If existing claim is IW display message to file next quarter.
			if (YesNoTypeEnum.NumericNo.getName().equals(
					claimData.getMonEligibleFlag())
					&& !claimBO.isByeExpired(new Date())) {
				objAssembly
						.addBusinessError("error.access.claimant.already.filed.iw.thisquarter");
				Date byeDate = Claim.getByeCweDate(claimData.getByeDate());
				objAssembly.addData("claimByeDatePreviousClaim",
						DateFormatUtility.format(byeDate));
				return objAssembly;
			}

			// if bye is still valid check whether the balance is exhausted or
			// not
			if (!claimBO.isByeExpired(new Date())) {
				if (GlobalConstants.BIGDECIMAL_ZERO.compareTo(claimData
						.getClaimInformationData().getMbaBalance()) == 0) {
					Date byeDate = Claim.getByeCweDate(claimData.getByeDate());
					Date bpStartDate = claimData.getBasePeriodStartDate();
					Date bpEndDate = claimData.getBasePeriodEndDate();
					String basePeriod = DateFormatUtility.format(bpStartDate,
							GlobalConstants.DATE_FORMAT)
							+ " - "
							+ DateFormatUtility.format(bpEndDate,
									GlobalConstants.DATE_FORMAT);
					objAssembly.addData("BASEPERIOD", basePeriod);
					objAssembly.addData("claimByeDatePreviousClaim",
							DateFormatUtility.format(byeDate));
					objAssembly
							.addBusinessError("error.access.claimant.exhausted.benefits.previous.claim");
					return objAssembly;
				}
			}
		}
		return objAssembly;
	}

	/**
	 * 
	 */
	public IObjectAssembly findInitiatedClaimApp(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findInitiatedClaimApp");
		}

		try {

			Date wkStartDate = Claim.determineClaimEffectiveDate().getTime();
			if (UserTypeEnum.MDES.getName().equals(
					objAssembly.getUserContext().getUserType())) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(wkStartDate);
				cal.add(Calendar.DATE, -7);
				wkStartDate = cal.getTime();
			}

			ClaimApplication claimApp = ClaimApplication
					.findInitiatedClaimAppBySsn(objAssembly.getSsn(), null,
							wkStartDate);
			if (claimApp != null) {
				objAssembly.addComponent(claimApp.getClaimApplicationData(),
						true);
				if (claimApp.getClaimApplicationData().getDuaApplicationData() != null) {
					objAssembly.addComponent(claimApp.getClaimApplicationData()
							.getDuaApplicationData(), true);
				}
				// Added. Since the original 'getOrCreateClaimApp' method adds
				// claimant data also to objAssembly which this method was not
				// doing
				if (claimApp.getClaimApplicationData()
						.getClaimApplicationClaimantData() != null) {
					objAssembly.addComponent(claimApp.getClaimApplicationData()
							.getClaimApplicationClaimantData(), true);
				}
			}
		} catch (BaseApplicationException BEx) {
			objAssembly.addBusinessError(BEx.getMessage());
		}

		return objAssembly;
	}

	public String getRandomPassword() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRandomPassword");
		}

		// CQ 22869

		String currentPassword = DynamicBizConstant.getValueAsString(
				"INS_PASSWORD", new Date());
		StringBuilder newPassword = new StringBuilder();

		// One upper case letter [A-Z]
		// One lower case letter [a-z]
		// Ten random case letters
		// One number
		// One special char

		int index = 0;
		int totalPasswordLength = 14;
		while (currentPassword.length() < totalPasswordLength) {
			currentPassword += ' ';
		}

		newPassword.append(getRandomChar(false, true,
				currentPassword.charAt(index)));
		index++;
		newPassword.append(getRandomChar(false, false,
				currentPassword.charAt(index)));
		index++;

		for (; index < totalPasswordLength - 2; index++) {
			newPassword.append(getRandomChar(true, false,
					currentPassword.charAt(index)));
		}

		newPassword.append(getRandomDigit(currentPassword.charAt(index)));
		index++;
		newPassword.append(getRandomSymbol(currentPassword.charAt(index)));

		return newPassword.toString();
	}

	public IObjectAssembly batchINSChangePassword(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchINSChangePassword");
		}
		LOGGER.batchRecordProcess("changing Ins password");
		String currentINSPassword = DynamicBizConstant.getValueAsString(
				"INS_PASSWORD", new Date());
		StringBuilder newPassword = new StringBuilder();

		// One upper case letter [A-Z]
		// One lower case letter [a-z]
		// Ten random case letters
		// One number
		// One special char

		int index = 0;
		int totalPasswordLength = 14;
		while (currentINSPassword.length() < totalPasswordLength) {
			currentINSPassword += ' ';
		}

		newPassword.append(getRandomChar(false, true,
				currentINSPassword.charAt(index)));
		index++;
		newPassword.append(getRandomChar(false, false,
				currentINSPassword.charAt(index)));
		index++;

		for (; index < totalPasswordLength - 2; index++) {
			newPassword.append(getRandomChar(true, false,
					currentINSPassword.charAt(index)));
		}

		newPassword.append(getRandomDigit(currentINSPassword.charAt(index)));
		index++;
		newPassword.append(getRandomSymbol(currentINSPassword.charAt(index)));
		index++;

		IIns ins = InterfaceFactory.instance.getIns();
		LOGGER.debug("Changing INS password from " + currentINSPassword
				+ " to " + newPassword.toString());
		ins.changePassword(newPassword.toString());

		// Update new password in Dynamic biz constant table
		IInsDAO insDAO = DAOFactory.instance.getInsDAO();
		insDAO.updateInsNewPassword(newPassword.toString());
		return objAssembly;
	}

	/**
	 * Returns a random character from a-z or A-Z, depending on the first two
	 * parameters. Return char will be different than parameter mustBeDifferent.
	 */
	private char getRandomChar(boolean randomCase, boolean upperCase,
			char mustBeDifferent) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRandomChar");
		}
		Random rand = new Random();
		char returnChar;

		if (randomCase) {
			upperCase = rand.nextBoolean();
		}

		do {
			returnChar = (char) rand.nextInt(26);
			if (upperCase) {
				returnChar += 'A';
			} else {
				returnChar += 'a';
			}
		} while (returnChar == mustBeDifferent);

		return returnChar;
	}

	/**
	 * Returns a random digit, 0-9, that is different from parameter
	 * mustBeDifferent
	 */
	private char getRandomDigit(char mustBeDifferent) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRandomDigit");
		}
		Random rand = new Random();
		char returnChar;

		do {
			returnChar = (char) rand.nextInt(10);
			returnChar += '0';
		} while (returnChar == mustBeDifferent);

		return returnChar;
	}

	/**
	 * Returns a random symbol that is different from the parameter
	 * mustBeDifferent
	 */
	private char getRandomSymbol(char mustBeDifferent) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRandomSymbol");
		}
		Random rand = new Random();
		char returnChar;

		char[] specialChars = { '!', '@', '$', '%', '*', '(', ')', '<', '>',
				'?', ':', ';', '{', '}', '+', '-', '~' };

		do {
			returnChar = specialChars[rand.nextInt(specialChars.length)];
		} while (returnChar == mustBeDifferent);

		return returnChar;
	}

	public IObjectAssembly checkPendingMonetaryIssue(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkPendingMonetaryIssue");
		}
		ClaimData claimData = Claim
				.findLatestNonCancelledRegClaimBySsn(objAssembly.getSsn());
		if (claimData == null) {
			return objAssembly;
		}

		// If previous claim was not IW return
		if (BigDecimal.valueOf(0).compareTo(claimData.getWba()) != 0) {
			return objAssembly;
		}

		Calendar byeCal = Calendar.getInstance();
		byeCal.setTime(claimData.getByeDate());
		int[] byeQtrYr = DateUtility.determineQuarterYear(byeCal);

		Calendar currentEffDate = Claim.determineClaimEffectiveDate();

		int[] currentEffDateQtrYr = DateUtility
				.determineQuarterYear(currentEffDate);

		int[] priorQtrYr = DateUtility.getPriorQuarterAndYear(
				currentEffDateQtrYr[0], currentEffDateQtrYr[1]);

		// If previous claim BYE is not in prior quater return
		if (byeQtrYr[0] != priorQtrYr[0] || byeQtrYr[1] != priorQtrYr[1]) {
			Calendar[] calArray = DateUtility.getQuarterStartAndEndDate(
					currentEffDateQtrYr[0], currentEffDateQtrYr[1]);
			Calendar cal = calArray[0];
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			byeCal.set(Calendar.HOUR_OF_DAY, 0);
			byeCal.set(Calendar.MINUTE, 0);
			byeCal.set(Calendar.SECOND, 0);
			byeCal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (byeCal.getTime().compareTo(cal.getTime()) != 0) {
				return objAssembly;
			}
		}

		objAssembly.addData("climantName", claimData.getClaimantData()
				.getClaimantFirstLastName());

		List msgList = new ArrayList();

		List cwcList = CWCOption.findUnprocessedCWCOptionByClaimID(claimData
				.getClaimId());
		// // test
		// cwcList = new ArrayList();
		// CWCOptionData t1 = new CWCOptionData();
		// t1.setState("AL");
		// cwcList.add(t1);
		// CWCOptionData t2 = new CWCOptionData();
		// t2.setState("NY");
		// cwcList.add(t2);
		// // test
		if ((cwcList != null) && (!cwcList.isEmpty())) {
			for (Iterator i = cwcList.iterator(); i.hasNext();) {
				CWCOptionData data = (CWCOptionData) i.next();
				msgList.add("CWC Option "
						+ (String) CacheUtility.getCachePropertyValue(
								"T_MST_STATE", "key", data.getState(),
								"description") + ".");
			}
		}

		// to check IB-4 Response
		List ib4RequestList = Ib4Request.findBySsnAndEffectiveDate(
				objAssembly.getSsn(), claimData.getEffectiveDate());
		if ((ib4RequestList != null) && (!ib4RequestList.isEmpty())) {
			for (Iterator i = ib4RequestList.iterator(); i.hasNext();) {
				Ib4RequestData reqData = (Ib4RequestData) i.next();
				Ib4Response resBO = Ib4Response
						.findIncomingBySsnEffectiveDateAndTransferringState(
								reqData.getSsn(), reqData.getEffectiveDate(),
								reqData.getTransferringState());
				if ((resBO == null)
						|| (GlobalConstants.DB_ANSWER_NO.equals(resBO
								.getIb4ResponseData().getProcessedFlag()))) {
					msgList.add("IB-4 Request "
							+ (String) CacheUtility.getCachePropertyValue(
									"T_MST_STATE", "key",
									reqData.getTransferringState(),
									"description") + ".");
				}
			}
		}

		// to check FCCC Request
		List fcccRequestList = FcccRequest
				.findListOfFcccRequestbySsnAndEffectiveDate(
						objAssembly.getSsn(), claimData.getEffectiveDate());
		if ((fcccRequestList != null) && (!fcccRequestList.isEmpty())) {
			for (Iterator i = fcccRequestList.iterator(); i.hasNext();) {
				FCCCRequestData reqData = (FCCCRequestData) i.next();
				FcccResponse resBO = FcccResponse
						.findbySsnAndEffectiveDateAndProgramType(
								reqData.getSsn(), reqData.getEffectiveDate(),
								reqData.getProgramType());
				if ((resBO == null)
						|| (GlobalConstants.DB_ANSWER_NO.equals(resBO
								.getFcccResponseData().getProcessedFlag()))) {
					if ("X".equals(reqData.getProgramType())) {
						msgList.add("Military employer (FCCC request).");
					} else if ("F".equals(reqData.getProgramType())) {
						msgList.add("Federal employer (FCCC request).");
					}
				}
			}
		}

		// to check Form525BData with claim application employer data
		if (claimData.getClaimApplicationData() != null) {
			List form525BList = Form525B.find525BDataByClmAppId(claimData
					.getClaimApplicationData().getPkId());
			if ((form525BList != null) && (!form525BList.isEmpty())) {
				for (Iterator i = form525BList.iterator(); i.hasNext();) {
					Form525BData data = (Form525BData) i.next();
					if (!GlobalConstants.DB_ANSWER_YES.equals(data
							.getProcessedFlag())) {
						msgList.add("525B ("
								+ (Form525BTypeEnum.getEnum(data
										.getForm525B_Type())).getDescription()
								+ ") with " + data.getEmployerName() + ".");
					}
				}
			}
		}
		// to check form525b data with IB4Request id

		if ((ib4RequestList != null) && (!ib4RequestList.isEmpty())) {
			for (Iterator i = ib4RequestList.iterator(); i.hasNext();) {
				Ib4RequestData ib4RequestData = (Ib4RequestData) i.next();
				List form525bIB4PendingList = Form525B
						.find525bDataByIb4Request(ib4RequestData
								.getIb4RequestId());
				if (form525bIB4PendingList != null
						&& form525bIB4PendingList.size() > 0) {
					for (Iterator j = form525bIB4PendingList.iterator(); j
							.hasNext();) {
						Form525BData data = (Form525BData) j.next();
						if (!GlobalConstants.DB_ANSWER_YES.equals(data
								.getProcessedFlag())) {
							msgList.add("525B ("
									+ (Form525BTypeEnum.getEnum(data
											.getForm525B_Type()))
											.getDescription() + ") with "
									+ data.getEmployerName() + ".");
						}
					}
				}
			}
		}

		// to check form525b data with claim id

		if (claimData != null) {
			List form525BList = Form525B.getForm525bDataByClaimId(claimData
					.getClaimId());
			if ((form525BList != null) && (!form525BList.isEmpty())) {
				for (Iterator i = form525BList.iterator(); i.hasNext();) {
					Form525BData data = (Form525BData) i.next();
					if (!GlobalConstants.DB_ANSWER_YES.equals(data
							.getProcessedFlag())) {
						msgList.add("525B ("
								+ (Form525BTypeEnum.getEnum(data
										.getForm525B_Type())).getDescription()
								+ ") with " + data.getEmployerName() + ".");
					}
				}
			}
		}

		if (!msgList.isEmpty()) {
			objAssembly
					.addData(TransientSessionConstants.MESSAGE_LIST, msgList);
		}

		return objAssembly;
	}

	/*
	 * This method will retrieve the OSOC code using the Auto-Coder web service,
	 * which is set up on a server at our site. The string with the title and
	 * description is sent to the service and a list of osoc codes with title is
	 * returned.
	 */
	public IObjectAssembly findOsocViaWebService(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findOsocViaWebService");
		}
		try {
			AutocoderRequestBean autoRqData = (AutocoderRequestBean) objAssembly
					.getFirstBean(AutocoderRequestBean.class);
			Autocoder autocoder = new Autocoder();
			ReturnWS[] resp = autocoder.getOsocMatches(autoRqData);
			objAssembly.addData("matchedOsoc", resp);

		} catch (Exception e) {
			LOGGER.error("Message ", e);
		}
		return objAssembly;
	}

	// This method will run to generate the program number for checks
	public IObjectAssembly generateAndUpdateProgramNumber(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method generateAndUpdateProgramNumber");
		}

		Long claimantId = (Long) objectAssembly.getPrimaryKey();
		Claimant claimant = Claimant.findByPrimaryKey(claimantId);
		claimant.generateAndUpdateProgramNumber();
		return objectAssembly;
	}

	public IObjectAssembly getClaimInfoForChargeBackInquiry(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimInfoForChargeBackInquiry");
		}
		Long primaryKeyForClaimId = objectAssembly
				.getPrimaryKeyAsLong(ClaimData.class);
		Long chargeabilityId = objectAssembly
				.getPrimaryKeyAsLong(ChargeabilityData.class);
		if (primaryKeyForClaimId != null
				&& primaryKeyForClaimId.longValue() != 0) {
			Long pkId = objectAssembly.getPrimaryKeyAsLong(ClaimData.class);
			objectAssembly.removeComponent(RegularClaimData.class);

			if ((pkId != null) && (pkId.longValue() > 0)) {
				Claim claimBO = Claim.findByPrimaryKey(pkId);

				if (claimBO != null) {
					objectAssembly.addComponent(claimBO.getClaimData(), true);
				}
			}
			ClaimData claimData = (RegularClaimData) objectAssembly
					.getFirstComponent(RegularClaimData.class);
			claimData.getClaimantData().getClaimantFirstLastName();
		} else if (chargeabilityId != null && chargeabilityId.longValue() != 0) {
			objectAssembly.removeComponent(Ib5Data.class);
			Chargeability chargeability = Chargeability
					.findByPrimaryKey(chargeabilityId);
			ChargeabilityData chargeabilityData = chargeability
					.getChargeabilityData();

			Ib5 ib5 = Ib5.findIncomingIb5BySsnBYEDateAndPayingState(
					chargeabilityData.getSsn(), chargeabilityData.getByeDate(),
					chargeabilityData.getPayingState());
			Ib5Data ib5Data = ib5.getIb5Data();
			objectAssembly.addComponent(ib5Data);
		}
		// Added by Jitendra for SEB handling in view charges R4UAT00010673
		else {
			Long ib5Id = objectAssembly.getPrimaryKeyAsLong(Ib5Data.class);
			if (null != ib5Id) {
				Ib5 ib5 = Ib5.getIb5ByPkId(ib5Id);
				Ib5Data ib5Data = ib5.getIb5Data();
				objectAssembly.removeComponent(Ib5Data.class);
				objectAssembly.addComponent(ib5Data);
			}

		}
		return objectAssembly;
	}

	public IObjectAssembly loadBaseperiodWage(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method loadBaseperiodWage");
		}

		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")
		Map totWageMap = new Hashtable();
		Calendar cal = new GregorianCalendar();

		if (clmAppData.getBpStartDate() == null) {
			cal.set(GregorianCalendar.YEAR, 2015);
			cal.set(GregorianCalendar.MONTH, 10);
			cal.set(GregorianCalendar.DATE, 01);
			clmAppData.setBpStartDate(cal);
		} else
			cal.setTime(clmAppData.getBpStartDate().getTime());

		for (int i = 0; i < 6; i++) {
			QtrBean bean = new QtrBean(cal);
			totWageMap.put(bean, GlobalConstants.BIGDECIMAL_ZERO);
			cal.add(Calendar.MONTH, 3);
		}
		cal.add(Calendar.MONTH, -3);

		// to get all wages for specified claimant and time period
		List wageList1 = Wage.getBpWageList(clmAppClmntData.getSsn(),
				clmAppData.getBpStartDate(), clmAppData.getBpEndDate());

		List regWageList = new ArrayList();

		// Get only Regular employer wages //6646
		for (Iterator i = wageList1.iterator(); i.hasNext();) {
			WageData wageData = (WageData) i.next();
			Employer emprBO = Employer.getEmployerDataByPkId(wageData
					.getEmployeeData().getEmployerData().getEmployerId());
			EmployerData emprData = emprBO.getEmployerData();
			if (EmployerTypeEnum.REG.getName().equals(
					emprData.getEmployerType())) {
				regWageList.add(wageData);
			}
		}

		Calendar calEff = new GregorianCalendar();
		calEff.setTime(clmAppData.getClaimEffectiveDate());
		List qtrList = RegularClaim.getBaseLagPeriodQuarters(calEff);

		Set wageSet = new HashSet();
		wageSet.addAll(regWageList);

		objAssembly.addBeanList(qtrList, true);
		List qtrWageList = Wage.getQuarterlyWageInfo(wageSet, qtrList);
		objAssembly.addBeanList(qtrWageList, true);
		return objAssembly;
	}

	public IObjectAssembly getOtherStateEmployer(IObjectAssembly objAssembly) {

		Employer emprBO = Employer.findOsaEmployerByState((String) objAssembly
				.getData("IB1STATE"));

		if (emprBO != null) {
			objAssembly.addComponent(
					(OtherStateEmployerData) emprBO.getEmployerData(), true);
		}
		return objAssembly;
	}

	public IObjectAssembly getLatestProcessedCWEBeforeEffDate(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getLatestProcessedCWEBeforeEffDate");
		}
		BackDateRequestBean bean = (BackDateRequestBean) objAssembly
				.getFirstBean(BackDateRequestBean.class);
		ClaimantData clmntData = (ClaimantDataBridge) objAssembly
				.getFirstComponent(ClaimantDataBridge.class, true);
		List aicReopenDataList = objAssembly
				.getComponentList(AicReopenData.class);
		TeucClaim teuc = null;
		Long claimId = bean.getClaimId();
		IClaimDAO dao = DAOFactory.instance.getClaimDAO();
		List teucClaimList = dao.getTeucClaimForParentClaim(claimId);
		if (teucClaimList != null) {
			throw new BaseApplicationException(
					"error.access.cin.teuc.claim.ineffect");
		}

		List traClaimList = dao.findTraClaimByParentClaimId(claimId);
		if (traClaimList != null) {
			throw new BaseApplicationException(
					"error.access.cin.tra.claim.ineffect");
		}
		if (ClaimApplicationTypeEnum.NEW.getName().equals(bean.getClaimType())) {
			Claim claim = Claim.findByPrimaryKey(claimId);
			Set<BackDateRequestData> setBackDate = claim.getClaimData()
					.getBackDateRequestData();
			// getlatest backDate Data
			if (null != setBackDate && !setBackDate.isEmpty()) {
				BackDateRequestData backDataData = null;
				for (BackDateRequestData backDateRequestData : setBackDate) {
					if (backDataData == null) {
						backDataData = backDateRequestData;
					} else if (backDateRequestData.getCreatedOn().after(
							backDataData.getCreatedOn())) {
						backDataData = backDateRequestData;
					}
				}
				if (backDataData.getDecisionMadeDate() == null
						|| !DateUtility.isSameDay(backDataData
								.getAllowedBackDate(), claim.getClaimData()
								.getEffectiveDate())) {
					throw new BaseApplicationException(
							"error.access.cin.backdaterequest.requestexists.message");
				}
			}
			/*
			 * BackDateRequest bo =
			 * BackDateRequest.getBackDateRequestDataByClaimId
			 * (bean.getClaimId()); if (bo != null) { throw new
			 * BaseApplicationException
			 * ("error.access.cin.backdaterequest.requestexists.message"); }
			 */
		} else {
			for (Iterator itr = aicReopenDataList.iterator(); itr.hasNext();) {
				AicReopenData aicReopenData = (AicReopenData) itr.next();
				if (bean.getAicReopenId().compareTo(
						aicReopenData.getAicReopenId()) == 0) {
					BackDateRequest bo = BackDateRequest
							.getBackDateRequestDataByClaimId(aicReopenData
									.getClaimData().getClaimId());
					if (bo != null) {
						throw new BaseApplicationException(
								"error.access.cin.backdaterequest.requestexists.message");
					}
				}
			}
		}
		ProcessedWeeklyCertBO processedWeeklyCertBO = ProcessedWeeklyCertBO
				.getLatestProcessedCWEBeforeEffDate(clmntData.getPkId(),
						DateFormatUtility.parse(bean.getEffectiveDate(),
								GlobalConstants.DATE_FORMAT));
		if (processedWeeklyCertBO != null) {
			bean.setLatestProcessedCWE(DateFormatUtility.format(
					processedWeeklyCertBO.getProcessedWeeklyCertData()
							.getWeeklyCertData().getClaimWeekEndDate(),
					GlobalConstants.DATE_FORMAT));
		}
		return objAssembly;
	}

	private IObjectAssembly checkFederalAndMonetaryIssuesForNBY(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkFederalAndMonetaryIssuesForNBY");
		}
		ClaimData claimData = Claim
				.findLatestNonCancelledRegClaimBySsn(objectAssembly.getSsn());
		if (claimData != null
				&& GlobalConstants.DB_ANSWER_YES.equals(claimData
						.getMonEligibleFlag())) {
			if (!ProgramTypeEnum.UI_ONLY.getName().equals(
					claimData.getProgramType())) {
				LOGGER.busError("The program type for previous Regular Claim is not FE/X So NBY claim can not be automatically established for "
						+ objectAssembly.getSsn());
				objectAssembly
						.addBusinessError("The program type for previous Regular Claim is FE/X So NBY claim can not be automatically established for "
								+ objectAssembly.getSsn());
				return objectAssembly;
			}
		}

		try {
			objectAssembly = checkPendingMonetaryIssue(objectAssembly);
		} catch (BaseApplicationException bex) {
			LOGGER.busError("BaseApplicationException " + bex.getMessage());
			objectAssembly.addBusinessError(bex.getMessage());
			return objectAssembly;
		}
		if (objectAssembly.getData(TransientSessionConstants.MESSAGE_LIST) != null) {
			ArrayList msgList = (ArrayList) objectAssembly
					.getData(TransientSessionConstants.MESSAGE_LIST);
			StringBuffer message = new StringBuffer();
			for (int x = 0; x < msgList.size(); x++) {
				message.append((String) msgList.get(x));
				message.append(",");
			}
			String msgString = message.substring(0,
					message.lastIndexOf(",") - 1);
			LOGGER.busError("Monetary Issues pending for SSN "
					+ objectAssembly.getSsn() + " Following are details :: "
					+ msgString);
			objectAssembly
					.addBusinessError("NBY claim can not be automatically established for "
							+ objectAssembly.getSsn()
							+ "as Monetary Issue(s) are pending. Following are details :: "
							+ msgString);
		}

		return objectAssembly;
	}

	public IObjectAssembly checkIssueWorkitemExists(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkIssueWorkitemExists");
		}
		// R4UAT00010790
		GenericWorkflowSearchBean bean = new GenericWorkflowSearchBean();
		bean.setGlobalContainerMember("ssneanfein", objectAssembly.getSsn(),
				true);
		bean.setGlobalContainerMember("type", "ssn", true);
		bean.setReadyWorkItemState();

		// CIF_00962 Uncommenting the commented out code previously
		// CIF_02244 this is commented as this is not covered in Design as of
		// now. Under state discussion if this is required or not
		// thus commenting as of now
		// TODO revisit with correct approach
		/*
		 * List issueRelatedWorkItemsList =
		 * RegularClaim.issueRelatedWorkItemsList(bean); if
		 * (issueRelatedWorkItemsList!=null &&
		 * issueRelatedWorkItemsList.size()>0){
		 * objectAssembly.addData("issue_work_item_exist",
		 * YesNoTypeEnum.NumericYes.getName()); }else{
		 * objectAssembly.addData("issue_work_item_exist",
		 * YesNoTypeEnum.NumericNo.getName()); }
		 */

		objectAssembly.addData("issue_work_item_exist",
				YesNoTypeEnum.NumericNo.getName());
		return objectAssembly;
	}

	public IObjectAssembly getTeucReachbackDetail(IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getTeucReachbackDetail");
		}
		String ssn = objectAssembly.getSsn();
		String type = (String) objectAssembly.getData("TEUC_REACHBACK_TYPE");
		TeucDeclarationData teucDeclarationData = (TeucDeclarationData) objectAssembly
				.getFirstComponent(TeucDeclarationData.class, true);

		if (ssn != null && type != null && teucDeclarationData != null) {
			TeucReachback reachbackBO = TeucReachback.findTeucReachbackBySsn(
					ssn, teucDeclarationData.getTeucDeclarationId(), type);
			if (reachbackBO != null
					&& reachbackBO.getTeucReachbackData() != null) {
				objectAssembly.addComponent(reachbackBO.getTeucReachbackData());
			}
		}
		return objectAssembly;
	}

	// Code has been added by 226762 for R4UAT00010678
	public IObjectAssembly getChargeInquiryTransactionRecords(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getChargeInquiryTransactionRecords");
		}
		// Get all the transaction for employer & quarter
		Date quarterStartDate = (Date) objectAssembly
				.getData("QUARTER_START_DATE");
		Date quarterEndDate = (Date) objectAssembly.getData("QUARTER_END_DATE");
		Long employerId = (Long) objectAssembly.getData("EMPLOYER_ID");
		String chargeability = null;
		List<ChargeInquiryTransactionBO> employerChargeTransactionList = new ArrayList<ChargeInquiryTransactionBO>();
		;
		IJdbcEmployerChargeDetailsDAO employerChargeDetailsDao = DAOFactory.instance
				.getJdbcEmployerChargeDetailsDAO();

		if (objectAssembly.getData("SSN") != null) {
			String ssn = (String) objectAssembly.getData("SSN");
			employerChargeTransactionList = employerChargeDetailsDao
					.getEmployerChargeTransactionforSsnAndQuarter(
							quarterStartDate, quarterEndDate, employerId, ssn);
		} else {
			employerChargeTransactionList = employerChargeDetailsDao
					.getEmployerChargeTransactionforQuarter(quarterStartDate,
							quarterEndDate, employerId);
		}

		if (objectAssembly.getData("CHARGEABILITY") != null) {
			chargeability = (String) objectAssembly.getData("CHARGEABILITY");
		}
		List<ChargeInquiryTransactionBO> employerChargeTransactionsWithChargeabilityStatus = new ArrayList<ChargeInquiryTransactionBO>();
		for (ChargeInquiryTransactionBO chargeTransactionBO : employerChargeTransactionList) {
			ChargeInquiryTransactionBO chargeBO = getEmployerChargeabilityStatus(chargeTransactionBO);

			if (chargeability == null
					|| (chargeability != null && chargeability
							.equalsIgnoreCase(chargeBO.getChargeStatus()))) {
				employerChargeTransactionsWithChargeabilityStatus.add(chargeBO);
			}
		}

		Boolean employerInvolvedInTransfer = getEmployerTransferRecords(
				employerId, quarterEndDate);

		objectAssembly.addData("CHARGE_TRANSACTIONS",
				employerChargeTransactionsWithChargeabilityStatus);
		objectAssembly.addData("EMPLOYER_TRANSFERRED",
				employerInvolvedInTransfer);

		return objectAssembly;
	}

	public ChargeInquiryTransactionBO getEmployerChargeabilityStatus(
			ChargeInquiryTransactionBO transactionBO) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getEmployerChargeabilityStatus");
		}

		IJdbcEmployerChargeDetailsDAO employerChargeDetailsDao = DAOFactory.instance
				.getJdbcEmployerChargeDetailsDAO();
		String chargeabilityStatus = "";
		if (transactionBO.getClaimId() != null
				&& transactionBO.getClaimId().intValue() != 0) {
			// Check for the pending status
			chargeabilityStatus = getPendingIssuesAndDecisionsForClaim(
					transactionBO.getClaimId(), transactionBO.getEmployerId());
			if (StringUtility.isBlank(chargeabilityStatus)) {
				chargeabilityStatus = employerChargeDetailsDao
						.getEmployerChargeabilityStatusForClaimId(
								transactionBO.getEmployerId(),
								transactionBO.getClaimId());
			}
		} else if (transactionBO.getChargeabilityId() != null
				&& transactionBO.getChargeabilityId().intValue() != 0) {
			// Fetch the employer chargeability based on SSN, BYE and
			// Chargeability Id
			chargeabilityStatus = employerChargeDetailsDao
					.getEmployerChargeabilityStatusForChargeabilityId(
							transactionBO.getEmployerId(),
							transactionBO.getChargeabilityId());
		} else if (transactionBO.getIb5Id() != null
				&& transactionBO.getIb5Id().intValue() != 0) {
			// Fetch the employer chargeability based on SSN, BYE and Ib5 Id
			chargeabilityStatus = employerChargeDetailsDao
					.getEmployerChargeabilityStatusForIb5Id(
							transactionBO.getEmployerId(),
							transactionBO.getIb5Id());
		} else {
			// Give the default "UNKNOWN" status
			chargeabilityStatus = "UNKNOWN";
		}
		transactionBO.setChargeStatus(chargeabilityStatus);
		return transactionBO;
	}

	/**
	 * @author jshrivas
	 * @param employerId
	 * @param transferDate
	 * @description - This method will check the pending issue for claim
	 * @return
	 */
	public String getPendingIssuesAndDecisionsForClaim(Long claimId,
			Long employerId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getPendingIssuesAndDecisionsForClaim");
		}
		String issueStatus = "";
		IJdbcEmployerChargeDetailsDAO employerChargeDetailsDao = DAOFactory.instance
				.getJdbcEmployerChargeDetailsDAO();
		// Check of issue status
		List pendingIssueList = Issue
				.findChargeabilityIssuesByClaimIdWithPendingStatus(claimId,
						employerId);
		if (pendingIssueList != null && pendingIssueList.size() > 0) {
			issueStatus = ClaimEmploymentChargeStatusEnum.PENDING.getName();
		} else {
			List<Long> activeDecisionIds = employerChargeDetailsDao
					.getActiveDecisionsForClaimId(claimId, employerId);
			if (activeDecisionIds != null && activeDecisionIds.size() > 0) {
				for (Long decisionId : activeDecisionIds) {
					NmonReconsiderationRequestData nmonReconRqstData = NmonReconsiderationRequest
							.getReconsiderationRequestByDecisionId(decisionId)
							.getNmonReconsiderationRequestData();
					if (nmonReconRqstData != null
							&& nmonReconRqstData
									.getReconsiderationStatus()
									.equalsIgnoreCase(
											NmonReconsiderationStatusEnum.RECONSIDERATIONPENDING
													.getName())) {
						issueStatus = ClaimEmploymentChargeStatusEnum.PENDING
								.getName();
						break;
					} else {
						boolean isAppealPending = employerChargeDetailsDao
								.getOutstandingAppealDecisionForDecisionId(decisionId);
						if (isAppealPending) {
							issueStatus = ClaimEmploymentChargeStatusEnum.PENDING
									.getName();
							break;
						}
					}
				}
			}
		}
		return issueStatus;
	}

	public Boolean getEmployerTransferRecords(Long employerId, Date transferDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getEmployerTransferRecords");
		}

		IJdbcTransferEmployerDAO jdbcTransferEmployerDAO = DAOFactory.instance
				.getJdbcTransferEmployerDAO();
		Integer count = (Integer) jdbcTransferEmployerDAO
				.getRecordCountForEmployerByTransferDate(employerId,
						transferDate);
		if (count != null && count.intValue() > 0) {
			return (new Boolean(true));
		} else {
			return (new Boolean(false));
		}
	}

	public IObjectAssembly checkMonetaryEligibility(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkMonetaryEligibility");
		}
		BatchCheckAlternateBasePeriodEligibilityBean batchData = (BatchCheckAlternateBasePeriodEligibilityBean) objectAssembly
				.getData("BatchData");
		Date[] date = DateUtility.determineQuarterDate(
				batchData.getEffectiveDate(), -1);
		Date firstSundayofNextQtr = DateUtility.getCurrentOrNextSunday(date[0]);
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstSundayofNextQtr);
		batchData.setNextQtrEffectiveDate(firstSundayofNextQtr);
		boolean uiEligible = RegularClaim.checkPotentialUIMonetaryEligiblity(
				batchData.getSsn(), cal);
		boolean filedUIEligNextQtr = false;

		if (uiEligible) {
			List<RegularClaimData> claimDataList = RegularClaim
					.getRegularClaimEffectiveBetweenGivenDate(
							batchData.getSsn(), firstSundayofNextQtr, date[1]);
			if (claimDataList != null && !claimDataList.isEmpty()) {
				filedUIEligNextQtr = true;
			}
		}

		batchData.setNextQtrEndDate(date[1]);
		batchData.setNextQtrUIEligible(uiEligible);
		batchData.setFiledUIEligNextQtr(filedUIEligNextQtr);
		return objectAssembly;
	}

	/**
	 * @param objectAssembly
	 *            IObjectAssembly
	 * @return objectAssembly IObjectAssembly
	 */
	public IObjectAssembly checkAndInitiateEmailVerification(
			final IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method checkAndInitiateEmailVerification");
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		final ClaimApplicationClaimantDataBridge claimantAppData = (ClaimApplicationClaimantDataBridge) objectAssembly
				.getFirstComponent(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")
		final ClaimantData claimantData = (ClaimantDataBridge) objectAssembly
				.getFirstComponent(ClaimantDataBridge.class);

		User userBO = null;
		UserData userData = null;

		if (claimantData != null
				&& StringUtility.isNotEmpty(claimantData.getUserId())) {
			userBO = User.findByUserId(claimantData.getUserId());
			userData = userBO.getUserData();
			userData.setEmailId(claimantAppData.getEmail());
			userData.setCorrespondenceMode(claimantAppData
					.getCorrespondenceMode());
		}
		if (userBO != null
				&& CorrespondenceModeEnum.EMAIL.getName().equals(
						claimantAppData.getCorrespondenceMode())) {
			try {
				userBO.checkAndInitiateEmailVerification(false, false);
			} catch (BaseApplicationException e) {
				LOGGER.error("error while initiating email verification :: "
						+ e);
			}
		}
		return objectAssembly;

	}

	public IObjectAssembly batchPerformEUCProgramOptionCheck(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchPerformEUCProgramOptionCheck");
		}

		BatchPerformEUCProgramOptionCheckBean batchBean = objectAssembly
				.getFirstBean(BatchPerformEUCProgramOptionCheckBean.class);

		String ssn = batchBean.getSsn();
		Long regularClaimId = batchBean.getClaimId();
		boolean rqrqIssueExists = true;

		RegularClaimData regClaimData = RegularClaim
				.findRegularClaimDataByClaimId(regularClaimId);

		List rqrqIssueList = Issue.getIssueFor715ForTeucClaims(regularClaimId);

		if (rqrqIssueList == null) {
			rqrqIssueExists = false;
		}

		Boolean isEligibleForEUCOption = RegularClaim.isEligibleForEUCOption(
				regClaimData, null, ssn, rqrqIssueExists);

		// added to check if elrq issue already exists
		boolean elrqIssueExists = false;
		List elrqIssueList = Issue.getEUCOptionIssuebySSN(ssn, TeucDeclaration
				.getDeclaration().getTeucDeclarationData().getLastPayableCwe());
		if (elrqIssueList != null) {
			// If EUC Option issue Exist, check if issue start date is after
			// latest regular claim effective date.
			Iterator<IssueData> elrqIssueListIterator = elrqIssueList
					.iterator();
			while (elrqIssueListIterator.hasNext()) {
				IssueData currentIssueData = elrqIssueListIterator.next();
				if ((DateUtility.isAfterDate(
						currentIssueData.getIssueStartDate(),
						regClaimData.getEffectiveDate()) || DateUtility
						.isSameDay(currentIssueData.getIssueStartDate(),
								regClaimData.getEffectiveDate()))) {
					elrqIssueExists = true;
					break;
				}
			}
		}
		// end of elrq issue check

		if (isEligibleForEUCOption.booleanValue() && !elrqIssueExists) {
			// Create Eligibility Requirement - EUC Program Option issue and
			// claimant will be paid from EUC
			IssueBean issueBean = new IssueBean();
			issueBean.setClaimantSsn(ssn);
			issueBean.setIssueStartDate(regClaimData.getEffectiveDate());
			issueBean.setClaimid(regularClaimId);
			Issue.createEUCOptionIssue(issueBean);
		}

		batchBean.setEligibleEUCOption(isEligibleForEUCOption.booleanValue());
		batchBean.setRqrqExists(rqrqIssueExists);
		batchBean.setElrqExists(elrqIssueExists);

		return objectAssembly;
	}

	public IObjectAssembly batchRecheckEUCProgramOption(
			IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchRecheckEUCProgramOption");
		}

		BatchPerformEUCProgramOptionCheckBean batchBean = objectAssembly
				.getFirstBean(BatchPerformEUCProgramOptionCheckBean.class);

		String ssn = batchBean.getSsn();
		Long regularClaimId = batchBean.getClaimId();
		boolean rqrqIssueExists = true;

		RegularClaimData regClaimData = RegularClaim
				.findRegularClaimDataByClaimId(regularClaimId);

		List rqrqIssueList = Issue.getIssueFor715ForTeucClaims(regularClaimId);

		if (rqrqIssueList == null) {
			rqrqIssueExists = false;
		}

		Boolean isEligibleForEUCOption = RegularClaim.isEligibleForEUCOption(
				regClaimData, null, ssn, rqrqIssueExists);

		batchBean.setEligibleEUCOption(isEligibleForEUCOption.booleanValue());
		batchBean.setRqrqExists(rqrqIssueExists);

		return objectAssembly;
	}

	/**
	 * This method takes ObjectAssembly with ClaimApplicationClaimantData and
	 * ClaimApplicationData as input and and saves them along with the OSOC Code
	 * Data, or updates them if they already exist.
	 * 
	 * @param objAssembly
	 *            with ClaimApplicationClaimantData, and ClaimApplicationData
	 * @return objAssembly
	 */
	public IObjectAssembly saveOrUpdateClmAppClmntWithOSOC(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClmAppClmntWithOSOC");
		}

		// BusinessDelegate.executeServiceMethod(objAssembly,
		// this.getClass().getName(), "deleteAllOSOCCodes",
		// objAssembly.getUserContext());

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		ClaimApplicationClaimant clmAppClaimantBO = new ClaimApplicationClaimant(
				clmAppClmntData);

		Set claimApplicationClaimantOsocSet = clmAppClmntData
				.getClaimApplicationClaimantOSOCDataSet();
		Set<ClaimApplicationClaimantOSOCData> dataSet = new HashSet<ClaimApplicationClaimantOSOCData>();

		List<JobTitleBean> jobTitleBeanList = objAssembly
				.getBeanList(JobTitleBean.class);
		if (jobTitleBeanList != null && jobTitleBeanList.size() > 0) {

			if (claimApplicationClaimantOsocSet == null) {
				LOGGER.debug("###############################################Creating new HashSet for OSOC code as claimApplicationClaimantOsocSet is null");
				claimApplicationClaimantOsocSet = new HashSet();
			}

			for (Iterator<JobTitleBean> it = jobTitleBeanList.iterator(); it
					.hasNext();) {
				LOGGER.debug("Creating OSOC code Data:"
						+ jobTitleBeanList.size());
				JobTitleBean jobTitleBean = it.next();
				ClaimApplicationClaimantOSOCData claimApplicationClaimantOSOCData = null;
				if (claimApplicationClaimantOsocSet != null
						&& !claimApplicationClaimantOsocSet.isEmpty()) {
					Iterator<ClaimApplicationClaimantOSOCData> its = claimApplicationClaimantOsocSet
							.iterator();
					while (its.hasNext()) {
						ClaimApplicationClaimantOSOCData data = its.next();
						if (data.getOsocTitle().equals(
								jobTitleBean.getJobTitle())) {
							claimApplicationClaimantOSOCData = data;
							break;
						}
					}
				}
				if (claimApplicationClaimantOSOCData == null) {
					claimApplicationClaimantOSOCData = new ClaimApplicationClaimantOSOCData();
					claimApplicationClaimantOSOCData
							.setClaimApplicationClaimantData(clmAppClmntData);
					claimApplicationClaimantOSOCData.setOsocTitle(jobTitleBean
							.getJobTitle());
				}

				claimApplicationClaimantOSOCData.setIsPrimary(jobTitleBean
						.getIsPrimary());
				claimApplicationClaimantOSOCData
						.setIsPreferredJobSkill(jobTitleBean
								.getIsPreferredJob());
				claimApplicationClaimantOSOCData
						.setWorkExperienceMonth(Integer.valueOf(jobTitleBean
								.getWorkExperienceYear())
								* 12
								+ Integer.valueOf(jobTitleBean
										.getWorkExperienceMonth()));
				dataSet.add(claimApplicationClaimantOSOCData);
				// CIF_01361 Start check whether the selected job is in
				// T_MST_OSOC_TITLE if not make an entry
				ClaimApplication clmAppBO = new ClaimApplication(
						new ClaimApplicationData());
				// boolean recordFlag =
				// clmAppBO.checkRecordByKey(jobTitleBean.getJobTitle());
				String description = (String) CacheUtility
						.getCachePropertyValue("T_MST_OSOC_TITLE", "key",
								jobTitleBean.getJobTitle(), "description");
				if (StringUtility.isBlank(description)) {
					MstOsocTitle mstOsocTitle = new MstOsocTitle();
					mstOsocTitle.setKey(jobTitleBean.getJobTitle());
					mstOsocTitle.setDescription(jobTitleBean
							.getJobDescription());
					mstOsocTitle.setCreatedBy(GlobalConstants.SYSTEM_USER_ID);
					mstOsocTitle.setUpdatedBy(GlobalConstants.SYSTEM_USER_ID);
					clmAppBO.saveOrUpdateMstOsocTitle(mstOsocTitle);
					objAssembly.addData("OSOC_TITLE_ADDED", Boolean.TRUE);
				}
				// CIF_01361 End
			}

			// for(Iterator<JobTitleBean> it = jobTitleBeanList.iterator();
			// it.hasNext();){
			// LOGGER.debug("###############################################Creating OSOC code Data for size:"+jobTitleBeanList.size());
			// JobTitleBean jobTitleBean = it.next();
			// ClaimApplicationClaimantOSOCData claimApplicationClaimantOSOCData
			// =null;
			// boolean newFlag=true;
			// if(claimApplicationClaimantOsocSet!=null &&
			// !claimApplicationClaimantOsocSet.isEmpty()){
			// Iterator<ClaimApplicationClaimantOSOCData> its
			// =claimApplicationClaimantOsocSet.iterator();
			// while(its.hasNext()){
			// ClaimApplicationClaimantOSOCData data = its.next();
			// if(data.getOsocTitle().equals(jobTitleBean.getJobTitle())){
			// claimApplicationClaimantOSOCData=data;
			// newFlag=false;
			// LOGGER.debug("###############################################Updating existing OSOC Data:"+data.getClaimApplicationClaimantOSOCId());
			// break;
			// }
			// }
			// }
			// if(newFlag){
			// claimApplicationClaimantOSOCData = new
			// ClaimApplicationClaimantOSOCData();
			// claimApplicationClaimantOSOCData.setClaimApplicationClaimantData(clmAppClmntData);
			// claimApplicationClaimantOSOCData.setOsocTitle(jobTitleBean.getJobTitle());
			// LOGGER.debug("###############################################Creating a new OSOC Data:"+claimApplicationClaimantOSOCData.getOsocTitle());
			//
			// }
			//
			// claimApplicationClaimantOSOCData.setIsPrimary(jobTitleBean.getIsPrimary());
			// claimApplicationClaimantOSOCData.setIsPreferredJobSkill(jobTitleBean.getIsPreferredJob());
			// claimApplicationClaimantOSOCData.setWorkExperienceMonth(Integer.valueOf(jobTitleBean.getWorkExperienceYear())*12
			// + Integer.valueOf(jobTitleBean.getWorkExperienceMonth()));
			// if(newFlag){
			// claimApplicationClaimantOsocSet.add(claimApplicationClaimantOSOCData);
			// }
			// }

			LOGGER.debug("Setting HashSet to Claim Application Claimant");
			if (dataSet != null) {
				clmAppClmntData.setClaimApplicationClaimantOSOCDataSet(dataSet);
			}

			clmAppClaimantBO.saveOrUpdate();
			clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
			ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
			clmAppBO.saveOrUpdate();
			// clmAppBO.flush();
		}
		return objAssembly;
	}

	/**
	 * This method finds all the available jobs for a claimant
	 */
	public IObjectAssembly findAvailablejobs(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findAvailablejobs");
		}
		// CIF_03398 starts
		// Defect_7063
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		String ssn = null;
		Long claimantId = null;
		if (null == clmAppData || StringUtility.isBlank(clmAppData.getSsn())) {
			WeeklyCertificationValidationDataBean weekBean = objAssembly
					.getFirstBean(WeeklyCertificationValidationDataBean.class);
			if (weekBean != null && null != weekBean.getClaimantData()) {
				if (StringUtility.isNotBlank(weekBean.getClaimantData()
						.getSsn())) {
					ssn = weekBean.getClaimantData().getSsn();
				} else {
					claimantId = weekBean.getClaimantData().getPkId();
				}
			}
		} else {
			ssn = clmAppData.getSsn();
		}
		if (StringUtility.isNotBlank(ssn) || null != claimantId) {
			Claimant claimant = null;
			if (StringUtility.isNotBlank(ssn)) {
				claimant = Claimant.findBySsn(ssn);
			} else {
				claimant = Claimant.findByPrimaryKey(claimantId);
			}
			objAssembly.addComponent(claimant.getClaimantData());
			String osocCode = null;
			Long radius = null;
			String zipCode = null;
			if (null != claimant
					&& null != claimant.getClaimantData()
					&& null != claimant.getClaimantData()
							.getClaimantOSOCDataSet()
					&& !claimant.getClaimantData().getClaimantOSOCDataSet()
							.isEmpty()) {
				Set<ClaimantOSOCData> osocDataSet = claimant.getClaimantData()
						.getClaimantOSOCDataSet();
				Set<ClaimantGeographicalPreferenceData> geoPrefDataSet = claimant
						.getClaimantData()
						.getClaimantGeographicalPreferenceDataSet();
				if (null != geoPrefDataSet && !geoPrefDataSet.isEmpty()) {
					for (ClaimantGeographicalPreferenceData geoPrefData : geoPrefDataSet) {
						if (ViewConstants.YES
								.equals(geoPrefData.getIsPrimary())) {
							radius = ((Integer) CacheUtility
									.getCachePropertyValue(
											"T_MST_GEOGRAPHICAL_RANGE", "key",
											geoPrefData.getGeographicalRange(),
											"value")).longValue();
							zipCode = geoPrefData.getZipcode();
							break;
						}
					}
					if (StringUtility.isBlank(zipCode)) {
						ClaimantGeographicalPreferenceData geoData = geoPrefDataSet
								.iterator().next();
						radius = ((Integer) CacheUtility.getCachePropertyValue(
								"T_MST_GEOGRAPHICAL_RANGE", "key",
								geoData.getGeographicalRange(), "value"))
								.longValue();
						zipCode = geoData.getZipcode();
					}
				}
				if (StringUtility.isBlank(zipCode)) {
					ClaimantAddressData addRData = claimant.getClaimantData()
							.getResidentialAddress();
					if (null != addRData) {
						zipCode = addRData.getAddrData().getZip();
						if (StringUtility.isNotBlank(zipCode)
								&& zipCode.length() == GlobalConstants.NUMERIC_NINE) {
							zipCode = zipCode.substring(
									GlobalConstants.NUMERIC_ZERO,
									GlobalConstants.NUMERIC_FIVE);
						}
					}
				}
				if (StringUtility.isNotBlank(zipCode)) {
					if (null != osocDataSet && !osocDataSet.isEmpty()) {
						String osocCodePrimary = null;
						for (ClaimantOSOCData claimantOsoc : osocDataSet) {
							if (ViewConstants.YES.equals(claimantOsoc
									.getIsPreferredJobSkill())) {
								osocCode = claimantOsoc.getOsocTitle();
								break;
							}
							if (ViewConstants.YES.equals(claimantOsoc
									.getIsPrimary())) {
								osocCodePrimary = claimantOsoc.getOsocTitle();
							}
						}
						if (StringUtility.isBlank(osocCode)
								&& StringUtility.isNotBlank(osocCodePrimary)) {
							osocCode = osocCodePrimary;
						}
					}
				}
				if (null == radius
						|| radius.longValue() == GlobalConstants.NUMERIC_ZERO) {
					radius = Long.valueOf(GlobalConstants.NUMERIC_THOUSAND);
				}
				if (StringUtility.isNotBlank(zipCode)
						&& StringUtility.isNotBlank(osocCode) && null != radius) {
					JobListing jobs = new JobListing(osocCode, zipCode, radius);
					try {
						List<JobSearchResultsStruct> result = jobs
								.getAvailableJobs();
						if (result != null && !result.isEmpty()) {
							List<AvailableJobBean> jobBeanList = new ArrayList<AvailableJobBean>();
							AvailableJobBean availableJobBean = null;
							for (JobSearchResultsStruct jobResult : result) {
								availableJobBean = new AvailableJobBean();
								availableJobBean.setJobOrderNumber(jobResult
										.getJoNumber());
								availableJobBean.setJobTitle(jobResult
										.getJobTitle());
								availableJobBean
										.setJobOrderURL(ApplicationProperties.JOB_LISTING_URL
												+ jobResult.getURL());
								availableJobBean
										.setDateTimeAvailable(DateFormatUtility
												.format(jobResult.getDate()
														.toGregorianCalendar()));
								availableJobBean
										.setExperience(null != jobResult
												.getMonthsExp() ? jobResult
												.getMonthsExp().toString()
												: "NA");
								JobListingDegreeCodeEnum degree = JobListingDegreeCodeEnum
										.getEnum(jobResult.getDegreeCode()
												.toString());
								availableJobBean
										.setDegree(null != degree ? degree
												.getDescription()
												: JobListingDegreeCodeEnum.NONE
														.getDescription());
								jobBeanList.add(availableJobBean);
							}
							objAssembly.addBeanList(jobBeanList, Boolean.TRUE);
						}
					} catch (Exception e) {
						LOGGER.error(
								"Exception occured while connecting to Job Service",
								e);
					}
				}
			}
		}
		// CIF_03398 ends
		// This is no longer required as interface for job listing is changed
		/*
		 * ClaimApplicationData clmAppData =
		 * (ClaimApplicationData)objAssembly.fetchORCreate
		 * (ClaimApplicationData.class); ClaimApplicationClaimantData
		 * clmAppClmntData = clmAppData.getClaimApplicationClaimantData();
		 * if(clmAppClmntData == null) { clmAppClmntData =
		 * (ClaimApplicationClaimantData
		 * )objAssembly.fetchORCreate(ClaimApplicationClaimantData.class); }
		 * 
		 * // CIF_03014 || Start || CR Implementation: To show the list of
		 * available jobs after each weekly certification is completed
		 * if(StringUtility.isBlank(clmAppClmntData.getSsn())){ WeeklyCertData
		 * wcData = objAssembly.getFirstComponent(WeeklyCertData.class);
		 * clmAppClmntData.setSsn(wcData.getClaimantData().getSsn()); } //
		 * CIF_03014 || End
		 * 
		 * Claimant claimant =
		 * Claimant.findClaimantWithAvailableJobs(clmAppClmntData.getSsn());
		 * if(claimant.getClaimantData().getClaimantMatchingJobDataSet() != null
		 * &
		 * claimant.getClaimantData().getClaimantMatchingJobDataSet().size()>0){
		 * for(Iterator<ClaimantMatchingJobData>it=claimant.getClaimantData().
		 * getClaimantMatchingJobDataSet().iterator();it.hasNext();){
		 * ClaimantMatchingJobData claimantMatchingJobData = it.next();
		 * if(claimantMatchingJobData.getJobOrderData() != null){
		 * claimantMatchingJobData.getJobOrderData().getJobOrderNumber(); } } }
		 * objAssembly.addComponent(claimant.getClaimantData(), true);
		 */
		return objAssembly;
	}

	public IObjectAssembly createClaimantJobRequestAndSendMessage(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createClaimantJobRequestAndSendMessage");
		}
		ClaimantData claimantData = objAssembly
				.getFirstComponent(ClaimantDataBridge.class);
		if (claimantData != null) {
			objAssembly.setPrimaryKey(claimantData.getPkId());
		} else {
			objAssembly.setPrimaryKey(objAssembly.getPrimaryKeyAsLong());
		}
		objAssembly = this.createClaimantJobRequest(objAssembly);
		objAssembly = this.findClaimantMatchingJobs(objAssembly);
		return objAssembly;
	}

	public IObjectAssembly createClaimantJobRequest(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createClaimantJobRequest");
		}
		Long claimantId = objAssembly.getPrimaryKeyAsLong();
		Claimant claimant = Claimant.findByPrimaryKey(claimantId);
		ClaimantMatchingJobRequestData claimantMatchingJobRequestData = new ClaimantMatchingJobRequestData();
		claimantMatchingJobRequestData.setClaimantData(claimant
				.getClaimantData());
		claimantMatchingJobRequestData.setRequestDate(new Date());
		claimantMatchingJobRequestData
				.setIsResponseReveived(GlobalConstants.DB_ANSWER_NO);
		if (claimant.getClaimantData().getClaimantMatchingJobRequestDataSet() != null) {
			claimant.getClaimantData().getClaimantMatchingJobRequestDataSet()
					.add(claimantMatchingJobRequestData);
		} else {
			Set<ClaimantMatchingJobRequestData> claimantMatchingJobRequestDataSet = new HashSet<ClaimantMatchingJobRequestData>();
			claimantMatchingJobRequestDataSet
					.add(claimantMatchingJobRequestData);
			claimant.getClaimantData().setClaimantMatchingJobRequestDataSet(
					claimantMatchingJobRequestDataSet);
		}
		claimant.saveOrUpdateClaimantMatchingJobRequest();
		claimant.flush();
		ClaimantJobRequestData claimantJobRequestData = new ClaimantJobRequestData();
		List<JobTitleBean> jobTitleBeanList = new ArrayList<JobTitleBean>();
		if (null != claimant.getClaimantData().getClaimantOSOCDataSet()) {

			for (ClaimantOSOCData claimantOSOCData : claimant.getClaimantData()
					.getClaimantOSOCDataSet()) {
				JobTitleBean jobTitleBean = new JobTitleBean();
				jobTitleBean.setJobTitle(claimantOSOCData.getOsocTitle());
				jobTitleBean.setWorkExperienceMonth(claimantOSOCData
						.getWorkExperienceMonth().toString());
				jobTitleBean.setIsPrimary(claimantOSOCData.getIsPrimary());
				jobTitleBean.setIsPreferredJob(claimantOSOCData
						.getIsPreferredJobSkill());
				jobTitleBeanList.add(jobTitleBean);
			}

		}
		claimantJobRequestData.setJobTitleBeanList(jobTitleBeanList);
		claimantJobRequestData.setClaimantId(claimantId);
		if (null != claimant.getClaimantData()
				.getClaimantGeographicalPreferenceDataSet()) {
			for (ClaimantGeographicalPreferenceData claimantGeographicalPreferenceData : claimant
					.getClaimantData()
					.getClaimantGeographicalPreferenceDataSet()) {
				if (!"NOLT".equals(claimantGeographicalPreferenceData
						.getGeographicalRange())) {
					claimantJobRequestData.setGeographicalRange(Integer
							.valueOf(claimantGeographicalPreferenceData
									.getGeographicalRange()));
					if (StringUtility
							.isNotBlank(claimantGeographicalPreferenceData
									.getCity())) {
						claimantJobRequestData
								.setCity(claimantGeographicalPreferenceData
										.getCity());
					} else {
						claimantJobRequestData
								.setZipCode(claimantGeographicalPreferenceData
										.getZipcode());
					}
				}
			}
		}
		objAssembly.addData("ClaimantJobRequestData", claimantJobRequestData);
		return objAssembly;
	}

	public IObjectAssembly sendJobRequestMessage(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendJobRequestMessage");
		}
		ClaimantJobRequestData claimantJobRequestData = (ClaimantJobRequestData) objAssembly
				.getData("ClaimantJobRequestData");
		ClaimantJobRequestMessage claimantJobRequestMessage = new ClaimantJobRequestMessage(
				claimantJobRequestData);
		claimantJobRequestMessage.sendMessage();
		return objAssembly;
	}

	public IObjectAssembly findClaimantMatchingJobs(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findClaimantMatchingJobs");
		}
		System.setProperty(
				"com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump",
				"true");
		System.setProperty("com.sun.metro.soap.dump", "true");
		ClaimantJobRequestData claimantJobRequestData = (ClaimantJobRequestData) objAssembly
				.getData("ClaimantJobRequestData");
		ClaimantJobRequestMessage claimantJobRequestMessage = new ClaimantJobRequestMessage(
				claimantJobRequestData);
		claimantJobRequestMessage.execute();
		return objAssembly;
	}

	public IObjectAssembly testServiceMethod(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method testServiceMethod");
		}
		/*
		 * ClaimApplication claimApplication =
		 * ClaimApplication.findClaimAppByPkId(Long.valueOf(1023147)); Claimant
		 * claimant = Claimant.findBySsn("770000006");
		 * LOGGER.debug("Claimant Loaded : " +
		 * claimant.getClaimantData().getSsn());
		 * objAssembly.addComponent(claimApplication.getClaimApplicationData(),
		 * true);
		 * objAssembly.addComponent(claimApplication.getClaimApplicationData
		 * ().getClaimApplicationClaimantData(), true);
		 * objAssembly.addComponent(claimant.getClaimantData(),true);
		 */
		return objAssembly;
	}

	public IObjectAssembly processWingsReferralResult(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processWingsReferralResult");
		}

		Long wingsReferralResultId = objAssembly.getPrimaryKeyAsLong();
		WingsReferralResult wingsReferralResult = WingsReferralResult
				.findByPrimaryKey(wingsReferralResultId);
		Claimant claimant = Claimant.fetchClaimantBySsn(wingsReferralResult
				.getWingsReferralResultData().getSsn());
		// if the referral SSN is not a claimant, no action, mark the record as
		// processed.
		if (claimant == null) {
			LOGGER.debug("Referral Result SSN is not a cliamant. No action is taken. Marking the referral result as processed.");
			wingsReferralResult.getWingsReferralResultData()
					.setIsRecordProcessed(GlobalConstants.DB_ANSWER_YES);
			wingsReferralResult.getWingsReferralResultData()
					.setRecordProcessedDate(new Date());
			wingsReferralResult.saveOrUpdate();
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_NO_SSN);
			return objAssembly;
		}
		// Find EmployerAccount using FEIN
		EmployerAccount employerAccount = EmployerAccount
				.findEmployerAccountDataByFein(wingsReferralResult
						.getWingsReferralResultData().getFein());
		if (employerAccount == null) {
			LOGGER.debug("Referral Result FEIN is not an employer. No action is taken. Marking the referral result as processed.");
			wingsReferralResult.getWingsReferralResultData()
					.setIsRecordProcessed(GlobalConstants.DB_ANSWER_YES);
			wingsReferralResult.getWingsReferralResultData()
					.setRecordProcessedDate(new Date());
			wingsReferralResult.saveOrUpdate();
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_NO_FEIN);
			return objAssembly;
		}
		// the finder method fetches pending accounts also. Ignore pending
		// accounts
		if (EmployerRegistrationStatusEnum.PENDING.getName().equals(
				employerAccount.getEmployerAccountData().getAccountStatus())) {
			LOGGER.debug("Referral Result FEIN is not an employer. No action is taken. Marking the referral result as processed.");
			wingsReferralResult.getWingsReferralResultData()
					.setIsRecordProcessed(GlobalConstants.DB_ANSWER_YES);
			wingsReferralResult.getWingsReferralResultData()
					.setRecordProcessedDate(new Date());
			wingsReferralResult.saveOrUpdate();
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_NO_FEIN);
			return objAssembly;
		}
		Set<RegisteredEmployerData> registeredEmployerDataSet = employerAccount
				.getEmployerAccountData().getRegisteredEmployerDataSet();
		if (registeredEmployerDataSet == null) {
			LOGGER.debug("Referral Result FEIN is not an employer. No action is taken. Marking the referral result as processed.");
			wingsReferralResult.getWingsReferralResultData()
					.setIsRecordProcessed(GlobalConstants.DB_ANSWER_YES);
			wingsReferralResult.getWingsReferralResultData()
					.setRecordProcessedDate(new Date());
			wingsReferralResult.saveOrUpdate();
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_NO_FEIN);
			return objAssembly;
		}
		RegisteredEmployerData registeredEmployerData = null;
		int numberofActiveUnits = 0;
		for (RegisteredEmployerData regEmployerData : registeredEmployerDataSet) {
			if (EmployerUnitStatusEnum.ACTIVE.getName().equals(
					regEmployerData.getStatus())) {
				numberofActiveUnits++;
				registeredEmployerData = regEmployerData;
			}
		}
		IssueBean issueBean = null;
		String employerName = wingsReferralResult.getWingsReferralResultData()
				.getEmployerName();
		if (numberofActiveUnits == 1) {
			// create issue with the active unit
			LOGGER.debug("Creating issue with employer id");
			employerName = registeredEmployerData.getEmployerName();
			issueBean = populateIssueBeanFromWingsReferralResult(
					wingsReferralResult.getWingsReferralResultData(),
					registeredEmployerData.getEmployerId(), employerName);
		} else {
			// create issue by employer name, do not add ean or employer id
			LOGGER.debug("Creating issue without employer id");
			issueBean = populateIssueBeanFromWingsReferralResult(
					wingsReferralResult.getWingsReferralResultData(), null,
					employerName);
		}
		if (WingsReferralResultTypeEnum.HIRD.equals(wingsReferralResult
				.getWingsReferralResultData().getResultType())) {
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_RTW);
		} else {
			objAssembly.addData(BenefitsConstants.WINGS_RECORD_DATA,
					BenefitsConstants.WINGS_REFERRAL_PROCESSING_RESULT_REFF);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("Issue created from WINGS referral result.");
		sb.append(" Employer Name (per WINGS record): ");
		sb.append(wingsReferralResult.getWingsReferralResultData()
				.getEmployerName());
		sb.append(" Job Order Number: ");
		sb.append(wingsReferralResult.getWingsReferralResultData()
				.getJobOrderNumber());
		sb.append(" Referral Result: ");
		sb.append(wingsReferralResult.getWingsReferralResultData()
				.getResultType().getDescription());
		sb.append(" Referral Result Date: ");
		sb.append(wingsReferralResult.getWingsReferralResultData()
				.getResultDate());
		issueBean.setAdditionalInformation(sb.toString());
		Issue.createIssue(issueBean, true);
		wingsReferralResult.getWingsReferralResultData().setIsRecordProcessed(
				GlobalConstants.DB_ANSWER_YES);
		wingsReferralResult.saveOrUpdate();
		return objAssembly;
	}

	private IssueBean populateIssueBeanFromWingsReferralResult(
			WingsReferralResultData wingsReferralResultData, Long employerId,
			String employerName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method populateIssueBeanFromWingsReferralResult");
		}
		IssueBean issueBean = new IssueBean();
		issueBean.setEmployerAddressComponentData(wingsReferralResultData
				.getEmployerAddress().getAddressComponentData());
		issueBean.setEmployerName(employerName);
		issueBean.setUpdatedBy(BenefitsConstants.WINGS_ISSUE_UPDATED_BY);
		if (employerId != null) {
			issueBean.setEmployerId(employerId);
		}
		issueBean
				.setClaimid(findClaimForWingsReferralIssue(wingsReferralResultData
						.getSsn()));
		if (WingsReferralResultTypeEnum.HIRD.equals(wingsReferralResultData
				.getResultType())) {
			issueBean.setIssueDescription(IssueCategoryEnum.FUTURE_ISSUE
					.getName());
			issueBean.setIssueDetails(IssueSubCategoryEnum.RETURN_TO_WORK
					.getName());
		} else {
			issueBean.setIssueDescription(IssueCategoryEnum.REFUSAL_OF_WORK
					.getName());
			issueBean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
		}
		issueBean.setDateIssueDetected(new Date());
		issueBean.setIssueStartDate(wingsReferralResultData.getResultDate());
		issueBean.setInformationProvidedBy("MDES");
		issueBean.setInformationProvidedHow("FRMS");
		issueBean
				.setClaimid(findClaimForWingsReferralIssue(wingsReferralResultData
						.getSsn()));
		issueBean.setClaimantSsn(wingsReferralResultData.getSsn());
		// CIF_01361 Commented to match with T_MST_ISSUE_SOURCE
		// issueBean.setIssueSource(IssueSourceEnum.WINGS.getName());
		return issueBean;
	}

	private Long findClaimForWingsReferralIssue(String ssn) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findClaimForWingsReferralIssue");
		}
		List<ClaimData> claimDataList = Claim.fetchClaimBySsn(ssn);
		if (claimDataList != null) {
			return claimDataList.get(0).getClaimId();
		} else {
			return null;
		}
	}

	public IObjectAssembly deleteClaimAppOSOCObject(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method deleteClaimAppOSOCObject");
		}
		List<JobTitleBean> jobTitleBeanListAfterDelete = new ArrayList<JobTitleBean>();

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		ClaimApplicationClaimant clmAppClaimantBO = new ClaimApplicationClaimant(
				clmAppClmntData);

		for (Iterator<JobTitleBean> it = objAssembly.getBeanList(
				JobTitleBean.class).iterator(); it.hasNext();) {
			JobTitleBean jobTitleBean = it.next();
			if (!objAssembly.getData("jobTitleId").toString()
					.equals(jobTitleBean.getJobTitle().toString())) {
				jobTitleBeanListAfterDelete.add(jobTitleBean);
			} else {
				Iterator<ClaimApplicationClaimantOSOCData> claimApplicationClaimantOSOCIterator = clmAppClmntData
						.getClaimApplicationClaimantOSOCDataSet().iterator();

				while (claimApplicationClaimantOSOCIterator.hasNext()) {
					ClaimApplicationClaimantOSOCData claimantOSOCData = claimApplicationClaimantOSOCIterator
							.next();
					if (claimantOSOCData.getOsocTitle().equalsIgnoreCase(
							jobTitleBean.getJobTitle().toString())) {
						claimApplicationClaimantOSOCIterator.remove();
						// remove(claimantOSOCData);
					}
				}
			}
		}

		clmAppClaimantBO.saveOrUpdate();
		clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
		ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		clmAppBO.saveOrUpdate();

		objAssembly.removeBean(JobTitleBean.class);
		objAssembly.addBeanList(jobTitleBeanListAfterDelete, true);
		return objAssembly;
	}

	private IObjectAssembly sendSIDRequest(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendSIDRequest");
		}
		try {
			IconService iconService = new IconService();
			objAssembly = iconService.sendSidRequestThroughBatch(objAssembly);
		} catch (BaseApplicationException e) {
			LOGGER.info("Error occured during the process of sending SID request for ssn "
					+ objAssembly.getSsn() + "." + e.getMessage());
			objAssembly.addBusinessError("error.access.sid.request.not.sent");
		}
		return objAssembly;
	}

	// Start-R4UAT00027212-Activate DUA Claim
	public IObjectAssembly validatingActivationOfDUAClaim(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method validatingActivationOfDUAClaim");
		}
		boolean isParentClaim = false;

		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		DuaApplicationDataBridge duaAppData = (DuaApplicationDataBridge) objAssembly
				.getFirstComponent(DuaApplicationDataBridge.class);
		DuaDeclarationData duaDecData = (DuaDeclarationData) objAssembly
				.getFirstComponent(DuaDeclarationData.class);

		if (clmAppData != null
				&& !ApplicationStatusEnum.PENDING.getName().equalsIgnoreCase(
						clmAppData.getClaimAppStatus())) {
			List<RegularClaimData> regClaimDataList = Claim
					.findActiveRegularClaimListBySsn(objAssembly.getSsn());
			for (RegularClaimData regClaimData : regClaimDataList) {
				// CIF_01486 changed the if condition as per the SIT Defect_7446
				if (regClaimData != null
						&& duaDecData.getStartDate().before(
								regClaimData.getByeDate())) {

					// to handle incomplete data due to migration
					if ((regClaimData.getClaimApplicationData() == null)
							|| (regClaimData.getClaimApplicationData()
									.getClaimApplicationClaimantData() == null)) {
						objAssembly
								.addBusinessError("access.cin.duaselectdisaster.duanotcompleteddata.message");
						return objAssembly;
					}

					ClaimantData clmntData = regClaimData.getClaimantData();
					clmAppData = (ClaimApplicationDataBridge) regClaimData
							.getClaimApplicationData();

					if (clmAppData == null) {
						objAssembly
								.addBusinessError("access.cin.duaselectdisaster.noclaim.message");
						return objAssembly;
					}

					// to find AicReopen claims if exists
					List aicReopenList = Claim
							.findAicReopenByClaimId(regClaimData.getClaimId());
					if ((aicReopenList != null) && (!aicReopenList.isEmpty())) {
						ClaimApplicationData aicClmAppData = ((AicReopenData) aicReopenList
								.get(0)).getClaimApplicationData();
						if (aicClmAppData != null) {// Fix for defect
													// R4UAT00000858
							objAssembly.addData("prevclaimappid",
									clmAppData.getPkId());
							clmAppData = (ClaimApplicationDataBridge) aicClmAppData;
						}
					}

					// duaAppData = clmAppData.getDuaApplicationData();
					duaAppData = (DuaApplicationDataBridge) clmAppData
							.getDuaApplicationData();
					ClaimApplicationClaimantData clmAppClmntData = clmAppData
							.getClaimApplicationClaimantData();
					Claim clm = null;
					// CIF_01486
					// Added this null check, since when we cancel the
					// application we donot have any dua app data saved. So it
					// will throw np exception here.
					// CIF_02419 Start
					/*
					 * if(duaAppData!=null){
					 * clm=Claim.findByClaimApplicationId(duaAppData
					 * .getClaimApplicationData().getPkId()); }else {
					 * clm=Claim.findByClaimApplicationId
					 * (regClaimData.getClaimApplicationData().getPkId()); }
					 */
					clm = Claim.findByClaimApplicationId(regClaimData
							.getClaimApplicationData().getPkId());
					// CIF_02419 End
					// Remove older data from objAssembly and add new one.
					objAssembly.addComponent(clmntData, true);
					objAssembly.addComponent(clmAppClmntData, true);
					objAssembly.addComponent(clm.getClaimData(), true);
					isParentClaim = true;
					break;

				}
			}
		} else if (ApplicationStatusEnum.PENDING.getName().equalsIgnoreCase(
				clmAppData.getClaimAppStatus())) {
			isParentClaim = true;
		} else {
			isParentClaim = false;
		}

		if (isParentClaim) {
			// If DUA exists and status=penidng/completed/expired - ERROR.
			// If DUA doesn't exists proceed further.
			if (duaAppData != null) {

				if (ApplicationStatusEnum.PENDING.getName().equals(
						duaAppData.getStatusFlag())) {
					objAssembly
							.addBusinessError("access.cin.duaselectdisaster.duapending.message");
					return objAssembly;
				} else if (ApplicationStatusEnum.COMPLETED.getName().equals(
						duaAppData.getStatusFlag())) {
					DuaApplication duaAppBO = DuaApplication
							.findDuaAppByClaimAppId(duaAppData.getPkId());
					// duaAppData = duaAppBO.getDuaApplicationData();
					duaAppData = (DuaApplicationDataBridge) duaAppBO
							.getDuaApplicationData();
					ClaimApplicationClaimantData clmData = duaAppData
							.getClaimApplicationData()
							.getClaimApplicationClaimantData();
					objAssembly.addComponent(clmData, true);
					if (DateUtility.isAfterDate(new Date(), duaAppData
							.getDuaDeclarationData().getEndDate())) {
						objAssembly
								.addBusinessError("access.cin.duaselectdisaster.duacompleted.message");
						return objAssembly;
					} else {
						objAssembly
								.addBusinessError("access.cin.duaselectdisaster.duaexpired.message");
						return objAssembly;
					}
				}
			} else {
				// duaAppData = new DuaApplicationData();
				duaAppData = new DuaApplicationDataBridge();
			}

			Object lObject = MultiStateClassFactory
					.getObject(this.getClass().getName(),
							BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

			if (null != lObject) {

				((CinService) lObject).duaApplicatioDataObject(duaAppData);
			}

			/*
			 * ClaimApplicationEmployerWyData claimApplicationClaimantWyData =
			 * new ClaimApplicationEmployerWyData();
			 * 
			 * claimApplicationClaimantWyData
			 * .setClaimApplicationEmployerDataBridge(clmAppEmpData);
			 * clmAppEmpData
			 * .setClaimApplicationEmployerWyData(claimApplicationClaimantWyData
			 * );
			 */
			// Mark DUA application as INITIATED, add DUADecalaration and add
			// ClaimApplication to it.
			duaAppData.setStatusFlag(ApplicationStatusEnum.INITIATED.getName());
			duaAppData.setDuaDeclarationData(duaDecData);
			duaAppData.setClaimApplicationData(clmAppData);
			clmAppData.setDuaApplicationData(duaAppData);

			// Remove older data from objAssembly and add new one.
			objAssembly.addComponent(duaAppData, true);
			objAssembly.addComponent(clmAppData, true);

			// Update DUA Application
			objAssembly = saveOrUpdateDuaApplication(objAssembly);

		} else {
			objAssembly
					.addBusinessError("access.cin.duaselectdisaster.noparent.claim");
			return objAssembly;
		}

		return objAssembly;
	}

	// CIF_00011
	/**
	 * This method takes OjectAssembly and save or update the data into the
	 * database.
	 * 
	 * @throws BaseApplicationException
	 * @throws BaseApplicationException
	 * 
	 * */
	public IObjectAssembly saveOrUpdateSharedWorkEmployerInfo(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSharedWorkEmployerInfo");
		}
		SharedWorkEmployerData sharedWorkEmp = (SharedWorkEmployerData) objAssembly
				.getFirstComponent(SharedWorkEmployerData.class);
		RegisteredEmployerData regEmpData = objAssembly
				.getFirstComponent(RegisteredEmployerData.class);
		SharedWorkEmployer employer = new SharedWorkEmployer(sharedWorkEmp);
		employer.saveOrUpdate();
		/*
		 * Boolean sendCorrespondence = (Boolean)
		 * objAssembly.getData("sendCorrespondence"); if(null !=
		 * sendCorrespondence && sendCorrespondence){ CorrespondenceData
		 * corrData=objAssembly.getFirstComponent(CorrespondenceData.class);
		 * Correspondence correspondenceBO = new Correspondence(corrData);
		 * correspondenceBO.saveOrUpdate();
		 * objAssembly.removeComponent(CorrespondenceData.class);
		 * objAssembly.addData("sendCorrespondence", Boolean.FALSE); }
		 */
		Boolean initiateWorkFlow = (Boolean) objAssembly
				.getData("InitiateWorkFLow");
		// Initiating WorkFlow
		if (null != initiateWorkFlow && initiateWorkFlow) {
			// CIF_03270 starts
			// generate SW1 and SW-2 correspondence
			CorrespondenceData corrSw1Data = new CorrespondenceData();
			corrSw1Data.setEmployerData(regEmpData);
			corrSw1Data.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_SW_1
					.getName());
			corrSw1Data.setParameter1(sharedWorkEmp.getAffectedUnit());
			corrSw1Data.setParameter2(sharedWorkEmp.getEmployerData().getEan());
			// CIF_INT_01706
			corrSw1Data.setParameter3(sharedWorkEmp.getTotalWorkers()
					.toString());
			corrSw1Data.setParameter3Desc(sharedWorkEmp.getAffectedWorkers()
					.toString());
			corrSw1Data.setParameter4(sharedWorkEmp.getPlanReduce().toString());
			corrSw1Data.setParameter4Desc("NAME".equals(sharedWorkEmp
					.getReceivedListBy()) ? ViewConstants.NO_DESCRIPTION_LC
					: ViewConstants.YES_DESCRIPTION_LC);
			corrSw1Data.setParameter5(ViewConstants.YES.equals(sharedWorkEmp
					.getFringBenetits()) ? ViewConstants.YES_DESCRIPTION_LC
					: ViewConstants.NO_DESCRIPTION_LC);
			Correspondence corr = new Correspondence(corrSw1Data);
			corr.saveOrUpdate();
			// CorrespondenceData corrSw2Data = new CorrespondenceData();
			// corrSw2Data.setEmployerData(regEmpData);
			// corrSw2Data.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_SW_2.getName());
			// CIF_03403
			// corrSw2Data.setParameter1(sharedWorkEmp.getAffectedUnit());
			// corrSw2Data.setParameter2(sharedWorkEmp.getPkId().toString());
			// corrSw2Data.setParameter7(sharedWorkEmp.getPlanNumber());
			// corrSw2Data.setParameter4(regEmpData.getEmployerName());
			// corrSw2Data.setParameter5(sharedWorkEmp.getAffectedWorkers().toString());
			// corr = new Correspondence(corrSw2Data);
			// corr.saveOrUpdate();
			Map<String, Object> mapValues = new HashMap<String, Object>();
			mapValues.put(WorkflowConstants.TYPE,
					WorkflowConstants.BUSINESS_TYPE.EAN);
			mapValues.put(WorkflowConstants.SSN_EAN_FEIN, regEmpData.getEan());
			mapValues.put(WorkflowConstants.NAME, regEmpData.getEmployerName());
			mapValues.put(WorkflowConstants.BUSINESS_KEY, sharedWorkEmp
					.getPkId().toString());
			Set set = regEmpData.getEmployerContactData();
			for (Object object : set) {
				EmployerContactData empcontactData = (EmployerContactData) object;
				mapValues.put(WorkflowConstants.ZIP_CODE,
						empcontactData.getZip());
				break;
			}
			mapValues
					.put(WorkflowConstants.PROCESS_NAME,
							WorkflowProcessTemplateConstants.Claims.SHARED_WORK_PLAN_REVIEW);
			WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
			wfTransactionService
					.invokeWorkFlowOperation(
							WorkFlowOperationsEnum.CREATE_WORKITEM.getName(),
							mapValues);
			// CIF_03270 ends

		}
		return objAssembly;

	}

	// End-R4UAT00027212-Activate DUA Claim
	public IObjectAssembly processApprissResponseData(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processApprissResponseData");
		}
		IssueBean issueBean = new IssueBean();
		Long incarcerationDetailId = objAssembly.getPrimaryKeyAsLong();
		Date fromDate = (Date) objAssembly.getData("strFromDate");
		Date toDate = (Date) objAssembly.getData("strToDate");
		LOGGER.debug("incarcerationDetailId : " + incarcerationDetailId);
		LOGGER.debug("fromDate : " + fromDate);
		LOGGER.debug("toDate : " + toDate);
		ApprissResponse apprissResponse = ApprissResponse
				.findByPrimaryKey(incarcerationDetailId);
		IClaimDAO claimDAO = DAOFactory.instance.getClaimDAO();
		ClaimData presentActiveClaimData = claimDAO
				.findAnyTypeLatestActiveClaimForClaimant(apprissResponse
						.getApprissResponseData().getClaimantData().getPkId());
		List<IssueData> listIssueData = Issue
				.findByClaimID(presentActiveClaimData.getClaimId());
		boolean isRequiredToCreateIssue = true;
		IProcessedWeeklyCertDAO objProcessedWeeklyCertDAO = DAOFactory.instance
				.getProcessedWeeklyCertDAO();
		Date paidStartDate = null;
		Date paidEndDate = null;
		/*
		 * If we have passed paidStartDate and paidEndDate then it will consider
		 * those dates otherwise last month first day as paidStartDate and last
		 * month last day as paidEndDate
		 */
		if (fromDate != null && toDate != null) {
			paidStartDate = fromDate;
			paidEndDate = toDate;
		} else {
			paidStartDate = DateUtility.getFirstDayOfPreviousMonth(new Date());
			paidEndDate = DateUtility.getLastDayOfPreviousMonth(new Date());
		}
		LOGGER.debug("paidStartDate : " + paidStartDate);
		LOGGER.debug("paidEndDate : " + paidEndDate);
		List<ProcessedWeeklyCertData> listProcessedWeeklyCertData = objProcessedWeeklyCertDAO
				.getPaidCWEsForDataRange(apprissResponse
						.getApprissResponseData().getClaimantData().getSsn(),
						paidStartDate, paidEndDate);
		List<Date> listAlreadyIssueCreatedCWEs = new ArrayList<Date>();
		if (listProcessedWeeklyCertData != null
				&& listProcessedWeeklyCertData.size() > 0) {
			for (int i = 0; i < listProcessedWeeklyCertData.size(); i++) {
				Date cwe = listProcessedWeeklyCertData.get(i)
						.getWeeklyCertData().getClaimWeekEndDate();
				LOGGER.debug("cwe : " + cwe);
				LOGGER.debug("BookDate is between CWE week : "
						+ DateUtility.isBetweenDates(apprissResponse
								.getApprissResponseData().getDateBookDate(),
								DateUtility.addCalendarDays(cwe, -6), cwe));
				if (DateUtility.isBetweenDates(apprissResponse
						.getApprissResponseData().getDateBookDate(),
						DateUtility.addCalendarDays(cwe, -6), cwe)) {
					boolean isReleaseDateGreaterThan2DaysFromBookDate = apprissResponse
							.isReleaseDateGreaterThan2DaysFromBookDate(
									apprissResponse.getApprissResponseData()
											.getDateBookDate(), apprissResponse
											.getApprissResponseData()
											.getDateReleaseDate());
					boolean isAlreadyIssueCreatedByThisCWEs = apprissResponse
							.isAlreadyIssueCreatedByThisCWEs(
									listAlreadyIssueCreatedCWEs, cwe);
					boolean checkIfDuplicateHitForIncar = apprissResponse
							.checkIfDuplicateHitForIncar(apprissResponse
									.getApprissResponseData(), apprissResponse
									.getApprissResponseData().getClaimantData());
					/*
					 * boolean isIssueAlreadyExist = false;
					 * if(checkIfDuplicateHitForIncar) { isIssueAlreadyExist =
					 * apprissResponse
					 * .isIncarcerationIssueAlreadyExists(DateUtility
					 * .addCalendarDays(cwe, -6),listIssueData); }
					 * LOGGER.debug("isIssueAlreadyExist : " +
					 * isIssueAlreadyExist);
					 */
					LOGGER.debug("isReleaseDateGreaterThan2DaysFromBookDate : "
							+ isReleaseDateGreaterThan2DaysFromBookDate);
					LOGGER.debug("isAlreadyIssueCreatedByThisCWEs : "
							+ isAlreadyIssueCreatedByThisCWEs);
					LOGGER.debug("checkIfDuplicateHitForIncar : "
							+ checkIfDuplicateHitForIncar);
					if (isReleaseDateGreaterThan2DaysFromBookDate
							&& !isAlreadyIssueCreatedByThisCWEs
							&& !checkIfDuplicateHitForIncar) {
						LOGGER.debug("Inside the issue creation");
						StringBuffer sb = new StringBuffer();
						issueBean = apprissResponse
								.populateIssueBeanFromApprissResponseData(apprissResponse
										.getApprissResponseData());
						issueBean
								.setAdditionalInformation(apprissResponse
										.buildIncarcerationAdditionalInformation(apprissResponse
												.getApprissResponseData()));
						IssueData issueData = Issue
								.createIssue(issueBean, true);
						listAlreadyIssueCreatedCWEs.add(cwe);
						LOGGER.debug("issue has been created for this incarcerationDetailId: "
								+ incarcerationDetailId);
						IJdbcQuestionnaireDAO jdbcQDAO = DAOFactory.instance
								.getJdbcQuestionnaireDAO();
						jdbcQDAO.insertQeIncarcerationDataFromIssue(issueData,
								apprissResponse.getApprissResponseData()
										.getDateBookDate());
						LOGGER.debug("insertQeIncarcerationData has been created for this incarcerationDetailId: "
								+ incarcerationDetailId);
					}
				}
			}
		}
		apprissResponse.getApprissResponseData().setIsProcessed(
				GlobalConstants.DB_ANSWER_YES);
		apprissResponse.saveOrUpdate();
		LOGGER.debug("processed flag had been updated and return: incarcerationDetailId :"
				+ incarcerationDetailId);
		return objAssembly;
	}

	// changes for CIF_00033 starts
	/**
	 * @param ObjectAssemble
	 * @return Object assembly method adds the data to the claimant table
	 * @throws BaseApplicationException
	 */
	public IObjectAssembly saveOrUpdateClaimantData(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateClaimantData");
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		List<ClaimApplicationClaimantDataBridge> lclaimantData = (List<ClaimApplicationClaimantDataBridge>) objAssembly
				.getData("lclaimApplicationData");
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")
		MassLayoffData massLayoffData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);
		objAssembly.removeBusinessError();

		/*
		 * for (ClaimApplicationClaimantData claimApplicationClaimantData :
		 * lclaimantData) {
		 * objAssembly.setSsn(claimApplicationClaimantData.getSsn());
		 * this.getOrCreateClaimApp(objAssembly); }
		 * if(objAssembly.hasBusinessError()){ return objAssembly; }
		 */

		// ICinDAO cinDao = new CinDAOImpl();
		MassLayoff massLayoffBO = new MassLayoff(massLayoffData);
		massLayoffBO.saveOrUpdateMassLayoffsData();
		// CIF_02910 adding unsaved data for masslayoff
		for (ClaimApplicationClaimantDataBridge claimApplicationClaimantData : lclaimantData) {
			// massLayoffBO.saveOrUpdateReasonForLayoffsData(claimApplicationClaimantData);
			// CIF_02957 || Jira : UIM-4138
			objAssembly.removeAll();
			objAssembly.setSsn(claimApplicationClaimantData.getSsn());
			// objAssembly.removeComponent(ClaimApplicationClaimantData.class);
			// objAssembly.removeComponent(ClaimApplicationData.class);
			objAssembly = this.getOrCreateClaimApp(objAssembly);
			// ClaimApplicationData clmAppData =
			// objAssembly.getFirstComponent(ClaimApplicationData.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
			ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="Start")
			ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="End")
			// to set Claim Application data
			clmAppData.setSsn(objAssembly.getSsn());
			// to set Claim Application Claimant data
			clmAppClmntData.setSsn(objAssembly.getSsn());
			clmAppClmntData.setFirstName(claimApplicationClaimantData
					.getFirstName());
			clmAppClmntData.setLastName(claimApplicationClaimantData
					.getLastName());
			clmAppClmntData.setDateOfBirth(claimApplicationClaimantData
					.getDateOfBirth());
			clmAppClmntData.setGender(claimApplicationClaimantData.getGender());
			clmAppClmntData.setCitizenship(claimApplicationClaimantData
					.getCitizenship());
			clmAppClmntData.setMailAddress(claimApplicationClaimantData
					.getMailAddress());
			clmAppClmntData.setPrimaryPhone(claimApplicationClaimantData
					.getPrimaryPhone());
			clmAppClmntData.setMiddleInitial(claimApplicationClaimantData
					.getMiddleInitial());
			clmAppClmntData.setSuffix(claimApplicationClaimantData.getSuffix());
			clmAppClmntData.setSsaStatus(SsaStatusEnum.V.getName());
			clmAppClmntData.setResAddress(claimApplicationClaimantData
					.getResAddress());
			clmAppClmntData.setFirstNameSsCard(claimApplicationClaimantData
					.getFirstName());
			clmAppClmntData.setLastNameSsCard(claimApplicationClaimantData
					.getLastName());
			clmAppClmntData.setAlienDocType(claimApplicationClaimantData
					.getAlienDocType());
			clmAppClmntData.setAlienNumber(claimApplicationClaimantData
					.getAlienNumber());
			clmAppClmntData.setAlienDocExpDate(claimApplicationClaimantData
					.getAlienDocExpDate());
			// objAssembly.addComponent(clmAppData, true);
			// objAssembly.addComponent(claimApplicationClaimantData,true);
			this.saveOrUpdateFileMassLayoffsInfo(objAssembly);

		}
		return objAssembly;

	}

	// CIF_02957 || Jira : UIM-4138
	public IObjectAssembly validateAndSaveLayoffData(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method ValidateDateForClaimant");
		}
		MassLayoffData massLayoffData = (MassLayoffData) objAssembly
				.getFirstComponent(MassLayoffData.class);
		Set<SsnForMassLayoffsData> ssnForMassLayoffsDataSet = massLayoffData
				.getSsnDataSet();
		/*
		 * Set<String> pendingClaims = new HashSet<String>(); Set<String>
		 * establishedClaims = new HashSet<String>(); for (SsnForMassLayoffsData
		 * ssnForMassLayoffsData : ssnForMassLayoffsDataSet) { List<ClaimData>
		 * claimDataList =
		 * Claim.fetchClaimBySsn(ssnForMassLayoffsData.getSsn()); if(null !=
		 * claimDataList && !claimDataList.isEmpty()){ // fetch only first
		 * element as claims are ordered by bye date and active
		 * if(claimDataList.
		 * get(0).getByeDate().after(massLayoffData.getMassLayoffClaimEffDate())
		 * && EntitlementTypeEnum.REGULAR.getName().equals(claimDataList.get(0).
		 * getEntitlementType()) ){
		 * establishedClaims.add(ssnForMassLayoffsData.getSsn()); } }
		 * if(!establishedClaims.contains(ssnForMassLayoffsData.getSsn())){
		 * if(!this.createClaimApplication(ssnForMassLayoffsData.getSsn(),
		 * objAssembly, massLayoffData.getMassLayoffClaimEffDate())){
		 * pendingClaims.add(ssnForMassLayoffsData.getSsn()); } } }
		 * if(establishedClaims.isEmpty() && pendingClaims.isEmpty()){
		 */
		// save data in T_MASS_LAYOFF_EMPLOYEE
		MassLayoff masslayoff = new MassLayoff(massLayoffData);
		masslayoff.saveOrUpdateMassLayoffsData();
		SsnForMassLayoffs ssnLayoffs = new SsnForMassLayoffs(null);
		ssnLayoffs.saveOrUpdateList(massLayoffData.getSsnDataSet());
		/*
		 * }else{ if(!establishedClaims.isEmpty()){
		 * objAssembly.addBusinessError(
		 * "error.access.getorcreateclaimapp.zerooroneweektobefilled.massLayOff"
		 * , establishedClaims.toArray()); } if(!pendingClaims.isEmpty()){
		 * objAssembly.addBusinessError(
		 * "error.access.getorcreateclaimapp.pendingclaimexists.massLayOff",
		 * pendingClaims.toArray()); } }
		 */
		// MassLayoffData massLayoffData = (MassLayoffData)
		// objAssembly.getFirstComponent(MassLayoffData.class);
		/*
		 * if(objAssembly.hasBusinessError()){ return objAssembly; } for
		 * (ClaimApplicationClaimantData claimApplicationClaimantData :
		 * lclaimantData) {
		 * objAssembly.setSsn(claimApplicationClaimantData.getSsn());
		 * this.getOrCreateClaimApp(objAssembly);
		 * objAssembly.removeComponent(ClaimApplicationClaimantData.class);
		 * objAssembly.removeComponent(ClaimApplicationData.class); }
		 */
		return objAssembly;
	}

	public IObjectAssembly getDataForMassLayOff(IObjectAssembly objectAssemply) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getDataForMassLayOff");
		}
		MassLayoff layoffData = MassLayoff
				.findByPrimaryKey((Long) objectAssemply
						.getPrimaryKey(MassLayoffData.class));
		MassLayoffData data = layoffData.getMassLayoffData();
		if (null != data) {
			objectAssemply.addComponent(data);
		}
		return objectAssemply;

	}

	/*
	 * // CIF_00061 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#updateClaimantAndClaimApplication
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Update the
	 * Claimant and Claim Application
	 */
	public IObjectAssembly updateClaimantAndClaimApplication(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateClaimantAndClaimApplication");
		}
		this.saveOrUpdateClmAppClmnt(objAssembly);
		ClaimantData data = (ClaimantDataBridge) objAssembly
				.getFirstComponent(ClaimantDataBridge.class);

		/*
		 * payment defect_41 If flow is coming from Renew claim then no need to
		 * update the claimant ssn last four *
		 */
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		if (data != null
				&& data.getSsn() == null
				&& clmAppData.getClaimAppType().equals(
						ClaimApplicationTypeEnum.AIC.getName())) {
			return objAssembly;
		}

		Claimant clmAppBO = new Claimant(data);
		clmAppBO.saveOrUpdate();
		// clmAppBO.flush();
		return objAssembly;
	}

	/*
	 * // CIF_00061 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#getClaimantBySsn
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Retrieve the
	 * Claimant by SSN.
	 */
	public IObjectAssembly getClaimantBySsn(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimantBySsn");
		}
		if (objAssembly.getSsn() != null && !objAssembly.getSsn().isEmpty()) {
			Claimant claimant = Claimant.findBySsn(objAssembly.getSsn());
			objAssembly.addComponent(claimant.getClaimantData());
		}
		return objAssembly;
	}

	/*
	 * // CIF_00060 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#getMasterBankDetails
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Function To
	 * Retrieve the Master Bank Details for that State
	 */
	public IObjectAssembly getMasterBankDetails(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getMasterBankDetails");
		}

		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="Start")
		ClaimApplicationClaimantDataBridge claimant = objAssembly
				.getFirstComponent(ClaimApplicationClaimantDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
		// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
		// impactPoint="End")
		if (claimant.getBankRoutingNumber() != null) {
			String routingNr = claimant.getBankRoutingNumber();
			MstBankDetailsData masterBankDetails = MstBankDetails
					.findByRoutingNumber(routingNr);
			if (masterBankDetails != null) {
				objAssembly.addComponent(masterBankDetails, true);
			}
		}
		return objAssembly;
	}

	/*
	 * // CIF_00046 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#getExtendedRecallDateData
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Function To
	 * Retrieve the Extended Recall Date for the ssn.
	 */
	public IObjectAssembly getExtendedRecallDateData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getExtendedRecallDateData");
		}
		/*
		 * if(objAssembly.getSsn()!=null){ ExtendedRecallDate
		 * extendedRecallDate=
		 * ExtendedRecallDate.findBySsn(objAssembly.getSsn());
		 * if(extendedRecallDate!=null) {
		 * objAssembly.addComponent(extendedRecallDate
		 * .getExtendedRecallDateData()); } }
		 */
		ClaimWorkSearchContact clmWorkSearchContact = new ClaimWorkSearchContact(
				null);
		List<ClaimWorkSearchContactData> clmWorkSearchContactDataList = clmWorkSearchContact
				.findWorkSearchBySsn(objAssembly.getSsn(),
						WorkSearchReasonEnum.EMPLOYER_RECALL.getName());
		if (null != clmWorkSearchContactDataList
				&& !clmWorkSearchContactDataList.isEmpty()) {
			Date currDate = new Date();
			for (ClaimWorkSearchContactData claimWorkSearchContactData : clmWorkSearchContactDataList) {
				if (currDate.after(claimWorkSearchContactData
						.getContactFromDate())
						&& currDate.before(claimWorkSearchContactData
								.getContactToDate())) {
					objAssembly.addData("recalldate",
							claimWorkSearchContactData.getContactToDate());
					break;
				}
			}
		}
		return objAssembly;
	}

	// CIF_00058
	/**
	 * Overrides @see
	 * gov.state.uim.cin.service.BICinService#getSidiIbiqResponse(
	 * gov.state.uim.framework.service.IObjectAssembly)
	 * 
	 * @param IObjectAssembly
	 *            objAssembly TODO Getting IBIQ Response For an SSN.
	 * @return IObjectAssembly IObjectAssembly
	 */
	public IObjectAssembly getSidiIbiqResponse(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSidiIbiqResponse");
		}
		// CIF_INT_03291 this is added so as we don't send multile request on a
		// single day
		// check if this is called for DetermineStateClaimTypeAction if yes then
		// get the response for IBIQ from DB else request have currently been
		// sent so wait for the response to come back
		Boolean getDbResultsOnly = (Boolean) objAssembly
				.getData("getDbResultsOnly");
		if (null != getDbResultsOnly && getDbResultsOnly) {
			objAssembly.addData("getDbResultsOnly", null);
			List<IbiqRequestData> dbList = IbiqRequestBO
					.findIbiqRequestListBySsnAndDate(objAssembly.getSsn());
			// get Response from Request
			Long prevRequestId = 0L;
			if (null != dbList && !dbList.isEmpty()) {
				List ibiqResponseDataList = new ArrayList();
				for (IbiqRequestData requestData : dbList) {
					if (null != requestData.getIbiqResponseData()
							&& prevRequestId.longValue() != requestData
									.getIbiqRequestId().longValue()) {
						prevRequestId = requestData.getIbiqRequestId();
						ibiqResponseDataList.add(requestData
								.getIbiqResponseData());
					}
				}
				objAssembly.addComponentList(ibiqResponseDataList, true);
			}
		} else {
			final IconService iconService = new IconService();
			objAssembly = iconService.getIbiqResponse(objAssembly);
		}
		return objAssembly;
	}

	// CIF_INT_03291 added to send request to ICON
	public IObjectAssembly sendIbiqRequestToIcon(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendIbiqRequestToIcon");
		}
		final IconService iconService = new IconService();
		List<IbiqResponseData> toAddList = objAssembly
				.getComponentList(IbiqRequestData.class);
		if (null != toAddList && !toAddList.isEmpty()) {
			objAssembly = iconService.sendIbiqRequestToIcon(objAssembly);
		}
		return objAssembly;
	}

	/**
	 * @param objAssembly
	 */
	public IObjectAssembly sendSidiIbiqRequest(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendSidiIbiqRequest");
		}
		final IconService iconService = new IconService();
		objAssembly = iconService.sendSidiIbiqRequest(objAssembly);
		return objAssembly;
	}

	/*
	 * // CIF_00057 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#getMstHandbookData
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Adding Function To
	 * Retrieve the State Wage Limits for the InterStates
	 */
	public IObjectAssembly getMstHandbookData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getMstHandbookData");
		}
		List claimantPeriodWageBeanList = objAssembly
				.getBeanList(ClaimantPeriodWageBean.class);
		// if (claimantPeriodWageBeanList != null) {
		List stateArrayList = new ArrayList();

		// //CIF_00972||Updating the fetch of the data from master table.
		// for (Object wage : claimantPeriodWageBeanList) {
		// ClaimantPeriodWageBean bean = (ClaimantPeriodWageBean) wage;
		// if(bean.getState()!=null){
		// //CIF_00227
		// //Collection State Codes Instead of State Description.
		// stateArrayList.add(bean.getStateCd());
		// }
		// }
		// CIF_00972
		Set ibiqStates = null != objAssembly.getData("IBIQSTATES") ? (Set) objAssembly
				.getData("IBIQSTATES") : new HashSet();
		if (!ibiqStates.isEmpty()) {
			// CIF_02934 || commented for None Option
			/*
			 * if(!ibiqStates.contains(ViewConstants.HOME_STATE)){
			 * ibiqStates.add(ViewConstants.HOME_STATE); }
			 */
			Object[] stateArray = new Object[ibiqStates.size()];
			stateArray = ibiqStates.toArray();
			List wagesLimit = MstHandBook
					.getStateWagesLimitDataByPrimaryKey(stateArray);
			if (wagesLimit != null) {
				objAssembly.addComponentList(wagesLimit);
			}
		}
		// }
		return objAssembly;
	}

	// CIF_00287
	/**
	 * This method get the shared work information and EMployer information for
	 * a shared_work_plan_id
	 * 
	 * @return ObjectAssembly containings two objects SharedWorkEmployerData and
	 *         EmployerData
	 */
	public IObjectAssembly getEmployerInformation(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		SharedWorkEmployerData sgaredData = (SharedWorkEmployerData) sharedEmployer
				.findByPrimaryKey(SharedWorkEmployerData.class,
						objAssembly.getPrimaryKeyAsLong());
		if (null != sgaredData) {
			// objAssembly.addComponent(sgaredData);
			objAssembly.setEan(sgaredData.getEan());

			// this.getSharedEmployerData(objAssembly);

			// objAssembly.removeComponent(EmployerData.class);
			// Employer employer = null;
			// employer = Employer.getEmployerByEan(objAssembly.getEan());
			// objAssembly.addComponent(employer.getEmployerData());
		}
		return objAssembly;
	}

	// CIF_00167 Start
	// SearchSharedEmployerData
	public IObjectAssembly getSharedEmployerData(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedEmployerData");
		}
		objAssembly.removeComponent(EmployerData.class);
		Employer employer = null;
		employer = Employer.getEmployerByEan(objAssembly.getEan());
		// The following statement is executed to load lazy properties.
		String employerName = employer.getEmployerData().getDisplayName();
		LOGGER.debug(employerName);

		if (employer != null) {
			objAssembly.addComponent(employer.getEmployerData(), true);
			SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
			SharedWorkEmployerData sgaredData = (SharedWorkEmployerData) sharedEmployer
					.findByPrimaryKey(SharedWorkEmployerData.class,
							objAssembly.getPrimaryKeyAsLong());
			// sharedEmployer =
			// sharedEmployer.findByPrimaryKey(SharedWorkEmployerData.class,objAssembly.getPrimaryKeyAsLong());
			if (null != sgaredData) {
				objAssembly.addComponent(sgaredData, true);
			}
		}

		return objAssembly;
	}

	// CIF_00167 End

	// CIF_239

	// CIF_00276
	/**
	 * TODO This methods gets the plan details for a shared Plan
	 */
	@Override
	public IObjectAssembly getSharedPlanData(IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedPlanData");
		}
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		SharedWorkEmployerData sharedData = (SharedWorkEmployerData) sharedEmployer
				.findByPrimaryKey(SharedWorkEmployerData.class,
						objectAssembly.getPrimaryKeyAsLong());
		objectAssembly.addComponent(sharedData, true);
		return objectAssembly;
	}

	// CIF_00077
	public IObjectAssembly loadCwes(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method loadCwes");
		}
		// CIF_03387 Start
		String paymentDataType = (String) objAssembly
				.getData("PaymentDataType");
		if (GlobalConstants.TYPE_CLAIMANT_PAYMENT
				.equalsIgnoreCase(paymentDataType)) {
			WeeklyCertificationValidationDataBean objWeeklyCertificationValidationData = (WeeklyCertificationValidationDataBean) objAssembly
					.getFirstBean(WeeklyCertificationValidationDataBean.class);

			if (objWeeklyCertificationValidationData != null) {

				ClaimApplication claimApp = ClaimApplication
						.findClaimAppByPkId(objWeeklyCertificationValidationData
								.getClaimData().getClaimApplicationData()
								.getPkId());
				objAssembly.addComponent(claimApp.getClaimApplicationData(),
						true);
				objAssembly.addComponent(claimApp.getClaimApplicationData()
						.getClaimApplicationClaimantData(), true);

			}
			// CIF_00105|| Payment Defect FIX
			String claimWeekEnding = "";
			IClaimantDAO claimantDao = DAOFactory.instance.getClaimantDAO();
			ClaimantData claimantData = claimantDao
					.findClaimantDetailsBySSN(objAssembly.getSsn());
			Long selectedClaimantPaymentId = (Long) objAssembly.getPrimaryKey();

			IClaimantPaymentDAO claimantPaymentDao = DAOFactory.instance
					.getClaimantPaymentDAO();
			List<ClaimantPaymentData> claimantPatDataList = claimantPaymentDao
					.getAllClaimantPaymentDataByClaimantId(claimantData
							.getPkId());

			for (ClaimantPaymentData item : claimantPatDataList) {
				Iterator itr = item.getProcessedWeeklyCertDataSet().iterator();
				while (itr.hasNext()) {
					ProcessedWeeklyCertData ppData = (ProcessedWeeklyCertData) itr
							.next();
					if (ppData.getWeeklyCertData().getClaimWeekEndDate() != null
							&& selectedClaimantPaymentId != null) {
						if (ppData.getClaimantPaymentData()
								.getClaimantPaymentId()
								.equals(selectedClaimantPaymentId)) {
							if (claimWeekEnding.equals("")) {
								claimWeekEnding = DateFormatUtility.format(
										ppData.getWeeklyCertData()
												.getClaimWeekEndDate(),
										"MM/dd/yyyy");
							} else {
								claimWeekEnding = claimWeekEnding
										+ ", "
										+ DateFormatUtility.format(ppData
												.getWeeklyCertData()
												.getClaimWeekEndDate(),
												"MM/dd/yyyy");
							}
						}

					}
				}
			}

			objAssembly.addData("WeekEndingDATA", claimWeekEnding);
		}
		// CIF_03387 End
		return objAssembly;

	}

	public IObjectAssembly getSharedEmployeeData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedEmployeeData");
		}
		String pkId = (String) objAssembly.getData("pkId");
		String remove = (String) objAssembly.getData("remove");
		objAssembly.removeBusinessError();
		if (StringUtility.isBlank(pkId) || !StringUtility.isNumeric(pkId)) {
			if (StringUtility.isNotBlank(remove)) {
				objAssembly
						.addBusinessError("error.access.sharedWorkPlan.Profile.remove.SelectEmployee");
			} else {
				objAssembly
						.addBusinessError("error.access.sharedWorkPlan.Profile.SelectEmployee");
			}
			return objAssembly;
		}
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		SharedWorkEmployeeData sharedData = (SharedWorkEmployeeData) sharedEmployer
				.findByPrimaryKey(SharedWorkEmployeeData.class,
						Long.valueOf(pkId));
		// CIF_02936 added for validation
		if (StringUtility.isNotBlank(remove)
				&& ViewConstants.YES
						.equals(sharedData.getEmployeeRemovedFlag())) {
			objAssembly
					.addBusinessError("error.access.sharedWorkPlan.Profile.remove.alreadyremoved");
		}
		objAssembly.addComponent(sharedData, true);
		return objAssembly;
	}

	public IObjectAssembly getSharedWorkEmployer(IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedWorkEmployer");
		}
		String pkId = (String) objectAssembly.getData("pkId");
		objectAssembly.removeBusinessError();
		if (StringUtility.isBlank(pkId) || !StringUtility.isNumeric(pkId)) {
			objectAssembly.addBusinessError("error.required.select.radio");
			return objectAssembly;
		}
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		SharedWorkEmployerData sgaredData = (SharedWorkEmployerData) sharedEmployer
				.findByPrimaryKey(SharedWorkEmployerData.class,
						Long.valueOf(pkId));
		objectAssembly.addComponent(sgaredData, true);
		return objectAssembly;
	}

	/**
	 * this method saves employee information into the database
	 * 
	 * @throws BaseApplicationException
	 * 
	 */
	// CIF_01737 starts : Methods modified as per Jira 2063
	public IObjectAssembly saveOrUpdateSWPEmployeeUpload(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSWPEmployeeUpload");
		}
		if (objAssembly.hasBusinessError()) {
			return objAssembly;
		}
		List<SharedWorkEmployeeData> lsharedEmployeeData = (List<SharedWorkEmployeeData>) objAssembly
				.getData("SHARED_EMPLOYEE");
		int size = lsharedEmployeeData.size();
		// Getting The SharedWorkEmployerData based on primary key
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		sharedEmployer = sharedEmployer.getSharedEmployerDataByPlanId(Long
				.valueOf(objAssembly.getPrimaryKey().toString()));
		objAssembly.addData("SHARED_EMPLOYER", sharedEmployer);
		objAssembly.removeBusinessError();
		for (int i = 0; i < size; i++) {
			objAssembly.addComponent(lsharedEmployeeData.get(i), true);
			// objAssembly.addComponent(lclaimApplicationData.get(i),true);
			objAssembly = validateEmployee(objAssembly);
		}
		if (objAssembly.hasBusinessError()) {
			return objAssembly;
		}
		for (int i = 0; i < size; i++) {
			objAssembly.addComponent(lsharedEmployeeData.get(i), true);
			// objAssembly.addComponent(lclaimApplicationData.get(i),true);
			objAssembly = updateEmployeeList(objAssembly);
		}
		return objAssembly;
	}

	private IObjectAssembly validateEmployee(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method validateEmployee");
		}

		// ClaimApplicationClaimantData clmAppClmntData =
		// (ClaimApplicationClaimantData)
		// objAssembly.fetchORCreate(ClaimApplicationClaimantData.class);
		SharedWorkEmployeeData sharedWorkEmployeeData = (SharedWorkEmployeeData) objAssembly
				.fetchORCreate(SharedWorkEmployeeData.class);

		/*
		 * try{ ClaimApplicationData claimAppBO =
		 * ClaimApplication.getPendingOrIncompleteClaimApp
		 * (clmAppClmntData.getSsn()); if(claimAppBO !=null) { throw new
		 * BaseApplicationException
		 * ("error.access.getorcreateclaimapp.pendingclaimexists",new
		 * Object[]{clmAppClmntData.getSsn()}); } } catch
		 * (BaseApplicationException bae) {
		 * objAssembly.addBusinessError(bae.getMessage()); return objAssembly; }
		 */

		// Getting The SharedWorkEmployerData based on primary key
		SharedWorkEmployer sharedEmployer = (SharedWorkEmployer) objAssembly
				.getData("SHARED_EMPLOYER");
		sharedWorkEmployeeData.setSharedWorkEmployerData(sharedEmployer
				.getSharedWorkEmployerData());

		SharedWorkEmployee shaedBo = new SharedWorkEmployee();
		// getting the list of employees which are part of shared plan
		List<SharedWorkEmployeeData> sharedEmployeeList = shaedBo
				.getSharedEmployeeDataBySsn(sharedWorkEmployeeData.getSsn());

		if (sharedEmployeeList != null && sharedEmployeeList.size() > 0) {
			for (SharedWorkEmployeeData employeeData : sharedEmployeeList) {
				try {
					// If an SSN entry exists with same shared plan id, then the
					// amployee is not allowed to add
					if (null != employeeData
							&& employeeData
									.getSharedWorkEmployerData()
									.getPkId()
									.equals(sharedWorkEmployeeData
											.getSharedWorkEmployerData()
											.getPkId())) {
						// objAssembly.addBusinessError("error.access.getorcreateclaimapp.employeepartofsharedplan");
						throw new BaseApplicationException(
								"error.access.getorcreateclaimapp.employeepartofsharedplan.bulkUpload");
					}// If an SSN entry exists with different shared plan id and
						// is active, then the amployee is not allowed to add
					/*
					 * else if(null != employeeData && null !=
					 * employeeData.getEmployeeRemovedFlag() &&
					 * Integer.valueOf(employeeData.getEmployeeRemovedFlag()) ==
					 * Integer.valueOf(ViewConstants.NO)){
					 * //objAssembly.addBusinessError
					 * ("error.access.getorcreateclaimapp.employeepartofsharedplan"
					 * ); throw new BaseApplicationException(
					 * "error.access.getorcreateclaimapp.employeepartofsharedplan.bulkUpload"
					 * ); }
					 */
				} catch (BaseApplicationException bae) {
					objAssembly.addBusinessError(bae.getMessage(),
							new Object[] { sharedWorkEmployeeData.getSsn() });
					return objAssembly;
				}
			}
		}

		return objAssembly;
	}

	// CIF_00236 START
	// Shared WORK PLAN - Add/UPDATE Employee Information
	private IObjectAssembly updateEmployeeList(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateEmployeeList");
		}
		// ClaimApplicationClaimantData clmAppClmntData =
		// (ClaimApplicationClaimantData)
		// objAssembly.fetchORCreate(ClaimApplicationClaimantData.class);
		SharedWorkEmployeeData sharedWorkEmployeeData = (SharedWorkEmployeeData) objAssembly
				.fetchORCreate(SharedWorkEmployeeData.class);
		// ClaimApplicationData claimAppBO =
		// ClaimApplication.getPendingOrIncompleteClaimApp(clmAppClmntData.getSsn());
		// if claim is not established for the SSN go and establish claim
		SharedWorkEmployer sharedEmployer = (SharedWorkEmployer) objAssembly
				.getData("SHARED_EMPLOYER");
		sharedWorkEmployeeData.setSharedWorkEmployerData(sharedEmployer
				.getSharedWorkEmployerData());
		// Need to create cliam
		// Claimant claimantBO =
		// Claimant.fetchClaimantBySsn(clmAppClmntData.getSsn());
		// ClaimantData clmntData;
		/*
		 * if (claimantBO != null) { clmntData = claimantBO.getClaimantData(); }
		 * else { clmntData = new ClaimantDataBridge();
		 * clmntData.setSsn(clmAppClmntData.getSsn());
		 * clmntData.setLastName(clmAppClmntData.getLastName());
		 * clmntData.setOtherLastName(clmAppClmntData.getLastName());
		 * clmntData.setFirstName(clmAppClmntData.getFirstName());
		 * clmntData.setDateOfBirth(clmAppClmntData.getDateOfBirth());
		 * clmntData.setSsaStatus(SsaStatusEnum.N.getName()); }
		 * clmAppClmntData.setSsaStatus(clmntData.getSsaStatus());
		 * 
		 * 
		 * //clmAppClmntData.setSharedWorkEmployeeData(sharedWorkEmployeeData);
		 * 
		 * claimantBO = new Claimant(clmntData); claimantBO =
		 * claimantBO.processClaimant(clmAppClmntData);
		 * //claimantBO.saveOrUpdate(); //claimantBO.flush(); //To establish the
		 * claim ClaimApplicationData clmAppData = new ClaimApplicationData();
		 * //ClaimApplicationData clmAppData =
		 * (ClaimApplicationData)objAssembly.
		 * fetchORCreate(ClaimApplicationData.class);
		 * clmAppData.setSsn(clmAppClmntData.getSsn());
		 * 
		 * 
		 * //clmAppData.setSharedWorkEmployerData(sharedEmployer.
		 * getSharedWorkEmployerData()); Calendar cal = Calendar.getInstance();
		 * cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		 * clmAppData.setClaimEffectiveDate(cal.getTime()); Calendar tempCal =
		 * new GregorianCalendar();
		 * tempCal.setTime(clmAppData.getClaimEffectiveDate()); Calendar[] qDate
		 * = RegularClaim.determineBasePeriod(tempCal);
		 * clmAppData.setBpStartDate(qDate[0]);
		 * clmAppData.setBpEndDate(qDate[1]);
		 * clmAppData.setClaimAppStatus(ApplicationStatusEnum
		 * .PENDING.getName()); clmAppData.setFileDate(new Date());
		 * clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
		 * 
		 * ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		 * clmAppBO.saveOrUpdate(); clmAppBO.flush(); RegularClaim regclaimBO =
		 * new RegularClaim(null);
		 * 
		 * regclaimBO.doEstablishClaim(clmntData, clmAppData, clmAppClmntData);
		 */

		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee(
				sharedWorkEmployeeData);
		sharedWorkEmployeeBo.saveOrUpdate();
		sharedWorkEmployeeBo.flush();

		/*
		 * if(clmAppClmntData.getCitizenship().equals(ViewConstants.NO)) {
		 * IssueBean ibean = new IssueBean();
		 * ibean.setClaimantSsn(clmAppClmntData.getSsn());
		 * ibean.setDateIssueDetected(new Date());
		 * ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
		 * ibean.setIssueDescription(IssueCategoryEnum.ABLE_AND_AVAILABLE
		 * .getName()); ibean.setIssueDetails(IssueSubCategoryEnum.ALIEN_STATUS
		 * .getName()); ibean.setIssueSource(IssueSourceEnum.SHARED_WORK
		 * .getName()); ibean.setIssueStartDate(new Date()); // CQ 8203
		 * //ibean.setClaimid(this.claimData.getClaimId());
		 * Issue.createIssue(ibean); }
		 * 
		 * if(clmAppClmntData.getPensionFlag().equals(ViewConstants.YES)) {
		 * IssueBean ibean = new IssueBean();
		 * ibean.setClaimantSsn(clmAppClmntData.getSsn());
		 * ibean.setDateIssueDetected(new Date());
		 * ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
		 * ibean.setIssueDescription(IssueCategoryEnum.PENSION .getName());
		 * ibean.setIssueDetails(IssueSubCategoryEnum.OTHER .getName());
		 * ibean.setIssueSource(IssueSourceEnum.SHARED_WORK .getName());
		 * ibean.setIssueStartDate(new Date()); // CQ 8203
		 * //ibean.setClaimid(this.claimData.getClaimId());
		 * Issue.createIssue(ibean); } // ClaimApplicationClaimant clmAppClaBO =
		 * new ClaimApplicationClaimant(clmAppClmntData);
		 * clmAppClaBO.saveOrUpdate(); clmAppClaBO.flush();
		 */
		return objAssembly;
	}

	// CIF_01737
	/**
	 * @description this method is used to add the employee data that is
	 *              captured from UI and is saved in T_SHARED_WORK_EMPLOYEE
	 * @param IObjectAssembly
	 *            objAssembly
	 * @return IObjectAssembly objAssembly
	 */
	public IObjectAssembly saveOrUpdateSWPEmployeeAdd(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSWPEmployeeAdd");
		}
		// ClaimApplicationClaimantData clmAppClmntData =
		// (ClaimApplicationClaimantData)
		// objAssembly.fetchORCreate(ClaimApplicationClaimantData.class);
		SharedWorkEmployeeData sharedWorkEmployeeData = (SharedWorkEmployeeData) objAssembly
				.fetchORCreate(SharedWorkEmployeeData.class);
		// checking ssn is established claim or not
		objAssembly.removeBusinessError();
		/*
		 * ClaimApplicationData claimAppBO = null; try{ claimAppBO =
		 * ClaimApplication
		 * .getPendingOrIncompleteClaimApp(clmAppClmntData.getSsn());
		 * if(claimAppBO !=null) { throw new BaseApplicationException(
		 * "error.access.getorcreateclaimapp.pendingclaimexists"); } } catch
		 * (BaseApplicationException bae) {
		 * objAssembly.addBusinessError(bae.getMessage()); return objAssembly; }
		 */
		// Getting The SharedWorkEmployerData based on primary key
		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer();
		sharedEmployer = sharedEmployer.getSharedEmployerDataByPlanId(Long
				.valueOf(objAssembly.getPrimaryKey().toString()));

		sharedWorkEmployeeData.setSharedWorkEmployerData(sharedEmployer
				.getSharedWorkEmployerData());

		SharedWorkEmployee shaedBo = new SharedWorkEmployee();
		// getting the list of employees which are part of shared plan
		List<SharedWorkEmployeeData> sharedEmployeeList = shaedBo
				.getSharedEmployeeDataBySsn(sharedWorkEmployeeData.getSsn());

		if (sharedEmployeeList != null && sharedEmployeeList.size() > 0) {
			for (SharedWorkEmployeeData employeeData : sharedEmployeeList) {
				// If an SSN entry exists with same shared plan id, then the
				// amployee is not allowed to add
				if (null != employeeData
						&& employeeData
								.getSharedWorkEmployerData()
								.getPkId()
								.equals(sharedWorkEmployeeData
										.getSharedWorkEmployerData().getPkId())) {
					// objAssembly.addBusinessError("error.access.getorcreateclaimapp.employeepartofsharedplan");
					throw new BaseApplicationException(
							"error.access.getorcreateclaimapp.employeepartofsharedplan");
				}// If an SSN entry exists with different shared plan id and is
					// active, then the amployee is not allowed to add
				/*
				 * else if(null != employeeData && null !=
				 * employeeData.getEmployeeRemovedFlag() &&
				 * Integer.valueOf(employeeData.getEmployeeRemovedFlag()) ==
				 * Integer.valueOf(ViewConstants.NO)){
				 * //objAssembly.addBusinessError
				 * ("error.access.getorcreateclaimapp.employeepartofsharedplan"
				 * ); throw new BaseApplicationException(
				 * "error.access.getorcreateclaimapp.employeepartofsharedplan");
				 * }
				 */
			}
		}

		// ClaimApplicationData claimAppBO =
		// ClaimApplication.getPendingOrIncompleteClaimApp(clmAppClmntData.getSsn());
		// if claim is not established for the SSN go and establish claim
		/*
		 * if(claimAppBO ==null) {
		 * 
		 * // Need to create cliam Claimant claimantBO =
		 * Claimant.fetchClaimantBySsn(clmAppClmntData.getSsn()); ClaimantData
		 * clmntData; if (claimantBO != null) { clmntData =
		 * claimantBO.getClaimantData(); } else { clmntData = new
		 * ClaimantData(); clmntData.setSsn(clmAppClmntData.getSsn());
		 * clmntData.setLastName(clmAppClmntData.getLastName());
		 * clmntData.setOtherLastName(clmAppClmntData.getLastName());
		 * clmntData.setFirstName(clmAppClmntData.getFirstName());
		 * clmntData.setDateOfBirth(clmAppClmntData.getDateOfBirth());
		 * clmntData.setSsaStatus(SsaStatusEnum.N.getName()); }
		 * clmAppClmntData.setSsaStatus(clmntData.getSsaStatus());
		 * 
		 * 
		 * //clmAppClmntData.setSharedWorkEmployeeData(sharedWorkEmployeeData);
		 * 
		 * claimantBO = new Claimant(clmntData); claimantBO =
		 * claimantBO.processClaimant(clmAppClmntData);
		 * //claimantBO.saveOrUpdate(); //claimantBO.flush(); //To establish the
		 * claim ClaimApplicationData clmAppData =
		 * (ClaimApplicationData)objAssembly
		 * .fetchORCreate(ClaimApplicationData.class);
		 * clmAppData.setSsn(clmAppClmntData.getSsn());
		 * 
		 * 
		 * //clmAppData.setSharedWorkEmployerData(sharedEmployer.
		 * getSharedWorkEmployerData()); Calendar cal = Calendar.getInstance();
		 * cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		 * clmAppData.setClaimEffectiveDate(cal.getTime()); Calendar tempCal =
		 * new GregorianCalendar();
		 * tempCal.setTime(clmAppData.getClaimEffectiveDate()); Calendar[] qDate
		 * = RegularClaim.determineBasePeriod(tempCal);
		 * clmAppData.setBpStartDate(qDate[0]);
		 * clmAppData.setBpEndDate(qDate[1]);
		 * clmAppData.setClaimAppStatus(ApplicationStatusEnum
		 * .PENDING.getName()); clmAppData.setFileDate(new Date());
		 * clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
		 * 
		 * ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		 * clmAppBO.saveOrUpdate(); clmAppBO.flush(); RegularClaim regclaimBO =
		 * new RegularClaim(null);
		 * 
		 * regclaimBO.doEstablishClaim(clmntData, clmAppData, clmAppClmntData);
		 * }
		 */
		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee(
				sharedWorkEmployeeData);
		sharedWorkEmployeeBo.saveOrUpdate();
		sharedWorkEmployeeBo.flush();

		/*
		 * if(clmAppClmntData.getCitizenship().equals(ViewConstants.NO)) {
		 * IssueBean ibean = new IssueBean();
		 * ibean.setClaimantSsn(clmAppClmntData.getSsn());
		 * ibean.setDateIssueDetected(new Date());
		 * ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
		 * ibean.setIssueDescription(IssueCategoryEnum.ABLE_AND_AVAILABLE
		 * .getName()); ibean.setIssueDetails(IssueSubCategoryEnum.ALIEN_STATUS
		 * .getName()); ibean.setIssueSource(IssueSourceEnum.SHARED_WORK
		 * .getName()); ibean.setIssueStartDate(new Date()); // CQ 8203
		 * //ibean.setClaimid(this.claimData.getClaimId());
		 * Issue.createIssue(ibean); }
		 * 
		 * if(clmAppClmntData.getPensionFlag().equals(ViewConstants.YES)) {
		 * IssueBean ibean = new IssueBean();
		 * ibean.setClaimantSsn(clmAppClmntData.getSsn());
		 * ibean.setDateIssueDetected(new Date());
		 * ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
		 * ibean.setIssueDescription(IssueCategoryEnum.PENSION .getName());
		 * ibean.setIssueDetails(IssueSubCategoryEnum.OTHER .getName());
		 * ibean.setIssueSource(IssueSourceEnum.SHARED_WORK .getName());
		 * ibean.setIssueStartDate(new Date()); // CQ 8203
		 * //ibean.setClaimid(this.claimData.getClaimId());
		 * Issue.createIssue(ibean); }
		 */
		/*
		 * if(clmAppClmntData.getPensionFlag() == ViewConstants.YES) {
		 * CorrespondenceData correspondenceData = new CorrespondenceData();
		 * correspondenceData
		 * .setCorrespondenceCode(CorrespondenceCodeEnum.MODES_B_246.getName());
		 * correspondenceData
		 * .setDirection(CorrespondenceDirectionEnum.OUTGOING.getName());
		 * correspondenceData
		 * .setParameter1(clmAppClmntData.getFirstName()+" "+clmAppClmntData
		 * .getLastName());
		 * correspondenceData.setParameter1Desc("CLAIMANT_NAME");
		 * correspondenceData
		 * .setMailingAddress(clmAppClmntData.getMailAddress());
		 * correspondenceData.setParameter2(clmAppClmntData.getSsn());
		 * correspondenceData.setParameter2Desc("SSN"); correspondenceData.setc
		 * 
		 * Correspondence correspondenceBO = new
		 * Correspondence(correspondenceData); correspondenceBO.saveOrUpdate();
		 * correspondenceBO.flush();
		 * 
		 * 
		 * }
		 */

		//
		/*
		 * ClaimApplicationClaimant clmAppClaBO = new
		 * ClaimApplicationClaimant(clmAppClmntData);
		 * clmAppClaBO.saveOrUpdate(); clmAppClaBO.flush();
		 */
		return objAssembly;
	}

	// CIF_01737 ends : Methods modified as per Jira 2063
	// CIF_00236 END

	// CIF_00418 START
	// Shared WORK PLAN - Update Employee Information
	public IObjectAssembly getUpdateSharedPlanEmployee(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getUpdateSharedPlanEmployee");
		}
		// objAssembly.removeComponent(EmployerData.class);
		// ClaimApplicationClaimantData clmAppClmntData =
		// (ClaimApplicationClaimantData)
		// objAssembly.fetchORCreate(ClaimApplicationClaimantData.class);
		SharedWorkEmployeeData sharedWorkEmployeeData = (SharedWorkEmployeeData) objAssembly
				.fetchORCreate(SharedWorkEmployeeData.class);

		/*
		 * if(clmAppClmntData.getPensionFlag().equals(ViewConstants.YES)) {
		 * IssueBean ibean = new IssueBean();
		 * ibean.setClaimantSsn(clmAppClmntData.getSsn());
		 * ibean.setDateIssueDetected(new Date());
		 * ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
		 * ibean.setIssueDescription(IssueCategoryEnum.PENSION .getName());
		 * ibean.setIssueDetails(IssueSubCategoryEnum.OTHER .getName());
		 * ibean.setIssueSource(IssueSourceEnum.SHARED_WORK .getName());
		 * ibean.setIssueStartDate(new Date()); Issue.createIssue(ibean); }
		 */
		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee(
				sharedWorkEmployeeData);
		sharedWorkEmployeeBo.saveOrUpdate();
		sharedWorkEmployeeBo.flush();

		/*
		 * ClaimApplicationClaimant clmAppClaBO = new
		 * ClaimApplicationClaimant(clmAppClmntData);
		 * clmAppClaBO.saveOrUpdate(); clmAppClaBO.flush();
		 */
		return objAssembly;
	}

	// CIF_00418 END

	// CIF_00417 START
	// Shared WORK PLAN - Remove Employee
	public IObjectAssembly getRemovedSharedPlanEmployee(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getRemovedSharedPlanEmployee");
		}
		// objAssembly.removeComponent(EmployerData.class);
		// ClaimApplicationClaimantData clmAppClmntData =
		// (ClaimApplicationClaimantData)
		// objAssembly.fetchORCreate(ClaimApplicationClaimantData.class);
		SharedWorkEmployeeData sharedWorkEmployeeData = (SharedWorkEmployeeData) objAssembly
				.fetchORCreate(SharedWorkEmployeeData.class);

		// CIF_02936 UIM-3453 This WI s not required
		// CREADING WORK ITEM FOR EMPLOYEE REMOVED
		/*
		 * if(sharedWorkEmployeeData.getEmployeeRemovedFlag().equals(ViewConstants
		 * .YES)) { Map<String,Object> mapValues = new HashMap<String,Object>();
		 * mapValues.put(WorkflowConstants.TYPE,
		 * WorkflowConstants.BUSINESS_TYPE.SSN);
		 * mapValues.put(WorkflowConstants.SSN_EAN_FEIN,
		 * sharedWorkEmployeeData.getSsn());
		 * mapValues.put(WorkflowConstants.NAME,
		 * sharedWorkEmployeeData.getLastName());
		 * mapValues.put(WorkflowConstants
		 * .BUSINESS_KEY,sharedWorkEmployeeData.getPkId().toString());
		 * mapValues.
		 * put(WorkflowConstants.ZIP_CODE,sharedWorkEmployeeData.getZip());
		 * mapValues.put(WorkflowConstants.PROCESS_NAME,
		 * WorkflowProcessTemplateConstants
		 * .Claims.SHARED_WORK_EMPLOYEE_REMOVED); WorkflowTransactionService
		 * wfTransactionService = new WorkflowTransactionService();
		 * wfTransactionService
		 * .invokeWorkFlowOperation(WorkFlowOperationsEnum.CREATE_WORKITEM
		 * .getName(), mapValues);
		 * 
		 * }
		 */
		if (StringUtility.isNotBlank(sharedWorkEmployeeData
				.getSeperatedReason())
				&& (sharedWorkEmployeeData.getSeperatedReason().equals(
						SeparationReasonEnum.VOLUNTARY_QUIT.getName()) || sharedWorkEmployeeData
						.getSeperatedReason().equals(
								SeparationReasonEnum.DISCHARGE.getName()))) {
			List<ClaimData> clmDataList = RegularClaim
					.findActiveRegularClaimListBySsn(sharedWorkEmployeeData
							.getSsn());
			if (null != clmDataList && !clmDataList.isEmpty()) {
				if (new Date().before(clmDataList.get(0).getByeDate())) {
					WeeklyCertData weekData = WeeklyCert
							.findWeeklyCertByClaimId(clmDataList.get(0)
									.getClaimantData().getSsn());

					IssueBean issueBean = new IssueBean();
					issueBean.setClaimantSsn(sharedWorkEmployeeData.getSsn());
					issueBean.setDateIssueDetected(new Date());
					if (null == weekData
							|| weekData.getClaimWeekEndDate().before(
									clmDataList.get(0).getEffectiveDate())) {
						issueBean.setIssueStartDate(clmDataList.get(0)
								.getEffectiveDate());
					} else {
						Calendar cal = Calendar.getInstance();
						cal.setTime(weekData.getClaimWeekEndDate());
						cal.add(Calendar.DAY_OF_MONTH,
								GlobalConstants.NUMERIC_ONE);
						issueBean.setIssueStartDate(cal.getTime());
					}
					// CIF_INT_01193
					// issueBean.setIssueStartDate(clmDataList.get(0).getEffectiveDate());
					issueBean.setIssueEndDate(DateFormatUtility.parse(
							GlobalConstants.ISSUE_INDEFINITE_DATE,
							DateFormatUtility.CUSTOM_DATE_PATTERN));
					if (sharedWorkEmployeeData.getSeperatedReason().equals(
							SeparationReasonEnum.VOLUNTARY_QUIT.getName())) {
						issueBean
								.setIssueDescription(IssueCategoryEnum.VOLUNTARY_LEAVING
										.getName());
						issueBean.setIssueDetails(IssueSubCategoryEnum.OTHER
								.getName());
					} else {
						issueBean
								.setIssueDescription(IssueCategoryEnum.DISCHARGE
										.getName());
						issueBean.setIssueDetails(IssueSubCategoryEnum.OTHER
								.getName());
					}
					issueBean.setClaimid(clmDataList.get(0).getClaimId());
					issueBean.setIssueSource(IssueSourceEnum.SHARED_WORK
							.getName());
					issueBean.setCallIssue(ViewConstants.YES_DESCRIPTION_LC);
					IssueData issueData = Issue.createIssue(issueBean,
							Boolean.TRUE);
					// CIF_INT_04742 adding seperation date
					// IssueData issueData = Issue.createIssue(issueBean,
					// Boolean.TRUE);
					if (null != issueData.getFactFindingData()) {
						issueData.getFactFindingData().setSeparationDate(
								issueBean.getIssueStartDate());
						Issue.saveOrUpdateCreateIssue(issueData);
					}
				}
			}
		}
		// CIF_02936 ends
		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee(
				sharedWorkEmployeeData);
		sharedWorkEmployeeBo.merge();
		// sharedWorkEmployeeBo.flush();
		return objAssembly;
	}

	// CIF_00417 END
	// CIF_00277
	/**
	 * this method fetches the data thats is to be displayed on weekly
	 * certification of the employee in shared work plan
	 * 
	 * @param ObjectAssembly
	 *            data containing SSN number week ending Date
	 * @return Employer information , Employee Information, Weekly Certification
	 *         Data for the week if available
	 */
	public IObjectAssembly fetchEmployeeInformation(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method fetchEmployeeInformation");
		}
		String ssn = objAssembly.getSsn();
		Claimant cData = Claimant.findBySsn(ssn);
		if (null == cData) {
			throw new BaseApplicationException(
					"error.access.sharedWorkPlan.InvalidSSN");
		}
		ClaimantData claimantData = cData.getClaimantData();
		objAssembly.addComponent(claimantData, true);
		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee();
		List<SharedWorkEmployeeData> list = sharedWorkEmployeeBo
				.getSharedEmployeeDataBySsn(ssn);

		boolean employeeFound = Boolean.FALSE;
		for (SharedWorkEmployeeData sharedWorkEmployeeData : list) {
			if (sharedWorkEmployeeData.getEmployeeRemovedFlag()
					.equalsIgnoreCase(BenefitsConstants.ZERO)) {
				Date date = (Date) objAssembly.getData("weekEndingDate");
				WeeklyCertData weekCertData = WeeklyCert
						.getWeeklyCertificationForSharedPlan(ssn, date);
				if (null != weekCertData) {
					if (null == weekCertData.getSharedWorkPlanId()) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.WEEK_OF_MONTH, -1);
						weekCertData = WeeklyCert
								.getWeeklyCertificationForSharedPlan(ssn,
										cal.getTime());
						if (null != weekCertData) {
							if (null == weekCertData.getSharedWorkPlanId()) {
								throw new BaseApplicationException(
										"error.access.sharedWorkPlan.InvalidWeekCertification");
							}
						}
						objAssembly.addData("weekEndingDate", cal.getTime());
					}
				}
				objAssembly.addComponent(sharedWorkEmployeeData, true);
				String ean = sharedWorkEmployeeData.getSharedWorkEmployerData()
						.getEan();
				objAssembly.setEan(ean);
				objAssembly.removeComponent(RegisteredEmployerData.class);
				// objAssembly=this.getRegisteredEmployerByEan(objAssembly);
				if (null != weekCertData) {
					objAssembly.addComponent(weekCertData, true);
				}

				// objAssembly.addComponent(cData.getClaimantData(),true);
				employeeFound = Boolean.TRUE;
				break;
			}
		}
		if (!employeeFound) {
			throw new BaseApplicationException(
					"error.access.sharedWorkPlan.InvalidSSN");
		}
		return objAssembly;
	}

	/**
	 * This Method will update data in T_SHARED_WEEKLY_CERT table for batch to
	 * process records
	 * 
	 * @throws BaseApplicationException
	 * @param objAssembly
	 *            IObjectAssembly
	 * @return objAssembly IObjectAssembly
	 */
	// CIF_01737 starts : Method modified as per Jira 2063
	@Override
	public IObjectAssembly saveOrUpdateSWPBiWeeklyCertHrs(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSWPBiWeeklyCertHrs");
		}
		String[] workHours = (String[]) objAssembly.getData("workHours");
		// String [] claimantId = (String[]) objAssembly.getData("claimantId");
		String[] sharedWeekId = (String[]) objAssembly.getData("sharedWeekId");
		String[] sharedEmployeeId = (String[]) objAssembly
				.getData("sharedEmployeeId");
		String[] ssnArray = (String[]) objAssembly.getData("ssnArray");
		// CalendarBean [] weekEndingDate = (CalendarBean[])
		// objAssembly.getData("weekEndingDateArray");
		CalendarBean cal = (CalendarBean) objAssembly.getData("weekEndingDate");
		int length = workHours.length;
		objAssembly.removeBusinessError();
		List<SharedWeeklyCertData> sharedWeeklyDataList = new ArrayList<SharedWeeklyCertData>();
		SharedWeeklyCertData sharedWeeklyCertData = null;
		SharedWorkEmployeeData sharedWorkEmpData = null;
		SharedWorkEmployee sharedWorkEmployee = new SharedWorkEmployee();

		for (int i = 0; i < length; i++) {
			if (StringUtility.isNotBlank(workHours[i])) {
				if (StringUtility.isNotBlank(sharedWeekId[i])) {
					// Update the record present in T_SHARED_WEEKLY_CERT to
					// unprocessed and batch will handle the reporcessing of the
					// week
					SharedWeeklyCertData sharedWeekData = SharedWeeklyCert
							.getSharedWeeklyCertDataByPrimaryKey(Long
									.parseLong(sharedWeekId[i]));
					if (sharedWeekData.getSharedWorkHrs().compareTo(
							new BigDecimal(workHours[i])) != 0) {
						sharedWeekData.setProcessedFlag(ViewConstants.NO);
						sharedWeekData.setSharedWorkHrs(new BigDecimal(
								workHours[i]));
						sharedWeeklyDataList.add(sharedWeekData);
					}
				} else {
					// insert the record in T_SHARED_WEEKLY_CERT for batch to
					// file weekly certification
					sharedWeeklyCertData = new SharedWeeklyCertData();
					sharedWeeklyCertData.setProcessedFlag(ViewConstants.NO);
					sharedWeeklyCertData.setCwe(cal.getValue());
					sharedWeeklyCertData.setSharedWorkHrs(new BigDecimal(
							workHours[i]));
					sharedWorkEmpData = sharedWorkEmployee
							.getSharedEmployeeDataByPrimaryKey(Long
									.valueOf(sharedEmployeeId[i]));
					sharedWeeklyCertData
							.setSharedWorkEmployeeData(sharedWorkEmpData);
					sharedWeeklyCertData.setSsn(ssnArray[i]);
					sharedWeeklyDataList.add(sharedWeeklyCertData);
				}
			}
		}
		SharedWeeklyCert sharedWeeklycert = new SharedWeeklyCert();
		sharedWeeklycert.saveOrUpdateAll(sharedWeeklyDataList);
		sharedWeeklycert.flush();

		/*
		 * for (int i = 0; i < length; i++) {
		 * if(StringUtility.isNotBlank(workHours[i])){
		 * if(StringUtility.isBlank(weekCertId[i])){ try {
		 * WeeklyCert.determineEligibilityToFileWeeklyCertification(ssnArray[i],
		 * null); } catch (BaseApplicationException e) {
		 * objAssembly.addBusinessError(e.getMessage()); } }
		 * 
		 * } } if(objAssembly.hasBusinessError()){ return objAssembly; }
		 * 
		 * for (int i = 0; i < length; i++) { //ClaimantData cData =
		 * Claimant.fetchClaimantDataByPkId
		 * (Long.valueOf(claimantId[i])).getClaimantData();
		 * if(StringUtility.isNotBlank(workHours[i])){ WeeklyCertData
		 * weekCertData=null; if(StringUtility.isNotBlank(weekCertId[i])){
		 * weekCertData
		 * =WeeklyCert.findByPrimaryKey(Long.valueOf(weekCertId[i])).
		 * getWeeklyCertData(); } if(null == weekCertData){ // Create Default
		 * Stub To insert Data into the T_WEEKLY_CERTIFICATION // Call
		 * Validation method in try catch and catch business exceptions and
		 * proceed with other records -- Done // Write logic to this method -
		 * validateAndInsertWeeklYCertification : This will validate the
		 * employee for doing weekly certification and Insert data into the
		 * required tables SharedWorkEmployer sharedBo = new
		 * SharedWorkEmployer(); SharedWorkEmployeeData sharedEmployeeData =
		 * (SharedWorkEmployeeData)
		 * sharedBo.findByPrimaryKey(SharedWorkEmployeeData.class,
		 * Long.valueOf(sharedEmployeeId[i])); weekCertData = new
		 * WeeklyCertData();
		 * weekCertData.setAdjustedEarnings(GlobalConstants.BIGDECIMAL_ZERO);
		 * weekCertData.setAvailable(GlobalConstants.ONE);
		 * weekCertData.setCertificationMode
		 * (CertificationModeEnum.INTERNET_CSR.getName());
		 * weekCertData.setClaimantData
		 * (Claimant.fetchClaimantDataByPkId(Long.valueOf
		 * (claimantId[i])).getClaimantData()); if(null != weekEndingDate){
		 * weekCertData.setClaimWeekEndDate(weekEndingDate[i].getValue());
		 * }else{ weekCertData.setClaimWeekEndDate(cal.getValue()); }
		 * weekCertData.setInvestigationPerformedFlag(GlobalConstants.ZERO);
		 * weekCertData.setWorksearchnumber(GlobalConstants.WEEKLY_CERT_ZERO);
		 * weekCertData.setPayDuaFlag(GlobalConstants.ZERO);
		 * weekCertData.setPendingReason(GlobalConstants.NPND);
		 * weekCertData.setPhysicallyAble(GlobalConstants.ONE);
		 * weekCertData.setProcessedFlag(GlobalConstants.ZERO);
		 * weekCertData.setReasonSeparation(GlobalConstants.NSEP);
		 * weekCertData.setRefusedJob(GlobalConstants.ZERO);
		 * weekCertData.setReturnedFulltimeWork(GlobalConstants.ONE);
		 * weekCertData.setTraining(GlobalConstants.ZERO);
		 * weekCertData.setSelfemployed(GlobalConstants.ZERO);
		 * weekCertData.setTotalEarnings(GlobalConstants.BIGDECIMAL_ZERO);
		 * weekCertData.setReceivepay(GlobalConstants.ZERO);
		 * weekCertData.setCompletedFlag(GlobalConstants.ONE);
		 * weekCertData.setWorkPerformed(GlobalConstants.ZERO);
		 * weekCertData.setWorkSearchFlag(GlobalConstants.ZERO);
		 * weekCertData.setWorkSearchReason(GlobalConstants.NAAP); // Shared
		 * Plan Related Values
		 * weekCertData.setSharedWorkPlanId(sharedEmployeeData
		 * .getSharedWorkEmployerData().getPkId());
		 * weekCertData.setRegularWorkHrs(new BigDecimal(
		 * sharedEmployeeData.getWeeklyWorkHours()));
		 * weekCertData.setSharedWorkHrs(new BigDecimal(workHours[i]));
		 * weekCertData.setPartWorkHrs(GlobalConstants.BIGDECIMAL_ZERO);
		 * WeeklyCert weekCert = new WeeklyCert(weekCertData);
		 * weekCert.saveOrUpdate(); weekCert.flush(); WeeklyCertJournalBean
		 * weeklycertjrnlbean = new WeeklyCertJournalBean();
		 * weeklycertjrnlbean.setClaimantData(weekCertData.getClaimantData());
		 * weeklycertjrnlbean.setStartDate(weekCertData.getClaimWeekEndDate());
		 * weeklycertjrnlbean.setEndDate(weekCertData.getClaimWeekEndDate());
		 * weeklycertjrnlbean
		 * .setWeekProcessingReasonEnum(WeekProcessingReasonEnum
		 * .WEEKLY_CERTIFICATION); //this will save in journal table only when
		 * this will be completed.(WC_COMPLETED flag should be 1)
		 * WeeklyCertificationJrnlBO
		 * .prepareWeeklycertificationJournal(weeklycertjrnlbean); }else if
		 * (weekCertData.getSharedWorkHrs().doubleValue() !=
		 * Double.valueOf(workHours[i])){ // Update Hours in Weekly Cert and
		 * check if the entry is required in JNRL table
		 * weekCertData.setSharedWorkHrs(new BigDecimal(workHours[i]));
		 * WeeklyCert weekCert = new WeeklyCert(weekCertData);
		 * weekCert.saveOrUpdate(); weekCert.flush(); WeeklyCertificationJrnlBO
		 * weeklyBO = WeeklyCertificationJrnlBO.
		 * getAnyWeeklyCertificationJrnlDataForWeeklyCertId
		 * (Long.valueOf(weekCertId[i]));
		 * if(StringUtility.isNotBlank(weeklyBO.getWeeklyCertificationJrnlData
		 * ().getRecordProcessedFlag()) &&
		 * !GlobalConstants.ONE.equals(weeklyBO.getWeeklyCertificationJrnlData
		 * ().getRecordProcessedFlag()) ){ WeeklyCertJournalBean
		 * weeklycertjrnlbean = new WeeklyCertJournalBean();
		 * weeklycertjrnlbean.setClaimantData(weekCertData.getClaimantData());
		 * weeklycertjrnlbean.setStartDate(weekCertData.getClaimWeekEndDate());
		 * weeklycertjrnlbean.setEndDate(weekCertData.getClaimWeekEndDate());
		 * weeklycertjrnlbean
		 * .setWeekProcessingReasonEnum(WeekProcessingReasonEnum
		 * .WEEKLY_CERTIFICATION); //this will save in journal table only when
		 * this will be completed.(WC_COMPLETED flag should be 1)
		 * WeeklyCertificationJrnlBO
		 * .prepareWeeklycertificationJournal(weeklycertjrnlbean); } } // else
		 * do nothing Already Updated record and increment the counter to next
		 * record } }
		 */
		return objAssembly;
	}

	// CIF_01737 ends : Method modified as per Jira 2063

	public IObjectAssembly saveOrUpdateSWPBiWeeklyPartCertHrs(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveOrUpdateSWPBiWeeklyPartCertHrs");
		}
		String workHours = (String) objAssembly.getData("partWorkHours");
		String claimantId = (String) objAssembly.getData("claimantId");
		String weekCertId = null != objAssembly.getData("weekCertId") ? objAssembly
				.getData("weekCertId").toString() : null;
		String sharedEmployeeId = null != objAssembly
				.getData("sharedEmployeeId") ? objAssembly.getData(
				"sharedEmployeeId").toString() : null;
		String ssn = (String) objAssembly.getData("ssn");
		CalendarBean cal = (CalendarBean) objAssembly.getData("weekEndingDate");
		objAssembly.removeBusinessError();
		// ClaimantData cData =
		// Claimant.fetchClaimantDataByPkId(Long.valueOf(claimantId[i])).getClaimantData();
		WeeklyCertData weekCertData = null;
		if (StringUtility.isNotBlank(weekCertId)) {
			// weekCertData=WeeklyCert.findByPrimaryKey(Long.valueOf(weekCertId)).getWeeklyCertData();
			weekCertData = objAssembly
					.getFirstComponent(WeeklyCertDataBridge.class);
			if (!weekCertData.getClaimWeekEndDate().equals(cal.getValue())) {
				weekCertData = WeeklyCert.getWeeklyCertificationForSharedPlan(
						ssn, cal.getValue());
			}
		} else {
			weekCertData = WeeklyCert.getWeeklyCertificationForSharedPlan(ssn,
					cal.getValue());
		}
		if (null == weekCertData) {
			// Create Default Stub To insert Data into the
			// T_WEEKLY_CERTIFICATION
			// Call Validation method in try catch and catch business exceptions
			// and proceed with other records -- Done
			// Write logic to this method - validateAndInsertWeeklYCertification
			// : This will validate the employee for doing weekly certification
			// and Insert data into the required tables
			WeeklyCert.determineEligibilityToFileWeeklyCertification(ssn, null);
			SharedWorkEmployer sharedBo = new SharedWorkEmployer();
			SharedWorkEmployeeData sharedEmployeeData = (SharedWorkEmployeeData) sharedBo
					.findByPrimaryKey(SharedWorkEmployeeData.class,
							Long.valueOf(sharedEmployeeId));
			weekCertData = new WeeklyCertData();
			weekCertData.setAdjustedEarnings(GlobalConstants.BIGDECIMAL_ZERO);
			weekCertData.setAvailable(GlobalConstants.ONE);
			weekCertData
					.setCertificationMode(GlobalConstants.WEEKLY_CERT_ONLINE);
			weekCertData.setClaimantData(Claimant.fetchClaimantDataByPkId(
					Long.valueOf(claimantId)).getClaimantData());
			weekCertData.setClaimWeekEndDate(cal.getValue());
			weekCertData.setInvestigationPerformedFlag(GlobalConstants.ZERO);
			weekCertData.setWorksearchnumber(GlobalConstants.WEEKLY_CERT_ZERO);
			weekCertData.setPayDuaFlag(GlobalConstants.ZERO);
			weekCertData.setPendingReason(GlobalConstants.NPND);
			weekCertData.setPhysicallyAble(GlobalConstants.ONE);
			weekCertData.setProcessedFlag(GlobalConstants.ZERO);
			weekCertData.setReasonSeparation(GlobalConstants.NSEP);
			weekCertData.setRefusedJob(GlobalConstants.ZERO);
			weekCertData.setReturnedFulltimeWork(GlobalConstants.ONE);
			weekCertData.setTraining(GlobalConstants.ZERO);
			weekCertData.setSelfemployed(GlobalConstants.ZERO);
			weekCertData.setTotalEarnings(GlobalConstants.BIGDECIMAL_ZERO);
			weekCertData.setReceivepay(GlobalConstants.ZERO);
			weekCertData.setCompletedFlag(GlobalConstants.ONE);
			weekCertData.setWorkPerformed(GlobalConstants.ZERO);
			weekCertData.setWorkSearchFlag(GlobalConstants.ZERO);
			weekCertData.setWorkSearchReason(GlobalConstants.NAAP);
			// Shared Plan Related Values
			weekCertData.setSharedWorkPlanId(sharedEmployeeData
					.getSharedWorkEmployerData().getPkId());
			weekCertData.setRegularWorkHrs(new BigDecimal(sharedEmployeeData
					.getWeeklyWorkHours()));
			weekCertData.setPartWorkHrs(new BigDecimal(workHours));
			weekCertData.setSharedWorkHrs(GlobalConstants.BIGDECIMAL_ZERO);
			WeeklyCert weekCert = new WeeklyCert(weekCertData);
			weekCert.saveOrUpdate();
			weekCert.flush();
			WeeklyCertJournalBean weeklycertjrnlbean = new WeeklyCertJournalBean();
			weeklycertjrnlbean.setClaimantData(weekCertData.getClaimantData());
			weeklycertjrnlbean.setStartDate(weekCertData.getClaimWeekEndDate());
			weeklycertjrnlbean.setEndDate(weekCertData.getClaimWeekEndDate());
			weeklycertjrnlbean
					.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.WEEKLY_CERTIFICATION);
			// this will save in journal table only when this will be
			// completed.(WC_COMPLETED flag should be 1)
			WeeklyCertificationJrnlBO
					.prepareWeeklycertificationJournal(weeklycertjrnlbean);
		} else if (weekCertData.getSharedWorkHrs().doubleValue() != Double
				.valueOf(workHours)) {
			// Update Hours in Weekly Cert and check if the entry is required in
			// JNRL table
			weekCertData.setPartWorkHrs(new BigDecimal(workHours));
			WeeklyCert weekCert = new WeeklyCert(weekCertData);
			weekCert.saveOrUpdate();
			weekCert.flush();
			WeeklyCertificationJrnlBO weeklyBO = WeeklyCertificationJrnlBO
					.getAnyWeeklyCertificationJrnlDataForWeeklyCertId(Long
							.valueOf(weekCertId));
			if (StringUtility.isNotBlank(weeklyBO
					.getWeeklyCertificationJrnlData().getRecordProcessedFlag())
					&& !GlobalConstants.ONE.equals(weeklyBO
							.getWeeklyCertificationJrnlData()
							.getRecordProcessedFlag())) {
				WeeklyCertJournalBean weeklycertjrnlbean = new WeeklyCertJournalBean();
				weeklycertjrnlbean.setClaimantData(weekCertData
						.getClaimantData());
				weeklycertjrnlbean.setStartDate(weekCertData
						.getClaimWeekEndDate());
				weeklycertjrnlbean.setEndDate(weekCertData
						.getClaimWeekEndDate());
				weeklycertjrnlbean
						.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.WEEKLY_CERTIFICATION);
				// this will save in journal table only when this will be
				// completed.(WC_COMPLETED flag should be 1)
				WeeklyCertificationJrnlBO
						.prepareWeeklycertificationJournal(weeklycertjrnlbean);
			}
		}
		// else do nothing Already Updated record and increment the counter to
		// next record
		return objAssembly;
	}

	/*
	 * // CIF_00061 /** Overrides @see
	 * gov.state.uim.cin.service.BICinService#updateClaimantAndClaimApplication
	 * (gov.state.uim.framework.service.IObjectAssembly) TODO Update the
	 * Claimant and Claim Application
	 */
	public IObjectAssembly updateClaimApplicationBySsn(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateClaimApplicationBySsn");
		}
		// this.saveOrUpdateClmAppClmnt(objAssembly);
		ClaimApplication claimApp = (ClaimApplication) objAssembly
				.getData("claimApplicationData");
		ClaimApplicationData clmAppData = claimApp.getClaimApplicationData();
		ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
		clmAppBO.saveOrUpdate();
		// ClaimantData
		// data=(ClaimantData)objAssembly.getFirstComponent(ClaimantDataBridge.class);
		// Claimant clmAppBO=new Claimant(data);
		// clmAppBO.saveOrUpdate();
		clmAppBO.flush();
		return objAssembly;
	}

	// Added for CIF_00580
	/**
	 * this method updates the technician review status in shared work plan
	 * 
	 * @param objAssembly
	 * @return
	 */
	public IObjectAssembly updateTechnicianReview(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateTechnicianReview");
		}
		SharedWorkEmployerData sharedData = objAssembly
				.getFirstComponent(SharedWorkEmployerData.class);
		if (objAssembly.hasBusinessError()) {
			return objAssembly;
		}
		String persistenceOid = objAssembly.getData("WfId").toString();
		String userId = (String) objAssembly.getData("userId");

		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowConstants.TASK_ID, persistenceOid);
		map.put(WorkflowConstants.USER_ID, userId);
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(), map);
		// BaseWorkflowDAO workflowDAO = new BaseWorkflowDAO();
		// workflowDAO.startAndCompleteWorkitem(persistenceOid, userId);

		SharedWorkEmployer sharedEmployer = new SharedWorkEmployer(sharedData);
		sharedEmployer.saveOrUpdate();

		return objAssembly;
	}

	// CIF_00417 START
	// shared plan REmove Employee review
	public IObjectAssembly getSharedWorkEmplyeeData(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getSharedWorkEmplyeeData");
		}

		Long primaryKey = objAssembly.getPrimaryKeyAsLong();
		SharedWorkEmployee sharedWorkEmployeeBo = new SharedWorkEmployee();
		SharedWorkEmployeeData sharedWorkEmployeeData = sharedWorkEmployeeBo
				.getSharedEmployeeDataByPrimaryKey(primaryKey);
		objAssembly.addComponent(sharedWorkEmployeeData);
		return objAssembly;
	}

	// CIF_00417 END

	// CIF_00972:Start
	/**
	 * This method hase been added to fetch the claim Application Information
	 * 
	 * @param objAssembly
	 * @return objAssembly
	 */
	public IObjectAssembly getClaimAppdetails(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getClaimAppdetails");
		}

		String ssn = objAssembly.getSsn();
		if (ssn != null) {
			ClaimApplication claimAppBO = ClaimApplication
					.fetchClaimAppDataBySsn(objAssembly.getSsn());
			if (claimAppBO != null) {
				ClaimApplicationData claimAppData = claimAppBO
						.getClaimApplicationData();
				objAssembly.addComponent(claimAppData);
			}
		}
		return objAssembly;
	}

	// CIF_00972:End

	/**
	 * CIF_02641 Defect_3690||Appeals AP: Referee Order canned statements should
	 * be edited before submittal to enter parameters.||Code added
	 * 
	 * @param objAssembly
	 *            containing the appeal id
	 * @return IObjectAssembly
	 * @throws BaseApplicationException
	 */
	@Override
	public IObjectAssembly getAppealByAppealId(final IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getAppealByAppealId");
		}

		Long prikey = objAssembly.getPrimaryKeyAsLong();
		Appeal appeal = Appeal.findByPrimaryKey(prikey);
		AppealData appealData = appeal.getAppealData();

		TaxInterceptInfoBean taxInterceptInfoBean = new TaxInterceptInfoBean();
		taxInterceptInfoBean.setAppealData(appealData);

		objAssembly.addBean(taxInterceptInfoBean);

		String indexVal = (String) objAssembly
				.getData(GlobalConstants.INDEX_VALUE);
		if (StringUtility.isNotBlank(indexVal)) {
			DocumentImageIndexData doImageIndexData = DocumentImageIndexBO
					.getDocumentPathByIndexVal(indexVal);
			objAssembly.addComponent(doImageIndexData.getDocImageData(), true);
		}

		/* Start-Fetch Adjudicate Decision for the appeal */

		Map map = Appeal
				.getIssueDescriptionAndDecisionDateFromAppealData(appealData);
		String issueDescription = (String) map
				.get(AppealConstants.ISSUE_DESCRIPTION);
		String previousDecisionDate = (String) map
				.get(AppealConstants.PREVIOUS_DECISION_DATE);

		String reasonCode = "";
		reasonCode = (String) map.get(AppealConstants.REASON_CODE_KEY);

		String decisionCode = "";
		decisionCode = (String) map.get(AppealConstants.DECISION_CODE_KEY);
		String determinationDetails = "";
		/* Issue to be fetched from T_MST_DETERMINATION_REASON_CODE */
		MstAppDeterReasonCodeMaster appealDeterminationMaster = null;
		String parentAppealType = "";
		LateAppealData lateAppeal = null;
		String miscAppealType = null;
		String miscIssueCateg = null;
		if (appealData.getAppealType().equals(AppealTypeEnum.LATE.getName())) {
			lateAppeal = (LateAppealData) appealData;
			parentAppealType = lateAppeal.getParentAppealData().getAppealType();
			/*
			 * if(AppealTypeEnum.MISC.getName().equals(parentAppealType)) {
			 * MiscAppealData miscAppealData=(MiscAppealData)
			 * lateAppeal.getParentAppealData();
			 * miscAppealType=miscAppealData.getMiscAppealType();
			 * miscIssueCateg=miscAppealData.getMiscAppIssue(); }
			 */
		}
		if (AppealTypeEnum.NON_MON.getName().equals(appealData.getAppealType())
				|| AppealTypeEnum.NON_MON.getName().equals(parentAppealType)) {
			String issueCat = (String) map
					.get(AppealConstants.ISSUE_CATEGORY_KEY);
			String issueSubCat = (String) map
					.get(AppealConstants.ISSUE_SUB_CATEGORY_KEY);
			// reasonCode=appealData.getAppealledAgainstDecision().getReasonCode();
			// String
			// decisionCode=appealData.getAppealledAgainstDecision().getIssueData().getDecisionCode();
			String issueType = "";
			if (reasonCode != null
					&& reasonCode
							.equalsIgnoreCase(AppealConstants.NO_REASON_CODE)) {
				reasonCode = GlobalConstants.NA;
			} else {
				reasonCode = "";
			}
			Appeal appealBO = Appeal.findByPrimaryKey(appealData.getAppealId());
			issueType = "LATE_APPEAL_DETER";
			appealDeterminationMaster = appealBO
					.fetchAppealDeterminationReasonMaster(issueType, issueCat,
							issueSubCat, decisionCode, reasonCode);

		} else if (AppealTypeEnum.MISC.getName().equals(
				appealData.getAppealType())
				|| AppealTypeEnum.MISC.getName().equals(parentAppealType)) {
			/*
			 * CIF_02688 || Defect_3652 || SIT ||Appeals || Appeal Notice
			 * Statement and Deputy determination is not coming correctly on
			 * MODES-L-20, MODES-3422
			 */
			/*
			 * CIF_00878 Starts,Desc:Added Appeal determination reason code
			 * master input details and result.
			 */
			// String
			// decisionCode=appealData.getAppealledAgainstDecision().getIssueData().getDecisionCode();

			/*
			 * MiscAppealData miscAppealData=null;
			 * if(!AppealTypeEnum.MISC.getName().equals(parentAppealType)) {
			 * miscAppealData=(MiscAppealData) appealData;
			 * miscIssueCateg=miscAppealData.getMiscAppIssue(); }
			 */

			if (appealData != null
					&& !AppealTypeEnum.MISC.getName().equals(parentAppealType)) {
				MiscAppealData miscAppealData = (MiscAppealData) appealData;
				miscAppealType = miscAppealData.getMiscAppealType();
				miscIssueCateg = miscAppealData.getMiscAppIssue();
			} else {
				MiscAppealData miscAppealData = (MiscAppealData) lateAppeal
						.getParentAppealData();
				miscAppealType = miscAppealData.getMiscAppealType();
				miscIssueCateg = miscAppealData.getMiscAppIssue();

			}

			// String
			// reasonCode=appealData.getAppealledAgainstDecision().getReasonCode();
			if (reasonCode != null
					&& reasonCode
							.equalsIgnoreCase(AppealConstants.NO_REASON_CODE)) {
				reasonCode = GlobalConstants.NA;
			}
			String issueDesc = "";
			String issueType = "";
			if (miscAppealType != null
					&& !StringUtility.isEmpty(miscAppealType)
					&& miscAppealType.equalsIgnoreCase(MiscAppealTypeEnum.DUA
							.getName())) {
				String issueCatModified = "";
				String issueSubCatModified = "";
				String decisionModified = "";
				if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.NDD
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_NOT_UNEMP_DIST
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.NEG
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.OTHER.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.NAA
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_TER_NOT_AOA_WORK_DIST
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.LND
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_LAST_EMP_NOT_DIST_REL
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.FSE
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_NO_EMP_PROOF_SUBMIT
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.NRE
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_NO_RECENT_EMPLOYEMENT
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.NTF
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_NOT_TIMELY_FILED
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.SED
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_SELF_EMP_NOT_UNEMP_DIST
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.TRN
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_TER_OTHER
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.ROS
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_REF_OFF_SUIT_WORK
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.WPT
								.getName())) {
					issueCatModified = IssueCategoryEnum.DISASTER_UA.getName();
					issueSubCatModified = IssueSubCategoryEnum.DUA_TER_WORK_PART_TIME
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else {
					reasonCode = "";
					appealDeterminationMaster = Appeal
							.fetchMiscAppealDeterminationReasonMaster(
									miscIssueCateg, reasonCode);
				}
				if (!StringUtility.isBlank(issueCatModified)
						&& !StringUtility.isBlank(issueSubCatModified)) {
					issueType = IssueTypeEnum.NONSEPARATION.getName();
					appealDeterminationMaster = Appeal
							.fetchAppealDeterminationReasonMasterForMisc(
									issueType, issueCatModified,
									issueSubCatModified, decisionModified);
				}

			} else if (miscAppealType != null
					&& !StringUtility.isEmpty(miscAppealType)
					&& miscAppealType
							.equalsIgnoreCase(MiscAppealTypeEnum.OVERPAYMENT_APPEAL
									.getName())) {
				String issueCatModified = "";
				String issueSubCatModified = "";
				String decisionModified = "";
				if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.FRD
								.getName())) {
					issueCatModified = IssueCategoryEnum.FRAUD.getName();
					issueSubCatModified = IssueSubCategoryEnum.OTHER.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.OOD
								.getName())) {
					issueCatModified = MiscAppealTypeEnum.OVERPAYMENT_APPEAL
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.OOD
							.getName();
					decisionModified = DecisionCodeEnum.ALLOWED.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.OOR
								.getName())) {
					issueCatModified = MiscAppealTypeEnum.OVERPAYMENT_APPEAL
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.OOR
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.OOF
								.getName())) {
					issueCatModified = MiscAppealTypeEnum.OVERPAYMENT_APPEAL
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.OOF
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.OOC
								.getName())) {
					issueCatModified = MiscAppealTypeEnum.OVERPAYMENT_APPEAL
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.OOC
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
				} else {
					reasonCode = "";
					appealDeterminationMaster = Appeal
							.fetchMiscAppealDeterminationReasonMaster(
									miscIssueCateg, reasonCode);
				}
				if (!StringUtility.isEmpty(issueCatModified)
						&& !StringUtility.isEmpty(issueSubCatModified)) {
					issueType = IssueTypeEnum.NONSEPARATION.getName();
					appealDeterminationMaster = Appeal
							.fetchAppealDeterminationReasonMasterForMisc(
									issueType, issueCatModified,
									issueSubCatModified, decisionModified);
				}
			} else if (miscAppealType != null
					&& !StringUtility.isEmpty(miscAppealType)
					&& miscAppealType
							.equalsIgnoreCase(MiscAppealTypeEnum.TRA_APPEAL
									.getName())) {
				String issueCatModified = "";
				String issueSubCatModified = "";
				String decisionModified = "";
				if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.TRA
								.getName())) {
					issueCatModified = IssueCategoryEnum.TRADE_ADJUSTMENT
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.TRA
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();
					// CIF_02765||Defect_3906||SIT||Appeals||validation Added
					issueType = IssueTypeEnum.NONSEPARATION.getName();
					appealDeterminationMaster = Appeal
							.fetchAppealDeterminationReasonMasterForMisc(
									issueType, issueCatModified,
									issueSubCatModified, decisionModified);
				} else {
					reasonCode = "";
					appealDeterminationMaster = Appeal
							.fetchMiscAppealDeterminationReasonMaster(
									miscIssueCateg, reasonCode);
				}

			} else if (miscAppealType != null
					&& !StringUtility.isEmpty(miscAppealType)
					&& miscAppealType
							.equalsIgnoreCase(MiscAppealTypeEnum.CORRESPONDENCE_APPEAL
									.getName())) {
				String issueCatModified = "";
				String issueSubCatModified = "";
				String decisionModified = "";
				if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.FDS
								.getName())) {
					issueType = IssueTypeEnum.NONSEPARATION.getName();
					issueCatModified = IssueCategoryEnum.DID_NOT_REPORT
							.getName();
					issueSubCatModified = "NTFD";
					decisionModified = DecisionCodeEnum.DENY.getName();
					reasonCode = ReasonCodeEnum.N2.getName();
					appealDeterminationMaster = Appeal
							.fetchAppealDeterminationReasonMaster(issueType,
									issueCatModified, issueSubCatModified,
									decisionModified, reasonCode);
				} else {
					reasonCode = "";
					appealDeterminationMaster = Appeal
							.fetchMiscAppealDeterminationReasonMaster(
									miscIssueCateg, reasonCode);
				}

			} else if (miscAppealType != null
					&& !StringUtility.isEmpty(miscAppealType)
					&& miscAppealType
							.equalsIgnoreCase(MiscAppealTypeEnum.MONETARY_APPEAL
									.getName())) {
				String issueCatModified = "";
				String issueSubCatModified = "";
				String decisionModified = "";
				if (miscIssueCateg != null
						&& miscIssueCateg.equals(MiscAppIssueCategoryEnum.DBN
								.getName())) {
					issueType = IssueTypeEnum.NONSEPARATION.getName();
					issueCatModified = MiscAppealTypeEnum.MONETARY_APPEAL
							.getName();
					issueSubCatModified = MiscAppIssueCategoryEnum.DBN
							.getName();
					decisionModified = DecisionCodeEnum.DENY.getName();

					issueType = IssueTypeEnum.NONSEPARATION.getName();
					appealDeterminationMaster = Appeal
							.fetchAppealDeterminationReasonMasterForMisc(
									issueType, issueCatModified,
									issueSubCatModified, decisionModified);

				} else {
					reasonCode = "";
					appealDeterminationMaster = Appeal
							.fetchMiscAppealDeterminationReasonMaster(
									miscIssueCateg, reasonCode);
				}
			} else {
				reasonCode = "";
				appealDeterminationMaster = Appeal
						.fetchMiscAppealDeterminationReasonMaster(
								miscIssueCateg, reasonCode);
			}
			/* CIF_00878 Ends */
		}
		/*
		 * else
		 * if(AppealTypeEnum.MISC.getName().equals(appealData.getAppealType())
		 * || AppealTypeEnum.MISC.getName().equals(parentAppealType)) { String
		 * miscIssueCateg="";
		 * if(AppealTypeEnum.MISC.getName().equals(parentAppealType)) {
		 * MiscAppealData miscAppealData=(MiscAppealData) appealData;
		 * 
		 * miscIssueCateg=miscAppealData.getMiscAppIssue(); } else {
		 * MiscAppealData miscAppealData=(MiscAppealData) appealData;
		 * 
		 * miscIssueCateg=miscAppealData.getMiscAppIssue(); }
		 * 
		 * String issueDesc=""; if(reasonCode!=null &&
		 * reasonCode.equalsIgnoreCase(AppealConstants.NO_REASON_CODE)) {
		 * reasonCode=GlobalConstants.NA; } else { reasonCode=""; }
		 * if(miscIssueCateg!=null &&
		 * miscIssueCateg.equals(MiscAppIssueCategoryEnum.FDS.getName())) {
		 * String issueType=IssueTypeEnum.NONSEPARATION.getName(); String
		 * issueCatModified=IssueCategoryEnum.DID_NOT_REPORT.getName(); String
		 * issueSubCatModified="NTFD"; String
		 * decisionModified=DecisionCodeEnum.DENY.getName();
		 * reasonCode=ReasonCodeEnum.N2.getName(); appealDeterminationMaster =
		 * Appeal
		 * .fetchAppealDeterminationReasonMaster(issueType,issueCatModified
		 * ,issueSubCatModified,decisionModified,reasonCode); } else{
		 * appealDeterminationMaster = Appeal
		 * .fetchMiscAppealDeterminationReasonMaster(miscIssueCateg,reasonCode);
		 * } }
		 */
		else if (AppealTypeEnum.TAX_INT.getName().equals(
				appealData.getAppealType())
				|| AppealTypeEnum.TAX_INT.getName().equals(parentAppealType)) {
			/*
			 * CIF_00878 Starts,Desc:Added Appeal determination reason code
			 * master input details and result.
			 */
			// String
			// decisionCode=appealData.getAppealledAgainstDecision().getIssueData().getDecisionCode();
			/*
			 * CIF_02685||Defect_3690||Appeals AP: Referee Order canned
			 * statements should be edited before submittal to enter parameters.
			 */
			miscIssueCateg = MiscAppIssueCategoryEnum.TIC.getName();
			// String
			// reasonCode=appealData.getAppealledAgainstDecision().getReasonCode();
			if (reasonCode != null
					&& reasonCode
							.equalsIgnoreCase(AppealConstants.NO_REASON_CODE)) {
				reasonCode = GlobalConstants.NA;
			} else {
				reasonCode = "";
			}
			String issueDesc = "";
			appealDeterminationMaster = Appeal
					.fetchMiscAppealDeterminationReasonMaster(miscIssueCateg,
							reasonCode);
		} else if (AppealTypeEnum.TAXA.getName().equals(
				appealData.getAppealType())
				|| AppealTypeEnum.TAXA.getName().equals(parentAppealType)) {
			String issueCat = (String) map
					.get(AppealConstants.ISSUE_CATEGORY_KEY);
			String issueSubCat = (String) map
					.get(AppealConstants.ISSUE_SUB_CATEGORY_KEY);
			// reasonCode=appealData.getAppealledAgainstDecision().getReasonCode();
			// String
			// decisionCode=appealData.getAppealledAgainstDecision().getIssueData().getDecisionCode();
			String issueType = "";
			if (reasonCode != null
					&& reasonCode
							.equalsIgnoreCase(AppealConstants.NO_REASON_CODE)) {
				reasonCode = GlobalConstants.NA;
			} else {
				reasonCode = "";
			}
			Appeal appealBO = Appeal.findByPrimaryKey(appealData.getAppealId());
			issueType = "LATE_APPEAL_DETER";
			appealDeterminationMaster = appealBO
					.fetchAppealDeterminationReasonMaster(issueType, issueCat,
							issueSubCat, decisionCode, reasonCode);

		}
		String deputyDetermination = null;

		ArrayList deputyDeterminationList = new ArrayList();

		if (appealDeterminationMaster != null
				&& appealDeterminationMaster.getDeputyDetermination() != null) {
			deputyDetermination = appealDeterminationMaster
					.getDeputyDetermination();
			if (deputyDetermination != null
					&& !deputyDetermination
							.equalsIgnoreCase(GlobalConstants.NA)) {
				String[] depDeterminationCsv = deputyDetermination.split(",");
				determinationDetails = deputyDeterminationStatus(depDeterminationCsv);
			}

		}
		if (determinationDetails != null) {
			objAssembly.addData("DEPUTY_DETERMINATION", determinationDetails);
		}

		/* End-Fetch Adjudicate Decision for the appeal */
		return objAssembly;

	}

	/**
	 * CIF_00758 This method will update the status of the tax intercept appeal
	 * data and generate correspondence for appeal acknowledgment/ oder of
	 * dismissal
	 * 
	 * @param objectAssembly
	 *            containing the appeal data and claimant data
	 * @return IObjectAssembly
	 * @throws CloneNotSupportedException
	 *             throws CloneNotSupportedException
	 */
	@Override
	public IObjectAssembly saveTaxInterceptAppeal(
			final IObjectAssembly objectAssembly)
			throws CloneNotSupportedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveTaxInterceptAppeal");
		}

		TaxInterceptInfoBean newBean = (TaxInterceptInfoBean) objectAssembly
				.getFirstBean(TaxInterceptInfoBean.class);
		final Map<String, Object> mapValues = (Map<String, Object>) objectAssembly
				.getData(GlobalConstants.WI_MAP_VALUES);

		WorkflowTransactionService wflow = new WorkflowTransactionService();
		wflow.invokeWorkFlowOperation(WorkFlowOperationsEnum.START.getName(),
				mapValues);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Work Item has started : "
					+ WorkflowProcessTemplateConstants.Appeal.FILE_INTERCEPT_APPEAL);
		}

		// ClaimantData claimant =
		// (ClaimantDataBridge)objectAssembly.getFirstComponent(ClaimantDataBridge.class);

		AppealData appealData = newBean.getAppealData();

		// Dissmiss the appeal
		if (appealData.getAppealStatus().equals(
				AppealStatusEnum.CLOSED.getName())) {

			ReverseDecisionBean reverseDecisionBean = new ReverseDecisionBean();

			if (newBean.isLateAppeal()) {
				reverseDecisionBean
						.setAdditionalReasoning(DismissalReasonEnum.LATE
								.getDescription());
				reverseDecisionBean.setReasonDismissal(DismissalReasonEnum.LATE
						.getName());
			} else {
				reverseDecisionBean
						.setAdditionalReasoning(DismissalReasonEnum.OTHER
								.getDescription());
				reverseDecisionBean
						.setReasonDismissal(DismissalReasonEnum.OTHER.getName());
			}

			if (appealData instanceof LateAppealData) {
				LateAppealData lateAppealData = (LateAppealData) appealData;
				LateAppeal appealBo = new LateAppeal(lateAppealData);
				appealBo.saveDismissDecision(reverseDecisionBean, null, null);
				appealData = lateAppealData.getParentAppealData();

			} else {
				// there will be only two status either its late or its normal
				// tax intercept appeal
				TaxInterceptAppeal appealBo = new TaxInterceptAppeal(appealData);
				appealBo.saveRefereeDecision(reverseDecisionBean);
			}
			// CIF_01712 start
			String deniedLate = "Dismiss due to late";
			String deniedOther = "Dismiss for other reason";
			TaxInterceptAppealData taxInterceptAppealData = (TaxInterceptAppealData) appealData;
			if (newBean.isLateAppeal()) {
				taxInterceptAppealData.setRecommendation(deniedLate);
				taxInterceptAppealData
						.setReasonForDisqualifyOrDisallow(deniedLate);
			} else {
				taxInterceptAppealData.setRecommendation(deniedOther);
				taxInterceptAppealData
						.setReasonForDisqualifyOrDisallow(deniedOther);
			}

			TaxInterceptAppeal taxInterceptAppealBO = (TaxInterceptAppeal) Appeal
					.getAppealBoByAppealData(taxInterceptAppealData);
			taxInterceptAppealBO.saveOrUpdate();
			// CIF_01712 end
		} else {
			// if not dismissed
			if (appealData instanceof LateAppealData) {
				LateAppeal appealBo = new LateAppeal(appealData);
				appealBo.saveOrUpdate();
			} else {
				// there will be only two status either its late or its normal
				// tax intercept appeal
				TaxInterceptAppeal appealBo = new TaxInterceptAppeal(appealData);
				appealBo.saveOrUpdate();
			}
		}

		// generate correspondence for Dismiss/ acknowledge appeal
		this.generateTaxInterceptAckDismissCorrespondence(objectAssembly);

		wflow = new WorkflowTransactionService();

		wflow.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.COMPLETE.getName(), mapValues);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Work Item has completed : "
					+ WorkflowProcessTemplateConstants.Appeal.FILE_INTERCEPT_APPEAL);
		}

		return objectAssembly;
	}

	/**
	 * CIF_00758 This method will generate correspondence for appeal
	 * acknowledgment/ oder of dismissal
	 * 
	 * @param objectAssembly
	 *            {@link IObjectAssembly} this holds the appeal data and
	 *            claimant data
	 * 
	 */
	public void generateTaxInterceptAckDismissCorrespondence(
			final IObjectAssembly objectAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method generateTaxInterceptAckDismissCorrespondence");
		}

		TaxInterceptInfoBean newBean = (TaxInterceptInfoBean) objectAssembly
				.getFirstBean(TaxInterceptInfoBean.class);

		ClaimantData claimantData = null;
		CorrespondenceData correspondenceData = null;
		TaxInterceptAppealData taxInterceptData = null;
		EmployerData employerData = null;
		List<CorrespondenceData> correspondenceList = new ArrayList<CorrespondenceData>();

		AppealData appealData = newBean.getAppealData();
		Set<AppealPartyData> appealPartySet = appealData.getAppealPartySet();
		Iterator<AppealPartyData> appealPartySetIt = appealPartySet.iterator();

		if (appealData instanceof LateAppealData) {
			taxInterceptData = (TaxInterceptAppealData) ((LateAppealData) appealData)
					.getParentAppealData();
		} else {
			taxInterceptData = (TaxInterceptAppealData) appealData;
		}

		String taxInterceptSpuseAppealed = null;
		boolean taxInterceptSpuseAppealedTaxFlow = false;
		if (taxInterceptData.getTaxInterceptMasterData() != null) {
			taxInterceptSpuseAppealed = taxInterceptData
					.getTaxInterceptMasterData().getSpouseAppealed();
		}
		// CIF_P2_01384 || Defect_5942
		else if (taxInterceptData.getTaxStiMasterData() != null) {
			taxInterceptSpuseAppealed = taxInterceptData.getTaxStiMasterData()
					.getAppealFiledBySpouse();
			taxInterceptSpuseAppealedTaxFlow = true;
		}
		boolean spouseAppealed = appealData.getAppellant().equals(
				AppealPartyTypeEnum.TAXI_APPELLANT_SPOUSE.getName())
				&& taxInterceptSpuseAppealed
						.equals(GlobalConstants.DB_ANSWER_YES) ? true : false;

		while (appealPartySetIt.hasNext()) {

			AppealPartyData appealPartyData = appealPartySetIt.next();

			// CIF_INT_00531 || Defect_7510 start: Fox Tax Intercept tax appeal,
			// acknowledgement letter needs to be generated only for appellant ,
			// spouse appellant && Employer Attorney
			if (taxInterceptData.getTaxStiMasterData() != null) {

				if (!(appealPartyData.getPartyType().equals(
						AppealPartyTypeEnum.APPELLANT.getName()) || appealPartyData
						.getPartyType()
						.equals(AppealPartyTypeEnum.EMPLOYER_ATTORNEY.getName()))) {

					continue;
				}
			}
			// CIF_INT_00531 || Defect_7510 end

			String clmntOrEmplrFullName = null;
			AddressComponentData clmntOrEmplrOrThirdPartyAddressCmpData = null;
			String employerAttention = null;

			if (appealPartyData.getClaimantData() != null) {
				claimantData = appealPartyData.getClaimantData();
				clmntOrEmplrFullName = claimantData.getClaimantFullName();
				clmntOrEmplrOrThirdPartyAddressCmpData = claimantData
						.getMailingAddress().getAddrData();
			} else if (appealPartyData.getEmployerData() != null) {
				employerData = appealPartyData.getEmployerData();
				clmntOrEmplrFullName = employerData.getDisplayName();

				Set<?> employerContactDataSet = employerData
						.getEmployerContactData();
				Iterator<?> iterator = employerContactDataSet.iterator();
				while (iterator.hasNext()) {
					EmployerContactData employerContactData = (EmployerContactData) iterator
							.next();
					if (employerContactData.getType().equals(
							AddressTypeEnum.MA.getName())) {
						employerAttention = employerContactData.getAttention();
					}
				}

				if (EmployerTypeEnum.FED.getName().equalsIgnoreCase(
						employerData.getEmployerType())) {
					FederalEmployer fedEmployerBO = FederalEmployer
							.fetchFedEmprByPkId(employerData.getEmployerId());
					clmntOrEmplrOrThirdPartyAddressCmpData = fedEmployerBO
							.getFederalEmployerData().getContactAddress();
				} else if (EmployerTypeEnum.REG.getName().equalsIgnoreCase(
						employerData.getEmployerType())) {
					RegisteredEmployer regularEmployerBO = RegisteredEmployer
							.findByPrimaryKey(employerData.getEmployerId());
					clmntOrEmplrOrThirdPartyAddressCmpData = regularEmployerBO
							.getRegisteredEmployer().getMailAddressData();
				}/*
				 * else if
				 * (EmployerTypeEnum.OSA.getName().equalsIgnoreCase(employerData
				 * .getEmployerType())){ //TODO }else if
				 * (EmployerTypeEnum.MIL.getName
				 * ().equalsIgnoreCase(employerData.getEmployerType())){ //TODO
				 * }
				 */
			} else {
				// third party information
				clmntOrEmplrFullName = appealPartyData.getPartyName();
				clmntOrEmplrOrThirdPartyAddressCmpData = appealPartyData
						.getMailingAddress();
			}

			correspondenceData = new CorrespondenceData();
			correspondenceData
					.setDirection(CorrespondenceDirectionEnum.OUTGOING
							.getName());

			// correspondenceData.setClaimantData(claimant);
			correspondenceData
					.setMailingAddress(clmntOrEmplrOrThirdPartyAddressCmpData);

			correspondenceData.setParameter2(clmntOrEmplrFullName);
			correspondenceData
					.setParameter2Desc(CorrespondenceParameterDescriptionEnum.ADDRESSEE
							.getName());

			if (null != employerAttention) {
				correspondenceData.setParameter3(employerAttention);
				correspondenceData
						.setParameter3Desc(CorrespondenceParameterDescriptionEnum.ATTENTION
								.getName());
			}

			if (taxInterceptData.getTaxInterceptMasterData() != null) {
				correspondenceData.setParameter6(taxInterceptData
						.getTaxInterceptMasterData().getTaxInterceptMasterId()
						.toString());
				correspondenceData
						.setParameter6Desc(CorrespondenceParameterEnum.TAX_INTERCEPT_MASTER_ID
								.getName());
			}
			// CIF_P2_01384 || Defect_5942
			else if (taxInterceptData.getTaxStiMasterData() != null) {
				correspondenceData.setParameter6(String
						.valueOf(taxInterceptData.getTaxStiMasterData()
								.getTaxStiMasterId()));
				// CIF_P2_01384 || Defect_5942
				correspondenceData
						.setParameter6Desc(CorrespondenceParameterEnum.TAX_STI_MASTER_ID
								.getName());
			}
			correspondenceData.setParameter7(appealData.getAppealId());
			correspondenceData
					.setParameter7Description(CorrespondenceParameterEnum.APPEAL_ID
							.getName());

			// generate dissmissal order for tax intercept/ lottery intercept
			if (appealData.getAppealStatus().equals(
					AppealStatusEnum.CLOSED.getName())) {

				// tax intercept
				if (taxInterceptData.getTaxInterceptMasterData()
						.getRefundType()
						.equals(InterceptRefundTypeEnum.STATETAX.getName())) {
					// if denying due to late
					if (newBean.isLateAppeal()) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4837_9
										.getName());
					} else {
						// spouse filed
						if (spouseAppealed && !taxInterceptSpuseAppealedTaxFlow) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4837_3
											.getName());
						} else if (spouseAppealed
								&& taxInterceptSpuseAppealedTaxFlow) {
							// correspondenceData.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4881.getName());
						} else {
							// claimant
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4837
											.getName());
						}
					}

				} else {
					// Lottery intercept
					// if denying due to late
					if (newBean.isLateAppeal()) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4837_11
										.getName());
					} else {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4837_7
										.getName());
					}
				}

				objectAssembly
						.addBusinessError(
								"error.app.interceptappeal.orderofdismissal.success.message",
								appealData.getDocketNumber().toString());

				if (appealData.getActiveDecisionData() != null) {
					Appeal.generateCorr4680_3(appealData,
							appealData.getActiveDecisionData());
				}

			} else {
				// generate acknowledge letter for tax intercept/ lottery
				// intercept
				// CIF_P2_01384 || Defect_5942
				if (taxInterceptData.getTaxInterceptMasterData() != null) {
					if (taxInterceptData.getTaxInterceptMasterData() != null
							&& taxInterceptData
									.getTaxInterceptMasterData()
									.getRefundType()
									.equals(InterceptRefundTypeEnum.STATETAX
											.getName())) {

						if (spouseAppealed) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4838
											.getName());
							objectAssembly
									.addBusinessError(
											"error.app.interceptappeal.acknowledgment.statetax.apportionment.success.message",
											appealData.getDocketNumber()
													.toString());
						} else {
							// appeal filed by claimant
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4838_3
											.getName());
							objectAssembly
									.addBusinessError(
											"error.app.interceptappeal.acknowledgment.statetax.success.message",
											appealData.getDocketNumber()
													.toString());
						}
					} else {
						// intercept type is lottery
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_4838_5
										.getName());
						objectAssembly
								.addBusinessError(
										"error.app.interceptappeal.acknowledgment.lottery.success.message",
										appealData.getDocketNumber().toString());
					}
				}
				// CIF_P2_01384 || Defect_5942
				if (taxInterceptData.getTaxStiMasterData() != null) {
					if (appealPartyData.getPartyType().equals(
							AppealPartyTypeEnum.APPELLANT.getName())
							&& appealData.getAppellant().equals(
									AppealPartyTypeEnum.TAXI_APPELLANT_SPOUSE
											.getName())) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeTaxEnum.ALJ_INTERCEPT_ACKNOWLEDGEMENT_SPOUSE
										.getName());
					} else if (appealPartyData.getPartyType().equals(
							AppealPartyTypeEnum.APPELLANT.getName())
							&& appealData.getAppellant().equals(
									AppealPartyTypeEnum.TAXI_APPELLANT_EMPLOYER
											.getName())) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeTaxEnum.ALJ_INTERCEPT_ACKNOWLEDGEMENT_EMPLOYER
										.getName());
					}

					if (appealPartyData.getPartyType().equals(
							AppealPartyTypeEnum.EMPLOYER_ATTORNEY.getName())) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeTaxEnum.ALJ_INTERCEPT_ACKNOWLEDGEMENT_ATTORNEY
										.getName());
					}

					// CIF_P2_01452 || Defect_6214
					objectAssembly
							.addBusinessError(
									"error.app.interceptappeal.acknowledgment.statetax.success.message",
									appealData.getDocketNumber().toString());

				}

			}

			correspondenceList.add(correspondenceData);
		}
		// CIF_INT_01157 || Defect_8108 start
		if (claimantData == null
				&& taxInterceptData.getTaxInterceptMasterData() != null) {
			String claimantSsn = taxInterceptData.getTaxInterceptMasterData()
					.getSsn();
			if (spouseAppealed && StringUtility.isNotBlank(claimantSsn)) {
				Claimant claimant = Claimant.findBySsn(claimantSsn);
				claimantData = claimant.getClaimantData();
			}
		}
		if (employerData == null
				&& taxInterceptData.getTaxStiMasterData() != null) {
			employerData = taxInterceptData.getTaxStiMasterData()
					.getEmployerData();
		}

		// CIF_INT_01157 || Defect_8108 end
		Correspondence correspondence = null;
		Iterator<CorrespondenceData> itCorr = correspondenceList.iterator();
		while (itCorr.hasNext()) {
			CorrespondenceData corrData = (CorrespondenceData) itCorr.next();
			if (claimantData != null) {
				corrData.setClaimantData(claimantData);
			}
			if (employerData != null) {
				corrData.setEmployerData(employerData);
			}
			
			//@cif_wy(storyNumber="P1-APP-37", requirementId="FR_1632",designDocName="Appendix B -  Appeals - Correspondence",designDocSection="1", dcrNo="", mddrNo="",impactPoint="start")
			Object lMultiStateObject = MultiStateClassFactory.getObject("gov.state.uim.cin.service.CinService", BaseOrStateEnum.STATE, null, null, Boolean.FALSE);
			
			boolean isCorrCreate = ((CinService) lMultiStateObject).isCorrGenerate(CorrespondenceCodeEnum.MODES_4680_3
					.getName());

			if(isCorrCreate)
			{
			correspondence = new Correspondence(corrData);
			correspondence.saveOrUpdate();
			}
			//@cif_wy(storyNumber="P1-APP-37", requirementId="FR_1632",designDocName="Appendix B -  Appeals - Correspondence",designDocSection="1", dcrNo="", mddrNo="",impactPoint="end")
		}

	}

	/**
	 * CIF_00760 This method will update the status of the non mon/miscellaneous
	 * late appeal data and generate correspondence for appeal acknowledgment/
	 * oder of dismissal
	 * 
	 * @param objectAssembly
	 *            containing the appeal data and claimant data
	 * @return IObjectAssembly
	 * @throws CloneNotSupportedException
	 *             throws CloneNotSupportedException
	 * @throws BaseApplicationException
	 */
	@Override
	public IObjectAssembly saveMiscNonMonLateAppeal(
			final IObjectAssembly objectAssembly)
			throws CloneNotSupportedException, BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveMiscNonMonLateAppeal");
		}

		TaxInterceptInfoBean newBean = (TaxInterceptInfoBean) objectAssembly
				.getFirstBean(TaxInterceptInfoBean.class);

		final Map<String, Object> mapValues = (Map<String, Object>) objectAssembly
				.getData(GlobalConstants.WI_MAP_VALUES);

		WorkflowTransactionService wflow = new WorkflowTransactionService();
		wflow.invokeWorkFlowOperation(WorkFlowOperationsEnum.START.getName(),
				mapValues);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Work Item has started : "
					+ WorkflowProcessTemplateConstants.Appeal.FILE_LATE_APPEAL);
		}

		// ClaimantData claimant =
		// (ClaimantDataBridge)objectAssembly.getFirstComponent(ClaimantDataBridge.class);

		AppealData appealData = newBean.getAppealData();

		// Only late appeal
		if (appealData.getAppealStatus().equals(
				AppealStatusEnum.CLOSED.getName())) {

			// Dissmiss the appeal
			ReverseDecisionBean reverseDecisionBean = new ReverseDecisionBean();

			reverseDecisionBean.setAdditionalReasoning(DismissalReasonEnum.LATE
					.getDescription());
			reverseDecisionBean.setReasonDismissal(DismissalReasonEnum.LATE
					.getName());

			// CIF_01376 to fetch the parent appeal decision associated with it
			appealData = Appeal.fetchAppealDataAppealedDecisionsByAppealId(
					appealData.getAppealId()).getAppealData();
			boolean isTaxAppeal = false;
			AppealData parentAppealData = null;
			if (appealData != null && appealData instanceof LateAppealData) {
				parentAppealData = ((LateAppealData) appealData)
						.getParentAppealData();
				if (parentAppealData != null
						&& parentAppealData instanceof TaxAppealData) {
					isTaxAppeal = true;
				}
			}
			if (appealData != null && appealData instanceof TaxAppealData) {
				isTaxAppeal = true;
			}

			LateAppeal appealBo = new LateAppeal(appealData);
			appealBo.saveDismissDecision(reverseDecisionBean, null, null);

			// CIF_00280 Starts,Need to uncomment this after the order of
			// dismissal is finalised
			this.generateMiscNonMonLateAckDismissCorr(objectAssembly);
			if (appealData.getActiveDecisionData() != null) {
				if (isTaxAppeal) {
					Appeal.generateCorr4680_3_T(appealData,
							appealData.getActiveDecisionData());
				} else {
					Appeal.generateCorr4680_3(appealData,
							appealData.getActiveDecisionData());
				}
			}

			objectAssembly
					.addBusinessError(
							"error.app.filelateappeal.orderofdismissal.success.message",
							appealData.getDocketNumber().toString());

		} else {
			// if not dismissed
			// No need to generate correspondence for acknowledgement if set for
			// hearing
			this.generateMiscNonMonLateAckDismissCorr(objectAssembly);
			LateAppeal appealBo = new LateAppeal(appealData);
			appealBo.saveOrUpdate();

			objectAssembly.addBusinessError(
					"error.app.filelateappeal.schedulehearing.success.message",
					appealData.getDocketNumber().toString());

		}

		wflow = new WorkflowTransactionService();

		wflow.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.COMPLETE.getName(), mapValues);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Work Item has completed : "
					+ WorkflowProcessTemplateConstants.Appeal.FILE_LATE_APPEAL);
		}

		return objectAssembly;
	}

	/**
	 * CIF_00760 This method will generate correspondence for appeal
	 * acknowledgment/ oder of dismissal
	 * 
	 * @param objectAssembly
	 *            {@link IObjectAssembly} this holds the appeal data and
	 *            claimant data
	 * @throws BaseApplicationException
	 * 
	 */
	public void generateMiscNonMonLateAckDismissCorr(
			final IObjectAssembly objectAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method generateMiscNonMonLateAckDismissCorr");
		}

		TaxInterceptInfoBean newBean = (TaxInterceptInfoBean) objectAssembly
				.getFirstBean(TaxInterceptInfoBean.class);

		HearingData hearingData = (HearingData) objectAssembly
				.getFirstComponent(HearingData.class);

		ClaimantData claimant = null;
		EmployerData employerData = null;
		AppealData appealData = newBean.getAppealData();
		Long hearingId = Long.valueOf(0);
		/*
		 * if(hearingData != null) { hearingId = hearingData.getHearingId(); }
		 * else { HearingData
		 * hearingDataNew=Hearing.getLastHearingData(appealData
		 * .getHearingSet()); if(hearingDataNew!=null) {
		 * hearingId=hearingDataNew.getHearingId(); }
		 * 
		 * }
		 */

		HearingData hearingDataNew = Hearing.getLastHearingData(appealData
				.getHearingSet());
		if (hearingDataNew != null) {
			hearingId = hearingDataNew.getHearingId();
		}
		Set<AppealPartyData> appealPartySet = appealData.getAppealPartySet();

		Iterator<AppealPartyData> appealPartySetIt = appealPartySet.iterator();

		List<CorrespondenceData> correspondenceList = new ArrayList<CorrespondenceData>();

		while (appealPartySetIt.hasNext()) {
			Boolean isEmployer = Boolean.FALSE;
			AppealPartyData appealPartyData = appealPartySetIt.next();
			String clmntOrEmplrFullName = null;
			AddressComponentData clmntOrEmplrOrThirdPartyAddressCmpData = null;
			String employerAttention = null;

			if (appealPartyData.getClaimantData() != null) {
				claimant = appealPartyData.getClaimantData();
				clmntOrEmplrFullName = claimant.getClaimantFullName();
				clmntOrEmplrOrThirdPartyAddressCmpData = claimant
						.getMailingAddress().getAddrData();
			} else if (appealPartyData.getEmployerData() != null) {
				employerData = appealPartyData.getEmployerData();
				clmntOrEmplrFullName = employerData.getDisplayName();

				Set<?> employerContactDataSet = employerData
						.getEmployerContactData();
				Iterator<?> iterator = employerContactDataSet.iterator();
				while (iterator.hasNext()) {
					EmployerContactData employerContactData = (EmployerContactData) iterator
							.next();
					if (employerContactData.getType().equals(
							AddressTypeEnum.MA.getName())) {
						employerAttention = employerContactData.getAttention();
					}
				}

				if (EmployerTypeEnum.FED.getName().equalsIgnoreCase(
						employerData.getEmployerType())) {
					FederalEmployer fedEmployerBO = FederalEmployer
							.fetchFedEmprByPkId(employerData.getEmployerId());
					clmntOrEmplrOrThirdPartyAddressCmpData = fedEmployerBO
							.getFederalEmployerData().getContactAddress();
				} else if (EmployerTypeEnum.REG.getName().equalsIgnoreCase(
						employerData.getEmployerType())) {
					RegisteredEmployer regularEmployerBO = RegisteredEmployer
							.findByPrimaryKey(employerData.getEmployerId());
					clmntOrEmplrOrThirdPartyAddressCmpData = regularEmployerBO
							.getRegisteredEmployer().getMailAddressData();
				}/*
				 * else if
				 * (EmployerTypeEnum.OSA.getName().equalsIgnoreCase(employerData
				 * .getEmployerType())){ //TODO }else if
				 * (EmployerTypeEnum.MIL.getName
				 * ().equalsIgnoreCase(employerData.getEmployerType())){ //TODO
				 * }
				 */
				isEmployer = Boolean.TRUE;
			} else {
				// third party information
				clmntOrEmplrFullName = appealPartyData.getPartyName();
				clmntOrEmplrOrThirdPartyAddressCmpData = appealPartyData
						.getMailingAddress();
			}

			Map map = Appeal
					.getIssueDescriptionAndDecisionDateFromAppealData(appealData);
			String issueDescription = (String) map
					.get(AppealConstants.ISSUE_DESCRIPTION);
			String previousDecisionDate = (String) map
					.get(AppealConstants.PREVIOUS_DECISION_DATE);

			String reasonCode = "";
			reasonCode = (String) map.get(AppealConstants.REASON_CODE_KEY);

			String decisionCode = "";
			decisionCode = (String) map.get(AppealConstants.DECISION_CODE_KEY);

			CorrespondenceData correspondenceData = null;

			correspondenceData = new CorrespondenceData();
			correspondenceData
					.setDirection(CorrespondenceDirectionEnum.OUTGOING
							.getName());

			// correspondenceData.setClaimantData(claimant);
			correspondenceData
					.setMailingAddress(clmntOrEmplrOrThirdPartyAddressCmpData);

			if (null != claimant) {
				// HibernateUtil.getJTAAwareSession().get(ClaimantData.class,
				// claimant.getPkId());
				correspondenceData.setClaimantData(claimant);
			}
			if (null != employerData) {
				correspondenceData.setEmployerData(employerData);
			}

			/* Addeded parameter */
			// Set parameter 1 equal to the party name
			correspondenceData.setParameter1(clmntOrEmplrFullName);
			correspondenceData
					.setParameter1Desc(CorrespondenceParameterDescriptionEnum.ADDRESSEE
							.getName());

			// DESCRIPTION OF ISSUE DEPENDING ON APPEAL TYPE (LATE, MISC,
			// NONMON, ETC)
			// correspondenceData.setParameter2(issueDescription);
			// correspondenceData.setParameter2Desc(CorrespondenceParameterDescriptionEnum.ISSUE_DESCRIPTION.getName());
			String determinationDetails = "";
			determinationDetails = (String) objectAssembly
					.getData("DEPUTY_DETERMINATION_FINAL");
			if (StringUtility.isNotBlank(determinationDetails)) {
				correspondenceData.setParameter2(determinationDetails);
				correspondenceData
						.setParameter2Desc(CorrespondenceParameterDescriptionEnum.ISSUE_DESCRIPTION
								.getName());
			}

			// DATE OF PREVIOUS DECISION IF ONE EXISTS
			correspondenceData.setParameter3(previousDecisionDate);
			correspondenceData
					.setParameter3Desc(CorrespondenceParameterDescriptionEnum.PREVIOUS_DECISION_DATE
							.getName());

			// REASON FOR DISMISSAL CODE
			// correspondenceData.setParameter4(reverseDecisionBean.getReasonDismissal());
			// correspondenceData.setParameter4Desc(CorrespondenceParameterDescriptionEnum.DIMISSAL_CODE.getName());

			// APPEAL PARTY ID FOR COVER LETTER MAIL ADDRESS SORT
			// CIF_02997||Defect_4628||SIT||Appeals||AP.089.01: MODES-L-30
			if (appealPartyData.getAppealPartyId() != null) {
				correspondenceData.setParameter4(appealPartyData
						.getAppealPartyId().toString());
			}
			correspondenceData
					.setParameter4Desc(CorrespondenceParameterDescriptionEnum.APPEAL_PARTY_ID
							.getName());

			// APPEAL ID
			correspondenceData.setParameter7(appealData.getAppealId());
			correspondenceData
					.setParameter7Description(CorrespondenceParameterDescriptionEnum.APPEAL_ID
							.getName());

			// HEARING ID
			if (hearingId != null) {
				correspondenceData.setParameter6(hearingId.toString());
				correspondenceData
						.setParameter6Desc(CorrespondenceParameterDescriptionEnum.HEARING_ID
								.getName());
			}
			/* End of added parameter */
			/*
			 * correspondenceData.setParameter2(clmntOrEmplrFullName);
			 * correspondenceData
			 * .setParameter2Desc(CorrespondenceParameterDescriptionEnum
			 * .ADDRESSEE.getName());
			 * 
			 * if(null != employerAttention){
			 * correspondenceData.setParameter3(employerAttention);
			 * correspondenceData
			 * .setParameter3Desc(CorrespondenceParameterDescriptionEnum
			 * .ATTENTION.getName()); }
			 * 
			 * correspondenceData.setParameter4(taxInterceptData.
			 * getTaxInterceptMasterData().
			 * getTaxInterceptMasterId().toString());
			 * correspondenceData.setParameter4Desc
			 * (CorrespondenceParameterEnum.TAX_INTERCEPT_MASTER_ID.getName());
			 * 
			 * correspondenceData.setParameter5(appealData.getAppealId().toString
			 * ());
			 * correspondenceData.setParameter5Desc(CorrespondenceParameterEnum
			 * .APPEAL_ID.getName());
			 */

			// generate dissmissal order for tax intercept/ lottery intercept
			if (appealData.getAppealStatus().equals(
					AppealStatusEnum.CLOSED.getName())) {

				// tax intercept
				/*
				 * if(taxInterceptData.getTaxInterceptMasterData().getRefundType(
				 * ). equals(InterceptRefundTypeEnum.STATETAX.getName())){
				 * 
				 * //spouse filed
				 * if(appealData.getAppellant().equals(AppealPartyTypeEnum
				 * .TAXI_APPELLANT_SPOUSE.getName()) &&
				 * taxInterceptData.getTaxInterceptMasterData
				 * ().getSpouseAppealed()
				 * .equals(GlobalConstants.DB_ANSWER_YES)){
				 * 
				 * correspondenceData.setCorrespondenceCode(CorrespondenceCodeEnum
				 * .MODES_4837_3.getName()); }else{ //claimant
				 * correspondenceData
				 * .setCorrespondenceCode(CorrespondenceCodeEnum
				 * .MODES_4837.getName()); }
				 * 
				 * }else{ //Lottery intercept
				 * correspondenceData.setCorrespondenceCode
				 * (CorrespondenceCodeEnum.MODES_4837_7.getName()); }
				 */
				/* CIF_P2_01341 || Added code for MODES-4900 and MODES-4900-3 */
				if (appealData.getAppealType().equals(
						AppealTypeEnum.LATE.getName())) {
					LateAppealData lateAppealData = (LateAppealData) appealData;
					if (lateAppealData.getParentAppealData().getAppealType()
							.equals(AppealTypeEnum.TAXA.getName())
							&& map.get(AppealConstants.ISSUE_CATEGORY) != null) {
						String taxAppealIssue = (String) map
								.get(AppealConstants.ISSUE_CATEGORY);
						if (taxAppealIssue != null
								&& taxAppealIssue
										.equals(MstDeterminationTypeEnum.WAGE
												.getDescription())) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4901
											.getName());
						} else if (taxAppealIssue != null
								&& taxAppealIssue
										.equals(MstDeterminationTypeEnum.ASMT
												.getDescription())) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4900_3
											.getName());
						} else if (taxAppealIssue != null
								&& !taxAppealIssue
										.equals(MstDeterminationTypeEnum.WAGE
												.getDescription())
								&& !taxAppealIssue
										.equals(MstDeterminationTypeEnum.ASMT
												.getDescription())) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4900
											.getName());
						}
					} else {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_3434_9
										.getName());
					}

				}
				/*
				 * objectAssembly.addBusinessError(
				 * "error.app.interceptappeal.orderofdismissal.success.message",
				 * appealData.getDocketNumber().toString());
				 */
			} else {
				// save the data in T_CORRESPONDENCE
				if (appealData.getAppealLevel() != null
						&& appealData.getAppealLevel().equals(
								MiscAppealLevelEnum.ADLJ.getName())) {
					if (appealPartyData.getPartyType().equalsIgnoreCase(
							AppealPartyTypeEnum.APPELLANT.getName())) {
						correspondenceData
								.setParameter2(GlobalConstants.FILE_APPEAL_REFEREE_ACKNOWLEDGEMENT_MESSAGE_TO_APPELLANT);
						correspondenceData
								.setParameter2Desc("Referee Acknowledgement Message to Appellant.");
					} else {
						correspondenceData
								.setParameter2(GlobalConstants.FILE_APPEAL_REFEREE_ACKNOWLEDGEMENT_MESSAGE_TO_OTHER_PARTY);
						correspondenceData
								.setParameter2Desc("Referee Acknowledgement Message to Other Party");
					}
				}

				// CIF_INT_00345
				correspondenceData
						.setCorrespondenceCode(CorrespondenceCodeEnum.ALJ_ACKNOWLEDGEMENT
								.getName());
				boolean isTaxAppeal = false;
				boolean isMiscAppeal = false;
				AppealData parentAppealData = null;
				MiscAppealData miscAppealData = null;
				if (appealData != null && appealData instanceof LateAppealData) {
					parentAppealData = ((LateAppealData) appealData)
							.getParentAppealData();
					if (parentAppealData != null
							&& parentAppealData instanceof TaxAppealData) {
						isTaxAppeal = true;
					} else if (parentAppealData != null
							&& parentAppealData instanceof MiscAppealData) {
						isMiscAppeal = true;
						miscAppealData = (MiscAppealData) parentAppealData;
					}
				}

				if (appealData instanceof TaxAppealData) {
					isTaxAppeal = true;
				} else if (appealData instanceof MiscAppealData) {
					isMiscAppeal = true;
					miscAppealData = (MiscAppealData) appealData;
				}

				if (isMiscAppeal && miscAppealData != null) {
					if (MiscAppealTypeEnum.WAGE_CREDIT.getName().equals(
							miscAppealData.getMiscAppealType())
							&& MiscAppIssueCategoryEnum.WAG.getName()
									.equalsIgnoreCase(
											miscAppealData.getMiscAppIssue())) {
						if (AppealPartyTypeEnum.EMPLOYER_ATTORNEY.getName()
								.equals(appealPartyData.getPartyType())
								|| AppealPartyTypeEnum.DIVISION_ATTORNEY
										.getName().equals(
												appealPartyData.getPartyType())
								|| AppealPartyTypeEnum.CLAIMANT_ATTORNEY
										.getName().equals(
												appealPartyData.getPartyType())) {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4878
											.getName());
						} else {
							correspondenceData
									.setCorrespondenceCode(CorrespondenceCodeTaxEnum.MODES_4877
											.getName());
						}
					}
				}
				if (isTaxAppeal) {
					if (AppealPartyTypeEnum.EMPLOYER_ATTORNEY.getName().equals(
							appealPartyData.getPartyType())) {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.ALJ_TAX_ACKNOWLEDGEMENT_ATTORNEY
										.getName());
					} else {
						correspondenceData
								.setCorrespondenceCode(CorrespondenceCodeEnum.ALJ_TAX_ACKNOWLEDGEMENT_EMPLOYER
										.getName());
					}
				}

			}
			/* CIF_00280 Ends */
			if (isEmployer
					&& appealData.getAppealAlternateAddressData() != null) {
				AppealAlternateAddressData alternateAddressData = appealData
						.getAppealAlternateAddressData();
				AddressBean objAddBean = new AddressBean();
				objAddBean.setAddress1(alternateAddressData.getAddressLine1());
				objAddBean.setAddress2(alternateAddressData.getAddressLine2());
				objAddBean.setCity(alternateAddressData.getCity());
				objAddBean.setState(alternateAddressData.getState());
				objAddBean.setZip(alternateAddressData.getZipCode());
				objAddBean.setCountry(alternateAddressData.getCountry());
				clmntOrEmplrOrThirdPartyAddressCmpData
						.setAddressFromAddressBean(objAddBean);
				correspondenceData
						.setMailingAddress(clmntOrEmplrOrThirdPartyAddressCmpData);
			}
			// correspondenceList.add(correspondenceData);
			Correspondence Correspondence = new Correspondence(
					correspondenceData);
			// Correspondence.flush();
			Correspondence.saveOrUpdate();
			Correspondence.flush();
		}
		/*
		 * Correspondence correspondence = null; Iterator<CorrespondenceData>
		 * itCorr = correspondenceList.iterator(); while (itCorr.hasNext()) {
		 * CorrespondenceData correspondenceData = (CorrespondenceData)
		 * itCorr.next();
		 * 
		 * if(null != claimant){
		 * //HibernateUtil.getJTAAwareSession().get(ClaimantData.class,
		 * claimant.getPkId());
		 * correspondenceData.setClaimantData((ClaimantData)
		 * HibernateUtil.getJTAAwareSession().get(ClaimantData.class,
		 * claimant.getPkId())); } if(null != employerData){
		 * correspondenceData.setEmployerData(employerData); }
		 * 
		 * correspondence = new Correspondence(correspondenceData);
		 * correspondence.saveOrUpdate(); }
		 */

	}

	public String deputyDeterminationStatus(String[] selectVal) {
		String currMessageStr = "";
		ArrayList currDeputyDeterminationParagraph = new ArrayList();
		if (selectVal != null) {

			String tempStr = "";
			for (int i = 0; i < selectVal.length; i++) {
				tempStr = selectVal[i];
				if (tempStr != null) {
					ArrayList newDeputyDeterminationParagraph = new ArrayList();
					newDeputyDeterminationParagraph = currDeputyDeterminationParagraph;
					AppealsDecisionLettersParagraphBuilder builder = new AppealsDecisionLettersParagraphBuilder();
					newDeputyDeterminationParagraph = builder
							.buildDeputyDeterminationParagraph(tempStr);
					if (newDeputyDeterminationParagraph != null) {
						if (newDeputyDeterminationParagraph.size() > 0) {
							MessageHolderBean newMessageBean = new MessageHolderBean();
							newMessageBean = (MessageHolderBean) newDeputyDeterminationParagraph
									.get(0);
							String newMessageStr = newMessageBean.getMessage();
							currMessageStr = newMessageStr;
						}
					}
				}

			}
		}
		return currMessageStr;
	}

	// CIF_01037 starts
	/**
	 * @param objAssembly
	 *            objectAssembly with ssn
	 * @return objectAssembly object with ClaimApplicationData
	 */
	public IObjectAssembly getClaimApplicationClaimantDetails(
			final IObjectAssembly objAssembly) {
		final ClaimApplication clmAppBO = ClaimApplication
				.findAClaimApplicationBySSN(objAssembly.getSsn());
		// final ClaimApplicationData clmAppData =
		// ClaimApplication.getPendingOrIncompleteClaimApp(objAssembly.getSsn());
		if (null != clmAppBO && null != clmAppBO.getClaimApplicationData()) {
			objAssembly.addComponent(clmAppBO.getClaimApplicationData(), true);
		}
		return objAssembly;
	}

	/**
	 * @param objAssembly
	 *            have components namely persistenceId and userId
	 * @return ObjectAssembly
	 * @description this method starts and complete a workitem.
	 */
	public IObjectAssembly completeWorkItem(final IObjectAssembly objAssembly) {
		final String persistenceOid = objAssembly.getData("wfId").toString();
		final String userId = (String) objAssembly.getData("userId");
		final WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowConstants.TASK_ID, persistenceOid);
		map.put(WorkflowConstants.USER_ID, userId);
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(), map);
		return objAssembly;
	}

	/**
	 * @param objAssembly
	 *            contains CLaimApplicationCLaimantData Object that is to be
	 *            updated into the database and also have persistanceId and
	 *            userId for wrokflow completion
	 * @return objectAssembly
	 */
	public IObjectAssembly updateSsnInfoReview(final IObjectAssembly objAssembly) {
		if (objAssembly.getData("csrResponse") != null
				&& objAssembly.getData("csrResponse").toString()
						.equalsIgnoreCase(ViewConstants.YES)) {
			final ClaimantRegService regService = new ClaimantRegService();
			regService.updateSsnInfo(objAssembly);
		}
		objAssembly.addData("csrResponse", null);
		this.completeWorkItem(objAssembly);
		return objAssembly;
	}

	// CIF_01037 ends

	/**
	 * @param objAssembly
	 *            IObjectAssembly
	 * @return objAssembly
	 */
	public IObjectAssembly completeEmployeeRemovalProcess(
			final IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method completeEmployeeRemovalProcess");
		}
		SharedWorkEmployeeData sharedWorkEmployeeData = objAssembly
				.getFirstComponent(SharedWorkEmployeeData.class);
		if (objAssembly.hasBusinessError()) {
			return objAssembly;
		}
		List clmDataList = Claim.fetchClaimBySsn(sharedWorkEmployeeData
				.getSsn());
		ClaimData clmData = null;
		if (null != clmDataList && !clmDataList.isEmpty()) {
			clmData = (ClaimData) clmDataList.get(0);
			if (clmData.getByeDate().before(new Date())) {
				clmData = null;
			}
		}
		if (null != clmData
				&& ViewConstants.YES.equals(sharedWorkEmployeeData
						.getEmployeeRemovedFlag())
				&& SeparationReasonEnum.VOLUNTARY_QUIT
						.equals(sharedWorkEmployeeData.getSeperatedReason())) {
			IssueBean ibean = new IssueBean();
			ibean.setClaimantSsn(sharedWorkEmployeeData.getSsn());
			ibean.setDateIssueDetected(new Date());
			ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
			ibean.setIssueDescription(IssueCategoryEnum.VOLUNTARY_LEAVING
					.getName());
			ibean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
			ibean.setIssueSource(IssueSourceEnum.SHARED_WORK.getName());
			ibean.setIssueStartDate(new Date());
			ibean.setIssueEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));
			ibean.setUpdatedBy(super.getContextUserId());
			Issue.createIssue(ibean);
		}

		if (null != clmData
				&& ViewConstants.YES.equals(sharedWorkEmployeeData
						.getEmployeeRemovedFlag())
				&& SeparationReasonEnum.DISCHARGE.equals(sharedWorkEmployeeData
						.getSeperatedReason())) {
			IssueBean ibean = new IssueBean();
			ibean.setClaimantSsn(sharedWorkEmployeeData.getSsn());
			ibean.setDateIssueDetected(new Date());
			ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
			ibean.setIssueDescription(IssueCategoryEnum.DISCHARGE.getName());
			ibean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
			ibean.setIssueSource(IssueSourceEnum.SHARED_WORK.getName());
			ibean.setIssueStartDate(new Date());
			ibean.setIssueEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));
			ibean.setUpdatedBy(super.getContextUserId());
			Issue.createIssue(ibean);
		}
		String persistenceOid = objAssembly.getData("WfId").toString();
		String userId = (String) objAssembly.getData("userId");

		SharedWorkEmployee sharedWorkEmployeeBO = new SharedWorkEmployee(
				sharedWorkEmployeeData);
		sharedWorkEmployeeBO.saveOrUpdate();

		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowConstants.TASK_ID, persistenceOid);
		map.put(WorkflowConstants.USER_ID, userId);
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(), map);

		return objAssembly;
	}

	/**
	 * this method will updated the workSearchContacts in T_CLAIM
	 * 
	 * @param objAssembly
	 *            having an integer value of searchContact and
	 *            ClaimApplicationClaimantData bean
	 * @return IObjectAssembly IObjectAssembly
	 */
	public IObjectAssembly updateNoOfJobs(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method updateNoOfJobs");
		}
		final RegularClaimData rClaimData = Claim
				.findActiveOrPendingRegularClaimDataBySsn(objAssembly.getSsn());
		final Integer searchContact = (Integer) objAssembly
				.getData("searchContact");
		if (null != rClaimData) {
			rClaimData.setMinWorkSearchContacts(searchContact);
			final Claim claim = new RegularClaim(rClaimData);
			claim.saveOrUpdate();
		}
		// CIF_01310 Commenting part of Code as we need not save in
		// T_CLAIM_APPLICAITON_CLAIMANT table.
		// final ClaimApplicationClaimantData clmAppClmData =
		// objAssembly.getFirstComponent(ClaimApplicationClaimantData.class);
		// if(null != clmAppClmData){
		// clmAppClmData.setMinWorkSearchContact(searchContact);
		// final ClaimApplicationClaimant clmAppClm = new
		// ClaimApplicationClaimant(clmAppClmData);
		// clmAppClm.saveOrUpdate();
		// }
		return objAssembly;
	}

	/**
	 * this method get the data from T_CLAIM_APPLICATION and checks if the
	 * claimant have filed claimed in other state or IBIQ result shows that he
	 * has claim in other state or both
	 * 
	 * @param objAssembly
	 *            holds Ssn
	 * @return IObjectAssembly with ClaimApplicationData and message
	 */
	public IObjectAssembly getClaimantDetails(IObjectAssembly objAssembly) {
		final String ssn = objAssembly.getSsn();
		final ClaimApplication claimApp = ClaimApplication
				.fetchClaimAppDataBySsn(ssn);
		final ClaimApplicationData clmAppData = claimApp
				.getClaimApplicationData();
		objAssembly.addComponent(clmAppData, true);
		boolean appAnswer = Boolean.FALSE;
		boolean Ibiqresult = Boolean.FALSE;
		if (StringUtility.isNotBlank(clmAppData.getAppliedUiFlag())
				&& clmAppData.getAppliedUiFlag().equalsIgnoreCase(
						ViewConstants.YES)) {
			appAnswer = Boolean.TRUE;
		}
		List<String> stateList = null;
		// if(appAnswer){
		final SidResponse sidResponse = SidResponse
				.getlatestSidResponseDataBySsn(ssn);
		if (null != sidResponse) {
			final SidResponseData sidResponseData = sidResponse
					.getSidResponse();
			final Set set = sidResponseData.getResponseStateWithClaim();
			stateList = new ArrayList<String>();
			for (Object object : set) {
				String state = (String) object;
				if (!state.equalsIgnoreCase(ViewConstants.HOME_STATE)) {
					stateList.add(state);
					Ibiqresult = Boolean.TRUE;
				}
			}
		}
		// }else{
		// Ibiqresult=Boolean.TRUE;
		// }
		objAssembly.addData("listStates", stateList);
		objAssembly.addData("Ibiqresult", Ibiqresult);
		objAssembly.addData("appAnswer", appAnswer);
		return objAssembly;
	}

	@Override
	public IObjectAssembly findCompletedClaimAppIdByClaimantssn(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method findCompletedClaimAppIdByClaimantssn");
		}
		ClaimApplication clmAppData = ClaimApplication
				.findCompletedClaimAppIdByClaimantssn(objAssembly.getSsn());
		if (null != clmAppData
				&& null != clmAppData.getClaimApplicationData()
				&& null != clmAppData.getClaimApplicationData()
						.getClaimApplicationClaimantData()) {
			clmAppData.getClaimApplicationData()
					.getClaimApplicationClaimantData()
					.getFederalTaxWithholdingStatus();
			objAssembly.addData("prevClmAppClmtData", clmAppData
					.getClaimApplicationData()
					.getClaimApplicationClaimantData());
			objAssembly.addData("prevClmAppData", clmAppData
					.getClaimApplicationData()
					.getClaimApplicationClaimantData());
		}
		return objAssembly;
	}

	public IObjectAssembly getBPWages(final IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getBPWages");
		}
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		List<WageData> wageDataList = Wage.getBpWageList(clmAppData.getSsn(),
				clmAppData.getBpStartDate(), clmAppData.getBpEndDate());
		if (null != wageDataList && !wageDataList.isEmpty()) {
			// CIF_01824 starts
			for (int i = 0; i < wageDataList.size(); i++) {
				// CIF_01824 ends
				if (StringUtility.isNotBlank(wageDataList.get(i).getState())
						&& !ViewConstants.HOME_STATE
								.equalsIgnoreCase(wageDataList.get(i)
										.getState())) {
					wageDataList.get(i).getEmployeeData().getEmployerData()
							.getDisplayName();
					wageDataList.remove(i);
					i--;
				}
			}
		}

		if (null != wageDataList && !wageDataList.isEmpty()) {
			objAssembly.addComponentList(wageDataList, true);
		}
		return objAssembly;

	}

	@Override
	public IObjectAssembly saveSidiRequest(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveSidiRequest");
		}
		IconService iconService = new IconService();
		iconService.saveSidiRequest(objAssembly);
		return objAssembly;
	}

	@Override
	/**
	 * this method will get all the records from T_SHARED_WEEKLY_CERT with processed flag = 0
	 * @ param IObjectAssembly objAssembly
	 */
	public IObjectAssembly getPendingRecordsForWc(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getPendingRecordsForWc");
		}
		List<SharedWeeklyCertData> sharedWeekCertDataList = SharedWeeklyCert
				.getUnprocessedSharedWorkWeeks();
		if (null != sharedWeekCertDataList && !sharedWeekCertDataList.isEmpty()) {
			objAssembly.addComponentList(sharedWeekCertDataList);
		}
		return objAssembly;
	}

	/**
	 * this method will file a weekly certification for a shared work plan , if
	 * claim is not found then it also establishes a new claim
	 * 
	 * @param IObjectAssembly
	 *            objAssembly containing SharedWeeklyCertData
	 * @return IObjectAssembly objAssembly
	 */
	public IObjectAssembly fileWcforsharedWorkPlan(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method fileWcforsharedWorkPlan");
		}
		SharedWeeklyCertData sharedWeeklyData = objAssembly
				.getFirstComponent(SharedWeeklyCertData.class);
		LOGGER.batchRecordProcess(sharedWeeklyData.getPkId().toString(),
				"T_SHARED_WEEKLY_CERT");
		if (StringUtility.isBlank(sharedWeeklyData.getMisMatchFlag())) {
			sharedWeeklyData.setMisMatchFlag(ViewConstants.NO);
		}
		// Date workFlowCreatedDate = (Date)
		// objAssembly.getData("workFlowCreatedDate");
		objAssembly.setSsn(sharedWeeklyData.getSsn());
		Date cwe = sharedWeeklyData.getCwe();
		Claimant clmnt = Claimant.fetchClaimantBySsn(sharedWeeklyData.getSsn());
		ClaimData regClaimData = null;
		boolean processWeek = Boolean.FALSE;
		boolean createClaimApp = Boolean.FALSE;
		boolean createFYI = Boolean.FALSE;

		if (null == clmnt || null == clmnt.getClaimantData().getClaimData()
				|| clmnt.getClaimantData().getClaimData().isEmpty()) {
			createClaimApp = createClaimApplication(sharedWeeklyData.getSsn(),
					objAssembly, cwe);
			processWeek = createClaimApp;
		} else if (null != clmnt
				&& null != clmnt.getClaimantData().getClaimData()
				&& !clmnt.getClaimantData().getClaimData().isEmpty()) {
			Set<ClaimData> claimDataSet = clmnt.getClaimantData()
					.getClaimData();
			// ClaimData latestRegClaimData= null;
			ClaimData claimData = null;
			for (Iterator iterator = claimDataSet.iterator(); iterator
					.hasNext();) {
				claimData = (ClaimData) iterator.next();
				if (null == regClaimData) {
					regClaimData = claimData;
				} else if (regClaimData.getByeDate().before(
						claimData.getByeDate())) {
					regClaimData = claimData;
				}
				if (EntitlementTypeEnum.REGULAR.getName().equals(
						claimData.getEntitlementType())
						&& (cwe.after(claimData.getEffectiveDate()) || DateUtility
								.isSameDay(cwe, claimData.getEffectiveDate()))
						&& (cwe.before(claimData.getByeDate()) || DateUtility
								.isSameDay(cwe, claimData.getByeDate()))) {
					processWeek = Boolean.TRUE;
					createClaimApp = Boolean.FALSE;
					break;
				}
			}
			if (!processWeek) {
				if (cwe.before(regClaimData.getEffectiveDate())) {
					createFYI = Boolean.TRUE;
				} else {
					createClaimApp = this.createClaimApplication(
							sharedWeeklyData.getSsn(), objAssembly, cwe);
					processWeek = createClaimApp;
				}

			}
		}

		if (createFYI) {
			// create workitem for the shared work plan to review that the cwe
			// lies outside the active claim period
			// Shared weekly certification cannot be file for the week {0}, as
			// the week lies outside of any of the claims BYB and BYE for SSN
			// {1}.
			Map<String, Object> mapValues = new HashMap<String, Object>();
			mapValues.put(WorkflowConstants.TYPE,
					WorkflowConstants.BUSINESS_TYPE.SSN);
			mapValues.put(WorkflowConstants.SSN_EAN_FEIN,
					sharedWeeklyData.getSsn());
			mapValues.put(WorkflowConstants.NAME, sharedWeeklyData
					.getSharedWorkEmployeeData().getFirstName()
					+ " "
					+ sharedWeeklyData.getSharedWorkEmployeeData()
							.getLastName());
			mapValues.put(WorkflowConstants.BUSINESS_KEY, sharedWeeklyData
					.getSharedWorkEmployeeData().getPkId());
			mapValues.put(WorkflowConstants.BUSINESS_DATA,
					DateFormatUtility.format(cwe));
			mapValues
					.put(WorkflowConstants.PROCESS_NAME,
							WorkflowProcessTemplateConstants.Claims.SHARED_WEEK_OUTSIDE_ACTIVE_CLAIM);
			WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
			wfTransactionService
					.invokeWorkFlowOperation(
							WorkFlowOperationsEnum.CREATE_WORKITEM.getName(),
							mapValues);
			sharedWeeklyData.setProcessedFlag(ViewConstants.YES);
			SharedWeeklyCert sharedWeeklycert = new SharedWeeklyCert(
					sharedWeeklyData);
			sharedWeeklycert.merge();
		}

		if (createClaimApp) {
			// this is added so as to use getOrCreateClaimApp as its is ... and
			// no changes are to be made . this will not establish NBY claim
			objAssembly.addData("NBY_INDICATOR", "1");
			Calendar effDate = Calendar.getInstance();
			effDate.setTime(cwe);
			effDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			objAssembly.addData("NBYEFFECTIVEDATE", effDate.getTime());
			objAssembly = this.getOrCreateClaimApp(objAssembly);
			if (null != clmnt && null != clmnt.getClaimantData()
					&& null != clmnt.getClaimantData().getClaimData()
					&& !clmnt.getClaimantData().getClaimData().isEmpty()) {
				ClaimantData clmantData = clmnt.getClaimantData();
				// check if details provided by Employer are different than
				// previous claims
				SharedWorkEmployeeData sharedWorkEmployeeData = sharedWeeklyData
						.getSharedWorkEmployeeData();
				if (!sharedWorkEmployeeData.getFirstName().equalsIgnoreCase(
						clmantData.getFirstName())
						|| !sharedWorkEmployeeData.getLastName()
								.equalsIgnoreCase(clmantData.getLastName())
						|| !(null != sharedWorkEmployeeData.getMiddleInitial() ? sharedWorkEmployeeData
								.getMiddleInitial() : "")
								.equalsIgnoreCase(clmantData.getMiddleInitial())
						|| !(null != sharedWorkEmployeeData.getSuffix() ? sharedWorkEmployeeData
								.getSuffix() : "").equalsIgnoreCase(clmantData
								.getSuffix())
						|| !DateUtility.isSameDay(
								sharedWorkEmployeeData.getDateOfBirth(),
								clmantData.getDateOfBirth())
						|| !sharedWorkEmployeeData.getCitizenship()
								.equalsIgnoreCase(
										clmantData.getClaimantProfileData()
												.getCitizenship())
						|| !(null != sharedWorkEmployeeData.getGender() ? sharedWorkEmployeeData
								.getGender() : "").equalsIgnoreCase(clmantData
								.getClaimantProfileData().getGender())) {
					// CreateFYI Workitem for data mismatch for CSR
					// Create WorkItem for CSR to review for data mismatch
					sharedWeeklyData.setMisMatchFlag(ViewConstants.YES);
					Date wfCreatedDate = (Date) objAssembly
							.getData("wfCreatedDate");
					if (null == wfCreatedDate) {
						wfCreatedDate = new Date();
						objAssembly.addData("wfCreatedDate", wfCreatedDate);
					}
					sharedWeeklyData.setWfCreatedDate(wfCreatedDate);
					this.populateDataForSharedClaimApp(objAssembly,
							Boolean.FALSE);
				} else {
					this.populateDataForSharedClaimApp(objAssembly,
							Boolean.TRUE);
				}
			} else {
				// set values for the claim_application_claimant
				this.populateDataForSharedClaimApp(objAssembly, Boolean.TRUE);
			}

			objAssembly = this.getBaseEmployers(objAssembly);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
			ClaimApplicationDataBridge clmAppData = objAssembly
					.getFirstComponent(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
			List employerList = objAssembly
					.getComponentList(ClaimApplicationEmployerData.class);
			// CIF_02910 adding last employer to shared work plan established
			// claim
			boolean sharedEmployerFound = Boolean.FALSE;
			if ((employerList != null) && (!employerList.isEmpty())) {
				LOGGER.info("There are " + employerList.size()
						+ " employers for SSN = " + objAssembly.getSsn());
				SharedWorkEmployerData sharedData = sharedWeeklyData
						.getSharedWorkEmployeeData()
						.getSharedWorkEmployerData();
				for (Iterator i = employerList.iterator(); i.hasNext();) {
					ClaimApplicationEmployerData emprdata = (ClaimApplicationEmployerData) i
							.next();
					emprdata.setWorkFlag(GlobalConstants.DB_ANSWER_YES);
					emprdata.setReasonSeparation(SeparationReasonEnum.LACK_OF_WORK
							.getName());
					emprdata.setPensionFlag(GlobalConstants.DB_ANSWER_NO);
					emprdata.setClaimApplicationData(clmAppData);
					if (!(emprdata.getEmployerData() instanceof RegisteredEmployerData)) {
						Employer employer = Employer.findByPrimaryKey(emprdata
								.getEmployerData().getEmployerId());
						emprdata.setEmployerData(employer.getEmployerData());
					}
					if (emprdata
							.getEmployerData()
							.getEmployerId()
							.equals(sharedData.getEmployerData()
									.getEmployerId())) {
						emprdata.setLastEmployerFlag(ViewConstants.YES);
						emprdata.setEndDate(sharedWeeklyData.getCwe());
						sharedEmployerFound = Boolean.TRUE;
					} else {
						emprdata.setLastEmployerFlag(ViewConstants.NO);
						emprdata.setEndDate(emprdata.getClaimApplicationData()
								.getBpEndDate().getTime());
					}

					// to save/update the Claim Application Employer Data
					ClaimApplicationEmployer claimAppEmp = new ClaimApplicationEmployer(
							emprdata);
					claimAppEmp.saveOrUpdate();
				}
				List<ClaimApplicationEmployerData> claimEmpList = new ArrayList<ClaimApplicationEmployerData>();
				if (null != employerList) {
					claimEmpList.addAll(employerList);
				}
				if (!sharedEmployerFound) {
					ClaimApplicationEmployerData clmAppEmpData = new ClaimApplicationEmployerData();
					clmAppEmpData.setEmployerName(sharedData.getEmployerData()
							.getEmployerName());
					// EmployerData empData = new RegisteredEmployerData();
					// empData.setEmployerId(sharedData.getEmployerData().getEmployerId());
					clmAppEmpData.setEmployerData(sharedData.getEmployerData());
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(clmAppData.getBpEndDate().getTime());
					// Set the employment date as one day after base period end
					// date.
					cal.add(Calendar.DAY_OF_YEAR, 1);
					clmAppEmpData.setStartDate(cal.getTime());
					clmAppEmpData.setEndDate(sharedWeeklyData.getCwe());
					clmAppEmpData
							.setReasonSeparation(SeparationReasonEnum.LACK_OF_WORK
									.getName());
					clmAppEmpData.setEmployerType(EmployerTypeEnum.REG
							.getName());
					clmAppEmpData.setWorkFlag(GlobalConstants.DB_ANSWER_YES);
					clmAppEmpData.setClaimApplicationData(clmAppData);
					clmAppEmpData.setLastEmployerFlag(ViewConstants.YES);
					// clmAppData.getClaimApplicationEmployerData().add(clmAppEmpData);
					ClaimApplicationEmployer claimAppEmp = new ClaimApplicationEmployer(
							clmAppEmpData);
					claimEmpList.add(clmAppEmpData);
					claimAppEmp.saveOrUpdate();
				}
				// CIF_02910 ends
				objAssembly.addComponentList(claimEmpList, true);
			} else {
				LOGGER.info("There are no employers for SSN = "
						+ objAssembly.getSsn());
			}
			objAssembly = this.processInitialClaim(objAssembly);
			if (ViewConstants.NO.equals(sharedWeeklyData
					.getSharedWorkEmployeeData().getCitizenship())
					|| ViewConstants.YES.equals(sharedWeeklyData
							.getSharedWorkEmployeeData().getPensionFlag())) {
				this.createAlienPensionIssue(objAssembly);
			}
			try {
				IconService iconService = new IconService();
				objAssembly.addData("SID_PROCESS_NAME",
						SIDProcessNameEnum.INIT.getName());
				objAssembly = iconService
						.sendSidRequestThroughBatch(objAssembly);
			} catch (BaseApplicationException e) {
				LOGGER.info("Error occured during the process of sending SID request for ssn "
						+ objAssembly.getSsn() + "." + e.getMessage());
				objAssembly
						.addBusinessError("error.access.sid.request.not.sent");
			}
			regClaimData = objAssembly
					.getFirstComponent(RegularClaimData.class);
			if (null == regClaimData) {
				processWeek = Boolean.FALSE;
			}
		}
		// Filing WC
		if (processWeek) {
			WeeklyCertData weekCertData = WeeklyCert
					.getWeeklyCertificationForSharedPlan(
							sharedWeeklyData.getSsn(), cwe);
			RegularClaim.addSharedPlanWorkSearch(sharedWeeklyData.getSsn(),
					regClaimData, sharedWeeklyData.getSharedWorkEmployeeData()
							.getSharedWorkEmployerData(), cwe);
			// CIF_02173 starts
			if (null == weekCertData) {
				// check if the week that is going to be filed is before waiting
				// week if yes then reprocess all weeks
				// this check is only required if the week is new if already
				// processed then this is not required
				ProcessedWeeklyCertData waitingWeek = WeeklyCertificationJrnlBO
						.findLatestWaitingWeekDataByClaimId(regClaimData
								.getClaimId());
				if (null != waitingWeek
						&& waitingWeek.getWeeklyCertData()
								.getClaimWeekEndDate().after(cwe)) {
					// all weeks nees to be reprocessed
					List<WeeklyCertData> weeklyCerts = WeeklyCert
							.getCertifiedWeeksForClaimantBetweenDates(
									regClaimData.getClaimantData().getPkId(),
									regClaimData.getEffectiveDate(),
									regClaimData.getByeDate());
					WeeklyCertificationJrnlBO weeklyBO = null;
					for (WeeklyCertData weeklyCertData : weeklyCerts) {
						weeklyBO = WeeklyCertificationJrnlBO
								.getAnyWeeklyCertificationJrnlDataForWeeklyCertId(Long
										.valueOf(weeklyCertData
												.getWeeklyCertId()));
						if (weeklyBO != null
								&& StringUtility.isNotBlank(weeklyBO
										.getWeeklyCertificationJrnlData()
										.getRecordProcessedFlag())
								&& Integer.valueOf(weeklyBO
										.getWeeklyCertificationJrnlData()
										.getRecordProcessedFlag()) > gov.state.uim.framework.BenefitsConstants.NUMBER_ZERO) {
							WeeklyCertJournalBean weeklycertjrnlbean = new WeeklyCertJournalBean();
							weeklycertjrnlbean.setClaimantData(weeklyCertData
									.getClaimantData());
							weeklycertjrnlbean.setStartDate(weeklyCertData
									.getClaimWeekEndDate());
							weeklycertjrnlbean.setEndDate(weeklyCertData
									.getClaimWeekEndDate());
							weeklycertjrnlbean
									.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.PAY_ADJUSTMENT);
							// this will save in journal table only when this
							// will be completed.(WC_COMPLETED flag should be 1)
							WeeklyCertificationJrnlBO
									.prepareWeeklycertificationJournal(weeklycertjrnlbean);
						}
					}
				}
				// CIF_02173 ends
				SharedWorkEmployeeData sharedEmployeeData = sharedWeeklyData
						.getSharedWorkEmployeeData();
				weekCertData = new WeeklyCertData();
				weekCertData
						.setAdjustedEarnings(GlobalConstants.BIGDECIMAL_ZERO);
				weekCertData.setAvailable(GlobalConstants.ONE);
				weekCertData
						.setCertificationMode(CertificationModeEnum.INTERNET_CSR
								.getName());
				weekCertData.setClaimantData(regClaimData.getClaimantData());
				weekCertData.setClaimWeekEndDate(cwe);
				weekCertData
						.setInvestigationPerformedFlag(GlobalConstants.ZERO);
				weekCertData
						.setWorksearchnumber(GlobalConstants.WEEKLY_CERT_ZERO);
				weekCertData.setPayDuaFlag(GlobalConstants.ZERO);
				weekCertData.setPendingReason(GlobalConstants.NPND);
				weekCertData.setPhysicallyAble(GlobalConstants.ONE);
				weekCertData.setProcessedFlag(GlobalConstants.ZERO);
				weekCertData.setReasonSeparation(GlobalConstants.NSEP);
				weekCertData.setRefusedJob(GlobalConstants.ZERO);
				weekCertData.setCertificationDate(new Date());
				weekCertData.setReturnedFulltimeWork(GlobalConstants.ONE);
				weekCertData.setTraining(GlobalConstants.ZERO);
				weekCertData.setSelfemployed(GlobalConstants.ZERO);
				weekCertData.setTotalEarnings(GlobalConstants.BIGDECIMAL_ZERO);
				weekCertData.setReceivepay(GlobalConstants.ZERO);
				weekCertData.setCompletedFlag(GlobalConstants.ONE);
				weekCertData.setWorkPerformed(GlobalConstants.ZERO);
				weekCertData.setWorkSearchFlag(GlobalConstants.ZERO);
				weekCertData
						.setWorkSearchReason(WorkSearchReasonEnum.SHARED_PLAN
								.getName());
				weekCertData.setMakeWkPayble(ViewConstants.YES);
				// Shared Plan Related Values
				weekCertData.setSharedWorkPlanId(sharedEmployeeData
						.getSharedWorkEmployerData().getPkId());
				weekCertData.setRegularWorkHrs(new BigDecimal(
						sharedEmployeeData.getWeeklyWorkHours()));
				weekCertData.setSharedWorkHrs(sharedWeeklyData
						.getSharedWorkHrs());
				weekCertData.setPartWorkHrs(GlobalConstants.BIGDECIMAL_ZERO);
				WeeklyCert weekCert = new WeeklyCert(weekCertData);
				weekCert.saveOrUpdate();
				weekCert.flush();
				WeeklyCertJournalBean weeklycertjrnlbean = new WeeklyCertJournalBean();
				weeklycertjrnlbean.setClaimantData(weekCertData
						.getClaimantData());
				weeklycertjrnlbean.setStartDate(weekCertData
						.getClaimWeekEndDate());
				weeklycertjrnlbean.setEndDate(weekCertData
						.getClaimWeekEndDate());
				weeklycertjrnlbean
						.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.WEEKLY_CERTIFICATION);
				// this will save in journal table only when this will be
				// completed.(WC_COMPLETED flag should be 1)
				WeeklyCertificationJrnlBO
						.prepareWeeklycertificationJournal(weeklycertjrnlbean);
				// CIF_INT_04641
				WeeklyCertService weekService = new WeeklyCertService();
				weekService.checkforI2Determination(weekCertData, regClaimData);
			} else {
				if (null == weekCertData.getSharedWorkPlanId()) {
					// CreateBusiness comment Stating CLiamant have already
					// filed for Full week chnaging to shared week
					// Filed weekly Certification for CWE {0} have been changed
					// to Shared week.
					Claimant claimantBO = new Claimant();
					claimantBO.createBusinessProcessEventComment("WCER",
							"REG_TO_SHARED_WEEK",
							new String[] { DateFormatUtility.format(cwe) },
							regClaimData.getClaimantData(), false);
					weekCertData.setSharedWorkPlanId(sharedWeeklyData
							.getSharedWorkEmployeeData()
							.getSharedWorkEmployerData().getPkId());
					weekCertData.setMakeWkPayble(ViewConstants.YES);
					weekCertData.setRegularWorkHrs(new BigDecimal(
							sharedWeeklyData.getSharedWorkEmployeeData()
									.getWeeklyWorkHours()));
				}
				// Update Hours in Weekly Cert and check if the entry is
				// required in JNRL table
				weekCertData.setSharedWorkHrs(sharedWeeklyData
						.getSharedWorkHrs());
				weekCertData.setRegularWorkHrs(new BigDecimal(sharedWeeklyData
						.getSharedWorkEmployeeData().getWeeklyWorkHours()));
				weekCertData.setPartWorkHrs(GlobalConstants.BIGDECIMAL_ZERO);
				WeeklyCert weekCert = new WeeklyCert(weekCertData);
				weekCert.saveOrUpdate();
				weekCert.flush();
				WeeklyCertificationJrnlBO weeklyBO = WeeklyCertificationJrnlBO
						.getAnyWeeklyCertificationJrnlDataForWeeklyCertId(Long
								.valueOf(weekCertData.getWeeklyCertId()));
				if (weeklyBO == null
						|| (StringUtility.isNotBlank(weeklyBO
								.getWeeklyCertificationJrnlData()
								.getRecordProcessedFlag()) && Integer
								.valueOf(weeklyBO
										.getWeeklyCertificationJrnlData()
										.getRecordProcessedFlag()) > gov.state.uim.framework.BenefitsConstants.NUMBER_ZERO)) {
					WeeklyCertJournalBean weeklycertjrnlbean = new WeeklyCertJournalBean();
					weeklycertjrnlbean.setClaimantData(weekCertData
							.getClaimantData());
					weeklycertjrnlbean.setStartDate(weekCertData
							.getClaimWeekEndDate());
					weeklycertjrnlbean.setEndDate(weekCertData
							.getClaimWeekEndDate());
					if (weeklyBO != null) {
						weeklycertjrnlbean
								.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.PAY_ADJUSTMENT);
					} else {
						weeklycertjrnlbean
								.setWeekProcessingReasonEnum(WeekProcessingReasonEnum.WEEKLY_CERTIFICATION);
					}
					// this will save in journal table only when this will be
					// completed.(WC_COMPLETED flag should be 1)
					WeeklyCertificationJrnlBO
							.prepareWeeklycertificationJournal(weeklycertjrnlbean);
				}
			}
			sharedWeeklyData.setProcessedFlag(ViewConstants.YES);
			SharedWeeklyCert sharedWeeklycert = new SharedWeeklyCert(
					sharedWeeklyData);
			sharedWeeklycert.merge();
		}
		return objAssembly;
	}

	/**
	 * this method detremines if the application needs to be created for the
	 * shared week or not
	 * 
	 * @param ssn
	 *            ssn for which application is to be created
	 * @param objAssembly
	 *            IObjectAssembly
	 * @return
	 */
	private Boolean createClaimApplication(String ssn,
			IObjectAssembly objAssembly, Date wkStartDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createClaimApplication");
		}
		// Date wkStartDate = Claim.determineClaimEffectiveDate().getTime();
		if (UserTypeEnum.MDES.getName().equals(
				objAssembly.getUserContext().getUserType())) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(wkStartDate);
			cal.add(Calendar.DATE, -7);
			wkStartDate = cal.getTime();
		}
		ClaimApplicationData clmAppData = ClaimApplication
				.findInitiatedClaimApplicationBySsn(ssn, wkStartDate);

		boolean createClaimApp = Boolean.FALSE;
		if (null != clmAppData) {
			LOGGER.info("claima application with status "
					+ clmAppData.getClaimAppStatus() + " found");
			if (ApplicationStatusEnum.DUA_PENDING.getName().equals(
					clmAppData.getClaimAppStatus())) {
				// cancelDuaClaim
				this.deleteClaimApp(objAssembly);
				createClaimApp = Boolean.TRUE;
			} else if (ApplicationStatusEnum.INITIATED.getName().equals(
					clmAppData.getClaimAppStatus())) {

				Date createdOn = clmAppData.getCreatedOn();
				if (createdOn.compareTo(wkStartDate) < 0) {
					this.deleteClaimApp(objAssembly);
					createClaimApp = Boolean.TRUE;
				}
			} else if (ApplicationStatusEnum.PENDING.getName().equals(
					clmAppData.getClaimAppStatus())) {
				createClaimApp = Boolean.FALSE;
			}
		} else {
			createClaimApp = Boolean.TRUE;
		}
		LOGGER.info("Will new claim Application be created " + createClaimApp);
		return createClaimApp;
	}

	/**
	 * this method populates the data in claimAPplication and
	 * claimApplicationclaimant table from the employer provided information
	 * 
	 * @param objAssembly
	 * @param updatePP
	 *            update personal particulars for claimant
	 * @return
	 */
	private IObjectAssembly populateDataForSharedClaimApp(
			IObjectAssembly objAssembly, boolean updatePP) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method populateDataForSharedClaimApp");
		}
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge clmAppData = objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

		ClaimApplicationClaimantData clmAppClmntData = clmAppData
				.getClaimApplicationClaimantData();
		SharedWeeklyCertData sharedData = objAssembly
				.getFirstComponent(SharedWeeklyCertData.class);
		SharedWorkEmployeeData sharedEmployee = sharedData
				.getSharedWorkEmployeeData();
		// CIF_02910 SS card first and last name
		if (updatePP) {
			clmAppClmntData.setFirstName(sharedEmployee.getFirstName());
			clmAppClmntData.setLastName(sharedEmployee.getLastName());
			clmAppClmntData.setMiddleInitial(sharedEmployee.getMiddleInitial());
			clmAppClmntData.setSuffix(sharedEmployee.getSuffix());
			clmAppClmntData.setGender(sharedEmployee.getGender());
			clmAppClmntData.setCitizenship(sharedEmployee.getCitizenship());
			clmAppClmntData.setSsaStatus(SsaStatusEnum.V.getName());
			clmAppClmntData.setFirstNameSsCard(sharedEmployee.getFirstName());
			clmAppClmntData.setLastNameSsCard(sharedEmployee.getLastName());
			clmAppClmntData.setDateOfBirth(sharedEmployee.getDateOfBirth());
			clmAppClmntData.setFirstNameSsCard(sharedEmployee.getFirstName());
			clmAppClmntData.setLastNameSsCard(sharedEmployee.getFirstName());
		}
		AddressComponentData addCompData = new AddressComponentData();
		addCompData.setLine1(sharedEmployee.getAddressLine1());
		addCompData.setLine2(sharedEmployee.getAddressLine2());
		addCompData.setCity(sharedEmployee.getCity());
		addCompData.setState(sharedEmployee.getState());
		addCompData.setZip(sharedEmployee.getZip());
		addCompData.setCountry(ViewConstants.HOME_COUNTRY);
		clmAppClmntData.setMailAddress(addCompData);
		if (null == clmAppClmntData.getResAddress()
				|| StringUtility.isBlank(clmAppClmntData.getResAddress()
						.getLine1())) {
			clmAppClmntData.setResAddress(addCompData);
			clmAppClmntData.setResCountyId(sharedEmployee.getCountyId());
		}
		clmAppData.setClaimApplicationClaimantData(clmAppClmntData);
		objAssembly.addComponent(clmAppData, true);
		objAssembly.addComponent(clmAppClmntData, true);
		return objAssembly;
	}

	/**
	 * this method create the workitem
	 * 
	 * @param objAssembly
	 *            this will contain mapsValues that are required to create the
	 *            workitem
	 */
	public IObjectAssembly createWorkitem(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createWorkitem");
		}
		HashMap<String, Object> mapValues = (HashMap<String, Object>) objAssembly
				.getData("mapValues");
		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.CREATE_WORKITEM.getName(), mapValues);
		return objAssembly;
	}

	/**
	 * this methdo will get the details for the details mismatch workitem ie
	 * when there is a mismatch between employer provided information and the
	 * information in the syatem
	 * 
	 * @param objAssembly
	 *            containing the mismatch date
	 */
	public IObjectAssembly getMisMatchDetails(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getMisMatchDetails");
		}
		Date mismatchDate = (Date) objAssembly.getData("misMatchDate");
		IGenericInquiryDAO dao = DAOFactory.instance.getGenericInquiryDAO();
		List list = dao.getInquiryResult("getMisMatchDetails", new Object[] {
				mismatchDate, ViewConstants.YES });
		objAssembly.addData("sharedEmployeeData", list);
		return objAssembly;
	}

	/**
	 * @param objAssembly
	 *            have components namely persistenceId and userId
	 * @return ObjectAssembly
	 * @description this method starts and complete a workitem.
	 */
	public IObjectAssembly completeMismatchWorkItem(
			final IObjectAssembly objAssembly) {
		Date misMatchDate = (Date) objAssembly.getData("misMatchDate");
		SharedWeeklyCert.updateMisMatchFlag(ViewConstants.PROCESSED_RESOLVED,
				misMatchDate, super.getContextUserId());
		final String persistenceOid = objAssembly.getData("wfId").toString();
		final String userId = (String) objAssembly.getData("userId");
		final WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(WorkflowConstants.TASK_ID, persistenceOid);
		map.put(WorkflowConstants.USER_ID, userId);
		wfTransactionService.invokeWorkFlowOperation(
				WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(), map);
		return objAssembly;
	}

	/**
	 * this method create alien and pension issues for the claimant if the
	 * employer says that he is receiving pension / or he's an alien
	 * 
	 * @param objAssembly
	 * 
	 */
	private void createAlienPensionIssue(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createAlienPensionIssue");
		}
		List<IssueData> issueList = Issue.getPendingIssuesOfSSN(objAssembly
				.getSsn());
		// get Previous Alien Issues for Alien
		boolean createAlienIssue = Boolean.TRUE;
		boolean createPensionIssue = Boolean.TRUE;
		if (null != issueList && !issueList.isEmpty()) {
			for (IssueData issueData : issueList) {
				MstIssueMaster mstCategory = issueData.getMstIssueMaster();
				String subCategory = mstCategory.getIssueSubCategory();
				String category = mstCategory.getIssueCategory();
				if (IssueSubCategoryEnum.ALIEN_STATUS.getName().equals(
						subCategory)) {
					createAlienIssue = Boolean.FALSE;
				}
				if (IssueCategoryEnum.PENSION.getName().equals(category)) {
					createPensionIssue = Boolean.FALSE;
				}
			}
		}

		SharedWeeklyCertData sharedWeeklyData = objAssembly
				.getFirstComponent(SharedWeeklyCertData.class);
		RegularClaimData regClaimData = objAssembly
				.getFirstComponent(RegularClaimData.class);
		if (null != regClaimData
				&& createAlienIssue
				&& ViewConstants.NO.equals(sharedWeeklyData
						.getSharedWorkEmployeeData().getCitizenship())) {
			IssueBean ibean = new IssueBean();
			ibean.setClaimantSsn(objAssembly.getSsn());
			ibean.setDateIssueDetected(new Date());
			ibean.setClaimid(regClaimData.getClaimId());
			ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
			ibean.setAutoCompletionDate(new Date());
			ibean.setIssueDescription(IssueCategoryEnum.ABLE_AND_AVAILABLE
					.getName());
			ibean.setIssueDetails(IssueSubCategoryEnum.ALIEN_STATUS.getName());
			ibean.setIssueSource(IssueSourceEnum.SHARED_WORK.getName());
			ibean.setIssueStartDate(new Date());
			ibean.setIssueEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));
			ibean.setUpdatedBy(super.getContextUserId());
			Issue.createIssue(ibean);
		}
		if (createPensionIssue
				&& ViewConstants.YES.equals(sharedWeeklyData
						.getSharedWorkEmployeeData().getCitizenship())) {
			IssueBean ibean = new IssueBean();
			ibean.setClaimantSsn(objAssembly.getSsn());
			ibean.setDateIssueDetected(new Date());
			ibean.setGenerateCorrespondenceFlag(ViewConstants.NO);
			ibean.setIssueDescription(IssueCategoryEnum.PENSION.getName());
			ibean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
			ibean.setIssueSource(IssueSourceEnum.SHARED_WORK.getName());
			ibean.setIssueStartDate(new Date());
			ibean.setIssueEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));
			ibean.setUpdatedBy(super.getContextUserId());
			Issue.createIssue(ibean);
		}

	}

	/**
	 * 
	 * 
	 */
	public IObjectAssembly getEmployeeInfoforSharedPlan(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		String ssn = objAssembly.getSsn();
		String planNumber = (String) objAssembly.getData("planNumber");
		SharedWorkEmployee sharedEmpBo = new SharedWorkEmployee();
		List<SharedWorkEmployeeData> sharedEmployeeList = sharedEmpBo
				.getSharedEmployeeDataBySsn(ssn);
		if (null == sharedEmployeeList || sharedEmployeeList.isEmpty()) {
			throw new BaseApplicationException(
					"access.cin.sharedworksearchbyssn.noPlan");
		}
		boolean planNumberFound = false;
		if (StringUtility.isNotBlank(planNumber)) {
			// fetch employee data basede on ssn and plan number
			for (SharedWorkEmployeeData sharedWorkEmployeeData : sharedEmployeeList) {
				if (planNumber
						.equals(sharedWorkEmployeeData
								.getSharedWorkEmployerData().getPlanNumber()
								.toString())) {
					objAssembly.addComponent(sharedWorkEmployeeData, true);
					objAssembly.addComponent(
							sharedWorkEmployeeData.getSharedWorkEmployerData(),
							true);
					planNumberFound = true;
					break;
				}
			}
			if (!planNumberFound) {
				throw new BaseApplicationException(
						"access.cin.sharedworksearchbyssn.noPlan");
			}
		} else {
			// fetch latest added employeedata based on ssn CIF_02374 starts
			boolean activeFlag = Boolean.FALSE;
			for (SharedWorkEmployeeData sharedWorkEmployeeData : sharedEmployeeList) {
				if (ViewConstants.NO.equals(sharedWorkEmployeeData
						.getEmployeeRemovedFlag())) {
					objAssembly.addComponent(sharedWorkEmployeeData, true);
					objAssembly.addComponent(
							sharedWorkEmployeeData.getSharedWorkEmployerData(),
							true);
					activeFlag = Boolean.TRUE;
					break;
				}
			}
			if (!activeFlag) {
				objAssembly.addComponent(sharedEmployeeList.get(0), true);
				objAssembly.addComponent(sharedEmployeeList.get(0)
						.getSharedWorkEmployerData(), true);
			}
			// CIF_02374 ends
		}
		return objAssembly;
	}

	// CIF_02957 || Jira : UIM-4138
	@Override
	public IObjectAssembly getUnProcessedMassLayoffEmployee(
			IObjectAssembly objAssembly) {
		SsnForMassLayoffs ssnLayoff = new SsnForMassLayoffs(null);
		Date batchDate = (Date) objAssembly.getData("batchDate");
		List<SsnForMassLayoffsData> list = ssnLayoff
				.getUnProcessedMassLayoffEmployee(batchDate);
		objAssembly.addComponentList(list);
		return objAssembly;
	}

	/**
	 * this method checks if claim has to be established for mass layoff else it
	 * rejects and updates employee
	 * 
	 * @param objectAssembly
	 * @return ObjectAssembly
	 */
	public IObjectAssembly batchEstablishMasslayoffClaim(
			IObjectAssembly objAssembly) throws BaseApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method batchEstablishMasslayoffClaim ");
		}
		SsnForMassLayoffsData ssnLayoffData = objAssembly
				.getFirstComponent(SsnForMassLayoffsData.class);
		LOGGER.batchRecordProcess(ssnLayoffData.getPkId().toString(),
				"T_MASS_LAYOFF_EMPLOYEE");
		// Validate if claim can be established for this SSN
		Boolean processClaim = Boolean.TRUE;
		try {
			objAssembly = this.getOrCreateClaimApp(objAssembly);
			if (objAssembly.hasBusinessError()) {
				processClaim = Boolean.FALSE;
			}
		} catch (BaseApplicationException e) {
			processClaim = Boolean.FALSE;
			LOGGER.debug("Cannot establish claim for SSN  "
					+ ssnLayoffData.getSsn() + " Error Log : " + e);
		}

		if (processClaim) {
			/*
			 * Get ClaimApplicationClaimantData and claimApplicationData from
			 * ObjAssembly created by getOrCreateClaimApp and add data from
			 * SsnForMassLayoffsData passed by batch also if alien create an
			 * issue
			 */

			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
			ClaimApplicationDataBridge clmAppData = (ClaimApplicationDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="Start")
			ClaimApplicationClaimantDataBridge clmAppClmntData = (ClaimApplicationClaimantDataBridge) objAssembly
					.fetchORCreate(ClaimApplicationClaimantDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-005", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.5.4",
			// dcrNo="DCR_MDDR_9, DCR_MDDR_10, DCR_MDDR_13", mddrNo="",
			// impactPoint="End")

			WeeklyCertificationValidationDataBean wcBean = (WeeklyCertificationValidationDataBean) objAssembly
					.getData("weeklyBean");
			if (wcBean != null) {
				clmAppData.setClaimAppType(ClaimApplicationTypeEnum.AIC
						.getName());
			}
			// to set Claim Application data
			clmAppData.setSsn(objAssembly.getSsn());
			// to set Claim Application Claimant data
			clmAppClmntData.setSsn(objAssembly.getSsn());
			clmAppClmntData.setFirstName(ssnLayoffData.getFirstName());
			clmAppClmntData.setLastName(ssnLayoffData.getLastName());
			clmAppClmntData.setDateOfBirth(ssnLayoffData.getBirthDate());
			clmAppClmntData.setGender(ssnLayoffData.getGender());
			clmAppClmntData.setCitizenship(ssnLayoffData.getCitizenship());
			clmAppClmntData.setPrimaryPhone(ssnLayoffData.getPrimaryPhone());
			clmAppClmntData.setMiddleInitial(ssnLayoffData.getMiddleInitial());
			clmAppClmntData.setSuffix(ssnLayoffData.getSuffix());
			clmAppClmntData.setSsaStatus(SsaStatusEnum.V.getName());
			clmAppClmntData.setFirstNameSsCard(ssnLayoffData.getFirstName());
			clmAppClmntData.setLastNameSsCard(ssnLayoffData.getLastName());
			if (ViewConstants.NO.equals(ssnLayoffData.getCitizenship())) {
				clmAppClmntData.setInsStatus(AlienStatusEnum.I.getName());
			}
			AddressBean addrBean = new AddressBean();
			addrBean.setAddress1(ssnLayoffData.getAddrline1());
			addrBean.setAddress2(ssnLayoffData.getAddrLine2());
			addrBean.setCity(ssnLayoffData.getCity());
			addrBean.setState(ssnLayoffData.getState());
			addrBean.setCountry(ViewConstants.HOME_COUNTRY);
			addrBean.setZip(ssnLayoffData.getZip());
			AddressComponentData addComponentData = new AddressComponentData(
					addrBean);
			clmAppClmntData.setResAddress(addComponentData);
			clmAppClmntData.setResCountyId(ssnLayoffData.getCounty());
			clmAppClmntData.setMailAddress(addComponentData);

			this.saveOrUpdateFileMassLayoffsInfo(objAssembly);
			if (wcBean != null) {
				ssnLayoffData.setProcessedFlag(GlobalConstants.STRING_THREE);
			} else {
				ssnLayoffData.setProcessedFlag(GlobalConstants.ONE);
			}
		} else {
			ssnLayoffData.setProcessedFlag(GlobalConstants.TWO);
		}
		SsnForMassLayoffs ssnLayoff = new SsnForMassLayoffs(ssnLayoffData);
		ssnLayoff.saveOrUpdate();
		return objAssembly;
	}

	// CIF_03270 starts
	@Override
	public IObjectAssembly sendModesSw3(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method sendModesSw3 ");
		}
		Date fetchDate = (Date) objAssembly.getData("fecthDate");
		SharedWorkEmployer sharedEmp = new SharedWorkEmployer();
		List<SharedWorkEmployerData> sharedEmployerDataList = sharedEmp
				.getDataForModesSw3(fetchDate);
		if (null != sharedEmployerDataList && !sharedEmployerDataList.isEmpty()) {
			// sending SW3 for each record
			CorrespondenceData corrData = null;
			Correspondence corr = null;
			for (SharedWorkEmployerData sharedEmpData : sharedEmployerDataList) {
				LOGGER.batchRecordProcess(sharedEmpData.getPkId().toString(),
						"T_SHARED_WORK_PLAN");
				if (null == sharedEmpData.getLastPaperFormSentDate()
						|| (fetchDate.after(sharedEmpData
								.getLastPaperFormSentDate()) && !DateUtility
								.isSameDay(fetchDate, sharedEmpData
										.getLastPaperFormSentDate()))) {
					corrData = new CorrespondenceData();
					corrData.setEmployerData(sharedEmpData.getEmployerData());
					corrData.setCorrespondenceCode(CorrespondenceCodeEnum.MODES_SW_3
							.getName());
					// CIF_03403
					corrData.setParameter7(sharedEmpData.getPlanNumber());
					// CIF_INT_01018
					corrData.setParameter2(DateFormatUtility.format(fetchDate,
							DateFormatUtility.CUSTOM_DATE_PATTERN));
					corr = new Correspondence(corrData);
					corr.saveOrUpdate();
					// Update sharedEmpData LastPaperFormSentDate column with
					// fetch date
					sharedEmpData.setLastPaperFormSentDate(fetchDate);
					sharedEmp = new SharedWorkEmployer(sharedEmpData);
					sharedEmp.saveOrUpdate();
				}
			}
		}
		return objAssembly;
	}

	@Override
	public IObjectAssembly getDocumentPathByIndexVal(IObjectAssembly objAssembly) {
		DocumentImageIndexData dmIndexData = DocumentImageIndexBO
				.getDocumentPathByIndexVal(String.valueOf(objAssembly
						.getPrimaryKey()));
		objAssembly.addComponent(dmIndexData.getDocImageData());
		return objAssembly;
	}

	/**
	 * this method create the workitem
	 * 
	 * @param objAssembly
	 *            this will contain mapsValues that are required to close
	 *            workitem
	 */
	public IObjectAssembly closeWorkitem(IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createWorkitem");
		}
		HashMap<String, Object> mapValues = (HashMap<String, Object>) objAssembly
				.getData("mapValues");
		WorkflowTransactionService wfTransactionService = new WorkflowTransactionService();
		wfTransactionService
				.invokeWorkFlowOperation(
						WorkFlowOperationsEnum.START_AAND_COMPLETE.getName(),
						mapValues);
		return objAssembly;
	}

	// CIF_03270 ends
	// CIF_03325 starts
	/**
	 * This method will get the Employer Account data and set in object assembly
	 * 
	 * @param IObjectAssembly
	 *            objAssembly
	 * @return IObjectAssembly objAssembly
	 */
	public IObjectAssembly getLoggedEmployerUserDetails(
			IObjectAssembly objAssembly) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method getLoggedEmployerUserDetails");
		}
		if (StringUtility.isNotBlank(getContextUserId())) {
			EmployerUser empUser = EmployerUser
					.fetchEmployerUserByUserId(getContextUserId());
			if (null != empUser) {
				EmployerAccount empAccData = EmployerAccount
						.findByPrimaryKey(empUser.getEmployerUser()
								.getEmployerAccountId(), false);
				empAccData.getEmployerAccountData()
						.getRegisteredEmployerDataSet();
				objAssembly.addComponent(empAccData.getEmployerAccountData());
			}
		}
		return objAssembly;
	}

	// CIF_INT_00309 Lexis Nexis Web Service
	/**
	 * Method to invoke Lexis Nexis Web Service
	 * 
	 * @param IObjectAssembly
	 *            objAssembly
	 * @return IObjectAssembly objAssembly
	 */
	@Override
	public IObjectAssembly invokeLexisNexisWebService(
			IObjectAssembly objAssembly) {
		/* CIF_INT_03653 || Lexis Nexis Spanish Conversion changes Start */
		String loggedInLanguage = StringUtils.trimToEmpty(Context
				.getUserContext().getLoggedInLanguage());
		Boolean isSpanish = GlobalConstants.SPANISH_LANGUAGE_LOCALE
				.equals(loggedInLanguage) ? Boolean.TRUE : Boolean.FALSE;
		/* CIF_INT_03653 || Lexis Nexis Spanish Conversion changes End */

		LexisNexisPageFlowBean lexisNexisPageFlowBean = objAssembly
				.getFirstBean(LexisNexisPageFlowBean.class);
		LexisNexisResponseBean response = null;
		boolean isLexisNexisWSFailed = false;
		LexisNexisRequestBean request = (LexisNexisRequestBean) objAssembly
				.getData(WebServicesConstants.LEXIS_NEXIS_WS_REQUEST);
		IClaimantDAO claimantDao = DAOFactory.instance.getClaimantDAO();
		ClaimantData claimantData = claimantDao
				.findClaimantDetailsBySSN(objAssembly.getSsn());
		// CIF_INT_04219
		if (null == claimantData
				|| StringUtility.isBlank(claimantData.getFirstName())
				|| StringUtility.isBlank(claimantData.getLastName())
				|| null == claimantData.getDateOfBirth()) {
			// check in Object Assembly that you have claimApplicationdata ie
			// this is from claims flow
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
			ClaimApplicationDataBridge claimAppData = objAssembly
					.getFirstComponent(ClaimApplicationDataBridge.class);
			// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
			// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "Start")
			claimantData = new ClaimantDataBridge();
			// @cif_wy(storyNumber = "P1-CM-012, P1-CM-013,P1-CM-014",
			// requirementId = "FR_1614", designDocName =
			// "01 Regular UI Claim,EUCC,EB.docx", designDocSection =
			// "1.34.4, 1.36.4,1.38.4", dcrNo = "DCR_MDDR_83", mddrNo = "",
			// impactPoint = "End")
			claimantData.setSsn(claimAppData.getSsn());
			claimantData.setFirstName(claimAppData
					.getClaimApplicationClaimantData().getFirstName());
			claimantData.setLastName(claimAppData
					.getClaimApplicationClaimantData().getLastName());
			claimantData.setMiddleInitial(claimAppData
					.getClaimApplicationClaimantData().getMiddleInitial());
			claimantData.setDateOfBirth(claimAppData
					.getClaimApplicationClaimantData().getDateOfBirth());
			claimantData.setSuffix(claimAppData
					.getClaimApplicationClaimantData().getSuffix());
		}
		try {
			if (WebServicesConstants.LEXIS_NEXIS_SERVICE_IDENTITY_VERIFICATION_OPERATION
					.equals((String) objAssembly
							.getData(WebServicesConstants.LEXIS_NEXIS_SERVICE_OPERATION))) {
				// TO_CALL_LEXIS_NEXIS_INVOKER_FOR_FIRST_TIME_REQUEST
				LexisNexisIdentityVerificationOperationInputBean inputBean = getLexisNexisInputBean(claimantData);
				response = LexisNexisWebServiceInvoker
						.identityVerificationOperation(inputBean, isSpanish);
			} else if (request != null
					&& WebServicesConstants.LEXIS_NEXIS_SERVICE_CONTINUE_OPERATION
							.equals((String) objAssembly
									.getData(WebServicesConstants.LEXIS_NEXIS_SERVICE_OPERATION))) {
				// TO_CALL_LEXIS_NEXIS_INVOKER_FOR_CONTINUE_REQUEST
				response = LexisNexisWebServiceInvoker.continueOperation(
						request, isSpanish);
			} else {
				LOGGER.debug("Request is null & Invalid Operation Call for Lexis Nexis Web Service");
			}
		} catch (Exception e) {
			isLexisNexisWSFailed = true;
			LOGGER.debug("Failed to invoke Lexis Nexis WS", e);
			// LOGGER.error("Message ",e);
		}/*
		 * finally{ callServiceAfterLexisNexisScreen(objAssembly,
		 * lexisNexisPageFlowBean); }
		 */
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimApplicationData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		PotentialNBYClaimData potentialNBYClaimData = (PotentialNBYClaimData) objAssembly
				.fetchORCreate(PotentialNBYClaimData.class);
		// CIF_INT_02211 || Defect_640
		objAssembly.addComponent(potentialNBYClaimData);
		if (response != null) {
			objAssembly.addData(
					WebServicesConstants.LEXIS_NEXIS_TRANSACTION_ID,
					response.getTransactionId());
			objAssembly.addData(
					WebServicesConstants.LEXIS_NEXIS_TRANSACTION_REQUEST_ID,
					response.getTransactionRequestId());
			objAssembly
					.addData(
							WebServicesConstants.LEXIS_NEXIS_TRANSACTION_QUESTION_SET_ID,
							response.getQuestionSetId());
			objAssembly.addData(
					WebServicesConstants.LEXIS_NEXIS_TRANSACTION_RESULT,
					response.getTransactionResult());
			objAssembly.addData(WebServicesConstants.LEXIS_NEXIS_WS_RESPONSE,
					response);

			if (!response.getTransactionResult().equalsIgnoreCase(
					LexisNexisTransactionResultType.QUESTIONS.value())) {
				if (response.getTransactionResult().equalsIgnoreCase(
						LexisNexisTransactionResultType.FAILED.value())
						|| response.getTransactionResult().equalsIgnoreCase(
								LexisNexisTransactionResultType.ERROR.value())) {
					if (LexisNexisFlowEnum.TRANSITIONAL_CLAIM.name().equals(
							lexisNexisPageFlowBean.getFlowType())) {
						potentialNBYClaimData.setLnStatus(GlobalConstants.ZERO);
						potentialNBYClaimData.setLnFailReason(response
								.getInformationDetails());
						potentialNBYClaimData.setLnTransactionId(response
								.getTransactionId());
					} else {
						claimApplicationData.setLnStatus(GlobalConstants.ZERO);
						claimApplicationData.setLnFailReason(response
								.getInformationDetails());
						claimApplicationData.setLnTransactionId(response
								.getTransactionId());
					}
				} else {
					if (LexisNexisFlowEnum.TRANSITIONAL_CLAIM.name().equals(
							lexisNexisPageFlowBean.getFlowType())) {
						potentialNBYClaimData.setLnStatus(GlobalConstants.ONE);
						potentialNBYClaimData.setLnTransactionId(response
								.getTransactionId());
					} else {
						claimApplicationData.setLnStatus(GlobalConstants.ONE);
						claimApplicationData.setLnTransactionId(response
								.getTransactionId());
					}
				}
				callServiceAfterLexisNexisScreen(objAssembly,
						lexisNexisPageFlowBean);
			}
		}
		if (isLexisNexisWSFailed) {
			if (LexisNexisFlowEnum.TRANSITIONAL_CLAIM.name().equals(
					lexisNexisPageFlowBean.getFlowType())) {
				// CIF_INT_01488
				potentialNBYClaimData.setLnStatus(null);
				potentialNBYClaimData.setLnFailReason(null);
				potentialNBYClaimData.setLnTransactionId(null);
			} else {
				// CIF_INT_01488
				claimApplicationData.setLnStatus(null);
				claimApplicationData.setLnFailReason(null);
				claimApplicationData.setLnTransactionId(null);
			}
			callServiceAfterLexisNexisScreen(objAssembly,
					lexisNexisPageFlowBean);
		}
		return objAssembly;
	}

	/**
	 * Private Method to call Service operations after LexisNexis Screen
	 * 
	 * @param objAssembly
	 * @param lexisNexisPageFlowBean
	 */
	private void callServiceAfterLexisNexisScreen(IObjectAssembly objAssembly,
			LexisNexisPageFlowBean lexisNexisPageFlowBean) {
		if (LexisNexisFlowEnum.TRANSITIONAL_CLAIM.name().equals(
				lexisNexisPageFlowBean.getFlowType())) {
			gov.state.uim.wc.service.WeeklyCertService weeklyCertService = new WeeklyCertService();
			weeklyCertService.submitNewBenefitYear(objAssembly);
		} else {
			if (LexisNexisFlowEnum.INITIAL_OR_RENEW_CLAIM.name().equals(
					lexisNexisPageFlowBean.getFlowType())) {
				try {
					this.processInitialClaim(objAssembly);
					// POST SERVICE METHOD CALL
					this.checkAndInitiateEmailVerification(objAssembly);
				} catch (BaseApplicationException e) {
					LOGGER.error("Message ", e);
				}
			} else if (LexisNexisFlowEnum.REOPEN_CLAIM.name().equals(
					lexisNexisPageFlowBean.getFlowType())) {
				try {
					this.saveInterveningEmploymentInfo(objAssembly);
				} catch (BaseApplicationException e) {
					LOGGER.error("Message ", e);
				}
			}
		}
	}

	/**
	 * Private Method to Get Lexis Nexis Input Bean from Claimant Data
	 * 
	 * @param ClaimantData
	 *            data
	 * @return LexisNexisIdentityVerificationOperationInputBean obj
	 */
	private LexisNexisIdentityVerificationOperationInputBean getLexisNexisInputBean(
			ClaimantData data) {
		LexisNexisIdentityVerificationOperationInputBean bean = null;
		if (data != null) {
			bean = new LexisNexisIdentityVerificationOperationInputBean();
			bean.setSsn(StringUtility.trimToEmpty(data.getSsn()));
			bean.setFirstName(StringUtility.trimToEmpty(data.getFirstName()));
			bean.setLastName(StringUtility.trimToEmpty(data.getLastName()));
			if (data.getDateOfBirth() != null) {
				bean.setDateOfBirth(data.getDateOfBirth());
			}
		}
		return bean;
	}

	public IObjectAssembly correctClaimNoticesData(IObjectAssembly objAssembly) {
		/*
		 * Long corrId = (Long) objAssembly.getData("corrId"); ClaimApplication
		 * claimAppBo = ClaimApplication.findClaimAppByPkId(corrId); ClaimData
		 * claimData = null; Claimant claimant =
		 * Claimant.fetchClaimantBySsn(claimAppBo
		 * .getClaimApplicationData().getSsn()); Set<ClaimData> claimDataSet =
		 * claimant.getClaimantData().getClaimData(); if(claimDataSet != null &&
		 * !claimDataSet.isEmpty()){ for (ClaimData claimData2 : claimDataSet) {
		 * if(null != claimData2.getClaimApplicationData() &&
		 * claimData2.getClaimApplicationData().getPkId() ==
		 * claimAppBo.getClaimApplicationData().getPkId()){ claimData =
		 * claimData2; break; } }
		 * 
		 * if(claimData == null){ objAssembly.addData("week_not_processed",
		 * Boolean.TRUE); return objAssembly; } }else{
		 * objAssembly.addData("week_not_processed", Boolean.TRUE); return
		 * objAssembly; }
		 */
		Long corrId = (Long) objAssembly.getData("corrId");
		CorrespondenceData corrData = Correspondence
				.getCorrespondenceByCorrespondenceId(corrId);
		List<CorrespondenceData> corrList = new ArrayList<CorrespondenceData>();
		corrList.add(corrData);
		ClaimData claimData = corrData.getClaimData();
		Set wageSet = claimData.getWages();
		List wageDataList = new ArrayList();
		wageDataList.addAll(wageSet);
		RegularClaim regClaim = new RegularClaim(claimData);
		List<CorrWageInfoData> corrWageInfoList = regClaim
				.saveCorrespondenceWages(corrList, wageDataList);
		for (CorrWageInfoData wageInfo : corrWageInfoList) {
			Correspondence.saveOrUpdateCorrWageData(wageInfo);
		}
		/*
		 * Set<Long> claimDataProcesdsed = (Set<Long>)
		 * objAssembly.getData("claimDataProcesdsed");
		 * if(claimDataProcesdsed.contains(claimData.getClaimId().longValue())){
		 * return objAssembly; }else{
		 * claimDataProcesdsed.add(claimData.getClaimId().longValue());
		 * objAssembly.addData("claimDataProcesdsed", claimDataProcesdsed); }
		 */
		/*
		 * regClaim.addworkSearchContactCI(claimData.getClaimApplicationData());
		 * Set potIssueSet =
		 * claimData.getClaimApplicationData().getPotentialIssueData(); if
		 * ((potIssueSet != null) && (potIssueSet.size() > 0)) {
		 * claimData.getClaimApplicationData().setClaimData(claimData); List
		 * potIssueList = new ArrayList(); for (Iterator i =
		 * potIssueSet.iterator(); i.hasNext();) { PotentialIssueData data =
		 * (PotentialIssueData) i.next(); if(null != data.getIssueData()){
		 * continue; } data.setClaimantData(claimData.getClaimantData());
		 * data.setClaimApplicationData(claimData.getClaimApplicationData());
		 * potIssueList.add(data); } // for (Iterator itr =
		 * potIssueSet.iterator(); itr.hasNext(); ) // { //
		 * this.claimDao.getSession().evict(itr.next()); // } List issueList =
		 * Issue.createIssue(potIssueList,
		 * IssueSourceEnum.COMMONINTAKE.getName()); } RegularClaim regClaim =
		 * new RegularClaim(claimData);
		 * regClaim.createLexisNexisIssue(claimData.getClaimApplicationData(),
		 * claimData); ClaimApplicationData clmAppData =
		 * claimData.getClaimApplicationData(); if(
		 * StringUtility.isNotBlank(clmAppData.getSocialSecurityDisability()) &&
		 * clmAppData.getSocialSecurityDisability().equals(ViewConstants.YES)){
		 * 
		 * PotentialIssueData potIssueData = new PotentialIssueData();
		 * potIssueData.setClaimApplicationData(clmAppData);
		 * potIssueData.setIssueCode("SSD");
		 * potIssueData.setQuestionnaireType("CIN_GEN_AVAILABLE");
		 * potIssueData.setQuestionnaireStatus("P");
		 * potIssueData.setUpdatedBy(super.getContextUserId()); if(null ==
		 * potIssueSet){ potIssueSet = new HashSet<PotentialIssueData>(); }
		 * potIssueSet.add(potIssueData);
		 * clmAppData.setPotentialIssueData(potIssueSet); ClaimApplication
		 * clmAppBO = new ClaimApplication(clmAppData); clmAppBO.saveOrUpdate();
		 * clmAppBO.flush();
		 * 
		 * IssueBean issuebean = new IssueBean();
		 * issuebean.setClaimantSsn(clmAppData.getSsn());
		 * issuebean.setIssueDescription
		 * (IssueCategoryEnum.ABLE_AND_AVAILABLE.getName());
		 * issuebean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
		 * issuebean.setDateIssueDetected(new Date());
		 * issuebean.setIssueStartDate(new Date());
		 * issuebean.setIssueEndDate(DateFormatUtility
		 * .parse(GlobalConstants.INDEFINITE_DATE));
		 * issuebean.setIssueSource(IssueSourceEnum.COMMONINTAKE.getName());
		 * issuebean.setInformationProvidedBy("CMIN");
		 * issuebean.setUpdatedBy(super.getContextUserId());
		 * issuebean.setLeadCaseFlag(GlobalConstants.ZERO);
		 * issuebean.setMultiClaimantIssueFlag(GlobalConstants.ZERO);
		 * issuebean.setGenerateCorrespondenceFlag(GlobalConstants.ZERO);
		 * issuebean.setOwnershipFlag(GlobalConstants.ZERO);
		 * Issue.createIssue(issuebean,true);
		 * 
		 * }
		 */
		Set potIssueSet = claimData.getClaimApplicationData()
				.getPotentialIssueData();
		if ((potIssueSet != null) && (potIssueSet.size() > 0)) {
			claimData.getClaimApplicationData().setClaimData(claimData);
			List potIssueList = new ArrayList();
			for (Iterator i = potIssueSet.iterator(); i.hasNext();) {
				PotentialIssueData data = (PotentialIssueData) i.next();
				if (null != data.getIssueData()) {
					continue;
				}
				data.setClaimantData(claimData.getClaimantData());
				data.setClaimApplicationData(claimData
						.getClaimApplicationData());
				potIssueList.add(data);
			}
			// for (Iterator itr = potIssueSet.iterator(); itr.hasNext(); )
			// {
			// this.claimDao.getSession().evict(itr.next());
			// }
			List issueList = Issue.createIssue(potIssueList,
					IssueSourceEnum.COMMONINTAKE.getName());
		}
		// RegularClaim regClaim = new RegularClaim(claimData);
		regClaim.createLexisNexisIssue(claimData.getClaimApplicationData(),
				claimData);
		ClaimApplicationData clmAppData = claimData.getClaimApplicationData();
		if (StringUtility.isNotBlank(clmAppData.getSocialSecurityDisability())
				&& clmAppData.getSocialSecurityDisability().equals(
						ViewConstants.YES)) {

			PotentialIssueData potIssueData = new PotentialIssueData();
			potIssueData.setClaimApplicationData(clmAppData);
			potIssueData.setIssueCode("SSD");
			potIssueData.setQuestionnaireType("CIN_GEN_AVAILABLE");
			potIssueData.setQuestionnaireStatus("P");
			potIssueData.setUpdatedBy(super.getContextUserId());
			if (null == potIssueSet) {
				potIssueSet = new HashSet<PotentialIssueData>();
			}
			potIssueSet.add(potIssueData);
			clmAppData.setPotentialIssueData(potIssueSet);
			ClaimApplication clmAppBO = new ClaimApplication(clmAppData);
			clmAppBO.saveOrUpdate();
			clmAppBO.flush();

			IssueBean issuebean = new IssueBean();
			issuebean.setClaimantSsn(clmAppData.getSsn());
			issuebean.setIssueDescription(IssueCategoryEnum.ABLE_AND_AVAILABLE
					.getName());
			issuebean.setIssueDetails(IssueSubCategoryEnum.OTHER.getName());
			issuebean.setDateIssueDetected(new Date());
			issuebean.setIssueStartDate(new Date());
			issuebean.setIssueEndDate(DateFormatUtility
					.parse(GlobalConstants.INDEFINITE_DATE));
			issuebean.setIssueSource(IssueSourceEnum.COMMONINTAKE.getName());
			issuebean.setInformationProvidedBy("CMIN");
			issuebean.setUpdatedBy(super.getContextUserId());
			issuebean.setLeadCaseFlag(GlobalConstants.ZERO);
			issuebean.setMultiClaimantIssueFlag(GlobalConstants.ZERO);
			issuebean.setGenerateCorrespondenceFlag(GlobalConstants.ZERO);
			issuebean.setOwnershipFlag(GlobalConstants.ZERO);
			Issue.createIssue(issuebean, true);

		}
		return objAssembly;

	}

	@Override
	/**
	 * This method is only invoked when there is change in effective date from claims intake
	 */
	public IObjectAssembly validateBackDateRequest(IObjectAssembly objAssembly)
			throws BaseApplicationException {
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimAppData = objAssembly
				.getFirstComponent(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		Claimant claimant = Claimant.fetchClaimantBySsn(claimAppData.getSsn());
		if (null != claimant && null != claimant.getClaimantData()) {
			Set<ClaimData> claimDataSet = claimant.getClaimantData()
					.getClaimData();
			if (null != claimDataSet && !claimDataSet.isEmpty()) {
				for (ClaimData claimData : claimDataSet) {
					if (ClaimEntitlementTypeEnum.REG.getName().equals(
							claimData.getEntitlementType())
							&& DateUtility.isBetweenDates(
									claimAppData.getBackDate(),
									claimData.getEffectiveDate(),
									claimData.getByeDate())) {
						throw new BaseApplicationException(
								"access.backdate.claimIntake");
					}
				}
			}
		}
		return objAssembly;
	}

	@cif_wy(storyNumber = "P1-CM-015", requirementId = "FR_74", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.26.4.1.1", dcrNo = "", mddrNo = "", impactPoint = "New")
	public BigDecimal calculateWbaDetails(Set wageSet, Date effectiveDate) {

		// Changed according to Present Business Rules
		Integer lastQuarter = GlobalConstants.NUMERIC_ZERO;
		boolean twoQuarterFlag = false;
		boolean minQalWageFlag = false;
		boolean toltalBaseWageFlag = false;
		boolean alternateFlag = false;

		BigDecimal wba = new BigDecimal(BigInteger.ZERO, 2);
		BigDecimal highQuarterAmount = Claim
				.calculateHighestQuarterWageAmount(wageSet);
		BigDecimal totalWages = Claim.calculateTotalWageAmount(wageSet);
		BigDecimal minQalWage = Claim.getDynamicBozConstantAsBigDecimal(
				"MIN_QUALIFYING_WAGE", effectiveDate);
		BigDecimal maxTaxableWage = Claim.getDynamicBozConstantAsBigDecimal(
				"MAX_TAXABLE_WAGE", effectiveDate);
		BigDecimal minWba = Claim.getDynamicBozConstantAsBigDecimal("MIN_WBA",
				effectiveDate);
		BigDecimal maxWba = Claim.getDynamicBozConstantAsBigDecimal("MAX_WBA",
				effectiveDate);
		BigDecimal minWyQalWage = Claim.getDynamicBozConstantAsBigDecimal(
				"MIN_BP_WAGES", effectiveDate);// P1-CM-008

		Iterator wagesIter = wageSet.iterator();

		Map<Integer, BigDecimal> wageMap = new HashMap<Integer, BigDecimal>();

		while (wagesIter.hasNext()) {
			WageData currentWage = (WageData) wagesIter.next();
			// wages.add(currentWage.getWageAmount());
			if (wageMap.containsKey(currentWage.getQuarter())) {
				wageMap.put(
						currentWage.getQuarter(),
						wageMap.get(currentWage.getQuarter()).add(
								currentWage.getWageAmount()));
			} else {
				wageMap.put(currentWage.getQuarter(),
						currentWage.getWageAmount());
			}

			if (!(currentWage.getWageAmount().compareTo(new BigDecimal(0.00)) == 0)) {

				if (lastQuarter.equals(GlobalConstants.NUMERIC_ZERO)) {
					lastQuarter = currentWage.getQuarter();
				}

				// Check whether the claimant has wages in two quarters
				if (currentWage.getQuarter().compareTo(lastQuarter) != 0) {
					twoQuarterFlag = true;
				}

			}
		}
		LinkedList<BigDecimal> wages = new LinkedList<BigDecimal>();
		for (BigDecimal quarterWage : wageMap.values()) {
			wages.add(quarterWage);
		}
		// Collections.sort(wages, BigDecimal..);
		Collections.sort(wages);

		// Check State Specific Object if exist in Multi-state Class factory For
		// ReqId-FR_74-StoryID-P1-CM-015
		Object IObject = MultiStateClassFactory.getObject(this.getClass()
				.getName(), BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

		// @cif_wy(storyNumber="P1-CM-008", requirementId="FR_1613",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",
		// designDocSection="1.20.4.1.1", dcrNo="",
		// mddrNo="",impactPoint="Start"
		if (null != IObject) {
			// WY WBA Calculation
			// wba =
			// ((CinService)IObject).getWbaValueBasedOnMultistate(wages,totalWages);
			wba = ((CinService) IObject).getInsuredWorkerDetails(wages,
					totalWages, highQuarterAmount, minQalWage, minWyQalWage,
					maxTaxableWage, twoQuarterFlag);

		} else {

			wba = getInsuredWorkerDetails(wages, totalWages, highQuarterAmount,
					minQalWage, minWyQalWage, maxTaxableWage, twoQuarterFlag);
			// @cif_wy(storyNumber="P1-CM-008", requirementId="FR_1613",
			// designDocName="01 Regular UI Claim,EUCC,EB.docx",
			// designDocSection="1.20.4.1.1", dcrNo="",
			// mddrNo="",impactPoint="End"
			// }
		}

		// compare with MIN and MAX wba
		if (wba != null && wba.compareTo(minWba) < 0) {
			wba = new BigDecimal(BigInteger.ZERO, 2);
		} else if (wba != null && wba.compareTo(maxWba) > 0) {
			wba = maxWba;
		}

		return wba;
	}

	/**
	 * 
	 * This method retrieves MBA by using WBA and wageset
	 * 
	 * This method calculate WBA * 26 times and 30% of the wage credits in the
	 * base period whichever is min
	 * 
	 * @param BigDecimal
	 *            wba - WBA value for specified Claimant
	 * 
	 * @param BigDecimal
	 *            totalWagesForMbaCal - set of totalWagesForMbaCal during Base
	 *            Period for specified Claimant
	 * 
	 * @return BigDecimal Mba
	 * 
	 */

	@cif_wy(storyNumber = "P1-CM-016", requirementId = "FR_127", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.26.4.1.1", dcrNo = "", mddrNo = "", impactPoint = "New")
	public BigDecimal calculateMbaDetails(BigDecimal wba, Set wageSet,
			Date claimEffectiveDate) {
		BigDecimal mba = new BigDecimal(BigInteger.ZERO, 2);
		if (DynamicBizConstant.isHb150LawEnabled(claimEffectiveDate)) {
			// get Data from avgUnemployment Rate table for the year and quarter
			// half
			Calendar cal = Calendar.getInstance();
			cal.setTime(claimEffectiveDate);
			int[] quarterYear = DateUtility.getQuarterAndYear(cal);
			IUnemploymentRateDao rateDao = DAOFactory.instance
					.getUnemloymentRateDao();
			AverageUnemploymentRateData avgData = rateDao
					.getUnemploymetRateDataByYearPeriod(quarterYear[1],
							quarterYear[0] > 2 ? GlobalConstants.TWO
									: GlobalConstants.ONE);
			// CIF_INT_01112 - Added null condition
			if (null != avgData) {
				mba = wba.multiply(BigDecimal.valueOf(avgData.getWeeks()));
			}
		} else {
			Iterator wageIter = wageSet.iterator();
			BigDecimal totalWagesForMbaCal = new BigDecimal(BigInteger.ZERO, 2);
			BigDecimal totalWagesForBasePeriod = new BigDecimal(
					BigInteger.ZERO, 2);
			while (wageIter.hasNext()) {
				WageData currentWage = (WageData) wageIter.next();
				totalWagesForMbaCal = totalWagesForMbaCal.add(currentWage
						.getWageAmount()
						.min(wba.multiply(BigDecimal
								.valueOf(GlobalConstants.MBA_MAX_QRT_WAGE))));
				totalWagesForBasePeriod = totalWagesForBasePeriod
						.add(currentWage.getWageAmount());
			}

			// Check State Specific Object if exist in Multi-state Class factory
			// For ReqId-FR_127-StoryID-P1-CM-016
			Object IObject = MultiStateClassFactory
					.getObject(this.getClass().getName(),
							BaseOrStateEnum.STATE, null, null, Boolean.TRUE);

			if (null != IObject) {

				mba = ((CinService) IObject).getMbaValueBasedOnMultistate(wba,
						totalWagesForBasePeriod);

			} else {
				mba = this.getMbaValueBasedOnMultistate(wba,
						totalWagesForMbaCal);
			}

		}
		return mba;

	}

	/**
	 * 
	 * This method retrieves WBA by taking wage amount from entered set of
	 * WageData.
	 * 
	 * This method calculate as 4% as average of highest quarters in the base
	 * period and check whether if any quarter wages is more than min qualifying
	 * wages requirement
	 * 
	 * LinkedList<BigDecimal>wages
	 * 
	 * @return BigDecimal wba
	 * 
	 */

	@cif_wy(storyNumber = "P1-CM-015", requirementId = "FR_74", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.26.4.1.1", dcrNo = "", mddrNo = "", impactPoint = "New")
	public BigDecimal getWbaValueBasedOnMultistate(LinkedList<BigDecimal> wages)

	{
		BigDecimal wba = new BigDecimal(BigInteger.ZERO, 2);

		int index = wages.size() - 1;
		BigDecimal sum = wages.get(index).add(wages.get(index - 1));
		BigDecimal avgValue = sum.divide(
				BigDecimal.valueOf(GlobalConstants.WBA_FIRST_SECOND_AVG),
				BigDecimal.ROUND_DOWN);
		wba = avgValue.multiply(BigDecimal
				.valueOf(GlobalConstants.WBA_QUARTERS_RATIO));
		wba = wba.setScale(0, BigDecimal.ROUND_DOWN);

		return wba;
	}

	/**
	 * 
	 * This method retrieves MBA by using WBA and wageset
	 * 
	 * This method calculate WBA * 26 times and 30% of the wage credits in the
	 * base period whichever is min
	 * 
	 * @param BigDecimal
	 *            wba - WBA value for specified Claimant
	 * 
	 * @param BigDecimal
	 *            totalWagesForMbaCal - set of totalWagesForMbaCal during Base
	 *            Period for specified Claimant
	 * 
	 * @return BigDecimal Mba
	 * 
	 */

	@cif_wy(storyNumber = "P1-CM-016", requirementId = "FR_127", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.26.4.1.1", dcrNo = "", mddrNo = "", impactPoint = "New")
	public BigDecimal getMbaValueBasedOnMultistate(BigDecimal wba,
			BigDecimal totalWagesForMbaCal) {

		return new BigDecimal(BigInteger.ZERO);

	}

	/**
	 * This method returns true if the isHavingNameMissMatchWage contain a name
	 * different than the name in the claimapplication .
	 * 
	 * @param wages
	 * @param otherNames
	 * @param lastNameThree
	 * 
	 * @return boolean of isHavingNameMissMatchWage
	 */

	@cif_wy(storyNumber = "P1-CM-018", requirementId = "FR_1613", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.26.4.1.1", dcrNo = "", mddrNo = "", impactPoint = "New")
	public boolean isHavingNameMissMatch(List wages, String[] otherNames,
			String lastNameThree) {
		// TODO Auto-generated method stub
		boolean isHavingNameMissMatchWage = false;
		boolean isLastNameEmpty = false;
		List removeWages = new ArrayList();
		Iterator wgIterator = wages.iterator();

		while (wgIterator.hasNext()) {
			WageData currentWage = (WageData) wgIterator.next();
			String currentWageLastNameThree;

			if (currentWage.getEmployeeData().getLastName().length() < 3) {
				currentWageLastNameThree = currentWage.getEmployeeData()
						.getLastName();
			} else {
				currentWageLastNameThree = currentWage.getEmployeeData()
						.getLastName().substring(0, 3);
			}
			// If useOtherLastName is trues then check the other last names as
			// well.
			if (!currentWageLastNameThree.equalsIgnoreCase(lastNameThree)) {
				if (!StringUtility.findInArray(currentWageLastNameThree,
						otherNames)) {
					removeWages.add(currentWage);
				}
			}
		}

		if (removeWages != null && removeWages.size() > 0) {
			isHavingNameMissMatchWage = true;

		}

		return isHavingNameMissMatchWage;
	}

	@cif_wy(storyNumber = "P1-CM-003", requirementId = "FR_1570", designDocName = "03 UCFE,UCX,CWC,IB1,Missing Wages", designDocSection = "1.2", dcrNo = "DCR_MDDR_6", mddrNo = "", impactPoint = "")
	protected ClaimApplicationEmployerData includeStateSpecificClmAppEmpDataObject(
			ClaimApplicationEmployerDataBridge clmAppEmpData) {
		// method is overridden in state specific class
		return null;
	}

	public void setStateSpecificClmAppEmpClmnt(
			ClaimApplicationDataBridge ClaimApplicationDataBridge,
			ClaimApplicationEmployerData clmAppEmpData) {
		// method is overridden in state specific class
	}

	@cif_wy(storyNumber = "P1-CM-008", requirementId = "FR_1613", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.20.4", dcrNo = "", mddrNo = "", impactPoint = "")
	public BigDecimal getInsuredWorkerDetails(LinkedList<BigDecimal> wages,
			BigDecimal totalWages, BigDecimal highQuarterAmount,
			BigDecimal minQalWage, BigDecimal minWyQalWage,
			BigDecimal maxTaxableWage, Boolean twoQuarterFlag) {
		// MO code
		Boolean toltalBaseWageFlag = false;
		Boolean alternateFlag = false;
		BigDecimal wba = new BigDecimal(BigInteger.ZERO, 2);
		if (highQuarterAmount.compareTo(minQalWage) >= 0) {

			// Check whether total base period wages must equal one and half of
			// the highest QTR
			if (totalWages.compareTo(highQuarterAmount.multiply(BigDecimal
					.valueOf(GlobalConstants.WBA_TOTAL_WAGE_RATIO))) >= 0) {
				toltalBaseWageFlag = true;
			}

			if (totalWages.compareTo(maxTaxableWage.multiply(BigDecimal
					.valueOf(GlobalConstants.WBA_TOTAL_WAGE_RATIO))) >= 0) {
				alternateFlag = true;
			}
			// Calculate the average of Highest Qtr and second Highest Qtr and *
			// 0.04
			// If wages are not paid in at least 2 quarters the claims is IW.

			if (toltalBaseWageFlag || alternateFlag) {
				int index = wages.size() - 1;
				BigDecimal sum = wages.get(index).add(wages.get(index - 1));
				BigDecimal avgValue = sum.divide(BigDecimal
						.valueOf(GlobalConstants.WBA_FIRST_SECOND_AVG),
						BigDecimal.ROUND_DOWN);
				wba = avgValue.multiply(BigDecimal
						.valueOf(GlobalConstants.WBA_QUARTERS_RATIO));
				wba = wba.setScale(0, BigDecimal.ROUND_DOWN);
			}
		}

		return wba;
	}

	public DuaApplicationDataBridge duaApplicatioDataObject(
			DuaApplicationDataBridge duaAppData) {
		// TODO Auto-generated method stub
		return null;
	}

	@cif_wy(storyNumber = "P1-CM-036,P1-CM-057", requirementId = "FR_1614", designDocName = "02	Questionnaire.docx", designDocSection = "1.1.4", dcrNo = "DCR_MDDR_62", mddrNo = "", impactPoint = "New")
	public ClaimApplicationDataBridge checkMedicalQuestionaire(
			IObjectAssembly objAssembly, ClaimApplicationDataBridge clmAppData) {
		// state specific method will override
		return clmAppData;

	}

	@cif_wy(storyNumber = "P1-BAD-014", requirementId = "FR_471", designDocName = "03 - Monetary Redetermination and Reconsideration.docx", designDocSection = "10.2", dcrNo = "", mddrNo = "", impactPoint = "New")
	public IObjectAssembly saveIb4RequestForTTDOutOfState(
			IObjectAssembly objAssembly,
			ClaimApplicationEmployerDataBridge clmApplEmplDataBridge) {
		return objAssembly;
	}

	@cif_wy(storyNumber = "P1-CM-012", requirementId = "FR_44", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.36.4", dcrNo = "DCR_MDDR_83", mddrNo = "", impactPoint = "New")
	public IObjectAssembly processClaimantJobAttached(
			IObjectAssembly objAssembly) {
		Object stateSpecificBO = MultiStateClassFactory.getObject(this
				.getClass().getName(), BaseOrStateEnum.STATE, null, null,
				Boolean.TRUE);
		// check multi state then call wy service else do nothing
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processClaimantJobAttached");
		}
		if (null != stateSpecificBO) {
			((CinService) stateSpecificBO)
					.processClaimantJobAttached(objAssembly);
		}
		return objAssembly;
	}

	@cif_wy(storyNumber = "P1-CM-014", requirementId = "FR_43", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.34.4", dcrNo = "DCR_MDDR_83", mddrNo = "", impactPoint = "New")
	public IObjectAssembly processClaimantUnionVerified(
			IObjectAssembly objAssembly) {
		Object stateSpecificBO = MultiStateClassFactory.getObject(this
				.getClass().getName(), BaseOrStateEnum.STATE, null, null,
				Boolean.FALSE);
		// check multi state then call wy service else do nothing
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processClaimantUnionVerified");
		}
		// if (null != stateSpecificBO) {
		objAssembly = ((CinService) stateSpecificBO)
				.processClaimantUnionVerifiedResolution(objAssembly);
		// }
		return objAssembly;
	}

	protected IObjectAssembly processClaimantUnionVerifiedResolution(
			IObjectAssembly objAssembly) {
		return objAssembly;

	}

	@cif_wy(storyNumber = " P1-CM-013", requirementId = "FR_44", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.38.4", dcrNo = "DCR_MDDR_83,DCR_MDDR_120", mddrNo = "", impactPoint = "New")
	public IObjectAssembly processClaimantRegisteredForWork(
			IObjectAssembly objAssembly) {

		Object stateSpecificBO = MultiStateClassFactory.getObject(this
				.getClass().getName(), BaseOrStateEnum.STATE, null, null,
				Boolean.TRUE);
		// check multi state then call wy service else do nothing
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method processClaimantRegisteredForWork");
		}
		if (null != stateSpecificBO) {
			((CinService) stateSpecificBO)
					.processClaimantRegisteredForWork(objAssembly);
		}
		return objAssembly;
	}

	@cif_wy(storyNumber = " P1-CM-013", requirementId = "FR_44", designDocName = "01 Regular UI Claim,EUCC,EB.docx", designDocSection = "1.38.4", dcrNo = "DCR_MDDR_83,DCR_MDDR_120", mddrNo = "", impactPoint = "New")
	public IObjectAssembly createWorkItemClaimantRegisteredForWork(
			IObjectAssembly objAssembly) {
		Object stateSpecificBO = MultiStateClassFactory.getObject(this
				.getClass().getName(), BaseOrStateEnum.STATE, null, null,
				Boolean.TRUE);
		// check multi state then call wy service else do nothing
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method createWorkItemClaimantRegisteredForWork");
		}
		if (null != stateSpecificBO) {
			((CinService) stateSpecificBO)
					.createWorkItemClaimantRegisteredForWork(objAssembly);
		}
		return objAssembly;
	}
	
	@cif_wy(storyNumber="P1-APP-37", requirementId="FR_1632",designDocName="Appendix B -  Appeals - Correspondence",designDocSection="1", dcrNo="", mddrNo="",impactPoint="new")
	protected Boolean isCorrGenerate(String corrCode)
	{
		return true;
	}

}