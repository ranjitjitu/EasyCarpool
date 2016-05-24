package com.easycarpool.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisHelper {
	
	public long put(String key, String mapName, Object obj);
	public Object get(String mapName, String key);
	public List<Object> getAll(String mapName);
	public long remove(String mapName,String key);
	public boolean containKey(String mapName,String key);
	public Map<String, Object> getMap(String mapName);
	public Set keySet(String mapName);
	public void clear(String mapName);
}
