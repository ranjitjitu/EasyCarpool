
package com.easycarpool.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.entity.UserDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;

/**
 * @author ranjit_behura
 *
 */
public class UserDetailsDaoImpl implements UserDetailsDao{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = UserDetailsDaoImpl.class.getName();

	/**
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	//	public ScenarioDaoImpl() {
	//	}
	//	public ScenarioDaoImpl(DataSource dataSource,JdbcTemplate jdbcTemplate) {
	//		this.dataSource=dataSource;
	//		this.jdbcTemplate=jdbcTemplate;
	//	}
	@Override
	public String insert(UserDetails scenario) {
		String sql = "INSERT INTO scenario " +
				"(scenarioName,keywords,documentType,problemSysId,categoryId,scenarioArtifactId,version) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, new Object[] { scenario.getScenarioName(),
					scenario.getKeywords(),scenario.getDocumentType(),scenario.getProblemSysId(),scenario.getCategoryId(),scenario.getScenarioArtifactId(),scenario.getVersion()});

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value into ScenarioMapping : "+e.getMessage());
			return "scenario insertion failed";
		} finally {
		}
		return "scenario inserted successfully";
	}

	@Override
	public int update(UserDetails scenario) {
		String sql = "UPDATE scenario " +
				"SET scenarioName=?, keywords=?,documentType=?, problemSysId=?,categoryId=?, scenarioArtifactId=?, version=? where scenarioId=?";
		try {
			jdbcTemplate.update(sql, new Object[] { scenario.getScenarioName(),
					scenario.getKeywords(),scenario.getDocumentType(),scenario.getProblemSysId(),scenario.getCategoryId(),scenario.getScenarioArtifactId(),scenario.getVersion(),scenario.getScenarioId()});

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "update", "Exception thrown while updating value into ScenarioMapping : "+e.getMessage());
			return 0;
		} finally {
		}
		return scenario.getScenarioId();
	}

	public String updateScenarioArtifactMapping(String newArtifactIdCreated, String tempArtifactId) {		
		String sql = "UPDATE scenario " +
				"SET scenarioArtifactId=? where scenarioArtifactId=?";
		try {
			jdbcTemplate.update(sql, new Object[] { newArtifactIdCreated, tempArtifactId});

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "updateScenarioArtifactMapping", "Scenario updation failed. Message : "+e.getMessage());
			return "scenario updation failed";
		} finally {
		}
		return "scenario updated successfully";
	}

	@Override
	public UserDetails findScenarioByScenarioId(int scenarioId) {
		String sql = "select * from scenario where scenarioId=? and status='Active'";
		UserDetails sc =  new UserDetails();
		try {
			sc=(UserDetails)jdbcTemplate.queryForObject(sql, new Object[] { scenarioId }, new BeanPropertyRowMapper<>(UserDetails.class));
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "findScenarioByScenarioName", "Exception thrown while getting scenario : "+e.getMessage());

		} finally {
		}
		return sc;
	}
	@Override
	public UserDetails findScenarioByScenarioName(String scenarioName,float version) {
		String sql = "select * from scenario where LOWER(scenarioName)=LOWER(?) and version=? and status='Active'";
		UserDetails sc =  new UserDetails();
		try {
			sc=(UserDetails)jdbcTemplate.queryForObject(sql, new Object[] { scenarioName,version }, new BeanPropertyRowMapper<>(UserDetails.class));
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "findScenarioByScenarioName", "Exception thrown while getting scenario : "+e.getMessage());

		} finally {
		}
		return sc;
	}

	public UserDetails findScenarioByScenarioName(String scenarioName) {
		String sql = "select * from scenario where LOWER(scenarioName)=LOWER(?) and status='Active'";
		UserDetails sc=new UserDetails();
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,scenarioName);			
			if(rows!=null && !rows.isEmpty()){
				Map row = rows.get(0);
				sc.setScenarioId(Integer.parseInt(String.valueOf(row.get("scenarioId"))));
				sc.setKeywords((String)row.get("keywords"));
				sc.setScenarioName((String)row.get("scenarioName"));
				sc.setProblemSysId((String)row.get("problemSysId"));
				sc.setCategoryId(Integer.parseInt(String.valueOf(row.get("categoryId"))));
				sc.setVersion((float)row.get("version"));
				sc.setScenarioState(((String)row.get("scenarioState")));
			}			
		}
		catch (Exception e) {
			//e.printStackTrace();
			logger.log(Level.ERROR, CLASS_NAME, "getAllScenarios", "Exception thrown while getting all the scenario : "+e.getMessage());

		} finally {
		}
		return sc;
	}

	@Override
	public List<UserDetails> getAllScenarios() {
		String sql = "select * from scenario where status='Active'";
		List<UserDetails> scenarioList=new ArrayList<UserDetails>();
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map row : rows) {
				UserDetails sc=new UserDetails();
				sc.setScenarioId(Integer.parseInt(String.valueOf(row.get("scenarioId"))));
				sc.setKeywords((String)row.get("keywords"));
				sc.setScenarioName((String)row.get("scenarioName"));
				sc.setProblemSysId((String)row.get("problemSysId"));
				sc.setCategoryId(Integer.parseInt(String.valueOf(row.get("categoryId"))));
				sc.setVersion((float)row.get("version"));
				sc.setScenarioState(((String)row.get("scenarioState")));
				scenarioList.add(sc);
			}
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getAllScenarios", "Exception thrown while getting all the scenario : "+e.getMessage());

		} finally {

		}
		return scenarioList;
	}
	@Override
	public List<UserDetails> getAllPublishedScenarios() {
		String sql = "select * from scenario where scenarioArtifactId in (select artifact_Id from artifactdenorm where document_type='DYNAMIC SCENARIO' and status_description='Published') and status='Active'";
		List<UserDetails> scenarioList=new ArrayList<UserDetails>();
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map row : rows) {
				UserDetails sc=new UserDetails();
				sc.setScenarioId(Integer.parseInt(String.valueOf(row.get("scenarioId"))));
				sc.setKeywords((String)row.get("keywords"));
				sc.setScenarioName((String)row.get("scenarioName"));
				sc.setProblemSysId((String)row.get("problemSysId"));
				sc.setCategoryId(Integer.parseInt(String.valueOf(row.get("categoryId"))));
				sc.setVersion((float)row.get("version"));
				sc.setScenarioState(String.valueOf(row.get("scenarioState")));
				scenarioList.add(sc);
			}
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getAllScenarios", "Exception thrown while getting all the scenario : "+e.getMessage());

		} finally {

		}
		return scenarioList;
	}
	@Override
	public String delete(int scenarioId) {
		String delParamMapping="update inputparammapping set inputMappingStatus='Deleted' where scenarioId=?";
		String delServiceMapping="update servicemapping set serviceStatus='Deleted' where scenarioId=?";
		String delScenarioMapping = "update scenariomapping set questionStatus='Deleted' where scenarioId=?";
		String delScenario = "update scenario set status='Deleted' where scenarioId=?";
		try {
			jdbcTemplate.update(delParamMapping, scenarioId);
			jdbcTemplate.update(delServiceMapping, scenarioId);
			jdbcTemplate.update(delScenarioMapping, scenarioId);
			jdbcTemplate.update(delScenario, scenarioId);
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "delete", "Exception thrown while getting all the scenario : "+e.getMessage());
			return "scenario deletion failed";

		} finally {

		}
		return "scenario deleted succesfully";

	}

	@Override
	public UserDetails findScenarioByArtifactId(String artifactId, String version) {

		String sql = "select * from scenario where scenarioArtifactId=? and VERSION=? and status='Active'";
		UserDetails sc =  new UserDetails();
		try {
			sc=(UserDetails)jdbcTemplate.queryForObject(sql, new Object[] { artifactId,Float.parseFloat(version) }, new BeanPropertyRowMapper<>(UserDetails.class));
		}
		catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "findScenarioByArtifactId", "Exception thrown while getting scenario : "+e.getMessage());
			//e.printStackTrace();
		} finally {
		}
		return sc;
	}

	public String saveStaticQuestion(int questionId, String staticQuestion) {
		String updateQuery = "UPDATE scenariomapping " +
				"SET staticQuestionCombination=? where questionId=?";
		try {
			int insertCount = jdbcTemplate.update(updateQuery, new Object[] { staticQuestion,questionId});
			if(insertCount>0){
				return "Static Question Saved Successfully";
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "saveStaticQuestion", "Exception thrown while inserting value into staticquestion : "+e.getMessage());
			return "Static Question Insertion Failed";
		} finally {
		}
		return "Static Question Insertion Failed";
	}
	public void copyNode(int questionId,int scenarioId,ArrayList<Integer> quesIdList){
		String selectMapping = "select questionId from scenariomapping where dependentQuestionId="+questionId+" and scenarioId="+scenarioId+" and questionStatus='Active'";
		List<Integer> rows = jdbcTemplate.queryForList(selectMapping,Integer.class);
		for (int i = 0; i < rows.size(); i++) {
			int quesId=rows.get(i);
			quesIdList.add(quesId);
			copyNode(quesId, scenarioId,quesIdList);
		}
	}

	
}
