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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.apache.commons.beanutils.PropertyUtils;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus;
import us.mn.state.health.lims.typeofresultstatus.dao.TypeOfResultStatusDAO;
import us.mn.state.health.lims.typeofresultstatus.daoimpl.TypeOfResultStatusDAOImpl;


/**
 * @author srivathsalac
 *
 */
public class TypeOfResultStatusAction extends BaseAction {

	private boolean isNew = false;

	protected ActionForward performAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// The first job is to determine if we are coming to this action with an
		// ID parameter in the request. If there is no parameter, we are
		// creating a new TypeOfResultStatus.
		// If there is a parameter present, we should bring up an existing
		// TypeOfResultStatus to edit.

		String id = request.getParameter(ID);

		String forward = FWD_SUCCESS;
		request.setAttribute(ALLOW_EDITS_KEY, "true");
		request.setAttribute(PREVIOUS_DISABLED, "true");
		request.setAttribute(NEXT_DISABLED, "true");

		DynaActionForm dynaForm = (DynaActionForm) form;

		// initialize the form
		dynaForm.initialize(mapping);

		TypeOfResultStatus typeOfResultStatus = new TypeOfResultStatus();
		System.out.println("I am in TypeOfResultStatusAction and this is id " + id);
		if ((id != null) && (!"0".equals(id))) {  
			typeOfResultStatus.setId(id);
			TypeOfResultStatusDAO typeOfResultStatusDAO = new TypeOfResultStatusDAOImpl();
			typeOfResultStatusDAO.getData(typeOfResultStatus);

			isNew = false; // this is to set correct page title
			
			// do we need to enable next or previous?
			//bugzilla 1427 pass in desc not id
			List typeOfResultStatuss = typeOfResultStatusDAO.getNextTypeOfResultStatusRecord(typeOfResultStatus.getDescription());
			if (typeOfResultStatuss.size() > 0) {
				// enable next button
				request.setAttribute(NEXT_DISABLED, "false");
			}
			//bugzilla 1427 pass in desc not id
			typeOfResultStatuss = typeOfResultStatusDAO.getPreviousTypeOfResultStatusRecord(typeOfResultStatus.getDescription());
			if (typeOfResultStatuss.size() > 0) {
				// enable next button
				request.setAttribute(PREVIOUS_DISABLED, "false");
			}
			// end of logic to enable next or previous button


		} else { // this is a new typeOfResultStatus

			isNew = true; // this is to set correct page title

		}

		if (typeOfResultStatus.getId() != null && !typeOfResultStatus.getId().equals("0")) {
			request.setAttribute(ID, typeOfResultStatus.getId());
		}

		// populate form from valueholder
		PropertyUtils.copyProperties(form, typeOfResultStatus);

		System.out.println("I am in TypeOfResultStatusAction this is forward " + forward);
		return mapping.findForward(forward);
	}

	protected String getPageTitleKey() {
		if (isNew) {
			return "typeofresultstatus.add.title";
		} else {
			return "typeofresultstatus.edit.title";
		}
	}

	protected String getPageSubtitleKey() {
		if (isNew) {
			return "typeofresultstatus.add.title";
		} else {
			return "typeofresultstatus.edit.title";
		}
	}

}
