package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private String massage;
    private LocalDateTime dateTime;

    public Notification(Long chatId, String massage, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.massage = massage;
        this.dateTime = dateTime;
    }
//
//    public Notification() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public Long getChatId() {
//        return chatId;
//    }
//
//    public String getMassage() {
//        return massage;
//    }
//
//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
}
