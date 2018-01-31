//package com.kongqw.serialport;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.ViewCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsoluteLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.android.jbs.tmc;
//import com.common.AsynHttpTask;
//import com.common.AsynHttpTaskUpload;
//import com.common.ConvertUtils;
//import com.common.DeviceUtils;
//import com.common.GlobalContants;
//import com.common.ImageDownLoader;
//import com.common.ImageDownLoader.onImageLoaderListener;
//import com.common.ShowMessage;
//import com.common.Tool;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class AllBuyRice extends Activity {
//    protected static final int BACKTO_MAIN = 50;
//    protected static final int BAOYUNYING_THJ = 47;
//    protected static final int CONNECTTING_FAIL = 39;
//    protected static final int PAYONLINE_GETDATA = 2;
//    protected static final int PAYONLINE_GETPAYYES = 4;
//    protected static final int PAYONLINE_GETWX = 5;
//    protected static final int PAYONLINE_GETZFB = 3;
//    protected static final int PAYONLINE_NOPAY = 28;
//    protected static final int PAYONLINE_NOPAY_ZFB = 40;
//    protected static final int SELECT_RICEINFO = 1;
//    protected static final int STARTMOTOR = 49;
//    protected static final int STARTMOTOR_OVER = 42;
//    protected static final int WX_ZFB_GONE = 26;
//    private int QR_HEIGHT = 200;
//    private int QR_WIDTH = 200;
//    private String TAG = "AllBuyRice";
//    private AbsoluteLayout al_jyts;
//    private AbsoluteLayout al_nocardbuy;
//    private AbsoluteLayout al_payselect;
//    private AbsoluteLayout al_select;
//    Handler handlerAddress = new Handler() {
//        public void handleMessage(Message msg) {
//            Toast toast;
//            switch (msg.what) {
//                case 1:
//                    try {
//                        if (AllBuyRice.this.m_GetRiceInfoRet.indexOf("[{") >= 0 && AllBuyRice.this.m_GetRiceInfoRet.indexOf("}]") >= 0) {
//                            int changdu = AllBuyRice.this.m_GetRiceInfoRet.split("\\},").length;
//                            if (changdu >= 1) {
//                                AllBuyRice.this.getImageView1(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[0]);
//                            }
//                            if (changdu >= 2) {
//                                AllBuyRice.this.getImageView2(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[1]);
//                            }
//                            if (changdu >= 3) {
//                                AllBuyRice.this.getImageView3(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[2]);
//                            }
//                            if (changdu >= 4) {
//                                AllBuyRice.this.getImageView4(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[3]);
//                            }
//                            if (changdu >= 5) {
//                                AllBuyRice.this.getImageView5(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[4]);
//                            }
//                            if (changdu >= 6) {
//                                AllBuyRice.this.getImageView6(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[5]);
//                            }
//                            if (changdu >= 7) {
//                                AllBuyRice.this.getImageView7(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[6]);
//                            }
//                            if (changdu >= 8) {
//                                AllBuyRice.this.getImageView8(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[7]);
//                            }
//                            if (changdu >= 9) {
//                                AllBuyRice.this.getImageView9(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[8]);
//                            }
//                            if (changdu >= 10) {
//                                AllBuyRice.this.getImageView10(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[9]);
//                            }
//                            if (changdu >= 11) {
//                                AllBuyRice.this.getImageView11(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[10]);
//                            }
//                            if (changdu >= 12) {
//                                AllBuyRice.this.getImageView12(AllBuyRice.this.m_GetRiceInfoRet.split("\\},")[11]);
//                                return;
//                            }
//                            return;
//                        }
//                        return;
//                    } catch (Exception ex) {
//                        AllBuyRice.this.insertErrorlog("SELECT_RICEINFO", ex);
//                        AllBuyRice.this.writeErrorLog("SELECT_RICEINFO", ex);
//                        return;
//                    }
//                case 2:
//                    try {
//                        AllBuyRice.this.tv_cpmc.setText("品名:" + AllBuyRice.this.m_GoodName);
//                        AllBuyRice.this.tv_cpjg.setText("价格:" + AllBuyRice.this.m_GoodPrice + "元");
//                        AllBuyRice.this.tv_gmsl.setText("购买数量:" + AllBuyRice.this.m_BuyNum);
//                        AllBuyRice.this.GetWeiXinCode(AllBuyRice.this.m_GoodName, AllBuyRice.this.m_NoCardOrderID, AllBuyRice.this.m_GoodPrice, AllBuyRice.this.m_RiceID);
//                        AllBuyRice.this.GetZFBUrl(AllBuyRice.this.m_GoodName, AllBuyRice.this.m_NoCardOrderID, AllBuyRice.this.m_GoodPrice);
//                        return;
//                    } catch (Exception ex2) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_GETDATA", ex2);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_GETDATA", ex2);
//                        return;
//                    }
//                case 3:
//                    try {
//                        AllBuyRice.this.createQRImage(AllBuyRice.this.iv_zfewm, AllBuyRice.this.m_PayOnlinezfbRet);
//                        return;
//                    } catch (Exception ex22) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_GETZFB", ex22);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_GETZFB", ex22);
//                        return;
//                    }
//                case 4:
//                    try {
//                        AllBuyRice.this.iv_qxzf.setVisibility(8);
//                        ShowMessage.ShowToast(AllBuyRice.this.getApplicationContext(), "支付成功!");
//                        AllBuyRice.this.tv_zftxt.setText("-此单已经支付-");
//                        AllBuyRice.this.sum_command_start(AllBuyRice.this.m_HDBH);
//                        AllBuyRice.this.sum_command_poll(AllBuyRice.this.m_HDBH);
//                        AllBuyRice.this.FetchGoods();
//                        AllBuyRice.this.al_nocardbuy.setVisibility(8);
//                        AllBuyRice.this.al_jyts.setVisibility(0);
//                        AllBuyRice.this.tv_jyts2.setText("正在出货,请稍等...");
//                        return;
//                    } catch (Exception ex222) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_GETPAYYES", ex222);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_GETPAYYES", ex222);
//                        return;
//                    }
//                case 5:
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception ex2222) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_GETWX", ex2222);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_GETWX", ex2222);
//                    }
//                    AllBuyRice.this.iv_zfewm.setImageBitmap(AllBuyRice.this.m_PayOnlinebitmap);
//                    return;
//                case AllBuyRice.WX_ZFB_GONE /*26*/:
//                    try {
//                        AllBuyRice.this.backto_selectrice();
//                        return;
//                    } catch (Exception ex22222) {
//                        AllBuyRice.this.insertErrorlog("WX_ZFB_GONE", ex22222);
//                        AllBuyRice.this.writeErrorLog("WX_ZFB_GONE", ex22222);
//                        return;
//                    }
//                case AllBuyRice.PAYONLINE_NOPAY /*28*/:
//                    try {
//                        AllBuyRice.this.backto_selectrice();
//                        return;
//                    } catch (Exception ex222222) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_NOPAY", ex222222);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_NOPAY", ex222222);
//                        return;
//                    }
//                case AllBuyRice.CONNECTTING_FAIL /*39*/:
//                    try {
//                        toast = Toast.makeText(AllBuyRice.this.getApplicationContext(), "网络连接失败!", 0);
//                        toast.setGravity(17, 0, 0);
//                        toast.show();
//                        return;
//                    } catch (Exception ex2222222) {
//                        AllBuyRice.this.insertErrorlog("CONNECTTING_FAIL", ex2222222);
//                        AllBuyRice.this.writeErrorLog("CONNECTTING_FAIL", ex2222222);
//                        return;
//                    }
//                case AllBuyRice.PAYONLINE_NOPAY_ZFB /*40*/:
//                    try {
//                        AllBuyRice.this.payonline_nopay_zfb(AllBuyRice.this.m_NoCardOrderID, String.valueOf(AllBuyRice.this.payType));
//                        AllBuyRice.this.backto_selectrice();
//                        return;
//                    } catch (Exception ex22222222) {
//                        AllBuyRice.this.insertErrorlog("PAYONLINE_NOPAY_ZFB", ex22222222);
//                        AllBuyRice.this.writeErrorLog("PAYONLINE_NOPAY_ZFB", ex22222222);
//                        return;
//                    }
//                case AllBuyRice.STARTMOTOR_OVER /*42*/:
//                    try {
//                        AllBuyRice.this.tv_jyts2.setText("欢迎下次光临");
//                        AllBuyRice.this.timertwo_waitYback();
//                        return;
//                    } catch (Exception ex222222222) {
//                        AllBuyRice.this.insertErrorlog("STARTMOTOR_OVER", ex222222222);
//                        AllBuyRice.this.writeErrorLog("STARTMOTOR_OVER", ex222222222);
//                        return;
//                    }
//                case AllBuyRice.BAOYUNYING_THJ /*47*/:
//                    try {
//                        toast = Toast.makeText(AllBuyRice.this.getApplicationContext(), "弹簧机故障!", 1);
//                        toast.setGravity(17, 0, 0);
//                        toast.show();
//                        return;
//                    } catch (Exception ex2222222222) {
//                        ex2222222222.printStackTrace();
//                        return;
//                    }
//                case AllBuyRice.STARTMOTOR /*49*/:
//                    try {
//                        AllBuyRice.this.FetchGoods();
//                        return;
//                    } catch (Exception ex22222222222) {
//                        AllBuyRice.this.insertErrorlog("STARTMOTOR", ex22222222222);
//                        AllBuyRice.this.writeErrorLog("STARTMOTOR", ex22222222222);
//                        return;
//                    }
//                case AllBuyRice.BACKTO_MAIN /*50*/:
//                    try {
//                        AllBuyRice.this.tv_jyts2.setText("");
//                        AllBuyRice.this.backto_selectrice();
//                        return;
//                    } catch (Exception ex222222222222) {
//                        AllBuyRice.this.insertErrorlog("STARTMOTOR", ex222222222222);
//                        AllBuyRice.this.writeErrorLog("STARTMOTOR", ex222222222222);
//                        return;
//                    }
//                default:
//                    return;
//            }
//        }
//    };
//    private Intent intent;
//    private Boolean isOpenIntent = Boolean.valueOf(false);
//    private Boolean isPayOnlineStart = Boolean.valueOf(false);
//    private Boolean isTHJThread = Boolean.valueOf(false);
//    private ImageView iv_a10;
//    private ImageView iv_a11;
//    private ImageView iv_a12;
//    private ImageView iv_a4;
//    private ImageView iv_a5;
//    private ImageView iv_a6;
//    private ImageView iv_a7;
//    private ImageView iv_a8;
//    private ImageView iv_a9;
//    private ImageView iv_clx;
//    private ImageView iv_dhx;
//    private ImageView iv_qxzf;
//    private ImageView iv_ricetp;
//    private ImageView iv_sm;
//    private ImageView iv_weixinxz;
//    private ImageView iv_wycz;
//    private ImageView iv_zfbxz;
//    private ImageView iv_zfewm;
//    private ImageDownLoader mImageDownLoader;
//    private String m_BuyNum = "";
//    private int m_FiveMin = 0;
//    private String m_GetRiceInfoRet = "";
//    private String m_GoodName = "";
//    private String m_GoodPrice = "";
//    public String m_HDBH = "";
//    public String m_NoCardOrderID = "";
//    public int m_PageNO = 1;
//    private ThreadOrder m_PayOnlineOrder;
//    private Bitmap m_PayOnlinebitmap;
//    private String m_PayOnlinezfbRet = "";
//    public String m_PicUrl = "";
//    public String m_RiceID = "";
//    private TimerTask m_TimerTask_four = null;
//    private TimerTask m_TimerTask_one = null;
//    private TimerTask m_TimerTask_three = null;
//    private TimerTask m_TimerTask_two = null;
//    private Timer m_Timer_four = null;
//    private Timer m_Timer_one = null;
//    private Timer m_Timer_three = null;
//    private Timer m_Timer_two = null;
//    public int m_TotalPage = 0;
//    private ThreadMachineStatus m_threadMachineStatus = null;
//    private ThreadTHJ m_threadTHJ = null;
//    private tmc m_tmc = null;
//    private String m_zhiling = "";
//    private String m_zhiling_check = "";
//    private String m_zhiling_poll = "";
//    private int openTHJ = -1;
//    private int payType = 0;
//    private TextView tv_a10_cpid;
//    private TextView tv_a10_cpjg;
//    private TextView tv_a10_cpmc;
//    private TextView tv_a10_gmsl;
//    private TextView tv_a10_hdbh;
//    private TextView tv_a10_pic;
//    private TextView tv_a11_cpid;
//    private TextView tv_a11_cpjg;
//    private TextView tv_a11_cpmc;
//    private TextView tv_a11_gmsl;
//    private TextView tv_a11_hdbh;
//    private TextView tv_a11_pic;
//    private TextView tv_a12_cpid;
//    private TextView tv_a12_cpjg;
//    private TextView tv_a12_cpmc;
//    private TextView tv_a12_gmsl;
//    private TextView tv_a12_hdbh;
//    private TextView tv_a12_pic;
//    private TextView tv_a4_cpid;
//    private TextView tv_a4_cpjg;
//    private TextView tv_a4_cpmc;
//    private TextView tv_a4_gmsl;
//    private TextView tv_a4_hdbh;
//    private TextView tv_a4_pic;
//    private TextView tv_a5_cpid;
//    private TextView tv_a5_cpjg;
//    private TextView tv_a5_cpmc;
//    private TextView tv_a5_gmsl;
//    private TextView tv_a5_hdbh;
//    private TextView tv_a5_pic;
//    private TextView tv_a6_cpid;
//    private TextView tv_a6_cpjg;
//    private TextView tv_a6_cpmc;
//    private TextView tv_a6_gmsl;
//    private TextView tv_a6_hdbh;
//    private TextView tv_a6_pic;
//    private TextView tv_a7_cpid;
//    private TextView tv_a7_cpjg;
//    private TextView tv_a7_cpmc;
//    private TextView tv_a7_gmsl;
//    private TextView tv_a7_hdbh;
//    private TextView tv_a7_pic;
//    private TextView tv_a8_cpid;
//    private TextView tv_a8_cpjg;
//    private TextView tv_a8_cpmc;
//    private TextView tv_a8_gmsl;
//    private TextView tv_a8_hdbh;
//    private TextView tv_a8_pic;
//    private TextView tv_a9_cpid;
//    private TextView tv_a9_cpjg;
//    private TextView tv_a9_cpmc;
//    private TextView tv_a9_gmsl;
//    private TextView tv_a9_hdbh;
//    private TextView tv_a9_pic;
//    private TextView tv_clx_cpid;
//    private TextView tv_clx_cpjg;
//    private TextView tv_clx_cpmc;
//    private TextView tv_clx_gmsl;
//    private TextView tv_clx_hdbh;
//    private TextView tv_clx_pic;
//    private TextView tv_cpjg;
//    private TextView tv_cpmc;
//    private TextView tv_dhx_cpid;
//    private TextView tv_dhx_cpjg;
//    private TextView tv_dhx_cpmc;
//    private TextView tv_dhx_gmsl;
//    private TextView tv_dhx_hdbh;
//    private TextView tv_dhx_pic;
//    private TextView tv_gmsl;
//    private TextView tv_jyts;
//    private TextView tv_jyts2;
//    private TextView tv_jyts3;
//    private TextView tv_sm_cpid;
//    private TextView tv_sm_cpjg;
//    private TextView tv_sm_cpmc;
//    private TextView tv_sm_gmsl;
//    private TextView tv_sm_hdbh;
//    private TextView tv_sm_pic;
//    private TextView tv_zftxt;
//
//    class ThreadMachineStatus extends Thread {
//        ThreadMachineStatus() {
//        }
//
//        public void run() {
//            while (true) {
//                try {
//                    AllBuyRice.this.update_jilu_yun();
//                    Thread.sleep(180000);
//                } catch (Exception ex) {
//                    AllBuyRice.this.insertErrorlog("ThreadMachineStatus", ex);
//                    AllBuyRice.this.writeErrorLog("ThreadMachineStatus", ex);
//                }
//            }
//        }
//    }
//
//    class ThreadOrder extends Thread {
//        ThreadOrder() {
//        }
//
//        public void run() {
//            while (true) {
//                if (AllBuyRice.this.isPayOnlineStart.booleanValue()) {
//                    try {
//                        Thread.sleep(5000);
//                        if (AllBuyRice.this.isPayOnlineStart.booleanValue()) {
//                            AllBuyRice.this.GetOrderOK(AllBuyRice.this.m_NoCardOrderID);
//                        }
//                    } catch (Exception ex) {
//                        AllBuyRice.this.insertErrorlog("classThreadOrder", ex);
//                        AllBuyRice.this.writeErrorLog("classThreadOrder", ex);
//                    }
//                }
//            }
//        }
//    }
//
//    class ThreadTHJ extends Thread {
//        ThreadTHJ() {
//        }
//
//        public void run() {
//            int StartFlag = 0;
//            byte[] send_start = new byte[20];
//            byte[] send_poll = new byte[20];
//            while (true) {
//                if (AllBuyRice.this.isTHJThread.booleanValue()) {
//                    if (-1 == AllBuyRice.this.openTHJ) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
//                    } else {
//                        byte[] strByte;
//                        if (StartFlag == 0) {
//                            StartFlag = 1;
//                            Log.e(AllBuyRice.this.TAG, "===========ThreadTHJ=========m_zhiling=" + AllBuyRice.this.m_zhiling);
//                            send_start[0] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(0, 2), 16);
//                            send_start[1] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(2, 4), 16);
//                            send_start[2] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(4, 6), 16);
//                            send_start[3] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(6, 8), 16);
//                            send_start[4] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(8, 10), 16);
//                            send_start[5] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(10, 12), 16);
//                            send_start[6] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(12, 14), 16);
//                            send_start[7] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(14, 16), 16);
//                            send_start[8] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(16, 18), 16);
//                            send_start[9] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(18, 20), 16);
//                            send_start[10] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(20, 22), 16);
//                            send_start[11] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(22, 24), 16);
//                            send_start[12] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(24, AllBuyRice.WX_ZFB_GONE), 16);
//                            send_start[13] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(AllBuyRice.WX_ZFB_GONE, AllBuyRice.PAYONLINE_NOPAY), 16);
//                            send_start[14] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(AllBuyRice.PAYONLINE_NOPAY, 30), 16);
//                            send_start[15] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(30, 32), 16);
//                            send_start[16] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(32, 34), 16);
//                            send_start[17] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(34, 36), 16);
//                            send_start[18] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(36, 38), 16);
//                            send_start[19] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling.substring(38, AllBuyRice.PAYONLINE_NOPAY_ZFB), 16);
//                            AllBuyRice.this.m_tmc.send(AllBuyRice.this.openTHJ, send_start);
//                            Log.e(AllBuyRice.this.TAG, "-------ThreadTHJ-------send_start--------send_start=" + AllBuyRice.this.m_zhiling);
//                            try {
//                                Thread.sleep(70);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            strByte = AllBuyRice.this.m_tmc.receiver(AllBuyRice.this.openTHJ, 1000);
//                            if (strByte != null) {
//                                Log.e(AllBuyRice.this.TAG, "----------ThreadTHJ---------send_start------status=" + Tool.bytesToHexString(strByte));
//                            }
//                        }
//                        Log.e(AllBuyRice.this.TAG, "===========ThreadTHJ=========m_zhiling_poll=" + AllBuyRice.this.m_zhiling_poll);
//                        send_poll[0] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(0, 2), 16);
//                        send_poll[1] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(2, 4), 16);
//                        send_poll[2] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(4, 6), 16);
//                        send_poll[3] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(6, 8), 16);
//                        send_poll[4] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(8, 10), 16);
//                        send_poll[5] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(10, 12), 16);
//                        send_poll[6] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(12, 14), 16);
//                        send_poll[7] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(14, 16), 16);
//                        send_poll[8] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(16, 18), 16);
//                        send_poll[9] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(18, 20), 16);
//                        send_poll[10] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(20, 22), 16);
//                        send_poll[11] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(22, 24), 16);
//                        send_poll[12] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(24, AllBuyRice.WX_ZFB_GONE), 16);
//                        send_poll[13] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(AllBuyRice.WX_ZFB_GONE, AllBuyRice.PAYONLINE_NOPAY), 16);
//                        send_poll[14] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(AllBuyRice.PAYONLINE_NOPAY, 30), 16);
//                        send_poll[15] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(30, 32), 16);
//                        send_poll[16] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(32, 34), 16);
//                        send_poll[17] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(34, 36), 16);
//                        send_poll[18] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(36, 38), 16);
//                        send_poll[19] = (byte) Integer.parseInt(AllBuyRice.this.m_zhiling_poll.substring(38, AllBuyRice.PAYONLINE_NOPAY_ZFB), 16);
//                        try {
//                            Thread.sleep(70);
//                        } catch (Exception e2) {
//                            e2.printStackTrace();
//                        }
//                        AllBuyRice.this.m_tmc.send(AllBuyRice.this.openTHJ, send_poll);
//                        Log.e(AllBuyRice.this.TAG, "----------ThreadTHJ---------send_poll------send_poll=" + AllBuyRice.this.m_zhiling_poll);
//                        try {
//                            Thread.sleep(70);
//                        } catch (Exception ex2) {
//                            ex2.printStackTrace();
//                        }
//                        strByte = AllBuyRice.this.m_tmc.receiver(AllBuyRice.this.openTHJ, 1000);
//                        if (strByte != null) {
//                            String status = Tool.bytesToHexString(strByte);
//                            Log.e(AllBuyRice.this.TAG, "----------ThreadTHJ---------send_poll------status=" + status);
//                            try {
//                                if (status.length() == AllBuyRice.PAYONLINE_NOPAY_ZFB) {
//                                    Message msg;
//                                    if (status.substring(2, 4).equals("03") && status.substring(4, 6).equals("02") && status.substring(8, 10).equals("00")) {
//                                        AllBuyRice.this.isTHJThread = Boolean.valueOf(false);
//                                        StartFlag = 0;
//                                        msg = new Message();
//                                        msg.what = AllBuyRice.STARTMOTOR_OVER;
//                                        AllBuyRice.this.handlerAddress.sendMessage(msg);
//                                    } else if (status.substring(2, 4).equals("03") && status.substring(4, 6).equals("02") && !status.substring(8, 10).equals("00")) {
//                                        msg = new Message();
//                                        msg.what = AllBuyRice.BAOYUNYING_THJ;
//                                        AllBuyRice.this.handlerAddress.sendMessage(msg);
//                                        AllBuyRice.this.isTHJThread = Boolean.valueOf(false);
//                                        StartFlag = 0;
//                                    }
//                                }
//                            } catch (Exception ex22) {
//                                ex22.printStackTrace();
//                                AllBuyRice.this.isTHJThread = Boolean.valueOf(false);
//                                StartFlag = 0;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    static {
//        System.loadLibrary("tmc");
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all);
//        try {
//            this.mImageDownLoader = new ImageDownLoader(getApplicationContext());
//            this.m_tmc = new tmc();
//            initAl();
//            initView();
//            initSeleView();
//            if (this.m_threadMachineStatus == null) {
//                this.m_threadMachineStatus = new ThreadMachineStatus();
//                this.m_threadMachineStatus.start();
//            }
//            Log.e(this.TAG, "onCreate");
//        } catch (Exception ex) {
//            insertErrorlog("onCreate", ex);
//            writeErrorLog("onCreate", ex);
//        }
//    }
//
//    private void initAl() {
//        this.al_select = (AbsoluteLayout) findViewById(R.id.al_select);
//        this.al_payselect = (AbsoluteLayout) findViewById(R.id.al_payselect);
//        this.al_nocardbuy = (AbsoluteLayout) findViewById(R.id.al_nocardbuy);
//        this.al_jyts = (AbsoluteLayout) findViewById(R.id.al_jyts);
//    }
//
//    private void initView() {
//        this.tv_sm_cpmc = (TextView) findViewById(R.id.tv_sm_cpmc);
//        this.tv_sm_gmsl = (TextView) findViewById(R.id.tv_sm_gmsl);
//        this.tv_sm_cpjg = (TextView) findViewById(R.id.tv_sm_cpjg);
//        this.tv_sm_cpid = (TextView) findViewById(R.id.tv_sm_cpid);
//        this.tv_sm_hdbh = (TextView) findViewById(R.id.tv_sm_hdbh);
//        this.tv_sm_pic = (TextView) findViewById(R.id.tv_sm_pic);
//        this.tv_dhx_cpmc = (TextView) findViewById(R.id.tv_dhx_cpmc);
//        this.tv_dhx_gmsl = (TextView) findViewById(R.id.tv_dhx_gmsl);
//        this.tv_dhx_cpjg = (TextView) findViewById(R.id.tv_dhx_cpjg);
//        this.tv_dhx_cpid = (TextView) findViewById(R.id.tv_dhx_cpid);
//        this.tv_dhx_hdbh = (TextView) findViewById(R.id.tv_dhx_hdbh);
//        this.tv_dhx_pic = (TextView) findViewById(R.id.tv_dhx_pic);
//        this.tv_clx_cpmc = (TextView) findViewById(R.id.tv_clx_cpmc);
//        this.tv_clx_gmsl = (TextView) findViewById(R.id.tv_clx_gmsl);
//        this.tv_clx_cpjg = (TextView) findViewById(R.id.tv_clx_cpjg);
//        this.tv_clx_cpid = (TextView) findViewById(R.id.tv_clx_cpid);
//        this.tv_clx_hdbh = (TextView) findViewById(R.id.tv_clx_hdbh);
//        this.tv_clx_pic = (TextView) findViewById(R.id.tv_clx_pic);
//        this.tv_a4_cpmc = (TextView) findViewById(R.id.tv_a4_cpmc);
//        this.tv_a4_gmsl = (TextView) findViewById(R.id.tv_a4_gmsl);
//        this.tv_a4_cpjg = (TextView) findViewById(R.id.tv_a4_cpjg);
//        this.tv_a4_cpid = (TextView) findViewById(R.id.tv_a4_cpid);
//        this.tv_a4_hdbh = (TextView) findViewById(R.id.tv_a4_hdbh);
//        this.tv_a4_pic = (TextView) findViewById(R.id.tv_a4_pic);
//        this.tv_a5_cpmc = (TextView) findViewById(R.id.tv_a5_cpmc);
//        this.tv_a5_gmsl = (TextView) findViewById(R.id.tv_a5_gmsl);
//        this.tv_a5_cpjg = (TextView) findViewById(R.id.tv_a5_cpjg);
//        this.tv_a5_cpid = (TextView) findViewById(R.id.tv_a5_cpid);
//        this.tv_a5_hdbh = (TextView) findViewById(R.id.tv_a5_hdbh);
//        this.tv_a5_pic = (TextView) findViewById(R.id.tv_a5_pic);
//        this.tv_a6_cpmc = (TextView) findViewById(R.id.tv_a6_cpmc);
//        this.tv_a6_gmsl = (TextView) findViewById(R.id.tv_a6_gmsl);
//        this.tv_a6_cpjg = (TextView) findViewById(R.id.tv_a6_cpjg);
//        this.tv_a6_cpid = (TextView) findViewById(R.id.tv_a6_cpid);
//        this.tv_a6_hdbh = (TextView) findViewById(R.id.tv_a6_hdbh);
//        this.tv_a6_pic = (TextView) findViewById(R.id.tv_a6_pic);
//        this.tv_a7_cpmc = (TextView) findViewById(R.id.tv_a7_cpmc);
//        this.tv_a7_gmsl = (TextView) findViewById(R.id.tv_a7_gmsl);
//        this.tv_a7_cpjg = (TextView) findViewById(R.id.tv_a7_cpjg);
//        this.tv_a7_cpid = (TextView) findViewById(R.id.tv_a7_cpid);
//        this.tv_a7_hdbh = (TextView) findViewById(R.id.tv_a7_hdbh);
//        this.tv_a7_pic = (TextView) findViewById(R.id.tv_a7_pic);
//        this.tv_a8_cpmc = (TextView) findViewById(R.id.tv_a8_cpmc);
//        this.tv_a8_gmsl = (TextView) findViewById(R.id.tv_a8_gmsl);
//        this.tv_a8_cpjg = (TextView) findViewById(R.id.tv_a8_cpjg);
//        this.tv_a8_cpid = (TextView) findViewById(R.id.tv_a8_cpid);
//        this.tv_a8_hdbh = (TextView) findViewById(R.id.tv_a8_hdbh);
//        this.tv_a8_pic = (TextView) findViewById(R.id.tv_a8_pic);
//        this.tv_a9_cpmc = (TextView) findViewById(R.id.tv_a9_cpmc);
//        this.tv_a9_gmsl = (TextView) findViewById(R.id.tv_a9_gmsl);
//        this.tv_a9_cpjg = (TextView) findViewById(R.id.tv_a9_cpjg);
//        this.tv_a9_cpid = (TextView) findViewById(R.id.tv_a9_cpid);
//        this.tv_a9_hdbh = (TextView) findViewById(R.id.tv_a9_hdbh);
//        this.tv_a9_pic = (TextView) findViewById(R.id.tv_a9_pic);
//        this.tv_a10_cpmc = (TextView) findViewById(R.id.tv_a10_cpmc);
//        this.tv_a10_gmsl = (TextView) findViewById(R.id.tv_a10_gmsl);
//        this.tv_a10_cpjg = (TextView) findViewById(R.id.tv_a10_cpjg);
//        this.tv_a10_cpid = (TextView) findViewById(R.id.tv_a10_cpid);
//        this.tv_a10_hdbh = (TextView) findViewById(R.id.tv_a10_hdbh);
//        this.tv_a10_pic = (TextView) findViewById(R.id.tv_a10_pic);
//        this.tv_a11_cpmc = (TextView) findViewById(R.id.tv_a11_cpmc);
//        this.tv_a11_gmsl = (TextView) findViewById(R.id.tv_a11_gmsl);
//        this.tv_a11_cpjg = (TextView) findViewById(R.id.tv_a11_cpjg);
//        this.tv_a11_cpid = (TextView) findViewById(R.id.tv_a11_cpid);
//        this.tv_a11_hdbh = (TextView) findViewById(R.id.tv_a11_hdbh);
//        this.tv_a11_pic = (TextView) findViewById(R.id.tv_a11_pic);
//        this.tv_a12_cpmc = (TextView) findViewById(R.id.tv_a12_cpmc);
//        this.tv_a12_gmsl = (TextView) findViewById(R.id.tv_a12_gmsl);
//        this.tv_a12_cpjg = (TextView) findViewById(R.id.tv_a12_cpjg);
//        this.tv_a12_cpid = (TextView) findViewById(R.id.tv_a12_cpid);
//        this.tv_a12_hdbh = (TextView) findViewById(R.id.tv_a12_hdbh);
//        this.tv_a12_pic = (TextView) findViewById(R.id.tv_a12_pic);
//        this.iv_sm = (ImageView) findViewById(R.id.iv_sm);
//        this.iv_dhx = (ImageView) findViewById(R.id.iv_dhx);
//        this.iv_clx = (ImageView) findViewById(R.id.iv_clx);
//        this.iv_a4 = (ImageView) findViewById(R.id.iv_a4);
//        this.iv_a5 = (ImageView) findViewById(R.id.iv_a5);
//        this.iv_a6 = (ImageView) findViewById(R.id.iv_a6);
//        this.iv_a7 = (ImageView) findViewById(R.id.iv_a7);
//        this.iv_a8 = (ImageView) findViewById(R.id.iv_a8);
//        this.iv_a9 = (ImageView) findViewById(R.id.iv_a9);
//        this.iv_a10 = (ImageView) findViewById(R.id.iv_a10);
//        this.iv_a11 = (ImageView) findViewById(R.id.iv_a11);
//        this.iv_a12 = (ImageView) findViewById(R.id.iv_a12);
//        this.iv_ricetp = (ImageView) findViewById(R.id.iv_ricetp);
//        this.iv_weixinxz = (ImageView) findViewById(R.id.iv_weixinxz);
//        this.iv_zfbxz = (ImageView) findViewById(R.id.iv_zfbxz);
//        this.tv_cpmc = (TextView) findViewById(R.id.tv_cpmc);
//        this.tv_gmsl = (TextView) findViewById(R.id.tv_gmsl);
//        this.tv_cpjg = (TextView) findViewById(R.id.tv_cpjg);
//        this.tv_zftxt = (TextView) findViewById(R.id.tv_zftxt);
//        this.iv_zfewm = (ImageView) findViewById(R.id.iv_zfewm);
//        this.iv_qxzf = (ImageView) findViewById(R.id.iv_qxzf);
//        this.tv_jyts = (TextView) findViewById(R.id.tv_jyts);
//        this.tv_jyts2 = (TextView) findViewById(R.id.tv_jyts2);
//        this.tv_jyts3 = (TextView) findViewById(R.id.tv_jyts3);
//    }
//
//    private void initSeleView() {
//        GetRiceInfo();
//    }
//
//    private void ClearSelectView() {
//        this.m_GetRiceInfoRet = "";
//        this.tv_sm_cpid.setText("");
//        this.tv_sm_cpmc.setText("");
//        this.tv_sm_cpjg.setText("");
//        this.tv_sm_hdbh.setText("");
//        this.tv_sm_gmsl.setText("");
//        this.tv_sm_pic.setText("");
//        this.tv_dhx_cpid.setText("");
//        this.tv_dhx_cpmc.setText("");
//        this.tv_dhx_cpjg.setText("");
//        this.tv_dhx_hdbh.setText("");
//        this.tv_dhx_gmsl.setText("");
//        this.tv_dhx_pic.setText("");
//        this.tv_clx_cpid.setText("");
//        this.tv_clx_cpmc.setText("");
//        this.tv_clx_cpjg.setText("");
//        this.tv_clx_hdbh.setText("");
//        this.tv_clx_gmsl.setText("");
//        this.tv_clx_pic.setText("");
//        this.tv_a4_cpid.setText("");
//        this.tv_a4_cpmc.setText("");
//        this.tv_a4_cpjg.setText("");
//        this.tv_a4_hdbh.setText("");
//        this.tv_a4_gmsl.setText("");
//        this.tv_a4_pic.setText("");
//        this.tv_a5_cpid.setText("");
//        this.tv_a5_cpmc.setText("");
//        this.tv_a5_cpjg.setText("");
//        this.tv_a5_hdbh.setText("");
//        this.tv_a5_gmsl.setText("");
//        this.tv_a5_pic.setText("");
//        this.tv_a6_cpid.setText("");
//        this.tv_a6_cpmc.setText("");
//        this.tv_a6_cpjg.setText("");
//        this.tv_a6_hdbh.setText("");
//        this.tv_a6_gmsl.setText("");
//        this.tv_a6_pic.setText("");
//        this.tv_a7_cpid.setText("");
//        this.tv_a7_cpmc.setText("");
//        this.tv_a7_cpjg.setText("");
//        this.tv_a7_hdbh.setText("");
//        this.tv_a7_gmsl.setText("");
//        this.tv_a7_pic.setText("");
//        this.tv_a8_cpid.setText("");
//        this.tv_a8_cpmc.setText("");
//        this.tv_a8_cpjg.setText("");
//        this.tv_a8_hdbh.setText("");
//        this.tv_a8_gmsl.setText("");
//        this.tv_a8_pic.setText("");
//        this.tv_a9_cpid.setText("");
//        this.tv_a9_cpmc.setText("");
//        this.tv_a9_cpjg.setText("");
//        this.tv_a9_hdbh.setText("");
//        this.tv_a9_gmsl.setText("");
//        this.tv_a9_pic.setText("");
//        this.tv_a10_cpid.setText("");
//        this.tv_a10_cpmc.setText("");
//        this.tv_a10_cpjg.setText("");
//        this.tv_a10_hdbh.setText("");
//        this.tv_a10_gmsl.setText("");
//        this.tv_a10_pic.setText("");
//        this.tv_a11_cpid.setText("");
//        this.tv_a11_cpmc.setText("");
//        this.tv_a11_cpjg.setText("");
//        this.tv_a11_hdbh.setText("");
//        this.tv_a11_gmsl.setText("");
//        this.tv_a11_pic.setText("");
//        this.tv_a12_cpid.setText("");
//        this.tv_a12_cpmc.setText("");
//        this.tv_a12_cpjg.setText("");
//        this.tv_a12_hdbh.setText("");
//        this.tv_a12_gmsl.setText("");
//        this.tv_a12_pic.setText("");
//        this.iv_sm.setVisibility(8);
//        this.iv_dhx.setVisibility(8);
//        this.iv_clx.setVisibility(8);
//        this.iv_a4.setVisibility(8);
//        this.iv_a5.setVisibility(8);
//        this.iv_a6.setVisibility(8);
//        this.iv_a7.setVisibility(8);
//        this.iv_a8.setVisibility(8);
//        this.iv_a9.setVisibility(8);
//        this.iv_a10.setVisibility(8);
//        this.iv_a11.setVisibility(8);
//        this.iv_a12.setVisibility(8);
//    }
//
//    private void getImageView1(String str) {
//        this.iv_sm.setVisibility(0);
//        this.m_TotalPage = Integer.parseInt(str.split("\\,")[4].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_sm_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_sm_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_sm_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_sm_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_sm_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_sm_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_sm != null && bitmap != null) {
//                    AllBuyRice.this.iv_sm.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_sm.setImageBitmap(bitmap);
//        } else {
//            this.iv_sm.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView2(String str) {
//        this.iv_dhx.setVisibility(0);
//        this.tv_dhx_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_dhx_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_dhx_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_dhx_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_dhx_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_dhx_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_dhx != null && bitmap != null) {
//                    AllBuyRice.this.iv_dhx.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_dhx.setImageBitmap(bitmap);
//        } else {
//            this.iv_dhx.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView3(String str) {
//        this.iv_clx.setVisibility(0);
//        this.tv_clx_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_clx_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_clx_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_clx_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_clx_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_clx_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_clx != null && bitmap != null) {
//                    AllBuyRice.this.iv_clx.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_clx.setImageBitmap(bitmap);
//        } else {
//            this.iv_clx.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView4(String str) {
//        this.iv_a4.setVisibility(0);
//        this.tv_a4_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a4_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a4_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a4_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a4_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a4_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a4 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a4.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a4.setImageBitmap(bitmap);
//        } else {
//            this.iv_a4.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView5(String str) {
//        this.iv_a5.setVisibility(0);
//        this.tv_a5_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a5_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a5_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a5_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a5_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a5_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a5 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a5.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a5.setImageBitmap(bitmap);
//        } else {
//            this.iv_a5.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView6(String str) {
//        this.iv_a6.setVisibility(0);
//        this.tv_a6_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a6_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a6_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a6_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a6_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a6_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a6 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a6.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a6.setImageBitmap(bitmap);
//        } else {
//            this.iv_a6.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView7(String str) {
//        this.iv_a7.setVisibility(0);
//        this.tv_a7_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a7_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a7_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a7_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a7_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a7_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a7 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a7.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a7.setImageBitmap(bitmap);
//        } else {
//            this.iv_a7.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView8(String str) {
//        this.iv_a8.setVisibility(0);
//        this.tv_a8_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a8_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a8_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a8_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a8_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a8_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a8 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a8.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a8.setImageBitmap(bitmap);
//        } else {
//            this.iv_a8.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView9(String str) {
//        this.iv_a9.setVisibility(0);
//        this.tv_a9_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a9_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a9_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a9_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a9_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a9_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a9 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a9.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a9.setImageBitmap(bitmap);
//        } else {
//            this.iv_a9.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView10(String str) {
//        this.iv_a10.setVisibility(0);
//        this.tv_a10_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a10_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a10_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a10_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a10_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a10_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a10 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a10.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a10.setImageBitmap(bitmap);
//        } else {
//            this.iv_a10.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView11(String str) {
//        this.iv_a11.setVisibility(0);
//        this.tv_a11_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a11_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a11_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a11_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a11_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a11_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a11 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a11.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a11.setImageBitmap(bitmap);
//        } else {
//            this.iv_a11.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    private void getImageView12(String str) {
//        this.iv_a12.setVisibility(0);
//        this.tv_a12_cpid.setText(str.split("\\,")[1].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a12_cpmc.setText(ConvertUtils.convert(str.split("\\,")[2].split("\\:")[1]).replaceAll("\"", ""));
//        this.tv_a12_cpjg.setText("单价:" + str.split("\\,")[3].split("\\:")[1].replaceAll("\"", "") + "元");
//        this.tv_a12_hdbh.setText("货道编号:" + str.split("\\,")[5].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a12_gmsl.setText("库存数量:" + str.split("\\,")[6].split("\\:")[1].replaceAll("\"", ""));
//        this.tv_a12_pic.setText(str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""));
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + str.split("\\,")[7].split("\\:")[1].replaceAll("\"", ""), new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_a12 != null && bitmap != null) {
//                    AllBuyRice.this.iv_a12.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_a12.setImageBitmap(bitmap);
//        } else {
//            this.iv_a12.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//    }
//
//    public void on_iv_previouspage(View v) {
//        try {
//            if (this.m_PageNO > 1) {
//                this.m_PageNO--;
//            }
//            ClearSelectView();
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_previouspage", ex);
//            writeErrorLog("on_iv_previouspage", ex);
//        }
//    }
//
//    public void on_iv_nextpage(View v) {
//        try {
//            if (this.m_PageNO < this.m_TotalPage) {
//                this.m_PageNO++;
//            }
//            GetRiceInfo();
//            ClearSelectView();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_nextpage", ex);
//            writeErrorLog("on_iv_nextpage", ex);
//        }
//    }
//
//    public void on_iv_a1(View v) {
//        try {
//            String[] arrayhdbh = this.tv_sm_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_sm_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_sm_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a1", ex);
//            writeErrorLog("on_iv_a1", ex);
//        }
//    }
//
//    public void on_iv_a2(View v) {
//        try {
//            String[] arrayhdbh = this.tv_dhx_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_dhx_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_dhx_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a2", ex);
//            writeErrorLog("on_iv_a2", ex);
//        }
//    }
//
//    public void on_iv_a3(View v) {
//        try {
//            String[] arrayhdbh = this.tv_clx_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_clx_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_clx_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a3", ex);
//            writeErrorLog("on_iv_a3", ex);
//        }
//    }
//
//    public void on_iv_a4(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a4_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a4_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a4_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a4", ex);
//            writeErrorLog("on_iv_a4", ex);
//        }
//    }
//
//    public void on_iv_a5(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a5_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a5_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a5_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a5", ex);
//            writeErrorLog("on_iv_a5", ex);
//        }
//    }
//
//    public void on_iv_a6(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a6_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a6_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a6_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a6", ex);
//            writeErrorLog("on_iv_a6", ex);
//        }
//    }
//
//    public void on_iv_a7(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a7_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a7_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a7_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a7", ex);
//            writeErrorLog("on_iv_a7", ex);
//        }
//    }
//
//    public void on_iv_a8(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a8_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a8_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a8_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a8", ex);
//            writeErrorLog("on_iv_a8", ex);
//        }
//    }
//
//    public void on_iv_a9(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a9_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a9_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a9_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a9", ex);
//            writeErrorLog("on_iv_a9", ex);
//        }
//    }
//
//    public void on_iv_a10(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a10_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a10_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a10_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a10", ex);
//            writeErrorLog("on_iv_a10", ex);
//        }
//    }
//
//    public void on_iv_a11(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a11_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a11_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a11_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a11", ex);
//            writeErrorLog("on_iv_a11", ex);
//        }
//    }
//
//    public void on_iv_a12(View v) {
//        try {
//            String[] arrayhdbh = this.tv_a12_hdbh.getText().toString().split("\\:");
//            if (arrayhdbh.length > 1) {
//                this.m_RiceID = this.tv_a12_cpid.getText().toString();
//                this.m_HDBH = arrayhdbh[1];
//                this.m_PicUrl = this.tv_a12_pic.getText().toString();
//                getorderid();
//                return;
//            }
//            GetRiceInfo();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_a12", ex);
//            writeErrorLog("on_iv_a12", ex);
//        }
//    }
//
//    private void getorderid() {
//        try {
//            SendBuyRice(DeviceUtils.getuniqueId(), this.m_RiceID, this.m_HDBH);
//            for (int i = 0; i < 10 && this.m_NoCardOrderID.equals(""); i++) {
//                try {
//                    Thread.sleep(100);
//                } catch (Exception e) {
//                }
//                Log.e(this.TAG, "getorderid");
//            }
//            if (this.m_NoCardOrderID.equals("")) {
//                Toast toast = Toast.makeText(getApplicationContext(), "网络连接失败!", 0);
//                toast.setGravity(17, 0, 0);
//                toast.show();
//                return;
//            }
//            all_gone();
//            this.al_payselect.setVisibility(0);
//            initPaySele();
//            ClearSelectView();
//        } catch (Exception ex) {
//            insertErrorlog("getorderid", ex);
//            writeErrorLog("getorderid", ex);
//        }
//    }
//
//    public void GetRiceInfo() {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("machineNo", DeviceUtils.getuniqueId());
//            map.put("pageno", String.valueOf(this.m_PageNO));
//            map.put("limitrow", "12");
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_jicunbiao_select_page2", map) {
//                protected void onRequestSucceed(String ret) {
//                    AllBuyRice.this.m_GetRiceInfoRet = ret;
//                    Message msg = new Message();
//                    msg.what = 1;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                    Log.d(AllBuyRice.this.TAG, "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetRiceInfo_net", ex);
//            writeErrorLog("GetRiceInfo_net", ex);
//        }
//    }
//
//    public void SendBuyRice(String machineno, String RiceID, String hdbh) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("id", RiceID);
//            map.put("num", "1");
//            map.put("jiqihao", machineno);
//            map.put("Huodao", hdbh);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_orders_insert", map) {
//                protected void onRequestSucceed(String ret) {
//                    AllBuyRice.this.m_NoCardOrderID = ret;
//                    Log.d(AllBuyRice.this.TAG, "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("SendBuyRice_net", ex);
//            writeErrorLog("SendBuyRice_net", ex);
//        }
//    }
//
//    private void initPaySele() {
//        GetProductInfo(this.m_RiceID, this.m_HDBH);
//        Bitmap bitmap = this.mImageDownLoader.downloadImage(GlobalContants.GetGoodsUrl + this.m_PicUrl, new onImageLoaderListener() {
//            public void onImageLoader(Bitmap bitmap, String url) {
//                if (AllBuyRice.this.iv_ricetp != null && bitmap != null) {
//                    AllBuyRice.this.iv_ricetp.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap != null) {
//            this.iv_ricetp.setImageBitmap(bitmap);
//        } else {
//            this.iv_ricetp.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
//        }
//        this.iv_weixinxz.setImageDrawable(getResources().getDrawable(R.drawable.weixin_s));
//        this.iv_zfbxz.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao));
//        this.payType = 1;
//    }
//
//    private void clearPaySele() {
//        this.tv_cpmc.setText("品名:");
//        this.tv_gmsl.setText("购买数量:");
//        this.tv_cpjg.setText("价格:");
//    }
//
//    public void on_iv_weixinxz(View v) {
//        try {
//            this.iv_weixinxz.setImageDrawable(getResources().getDrawable(R.drawable.weixin_s));
//            this.iv_zfbxz.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao));
//            this.payType = 1;
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_weixinxz", ex);
//            writeErrorLog("on_iv_weixinxz", ex);
//        }
//    }
//
//    public void on_iv_zfbxz(View v) {
//        try {
//            this.iv_zfbxz.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao_s));
//            this.iv_weixinxz.setImageDrawable(getResources().getDrawable(R.drawable.weixin));
//            this.payType = 2;
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_zfbxz", ex);
//            writeErrorLog("on_iv_zfbxz", ex);
//        }
//    }
//
//    public void on_iv_pay_qrzf(View v) {
//        try {
//            Message msg;
//            if (2 == this.payType) {
//                msg = new Message();
//                msg.what = 3;
//                this.handlerAddress.sendMessage(msg);
//                this.al_payselect.setVisibility(8);
//                this.al_nocardbuy.setVisibility(0);
//                this.iv_qxzf.setVisibility(0);
//                this.tv_zftxt.setText(" 支付宝支付");
//                initBuyOnLine();
//                this.isPayOnlineStart = Boolean.valueOf(true);
//            }
//            if (1 == this.payType) {
//                msg = new Message();
//                msg.what = 5;
//                this.handlerAddress.sendMessage(msg);
//                this.al_payselect.setVisibility(8);
//                this.al_nocardbuy.setVisibility(0);
//                this.iv_qxzf.setVisibility(0);
//                this.tv_zftxt.setText("  微信支付");
//                initBuyOnLine();
//                this.isPayOnlineStart = Boolean.valueOf(true);
//            }
//            clearPaySele();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_pay_qrzf", ex);
//            writeErrorLog("on_iv_pay_qrzf", ex);
//        }
//    }
//
//    private void initTimer_one() {
//        if (this.m_Timer_one == null) {
//            this.m_Timer_one = new Timer();
//        }
//    }
//
//    private void initTimer_two() {
//        if (this.m_Timer_two == null) {
//            this.m_Timer_two = new Timer();
//        }
//    }
//
//    private void stopTimer_one() {
//        if (this.m_Timer_one != null) {
//            this.m_Timer_one.cancel();
//            this.m_Timer_one = null;
//        }
//        if (this.m_TimerTask_one != null) {
//            this.m_TimerTask_one.cancel();
//            this.m_TimerTask_one = null;
//        }
//    }
//
//    private void stopTimer_two() {
//        if (this.m_Timer_two != null) {
//            this.m_Timer_two.cancel();
//            this.m_Timer_two = null;
//        }
//        if (this.m_TimerTask_two != null) {
//            this.m_TimerTask_two.cancel();
//            this.m_TimerTask_two = null;
//        }
//    }
//
//    private void initTimer_three() {
//        if (this.m_Timer_three == null) {
//            this.m_Timer_three = new Timer();
//        }
//    }
//
//    private void initTimer_four() {
//        if (this.m_Timer_four == null) {
//            this.m_Timer_four = new Timer();
//        }
//    }
//
//    private void stopTimer_three() {
//        if (this.m_Timer_three != null) {
//            this.m_Timer_three.cancel();
//            this.m_Timer_three = null;
//        }
//        if (this.m_TimerTask_three != null) {
//            this.m_TimerTask_three.cancel();
//            this.m_TimerTask_three = null;
//        }
//    }
//
//    private void stopTimer_four() {
//        if (this.m_Timer_four != null) {
//            this.m_Timer_four.cancel();
//            this.m_Timer_four = null;
//        }
//        if (this.m_TimerTask_four != null) {
//            this.m_TimerTask_four.cancel();
//            this.m_TimerTask_four = null;
//        }
//    }
//
//    private void scheduleTimer_one(long delay, long period) {
//        if (this.m_Timer_one != null && this.m_TimerTask_one != null) {
//            this.m_Timer_one.schedule(this.m_TimerTask_one, delay, period);
//        }
//    }
//
//    private void scheduleTimer_two(long delay) {
//        if (this.m_Timer_two != null && this.m_TimerTask_two != null) {
//            this.m_Timer_two.schedule(this.m_TimerTask_two, delay);
//        }
//    }
//
//    private void scheduleTimer_four(long delay) {
//        if (this.m_Timer_four != null && this.m_TimerTask_four != null) {
//            this.m_Timer_four.schedule(this.m_TimerTask_four, delay);
//        }
//    }
//
//    public void on_iv_pay_qxgm(View v) {
//        try {
//            backto_selectrice();
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_pay_qxgm", ex);
//            writeErrorLog("on_iv_pay_qxgm", ex);
//        }
//    }
//
//    private void backto_selectrice() {
//        try {
//            ClearAll();
//            all_gone();
//            this.al_select.setVisibility(0);
//            initSeleView();
//        } catch (Exception ex) {
//            insertErrorlog("backto_selectrice", ex);
//            writeErrorLog("backto_selectrice", ex);
//        }
//    }
//
//    private void initBuyOnLine() {
//        if (this.m_PayOnlineOrder == null) {
//            this.m_PayOnlineOrder = new ThreadOrder();
//            this.m_PayOnlineOrder.start();
//        }
//    }
//
//    public void ClearPayOnline(Boolean goOn) {
//        this.isPayOnlineStart = Boolean.valueOf(false);
//        this.m_PayOnlinezfbRet = "";
//        this.iv_zfewm.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao));
//        if (goOn.booleanValue()) {
//            this.m_GoodName = "";
//            this.m_BuyNum = "";
//            this.m_GoodPrice = "";
//            this.m_RiceID = "";
//            this.m_HDBH = "";
//            this.m_PicUrl = "";
//            this.m_NoCardOrderID = "";
//            this.m_zhiling = "";
//            this.m_zhiling_poll = "";
//            this.m_zhiling_check = "";
//        }
//    }
//
//    private void al_nocardbuy_gone() {
//        stopTimer_two();
//        initTimer_two();
//        if (this.m_TimerTask_two == null) {
//            this.m_TimerTask_two = new TimerTask() {
//                public void run() {
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.WX_ZFB_GONE;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                    AllBuyRice.this.stopTimer_two();
//                }
//            };
//        }
//        scheduleTimer_two(10000);
//    }
//
//    public void on_iv_qxzf(View v) {
//        try {
//            all_gone();
//            this.al_select.setVisibility(0);
//            initSeleView();
//            ClearPayOnline(Boolean.valueOf(true));
//        } catch (Exception ex) {
//            insertErrorlog("on_iv_qxzf", ex);
//            writeErrorLog("on_iv_qxzf", ex);
//        }
//    }
//
//    public void GetProductInfo(String RiceId, String hdbh) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("GoodID", RiceId);
//            map.put("machineNo", DeviceUtils.getuniqueId());
//            map.put("Huodao", hdbh);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_jicunbiao_select2", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d(AllBuyRice.this.TAG, "onRequestSucceed >>>>>>>>>>>");
//                    if (ret.indexOf("[{") >= 0 && ret.indexOf("}]") >= 0) {
//                        String[] _array = ret.split("\\,");
//                        AllBuyRice.this.m_GoodName = ConvertUtils.convert(_array[2].split("\\:")[1]).replaceAll("\"", "");
//                        AllBuyRice.this.m_BuyNum = "1";
//                        AllBuyRice.this.m_GoodPrice = _array[3].split("\\:")[1].replaceAll("\"", "");
//                        Message msg = new Message();
//                        msg.what = 2;
//                        AllBuyRice.this.handlerAddress.sendMessage(msg);
//                    }
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "GetProductInfo ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetProductInfo_net", ex);
//            writeErrorLog("GetProductInfo_net", ex);
//        }
//    }
//
//    public void GetWeiXinCode(String Body, String Out_trade_no, String Total_fee, String productid) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("Body", Body);
//            map.put("Out_trade_no", Out_trade_no);
//            map.put("Total_fee", Total_fee);
//            map.put("productid", productid);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/Wxpay/native", map) {
//                protected void onRequestSucceed(String ret) {
//                    AllBuyRice.this.m_PayOnlinebitmap = AllBuyRice.this.getHttpBitmap("http://paysdk.weixin.qq.com/example/qrcode.php?data=" + ret);
//                    Log.d(AllBuyRice.this.TAG, "GetWeiXinCode >>>>>>>>>>>:=http://paysdk.weixin.qq.com/example/qrcode.php?data=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "GetWeiXinCode ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetWeiXinCode_net", ex);
//            writeErrorLog("GetWeiXinCode_net", ex);
//        }
//    }
//
//    public void GetZFBUrl(String Body, String Out_trade_no, String Total_fee) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("subject", Body);
//            map.put("out_trade_no", Out_trade_no);
//            map.put("total_amount", Total_fee);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/Alipay/alipay", map) {
//                protected void onRequestSucceed(String ret) {
//                    AllBuyRice.this.m_PayOnlinezfbRet = ret;
//                    Log.d(AllBuyRice.this.TAG, "GetZFBUrl >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "GetZFBUrl ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetZFBUrl_net", ex);
//            writeErrorLog("GetZFBUrl_net", ex);
//        }
//    }
//
//    public Bitmap getHttpBitmap(String url) {
//        Bitmap bitmap = null;
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//            conn.setConnectTimeout(3000);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//            conn.disconnect();
//            return bitmap;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("getHttpBitmap_net", ex);
//            writeErrorLog("getHttpBitmap_net", ex);
//            return bitmap;
//        }
//    }
//
//    public void createQRImage(ImageView sweepIV, String url) {
//        if (url != null) {
//            try {
//                if (!"".equals(url) && url.length() >= 1) {
//                    Hashtable<EncodeHintType, String> hints = new Hashtable();
//                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//                    BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, this.QR_WIDTH, this.QR_HEIGHT, hints);
//                    int[] pixels = new int[(this.QR_WIDTH * this.QR_HEIGHT)];
//                    for (int y = 0; y < this.QR_HEIGHT; y++) {
//                        for (int x = 0; x < this.QR_WIDTH; x++) {
//                            if (bitMatrix.get(x, y)) {
//                                pixels[(this.QR_WIDTH * y) + x] = ViewCompat.MEASURED_STATE_MASK;
//                            } else {
//                                pixels[(this.QR_WIDTH * y) + x] = -1;
//                            }
//                        }
//                    }
//                    Bitmap bitmap = Bitmap.createBitmap(this.QR_WIDTH, this.QR_HEIGHT, Config.ARGB_8888);
//                    bitmap.setPixels(pixels, 0, this.QR_WIDTH, 0, 0, this.QR_WIDTH, this.QR_HEIGHT);
//                    sweepIV.setImageBitmap(bitmap);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                insertErrorlog("createQRImage_net", ex);
//                writeErrorLog("createQRImage_net", ex);
//            }
//        }
//    }
//
//    public void GetOrderOK(String orderID) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("Out_trade_no", orderID);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_orders_select2", map) {
//                protected void onRequestSucceed(String ret) {
//                    Message msg;
//                    if (ret.equals("fail")) {
//                        AllBuyRice allBuyRice = AllBuyRice.this;
//                        allBuyRice.m_FiveMin = allBuyRice.m_FiveMin + 1;
//                        if (10 == AllBuyRice.this.m_FiveMin || 30 == AllBuyRice.this.m_FiveMin) {
//                            if (2 == AllBuyRice.this.payType) {
//                                AllBuyRice.this.GetOrderOK2(AllBuyRice.this.m_NoCardOrderID);
//                            }
//                            if (1 == AllBuyRice.this.payType) {
//                                AllBuyRice.this.GetOrderOK3(AllBuyRice.this.m_NoCardOrderID);
//                            }
//                        }
//                        if (60 == AllBuyRice.this.m_FiveMin) {
//                            AllBuyRice.this.isPayOnlineStart = Boolean.valueOf(false);
//                            AllBuyRice.this.m_FiveMin = 0;
//                            msg = new Message();
//                            msg.what = AllBuyRice.PAYONLINE_NOPAY_ZFB;
//                            AllBuyRice.this.handlerAddress.sendMessage(msg);
//                        }
//                    } else {
//                        AllBuyRice.this.isPayOnlineStart = Boolean.valueOf(false);
//                        AllBuyRice.this.m_FiveMin = 0;
//                        String[] _array = ret.split("\\,");
//                        msg = new Message();
//                        msg.what = 4;
//                        AllBuyRice.this.handlerAddress.sendMessage(msg);
//                    }
//                    Log.d("GetOrderOK", "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("GetOrderOK", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetOrderOK_net", ex);
//            writeErrorLog("GetOrderOK_net", ex);
//        }
//    }
//
//    public void GetOrderOK2(String orderID) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("Out_trade_no", orderID);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/Alipay/search_order_status", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d("GetOrderOK2_net", "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("GetOrderOK2_net", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetOrderOK2_net", ex);
//            writeErrorLog("GetOrderOK2_net", ex);
//        }
//    }
//
//    public void GetOrderOK3(String orderID) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("out_trade_no", orderID);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/Wxpay/search_status", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d("GetOrderOK3_net", "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("GetOrderOK3_net", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("GetOrderOK3_net", ex);
//            writeErrorLog("GetOrderOK3_net", ex);
//        }
//    }
//
//    public void payonline_nopay_zfb(String orderID, String type) {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("Out_trade_no", orderID);
//            map.put("type", type);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_payonline_nopay_zfb", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d("payonline_nopay_zfb_net", "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("payonline_nopay_zfb_net", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("payonline_nopay_zfb_net", ex);
//            writeErrorLog("payonline_nopay_zfb_net", ex);
//        }
//    }
//
//    public void update_jilu_yun() {
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("machineNo", DeviceUtils.getuniqueId());
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/WeiChao/sp_insert_jilu_machinestatus", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d(AllBuyRice.this.TAG, "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d(AllBuyRice.this.TAG, "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            insertErrorlog("update_jilu_yun", ex);
//            writeErrorLog("update_jilu_yun", ex);
//        }
//    }
//
//    private void FetchGoods() {
//        if (this.m_tmc == null) {
//            this.m_tmc = new tmc();
//        }
//        OpenTanhuangji();
//        this.isTHJThread = Boolean.valueOf(true);
//        if (this.m_threadTHJ == null) {
//            this.m_threadTHJ = new ThreadTHJ();
//            this.m_threadTHJ.start();
//        }
//    }
//
//    private void OpenTanhuangji() {
//        if (-1 == this.openTHJ) {
//            this.openTHJ = this.m_tmc.open("/dev/ttyS1", 9600);
//        }
//    }
//
//    private void ClearAll() {
//        ClearSelectView();
//        clearPaySele();
//        ClearPayOnline(Boolean.valueOf(true));
//    }
//
//    private void all_gone() {
//        this.al_select.setVisibility(8);
//        this.al_nocardbuy.setVisibility(8);
//        this.al_payselect.setVisibility(8);
//        this.al_jyts.setVisibility(8);
//    }
//
//    private void timertwo_waitYback() {
//        stopTimer_two();
//        initTimer_two();
//        if (this.m_TimerTask_two == null) {
//            this.m_TimerTask_two = new TimerTask() {
//                public void run() {
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.BACKTO_MAIN;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                    AllBuyRice.this.stopTimer_two();
//                }
//            };
//        }
//        scheduleTimer_two(8000);
//    }
//
//    private void writeErrorLog(String str, Exception ex) {
//        String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(Long.valueOf(System.currentTimeMillis()));
//        StackTraceElement[] stack = ex.getStackTrace();
//        String message = ex.toString();
//        if (Environment.getExternalStorageState().equals("mounted")) {
//            String path = Environment.getExternalStorageDirectory() + "/errlog/";
//            String fileName = DeviceUtils.getuniqueId() + "-err-" + nowtime + ".log";
//            File dir = new File(path);
//            File file = new File(new StringBuilder(String.valueOf(path)).append(fileName).toString());
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            try {
//                FileWriter fw = new FileWriter(file);
//                fw.write(new StringBuilder(String.valueOf(str)).append("\r\n").toString());
//                fw.write(new StringBuilder(String.valueOf(message)).append("\r\n").toString());
//                for (StackTraceElement stackTraceElement : stack) {
//                    fw.write(new StringBuilder(String.valueOf(stackTraceElement.toString())).append("\r\n").toString());
//                }
//                fw.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            PostErrorLog(fileName, new StringBuilder(String.valueOf(path)).append(fileName).toString());
//        }
//    }
//
//    public void insertErrorlog(String str, Exception ex) {
//        StackTraceElement[] stack = ex.getStackTrace();
//        String message = "";
//        for (StackTraceElement stackTraceElement : stack) {
//            message = new StringBuilder(String.valueOf(message)).append(stackTraceElement.toString().replaceAll(" ", "")).toString();
//        }
//        try {
//            HashMap<String, Object> map = new HashMap();
//            map.put("machineNo", DeviceUtils.getuniqueId());
//            map.put("str1", str);
//            map.put("str2", ex.toString());
//            map.put("str3", message);
//            new AsynHttpTask("http://www.hljyifukeji.com/index.php/Qian/sp_errorlog_insert", map) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d("insertErrorlog", "zyf >>>>>>>>>>>:=" + ret);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("insertErrorlog", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void PostErrorLog(String onlyfilename, String str) {
//        try {
//            Map<String, String> strParams = new HashMap();
//            strParams.put("paramOne", "valueOne");
//            strParams.put("paramTwo", "valueTwo");
//            Map<String, File> files = new HashMap();
//            files.put(onlyfilename, new File(str));
//            new AsynHttpTaskUpload("http://www.hljyifukeji.com/index.php/Qian/javauploadFile", strParams, files) {
//                protected void onRequestSucceed(String ret) {
//                    Log.d("PostErrorLog", "zyf >>>>>>>>>>>:=" + ret);
//                    System.exit(0);
//                }
//
//                protected void onRequestFail() {
//                    Log.d("PostErrorLog", "zyf ############:=");
//                    Message msg = new Message();
//                    msg.what = AllBuyRice.CONNECTTING_FAIL;
//                    AllBuyRice.this.handlerAddress.sendMessage(msg);
//                }
//            }.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void onDestroy() {
//        System.exit(0);
//        super.onDestroy();
//    }
//
//    private void sum_command_start(String hdbh) {
//        int s2 = Integer.parseInt(hdbh.substring(1));
//        byte[] data = new byte[]{(byte) 1, (byte) 5, (byte) (s2 - 1), (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
//        this.m_zhiling = Tool.bytesToHexString(data) + Tool.Make_CRC(data);
//        Log.d("sum_command_start", "m_zhiling=" + this.m_zhiling);
//    }
//
//    private void sum_command_poll(String hdbh) {
//        int s2 = Integer.parseInt(hdbh.substring(1));
//        byte[] data = new byte[]{(byte) 1, (byte) 3, (byte) (s2 - 1), (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
//        this.m_zhiling_poll = Tool.bytesToHexString(data) + Tool.Make_CRC(data);
//        Log.d("sum_command_poll", "m_zhiling_poll=" + this.m_zhiling_poll);
//    }
//}