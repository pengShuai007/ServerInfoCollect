package com.quyiyuan.performanceMonitor.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.quyiyuan.performanceMonitor.service.MonitorSysService;
import com.quyiyuan.performanceMonitor.vo.MonitorInfo;

public class MonitorController extends HttpServlet{
	
	//private MonitorService monitorService;
	private MonitorSysService monitorSysService;
    private  Gson gson;
	
	@Override
	public void init() throws ServletException {
		
		//monitorService = new MonitorService();
		monitorSysService =new MonitorSysService();
		gson=new GsonBuilder().create();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		MonitorInfo monitorInfo = null;
		Properties prop = new Properties();
		try {
			String classesPath = this.getClass().getClassLoader().getResource("/").getPath();
			String properPath = classesPath + File.separator + "System.properties";
			 InputStream in = new BufferedInputStream (new FileInputStream(properPath));
			 prop.load(in);
			if (prop.getProperty("OS.TYPE").equals("windows")) {
				monitorInfo = monitorSysService.getMonitorWindowsInfo();
			}else if (prop.getProperty("OS.TYPE").equals("linux")) {
				monitorInfo = monitorSysService.getMonitorLinuxInfo();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("json");
		resp.getWriter().write(gson.toJson(monitorInfo));
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}
}
