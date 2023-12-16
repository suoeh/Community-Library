import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class Book {
    // variables
    int bookID;
    String title;
    String author;
    int availableCopies;
    LinkedList<Integer> borrowing;
    Queue<Integer> waitList;

    // constructor
    public Book(int bookID, String title, String author, int availableCopies) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.availableCopies = availableCopies;
        this.waitList = new PriorityQueue<>();
        this.borrowing = new LinkedList<>();
    }

    // getters
    public boolean checkAvailability() {
        return availableCopies > 0;
    }

    public int getID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // waitlist methods
    public void addWaitList(int member) {
        waitList.add(member);
    }

    public int removeWaitList() {
        if (!waitList.isEmpty()) { // only works with non-empty waitlist
            return waitList.poll(); // returns and deletes head element
        } else {return -1;}
    }
}