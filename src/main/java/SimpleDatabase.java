public interface SimpleDatabase<T> {
    void add(String key, T value);

    T get(String key);

    boolean delete(String key);

    int count(T key);

    void begin();

    void commit();

    void rollback();
}
