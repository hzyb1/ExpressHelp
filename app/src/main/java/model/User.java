package model;

import java.io.Serializable;

public class User implements Serializable {
	private int id;		//主键
	private String telephone;		//电话号
	private String username;		//用户名
	private String password;		//密码
	private int bedroomBuild;		//寝室楼栋
	private int bedroomNumber;		//寝室号
	private String sex;				//性别
	private String headImage;		//头像路径
	private int sendNumber;		//发出订单数量
	private boolean isBan;	//是否被禁用
	private float balance;	//余额
	private int credit;		//信誉度
	private String idCard;	//身份证号
	private String trueName;	//真实姓名
	private String schoolNumber;	//学号
	private int acceptNumber;	//接单数量
	private float grade;	//评分

	public boolean isBan() {
		return isBan;
	}

	public void setBan(boolean ban) {
		isBan = ban;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public int getAcceptNumber() {
		return acceptNumber;
	}
	public void setAcceptNumber(int acceptNumber) {
		this.acceptNumber = acceptNumber;
	}
	public int getSendNumber() {
		return sendNumber;
	}
	public void setSendNumber(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getBedroomBuild() {
		return bedroomBuild;
	}
	public void setBedroomBuild(int bedroomBuild) {
		this.bedroomBuild = bedroomBuild;
	}
	public int getBedroomNumber() {
		return bedroomNumber;
	}
	public void setBedroomNumber(int bedroomNumber) {
		this.bedroomNumber = bedroomNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSchoolNumber() {
		return schoolNumber;
	}
	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}


}
