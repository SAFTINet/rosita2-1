package com.recomdata.grails.domain

import org.hibernate.sql.Delete;
import org.hsqldb.Like;

class MultiClinicDataSourceController {

    def fileService
	def rositaJobService
	def rositaPropertiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	
	def beforeInterceptor = [action:this.&checkJob, only: ['create', 'save', 'deactivate','activate', 'delete']]
	
	def checkJob = {
		if (rositaJobService.getCurrentJob()) {
			flash.message = 'A job is currently running - complete or cancel it before creating data sources or tables.'
			redirect(action: 'list')
			return false
		}
		
	}
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [multiClinicDataSourceInstanceList: MultiClinicDataSource.list(params),
                multiClinicDataSourceInstanceTotal: MultiClinicDataSource.list().size()]
    }

    def create = {
        def multiClinicDataSourceInstance = new MultiClinicDataSource()
		def thedirs = []
		def browseFolder = fileService.getBrowseFolder()
		thedirs = fileService.getDirs(browseFolder, false)
		thedirs = thedirs.sort()

        def schemasFolder = rositaPropertiesService.get('folder.schemas')
        def rulesFolder =  rositaPropertiesService.get('folder.etlrules')
		def schemaFiles = fileService.getFiles(null, schemasFolder)?.sort();
		def rulesFiles = fileService.getFiles(null, rulesFolder)?.sort();

        multiClinicDataSourceInstance.properties = params
		multiClinicDataSourceInstance.quoteCharacter = "\""
		multiClinicDataSourceInstance.delimiter = "|"
		multiClinicDataSourceInstance.active = true
        return [multiClinicDataSourceInstance: multiClinicDataSourceInstance, thedirs:thedirs, schemaFiles: schemaFiles, rulesFiles: rulesFiles, schemaFiles: schemaFiles, rulesFiles: rulesFiles]
    }

    


    def save = {
        def multiClinicDataSourceInstance = new MultiClinicDataSource(params)
        multiClinicDataSourceInstance.active = true
        if (multiClinicDataSourceInstance.save(flush: true)) {
            redirect(action: "list", id: multiClinicDataSourceInstance.id)
        }
        else {
            render(view: "create", model: [multiClinicDataSourceInstance: multiClinicDataSourceInstance])
        }
    }

    def show = {
        def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
        if (!multiClinicDataSourceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'multiClinicDataSource.label', default: 'MultiClinicDataSource'), params.id])}"
            redirect(action: "list")
        }
        else {
            [multiClinicDataSourceInstance: multiClinicDataSourceInstance]
        }
    }

    def edit = {
        def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
        def thedirs = []
        def browseFolder = fileService.getBrowseFolder()
        if (!multiClinicDataSourceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'multiClinicDataSource.label', default: 'MultiClinicDataSource'), params.id])}"
            redirect(action: "list")
        }
        else {
             thedirs = fileService.getDirs(browseFolder, false)
             thedirs = thedirs.sort()
			 def schemaFiles = fileService.getFiles(null, rositaPropertiesService.get('folder.schemas'))?.sort();
			 def rulesFiles = fileService.getFiles(null, rositaPropertiesService.get('folder.etlrules'))?.sort();
             return [multiClinicDataSourceInstance: multiClinicDataSourceInstance, thedirs:thedirs, schemaFiles: schemaFiles, rulesFiles: rulesFiles]
        }
    }

    def delete = {
       def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
       try{
           if (multiClinicDataSourceInstance){
                def ClinicStatusInstanceList = ClinicStatus.list(params)
		 	    if (!ClinicStatusInstanceList) {
	                 multiClinicDataSourceInstance.delete()
	 
		 	    }else{
	                 ClinicStatus.executeUpdate("delete ClinicStatus c where c.mcDataSource = ?", [multiClinicDataSourceInstance]);
	                 multiClinicDataSourceInstance.delete()
		 	    }
            }
       }catch(Exception e){
           render(e.getMessage())
       }
       redirect(action: "list")  
    }

    def update = {			
        def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
        if (multiClinicDataSourceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (multiClinicDataSourceInstance.version > version) {
                    
                    multiClinicDataSourceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'multiClinicDataSource.label', default: 'MultiClinicDataSource')] as Object[], "Another user has updated this MultiClinicDataSource while you were editing")
                    render(view: "edit", model: [multiClinicDataSourceInstance: multiClinicDataSourceInstance])
                    return
                }
            }
            multiClinicDataSourceInstance.properties = params
            if (!multiClinicDataSourceInstance.hasErrors() && multiClinicDataSourceInstance.save(flush: true)) {
                redirect(action: "list", id: multiClinicDataSourceInstance.id)
            }
            else {
                render(view: "edit", model: [multiClinicDataSourceInstance: multiClinicDataSourceInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'multiClinicDataSource.label', default: 'MultiClinicDataSource'), params.id])}"
            redirect(action: "list")
        }
    }

    def deactivate = {
        def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
        if (multiClinicDataSourceInstance) {
            multiClinicDataSourceInstance.active = false
            multiClinicDataSourceInstance.save(flush: true)
        }
        redirect(action: "list")
    }

    def activate = {
        def multiClinicDataSourceInstance = MultiClinicDataSource.get(params.id)
        if (multiClinicDataSourceInstance) {
            multiClinicDataSourceInstance.active = true
            multiClinicDataSourceInstance.save(flush: true)
        }
        redirect(action: "list")
    }

	def getSchemaLayout = {
		def pathToSchema = (rositaPropertiesService.get('folder.schemas') ?: '') + File.separator + params.schemaFile
		def tables = []
		try {
			tables = fileService.getSchemaLayout(pathToSchema)
			def missingColumnAlert = false
			def columnLists = tables.values()
			for (columnList in columnLists) {
				if (missingColumnAlert) { break; }
				for (column in columnList) {
					if (!column.exists) {
						missingColumnAlert = true
						break;
					}
				}
			}
			if (tables) {
				render(template: 'schemaDisplay', model: [tables: tables, missingColumnAlert: missingColumnAlert])
			}
			else {
				render("")
			}
		}
		catch (FileNotFoundException e) {
			//Just keep it quiet
			render("")
		}
		catch (Exception e) {
			if (pathToSchema.endsWith(".xsd")) {
				render("This is an XSD schema and will be handled by the CDM XSD loader.")
				//tables = fileService.getXsdSchemaLayout(params.schemaPath)
				//render(template: 'schemaDisplay', model: [tables: tables])
			}
			else {
				render(status: 500, text:"Not a valid CSV schema!")
			}
		}
	}
	
	def checkFileExists = {
		def type = params.type
		def filename = params.filename
		def path = ''
		if (type.equals("schema")) {
			path = rositaPropertiesService.get('folder.schemas') ?: ''
		}
		else if (type.equals("etlrules")) {
			path = rositaPropertiesService.get('folder.etlrules') ?: ''
		}
		
		path = path + File.separator + filename
		
		try {
			File file = new File(path)
			
			if (file.exists() && file.isFile()) {
				render("")
			}
			else {
				render("Not found")
			}
		}
		catch (Exception e) {
			render(e.getMessage())
		}
	}
	
	def recreateTables = {
		def id = params.id
		if (rositaJobService.getCurrentJob()) {
			render(status: 500, text: "A job is currently running - please cancel or complete it before modifying the tables.")
			return
		}
		MultiClinicDataSource dataSource = MultiClinicDataSource.get(id)
		if (!dataSource) {
			def path = params.path;
			if (!path) {
				render (status: 500, text: 'Data source not found')
				return
			}
			else {
				dataSource = [schemaPath: path];
			}
		}
		try {
			fileService.recreateTablesInSchema(rositaPropertiesService.get('folder.schemas') + File.separator + dataSource.schemaPath)
		}
		catch (Exception e) {
			render(status: 500, text: e.getClass().getName() + ": " + e.getMessage())
			return;
		}
		redirect(action: getSchemaLayout, params: [schemaFile: dataSource.schemaPath])
	}
}
