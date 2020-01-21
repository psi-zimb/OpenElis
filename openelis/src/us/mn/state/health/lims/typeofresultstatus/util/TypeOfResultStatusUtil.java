/**
 * 
 */
package typeofresultstatus.util;

import java.util.ArrayList;
import java.util.List;

import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.typeofresultstatus.dao.TypeOfResultStatusDAO;
import us.mn.state.health.lims.typeofresultstatus.daoimpl.TypeOfResultStatusDAOImpl;
import us.mn.state.health.lims.typeofresultstatus.valueholder.TypeOfResultStatus;

/**
 * @author srivathsalac
 *
 */
public class TypeOfResultStatusUtil {

	private static List<IdValuePair> toResultStatuses;

	public static List<IdValuePair> getAllActiveTypeOfResultStatus() {

		if (toResultStatuses == null) {

			TypeOfResultStatusDAO torsDAO = new TypeOfResultStatusDAOImpl();
			List<TypeOfResultStatus> torsList = torsDAO.getAllActiveResultStatus();

			if (torsList != null && torsList.size() > 0) {
				toResultStatuses = new ArrayList<IdValuePair>();

				for (TypeOfResultStatus tors : torsList) {
					toResultStatuses.add(new IdValuePair(tors.getId(), tors.getName()));
				}
			}
		}
		return toResultStatuses;
	}

}
