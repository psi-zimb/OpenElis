<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	java.util.Hashtable,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />


<table width="100%" border=2">
	<tr>
	   <th>
	     <bean:message key="label.form.select"/>
	   </th>
	   
	   <th> 
	   	  <bean:message key="typeofresultstatus.name"/>
	   </th>
	   <th>
	   	  <bean:message key="typeoftestresult.description"/>
	   </th>
	   <th>
	   	  <bean:message key="typeoftestresult.statusType"/>
	   </th>
  
	</tr>
	<logic:iterate id="tors" indexId="ctr" name="<%=formName%>" property="menuList" type="us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus">
	<bean:define id="torsID" name="tors" property="id"/>
	  <tr>
	   <td class="textcontent">
	      <html:multibox name="<%=formName%>" property="selectedIDs">
	         <bean:write name="torsID" />
	      </html:multibox>
     
   	   </td>
   	    
	   <td class="textcontent">
	   	  <bean:write name="tors" property="name"/>
	   </td>
	   <td class="textcontent">
	   	  <bean:write name="tors" property="description"/>
	   </td>
	   <td class="textcontent">
	   	  <bean:write name="tors" property="statusType"/>
	   </td>
       </tr>
	</logic:iterate>
</table>
