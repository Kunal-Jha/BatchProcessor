package org.mls.meetingbook.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;

/**
 * @author Kunal
 *
 */
public class Meeting {
	Timestamp registerTime;
	String empId;
	int duration;
	Timestamp startTime;
	Timestamp endTime;

	public Meeting(String registerTime, String empId, String startTime, int duration) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			this.registerTime = new Timestamp(format.parse(registerTime).getTime());
			this.startTime = new Timestamp(format2.parse(startTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.duration = duration;
		this.empId = empId;
		this.endTime = new Timestamp(this.startTime.getTime() + this.duration * 3600 * 1000);

	}

	public Meeting(Timestamp registerTime, String empId, Timestamp startTime, Timestamp endTime) {
		this.registerTime = registerTime;
		this.startTime = startTime;
		this.empId = empId;
		this.endTime = endTime;
	}

	/**
	 * @return the registerTime
	 */
	public Timestamp getRegisterTime() {
		return registerTime;
	}

	/**
	 * @param registerTime
	 *            the registerTime to set
	 */
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 *            the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the startTime
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return startTime + "  " + endTime + "  " + empId;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}

}
