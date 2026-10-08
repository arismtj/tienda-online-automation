package steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {

    private static WebDriver driver;

    private DriverManager() {
    }

    public static WebDriver getDriver() {

        if (driver == null) {

            ChromeOptions options = new ChromeOptions();

            options.addArguments("--start-maximized");

            options.addArguments(
                    "--user-data-dir=D:\\SeleniumProfileNTT"
            );

            driver = new ChromeDriver(options);
        }

        return driver;
    }

    public static void cerrarDriver() {

        if (driver != null) {

            try {
                driver.quit();
            } catch (Exception ignored) {
            }

            driver = null;
        }
    }
}