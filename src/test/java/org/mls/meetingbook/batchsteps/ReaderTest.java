package org.mls.meetingbook.batchsteps;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class ReaderTest {

	Reader itemReader;

	@Before
	public void setUp() throws Exception {
		String items = "2018-05-17 10:17:06 EMP001 2018-05-21 09:00 2\n "
				+ "2018-05-16 12:34:56 EMP002 2018-05-21 09:00 2\n" + "2018-05-16 09:28:23 EMP003 2018-05-22 14:00 2";
		itemReader = new Reader(items, true);
	}

	@Test
	public void test() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		assertEquals("2018-05-17 10:17:06 EMP001 2018-05-21 09:00 2", itemReader.read());
		assertEquals("2018-05-16 12:34:56 EMP002 2018-05-21 09:00 2", itemReader.read());
		assertEquals("2018-05-16 09:28:23 EMP003 2018-05-22 14:00 2", itemReader.read());
	}

}
