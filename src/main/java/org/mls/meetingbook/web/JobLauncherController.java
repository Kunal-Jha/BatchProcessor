package org.mls.meetingbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@RestController
public class JobLauncherController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;
	public static String result;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		JobLauncherController.result = result;
	}

	@RequestMapping(value = "/timetable-creation", method = RequestMethod.PUT, consumes = "text/plain")
	public String setMeetingRequest(@RequestBody String meetingRequests) throws Exception {
		result = "";
		String[] dateMessage = meetingRequests.trim().split("\\R", 2);
		try {
			jobLauncher.run(job, getJobParameters(dateMessage[1], dateMessage[0]));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		if (JobLauncherController.result == "")
			return "Default";
		return JobLauncherController.result;
	}

	public JobParameters getJobParameters(String meetingRequests, String businessHours) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("bussinessHours", businessHours);
		jobParametersBuilder.addString("meetingRequests", meetingRequests);
		jobParametersBuilder.addLong("time", System.currentTimeMillis());
		return jobParametersBuilder.toJobParameters();
	}
}