/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/
package us.mn.state.health.lims.typeofresultstatus.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.apache.commons.beanutils.PropertyUtils;
import us.mn.state.health.lims.common.action.BaseMenuAction;
import us.mn.state.health.lims.common.util.SystemConfiguration; 
import us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus;  
import us.mn.state.health.lims.typeofresultstatus.dao.TypeOfResultStatusDAO;
import us.mn.state.health.lims.typeofresultstatus.daoimpl.TypeOfResultStatusDAOImpl;

public class TypeOfResultStatusMenuAction extends BaseMenuAction {

	protected List createMenuList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

 		//System.out.println("I am in TypeOfResultStatusMenuAction createMenuList()");

		List typeOfResultStatus = new ArrayList();

		String stringStartingRecNo = (String) request
				.getAttribute("startingRecNo");
		int startingRecNo = Integer.parseInt(stringStartingRecNo);

		TypeOfResultStatusDAO typeOfResultStatusDAO = new TypeOfResultStatusDAOImpl(); 
		typeOfResultStatus = typeOfResultStatusDAO.getPageOfTypeOfResultStatus(startingRecNo);

		request.setAttribute("menuDefinition", "TypeOfResultStatusMenuDefinition"); 
		
		request.setAttribute(MENU_TOTAL_RECORDS, String.valueOf(typeOfResultStatusDAO
				.getTotalTypeOfResultStatusCount()));
		request.setAttribute(MENU_FROM_RECORD, String.valueOf(startingRecNo));
		int numOfRecs = 0;
		if (typeOfResultStatus != null) {
			if (typeOfResultStatus.size() > SystemConfiguration.getInstance()
					.getDefaultPageSize()) {
				numOfRecs = SystemConfiguration.getInstance()
						.getDefaultPageSize();
			} else {
				numOfRecs = typeOfResultStatus.size();
			}
			numOfRecs--;
		}
		int endingRecNo = startingRecNo + numOfRecs;
		request.setAttribute(MENU_TO_RECORD, String.valueOf(endingRecNo));
		//end bugzilla 1411
		
		return typeOfResultStatus;
	}

	protected String getPageTitleKey() {
		return "typeofresultstatus.browse.title";
	}

	protected String getPageSubtitleKey() {
		return "typeofresultstatus.browse.title";
	}

	protected int getPageSize() {
		return SystemConfiguration.getInstance().getDefaultPageSize();
	}

	protected String getDeactivateDisabled() {
		return "true";
	}

}
