public class Main {
    public static void main(String[] args) {
        if (args.length == 0) { // No Argument, print the help string of acceptable args
            printHelp();
            return;
        }

        switch (args[0]) {
            case "add" -> { //Todo add prompt for title + entry
                System.out.println("Adding a new journal entry");
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
