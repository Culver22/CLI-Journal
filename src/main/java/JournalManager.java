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

    public boolean deleteJournalEntry(int id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id() == id) {
                data.remove(i);  // safe to remove by index
                return true;
            }
        }
        return false;
    }

    public List<JournalEntry> listJournalEntries() {
        List<JournalEntry> sortedJournalEntries = new ArrayList<>(data);
        // Compares each Journal entry into descending order (latest first)
        sortedJournalEntries.sort((a, b) -> b.timestamp().compareTo(a.timestamp()));
        return sortedJournalEntries;
    }

    public List<JournalEntry> searchByKeyword(String keyword) {
        List<JournalEntry> filteredJournalEntries = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (JournalEntry entry : data) { // Loop through all entries
            if (entry.title().toLowerCase().contains(lowerCaseKeyword)
                    || entry.content().toLowerCase().contains(lowerCaseKeyword)) {
                // if either title or content contains keyword add to the filtered list
                filteredJournalEntries.add(entry);
            }
        }
        return filteredJournalEntries;
    }

    public List<JournalEntry> searchByDate(String date) {
        List<JournalEntry> filteredJournalEntries = new ArrayList<>();
        for (JournalEntry entry : data) {
            if (entry.timestamp().toLocalDate().equals(date)) { // Compare date given to timestamps
                filteredJournalEntries.add(entry);
            }
        }
        return filteredJournalEntries;
    }
}
