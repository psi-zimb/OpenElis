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
package us.mn.state.health.lims.testresultstatus.daoimpl;

import java.util.List;

 
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.testresultstatus.valueholder.TestResultStatus;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.testresultstatus.dao.TestResultStatusDAO;


public class TestResultStatusDAOImpl extends BaseDAOImpl implements TestResultStatusDAO {

	public boolean insertData(TestResultStatus testResultStatus) throws LIMSRuntimeException {
		try {
			String id = (String)HibernateUtil.getSession().save(testResultStatus); 
			
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = testResultStatus.getSysUserId();
			String tableName = "TEST_RESULT_STATUS";
			auditDAO.saveNewHistory(testResultStatus,sysUserId,tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) { 
			LogEvent.logError("TestResultStatusDAOImpl","insertData()",e.toString());
			throw new LIMSRuntimeException("Error in TestResultStatus insertData()",e);
		}

		return true;
	}


	public TestResultStatus getTestResultStatusByTestId(String testId)throws LIMSRuntimeException {
		TestResultStatus testResultStatus = null;
		try {
			String sql = "from TestResultStatus ts where ts.test = :testId";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testId", Integer.parseInt(testId));
			
			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TestResultStatus) list.get(0);
			}
		} catch (Exception e) { 
			LogEvent.logError("TestResultStatusDAOImpl","getTestResultStatusByTestId()",e.toString());
			throw new LIMSRuntimeException("Error in TestResultStatus getTestResultStatusByTestId()", e);
		}

		return testResultStatus;
	}
}
