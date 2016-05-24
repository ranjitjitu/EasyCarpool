package com.easycarpool.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class RedisWrapper {
	private static RedisHelper redis = new RedisImpl();
	
	public long insert(String key, String mapName ,Object obj) {
		return redis.put(key, mapName, obj);
	}
	public Object get(String key, String mapName) {
		return redis.get(mapName, key);
	}
	public long remove(String key, String mapName) {
		return redis.remove(mapName, key);
	}
	public boolean containKey(String key, String mapName) {
		return redis.containKey(mapName, key);
	}
	public List<Object> getAll(String mapName){
		return redis.getAll(mapName);
	}
	public Map<String, Object> getMap(String mapName){
		return redis.getMap(mapName);
	}
	public Set keySet(String mapName){
		return redis.keySet(mapName);
	}
	public void clear(String mapName){
		redis.clear(mapName);
	}
}
