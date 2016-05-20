package com.easycarpool.redis;

import java.util.Map;

public interface RedisHelper {
	
	public void put(String key, String mapName, Object obj);
	public Object get(String mapName, String key);
	public Map<String,Object> getAll(String mapName);
	public void remove(String mapName,String key);
	public boolean containKey(String mapName,String key);
}
