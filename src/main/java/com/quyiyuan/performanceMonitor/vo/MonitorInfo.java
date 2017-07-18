package com.quyiyuan.performanceMonitor.vo;

public class MonitorInfo {

	private double cpuRatio;
	private double memUsedRatio;
	private double loadAvg;
	private double diskUsedRatio;
	private double diskRdRate;
	private double diskWrRate;
	private double netRxBytes;
	private double netTxBytes;
	private long tcpNum;
	private int processesNum;
	private String tomcatState;
	private String countLinks;
	private String maxLinks;
	private String lastUserVisitTime;
	private String lastPackSendTime;
	private String T_VERSION;
	private String VISIT_COUNT;
	private String VISIT_SUCCESS_COUNTS;
	private String APPOINT_TIME_DIFF;
	private String REGISTER_TIME_DIFF;
	private String QUERY_CARD_TIME;

	public String getCountLinks() {
		return countLinks;
	}

	public void setCountLinks(String countLinks) {
		this.countLinks = countLinks;
	}

	public double getMemUsedRatio() {
		return memUsedRatio;
	}

	public void setMemUsedRatio(double memUsedRatio) {
		this.memUsedRatio = memUsedRatio;
	}

	public int getProcessesNum() {
		return processesNum;
	}

	public void setProcessesNum(int processesNum) {
		this.processesNum = processesNum;
	}

	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public double getLoadAvg() {
		return loadAvg;
	}

	public void setLoadAvg(double loadAvg) {
		this.loadAvg = loadAvg;
	}

	public double getDiskUsedRatio() {
		return diskUsedRatio;
	}

	public void setDiskUsedRatio(double diskUsedRatio) {
		this.diskUsedRatio = diskUsedRatio;
	}

	public double getDiskRdRate() {
		return diskRdRate;
	}

	public void setDiskRdRate(double diskRdRate) {
		this.diskRdRate = diskRdRate;
	}

	public double getDiskWrRate() {
		return diskWrRate;
	}

	public void setDiskWrRate(double diskWrRate) {
		this.diskWrRate = diskWrRate;
	}

	public double getNetRxBytes() {
		return netRxBytes;
	}

	public void setNetRxBytes(double netRxBytes) {
		this.netRxBytes = netRxBytes;
	}

	public double getNetTxBytes() {
		return netTxBytes;
	}

	public void setNetTxBytes(double netTxBytes) {
		this.netTxBytes = netTxBytes;
	}

	public long getTcpNum() {
		return tcpNum;
	}

	public void setTcpNum(long tcpNum) {
		this.tcpNum = tcpNum;
	}

	public String getTomcatState() {
		return tomcatState;
	}

	public void setTomcatState(String tomcatState) {
		this.tomcatState = tomcatState;
	}

	public String getLastUserVisitTime() {
		return lastUserVisitTime;
	}

	public void setLastUserVisitTime(String lastUserVisitTime) {
		this.lastUserVisitTime = lastUserVisitTime;
	}

	public String getLastPackSendTime() {
		return lastPackSendTime;
	}

	public void setLastPackSendTime(String lastPackSendTime) {
		this.lastPackSendTime = lastPackSendTime;
	}

	public String getMaxLinks() {
		return maxLinks;
	}

	public void setMaxLinks(String maxLinks) {
		this.maxLinks = maxLinks;
	}

	public String getT_VERSION() {
		return T_VERSION;
	}

	public void setT_VERSION(String t_VERSION) {
		T_VERSION = t_VERSION;
	}

	public String getVISIT_COUNT() {
		return VISIT_COUNT;
	}

	public void setVISIT_COUNT(String vISIT_COUNT) {
		VISIT_COUNT = vISIT_COUNT;
	}

	public String getVISIT_SUCCESS_COUNTS() {
		return VISIT_SUCCESS_COUNTS;
	}

	public void setVISIT_SUCCESS_COUNTS(String vISIT_SUCCESS_COUNTS) {
		VISIT_SUCCESS_COUNTS = vISIT_SUCCESS_COUNTS;
	}

	public String getAPPOINT_TIME_DIFF() {
		return APPOINT_TIME_DIFF;
	}

	public void setAPPOINT_TIME_DIFF(String aPPOINT_TIME_DIFF) {
		APPOINT_TIME_DIFF = aPPOINT_TIME_DIFF;
	}

	public String getREGISTER_TIME_DIFF() {
		return REGISTER_TIME_DIFF;
	}

	public void setREGISTER_TIME_DIFF(String rEGISTER_TIME_DIFF) {
		REGISTER_TIME_DIFF = rEGISTER_TIME_DIFF;
	}

	public String getQUERY_CARD_TIME() {
		return QUERY_CARD_TIME;
	}

	public void setQUERY_CARD_TIME(String qUERY_CARD_TIME) {
		QUERY_CARD_TIME = qUERY_CARD_TIME;
	}
}
