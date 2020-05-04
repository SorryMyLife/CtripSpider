package com.CtripSpider.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.CtripSpider.info.CtripHotelInfo;
import com.CtripSpider.other.ConfigureOptions;
import com.CtripSpider.other.CtripHotelDay;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.FileTool;
import com.ToolBox.util.HtmlTool;
import com.ToolBox.util.StringTool;

/**
 * Я�̾Ƶ�����
 * <p>
 * ��Ҫ����Я����վ�ھƵ���Ϣ�ɼ�����ϴ�����
 * 
 */

public class CtripHotelUtils {

	private static final String jdbc = "jdbc:mysql://127.0.0.1:3306/ctriphotel?serverTimezone=GMT";

//	private static final String jdbc = "jdbc:mysql://127.0.0.1:3307/abc?serverTimezone=GMT";

	private static Connection c = null;
	private static PreparedStatement ps = null;
	public static final FileTool ft = new FileTool();
	public static final HttpUtils hu = new HttpUtils();
	public static final StringTool st = new StringTool();
	protected static final ConfigureOptions configureOption = new ConfigureOptions();
	public static final String hs[] = {
			"accept: text/html,application/xhtml,xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
//			"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36"
			"user-agent: Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Mobile Safari/537.36"
//			"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1" 
	};
	private static String tmpData = null;

	/**
	 * ���캯��
	 * <p>
	 */
	public CtripHotelUtils() {
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
	 * ��ȡ����ʱ�䣬��λ����
	 * <p>
	 * max�����ʱ��
	 * <p>
	 * min����Сʱ��
	 */
	@SuppressWarnings("unused")
	private int getSleepTime(int max, int min) {
		return (new Random().nextInt(max - min + 1) + min) * 1000;
	}

	/**
	 * ���ߺ���
	 * <p>
	 * times : ���ߵ�ʱ��,��λ: ����
	 */

	@SuppressWarnings({ "unused", "static-access" })
	private void sleep(int times) {
		try {
			new Thread().sleep(times);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public String[] getRuleID(String page) {
		String ruleids[] = st.getByAllString(st.getByString(page, "\"RuleList\":(.+?],)", "\"RuleList\":|\\]"),
				"\"RuleId\":(.+?\\d+)", "\"RuleId\":").split("\n");
		return ruleids != null ? ruleids : null;
	}

	/**
	 * ��ӾƵ�۸�
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 * <p>
	 * day : �Ƶ�ҳ��Դ��
	 * <p>
	 * commit : ��Ҫ�����˵��
	 */
	private void addMinPrice(List<String> MinPriceList, String day, String commit) {
		MinPriceList.add(getMinPrice(day, commit));
//		String RuleId = st.getByJson(day, "RuleId").replaceAll("\\s+", "");
//		if(RuleId.equals(CtripHotelDay.TWO_DAY_ONE_NIGHT)) {
//			MinPriceList.add(getMinPrice(day, "����һҹ--"));
//		}else if(RuleId.equals(CtripHotelDay.THREE_DAY_TWO_NIGHT)) {
//			MinPriceList.add(getMinPrice(day, "������ҹ--"));
//		}else if(RuleId.equals(CtripHotelDay.FOUR_DAY_THREE_NIGHT)) {
//			MinPriceList.add(getMinPrice(day, "������ҹ--"));
//		}else if(RuleId.equals(CtripHotelDay.FIVE_DAY_FOUR_NIGHT)) {
//			MinPriceList.add(getMinPrice(day, "������ҹ--"));
//		}
	}

	/**
	 * ��ȡ�Ƶ�۸�
	 * <p>
	 * data : �Ƶ�ҳ��Դ��
	 * <p>
	 * commit : ��Ҫ�����˵��
	 */
	private String getMinPrice(String data, String commit) {
		return commit + st.getByJson(st.getByString(st.getByString(data, "window.__INITIAL_STATE__(.+?</script>)", ""),
				"\\{(.+?window.__APP)", "window.__APP"), "MinPrice") + "";
	}

	/**
	 * ���ػ�ȡ���ľƵ�۸�list����
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 */
	protected List<String> getMinPrice(String detailUrl) {
		String TWO_DAY_ONE_NIGHT_MINPRICE = hu
				.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid="
						+ st.getByString(detailUrl, "p\\d+", "p").replaceAll("\\s+", "") + "&ruleid="
						+ CtripHotelDay.TWO_DAY_ONE_NIGHT + "&frompc=1");
		if (getMinPrice(TWO_DAY_ONE_NIGHT_MINPRICE, "").length() > 0) {
			List<String> MinPriceList = new ArrayList<String>();
			for (String RuleID : getRuleID(TWO_DAY_ONE_NIGHT_MINPRICE)) {
				if (RuleID.equals(CtripHotelDay.TWO_DAY_ONE_NIGHT)) {
					addMinPrice(MinPriceList, TWO_DAY_ONE_NIGHT_MINPRICE, "����һҹ--");
				} else if (RuleID.equals(CtripHotelDay.THREE_DAY_TWO_NIGHT)) {
					String THREE_DAY_TWO_NIGHT_MINPRICE = hu
							.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid="
									+ st.getByString(detailUrl, "p\\d+", "p").replaceAll("\\s+", "") + "&ruleid="
									+ CtripHotelDay.THREE_DAY_TWO_NIGHT + "&frompc=1");
					addMinPrice(MinPriceList, THREE_DAY_TWO_NIGHT_MINPRICE, "������ҹ--");
				} else if (RuleID.equals(CtripHotelDay.FOUR_DAY_THREE_NIGHT)) {
					String FOUR_DAY_THREE_NIGHT_MINPRICE = hu
							.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid="
									+ st.getByString(detailUrl, "p\\d+", "p").replaceAll("\\s+", "") + "&ruleid="
									+ CtripHotelDay.FOUR_DAY_THREE_NIGHT + "&frompc=1");
					addMinPrice(MinPriceList, FOUR_DAY_THREE_NIGHT_MINPRICE, "������ҹ--");
				} else if (RuleID.equals(CtripHotelDay.FIVE_DAY_FOUR_NIGHT)) {
					String FIVE_DAY_FOUR_NIGHT_MINPRICE = hu
							.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid="
									+ st.getByString(detailUrl, "p\\d+", "p").replaceAll("\\s+", "") + "&ruleid="
									+ CtripHotelDay.FIVE_DAY_FOUR_NIGHT + "&frompc=1");
					addMinPrice(MinPriceList, FIVE_DAY_FOUR_NIGHT_MINPRICE, "������ҹ--");
				}
			}
			return MinPriceList;
		} else {
			System.out.println("skip :::: " + detailUrl);
		}
		return null;
	}

	/**
	 * �Ƴ���ͬ�۸�
	 * <p>
	 * MinPriceList : �Ƶ�۸�List
	 */
	@SuppressWarnings("unused")
	private void remove(List<String> MinPriceList) {
		List<String> Min = new ArrayList<String>();
		for (int i = 0; i < MinPriceList.size(); i++) {
			String tmp = MinPriceList.get(0).split("--")[1];
			if (i != 0) {
				if (!MinPriceList.get(i).split("--")[1].equals(tmp)) {
					Min.add(MinPriceList.get(i));
				}
			} else {
				Min.add(MinPriceList.get(0));
			}
		}
		MinPriceList.clear();
		MinPriceList.addAll(Min);
	}

	/**
	 * ��ȡ�Ƶ�ҳ��ID
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 */
	private String getProductid(String detailUrl) {
		return st.getByString(detailUrl, "p\\d+", "p").replaceAll("\\s+", "");
	}

	/**
	 * ��ȡ�Ƶ�ҳ��Դ��
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 */
	private String getPage(String detailUrl) {
		String page = hu.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid="
				+ getProductid(detailUrl) + "&ruleid=" + CtripHotelDay.TWO_DAY_ONE_NIGHT + "&frompc=1");
		return page;
	}

	/**
	 * ��ȡ�Ƶ�ҳ��Ϊjson
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 */
	private String getJsonData(String detailUrl , int id) {
		if(id == 1) {
			return st.getByString(st.getByString(getPage(detailUrl), "window.__INITIAL_STATE__(.+?</script>)", ""),
					"\\{(.+?window.__APP)", "window.__APP");
		}
		return st.getByString(st.getByString(hu.getResponse(detailUrl), "window.__INITIAL_STATE__(.+?</script>)", ""),
				"\\{(.+?window.__APP)", "window.__APP");
	}

	/**
	 * ��ȡ�Ƶ���Ϣ
	 * <p>
	 * detailUrl : �Ƶ����ת����
	 * <p>
	 * CtripHotelInfoList : ����CtripHotelInfo���͵�Listʵ��
	 */
	private void getHotelInfo(String detailUrl, ArrayList<CtripHotelInfo> CtripHotelInfoList) {
		CtripHotelInfo ctripHotelInfo = new CtripHotelInfo();
		String json = getJsonData((detailUrl.indexOf("http") != -1  || detailUrl.indexOf("https") != -1 )? detailUrl : "https:"+detailUrl , 1);
		System.out.println("= 245 == json : "+json);
//		System.out.println("detailUrl : "+detailUrl);
		if (json.indexOf("ErrorMessage\":\"�ò�Ʒ��Ч\"}") != -1) {
			System.out.println("= 248 == ��Ʒ��Ч");
			json = getJsonData((detailUrl.indexOf("http") != -1  || detailUrl.indexOf("https") != -1 )? detailUrl : "https:"+detailUrl , 2);
		} 
		
		if (!getProductid(detailUrl).equals("") && (json.indexOf("Title") != -1 || json.indexOf("MainName") != -1
				|| json.indexOf("DestCityId") != -1 || json.indexOf("Introduction") != -1)) {
			String basicInfo = st.getByString(json, "BasicInfo(.+?\\}\\],)", "");
			String hotelInfo = st.getByString(json, "HotelInfoList(.+?\\],)", "");
			System.out.println("= 260 == hotelinfo "+hotelInfo);
			ctripHotelInfo.setProductId(st.getByJson(json, "ProductId"));
			ctripHotelInfo.setTitle(st.getByJson(basicInfo, "Title"));
			ctripHotelInfo.setMainName(st.getByJson(basicInfo, "MainName"));
			ctripHotelInfo.setDepartureCityName(st.getByJson(basicInfo, "DepartureCityName"));
			ctripHotelInfo.setDestCityId(st.getByJson(basicInfo, "DestCityId"));
			ctripHotelInfo.setDestProvinceId(st.getByJson(basicInfo, "DestProvinceId"));
			ctripHotelInfo.setDestProvinceName(st.getByJson(basicInfo, "DestProvinceName"));
			ctripHotelInfo.setDestinationCountryId(st.getByJson(basicInfo, "DestinationCountryId"));
			ctripHotelInfo.setDestinationCountryName(st.getByJson(basicInfo, "DestinationCountryName"));
			ctripHotelInfo.setHotelPhone(st.getByJson(json, "VendorPhone"));
			ctripHotelInfo.setHotelId(st.getByJson(hotelInfo, "HotelId").replaceAll("\\s+", ""));
//			System.out.println(ctripHotelInfo.getHotelId());
			String introduction = st.getByString(json, "\"Introduction\":\"(.+?\",)",
					"\"Introduction\":\"|\",|\\\\");
			if (introduction == null || introduction.length() < 10 || introduction.equals("")
					|| introduction.indexOf("null") != -1 || introduction.indexOf("IsBindTravelInfo") != -1
					|| introduction.indexOf("CostInfoList") != -1 || ctripHotelInfo.getHotelPhone().equals("null")
					|| ctripHotelInfo.getHotelPhone().equals("")) {
//				System.out.println(ctripHotelInfo.getHotelId() + " : ������������" + detailUrl);
				if (ctripHotelInfo.getHotelId().length() > 1 || !ctripHotelInfo.getHotelId().isEmpty()) {
					String ttt = getTarGet(ctripHotelInfo.getHotelId());
//					System.out.println(ttt);
					ctripHotelInfo.setIntroduction(ttt.split("----")[1] + "");
					ctripHotelInfo.setHotelPhone(ttt.split("----")[0].isEmpty() ? "0" : ttt.split("----")[0]);
				}
			} else {
				ctripHotelInfo.setIntroduction(introduction);
			}
			ctripHotelInfo.setHotelName(st.getByJson(hotelInfo, "HotelName"));
			ctripHotelInfo.setHotelAddress(st.getByJson(hotelInfo, "HotelAddress"));
			ctripHotelInfo.setLatitude(st.getByJson(json, "Latitude"));
			ctripHotelInfo.setLongitude(st.getByJson(json, "Longitude"));
			if (ctripHotelInfo.getHotelName().isEmpty() || ctripHotelInfo.getHotelAddress().isEmpty()) {
				ctripHotelInfo.setHotelName(st.getByJson(json, "ScenicSpotName"));
				ctripHotelInfo.setHotelAddress(st.getByJson(json, "ScenicSpotAddress"));
			}
			ctripHotelInfo.setMinPrice(getMinPrice(detailUrl));
			ctripHotelInfo.setMinPriceRemark(st.getByJson(json, "MinPriceRemark"));
			ctripHotelInfo.setHotelImages(st.getByAllString(json, "\"Value\":\"(.+?\")", "\"Value\":\"|\""));
			if(ctripHotelInfo.getIntroduction() == null)
			{
				StringBuilder sb = new StringBuilder();
				for(String img : ctripHotelInfo.getHotelImages().split("\n")) {
					sb.append("<p>"+img+"</p>");
				}
				ctripHotelInfo.setIntroduction(sb.toString());
			}
//			System.out.println(detailUrl+" -- "+ctripHotelInfo.getIntroduction());
			CtripHotelInfoList.add(ctripHotelInfo);
//			sleep(getSleepTime(3, 1));
		}
		
		
	}

	private boolean exists(String hotelname) {
		try {
			ps = c.prepareStatement(" select 1 from hotels where hotelname='" + hotelname + "' limit 1");
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1) == 1;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��������ݿ���
	 * <p>
	 * sqldata : ������ַ�����̬����
	 */
	private void insert(String... sqldata) {
		try {
			if (sqldata.length == 18) {
				String sql = "insert into hotels(" + "productid," + "DestCityId," + "DestProvinceId,"
						+ "DestinationCountryId," + "HotelId," + "Title," + "MainName," + "DepartureCityName,"
						+ "DestProvinceName," + "DestinationCountryName," + "HotelPhone," + "HotelName,"
						+ "HotelAddress," + "LatLon," + "MinPrice," + "MinPriceRemark," + "introduction,"
						+ "hotelImages" + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps = c.prepareStatement(sql);
				for (int i = 0; i < sqldata.length; i++) {
					if (i < 5) {
						String data = sqldata[i];
						if (data == null) { // ������������ַ�������������գ��Ǿ�Ĭ��Ϊ0������ͻ����ʧ��
							data = "0";
						}
						data = data.replaceAll("\\s+", "") + "";
						if (data.isEmpty()) {
							data = "0";
						}
						int ii = Integer.parseInt(data);
						ps.setInt((i + 1), ii);
					} else {
						ps.setString((i + 1), sqldata[i]);
					}
				}
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			System.exit(-1);
		}
	}

	/**
	 * �������
	 * <p>
	 * search_name : �Ƶ���߾�������
	 * <p>
	 * pageNum : �Ƶ���߾���������λ��
	 */
	public String getLink(String search_name, int pageNum) {
		return new String("https://vacations.ctrip.com/list/scenichotel/sc.html/?sv=" + st.urlencode(search_name)
				+ "&p=" + pageNum).replaceAll("\\s+", "");
	}

	/**
	 * ��ȡ��ȡ����λ��
	 * <p>
	 * search_name : �Ƶ���߾�������
	 */
	protected int getNowPageNum(String search_name) {
		String now = ft.readFile(configureOption.getCityLinkPath() + "/Hotel-" + search_name);
		now = now.substring(now.lastIndexOf("&p=") + 3).replaceAll("\\s+", "");
		return Integer.parseInt(now);
	}

	/**
	 * ��ȡ�Ƶ����ҳ��
	 * <p>
	 * data : �Ƶ�ҳ��
	 */
	protected int getByMaxPageNum(String data) {
		data = st.getByString(data, "class=\"list_paging_text\"(.+?</span>)", "");
		data = data.substring(data.lastIndexOf("-->"));
		data = data.substring(data.indexOf(">") + 1, data.lastIndexOf("<"));
		return Integer.parseInt(data.replaceAll("\\s+", ""));
	}

	/**
	 * ��ȡ�Ƶ���Ϣ
	 * <p>
	 * search_name : �Ƶ���߾�������
	 * <p>
	 * pageNum : �Ƶ���߾���������λ��
	 */
	public boolean getHotelInfo(String search_name, String page) {
		if (!page.equals(tmpData) || tmpData == null) {
			if (!st.getByAllString(page, "detailUrl(.+?\",)", "detailUrl\":\"|\",").isEmpty()) {
//				String targetUrl = "";
				
				ArrayList<CtripHotelInfo> CtripHotelInfoList = new ArrayList<CtripHotelInfo>();
				for (String detailUrl : st.getByAllString(page, "detailUrl(.+?\",)", "detailUrl\":\"|\",")
						.split("\n")) {
					System.out.println("= 412 ====  "+detailUrl);
					getHotelInfo(detailUrl, CtripHotelInfoList);
				}
				if (CtripHotelInfoList.size() > 1) {
					for (CtripHotelInfo ctripHotelInfo : CtripHotelInfoList) {
						
//						if (!oss.exist("hImages")) {
//							oss.CreateFolder("hImages");
//						}
						if (!exists(ctripHotelInfo.getHotelName())) {
							StringBuilder sb = new StringBuilder();
							String images[] = ctripHotelInfo.getHotelImages().split("\n");
							int imageLen = images.length;
							int maxImageNum = 8, stopNum = -1;
							if (maxImageNum == imageLen || maxImageNum > imageLen) {
								stopNum = imageLen;
							} else if (maxImageNum < imageLen) {
								stopNum = maxImageNum;
							}
							for (int i = 0; i < imageLen; i++) {
								if (i <= stopNum) {
									try {
										String fileName = hu.getFileName(images[i]);
										String dd = new Date().toString().replaceAll(" ", "").replaceAll(":", "");
										System.out.println(fileName + " -- " +new URL(images[i]).openConnection().getInputStream() + " -- " +
												"hImages/" + dd);
//										oss.Upload(fileName, new URL(images[i]).openConnection().getInputStream(),
//												"hImages/" + dd);
										sb.append(images[i]);
									} catch (MalformedURLException e) {

										e.printStackTrace();
									} catch (IOException e) {

										e.printStackTrace();
									}
								}
							}
							ctripHotelInfo.setHotelImages(sb.toString());
							String inc = ctripHotelInfo.getIntroduction();
//							System.err.println(ctripHotelInfo.getHotelName()+" --- " + ctripHotelInfo.getHotelId());
							for (String img : new HtmlTool(inc).getByElementValueAll("src").toString().split("\n")) {
								try {
									String fileName = hu.getFileName(img);
									String dd = new Date().toString().replaceAll(" ", "").replaceAll(":", "");
									System.out.println(fileName + " -- " + new URL(img).openConnection().getInputStream() + " -- " +
											"hImages/" + dd);
//									oss.Upload(fileName, new URL(img).openConnection().getInputStream(),
//											"hImages/" + dd);
//									inc = inc.replaceAll(img, targetUrl + "/" + dd + "/" + fileName);
//									targetUrl+"/"+dd+"/"+fileName
								} catch (MalformedURLException e) {

									e.printStackTrace();
								} catch (IOException e) {

									e.printStackTrace();
								}

							}
							ctripHotelInfo.setIntroduction(inc);
//							oss.shutdown();
							insert(ctripHotelInfo.getProductId(), ctripHotelInfo.getDestCityId(),
									ctripHotelInfo.getDestProvinceId(), ctripHotelInfo.getDestinationCountryId(),
									ctripHotelInfo.getHotelId(), ctripHotelInfo.getTitle(),
									ctripHotelInfo.getMainName(), ctripHotelInfo.getDepartureCityName(),
									ctripHotelInfo.getDestProvinceName(), ctripHotelInfo.getDestinationCountryName(),
									ctripHotelInfo.getHotelPhone(), ctripHotelInfo.getHotelName(),
									ctripHotelInfo.getHotelAddress(),
									ctripHotelInfo.getLatitude() + "," + ctripHotelInfo.getLongitude(),
									ctripHotelInfo.getMinPrice() == null ? "0"
											: Arrays.toString(ctripHotelInfo.getMinPrice().toArray()),
									ctripHotelInfo.getMinPriceRemark(), ctripHotelInfo.getIntroduction(),
									ctripHotelInfo.getHotelImages());
						} else {
							System.out.println("���� : [ " + ctripHotelInfo.getHotelName() + " ]");
						}
//						System.out.println("��ƷID: " + ctripHotelInfo.getProductId());
//						System.out.println("��ǩȫ��: " + ctripHotelInfo.getTitle());
//						System.out.println("��ǩ����: " + ctripHotelInfo.getMainName());
//						System.out.println("�Ƶ���������: " + ctripHotelInfo.getDepartureCityName());
//						System.out.println("�Ƶ���������ID: " + ctripHotelInfo.getDestCityId());
//						System.out.println("�Ƶ�����ʡID: " + ctripHotelInfo.getDestProvinceId());
//						System.out.println("�Ƶ�����ʡ: " + ctripHotelInfo.getDestProvinceName());
//						System.out.println("�Ƶ���������ID: " + ctripHotelInfo.getDestinationCountryId());
//						System.out.println("�Ƶ���������: " + ctripHotelInfo.getDestinationCountryName());
//						System.out.println("�Ƶ���תID: " + ctripHotelInfo.getHotelId());
//						System.out.println("�Ƶ���ϵ��ʽ: " + ctripHotelInfo.getHotelPhone());
//						System.out.println("�Ƶ�����: " + ctripHotelInfo.getHotelName());
//						System.out.println("�Ƶ��ַ: " + ctripHotelInfo.getHotelAddress());
//						System.out.println("�Ƶ꾭γ��: " + ctripHotelInfo.getLatitude() + " [Latitude]");
//						System.out.println("�Ƶ꾭γ��: " + ctripHotelInfo.getLongitude() + " [Longitude]");
//						System.out.println("�Ƶ�۸�: " + ctripHotelInfo.getMinPrice());
//						System.out.println("�۸�˵��: " + ctripHotelInfo.getMinPriceRemark());
//						System.out.println("�Ƶ���: " + ctripHotelInfo.getIntroduction());
//						System.out.println("�Ƶ�ͼƬ: " + ctripHotelInfo.getHotelImages());
//						System.out.println("############################################################################################################################");
					}

					System.out.println("��ǰҳ���ȡ [ " + CtripHotelInfoList.size() + " ] ������");
					return true;
				} else {
					System.out.println("���� : [ " + search_name + " ]");
				}
				tmpData = page;
			} else {
				System.out.println("��ȡ��� [ " + search_name + " ]");
			}
		} else {
			System.err.println("��� [ " + search_name + " ]");
			return false;
		}
		return false;
	}

	/**
	 * ��ȡguid
	 * <p>
	 * link : Я����������
	 */
	private String getGUID(String link) {
		System.setProperty("webdriver.chrome.driver", configureOption.getDriverPath());
		System.setProperty("webdriver.chrome.silentOutput", "true");
//		Ϊ�˼���webdrive��Դռ���Լ��ر�һЩ����Ҫ��ѡ��
		ChromeOptions co = new ChromeOptions();
		co.addArguments("disable-gpu", "disable-impl-side-painting", "disable-gpu-sandbox",
				"disable-accelerated-2d-canvas", "no-sandbox", "test-type=ui", "disable-dev-shm-usage", "headless");
		ChromeDriver cd = new ChromeDriver(co);
		cd.get(link);
		String guid = cd.manage().getCookieNamed("GUID").getValue();
		cd.quit();
		System.out.println(guid);
		return guid;
	}

	/**
	 * ��ȡ�Ƶ���Ϣ
	 * <p>
	 * id : �Ƶ�ΨһID
	 */
	private String getTarGet(String id) {
		String targetLink = "https://m.ctrip.com/webapp/hotel/hoteldetail/more/" + id + ".html";
//		System.out.println("link : " +targetLink);
		String cookie = ft.readFile("conf/cookies");
		String h[] = {
				"accept: text/html,application/xhtml,xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
				"accept-language: zh-CN,zh;q=0.9", "cache-control: max-age=0",
				"cookie: supportwebp=true; GUID= " + cookie + ";", "upgrade-insecure-requests: 1",
				"user-agent: Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Mobile Safari/537.36" };
		hu.setHeaders(h);
		String data = st.getByString(hu.getResponse(targetLink), "HOTEL_PAGE_DATA(.+?\\};)", "");
//		System.out.println(data);
		while (data.equals("") || data == null) {
			cookie = "cookie: supportwebp=true; GUID=" + getGUID(targetLink) + ";";
			ft.writeFile(cookie, "conf/cookies");
			String hh[] = {
					"accept: text/html,application/xhtml,xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
					"accept-language: zh-CN,zh;q=0.9", "cache-control: max-age=0",
					"cookie: " + "supportwebp=true; " + "GUID= " + getGUID(targetLink) + ";",
					"upgrade-insecure-requests: 1",
					"user-agent: Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Mobile Safari/537.36" };
			hu.setHeaders(hh);
			data = st.getByString(hu.getResponse(targetLink), "HOTEL_PAGE_DATA(.+?\\};)", "");
		}
		String phone = st.getByString(data, "phoneList(.+?\\])", "phoneList\":|\"|\\]|\\[").replaceAll("\\s+", "");
		String info = st.getByString(data, "sellerShow(.+?\",)", "sellerShow\":\"|\",|\\\\");
		return (phone.isEmpty() ? "0" : phone) + "----" + (info.isEmpty() ? "null" : info);
	}

//	@SuppressWarnings("unused")
//	private void test() {
////		System.out.println(getTarGet("4498692"));
////		System.out.println(getJsonData("//vacations.ctrip.com/tour/detail/p20872395s376.html").indexOf("Title"));
//
//		System.out.println(getByMaxPageNum(hu.getResponse(getLink("�ϲ�", 1))));
//
//	}

}
