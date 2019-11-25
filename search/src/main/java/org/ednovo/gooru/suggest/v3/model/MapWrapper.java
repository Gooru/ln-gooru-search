package org.ednovo.gooru.suggest.v3.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapWrapper<T extends Object> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, T> values;

	public MapWrapper() {
		this.values = new HashMap<String, T>();
	}

	public MapWrapper(Map<String, T> map) {
		if (map != null) {
			this.values = new HashMap<String, T>(map);
		} else {
			this.values = new HashMap<String, T>();
		}
	}

	public Object getObject(String key) {
		Object result = values.get(key);
		if (result != null) {
			if (result instanceof String[] && ((String[]) result).length > 0) {
				return ((Object[]) result)[0];
			} else {
				return result;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(String key) {
		Object result = values.get(key);
		if (result != null && result instanceof List) {
			return (List<String>) result;
		}
		return null;
	}

	public String getString(String key) {
		Object result = getObject(key);
		if (result != null) {
			if (result instanceof String) {
				return (String) result;
			} else {
				return result.toString();
			}
		}
		return null;
	}

	public String getString(String key,
			String defaultValue) {
		String value = getString(key);
		return value != null ? value : defaultValue;
	}

	public Integer getInteger(String key,
			Integer defaultValue) {
		Integer value = getInteger(key);
		return value != null ? value : defaultValue;
	}

	public Integer getInteger(String key) {
		Object result = getObject(key);
		if (result != null) {
			if (result instanceof Integer) {
				return (Integer) result;
			} else {
				return Integer.parseInt(result.toString());
			}
		}
		return null;
	}

	public Boolean getBoolean(String key,
			Boolean defaultValue) {
		Boolean value = getBoolean(key);
		return value != null ? value : defaultValue;
	}

	public Boolean getBoolean(String key) {
		Object result = getObject(key);
		if (result != null) {
			if (result instanceof Boolean) {
				return (Boolean) result;
			} else {
				return Boolean.parseBoolean(result.toString());
			}
		}
		return null;
	}

	public Long getLong(String key,
			Long defaultValue) {
		Long value = getLong(key);
		return value != null ? value : defaultValue;
	}

	public Long getLong(String key) {
		Object result = getObject(key);
		if (result != null) {
			if (result instanceof Long) {
				return (Long) result;
			} else {
				return Long.parseLong(result.toString());
			}
		}
		return null;
	}

	public Map<String, T> getValues() {
		return values;
	}

	public void setValues(Map<String, T> map) {
		this.values = map;
	}

	public MapWrapper<T> put(String key,
			T value) {
		if (this.values == null) {
			this.values = new HashMap<String, T>();
		}
		this.values.put(key, value);
		return this;
	}

	public void remove(String key) {
		this.values.remove(key);
	}

	public MapWrapper<T> put(String key,
			T value,
			boolean ifKeyNotExist) {
		if (!ifKeyNotExist && this.values != null && !this.values.containsKey(key)) {
			put(key, value);
		}

		return this;
	}

	public boolean containsKey(String key) {
		return values.containsKey(key);
	}

}
