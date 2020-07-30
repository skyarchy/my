package timeBot;


import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import timeBot.mainbot.Bot;

@SpringBootApplication
@AllArgsConstructor
public class SpringTimeBot implements CommandLineRunner {

    private final Bot bot;

    @Override
    public void run(String... args) throws Exception {

        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        options.setProxyHost("127.0.0.1");
        options.setProxyPort(9123);

        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(bot);

    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(SpringTimeBot.class, args);

    }
}
