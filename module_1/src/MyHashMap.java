
import java.util.Objects;

public class MyHashMap<K, V> {

    private static class Entry<K, V> {

        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry<K, V>[] table;
    private int capacity;
    private int size;
    private final float loadFactor;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Entry[capacity];
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        if ((float) size / capacity >= loadFactor) {
            resize();
        }

        int index = hash(key);
        Entry<K, V> current = table[index];

        if (current == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }

        Entry<K, V> prev = null;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Entry<>(key, value);
        size++;
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> current = oldTable[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    public int size() {
        return size;
    }

    // Тест
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        for (int i = 1; i <= 20; i++) {
            map.put("Key" + i, i);
        }

        System.out.println("Size: " + map.size()); // 20
        System.out.println("Get Key6: " + map.get("Key6")); // 10
        map.remove("Key11");
        System.out.println("Get Key11 after remove: " + map.get("Key11")); // null
    }
}
