package us.mn.state.health.lims.typeofteststatus.daoimpl;

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
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

import java.util.List;
import java.util.Vector;

/**
 * @author Srivathsala
 */
public class TypeOfTestStatusDAOImpl extends BaseDAOImpl implements TypeOfTestStatusDAO {

	public void deleteData(List typeOfTestStatus) throws LIMSRuntimeException {
		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < typeOfTestStatus.size(); i++) {
				TypeOfTestStatus data = (TypeOfTestStatus) typeOfTestStatus.get(i);

				TypeOfTestStatus oldData = (TypeOfTestStatus) readTypeOfTestStatus(data.getId());
				TypeOfTestStatus newData = new TypeOfTestStatus();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "TYPE_OF_Test_STATUS";
				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "AuditTrail deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus AuditTrail deleteData()", e);
		}

		try {
			for (int i = 0; i < typeOfTestStatus.size(); i++) {
				TypeOfTestStatus data = (TypeOfTestStatus) typeOfTestStatus.get(i);
				data = (TypeOfTestStatus) readTypeOfTestStatus(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus deleteData()", e);
		}
	}

	public boolean insertData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException {
		try {
			if (duplicateTypeOfTestStatusExists(typeOfTestStatus)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for " + typeOfTestStatus.getDescription());
			}

			String id = (String) HibernateUtil.getSession().save(typeOfTestStatus);
			typeOfTestStatus.setId(id);

			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = typeOfTestStatus.getSysUserId();
			String tableName = "TYPE_OF_Test_STATUS";
			auditDAO.saveNewHistory(typeOfTestStatus, sysUserId, tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "insertData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus insertData()", e);
		}

		return true;
	}

	public void updateData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException {
		try {
			if (duplicateTypeOfTestStatusExists(typeOfTestStatus)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for " + typeOfTestStatus.getDescription());
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus updateData()", e);
		}

		TypeOfTestStatus oldData = (TypeOfTestStatus) readTypeOfTestStatus(typeOfTestStatus.getId());
		TypeOfTestStatus newData = typeOfTestStatus;

		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = typeOfTestStatus.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "TYPE_OF_Test_STATUS";
			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "AuditTrail updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus AuditTrail updateData()", e);
		}

		try {
			HibernateUtil.getSession().merge(typeOfTestStatus);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(typeOfTestStatus);
			HibernateUtil.getSession().refresh(typeOfTestStatus);
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus updateData()", e);
		}
	}

	public void getData(TypeOfTestStatus typeOfTestStatus) throws LIMSRuntimeException {
		try {
			TypeOfTestStatus sc = (TypeOfTestStatus) HibernateUtil.getSession().get(TypeOfTestStatus.class,
					typeOfTestStatus.getId());
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			if (sc != null) {
				PropertyUtils.copyProperties(typeOfTestStatus, sc);
			} else {
				typeOfTestStatus.setId(null);
			}
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "getData()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus getData()", e);
		}
	}

	public List getAllTypeOfTestStatus() throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfTestStatus";
			Query query = HibernateUtil.getSession().createQuery(sql);
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			return list;
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfTestStatusDAOImpl", "getAllTypeOfTestStatus()", e);
			throw new LIMSRuntimeException("Error in TypeOfTestStatus getAllTypeOfTestStatus()", e);
		}
	}

	public List getPageOfTypeOfTestStatus(int startingRecNo) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			// bugzilla 1399
			String sql = "from TypeOfTestStatus t order by t.name";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "getPageOfTypeOfTesttatus()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus getPageOfTypeOfTestStatus()", e);
		}

		return list;
	}

	public TypeOfTestStatus readTypeOfTestStatus(String idString) {
		TypeOfTestStatus data = null;
		try {
			data = (TypeOfTestStatus) HibernateUtil.getSession().get(TypeOfTestStatus.class, idString);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "readTypeOfTestStatus()", e.toString());
			throw new LIMSRuntimeException("Error in TypeOfTestStatus readTypeOfTestStatus()", e);
		}

		return data;
	}

	public List getNextTypeOfTestStatusRecord(String id) throws LIMSRuntimeException {

		return getNextRecord(id, "TypeOfTestStatus", TypeOfTestStatus.class);

	}

	public List getPreviousTypeOfTestStatusRecord(String id) throws LIMSRuntimeException {

		return getPreviousRecord(id, "TypeOfTestStatus", TypeOfTestStatus.class);
	}

	public Integer getTotalTypeOfTestStatusCount() throws LIMSRuntimeException {
		return getTotalCount("TypeOfTestStatus", TypeOfTestStatus.class);
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
			LogEvent.logError("TypeOfTestStatusDAOImpl", "getNextRecord()", e.toString());
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
			LogEvent.logError("TypeOfTestStatusDAOImpl", "getPreviousRecord()", e.toString());
			throw new LIMSRuntimeException("Error in getPreviousRecord() for " + table, e);
		}

		return list;
	}

	// bugzilla 1482
	private boolean duplicateTypeOfTestStatusExists(TypeOfTestStatus typeOfTestStatus)
			throws LIMSRuntimeException {
		try {

			// not case sensitive hemolysis and Hemolysis are considered
			// duplicates
			String sql = "from TypeOfTestStatus t where (trim(lower(t.name)) = :param and t.id != :param2) or (trim(lower(t.statusType)) = :param3 and t.id != :param2)";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfTestStatus.getName().toLowerCase().trim());

			query.setParameter("param3", typeOfTestStatus.getStatusType().toLowerCase().trim());

			// initialize with 0 (for new records where no id has been generated
			// yet
			String typeOfTestStatusId = "0";
			if (!StringUtil.isNullorNill(typeOfTestStatus.getId())) {
				typeOfTestStatusId = typeOfTestStatus.getId();
			}
			query.setParameter("param2", Integer.parseInt(typeOfTestStatusId));

			List list = query.list();

			return list.size() > 0;

		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "duplicateTypeOfTestStatusExists()", e.toString());
			throw new LIMSRuntimeException("Error in duplicateTypeOfTestStatusExists()", e);
		}
	}

	// bugzilla 1866 to get HL7 value
	public TypeOfTestStatus getTypeOfTestStatusByType(TypeOfTestStatus typeOfTestStatus)
			throws LIMSRuntimeException {
		TypeOfTestStatus totr = null;
		try {
			String sql = "from TypeOfTestStatus totr where upper(totr.statysType) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfTestStatus.getStatusType().trim().toUpperCase());

			List list = query.list();

			if (list != null && list.size() > 0)
				totr = (TypeOfTestStatus) list.get(0);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			LogEvent.logError("TypeOfTestStatusDAOImpl", "getTypeOfTestStatusByType()", e.toString());
			throw new LIMSRuntimeException("Error in getTypeOfTestStatusByType()", e);
		}

		return totr;
	}

	@Override
	public TypeOfTestStatus getTypeOfTestStatusByType(String testTestStatusType) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfTestStatus totr where upper(totr.statusType) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", testTestStatusType.trim().toUpperCase());
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			return list.isEmpty() ? null : (TypeOfTestStatus) list.get(0);
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfTestStatusDAOImpl",
					"getTypeOfTestStatusByType(String testTestStatusType)", e);
			throw new LIMSRuntimeException("Error in getTypeOfTestStatusByType(String testTestStatusType)", e);
		}
	}

	public TypeOfTestStatus getTypeOfTestStatusByName(String typeOfTestStatusName) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfTestStatus totr where upper(totr.name) = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", typeOfTestStatusName.trim().toUpperCase());

			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TypeOfTestStatus) list.get(0);
			}
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfTestStatusDAOImpl",
					"getTypeOfTestStatusByName(String TypeOfTestStatusName)", e);
			throw new LIMSRuntimeException("Error in getTypeOfTestStatusByType(String typeOfTestStatusName)", e);
		}

		return null;
	}

	@Override
	public TypeOfTestStatus getTypeOfTestStatusById(String TestStatusId) throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfTestStatus totr where totr.id = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", Integer.parseInt(TestStatusId));

			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TypeOfTestStatus) list.get(0);
			}
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfTestStatusDAOImpl", "getTypeOfTestStatusById(String TestStatusId)", e);
			throw new LIMSRuntimeException("Error in getTypeOfTestStatusByType(String TestStatusId)", e);
		}

		return null;
	}

	@Override
	public List getAllActiveTestStatus() throws LIMSRuntimeException {
		try {
			String sql = "from TypeOfTestStatus totr where totr.isActive = 'Y'";
			Query query = HibernateUtil.getSession().createQuery(sql);
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			return list;
		} catch (Exception e) {
			LogEvent.logErrorStack("TypeOfTestStatusDAOImpl", "getAllActiveTestStatus()", e);
			throw new LIMSRuntimeException("Error in TypeOfTestStatus getAllActiveTestStatus()", e);
		}
	}

}
