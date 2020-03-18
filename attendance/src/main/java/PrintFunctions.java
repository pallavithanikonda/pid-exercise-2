/**
 * This class represents the text user interface.
 */
public class PrintFunctions {


    public PrintFunctions(){
    }

    public void printWelcome() {
        System.out.println("Welcome to Attendance application!\n");
    }

    /**
     * Print main menu.
     */
    public void printOptions(){
        System.out.println("Pick an option to perform actions:");
        String[] actions = {
                "(1) Load member file",
                "(2) Take attendance",
                "(3) Display attendance",
                "(4) Edit member file",
                "(5) Quit"
        };
        for (int i = 0; i < actions.length; i++) {
            System.out.println(actions[i]);
        }
    }

    /**
     * Print edit menu.
     */
    public void printEditOptions(){
        System.out.println("Pick an option:");
        String[] actions = {
                "(1) Add a member",
                "(2) Remove a member"
        };
        for (int i = 0; i < actions.length; i++) {
            System.out.println(actions[i]);
        }
    }

    public void printReturnMenu() {
        System.out.println("Please, press Enter to return to the menu options.");
    }

    public void printLoadMessage() {
        System.out.println("Members List loaded into the application.");
    }

    public void printSaveMessage() {
        System.out.println("List saved in the file.");
    }

    public void printDateQuestion() {
        System.out.println("Insert a date (YYYY-MM-DD) to take or Display the attendance:");
    }

    public void printInvalidInput() {
        System.out.println("Invalid input.");
    }

    public void printAttendanceMessage() {
        System.out.println("Take the attendance by pressing Y/N:");
    }


    public void printNoMembersInList() {
        System.out.println("You need to load members into the application first.");
    }

    public void printNoAttendancesInList() {
        System.out.println("There are no attendance sheets to check.");
    }

    public void printAttendanceComplete(int present, int absent) {
        System.out.println("Attendance complete. From the " + (present + absent) + " people in the list, "
                                + present + " where present, while "
                                + absent + " where absent.");
    }


    public void printDuplicateIdMessage() {
        System.out.println("WARNING: the list as members with duplicate ids!");
    }

    public void printNoDirectory() {
        System.out.println("Could not create directory. File not saved.");
    }

    public void printEmptyFile() {
        System.out.println("Could not retrieve data. File is empty.");
    }

    public void printFileNotFound() {
        System.out.println("File not found.");
    }

    public void printSelectIndex() {
        System.out.println("Select the index of a member.");
    }

    public void printMemberRemoved() {
        System.out.println("Member was successfully removed.");
    }

    /**
     * Print a message stating that the field is empty.
     */
    public void printFieldEmpty() {
        System.out.println(">> Field cannot be empty.");
        System.out.println(">> Press (Enter) to continue or (0) to return to main menu.");
    }
}