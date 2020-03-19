import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AttendanceHandlerTest {

    Member member = new Member("kota", "av8d");
    @Test
    public void isHasAttendedTrue() {
        AttendanceHandler attendance = new AttendanceHandler(member, true);

        assertEquals(true, attendance.isHasAttended());
    }

    @Test
    public void isHasAttendedFalse() {
        AttendanceHandler attendance = new AttendanceHandler(member, true);

        assertNotEquals(false, attendance.isHasAttended());
    }

    @Test
    public void hasAttendedToStringTrue() {
        AttendanceHandler attendance = new AttendanceHandler(member, true);

        assertEquals("Attended", attendance.hasAttendedToString());
    }

}
