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
                    .orElse(0) + 1;
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
                // remove from memory *after* storage confirms
                boolean removedFromFile = storage.deleteById(id);
                if (removedFromFile) {
                    data.remove(i);
                    return true;
                } else {
                    throw new IllegalStateException("Failed to delete entry " + id + " from storage, keeping in memory");
                }
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
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        return data.stream()
                .filter(e -> e.title().toLowerCase().contains(keyword.toLowerCase())
                        || e.content().toLowerCase().contains(keyword.toLowerCase()))
                .sorted((a, b) -> b.timestamp().compareTo(a.timestamp()))
                .toList();
    }

    public List<JournalEntry> searchByDate(LocalDate date) {
        return data.stream()
                .filter(e -> e.timestamp().toLocalDate().equals(date)) // Compare date given to timestamps
                .sorted((a, b) -> b.timestamp().compareTo(a.timestamp()))
                .toList();
    }

    public Optional<JournalEntry> searchById(int id) {
        // no need for a list as IDs are unique to each entry. Hence, one result
        return data.stream()
                .filter(e -> e.id() == id)
                .findFirst();
    }
}
