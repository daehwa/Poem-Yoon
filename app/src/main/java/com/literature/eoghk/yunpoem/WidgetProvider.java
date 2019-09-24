package com.literature.eoghk.yunpoem;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class WidgetProvider extends AppWidgetProvider {
    static String file;
    static String fileList_yun[];
    static File fileList_user[];
    static boolean isYuns = true;


    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 위젯을 갱신할때 호출됨
     *
     * 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        try {
            fileList_yun = context.getResources().getAssets().list("poem");
            File files = new File(context.getFilesDir().getAbsolutePath());
            fileList_user=files.listFiles();
            Random r = new Random();
            int j = r.nextInt(fileList_yun.length + fileList_user.length);
            if(j < fileList_yun.length)
                isYuns = true;
            else
                isYuns = false;
            if(isYuns)
                file = fileList_yun[j];
            else
                file = fileList_user[j-fileList_yun.length].toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    /**
     * 위젯이 처음 생성될때 호출됨
     *
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨
     *
     * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    public static void updateAppWidget(Context context,
                                       AppWidgetManager appWidgetManager, int appWidgetId) {
        /**
         * 시 읽어오기
         */

        Random r = new Random();
        int j = r.nextInt(fileList_yun.length + fileList_user.length);
        if(j < fileList_yun.length)
            isYuns = true;
        else
            isYuns = false;
        if(isYuns)
            file = fileList_yun[j];
        else
            file = fileList_user[j-fileList_yun.length].toString();

        String poemText = "",poemWriter= null,poemTitle= null;
        String strTemp="";

        InputStream is = null;
        FileInputStream is_ = null;
        BufferedReader bIn = null;
        int current=0;
        try {
            if(isYuns) {
                is = context.getResources().getAssets().open("poem/" + file);
                bIn=new BufferedReader(new InputStreamReader(is));
            }
            else {
                is_ = new FileInputStream (new File(file));
                bIn=new BufferedReader(new InputStreamReader(is_));
            }
            while((strTemp=bIn.readLine())!=null) {
                if(current==0) {
                    poemTitle=strTemp;
                    current++;
                }
                else if(current==1){
                    poemWriter=strTemp;
                    current++;
                }
                else    poemText += strTemp + "\r\n";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * RemoteViews를 이용해 Text설정
         */
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        updateViews.setTextViewText(R.id.mTitle,poemTitle);
        updateViews.setTextViewText(R.id.writer, "                           "+poemWriter);
        updateViews.setTextViewText(R.id.mText,poemText);


        /**
         * 레이아웃을 클릭하면 홈페이지 이동
        **/
        Intent intent = new Intent(context,AllPoemActivity.class);
        intent.putExtra("ttt",poemTitle);
        intent.putExtra("www",poemWriter);
        intent.putExtra("ccc",poemText);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.mLayout, pendingIntent);

        /**
         * 위젯 업데이트
         */
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

}