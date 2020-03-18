/**
 * This class represents an Attendance object.
 */
public class AttendanceHandler {
    private Member member;
    private boolean hasAttended;


    public AttendanceHandler(Member member, boolean hasAttended) {
        this.member = member;
        this.hasAttended = hasAttended;
    }


    public Member getMember() {
        return member;
    }


    public boolean isHasAttended() {
        return hasAttended;
    }


    public String hasAttendedToString() {

        return (hasAttended) ? "Attended" : "Not Attended";
    }

    public String toString() {
        return "{" +
                "ID = " + member.getMemberID() +
                ", Name = " + member.getMemberName() +
                ", Status = " + hasAttendedToString() +
                '}';
    }
}