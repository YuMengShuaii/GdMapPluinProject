package com.enation.javashop.map.utils;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.RouteBusLineItem;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 字符串拼接工具类
 */
public class SchemeUtil {

	/**
	 * 获取公交路径前缀
	 * @param busPath  公交路径
	 * @return         公交信息
     */
	public static String getBusPathTitle(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		List<BusStep> busSetps = busPath.getSteps();
		if (busSetps == null) {
			return String.valueOf("");
		}
		StringBuffer sb = new StringBuffer();
		for (BusStep busStep : busSetps) {
			RouteBusLineItem busline = busStep.getBusLine();
			if (busline == null) {
				continue;
			}
			String buslineName = getSimpleBusLineName(busline.getBusLineName());
			sb.append(buslineName);
			sb.append(">");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取公交车用时
	 * @param busPath  公交路径信息
	 * @return         公交信息
     */
	public static String getBusPathDes(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		long second = busPath.getDuration();
		String time = getFriendlyTime((int) second);
		float subDistance = busPath.getDistance();
		String subDis = getFriendlyLength((int) subDistance);
		float walkDistance = busPath.getWalkDistance();
		String walkDis = getFriendlyLength((int) walkDistance);
		return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
	}

	/**
	 * 获取自驾用时
	 * @param busPath   自驾信息
	 * @return          字符串信息
     */
	public static String getDriverPathDes(DrivePath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		long second = busPath.getDuration();
		String time = getFriendlyTime((int) second);
		float subDistance = busPath.getDistance();
		String subDis = getFriendlyLength((int) subDistance);
		float walkDistance = busPath.getDistance();
		String walkDis = getFriendlyLength((int) walkDistance);
		return String.valueOf(time + " | " + subDis);
	}

	/**
	 * 距离转换为字符串
	 * @param lenMeter int
	 * @return         字符串
     */
	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) { // 10 km
			int dis = lenMeter / 1000;
			return dis + MapConst.Kilometer;
		}
		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + MapConst.Kilometer;
		}
		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + MapConst.Meter;
		}
		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}
		return dis + MapConst.Meter;
	}

	/**
	 * int 转时间
	 * @param second int
	 * @return       String事件
     */
	public static String getFriendlyTime(int second) {
		if (second > 3600) {
			int hour = second / 3600;
			int miniate = (second % 3600) / 60;
			return hour + "小时" + miniate + "分钟";
		}
		if (second >= 60) {
			int miniate = second / 60;
			return miniate + "分钟";
		}
		return second + "秒";
	}

	/**
	 * 获取公交车名字
	 * @param busLineName  bus名
	 * @return             名字
     */
	public static String getSimpleBusLineName(String busLineName) {
		if (busLineName == null) {
			return String.valueOf("");
		}
		return busLineName.replaceAll("\\(.*?\\)", "");
	}

	/**
	 * 计算距离
	 * @param start 开始点
	 * @param end   结束点
     * @return      距离
     */
	public static int calculateLineDistance(LatLonPoint start, LatLonPoint end) {
		int distance = 0;
		if (start == null || end == null) {
			return distance;
		}
		double startLat = start.getLatitude();
		double startLon = start.getLongitude();
		double endLat = end.getLatitude();
		double endLon = end.getLongitude();
		LatLng amapStart = new LatLng(startLat, startLon);
		LatLng amapEnd = new LatLng(endLat, endLon);
		return (int) AMapUtils.calculateLineDistance(amapStart, amapEnd);
	}

	/**
	 * 获取公交车标题
	 * @param duration 时间
	 * @param distance 距离
     * @return         结果
     */
	public static String getBusRouteTitle(int duration, int distance) {
		String dur = SchemeUtil.getFriendlyTime(duration);
		String dis = SchemeUtil.getFriendlyLength(distance);
		return dur + "(步行" + dis + ")";
	}
}
