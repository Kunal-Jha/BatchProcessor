package org.mls.meetingbook.model;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MeetingTestCase {

	String registerTime = null;
	String startTime = null;
	String empId = null;
	Timestamp register = null, start = null, end = null;
	int duration = -1;

	@Before
	public void setUp() {
		registerTime = "2018-05-17 10:17:06";
		startTime = "2018-05-21 09:00";
		empId = "EMP001";
		duration = 2;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			register = new Timestamp(df.parse(registerTime).getTime());
			start = new Timestamp(df1.parse(startTime).getTime());
			end = new Timestamp(df1.parse("2018-05-21 11:00").getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void MeetingCreation() {

		Meeting meeting = new Meeting(registerTime, empId, startTime, duration);

		assertEquals(start, meeting.getStartTime());
		assertEquals(end, meeting.getEndTime());
		assertEquals(duration, meeting.getDuration());
		assertEquals(register, meeting.getRegisterTime());

	}

}
