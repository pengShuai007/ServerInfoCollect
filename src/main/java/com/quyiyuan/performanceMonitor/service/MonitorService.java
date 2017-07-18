package com.quyiyuan.performanceMonitor.service;

import java.awt.Menu;
import java.text.DecimalFormat;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.cmd.CpuInfo;

import com.quyiyuan.performanceMonitor.vo.MonitorInfo;

public class MonitorService {
	final int PERCENT = 100;
	final int CPUSLIP = 10000;
	final int NETBANDWIDTH = 100;

	public MonitorInfo getMonitorInfo() throws Exception {
		MonitorInfo infoBean = new MonitorInfo();
		try {
			Sigar sigar = new Sigar();
			Mem mem = sigar.getMem();
			System.out.println("mem:"+mem);
			infoBean.setMemUsedRatio(100L * (mem.getActualUsed()) / mem.getTotal());
			long[] diskUsedInfo = getDiskUsedInfo(sigar);
			long[] netUsedratio = getNetUsed(sigar);
			int procNum = sigar.getProcList().length;
			//获取计算多核CPU平均使用率的参数	
			double CpuAvgRatio = cpuRatioCal(sigar);
			System.out.println("avrager使用率："+CpuAvgRatio);
			//cpu 速率
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

	public long[] getDiskUsedInfo(Sigar sigar)
	  {
	    long[] diskUsedInfo = new long[4];
	    try {
	      FileSystem[] fslist = sigar.getFileSystemList();
	      for (FileSystem fs : fslist) {
	        FileSystemUsage usage = null;
	        try {
	          usage = sigar.getFileSystemUsage(fs.getDirName());
	        } catch (SigarException e) {
	        //  break label162:
	        	break;
	        }
	        switch (fs.getType())
	        {
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
	    }
	    catch (SigarException e) {
	      e.printStackTrace();
	    }
	    return diskUsedInfo;
	  }

	//cpu平均速率
	private Double cpuRatioCal(Sigar sigar) throws InterruptedException, SigarException {
		    Double cpuRatio = 0.0;
	        CpuPerc[] cpuList = sigar.getCpuPercList();
		for(int i = 0; i < cpuList.length; i++){
			cpuRatio += cpuList[i].getCombined();
		}               
		return cpuRatio*100/cpuList.length;
	}
	//cpu速率
	private double convertDec(double value, int dec) {
		String pattern = "#.";
		for (int i = 0; i < dec; ++i) {
			pattern = pattern + "0";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		return Double.parseDouble(df.format(value));
	}
	
	public static void main(String[] srgs) throws Exception{
		new MonitorService().getMonitorInfo();
	}
}
