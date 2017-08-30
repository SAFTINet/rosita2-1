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

/*******************************************************************************
 * Copyright(c) 2010-2011 Recombinant Data Corp., All rights Reserved
 ******************************************************************************/

package com.rdc.domain;

/**
 * placeholder for storing standard CRUD permissions for a particular domain
 * @author JSpencer
 */
public class DomainPermissionDetails {

	private boolean canCreate = false;
	private boolean canRead = false;
	private boolean canUpdate = false;
	private boolean canDelete = false;
	private boolean canSpecialUpdate = false;
	
	public DomainPermissionDetails() {
		super();
	}

	public DomainPermissionDetails(boolean canCreate, boolean canRead, boolean canUpdate,
			boolean canDelete, boolean canSpecialUpdate) {
		super();
		this.canCreate = canCreate;
		this.canRead = canRead;
		this.canUpdate = canUpdate;
		this.canDelete = canDelete;
		this.canSpecialUpdate = canSpecialUpdate;
	}

	public boolean getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}

	public boolean getCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean getCanSpecialUpdate() {
		return canSpecialUpdate;
	}

	public void setCanSpecialUpdate(boolean canSpecialUpdate) {
		this.canSpecialUpdate = canSpecialUpdate;
	}

	public boolean getCanRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	
}
