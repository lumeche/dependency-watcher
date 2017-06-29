package com.nuance.mobility.dependencywatcher.data;

public class Artifact {
	private String groupId="";
	private String id="";
	private String version="";
	private String scm="";
	
	public Artifact(){
		
	}
	
	public Artifact(String groupId, String id, String version) {
		super();
		this.groupId = groupId;
		this.id = id;
		this.version = version;
	
	}
	
	public Artifact(String groupId, String id, String version, String scm) {
		super();
		this.groupId = groupId;
		this.id = id;
		this.version = version;
		this.scm = scm;
	}

	Artifact(String id){
		this.id=id;
	}
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScm() {
		return scm;
	}

	public void setScm(String scm) {
		this.scm = scm;
	}

	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((scm == null) ? 0 : scm.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artifact other = (Artifact) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (scm == null) {
			if (other.scm != null)
				return false;
		} else if (!scm.equals(other.scm))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	
	
}
