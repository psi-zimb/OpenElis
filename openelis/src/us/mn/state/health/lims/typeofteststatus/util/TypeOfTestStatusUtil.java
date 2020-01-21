/**
 * 
 */
package us.mn.state.health.lims.typeofteststatus.util;

import java.util.ArrayList;
import java.util.List;

import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

/**
 * @author srivathsalac
 *
 */
public class TypeOfTestStatusUtil {

	private static List<IdValuePair> toResultStatuses;

	public static List<IdValuePair> getAllActiveTypeOfResultStatus() {

		if (toResultStatuses == null) {

			TypeOfTestStatusDAO torsDAO = new TypeOfTestStatusDAOImpl();
			List<TypeOfTestStatus> torsList = torsDAO.getAllActiveTestStatus();

			if (torsList != null && torsList.size() > 0) {
				toResultStatuses = new ArrayList<IdValuePair>();

				for (TypeOfTestStatus tors : torsList) {
					toResultStatuses.add(new IdValuePair(tors.getId(), tors.getName()));
				}
			}
		}
		return toResultStatuses;
	}

}
