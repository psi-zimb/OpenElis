package us.mn.state.health.lims.typeofteststatus.valueholder;

import java.sql.Timestamp;

import us.mn.state.health.lims.common.valueholder.BaseObject;

public class TypeOfTestStatus extends BaseObject {

	/**
	 * Buvaneswari Arun
	 */
	private static final long serialVersionUID = -7050083545355949587L;

	private String id;
	
	private String name;

	private String description;

	private String statusType;
	
	private Boolean isActive;
	
	private Boolean isResultRequired;
	
	private Boolean isApprovalRequired;
	
	private Timestamp dateCreated;
	
	public TypeOfTestStatus() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsResultRequired() {
		return isResultRequired;
	}

	public void setIsResultRequired(Boolean isResultRequired) {
		this.isResultRequired = isResultRequired;
	}

	public Boolean getIsApprovalRequired() {
		return isApprovalRequired;
	}

	public void setIsApprovalRequired(Boolean isApprovalRequired) {
		this.isApprovalRequired = isApprovalRequired;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	} 

}
