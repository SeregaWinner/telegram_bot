package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repusitory.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(TelegramBotApplication.class)
class TelegramBotApplicationTests {

    @MockBean
    NotificationRepository messageRepository;


    @MockBean
    TelegramBot telegramBot;
    @SpyBean
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Captor
    private ArgumentCaptor<SendMessage> massageCaptor;
    @Captor
    private ArgumentCaptor<Notification> taskArgumentCaptor;

    @Test
    void processStartTest() {

        Update update = mock(Update.class);
        com.pengrad.telegrambot.model.Message message = mock(com.pengrad.telegrambot.model.Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(mock(com.pengrad.telegrambot.model.Chat.class));
        when(message.chat().id()).thenReturn(112233L);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        String expectedText = "Добро пожаловать в ТГ бот <События>.\n" +
                "Для создания уведомления отправьте мне сообщение согласно паттерну:\n" +
                "дд.мм.гггг чч:мм <Текст уведомления> ";

        telegramBotUpdatesListener.process(updates);
        verify(telegramBot).execute(massageCaptor.capture());
        SendMessage capturedMessage = massageCaptor.getValue();

        assertEquals(112233L, capturedMessage.getParameters().get("chat_id"));
        assertEquals(expectedText, capturedMessage.getParameters().get("text"));
    }

    @Test
    void processNegativeDateTest() {
        Update update = mock(Update.class);
        com.pengrad.telegrambot.model.Message message = mock(com.pengrad.telegrambot.model.Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("20.10.2000 20:45 Вечеринка");
        when(message.chat()).thenReturn(mock(com.pengrad.telegrambot.model.Chat.class));
        when(message.chat().id()).thenReturn(112233L);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        String expectedText = "Указана не верная дата или время";

        telegramBotUpdatesListener.process(updates);
        verify(telegramBot).execute(massageCaptor.capture());
        SendMessage capturedMessage = massageCaptor.getValue();

        assertEquals(112233L, capturedMessage.getParameters().get("chat_id"));
        assertEquals(expectedText, capturedMessage.getParameters().get("text"));
    }

    @Test
    void processCreateTest() {
        Update update = mock(Update.class);
        com.pengrad.telegrambot.model.Message message = mock(com.pengrad.telegrambot.model.Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("22.09.2024 20:45 Вечеринка");
        when(message.chat()).thenReturn(mock(com.pengrad.telegrambot.model.Chat.class));
        when(message.chat().id()).thenReturn(112233L);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        String expectedText = "Уведомление успешно создано, ожидайте";

        telegramBotUpdatesListener.process(updates);
        verify(telegramBot).execute(massageCaptor.capture());
        SendMessage capturedMessage = massageCaptor.getValue();

        assertEquals(112233L, capturedMessage.getParameters().get("chat_id"));
        assertEquals(expectedText, capturedMessage.getParameters().get("text"));
    }

    @Test
    void processNegativeCreateTest() {
        Update update = mock(Update.class);
        com.pengrad.telegrambot.model.Message message = mock(com.pengrad.telegrambot.model.Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("Вечеринка");
        when(message.chat()).thenReturn(mock(com.pengrad.telegrambot.model.Chat.class));
        when(message.chat().id()).thenReturn(112233L);
        List<Update> updates = new ArrayList<>();
        updates.add(update);
        String expectedText = "Команда введена не верно!";

        telegramBotUpdatesListener.process(updates);
        verify(telegramBot).execute(massageCaptor.capture());
        SendMessage capturedMessage = massageCaptor.getValue();

        assertEquals(112233L, capturedMessage.getParameters().get("chat_id"));
        assertEquals(expectedText, capturedMessage.getParameters().get("text"));
    }
}
