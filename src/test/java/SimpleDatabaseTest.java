import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleDatabaseTest {
    private SimpleDatabaseImpl<String> db;

    @BeforeEach
    void setUp() {
        db = new SimpleDatabaseImpl<>();
    }

    @Test
    void testCase1() {
        db.add("a", "10");
        assertEquals("10", db.get("a"));
        assertTrue(db.delete("a"));
        assertNull(db.get("a"));
    }

    @Test
    void testCase2() {
        db.add("a", "10");
        db.add("b", "10");
        assertEquals(2, db.count("10"));
        assertEquals(0, db.count("20"));
        assertTrue(db.delete("a"));
        assertEquals(1, db.count("10"));
        db.add("b", "30");
        assertEquals(0, db.count("10"));
    }

    @Test
    void testCase3() {
        db.begin();
        db.add("a", "10");
        assertEquals("10", db.get("a"));
        db.begin();
        db.add("a", "20");
        assertEquals("20", db.get("a"));
        db.rollback();
        assertEquals("10", db.get("a"));
        db.rollback();
        assertNull(db.get("a"));
    }

    @Test
    void testCase4() {
        db.begin();
        db.add("a", "30");
        db.begin();
        db.add("a", "40");
        db.commit();
        assertEquals("40", db.get("a"));
        db.rollback();
        assertEquals("40", db.get("a"));
    }

    @Test
    void testCase5() {
        db.add("a", "50");
        db.begin();
        assertEquals("50", db.get("a"));
        db.add("a", "60");
        db.begin();
        assertTrue(db.delete("a"));
        assertNull(db.get("a"));
        db.rollback();
        assertEquals("60", db.get("a"));
        db.commit();
        assertEquals("60", db.get("a"));
    }

    @Test
    void testCase6() {
        db.add("a", "10");
        db.begin();
        assertEquals(1, db.count("10"));
        db.begin();
        assertTrue(db.delete("a"));
        assertEquals(0, db.count("10"));
        db.rollback();
        assertEquals(1, db.count("10"));
        db.rollback();
        assertEquals(1, db.count("10"));
    }

}