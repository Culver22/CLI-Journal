package journal;
import java.time.LocalDateTime;

public record JournalEntry(
        int id,
        String title,
        String content,
        LocalDateTime timestamp
) { }