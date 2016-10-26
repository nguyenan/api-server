package com.wut.support.performance;

import java.util.Collection;
//import java.util.Map;
//import java.util.TreeMap;
//
//import com.wut.support.ErrorHandler;

//import etm.core.configuration.BasicEtmConfigurator;
//import etm.core.configuration.EtmManager;
//import etm.core.monitor.EtmMonitor;
//import etm.core.monitor.EtmPoint;
//import etm.core.renderer.SimpleTextRenderer;

// Rename to Timer!!! PerformanceTimer
// http://jetm.void.fm/intro/one_minute_tutorial.html
public class PerformanceTimer {
	//private static PerformanceMonitor singleton = new PerformanceMonitor();
	//private static Logger logger = Logger.getLogger("com.naviquan");
//	private EtmMonitor monitor = null;
//	private EtmPoint point;
//	private Map<String, EtmPoint> points = new TreeMap<String, EtmPoint>();
	
	private PerformanceTimer() {
		//BasicEtmConfigurator.configure();
		//monitor = EtmManager.getEtmMonitor();
		//monitor.start();
		//monitor.render(new SimpleTextRenderer());
	}
	
	public static PerformanceTimer create(Class<?> c) {
		return new PerformanceTimer();
	}
	
	public void start(String name) {
//		point = monitor.createPoint(name);
//		points.put(name, point);
	}
	
	public void results() {
		//monitor.stop();
		//monitor.render(new SimpleTextRenderer());
	}
	
	public void stop(String name) {
//		EtmPoint point = points.get(name);
//		if (point == null) {
//			ErrorHandler.systemError(getClass().getSimpleName() + " point '" + name + "' stopped but not started");
//		} else {
//			point.collect();
//		}
	}
	
	public long getTime(String name) {
//		EtmPoint point = points.get(name);
//		return point.getEndTime()-point.getStartTime();
		return 0;
	}

	public Collection<String> getPointNames() {
//		return points.keySet();
		return null;
	}
	
}
