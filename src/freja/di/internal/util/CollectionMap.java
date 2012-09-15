package freja.di.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SiroKuro
 */
public class CollectionMap<K, V> {
	private Map<K, Collection<V>> map = newMap();
	
	public void clear() {
		map.clear();
	}
	
	public void remove(Object key) {
		map.remove(key);
	}
	
	public Collection<V> get(Object key) {
		Collection<V> result = map.get(key);
		if (result != null) {
			return newCollection(result);
		}
		else {
			return newCollection();
		}
	}
	
	public void put(K key, V value) {
		Collection<V> collection = map.get(key);
		if (collection == null) {
			collection = newCollection();
			map.put(key, collection);
		}
		collection.add(value);
	}
	
	protected Collection<V> newCollection() {
		return new ArrayList<V>();
	}
	
	protected Collection<V> newCollection(Collection<? extends V> c) {
		return new ArrayList<V>(c);
	}
	
	protected Map<K, Collection<V>> newMap() {
		return new HashMap<K, Collection<V>>();
	}
}
