package com.CtripSpider.other;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverClick {

	// �߳�����
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
	 * ���ڽ����ʾ��ҳ����̫������
	 * <p>
	 * cd������ȸ����������
	 * <p>
	 * 
	 */
	public void Iknown(ChromeDriver cd) {
		try {
			if (cd.getPageSource().indexOf("̫����") != -1) {
				WebElement we = cd.findElementByCssSelector("[class='cui-roller-btns']");
				we.click();
//					we.click();
//					JSclick(cd, we);
				System.err.println("click i known!");
				cd.navigate().refresh();
//					System.err.println("�����Ѿ�ģ�ⵥ�����ˣ���ȷ���Ƿ�ɹ�!!!!");
//					Scanner s = new Scanner(System.in);
//					s.nextLine();
			}
		} catch (Exception e) {

		}

	}

	/**
	 * ͨ��JavaScript�ű�������Ԫ��
	 * <p>
	 * cd����������
	 * <p>
	 * we������Ԫ�ض���
	 * <p>
	 * 
	 */
	public void JSclick(WebDriver cd, WebElement we) {
		JavascriptExecutor js = (JavascriptExecutor) cd;
		js.executeScript("arguments[0].click();", we);
	}

	/**
	 * ����<�鿴����>Ԫ��λ��
	 * <p>
	 * ��Ҫ����ChromeDriver����
	 * <p>
	 * 
	 */
	public String clickInfo(ChromeDriver cd) {
		isSelect(cd, "[class='viewspotintro-look-more']");
		return cd.getPageSource();
	}

	/*
	 * �ж�Ԫ���Ƿ���� <p> cd���ȸ���������� <p> SelectElement����Ҫѡ���Ԫ�� <p>
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
