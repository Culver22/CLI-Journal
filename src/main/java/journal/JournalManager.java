package journal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class JournalManager {
    private final List<JournalEntry> data = new ArrayList<>();
    private final JournalStorage storage;
    private int nextId = 1;

    public JournalManager(JournalStorage storage) {
        this.storage = storage;  // Load existing entries for file

        List<JournalEntry> fromFile = storage.loadAll();
        data.addAll(fromFile);

        if (!data.isEmpty()) {
            nextId = data.stream()
                    .mapToInt(JournalEntry::id) // For every Journal entry ID
                    .max() // Find the highest ID
                    .getAsInt() + 1; // set next ID to the highest ID + 1
        }
    }

    public JournalEntry addJournalEntry(String title, String content) {
        if (title == null || title.isBlank()) {
            // Throw exception if title is blank or null
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null || content.isBlank()) {
            // Throw exception if the journal content is blank or null
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        JournalEntry entry = new JournalEntry( nextId++, title, content, LocalDateTime.now());

        data.add(entry);
        storage.append(entry);
        return entry;
    }

    public boolean deleteJournalEntry(int id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).id() == id) {
                data.remove(i);  // safe to remove by index
                storage.deleteById(id);
                return true;
            }
        }
        return false;
    }

    public List<JournalEntry> listJournalEntries() {
        List<JournalEntry> sortedJournalEntries = new ArrayList<>(data);
        // Compares each Journal entry into descending order (latest first)
        sortedJournalEntries.sort((a, b) -> b.timestamp().compareTo(a.timestamp()));
        return Collections.unmodifiableList(sortedJournalEntries);
    }

    public List<JournalEntry> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        List<JournalEntry> filteredJournalEntries = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (JournalEntry entry : data) { // Loop through all entries
            if (entry.title().toLowerCase().contains(lowerCaseKeyword)
                    || entry.content().toLowerCase().contains(lowerCaseKeyword)) {
                // if either title or content contains keyword add to the filtered list
                filteredJournalEntries.add(entry);
            }
        }
        filteredJournalEntries.sort((a, b) -> b.timestamp().compareTo(a.timestamp()));
        return filteredJournalEntries;
    }

    public List<JournalEntry> searchByDate(LocalDate date) {
        List<JournalEntry> filteredJournalEntries = new ArrayList<>();
        for (JournalEntry entry : data) {
            if (entry.timestamp().toLocalDate().equals(date)) { // Compare date given to timestamps
                filteredJournalEntries.add(entry);
            }
        }
        return filteredJournalEntries;
    }

    public Optional<JournalEntry> searchById(int id) {
        // no need for a list as IDs are unique to each entry. Hence, one result
        for (JournalEntry entry : data) {
            if (entry.id() == id) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }
}
