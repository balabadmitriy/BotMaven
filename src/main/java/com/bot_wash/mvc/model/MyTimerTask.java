package com.bot_wash.mvc.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MyTimerTask extends TimerTask
{
    private final String END_WASHING = "Стирка закончится через 5 мин.";
    private Long minutes = 5l;
    private Timer timer;
    private TimeOutWash timeOutWash;
    private Long chatId;

    public MyTimerTask(Timer timer, Long minutes, TimeOutWash timeOutWash,Long chatId) {
        this.timer = timer;
        this.minutes = minutes;
        this.timeOutWash = timeOutWash;
        this.chatId = chatId;
        timer.schedule(this,1000,60000);
    }

    @Override
    public void run() {
        if (minutes == 0){
//            System.out.println("end");
            timeOutWash.setFree(true);
            timer.cancel();
        }else if (minutes == 1)
        {

            try {
                String urlParameters = "chat_id=".concat(chatId.toString()).concat("&text=").concat(END_WASHING);
                URL url = new URL("https://api.telegram.org/bot387023002:AAFp5tIFF_2OeZaZz7F_xZ1A4m07nopqfyY/sendMessage?".concat(urlParameters));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

//                System.out.println(con.getRequestMethod());
//                System.out.println(con.getResponseMessage());
                con.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(minutes + " == " + (timeOutWash.getDateNow().getTime()));
//            System.out.println(" Minutes left" + minutes);
        }
        minutes--;
    }

    public Long getMinutes() {
        return minutes;
    }

}