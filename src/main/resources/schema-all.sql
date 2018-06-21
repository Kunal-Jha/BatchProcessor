DROP TABLE meetingSchedule IF EXISTS;
CREATE TABLE  meetingSchedule (
	id INT NOT NULL IDENTITY,	
	register_Time TIMESTAMP NOT NULL,  
  	emp_Id VARCHAR(500) NOT NULL,
  	start_Time TIMESTAMP NOT NULL,
  	duration INT NOT NULL,
  	end_Time DATE NOT NULL

);
