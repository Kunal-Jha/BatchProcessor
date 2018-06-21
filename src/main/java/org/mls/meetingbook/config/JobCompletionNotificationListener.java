package org.mls.meetingbook.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.stream.Collectors;

import org.mls.meetingbook.model.Meeting;
import org.mls.meetingbook.web.JobLauncherController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	final String SELECT_QUERY = "SELECT register_Time, emp_Id, start_Time, end_Time  FROM meetingSchedule ORDER BY start_Time ASC";
	final String DELETE_QUERY = "DELETE FROM meetingSchedule WHERE emp_Id = ? AND register_Time = ?";
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			List<Meeting> existingMeetings = jdbcTemplate.query(SELECT_QUERY, (rs,
					row) -> new Meeting(rs.getTimestamp(1), rs.getString(2), rs.getTimestamp(3), rs.getTimestamp(4)));
			List<Meeting> confirmedMeeting = new ArrayList<Meeting>();
			boolean conflict = false;
			for (int i = 0; i < existingMeetings.size(); i++) {
				conflict = false;
				Meeting meeting1 = existingMeetings.get(i);
				log.info(meeting1.getEmpId() + " Found in Major  ");
				for (int j = i + 1; j < existingMeetings.size(); j++) {
					Meeting meeting2 = existingMeetings.get(j);
					log.info(meeting2.getEmpId() + " Found in Minor ");

					// Second meeting intersect with first
					if ((meeting2.getStartTime().before(meeting1.getStartTime())
							&& (meeting2.getEndTime().after(meeting1.getStartTime())))
							|| ((meeting2.getStartTime().before(meeting1.getEndTime())
									&& (meeting2.getEndTime().after(meeting1.getEndTime()))))
							// First meeting intersect with second
							|| ((meeting1.getStartTime().before(meeting2.getStartTime())
									&& (meeting1.getEndTime().after(meeting2.getStartTime())))
									|| ((meeting1.getStartTime().before(meeting2.getEndTime())
											&& (meeting1.getEndTime().after(meeting2.getEndTime())))))
							// If they are equal
							|| (meeting2.getStartTime().equals(meeting1.getStartTime())
									|| (meeting2.getEndTime().equals(meeting1.getEndTime())))) {

						conflict = true;
						if (meeting1.getRegisterTime().before(meeting2.getRegisterTime())) {
							log.info(meeting2.getEmpId() + " Conflict  " + meeting1.getEmpId());
							if (!existingMeetings.contains(meeting1)) {
								confirmedMeeting.add(meeting1);
							}
						} else {
							if (!existingMeetings.contains(meeting2)) {
								confirmedMeeting.add(meeting2);
							}
						}
					}

				}

				if (conflict == false) {
					confirmedMeeting.add(meeting1);
				}
			}

			List<Meeting> confirmedMeetingDistinct = confirmedMeeting.stream().distinct()
					.sorted(Comparator.comparing(Meeting::getStartTime)).collect(Collectors.toList());

			existingMeetings.removeAll(confirmedMeetingDistinct);

			confirmedMeetingDistinct.forEach(ele -> System.out.println(ele + " "));
			// elementsinDB.forEach(ele -> System.out.println(ele + " "));
			for (Meeting ele : confirmedMeetingDistinct) {
				JobLauncherController.result += ele.toString() + "\n";
			}
			for (Meeting ele : existingMeetings) {
				jdbcTemplate.update(DELETE_QUERY, ele.getEmpId(), ele.getRegisterTime());
				log.info("DELETING " + ele.getEmpId());
			}

			// TO see database elements after batch.
			jdbcTemplate.query(SELECT_QUERY, (rs, row) -> new Meeting(rs.getTimestamp(1), rs.getString(2),
					rs.getTimestamp(3), rs.getTimestamp(4))).forEach(meeting -> {
						log.info("Found <" + meeting.toString() + "> in the database.");
					});

		}
	}

}
