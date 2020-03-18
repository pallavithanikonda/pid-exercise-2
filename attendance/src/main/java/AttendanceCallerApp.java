import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This is the main class of the application for Attendance.
 */
public class AttendanceCallerApp {
    private HashMap<LocalDate, ArrayList<AttendanceHandler>> attendances;
    private ArrayList<Member> members;
    private PrintFunctions printFunctions;
    private JSONFileManager fm;
    private String membersFileName;


    public AttendanceCallerApp() {
        attendances = new HashMap<>();
        members = new ArrayList<>();
        printFunctions = new PrintFunctions();
        fm = new JSONFileManager();
        membersFileName = null;
    }

    private void start() {
        printFunctions.printWelcome();
        while (true) {
            printFunctions.printOptions();
            switch (userInput()) {
                case "1":
                    //Load members file
                    loadMemberList();
                    if(!UtilityFunctions.checkDuplicateId(members)){
                        printFunctions.printDuplicateIdMessage();
                    }
                    break;
                case "2":
                    //Check attendance
                    checkAttendance();
                    break;
                case "3":
                    //Load attendance from file
                    loadAttendanceList();
                    // Display attendance
                    printAttendance();
                    break;
                case "4":
                    //Add or remove members from a member file
                    editMenu();
                    break;
                case "5":
                    //quit
                    return;
                default:
                    printFunctions.printInvalidInput();
            }
            printFunctions.printReturnMenu();
            userInput();
        }
    }

    /**
     * Shows the edit member file menu.
     */
    private void editMenu(){
        if(members.size() == 0){
            printFunctions.printNoMembersInList();
        }
        else{
            while (true) {
                printFunctions.printEditOptions();
                switch (userInput()) {
                    case "1":
                        addMember(membersFileName);
                        return;
                    case "2":
                        removeMember(printMembersAndSelectIndex(), membersFileName);
                        return;
                    default:
                        printFunctions.printInvalidInput();
                }
                printFunctions.printReturnMenu();
                userInput();
            }
        }
    }

    private void removeMember(int index, String fileName){
        if(index != -1){
            //remove member
            members.remove(index-1);
            // Save members to file
            fm.writeJsonToFile(fm.convertMembersToJson(members),
                    "member-lists/",fileName);
            printFunctions.printMemberRemoved();
        }
        else{
            printFunctions.printInvalidInput();
        }
    }

    private void addMember(String fileName) {
        String quit = ""; //backup for user to be able to quit add mode

        //create an array with the fields to be inserted
        //and another to capture the data inserted
        String[] memberDetails = new String[2];
        String[] memberQuestions = new String[2];
        memberQuestions[0] = ">> Insert member name:";
        memberQuestions[1] = ">> Insert member ID:";

        //Print insert statements and collect user input
        for (int index = 0; index < memberQuestions.length; index++) {
            boolean fieldIsValid = false;
            while (!fieldIsValid) {
                System.out.println(memberQuestions[index]);

                String input = userInput();

                //Check if field is empty
                if (input.isEmpty()) {
                    printFunctions.printFieldEmpty();
                    quit = userInput();
                } else { //For not empty fields
                    memberDetails[index] = input;
                    fieldIsValid = true;
                }

                if (quit.equals("0")) {
                    return;
                }
            }
        }
        for (Member member:members){
            if(member.getMemberID().equals(memberDetails[1])){
                System.out.println("The Member Id "+member.getMemberID()+" already available, please select unique ID");
                return;
            }

        }
        //add to list
        members.add(new Member(memberDetails[0], memberDetails[1]));

        // Save members to file
        fm.writeJsonToFile(fm.convertMembersToJson(members),
                "member-lists/",fileName);
    }

    /**
     * Loads a specific file with members list.
     */
    private void loadMemberList(){
        members.clear();
        members.addAll(fm.readFileMembers("member-lists/member-list.json"));

        if(members.size() > 0)
        {
            //update members file name
            membersFileName = "member-list.json";
            //print success message
            printFunctions.printLoadMessage();
        }
    }

    /**
     * Loads the attendance list.
     */
    private void loadAttendanceList(){
        attendances.clear();
        attendances.putAll(fm.readFileAttendance("attendance-lists/attendance-list.json"));
    }

    /**
     * Method that allows user to check attendance for a specific day.
     */
    private void checkAttendance(){
        // if a member file was loaded check attendance
        if(members.size() == 0){
            printFunctions.printNoMembersInList();
        }
        else {
            printFunctions.printDateQuestion();
            LocalDate date = UtilityFunctions.convertDate(userInput());
            if (date != null) {
                //Load attendances from file
                loadAttendanceList();
                //Check attendance
                loopAttendance(date);
                // Save attendance to file
                fm.writeJsonToFile(fm.convertAttendanceToJson(attendances),
                        "attendance-lists/","attendance-list.json");
                printFunctions.printSaveMessage();
            } else {
                printFunctions.printInvalidInput();
            }
        }
    }

    /**
     * Loops through members list and takes attendance.
     * @param date date of the day from which the user should check attendance.
     */
    private void loopAttendance(LocalDate date) {
        ArrayList<AttendanceHandler> attendance = new ArrayList<>();
        int present = 0;
        int absent = 0;
        for(Member member : members){
            boolean quit = false;
            while (!quit){
                //print the member
                System.out.println(member.getMemberID() + ", " + member.getMemberName());

                //print message
                printFunctions.printAttendanceMessage();

                switch (userInput().toLowerCase()){
                    case "y":
                        attendance.add(new AttendanceHandler(member, true));
                        quit = true;
                        present++;
                        break;
                    case "n":
                        attendance.add(new AttendanceHandler(member, false));
                        quit = true;
                        absent++;
                        break;
                    default:
                        printFunctions.printInvalidInput();
                        break;
                }
            }
        }

        attendances.put(date,attendance);

        printFunctions.printAttendanceComplete(present, absent);
    }

    /**
     * Collect user input.
     * @return user input as a String.
     */
    public String userInput() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    /**
     * Prints the attendance list for a given date.
     */
    public void printAttendance(){
        // if is there any data in the attendance file
        if(attendances.size() == 0){
            printFunctions.printNoAttendancesInList();
        }
        else {
            //show list of dates
            System.out.println("We have attendance for the below dates");
            attendances.entrySet().stream()
                    .map(HashMap.Entry::getKey)
                    .forEach(System.out::println);

            //User chooses a date
            //If it is valid show attendance for that day
            printFunctions.printDateQuestion();
            LocalDate date = UtilityFunctions.convertDate(userInput());
            if (date != null) {
                attendances.entrySet().stream()
                        .filter(map -> map.getKey().isEqual(date))
                        .map(HashMap.Entry::getValue)
                        .flatMap(ArrayList::stream)
                        .forEach(System.out::println);
            } else {
                printFunctions.printInvalidInput();
            }
        }
    }

    /**
     * Prints the members list and selects an index.
     * @return an int which is the index of the member in the list.
     */
    public int printMembersAndSelectIndex() {
        // if is there any data in the member file
        if (members.size() == 0) {
            printFunctions.printNoMembersInList();
        }
        else{
            members.stream()
                    .map(member -> "index = '" + (members.indexOf(member) + 1) + "', " + member.toString())
                    .forEach(System.out::println);

            printFunctions.printSelectIndex();
        }

        return UtilityFunctions.convertIndexToInt(userInput());
    }

    /**
     * Start of the application.
     * @param args
     */
    public static void main(String[] args) {
        AttendanceCallerApp attendanceCallerApp = new AttendanceCallerApp();
        attendanceCallerApp.start();
    }
}