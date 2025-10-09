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

            case "remove" -> {
                // if the user typed 'remove' without the ID
                if (args.length < 2) {
                    System.out.println("Usage: remove <id>");
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    if (manager.deleteJournalEntry(id)) {
                        System.out.println("Journal entry with ID: " + args[1] + " has been removed.");
                    } else{
                        System.out.println("Could not find entry with ID: " + args[1]);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("ID must be an integer.");
                } catch (IllegalStateException ex) {
                    System.out.println("Delete failed: " + ex.getMessage());
                }
            }

            case "list" -> {
                List<JournalEntry> entries = manager.listJournalEntries();
                if (entries.isEmpty()) {
                    System.out.println("No entries yet.");
                } else {
                    System.out.println("Journal Entries:");
                    for (JournalEntry entry : entries) {
                        printFullEntry(entry);
                    }
                }
            }
            case "searchKey" -> {
                if (args.length < 2) {
                    System.out.println("Usage: searchKey <keyword>");
                    return;
                }

                String keyword = args[1];

                try {
                    List<JournalEntry> results = manager.searchByKeyword(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No matches for: " + keyword);
                    } else {
                        System.out.println("Journal Entries containing: " + keyword);
                        for (JournalEntry e : results) {
                            printFullEntry(e);
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Search error: " + ex.getMessage());
                }
            }

            case "searchDate" -> {
                if (args.length < 2) {
                    System.out.println("Usage: searchDate <YYYY-MM-DD>");
                    return;
                }

                try {
                    LocalDate date = LocalDate.parse(args[1]);
                    List<JournalEntry> results = manager.searchByDate(date);

                    if (results.isEmpty()) {
                        System.out.println("No entries found on " + date);
                    } else {
                        System.out.println("Journal Entries from: " + date);
                        for (JournalEntry e : results) {
                            printFullEntry(e);
                        }
                    }
                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date format. Use YYYY-MM-DD.");
                }
            }

            case "searchID" -> { // Todo Show entries using an ID
                System.out.println("Searching for journal entry by ID");
            }
            default -> printHelp();
        }
    }

    private static void printFullEntry(JournalEntry entry) {
        System.out.println("-----------------------------------------");
        System.out.println("ID: " + entry.id());
        System.out.println("Date: " + entry.timestamp());
        System.out.println("Title: " + entry.title());
        System.out.println();
        System.out.println(entry.content());
        System.out.println("-----------------------------------------");
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
