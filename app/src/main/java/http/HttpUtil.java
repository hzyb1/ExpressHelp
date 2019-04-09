package http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private static String session_id = null;

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //这里写相应的网络设置处理
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
            /* 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
                    // 设置是否向HttpURLConnection输出
                    connection.setDoOutput(false);
                    // 设置是否从httpUrlConnection读入
                    connection.setDoInput(true);
                    // 设置请求方式
                    connection.setRequestMethod("GET");
                    // 设置是否使用缓存
                    connection.setUseCaches(true);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    connection.setInstanceFollowRedirects(true);
                    // 设置超时时间
                    connection.setConnectTimeout(3000);
                    // 连接
                    connection.connect();
                    // 4. 得到响应状态码的返回值 responseCode
                    int code = connection.getResponseCode();
                    // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
                    String msg = "";
                    if (code == 200) { // 正常响应
                        // 从流中读取响应信息
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        String line = null;
                        StringBuilder response = new StringBuilder();
                        while ((line = reader.readLine()) != null) { // 循环从流中读取
                            response.append(line);

                        }
                        msg = response.toString();
                        Log.d("日志",msg+"返回妈妈");
                        reader.close(); // 关闭流
                    }
                    // 6. 断开连接，释放资源
                    connection.disconnect();
                    if (listener != null){
                            listener.onFinish(msg.toString());
                        }

//                    URL url = new URL(address);
//                    Log.d("日志","url："+url);
//                    //使用HttpURLConnection
//                    connection = (HttpURLConnection) url.openConnection();
//                    //设置方法和参数
//
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//        //            connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    connection.connect();
//                    if(connection.getResponseCode() == 200){
//                        if (session_id != null) {
//                            connection.setRequestProperty("Cookie", session_id);//设置sessionid
//                        }
//                  //      InputStream is = connection.getInputStream();
////                        String cookieval = connection.getHeaderField("Set-Cookie");
////                        if (cookieval != null) {
////                            session_id = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
////                            Log.d("SESSION", "session_id=" + session_id);
////                        }
//                        //获取返回结果
//                        InputStream inputStream = connection.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                        StringBuilder response = new StringBuilder();
//                        String line = "";
//                        String result = "";
//                        while ((line = reader.readLine()) != null){
//                            result += line;
//                            response.append(line);
//                        }
//                        Log.d("日志","返回结果1："+result);
//                        Log.d("日志","返回结果："+response.toString());
//                        //成功则回调onFinish
//                        if (listener != null){
//                            listener.onFinish(response.toString());
//                        }
//                   }

                } catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
             //       if (connection != null){
              //          connection.disconnect();
               //     }
                }
            }
        }).start();
    }

    //组装出带参数的完整URL
    public static String getURLWithParams(String address,HashMap<String,String> params) throws UnsupportedEncodingException {
        //设置编码
        final String encode = "UTF-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        //将map中的key，value构造进入URL中
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        //删掉最后一个&
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(){
        //这里检查网络，后续再添加
        return true;
    }

}