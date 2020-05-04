package com.CtripSpider.other;

/**
 * 配置文件管理
 * 
 * 
 * */

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.ToolBox.util.FileTool;
import com.ToolBox.util.StringTool;

public class ConfigureOptions {
	private static final StringTool st = new StringTool();
	private static final FileTool ft = new FileTool();
	/**配置文件路径*/
	private static final String configureFilePath = "conf/conf.ini";

/**	管理配置文件里的内容是否齐全有效*/
	public boolean checkConfOptions(String checkConfile) {
		if (st.getByJson(checkConfile(), "DRIVERPATH").equals("")) {
			System.err.println("you need set \"DRIVERPATH\":\"your driver path !\"");
			return false;
		} else {
			if (st.getByJson(checkConfile(), "CITYLINKPATH").equals("")) {
				System.err.println("you need set \"CITYLINKPATH\":\"your url link text save path !\"");
				return false;
			} else {
				if (st.getByJson(checkConfile(), "CITYLINK").equals("")) {
					System.err.println("you need set \"CITYLINK\":\"start url link !\"");
					return false;
				} else {
					if (st.getByJson(checkConfile(), "CITYNAME").equals("")) {
						System.err.println("you need set \"CITYNAME\":\" city name !\"");
						return false;
					} else {
						return true;
					}
				}
			}
		}
	}

	/**
	 * 判断配置文件是否存在
	 * 
	 */
	public String checkConfile() {
		File fileDIR = new File("conf");
		if (!fileDIR.exists()) {
			System.err.println("没有找到配置文件目录! (./conf)");
		} else {
			File file = new File(configureFilePath);
			if (file.exists()) {
				return getConfigureFile();
			} else {
				System.err.println("没有找到配置文件! [ " + configureFilePath + " ]");
			}
		}
		return null;
	}

	/**
	 * 添加一个新的类型与值
	 * <p>
	 * key:类型名称 value:类型的值
	 */
	public boolean AddKey(String key, String value) {
		if (AddKey(key)) {
			return Change(key, value);
		}
		return false;
	}

	/**
	 * 获取一个类型的值
	 * <p>
	 * key:类型名称
	 */
	public String getKey(String key) {
		return st.getByJson(getConfigureFile(), key).replaceAll("\\s+", "");
	}

	/**
	 * 返回类型是否存在
	 * <p>
	 * key:类型名称
	 */
	public boolean isExists(String key) {
		return getKey(key).isEmpty() ? false : true;
	}

	/**
	 * 删除一个类型
	 * <p>
	 * key:类型名称
	 */
	public boolean DeleteKey(String key) {
		ft.writeFile(getConfigureFile().replaceAll(",\"" + key + "\":\"(.+?\")", ""), configureFilePath);
		System.err.println(st.getByJson(getConfigureFile(), key));
		return isExists(key) ? false : true;
	}

	/**
	 * 添加一个新的空类型
	 * <p>
	 * key:类型名称
	 */
	public boolean AddKey(String key) {
		String conf = getConfigureFile();
		if (st.getByJson(conf, key).isEmpty()) {
			ft.writeFile(conf.replaceAll("\\}", ",\"" + key + "\":\"t\"\\}"), configureFilePath);
			return true;
		}
		return false;
	}
	
	/**
	 * 修改一个已存在的类型与值
	 * <p>
	 * key:类型名称 value:类型的值
	 */
	public boolean Change(String key, String value) {
		String conf = getConfigureFile();
		if (st.getByJson(conf, key).isEmpty()) {
			System.out.println("[key : " + key + " ] is not found !");
			return false;
		} else {
			new File(configureFilePath).delete();
			ft.writeFile(conf.replaceAll("\"" + key + "\":\"(.+?\")", "\"" + key + "\":\"" + value + "\""),
					configureFilePath);
			return isExists(key);
		}
	}

	/**
	 * 返回配置表里的内容
	 */
	public String getConfigureFile() {
		try {
			return new String(ft.readFile(configureFilePath).getBytes(), "gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 以数组方式返回配置表里的内容
	 */
	public String[] getAllKeyAndValue() {
		return getConfigureFile().replaceAll("\\{|\\}|\"", "").split(",");
	}

	/**
	 * 返回需要爬取的酒店页数
	 */
	public int getHotelPage() {
		return Integer.parseInt(checkConfOptions(checkConfile()) ? getKey("HOTELPAGE") : "1");
	}

	/**
	 * 返回需要爬取的酒店名称
	 */
	public String getHotelName() {
		return checkConfOptions(checkConfile()) ? getKey("HOTELNAME") : "";
	}

	/**
	 * 返回需要爬取的城市名称
	 */
	public String getCityName() {
		return checkConfOptions(checkConfile()) ? getKey("CITYNAME") : "";
	}

	/**
	 * 返回需要爬取的城市链接
	 */
	public String getCityLink() {
		return checkConfOptions(checkConfile()) ? getKey("CITYLINK") : "";
	}

	/**
	 * 返回浏览器驱动路径
	 */
	public String getDriverPath() {
		return checkConfOptions(checkConfile()) ? getKey("DRIVERPATH") : "";
	}

	/**
	 * 返回城市爬取链接的保存路径
	 */
	public String getCityLinkPath() {
		return checkConfOptions(checkConfile()) ? getKey("CITYLINKPATH") : "";
	}
}
