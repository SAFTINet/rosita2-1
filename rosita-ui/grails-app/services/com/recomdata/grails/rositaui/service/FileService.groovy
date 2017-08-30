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

package com.recomdata.grails.rositaui.service

import java.io.BufferedReader;
import java.io.FileReader;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.recomdata.grails.domain.MultiClinicDataSource
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import com.recomdata.grails.rositaui.utils.GenericFilenameFilter

import edu.ucdenver.rosita.utils.ParsingService;

class FileService {
	
	def rositaPropertiesService

    static transactional = true

    def getBrowseFolder() {
        String folder = rositaPropertiesService.get("folder.datasources") ?: ""
        if (!folder.endsWith("/")) {
            folder += "/"
        }
		return folder
    }
	
	def getFiles(extension) {
		return getFiles(extension, rositaPropertiesService.get("folder.datasources") ?: null)
	}

	def getFiles(extension, folderName) {

		GenericFilenameFilter filter = null
		if (extension) {
			filter = new GenericFilenameFilter(extension)
		}
		if (!folderName) {
			return null;
		}

		File folder = new File(folderName);
		if (!folder.isDirectory()) {
			return null
		}

		def filenames = []
		File[] files = folder.listFiles(filter);
		for (file in files) {
			filenames.add(file.getName())
		}

		return filenames
	}

    def getDirs(browseFolder,allowDups) {

        //def dirs = addDirs(browseFolder, new GenericFilenameFilter(".csv||.txt"))
        def dirs = []
        def csvfilter = new GenericFilenameFilter(".csv")
        def txtfilter = new GenericFilenameFilter(".txt")
        addDirs(browseFolder,csvfilter,dirs,allowDups)
        addDirs(browseFolder,txtfilter,dirs,allowDups)
        def xmlfilter = new GenericFilenameFilter(".xml")
        addDirs(browseFolder,xmlfilter,dirs,allowDups)

        return dirs
    }

	def addDirs(browseFolder,filter,dirnames,allowDups) {
        File folder = new File(browseFolder)
        if (!folder.isDirectory()) {
            return null
        }
		
		//NB. This is completely awful. Previously it was slightly more awful and I
		//hesitate to change it further in case anything is relying on its awfulness
        File[] files = folder.listFiles()
        for (dir in files) {
            if (dir.isDirectory()) {
                File[] dirfiles = new File(dir.getCanonicalPath()).listFiles(filter)
                if (dirfiles != null && dirfiles.size() != 0) {
                    if (!allowDups) {
                        if (!dirnames.contains(dir.getName())) {
                            dirnames.add(dir.getName())
                        }
                    } else {
                        dirnames.add(dir.getName())
                    }
                }
            }
        }
        return dirnames
    }

    def setFileType(browseFolder,allowDups) {
        def clinics = MultiClinicDataSource.list()
        def dirs = getDirs(browseFolder,allowDups)

        for(clinic in clinics) {
            System.out.println("clinic dir: ${clinic.dataSourceDirectory}")
            def dir = clinic.dataSourceDirectory
            if (dirs.count(dir) > 1) {
                //if count > 1 then we have mixed file types
                clinic.fileType = null
            } else if (!dirs.contains(dir))
                //if not in list then the dir is empty
                clinic.fileType = null
            else {
                //we have a hit so get the type
                clinic.fileType = getType(browseFolder,dir)
            }
            clinic.save()
        }

        //System.out.println("setFileType: ${dirs}")
    }

    def getType(browseFolder,dir) {
        File folder = new File(browseFolder+dir)
//        if (!folder.isDirectory()) {
//            return null
//        }

        def csvfilter = new GenericFilenameFilter(".csv")
        def txtfilter = new GenericFilenameFilter(".txt")
        def filters = []
        filters.add(["csv",csvfilter])
        filters.add(["txt",txtfilter])
        def xmlfilter = new GenericFilenameFilter(".xml")
        filters.add(["xml",xmlfilter])
		
        for(filter in filters) {
            File[] dirfiles = new File(folder.getCanonicalPath()).listFiles(filter[1])
            if (dirfiles != null && dirfiles.size() > 0) {
                //System.out.println("dir,type == ${dir},${filter[0]}")
                return filter[0]
            }
        }
    }
	
	def getSchemaLayout(String pathToSchema) throws Exception {
        if(pathToSchema != ""){
              String fileExtension = pathToSchema.substring(pathToSchema.lastIndexOf('.')+1).trim()
              if(fileExtension.equalsIgnoreCase("csv")){
                    return ParsingService.getSchemaLayoutFromPath(pathToSchema, true)
              }
              else if(fileExtension.equalsIgnoreCase("xsd")){
                    return ParsingService.getXmlDefaultSchemaLayout()                     
              }
        }
        else{
		      return ParsingService.getSchemaLayoutFromPath(pathToSchema, true)
        }
	}
	
	def recreateTablesInSchema(String pathToSchema) throws Exception {
		return ParsingService.createTablesInSchema(pathToSchema);
	}
	
	def getParsePreview(String filename, MultiClinicDataSource dataSource, schemaLayout) throws Exception {
		File schema = new File(rositaPropertiesService.get("folder.schemas") + File.separator + dataSource.schemaPath)
		File file = new File(rositaPropertiesService.get("folder.datasources") + File.separator + dataSource.dataSourceDirectory + File.separator + filename)

		String cleanFilename = file.getName().toLowerCase();
		int lastIndex = cleanFilename.lastIndexOf(".");
		if (lastIndex > 0) {
			cleanFilename = cleanFilename.substring(0, cleanFilename.lastIndexOf("."))
		}
		
		//If this is XML, just return nothing
		if (filename.endsWith(".xml")) {
			return [error:'', warning:'', table:[]]
		}
		
		//If this is a CSV or text, attempt to parse it with the clinic's parameters
		char delimiter = dataSource.delimiter ?: ','
		char quoteCharacter = dataSource.quoteCharacter ?: '"' 
		
		def tablePreview = [error:'',warning:'',table:[]]
		
		def tableLayout = schemaLayout?."${cleanFilename}"
		if (!tableLayout) {
			tablePreview.warning = 'Could not find any columns for table ' + cleanFilename + ' in file ' + dataSource.schemaPath + '. This file will not be loaded'
			return tablePreview
		}
		def columnNames = []
		def columnMap = [:]
		if (schema.exists() && schema.isFile() && schema.canRead()) {
			BufferedReader reader = null;
			try {
				CSVParser parser = new CSVParser(delimiter, quoteCharacter);
				String line;
				
				reader = new BufferedReader(new FileReader(file));
				
				
				//Build up our column map if it contains names - if not, assume in order in the CSV specification
				if (dataSource.firstRowType.equals("MAP")) {
					line = reader.readLine()
					columnNames = parser.parseLine(line.trim()) as List
				}
				else if (dataSource.firstRowType.equals("IGNORE")) {
					line = reader.readLine() //Skip first line
				}
				def defaultIndex = 0
				def schemaIndex = 0
				for (column in tableLayout) {
					//This tangle builds a map between columns declared in the schema
					//and columns that are actually defined in the CSV (if we have names - if not, it just
					//assumes the schema and CSV declare the columns in the same order)
					
					def index
					if (columnNames) {
						index = columnNames.indexOf(column.name)
						if (index == -1) {
							tablePreview.error = 'Column defined in schema but missing from data: ' + column.name + errorMessageCheckForOnlyOneColumn(columnNames)
							return tablePreview
						}
					}
					else {
						index = defaultIndex++ //Assign then increment!
					}

					columnMap.put(schemaIndex, index)
					schemaIndex++
				}
				
				//Now get column data from the table layout
				def columnData = []
				for (column in tableLayout) {
					columnData.push(column)
				}
				
				
				def table = []
				table.push(columnData)
				def lineIndex = 0
				while ((line = reader.readLine()) != null && lineIndex < 5) {
					String[] columns = parser.parseLine(line.trim());
					if (columns && columns[0]) {
						//We have our data for the columns - loop through and assign them to table columns according to the map
						def row = new String[schemaIndex]
						def columnIndex = 0
						for (mappedColumn in columnMap) {
							def translatedColumnIndex = mappedColumn.key
							def sourceColumnIndex = mappedColumn.value
							row[translatedColumnIndex] = ParsingService.maskData(columns[sourceColumnIndex])
							columnIndex++
						}
						table.push(row)
					}
					lineIndex++
				}
				tablePreview.table = table
			}
			catch (ArrayIndexOutOfBoundsException e) {
				tablePreview.error = dataSource.schemaPath + ' mentions more columns than exist in file ' + cleanFilename + '. The data file delimiter (' + delimiter + ') may be wrong.'
				e.printStackTrace()
				return tablePreview
			}
			catch (Exception e) {
				tablePreview.error = 'Could not parse table in ' + dataSource.schemaPath + errorMessageCheckForOnlyOneColumn(columnMap)
				e.printStackTrace()
				return tablePreview
			}
			finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
		else {
			throw new FileNotFoundException("Schema file '" + schema.getPath() + "' is not readable")
		}
		
		return tablePreview;
	}
	
	private errorMessageCheckForOnlyOneColumn(data) {
		//Awfully-named method to display a supplementary error message if only one column has been found
		if (1 == data?.size()) {
			return ": Only one column was found. The delimiter may be incorrect."
		}
		return ""
	}
}
