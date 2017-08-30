package edu.ucdenver.rosita.etl.omop;

import java.util.Date;

public class OmopXCohortMetadata {

	 Long id; 
	 String cohortName;
	 String cohortDescription;
	 String cohortCreatorName;
	 String cohortCreatorContact;
	 Boolean isCohortShared;
	 String cohortQuery;
	 String serviceUrl;
	 Long originalCohortCount;
	 Long lastCohortCount;
	 Date originalCohortDate;
	 Date lastCohortDate;
	 String cohortPhiNotes;
	 String cohortOtherNotes;
	 Date cohortExpirationDate;
	 Long xgridNodeId;
	 
	
	public String getCohortName() {
		return cohortName;
	}
	public void setCohortName(String cohortName) {
		this.cohortName = cohortName;
	}
	public String getCohortDescription() {
		return cohortDescription;
	}
	public void setCohortDescription(String cohortDescription) {
		this.cohortDescription = cohortDescription;
	}
	public String getCohortCreatorName() {
		return cohortCreatorName;
	}
	public void setCohortCreatorName(String cohortCreatorName) {
		this.cohortCreatorName = cohortCreatorName;
	}
	public String getCohortCreatorContact() {
		return cohortCreatorContact;
	}
	public void setCohortCreatorContact(String cohortCreatorContact) {
		this.cohortCreatorContact = cohortCreatorContact;
	}
	public Boolean getIsCohortShared() {
		return isCohortShared;
	}
	public void setIsCohortShared(Boolean isCohortShared) {
		this.isCohortShared = isCohortShared;
	}
	public String getCohortQuery() {
		return cohortQuery;
	}
	public void setCohortQuery(String cohortQuery) {
		this.cohortQuery = cohortQuery;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public Long getOriginalCohortCount() {
		return originalCohortCount;
	}
	public void setOriginalCohortCount(Long originalCohortCount) {
		this.originalCohortCount = originalCohortCount;
	}
	public Long getLastCohortCount() {
		return lastCohortCount;
	}
	public void setLastCohortCount(Long lastCohortCount) {
		this.lastCohortCount = lastCohortCount;
	}
	public Date getOriginalCohortDate() {
		return originalCohortDate;
	}
	public void setOriginalCohortDate(Date originalCohortDate) {
		this.originalCohortDate = originalCohortDate;
	}
	public Date getLastCohortDate() {
		return lastCohortDate;
	}
	public void setLastCohortDate(Date lastCohortDate) {
		this.lastCohortDate = lastCohortDate;
	}
	public String getCohortPhiNotes() {
		return cohortPhiNotes;
	}
	public void setCohortPhiNotes(String cohortPhiNotes) {
		this.cohortPhiNotes = cohortPhiNotes;
	}
	public String getCohortOtherNotes() {
		return cohortOtherNotes;
	}
	public void setCohortOtherNotes(String cohortOtherNotes) {
		this.cohortOtherNotes = cohortOtherNotes;
	}
	public Date getCohortExpirationDate() {
		return cohortExpirationDate;
	}
	public void setCohortExpirationDate(Date cohortExpirationDate) {
		this.cohortExpirationDate = cohortExpirationDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getXgridNodeId() {
		return xgridNodeId;
	}
	public void setXgridNodeId(Long xgridNodeId) {
		this.xgridNodeId = xgridNodeId;
	}

}
