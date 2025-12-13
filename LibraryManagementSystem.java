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
                System.out.print("Do you want to exit? (yes/no): ");
                String exitChoice = sc.nextLine().trim();
                if (exitChoice.equalsIgnoreCase("yes")) {
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                }
            } else {
                System.out.println("Invalid choice. Enter again.");
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
            System.out.println("7. Exit");
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
                case 7: exit(); break;
                default: System.out.println("Enter correct option!");
            }
        }
    }

    public static void addBook() {
        if (!isStaff()) {
            System.out.println("Only Staff can add books!");
            return;
        }

        System.out.print("Enter Book ID: ");
        String id = sc.nextLine().trim();

        System.out.print("Enter Book Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Author Name: ");
        String author = sc.nextLine().trim();

        System.out.print("Enter Number of Copies: ");
        String copies = sc.nextLine().trim();

        System.out.print("Enter Book Category (History/English/CS/Science/etc.): ");
        String category = sc.nextLine().trim();

        String[] newBook = {id, name, author, copies, category};
        books.add(newBook);
        saveBooks();

        System.out.println("Book added successfully!");
    }

    public static void removeBook() {
        if (!isStaff()) {
            System.out.println("Only Staff can remove books!");
            return;
        }

        System.out.print("Enter Book ID or Name to remove: ");
        String input = sc.nextLine().trim();
        String[] foundBook = null;

        for (String[] b : books) {
            if (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input)) {
                foundBook = b;
                break;
            }
        }

        if (foundBook != null) {
            books.remove(foundBook);
            saveBooks();
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    public static void issueBook() {
        System.out.println("Issue Book feature under process.");
    }

    public static void returnBook() {
        while (true) {
            System.out.print("Enter Book ID or Book Name to return: ");
            String input = sc.nextLine().trim();
            String[] foundBook = null;

            for (String[] b : books) {
                if (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input)) {
                    foundBook = b;
                    break;
                }
            }

            if (foundBook == null) {
                System.out.println("Book not found!");
            } else {
                int copies = Integer.parseInt(foundBook[3]);
                foundBook[3] = String.valueOf(copies + 1);

                for (String[] u : users) {
                    if (u[0].equals(currentUserID)) {
                        int count = Integer.parseInt(u[5]);
                        if (count > 0) u[5] = String.valueOf(count - 1);
                        break;
                    }
                }

                saveBooks();
                saveUsers();
                System.out.println("Book returned successfully!");
            }

            System.out.print("Do you want to return another book? (yes/no): ");
            String choice = sc.nextLine().trim();
            if (!choice.equalsIgnoreCase("yes")) break;
        }
    }

    public static void checkBookAvailability() {
        while (true) {
            System.out.print("Enter Book ID, Name or Category to check availability: ");
            String input = sc.nextLine().trim();
            ArrayList<String[]> foundBooks = new ArrayList<>();

            for (String[] b : books) {
                if (b[0].equalsIgnoreCase(input) || b[1].equalsIgnoreCase(input) || b[4].equalsIgnoreCase(input)) {
                    foundBooks.add(b);
                }
            }

            if (foundBooks.isEmpty()) {
                System.out.println("No books found!");
            } else {
                for (String[] foundBook : foundBooks) {
                    System.out.println("\n--- Book Details ---");
                    System.out.println("Book ID: " + foundBook[0]);
                    System.out.println("Book Name: " + foundBook[1]);
                    System.out.println("Author: " + foundBook[2]);
                    System.out.println("Available Copies: " + foundBook[3]);
                    System.out.println("Category: " + foundBook[4]);
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

            System.out.print("Do you want to check borrow limit again? (yes/no): ");
            String choice = sc.nextLine().trim();
            if (!choice.equalsIgnoreCase("yes")) break;
        }
    }

    public static void exit() {
        System.out.print("Do you really want to exit? (yes/no): ");
        String confirm = sc.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("Exiting... Goodbye!");
            System.exit(0);
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
            System.out.println("Error saving users.");
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
            System.out.println("Error saving books.");
        }
    }
}
