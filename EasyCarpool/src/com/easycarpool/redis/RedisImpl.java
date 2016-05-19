package com.easycarpool.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public void put(String key, String mapName, Object obj){
		Jedis jedis = pool.getResource();
		try {
			Map<byte[],byte[]> retrieveMap = jedis.hgetAll(mapName.getBytes("UTF-8"));
			retrieveMap.put(key.getBytes("UTF-8"), serialize(obj));
			jedis.hmset(mapName.getBytes("UTF-8"), retrieveMap);
			
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "put method", "Exception in put method. Message : "+e);
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
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
	public Map<String,Object> getAll(String mapName){
		Jedis jedis = pool.getResource();
		try {
			Map<byte[], byte[]> resultMap = jedis.hgetAll(mapName.getBytes("UTF-8"));
			
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "put method", "Exception in put method. Message : "+e);
		}finally{
			if(jedis.isConnected()){
				jedis.close();
			}
		}
		return null;
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
