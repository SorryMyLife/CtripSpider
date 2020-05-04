package com.CtripSpider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.CtripSpider.utils.CtripHotelUtils;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.HtmlTool;

/**
 * Я�̾Ƶ�����
 * ��Ҫ������ȡ�Ƶ����ơ���ַ�����꾭γ���۸�Ԥ��ͼ���û�����ͼ�����°�ʱ�䡢��ϵ��ʽ
 * 
 * */
public class CtripHotelSpider extends CtripHotelUtils {
	String jdbc = "jdbc:mysql://127.0.0.1:3306/ctriphotel?serverTimezone=GMT";
	Connection c = null;
	PreparedStatement ps = null;

	public CtripHotelSpider() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection(jdbc, "root", "123");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * �����������
	 */
	public void Run() {
		int MaxPageNum = configureOption.getHotelPage();
//		String search_name = configureOption.getHotelName();
//		Run(search_name, MaxPageNum);
		String NowCity = configureOption.getKey("NowCity");
		System.out.println(NowCity);
		if (NowCity != null || !NowCity.isEmpty()) {
			String citys[] = getCitys().split("\n");
			for (int i = 0; i < citys.length;) {
				if (citys[i].equals(NowCity)) {
					Run(citys[i].replaceAll("��", ""), MaxPageNum);
					for (int j = i; j < citys.length; j++) {
						Run(citys[j].replaceAll("��", ""), MaxPageNum);
					}
//					System.out.println("is equals");
				} else {
					i++;
				}
			}
		} else {
			for (String search_name : getCitys().split("\n")) {
				Run(search_name.replaceAll("��", ""), MaxPageNum);
//				System.out.println("no equals");
			}
		}

	}

	/**
	 * �����������
	 * <p>
	 * search_name : �Ƶ���߾�������
	 * <p>
	 * MaxPageNum : ���ҳ��
	 */
	public void Run(String search_name, int MaxPageNum) {
		File configureHotelLink = new File(configureOption.getCityLinkPath() + "/Hotel-" + search_name);
		int start = 1;
		if (configureHotelLink.exists()) {
			search_name = configureHotelLink.getName().split("-")[1];
			try {
				start = getNowPageNum(search_name) + 1;
			} catch (Exception e) {
				start = 1;
			}
		} else if (search_name.isEmpty()) {
			search_name = "�Ϻ�";
		}
		System.out.println("= 82 == ����: [ " + search_name + " ]");
		String page = hu.getResponse(getLink(search_name, start));
		int max = 1;
		try {
			max = getByMaxPageNum(page);
		} catch (Exception e) {
			max = 1;
		}
//		System.out.println(max);
		System.out.println("= 91 == ��Ҫ��ȡ��ҳ������ : " + MaxPageNum + "\n����ҳ��λ�� : [ " + start + " ]  ---  ����λ�� : [ "
				+ (MaxPageNum + start) + " ]  ---  һ���� : [ " + max + " ] ҳ");
		for (int i = 1; i <= MaxPageNum; i++) {
			if (start <= max) {
				getHotelInfo(search_name, hu.getResponse(getLink(search_name, start)));
				System.out.println("= 96 == [ " + i + " ] ҳ��ȡ��ɻ�ʣ�� [ " + (MaxPageNum - i) + " ]");
			} else {
				System.out.println("= 98 == ��ȡ������� [ " + search_name + " ]\n��ȡ�� [ " + start + " ]ҳ");
				break;
			}
			if (configureHotelLink.exists()) {
				configureHotelLink.delete();
			}
			ft.writeFile(getLink(search_name, start), configureHotelLink.toString());
			start++;
		}
		System.out.println("= 107 == "+search_name + "  ��ȡ [ " + start + " ] ҳ��� !");
		configureOption.Change("NowCity", search_name + "��");
	}

	public String getCitys() {
		String sql = "select cityname from city where ISNULL(countyname) and !ISNULL(cityname)";
		StringBuilder sb = new StringBuilder();
		try {
			ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sb.append(rs.getString(1).replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
				sb.append("\n");
//				System.out.println(rs.getString(1).replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public void test() throws Exception {
		HttpUtils hu = new HttpUtils();
		String response = hu.getResponse("http://www.mca.gov.cn/article/sj/xzqh/1980/201903/201903011447.html");
		String cityA = null, cityB = null, cityC = null;
		for (String tr : new HtmlTool(response).getByElement("tr").toString().split("\n")) {
			if (tr.indexOf("mso-height-source:userset;height:14.25pt") != -1) {
				String td[] = new HtmlTool(tr).getByElement("td").toString().split("\n");
				String id = st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "");
				String cityName = st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("/span", "")
						.replaceAll("\\?", "").replaceAll("\\s+", "");

				if (td[2].indexOf("ʡ") != -1) {
//					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
//							+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
//					System.out.println(id + " --- " + cityName);
					cityA = id + "---" + cityName;
				} else if (td[2].indexOf("��") != -1) {
//					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
//					+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
					cityB = id + "---" + cityName;
					if (cityA == null) {
						cityA = cityB;
					}
					String sql = "insert into city(" + " provinceid, provincename,cityid,cityname" + ")values(?,?,?,?)";
					ps = c.prepareStatement(sql);
					ps.setString(1, cityA.split("---")[0].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(2, cityA.split("---")[1].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(3, cityB.split("---")[0].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(4, cityB.split("---")[1].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));

					ps.execute();
//					System.out.println(id + " --- " + cityName);
				} else {
//					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
//							+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
//					System.out.println(id + " --- " + cityName);
					cityC = id + "---" + cityName;
					if (cityA == null) {
						cityA = cityB;
					}

//					System.out.println("ʡ: "+cityA+",��: "+cityB+",��: "+cityC);
					String sql = "insert into city(" + " provinceid, provincename,cityid,cityname,countyid,countyname"
							+ ")values(?,?,?,?,?,?)";
					ps = c.prepareStatement(sql);
					ps.setString(1, cityA.split("---")[0].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(2, cityA.split("---")[1].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(3, cityB.split("---")[0].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(4, cityB.split("---")[1].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(5, cityC.split("---")[0].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));
					ps.setString(6, cityC.split("---")[1].replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", ""));

					ps.execute();

				}

			}
		}
	}

	public String getCity() {
		return "";
	}

//	public static void main(String[] args) throws Exception {
//		CtripHotelSpider ctripHotelUtils = new CtripHotelSpider();
////		ctripHotelUtils.getCitys();
//		ctripHotelUtils.Run();
////		ctripHotelUtils.test();
//	}

}
