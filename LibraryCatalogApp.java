import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class LibraryCatalogApp {
    private static final int MAX_BOOKS = 10; // Maximum number of books in the catalog
    private static final int BOOK_ID_INDEX = 0;
    private static final int BOOK_TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int AVAILABILITY_INDEX = 3;
    private static final int ISSUE_DATE_INDEX = 4;
    private static final double CHARGE_PER_DAY = 10.0;

    private static String[][] libraryCatalog = new String[MAX_BOOKS][5]; // Multi-dimensional array for books

    public static void main(String[] args) {
        initializeLibraryCatalog(); // Initialize the library catalog with sample data
        displayWelcomeMessage();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print("Please choose an option from the above menu: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    displayAllBooks();
                    break;
                case 2:
                    issueBook(scanner);
                    break;
                case 3:
                    returnBook(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println();
        }

        System.out.println("Thank you for visiting the Library!");
    }

    private static void initializeLibraryCatalog() {
        libraryCatalog[0] = new String[]{"101", "HTML and CSS", "Jon Duckett", "Available", "Null"};
        libraryCatalog[1] = new String[]{"102", "JavaScript: The Good Parts", "Douglas C", "Available", "Null"};
        libraryCatalog[2] = new String[]{"103", "Learning Web Design", "Jennifer N", "CP2014", "23-May-2023"};
        libraryCatalog[3] = new String[]{"104", "Responsive Web Design", "Ben Frain", "EC3142", "17-May-2023"};
    }

    private static void displayWelcomeMessage() {
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Unique Library");
        System.out.println("--------------------------------------------------");
    }

    private static void displayMainMenu() {
        System.out.println("View the complete list of Books");
        System.out.println("Issue a Book");
        System.out.println("Return a Book");
        System.out.println("Exit");
    }

    private static void displayAllBooks() {
        System.out.println("--------------------------------------------------");
        System.out.println("List of all Books");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-8s%-20s%-15s%-15s%-15s%n",
                "Book ID", "Book Title", "Author", "Availability", "Issue Date");
        for (String[] book : libraryCatalog) {
            System.out.printf("%-8s%-20s%-15s%-15s%-15s%n",
                    book[BOOK_ID_INDEX], book[BOOK_TITLE_INDEX], book[AUTHOR_INDEX],
                    book[AVAILABILITY_INDEX], book[ISSUE_DATE_INDEX]);
        }
        System.out.println();
    }

    private static void issueBook(Scanner scanner) {
        System.out.println("--------------------------------------------------");
        System.out.println("Issue the Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine();

        int bookIndex = findBookIndexById(bookId);
        if (bookIndex != -1) {
            String[] book = libraryCatalog[bookIndex];
            String bookTitle = book[BOOK_TITLE_INDEX];
            String availabilityStatus = book[AVAILABILITY_INDEX];
            String issueDate = book[ISSUE_DATE_INDEX];

            if (availabilityStatus.equals("Available")) {
                System.out.println(bookId + " " + bookTitle + " - " + book[AUTHOR_INDEX] + ": " + availabilityStatus);
                System.out.print("Enter StudentID: ");
                String studentId = scanner.nextLine();
                System.out.print("Enter 'C' to confirm: ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("C")) {
                    book[AVAILABILITY_INDEX] = studentId;
                    book[ISSUE_DATE_INDEX] = getCurrentDate();
                    System.out.println("BookID: " + bookId + " is Issued to " + studentId);
                } else {
                    System.out.println("Issue process canceled.");
                }
            } else {
                System.out.println("The book is not available for issuing.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    private static void returnBook(Scanner scanner) {
        System.out.println("--------------------------------------------------");
        System.out.println("Return the Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine();

        int bookIndex = findBookIndexById(bookId);
        if (bookIndex != -1) {
            String[] book = libraryCatalog[bookIndex];
            String availabilityStatus = book[AVAILABILITY_INDEX];
            String issueDate = book[ISSUE_DATE_INDEX];

            if (!availabilityStatus.equals("Available")) {
                System.out.println(bookId + " - " + book[BOOK_TITLE_INDEX]);
                System.out.println("StudentID - " + availabilityStatus);
                System.out.println("Issue Date - " + issueDate);

                int delayedDays = calculateDelayedDays(issueDate);
                if (delayedDays > 0) {
                    double delayedCharges = delayedDays * CHARGE_PER_DAY;
                    System.out.println("Delayed by - " + delayedDays + " days");
                    System.out.println("Delayed Charges - Rs. " + delayedCharges);
                }

                System.out.print("Enter 'R' to confirm the return: ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("R")) {
                    book[AVAILABILITY_INDEX] = "Available";
                    book[ISSUE_DATE_INDEX] = "Null";
                    System.out.println("BookID: " + bookId + " is returned back");
                } else {
                    System.out.println("Return process canceled.");
                }
            } else {
                System.out.println("The book is not issued to anyone.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    private static int findBookIndexById(String bookId) {
        for (int i = 0; i < libraryCatalog.length; i++) {
            if (libraryCatalog[i][BOOK_ID_INDEX].equals(bookId)) {
                return i;
            }
        }
        return -1;
    }

    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(new Date());
    }

    private static int calculateDelayedDays(String issueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date currentDate = sdf.parse(getCurrentDate());
            Date dateIssued = sdf.parse(issueDate);

            long difference = currentDate.getTime() - dateIssued.getTime();
            long daysDifference = difference / (1000 * 60 * 60 * 24);
            return (int) daysDifference - 7;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
