
package ru.otus.elena.tester.main;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ZL
 */
public class TesterTest {
    
    public TesterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testDoWork() {
        System.out.println("doWork");
        Class clazz = null;       
        ArrayList<String> result = Tester.doWork(clazz);
        System.out.println(result);
        
        
    }
    
}
