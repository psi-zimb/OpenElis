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
package us.mn.state.health.lims.typeofresultstatus.dao;

import us.mn.state.health.lims.common.dao.BaseDAO;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus;

import java.util.List;

/**
 * @author Srivathsala 
 */
public interface TypeOfResultStatusDAO extends BaseDAO {

	public boolean insertData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException;

	public void deleteData(List typeOfResultStatuss) throws LIMSRuntimeException;

	public List getAllTypeOfResultStatus() throws LIMSRuntimeException;

	public List getPageOfTypeOfResultStatus(int startingRecNo) throws LIMSRuntimeException;

	public void getData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException;

	public void updateData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException;
	
	public List getNextTypeOfResultStatusRecord(String id) throws LIMSRuntimeException;

	public List getPreviousTypeOfResultStatusRecord(String id) throws LIMSRuntimeException; 
	
	public Integer getTotalTypeOfResultStatusCount() throws LIMSRuntimeException; 
	
	public TypeOfResultStatus getTypeOfResultStatusByType(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException;

    public TypeOfResultStatus getTypeOfResultStatusByType(String type) throws LIMSRuntimeException;

	public TypeOfResultStatus getTypeOfResultStatusByName(String typeOfResultStatusName) throws LIMSRuntimeException;

    public TypeOfResultStatus getTypeOfResultStatusById(String resultTypeId) throws LIMSRuntimeException;
}
