import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class BotMain extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getGlobal();

    private boolean expectedDate = false;
    private boolean expectedCity = false;
    private static OutputFormatter outputFormatter;

    public static void main(String[] args) throws IOException {
        outputFormatter = new OutputFormatter();
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new BotMain());
        } catch (TelegramApiRequestException e) {
            logger.warning("Ошибка регистрации бота");
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if(message.hasText()){
            if(expectedDate){
                if(Pattern.matches("[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}", message.getText())){
                    try {
                        String output = outputFormatter.formatXmlOutput(message.getText());
                        if(output.isEmpty())
                            sendMessage(message, "Недопустимая дата");
                        else
                            sendMessage(message, outputFormatter.formatXmlOutput(message.getText()));
                    } catch (IOException e) {
                        logger.warning(e.getMessage());
                    }
                }
                else {
                        sendMessage(message, "Неверный формат даты");
                }
                expectedDate = false;
                return;
            }

            if(message.getText().toLowerCase().contains("цб") ||
                    (message.getText().toLowerCase().contains("банк") &&
                            (message.getText().toLowerCase().contains("россии") ||
                                    message.getText().toLowerCase().contains("центр")
                            )
                    )
            ){
                sendMessage(message, "Укажите дату в формате dd/mm/yyyy");
                expectedDate = true;
                return;
            }

            if(expectedCity){
                expectedCity = false;
                try {
                    Gson gson = new Gson();
                    HashMap<String, String> cityCode = gson.fromJson(new BufferedReader(
                            new FileReader("./src/main/resources/cityCode.json")), HashMap.class);
                    boolean cityFound = false;
                    for (Map.Entry<String, String> city: cityCode.entrySet()){
                        if (city.getKey().contains(message.getText().toLowerCase())){
                            sendMessage(message, outputFormatter.formatSiteOutput(city.getValue()));
                            cityFound = true;
                            break;
                        }
                    }
                    if(!cityFound) sendMessage(message, "Данный город не доступен для  получения курсов валют");
                    return;
                } catch (FileNotFoundException e) {
                    logger.warning("Не найден json файл");
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }

            if(message.getText().toLowerCase().contains("курс") ||
                    message.getText().toLowerCase().contains("валют")
            ){
                if(!message.getText().contains("город")){
                    sendMessage(message, "Укажите город");
                    expectedCity = true;
                    return;
                }
                else {
                    Gson gson = new Gson();
                    try {
                        String[] splited = message.getText().split(" ");
                        String messageCity = "";
                        for (int i = 0; i < splited.length; i++){
                            if (splited[i].contains("город") && i + 1 < splited.length){
                                messageCity = splited[i+1];
                                break;
                            }
                        }
                        if(messageCity.isEmpty()){
                            sendMessage(message, "Город не указан, укажите город в следующем сообщении");
                            expectedCity = true;
                        }

                        HashMap<String, String> cityCode = gson.fromJson(new BufferedReader(
                                new FileReader("./src/main/resources/cityCode.json")), HashMap.class);
                        boolean cityFound = false;
                        for (Map.Entry<String, String> city: cityCode.entrySet()){
                            if (city.getKey().contains(messageCity.toLowerCase())){
                                sendMessage(message, outputFormatter.formatSiteOutput(city.getValue()));
                                cityFound = true;
                                break;
                            }
                        }
                        if(!cityFound) sendMessage(message, "Данный город не доступен для  получения курсов валют");
                        return;
                    } catch (FileNotFoundException e) {
                        logger.warning("Не найден json файл");
                    } catch (IOException e) {
                        logger.warning(e.getMessage());
                    }
                }
            }

            if(message.getText().equals("/help")){
                String help = "1) Для получения инфорации о курсах валют, предоставляемых Центробанком упомяните в" +
                        " сообщении хотя бы одно из следующих слов: цб, банк России, центральный банк\n" +
                        "2) Для получения информации о курсах валют в регионе, упомяните в сообщении одно из" +
                        " следующих слов : курс, валюта. Чтобы сразу указать город упомяните в сообщении слово 'город'" +
                        " и сразу после него укажите название города\n" +
                        "Пример: курс валют в городе Москва\n" +
                        "3) Для получения информации о доступных городах отправьте команду /cities\n" +
                        "4) Для получения информации о доступных валютах отправьте команду /currencies";
                sendMessage(message, help);
                return;
            }

            if(message.getText().equals("/cities")){
                String cities = "Доступные города:\n" +
                        "1) Москва\n" +
                        "2) Санкт-Петербург\n" +
                        "3) Новосибирск\n" +
                        "4) Екатеринбург\n" +
                        "5) Казань\n" +
                        "6) Нижний Новгород\n" +
                        "7) Челябинск\n" +
                        "8) Самара\n" +
                        "9) Омск\n" +
                        "10) Ростов\n" +
                        "11) Уфа\n" +
                        "12) Красноярск\n" +
                        "13) Воронеж\n" +
                        "14) Пермь\n" +
                        "15) Волгоград\n" +
                        "16) Краснодар\n" +
                        "17) Саратов\n" +
                        "18) Тюмень\n" +
                        "19) Тольятти\n" +
                        "20) Ижевск\n" +
                        "21) Барнаул\n" +
                        "22) Ульяновск\n" +
                        "23) Иркутск\n" +
                        "24) Хабаровск\n" +
                        "25) Ярославль\n" +
                        "26) Владивосток\n" +
                        "27) Махачкала\n" +
                        "28) Томск\n" +
                        "29) Оренбург\n" +
                        "30) Кемерово\n" +
                        "31) Новокузнецк\n" +
                        "32) Рязань\n" +
                        "33) Набережные челны\n" +
                        "34) Астрахань\n" +
                        "35) Пенза\n" +
                        "36) Киров\n" +
                        "37) Липецк\n" +
                        "38) Балашиха";
                sendMessage(message, cities);
                return;
            }

            if(message.getText().equals("/currencies")){
                String currencies = "Доступные валюты:\n" +
                        "1) USD\n" +
                        "2) EUR\n" +
                        "3) GBP\n" +
                        "4) JPY\n" +
                        "5) CNY";
                sendMessage(message, currencies);
                return;
            }

            if(!message.isGroupMessage()){
                sendMessage(message, "Непонятный запрос. Для получения информации отправьте /help");
            }
        }
        else {
            if(!message.isGroupMessage()){
                sendMessage(message, "Сообщение не содержит текст. Доступна только работа с текстом");
            }
        }
    }

    private void sendMessage(Message message, String messageText){
        SendMessage sendedMessage = new SendMessage();
        sendedMessage.enableMarkdown(true).setChatId(message.getChatId().toString());
        sendedMessage.setText(messageText);
        try {
            sendMessage(sendedMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "ITMO_bank_bot";
    }

    public String getBotToken() {
        return "1129363912:AAE6nQA2POrXiNI9Ttkwm1AnVL8pJZ2vYaM";
    }
}
