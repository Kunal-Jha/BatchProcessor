package org.mls.meetingbook.batchsteps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.mls.meetingbook.model.Meeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class MeetingProcessor implements ItemProcessor<String, Meeting> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	Date openTime;
	Date closeTime;

	public MeetingProcessor(String openingHour) {
		SimpleDateFormat parser = new SimpleDateFormat("HHmm");
		String[] openingHours = openingHour.split("\\s+");
		try {
			openTime = parser.parse(openingHours[0].trim());
			closeTime = parser.parse(openingHours[1].trim());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Meeting process(String meetingRequest) throws Exception {
		String[] requestElements = meetingRequest.trim().split("\\s+");
		Meeting nextRequest = new Meeting(requestElements[0].trim() + " " + requestElements[1].trim(),
				requestElements[2].trim(), requestElements[3].trim() + " " + requestElements[4].trim(),
				Integer.parseInt(requestElements[5].trim()));
		if ((compareTime(openTime, nextRequest.getStartTime()) <= 0)
				&& (compareTime(closeTime, nextRequest.getEndTime()) >= 0)) {
			logger.info("Within Office hours!!");
			return nextRequest;
		} else {
			logger.info("Meeting Not within Office hours!!");
			return null;

		}
	}

	private long compareTime(Date workHour, Date meetingtime) {
		Calendar workCal = Calendar.getInstance();
		workCal.setTime(workHour);
		Calendar meetingCal = Calendar.getInstance();
		meetingCal.setTime(meetingtime);
		meetingCal.set(Calendar.DAY_OF_MONTH, workCal.get(Calendar.DAY_OF_MONTH));
		meetingCal.set(Calendar.MONTH, workCal.get(Calendar.MONTH));
		meetingCal.set(Calendar.YEAR, workCal.get(Calendar.YEAR));
		return workCal.getTimeInMillis() - meetingCal.getTimeInMillis();
	}

}