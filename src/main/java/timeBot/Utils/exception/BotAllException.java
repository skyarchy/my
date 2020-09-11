package timeBot.Utils.exception;

public class BotAllException extends RuntimeException {

    // создаем свой ексепшн пригодитсо
    public BotAllException(String message) {
        super(message);
    }

    public BotAllException(String message, Throwable cause) {
        super(message, cause);
    }

}
