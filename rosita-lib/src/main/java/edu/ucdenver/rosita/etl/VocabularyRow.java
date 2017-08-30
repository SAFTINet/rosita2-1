/*
*   Copyright 2012-2013 The Regents of the University of Colorado
*
*   Licensed under the Apache License, Version 2.0 (the "License")
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package edu.ucdenver.rosita.etl;

import java.util.Date;


public class VocabularyRow {
	
	private String sourceCode;
	private int sourceVocabularyId;
	private String sourceCodeDescription;
	private int targetConceptId;
	private int targetVocabularyId;
	private String mappingType;
	private String primaryMap;
	private Date validStartDate;
	private Date validEndDate;
	private String invalidReason;
	private String mapSource;
	private Long dataSourceId;
	
	public VocabularyRow(String sourceCode, int sourceVocabularyId,
			String sourceCodeDescription, int targetConceptId,
			int targetVocabularyId, String mappingType, String primaryMap,
			Date validStartDate, Date validEndDate, String invalidReason,
			String mapSource, Long dataSourceId) {
		this.sourceCode = sourceCode;
		this.sourceVocabularyId = sourceVocabularyId;
		this.sourceCodeDescription = sourceCodeDescription;
		this.targetConceptId = targetConceptId;
		this.targetVocabularyId = targetVocabularyId;
		this.mappingType = mappingType;
		this.primaryMap = primaryMap;
		this.validStartDate = validStartDate;
		this.validEndDate = validEndDate;
		this.invalidReason = invalidReason;
		this.mapSource = mapSource;
		this.dataSourceId = dataSourceId;
	}
	
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public int getSourceVocabularyId() {
		return sourceVocabularyId;
	}
	public void setSourceVocabularyId(int sourceVocabularyId) {
		this.sourceVocabularyId = sourceVocabularyId;
	}
	public String getSourceCodeDescription() {
		return sourceCodeDescription;
	}
	public void setSourceCodeDescription(String sourceCodeDescription) {
		this.sourceCodeDescription = sourceCodeDescription;
	}
	public int getTargetConceptId() {
		return targetConceptId;
	}
	public void setTargetConceptId(int targetConceptId) {
		this.targetConceptId = targetConceptId;
	}
	public int getTargetVocabularyId() {
		return targetVocabularyId;
	}
	public void setTargetVocabularyId(int targetVocabularyId) {
		this.targetVocabularyId = targetVocabularyId;
	}
	public String getMappingType() {
		return mappingType;
	}
	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}
	public String getPrimaryMap() {
		return primaryMap;
	}
	public void setPrimaryMap(String primaryMap) {
		this.primaryMap = primaryMap;
	}
	public Date getValidStartDate() {
		return validStartDate;
	}
	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}
	public Date getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}
	public String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public String getMapSource() {
		return mapSource;
	}
	public void setMapSource(String mapSource) {
		this.mapSource = mapSource;
	}

	public Long getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
}
