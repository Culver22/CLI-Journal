import java.time.LocalDateTime;
import java.util.*;

public class JournalManager {
    private final List<JournalEntry> data = new ArrayList<>();
    private int nextId = 0;

    public JournalEntry addJournalEntry(String title, String content) {
        if (title == null || title.isBlank()) {
            // Throw exception if title is blank or null
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null || content.isBlank()) {
            // Throw exception if the journal content is blank or null
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        JournalEntry entry = new JournalEntry(
                nextId ++, // increase the ID value
                title,
                content,
                LocalDateTime.now()
        );
        data.add(entry);
        return entry;
    }
}
