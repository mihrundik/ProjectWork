package config;

public class EnvConfig {

    // читаем переменные из файла типа setenv.sh
    public static String getUrl() {
        return System.getenv("TEST_URL");
    }

    public static String getLogin() {
        return System.getenv("TEST_LOGIN");
    }

    public static String getPassword() {
        return System.getenv("TEST_PASSWORD");
    }

}