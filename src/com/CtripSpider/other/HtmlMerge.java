package com.CtripSpider.other;

import com.ToolBox.util.HtmlTool;

public class HtmlMerge {

	/**
	 * 负责修改标签名
	 * <p>
	 * title：标签名
	 * <p>
	 * 
	 */
	public String TitleAppend(String title) {
		String titleHead = "<p align=\"center\" style=\"text-align:left;\">\r\n"
				+ "    <span style=\"line-height:1.5;color:#333333;font-family:Microsoft YaHei;font-size:14px;background-color:#FFFFFF;\">Text</span>\r\n"
				+ "</p>";
		return titleHead.replaceAll("Text", title);
	}

	/**
	 * 负责拼接、拆分、定制出符合要求的网页
	 * <p>
	 * title:景点名称
	 * <p>
	 * imageLinks：景点所有图片链接
	 * <p>
	 */
	public String htmlOut(String title, String imageLinks) {
		String end = "<p align=\"center\" style=\"text-align:left;\">\r\n"
				+ "  <span style=\"font-size:14px;color:#333333;\">\r\n" + "    <br></span>\r\n" + "</p>";
		title = new HtmlTool(title).getByElement("p").toString();
		if (title.indexOf("img") != -1) {
			title = title.replaceAll("<p>",
					"<span style=\"line-height:1.5;color:#333333;font-family:Microsoft YaHei;font-size:14px;background-color:#FFFFFF;\"><span><p>")
					.replaceAll("</p>", "</span></p>").replaceAll("<img", "<p><img").replaceAll("/>", "/></p>");
		} else {
//			System.err.println(imageLinks);
			String img = "<p><img src=\"IMG\" /></p>";
			String arrImg[] = imageLinks.split("\n");
			String arrTitle[] = title.split("\n");
			int len = arrImg.length;
			int titleLen = arrTitle.length;
			int num = 0;
			StringBuilder titleString = new StringBuilder();
			if (title.equals("")) {
//				img = img.replaceAll("IMG\"", "IMG\" style=\"transform: scale(0.5);transform-origin: 0 0;\"");
				title = TitleAppend(title) + end;
				for (int i = 0; i < len; i++) {
					titleString.append(title.replaceAll("</p>", img.replaceAll("IMG", arrImg[i]) + "+</p>"));
				}
			} else {
				if (title.indexOf("img") == -1) {
//					img = img.replaceAll("IMG\"", "IMG\" style=\"transform: scale(0.5);transform-origin: 0 0;\"");
					if ((len % 2) == 0) {
						num = 0;
						for (int i = 0; i < titleLen; i++) {
							if (num < len) {
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
							} else {
								titleString.append(arrTitle[i]);
							}
						}
					} else {
						num = 0;
						for (int i = 0; i < titleLen; i++) {
							if (num < len) {
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
							} else {
								titleString.append(arrTitle[i]);
							}
						}
					}
				} else {
					if ((len % 2) == 0) {
						num = 0;
						for (int i = 0; i < titleLen; i++) {
							if (num < len) {
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
							} else {
								titleString.append(arrTitle[i]);
							}
						}
					} else {
						num = 0;
						for (int i = 0; i < titleLen; i++) {
							if (num < len) {
								titleString.append(
										arrTitle[i].replaceAll("</p>", img.replaceAll("IMG", arrImg[num]) + "</p>"));
								num++;
							} else {
								titleString.append(arrTitle[i]);
							}
						}
					}
				}
				title = titleString.toString();
				title = title.replaceAll("<p>",
						"<span style=\"line-height:1.5;color:#333333;font-family:Microsoft YaHei;font-size:14px;background-color:#FFFFFF;\"><span><p>")
						.replaceAll("</p>", "</span></p>").replaceAll("<img", "<p><img").replaceAll("/>", "/></p>");
				title = TitleAppend(title) + end;
			}

		}
		return title;
	}

}
