package com.xjj.schoollbigscreen.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import androidx.core.app.ActivityCompat;

/**
 * FileName: NetStateUtils
 * Author: Target
 * Date: 2020-08-06 16:30
 * 网络状态工具类
 */
public class NetStateUtils {

    private static final String TAG = "PingNetworkUtils >>> ";
    private static PingNetWorkListener pingNetWorkListener;

    /**
     * ping监听器接口定义
     */
    public interface PingNetWorkListener {
        void onPingState(boolean isConnected);
    }


    /**
     * 设置ping结果反馈监听器
     * @param listener  监听器
     */
    public static void setPingListener(PingNetWorkListener listener) {
        pingNetWorkListener = listener;
    }

    /**
     * 开始ping www.baidu.com， 异步处理
     */
    public static void startPing() {
        Log.d(TAG, " ========= startPing ========== ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (pingNetWorkListener != null) {
                    int state = checkNetworkByHttp();
                    if (state == 2) {
                        boolean ret = checkNetworkByPing();
                        pingNetWorkListener.onPingState(ret);
                    } else if (state == 0){
                        pingNetWorkListener.onPingState(true);
                    } else {
                        pingNetWorkListener.onPingState(false);
                    }
                } else {
                    Log.d(TAG, "[startPing] pingNetWorkListener is null.");
                }
            }
        }).start();
    }

    /**
     * 检测网络是否能连接外网，通过http方式
     * @return  0 连通  1 不连通 2 异常
     */
    public static int checkNetworkByHttp() {
        Log.d(TAG, "start to connect www.baidu.com by http....");
        int isConnected;
        try {
            URL url = new URL("http://www.baidu.com/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection == null) {
                Log.d(TAG, "[checkNetworkByHttp] httpURLConnection is null!");
                return 1;
            }
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                Log.d(TAG, "[checkNetworkByHttp] inputStream is null!");
                return 1;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (bufferedReader.readLine().length() > 1) {
                isConnected = 0;
                Log.d(TAG, "connected baidu by http success!");
            } else {
                isConnected = 1;
                Log.d(TAG, "connected baidu by http failed!");
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            isConnected = 2;
            Log.d(TAG, "connected baidu wlan by http exception!");
            e.printStackTrace();
        }
        return isConnected;
    }

    /**
     * 检测网络是否能连接外网，通过ping方式
     * @return  true/false
     */
    public static boolean checkNetworkByPing() {
        Log.d(TAG, "start to connect www.baidu.com by ping....");
        try {
            // ping 10次 每次超时1秒，阻塞处理
            Process process = Runtime.getRuntime().exec("ping -c 10 -W 1 www.baidu.com");
            if (process == null) {
                Log.d(TAG, " ping process is null.");
                return false;
            }

            // 读取ping的内容
            InputStream input = process.getInputStream();
            if (input == null) {
                Log.d(TAG, " process InputStream is null.");
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuffer = new StringBuilder();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content).append("\n");
            }
            Log.d(TAG, "ping result : \n" + stringBuffer.toString());

            if (stringBuffer.length() > 0 && !stringBuffer.toString().contains("100% packet loss")) {
                return true;
            }

//            /* ping的状态, 不调用waitFor接口，会导致卡住异常 */
//            int status;
//            try {
//                status = process.waitFor();
//                if (status == 0) {
//                    isConnected = true;
//                    Log.d(TAG, "connected baidu by ping success.");
//                } else {
//                    isConnected = false;
//                    Log.d(TAG, "connected baidu by ping failed.");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            Log.e(TAG, "connected baidu by ping failed because of IOException.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前网络是否是移动网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi网络
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否是2G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * wifi是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * 原生
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                //返回NetworkInfo的类型
                return networkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                    (Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                netType = 2;
            }
        }
        return netType;
    }

    /**
     * 判断GPS是否打开
     * ACCESS_FINE_LOCATION权限
     *
     * @param context
     * @return
     */
    public static boolean isGPSEnabled(Context context) {
        //获取手机所有连接LOCATION_SERVICE对象
        LocationManager locationManager = ((LocationManager) context.getSystemService(Context
                .LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获得本机ip地址
     *
     * @return
     */
    public static String GetHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取本机串号imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return telephonyManager.getDeviceId();
        }
        return telephonyManager.getDeviceId();
    }

    /***
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     *
     * @return
     */

    public static final boolean ping() {

        String result = null;
        try {
            String ip = "https://www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;

    }
}
