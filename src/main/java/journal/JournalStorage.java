package journal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JournalStorage {
    private static final String EntrySeparator = "---";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path file;

    public JournalStorage(Path file) {
        this.file = file;
    }

    public List<JournalEntry> loadAll() {
        List<JournalEntry> journalEntries = new ArrayList<>();
        if (!Files.exists(file)) {
            // return empty list if file doesn't exist
            return journalEntries;
        }

        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            List<String> blockLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith(EntrySeparator)) { // end of block
                    parseBlock(blockLines, journalEntries);  // ToDo add parseBlock method
                    blockLines.clear(); // reset for next block
                } else {
                    blockLines.add(line); // collect lines
                }

            }
            if (!blockLines.isEmpty()) { // at the end of the file - no separator
                parseBlock(blockLines, journalEntries);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return journalEntries;


    }

    private void parseBlock(List<String> blockLines, List<JournalEntry> journalEntries) {
        if (blockLines.isEmpty()) return;

        try {
            String[] parts = blockLines.getFirst().split("\\|", 3);  // Split at every '|' into 3 parts
            if (parts.length < 3) return; // Incomplete block - skip

            int id = Integer.parseInt(parts[0].trim());
            // First part is the ID which is parsed as an integer

            LocalDateTime time = LocalDateTime.parse(parts[1].trim(), formatter);
            // Second is time, which is parsed as date and time

            String title = parts[2].trim();
            // Thirdly the title which is parsed as a string

            StringBuilder content = new StringBuilder();
            // Start building the content of the entry, by concatenating each line after the header
            for (int i = 1; i < blockLines.size(); i++) {
                content.append(blockLines.get(i)).append(System.lineSeparator());
            }

            journalEntries.add(new JournalEntry(id, title, content.toString().trim(), time));

        } catch (Exception e) {
            System.out.println("Skipping malformed entry block: " + e.getMessage());
        }
    }

    public void append(JournalEntry journalEntry) {
        String block = journalEntry.id() + "|"
                + journalEntry.timestamp().format(formatter) + "|"
                + journalEntry.title() + System.lineSeparator()
                + journalEntry.content() + System.lineSeparator()
                + EntrySeparator + System.lineSeparator();

        try {
            // Create the file if it doesn't already exist
            if (!Files.exists(file)) {
                Files.createFile(file);
            }

            // Append the entry text to the end of the file
            Files.writeString(
                    file, block, StandardCharsets.UTF_8, StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.out.println("Error writing entry to file: " + e.getMessage());
        }
    }
}
