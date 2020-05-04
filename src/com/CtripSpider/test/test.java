package com.CtripSpider.test;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import com.CtripSpider.Main;

public class test extends Main{
	
	public static void sleep(int time) {
		try {
		new Thread().sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 用于图片等比例缩放
	 * srcImageFilePath：需要被缩放的图片路径
	 * ImageOutPath：缩放后的图片保存路径
	 * */
	public static void ImageScale(String srcImageFilePath , String ImageOutPath) throws IOException {
		BufferedImage image = ImageIO.read(new File(srcImageFilePath));
		int h = image.getHeight() , w = image.getWidth();
		int scaleA=4 , scaleB =2;
		if(h > 800 && w > 1200) {
			BufferedImage tmpImage = new BufferedImage(w/scaleA, h/scaleA, BufferedImage.TYPE_INT_RGB);
			Graphics2D grap = tmpImage.createGraphics();
			grap.scale(0.5, 0.5);
			grap.drawImage(image, 0, 0, w/scaleB, h/scaleB, null);
			grap.dispose();
			ImageIO.write(tmpImage, "jpg", new File(ImageOutPath));
		}
	}
	
	public static String getRGB(int px) {
		int r = (px & 0xff0000) >> 16 , g = (px & 0xff00) >> 8, b = px & 0xff;
		return r+"-"+g+"-"+b;
	}
	
	public static boolean ImageGrayRGBCheck(String ImagePath) throws IOException {
		String rgb = "128-128-128" , rgb1 = null , rgb2 = null;
		BufferedImage image = ImageIO.read(new File(ImagePath));
	    int h = image.getHeight() , w = image.getWidth();
	    int px = image.getRGB(15,h-15);
	    int px1 = image.getRGB(w-15, h-15);
	    rgb1 = getRGB(px);rgb2=getRGB(px1);
	    if(rgb1.equals(rgb2)) {
	    	if(rgb1.equals(rgb)) {
	    		return true;
	    	}
	    }
	    return false;
	}
	
	/***
	 * 给图片加上水印，右下角，jpg图片格式
	 * <p>
	 * image1为需要被加上水印的图片
	 * <p>
	 * image2为水印图片
	 * <p>
	 * targetImage为输出路径
	 * <p>
	 * fileName为上传文件名称
	 * <p>
	 * folderName为上传路径
	 * */
	public void ImageAddOnOSS(String image1, String image2,String fileName, String folderName) throws Exception {
//		OSSUtils oss = new OSSUtils();
//		if(!oss.exist(folderName)) {
//			oss.CreateFolder(folderName);
//			oss.shutdown();
//			oss = new OSSUtils();
//		}
//		System.out.println(oss.exist(folderName));
		BufferedImage big = ImageIO.read(new File(image1));
		BufferedImage tmpbig =null,tmpSmall=null;
		BufferedImage small = ImageIO.read(new File(image2));
		File tmpFile = new File("tmp.jpg");
		Graphics2D g2=null,image2Grap=null;
		Integer w=big.getWidth(),h=big.getHeight(),x=null,y=null;
		if(big.getHeight() > 2700 || big.getWidth() > 2700) {
			tmpbig=new BufferedImage(w/8, h/8, BufferedImage.TYPE_INT_RGB);
			tmpSmall=small;
			g2=tmpbig.createGraphics();
			g2.scale(0.5, 0.5);
			g2.drawImage(big, 0,0,w/4, h/4,null);
			if(tmpbig.getHeight() < 500 || tmpbig.getWidth()<500) {
				tmpSmall=new BufferedImage(small.getWidth()/2, small.getHeight()/2, BufferedImage.TYPE_INT_RGB);
				image2Grap=tmpSmall.createGraphics();
				tmpSmall=image2Grap.getDeviceConfiguration().createCompatibleImage(small.getWidth()/2, small.getHeight()/2,Transparency.TRANSLUCENT);
				image2Grap=tmpSmall.createGraphics();
				image2Grap.scale(0.5, 0.5);
				image2Grap.drawImage(small,0,0,small.getWidth()/2,small.getHeight()/2,null);
				image2Grap.dispose();
				g2=tmpbig.createGraphics();
				x = tmpbig.getWidth()-(tmpSmall.getWidth()-(tmpSmall.getWidth()/4));
				y = tmpbig.getHeight()-tmpSmall.getHeight();
			}else {
				x = tmpbig.getWidth()+(tmpbig.getWidth()-(tmpSmall.getWidth()+(tmpSmall.getWidth()/2)));
				y = tmpbig.getHeight()+(tmpbig.getHeight()-(tmpSmall.getHeight()+(tmpSmall.getHeight()/2)));
			}
			
		}else if(big.getHeight() > 1700 || big.getWidth() > 1700) {
			tmpSmall=small;
			tmpbig=new BufferedImage(big.getWidth()/4, big.getHeight()/4, BufferedImage.TYPE_INT_RGB);
			g2=tmpbig.createGraphics();
			g2.scale(0.5, 0.5);
			g2.drawImage(big, 0,0,w/2, h/2,null);
			if(tmpbig.getHeight() < 500 || tmpbig.getWidth()<500) {
				tmpSmall=new BufferedImage(small.getWidth()/2, small.getHeight()/2, BufferedImage.TYPE_INT_RGB);
				image2Grap=tmpSmall.createGraphics();
				tmpSmall=image2Grap.getDeviceConfiguration().createCompatibleImage(small.getWidth()/2, small.getHeight()/2,Transparency.TRANSLUCENT);
				image2Grap=tmpSmall.createGraphics();
				image2Grap.scale(0.5, 0.5);
				image2Grap.drawImage(small,0,0,small.getWidth()/2,small.getHeight()/2,null);
				image2Grap.dispose();
				g2=tmpbig.createGraphics();
				x = tmpbig.getWidth()-(tmpSmall.getWidth()-(tmpSmall.getWidth()/4));
				y = tmpbig.getHeight()-tmpSmall.getHeight();
			}else {
				x = tmpbig.getWidth()+(tmpbig.getWidth()-tmpSmall.getWidth()-20);
				y = tmpbig.getHeight()+(tmpbig.getHeight()-tmpSmall.getHeight()-20);
			}
		}else if(big.getHeight() < 500 || big.getWidth()<500){
			tmpbig=big;
			tmpSmall=new BufferedImage(small.getWidth()/2, small.getHeight()/2, BufferedImage.TYPE_INT_RGB);
			image2Grap=tmpSmall.createGraphics();
			tmpSmall=image2Grap.getDeviceConfiguration().createCompatibleImage(small.getWidth()/2, small.getHeight()/2,Transparency.TRANSLUCENT);
			image2Grap=tmpSmall.createGraphics();
			image2Grap.scale(0.5, 0.5);
			image2Grap.drawImage(small,0,0,small.getWidth()/2,small.getHeight()/2,null);
			image2Grap.dispose();
			g2=tmpbig.createGraphics();
			x = tmpbig.getWidth()-(tmpSmall.getWidth()-(tmpSmall.getWidth()/4));
			y = tmpbig.getHeight()-tmpSmall.getHeight();
		}else {
			tmpbig=big;
			tmpSmall=small;
			g2=tmpbig.createGraphics();
			x = tmpbig.getWidth()-(tmpbig.getWidth()/4<tmpSmall.getWidth()?(tmpbig.getWidth()/2)-(tmpSmall.getWidth()/2):tmpbig.getWidth()/4);
			y = tmpbig.getHeight()-(tmpSmall.getHeight()+(tmpSmall.getHeight()/2));
		}
		g2.drawImage(tmpSmall, x, y, tmpSmall.getWidth(), tmpSmall.getHeight(), null);
		g2.dispose();
		ImageIO.write(tmpbig, "jpg",tmpFile );
//		oss.Upload(fileName, tmpFile.toString(), folderName);
//		tmpFile.delete();
	}
	
	public static boolean isZipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
	
	public static void main(String[] args) throws Exception {
		
		
		
		String text = "温州市文成县百丈祭风景名胜区西部";
		String head = "";
		
		String text2= "三亚市亚龙湾区湾路665号亚龙湾国家旅游度假区";
		String text3 = "三亚市亚龙湾国家旅游度假区";
		
		String hello = "浙江省温州市洞头区东屏镇东岙顶村仙叠岩景县内";
//		System.out.println(hello.substring(0,hello.indexOf("洞头县")).length());
		
		String str = "呼伦贝尔市莫尔道嘎镇11公里处";
		String tt = "厦门市同安区五显镇北辰山景区";
		
		String ss = "中国福建省厦门思明区大学路";
		
		//修复大图水印无法正确位置

//		String stsss = "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";
//		byte buff[] = Base64.getDecoder().decode(stsss);
//		if(isZipped(buff)) {
//			System.out.println("yes");
//		}else {
//			System.out.println("|no");
//		}
//		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
//		FileOutputStream fos = new FileOutputStream(new File("fasdfsd"));
//		DataInputStream dis = new DataInputStream(bais);
//		int len = -1;
//		byte buf[] = new byte[255];
//		while((len = dis.read(buf)) != -1) {
//			fos.write(buf, 0, len);
//		}
//		fos.close();
//		dis.close();
//		new test().ImageAddOnOSS("d:\\test\\\\yyysdfjx.jpg", "d:\\test\\\\shuiy.png","", "");
		
		
		
		System.out.println("hello");
		
//		new test().ImageAddOnOSS("D:\\eclipse-workspace\\CtripSpider\\tmp.jpg", "d:\\test\\\\shuiy.png","", "");
//		new test().ImageAddOnOSS("D:\\eclipse-workspace\\CtripSpider\\tmp.jpg", "d:\\test\\\\shuiy.png","", "");
//		
//		new test().ImageAddOnOSS("D:\\eclipse-workspace\\CtripSpider\\tmp.jpg", "d:\\test\\\\shuiy.png","", "");
//		
		
		
		

		
//		HttpUtils hu = new HttpUtils();
//		String response = hu.getResponse("http://www.mca.gov.cn/article/sj/xzqh/1980/201903/201903011447.html");
//		String cityA = null , cityB  = null , cityC = null ;
//		StringBuilder sb = new StringBuilder();
//		for(String tr : new HtmlTool(response).getByElement("tr").toString().split("\n")) {
//			if(tr.indexOf("mso-height-source:userset;height:14.25pt") != -1) {
//				String td[] = new HtmlTool(tr).getByElement("td").toString().split("\n");
//				String id=st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "");
//				String cityName = st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("/span", "").replaceAll("\\?", "").replaceAll("\\s+", "");
//				
//				if(td[2].indexOf("省") != -1) {
////					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
////							+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
////					System.out.println(id + " --- " + cityName);
//					cityA = id+"---"+cityName;
//				}else if(td[2].indexOf("市") != -1) {
////					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
////					+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
//					cityB = id+"---"+cityName;
////					sb.append(cityB+"\n");
////					System.out.println(cityB);
////					System.out.println(id + " --- " + cityName);
//				}else {
////					System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
////							+st.getByString(td[2], ">\\S+<", ">|<|>\\?/").replaceAll("\\s+", ""));
////					System.out.println(id + " --- " + cityName);
//					cityC = id+"---"+cityName;
//					System.out.println("省: "+cityA+",市: "+cityB+",县: "+cityC);
////					sb.append("省: "+cityA+",市: "+cityB+",县: "+cityC+"\n");
//				}
//				
//			}
////			System.out.println(st.getByString(td[1], ">\\d+<", ">|<").replaceAll("\\s+", "")+" --- "
////								+st.getByString(td[2], ">\\S+<", ">|<").replaceAll("\\s+", ""));
//		}
		
//		ft.writeFile(sb.toString(), "d:\\test\\\\hahasdfafgasa1.txt");
		
		
//		System.setProperty("webdriver.chrome.driver", "D:\\\\jdk\\\\chromedriver_win32\\\\chromedriver.exe");
//		System.setProperty("webdriver.chrome.silentOutput", "true");
////		为了减少webdrive资源占用以及关闭一些不必要的选项
//		ChromeOptions co = new ChromeOptions();
//		co.addArguments("disable-gpu", "disable-impl-side-painting", "disable-gpu-sandbox",
//				"disable-accelerated-2d-canvas", "no-sandbox", "test-type=ui", "disable-dev-shm-usage");
//		ChromeDriver cd = new ChromeDriver(co);
//		cd.get("https://vacations.ctrip.com/list/scenichotel/sc.html/?sv=%E6%9D%AD%E5%B7%9E&p=1");
//		ft.writeFile(cd.getPageSource(), "D:\\abc\\aaa.html");
//		cd.quit();
		
//		OSSUtils oss = new OSSUtils();
////		oss.CreateFolder("hImages");
////		oss.deleteFolder("hImages");
//		oss.printFiles("hImages");
////		for(int i =0 ;i<20;i++) {
////			OSSUtils oss = new OSSUtils();
//////			oss.CreateFolder("hImages");
////			oss.deleteFolder("hImages");
////			oss.printFiles("hImages");
////		}
		
		
//		String data = hu.getResponse("https://m.ctrip.com/webapp/vacations/diysh/detail?productid=20689199&ruleid=2001&frompc=1");
//		String json = st.getByString(st.getByString(data, "window.__INITIAL_STATE__(.+?</script>)", ""), "\\{(.+?window.__APP)", "window.__APP");
//		System.out.println("产品ID: "+st.getByJson(json, "ProductId"));
//		String basicInfo = st.getByString(json, "BasicInfo(.+?\\}\\],)", "");
//		String hotelInfo = st.getByString(json, "HotelInfoList(.+?\\],)", "");
//		System.out.println("标签全名: "+st.getByJson(basicInfo, "Title"));
//		System.out.println("标签名称: "+st.getByJson(basicInfo, "MainName"));
//		System.out.println("酒店所属城市: " + st.getByJson(basicInfo, "DepartureCityName"));
//		System.out.println("酒店所属城市ID: " + st.getByJson(basicInfo, "DestCityId"));
//		System.out.println("酒店所属省ID: " + st.getByJson(basicInfo, "DestProvinceId"));
//		System.out.println("酒店所属省: " + st.getByJson(basicInfo, "DestProvinceName"));
//		System.out.println("酒店所属国家ID: " + st.getByJson(basicInfo, "DestinationCountryId"));
//		System.out.println("酒店所属国家: " + st.getByJson(basicInfo, "DestinationCountryName"));
//		System.out.println("酒店名称: "+st.getByJson(hotelInfo, "HotelName"));
//		System.out.println("酒店地址: "+st.getByJson(hotelInfo, "HotelAddress"));
//		System.out.println("酒店经纬度: " + st.getByJson(hotelInfo, "Latitude")+" [Latitude]");
//		System.out.println("酒店经纬度: " + st.getByJson(hotelInfo, "Longitude")+" [Longitude]");
//		System.out.println("酒店价格: " + st.getByJson(json, "MinPrice"));
//		System.out.println("价格说明: " + st.getByJson(json, "MinPriceRemark"));
//		System.out.println("酒店简介: " + st.getByString(json, "Introduction(.+?\",)", "Introduction\":\"|\",|\\\\"));
//		System.out.println("酒店图片: "+st.getByAllString(json, "\"Value\":\"(.+?\")", "\"Value\":\"|\""));
		
		
		
		
		
//		String data = ft.readFile("D:\\abc\\aaa.html");
//		for(String s : st.getByAllString(st.getByString(data, "products(.+?window.__APP_SETTINGS)", ""), "\"proName(.+?supportTelBooking)", "").split("\n")) {
////			System.out.println(s);
//			System.out.println("酒店宣传标签: "+st.getByJson(s, "proName"));
//			System.out.println("酒店预览图: "+st.getByJson(s, "image"));
//			System.out.println("酒店ID:" +st.getByJson(s.substring(s.indexOf("hotelID")), "value"));
//			System.out.println("酒店跳转链接: "+"https:"+st.getByJson(s, "detailUrl"));
//			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//			
//		}
		
		
		
//		Long lonnum = 000001L;
//		MySqlTools mt = new MySqlTools();
//		AtomicLong at = new AtomicLong(mt.getLastSceneryNo());
//        for (int i = 0; i < 10; i++)
//        {
//            System.out.println(String.format("%06d", at.incrementAndGet()));
//           
//        }	
      
		
//		BufferedImage image = ImageIO.read(new File("D:\\abc\\html\\images\\120418\\243\\120418-243-0.jpg"));
//		int h = image.getHeight() , w = image.getWidth();
//		int scaleA=4 , scaleB =2;
//		if(h > 1000 && w > 1300) {
//			BufferedImage tmpImage = new BufferedImage(w/scaleA, h/scaleA, BufferedImage.TYPE_INT_RGB);
//			Graphics2D grap = tmpImage.createGraphics();
//			grap.scale(0.5, 0.5);
//			grap.drawImage(image, 0, 0, w/scaleB, h/scaleB, null);
//			grap.dispose();
//			ImageIO.write(tmpImage, "jpg", new File("d:\\test\\\\haha.jpg"));
//		}
		
	
		
		
//		String data = new HttpUtils().getPage("http://www.mca.gov.cn/article/sj/xzqh/1980/201903/201903011447.html");
//		String ccName = "" , ccupName = "";
//		String upID = "" , upName ="" , ccID="",ccNam="";
//		StringBuilder sb = new StringBuilder();
//		for(String tr : new HtmlTool(data.replaceAll("\\s+", "")).getByElement("tr").toString().split("\n")) {
//			if(tr.indexOf("mso-height-source:userset;height:14.25pt") != -1) {
//				String tds[] = new HtmlTool(tr).getByElement("td").toString().split("\n");
//				String cityID = st.getByString(tds[1], ">(.+?</td)" , ">|</td").replaceAll("\\s+", "");
//				String cityName = st.getByString(tds[2], "</span>(.+?</td>)" , "</(span|td)>").replaceAll("\\s+", "");
//				String cityUP = st.getByString(tds[2], ">(.+?</td)" , ">|</td").replaceAll("\\s+", "");
//				if(cityUP.indexOf("spanstyle") == -1) {
//					upID = cityID;
//					upName = cityUP;
//					
//					ccupName = cityID + " ---- " + cityUP;
////					System.out.println();
//				}else {
//					
//					if(cityID.split("")[4].equals("0")&&cityID.split("")[5].equals("0")) {
//						ccName = cityID + " --- " +  cityName;
//						ccID = cityID;
//						ccNam = cityName;
////						System.out.println(ccName);
//					}else {
//						sb.append(upName.replaceAll("\\s+", "")+"-"+ccNam.replaceAll("\\s+", "")+"-"+cityName.replaceAll("\\s+", "")+"--"+upID.replaceAll("\\s+", "")+"-"+ccID.replaceAll("\\s+", "")+"-"+cityID.replaceAll("\\s+", "")+"\n");
//						System.out.println(ccupName + " -- " +ccName + " ------- " +cityID + " --- " + cityName);
//					}	
//				}
//				System.out.println();
//			}
//			
//		}
//		
//		ft.writeFile(sb.toString(), "d:\\test\\\\cityss.txt");
		
		
		
//		ArrayList<city> cityList = new ArrayList<test.city>();
//		ArrayList<citys> citysList = new ArrayList<test.citys>();
//		HttpUtils hu = new HttpUtils();
//		String page = hu.getResponse("http://www.ip33.com/area_code.html");
//		StringBuilder sb = new StringBuilder();
//		for(String div : st.getByAllString(page, "<div class=\"ip\">(.+?</div>)", "<div class=\"ip\">|<div").split("\n")) {
//			test.city ci = new test.city();
//			String cityName = st.getByString(div, "<h4>(.+?</h4>)", "<h4>|</h4>");
//			ci.setCityName(cityName.split(" ")[0]);
//			ci.setCityID(cityName.split(" ")[1]);
//			System.out.println("city : "+cityName.split(" ")[0]);
//			String li = st.getByAllString(div, "<li>(.+?h5(.+?</ul(.+?</li>)))","");
//			for(String l : li.split("\n")) {
//				HtmlTool ht = new HtmlTool(l);
//				String cityHome = ht.getByElement("h5").toString().replaceAll("<h5>|</h5>", "");
//				String cityS = ht.getByElement("li").toString().replaceAll("<li>|</li>", "");
//				System.out.println("cityHome : "+cityHome);
//				if(cityS.indexOf("ul") != -1) {
//					cityS = cityS.split("<ul>")[1].replaceAll("                        ", "");
//				}
//				System.out.println("cityS : "+cityS);
//				for(String ccc : cityS.split("\n")) {
////					if(ccc.split(" ")[0].indexOf("县") != -1) {
////						sb.append(cityName.split(" ")[0].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[0].replaceAll("\\s+", "")+"-"+ccc.split(" ")[0].replaceAll("\\s+", "").replaceAll("县", "区")+"--"+cityName.split(" ")[1].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[1].replaceAll("\\s+", "")+"-"+ccc.split(" ")[1].replaceAll("\\s+", "")+"\n");
////					}
//					sb.append(cityName.split(" ")[0].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[0].replaceAll("\\s+", "")+"-"+ccc.split(" ")[0].replaceAll("\\s+", "")+"--"+cityName.split(" ")[1].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[1].replaceAll("\\s+", "")+"-"+ccc.split(" ")[1].replaceAll("\\s+", "")+"\n");
//				}
////				sb.append(cityName.split(" ")[0].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[0].replaceAll("\\s+", "")+"--"+cityName.split(" ")[1].replaceAll("\\s+", "")+"-"+cityHome.split(" ")[1].replaceAll("\\s+", "")+"\n");
//
//			}
////			System.out.println(li);
//			System.out.println("######################################################");
////			sb.append(cityName.split(" ")[0].replaceAll("\\s+", "")+"--"+cityName.split(" ")[1].replaceAll("\\s+", "")+"\n");
//		}
//		System.out.println(st.getByString(text2, text2.substring(0,lastIndex+1)+"(.+?区)", ""));

		
		
//		ft.writeFile(sb.toString(), "d:\\test\\\\citys.txt");
		
	}

}
