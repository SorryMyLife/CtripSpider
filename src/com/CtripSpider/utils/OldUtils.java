package com.CtripSpider.utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.CtripSpider.info.CtripCityScenic;
import com.ToolBox.util.FileTool;
import com.ToolBox.util.HtmlTool;

public class OldUtils {

	public void Scan(WebDriver driver) {// 判断网页是否需要进行安全验证，如果需要安全验证那就阻塞
//		sleep(1000);
		if (checkSeccenter(driver.getCurrentUrl())) {
			MoveImg(driver);
			System.out.println("请解决安全验证(解决完后输入任意内容回车跳过)");
			@SuppressWarnings("resource")
			Scanner s = new Scanner(System.in);
			s.nextLine();
		}

	}

	// 获取某个城市里的景点链接以及名称
	public void getCityScenics(WebDriver driver, int MaxPageNum, ArrayList<CtripCityScenic> citys, String cityName,
			String cityLinkPath) {
//		HtmlTool ht = new HtmlTool(new FileTool().readFile("f:\\files\\sdfjjllll.html").replaceAll("\\s+", ""));
//		sleep(3000);
		Scan(driver);
		String page = driver.getPageSource();
		String urllink = null;
		for (int i = 0; i < MaxPageNum; i++) {
			int sleepTime = getSleepTime(2, 1);
			HtmlTool ht = new HtmlTool(page.replaceAll("\\s+", ""));
			for (String a : ht.getByElement("dt").toHtmlTool().getByElement("a").toString().split("\n")) {
				if (a.indexOf("sight") != -1) {
					HtmlTool ahtml = new HtmlTool(a);
					CtripCityScenic cityScenic = new CtripCityScenic();
					cityScenic.setHref("https://you.ctrip.com"
							+ ahtml.getByElementValue("href").toString().replaceAll("\\s+", ""));
					cityScenic.setTitle(ahtml.getByElementValue("title").toString().replaceAll("\\s+", ""));
					citys.add(cityScenic);
//					System.out.println("href : "+cityScenic.getHref());
//					System.out.println("title : "+cityScenic.getTitle());
//					System.out.println("####################################################################################################################\n\n\n");
				}
			}
			System.err.println("sleep time : " + sleepTime / 1000 + " (s)");
			if (checkNextPage(page)) {
//				System.out.println("yes");
				sleep(sleepTime);
				nextPage(driver);
				sleep(sleepTime);
			}
//			System.out.println(page.equals(driver.getPageSource()));
			Scan(driver);
			page = driver.getPageSource();
			urllink = driver.getCurrentUrl();
//			System.err.println("url : " +urllink);
		}
		System.err.println("city size : " + citys.size());
		new FileTool().writeFile(urllink, cityLinkPath + "/" + cityName + "-bak");
		System.err.println("url : " + urllink);
		System.err.println(
				"-------------------------------------------------------------------------------------------------------------------------------");
	}

	// 判断当前网页是否需要进行安全验证
	public boolean checkSeccenter(String page) {
		return page.indexOf("seccenter.ctrip.com") != -1 ? true : false;
	}

	// 破解携程安全验证滑块
//	可能会报错，但能运行
	public void MoveImg(WebDriver cd) {
		sleep(1000);
		WebElement we = cd.findElement(By.className("cpt-drop-btn"));
		int x = we.getLocation().getX(), y = we.getLocation().getY();
		Actions ac = new Actions(cd);
		ac.clickAndHold(we).moveToElement(we).moveByOffset(x + 10, y).build().perform();
	}

	// 判断当前景点里面是否有用户评论
	public boolean checkAlignCenter(String page) {
		for (String p : new HtmlTool(page.replaceAll("\\s+", "")).getByElement("p").toString().split("\n")) {
			if (p.indexOf("aligncenter") != -1) {
				return true;
			}
		}
		return false;
	}

//	配置是否有下一页
	public boolean checkNextPage(String page) {
		String a = new HtmlTool(page.replaceAll("\\s+", "")).getByElement("a").toString();
		if (a.indexOf("class=\"nextpage\"") != -1 || a.indexOf("class=\"down\"") != -1) {
			return true;
		}
		return false;
	}

	// 获取休眠时间，单位：秒
//	max是最大时间
//	min是最小时间
	public int getSleepTime(int max, int min) {
		return (new Random().nextInt(max - min + 1) + min) * 1000;
	}

//线程休眠
	@SuppressWarnings("static-access")
	public void sleep(int time) {
		try {
			new Thread().sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	单击下一页
	public void nextPage(WebDriver driver) {
//		sleep(1000);
		WebElement we = driver.findElement(By.className("nextpage"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", we);
	}

//	单击下一页
	public void DownnextPage(WebDriver driver) {
//		sleep(1000);
		WebElement we = driver.findElement(By.className("down"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", we);
	}

//	打开新的标签页
	public void openNewTable(WebDriver driver, String url) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"" + url + "\")");
	}

	// 格式化HTML转码
	public String formatHTML(String htmlData) {
		htmlData = htmlData.replaceAll("&quot;", "\"").replaceAll("&amp;", "\\&").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">").replaceAll("&nbsp;", " ");
		return htmlData;
	}

//移除特殊字符串
	public String removeStr(String data) {
		data = data.replaceAll("<p(.+?>)", "");
		data = data.replaceAll("<p>", "");
		data = data.replaceAll("</p>", "");
		data = data.replaceAll("<strong>", "");
		data = data.replaceAll("<strong(.+?>)", "");
		data = data.replaceAll("</strong>", "");
		data = data.replaceAll("<span(.+?>)|</span>", "");
		data = data.replaceAll("<span>", "");
		data = data.replaceAll("</span>", "");
		data = data.replaceAll("<br(.?>)|</br>", "");
		data = data.replaceAll("<br>", "");
		data = data.replaceAll("</br>", "");
		return data;
	}

}
