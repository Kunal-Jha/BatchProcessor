package org.mls.meetingbook.config;

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

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			jdbcTemplate.query(SELECT_QUERY,
					(rs, row) -> new Meeting(rs.getTimestamp(1), rs.getString(2), rs.getTimestamp(3), rs.getTimestamp(4)))
					.forEach(meeting -> {
						log.info("Found <" + meeting.toString() + "> in the database.");
						JobLauncherController.result += meeting.toString() +"\n";
					});
		}
	}

}
