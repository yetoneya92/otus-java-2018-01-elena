
package ru.otus.elena.tester.example;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Test;


public class ExampleTest {
    
    public ExampleTest(){}
    
    @Before
    public void SetUp(){}
    
    @After
    public void tearDown(){
        System.out.println("Tests have done");
    }
    
    
    @Test
    public void testSum() throws Exception {
        System.out.println("sum");
        int a = 0;
        int b = 0;
        Example instance = new Example();
        int expResult = 0;
        int result = instance.sum(a, b);
        assert result==expResult; 
        System.out.println(result);
    }
    
    @Test
    public void testSub() throws Exception {
        System.out.println("sub");
        int a = 0;
        int b = 0;
        Example instance = new Example();
        int expResult = 0;
        int result = instance.sub(a, b);
        
        System.out.println(result);
    }
    
    
    @Test
    public void testExperience() throws Exception {
        System.out.println("experience");
        int a = 0;
        int b = 0;
        Example instance = new Example();
        int expResult = 0;
        int result = instance.experience(a, b);
        System.out.println(result);
    }

}
