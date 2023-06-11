import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class LibraryCatalogApp {
    private static final int MAX_BOOKS = 100;
    private static final int BOOK_ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int AVAILABILITY_INDEX = 3;
    private static final int ISSUE_DATE_INDEX = 4;

    private String[][] catalog={ };
    private int bookCount;
    private Scanner scanner;

    public LibraryCatalogApp() {
        catalog = new String[MAX_BOOKS][5];
        bookCount = 0;
        scanner = new Scanner(System.in);
    }

    public void run() {
        displayWelcomeMessage();
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int option = getOption();
            switch (option) {
                case 1:
                    displayAllBooks();
                    break;
                case 2:
                    issueBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
        scanner.close();
        System.out.println("Thank you for using the Library Catalog App!");
    }

    private void displayWelcomeMessage() {
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Unique Library");
        System.out.println("--------------------------------------------------");
    }

    private void displayMainMenu() {
        System.out.println("View the complete list of Books");
        System.out.println("Issue a Book");
        System.out.println("Return a Book");
        System.out.println("Exit");
        System.out.print("Please choose an option from the above menu: ");
    }

    private int getOption() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void displayAllBooks() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("List of all Books");
        System.out.println("--------------------------------------------------");
        System.out.println("Book ID\t\tBook Title\t\tAuthor\t\tAvailability\tIssue Date");
        for (int i = 0; i < bookCount; i++) {
            System.out.printf("%s\t\t%s\t\t%s\t\t%s\t\t%s%n",
                    catalog[i][BOOK_ID_INDEX],
                    catalog[i][TITLE_INDEX],
                    catalog[i][AUTHOR_INDEX],
                    catalog[i][AVAILABILITY_INDEX],
                    catalog[i][ISSUE_DATE_INDEX]);
        }
        System.out.println();
        System.out.print("Enter 'Y' to return to the main menu or 'N' to Exit: ");
        String choice = scanner.nextLine().trim();
        if (!choice.equalsIgnoreCase("Y")) {
            System.exit(0);
        }
    }

    private void issueBook() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Issue the Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine().trim();
        int bookIndex = findBookIndex(bookId);
        if (bookIndex == -1) {
            System.out.println("Book not found.");
            return;
        }
        String availability = catalog[bookIndex][AVAILABILITY_INDEX];
        if (!availability.equalsIgnoreCase("available")) {
            System.out.println("Book is already issued to a student.");
            return;
        }
        System.out.println(bookId + " - " + catalog[bookIndex][TITLE_INDEX] + " by " + catalog[bookIndex][AUTHOR_INDEX] + ": Available");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Enter 'C' to confirm: ");
        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("C")) {
            catalog[bookIndex][AVAILABILITY_INDEX] = studentId;
            catalog[bookIndex][ISSUE_DATE_INDEX] = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
            System.out.println("BookID: " + bookId + " is issued to " + studentId);
        } else {
            System.out.println("Issue canceled.");
        }
        System.out.print("Enter 'Y' to return to the main menu or 'N' to Exit: ");
        String choice = scanner.nextLine().trim();
        if (!choice.equalsIgnoreCase("Y")) {
            System.exit(0);
        }
    }

    private void returnBook() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Return the Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine().trim();
        int bookIndex = findBookIndex(bookId);
        if (bookIndex == -1) {
            System.out.println("Book not found.");
            return;
        }
        String availability = catalog[bookIndex][AVAILABILITY_INDEX];
        if (availability.equalsIgnoreCase("available")) {
            System.out.println("Book is already available in the library.");
            return;
        }
        System.out.println(bookId + " - " + catalog[bookIndex][TITLE_INDEX] + " by " + catalog[bookIndex][AUTHOR_INDEX]);
        System.out.println("StudentID - " + availability);
        System.out.println("Issue Date - " + catalog[bookIndex][ISSUE_DATE_INDEX]);
        long daysDelayed = calculateDaysDelayed(catalog[bookIndex][ISSUE_DATE_INDEX]);
        if (daysDelayed <= 0) {
            System.out.println("No delayed charges.");
        } else {
            double charges = calculateCharges(daysDelayed);
            System.out.printf("Delayed by - %d days%n", daysDelayed);
            System.out.printf("Delayed Charges - Rs. %.2f%n", charges);
        }
        System.out.print("Enter 'R' to confirm the return: ");
        String confirmation = scanner.nextLine().trim();
        if (confirmation.equalsIgnoreCase("R")) {
            catalog[bookIndex][AVAILABILITY_INDEX] = "available";
            System.out.println("BookID: " + bookId + " is returned back");
        } else {
            System.out.println("Return canceled.");
        }
        System.out.print("Enter 'Y' to return to the main menu or 'N' to Exit: ");
        String choice = scanner.nextLine().trim();
        if (!choice.equalsIgnoreCase("Y")) {
            System.exit(0);
        }
    }

    private int findBookIndex(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (catalog[i][BOOK_ID_INDEX].equalsIgnoreCase(bookId)) {
                return i;
            }
        }
        return -1;
    }

    private long calculateDaysDelayed(String issueDateStr) {
        LocalDate issueDate = LocalDate.parse(issueDateStr, DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.DAYS.between(issueDate, currentDate) - 7;
    }

    private double calculateCharges(long daysDelayed) {
        return daysDelayed * 10.0;
    }

    public static void main(String[] args) {
        LibraryCatalogApp app = new LibraryCatalogApp();
        app.run();
    }
}
