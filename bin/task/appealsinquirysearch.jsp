<%-- appealsinquirysearch.jsp													--%>
<%-- This jsp is a search to get desired appeal based on ssn, ean, docket number   	--%>
<%-- jsp            : appealsinquirysearch.jsp								--%>
<%-- Action         : AppealsInquirySearchAction.java							--%>
<%-- Form           : AppealsInquirySearchForm.java								--%>
<%-- tiles-defs.xml : .appealsinquirysearch(accessms/WEB-INF/jsp)			--%>

<%@ page language="java"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/taglib/accesstag" prefix="access"%>

<%-- form area & content area --%>
<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "input" omitted, but OMITTAG NO was specified--%><html:xhtml/><%--CIF_01127 end --%><html:form action="appealsinquirysearch" method="post">
<table style="display:none"><tr><td><input type="hidden" name="encKey" value="<c:out value="${encKeyEncrypted}"/>" id="encKeyId"/></td></tr></table>

	<html:errors />

	<br/>
	<br/>
	<div class="pageheader">
		<bean:message key="access.app.appealsinquirysearch.page.title" />
	</div>
	<bean:message key="access.requiredfield" />
	<br/>
	<br/>
	<table  class="tablefields">
		<tr>
			<td class="tdnumber"><bean:message
					key="access.app.appealsinquirysearch.istquestion.number" />.</td>
			<td class="tdmandatory">*</td>
			<td class="textlabel" colspan="4"><bean:message
					key="access.app.appealsinquirysearch.istquestion" /></td>
		</tr>
		<tr>
			<td colspan="5"></td>
		</tr>
		<tr>
			<td colspan="5"></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td class="tdnumber"><bean:message
					key="access.app.appealsinquirysearch.docket.label.a" />.</td>
					<access:error property="docket">
			<td class="textlabel"><label for="docketId"><bean:message
					key="access.app.appealsinquirysearch.docket.number" /></label></td>
					</access:error>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<%--CIF_03279 || Defect_4901 --%>
			<td><html:text styleId="docketId"  property="docket" readonly="false" maxlength="18"/></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td class="textlabelbold" align="center"><bean:message
					key="access.app.search.or" /></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td class="tdnumber"><bean:message
					key="access.app.appealsinquirysearch.ssn.label.b" />.</td>
			<td class="textlabel"><label for="ssnBeanId.ssn1"><bean:message
					key="access.app.appealsinquirysearch.ssn" /></label></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><access:ssn styleId="ssnBeanId.ssn1"  property="ssnBean" readonly="false" /></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td class="textlabelbold" align="center"><bean:message
					key="access.app.search.or" /></td>
		</tr>
		
		<c:choose>
		
		<c:when test="${appealsinquirysearchform.isClaimant == 'true' }">
		<tr>
			<td></td>
			<td></td>
			<td class="tdnumber"><bean:message
					key="access.app.appealsinquirysearch.ean.label.c" />.</td>
			<td class="textlabel"><label for="eanBeanId.ean1"><bean:message
					key="access.app.appealsinquirysearch.ean" /></label></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><access:ean styleId="eanBeanId.ean1"  property="eanBean" readonly="true" ean4="true" disabled="true"/></td>
		</tr>
		</c:when>
		<c:otherwise>
		<tr>
			<td></td>
			<td></td>
			<td class="tdnumber"><bean:message
					key="access.app.appealsinquirysearch.ean.label.c" />.</td>
			<td class="textlabel"><label for="eanBeanId2.ean1"><bean:message
					key="access.app.appealsinquirysearch.ean" /></label></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><access:ean styleId="eanBeanId2.ean1"  property="eanBean" readonly="false" ean4="true" /></td>
		</tr>
		</c:otherwise>
		</c:choose>
		
		
	</table>
	<br/>
	<br/>
	<br/>
	<table  class="buttontable">
		<%--CIF_01127 - Non-Functional Requirements/ Error: document type does not allow element "td" here; assuming missing "tr" start-tag--%><tr><%--CIF_01127 end --%><td class="buttontdleft"><access:localebuttontag
	property="method" styleClass="formbutton"
	helpFileName="./html/Help/app/Appeals_Inquiry.htm"
	onmouseover="this.className='formbutton formbuttonhover'"
	onmouseout="this.className='formbutton'">
	<bean:message key="access.help" />
</access:localebuttontag></td>

		<td class="buttontdright"><html:submit property="method"
				styleClass="formbutton"
				onmouseover="this.className='formbutton formbuttonhover'"
				onmouseout="this.className='formbutton'">
				<bean:message key="access.continue" />
			</html:submit></td>
	<%--CIF_01127 - Non-Functional Requirements/ Error: end tag for "tr" omitted, but OMITTAG NO was specified--%></tr><%--CIF_01127 end --%></table>
</html:form>

