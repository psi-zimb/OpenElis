<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />

<%!

String allowEdits = "true";

%>

<%
if (request.getAttribute(IActionConstants.ALLOW_EDITS_KEY) != null) {
 allowEdits = (String)request.getAttribute(IActionConstants.ALLOW_EDITS_KEY);
}

%>

<script language="JavaScript1.2">
function validateForm(form) {
 return validateTypeOfTestStatusForm(form);
}
</script>

<table>
	    <tr>
						<td class="label">
							<bean:message key="typeofresultstatus.id"/>:
						</td>
						<td>
							<app:text name="<%=formName%>" property="id" allowEdits="false"/>
						</td>
	    </tr>
		<tr>
						<td class="label">
							<bean:message key="typeofresultstatus.name"/>:<span class="requiredlabel">*</span>
						</td>
						<td>
							<html:text name="<%=formName%>" property="name"/>
						</td>
		</tr>
		<tr>
						<td class="label">
							<bean:message key="typeofresultstatus.description"/>:<span class="requiredlabel">*</span>
						</td>
						<td>
							<html:text name="<%=formName%>" property="description"/>
						</td>
        </tr>
         <tr>
						<td class="label">
							<bean:message key="typeofresultstatus.isActive"/>:<span class="requiredlabel">*</span>
						</td>
						<td>
							<html:text name="<%=formName%>" property="isActive"/>
						</td>
		 </tr>
		 <tr>
         						<td class="label">
         							<bean:message key="typeofresultstatus.isResultRequired"/>:<span class="requiredlabel">*</span>
         						</td>
         						<td>
         							<html:text name="<%=formName%>" property="isResultRequired"/>
         						</td>
         </tr>
         <tr>
                  						<td class="label">
                  							<bean:message key="typeofresultstatus.isApprovalRequired"/>:<span class="requiredlabel">*</span>
                  						</td>
                  						<td>
                  							<html:text name="<%=formName%>" property="isApprovalRequired"/>
                  						</td>
         </tr>
         <tr>
						<td class="label">
							<bean:message key="typeofresultstatus.statusType"/>:<span class="requiredlabel">*</span>
						</td>
						<td>
							<html:text name="<%=formName%>" property="statusType" size="1" onblur="this.value=this.value.toUpperCase()"/>
						</td>
          </tr>

 		<tr>
		<td>&nbsp;</td>
		</tr>
</table>

<html:javascript formName="typeOfTestStatusForm"/>

