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

public class ConceptMap {

	Long conceptMapId;
	String targetTable;
	String targetColumn;
	String sourceValue;
	String sourceVocabulary;
	String sourceDesc;
	Long targetConceptId;
	String mapped;
	String empty;
	Long sourceCount;
	
	public Long getConceptMapId() {
		return conceptMapId;
	}
	public void setConceptMapId(Long conceptMapId) {
		this.conceptMapId = conceptMapId;
	}
	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}
	public String getTargetColumn() {
		return targetColumn;
	}
	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}
	public String getSourceValue() {
		return sourceValue;
	}
	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
	public String getSourceVocabulary() {
		return sourceVocabulary;
	}
	public void setSourceVocabulary(String sourceVocabulary) {
		this.sourceVocabulary = sourceVocabulary;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	public Long getTargetConceptId() {
		return targetConceptId;
	}
	public void setTargetConceptId(Long targetConceptId) {
		this.targetConceptId = targetConceptId;
	}
	public String getMapped() {
		return mapped;
	}
	public void setMapped(String mapped) {
		this.mapped = mapped;
	}
	public String getEmpty() {
		return empty;
	}
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	public Long getSourceCount() {
		return sourceCount;
	}
	public void setSourceCount(Long sourceCount) {
		this.sourceCount = sourceCount;
	}

}
