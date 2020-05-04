package com.CtripSpider.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.CtripSpider.other.ConfigureOptions;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.HtmlTool;
import com.ToolBox.util.StringTool;

public class CtripScenicUtils {
	private ConfigureOptions configureOptions = new ConfigureOptions();
	private  StringTool st = new StringTool();
	private  HttpUtils hu = new HttpUtils();
//	private  CtripSpiderImage ctripSpiderImage = new CtripSpiderImage();
	private  String jdbc = "jdbc:mysql://127.0.0.1:3306/ctriphotel?serverTimezone=GMT";
	private Connection c = null;
	private PreparedStatement ps = null;
	
	public CtripScenicUtils() {
		
	}
	
	private void initJDBC() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection(jdbc, "root", "123");
		} catch (ClassNotFoundException | SQLException e) {
			try {
				c.close();
//				c2.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.err.println("请确保数据库ctriphotel存在");
			e.printStackTrace();
		}
	}

	private  String hs[] = {
			"accept: text/html,application/xhtml,xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
			"accept-language: zh-CN,zh;q=0.9", "content-type: application/json", "upgrade-insecure-requests: 1",
			"user-agent: Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1" };

	@SuppressWarnings("static-access")
	private void sleep(int time) {
		try {
			new Thread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void closeJDBC() {
		try {
			c.close();
//			c2.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//更新城市列表
	public void Upgrade() {
		
		try {
			initJDBC();
			String sql  ="insert into sceniccity(sceniccityname,sceniccitylink) values(?,?)";
			ps = c.prepareStatement(sql);
			String page = hu.getPage("https://you.ctrip.com/destinationsite/TTDSecond/Place");
			for(String href : new HtmlTool(page).getByElement("li").toString().split("\n")) {
				if(href.indexOf("/you/place") != -1) {
					String link = new HtmlTool(href).getByElementValue("href").toString().replaceAll("\\s+|.html", "");
					String text = st.getByString(href, "\">(.+?</a)", "\">|</a").replaceAll("旅游攻略", "");
					link = "https://you.ctrip.com/sight/"+link.substring(link.lastIndexOf("/")+1);
//					System.out.println(link + " -- " +text);
					ps.setString(1, text);
					ps.setString(2, link);
					ps.execute();
//					index++;
				}
			}
			closeJDBC();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
		
	}
	
	
	private void insert(String... data) {
		try {
			initJDBC();
			String sql = "insert into scenic(sceneryNo,sceneryName,price,areaId,areaName,address,latLon,sceneryPic,sceneryImages,sceneryLevel,openTime,introduction,isOnline,status,ruralImgBig,ruralImgSmall,contacts)"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			ps = c.prepareStatement(sql);
			for (int i = 0; i < data.length; i++) {
				if (i == 3) {
					ps.setInt((i + 1), Integer.parseInt(data[i]==null ? "8080" : data[i].replaceAll("\\s+", "")));
				} else {
					ps.setString((i + 1), data[i]);
				}
			}
			ps.execute();
			closeJDBC();
		} catch (SQLException e) {
			System.err.println(
					"== 96 == 请确认数据已经被启动并存在scenic数据表 : sceneryNo,sceneryName,price,areaId,areaName,address,latLon,sceneryPic,sceneryImages,sceneryLevel,openTime,introduction,isOnline,status,ruralImgBig,ruralImgSmall,contacts");
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
	}
	
	

	private String getGuidAndPageid(String link) {
		System.setProperty("webdriver.chrome.driver", configureOptions.getDriverPath());
		System.setProperty("webdriver.chrome.silentOutput", "true");
		String guidAndPageid = null;
		ChromeOptions co = new ChromeOptions();
		List<String> list = new ArrayList<String>();
		list.add("enable-automation");
		co.addArguments("disable-gpu", "disable-impl-side-painting", "disable-gpu-sandbox",
				"disable-accelerated-2d-canvas", "no-sandbox", "test-type=ui", "disable-dev-shm-usage", "headless");// 加入这些选项可以减少Chrome的资源消耗
		co.setExperimentalOption("excludeSwitches", list);
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("deviceName", "Nexus 10");
//		co.setExperimentalOption("mobileEmulation", map);// 设置以安卓模式访问页面
		ChromeDriver cd = null;
		try {
			cd = new ChromeDriver(co);
			cd.get(link);
			sleep(2000);
			String guid = cd.manage().getCookieNamed("GUID").getValue();
			String pageid = cd.manage().getCookieNamed("_bfa").getValue();
			pageid = pageid.substring(pageid.lastIndexOf(".") + 1);
			guidAndPageid = guid + "-" + pageid;
			cd.quit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			cd.quit();
		}
		System.out.println(guidAndPageid);
		return guidAndPageid;
	}

	@SuppressWarnings("null")
	private void getScenicInfo(String SightId, String districtId, String guid, String pageid, String scenicNo,
			String cityName) {
		hu.setHeaders(hs);
		// String guid="09031030311219772972";
		String param = "{\"SightId\":" + SightId + ",\"districtId\":" + districtId
				+ ",\"isSearchLandingPage\":false,\"fromChannel\":2,\"coverImageId\":null,\"fromCategoryId\":null,\"head\":{\"cid\":\""
				+ guid
				+ "\",\"ctok\":\"\",\"cver\":\"1.0\",\"lang\":\"01\",\"sid\":\"8888\",\"syscode\":\"09\",\"auth\":null,\"extension\":[{\"name\":\"protocal\",\"value\":\"https\"}]},\"contentType\":\"json\"}";
		String url1 = "https://m.ctrip.com/restapi/soa2/13342/json/getsightdetail?_fxpcqlniredt=" + guid;
		String data = hu.getPostResponse(url1, param);
		if (data.isEmpty()) {
			System.out.println("== 150 == 陷入递归......");
			getScenicInfo(SightId, districtId, guid, pageid, scenicNo, cityName);
		} else {
//			CitysUtils citysUtils = new CitysUtils();
			String Introdcution = st.getByString(data, "introdcution\":\"(.+?>\",\")", "introdcution\":\"|>\",\"");
			String address = st.getByJson(data, "address");
			address=address.indexOf("市") != -1?address:cityName + "市" +address ;
			String latLon = st.getByJson(data, "bLat") + "," + st.getByJson(data, "bLon");
			String sightName = st.getByJson(data, "sightName");
			String openTimeDesc = st.getByJson(data, "openTimeDesc");
			openTimeDesc = st.getByString(openTimeDesc, "\\d+:\\d+-\\d+:\\d+", "");
			if (openTimeDesc.isEmpty()) {
				openTimeDesc = "全天开放";
			}
			String phone = st.getByJson(data, "phone");
			if (phone.equals("null") || phone.isEmpty() || phone.length() < 3) {
				phone = "没有联系方式";
			}
			String poiPhotos = getPoiPhotosImages(data);
			String displayMinPrice = st.getByJson(data, "displayMinPrice");
			if (displayMinPrice.equals("0")) {
				displayMinPrice = "免费开放";
			}
			String areaid = null, areaName = null;

			if (ScenicExists(sightName)) {
				System.out.println("= 224 == 跳过 [ " + sightName + " ]");
			} else {	
				String tmpIntrodcution = getIntrodcution(SightId, guid, pageid);
				if (tmpIntrodcution.isEmpty()) {
					if (Introdcution.isEmpty()) {
						StringBuilder sb = new StringBuilder();
						for (String img : poiPhotos.split("\n")) {
							sb.append("<p><img src=\"" + img.replaceAll("\\s+", "") + "\"></p>");
						}
						Introdcution = sb.toString();
					}
				} else {
					Introdcution = tmpIntrodcution;
				}
				Introdcution = Introdcution.replaceAll("\\\\", "");
				String tmpImages = st.getByAllString(Introdcution, "src=\"(.+?.(jpg|jpeg|png))", "src=\"|\\s+|,");
				poiPhotos += tmpImages;
				TreeSet<String> set = new TreeSet<String>();
				for (String s : poiPhotos.split("\n")) {
					set.add(s);
				}
				StringBuilder imagesSb = new StringBuilder();
				for (String img : set) {
					try {
						if (img != null || !img.isEmpty()) {
							imagesSb.append(img + "\n");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				poiPhotos = imagesSb.length() > 0 ? imagesSb.toString():poiPhotos;
				System.out.println( "==  265  == "+sightName + "  ---  " + address + " - " + SightId + " - " + districtId);
//				System.out.println(Introdcution);
//				System.out.println("-----------------------------------------------------------------------------");

				insert(scenicNo, sightName, displayMinPrice, areaid, areaName, address, latLon,
						hu.getFileName(poiPhotos.split("\n")[0]) + "|" + poiPhotos.split("\n")[0],
						poiPhotos.replaceAll("\n", ",").replaceAll(",$", ""), "1", openTimeDesc, Introdcution, "1", "1",
						hu.getFileName(poiPhotos.split("\n")[0]) + "|" + poiPhotos.split("\n")[0],
						hu.getFileName(poiPhotos.split("\n")[0]) + "|" + poiPhotos.split("\n")[0], phone);
				
			}
		}

	}

	private long getLastSceneryNo() {
		try {
			initJDBC();
			ps = c.prepareStatement("select sceneryNo from scenic order by id desc limit 1");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
			closeJDBC();
		} catch (SQLException e) {
			System.err.println("== 290 == 请确认数据已经被启动并存在scenic数据表 : sceneryNo");
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
		return 000000L;
	}

	private String getIntrodcution(String SightId, String GUID, String pageid) {
		String url = "https://sec-m.ctrip.com/restapi/soa2/12530/json/scenicSpotDescription?_fxpcqlniredt=" + GUID;
		String param = "{\"viewid\":" + SightId + ",\"retype\":1,\"searchtype\":1,\"pageid\":" + pageid
				+ ",\"ver\":\"8.2.2\",\"head\":{\"cid\":\"" + GUID
				+ "\",\"ctok\":\"\",\"cver\":\"1.0\",\"lang\":\"01\",\"sid\":\"8888\",\"syscode\":\"09\",\"auth\":\"\",\"extension\":[{\"name\":\"protocal\",\"value\":\"https\"}]},\"contentType\":\"json\"}";
		hu.setHeaders(hs);
		String introdcution = hu.getPostPage(url, param);
		introdcution = st.getByString(st.getByString(introdcution, "详情介绍(.+?desclist(.+?tcode))", ""),
				"text(.+?\"label)", "text\":\"|\",\"label").replaceAll("\\\\", "");
//		System.out.println(introdcution);
		return introdcution;
	}

	/**
	 * 获取post请求页面里所有图片链接
	 * <p>
	 * ResponsePage：post请求后的页面
	 * <p>
	 */
	private String getPoiPhotosImages(String ResponsePage) {
		StringBuilder sb = new StringBuilder();
		int startNum = 1, endNum = 5;
		for (String img : st.getByString(st.getByString(ResponsePage, "poiPhotos(.+?\\],)", ""), "\\[(.+?\\])", "")
				.split(",")) {
			if (startNum >= endNum) {
				break;
			}
			sb.append(img.replaceAll("\\[|\"|\\]", "") + "\n");
			startNum++;
		}
		return sb.toString();
	}

	private void ScenicStart(String ScenicCityLink, String cityName, int maxNum) {
		String guidAndPageid = null;
		if (configureOptions.getKey("GUID").isEmpty() && configureOptions.getKey("PAGEID").isEmpty()) {
			guidAndPageid = getGuidAndPageid(ScenicCityLink + "/s0-p1.html");
			if (guidAndPageid != null) {
				if (!guidAndPageid.split("-")[0].isEmpty() || !guidAndPageid.split("-")[1].isEmpty()) {
					configureOptions.Change("GUID", guidAndPageid.split("-")[0]);
					configureOptions.Change("PAGEID", guidAndPageid.split("-")[1]);
				}
			}
		} else {
			guidAndPageid = configureOptions.getKey("GUID") + "-" + configureOptions.getKey("PAGEID");
		}
		System.out.println("= 341 == "+guidAndPageid);
		AtomicLong at = new AtomicLong(getLastSceneryNo());
		String nowPageNum=st.getByString(ScenicCityLink, "s0(.+?.html)", "s0-p|.html").replaceAll("\\s+", "");
		int nowPage = nowPageNum.isEmpty()?1:Integer.parseInt(nowPageNum);
		maxNum = nowPage+maxNum;
		hu.setHeaders(hs);
		for (int PageNum = nowPage; PageNum <= maxNum; PageNum++) {
			String url =ScenicCityLink.replaceAll("/s0-p\\d+.html", "")+ "/s0-p" + PageNum + ".html";
//			System.out.println(url);
			TreeSet<String> set = new TreeSet<String>();
			for (String s : st.getByAllString(hu.getResponse(url), "href=\"/sight/\\S+\\d+/\\d+.html\"", "href=|\"")
					.split("\n")) {
				set.add(s);
			}
//			System.out.println(set.size());
			if (set.size() < 1) {
				if (exists(cityName)) {
					Update(cityName, url);
				} else {
					try {
						initJDBC();
						ps = c.prepareStatement("insert into nowcity(cityname,citynamelink)values(?,?)");
						ps.setString(1, cityName);
						ps.setString(2, url);
						ps.execute();
						closeJDBC();
					} catch (SQLException e) {
						e.printStackTrace();
					}finally {
						closeJDBC();
					}
				}
				System.out.println("== 376 == 跳过 [ " + cityName + " ] -- [ " + url + " ] ");
				break;
			} else {
				for (String s : set) {
//					System.out.println(s);
					String SightId = st.getByString(s.split("/")[3], "\\d+", "\\s+").replaceAll("\\s+", ""),
							districtId = st.getByString(s.split("/")[2], "\\d+", "\\s+").replaceAll("\\s+", ""),
//					System.out.println(SightId+ " ---  "+districtId);
							guid = guidAndPageid.split("-")[0], pageid = guidAndPageid.split("-")[1];
					getScenicInfo(SightId, districtId, guid, pageid, String.format("%06d", at.incrementAndGet()),
							cityName);
					
//					sleep(getSleepTime(2, 1));
				}
			}
			if (exists(cityName)) {
				Update(cityName, url);
			} else {
				try {
					initJDBC();
					ps = c.prepareStatement("insert into nowcity(cityname,citynamelink)values(?,?)");
					ps.setString(1, cityName);
					ps.setString(2, url);
					ps.execute();
					closeJDBC();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					closeJDBC();
				}
			}
			System.out.println("=== 407 ==== cityname : " + cityName + " -- url : " + url.replaceAll("/s0-p\\d+.html", ""));
			changeCityConf(cityName,url.replaceAll("/s0-p\\d+.html", ""));
		}
	}

	private void Update(String cityName, String link) {
		String sql = "update nowcity set citynamelink=\"" + link + "\" where cityname=\"" + cityName + "\"";
		try {
			initJDBC();
			ps = c.prepareStatement(sql);
			ps.executeUpdate();
			closeJDBC();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
	}

	@SuppressWarnings("unused")
	private int getSleepTime(int max, int min) {
		return (new Random().nextInt(max - min + 1) + min) * 1000;
	}

	private String getNowCity(String cityName) {
		String nowCitys = null;
		String sql = "select cityname,citynamelink from  nowCity";
		try {
			initJDBC();
//			ps = c2.prepareStatement(sql);
			ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(cityName)) {
					nowCitys = rs.getString(1) + "--" + rs.getString(2);
				}
			}
			closeJDBC();
		} catch (SQLException e) {
			System.err.println("== 446 == 请确认数据已经被启动并存在nowCity数据表");
			e.printStackTrace();
		}
//		finally {
//			closeJDBC();
//		}
		return nowCitys;
	}

	private boolean exists(String key) {
		try {
			initJDBC();
			ps = c.prepareStatement(" select 1 from nowcity where cityname='" + key + "' limit 1");
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1) == 1;
			}
			closeJDBC();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
		return false;
	}

	private boolean ScenicExists(String key) {
		try {
			initJDBC();
			ps = c.prepareStatement(" select 1 from scenic where sceneryName='" + key + "' limit 1");
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1) == 1;
			}
			closeJDBC();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
		return false;
	}
	
	public void changeCityConf(String cityName , String url) {
		configureOptions.Change("CITYNAME", cityName);
		configureOptions.Change("CITYLINK", url);
	}
	
	public void start(String cityName, int maxPage) {
		String nowCity = getNowCity(cityName);
		if (nowCity != null) {
			System.out.println( " == 468 == " +nowCity.split("--")[0]+ nowCity.split("--")[1]);
			ScenicStart(nowCity.split("--")[1], nowCity.split("--")[0], maxPage);
//			changeCityConf(nowCity.split("--")[0].replaceAll("/s0-p\\d+.html", ""),nowCity.split("--")[1]);
		} 
		
		
		
//		HashMap<String, String> citysMap = new HashMap<String, String>();
		
		String sql = "select sceniccityname,sceniccitylink from sceniccity";
		try {
			initJDBC();
			ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
//				System.out.println(rs.getString(2)+"-"+rs.getString(3)+"-"+rs.getString(4));
//				citysMap.put(rs.getString(2),rs.getString(4));
				if (rs.getString(1).equals(cityName)) {
					continue;
//					System.out.println(rs.getString(2)+" -- "+rs.getRow()+" -- "+rs.getString(1));
//					ScenicStart(rs.getString(2), rs.getString(1), maxPage);
//					break;
//					while (rs.next()) {
//						ScenicStart(rs.getString(2), rs.getString(1), maxPage);
//					}
				}else {
					ScenicStart(rs.getString(2), rs.getString(1), maxPage);
				}
			}
			closeJDBC();
		} catch (SQLException e) {
			System.err.println("请确认数据已经被启动并存在sceniccity数据表");
			e.printStackTrace();
		}finally {
			closeJDBC();
		}
		
//		ScenicStart("https://you.ctrip.com/sight/guiyang33","贵阳", maxPage);
		
		
	}

//	public static void main(String[] args) {
//		ConfigureOptions configureOptions = new ConfigureOptions();
//		String cityName = configureOptions.getKey("CITYNAME");
//		int maxPage = Integer.parseInt(configureOptions.getKey("CITYPAGENUM"));
////		System.out.println("hello");
//		CtripScenicUtils csu = new CtripScenicUtils();
//		csu.ossSql("");
////		csu.start(cityName, maxPage);
////		System.out.println("hello");
//
//	}
}
