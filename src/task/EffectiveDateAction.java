package gov.state.uim.cin.struts;

import gov.state.uim.common.ServiceLocator;
import gov.state.uim.domain.Claim;
import gov.state.uim.domain.bean.CalendarBean;
import gov.state.uim.domain.data.ClaimApplicationDataBridge;
import gov.state.uim.domain.enums.ClaimAppFlowTypeEnum;
import gov.state.uim.framework.logging.log4j.AccessLogger;
import gov.state.uim.framework.service.IObjectAssembly;
import gov.state.uim.framework.struts.BaseLookUpDispatchAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class is Action that corresponds to Effective Date screen. jsp :
 * effectivedate.jsp Action : EffectiveDateAction.java Form :
 * EffectiveDateForm.java tiles-defs.xml : .effectiveDate
 * (acccessms/WEB-INF/jsp)
 * 
 * @author Tata Consultancy Services
 * 
 * @version 1.0
 * 
 * 
 * @struts.action path="/effectivedate" name="effectivedateform" scope="request"
 *                input=".effectiveDate" validate="true" parameter="method"
 * 
 * @struts.action-set-property value = "true" property = "load"
 * @struts.action-set-property value = "continue,finishlater" property =
 *                             "forwards"
 * @struts.action-set-property value = "/claimant,/saveclose" property =
 *                             "preLoadPath"
 * 
 * @struts.action-forward name="continueintemp" path= ".interveningEmployment"
 *                        redirect="false"
 * @struts.action-forward name="continue" path= ".claimant" redirect="false"
 * @struts.action-forward name="cancel" path= ".home" redirect="false"
 * @struts.action-set-property value = "gov.state.uim.cin.service.CinService"
 *                             property = "bizServiceClass"
 */

public class EffectiveDateAction extends BaseLookUpDispatchAction {
	private static final AccessLogger LOGGER = ServiceLocator.instance
			.getLogger(EffectiveDateAction.class);

	/*
	 * Method executed during the load of page. There is populating values from
	 * db to the current form.
	 */
	public void load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method load");
		}

		IObjectAssembly objAssembly = getObjectAssemblyFromSession(request);

		//@cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614", designDocName="01 Regular UI Claim,EUCC,EB.docx", 
		//designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimApp = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		//@cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614", designDocName="01 Regular UI Claim,EUCC,EB.docx", 
		//designDocSection="1.8.4", dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")
		EffectiveDateForm currentForm = (EffectiveDateForm) form;

		// populate values in the current form

		if (claimApp.getBackDate() != null) {
			currentForm.setEffDate(new CalendarBean(claimApp.getBackDate()));
		} else {
			currentForm.setEffDate(new CalendarBean(Claim
					.determineClaimEffectiveDate().getTime()));
		}
		// 4416
		/*
		 * currentForm.setReasonBackdating(claimApp.getBackDateReason());
		 * currentForm.setReasonOther(claimApp.getBackDateDetails());
		 */
	}

	/*
	 * Method to map buttons and corresponding actions
	 */
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("access.continue", "cont");
		map.put("access.cancel", "cancel");

		return map;
	}

	/*
	 * Method to map actions and corresponding service methods
	 */
	protected Map getServiceKeyMethodName() {
		Map map = new HashMap();
		map.put("cont", "processClaimEffectiveDate");
		map.put("cancel", "deleteClaimApp");
		return map;
	}

	/*
	 * Method to map non-validate buttons
	 */
	protected List getNonValidateKey() {
		List list = new ArrayList();

		list.add("access.cancel");
		return list;
	}

	/*
	 * Pre-method for continue button. The continue button forwards to the
	 * Determine State Screen.
	 */
	public IObjectAssembly precont(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method precont");
		}

		IObjectAssembly objAssembly = saveData(mapping, form, request, response);

		return objAssembly;
	}

	/*
	 * Method for continue button. The continue button forwards to the Determine
	 * State Screen.
	 */
	public ActionForward cont(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method cont");
		}

		IObjectAssembly objAssembly = super.getObjectAssembly(request);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimAppData = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

		if (ClaimAppFlowTypeEnum.INT.getName().equals(
				claimAppData.getFlowType())) {
			return (mapping.findForward("continueintemp"));
		}

		return (super.continueForward(mapping));
	}

	/*
	 * Pre-ethod for Cancel button.
	 */
	public IObjectAssembly precancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method precancel");
		}

		IObjectAssembly objAssembly = getObjectAssemblyFromSession(request);
		return objAssembly;
	}

	/*
	 * Method for Cancel button.
	 */
	public ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method cancel");
		}

		return (mapping.findForward("cancel"));

	}

	/*
	 * Private method for preparation data for execution of the service method.
	 */
	private IObjectAssembly saveData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start of method saveData");
		}

		EffectiveDateForm currentForm = (EffectiveDateForm) form;

		// session processing
		IObjectAssembly objAssembly = getObjectAssemblyFromSession(request);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="Start")
		ClaimApplicationDataBridge claimApp = (ClaimApplicationDataBridge) objAssembly
				.fetchORCreate(ClaimApplicationDataBridge.class);
		// @cif_wy(storyNumber="P1-CM-011", requirementId="FR_1614",
		// designDocName="01 Regular UI Claim,EUCC,EB.docx",designDocSection="1.8.4",
		// dcrNo="DCR_MDDR_16, DCR_MDDR_17", mddrNo="", impactPoint="End")

		// populate values in ClaimApplicationData object
		claimApp.setBackDate(currentForm.getEffDate().getValue());
		// 4416
		/*
		 * claimApp.setBackDateReason(currentForm.getReasonBackdating());
		 * claimApp.setBackDateDetails(currentForm.getReasonOther());
		 */
		claimApp.setUpdatedBy(super.getUserId(request));

		return objAssembly;
	}
}
