
import java.io.*;
import java.util.*;

public class ContactDirectory {

    static class Contact {
        private String name;
        private String phone;
        private String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        String getName() { return name; }
        String getPhone() { return phone; }
        String getEmail() { return email; }
        void setPhone(String phone) { this.phone = phone; }

        public String toString() {
            return name + " | " + phone + " | " + email;
        }
    }

    static ArrayList<Contact> contacts = new ArrayList<>();
    static HashMap<String, Contact> phoneMap = new HashMap<>();
    static final String FILE = "contacts.txt";
    static Scanner sc = new Scanner(System.in);

    static String read(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    static boolean isValidPhone(String phone) {
        if (phone.length() != 10) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static Contact findByName(String name) {
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    static void loadFromFile() {
        File file = new File(FILE);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Contact c = new Contact(parts[0], parts[1], parts[2]);
                    contacts.add(c);
                    phoneMap.put(parts[1], c);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Could not read " + FILE);
        }
    }

    static void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE));
            for (Contact c : contacts) {
                writer.println(c.getName() + "," + c.getPhone() + "," + c.getEmail());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save " + FILE);
        }
    }

    static void addContact() {
        String name = read("Name: ");
        String phone = read("Phone: ");
        String email = read("Email: ");

        if (!isValidPhone(phone)) {
            System.out.println("Invalid phone number. Enter 10 digits.");
            return;
        }
        if (phoneMap.containsKey(phone)) {
            System.out.println("Duplicate phone number not allowed.");
            return;
        }
        Contact c = new Contact(name, phone, email);
        contacts.add(c);
        phoneMap.put(phone, c);
        System.out.println("Contact added. " + c);
    }

    static void displayAll() {
        if (contacts.isEmpty()) {
            System.out.println("Directory is empty.");
            return;
        }
        for (Contact c : contacts) {
            System.out.println(c);
        }
    }

    static void searchContact() {
        String how = read("Search by 1. Name 2. Phone: ");
        if (how.equals("1")) {
            String key = read("Enter name: ").toLowerCase();
            boolean found = false;
            for (Contact c : contacts) {
                if (c.getName().toLowerCase().contains(key)) {
                    System.out.println("Found: " + c);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Contact not found.");
            }
        } else if (how.equals("2")) {
            Contact c = phoneMap.get(read("Enter phone: "));
            if (c != null) {
                System.out.println("Found: " + c);
            } else {
                System.out.println("Contact not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    static void updatePhone() {
        Contact c = findByName(read("Name of contact: "));
        if (c == null) {
            System.out.println("Contact not found.");
            return;
        }
        String newPhone = read("New phone: ");
        if (!isValidPhone(newPhone)) {
            System.out.println("Invalid phone number. Enter 10 digits.");
            return;
        }
        if (phoneMap.containsKey(newPhone)) {
            System.out.println("Duplicate phone number not allowed.");
            return;
        }
        phoneMap.remove(c.getPhone());
        c.setPhone(newPhone);
        phoneMap.put(newPhone, c);
        System.out.println("Contact updated. " + c.getName() + " | " + newPhone);
    }

    static void deleteContact() {
        Contact c = findByName(read("Name of contact: "));
        if (c == null) {
            System.out.println("Contact not found.");
            return;
        }
        contacts.remove(c);
        phoneMap.remove(c.getPhone());
        System.out.println("Contact deleted.");
        if (contacts.isEmpty()) {
            System.out.println("Directory is now empty.");
        }
    }

    public static void main(String[] args) {
        loadFromFile();
        String choice = "";

        while (!choice.equals("6")) {
            System.out.println("\n===== STUDENT CONTACT DIRECTORY =====");
            System.out.println("1. Add 2. Display 3. Search 4. Delete 5. Update     6. Exit");
            if (!sc.hasNextLine()) {
                break;
            }
            choice = read("Enter choice: ");

            if (choice.equals("1")) {
                addContact();
            } else if (choice.equals("2")) {
                displayAll();
            } else if (choice.equals("3")) {
                searchContact();
            } else if (choice.equals("4")) {
                deleteContact();
            } else if (choice.equals("5")) {
                updatePhone();
            } else if (!choice.equals("6")) {
                System.out.println("Invalid choice. Enter 1 to 6.");
            }
        }
        saveToFile();
        System.out.println("Contacts saved. Program ended.");
    }
}
