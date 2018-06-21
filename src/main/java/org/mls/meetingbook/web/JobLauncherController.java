package org.mls.meetingbook.web;

import java.io.File;

import java.io.FileWriter;

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

/**
 * @author Kunal Controller Class to deal with the client.
 */
@EnableScheduling
@RestController
public class JobLauncherController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;
	public static String result;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static String data;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/*
	 * 
	 * Controller function to interact with client and handle RequestBody to
	 * initiate batch Job
	 */
	// TODO: On closing the service delte all the temp files.
	// TODO: Return Json instead of String.
	@RequestMapping(value = "/timetable-creation", method = RequestMethod.PUT, consumes = "text/plain")
	public String setMeetingRequest(@RequestBody String meetingRequests) throws Exception {
		result = "";
		File inputFile = new File("input.txt");
		FileWriter fileWriter = new FileWriter(inputFile, false);
		String[] dateMessage = meetingRequests.trim().split("\\R", 2);
		logger.info("File Written");
		fileWriter.write(dateMessage[1]);

		fileWriter.close();
		try {
			jobLauncher.run(job, getJobParameters(dateMessage[0]));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		if (JobLauncherController.result == "")
			return "Default";
		return JobLauncherController.result;
	}

	// Setting parmaters for the job.
	public JobParameters getJobParameters(String businessHours) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("bussinessHours", businessHours);
		jobParametersBuilder.addString("file", "input.txt");
		jobParametersBuilder.addLong("time", System.currentTimeMillis());
		return jobParametersBuilder.toJobParameters();
	}
}