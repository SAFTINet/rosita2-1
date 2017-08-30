package edu.ucdenver.rosita.etl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType( XmlAccessType.NONE )
@XmlRootElement(name="x_cohort_metadata")
public class XCohortMetadata {

	 String id; 
	 @XmlElement(name="cohort_name")
	 String cohortName;
	 @XmlElement(name="cohort_description")
	 String cohortDescription;
	 @XmlElement(name="cohort_creator_name")
	 String cohortCreatorName;
	 @XmlElement(name="cohort_creator_contact")
	 String cohortCreatorContact;
	 @XmlElement(name="is_cohort_shared")
	 Boolean isCohortShared;
	 @XmlElement(name="cohort_query")
	 String cohortQuery;
	 @XmlElement(name="service_url")
	 String serviceUrl;
	 @XmlElement(name="original_cohort_count")
	 String originalCohortCount;
	 @XmlElement(name="last_cohort_count")
	 String lastCohortCount;
	 @XmlElement(name="original_cohort_date")
	 String originalCohortDate;
	 @XmlElement(name="last_cohort_date")
	 String lastCohortDate;
	 @XmlElement(name="cohort_phi_notes")
	 String cohortPhiNotes;
	 @XmlElement(name="cohort_other_notes")
	 String cohortOtherNotes;
	 @XmlElement(name="cohort_expiration_date")
	 String cohortExpirationDate;
	 @XmlElement(name="grid_node_id")
	 String xgridNodeId;
	 
	 
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getOriginalCohortCount() {
		return originalCohortCount;
	}
	public void setOriginalCohortCount(String originalCohortCount) {
		this.originalCohortCount = originalCohortCount;
	}
	public String getLastCohortCount() {
		return lastCohortCount;
	}
	public void setLastCohortCount(String lastCohortCount) {
		this.lastCohortCount = lastCohortCount;
	}
	public String getOriginalCohortDate() {
		return originalCohortDate;
	}
	public void setOriginalCohortDate(String originalCohortDate) {
		this.originalCohortDate = originalCohortDate;
	}
	public String getLastCohortDate() {
		return lastCohortDate;
	}
	public void setLastCohortDate(String lastCohortDate) {
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
	public String getCohortExpirationDate() {
		return cohortExpirationDate;
	}
	public void setCohortExpirationDate(String cohortExpirationDate) {
		this.cohortExpirationDate = cohortExpirationDate;
	}
	public String getXgridNodeId() {
		return xgridNodeId;
	}
	public void setXgridNodeId(String xgridNodeId) {
		this.xgridNodeId = xgridNodeId;
	}
	
}
