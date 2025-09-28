import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                }
                else {
                    blockLines.add(line); // collect lines
            }

        }
            if (!blockLines.isEmpty()) { // at the end of the file - no separator
                parseBlock(blockLines,journalEntries);
            }
    }
        catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return journalEntries;


}
