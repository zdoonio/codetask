public class ProgramRunner {
    public static void main(String[] args) {
        SimpleDatabaseImpl<String> db = new SimpleDatabaseImpl<>();
        db.add("a", "10");
        db.add("b", "10");

        System.out.println(db.get("a")); // Output: 10
        System.out.println(db.count("10")); // Output: 2

        db.begin();
        db.add("c", "10");
        System.out.println(db.count("10")); // Output: 3

        db.rollback();
        System.out.println(db.count("10")); // Output: 2

        db.begin();
        db.add("c", "10");
        db.commit();
        System.out.println(db.count("10")); // Output: 3

        db.rollback(); // Output: No active transaction to rollback
    }
}
