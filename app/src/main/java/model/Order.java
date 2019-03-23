package model;


public class Order {
    private int id;		//订单号
    private String submitTime;	//发布时间
    private int acceptId;	//收单人Id
    private int sendId;		//发单人Id
    private String takeName;		//收货人姓名
    private String getAdress;	//收货地址
    private String takeCode;		//取货号
    private String takeTeleohone;	//收货电话
    private String takeTime;		//取货时间
    private String expressName;		//快递点
    private int state;		//订单状态
    private String endTime;	//订单结束时间
    private float money;
//	private String vTelephone;		//取货人电话
//	private int bedroomBuild;		//收货楼栋
//	private int bedroomNumber;		//收货寝室号
//	private String sentTelephone;
//	private String hTelephone;		//收货人电话

    public int getId() {
        return id;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSubmitTime() {
        return submitTime;
    }
    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }
    public int getAcceptId() {
        return acceptId;
    }
    public void setAcceptId(int acceptId) {
        this.acceptId = acceptId;
    }
    public int getSendId() {
        return sendId;
    }
    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public String getTakeName() {
        return takeName;
    }
    public void setTakeName(String takeName) {
        this.takeName = takeName;
    }
    public String getGetAdress() {
        return getAdress;
    }
    public void setGetAdress(String getAdress) {
        this.getAdress = getAdress;
    }
    public String getTakeCode() {
        return takeCode;
    }
    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }
    public String getTakeTeleohone() {
        return takeTeleohone;
    }
    public void setTakeTeleohone(String takeTeleohone) {
        this.takeTeleohone = takeTeleohone;
    }
    public String getTakeTime() {
        return takeTime;
    }
    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }
    public String getExpressName() {
        return expressName;
    }
    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public float getMoney(){
        return money;
    }
    public void setMoney(float money){
        this.money = money;
    }

}
