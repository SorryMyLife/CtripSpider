package com.CtripSpider;
/**<pre>
 * Я�����棬����ȡ�Ƶꡢ������ּ۸񡢵�ַ��������š����͡�ͼƬ��������Ϣ
 * ����MySQL���ݿ����ͽ��д洢
 * 
 * ���ݿ������Լ�����ṹ����:
 * 
 * ���ݿ�����: ctriphotel
 * 
  	+----------------------+
	| Tables_in_ctriphotel |
	+----------------------+
	| city                 |
	| nowcity              |
	| scenic               |
	| sceniccity           |
	+----------------------+
 * 
 * city 
  	+--------------+--------------+------+-----+---------+----------------+
	| Field        | Type         | Null | Key | Default | Extra          |
	+--------------+--------------+------+-----+---------+----------------+
	| id           | int(11)      | NO   | PRI | NULL    | auto_increment |
	| provinceid   | varchar(256) | YES  |     | NULL    |                |
	| provincename | text         | YES  |     | NULL    |                |
	| cityid       | varchar(256) | YES  |     | NULL    |                |
	| cityname     | text         | YES  |     | NULL    |                |
	| countyid     | varchar(256) | YES  |     | NULL    |                |
	| countyname   | text         | YES  |     | NULL    |                |
	+--------------+--------------+------+-----+---------+----------------+
 * 
 * nowcity
	+--------------+------+------+-----+---------+-------+
	| Field        | Type | Null | Key | Default | Extra |
	+--------------+------+------+-----+---------+-------+
	| cityname     | text | YES  |     | NULL    |       |
	| citynamelink | text | YES  |     | NULL    |       |
	+--------------+------+------+-----+---------+-------+
 * 
 * scenic
	+---------------+--------------+------+-----+---------+----------------+
	| Field         | Type         | Null | Key | Default | Extra          |
	+---------------+--------------+------+-----+---------+----------------+
	| sceneryNo     | int(11)      | YES  |     | NULL    |                |
	| sceneryName   | varchar(256) | YES  |     | NULL    |                |
	| price         | text         | YES  |     | NULL    |                |
	| areaId        | int(11)      | YES  |     | NULL    |                |
	| address       | text         | YES  |     | NULL    |                |
	| latLon        | varchar(512) | YES  |     | NULL    |                |
	| sceneryPic    | text         | YES  |     | NULL    |                |
	| sceneryImages | text         | YES  |     | NULL    |                |
	| sceneryLevel  | varchar(256) | YES  |     | NULL    |                |
	| openTime      | varchar(256) | YES  |     | NULL    |                |
	| introduction  | mediumtext   | YES  |     | NULL    |                |
	| isOnline      | varchar(128) | YES  |     | NULL    |                |
	| status        | varchar(128) | YES  |     | NULL    |                |
	| ruralImgBig   | text         | YES  |     | NULL    |                |
	| ruralImgSmall | text         | YES  |     | NULL    |                |
	| contacts      | varchar(256) | YES  |     | NULL    |                |
	| id            | int(11)      | NO   | PRI | NULL    | auto_increment |
	| areaName      | varchar(512) | YES  |     | NULL    |                |
	+---------------+--------------+------+-----+---------+----------------+
 * 
 * sceniccity
	+----------------+------+------+-----+---------+-------+
	| Field          | Type | Null | Key | Default | Extra |
	+----------------+------+------+-----+---------+-------+
	| sceniccityname | text | YES  |     | NULL    |       |
	| sceniccitylink | text | YES  |     | NULL    |       |
	+----------------+------+------+-----+---------+-------+
 * 
 </pre>
 */
public class Main {
//	public static final String driverPath = "";
	
	
	public static void main(String[] args) throws Exception {
		
//		CtripSpider ctripSpider = new CtripSpider();
//		ctripSpider.ScenicStart();
		
//		CtripHotelSpider hotelSpider = new CtripHotelSpider();
//		hotelSpider.Run();
		
	}

}
