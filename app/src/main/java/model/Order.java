package model;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Order implements Serializable {
    private int id;		//订单号
    private Timestamp submitTime;	//发布时间
    private int acceptId;	//收单人Id
    private int sendId;		//发单人Id
    private String takeName;		//收货人姓名
    private String getAddress;	//收货地址
    private String takeCode;		//取货号
    private String takeTelephone;	//收货电话
    private String secondtakeTime;
    private String expressName;		//快递点
    private int state;		//订单状态
    private Timestamp finishTime;	//订单结束时间
    private float money;
    private Timestamp firstTakeTimeBegin;		//第一个收货时间的起始时间
    private Timestamp firstTakeTimeEnd;		//第一个收货时间的结束时间
    private Timestamp secondTakeTimeBegin;		//第二个收货时间的起始时间
    private Timestamp secondTakeTimeEnd;		//第二个收货时间的结束时间
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

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
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

    public String getSecondtakeTime() {
        return secondtakeTime;
    }

    public void setSecondtakeTime(String secondtakeTime) {
        this.secondtakeTime = secondtakeTime;
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

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Timestamp getFirstTakeTimeBegin() {
        return firstTakeTimeBegin;
    }

    public void setFirstTakeTimeBegin(Timestamp firstTakeTimeBegin) {
        this.firstTakeTimeBegin = firstTakeTimeBegin;
    }

    public Timestamp getFirstTakeTimeEnd() {
        return firstTakeTimeEnd;
    }

    public void setFirstTakeTimeEnd(Timestamp firstTakeTimeEnd) {
        this.firstTakeTimeEnd = firstTakeTimeEnd;
    }

    public Timestamp getSecondTakeTimeBegin() {
        return secondTakeTimeBegin;
    }

    public void setSecondTakeTimeBegin(Timestamp secondTakeTimeBegin) {
        this.secondTakeTimeBegin = secondTakeTimeBegin;
    }

    public Timestamp getSecondTakeTimeEnd() {
        return secondTakeTimeEnd;
    }

    public void setSecondTakeTimeEnd(Timestamp secondTakeTimeEnd) {
        this.secondTakeTimeEnd = secondTakeTimeEnd;
    }

    public int getBanUser() {
        return banUser;
    }

    public void setBanUser(int banUser) {
        this.banUser = banUser;
    }
}
