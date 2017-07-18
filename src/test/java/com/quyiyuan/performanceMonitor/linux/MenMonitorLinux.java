package com.quyiyuan.performanceMonitor.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class MenMonitorLinux {

	private static MenMonitorLinux INSTANCE = new MenMonitorLinux();

	private MenMonitorLinux() {
	}

	public static MenMonitorLinux getInstance() {
		return INSTANCE;
	}

	/**
	 * Purpose:采集内存使用率
	 * 
	 * @param args
	 * @return float,内存使用率,小于1
	 */
	public float get() {

		float memUsage = 0.0f;
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cat /proc/meminfo";
			pro = r.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			int count = 0;
			long totalMem = 0, freeMem = 0,buffers=0,cached=0;
			while ((line = in.readLine()) != null) {
				
				String[] memInfo = line.split("\\s+");
				if (memInfo[0].startsWith("MemTotal")) {
					totalMem = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("MemFree")) {
					freeMem = Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("Buffers")) {
					buffers=Long.parseLong(memInfo[1]);
				}
				if (memInfo[0].startsWith("Cached")) {
					cached=Long.parseLong(memInfo[1]);
				}
				memUsage = 1 - ((float) freeMem+(float) buffers+(float) cached) / (float) totalMem;
				System.out.println("内存使用率为"+memUsage);
				if (++count == 2) {
					break;
				}
			}
			in.close();
			pro.destroy();
		} catch (IOException e) {
			
		}
		return memUsage;
	}
    
}

