package com.CtripSpider.other;

/**
 * �����ļ�����
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
	/**�����ļ�·��*/
	private static final String configureFilePath = "conf/conf.ini";

/**	���������ļ���������Ƿ���ȫ��Ч*/
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
	 * �ж������ļ��Ƿ����
	 * 
	 */
	public String checkConfile() {
		File fileDIR = new File("conf");
		if (!fileDIR.exists()) {
			System.err.println("û���ҵ������ļ�Ŀ¼! (./conf)");
		} else {
			File file = new File(configureFilePath);
			if (file.exists()) {
				return getConfigureFile();
			} else {
				System.err.println("û���ҵ������ļ�! [ " + configureFilePath + " ]");
			}
		}
		return null;
	}

	/**
	 * ���һ���µ�������ֵ
	 * <p>
	 * key:�������� value:���͵�ֵ
	 */
	public boolean AddKey(String key, String value) {
		if (AddKey(key)) {
			return Change(key, value);
		}
		return false;
	}

	/**
	 * ��ȡһ�����͵�ֵ
	 * <p>
	 * key:��������
	 */
	public String getKey(String key) {
		return st.getByJson(getConfigureFile(), key).replaceAll("\\s+", "");
	}

	/**
	 * ���������Ƿ����
	 * <p>
	 * key:��������
	 */
	public boolean isExists(String key) {
		return getKey(key).isEmpty() ? false : true;
	}

	/**
	 * ɾ��һ������
	 * <p>
	 * key:��������
	 */
	public boolean DeleteKey(String key) {
		ft.writeFile(getConfigureFile().replaceAll(",\"" + key + "\":\"(.+?\")", ""), configureFilePath);
		System.err.println(st.getByJson(getConfigureFile(), key));
		return isExists(key) ? false : true;
	}

	/**
	 * ���һ���µĿ�����
	 * <p>
	 * key:��������
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
	 * �޸�һ���Ѵ��ڵ�������ֵ
	 * <p>
	 * key:�������� value:���͵�ֵ
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
	 * �������ñ��������
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
	 * �����鷽ʽ�������ñ��������
	 */
	public String[] getAllKeyAndValue() {
		return getConfigureFile().replaceAll("\\{|\\}|\"", "").split(",");
	}

	/**
	 * ������Ҫ��ȡ�ľƵ�ҳ��
	 */
	public int getHotelPage() {
		return Integer.parseInt(checkConfOptions(checkConfile()) ? getKey("HOTELPAGE") : "1");
	}

	/**
	 * ������Ҫ��ȡ�ľƵ�����
	 */
	public String getHotelName() {
		return checkConfOptions(checkConfile()) ? getKey("HOTELNAME") : "";
	}

	/**
	 * ������Ҫ��ȡ�ĳ�������
	 */
	public String getCityName() {
		return checkConfOptions(checkConfile()) ? getKey("CITYNAME") : "";
	}

	/**
	 * ������Ҫ��ȡ�ĳ�������
	 */
	public String getCityLink() {
		return checkConfOptions(checkConfile()) ? getKey("CITYLINK") : "";
	}

	/**
	 * �������������·��
	 */
	public String getDriverPath() {
		return checkConfOptions(checkConfile()) ? getKey("DRIVERPATH") : "";
	}

	/**
	 * ���س�����ȡ���ӵı���·��
	 */
	public String getCityLinkPath() {
		return checkConfOptions(checkConfile()) ? getKey("CITYLINKPATH") : "";
	}
}
