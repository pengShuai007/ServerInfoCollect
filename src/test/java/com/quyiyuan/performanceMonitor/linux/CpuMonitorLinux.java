package com.quyiyuan.performanceMonitor.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CpuMonitorLinux {

	public float getcpuUsage() {

		float cpuUsage = 0;
		Process pro1, pro2;
		Runtime r = Runtime.getRuntime();
		try {
			String command = "cat /proc/stat";
			// 第一次采集CPU时间
			long startTime = System.currentTimeMillis();
			pro1 = r.exec(command);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String line = null;
			long idleCpuTime1 = 0, totalCpuTime1 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间
			while ((line = in1.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					String[] temp = line.split("\\s+");
					idleCpuTime1 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime1 += Long.parseLong(s);
						}
					}
					break;
				}
			}
			in1.close();
			pro1.destroy();

			  try {
				Thread.sleep(100); //休眠100ms
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			// 第二次采集CPU时间
			long endTime = System.currentTimeMillis();
			pro2 = r.exec(command);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			long idleCpuTime2 = 0, totalCpuTime2 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间
			while ((line = in2.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					String[] temp = line.split("\\s+");
					idleCpuTime2 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime2 += Long.parseLong(s);
						}
					}
					break;
				}
			}
			if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
				cpuUsage = 1 - (float) (idleCpuTime2 - idleCpuTime1) / (float) (totalCpuTime2 - totalCpuTime1);
				System.out.println("cpu使用速率" + cpuUsage);
			}
			in2.close();
			pro2.destroy();
		} catch (IOException e) {
		}
		return cpuUsage;
	}

}
