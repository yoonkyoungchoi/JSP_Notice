package notice;

public class Notice {
	private int bsID;
	private String bsTitle;
	private String userID;
	private String bsDate;
	private String bsContent;
	private int bsAvailable;
	
	public int getBsID() {
		return bsID;
	}
	public void setBsID(int bsID) {
		this.bsID = bsID;
	}
	public String getBsTitle() {
		return bsTitle;
	}
	public void setBsTitle(String bsTitle) {
		this.bsTitle = bsTitle;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getBsDate() {
		return bsDate;
	}
	public void setBsDate(String bsDate) {
		this.bsDate = bsDate;
	}
	public String getBsContent() {
		return bsContent;
	}
	public void setBsContent(String bsContent) {
		this.bsContent = bsContent;
	}
	public int getBsAvailable() {
		return bsAvailable;
	}
	public void setBsAvailable(int bsAvailable) {
		this.bsAvailable = bsAvailable;
	}

}
