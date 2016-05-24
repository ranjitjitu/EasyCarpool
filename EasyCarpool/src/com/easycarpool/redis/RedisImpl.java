package com.easycarpool.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;

import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisImpl implements RedisHelper{

	private IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RedisImpl.class.getName();
	private static String redisHost;
	private static int redisPort;
	private static JedisPool pool = null; 

	static{
		redisHost = ConfigUtils.getProperty(EasyCarpoolConstants.REDIS_HOST);
		redisPort = Integer.parseInt(ConfigUtils.getProperty(EasyCarpoolConstants.REDIS_PORT));
		pool = new JedisPool(redisHost, redisPort);
	}

	public long put(String key, String mapName, Object obj){
		Jedis jedis = pool.getResource();
		try {
			Map<byte[],byte[]> retrieveMap = jedis.hgetAll(mapName.getBytes("UTF-8"));
			retrieveMap.put(key.getBytes("UTF-8"), serialize(obj));
			jedis.hmset(mapName.getBytes("UTF-8"), retrieveMap);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "put method", "Exception in put method. Message : "+e);
			return 0;
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
		return 1;
	}
	public Object get(String mapName, String key){
		Jedis jedis = pool.getResource();
		try {
			byte[] bytesObj = jedis.hget(mapName.getBytes("UTF-8"), key.getBytes("UTF-8"));
			return deserialize(bytesObj);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "put method", "Exception in put method. Message : "+e);
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
		return null;
	}
	public List<Object> getAll(String mapName) {
		ArrayList<Object> value = new ArrayList<Object>();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<byte[],byte[]> retrieveMap = jedis.hgetAll(mapName.getBytes("UTF-8"));
			for (byte[] keyMap : retrieveMap.keySet()) {
				try{
					if(!new String(keyMap,"UTF-8").contains("--ALL--")){
						byte[] tempValue = jedis.get(retrieveMap.get(keyMap));
						value.add(deserialize(tempValue));  
					}
				}catch(Exception ex){
					logger.log(Level.ERROR, CLASS_NAME, "getAll", "Exception in inner getAll method : "+ex);
				}

			}
			return value;
		}
		catch (Exception ex) {
			logger.log(Level.ERROR, CLASS_NAME, "getAll", "Exception in outer getAll method : "+ex);
		}
		finally {
			if (jedis.isConnected()) {
				jedis.close();
			}
		}
		return null;
	}
	public Map<String, Object> getMap(String mapName) {
		HashMap<String, Object> newMap = new HashMap<String, Object>();
		Jedis jedis = null;
		try {

			jedis = pool.getResource();
			Map<byte[],byte[]> retrieveMap = jedis.hgetAll(mapName.getBytes("UTF-8"));
			for (byte[] keyMap : retrieveMap.keySet()) {
				try{
					if(!new String(keyMap,"UTF-8").contains("--ALL--")){
						byte[] tempValue = jedis.get(retrieveMap.get(keyMap));
						newMap.put(new String(keyMap,"UTF-8"), deserialize(tempValue));
					}
				}catch(Exception ex){
					logger.log(Level.ERROR, CLASS_NAME, "getMap", "Exception in inner getMap method : "+ex);
				}
			}
			HashMap<String, Object> hashMap = newMap;
			return hashMap;
		}
		catch (Exception ex) {
			logger.log(Level.ERROR, CLASS_NAME, "getMap", "Exception in outer getMap method : "+ex);
		}
		finally {
			if (jedis.isConnected()) {
				jedis.close();
			}
		}
		return newMap;
	}
	public long remove(String mapName,String key){
		Jedis jedis = pool.getResource();
		try {
			return jedis.hdel(mapName.getBytes("UTF-8"),key.getBytes("UTF-8"));

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "remove method", "Exception in remove method. Message : "+e);
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
		return 0L;
	}
	public boolean containKey(String mapName,String key){
		Jedis jedis = pool.getResource();
		try {
			return jedis.hexists(mapName.getBytes("UTF-8"),key.getBytes("UTF-8"));

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "remove method", "Exception in remove method. Message : "+e);
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
		return false;
	}
	public Set keySet(String mapName) {
		Jedis jedis = null;
		try {
				jedis = pool.getResource();
			Map<String, String> retrieveMap = jedis.hgetAll(mapName);
			Set set = retrieveMap.keySet();
			return set;
		}
		catch (Exception ex) {
			logger.log(Level.ERROR, CLASS_NAME, "keySet", "Exception in keySet method. Message : "+ex);
		}
		finally {
			if (jedis.isConnected()) {
				jedis.close();
			}
		}
		return null;
	}
	public void clear(String mapName) {
		Jedis jedis = null;
		try {

			jedis = pool.getResource();
			Map<byte[], byte[]> retrieveMap = jedis.hgetAll(mapName.getBytes("UTF-8"));  
			for (byte[] keyMap : retrieveMap.keySet()) {  
				jedis.del(retrieveMap.get(keyMap)); 
			}
			jedis.del(mapName.getBytes("UTF-8"));
		}
		catch (Exception ex) {
			logger.log(Level.ERROR, CLASS_NAME, "clear", "Exception in clear method. Message : "+ex);
		}
		finally {
			if (jedis.isConnected()) {
				jedis.close();
			}
		}
	}
	private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException{
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
		return obj;
	}
	private byte[] serialize(Object obj) throws IOException{
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} finally {
			if (oos != null) {
				oos.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return bytes;
	}
}
