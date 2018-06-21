package org.mls.meetingbook.config;

import javax.sql.DataSource;

import org.mls.meetingbook.batchsteps.MeetingProcessor;
import org.mls.meetingbook.batchsteps.Reader;

import org.mls.meetingbook.model.Meeting;
import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
	final String INSERT_QUERY = "INSERT INTO meetingSchedule (register_Time,emp_Id, start_Time, duration, end_Time) VALUES (:registerTime, :empId, :startTime, :duration, :endTime)";

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	@Bean
	public JdbcBatchItemWriter<Meeting> writer(DataSource dataSource) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		// List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * WHERE
		// :registerTime ");

		return new JdbcBatchItemWriterBuilder<Meeting>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Meeting>())
				.sql(INSERT_QUERY).dataSource(dataSource).build();

	}

	@Bean
	@StepScope
	public Reader reader(@Value("#{jobParameters[meetingRequests]}") String meetingRequests) {
		return new Reader(meetingRequests);
	}

	@Bean
	@StepScope
	public MeetingProcessor processor(@Value("#{jobParameters[bussinessHours]}") String openingHours) {
		return new MeetingProcessor(openingHours);
	}
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("writeToDB").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
				.end().build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Meeting> writer) {
		return stepBuilderFactory.get("step1").<String, Meeting>chunk(200).reader(reader(OVERRIDDEN_BY_EXPRESSION))
				.processor(processor(OVERRIDDEN_BY_EXPRESSION)).writer(writer).build();
	}

}
