package model;


import java.sql.Date;
import java.sql.Time;

public class Order {
    private int id;		//订单号
    private Date submitTime;	//发布时间
    private int acceptId;	//收单人Id
    private int sendId;		//发单人Id
    private String takeName;		//收货人姓名
    private String getAddress;	//收货地址
    private String takeCode;		//取货号
    private String takeTelephone;	//收货电话
    private String expressName;		//快递点
    private int state;		//订单状态
    private Date completionTime;	//订单结束时间
    private float money;
    private Time firstTakeTimeBegin;		//第一个收货时间的起始时间
    private Time firstTakeTimeEnd;		//第一个收货时间的结束时间
    private Time secondTakeTimeBegin;		//第二个收货时间的起始时间
    private Time secondTakeTimeEnd;		//第二个收货时间的结束时间
    private int banUser;	//上一个被拒绝的跑手
//	private String vTelephone;		//取货人电话
//	private int bedroomBuild;		//收货楼栋
//	private int bedroomNumber;		//收货寝室号
//	private String sentTelephone;
//	private String hTelephone;		//收货人电话


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
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

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }

    public String getTakeTelephone() {
        return takeTelephone;
    }

    public void setTakeTelephone(String takeTelephone) {
        this.takeTelephone = takeTelephone;
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

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Time getFirstTakeTimeBegin() {
        return firstTakeTimeBegin;
    }

    public void setFirstTakeTimeBegin(Time firstTakeTimeBegin) {
        this.firstTakeTimeBegin = firstTakeTimeBegin;
    }

    public Time getFirstTakeTimeEnd() {
        return firstTakeTimeEnd;
    }

    public void setFirstTakeTimeEnd(Time firstTakeTimeEnd) {
        this.firstTakeTimeEnd = firstTakeTimeEnd;
    }

    public Time getSecondTakeTimeBegin() {
        return secondTakeTimeBegin;
    }

    public void setSecondTakeTimeBegin(Time secondTakeTimeBegin) {
        this.secondTakeTimeBegin = secondTakeTimeBegin;
    }

    public Time getSecondTakeTimeEnd() {
        return secondTakeTimeEnd;
    }

    public void setSecondTakeTimeEnd(Time secondTakeTimeEnd) {
        this.secondTakeTimeEnd = secondTakeTimeEnd;
    }

    public int getBanUser() {
        return banUser;
    }

    public void setBanUser(int banUser) {
        this.banUser = banUser;
    }
}
