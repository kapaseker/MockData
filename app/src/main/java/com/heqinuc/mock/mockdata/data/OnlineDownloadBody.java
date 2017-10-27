package com.heqinuc.mock.mockdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Panoo on 2017/7/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OnlineDownloadBody {

	/**
	 * httpResCode : XXX
	 * count : XXX
	 * time : XXX
	 * mapblocknum : XXX
	 * maplevel : XXX
	 * resultLen : XXXX
	 */

	private int httpResCode;
	private int count;
	private int time;
	private int mapblocknum;
	private float maplevel;
	private int resultLen;

	public OnlineDownloadBody(int httpResCode, int count, int time, int mapblocknum, float maplevel, int resultLen) {
		this.httpResCode = httpResCode;
		this.count = count;
		this.time = time;
		this.mapblocknum = mapblocknum;
		this.maplevel = maplevel;
		this.resultLen = resultLen;
	}

	public int getHttpResCode() {
		return httpResCode;
	}

	public void setHttpResCode(int httpResCode) {
		this.httpResCode = httpResCode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getMapblocknum() {
		return mapblocknum;
	}

	public void setMapblocknum(int mapblocknum) {
		this.mapblocknum = mapblocknum;
	}

	public float getMaplevel() {
		return maplevel;
	}

	public void setMaplevel(float maplevel) {
		this.maplevel = maplevel;
	}

	public int getResultLen() {
		return resultLen;
	}

	public void setResultLen(int resultLen) {
		this.resultLen = resultLen;
	}
}
