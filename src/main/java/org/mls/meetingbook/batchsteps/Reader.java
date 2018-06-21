package org.mls.meetingbook.batchsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<String> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	String[] messages = null;

	public Reader(String meetingRequests) {
		this.messages = meetingRequests.split("\\r?\\n");
	}

	private int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (count < messages.length)
			return messages[count++];
		else
			count = 0;

		return null;
	}

}