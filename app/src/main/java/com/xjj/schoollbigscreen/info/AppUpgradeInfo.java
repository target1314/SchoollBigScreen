package com.xjj.schoollbigscreen.info;

import java.io.Serializable;

/**
 * 软件升级检测实体 TODO 类描述
 * <p/>
 * 创建时间: 2014年10月24日 上午10:16:22 <br/>
 * 
 * @author hwp
 * @version
 * @since v0.0.1
 */
public class AppUpgradeInfo extends  BaseResponeInfo implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * bizData : {"app":"com.cicada.kidscard.vertical","createDate":1596765599472,"createDateAsDate":1596765599472,"creator":0,"downLoadUrl":"https://static.imzhiliao.com/V1.0.2_202008071031.apk","id":274,"issue":0,"lastModDate":1603352554186,"lastModDateAsDate":1603352554186,"lastModifier":0,"status":0,"type":0,"updateType":1,"version":"1.0.2","versionCode":2,"versionIntro":"32寸刷卡机，首个对外版本","versionPics":"","versionSize":"16.6"}
	 */

	private BizDataBean bizData;

	public BizDataBean getBizData() {
		return bizData;
	}

	public void setBizData(BizDataBean bizData) {
		this.bizData = bizData;
	}

	public static class BizDataBean {
		/**
		 * app : com.cicada.kidscard.vertical
		 * createDate : 1596765599472
		 * createDateAsDate : 1596765599472
		 * creator : 0
		 * downLoadUrl : https://static.imzhiliao.com/V1.0.2_202008071031.apk
		 * id : 274
		 * issue : 0
		 * lastModDate : 1603352554186
		 * lastModDateAsDate : 1603352554186
		 * lastModifier : 0
		 * status : 0
		 * type : 0
		 * updateType : 1
		 * version : 1.0.2
		 * versionCode : 2
		 * versionIntro : 32寸刷卡机，首个对外版本
		 * versionPics :
		 * versionSize : 16.6
		 */

		private String app;
		private long createDate;
		private long createDateAsDate;
		private int creator;
		private String downLoadUrl;
		private int id;
		private int issue;
		private long lastModDate;
		private long lastModDateAsDate;
		private int lastModifier;
		private int status;
		private int type;
		private int updateType;
		private String version;
		private int versionCode;
		private String versionIntro;
		private String versionPics;
		private String versionSize;

		public String getApp() {
			return app;
		}

		public void setApp(String app) {
			this.app = app;
		}

		public long getCreateDate() {
			return createDate;
		}

		public void setCreateDate(long createDate) {
			this.createDate = createDate;
		}

		public long getCreateDateAsDate() {
			return createDateAsDate;
		}

		public void setCreateDateAsDate(long createDateAsDate) {
			this.createDateAsDate = createDateAsDate;
		}

		public int getCreator() {
			return creator;
		}

		public void setCreator(int creator) {
			this.creator = creator;
		}

		public String getDownLoadUrl() {
			return downLoadUrl;
		}

		public void setDownLoadUrl(String downLoadUrl) {
			this.downLoadUrl = downLoadUrl;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getIssue() {
			return issue;
		}

		public void setIssue(int issue) {
			this.issue = issue;
		}

		public long getLastModDate() {
			return lastModDate;
		}

		public void setLastModDate(long lastModDate) {
			this.lastModDate = lastModDate;
		}

		public long getLastModDateAsDate() {
			return lastModDateAsDate;
		}

		public void setLastModDateAsDate(long lastModDateAsDate) {
			this.lastModDateAsDate = lastModDateAsDate;
		}

		public int getLastModifier() {
			return lastModifier;
		}

		public void setLastModifier(int lastModifier) {
			this.lastModifier = lastModifier;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getUpdateType() {
			return updateType;
		}

		public void setUpdateType(int updateType) {
			this.updateType = updateType;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionIntro() {
			return versionIntro;
		}

		public void setVersionIntro(String versionIntro) {
			this.versionIntro = versionIntro;
		}

		public String getVersionPics() {
			return versionPics;
		}

		public void setVersionPics(String versionPics) {
			this.versionPics = versionPics;
		}

		public String getVersionSize() {
			return versionSize;
		}

		public void setVersionSize(String versionSize) {
			this.versionSize = versionSize;
		}
	}
}
