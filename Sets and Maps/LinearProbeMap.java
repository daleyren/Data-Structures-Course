/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: Implements Hashing in a Map
 * Due: 7/19/2020
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/
package renD_Project06;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LinearProbeMap<K, V> implements MyMap<K, V> {
	public static void main(String[] args) {
		LinearProbeMap<Integer, Integer> map1 = new LinearProbeMap<Integer, Integer>();
		map1.put(0, 0);
		map1.put(2, 2);
		map1.put(8, 8);
		map1.put(24, 24);

		map1.remove(8); // Test Removal
		System.out.println("HashMap contains key 8: " + map1.containsKey(8)); // Test Contains Removed Key
		System.out.println("HashMap contains key 24: " + map1.containsKey(24)); // Test Contains Key with same Modulus
		System.out.println("");

		System.out.println("HashMap contains value 8: " + map1.containsValue(8)); // Test Contains Removed Value
		System.out.println("HashMap contains value 24: " + map1.containsValue(24)); // Test Contains Value with same
																					// Modulus
		System.out.println("");

		System.out.println("Get 8 from HashMap: " + map1.get(8)); // Test Contains Removed Value
		System.out.println("Get 2 from HashMap: " + map1.get(2)); // Test Contains Value with same Modulus
		System.out.println("");

		System.out.println("HashMap Empty: " + map1.isEmpty()); //
		System.out.println("");
		
		System.out.println("HashMap Size: " + map1.size()); // Test size method on HashMap
		System.out.println("");

		System.out.println("Print of Hash Map:");
		for (int i = 0; i < map1.size(); i++) { // Prints The Hash Map
			if (map1.hashMap.get(i) == null) {
				System.out.println("null");
			} else {
				if (map1.hashMap.get(i).value == null) {
					System.out.println("X");
				} else {
					System.out.println(map1.hashMap.get(i).value); 
				}
			}
		}

		map1.put(40, 40); // Testing putting MapItem in previously removed index
		map1.put(1, 23); // Practice putting in map1
		System.out.println("");

		System.out.println("Print of Hash Map:");
		for (int i = 0; i < map1.size(); i++) { // Prints The Hash Map
			if (map1.hashMap.get(i) == null) {
				System.out.println("null");
			} else {
				if (map1.hashMap.get(i).value == null) {
					System.out.println("X");
				} else {
					System.out.println(map1.hashMap.get(i).value);
				}
			}
		}
		
		System.out.println("");
		Set<Integer> keys = map1.keySet(); //Uses keySet() method to make a set of keys from map1
		System.out.println("Keys of the Hash Map: " + keys);
		Set<Integer> values = map1.values(); //Uses valuesSet() method to make a set of values from map1
		System.out.println("Values of the Hash Map: " + values);
		Set<Entry<Integer, Integer>> entries = map1.entrySet(); //Uses entrySet() method to make a set of entries from map1
		System.out.println("");
		System.out.println("Entries of the Hash Map: ");
		for(Entry<Integer, Integer> in: entries) {
			System.out.println("(" + in.getKey() + ", " + in.getValue() + ")");
		}
	}

	class MapItem {
		public K key;
		public V value;

		MapItem(K k, V v) {
			key = k;
			value = v;
		}
	}

	public ArrayList<MapItem> hashMap;

	LinearProbeMap() {
		hashMap = new ArrayList<>(4);
		hashMap.add(null);
		hashMap.add(null);
		hashMap.add(null);
		hashMap.add(null);
	}

	static public double threshold = 0.5;
	static public int nums = 0;

	@Override
	public void clear() {
		for (int i = 0; i < hashMap.size(); i++) {
			hashMap.set(i, null);
		}
	}

	@Override
	public boolean containsKey(K key) {
		int index = ((int) key) % hashMap.size();
		while (hashMap.get(index) != null) {
			if (hashMap.get(index).key == key) {
				return true;
			}
			index += 1;
		}
		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (int i = 0; i < hashMap.size(); i++) {
			if (hashMap.get(i) != null && hashMap.get(i).value == value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Entry<K, V>> entrySet() { // Needs Implementation
		Set<Entry<K,V>> mutableSet = new HashSet<>(Arrays.asList());
		for(int i  = 0; i < hashMap.size(); i++) {
			if(hashMap.get(i) != null && hashMap.get(i).key != null) {
				Entry<K, V> newPair = new Entry<K, V>(hashMap.get(i).key, hashMap.get(i).value);
				mutableSet.add(newPair);
			}
		}
		return mutableSet;
	}

	@Override
	public V get(K key) {
		int index = ((int) key) % hashMap.size();
		while (hashMap.get(index) != null) {
			if (hashMap.get(index).key == key) {
				return hashMap.get(index).value;
			}
			index += 1;
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < hashMap.size(); i++) {
			if (hashMap.get(i) != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<K> keySet() {
		Set<K> mutableSet = new HashSet<>(Arrays.asList());
		for(int i  = 0; i < hashMap.size(); i++) {
			if(hashMap.get(i) != null && hashMap.get(i).key != null) {
				mutableSet.add(hashMap.get(i).key);
			}
		}
		return mutableSet;
	}

	@Override
	public V put(K key, V value) {
		int index = ((int) key) % hashMap.size();
		while (hashMap.get(index) != null) {
			if(hashMap.get(index).key == null) {
				break;
			}
			if (hashMap.get(index).key == key) {
				hashMap.get(index).value = value;
				return value;
			} else {
				index += 1;
			}
		}

		hashMap.set(index, new MapItem(key, value));
//		System.out.println("Size: " + hashMap.size());

		nums += 1;
		if ((double) nums / hashMap.size() >= 0.5) {
			hashMap = resize(hashMap);
		}
		return value;
	}

	@Override
	public void remove(K key) {
		int index = ((int) key) % hashMap.size();
		while (hashMap.get(index) != null) {
			if (hashMap.get(index).key == key) {
				hashMap.set(index, new MapItem(null, null));
				break;
			}
			index += 1;
		}
		nums -= 1;
	}

	@Override
	public int size() {
		return hashMap.size();
	}

	@Override
	public Set<V> values() { 
		Set<V> mutableSet = new HashSet<>(Arrays.asList());
		for(int i  = 0; i < hashMap.size(); i++) {
			if(hashMap.get(i) != null && hashMap.get(i).value != null) {
				mutableSet.add(hashMap.get(i).value);
			}
		}
		return mutableSet;
	}

	public ArrayList<MapItem> createMap(int n) {
		ArrayList<MapItem> output = new ArrayList<MapItem>(n);
		for (int i = 0; i < n; i++) {
			output.add(null);
		}
		return output;
	}

	public ArrayList<MapItem> resize(ArrayList<MapItem> oldMap) {
		ArrayList<MapItem> output = createMap(2 * oldMap.size());
		for (MapItem item : oldMap) {
			if (item != null) {
				int index = ((int) item.key) % output.size();
				while (output.get(index) != null) {
					index += 1;
				}
				output.set(index, new MapItem(item.key, item.value));
			}
		}
		return output;
	}
}

