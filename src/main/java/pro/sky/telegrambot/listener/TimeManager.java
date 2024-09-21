package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repusitory.NotificationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TimeManager {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private  final NotificationRepository notificationRepository;
    private  final TelegramBot telegramBot;

    public TimeManager(NotificationRepository notificationRepository, TelegramBot telegramBot) {
        this.notificationRepository = notificationRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    void run() {
        List<Notification> messages = notificationRepository.findAllByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        messages.forEach(task -> {
            SendMessage message = new SendMessage(task.getChatId(), task.getMassage());
            telegramBot.execute(message);
            logger.info("Уведомление отправлено: chatId = " + task.getChatId() + " , text = " + task.getMassage());
            notificationRepository.deleteById(task.getId());
            logger.info("Уведомление удалено из БД");
        });
    }



}
