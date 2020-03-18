import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A class that reads and writes JSON files.
 */
public class JSONFileManager {
    PrintFunctions printFunctions;

    public JSONFileManager(){
        printFunctions = new PrintFunctions();
    }


    public ArrayList<Member> readFileMembers(String membersFile){
        ArrayList<Member> jsonMembers = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(membersFile)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // loop members array
            JSONArray members = (JSONArray) jsonObject.get("members");
            Iterator<String> iterator = members.iterator();
            while (iterator.hasNext()) {
                Object slide = iterator.next();
                JSONObject jsonObject2 = (JSONObject) slide;
                String name = (String) jsonObject2.get("name");
                String id = (String) jsonObject2.get("id");
                Member member = new Member(name, id);
                jsonMembers.add(member);
            }

        } catch (IOException e) {
            printFunctions.printFileNotFound();
        } catch (ParseException e) {
            printFunctions.printEmptyFile();
        }

        return jsonMembers;
    }


    public JSONObject convertMembersToJson(ArrayList<Member> members){
        //Main JSON object
        JSONObject object = new JSONObject();

        //Create a JSON array
        JSONArray array = new JSONArray();

        for(int index = 0; index < members.size(); index++)
        {
            //Second JSON object with an array
            JSONObject arrayElement = new JSONObject();
            arrayElement.put("name", members.get(index).getMemberName());
            arrayElement.put("id", members.get(index).getMemberID());

            //insert in the array
            array.add(arrayElement);
        }

        //put everything inside the object
        object.put("members", array);

        return object;
    }


    public void writeJsonToFile(JSONObject object, String filePath, String fileName){
        //Check if directory exists, otherwise create it
        File directory = new File(filePath);
        if(!directory.isDirectory()){
            boolean createDirectory = directory.mkdir();
            if(!createDirectory){
                printFunctions.printNoDirectory();
                return;
            }
        }

        //Write on the file
        try (FileWriter file = new FileWriter(filePath + fileName)) {
            file.write(object.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<LocalDate, ArrayList<AttendanceHandler>> readFileAttendance(String attendanceFile){
        HashMap<LocalDate, ArrayList<AttendanceHandler>> attendancesMap = new HashMap<>();

        //Parse the json file
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(attendanceFile)) {

            JSONObject mainJsonObject = (JSONObject) parser.parse(reader);

            JSONArray attendances = (JSONArray) mainJsonObject.get("attendances");

            //Iterate the main object to retrieve the date and attendance list
            Iterator mainIterator = attendances.iterator();
            while (mainIterator.hasNext()){
                Object mainSlide = mainIterator.next();
                JSONObject jsonObject = (JSONObject) mainSlide;

                String date = (String) jsonObject.get("date");

                JSONArray attendance = (JSONArray) jsonObject.get("attendance");

                //Iterate through the attendance list to retrieve the member
                //and attendance status
                ArrayList<AttendanceHandler> jsonAttendance = new ArrayList<>();
                Iterator iterator = attendance.iterator();
                while (iterator.hasNext()) {
                    Object slide = iterator.next();
                    JSONObject jsonObject2 = (JSONObject) slide;

                    String name = (String) jsonObject2.get("name");

                    String id = (String) jsonObject2.get("id");

                    boolean attended = (boolean) jsonObject2.get("attended");

                    //Create a new member
                    Member member = new Member(name, id);

                    //create an attendance
                    AttendanceHandler at = new AttendanceHandler(member, attended);
                    jsonAttendance.add(at);
                }

                attendancesMap.put(UtilityFunctions.convertDate(date), jsonAttendance);
            }

        } catch (IOException e) {
            printFunctions.printFileNotFound();
        } catch (ParseException e) {
            printFunctions.printEmptyFile();
        }

        return attendancesMap;
    }

    public JSONObject convertAttendanceToJson(HashMap<LocalDate, ArrayList<AttendanceHandler>> attendances){
        //Main JSON object
        JSONObject mainObject = new JSONObject();

        //Create a JSON array
        JSONArray mainArray = new JSONArray();

        // iterating over a map
        for(HashMap.Entry<LocalDate, ArrayList<AttendanceHandler>> attendanceByDate : attendances.entrySet()){
            //JSON object inside the main an array
            JSONObject arrayElement = new JSONObject();

            //Add date and another array to arrayElement
            arrayElement.put("date", attendanceByDate.getKey().toString());

            //Create a JSON array to insert inside the arrayElement
            JSONArray array = new JSONArray();

            // iterating over the array list
            int index = 0;
            for(AttendanceHandler attendance : attendanceByDate.getValue()){
                JSONObject attendanceElement = new JSONObject();
                attendanceElement.put("name", attendance.getMember().getMemberName());
                attendanceElement.put("id", attendance.getMember().getMemberID());
                attendanceElement.put("attended", attendance.isHasAttended());
                array.add(attendanceElement);
                index++;
            }
            //insert in the main array
            arrayElement.put("attendance", array);
            mainArray.add(arrayElement);

        }

        //put everything inside the object
        mainObject.put("attendances", mainArray);

        return mainObject;
    }
}