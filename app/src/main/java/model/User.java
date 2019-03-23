package model;

public class User {
	private int id;		//主键
	private String telephone;		//电话号
	private String username;		//用户名
	private String password;		//密码
	private int bedroomBuild;		//寝室楼栋
	private int bedroomNumber;		//寝室号
	private String sex;				//性别
	private String idCard;			//身份证
	private String trueName;		//真实姓名
	private String schoolNumber;	//学号
	private String headImage;		//头像路径
	private int acceptNumber;
	private int sendNumber;



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
