class CommunityMember {
    // variables
    int cardID;
    String firstName;
    String lastName;
    int[] borrowedBookID;
    int borrowedBooks; // book counter

    // constructor
    public CommunityMember(int cardID, String firstName, String lastName) {
        this.cardID = cardID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.borrowedBookID = new int[2];
        this.borrowedBooks = 0;
    }

    // loan book
    public void loanBook(Book book) { // assume that book exists
        // DEBUG, MAKE SURE BOOK EXISTS, MAKE SURE BORROWED BOOK LIMIT IS REACHED
        borrowedBookID[borrowedBooks] = book.bookID;
        borrowedBooks++;
        book.availableCopies--;
    }

    // return book
    public void returnBook(int index) { // assume that book exists
        // DEBUG, MAKE SURE THE COUNTER IS MORE THAN 0
        for (int i = index; i < borrowedBooks - 1; i++) { // shifts everything to the right of index left
            borrowedBookID[i] = borrowedBookID[i + 1];
        }
        borrowedBookID[borrowedBooks - 1] = -1; // sets last element to -1, which means no book
        borrowedBooks--;
    }
}