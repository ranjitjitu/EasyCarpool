
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

import javax.servlet.http.HttpServletRequest;
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
import com.easycarpool.redis.RedisHelper;
import com.easycarpool.redis.RedisImpl;
import com.easycarpool.redis.RedisWrapper;
import com.google.gson.Gson;

/**
 * @author ranjit_behura
 *
 */
public class UserDetailsDaoImpl implements UserDetailsDao{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = UserDetailsDaoImpl.class.getName();
	private static final String mapName = "ec_userDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();

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
	public String insert(HttpServletRequest request) {
		try {
			UserDetails user = new UserDetails();
			user.setUsername(request.getParameter("username"));
			user.setCompany(request.getParameter("company"));
			user.setEmail(request.getParameter("email"));
			user.setGender(request.getParameter("gender"));
			user.setAge(Integer.parseInt(request.getParameter("age")));
			redisWrapper.insert(user.getUsername(), mapName, user);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value for userDetails : "+e.getMessage());
			return "scenario insertion failed";
		} finally {
		}
		return "scenario inserted successfully";
	}

	@Override
	public String get(HttpServletRequest request) {
		UserDetails user = null;
		try {
			String username = request.getParameter("username");
			user = (UserDetails)redisWrapper.get(username, mapName);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "get", "Exception thrown while get value for userDetails : "+e.getMessage());
		} finally {
		}
		return new Gson().toJson(user);
	}
	
}
