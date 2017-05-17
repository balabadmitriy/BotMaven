package com.bot_wash.mvc.model;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

    Set<Long> idSet= new LinkedHashSet<>();

    boolean free = true;
    private Date now;
    TimeOutWash timeOutWash;

    MyTimerTask task = null;
    public Bot(TimeOutWash timeOutWash) {
        this.timeOutWash = timeOutWash;

    }

    @Override
    public void onUpdateReceived(Update update) {


        idSet.add(update.getMessage().getChatId());
//        System.out.println(update.getMessage().getFrom().getFirstName() +" = " +
//                update.getMessage().getText());
       // Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("YYYY.MM.dd HH:mm");
//        System.out.println(format.format(calendar.getTime()));

        Iterator<Long> iterator = idSet.iterator();

        String timeForWashing = update.getMessage().getText();

        if (timeOutWash.isFree()) {
//            while (iterator.hasNext()) {
                Long id = iterator.next();
                if (timeForWashing.equals("85") || timeForWashing.equals("130") || timeForWashing.equals("160"))
                {
                    timeOutWash.setTime(Long.parseLong(timeForWashing));
                    timeOutWash.setFirstName(update.getMessage().getFrom().getFirstName());
                    timeOutWash.setLastName(update.getMessage().getFrom().getLastName());
                    SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
                    sendMessage.setText("=> Вы поставили стирку на " + timeForWashing + " мин.\n" +
                            format.format(calendar.getTime()));
                    try {
                        sendMessage(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    task = new MyTimerTask(new Timer(),Long.parseLong(timeForWashing),timeOutWash,update.getMessage().getChatId());
                    timeOutWash.setDateNow(calendar.getTime());
                    timeOutWash.setFree(false);
                }else{
                    SendMessage sendMessage = new SendMessage().setChatId(id);
                    sendMessage.setText("=> "+ "Для запуска машинки необходимо ввести время стирки.\n" +
                            "Время стирки: \n"+
                            "Стирка 85 мин.\nСтирка 130 мин.\nСтирка 160 мин.");
                    try {
                        sendMessage(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
//            }
        }else{
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            sendMessage.setText("=> Сообщаю! Машинку занял " + timeOutWash.getFirstName() + " " + timeOutWash.getLastName().concat(".") +
                    "\n" + "Стирка закончится в " +
                    format.format(calendar.getTime().getTime() + (task.getMinutes()*60000l)) + " мин.");

//            format.format(calendar.getTime().getTime()-(task.getMinutes()*60000l)) + " мин."
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }



    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "387023002:AAFp5tIFF_2OeZaZz7F_xZ1A4m07nopqfyY";
    }

}
