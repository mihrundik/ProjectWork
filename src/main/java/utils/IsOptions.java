package utils;

import org.openqa.selenium.remote.AbstractDriverOptions;


public interface IsOptions {

    default AbstractDriverOptions webDriverOptions() {
        return null;
    }

}
