package us.mn.state.health.lims.typeofteststatus.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TypeOfTestStatusMenuCancelAction extends TypeOfTestStatusMenuAction {

    protected ActionForward performAction(ActionMapping mapping,
                                          ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        request.setAttribute("menuDefinition", DEFAULT);

        return mapping.findForward(FWD_CLOSE);

    }

}
