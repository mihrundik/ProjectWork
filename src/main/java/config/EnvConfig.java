package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvConfig {

    private static final Logger log = LoggerFactory.getLogger(EnvConfig.class);

    private EnvConfig() {

    }

    /**
     * Возвращает URL тестового окружения из переменной окружения TEST_URL.
     */
    public static String getUrl() {
        String url = System.getenv("TEST_URL");
        log.debug("Получен URL тестового окружения: {}", url);
        return url;
    }

    /**
     * Возвращает логин для авторизации в тестовом окружении из переменной окружения TEST_LOGIN.
     */
    public static String getLogin() {
        String login = System.getenv("TEST_LOGIN");
        log.debug("Получен логин тестового пользователя: {}", login);
        return login;
    }

    /**
     * Возвращает пароль для авторизации в тестовом окружении из переменной окружения TEST_PASSWORD.
     */
    public static String getPassword() {
        String password = System.getenv("TEST_PASSWORD");
        // Пароль не логируем в целях безопасности
        log.debug("Получен пароль тестового пользователя (скрыто)");
        return password;
    }
}