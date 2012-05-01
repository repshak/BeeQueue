package org.beequeue.agent;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;

public class CpuRawData {
	public CpuInfo info;
	public CpuPerc total;
	public CpuPerc[] all;
}