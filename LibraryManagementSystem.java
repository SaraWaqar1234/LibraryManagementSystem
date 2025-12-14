import java.util.*;
import java.io.*;

public class LibraryManagementSystem {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<String[]> users = new ArrayList<>();
    static ArrayList<String[]> books = new ArrayList<>();
    static String currentUserID = "";
    static String currentUserName = "";

    public static void main(String[] args) {
        loadUsers();
        loadBooks();
        boolean loggedIn = false;

        System.out.println("Welcome to the Library Management System!");

       while (!loggedIn) {
    System.out.print("Do you want to log in? (yes/no): ");
    String choice = sc.nextLine().trim();

    if (choice.equalsIgnoreCase("yes")) {
        loggedIn = login();
    } else if (choice.equalsIgnoreCase("no")) {
        while (true) {
            System.out.print("Do you want to exit? (yes/no): ");
            String exitChoice = sc.nextLine().trim();
            if (exitChoice.equalsIgnoreCase("yes")) {
                System.out.println("Exiting the system. Goodbye!");
                return;
            } else if (exitChoice.equalsIgnoreCase("no")) {
                break; // go back to login prompt
            } else {
                System.out.println("Invalid choice. Enter 'yes' or 'no'.");
            }
        }
    } else {
        System.out.println("Invalid choice. Enter 'yes' or 'no'.");
    }
}

        mainMenu();
    }

    public static boolean login() {
        while (true) {
            System.out.print("Do you have an account? (yes/no): ");
            String hasAccount = sc.nextLine().trim();

            if (hasAccount.equalsIgnoreCase("yes")) {
                String email = getValidEmail();
                System.out.print("Enter your password: ");
                String password = sc.nextLine();

                for (String[] u : users) {
                    if (u[2].equals(email) && u[3].equals(password)) {
                        currentUserID = u[0];
                        currentUserName = u[1];
                        System.out.println("Login successful! Welcome " + u[1]);
                        return true;
                    }
                }
                System.out.println("Email or password incorrect. Try again.");

            } else if (hasAccount.equalsIgnoreCase("no")) {
                System.out.print("Do you want to register? (yes/no): ");
                String choice = sc.nextLine().trim();
                if (choice.equalsIgnoreCase("yes")) {
                    register();
                } else if (choice.equalsIgnoreCase("no")) {
                    return false;
                } else {
                    System.out.println("Invalid choice. Enter again.");
                }

            } else {
                System.out.println("Invalid choice. Enter again.");
            }
        }
    }

    public static void register() {
        System.out.println("Register a new account:");

        System.out.print("Enter your ID: ");
        String id = sc.nextLine();

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        String email = getValidEmail();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        String role;
        while (true) {
            System.out.print("Enter your role (Student/Staff): ");
            role = sc.nextLine().trim();
            if (role.equalsIgnoreCase("Student") || role.equalsIgnoreCase("Staff")) break;
            else System.out.println("Invalid role. Enter Student or Staff.");
        }

        String borrowedCount = "0";
        String[] newUser = {id, name, email, password, role, borrowedCount};
        users.add(newUser);
        saveUsers();

        System.out.println("Registration successful! Please log in.");
    }

    public static String getValidEmail() {
        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = sc.nextLine().trim();
            if (isValidEmail(email)) break;
            else System.out.println("Invalid email. Only email.com or gmail.com are allowed.");
        }
        return email;
    }

    public static boolean isValidEmail(String email) {
        if (!email.contains("@") || !email.contains(".")) return false;
        int atPos = email.indexOf("@");
        int dotPos = email.lastIndexOf(".");
        if (atPos == 0 || dotPos < atPos || dotPos == email.length() - 1) return false;
        return email.endsWith("@gmail.com") || email.endsWith("@email.com");
    }

    public static boolean isStaff() {
        for (String[] u : users) {
            if (u[0].equals(currentUserID)) {
                return u[4].equalsIgnoreCase("Staff");
            }
        }
        return false;
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Issue Books");
            System.out.println("4. Return Books");
            System.out.println("5. Check Book Availability");
            System.out.println("6. Display Borrow Limit");
            System.out.println("7. Display All Books");      
            System.out.println("8. Search Books");
            System.out.println("9. Sort Books");
            System.out.println("10. Display Records");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1: addBook(); break;
                case 2: removeBook(); break;
                case 3: issueBook(); break;
                case 4: returnBook(); break;
                case 5: checkBookAvailability(); break;
                case 6: displayBorrowLimit(); break;
                case 7: displayBooks(); break;
                case 8: searchBook(); break;
                case 9: sortBooks(); break;
                case 10: displayRecords(); break;
                case 11: exit(); break;
                default: System.out.println("Enter correct option!");
            }
        }
    }

   public static void addBook() {
    // Only staff can add books
    if (!isStaff()) {
        System.out.println("Only staff can add books!");
        return;
    }

    try {
        // ----------- Book ID -----------
        String id;
        while (true) {
            System.out.print("Enter Book ID (leave empty to auto-generate): ");
            id = sc.nextLine().trim();
            if (id.isEmpty()) {
                id = "B" + (books.size() + 1); // auto-generate ID
                System.out.println("Generated Book ID: " + id);
            }

            boolean duplicate = false;
            for (String[] b : books) {
                if (b[0].equalsIgnoreCase(id)) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) break;
            System.out.println("Book ID already exists. Enter a different one.");
        }

        // ----------- Book Name -----------
        String name;
        while (true) {
            System.out.print("Enter Book Name: ");
            name = sc.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Book name cannot be empty.");
        }

        // ----------- Author -----------
        String author;
        while (true) {
            System.out.print("Enter Author: ");
            author = sc.nextLine().trim();
            if (!author.isEmpty()) break;
            System.out.println("Author cannot be empty.");
        }

        // Check duplicate name + author
        boolean duplicateCombo = false;
        for (String[] b : books) {
            if (b[1].equalsIgnoreCase(name) && b[2].equalsIgnoreCase(author)) {
                duplicateCombo = true;
                break;
            }
        }
        if (duplicateCombo) {
            System.out.println("This book by this author already exists!");
            return;
        }

        // ----------- Section / Genre -----------
        System.out.print("Enter Section/Genre (optional): ");
        String section = sc.nextLine().trim();

        // ----------- Copies -----------
        int copies;
        while (true) {
            try {
                System.out.print("Enter number of copies: ");
                copies = Integer.parseInt(sc.nextLine().trim());
                if (copies <= 0) {
                    System.out.println("Number of copies must be positive. Try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Enter an integer.");
            }
        }

        // ----------- Confirmation -----------
        System.out.println("\nConfirm adding book:");
        System.out.println("ID: " + id + ", Name: " + name + ", Author: " + author + ", Section: " + section + ", Copies: " + copies);
        System.out.print("Add this book? (yes/no): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Book addition cancelled.");
            return;
        }

        // ----------- Add Book to List -----------
String[] newBook = {id, name, author, section, String.valueOf(copies), ""}; // last "" is borrowedBy
books.add(newBook);

// ----------- Save to books.txt -----------
saveBooks();

        // ----------- Log Addition -----------
        try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Book added: " + name + " by " + author + " by user " + currentUserName);
            log.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }

        System.out.println("Book added successfully!");

    } catch (Exception e) {
        System.out.println("Error adding book: " + e.getMessage());
    }
}
       public static void removeBook() {
    // Only staff can remove books
    if (!isStaff()) {
        System.out.println("Only staff can remove books!");
        return;
    }

    try {
        // ----------- Book ID Input -----------
        System.out.print("Enter Book ID to remove: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("Book ID cannot be empty!");
            return;
        }

        // ----------- Find Book -----------
        String[] bookToRemove = null;
        for (String[] b : books) {
            if (b[0].equalsIgnoreCase(id)) {
                bookToRemove = b;
                break;
            }
        }

        if (bookToRemove == null) {
            System.out.println("Book not found with ID: " + id);
            return;
        }

        // ----------- Confirmation -----------
        System.out.println("\nConfirm removing book:");
        System.out.println("ID: " + bookToRemove[0] + ", Name: " + bookToRemove[1] + ", Author: " + bookToRemove[2] + ", Section: " + bookToRemove[3] + ", Copies: " + bookToRemove[4]);
        System.out.print("Remove this book? (yes/no): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Book removal cancelled.");
            return;
        }

        // ----------- Remove Book from List -----------
        books.remove(bookToRemove);

        // ----------- Save Updated Books -----------
        saveBooks();

        System.out.println("Book removed successfully!");

        // ----------- Log Removal -----------
        try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Book removed: " + bookToRemove[1] + " by " + bookToRemove[2] + " by user " + currentUserName);
            log.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }

    } catch (Exception e) {
        System.out.println("Error removing book: " + e.getMessage());
    }
} 
    
 
   public static void issueBook() {
    while (true) {
        try {
            // find current user
            String[] currentUser = null;
            for (String[] u : users) {
                if (u[0].equals(currentUserID)) {
                    currentUser = u;
                    break;
                }
            }
            if (currentUser == null) {
                System.out.println("User not found!");
                return;
            }

            int borrowLimit = currentUser[4].equalsIgnoreCase("Student") ? 3 : 5;
            int borrowedCount = Integer.parseInt(currentUser[5]);
            if (borrowedCount >= borrowLimit) {
                System.out.println("You have reached your borrow limit.");
                return;
            }

            System.out.print("Enter Book ID or Name to issue: ");
            String input = sc.nextLine().trim();
            String[] foundBook = null;

            for (String[] b : books) {
                if (b.length >= 6 && (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input))) {
                    int copies = Integer.parseInt(b[4]);
                    if (copies > 0) {
                        foundBook = b;
                        break;
                    }
                }
            }

            if (foundBook == null) {
                System.out.println("Book not found or no copies available!");
            } else {
                int availableCopies = Integer.parseInt(foundBook[4]);
                foundBook[4] = String.valueOf(availableCopies - 1);
                foundBook[5] = currentUserID; // borrowedBy
                currentUser[5] = String.valueOf(borrowedCount + 1);

                saveBooks();
                saveUsers();

                System.out.println("Book issued successfully!");

                try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
                    log.write(java.time.LocalDateTime.now() + " - Book issued: " + foundBook[1] + " by " + currentUserName);
                    log.newLine();
                } catch (IOException e) {
                    System.out.println("Could not write to log: " + e.getMessage());
                }
            }

            System.out.print("Do you want to issue another book? (yes/no): ");
            String choice = sc.nextLine().trim();
            if (!choice.equalsIgnoreCase("yes")) break;

        } catch (Exception e) {
            System.out.println("An error occurred while issuing the book. Please try again.");
        }
    }
}

    public static void returnBook() {
        while (true) {
            try {
                String[] currentUser = null;
                for (String[] u : users) {
                    if (u[0].equals(currentUserID)) {
                        currentUser = u;
                        break;
                    }
                }
                if (currentUser == null) {
                    System.out.println("User not found!");
                    return;
                }

                int borrowedCount = Integer.parseInt(currentUser[5]);
                if (borrowedCount <= 0) {
                    System.out.println("You have no books to return.");
                    return;
                }

                System.out.print("Enter Book ID or Name to return: ");
                String input = sc.nextLine().trim();
                String[] foundBook = null;

                for (String[] b : books) {
                    if (b.length >= 6 && (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input))) {
                        String borrowedBy = b[5];
                        if (borrowedBy != null && borrowedBy.equals(currentUserID)) {
                            foundBook = b;
                            break;
                        }
                    }
                }

                if (foundBook == null) {
                    System.out.println("Book not found or you did not borrow this book!");
                } else {
                    int availableCopies = Integer.parseInt(foundBook[4]);
                    foundBook[4] = String.valueOf(availableCopies + 1);
                    foundBook[5] = "";
                    currentUser[5] = String.valueOf(borrowedCount - 1);

                    saveBooks();
                    saveUsers();
                    System.out.println("Book returned successfully!");
                }
                
                    try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
                    log.write(java.time.LocalDateTime.now() + " - Book returned: " + foundBook[1] + " by " + currentUserName);
                    log.newLine();
                } catch (IOException e) {
                    System.out.println("Could not write to log: " + e.getMessage());
                }
            }
                System.out.print("Do you want to return another book? (yes/no): ");
                String choice = sc.nextLine().trim();
                if (!choice.equalsIgnoreCase("yes")) break;

            } catch (Exception e) {
                System.out.println("An error occurred while returning the book. Please try again.");
            }
        }
    }

    public static void checkBookAvailability() {
        while (true) {
            System.out.print("Enter Book ID or Name to check availability: ");
            String input = sc.nextLine().trim();
            String[] foundBook = null;

            for (String[] b : books) {
                try {
                    if (b.length >= 5 && (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input))) {
                        foundBook = b;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error reading book data: " + e.getMessage());
                }
            }

            if (foundBook == null) {
                System.out.println("Book not found!");
            } else {
                try {
                    int copies = Integer.parseInt(foundBook[4]);
                    System.out.println("\n--- Book Details ---");
                    System.out.println("Book ID: " + foundBook[0]);
                    System.out.println("Book Name: " + foundBook[1]);
                    System.out.println("Author: " + foundBook[2]);
                    System.out.println("Section: " + foundBook[3]);
                    System.out.println("Available Copies: " + copies);
                } catch (Exception e) {
                    System.out.println("Error displaying book: " + e.getMessage());
                }
            }
            
     if (foundBook != null) {
    try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
        log.write(java.time.LocalDateTime.now() + " - Book availability checked: " + foundBook[1] + " by " + currentUserName);
        log.newLine();
    } catch (IOException e) {
        System.out.println("Could not write to log: " + e.getMessage());
    }
}
            System.out.print("Do you want to check another book? (yes/no): ");
            String choice = sc.nextLine().trim();
            if (!choice.equalsIgnoreCase("yes")) break;
        }
    }

    public static void displayBorrowLimit() {
        while (true) {
            String[] currentUser = null;
            for (String[] u : users) {
                if (u[0].equals(currentUserID)) {
                    currentUser = u;
                    break;
                }
            }

            if (currentUser == null) {
                System.out.println("User not found!");
                return;
            }

            int borrowLimit = currentUser[4].equalsIgnoreCase("Student") ? 3 : 5;
            int borrowedCount = Integer.parseInt(currentUser[5]);
            int remaining = borrowLimit - borrowedCount;

            System.out.println("Your borrow limit: " + borrowLimit + " books.");
            System.out.println("Books already borrowed: " + borrowedCount);
            System.out.println("You can still borrow: " + remaining + " books.");
            try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Borrow limit checked by: " + currentUserName);
            log.newLine();
}           catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
}

            System.out.print("Do you want to check borrow limit again? (yes/no): ");
            String choice = sc.nextLine().trim();
            if (!choice.equalsIgnoreCase("yes")) break;
        }
    }

    public static void displayRecords() {
        if (!isStaff()) {
            System.out.println("Access denied! Only Staff can view records.");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("record.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                found = true;
            }
            br.close();
            if (!found) System.out.println("No records found.");
        } catch (Exception e) {
            System.out.println("No record file found.");
        }
    }

  public static void exit() {
    while (true) {
        System.out.print("Do you really want to exit? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("\nThank you for using the Library Management System!");
            System.out.println("We hope to see you again soon. Have a great day! ");
            System.out.println("Exiting... Goodbye!");
            System.exit(0);
        } else if (confirm.equalsIgnoreCase("no")) {
            System.out.println("Returning to main menu...\n");
            return;
        } else {
            System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
        }
    }
}


    public static void loadUsers() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                users.add(line.split(","));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("users.txt not found. Starting with empty list.");
        }
    }

    public static void saveUsers() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String[] u : users) {
                bw.write(String.join(",", u));
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadBooks() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("books.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                books.add(line.split(","));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("books.txt not found. Starting with empty list.");
        }
    }

    public static void saveBooks() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"));
            for (String[] b : books) {
                bw.write(String.join(",", b));
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

 public static void createLogFile() {
    try {
        File logFile = new File("book_log.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    } catch (IOException e) {
        System.out.println("Could not create log file: " + e.getMessage());
    }
}
    public static void displayBooks() {
    try {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }

        System.out.println("\n--- All Books ---");
        System.out.printf("%-5s %-30s %-20s %-15s %-6s%n", "ID", "Name", "Author", "Section", "Copies");
        System.out.println("----------------------------------------------------------------------");

        for (String[] b : books) {
            System.out.printf("%-5s %-30s %-20s %-15s %-6s%n", b[0], b[1], b[2], b[3], b[4]);
        }

        // ----------- Log display -----------
        try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Books displayed to user " + currentUserName);
            log.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }

    } catch (Exception e) {
        System.out.println("Error displaying books: " + e.getMessage());
    }
}
public static void searchBook() {
    try {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }

        System.out.print("Enter Book ID, Name, or Author to search: ");
        String query = sc.nextLine().trim().toLowerCase();

        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty.");
            return;
        }

        boolean found = false;
        System.out.println("\n--- Search Results ---");
        System.out.printf("%-5s %-30s %-20s %-15s %-6s%n", "ID", "Name", "Author", "Section", "Copies");
        System.out.println("----------------------------------------------------------------------");

        for (String[] b : books) {
            if (b[0].toLowerCase().contains(query) || b[1].toLowerCase().contains(query) || b[2].toLowerCase().contains(query)) {
                System.out.printf("%-5s %-30s %-20s %-15s %-6s%n", b[0], b[1], b[2], b[3], b[4]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found matching: " + query);
        }

        // ----------- Log search -----------
        try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Book search performed by " + currentUserName + " for: " + query);
            log.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }

    } catch (Exception e) {
        System.out.println("Error searching books: " + e.getMessage());
    }
}
public static void sortBooks() {
    // Only staff can sort books
    if (!isStaff()) {
        System.out.println("Only staff can sort the books!");
        return;
    }

    try {
        if (books.isEmpty()) {
            System.out.println("No books available to sort.");
            return;
        }

        System.out.println("\nSort books by:");
        System.out.println("1. Name");
        System.out.println("2. Author");
        System.out.println("3. ID");
        System.out.println("4. Copies");
        System.out.print("Enter your choice: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number from 1-4.");
            return;
        }

        Comparator<String[]> comparator;

        switch (choice) {
            case 1: comparator = Comparator.comparing(b -> b[1].toLowerCase()); break;
            case 2: comparator = Comparator.comparing(b -> b[2].toLowerCase()); break;
            case 3: comparator = Comparator.comparing(b -> b[0]); break;
            case 4: comparator = Comparator.comparingInt(b -> Integer.parseInt(b[4])); break;
            default:
                System.out.println("Invalid choice. Please enter 1-4.");
                return;
        }

        books.sort(comparator);

        // Save the sorted list
        saveBooks();

        System.out.println("Books sorted successfully!");
        displayBooks(); // Show sorted list

        // Log the sorting action
        try (BufferedWriter log = new BufferedWriter(new FileWriter("book_log.txt", true))) {
            log.write(java.time.LocalDateTime.now() + " - Books sorted by choice " + choice + " by user " + currentUserName);
            log.newLine();
        } catch (IOException e) {
            System.out.println("Could not write to log: " + e.getMessage());
        }

    } catch (Exception e) {
        System.out.println("Error sorting books: " + e.getMessage());
    }
}
}
