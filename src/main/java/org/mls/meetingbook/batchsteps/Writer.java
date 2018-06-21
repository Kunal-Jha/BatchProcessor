package org.mls.meetingbook.batchsteps;

import java.util.List;

import javax.sql.DataSource;

import org.mls.meetingbook.model.Meeting;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

public class Writer implements ItemWriter<List<Meeting>> {
	final String INSERT_QUERY = "INSERT INTO meetingSchedule (register_Time,emp_Id, start_Time, duration, end_Time) "
			+ "VALUES (:registerTime, :empId, :startTime, :duration, :endTime)";
	JdbcBatchItemWriter<Meeting> wrapped;

	public Writer(DataSource dataSource) {
		this.wrapped = new JdbcBatchItemWriter<Meeting>();
		this.wrapped.setDataSource(dataSource);
		this.wrapped.setSql(INSERT_QUERY);
		this.wrapped.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Meeting>());

	}

	@Override
	public void write(List<? extends List<Meeting>> items) throws Exception {
		for (List<Meeting> subList : items) {
			wrapped.write(subList);
		}
	}
}
