/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.myarraylist;


import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;






public class MyArrayList<E> implements List<E>{
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    int lastElementNumber=-1;
    private int size;
    private E[]elements;
    protected transient int modCount = 0;

    public MyArrayList() {       
        elements = (E[]) new Object[0];
    }

    public MyArrayList(int initialCapacity) {
        elements = (E[]) new Object[initialCapacity];
        size = initialCapacity;
    }

    public MyArrayList(Collection<? extends E> coll) {
        elements = (E[]) coll.toArray();
        size = elements.length;
        lastElementNumber = size - 1;
    }

    @Override
    public E[] toArray() {        
        return Arrays.copyOf(elements, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);

    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }
    
    @Override
        public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        lastElementNumber = -1;
    }

    @Override
    public E get(int index) {
        if (elements.length == 0) {
            return null;
        }
        if (!checkIndex(index)) {
            return null;
        }
        return elements[index];
    }

    @Override
    public E set(int index, E element) {

        if (!checkIndex(index)) {
            if (index >= size) {
                elements = Arrays.copyOf(elements, index + 1);
                lastElementNumber = index;
            } else {
                throw new IndexOutOfBoundsException("wrong index");
            }
        }
        if (elements.length == 1) {
            elements[index] = element;
            return null;
        }
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public boolean add(E e) {
        lastElementNumber += 1;
        if (lastElementNumber >= MAX_ARRAY_SIZE) {
            return false;
        } else if (lastElementNumber == size) {
            modCount++;
            size += 1;
            elements = Arrays.copyOf(elements, size);
        }
        elements[lastElementNumber] = e;        
        //System.out.println(Arrays.toString(elements));

        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index > size) {
            elements = Arrays.copyOf(elements, index + 1);
            lastElementNumber = index;
        } else if (index < 0) {
            index=0;
            add(index, element);
            return;
        } else {
            if (lastElementNumber == (size - 1)) {
                size += 1;
                modCount++;
                elements = Arrays.copyOf(elements, size);
            }
            for (int i = size - 1; i > index; i--) {
                elements[i] = elements[i - 1];
            }
            lastElementNumber += 1;
        }
        
        elements[index] = element;
        
    }

    @Override
    public boolean addAll(Collection<? extends E> coll) {
        if (coll == null) {
            return false;
        }
        int len = coll.size();
        int temp = lastElementNumber;
        lastElementNumber += len;
        if (lastElementNumber >= size) {

            size = lastElementNumber + 1;
            if (size > MAX_ARRAY_SIZE) {
                return false;
            }
            modCount++;
            elements = Arrays.copyOf(elements, size);
        }
        E[] e = (E[]) coll.toArray();
        
        System.arraycopy(e, 0, elements, temp + 1, len);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> coll) {
        if (coll == null) {
            return false;
        }
        if (index < 0) {
            index = 0;
            return addAll(index, coll);                       
        } 
        int len = coll.size();
        if (index + len >= MAX_ARRAY_SIZE - 1 || lastElementNumber + len >= MAX_ARRAY_SIZE - 1) {
            return false;
        }
        E[] e = (E[]) coll.toArray();
        int temp=lastElementNumber;
        if (lastElementNumber < index) {
            if (size < index + len) {
                size = index + len;
                modCount++;
                elements = Arrays.copyOf(elements, size);
            }
            lastElementNumber = index + len - 1;            

        } else {
            if (size <= lastElementNumber + len) {
                size = lastElementNumber + len + 1;
                modCount++;
                elements = Arrays.copyOf(elements, size);
                lastElementNumber=size-1;
            }
            System.arraycopy(elements, index, elements, index + len, temp - index + 1);

        }
        System.arraycopy(e, 0, elements, index, len);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (elements.length == 0) {
            return false;
        }
        if (o == null) {
            for (int index = 0; index < size; index++) {
                if (elements[index] == null) {
                    removeElement(index);
                    return true;
                }
            }
        } else {
            for (int index = 0; index < size; index++) {
                if (o.equals(elements[index])) {
                    removeElement(index);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E remove(int index) {
        if (elements.length == 0) {
            return null;
        }
        if (!checkIndex(index)) {
            return null;
        }
        E oldValue = elements[index];
        removeElement(index);        
        return oldValue;
    }

    private void removeElement(int index) {
        for (int i = index; i < size - 1; i++) {            
            elements[i] = elements[i + 1];           
        }
        size -= 1;
        if (lastElementNumber >= index) {
            lastElementNumber -= 1;
        }
        modCount++;
        elements = Arrays.copyOfRange(elements, 0, size);
    }

    @Override
    public boolean removeAll(Collection<?> coll) {
        if (coll == null) {
            return false;
        }
        if (elements.length == 0) {
            return false;
        }
        int counter = 0;
        for (Object o : elements) {
            if (!coll.contains(o)) {
                elements[counter] = (E) o;
                counter++;
            }
        }
        size=counter;        
        lastElementNumber=counter-1;
        modCount++;
        elements=Arrays.copyOf(elements,size);      
        return true;
    }    

    @Override
    public boolean retainAll(Collection<?> coll) {
        if (coll == null) {
            return false;
        }
        if (elements.length == 0) {
            return false;
        }
        int counter = 0;
        for (Object o : elements) {
            if (coll.contains(o)) {
                elements[counter] = (E) o;
                counter++;
            }
        }
        size=counter;        
        lastElementNumber=counter-1;
        modCount++;
        elements=Arrays.copyOf(elements,size);      
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (elements.length == 0) {
            return false;
        }
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> coll) {
        if (coll == null) {
            return false;
        }
        if (elements.length == 0) {
            return false;
        }
        for (Object e : coll) {
            if (!this.contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int indexOf(Object o) {
        if (elements.length == 0) {
            return -1;
        }
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (elements.length == 0) {
            return -1;
        }
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public MyArrayList<E> subList(int fromIndex, int toIndex) {
        if (elements.length == 0) {
            return null;
        }
        if (!(checkIndex(fromIndex) && checkIndex(toIndex) && (fromIndex < toIndex))) {
            return null;
        }
        E[] sub = (E[]) new Object[toIndex - fromIndex];
        System.arraycopy(elements, fromIndex, sub, 0, toIndex - fromIndex);
        return new MyArrayList(Arrays.asList(sub));
    }

    private boolean checkIndex(int index) {
        if (index >= size || index < 0) {
            return false;
        }
        return true;
    }



    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor=-1;
            int expectedModCount = modCount;
            @Override
            public boolean hasNext() {
                return cursor < lastElementNumber;
            }

            @Override
            public E next() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if (cursor >= lastElementNumber) {
                    throw new IndexOutOfBoundsException();
                }
                cursor++;
                return elements[cursor];
            }

            public void remove() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                try {
                    MyArrayList.this.remove(cursor);
                    cursor--;
                    expectedModCount++;
                } catch (IndexOutOfBoundsException ex) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(-1);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {

        int cursor;
        int removed;
        int prev;
        int expectedModCount;
        ListItr(int index) {
            if (index < -1) {
                index = -1;
            }
            if (index > lastElementNumber) {
                index = lastElementNumber+1;
            }
            cursor = index;
            expectedModCount = modCount;
        }
        @Override
        public boolean hasNext() {            
            return cursor - removed < lastElementNumber;            
        }

        @Override
        public E next() {
             if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            cursor -= removed;
            removed = 0;
            prev=1;
            if (cursor >=lastElementNumber ) {
                throw new IndexOutOfBoundsException();
            }            
            return elements[++cursor];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor < 1) {
                throw new IndexOutOfBoundsException();
            }
            cursor+=prev;
            prev=0;
            return elements[--cursor];
        }

           @Override
           public int nextIndex() {
             return cursor+1; 
           }

        @Override
        public int previousIndex() {
            return cursor;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            try {
                MyArrayList.this.remove(cursor);
                    removed=1;
                    expectedModCount++;
                } catch (IndexOutOfBoundsException ex) {
                    throw new ConcurrentModificationException();
                }
            }

        @Override
        public void set(E e) {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            elements[cursor] = e;
        }

        @Override
        public void add(E e) {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            MyArrayList.this.add(cursor, e);
            expectedModCount++;
        }
    }



    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        List.super.replaceAll(operator); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sort(Comparator<? super E> c) {
        List.super.sort(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spliterator<E> spliterator() {
        return List.super.spliterator(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return List.super.removeIf(filter); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stream<E> stream() {
        return List.super.stream(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stream<E> parallelStream() {
        return List.super.parallelStream(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        List.super.forEach(action); 
    }


    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

}
