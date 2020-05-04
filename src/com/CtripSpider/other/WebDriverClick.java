package com.CtripSpider.other;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverClick {

	// 线程休眠
	@SuppressWarnings("static-access")
	public void sleep(int time) {
		try {
			new Thread().sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用于解决提示网页访问太快问题
	 * <p>
	 * cd：传入谷歌浏览器驱动
	 * <p>
	 * 
	 */
	public void Iknown(ChromeDriver cd) {
		try {
			if (cd.getPageSource().indexOf("太快了") != -1) {
				WebElement we = cd.findElementByCssSelector("[class='cui-roller-btns']");
				we.click();
//					we.click();
//					JSclick(cd, we);
				System.err.println("click i known!");
				cd.navigate().refresh();
//					System.err.println("程序已经模拟单击完了，请确认是否成功!!!!");
//					Scanner s = new Scanner(System.in);
//					s.nextLine();
			}
		} catch (Exception e) {

		}

	}

	/**
	 * 通过JavaScript脚本来单击元素
	 * <p>
	 * cd：网络驱动
	 * <p>
	 * we：网络元素对象
	 * <p>
	 * 
	 */
	public void JSclick(WebDriver cd, WebElement we) {
		JavascriptExecutor js = (JavascriptExecutor) cd;
		js.executeScript("arguments[0].click();", we);
	}

	/**
	 * 单击<查看更多>元素位置
	 * <p>
	 * 需要传入ChromeDriver参数
	 * <p>
	 * 
	 */
	public String clickInfo(ChromeDriver cd) {
		isSelect(cd, "[class='viewspotintro-look-more']");
		return cd.getPageSource();
	}

	/*
	 * 判断元素是否存在 <p> cd：谷歌浏览器驱动 <p> SelectElement：需要选择的元素 <p>
	 */
	public boolean isSelect(ChromeDriver cd, String SelectElement) {
		try {
//			System.out.println("start :::: ");
			if (cd.getCurrentUrl().indexOf("m.ctrip.com") != -1) {
//				System.out.println("mmmmmmmm1");
				WebElement we = cd.findElementByCssSelector(SelectElement);
//				System.out.println("mmmmmmmm2");
				we.click();
//				System.out.println("mmmmmmmm3");
				sleep(2000);
//				System.out.println("mmmmmmmm4");
				return true;
			} else if (cd.getCurrentUrl().indexOf("gs.ctrip.com") != -1) {
				WebElement we = cd.findElementByCssSelector(SelectElement);
//				System.out.println("gs1");
				we.click();
//				System.out.println("gs2");
				sleep(2000);
//				System.out.println("gs3");
//				we.click();
//				System.out.println("gs4");
//				sleep(2000);
				return true;
			}
		} catch (Exception e) {
			System.err.println(SelectElement + " not found ");
		}
		return false;
	}

}
