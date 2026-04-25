import java.time.LocalDate;

interface MyComparator<T> {
    int compare(T a, T b);
}

class Student {
    String name;
    String lastName;
    LocalDate birthday;
    String group;
    double gpa;

    Student(String name, String lastName, String birthday, String group, double gpa) {
        this.name = name;
        this.lastName = lastName;
        this.birthday = LocalDate.parse(birthday);
        this.group = group;
        this.gpa = gpa;
    }

    int getAge() {
        return LocalDate.now().getYear() - birthday.getYear();
    }

    String getFullName() {
        return name + " " + lastName;
    }

    public String toString() {
        return getFullName() + " | age: " + getAge() + " | group: " + group + " | GPA: " + gpa;
    }
}

class MyArrayList<T> {
    private Object[] data;
    private int size;

    MyArrayList() {
        data = new Object[10];
        size = 0;
    }

    void add(T value) {
        if (size == data.length) resize();
        data[size++] = value;
    }

    T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Wrong index");
        }
    }

    private void swap(int i, int j) {
        Object temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    void print() {
        for (int i = 0; i < size; i++) {
            System.out.println(data[i]);
        }
        System.out.println();
    }

    int linearSearch(T target, MyComparator<T> comp) {
        for (int i = 0; i < size; i++) {
            if (comp.compare(get(i), target) == 0) return i;
        }
        return -1;
    }

    int binarySearch(T target, MyComparator<T> comp) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int result = comp.compare(get(mid), target);

            if (result == 0) return mid;
            else if (result < 0) left = mid + 1;
            else right = mid - 1;
        }

        return -1;
    }

    void bubbleSort(MyComparator<T> comp) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (comp.compare(get(j), get(j + 1)) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    void quickSort(MyComparator<T> comp) {
        quickSortHelper(0, size - 1, comp);
    }

    private void quickSortHelper(int low, int high, MyComparator<T> comp) {
        if (low < high) {
            int p = partition(low, high, comp);
            quickSortHelper(low, p - 1, comp);
            quickSortHelper(p + 1, high, comp);
        }
    }

    private int partition(int low, int high, MyComparator<T> comp) {
        T pivot = get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comp.compare(get(j), pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    void heapSort(MyComparator<T> comp) {
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(size, i, comp);
        }

        for (int i = size - 1; i > 0; i--) {
            swap(0, i);
            heapify(i, 0, comp);
        }
    }

    private void heapify(int n, int i, MyComparator<T> comp) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && comp.compare(get(left), get(largest)) > 0) {
            largest = left;
        }

        if (right < n && comp.compare(get(right), get(largest)) > 0) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            heapify(n, largest, comp);
        }
    }
}

class MyLinkedList<T> {
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node head;
    private int size;

    void add(T value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        size++;
    }

    T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Wrong index");
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }

    int linearSearch(T target, MyComparator<T> comp) {
        Node current = head;
        int index = 0;

        while (current != null) {
            if (comp.compare(current.data, target) == 0) return index;
            current = current.next;
            index++;
        }

        return -1;
    }

    void mergeSort(MyComparator<T> comp) {
        head = mergeSortHelper(head, comp);
    }

    private Node mergeSortHelper(Node node, MyComparator<T> comp) {
        if (node == null || node.next == null) return node;

        Node middle = getMiddle(node);
        Node second = middle.next;
        middle.next = null;

        Node left = mergeSortHelper(node, comp);
        Node right = mergeSortHelper(second, comp);

        return merge(left, right, comp);
    }

    private Node getMiddle(Node node) {
        Node slow = node;
        Node fast = node.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private Node merge(Node a, Node b, MyComparator<T> comp) {
        if (a == null) return b;
        if (b == null) return a;

        if (comp.compare(a.data, b.data) <= 0) {
            a.next = merge(a.next, b, comp);
            return a;
        } else {
            b.next = merge(a, b.next, comp);
            return b;
        }
    }

    void print() {
        Node current = head;

        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }

        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        MyComparator<Integer> intComp = (a, b) -> a - b;
        System.out.println("ARRAYLIST DEMO");

        MyArrayList<Integer> arr = new MyArrayList<>();
        arr.add(5);
        arr.add(2);
        arr.add(9);
        arr.add(1);
        arr.add(7);

        System.out.println("Before sorting:");
        arr.print();

        arr.bubbleSort(intComp);
        System.out.println("After Bubble Sort:");
        arr.print();

        System.out.println("Linear Search 9 index: " + arr.linearSearch(9, intComp));
        System.out.println("Binary Search 7 index: " + arr.binarySearch(7, intComp));

        MyArrayList<Integer> arr2 = new MyArrayList<>();
        arr2.add(8);
        arr2.add(3);
        arr2.add(6);
        arr2.add(1);

        arr2.quickSort(intComp);
        System.out.println("Quick Sort:");
        arr2.print();

        MyArrayList<Integer> arr3 = new MyArrayList<>();
        arr3.add(10);
        arr3.add(4);
        arr3.add(2);
        arr3.add(15);

        arr3.heapSort(intComp);
        System.out.println("Heap Sort:");
        arr3.print();

        System.out.println("LINKED LIST DEMO");

        MyLinkedList<Integer> linked = new MyLinkedList<>();
        linked.add(4);
        linked.add(1);
        linked.add(6);
        linked.add(2);

        System.out.println("Before Merge Sort:");
        linked.print();

        linked.mergeSort(intComp);
        System.out.println("After Merge Sort:");
        linked.print();

        System.out.println("Linear Search 6 index: " + linked.linearSearch(6, intComp));

        System.out.println("STUDENT SORTING DEMO");

        MyComparator<Student> byGpa = (a, b) -> Double.compare(a.gpa, b.gpa);
        MyComparator<Student> byAge = (a, b) -> a.getAge() - b.getAge();
        MyComparator<Student> byFullName = (a, b) -> a.getFullName().compareTo(b.getFullName());

        MyArrayList<Student> studentsByGpa = createStudents();
        studentsByGpa.quickSort(byGpa);
        System.out.println("Students sorted by GPA:");
        studentsByGpa.print();

        MyArrayList<Student> studentsByAge = createStudents();
        studentsByAge.heapSort(byAge);
        System.out.println("Students sorted by age:");
        studentsByAge.print();

        MyLinkedList<Student> studentsByName = createStudentLinkedList();
        studentsByName.mergeSort(byFullName);
        System.out.println("Students sorted by full name:");
        studentsByName.print();
    }

    static MyArrayList<Student> createStudents() {
        MyArrayList<Student> students = new MyArrayList<>();

        students.add(new Student("Nurgul", "Amanbek", "2006-03-12", "SE-2401", 3.4));
        students.add(new Student("Ali", "Sultan", "2005-07-20", "SE-2402", 3.8));
        students.add(new Student("Dana", "Kairat", "2004-11-05", "SE-2401", 2.9));
        students.add(new Student("Aruzhan", "Bekzat", "2006-01-15", "SE-2403", 3.6));

        return students;
    }

    static MyLinkedList<Student> createStudentLinkedList() {
        MyLinkedList<Student> students = new MyLinkedList<>();

        students.add(new Student("Nurgul", "Amanbek", "2006-03-12", "SE-2401", 3.4));
        students.add(new Student("Ali", "Sultan", "2005-07-20", "SE-2402", 3.8));
        students.add(new Student("Dana", "Kairat", "2004-11-05", "SE-2401", 2.9));
        students.add(new Student("Aruzhan", "Bekzat", "2006-01-15", "SE-2403", 3.6));

        return students;
    }
}
