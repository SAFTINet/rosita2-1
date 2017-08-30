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

package com.recomdata.grails.domain

import au.com.bytecode.opencsv.CSVWriter;

class ConceptMapController {

    def export = {
		def all = params.all
		
		response.setHeader('Content-disposition', 'attachment; filename=unmapped_source_values.csv')
		response.contentType = 'text/plain'
		
		def mappings
		if (all) {
            response.setHeader('Content-disposition', 'attachment; filename=all_source_values.csv')
			mappings = ConceptMap.list();
		}
		else {
			mappings = ConceptMap.findAllByMapped('N');
		}
		
		String lineSeparator = System.getProperty('line.separator')
		CSVWriter csv = new CSVWriter(response.writer)
		
		String[] head = ["Target Table", "Target Column", "Source Value", "Source Vocabulary", "Source Desc", "Data Source ID"]
		csv.writeNext(head)
		
		for (row in mappings) {
			String[] vals = [row.targetTable, row.targetColumn, row.sourceValue, row.sourceVocabulary, row.sourceDesc, row.dataSourceId]
			csv.writeNext(vals)
		}
		csv.close()
	}
}
