package com.quyiyuan.performanceMonitor.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.quyiyuan.performanceMonitor.vo.MonitorInfo;

public class MonitorSysService {

	public MonitorInfo getMonitorLinuxInfo() throws Exception {
		MonitorInfo infoBean = new MonitorInfo();
		try {
			infoBean.setMemUsedRatio(getLinuxMem());
			// 获取计算多核CPU平均使用率的参数
			double CpuAvgRatio = getLinuxCpuUsage();
			// cpu 速率
			infoBean.setCpuRatio(convertDec(CpuAvgRatio, 1));
			infoBean.setDiskUsedRatio(getLinuxDisk());

			Sigar sigar = new Sigar();

			long[] diskUsedInfo = getDiskUsedInfo(sigar);
			long[] netUsedratio = getNetUsed(sigar);

			int procNum = sigar.getProcList().length;

			infoBean.setDiskRdRate(diskUsedInfo[2]);
			infoBean.setDiskWrRate(diskUsedInfo[3]);
			infoBean.setNetRxBytes(netUsedratio[0]);
			infoBean.setNetTxBytes(netUsedratio[1]);
			infoBean.setProcessesNum(procNum);
			infoBean.setTcpNum(sigar.getTcp().getActiveOpens());

			sigar.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoBean;
	}

	public MonitorInfo getMonitorWindowsInfo() throws Exception {
		MonitorInfo infoBean = new MonitorInfo();
		try {
			Sigar sigar = new Sigar();
			Mem mem = sigar.getMem();
			System.out.println("mem:" + mem);
			infoBean.setMemUsedRatio(100L * (mem.getActualUsed()) / mem.getTotal());
			long[] diskUsedInfo = getDiskUsedInfo(sigar);
			long[] netUsedratio = getNetUsed(sigar);
			int procNum = sigar.getProcList().length;
			// 获取计算多核CPU平均使用率的参数
			double CpuAvgRatio = cpuRatioCal(sigar);
			System.out.println("avrager使用率：" + CpuAvgRatio);
			// cpu 速率
			infoBean.setCpuRatio(convertDec(CpuAvgRatio, 1));

			infoBean.setDiskUsedRatio(100L * diskUsedInfo[1] / diskUsedInfo[0]);
			infoBean.setDiskRdRate(diskUsedInfo[2]);
			infoBean.setDiskWrRate(diskUsedInfo[3]);
			infoBean.setNetRxBytes(netUsedratio[0]);
			infoBean.setNetTxBytes(netUsedratio[1]);
			infoBean.setProcessesNum(procNum);
			infoBean.setTcpNum(sigar.getTcp().getActiveOpens());

			sigar.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoBean;
	}

	// 获取linux系统CPU使用率
	public float getLinuxCpuUsage() {
		float cpuUsage = 0.0f;
		Runtime r = Runtime.getRuntime();
		String command = "sar -u 1 1";
		try {
			Process pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			while (true) {
				line = in.readLine();
				if (line.startsWith("Average")) {
					line = line.trim();
					String[] temp = line.split("\\s+");
					cpuUsage = 100 - Float.parseFloat(temp[temp.length - 1]);
					break;
				}
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {
		}

		return cpuUsage;
	}

	// 获取linux系统CPU使用率
	/*
	 * public float getLinuxCpuUsage() {
	 * 
	 * float cpuUsage = 0.0f; Process pro1, pro2; Runtime r =
	 * Runtime.getRuntime(); try { String command = "cat /proc/stat"; //
	 * 第一次采集CPU时间 long startTime = System.currentTimeMillis(); pro1 =
	 * r.exec(command); BufferedReader in1 = new BufferedReader(new
	 * InputStreamReader(pro1.getInputStream())); String line = null; long
	 * idleCpuTime1 = 0, totalCpuTime1 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间 while
	 * ((line = in1.readLine()) != null) { if (line.startsWith("cpu")) { line =
	 * line.trim(); String[] temp = line.split("\\s+"); idleCpuTime1 =
	 * Long.parseLong(temp[4]); for (String s : temp) { if (!s.equals("cpu")) {
	 * totalCpuTime1 += Long.parseLong(s); } } break; } } in1.close();
	 * pro1.destroy();
	 * 
	 * try { Thread.sleep(100); //休眠100ms } catch (InterruptedException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * // 第二次采集CPU时间 long endTime = System.currentTimeMillis(); pro2 =
	 * r.exec(command); BufferedReader in2 = new BufferedReader(new
	 * InputStreamReader(pro2.getInputStream())); long idleCpuTime2 = 0,
	 * totalCpuTime2 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间 while ((line =
	 * in2.readLine()) != null) { if (line.startsWith("cpu")) { line =
	 * line.trim(); String[] temp = line.split("\\s+"); idleCpuTime2 =
	 * Long.parseLong(temp[4]); for (String s : temp) { if (!s.equals("cpu")) {
	 * totalCpuTime2 += Long.parseLong(s); } } break; } } if (idleCpuTime1 != 0
	 * && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
	 * cpuUsage = 1 - (float) (idleCpuTime2 - idleCpuTime1) / (float)
	 * (totalCpuTime2 - totalCpuTime1); System.out.println("cpu使用速率" +
	 * cpuUsage); } in2.close(); pro2.destroy(); } catch (IOException e) { }
	 * return cpuUsage*100; }
	 */
	// 获取Linux系统内存使用率
	public float getLinuxMem() {

		float memUsage = 0.0f;
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cat /proc/meminfo";
			pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			int count=0;
			long totalMem = 0, freeMem = 0, buffers = 0, cached = 0;
			while ((line = in.readLine()) != null) {

				String[] memInfo = line.split("\\s+");
				if (memInfo[0].startsWith("MemTotal")) {
					totalMem = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("MemFree")) {
					freeMem = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("Buffers")) {
					buffers = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("Cached")) {
					 cached= Long.parseLong(memInfo[1]);
				}
				memUsage = ((float) totalMem - (float) freeMem -(float) buffers - (float) cached) / (float) totalMem;
				if (++count==4) {
					break;
				}
				System.out.println("内存使用率为" + memUsage);
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {

		}
		return memUsage * 100;
	}

	// 获取linux磁盘使用率
	public float getLinuxDisk() {

		float diskUsage = 0.0f;
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "df -h"; // df -h
			pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			float util = 0.0f;
			while ((line = in.readLine()) != null) {
				String[] temp = line.split("\\s+");
				if (temp[0].startsWith("/dev/")) {
					if (temp.length > 1) {
						util = Float.parseFloat(temp[temp.length - 2].substring(0, temp[temp.length - 2].length() - 1));
						util = util / 100;
						diskUsage = (diskUsage > util) ? diskUsage : util;
					}
				}
			}
			if (diskUsage > 0) {
				diskUsage /= 100;
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {

		}
		return diskUsage * 100;
	}

	// cpu平均速率
	private Double cpuRatioCal(Sigar sigar) throws InterruptedException, SigarException {
		Double cpuRatio = 0.0;
		CpuPerc[] cpuList = sigar.getCpuPercList();
		for (int i = 0; i < cpuList.length; i++) {
			cpuRatio += cpuList[i].getCombined();
		}
		return cpuRatio * 100 / cpuList.length;
	}

	public long[] getDiskUsedInfo(Sigar sigar) {
		long[] diskUsedInfo = new long[4];
		try {
			FileSystem[] fslist = sigar.getFileSystemList();
			for (FileSystem fs : fslist) {
				FileSystemUsage usage = null;
				try {
					usage = sigar.getFileSystemUsage(fs.getDirName());
				} catch (SigarException e) {
					// break label162:
					break;
				}
				switch (fs.getType()) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					diskUsedInfo[0] += usage.getTotal();
					diskUsedInfo[1] += usage.getUsed();
					diskUsedInfo[2] += usage.getDiskReadBytes();
					diskUsedInfo[3] += usage.getDiskWriteBytes();
					break;
				case 3:
					break;
				case 4:
					continue;
				case 5:
				case 6:
				}
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return diskUsedInfo;
	}

	private long[] getNetUsed(Sigar sigar) {
		long[] netBypes = new long[2];
		try {
			String[] netList = sigar.getNetInterfaceList();

			for (int i = 0; i < netList.length; ++i) {
				String name = netList[i];
				NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
				if ((ifconfig.getFlags() & 1L) <= 0L)
					continue;
				try {
					NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
					netBypes[0] += ifstat.getRxBytes();
					netBypes[1] += ifstat.getTxBytes();
				} catch (SigarException e) {
					e.printStackTrace();
				}
			}
		} catch (SigarException e1) {
			e1.printStackTrace();
		}
		return netBypes;
	}

	private double convertDec(double value, int dec) {
		String pattern = "#.";
		for (int i = 0; i < dec; ++i) {
			pattern = pattern + "0";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		return Double.parseDouble(df.format(value));
	}
}
