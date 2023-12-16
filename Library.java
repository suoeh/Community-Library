import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

class Library {
    // variables
    Scanner scanner = new Scanner(System.in);
    String input;
    int tempID;

    // hashmaps to store data!
    HashMap<Integer, Book> books;
    HashMap<Integer, CommunityMember> members; // these two maps are mapped using IDs

    // constructor to initialize new hashmaps
    public Library() {
        this.books = new HashMap<>();
        this.members = new HashMap<>();
    }

    public void addMember(CommunityMember member) {
        this.members.put(member.cardID, member); // puts the member at their ID slot
    }

    public void removeMember(int cardID) {
        this.members.remove(cardID); // removes member from the key
    }

    public void addBook(Book book) {
        this.books.put(book.bookID, book); // puts the book at their ID slot
    }

    public void removeBook(Book book) {
        this.books.remove(book.bookID); // removes book from the key
    }

    // load books from file
    // uploads contents as hashmap objects
    public void loadBooks(String filename) {

        // parsed by ^ character to avoid comma complications from book entries
        // loads scanner for books.csv

        try (Scanner scanner = new Scanner(new File(filename))) {
            // initializes array, skips first line
            String[] data;
            // before scanner reaches EOF

            while (scanner.hasNextLine()) {
                // loads new line, deletes quotations and splits by ^ character
                // 0th index is ID, 1st index is title, 2nd index is author, 3rd index is available copies
                data = scanner.nextLine().replaceAll("\"", "").split("\\^");

                // data parsing
                int bookID = Integer.parseInt(data[0]);
                String title = data[1];
                String author = data[2];
                int availableCopies = Integer.parseInt(data[3]);
                // uploads to hashmap
                books.put(bookID, new Book(bookID, title, author, availableCopies));
            }
        } catch (FileNotFoundException e) { // if the file doesn't exist.... nice try
            System.out.println(".csv file not found!");
        }
    }

    // loads community members from file
    public void loadMembers(String filename) {
        // initializes scanner
        try (Scanner scanner = new Scanner(new File(filename))) {
            // loops until EOF
            String[] data;

            while (scanner.hasNextLine()) {
                // takes first line with titles
                data = scanner.nextLine().split("\\^");

                // parses data
                int cardID = Integer.parseInt(data[0]);
                String firstName = data[1];
                String lastName = data[2];
                CommunityMember newMember = new CommunityMember(cardID, firstName, lastName);
                members.put(cardID, newMember);

                for (int i = 0; i < 2; i++) {
                    newMember.borrowedBookID[i] = Integer.parseInt(data[i + 3]);
                }
                // System.out.println(Arrays.toString(newMember.borrowedBookID));
            }
        } catch (FileNotFoundException e) {
            System.out.println(".csv file not found!");
        }
    }

    // writes books to csv
    public void saveBooks(HashMap<Integer, Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src//books.csv"))) {
            // iterates through all books in hashmap, writes out in csv
            for (Book book : books.values()) {
                bw.write(book.bookID + "^" + book.title + "^" + book.author + "^" + book.availableCopies);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // writes members to csv
    public void saveMembers(HashMap<Integer, CommunityMember> members) {
        String line;
        // loads writer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src//communityMembers.csv"))) {
            for (CommunityMember member : members.values()) {
                StringBuilder sb = new StringBuilder(); // temp variable to compile data into writeable line
                line = member.cardID + "^" + member.firstName + "^" + member.lastName;
                sb.append(line);

                for (int id : member.borrowedBookID) {
                    sb.append("^").append(id);
                }
                bw.write(sb.toString()); // uploads line to csv
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // implemented switch methods
    public void changeBookCount() {

        // variables
        int amount;
        Book tempBook;

        System.out.println("Would you like to add or remove copies? (add/remove)");
        input = scanner.nextLine();

        switch(input) {
            case "add":
                // book info details
                System.out.println("Please enter the ID of the book:");
                tempID = Integer.parseInt(scanner.nextLine());

                while (books.get(tempID) == null) { // hashmap call forces valid book ID, invalid input handling
                    System.out.println("Please enter a valid book ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }

                tempBook = books.get(tempID);

                System.out.println("Enter the amount to increase the count by:");
                amount = Integer.parseInt(scanner.nextLine());
                while (amount <= 0){ // invalid input handling
                    System.out.println("Please enter a valid value: ");
                    amount = Integer.parseInt(scanner.nextLine());
                }

                tempBook.availableCopies += amount; // modifies amount
                System.out.println("Book count updated of book with ID " + tempID + " from " +
                        (tempBook.availableCopies - amount) + " to " + tempBook.availableCopies + ".");

            case "remove":
                // book info details
                System.out.println("Please enter the ID of the book:");
                tempID = Integer.parseInt(scanner.nextLine());

                while (books.get(tempID) == null) { // hashmap call forces valid book ID, invalid input handling
                    System.out.println("Please enter a valid book ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }

                tempBook = books.get(tempID);

                if (tempBook.availableCopies <= 0) { // edge case, when there are no available books to remove
                    System.out.println("There are no available copies of " + tempID + " to remove :(");
                    break;
                }

                System.out.println("Enter the amount to decrease the count by:");
                amount = Integer.parseInt(scanner.nextLine());
                // invalid input handling, doesn't allow negative integer input OR a negative amount of copies after
                while (tempBook.availableCopies - amount < 0 || amount <= 0) {
                    System.out.println("Please enter a valid value: ");
                    amount = Integer.parseInt(scanner.nextLine());
                }

                tempBook.availableCopies -= amount; // modifies amount
                System.out.println("Book count updated of book with ID " + tempID + " from " +
                        (tempBook.availableCopies + amount) + " to " + tempBook.availableCopies + ".");
        }
    }

    public void modifyCatalogue() {
        System.out.println("Would you like to add a new book or delete a previous one?");
        input = scanner.nextLine();

        switch (input) {
            case "add" -> {
                System.out.println("Please enter the ID of the book:");
                tempID = Integer.parseInt(scanner.nextLine());
                while (books.containsKey(tempID) || tempID <= 0) { // hashmap call for existing ID
                    System.out.println("Give an unoccupied or positive book ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }
                System.out.println("What is the name of the book?");
                String name = scanner.nextLine();

                System.out.println("What is the name of the author?");
                String author = scanner.nextLine();

                System.out.println("How many copies will be available?");
                int amount = Integer.parseInt(scanner.nextLine());

                while (amount < 0) {
                    System.out.println("Give a valid amount of available copies: ");
                    amount = Integer.parseInt(scanner.nextLine());
                }

                Book tempBook = new Book(tempID, name, author, amount); // initialized book instance
                addBook(tempBook);
                System.out.println("Book added!");
            }
            case "remove" -> {
                // book info details
                System.out.println("Please enter the ID of the book:");
                tempID = Integer.parseInt(scanner.nextLine());

                while (books.get(tempID) == null) { // hashmap call forces valid book ID, invalid input handling
                    System.out.println("Please enter a valid book ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }

                removeBook(books.get(tempID));
                System.out.println("Book removed.");
            }
        }
    }

    public void modifyUsers() {
        System.out.println("Would you like to add a member or remove one?");
        input = scanner.nextLine();

        switch (input) {
            case "add" -> {
                System.out.println("Please enter the ID of the member:");
                tempID = Integer.parseInt(scanner.nextLine());
                while (!members.containsKey(tempID)) {
                    System.out.println("Please give a valid member ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }
                System.out.println("What is their first name?");
                String firstName = scanner.nextLine();

                System.out.println("What is their last name?");
                String lastName = scanner.nextLine();

                CommunityMember member = new CommunityMember(tempID, firstName, lastName); // initialized member
                addMember(member);

            }
            case "remove" -> {
                System.out.println("Please enter the ID of the member to remove:");
                tempID = Integer.parseInt(scanner.nextLine());
                while (!members.containsKey(tempID)) { // invalid ID checker
                    System.out.println("Please give a valid member ID: ");
                    tempID = Integer.parseInt(scanner.nextLine());
                }
                removeMember(tempID);
            }
        }
    }

    public void loanBook() {
        System.out.println("Please enter the ID of the member:"); // getting member
        tempID = Integer.parseInt(scanner.nextLine());
        while (!members.containsKey(tempID)) {
            System.out.println("Please give a valid member ID: ");
            tempID = Integer.parseInt(scanner.nextLine());
        }
        CommunityMember tempMember = members.get(tempID);

        System.out.println("Please enter the ID of the book"); // getting book
        tempID = Integer.parseInt(scanner.nextLine());
        while (books.get(tempID) == null) { // hashmap call forces valid book ID, invalid input handling
            System.out.println("Please enter a valid book ID: ");
            tempID = Integer.parseInt(scanner.nextLine());
        }
        Book tempBook = books.get(tempID);

        if (tempBook.checkAvailability()) { // book loaning process
            tempMember.loanBook(tempBook);
            tempBook.availableCopies--;
            System.out.println("Book loaned.");
        } else {
            System.out.println("Book is currently being loaned. Would you like to be added to the waitlist? (yes/no)");
            input = scanner.nextLine(); // input to determine if user is added to waitlist
            if (input.equals("yes")) {
                tempBook.addWaitList(tempMember.cardID);
            }
        }
    }

    public void returnBook() {
        System.out.println("Please enter the ID of the member");
        int tempRetUserID = Integer.parseInt(scanner.nextLine());

        if (!members.containsKey(tempRetUserID)) {
            System.out.println("Member not found.");
        } else {
            CommunityMember tempRetMem = members.get(tempRetUserID);
            System.out.println("Please enter the ID of the book");
            int tempRetBookID = Integer.parseInt(scanner.nextLine());

            if (!books.containsKey(tempRetBookID)) {
                System.out.println("Book not found");
            } else {
                Book tempRetBook = books.get(tempRetBookID);
                int nextMemID = tempRetBook.removeWaitList();
                while ((!members.containsKey(nextMemID)) && (!tempRetBook.waitList.isEmpty())) {
                    nextMemID = tempRetBook.removeWaitList();
                }
                if (nextMemID == -1) {
                    tempRetBook.availableCopies++;
                } else {
                    CommunityMember nextMem = members.get(nextMemID);
                    if (nextMem.borrowedBooks == 5) {
                        tempRetBook.addWaitList(nextMemID);
                        tempRetBook.availableCopies++;
                    } else {
                        nextMem.loanBook(tempRetBook);
                    }
                }

                for (int i = 0; i < tempRetMem.borrowedBooks; i++) {
                    if (tempRetMem.borrowedBookID[i] == tempRetUserID) {
                        tempRetMem.returnBook(i);
                    }
                }
            }
        }
    }
    public void checkAvailability() {
        System.out.println("Please enter the ID of the book");
        int tempCheckID = Integer.parseInt(scanner.nextLine());
        if (!books.containsKey(tempCheckID)) {
            System.out.println("Book not found.");
        } else {
            boolean avail = books.get(tempCheckID).checkAvailability();
            if (avail) {
                System.out.println("The book is available");
            } else {
                System.out.println("The book is currently being loaned");
            }
        }
    }

    public void placeWaitList() {
        System.out.println("Please enter the ID of the member");
        int placeUserID = Integer.parseInt(scanner.nextLine());

        if (!members.containsKey(placeUserID)) {
            System.out.println("Member not found.");
        } else {
            CommunityMember placeUser = members.get(placeUserID);
            System.out.println("Please enter the ID of the book");
            int tempPlaceID = Integer.parseInt(scanner.nextLine());
            if (!books.containsKey(tempPlaceID)) {
                System.out.println("Book not found");
            } else {
                Book tempPlace = books.get(tempPlaceID);
                tempPlace.addWaitList(placeUserID);
            }
        }
    }

    public void removeWaitList() {
        System.out.println("Please enter the ID of the member:");
        int memberId = Integer.parseInt(scanner.nextLine());

        System.out.println("Please enter the ID of the book:");
        int bookId = Integer.parseInt(scanner.nextLine());

        if (members.containsKey(memberId) && books.containsKey(bookId)) {
            CommunityMember member = members.get(memberId);
            Book book = books.get(bookId);

            if (book.waitList.contains(memberId)) {
                book.removeWaitList();
                System.out.println("Member removed from the book's waitlist.");
            } else {
                System.out.println("Member is not in the waitlist for this book.");
            }
        } else {
            System.out.println("Member or Book ID not found.");
        }
    }
    public void endProgram() {
        saveBooks(books);
        saveMembers(members);
        System.out.println("Have a great day !");
        System.exit(0);
    }

}