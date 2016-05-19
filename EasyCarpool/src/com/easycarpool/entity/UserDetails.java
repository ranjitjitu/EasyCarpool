package com.easycarpool.entity;

public class UserDetails {

	private int scenarioId;
	private String scenarioName;
	private String keywords;
	private String documentType;
	private String problemSysId;
	private int categoryId;
	private String scenarioArtifactId;
	private float version;
	private String status;
	private String scenarioState;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getScenarioState() {
		return scenarioState;
	}
	public void setScenarioState(String scenarioState) {
		this.scenarioState = scenarioState;
	}

	public String getScenarioArtifactId() {
		return scenarioArtifactId;
	}
	public void setScenarioArtifactId(String scenarioArtifactId) {
		this.scenarioArtifactId = scenarioArtifactId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getProblemSysId() {
		return problemSysId;
	}
	public void setProblemSysId(String problemSysId) {
		this.problemSysId = problemSysId;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public int getScenarioId() {
		return scenarioId;
	}
	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}
	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public float getVersion() {
		return version;
	}
	public void setVersion(float version) {
		this.version = version;
	}
	
}
