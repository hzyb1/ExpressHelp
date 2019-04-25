package util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class OrderInfoUtil2_0 {
	/**
	 * 构造支付订单参数列表
	 */
	public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2, String price) {
		Map<String, String> keyValues = new HashMap<String, String>();
		keyValues.put("app_id", app_id);
		keyValues.put("biz_content", bizCotent(price));
		keyValues.put("charset", "utf-8");
		keyValues.put("method", "alipay.trade.app.pay");
		keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
		keyValues.put("timestamp", "2016-07-29 16:55:53");
		keyValues.put("version", "1.0");
		return keyValues;
	}

	public static String bizCotent(String price) {
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		String bizValue = "{\"timeout_express\":\"15m\",";
		bizValue += "\"product_code\":\"QUICK_MSECURITY_PAY\",";
		// 商品金额
		bizValue += "\"total_amount\":" + "\"" + price + "\",";
		// 商品名称
		bizValue += "\"subject\":" + "\"" + "兔泊哥停取车交付款" + "\",";
		// 商品详情
		bizValue += "\"body\":" + "\"" + "兔泊哥停取车付款界面" + "\",";
		//商户网站唯一订单号
		bizValue += "\"out_trade_no\":" + "\"" + getOutTradeNo() + "\"}";
		return bizValue;
	}

	/**
	 * 要求外部订单号必须唯一。
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

}