package com.CtripSpider.Image;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class CtripSpiderImage {

	/**
	 * 用于图片等比例缩放
	 * <p>
	 * srcImageFilePath：需要被缩放的图片路径
	 * <p>
	 * ImageOutPath：缩放后的图片保存路径
	 */
	public void ImageScale(String srcImageFilePath, String ImageOutPath) throws IOException {
		BufferedImage image = ImageIO.read(new File(srcImageFilePath));
		int h = image.getHeight(), w = image.getWidth();
		int scaleA = 4, scaleB = 2;
		if (h > 1000 && w > 1100) {
			BufferedImage tmpImage = new BufferedImage(w / scaleA, h / scaleA, BufferedImage.TYPE_INT_RGB);
			Graphics2D grap = tmpImage.createGraphics();
			grap.scale(0.5, 0.5);
			grap.drawImage(image, 0, 0, w / scaleB, h / scaleB, null);
			grap.dispose();
			ImageIO.write(tmpImage, "jpg", new File(ImageOutPath));
		}
	}

	/**
	 * 给图片去掉图标内容以及在右下角加上水印图片
	 * <p>
	 * imgPath为需要处理的图片，同时也是需要加上水印的图片
	 * <p>
	 * image2为水印图片
	 * <p>
	 * targetimage为输出路径
	 * 
	 */
	public void ImageChange(String imgPath, String image2, String targetImage) throws Exception {
		File tmpPath = new File("test.jpg");
		ImageCut(imgPath, tmpPath.toString());
		ImageAdd(tmpPath.toString(), image2, targetImage);
		tmpPath.delete();
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
	public void ImageAddOnOSS(InputStream image1, InputStream image2,String fileName, String folderName) throws Exception {
//		OSSUtils oss = new OSSUtils();
//		if(!oss.exist(folderName)) {
//			oss.CreateFolder(folderName);
//			oss.shutdown();
//			oss = new OSSUtils();
//		}
//		System.out.println(oss.exist(folderName));
		BufferedImage big = ImageIO.read(image1);
		BufferedImage tmpbig =null,tmpSmall=null;
		BufferedImage small = ImageIO.read(image2);
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
		tmpFile.delete();
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
	public void ImageAddOnOSS(InputStream image1, String image2,String fileName, String folderName) throws Exception {
		ImageAddOnOSS(image1, new FileInputStream(new File(image2)), fileName, folderName);
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
	public void ImageAddOnOSS(InputStream image1, File image2,String fileName, String folderName) throws Exception {
		ImageAddOnOSS(image1, new FileInputStream(image2), fileName, folderName);
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
		ImageAddOnOSS(new FileInputStream(new File(image1)), image2, fileName, folderName);
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
	public void ImageAddOnOSS(File image1, String image2,String fileName, String folderName) throws Exception {
		ImageAddOnOSS(new FileInputStream(image1), image2, fileName, folderName);
	}
	
	
	/***
	 * 给图片加上水印，右下角，jpg图片格式
	 * <p>
	 * image1为需要被加上水印的图片
	 * <p>
	 * image2为水印图片
	 * <p>
	 * targetImage为输出路径
	 */
	public void ImageAdd(String image1, String image2, String targetImage) throws Exception {
		BufferedImage big = ImageIO.read(new File(image1));
		BufferedImage small = ImageIO.read(new File(image2));
		Graphics2D g = big.createGraphics();
		int x = big.getWidth() - 250, y = big.getHeight() - 100;
//		System.out.println(x + " --- " +y);
		g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
		g.dispose();
		ImageIO.write(big, "jpg", new File(targetImage));
	}

	/**
	 * 去除携程图片底下带有携程图标内容
	 * <p>
	 * imgPath：图片路径
	 * <p>
	 * outPath：图片输出路径以及文件名称
	 */
	public void ImageCut(String imgPath, String outPath) throws Exception {
		BufferedImage image = ImageIO.read(new File(imgPath));
		int height = image.getHeight(), width = image.getWidth();
		image = image.getSubimage(0, 0, width, (height - 150));
//		System.out.println(height + " -- " + width);
		ImageIO.write(image, "jpeg", new File(outPath));
	}

	/**
	 * 返回图像的rgb值
	 * <p>
	 * px: 像素坐标位置
	 */
	public String getRGB(int px) {
		int r = (px & 0xff0000) >> 16, g = (px & 0xff00) >> 8, b = px & 0xff;
		return r + "-" + g + "-" + b;
	}

	/**
	 * 检查图片里是否存在未下载完成的部分
	 * <p>
	 * ImagePath： 图像路径
	 * 
	 */
	public boolean ImageGrayRGBCheck(String ImagePath) throws IOException {
		String rgb = "128-128-128", rgb1 = null, rgb2 = null;
		BufferedImage image = ImageIO.read(new File(ImagePath));
		int h = image.getHeight(), w = image.getWidth();
		int px = image.getRGB(15, h - 15);
		int px1 = image.getRGB(w - 15, h - 15);
		rgb1 = getRGB(px);
		rgb2 = getRGB(px1);
		if (rgb1.equals(rgb2)) {
			if (rgb1.equals(rgb)) {
				return true;
			}
		}
		return false;
	}

}
