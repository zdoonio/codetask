import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SimpleDatabaseImpl<T> implements SimpleDatabase<T> {
    private final Map<String, T> collection = new HashMap<>();
    private final Map<T, Integer> countStack = new HashMap<>();
    private final Stack<Map<String, T>> transactionStack = new Stack<>();
    private boolean transactionActive = false;

    @Override
    public void add(String key, T value) {
        if (transactionActive) {
            Map<String, T> transactionMap = transactionStack.peek();
                transactionMap.put(key, collection.get(key));
        }
        Integer currentCountStack = countStack.get(value);
        if(currentCountStack != null) {
            countStack.put(value, ++currentCountStack);
        } else {
            countStack.put(value, 1);
        }

        collection.put(key, value);
    }

    @Override
    public T get(String key) {
        return collection.get(key);
    }

    @Override
    public boolean delete(String key) {
        if (transactionActive) {
            Map<String, T> transactionMap = transactionStack.peek();
            transactionMap.put(key, null);
            add(key, null);
                return true;
                //return transactionMap.remove(key) != null;
        }
        return collection.remove(key) != null;
    }

    @Override
    public int count(T value) {
        int count = 0;
        for (T val : collection.values()) {
            if (val != null && val.equals(value)) {
                count++;
            }
        }
        return count;
    }

    public int countHash(T value) {
        countStack.putIfAbsent(value, 1);
        return countStack.get(value);
    }

    @Override
    public void begin() {
        if (transactionActive) {
            transactionStack.push(new HashMap<>());
        } else {
            transactionActive = true;
            transactionStack.push(new HashMap<>());
        }
    }

    @Override
    public void commit() {
        if (!transactionActive) {
            System.out.println("No active transaction to commit");
            return;
        }

        updateCollection();

        if (transactionStack.isEmpty()) {
            transactionActive = false;
        }

    }

    @Override
    public void rollback() {
        if (!transactionActive) {
            System.out.println("No active transaction to rollback");
            return;
        }

        updateCollection();

        if (transactionStack.isEmpty()) {
            transactionActive = false;
        }

    }

    private void updateCollection() {
        Map<String, T> transactionMap = transactionStack.pop();

        for (Map.Entry<String, T> entry : transactionMap.entrySet()) {
            if (entry.getValue() == null) {
                collection.remove(entry.getKey());
            } else {
                collection.put(entry.getKey(), entry.getValue());
            }
        }
    }
}

