package com.CtripSpider.utils;

import java.util.ArrayList;
import java.util.List;

import com.CtripSpider.other.WebDriverClick;
import org.openqa.selenium.chrome.ChromeDriver;

import com.CtripSpider.Image.CtripSpiderImage;
import com.CtripSpider.info.CtripScenicInfo;
import com.CtripSpider.info.CtripScenicMoney;
import com.CtripSpider.other.ConfigureOptions;
import com.CtripSpider.other.CtripHeadUtils;
import com.CtripSpider.other.HtmlMerge;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.FileTool;
import com.ToolBox.util.StringTool;

/**
 * 用于实现携程爬虫核心功能部分 you链接网站筛选 piao链接网站筛选
 * 
 * 
 */

public class CtripScenicSpiderInfo {

	public static final StringTool st = new StringTool();
	public static final FileTool ft = new FileTool();
	public static final String htmlEnd = "</body></html>";
	public static final HttpUtils hu = new HttpUtils();
	public static final CtripHeadUtils HEADERS = new CtripHeadUtils();
	public static final CtripSpiderImage ctripSpiderImage = new CtripSpiderImage();
	public static final WebDriverClick driverClicked = new WebDriverClick();
	public static final HtmlMerge htmlMerge = new HtmlMerge();
	public static final ConfigureOptions configureOptions = new ConfigureOptions();
	public static final String hs[] = { "accept: application/json", "accept-language: zh-CN,zh;q=0.9",
			"content-type: application/json",
			"user-agent: Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Mobile Safari/537.36" };

	/**
	 * 线程休眠 time ： 需要休眠的毫秒
	 * <p>
	 * PS：1000==1s
	 * 
	 */
	@SuppressWarnings("static-access")
	public void sleep(int time) {
		try {
			new Thread().sleep(time);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 获取付费景区div图文简介
	 * <p>
	 * data ： 景区图文
	 * <p>
	 */
	public String getMScenicText(String data) {
		return st.getByString(data.replaceAll("\\n|\\r|\\t|\\v", ""),
				"<div data-target=\"introduce-tab-B\"(.+?</div>(.+?</div>))", "") + "</div></div>";
	}

	/**
	 * 获取免费景区div图文简介
	 * <p>
	 * data ： 景区图文
	 * <p>
	 */
	public String getGsScenicText(String data) {
		return st.getByString(data,
				"<div class=\"tickets-info sight-infowrap js_tips_content current(.+?js_functional_a(.+?js_functional_a(.+?</div>)))",
				"") + "</div>";
	}

	/**
	 * 获取免费景区图文简介
	 * <p>
	 * cd：谷歌浏览器驱动
	 * <p>
	 * gsHead：免费景点头部
	 * <p>
	 * end：统一格式尾部
	 * <p>
	 * 
	 */
	@SuppressWarnings("static-access")
	public String getGSScenicText(ChromeDriver cd, String gsHead, String end) {
		String data = getScenicText(cd);

		if (data == null || data.equals("")) {
			cd.navigate().refresh();
			sleep(2000);
			data = getScenicText(cd);
		}
		if (data == null || data.equals("")) {
			data = "无法获取到当前景点的图文信息";
		} else {
			data = data.replaceAll("\\n|\\r|\\t|\\v", "");
//			cd.quit();
			if (data.indexOf("图文") != -1) {
				data = HEADERS.MHEADER + getMScenicText(driverClicked.clickInfo(cd)) + end;
				data = data.replaceAll("data-src", "src");
			} else {
				data = gsHead + getGsScenicText(data) + end;
				data = data.replaceAll("data-src", "src");

			}
		}
		return data;
	}

	/**
	 * 获取景区单击后的界面
	 * <p>
	 * 需要传入ChromeDriver参数
	 * <p>
	 * 
	 */
	public String getScenicText(ChromeDriver cd) {
		String data = null;
		sleep(3000);
//		int count = 1;
		if (driverClicked.isSelect(cd, "[class='find_morelink js_web']")) {
			data = cd.getPageSource();
//			count++;
		} else if (driverClicked.isSelect(cd, "[class='ttd-col-auto ellips intr_title']")) {
			data = driverClicked.clickInfo(cd);
//			count++;
		} else if (driverClicked.isSelect(cd, "[class='titleView titleViewBorder']")) {
			data = cd.getPageSource();
//			count++;
		}
//		System.err.println("count : "+count);
		return data;
	}

	/**
	 * 获取付费景区div内容
	 * <p>
	 * 需要传入Chromedriver谷歌浏览器驱动
	 * <p>
	 * 付费景区头部
	 * <p>
	 * 网页统一尾部
	 * <p>
	 * 
	 */
	public String getMScenicText(ChromeDriver cd, String mHead, String end) {
		String data = getScenicText(cd);
		if (data == null || data.equals("")) {
			cd.navigate().refresh();
			sleep(2000);
			data = getScenicText(cd);
		}
		if (data == null || data.equals("")) {
			data = "无法获取到当前景点的图文信息";
		} else {
			data = data.replaceAll("\\n|\\r|\\t|\\v", "");
//			cd.quit();
			data = mHead + getMScenicText(data) + end;
			data = data.replaceAll("data-src", "src");
		}
		return data;
	}

	/**
	 * 获取付费景区价格内容
	 * <p>
	 * 需要传入景区post内容
	 * <p>
	 * 传入一个带Money对象的list
	 * <p>
	 */
	public void getMMoney(String data, List<CtripScenicMoney> moneyList) {
		for (String json : st
				.getByAllString(st.getByString(data, "saleunits\":\\[\\{(.+?\\}\\],)", ""), "\\{(.+?\\})", "")
				.split("\n")) {
//			System.out.println(json);
			CtripScenicMoney m = new CtripScenicMoney(st.getByJson(json, "name"), st.getByJson(json, "propleproperty"),
					st.getByString(json, "\"price\":\\d+", "\"price\":").replaceAll("\\s+", "").equals("") ? null
							: st.getByString(json, "\"price\":\\d+", "\"price\":").replaceAll("\\s+", ""));
			moneyList.add(m);
//			System.err.println("name : " +m.getPerson());
//			System.err.println("propleproperty : " +m.getTicket());
//			System.err.println("price : " +m.getMoney());		
		}
	}

	/**
	 * 获取景区图片链接地址
	 * <p>
	 * 需要传入一个div标签的景区文本
	 * <p>
	 * 
	 */
	public String getSenicTextImg(String scenicText) {
		return st.getByAllString(scenicText, "src=\"(.+?\")", "src=|\"");
	}

	/**
	 * 获取付费景区坐标、价格、地址、图文简介
	 * <p>
	 * Chromedriver谷歌浏览器驱动
	 * <p>
	 * link：当前网页链接
	 * <p>
	 * cityname：爬取当前城市名字
	 * <p>
	 * csiList：一个CtripScenicInfo对象list
	 * <p>
	 */
	@SuppressWarnings("static-access")
	public void getMXYAddrAndMoney(ChromeDriver cd, String link, String cityName, List<CtripScenicInfo> csiList) {
		ArrayList<CtripScenicMoney> moneyList = new ArrayList<CtripScenicMoney>();
		String districtId = st.getByString(link.split("/")[6], "\\d+", "").replaceAll("\\s+", "");
		String SightId = st.getByString(link.split("/")[7], "\\d+", "").replaceAll("\\s+", "");
		String data = hu.getPage(link);
		CtripScenicInfo csi = new CtripScenicInfo();
		for (String s : st.formatToJson(data.replaceAll("\\s+", ""))) {
			if (s.indexOf("spotname") != -1 && s.indexOf("address") != -1) {
//				System.out.println(s);
				getMMoney(data, moneyList);
				csi.setX(st.getByString(s, "\"lon(.+?,)", "lon|\"|:|,").replaceAll("\\s+", ""));
				csi.setY(st.getByString(s, "\"lat(.+?,)", "lat|\"|:|,").replaceAll("\\s+", ""));
				csi.setAddress(st.getByJson(s, "address"));
				csi.setDate(st.getByJson(s, "otdesc"));
				csi.setScenicName(st.getByJson(s, "spotname"));
				csi.setText(getMScenicText(cd, HEADERS.MHEADER, htmlEnd));
				csi.setPhone(getPhone(csi.getText()));
				csi.setImageLink(getImages(data.replaceAll("\\s+", "")));
				csi.setMoney(moneyList);
				csi.setDistrictId(districtId);
				csi.setSightId(SightId);
				csi.setCityName(cityName);
//				System.err.println("x : " + csi.getX());
//				System.err.println("y : " + csi.getY());
//				System.err.println("address : " + csi.getAddress());
//				System.err.println("otdesc : " + csi.getDate());
//				System.err.println("spotname : " + csi.getScenicName());
				break;
			}
		}
		csiList.add(csi);
	}

	/**
	 * 获取post请求页面里所有图片链接
	 * <p>
	 * ResponsePage：post请求后的页面
	 * <p>
	 */
	public String getImages(String ResponsePage) {
		StringBuilder sb = new StringBuilder();
		for (String img : st.getByString(st.getByString(ResponsePage, "poiPhotos(.+?\\],)", ""), "\\[(.+?\\])", "")
				.split(",")) {
			sb.append(img.replaceAll("\\[|\"|\\]", "") + "\n");
		}
		return sb.toString();
	}

	/**
	 * 获取免费景点信息：景区坐标、价格、地址、名称
	 * <p>
	 * ChromeDriver ： 谷歌浏览器驱动
	 * <p>
	 * link：当前网页链接
	 * <p>
	 * cityName：爬取城市名字
	 * <p>
	 * csiList：一个CtripScenicInfo对象list
	 * <p>
	 * 
	 */
	@SuppressWarnings("static-access")
	public void getGsXYAddrAndMoney(ChromeDriver cd, String link, String cityName, List<CtripScenicInfo> csiList) {

		ArrayList<CtripScenicMoney> moneyList = new ArrayList<CtripScenicMoney>();
		CtripScenicInfo csi = new CtripScenicInfo();
		String districtId = st.getByString(link.split("/")[6], "\\d+", "").replaceAll("\\s+", "");
		String SightId = st.getByString(link.split("/")[7], "\\d+", "").replaceAll("\\s+", "");
		sleep(3000);
		String guid = cd.manage().getCookieNamed("GUID").getValue();
		String param = "{\"SightId\":" + SightId + ",\"districtId\":" + districtId
				+ ",\"isSearchLandingPage\":false,\"fromChannel\":2,\"coverImageId\":null,\"fromCategoryId\":null,\"head\":{\"cid\":\""
				+ guid
				+ "\",\"ctok\":\"\",\"cver\":\"1.0\",\"lang\":\"01\",\"sid\":\"8888\",\"syscode\":\"09\",\"auth\":null,\"extension\":[{\"name\":\"protocal\",\"value\":\"https\"}]},\"contentType\":\"json\"}";
		String url = "https://m.ctrip.com/restapi/soa2/13342/json/getsightdetail?_fxpcqlniredt=" + guid;
//		System.out.println(guid);
		csi.setText(getGSScenicText(cd, HEADERS.GSHEADER, htmlEnd));
		sleep(2000);
		String page = hu.getPostResponse(url, param);
		for (String kk : st.getByAllString(page.replaceAll("\\s+", ""), "\\{(.+?\\})", "").split("\n")) {
			if (kk.indexOf("price") != -1 && kk.indexOf("\"id\"") != -1) {
				if (kk.indexOf("detailUrl") == -1) {
//					System.out.println(kk);
					String mp = st.getByJson(kk, "name").replaceAll("\\s+", "");
					String lx = st.getByString(st.getByJson(kk, "name"), "(儿童|学生|家庭|优待|亲子|\\S人)票|大小同价", "")
							.replaceAll("\\s+", "");
					String jg = st.getByString(kk, "\"price\":(.+?,)", "\"price\":|,").replaceAll("\\s+", "");
					lx = lx.equals("") ? "门票" : lx;
					CtripScenicMoney m = new CtripScenicMoney(mp, lx, jg);
					moneyList.add(m);
//					System.out.println("门票  :   " + mp);
//					System.out.println("门票类型: "+lx);
//					System.out.println("门票价格 :   " + jg);
//					System.out.println("################++++++++++++++======================");
				}
			}
		}

		csi.setX(st.getByString(page, "gGLat\":\\d+.\\d+", "gGLat\":").replaceAll("\\s+", ""));
		csi.setY(st.getByString(page, "gDLon\":\\d+.\\d+", "gDLon\":").replaceAll("\\s+", ""));
		csi.setAddress(st.getByJson(page, "address"));
		csi.setDate(st.getByJson(page, "openTimeDesc"));
		csi.setScenicName(st.getByJson(page, "sightName"));
		csi.setPhone(getPhone(csi.getText()));
		csi.setImageLink(getImages(page));
		csi.setMoney(moneyList);
		csi.setDistrictId(districtId);
		csi.setSightId(SightId);
		csi.setCityName(cityName);
		csiList.add(csi);
//		System.out.println("x ： "+csi.getX());
//		System.out.println("y : "+csi.getY());
//		System.out.println("address : " +csi.getAddress());
//		System.out.println("openTimeDesc : " +csi.getDate() );
//		System.out.println("sightName : " + csi.getScenicName());
	}

	/**
	 * 获取景区联系方式
	 * <p>
	 * scenicText：景区图文简介
	 * <p>
	 */
	public String getPhone(String scenicText) {
		return st.getByAllString(scenicText, "class=\"link-color\"(.+?</p)", "class=\"link-color\"|</p|>")
				.replaceAll("\\s+", "");
	}

}
