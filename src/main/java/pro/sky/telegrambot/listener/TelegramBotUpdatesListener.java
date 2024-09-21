package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.IllegalDateTimeException;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repusitory.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationRepository notificationRepository) {
        this.telegramBot = telegramBot;
        this.notificationRepository = notificationRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Long chatId = update.message().chat().id();
            String textMessage = update.message().text();
            Matcher matcher = pattern.matcher(textMessage);
            // Process your updates here
            if (textMessage.equals("/start")) {
                sendMessage(chatId, "Добро пожаловать в ТГ бот <События>.\n" +
                        "Для создания уведомления отправьте мне сообщение согласно паттерну:\n" +
                        "дд.мм.гггг чч:мм <Текст уведомления> ");
            } else if (matcher.matches()) {
                LocalDateTime date = LocalDateTime.parse(matcher.group(1), dateTimeFormatter);
                String item = matcher.group(3);
                try {
                    if (!date.isAfter(LocalDateTime.now())) {
                        throw new IllegalDateTimeException("Указана не верная дата или время");
                    }
                    Notification notification = new Notification(chatId, item, date);
                    sendMessage(chatId, "Уведомление успешно создано, ожидайте");
                    notificationRepository.save(notification);
                    logger.info("Уведомление создано");
                } catch (IllegalDateTimeException e) {
                    sendMessage(chatId, "Указана не верная дата или время");
                    logger.info("Отправлена неправильная дата");
                }
            } else {
                sendMessage(chatId, "Команда введена не верно!");
                logger.info("В чат отправлена не верная команда!");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendResponse sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        logger.info("Отправлено в чат: " + message);
        return sendResponse;
    }


}
