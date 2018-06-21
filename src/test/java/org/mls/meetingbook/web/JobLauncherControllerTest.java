package org.mls.meetingbook.web;

import static org.hamcrest.Matchers.containsString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobLauncherControllerTest {
	@Autowired
	JobLauncherController jobController;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contexLoads() throws Exception {
		assert (jobController != null);
	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(put("/timetable-creation").contentType(MediaType.TEXT_PLAIN).content(
				"0900 1730\n 2018-05-16 12:34:56 EMP002 2018-05-21 09:00 2\n 2018-05-16 09:28:23 EMP003 2018-05-22 14:00 2"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("2018-05-21 09:00:00.0  2018-05-21 11:00:00.0  EMP002")));

	}
}
