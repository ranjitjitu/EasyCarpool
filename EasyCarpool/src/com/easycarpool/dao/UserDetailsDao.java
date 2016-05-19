package com.easycarpool.dao;

import java.util.List;

import com.easycarpool.entity.UserDetails;

public interface UserDetailsDao {

	/**
	 * @param scenario
	 * @return
	 */
	public String insert(UserDetails scenario);

	/**
	 * @param scenario
	 * @return
	 */
	public int update(UserDetails scenario);

	/**
	 * @param scenarioId
	 * @return
	 */
	public String delete(int scenarioId);

	/**
	 * @param scenarioId
	 * @return
	 */
	public UserDetails findScenarioByScenarioId(int scenarioId);
	/**
	 * @param scenarioName
	 * @return
	 */
	public UserDetails findScenarioByScenarioName(String scenarioName,float version);

	/**
	 * @param artifactId
	 * @return
	 */
	public UserDetails findScenarioByArtifactId(String artifactId, String version);

	/**
	 * @return
	 */
	public List<UserDetails> getAllScenarios();
	
	public List<UserDetails> getAllPublishedScenarios();
	/**
	 * @param newArtifactIdCreated
	 * @param tempArtifactId
	 * @return
	 */
}
