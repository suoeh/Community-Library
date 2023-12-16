import java.util.Scanner;

public class CommunityLibrary {

    public static void main(String[] args) {
        // variables, initializing scanner/library
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.loadBooks("src//books.csv"); // uploading books/members to library
        library.loadMembers("src//communityMembers.csv");
        String input;

        System.out.println("Hello librarian, welcome to the online community library admin catalogue! " +
                "\nInput the following prompts to perform the following functions: ");

        while (true) { // input loop
            System.out.println("""
                    1: Add/remove copies of an existing book
                    2: Add a new book or delete an existing book
                    3: Add/remove a member
                    4: Loan a book
                    5: Return a book
                    6: Check if a book is available
                    7: Place a book on the wait list
                    8: Remove a book from the wait list
                    end: Exit the library admin catalogue""");

            input = scanner.nextLine();

            switch(input) { // input
                case "1" -> library.changeBookCount();
                case "2" -> library.modifyCatalogue();
                case "3" -> library.modifyUsers();
                case "4" -> library.loanBook();
                case "5" -> library.returnBook();
                case "6" -> library.checkAvailability();
                case "7" -> library.placeWaitList();
                case "8" -> library.removeWaitList();
                case "end" -> library.endProgram();
            }
        }
    }
}