/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.myarraylist;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
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
public class MyArrayListTest {
    
    public MyArrayListTest() {
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
    public void testToArray_0args() {
        System.out.println("toArray");
        MyArrayList instance = new MyArrayList();
        Object[] expResult = new Object[0];
        Object[] result = instance.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }

    @Test
    public void testSize() {
        System.out.println("size");
        MyArrayList instance = new MyArrayList();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        MyArrayList instance = new MyArrayList();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    @Test
    public void testClear() {
        System.out.println("clear");
        MyArrayList instance = new MyArrayList();
        instance.clear();
    }

    @Test
    public void testGet() {
        System.out.println("get");
        int index = 0;
        MyArrayList instance = new MyArrayList();
        Object expResult = null;
        Object result = instance.get(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testSet() {
        System.out.println("set");
        int index = 0;
        Object element = null;
        MyArrayList instance = new MyArrayList();
        Object expResult = null;
        Object result = instance.set(index, element);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAdd_GenericType() {
        System.out.println("add");
        Object e = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = true;
        boolean result = instance.add(e);
        assertEquals(expResult, result);

    }

    @Test
    public void testAdd_int_GenericType() {
        System.out.println("add");
        int index = 0;
        Object element = null;
        MyArrayList instance = new MyArrayList();
        instance.add(index, element);
    }

    @Test
    public void testAddAll_Collection() {
        System.out.println("addAll");
        Collection coll = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(coll);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddAll_int_Collection() {
        System.out.println("addAll");
        int index = 0;
        Collection coll = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.addAll(index, coll);
        assertEquals(expResult, result);
    }

    @Test
    public void testRemove_Object() {
        System.out.println("remove");
        Object o = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.remove(o);
        assertEquals(expResult, result);

    }

    @Test
    public void testRemove_int() {
        System.out.println("remove");
        int index = 0;
        MyArrayList instance = new MyArrayList();
        Object expResult = null;
        Object result = instance.remove(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        Collection coll = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.removeAll(coll);
        assertEquals(expResult, result);

    }
    
    @Test
    public void testRetainAll() {
        System.out.println("retainAll");
        Collection coll = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.retainAll(coll);
        assertEquals(expResult, result);

    }

    @Test
    public void testContains() {
        System.out.println("contains");
        Object o = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.contains(o);
        assertEquals(expResult, result);

    }

    @Test
    public void testContainsAll() {
        System.out.println("containsAll");
        Collection coll = null;
        MyArrayList instance = new MyArrayList();
        boolean expResult = false;
        boolean result = instance.containsAll(coll);
        assertEquals(expResult, result);

    }

    @Test
    public void testIndexOf() {
        System.out.println("indexOf");
        Object o = null;
        MyArrayList instance = new MyArrayList();
        int expResult = -1;
        int result = instance.indexOf(o);
        assertEquals(expResult, result);

    }

    @Test
    public void testLastIndexOf() {
        System.out.println("lastIndexOf");
        Object o = null;
        MyArrayList instance = new MyArrayList();
        int expResult = -1;
        int result = instance.lastIndexOf(o);
        assertEquals(expResult, result);

    }

    @Test
    public void testSubList() {
        System.out.println("subList");
        int fromIndex = 0;
        int toIndex = 0;
        MyArrayList instance = new MyArrayList();
        MyArrayList expResult = null;
        MyArrayList result = instance.subList(fromIndex, toIndex);
        assertEquals(expResult, result);

    }

    @Test
    public void testIterator() {
        System.out.println("iterator");
        MyArrayList instance = new MyArrayList();       
        Iterator result = instance.iterator();
        
 
    }

    @Test
    public void testListIterator_0args() {
        System.out.println("listIterator");
        MyArrayList instance = new MyArrayList();
        ListIterator result = instance.listIterator();        
  
    }

    @Test
    public void testListIterator_int() {
        System.out.println("listIterator");
        int index = 0;
        MyArrayList instance = new MyArrayList();
        ListIterator result = instance.listIterator(index);

    }
   
}
