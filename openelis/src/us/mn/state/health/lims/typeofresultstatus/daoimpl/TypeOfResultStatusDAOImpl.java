package us.mn.state.health.lims.typeofresultstatus.daoimpl;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.typeofresultstatus.dao.TypeOfResultStatusDAO;
import us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Srivathsala
 */
public class TypeOfResultStatusDAOImpl extends BaseDAOImpl implements TypeOfResultStatusDAO {

	public void deleteData(List typeOfResultStatus) throws LIMSRuntimeException {
		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < typeOfResultStatus.size(); i++) {
				TypeOfResultStatus data = (TypeOfResultStatus) typeOfResultStatus.get(i);

				TypeOfResultStatus oldData = (TypeOfResultStatus) readTypeOfResultStatus(data.getId());
				TypeOfResultStatus newData = new TypeOfResultStatus();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "TYPE_OF_RESULT_STATUS";
				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "AuditTrail deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus AuditTrail deleteData()", e);
		}

		try {
			for (int i = 0; i < typeOfResultStatus.size(); i++) {
				TypeOfResultStatus data = (TypeOfResultStatus) typeOfResultStatus.get(i);
				data = (TypeOfResultStatus) readTypeOfResultStatus(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus deleteData()", e);
		}
	}

	public boolean insertData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException {
		try {
			if (duplicateTypeOfResultStatusExists(typeOfResultStatus)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for " + typeOfResultStatus.getDescription());
			}

			String id = (String) HibernateUtil.getSession().save(typeOfResultStatus);
			typeOfResultStatus.setId(id);

			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = typeOfResultStatus.getSysUserId();
			String tableName = "TYPE_OF_RESULT_STATUS";
			auditDAO.saveNewHistory(typeOfResultStatus, sysUserId, tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "insertData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus insertData()", e);
		}

		return true;
	}

	public void updateData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException {
		try {
			if (duplicateTypeOfResultStatusExists(typeOfResultStatus)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for " + typeOfResultStatus.getDescription());
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus updateData()", e);
		}

		TypeOfResultStatus oldData = (TypeOfResultStatus) readTypeOfResultStatus(typeOfResultStatus.getId());
		TypeOfResultStatus newData = typeOfResultStatus;

		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = typeOfResultStatus.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "TYPE_OF_RESULT_STATUS";
			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "AuditTrail updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus AuditTrail updateData()", e);
		}

		try {
			HibernateUtil.getSession().merge(typeOfResultStatus);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(typeOfResultStatus);
			HibernateUtil.getSession().refresh(typeOfResultStatus);
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus updateData()", e);
		}
	}

	public void getData(TypeOfResultStatus typeOfResultStatus) throws LIMSRuntimeException {
		try {
			TypeOfResultStatus sc = (TypeOfResultStatus) HibernateUtil.getSession().get(TypeOfResultStatus.class,
					typeOfResultStatus.getId());
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			if (sc != null) {
				PropertyUtils.copyProperties(typeOfResultStatus, sc);
			} else {
				typeOfResultStatus.setId(null);
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "getData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus getData()", e);
		}
	}

	public List getAllTypeOfResultStatus() throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfResultStatus";
			Query query = HibernateUtil.getSession().createQuery(sql);
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			return list;
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfResultStatusDAOImpl", "getAllTypeOfResultStatus()", e);
			throw new LIMSRuntimeException("Error in TypeOfResultStatus getAllTypeOfResultStatus()", e);
		}
	}

	public List getPageOfTypeOfResultStatus(int startingRecNo) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			// bugzilla 1399
			String sql = "from TypeOfResultStatus t order by t.name";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "getPageOfTypeOfResultStatus()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus getPageOfTypeOfResultStatus()", e);
		}

		return list;
	}

	public TypeOfResultStatus readTypeOfResultStatus(String idString) {
		TypeOfResultStatus data = null;
		try {
			data = (TypeOfResultStatus) HibernateUtil.getSession().get(TypeOfResultStatus.class, idString);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "readTypeOfResultStatus()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfResultStatus readTypeOfResultStatus()", e);
		}

		return data;
	}

	public List getNextTypeOfResultStatusRecord(String id) throws LIMSRuntimeException {

		return getNextRecord(id, "TypeOfResultStatus", TypeOfResultStatus.class);

	}

	public List getPreviousTypeOfResultStatusRecord(String id) throws LIMSRuntimeException {

		return getPreviousRecord(id, "TypeOfResultStatus", TypeOfResultStatus.class);
	}

	public Integer getTotalTypeOfResultStatusCount() throws LIMSRuntimeException {
		return getTotalCount("TypeOfResultStatus", TypeOfResultStatus.class);
	}

	public List getNextRecord(String id, String table, Class clazz) throws LIMSRuntimeException {

		List list = new Vector();
		try {
			String sql = "from " + table + " t where id >= " + enquote(id) + " order by t.name";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2);

			list = query.list();

		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "getNextRecord()", e.toString());
			throw new LIMSRuntimeException("Error in getNextRecord() for " + table, e);
		}

		return list;
	}

	public List getPreviousRecord(String id, String table, Class clazz) throws LIMSRuntimeException {

		List list = new Vector();
		try {
			String sql = "from " + table + " t order by t.description desc where description <= " + enquote(id);
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2);

			list = query.list();
		} catch (Exception e) {
			// bugzilla 2154
			LogEvent.logError("TypeOfResultStatusDAOImpl", "getPreviousRecord()", e.toString());
			throw new LIMSRuntimeException("Error in getPreviousRecord() for " + table, e);
		}

		return list;
	}

	// bugzilla 1482
	private boolean duplicateTypeOfResultStatusExists(TypeOfResultStatus typeOfResultStatus)
			throws LIMSRuntimeException {
		try {

			// not case sensitive hemolysis and Hemolysis are considered
			// duplicates
			String sql = "from TypeOfResultStatus t where (trim(lower(t.name)) = :param and t.id != :param2) or (trim(lower(t.statusType)) = :param3 and t.id != :param2)";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfResultStatus.getName().toLowerCase().trim());
			
			query.setParameter("param3", typeOfResultStatus.getStatusType().toLowerCase().trim());

			// initialize with 0 (for new records where no id has been generated
			// yet
			String typeOfResultStatusId = "0";
			if (!StringUtil.isNullorNill(typeOfResultStatus.getId())) {
				typeOfResultStatusId = typeOfResultStatus.getId();
			}
			query.setParameter("param2", Integer.parseInt(typeOfResultStatusId));

			List list = query.list();

			return list.size() > 0;

		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "duplicateTypeOfResultStatusExists()", e.toString());
			throw new LIMSRuntimeException("Error in duplicateTypeOfResultStatusExists()", e);
		}
	}

	// bugzilla 1866 to get HL7 value
	public TypeOfResultStatus getTypeOfResultStatusByType(TypeOfResultStatus typeOfResultStatus)
			throws LIMSRuntimeException {
		TypeOfResultStatus totr = null;
		try {
			String sql = "from TypeOfResultStatus totr where upper(totr.statysType) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfResultStatus.getStatusType().trim().toUpperCase());

			List list = query.list();

			if (list != null && list.size() > 0)
				totr = (TypeOfResultStatus) list.get(0);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			LogEvent.logError("TypeOfResultStatusDAOImpl", "getTypeOfResultStatusByType()", e.toString());
			throw new LIMSRuntimeException("Error in getTypeOfResultStatusByType()", e);
		}

		return totr;
	}

	@Override
	public TypeOfResultStatus getTypeOfResultStatusByType(String testResultStatusType) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfResultStatus totr where upper(totr.statusType) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", testResultStatusType.trim().toUpperCase());
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			return list.isEmpty() ? null : (TypeOfResultStatus) list.get(0);
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfResultStatusDAOImpl",
					"getTypeOfResultStatusByType(String testResultStatusType)", e);
			throw new LIMSRuntimeException("Error in getTypeOfResultStatusByType(String testResultStatusType)", e);
		}
	}

	public TypeOfResultStatus getTypeOfResultStatusByName(String typeOfResultStatusName) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfResultStatus totr where upper(totr.name) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfResultStatusName.trim().toUpperCase());

			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TypeOfResultStatus) list.get(0);
			}
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfResultStatusDAOImpl",
					"getTypeOfResultStatusByName(String TypeOfResultStatusName)", e);
			throw new LIMSRuntimeException("Error in getTypeOfResultStatusByType(String typeOfResultStatusName)", e);
		}

		return null;
	}

	@Override
	public TypeOfResultStatus getTypeOfResultStatusById(String resultStatusId) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfResultStatus totr where totr.id = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", Integer.parseInt(resultStatusId));

			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TypeOfResultStatus) list.get(0);
			}
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfResultStatusDAOImpl", "getTypeOfResultStatusById(String resultStatusId)", e);
			throw new LIMSRuntimeException("Error in getTypeOfResultStatusByType(String resultStatusId)", e);
		}

		return null;
	}
}
