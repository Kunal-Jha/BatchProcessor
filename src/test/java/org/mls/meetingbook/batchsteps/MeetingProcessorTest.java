package org.mls.meetingbook.batchsteps;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mls.meetingbook.model.Meeting;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeetingProcessorTest {
	MeetingProcessor processor;
	MeetingProcessor processor1;
	Date openHours;
	Date closeHours;

	@Before
	public void setUp() throws Exception {
		processor = mock(MeetingProcessor.class);
		processor1 = new MeetingProcessor("0900 1730");
		SimpleDateFormat parser = new SimpleDateFormat("HHmm");
		openHours = parser.parse("0900");
		closeHours = parser.parse("1730");
		processor.setOpenTime(openHours);
		processor.setCloseTime(closeHours);

	}

	@Test
	public void workingHourTest()

	{
		assertEquals(openHours, processor1.openTime);
		assertEquals(closeHours, processor1.closeTime);
	}

	@Test
	public void testTransform() throws Exception {
		Meeting itemAfterTransformation = new Meeting("2018-05-17 10:17:06", "EMP001", "2018-05-21 09:00", 2);
		when(processor.process("2018-05-17 10:17:06 EMP001 2018-05-21 09:00 2")).thenReturn(itemAfterTransformation);
		when(processor.process("2018-05-17 10:17:06 EMP001 2018-05-21 08:00 2")).thenReturn(null);

	}
}
