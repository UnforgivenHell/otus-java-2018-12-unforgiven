package ru.otus.hw031;

import java.util.*;

public class MyArrayList<T>  implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private T[] array;

    public MyArrayList() {
        setEmptyArray();
    }

    private MyArrayList(T[] input, int size) {
        this.array = Arrays.copyOf(input, size);
        this.size = size;
    }

    @Override
    public void clear() {
        setEmptyArray();
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T set(int index, T element) {
        throwIsIncorrectRange(index);
        array[index] = element;
        return array[index];
    }

    @Override
    public T get(int index) {
        if (isCorrectRange(index)) {
            return array[index];
        }
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = this.size - 1; i >= 0; i--) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List<T> subList (int fromIndex, int toIndex){
        T[] subarray = Arrays.copyOfRange(array, fromIndex, toIndex);
        return new MyArrayList<T>(subarray, subarray.length);
    }

    @Override
    public ListIterator<T> listIterator(){
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index){
        return new ListItr(index);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o){
        int i = 0;
        while (i < this.size) {
            if (this.array[i] == o) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public T remove(int index) {
        if (isCorrectRange(index)) {
            T element = get(index);
            System.arraycopy(this.array, index + 1, this.array, index, this.size - 1 - index);
            this.size--;
            return element;
        } else {
            return null;
        }
    }

    @Override
    public void add(int index, T element) {
        throwIsIncorrectRange(index);
        System.arraycopy(this.array, index, this.array, index + 1, this.size - index);
        increaseArraySize();
        this.array[index] = element;
        this.size++;
    }

    @Override
    public boolean add(T t) {
        increaseArraySize();
        this.size++;
        this.array[this.size - 1] = t;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (isCorrectRange(index)) {
            T[] arrayL = (T[]) new Object[this.size + c.size()];
            System.arraycopy(this.array, 0, arrayL, 0, index);
            int i = 0;
            for (Object aC : c) {
                arrayL[index + i++] = (T) aC;
            }
            for (i = index; i < this.size; i++) {
                arrayL[c.size() + i] =this.array[i];
            }
            this.size += c.size();
            this.array = arrayL;
            return true;
        } else {
            return false;
        }
    }
    public boolean addAll(Collection<? super T> c, T... elements) {
        T[] arrayL = (T[]) new Object[this.size + c.size() + elements.length];
        System.arraycopy(this.array, 0, arrayL, 0, this.size);
        //increaseArraySize();
        int i = 0;
        for (Object aC : c) {
            arrayL[this.size + i++] = (T) aC;
        }
        for (T el: elements)  {
            arrayL[this.size + i++] = el;
        }
        this.array = arrayL;
        this.size += c.size() + elements.length;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        T[] arrayL = (T[]) new Object[this.size + c.size()];
        System.arraycopy(this.array, 0, arrayL, 0, this.size);
        //increaseArraySize();
        int i = 0;
        for (Object aC : c) {
            arrayL[this.size + i++] = (T) aC;
        }
        this.array = arrayL;
        this.size += c.size();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removeAll = true;
        Object[] addArray = c.toArray();
        for (Object element : addArray) {
            removeAll &= remove(element);
        }
        return removeAll;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("");
        if (isEmpty()) {
            string.append("Is empty");
        } else {
            string.append("List = [");
            for (int i = 0; i < this.size; i++) {
                string.append(this.array[i] + ", ");
            }
            if (this.size > 0) {
                string.delete(string.length() - 2, string.length());
            }
            string.append("]");
            string.append("; size = " + this.size);
            string.append("; arraySize = " + this.array.length);
        }
        return String.valueOf(string);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i >= 0) {
            System.arraycopy(this.array, i + 1, this.array, i, this.size - 1 - i);
            size--;
            //decreaseArraySize();
            return true;
        } else {
            return false;
        }
    }



    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.array, this.size);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    private boolean isCorrectRange(int index) {
        boolean res = true;
        if (index >= this.size || index < 0)
            res = false;
        return res;
    }

    private void setEmptyArray() {
        this.size = 0;
        this.array = (T[]) new Object[this.DEFAULT_CAPACITY];
    }

    private void throwIsIncorrectRange(int index){
        if (!isCorrectRange(index)) {
            throw new IndexOutOfBoundsException(String.format("Current index: %d, Size: %d", index, this.size) );
        }
    }

    private void increaseArraySize() {
        if (this.array.length < this.size + 1) {
            array = Arrays.copyOf(this.array, this.array.length + this.DEFAULT_CAPACITY);
        }
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Object[] a = toArray();
        Arrays.sort(a, (Comparator) c);
        this.array = (T[]) a;
    }

    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        int srcSize = src.size();
        int destSize = dest.size();

        if (destSize < srcSize) {
            for(int i = 0; i < destSize; i++) {
                dest.set(i, src.get(i));
            }
        } else {
            for(int i = 0; i < srcSize; i++) {
                dest.set(i, src.get(i));
            }
        }
    }

    private class Itr implements Iterator<T>{
        int cursor = 0;
        int lastRet = -1;

        Itr() {}

        public boolean hasNext() {
            return cursor != size();
        }
        public T next(){
            int i = cursor;
            T next = get(i);
            lastRet = i;
            cursor = i + 1;
            return next;
        }
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                MyArrayList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends Itr implements ListIterator<T> {
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public T previous() {
            try {
                int i = cursor - 1;
                T previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T e) {
            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}