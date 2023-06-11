import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LibraryCatalogAppCsv {
    private static final String FILE_PATH = "library_catalog.csv";
    private static final Scanner scanner = new Scanner(System.in);

    private static List<Book> libraryCatalog = new ArrayList<>();

    public static void main(String[] args) {
        loadCatalogFromCSV();

        int choice = 0;
        while (choice != 4) {
            displayMenu();
            choice = getChoice();
            switch (choice) {
                case 1:
                    viewCatalog();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    issueBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    System.out.println("Exiting the program... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Unique Library");
        System.out.println("--------------------------------------------------");
        System.out.println("1. View the complete list of Books");
        System.out.println("2. Add a Book");
        System.out.println("3. Issue a Book");
        System.out.println("4. Return a Book");
        System.out.println("5. Exit");
        System.out.print("Please choose an option from the above menu: ");
    }

    private static int getChoice() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid choice. Please enter a valid option: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void viewCatalog() {
        System.out.println("--------------------------------------------------");
        System.out.println("List of all Books");
        System.out.println("--------------------------------------------------");
        System.out.println("Book ID\tBook Title\tAuthor\tAvailability\tIssue Date");
        for (Book book : libraryCatalog) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                    book.getBookId(), book.getBookTitle(), book.getAuthor(),
                    book.getAvailability(), book.getIssueDate());
        }
    }

    private static void addBook() {
        System.out.println("--------------------------------------------------");
        System.out.println("Add a Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter the Book Title: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter the Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter the Availability: ");
        String availability = scanner.nextLine();
        System.out.print("Enter the Issue Date: ");
        String issueDate = scanner.nextLine();

        Book newBook = new Book(bookId, bookTitle, author, availability, issueDate);
        libraryCatalog.add(newBook);
        saveCatalogToCSV();
        System.out.println("Book added successfully.");
    }

    private static void issueBook() {
        System.out.println("--------------------------------------------------");
        System.out.println("Issue a Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine();

        Book bookToIssue = findBookById(bookId);
        if (bookToIssue != null) {
            if (bookToIssue.getAvailability().equalsIgnoreCase("Available")) {
                System.out.print("Enter the Student ID: ");
                String studentId = scanner.nextLine();
                bookToIssue.setAvailability(studentId);
                System.out.println("Book issued successfully.");
            } else {
                System.out.println("Book with ID " + bookId + " is not available for issuing.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    private static void returnBook() {
        System.out.println("--------------------------------------------------");
        System.out.println("Return a Book");
        System.out.println("--------------------------------------------------");
        System.out.print("Enter the Book ID: ");
        String bookId = scanner.nextLine();

        Book bookToReturn = findBookById(bookId);
        if (bookToReturn != null) {
            if (!bookToReturn.getAvailability().equalsIgnoreCase("Available")) {
                bookToReturn.setAvailability("Available");
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Book with ID " + bookId + " is not issued.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    private static Book findBookById(String bookId) {
        for (Book book : libraryCatalog) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    private static void saveCatalogToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book book : libraryCatalog) {
                writer.write(book.getBookId() + "," + book.getBookTitle() + ","
                        + book.getAuthor() + "," + book.getAvailability() + ","
                        + book.getIssueDate());
                writer.newLine();
            }
            System.out.println("Catalog saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving the catalog: " + e.getMessage());
        }
    }

    private static void loadCatalogFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String bookId = data[0].trim();
                    String bookTitle = data[1].trim();
                    String author = data[2].trim();
                    String availability = data[3].trim();
                    String issueDate = data[4].trim();
                    Book book = new Book(bookId, bookTitle, author, availability, issueDate);
                    libraryCatalog.add(book);
                }
            }
            System.out.println("Catalog loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading the catalog: " + e.getMessage());
        }
    }

    private static class Book {
        private String bookId;
        private String bookTitle;
        private String author;
        private String availability;
        private String issueDate;

        public Book(String bookId, String bookTitle, String author, String availability, String issueDate) {
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.author = author;
            this.availability = availability;
            this.issueDate = issueDate;
        }

        public String getBookId() {
            return bookId;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public String getAuthor() {
            return author;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }

        public String getIssueDate() {
            return issueDate;
        }
    }
}
