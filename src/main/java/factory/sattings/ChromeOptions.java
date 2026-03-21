package factory.sattings;

import org.openqa.selenium.remote.AbstractDriverOptions;


public class ChromeOptions implements IsOptions {

    @Override
    public AbstractDriverOptions webDriverOptions() {
        org.openqa.selenium.chrome.ChromeOptions chromeOptions = new org.openqa.selenium.chrome.ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("--incognito");
        return chromeOptions;
    }
}