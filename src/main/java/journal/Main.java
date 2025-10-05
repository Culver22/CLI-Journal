package journal;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JournalStorage storage = new JournalStorage(Path.of("journal.txt"));
        JournalManager manager = new JournalManager(storage);

        if (args.length == 0) { // No Argument, print the help string of acceptable args
            printHelp();
            return;
        }

        switch (args[0]) {
            case "add" -> {
                Scanner sc = new Scanner(System.in);

                System.out.print("Title: ");
                String title = sc.nextLine();

                System.out.println("Content (Press ENTER TWICE to Finish):");
                StringBuilder content = new StringBuilder();
                int emptyCount = 0;

                while (true) {
                    String line = sc.nextLine();
                    if (line.isBlank()) {
                        emptyCount++;
                        if (emptyCount == 2) {
                            break; // stop after two consecutive blank lines
                        }
                    } else {
                        emptyCount = 0; // reset if user typed something
                        content.append(line).append(System.lineSeparator());
                    }
                }

                if (content.isEmpty()) { // Prevent saving empty entries
                    System.out.println("No content entered. Entry discarded.");
                    return;
                }

                try {
                    JournalEntry saved = manager.addJournalEntry(title, content.toString().trim());
                    System.out.println("Saved entry with ID: " + saved.id());
                } catch (IllegalArgumentException ex) {
                    System.out.println("Could not add entry: " + ex.getMessage());
                }
            }
            case "remove" -> { //Todo remove entry from file
                System.out.println("Removing a new journal entry");
            }
            case "list" -> { //Todo read from file and print all entries
                System.out.println("Listing all journal entries");
            }
            case "searchKey" -> { // Todo Show entries using a keyword
                System.out.println("Searching for journal entry by keyword");
            }
            case "searchDate" -> { //Todo Show entries using a Date
                System.out.println("Searching for journal entry by date");
            }
            case "searchID" -> { // Todo Show entries using an ID
                System.out.println("Searching for journal entry by ID");
            }
            default -> printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("""
                CLI-Journal Commands:
                    add        - create a new journal entry
                    remove     - remove an entry from the file
                    list       - list all journal entries
                    searchKey  - search for entries containing a keyword
                    searchDate - search for entries created on a date
                    searchID   - search for a specific entry by its ID
                """);
    }
}
