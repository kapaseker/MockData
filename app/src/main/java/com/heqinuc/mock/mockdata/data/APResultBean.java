package com.heqinuc.mock.mockdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Panoo on 2017/7/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class APResultBean {


	private String sn;
	private String status;
	private String status_msg;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_msg() {
		return status_msg;
	}

	public void setStatus_msg(String status_msg) {
		this.status_msg = status_msg;
	}
}
