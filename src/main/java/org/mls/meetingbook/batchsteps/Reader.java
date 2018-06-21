package org.mls.meetingbook.batchsteps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<String> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	List<String> messages = new ArrayList<String>();

	public Reader(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				this.messages.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// For Test Cases
	public Reader(String text, boolean Test) {
		String[] elements = text.split("\n");
		this.messages = Arrays.asList(elements);
	}

	int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (count < messages.size())
			return messages.get(count++).trim();
		else
			count = 0;
		return null;
	}

}