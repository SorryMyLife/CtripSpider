package com.CtripSpider;

import java.io.File;

import com.CtripSpider.info.CtripScenicInfo;
import com.CtripSpider.utils.CtripScenicSpiderInfo;
import com.CtripSpider.utils.CtripScenicUtils;
import com.ToolBox.net.HttpUtils;

/**
 * 需求： 携程爬虫<p> 需要爬取全国景区图文、用户评论<p> 思路分析： 携程官网获取全国各地城市列表->获取每个城市里景点列表->获取每个景点里图文简介、用户评论
 * 
 */

public class CtripSpider extends CtripScenicSpiderInfo {
	
	
	
	public void ScenicStart() {
		String cityName = configureOptions.getCityName();
		int maxPage = Integer.parseInt(configureOptions.getKey("CITYPAGENUM"));
		CtripScenicUtils ctripScenicUtils = new CtripScenicUtils();
		ctripScenicUtils.start(cityName, maxPage);
//		ctripScenicUtils.Upgrade();
		
	}
	
	
	

	/**
	 * images : 获取到的图片链接
	 * <p>
	 * savepath : 将获取到的图片下载保存在这个路径下面
	 * <p>
	 * 
	 */
	public void save(String savePath, CtripScenicInfo csi) {
		HttpUtils hu = new HttpUtils();
		hu.setPrintLog(false);
		String districtId = csi.getDistrictId(), SightId = csi.getSightId();
//		hu.setPrintLog(false);
		String targetUrl = "";
		String text = csi.getText();
		StringBuilder imgSb = new StringBuilder();
//		StringBuilder sb = new StringBuilder();
		int num = 0;
		savePath = savePath.replaceAll("\\\\", "/");
		String dir = savePath + "/" + districtId + "/" + SightId + "/";
		for (String img : csi.getImageLink().split("\n")) {
			try {
				
				img = img.replaceAll("\\s+", "");
				String fileName = districtId + "-" + SightId + "-" + num + ".jpg";
				String path = dir + fileName;
				hu.Download(img, dir, fileName);
				File ImageFile = new File(path);
				if (ctripSpiderImage.ImageGrayRGBCheck(path)) {
					ImageFile.delete();
					hu.Download(img, dir, fileName);
				}
				if (ctripSpiderImage.ImageGrayRGBCheck(path)) {
					ImageFile.delete();
					hu.Download(img, dir, fileName);
				}
				if (ctripSpiderImage.ImageGrayRGBCheck(path)) {
					ImageFile.delete();
					hu.Download(img, dir, fileName);
				}
				ctripSpiderImage.ImageScale(path, path);
				ctripSpiderImage.ImageAdd(path, "d:\\test\\\\shuiy.png", path);
				System.out.println("nlImage/" + districtId + "/" + SightId);
				System.out.println(fileName + "-- "+ path + " -- " +"nlImage/" + districtId + "/" + SightId);
				
//				oss.CreateFolder("nlImage/" + districtId + "/" + SightId);
//				oss.Upload(fileName, path, "nlImage/" + districtId + "/" + SightId);
//				text = text.replaceAll(img, path);
//				text = text.replaceAll("\\./n_main.css", (savePath + "/n_main.css"));
//				text = text.replaceAll("\\./common-iconfont.css", (savePath + "/common-iconfont.css"));
//				text = text.replaceAll("\\./all.css", (savePath + "/all.css"));
//				text = text.replaceAll("\\./header-icon.css", (savePath + "/header-icon.css"));
//				text = text.replaceAll("\\./addon.css", (savePath + "/addon.css"));
//				text = text.replaceAll("\\./h5loginsdk.css", (savePath + "/h5loginsdk.css"));
				text = text.replaceAll(img, targetUrl + "/" + districtId + "/" + SightId + "/" + fileName);
				text = text.replaceAll("\\./n_main.css", (targetUrl + "/n_main.css"));
				text = text.replaceAll("\\./common-iconfont.css", (targetUrl + "/common-iconfont.css"));
				text = text.replaceAll("\\./all.css", (targetUrl + "/all.css"));
				text = text.replaceAll("\\./header-icon.css", (targetUrl + "/header-icon.css"));
				text = text.replaceAll("\\./addon.css", (targetUrl + "/addon.css"));
				text = text.replaceAll("\\./h5loginsdk.css", (targetUrl + "/h5loginsdk.css"));
				csi.setText(text);
				imgSb.append(targetUrl + "/" + districtId + "/" + SightId + "/" + fileName + "\n");
//				csi.setText(htmlOut(text,images));
				csi.setImageLink(targetUrl+"/"+districtId+"/"+SightId+"/"+fileName);
//				sb.append(targetUrl+"/"+districtId+"/"+SightId+"/"+fileName+"\\|");
//				oss.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
			num++;
		}

		if (!getSenicTextImg(text).equals("")) {
			for (String im : getSenicTextImg(text).split("\n")) {
				im = im.replaceAll("\\s+", "");
				if (im.indexOf("jpg") != -1 || im.indexOf("png") != -1 || im.indexOf("gif") != -1
						|| im.indexOf("jpeg") != -1) {
					try {
						
						im = im.replaceAll("\\s+", "");
						String fileName = districtId + "-" + SightId + "-" + num + ".jpg";
						hu.Download(im, dir, fileName);
						ctripSpiderImage.ImageAdd(dir + fileName, "d:\\test\\\\shuiy.png",
								dir + fileName);
//						text = text.replaceAll(im, (dir + fileName));
						
						System.out.println("nlImage/" + districtId + "/" + SightId);
						System.out.println(fileName + " -- "+ dir + fileName + " -- "+ "nlImage/" + districtId + "/" + SightId);
						
//						oss.CreateFolder("nlImage/" + districtId + "/" + SightId);
//						oss.Upload(fileName, dir + fileName, "nlImage/" + districtId + "/" + SightId);
						text = text.replaceAll(im, targetUrl + "/" + districtId + "/" + SightId + "/" + fileName);
//						sb.append(targetUrl+"/"+districtId+"/"+SightId+"/"+fileName+"\\|");
						csi.setText(text);
						imgSb.append(targetUrl + "/" + districtId + "/" + SightId + "/" + fileName + "\n");
						csi.setImageLink(targetUrl+"/"+districtId+"/"+SightId+"/"+fileName);
//						csi.setText(htmlOut(text,csi.getImageLink()));
//						oss.shutdown();
					} catch (Exception e) {
						e.printStackTrace();
					}
					num++;
				}
			}
//			csi.setImageLink(sb.toString());
		}
		csi.setText(htmlMerge.htmlOut(csi.getText(), imgSb.toString()));
//		oss.printFiles("nlImage");
	}

}
