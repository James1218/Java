<%-- claimsummary.jsp										--%>

<%-- This jsp shows current claim's summary					--%>
<%-- jsp			: claimsummary.jsp 						--%>
<%-- Action			: ClaimSummaryAction.java 				--%>
<%-- Form			: CinInqForm.java 							--%>
<%-- tiles-defs.xml	: .claimsummary (acccessms/WEB-INF/jsp) 	--%>


<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/taglib/accesstag" prefix="access"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- <%@ taglib uri="/taglib/Owasp.CsrfGuard" prefix="csrf" %> --%>


<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "input" omitted, but OMITTAG NO was specified--%>
<html:xhtml />
<%--CIF_01127 end --%>
<%--  form area & content area --%>

<html:form method="post" action="claimsummary">
	<table style="display: none">
		<tr>
			<td><input type="hidden" name="encKey"
				value="<c:out value="${encKeyEncrypted}"/>" id="encKeyId" /></td>
		</tr>
		<tr><td><input type="hidden" name="OWASP_CSRFTOKEN" value="<c:out value="${OWASP_CSRFTOKEN}"/>" id="secKeyId"/></td></tr>
	</table>

	<br />
	<html:errors />
	<br />
	<html:hidden property="claimantId" />
	<html:hidden property="ssn" />
	<%-- <input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value uri="/benefits/claimsummary.do"/>"/> --%>
	
<%-- 	<input type="hidden" name="OWASP_CSRFTOKEN" value="${securitykey}"/> --%>

	<div class="pageheader" align="center">
		<bean:message key="access.cin.inq.claimsummary.page.title" />
	</div>
	<br />
	<br />

	<c:set var="claimant" value="${cininqform.details[0].values}" />
	<c:set var="claimantAddress" value="${cininqform.details[1].values}" />
	<c:set var="claimantProfile" value="${cininqform.details[2].values}" />
	<c:set var="claimInformation" value="${cininqform.details[3].values}" />
	<c:set var="basePeriodQuarters" value="${cininqform.details[4]}" />
	<c:set var="weeklyCertifications" value="${cininqform.details[5]}" />
	<%--CIF_00429 --%>
	<%--This line was uncommented to enable the history log link in claim summary screen --%>
	<c:set var="historylog" value="${cininqform.details[6]}" />
	<c:set var="pensionInformation" value="${cininqform.details[7]}" />
	<c:set var="adjustedwba" value="${cininqform.details[8]}" />
	<c:set var="parentClaimDetails" value="${cininqform.details[9].values}" />
	<c:set var="teucAugmentation" value="${cininqform.details[11]}" />
	<c:set var="claimantResidentialAddress"
		value="${cininqform.details[12].values}" />
	<c:set var="ataaApplicationData" value="${cininqform.details[13]}" />
	<!--  CIF_02150 || Defect_3094 Rejected Weeks -->
	<c:set var="rejectedWeeksData" value="${cininqform.details[14]}" />
	<c:set var="pendingClaimData" value="${cininqform.details[16]}" />



	<c:choose>
		<c:when test="${claimant.claimantid == null}">
			<table class="tablefields">
				<%--CIF_01127 - Non-Functional Requirements/ Error: document type does not allow element "table" here--%>
				<%--CIF_01127 end --%>
				<tr>
					<%--CIF_01127 - Non-Functional Requirements/ Error: an attribute specification must start with a name or name token--%>
					<td class="tabledatathead"><bean:message
							key="access.cin.inq.claimsummary.notfound" /></td>
					<%--CIF_01127 end --%>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<c:set var="claimantId" value="${claimant.claimantid}" />
			<c:set var="claimantssn" value="${claimant.ssn}" />
			<c:set var="claimId" value="${claimInformation.claimid}" />
			<table class="tableinq" width="100%">
				<tr>
					<td class="textinq" align="center"><c:choose>
							<c:when test="${claimant.ssn != null}">

								<table class="tableinq" width="85%" align="center"
									style="margin: auto">
									<tr>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimant.ssn" /></td>
										<%--CIF_01127 end --%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "td" omitted, but OMITTAG NO was specified--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textdata" width="5%" nowrap="nowrap">${access:fmtssn(claimant.ssn)}</td>
										<%--CIF_01127 end --%>
										<td width="5%">&nbsp;</td>
										<%--CIF_00429 Start--%>
										<%--To align fields correctly--%>
										<td width="5%">&nbsp;</td>
										<td width="5%">&nbsp;</td>
										<%--CIF_00429 End--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimant.name" /></td>
										<%--CIF_01127 end --%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textdata" width="5%" nowrap="nowrap"><c:out
												value="${cininqform.fullNameWithMiddleInitial}" /></td>
										<%--CIF_01127 end --%>
									</tr>
									<tr>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimantdetails.selfservicemode" /></td>
										<%--CIF_01127 end --%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "td" omitted, but OMITTAG NO was specified--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<td class="textdata" width="5%" nowrap="nowrap">${access:fnenum(UserIdLockReasonEnum,cininqform.selfServMode)}</td>
										<%--CIF_01127 end --%>
										<td width="5%">&nbsp;</td>
										<%--CIF_01127 - Non-Functional Requirements/ Error: document type does not allow element "input" here--%>
										<td style="display: none"><html:hidden
												property="selfServMode" /></td>
										<%--CIF_01127 end --%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for element "td" which is not open--%>
										<%--CIF_01127 end --%>
										<%--CIF_00429 Start--%>
										<%--Commented Since this Field is removed from the MO Requirement--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<%-- <td class="textlabel" width="5%" nowrap="nowrap"><bean:message
														key="access.cin.inq.claimantdetails.ivr.pin.reset" /></td>
												<td class="textdata" width="5%" nowrap="nowrap"><c:out
														value="${cininqform.pinReset}" /></td> --%>
										<%--CIF_01127 end --%>
										<%--CIF_00429 End--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<c:if
											test="${YesNoTypeEnum.NumericYes.name == cininqform.ivrStatusMode}">
											<td></td>
											<td></td>
											<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.ivrstatusmode" /></td>
											<td class="textdata" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.disabled" /></td>
										</c:if>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for element "td" which is not open--%>
										<%--CIF_01127 end --%>
										<%--CIF_01127 end --%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
										<c:if
											test="${YesNoTypeEnum.NumericNo.name == cininqform.ivrStatusMode}">
											<td></td>
											<td></td>
											<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.ivrstatusmode" /></td>
											<td class="textdata" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.enabled" /></td>
										</c:if>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for element "td" which is not open--%>
										<%--CIF_01127 end --%>
										<%--CIF_01127 end --%>
									</tr>
									<%--CIF_00429 Start--%>
									<%--Commented Since this Field is removed from the MO Requirement--%>
									<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
									<%-- <tr>
												<c:if
													test="${YesNoTypeEnum.NumericYes.name == cininqform.ivrStatusMode}">
													<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.ivrstatusmode" /></td>
													<td class="textdata" width="5%" nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.disabled" /></td>
												</c:if>
											</tr>
											<tr>
												<c:if
													test="${YesNoTypeEnum.NumericNo.name == cininqform.ivrStatusMode}">
													<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.ivrstatusmode" /></td>
													<td class="textdata" width="5%" nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.enabled" /></td>
												</c:if>
											</tr> --%>
									<%--CIF_01127 end --%>
									<%--CIF_00429 End--%>
								</table>
								<br />

								<table class="tablefields" width="100%">
									<tr>
										<%--CIF_00429 Start--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "td" omitted, but OMITTAG NO was specified--%>
										<%--This block of code was commented in order to provide
										 selective access rights for CSR and claimant, for the Claimant Details hyperlink, in claim summary screen --%>
										<%-- <access:secure
														resourceGroup="externaluserandexternalentity"
														reverse="true">
														<access:link forwardName="claimantdetails"
															paramId="claimantId" paramName="claimantId">
															<bean:message key="access.cin.inq.claimant" />
														</access:link>
													</access:secure> <access:secure
														resourceGroup="externaluserandexternalentity">
														<bean:message key="access.cin.inq.claimant" />
													</access:secure></td>
													<td width="85%" valign="bottom">
													<hr/></td> --%>
										<%--CIF_00429 End--%>
										<%--CIF_00429 Start--%>
										<%--CIF_01127 end --%>
										<%--This block of code was added in order to provide
										 selective access rights for CSR and claimant, for the Claimant Details hyperlink, in claim summary screen --%>
										<access:usertypesecure userType="${UserTypeEnum.MDES.name}"
											reverse="true">
											<td width="15%"><access:link
													forwardName="claimantdetails" paramId="claimantId"
													paramName="claimantId">
													<bean:message key="access.cin.inq.claimant" />
												</access:link></td>
											<%--CIF_01127 end--%>
											<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
											<td width="85%"><hr /></td>
											<%--CIF_01127 end--%>
										</access:usertypesecure>


										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for element "td" which is not open--%>
										<%--CIF_01127 end --%>
									</tr>
								</table>
								<table class="tablefields" width="100%">
									<tr>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "tr" which is not finished--%>
										<td style="display: none"></td>
										<%--CIF_01127 end --%>
										<access:usertypesecure userType="${UserTypeEnum.CLMT.name}"
											reverse="true">
											<td width="15%"><bean:message
													key="access.cin.inq.claimant" /></td>
											<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
											<td width="85%"><hr /></td>
											<%--CIF_01127 end--%>
										</access:usertypesecure>
										<%--CIF_00429 End--%>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for element "td" which is not open--%>
										<%--CIF_01127 end --%>
									</tr>
								</table>
								<%--CIF_00429--%>
								<%--To align fields correctly--%>
								<table class="tablefields" width="85%" style="margin: auto"
									border="0">
									<tr>

										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimantdetails.address" /></td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:if
												test="${!empty claimantAddress.line1}">
												<c:out value="${claimantAddress.line1}" />
												<br />
											</c:if></td>
										<td width="5%">&nbsp;</td>

										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.phonenumber" /></td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:if
												test="${!empty claimantProfile.primaryphone}">
												<c:out
													value="${access:fmtphone(claimantProfile.primaryphone)}" />
											</c:if> <c:if test="${empty claimantProfile.primaryphone}">
												<bean:message key="access.na" />
											</c:if></td>
									</tr>
									<tr>


										<td width="5%">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:if
												test="${not empty claimantAddress.line2}">
												<c:out value="${claimantAddress.line2}" />
												<br />
											</c:if> <c:if test="${empty claimantAddress.line2}">
												<c:out value="${claimantAddress.city}" />&nbsp;
						  								 <c:out value="${claimantAddress.state}" />&nbsp;
						  								<c:if test="${claimantAddress.country != 'US'}">
													<c:out value="${claimantAddress.country}" />
												</c:if>
												<c:out value="${access:fmtzip(claimantAddress.zip)}" />&nbsp;
														</c:if></td>

										<td width="5%">&nbsp;</td>

										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.dob" /></td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:out
												value="${claimant.dob}" /></td>
									</tr>
									<tr>
										<td width="5%">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:if
												test="${!empty claimantAddress.line2}">
												<c:out value="${claimantAddress.city}" />&nbsp;
							<c:out value="${claimantAddress.state}" />&nbsp;
													<c:if test="${claimantAddress.country != 'US'}">
													<c:out value="${claimantAddress.country}" />
												</c:if>

												<c:out value="${access:fmtzip(claimantAddress.zip)}" />&nbsp;
							
													</c:if></td>

										<td width="5%">&nbsp;</td>

										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimantdetails.gender" /></td>

										<td class="textdata" width="5%" nowrap="nowrap"><c:if
												test="${claimantProfile.sex=='M'}">
												<c:out
													value="${applicationScope.ViewConstants.MALE_DESCRIPTION}" />
											</c:if> <c:if test="${claimantProfile.sex=='F'}">
												<c:out
													value="${applicationScope.ViewConstants.FEMALE_DESCRIPTION}" />
											</c:if> <c:if test="${empty claimantProfile.sex}">
												<bean:message key="access.na" />
											</c:if></td>
									</tr>
									<tr>

										<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
										<td width="5%">&nbsp;</td>

										<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
									</tr>
									<c:if test="${!empty claimantResidentialAddress.line1}">
										<tr>

											<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.address.residential" /></td>
											<td class="textdata" width="5%" nowrap="nowrap"><c:if
													test="${!empty claimantResidentialAddress.line1}">
													<c:out value="${claimantResidentialAddress.line1}" />
													<br />
												</c:if></td>
											<td width="5%">&nbsp;</td>

											<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
											<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
										</tr>
										<tr>
											<td width="5%">&nbsp;</td>
											<td class="textdata" width="5%" nowrap="nowrap"><c:if
													test="${not empty claimantResidentialAddress.line2}">
													<c:out value="${claimantResidentialAddress.line2}" />
													<br />
													<c:out value="${claimantResidentialAddress.city}" />&nbsp;
							   <c:out value="${claimantResidentialAddress.state}" />&nbsp; &nbsp;
						       <c:out
														value="${access:fmtzip(claimantResidentialAddress.zip)}" />

												</c:if> <c:if test="${empty claimantResidentialAddress.line2}">
													<c:out value="${claimantResidentialAddress.city}" />&nbsp;
							   <c:out value="${claimantResidentialAddress.state}" />&nbsp; &nbsp;
						       <c:out
														value="${access:fmtzip(claimantResidentialAddress.zip)}" />

												</c:if></td>

											<td width="5%">&nbsp;</td>

											<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>

											<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
										</tr>
									</c:if>
									<c:if test="${empty claimantResidentialAddress.line1}">
										<tr>

											<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
													key="access.cin.inq.claimantdetails.address.residential" /></td>
											<td class="textdata" width="5%" nowrap="nowrap"><bean:message
													key="access.na" /></td>
											<td width="5%">&nbsp;</td>

											<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
											<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
										</tr>
									</c:if>
									<tr>

										<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
										<td width="5%">&nbsp;</td>

										<td class="textlabel" width="5%" nowrap="nowrap">&nbsp;</td>
										<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
									</tr>
									<tr>
										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimantdetails.residencecounty" /></td>
										<td class="textdata" width="5%" nowrap="nowrap"><c:out
												value="${claimantAddress.residencecountydescription}" /></td>
										<td width="5%">&nbsp;</td>
										<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
												key="access.cin.inq.claimantdetails.reportlocation" /></td>
										<c:if test="${!empty claimantProfile.localofficedesciption}">
											<td class="textdata" width="5%" nowrap="nowrap"><c:out
													value="${claimantProfile.localofficedesciption}" /></td>
										</c:if>
										<c:if test="${empty claimantProfile.localofficedesciption}">
											<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
											<td class="textdata" nowrap="nowrap"><bean:message
													key="access.na" /></td>
											<%--CIF_01127 end --%>
										</c:if>

									</tr>
									<%--CIF_00429 Start--%>
									<%--Commented Since this Field is removed from the MO Requirement--%>
									<%-- <tr>
												<td class="textlabel" nowrap="nowrap"><bean:message
														key="access.nmon.investigation.factfinding.prospectflag" /></td>
												<td class="textdata">${access:fnenum(ProspectTypeEnum,
													claimantProfile.prospecttype)}</td>
											</tr> --%>
									<%--CIF_00429 End--%>

								</table>
								<br />

								<c:choose>
									<c:when test="${claimInformation.claimid != null && pendingClaimData == null}">
										<table class="tablefields" width="100%">
											<tr>
												<%--CIF_00429 Start--%>
												<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
												<%--This block of code was commented in order to provide
													 selective access rights for CSR and claimant, for the Claim Details hyperlink, in claim summary screen --%>
												<%-- <td width="13%"><access:secure
																resourceGroup="externaluserandexternalentity"
																reverse="true">
																<c:url var="claimDetailsLink" value="/linkaction.do">
<c:param name="${access:encryptParams('encKey',encKey)}" value="${access:encryptParams(encKey,encKey)}" />
																	<c:param name="${access:encryptParams('forwardName',encKey)}" value="${access:encryptParams('claimdetails',encKey)}" />
																	<c:param name="${access:encryptParams('claimantId',encKey)}"
																		value="${access:encryptParams(claimant.claimantid,encKey)}" />
																	<c:param name="${access:encryptParams('claimId',encKey)}"
																		value="${access:encryptParams(claimInformation.claimid,encKey)}" />
																</c:url>

																<html:link href="${fn:replace(claimDetailsLink,'&','&amp;')}">
																	<bean:message key="access.cin.inq.active.claim.details" />
																</html:link></td>
														<td width="87%" valign="bottom">
															 <hr/>
															 </access:secure> <access:secure
																resourceGroup="externaluserandexternalentity">
																<bean:message key="access.cin.inq.active.claim.details" />
															</access:secure>
														</td> --%>
												<%--CIF_00429 End--%>
												<%--CIF_00429 Start--%>
												<%--CIF_01127 end--%>
												<%--This block of code was added in order to provide
													 selective access rights for CSR and claimant, for the Claim Details hyperlink, in claim summary screen --%>
												<access:usertypesecure userType="${UserTypeEnum.MDES.name}"	reverse="true">
													<td width="13%"><c:url var="claimDetailsLink"
															value="/linkaction.do">
															<c:param name="${access:encryptParams('encKey',encKey)}"
																value="${access:encryptParams(encKey,encKey)}" />
															<c:param
																name="${access:encryptParams('forwardName',encKey)}"
																value="${access:encryptParams('claimdetails',encKey)}" />
															<c:param
																name="${access:encryptParams('claimantId',encKey)}"
																value="${access:encryptParams(claimant.claimantid,encKey)}" />
															<c:param name="${access:encryptParams('claimId',encKey)}"
																value="${access:encryptParams(claimInformation.claimid,encKey)}" />
														</c:url> <html:link
															href="${fn:replace(claimDetailsLink,'&','&amp;')}">
															<bean:message key="access.cin.inq.active.claim.details" />
														</html:link></td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="87%"><hr /></td>
													<%--CIF_01127 end--%>
												</access:usertypesecure>
												<%--//NTR || CIF_INT_04210  || UIM-6006  || claim details, monetary link access to external agency users--%>
													<access:usertypesecure userType="${UserTypeEnum.OTHR.name}"	reverse="true">
													<td width="13%"><c:url var="claimDetailsLink"
															value="/linkaction.do">
															<c:param name="${access:encryptParams('encKey',encKey)}"
																value="${access:encryptParams(encKey,encKey)}" />
															<c:param
																name="${access:encryptParams('forwardName',encKey)}"
																value="${access:encryptParams('claimdetails',encKey)}" />
															<c:param
																name="${access:encryptParams('claimantId',encKey)}"
																value="${access:encryptParams(claimant.claimantid,encKey)}" />
															<c:param name="${access:encryptParams('claimId',encKey)}"
																value="${access:encryptParams(claimInformation.claimid,encKey)}" />
														</c:url> <html:link
															href="${fn:replace(claimDetailsLink,'&','&amp;')}">
															<bean:message key="access.cin.inq.active.claim.details" />
														</html:link></td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="87%"><hr /></td>
													<%--CIF_01127 end--%>
												</access:usertypesecure>
											</tr>
										</table>
										<table class="tablefields" width="100%">
											<tr>
												<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "tr" which is not finished--%>
												<td style="display: none"></td>
												<%--CIF_01127 end --%>
												<access:usertypesecure userType="${UserTypeEnum.CLMT.name}"
													reverse="true">
													<td width="13%"><bean:message
															key="access.cin.inq.active.claim.details" /></td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="87%"><hr /></td>
													<%--CIF_01127 end--%>
												</access:usertypesecure>
												<%--CIF_00429 End--%>
											</tr>
										</table>

										<table class="tablefields" width="85%" style="margin: auto"
											border="0">
											<tr>

												<td id="type" class="textlabel" width="5%" nowrap="nowrap"><bean:message
														key="access.cin.inq.claimantdetails.type" /></td>

												<c:choose>
													<c:when test="${'TRA' == claimInformation.entitlementtype}">
														<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
														<td class="textdata" nowrap="nowrap"><c:out
																value="${access:fnenum(EntitlementTypeEnum,claimInformation.entitlementtype)}" />
															<c:if test="${not empty cininqform.traClaimType}">
																<%-- show this only if NOT dua--%>
																<br />- <c:out
																	value="${access:fnenum(TrClaimTypeEnum,cininqform.traClaimType)}" />
															</c:if></td>
														<%--CIF_01127 end --%>
													</c:when>
													<c:when test="${'TUC' == claimInformation.entitlementtype}">
														<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
														<td class="textdata" nowrap="nowrap"><c:out
																value="${access:fnenum(EntitlementTypeEnum,claimInformation.entitlementtype)}" />
															<c:if test="${not empty claimInformation.teucclaimtype}">
																<%-- show this only if NOT dua--%>  
									- <c:out
																	value="${access:fnenum(TeucClaimTypeEnum,claimInformation.teucclaimtype)}" />
															</c:if></td>
														<%--CIF_01127 end --%>
													</c:when>
													<c:when test="${not empty claimInformation.programtype}">
														<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
														<td class="textdata" nowrap="nowrap"><c:out
																value="${access:fnenum(EntitlementTypeEnum,claimInformation.entitlementtype)}" />
															<c:if test="${'DUA' ne claimInformation.entitlementtype}">
																<%-- show this only if NOT dua--%> 
				                   	 -<c:out
																	value="${access:fnenum(ProgramTypeEnum,claimInformation.programtype)}" />
															</c:if></td>
														<%--CIF_01127 end --%>
													</c:when>

													<c:otherwise>
														<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
														<td class="textdata" nowrap="nowrap"><c:out
																value="${access:fnenum(EntitlementTypeEnum,claimInformation.entitlementtype)}" />
															<c:if test="${'REG' == claimInformation.entitlementtype}">
																<%-- show this only if NOT dua--%>  
									-<c:out value="N/A" />
															</c:if></td>
														<%--CIF_01127 end --%>
													</c:otherwise>
												</c:choose>

												<td width="5%">&nbsp;</td>
												<td id="status" class="textlabel" width="5%" nowrap="nowrap"><bean:message
														key="access.cin.inq.claimdetails.status" /></td>
												<td class="textdata" width="5%" nowrap="nowrap"><c:out
														value="${claimInformation.claimstatusdescription}" /></td>


											</tr>
											<c:if
												test="${EntitlementTypeEnum.TEUC.name == claimInformation.entitlementtype }">
												<tr>
													<td id="begindate" class="textlable" width="1%"
														nowrap="nowrap"><bean:message
															key="access.cin.inq.claimdetails.parent.begin.date" /></td>
													<td class="textdata" width="1%" nowrap="nowrap"><c:out
															value="${parentClaimDetails.byedate}" /></td>
													<td width="5%">&nbsp;</td>
													<td class="textlable" colspan="2">&nbsp;</td>

												</tr>
											</c:if>

											<c:choose>
												<c:when test="${'TRA' == claimInformation.entitlementtype}">
													<tr>
														<td id="trabybdate" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.trabybdate" /></td>

														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${cininqform.bybDate}" /></td>

														<td width="5%">&nbsp;</td>

														<td id="separationdate" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.separationdate" /></td>

														<td class="textdata" width="5%" nowrap="nowrap">
															<%--<c:out	value="${claimInformation.separationdate}" />--%>
															<fmt:formatDate
																value="${claimInformation.separationdate}"
																pattern="MM/dd/yyyy" />
														</td>
													</tr>
												</c:when>
											</c:choose>

											<tr>
												<td id="claimbegindate" class="textlabel" width="5%"
													nowrap="nowrap">
													<%--CIF_00429--%> <%--To display correct name of field--%>
													<bean:message key="access.cin.inq.viewwages.claimbegindate" />
												</td>
												<td class="textdata" width="5%" nowrap="nowrap"><c:out
														value="${claimInformation.effectivedate}" /></td>
												<td width="5%">&nbsp;</td>
												<c:choose>
													<c:when test="${'TRA' == claimInformation.entitlementtype}">
														<td id="traclaimfiledate" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.traclaimfiledate" /></td>

														<c:choose>
															<%-- <c:when test="${'ALOW' == claimInformation.traclaimstatus}">--%>
															<c:when
																test="${'TRA' == claimInformation.entitlementtype}">
																<td class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${cininqform.fileDate}" /></td>
															</c:when>
															<c:otherwise>
																<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
															</c:otherwise>
														</c:choose>

													</c:when>
													<%-- <c:when
																test="${'TUC' == claimInformation.entitlementtype}">
																<td  class="textdata" width="5%" nowrap="nowrap"></td>
															</c:when> --%>
													<c:otherwise>


														<td id="period" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.base.period" /></td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${basePeriodQuarters[0]}" /></td>
													</c:otherwise>
												</c:choose>
											</tr>
											<%-- Other than EB Entitlement Type --%>
											<c:if test="${'SEB' != claimInformation.entitlementtype}">
												<tr>
													<td id="claimenddate" class="textlabel" width="5%"
														nowrap="nowrap">
														<%--CIF_00429--%> <%--To display correct name of field--%>
														<bean:message key="access.cin.inq.viewwages.claimenddate" />
													</td>
													<c:if test="${'TRA' == claimInformation.entitlementtype}">
														<c:choose>
															<c:when
																test="${'ALOW' == claimInformation.traclaimstatus}">
																<td class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${claimInformation.byedate}" /></td>
															</c:when>
															<c:otherwise>
																<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
															</c:otherwise>
														</c:choose>
													</c:if>
													<c:if test="${'TRA' != claimInformation.entitlementtype}">
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${claimInformation.byedate}" /></td>
													</c:if>
													<td id="space" width="5%">&nbsp;</td>
													<c:choose>
														<c:when
															test="${'TRA' == claimInformation.entitlementtype}">
															<td id="petitionnum" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.petitionnum" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${cininqform.petitionNum}" /></td>
														</c:when>
														<c:otherwise>

															<td class="textlabel" width="5%" nowrap="nowrap"></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${basePeriodQuarters[1]}" /></td>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:if>
											<c:if test="${'SEB' == claimInformation.entitlementtype}">
												<tr>
													<td id="ebenddate" class="textlabel" width="5%"
														nowrap="nowrap"><bean:message
															key="access.cin.inq.claimsummary.eb.ebenddate" /></td>
													<c:if test="${'TRA' == claimInformation.entitlementtype}">
														<c:choose>
															<c:when
																test="${'ALOW' == claimInformation.traclaimstatus}">
																<td class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${claimInformation.byedate}" /></td>
															</c:when>
															<c:otherwise>
																<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
															</c:otherwise>
														</c:choose>
													</c:if>
													<c:if test="${'TRA' != claimInformation.entitlementtype}">
														<td class="textdata" width="5%" nowrap="nowrap"><fmt:formatDate
																value="${claimInformation.eb_end_date}"
																pattern="MM/dd/yyyy" /></td>
													</c:if>
													<td width="5%">&nbsp;</td>
													<c:choose>
														<c:when
															test="${'TRA' == claimInformation.entitlementtype}">
															<td id="petitionnum.1" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.petitionnum" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${cininqform.petitionNum}" /></td>
														</c:when>
														<c:otherwise>
															<td class="textlabel" width="5%" nowrap="nowrap"></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${basePeriodQuarters[1]}" /></td>
														</c:otherwise>
													</c:choose>
												</tr>
												<tr>
													<td id="ebtriggeroffdate" class="textlabel" width="5%"
														nowrap="nowrap"><bean:message
															key="access.cin.inq.claimsummary.eb.ebtriggeroffdate" /></td>
													<c:if test="${'TRA' == claimInformation.entitlementtype}">
														<c:choose>
															<c:when
																test="${'ALOW' == claimInformation.traclaimstatus}">
																<td class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${claimInformation.byedate}" /></td>
															</c:when>
															<c:otherwise>
																<td class="textdata" width="5%" nowrap="nowrap">&nbsp;</td>
															</c:otherwise>
														</c:choose>
													</c:if>
													<c:if test="${'TRA' != claimInformation.entitlementtype}">
														<td class="textdata" width="5%" nowrap="nowrap"><fmt:formatDate
																value="${claimInformation.eb_trigger_off_date}"
																pattern="MM/dd/yyyy" /></td>
													</c:if>
													<td id="space1" width="5%">&nbsp;</td>
													<c:choose>
														<c:when
															test="${'TRA' == claimInformation.entitlementtype}">
															<td class="textlabel" width="5%" nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.petitionnum" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${cininqform.petitionNum}" /></td>
														</c:when>
														<c:otherwise>
															<td class="textlabel" width="5%" nowrap="nowrap"></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="${basePeriodQuarters[1]}" /></td>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:if>

											<tr>
												<c:if test="${'TRA' == claimInformation.entitlementtype}">
													<c:choose>
														<c:when
															test="${'ALOW' == claimInformation.traclaimstatus}">
															<td id="wba" class="textlabel" width="5%" nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message
																	key="access.cin.claimantdetails.weekly.wba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap">$
																&nbsp;<fmt:formatNumber value="${claimInformation.wba}"
																	pattern=",##0.00" />
															</td>
														</c:when>
														<c:otherwise>
															<td id="wba.1" class="textlabel" width="5%"
																nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message
																	key="access.cin.claimantdetails.weekly.wba" />
															</td>
															<td class="tdnumber" width="5%" nowrap="nowrap"><span
																class="textdata"> &nbsp;</span></td>
														</c:otherwise>
													</c:choose>
												</c:if>


												<c:if test="${'TRA' != claimInformation.entitlementtype}">
													<c:choose>
														<c:when test="${claimInformation.wba != null}">
															<td id="wba.2" class="textlabel" width="5%"
																nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message
																	key="access.cin.claimantdetails.weekly.wba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap">$&nbsp;<fmt:formatNumber
																	value="${claimInformation.wba}" pattern=",##0.00" /></td>
														</c:when>
														<c:when test="${claimInformation.wba == null}">
															<td class="textlabel" width="5%" nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message
																	key="access.cin.claimantdetails.weekly.wba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="N/A" /></td>
														</c:when>
													</c:choose>
												</c:if>

												<td id="space2" width="35">&nbsp;</td>
												<c:choose>
													<c:when test="${'TRA' == claimInformation.entitlementtype}">
														<td id="certdate" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.certdate" /></td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${cininqform.certDate}" /></td>
													</c:when>
													<%-- <c:when
																test="${'TUC' == claimInformation.entitlementtype}">
																<td id="amount" class="textlablel" nowrap="nowrap"><bean:message
																		key="access.cin.inq.claimdetails.sequestra.weekly.amount" /></td>
																<td  class="tdnumber" width="5%" nowrap="nowrap"><span
																	class="textdata"> $ &nbsp;<fmt:formatNumber
																			value="${claimInformation.sequeswba}"
																			pattern=",##0.00" /></span></td>
															</c:when> --%>
													<c:otherwise>
														<td width="5%">&nbsp;</td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${basePeriodQuarters[2]}" /></td>
													</c:otherwise>
												</c:choose>
											</tr>

											<tr>
												<c:if test="${'TRA' == claimInformation.entitlementtype}">
													<c:choose>
														<c:when
															test="${((('ALOW' == claimInformation.traclaimstatus) && ('BSIC' == cininqform.traClaimType)) or
	  									(cininqform.stimulus2009Flag == '1'))}">
															<td id="mba" class="textlabel" width="5%" nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message key="access.cin.determinebenefits.mba" />
															</td>

															<td class="textdata" width="5%" nowrap="nowrap">
																$&nbsp;<c:if
																	test="${not empty claimInformation.teucclaimtype && claimInformation.teucclaimtype == 'TIR3'}">

																	<c:if
																		test="${(claimInformation.mba-claimInformation.wk14_eligible_amount)>0}">
																		<fmt:formatNumber
																			value="${claimInformation.mba-claimInformation.wk14_eligible_amount}"
																			pattern=",##0.00" />
																	</c:if>
																	<c:if
																		test="${(claimInformation.mba-claimInformation.wk14_eligible_amount)<=0}">
																		<fmt:formatNumber value="${claimInformation.mba}"
																			pattern=",##0.00" />
																	</c:if>
																</c:if> <c:if
																	test="${empty claimInformation.teucclaimtype || claimInformation.teucclaimtype != 'TIR3'}">
																	<fmt:formatNumber value="${claimInformation.mba}"
																		pattern=",##0.00" />
																</c:if>
															</td>
														</c:when>
														<c:otherwise>
															<td id="mba.1" class="textlabel" width="5%"
																nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message key="access.cin.determinebenefits.mba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap">N/A</td>
														</c:otherwise>
													</c:choose>
												</c:if>



												<c:if test="${'TRA' != claimInformation.entitlementtype}">
													<c:choose>
														<c:when
															test="${claimInformation.mba != null && claimInformation.entitlementtype != 'DUA'}">
															<td id="mba.2" class="textlabel" width="5%"
																nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message key="access.cin.determinebenefits.mba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap">$&nbsp;<c:if
																	test="${empty claimInformation.teucclaimtype || claimInformation.teucclaimtype != 'TIR3'}"><fmt:formatNumber value="${claimInformation.mba}"
																		pattern=",##0.00" />
																</c:if><c:if
																	test="${not empty claimInformation.teucclaimtype && claimInformation.teucclaimtype == 'TIR3'}">
																	<c:if
																		test="${(claimInformation.mba-claimInformation.wk14_eligible_amount)>0}"><fmt:formatNumber
																			value="${claimInformation.mba-claimInformation.wk14_eligible_amount}"
																			pattern=",##0.00" />
																	</c:if><c:if
																		test="${(claimInformation.mba-claimInformation.wk14_eligible_amount)<=0}">
																		<fmt:formatNumber value="${claimInformation.mba}"
																			pattern=",##0.00" />
																	</c:if>
																</c:if>
															</td>
														</c:when>
														<c:when
															test="${claimInformation.mba == null || claimInformation.entitlementtype == 'DUA'}">
															<td id="mba.3" class="textlabel" width="5%"
																nowrap="nowrap">
																<%--CIF_00429--%> <%--To display correct name of field--%>
																<bean:message key="access.cin.determinebenefits.mba" />
															</td>
															<td class="textdata" width="5%" nowrap="nowrap">N/A</td>
														</c:when>
													</c:choose>
												</c:if>

												<td id="space3" width="5%">&nbsp;</td>
												<c:choose>
													<c:when test="${'TRA' == claimInformation.entitlementtype}">
														<td id="impactdate" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.impactdate" /></td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${cininqform.impactDate}" /></td>
													</c:when>
													<%-- 	<c:when
																test="${'TUC' == claimInformation.entitlementtype}">
																<td id="amount.1" class="textlabel" width="5%" nowrap="nowrap"><bean:message
																		key="access.cin.inq.claimdetails.sequestra.maximum.amount" /></td>
																<td  class="tdnumber" width="5%" nowrap="nowrap"><span
																	class="textdata"> $ &nbsp; <fmt:formatNumber
																			value="${claimInformation.sequesmba}"
																			pattern=",##0.00" /></span></td>
															</c:when> --%>
													<c:otherwise>
														<td width="5%">&nbsp;</td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${basePeriodQuarters[3]}" /></td>
													</c:otherwise>
												</c:choose>
											</tr>




											<tr>


												<c:if test="${'TRA' == claimInformation.entitlementtype}">
													<c:choose>

														<c:when
															test="${((('ALOW' == claimInformation.traclaimstatus)  && ('BSIC' == cininqform.traClaimType)) or
	  									(cininqform.stimulus2009Flag == '1'))}">
															<td id="mbabalance" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.mba.balance" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:if
																	test="${claimInformation.mbabalance == '0.00'}"> $
																				&nbsp;<fmt:formatNumber
																		value="${claimInformation.mbabalance}"
																		pattern=",##0.00" />

																</c:if> <c:if test="${claimInformation.mbabalance != '0.00'}"> $ &nbsp;<fmt:formatNumber
																		value="${claimInformation.mbabalance}"
																		pattern=",##0.00" />
																</c:if></td>
														</c:when>
														<c:otherwise>
															<td id="mbabalance.1" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.mba.balance" /></td>
															<td class="textdata" width="5%" nowrap="nowrap">
																<%--CIF_02928 : Defect_4135 - displaying the week balance --%>
																<fmt:formatNumber value="${cininqform.traWeekBalance}"
																	pattern=",##0" />&nbsp;<bean:message
																	key="access.cin.inq.claimsummary.weeks" />
															</td>
														</c:otherwise>
													</c:choose>
												</c:if>
											</tr>
											<tr>
												<c:if test="${'TRA' != claimInformation.entitlementtype}">
													<c:choose>
														<%-- CIF_03128 Start - Defect_5331 --%>
														<c:when
															test="${'DUA' == claimInformation.entitlementtype || claimInformation.mbabalance == null}">
															<td id="mbabalance.3" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.mba.balance" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:out
																	value="N/A" /></td>
														</c:when>
														<%-- CIF_03128 End --%>
														<c:when test="${claimInformation.mbabalance != null}">
															<td id="mbabalance.2" class="textlabel" width="5%"
																nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.mba.balance" /></td>
															<td class="textdata" width="5%" nowrap="nowrap"><c:if
																	test="${claimInformation.mbabalance == '0.00'}">
																				$&nbsp;<fmt:formatNumber
																		value="${claimInformation.mbabalance}"
																		pattern=",##0.00" />

																</c:if> <c:if test="${claimInformation.mbabalance != '0.00'}"> $&nbsp;<fmt:formatNumber
																		value="${claimInformation.mbabalance}"
																		pattern=",##0.00" />
																</c:if></td>
														</c:when>
														<%-- <c:when test="${claimInformation.mbabalance == null}">
																	<td id="mbabalance.3" class="textlabel" width="5%" nowrap="nowrap"><bean:message
																			key="access.cin.inq.claimantdetails.mba.balance" /></td>
																	<td  class="textdata" width="5%" nowrap="nowrap"><c:out
																			value="N/A" /></td>
																</c:when> --%>
													</c:choose>
												</c:if>
												<%-- 	<c:if test="${'TUC' == claimInformation.entitlementtype}">
															<td  class="textlabel" width="5%" nowrap="nowrap"></td>
															<td id="sequestrabalance" class="textlabel" width="5%" nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimdetails.sequestra.balance" /></td>
															<td  class="tdnumber" width="5%" nowrap="nowrap"><span
																class="textdata"> $&nbsp;<fmt:formatNumber
																		value="${claimInformation.sequesbal}"
																		pattern=",##0.00" /></span></td>
														</c:if> --%>
											</tr>




											<c:if
												test="${ not empty claimInformation.teucclaimtype && claimInformation.teucclaimtype == 'TIR3'}">
												<tr>
													<td id="tier2mba" class="textlabel" width="5%"
														nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.tier3.plus.tier2.mba" /></td>
													<td class="tdnumber" width="5%" nowrap="nowrap"><span
														class="textdata"> $&nbsp;<fmt:formatNumber
																value="${claimInformation.mba}" pattern=",##0.00" /></span></td>
												</tr>
											</c:if>


											<c:if
												test="${ not empty claimInformation.teucclaimtype && claimInformation.teucclaimtype == 'TIR1'}">
												<tr>
													<td id="tier1mba" class="textlabel" width="5%"
														nowrap="nowrap"><bean:message
															key="access.cin.inq.claimantdetails.basic.plus.tier1.mba" /></td>
													<td class="textdata" width="5%" nowrap="nowrap"><span
														class="textdata"> $&nbsp;<fmt:formatNumber
																value="${claimInformation.tieronemba}" pattern=",##0.00" /></span></td>
												</tr>
											</c:if>

					<!-- //@cif_wy(impactNumber = "P1-FE-010", requirementId = "FR_1632", designDocName = "03 General - 2nd review", designDocSection = "7.2.3", dcrNo = "", mddrNo = "", impactPoint = "START") -->
					<!-- 	#CIF_P1-FE-001 - few not needed in wy   --%> -->
						
						<access:states state="MO">
								<%@include file="claimsummarymo.jsp"%>
		 	  			</access:states>
              			<access:states state="WY">
								<%@include file="claimsummarywy.jsp"%>
		 	  			</access:states>	
					<!-- //@cif_wy(impactNumber = "P1-FE-010", requirementId = "FR_1632", designDocName = "03 General - 2nd review", designDocSection = "7.2.3", dcrNo = "", mddrNo = "", impactPoint = "END") -->

					
											<%-- <tr>
												<td id="week" class="textlabel" width="5%" nowrap="nowrap">
													CIF_00429 To display correct name of field
													<bean:message
														key="access.cin.inq.claimantdetails.waiting.week" />
												</td>
												<td class="textdata" width="5%" nowrap="nowrap"><c:choose>
														<c:when
															test="${'REG' == claimInformation.entitlementtype}">
															<c:if test="${claimInformation.waitingperiodflag == '1'}">
																<c:out
																	value="${WaitingWeekStatusEnum.SERVED.description}" />
															</c:if>
															<c:if test="${claimInformation.waitingperiodflag == '0'}">
																<c:out
																	value="${WaitingWeekStatusEnum.NOT_SERVED.description}" />
															</c:if>
															<c:if test="${claimInformation.waitingperiodflag == '2'}">
																<c:out
																	value="${WaitingWeekStatusEnum.WAIVED_OFF.description}" />
															</c:if>
															<!-- CIF_02124  || Defect_3121	Add code for waiting week Paid Start -->
															<c:if test="${claimInformation.waitingperiodflag == '3'}">
																<c:out value="${WaitingWeekStatusEnum.PAID.description}" />
															</c:if>
															<!-- CIF_02124  || Add code for waiting week Paid End -->
														</c:when>
														<c:otherwise>
															<c:out value="N/A" />
														</c:otherwise>
													</c:choose></td>
											</tr>



											<tr>
												<c:choose>
													<c:when
														test="${pensionInformation.values.pensionamount != null}">
														<td id="pension" class="textlabel" width="5%"
															nowrap="nowrap">
															CIF_00429 To display correct name of field
															<bean:message
																key="access.cin.inq.claimantdetails.weekly.pension" />
														</td>
														DEFECT_1992
														<td class="textdata" width="5%" nowrap="nowrap">$
															&nbsp;<fmt:formatNumber
																value="${pensionInformation.values.pensionamount}"
																pattern="##0.00" />
														</td>
													</c:when>
													<c:when
														test="${pensionInformation.values.pensionamount == null}">
														<td id="pension.1" class="textlabel" width="5%"
															nowrap="nowrap">
															CIF_00429 To display correct name of field
															<bean:message
																key="access.cin.inq.claimantdetails.weekly.pension" />
														</td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="N/A" /></td>
													</c:when>
												</c:choose>
											</tr>
											<tr>
												<c:choose>
													<c:when test="${adjustedwba != null}">
														<td id="finalwba" class="textlabel" width="5%"
															nowrap="nowrap">
															CIF_00429 To display correct name of field
															<bean:message
																key="access.cin.inq.claimantdetails.final.wba" />
														</td>
														<td class="textdata" width="5%" nowrap="nowrap">$
															&nbsp;<fmt:formatNumber value="${adjustedwba}"
																pattern=",##0.00" />
														</td>
													</c:when>
													<c:when test="${adjustedwba == null}">
														<td id="finalwba.1" class="textlabel" width="5%"
															nowrap="nowrap">
															CIF_00429 To display correct name of field
															<bean:message
																key="access.cin.inq.claimantdetails.final.wba" />
														</td>
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="N/A" /></td>
													</c:when>
												</c:choose>
												<c:choose>
															<c:when test="${adjustedwba != null}">
																<td id="finalwba.2" class="textlabel" width="5%" nowrap="nowrap">
																	CIF_00429
																	To display correct name of field <bean:message
																		key="access.cin.inq.claimantdetails.final.wba" />
																</td>
																<td  class="tdnumber" width="5%" nowrap="nowrap"><span
																	class="textdata"> $ &nbsp;<fmt:formatNumber
																			value="${adjustedwba}" pattern=",##0.00" /></span></td>
															</c:when>
														</c:choose>
												CIF_00429 Start
												This block of code was commented to remove the 'oil spill' field

													<td  class="textlabel" width="5%" nowrap="nowrap"></td>
														<td  class="textlabel" width="5%" nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.oilspill" /></td>
														<td  class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${cininqform.isoilSpillClaim}" /></td>
												CIF_00429 End
											</tr>

											<c:if test="${'TRA' != claimInformation.entitlementtype}">

												<tr>
													CIF_00429 Start
													This block of code was commented to remove the 'Seasonal Benefits' field
															<td  class="textlabel" width="5%" nowrap="nowrap"><bean:message
																	key="access.cin.inq.claimantdetails.seasonalbenefits" /></td>
															<c:if test="${claimInformation.seasonalflag == '1'}">
																<td  class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${YesNoTypeEnum.NumericYes.description}" /></td>
															</c:if>
															<c:if test="${claimInformation.seasonalflag == '0'}">
																<td  class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="${YesNoTypeEnum.NumericNo.description}" /></td>
															</c:if>
															<c:if test="${claimInformation.seasonalflag == null}">
																<td  class="textdata" width="5%" nowrap="nowrap"><c:out
																		value="N/A" /></td>
															</c:if>
													CIF_00429 End

													CIF_00429 End

													<td id="nonschoolbalance" class="textlabel" width="5%"
														nowrap="nowrap">
														CIF_00429 To display correct name of field
														<bean:message
															key="access.cin.inq.claimantdetails.nonschoolbalance" />
													</td>
													<c:if test="${claimInformation.schoolflag == '1'}">
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${YesNoTypeEnum.NumericYes.description}" /></td>
													</c:if>
													<c:if test="${claimInformation.schoolflag == '0'}">
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="${YesNoTypeEnum.NumericNo.description}" /></td>
													</c:if>
													<c:if test="${claimInformation.schoolflag == null}">
														<td class="textdata" width="5%" nowrap="nowrap"><c:out
																value="N/A" /></td>
													</c:if>
												</tr>

												<tr>
													<c:if
														test="${(claimInformation.seasonalflag == '1') && (claimInformation.wba > '0.00')}">
														<td id="claimantdetailwba" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.wba" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.wba}" pattern=",##0.00" /></span></td>
													</c:if>

													<c:if test="${claimInformation.seasonalflag == '0'}">
														<td id="space4" width="5%">&nbsp;</td>
														<td id="space5" width="5%">&nbsp;</td>
														<td id="space6" width="5%">&nbsp;</td>
													</c:if>
													<c:if test="${claimInformation.seasonalflag == null}">
														<td id="space7" width="5%">&nbsp;</td>
														<td id="space8" width="5%">&nbsp;</td>
														<td id="space9" width="5%">&nbsp;</td>
													</c:if>

													<c:if test="${claimInformation.schoolflag == '1'}">
														<td id="claimantdetailwba.1" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.wba" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.schoolwba}" pattern=",##0.00" /></span></td>
													</c:if>


												</tr>


												<tr>
													<c:if
														test="${(claimInformation.seasonalflag == '1') && (claimInformation.seasonalmba > '0.00')}">
														<td id="claimantdetailmba" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.mba" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.seasonalmba}"
																	pattern=",##0.00" /></span></td>
													</c:if>
													<c:if test="${claimInformation.seasonalflag == '0'}">
														<td id="space10" width="5%">&nbsp;</td>
														<td id="space11" width="5%">&nbsp;</td>
														<td id="space12" width="5%">&nbsp;</td>
													</c:if>
													<c:if test="${claimInformation.seasonalflag == null}">
														<td id="space13" width="5%">&nbsp;</td>
														<td id="space14" width="5%">&nbsp;</td>
														<td id="space15" width="5%">&nbsp;</td>
													</c:if>


													<c:if test="${claimInformation.schoolflag == '1'}">
														<td id="claimantdetailmba.1" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.mba" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.schoolmba}" pattern=",##0.00" /></span></td>
													</c:if>

												</tr>

												<tr>
													<c:if
														test="${(claimInformation.seasonalflag == '1') && (claimInformation.seasonalmbabalance > '0.00')}">
														<td id="balance" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.balance" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.seasonalmbabalance}"
																	pattern=",##0.00" /></span></td>
													</c:if>
													<c:if test="${claimInformation.seasonalflag == '0'}">
														<td id="space16" width="5%">&nbsp;</td>
														<td id="space17" width="5%">&nbsp;</td>
														<td id="space18" width="5%">&nbsp;</td>
													</c:if>
													<c:if test="${claimInformation.seasonalflag == null}">
														<td id="space19" width="5%">&nbsp;</td>
														<td id="space20" width="5%">&nbsp;</td>
														<td id="space21" width="5%">&nbsp;</td>
													</c:if>

													<c:if test="${claimInformation.schoolflag == '1'}">
														<td id="balance.1" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.balance" /></td>
														<td class="tdnumber" width="5%" nowrap="nowrap"><span
															class="textdata"> $ &nbsp;<fmt:formatNumber
																	value="${claimInformation.schoolmbabalance}"
																	pattern=",##0.00" /></span></td>
													</c:if>
												</tr>
											</c:if>

											<c:choose>
												<c:when test="${'TRA' == claimInformation.entitlementtype}">

													<tr>
														<td id="traeligibility" class="textlabel" width="5%"
															nowrap="nowrap"><bean:message
																key="access.cin.inq.claimantdetails.traeligibility" /></td>
														<td class="textdata" colspan="4">${access:fnenum(TraClaimStatusEnum,claimInformation.traclaimstatus)}</td>


													</tr>

												</c:when>
											</c:choose>
											CIF_02121 Start


											<tr>
												<td class="textlabel" align="left"><bean:message
														key="access.cin.inq.claimdetails.totalforfeiture" /></td>
												<td class="textdata"><c:out
														value="${claimInformation.totalforfeiture}" /></td>
												<td width="5%">&nbsp;</td>
												<td class="textlabel" align="left"><bean:message
														key="access.cin.inq.claimdetails.balanceforfeiture" /></td>
												<td class="textdata"><c:out
														value="${claimInformation.balanceforfeiture}" /></td>

											</tr> --%>
											<%--CIF_02121 End --%>
                             <!--        @cif_wy(impactNumber = "P1-FE-010", requirementId = "FR_1632", designDocName = "03 General - 2nd review", designDocSection = "7.2.3", dcrNo = "", mddrNo = "", impactPoint = "END") -->

										</table>
									</c:when>
									<c:otherwise>
										<table class="tablefields" width="100%">
											<tr>
										
												<td width="18%"><bean:message
														key="access.cin.inq.active.claim.details" /></td>
												<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
												<td width="82%" valign="bottom">
													<hr />
												</td>
												<%--CIF_01127 end--%>
											</tr>
											<tr>
												<c:if test="${pendingClaimData == null}">
												<td colspan="3"><bean:message
														key="access.cin.inq.claim.noton.file" /></td>
												</c:if>
												<c:if test="${pendingClaimData != null}">
												<td colspan="3"><bean:message
														key="access.cin.pendingclaim.page.title" /></td>
												</c:if>		
											</tr>
										</table>
									</c:otherwise>
								</c:choose>

								<br />


								<c:if test="${cininqform.details[11] != null}">
									<br />
									<table class="tablefields" width="100%" style="margin: auto">
										<tr>
											<%--CIF_01014:Start --%>
											<td width="20%" class="textlabelbold" nowrap="nowrap"><a
												href="javascript:openContextHelp('inq/euc_sequestration.htm')">
													<img border='0' title="Help Question" alt="Help Question"
													src="images/help_question.gif" />
											</a> <bean:message
													key="access.cin.inq.claimdetails.teucaugmentation" /></td>
											<%--CIF_01014:End --%>
											<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
											<td width="80%" valign="bottom">
												<hr />
											</td>
											<%--CIF_01127 end--%>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
									</table>
								</c:if>

								<c:if test="${cininqform.details[11] != null}">
									<%--CIF_01638 - Mozilla Firefox Bug/ Cellspacing attribute added to table--%>
									<table class="tabledatainq" cellspacing="0" align="center"
										width="80%" style="margin: auto">
										<%--CIF_01638 end--%>
										<tr class="firefoxspecial2">
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.augmentationtype" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.effectivedate" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.mba" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.weeksuplimented" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.mbasuplimented" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.mbabalance" /></td>
										</tr>

										<c:forEach var="teucAugmentation"
											items="${cininqform.details[11]}">
											<tr>
												<td class="textdata" align="center"><c:out
														value="${teucAugmentation.values.description}" /></td>
												<td class="textdata" align="center"><c:out
														value="${teucAugmentation.values.effectivedate}" /></td>
												<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
												<td class="tdnumber" nowrap="nowrap" style="color: navy;">$&nbsp;<fmt:formatNumber
														value="${teucAugmentation.values.mba}" pattern=",##0.00" /></td>
												<%--CIF_01127 end --%>
												<td class="textdata" align="center"><c:out
														value="${teucAugmentation.values.thirteenwksuplimented}" /></td>
												<c:if
													test="${'TIR4'==teucAugmentation.values.augmentationtype}">
													<td class="textdata" align="center" nowrap="nowrap"><span
														class="textdata">N/A</span></td>
												</c:if>
												<c:if
													test="${'TIR2'==teucAugmentation.values.augmentationtype}">
													<td class="tdnumber" nowrap="nowrap"><span
														class="textdata">$&nbsp;<fmt:formatNumber
																value="${teucAugmentation.values.mbaplussuplimental}"
																pattern=",##0.00" /></span></td>
												</c:if>
												<td class="tdnumber" nowrap="nowrap"><span
													class="textdata">$&nbsp;<fmt:formatNumber
															value="${teucAugmentation.values.mbabalance}"
															pattern=",##0.00" /></span></td>
											</tr>
										</c:forEach>
									</table>
								</c:if>

								<c:if test="${cininqform.details[11] != null}">
									<br />
									<table class="tablefields" width="100%" style="margin: auto">
										<tr>
											<%--CIF_01014:Start --%>
											<td width="20%" class="textlabelbold" nowrap="nowrap"><a
												href="javascript:openContextHelp('inq/euc_sequestration.htm')">
													<img border='0' title="Help Question" alt="Help Question"
													src="images/help_question.gif" />
											</a> <bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.sequestration" />
											</td>
											<%--CIF_01014:End --%>
											<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
											<td width="80%" valign="bottom">
												<hr />
											</td>
											<%--CIF_01127 end--%>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
									</table>
								</c:if>

								<c:if test="${cininqform.details[11] != null}">
									<%--CIF_01638 - Mozilla Firefox Bug/ Cellspacing attribute added to table--%>
									<table class="tabledatainq" cellspacing="0" align="center"
										width="80%" style="margin: auto">
										<%--CIF_01638 end--%>
										<tr class="firefoxspecial2">
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.augmentationtype" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.effectivedate" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.sequester.mba" /></td>
											<td class="columnHeader"><bean:message
													key="access.cin.inq.claimdetails.teucaugmentation.sequester.mbabalance" /></td>
										</tr>

										<c:forEach var="teucAugmentation"
											items="${cininqform.details[11]}">
											<tr>
												<td class="textdata" align="center"><c:out
														value="${teucAugmentation.values.description}" /></td>
												<td class="textdata" align="center"><c:out
														value="${teucAugmentation.values.effectivedate}" /></td>
												<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
												<td class="tdnumber" nowrap="nowrap" style="color: navy;">$&nbsp;<fmt:formatNumber
														value="${teucAugmentation.values.sequestermba}"
														pattern=",##0.00" /></td>
												<%--CIF_01127 end --%>
												<td class="tdnumber" nowrap="nowrap"><span
													class="textdata">$&nbsp;<fmt:formatNumber
															value="${teucAugmentation.values.sequesterbalance}"
															pattern=",##0.00" /></span></td>
										</c:forEach>
										<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "tr" omitted, but OMITTAG NO was specified--%>
										</tr>
										<%--CIF_01127 end --%>
									</table>
								</c:if>

								<br />





								<!-- CIF_02150 || Defect_3094 Rejected Weeks - START -->
								<!-- CIF_02390 || Defect_3419 Fixed - table width has been modified and Vertical scroll bar code has been commented -->
								<table>
									<tr>
										<td width="20%" nowrap="nowrap"><bean:message
												key="access.cin.inquiry.reject.weeks" /></td>
										<td width="100%" valign="bottom">
											<hr />
										</td>
									</tr>
								</table>
								<c:choose>
									<c:when test="${cininqform.details[13] != null}">
										<!--<div
											style='overflow-y: scroll; overflow-x: hidden; width: 900px'>-->
										<table class="tabledatainq" cellspacing="0" align="center"
											width="80%" style="margin: auto">
											<tr class="firefoxspecial2">
												<td class="columnHeader"><bean:message
														key="access.cin.inquiry.reject.weeks.ending" /></td>
												<td class="columnHeader"><bean:message
														key="access.cin.inquiry.reject.weeks.filed.date" /></td>
												<td class="columnHeader"><bean:message
														key="access.cin.inquiry.reject.weeks.reject.reason" /></td>
											</tr>
											<tr>
												<c:forEach var="rejectedWeeksData"
													items="${cininqform.details[13]}">
													<td width="30%" nowrap="nowrap"><c:url
															var="cwedetailsLink" value="/cwedetailsLink.do">
															<c:param name="${access:encryptParams('encKey',encKey)}"
																value="${access:encryptParams(encKey,encKey)}" />
															<c:param
																name="${access:encryptParams('forwardName',encKey)}"
																value="${access:encryptParams('weeklycertificationdetailsinquiry',encKey)}" />
															<c:param name="${access:encryptParams('claimId',encKey)}"
																value="${access:encryptParams(claimInformation.claimid,encKey)}" />
															<c:param name="${access:encryptParams('cweId',encKey)}"
																value="${access:encryptParams(rejectedWeeksData.values.cweid,encKey)}" />
															<c:param name="${access:encryptParams('na',encKey)}"
																value="${access:encryptParams('a',encKey)}" />
															<c:param
																name="${access:encryptParams('previousScreen',encKey)}"
																value="${access:encryptParams('claimsummary',encKey)}" />
														</c:url> <html:link
															href="${fn:replace(cwedetailsLink,'&','&amp;')}">
															<fmt:formatDate value="${rejectedWeeksData.values.cwe}"
																pattern="MM/dd/yyyy" />
														</html:link></td>
													<%-- CIF_02876 - Formatted date--%>
													<td class="textdata" align="center"><fmt:formatDate
															value="${rejectedWeeksData.values.certificationdate}"
															pattern="MM/dd/yyyy" /></td>
													<td class="textdata" align="center"><c:out
															value="${rejectedWeeksData.values.rejectreason}" /></td>
											</tr>
											</c:forEach>
										</table>
										</div>
									</c:when>
									<c:otherwise>
										<table>
											<tr>
												<td width="30%" nowrap="nowrap"><bean:message
														key="access.cin.inquiry.reject.weeks.no.reject.records" /></td>
											</tr>
										</table>
									</c:otherwise>
								</c:choose>


								<!-- CIF_02150 || Defect_3094 Rejected Weeks - End -->

								<c:if test="${cininqform.showAtaaClaimFlag !='1'}">
									<c:choose>
										<c:when test="${cininqform.details[5] != null}">
											<br />
											<table class="tablefields" width="100%" style="margin: auto">
												<tr>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "td" omitted, but OMITTAG NO was specified--%>
													<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="30%" nowrap="nowrap">
														<%--<access:link forwardName="listofweeklycertifications" paramId="claimantId" paramName="claimantId">--%>
														<%--</access:link>--%> <bean:message
															key="access.cin.inq.weekly.certifications.fe" />
													</td>

													<td width="70%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 end--%>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>

											<%--CIF_01638 - Mozilla Firefox Bug/ Cellspacing attribute added to table--%>
											<table class="tabledatainq" cellspacing="0" align="center"
												width="80%" style="margin: auto">
												<%--CIF_01638 end--%>
												<tr class="firefoxspecial2">
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimantdetails.week.ending" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimantdetails.filed.date" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimantdetails.pendingreason" />
														&nbsp; <a
														href="javascript:openContextHelp('inq/pending_weekly_cert.htm')">
															<img border='0' title="Help Question" alt="Help Question"
															src="images/help_question.gif" />
													</a></td>
												</tr>
												<c:forEach var="weeklyCertifications"
													items="${cininqform.details[5]}">
													<tr>
														<td class="textdata" align="center">
															<%--CIF_00429 --%> <%--To enable the 'week ending date' hyperlink under 'pending weekly certifications' section, in claim summary screen --%>
															<%-- <access:secure
																	resourceGroup="externaluserandexternalentity"
																	reverse="true"> --%> 
														<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}"  >			
															<c:url var="cwedetailsLink"
																value="/cwedetailsLink.do">
																<c:param name="${access:encryptParams('encKey',encKey)}"
																	value="${access:encryptParams(encKey,encKey)}" />
																<c:param
																	name="${access:encryptParams('forwardName',encKey)}"
																	value="${access:encryptParams('weeklycertificationdetailsinquiry',encKey)}" />
																<c:param
																	name="${access:encryptParams('claimId',encKey)}"
																	value="${access:encryptParams(claimInformation.claimid,encKey)}" />
																<c:param name="${access:encryptParams('cweId',encKey)}"
																	value="${access:encryptParams(weeklyCertifications.cweId,encKey)}" />
																<c:param name="${access:encryptParams('na',encKey)}"
																	value="${access:encryptParams('a',encKey)}" />
																<%-- CIF_01755 : Changes for suspended claim--%>
																<c:param
																	name="${access:encryptParams('previousScreen',encKey)}"
																	value="${access:encryptParams('claimsummary',encKey)}" />
															</c:url> <html:link
																href="${fn:replace(cwedetailsLink,'&','&amp;')}">
																<fmt:formatDate
																	value="${weeklyCertifications.cwe.value}"
																	pattern="MM/dd/yyyy" />
															</html:link>
														</access:usertypesecure>
														<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}" reverse="true" >
																	<fmt:formatDate
																		value="${weeklyCertifications.cwe.value}"
																		pattern="MM/dd/yyyy" />
														</access:usertypesecure>
														
															 <%--CIF_00429 Start--%> <%--To enable the 'week ending date' hyperlink under 'pending weekly certifications' section, in claim summary screen --%>
															<%-- </access:secure> <access:secure
																	resourceGroup="externaluserandexternalentity"> --%> <%-- <fmt:formatDate
																		value="${weeklyCertifications.cwe.value}"
																		pattern="MM/dd/yyyy" /> --%> <%-- </access:secure> --%>
															<%--CIF_00429 End--%>
														</td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${weeklyCertifications.filedDate.value}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><c:out
																value="${weeklyCertifications.status}" /></td>

													</tr>
													<%--<tr>
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.cweAsString}" /></td>
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.filedDateAsString}" /></td>
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.processedDateAsString}" /></td>
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.amount}" /></td>
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.paymentMethod}" /></td>                               
									<td class="textdata" align="center"><c:out	value="${weeklyCertifications.status}" /></td>                               
									<c:choose>
									<c:when test="${ weeklyCertifications.processedWeekId eq ''}">
									<td class="textdata" align="center">view</td>      									  
									</c:when>                          
									<c:otherwise>
									<td class="textdata" align="center">
										<c:url var="cwedetailsLink" value="/cwedetailsLink.do">
<c:param name="${access:encryptParams('encKey',encKey)}" value="${access:encryptParams(encKey,encKey)}" />
										<c:param name="${access:encryptParams('forwardName',encKey)}" value="${access:encryptParams('weeklycertificationdetailsinquiry',encKey)}" />
										<c:param name="${access:encryptParams('cweId',encKey)}" value="${access:encryptParams(weeklyCertifications.cweId,encKey)}" />
										<c:param name="${access:encryptParams('processedWeekId',encKey)}" value="${access:encryptParams(weeklyCertifications.processedWeekId,encKey)}" />
										<c:param name="${access:encryptParams('claimId',encKey)}" value="${access:encryptParams(weeklyCertifications.claimId,encKey)}" />
									</c:url>
									<html:link href="${fn:replace(cwedetailsLink,'&','&amp;')}">
									view
									</html:link>
  									</td>    
									</c:otherwise>
									</c:choose>
									</tr>
									--%>
												</c:forEach>
											</table>

										</c:when>
										<c:otherwise>

											<table class="tablefields" width="100%">
												<tr>
													<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
													<td nowrap="nowrap">
														<%--<access:link forwardName="listofweeklycertifications" paramId="claimantId" paramName="claimantId">--%>
														<bean:message
															key="access.cin.inq.weekly.certifications.fe" /> <%--</access:link>--%>
													</td>
													<%--CIF_01127 end --%>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="70%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 end--%>
												</tr>
												<tr>
													<td colspan="3"><bean:message
															key="access.cin.inq.weekly.certifications.noton.file" /></td>
												</tr>
											</table>
										</c:otherwise>

									</c:choose>

									<br />
									<c:choose>
										<c:when test="${cininqform.details[10] != null}">
											<br />
											<table class="tablefields" width="100%" style="margin: auto">
												<tr>
													<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
													<td width="30%" nowrap="nowrap"><bean:message
															key="access.cin.inq.pwc.fe" /></td>
													<%--CIF_01127 end --%>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="70%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 end--%>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>

											<%--CIF_01638 - Mozilla Firefox Bug/ Cellspacing attribute added to table--%>
											<table class="tabledatainq" cellspacing="0" align="center"
												width="80%" style="margin: auto">
												<%--CIF_01638 end--%>
												<tr class="firefoxspecial2">
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimantdetails.week.ending" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimantdetails.filed.date" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.pwc.processed.date" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.pwc.status" /> &nbsp; <a
														href="javascript:openContextHelp('inq/processed_weekly_cert.htm')">
															<img border='0' title="Help Question" alt="Help Question"
															src="images/help_question.gif" />
													</a></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.pwc.amount.paid" /></td>
												</tr>

												<c:forEach var="processedWeeklyCertifications"
													items="${cininqform.details[10]}" begin="0" end="5">
													<tr>
														<td class="textdata" align="center">
															<%--CIF_00429 --%> <%--To enable the 'week ending date' hyperlink under 'processed weekly certifications' section, in claim summary screen --%>
															<%--  <access:secure 	resourceGroup="externaluserandexternalentity" reverse="true"> --%>
															<%-- CIF_INT_03579 || Defect_1701 display hyper link in processed weekly certification for DES users--%>
															<%--NTR || CIF_INT_04442|| Defect_750 || external agency users authorization error --%>
														<%-- #@cif_wy(impactNumber = " Defect_208" , requirementId = " ", designDocName = "03 Conduct Hearing", designDocSection = "", dcrNo = "", mddrNo = "", impactPoint = "")  --%>
														<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}">
														  <c:url var="cwedetailsLink"
																value="/cwedetailsLink.do">
																<c:param name="${access:encryptParams('encKey',encKey)}"
																	value="${access:encryptParams(encKey,encKey)}" />
																<c:param
																	name="${access:encryptParams('forwardName',encKey)}"
																	value="${access:encryptParams('weeklycertificationdetailsinquiry',encKey)}" />
																<c:param
																	name="${access:encryptParams('claimId',encKey)}"
																	value="${access:encryptParams(processedWeeklyCertifications.claimId,encKey)}" />
																<c:param name="${access:encryptParams('cweId',encKey)}"
																	value="${access:encryptParams(processedWeeklyCertifications.cweId,encKey)}" />
																<c:param
																	name="${access:encryptParams('previousScreen',encKey)}"
																	value="${access:encryptParams('claimsummary',encKey)}" />
															</c:url> <html:link
																href="${fn:replace(cwedetailsLink,'&','&amp;')}">
																<fmt:formatDate
																	value="${processedWeeklyCertifications.cwe.value}"
																	pattern="MM/dd/yyyy" />
															</html:link> <%--CIF_00429 Start--%> <%--To enable the 'week ending date' hyperlink under 'processed weekly certifications' section, in claim summary screen --%>
															 </access:usertypesecure> 
														<%--	 <access:secure	resourceGroup="externaluserandexternalentity"> 
															<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}" reverse="true" >
																	<fmt:formatDate
																		value="${processedWeeklyCertifications.cwe.value}"
																		pattern="MM/dd/yyyy" />
															</access:usertypesecure>		 --%>	 
														<%--	</access:secure>  --%>
																 <%--CIF_00429 End--%>
														</td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${processedWeeklyCertifications.filedDate.value}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${processedWeeklyCertifications.processedDate.value}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center">
															${access:fnenum(InquiryWeeklyTransactionTypeEnum,processedWeeklyCertifications.status)}</td>
														<td class="textdata" align="center"><fmt:formatNumber
																value="${processedWeeklyCertifications.amount}"
																pattern=",##0.00" /></td>
													</tr>
												</c:forEach>
											</table>

										</c:when>
										
										<c:otherwise>

											<table class="tablefields" width="100%">
												<tr>
													<%--CIF_01127 - Non-Functional Requirements/ Error: value of attribute "nowrap" cannot be "true"; must be one of "nowrap"--%>
													<td nowrap="nowrap"><bean:message
															key="access.cin.inq.pwc.fe" /></td>
													<%--CIF_01127 end --%>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="70%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 end--%>
												</tr>
												<tr>
													<td colspan="3"><bean:message
															key="access.cin.inq.pwc.empty" /></td>
												</tr>
											</table>
										</c:otherwise>

									</c:choose>
								</c:if>
								<%--CIF_01624 Start - Ataa application details --%>
								<c:if test="${cininqform.showAtaaClaimFlag =='1'}">
									<c:choose>
										<c:when test="${cininqform.details[14] != null}">
											<br />
											<table class="tablefields" width="100%" style="margin: auto">
												<tr>
													<td width="15%" nowrap="nowrap"><a href="#"><bean:message
																key="access.cin.inq.active.ataaclaim.details" /></a></td>

													<td width="85%" valign="bottom">
														<hr />
													</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>

											<table class="tabledatainq" cellspacing="0" align="center"
												width="90%" style="margin: auto">
												<tr class="firefoxspecial2">
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.reempdate" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.reempwage" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.sepwage" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.petitionno" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.processdate" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.eligbegan" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.eligend" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.mba" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.balance" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.active.ataaclaim.decision" /></td>
												</tr>

												<c:forEach var="ataaApplicationDetails"
													items="${cininqform.details[14]}" begin="0" end="5">
													<tr>
														<td class="textdata" align="center"><fmt:formatDate
																value="${ataaApplicationDetails.values.reempdate}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><c:out
																value="${ataaApplicationDetails.values.reempwage}" /></td>
														<td class="textdata" align="center"><c:out
																value="${ataaApplicationDetails.values.hwsep}" /></td>
														<td class="textdata" align="center"><c:out
																value="${ataaApplicationDetails.values.petitionnum}" /></td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${ataaApplicationDetails.values.processdate}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${ataaApplicationDetails.values.eligstartdate}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><fmt:formatDate
																value="${ataaApplicationDetails.values.eligenddate}"
																pattern="MM/dd/yyyy" /></td>
														<td class="textdata" align="center"><c:out
																value="${ataaApplicationDetails.values.mba}" /></td>
														<td class="textdata" align="center"><c:out
																value="${ataaApplicationDetails.values.balance}" /></td>
														<c:if
															test="${ataaApplicationDetails.values.decision =='DEND'}">
															<td class="textdata" align="center">Denied</td>
														</c:if>
														<c:if
															test="${ataaApplicationDetails.values.decision =='APPR'}">
															<td class="textdata" align="center">Approved</td>
														</c:if>
														<c:if
															test="${ataaApplicationDetails.values.decision == null}">
															<td class="textdata" align="center">N/A</td>
														</c:if>
													</tr>
												</c:forEach>
											</table>
											</c:when>
											 <c:otherwise>
										 
											<table class="tablefields" width="100%">

												<tr>
													<td colspan="2"><bean:message
															key="access.cin.inq.active.ataaclaim.empty" /></td>
												</tr>
											</table>
											
										</c:otherwise> 
									</c:choose>
											<br/><br/>
											<c:if test="${(not empty cininqform.details[15])}">
												<table class="tablefields" width="100%" style="margin: auto">
													<tr>
														<td width="15%" nowrap="nowrap"><c:url
																var="cwedetailsLink" value="/cwedetailsLink.do">
																<c:param name="${access:encryptParams('encKey',encKey)}"
																	value="${access:encryptParams(encKey,encKey)}" />
																<c:param
																	name="${access:encryptParams('forwardName',encKey)}"
																	value="${access:encryptParams('ataapaymentsummary',encKey)}" />

															</c:url> <html:link
																href="${fn:replace(cwedetailsLink,'&','&amp;')}">
																<bean:message
																	key="access.cin.inq.active.ataaclaim.payments" />
															</html:link></td>

														<td width="85%" valign="bottom">
															<hr />
														</td>
													</tr>
													<tr>
														<td>&nbsp;</td>
													</tr>
												</table>
											</c:if>
											<!-- CIF_INT_01298||Defect_8141 START-->
											<c:if test="${empty cininqform.details[15] || cininqform.details[15] eq null}">
												<table class="tablefields" width="100%">

												<tr>
													<td><bean:message
															key="access.cin.inq.ataapaymentsummary.notfound" /></td>
												</tr>
											</table>
												<br />
											</c:if>
											<c:if test="${(not empty cininqform.details[15])}">
												<br />
												<center>
													<table class="tabledata" cellspacing="0">
														<thead>
															<tr>
																<th scope="col" class="textlabel" align="center"><bean:message
																		key="access.cin.inq.ataapaymentsummary.cwe" /></th>

																<th scope="col" class="textlabel" align="center"><bean:message
																		key="access.cin.inq.ataapaymentsummary.paymentmode" /></th>

																<th scope="col" class="textlabel"><bean:message
																		key="access.cin.inq.ataapaymentsummary.paymentdate" /></th>

																<th scope="col" class="textlabel"><bean:message
																		key="access.cin.inq.ataapaymentsummary.csiamount" /></th>

																<th scope="col" class="textlabel"><bean:message
																		key="access.cin.inq.ataapaymentsummary.fitAmount" /></th>

																<th scope="col" class="textlabel"><bean:message
																		key="access.cin.inq.ataapaymentsummary.paymentamount" /></th>

																<th scope="col" class="textlabel"><bean:message
																		key="access.cin.inq.ataapaymentsummary.paymentstatus" /></th>

																<th scope="col" align="left" valign="top"><bean:message
																		key="access.cin.inq.ataapaymentsummary.checknumber" /></th>

																<th scope="col" align="left" valign="top"><bean:message
																		key="access.cin.inq.ataapaymentsummary.actualhrsworked" /></th>

																<th scope="col" align="left" valign="top"><bean:message
																		key="access.cin.inq.ataapaymentsummary.hourlyrate" /></th>
																	
																<th scope="col" align="left" valign="top"><bean:message
																		key="access.cin.inq.ataapaymentsummary.payableamount" /></th>
																		
																<th scope="col" align="left" valign="top"><bean:message
																		key="access.cin.inq.ataapaymentsummary.stateoffset" /></th>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="paymentBean"
																items="${cininqform.details[15]}">
																<tr>
																	<td align="left" class="textdata" width="80px"><html:hidden
																			name="paymentBean" property="cwe.dateAsString"
																			indexed="true" write="true" /></td>
																	<td align="left" class="textdata">
																		${access:fnenum(WeeklyPaymentModeEnum, paymentBean.paymentMode)}
																	</td>
																	<td align="left" class="textdata" width="80px"><c:if
																			test="${paymentBean.paymentDate!=null}">
																			<html:hidden name="paymentBean"
																				property="paymentDate.dateAsString" indexed="true"
																				write="true" />
																		</c:if></td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="csiAmount" />
																	</td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="fitAmount" />
																	</td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="netAmount" />
																	</td>
																	<td align="left" class="textdata" valign="top">
																		${access:fnenum(WeeklyPaymentStatusEnum, paymentBean.paymentStatus)}</td>
																	<td align="left" class="textdata" valign="top"><c:choose>
																			<c:when test="${paymentBean.paymentMode=='CHEK' }">
																				<c:out value="${paymentBean.checkNumber }" />
																			</c:when>
																			<c:when test="${paymentBean.paymentMode=='DCRD' }">
																				<c:out value="Debit Card" />
																			</c:when>
																			<c:when test="${paymentBean.paymentMode=='DDEP' }">
																				<c:out value="${paymentBean.traceNumber }" />
																			</c:when>
																			<c:otherwise>
																				<c:out value="N/A" />
																			</c:otherwise>
																		</c:choose></td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="actualHoursWorked" />
																	</td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="newHourlyRate" />
																	</td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="payableAmount" />
																	</td>
																	<td align="left" class="textdata" valign="top">&nbsp;<access:fmtnumber
																			name="paymentBean" property="stateOffset" />
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</center>
											</c:if>
										
										

								</c:if>
								<%--CIF_01624 End --%>
								<br />
								<%--CIF_00429 --%>
								<%--Thisline of code was commented and the line after that was added to provide selective access rights for claimant and CSR, for 'history log' hyperlink--%>
								<%-- <access:secure resourceGroup="externaluserandexternalentity"
											reverse="true"> --%>
								<%--CIF_00429 --%>
								<%--Thisline of code was commented and the line after that was added to provide selective access rights for claimant and CSR, for 'history log' hyperlink--%>
								<%-- <access:usertypesecure userType="${UserTypeEnum.MDES.name}"	reverse="true"> --%>
								<%-- //NTR || CIF_INT_04423|| UIM-5991 || inquiry access to DWD WOTC and TRA users --%>
								<access:secure resourceGroup="claimsummaryhistoryLogaccess" >
									<c:choose>
										<c:when test="${cininqform.details[6] != null}">
											<br />
											<table class="tablefields" width="100%" style="margin: auto">
												<tr>
													<td width="11%"><access:link forwardName="historylog"
															paramId="claimantId" paramName="claimantId">
															<bean:message
																key="access.cin.inq.claimsummary.history.log" />
														</access:link></td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="89%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: document type does not allow element "tr" here--%>
												</tr>
												<%--CIF_01127 end --%>
												<%--CIF_01127 end--%>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>

											<%--CIF_01638 - Mozilla Firefox Bug/ Cellspacing attribute added to table--%>
											<table class="tabledatainq" cellspacing="0" align="center"
												width="80%" style="margin: auto">
												<%--CIF_01638 end--%>
												<tr class="firefoxspecial2">
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimsummary.history.log.comment.date" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimsummary.history.log.comment" /></td>
													<td class="columnHeader"><bean:message
															key="access.cin.inq.claimsummary.history.log.addedby" /></td>
												</tr>

												<c:forEach var="historyLog" items="${cininqform.details[6]}"
													begin="0" end="9">
													<tr>
														<td class="textdata" align="center"><c:out
																value="${historyLog.values.commentdate}" /></td>
														<td class="textdata"><c:out
																value="${historyLog.values.comment}" /></td>
														<c:if test="${empty historyLog.values.addedby}">
															<td class="textdata"><c:out value=" " /></td>
														</c:if>
														<c:if test="${!empty historyLog.values.addedby}">
															<td class="textdata"><c:out
																	value="${historyLog.values.addedby}" /></td>
														</c:if>
													</tr>
												</c:forEach>
											</table>
										</c:when>
										<c:otherwise>

											<table class="tablefields" width="100%">
												<tr>
													<td width="11%"><bean:message
															key="access.cin.inq.claimsummary.history.log" /></td>
													<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "hr" which is not finished--%>
													<td width="88%" valign="bottom">
														<hr />
													</td>
													<%--CIF_01127 end--%>
												</tr>
												<tr>
													<td colspan="3"><bean:message
															key="access.cin.inq.claimsummary.history.log.not.on.file" /></td>
												</tr>
											</table>
										</c:otherwise>
									</c:choose>
									<%--CIF_00429 --%>
									<%--Thisline of code was commented  to provide selective access rights for claimant and CSR, for 'history log' hyperlink--%>
									<%-- 	</access:secure> --%>
									<%--CIF_00429 --%>
									<%--Thisline of code  was added to provide selective access rights for claimant and CSR, for 'history log' hyperlink--%>
									<%--  </access:usertypesecure> --%>
								</access:secure>	
							</c:when>
						</c:choose></td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<br />
	<br />
		<!-- CIF_INT_03494 Added external agency access -->
<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}" >
	<c:if test="${claimInformation.claimid != null}">
		<access:secure resourceGroup="externaluserandexternalentity"
			reverse="true">

			<table class="tableinq" align="center" style="margin: auto">
				<tr>
					<td align="center"><c:url var="monetaryLink"
							value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('monetary',encKey)}" />
							<c:param name="${access:encryptParams('claimantId',encKey)}"
								value="${access:encryptParams(claimant.claimantid,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(claimInformation.claimid,encKey)}" />
						</c:url> <html:link href="${fn:replace(monetaryLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary" />
						</html:link> | <access:link forwardName="nonmonetary" paramId="claimantId"
							paramName="claimantId">
							<bean:message key="access.cin.inq.nonmonetary" />
						</access:link> | <access:link forwardName="claimlist" paramId="claimantId"
							paramName="claimantId">
							<bean:message key="access.cin.inq.claims" />
						</access:link> <%-- | <access:link forwardName="eligibilityreview"
										paramId="claimantId" paramName="claimantId">
										<bean:message key="access.cin.inq.eligibility.review" /></access:link>  --%>
						| <access:link forwardName="listofappealsbyssn"
							paramId="claimantssn" paramName="claimantssn">
							<bean:message key="access.app.inq.listofappealsbyssn" />
						</access:link> | <access:link forwardName="processedweeklycertificationslist">
							<bean:message
								key="access.cin.inq.processedweeklycertificationslist.review" />
						</access:link></td>
				</tr>
				<tr>
					<td align="center"><access:link forwardName="facweekslist">
							<bean:message key="access.cin.inq.facweekslistt.linkto" />
						</access:link> | <access:link forwardName="overpaymentinquirylist">
							<bean:message
								key="access.cin.inq.processedweeklycertificationslist.overpayments" />
						</access:link> | <access:link forwardName="trainingwaivertimeinquiry">
							<bean:message key="access.inquiry.information.linkto" />
						</access:link> | <c:url var="claimantsummaryverification" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('claimantsummaryverification',encKey)}" />
							<c:param name="${access:encryptParams('previousScreen',encKey)}"
								value="${access:encryptParams('claimsummary',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(claimantsummaryverification,'&','&amp;')}">	
											Unemployment Verification								 
			</html:link>| <c:url var="claimworksearchcontact" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('claimworksearchcontact',encKey)}" />
							<c:param name="${access:encryptParams('previousScreen',encKey)}"
								value="${access:encryptParams('claimsummary',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(claimworksearchcontact,'&','&amp;')}">
							<bean:message
								key="access.inquiry.claimsummary.worksearchcontactinquiry" />
						</html:link></td>
				</tr>

			</table>
			<%--CIF_00429 Start--%>
			<%--To align hyperlinks correctly--%>
			<br />
			<br />
			<%--CIF_00429 End--%>
			<table class="tableinq" align="center" style="margin: auto">
				<tr>
					<td><c:url var="viewAllBaseperiodLink"
							value="/ib4inquirylinkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('viewallbaseperiodwages',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('monetaryinqscreen',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(viewAllBaseperiodLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary.view.baseperiod.wages" />
						</html:link>| <c:url var="viewAllCorrespondenceLink"
							value="/ib4inquirylinkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('viewallcorrepsondence',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('claimsummaryscreen',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(viewAllCorrespondenceLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary.view.correspondence" />
						</html:link></td>
				</tr>
			</table>
		</access:secure>
		<%-- Code changed by 226762 for CQ R4UAT00010326 --%>
		<access:secure resourceGroup="externaluser">
			<table class="tableinq" align="center" style="margin: auto">
				<tr>
					<%--CIF_00429 Start--%>
					<%--To display and reposition hyperlinks correctly--%>
					<%--Removed the 'Reemployment Services' hyperlink as this is not significant for missouri --%>
					<td align="center"><c:url var="monetaryLink"
							value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('monetary',encKey)}" />
							<c:param name="${access:encryptParams('claimantId',encKey)}"
								value="${access:encryptParams(claimant.claimantid,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(claimInformation.claimid,encKey)}" />
						</c:url> <html:link href="${fn:replace(monetaryLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary" />
						</html:link> | <access:link forwardName="nonmonetary" paramId="claimantId"
							paramName="claimantId">
							<bean:message key="access.cin.inq.nonmonetary" />
						</access:link> | <access:link forwardName="claimlist" paramId="claimantId"
							paramName="claimantId">
							<bean:message key="access.cin.inq.claims" />
						</access:link> | <%-- <access:link forwardName="eligibilityreview"
										paramId="claimantId" paramName="claimantId">
										<bean:message key="access.cin.inq.eligibility.review" />
									</access:link>  |--%> <access:link
							forwardName="listofappealsbyssn" paramId="claimantssn"
							paramName="claimantssn">
							<bean:message key="access.app.inq.listofappealsbyssn" />
						</access:link> |<c:url var="viewAllProcessWCLink" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('processedweeklycertificationslist',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('monetaryinqscreen',encKey)}" />
						</c:url> <html:link href="${fn:replace(viewAllProcessWCLink,'&','&amp;')}">
							<bean:message
								key="access.cin.inq.processedweeklycertificationslist.review" />
						</html:link> <%-- <access:link forwardName="processedweeklycertificationslist">
										<bean:message
											key="access.cin.inq.processedweeklycertificationslist.review" />
									</access:link> | --%> <br /> <access:link
							forwardName="facweekslist">
							<bean:message key="access.cin.inq.facweekslistt.linkto" />
						</access:link> |<access:link forwardName="overpaymentinquirylist">
							<bean:message
								key="access.cin.inq.processedweeklycertificationslist.overpayments" />
						</access:link>| <access:link forwardName="trainingwaivertimeinquiry">
							<bean:message key="access.inquiry.information.linkto" />
						</access:link>| <c:url var="claimantsummaryverification" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('claimantsummaryverification',encKey)}" />
							<c:param name="${access:encryptParams('previousScreen',encKey)}"
								value="${access:encryptParams('claimsummary',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(claimantsummaryverification,'&','&amp;')}">
							<bean:message
								key="access.inquiry.claimsummary.claimantsummaryverification" />
						</html:link>| <c:url var="claimworksearchcontact" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('claimworksearchcontact',encKey)}" />
							<c:param name="${access:encryptParams('previousScreen',encKey)}"
								value="${access:encryptParams('claimsummary',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(claimworksearchcontact,'&','&amp;')}">
							<bean:message
								key="access.inquiry.claimsummary.worksearchcontactinquiry" />
						</html:link> <br /> <br /> <c:url var="viewAllBaseperiodLink"
							value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('viewallbaseperiodwages',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('monetaryinqscreen',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(viewAllBaseperiodLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary.view.baseperiod.wages" />
						</html:link>|<c:url var="viewAllCorrespondenceLink"
							value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('viewallcorrepsondence',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('claimsummaryscreen',encKey)}" />
						</c:url> <html:link
							href="${fn:replace(viewAllCorrespondenceLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary.view.correspondence" />

						</html:link></td>
					<%--CIF_INT_01717 Added  viewwages and searchbenefitdocument in linkaction--%>	

					<%--CIF_00429 End--%>
					<%--CIF_00429 Start--%>
					<%--Commented the redundant code for 'view wages' hyperlink--%>
					<%-- CIF_00374 Start --%>
					<%-- Changes for FE_040, to add 'view wages' hyperlink in 'Claim Summary' screen --%>

					<%-- 	<access:secure resourceGroup="externaluserformedicaid">
									<td><c:url var="viewAllBaseperiodLink"
											value="/ib4inquirylinkaction.do">
<c:param name="${access:encryptParams('encKey',encKey)}" value="${access:encryptParams(encKey,encKey)}" />
											<c:param name="${access:encryptParams('forwardName',encKey)}" value="${access:encryptParams('viewallbaseperiodwages',encKey)}" />
											<c:param name="${access:encryptParams('ssn',encKey)}" value="${access:encryptParams(claimant.ssn,encKey)}" />
											<c:param name="${access:encryptParams('claimId',encKey)}" value="${access:encryptParams(cininqform.claimId,encKey)}" />
											<c:param name="${access:encryptParams('frompage',encKey)}" value="${access:encryptParams('monetaryinqscreen',encKey)}" />
										</c:url> <html:link href="${fn:replace(viewAllBaseperiodLink,'&','&amp;')}">
											<bean:message
												key="access.cin.inq.monetary.view.baseperiod.wages" />
										</html:link></td>

								</access:secure> --%>
					<%-- CIF_00374 End --%>
					<%--CIF_00429 End--%>
					<%--CIF_00429 Start--%>
					<%--Commented out redundant code for unemployment verification--%>
					<%-- CIF_00384 Starts --%>
					<%--CIF_01127 - Non-Functional Requirements/ Error: document type does not allow element "input" here--%>
					<%-- <c:url var="claimantsummaryverification" value="/linkaction.do">
<c:param name="${access:encryptParams('encKey',encKey)}" value="${access:encryptParams(encKey,encKey)}" />
									<c:param name="${access:encryptParams('ssn',encKey)}" value="${access:encryptParams(claimant.ssn,encKey)}" />
									<c:param name="${access:encryptParams('forwardName',encKey)}" value="${access:encryptParams('claimantsummaryverification',encKey)}" />
									<c:param name="${access:encryptParams('previousScreen',encKey)}" value="${access:encryptParams('claimsummary',encKey)}" />
								</c:url>
								<td><html:link href="${fn:replace(claimantsummaryverification,'&','&amp;')}">
											<bean:message
												key="access.inquiry.claimsummary.claimantsummaryverification" />		 
								</html:link></td> --%>
					<%--CIF_01127 end --%>
					<%-- CIF_00384 Ends --%>
					<%--CIF_00429 End--%>
				</tr>
			</table>
		</access:secure>
		<access:secure resourceGroup="externalentityuser">
			<table class="tableinq" align="center" style="margin: auto">
				<tr>
					<access:secure resourceGroup="externaluserviewweeklycert">
						<%--CIF_00429 Start--%>
						<%--Commented the redundant code for 'processed weekly certifications' hyperlink--%>
						<%-- <td><c:url var="viewAllProcessWCLink"
											value="/linkaction.do">
<c:param name="${access:encryptParams('encKey',encKey)}" value="${access:encryptParams(encKey,encKey)}" />
											<c:param name="${access:encryptParams('forwardName',encKey)}"
												value="${access:encryptParams('processedweeklycertificationslist',encKey)}" />
											<c:param name="${access:encryptParams('ssn',encKey)}" value="${access:encryptParams(claimant.ssn,encKey)}" />
											<c:param name="${access:encryptParams('claimId',encKey)}" value="${access:encryptParams(cininqform.claimId,encKey)}" />
											<c:param name="${access:encryptParams('frompage',encKey)}" value="${access:encryptParams('monetaryinqscreen',encKey)}" />
										</c:url> <html:link href="${fn:replace(viewAllProcessWCLink,'&','&amp;')}">
											<bean:message
												key="access.cin.inq.processedweeklycertificationslist.review" />
										</html:link></td> --%>
						<%--CIF_00429 End--%>
					</access:secure>
					<access:secure resourceGroup="externaluserviewwage">
						<td><access:secure resourceGroup="externaluserviewweeklycert"> | 
									</access:secure> <c:url var="viewAllBaseperiodLink"
								value="/ib4inquirylinkaction.do">
								<c:param name="${access:encryptParams('encKey',encKey)}"
									value="${access:encryptParams(encKey,encKey)}" />
								<c:param name="${access:encryptParams('forwardName',encKey)}"
									value="${access:encryptParams('viewallbaseperiodwages',encKey)}" />
								<c:param name="${access:encryptParams('ssn',encKey)}"
									value="${access:encryptParams(claimant.ssn,encKey)}" />
								<c:param name="${access:encryptParams('claimId',encKey)}"
									value="${access:encryptParams(cininqform.claimId,encKey)}" />
								<c:param name="${access:encryptParams('frompage',encKey)}"
									value="${access:encryptParams('monetaryinqscreen',encKey)}" />
							</c:url> <html:link
								href="${fn:replace(viewAllBaseperiodLink,'&','&amp;')}">
								<bean:message
									key="access.cin.inq.monetary.view.baseperiod.wages" />
							</html:link></td>

					</access:secure>
				</tr>
			</table>
		</access:secure>
	</c:if>
</access:usertypesecure>	
	<c:if test="${claimInformation.claimid == null}">
		<table class="tableinq" align="center" style="margin: auto">
			<tr>
				<td align="center"><access:link
						forwardName="overpaymentinquirylist">
						<bean:message
							key="access.cin.inq.processedweeklycertificationslist.overpayments" />
					</access:link> &nbsp;&nbsp; <c:url var="viewAllCorrespondenceLink"
						value="/ib4inquirylinkaction.do">
						<c:param name="${access:encryptParams('encKey',encKey)}"
							value="${access:encryptParams(encKey,encKey)}" />
						<c:param name="${access:encryptParams('forwardName',encKey)}"
							value="${access:encryptParams('viewallcorrepsondence',encKey)}" />
						<c:param name="${access:encryptParams('ssn',encKey)}"
							value="${access:encryptParams(claimant.ssn,encKey)}" />
						<c:param name="${access:encryptParams('frompage',encKey)}"
							value="${access:encryptParams('claimsummaryscreen',encKey)}" />
						<c:param name="OWASP_CSRFTOKEN" value="${OWASP_CSRFTOKEN}" />
					</c:url> <html:link
						href="${fn:replace(viewAllCorrespondenceLink,'&','&amp;')}">
						<bean:message key="access.cin.inq.monetary.view.correspondence" />
					</html:link></td>
			</tr>
		</table>
	</c:if>
	<!-- CIF_INT_03494 claim, weeklycertification,TRA link to external agency users start	 -->
	<access:usertypesecure userType = "${UserTypeEnum.OTHR.name}" reverse = "true" >
		<c:if test="${claimInformation.claimid != null}">
			<table class="tableinq" align="center" style="margin: auto">
			<tr>
					
					<td align="center">
						<c:url var="monetaryLink" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('monetary',encKey)}" />
							<c:param name="${access:encryptParams('claimantId',encKey)}"
								value="${access:encryptParams(claimant.claimantid,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(claimInformation.claimid,encKey)}" />
						</c:url>  
						<%--//NTR || CIF_INT_04210  || UIM-6006  || claim details, monetary link access to external agency users--%>
						<html:link href="${fn:replace(monetaryLink,'&','&amp;')}">
							<bean:message key="access.cin.inq.monetary" />
						</html:link> |
						
						<access:secure resourceGroup="claimsummaryhistoryLogaccess">
							<access:link forwardName="nonmonetary" paramId="claimantId"
								paramName="claimantId">
								<bean:message key="access.cin.inq.nonmonetary" />
							</access:link> |
							<access:link forwardName="listofappealsbyssn"
								paramId="claimantssn" paramName="claimantssn">
								<bean:message key="access.app.inq.listofappealsbyssn" />
							</access:link> |
						</access:secure>
						
						
						<access:link forwardName="claimlist" paramId="claimantId"	paramName="claimantId">
							<bean:message key="access.cin.inq.claims" />
						</access:link>|
					    <c:url var="viewAllProcessWCLink" value="/linkaction.do">
							<c:param name="${access:encryptParams('encKey',encKey)}"
								value="${access:encryptParams(encKey,encKey)}" />
							<c:param name="${access:encryptParams('forwardName',encKey)}"
								value="${access:encryptParams('processedweeklycertificationslist',encKey)}" />
							<c:param name="${access:encryptParams('ssn',encKey)}"
								value="${access:encryptParams(claimant.ssn,encKey)}" />
							<c:param name="${access:encryptParams('claimId',encKey)}"
								value="${access:encryptParams(cininqform.claimId,encKey)}" />
							<c:param name="${access:encryptParams('frompage',encKey)}"
								value="${access:encryptParams('monetaryinqscreen',encKey)}" />
						</c:url>
						<html:link href="${fn:replace(viewAllProcessWCLink,'&','&amp;')}">
									<bean:message 	key="access.cin.inq.processedweeklycertificationslist.review" />
						</html:link>|  
						<access:link forwardName="trainingwaivertimeinquiry">
							<bean:message key="access.inquiry.information.linkto" />
						</access:link>
					</td>
				</tr>

				<tr>
					<td align="center">
						<access:secure resourceGroup="claimsummaryhistoryLogaccess" >
							 <access:link forwardName="overpaymentinquirylist">
								<bean:message
									key="access.cin.inq.processedweeklycertificationslist.overpayments" />
							</access:link>
						</access:secure>
					</td
				</tr>

			</table>
			<access:secure resourceGroup="claimsummaryhistoryLogaccess">
				<table class="tableinq" align="center" style="margin: auto">
					<tr>
						<td><c:url var="viewAllBaseperiodLink"
								value="/ib4inquirylinkaction.do">
								<c:param name="${access:encryptParams('encKey',encKey)}"
									value="${access:encryptParams(encKey,encKey)}" />
								<c:param name="${access:encryptParams('forwardName',encKey)}"
									value="${access:encryptParams('viewallbaseperiodwages',encKey)}" />
								<c:param name="${access:encryptParams('ssn',encKey)}"
									value="${access:encryptParams(claimant.ssn,encKey)}" />
								<c:param name="${access:encryptParams('claimId',encKey)}"
									value="${access:encryptParams(cininqform.claimId,encKey)}" />
								<c:param name="${access:encryptParams('frompage',encKey)}"
									value="${access:encryptParams('monetaryinqscreen',encKey)}" />
							</c:url> <html:link
								href="${fn:replace(viewAllBaseperiodLink,'&','&amp;')}">
								<bean:message key="access.cin.inq.monetary.view.baseperiod.wages" />
							</html:link>| <c:url var="viewAllCorrespondenceLink"
								value="/ib4inquirylinkaction.do">
								<c:param name="${access:encryptParams('encKey',encKey)}"
									value="${access:encryptParams(encKey,encKey)}" />
								<c:param name="${access:encryptParams('forwardName',encKey)}"
									value="${access:encryptParams('viewallcorrepsondence',encKey)}" />
								<c:param name="${access:encryptParams('ssn',encKey)}"
									value="${access:encryptParams(claimant.ssn,encKey)}" />
								<c:param name="${access:encryptParams('frompage',encKey)}"
									value="${access:encryptParams('claimsummaryscreen',encKey)}" />
							</c:url> <html:link
								href="${fn:replace(viewAllCorrespondenceLink,'&','&amp;')}">
								<bean:message key="access.cin.inq.monetary.view.correspondence" />
							</html:link></td>
					</tr>
				</table>
			</access:secure>
		</c:if>
	</access:usertypesecure>
	<!-- CIF_INT_03494 claim, weeklycertification,TRA link to external agency users end	 -->

	<br />
	<br />
	<table class="buttontable">
		<tr>
			<!--CIF_02563	Defect_3577 || pass variable openFrom for help screen  -->
			<td class="buttontdleft"><access:localebuttontag
					property="method" styleClass="formbutton"
					helpFileName="./html/Help/inq/claimant_summary.htm?openFrom=2"
					onmouseover="this.className='formbutton formbuttonhover'"
					onmouseout="this.className='formbutton'">
					<bean:message key="access.help" />
				</access:localebuttontag></td>
			<td class="buttontdright"><c:if
					test="${cininqform.showAtaaClaimFlag !='1'}">
					<html:cancel styleClass="formbutton"
						onmouseover="this.className='formbutton formbuttonhover'"
						onmouseout="this.className='formbutton'">
						<bean:message key="access.back" />
					</html:cancel>
				</c:if> <c:if test="${cininqform.showAtaaClaimFlag =='1'}">
					<html:cancel styleClass="formbutton"
						onmouseover="this.className='formbutton formbuttonhover'"
						onmouseout="this.className='formbutton'">
						<bean:message key="access.back" />
					</html:cancel>
					<html:submit property="method"  styleId="next" styleClass="formbutton"
						onmouseover="this.className='formbutton formbuttonhover'"
						onmouseout="this.className='formbutton'">
						<bean:message key="access.continue" />
					</html:submit>
				</c:if></td>
		</tr>
	</table>
</html:form>