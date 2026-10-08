package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By campoEmail =
            By.id("field-email");

    private final By campoPassword =
            By.id("field-password");

    private final By botonLogin =
            By.id("submit-login");

    public LoginPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    public void iniciarSesion(String usuario, String clave) {

        WebElement email = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        campoEmail
                )
        );

        email.clear();
        email.sendKeys(usuario);

        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        campoPassword
                )
        );

        password.clear();
        password.sendKeys(clave);

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        botonLogin
                )
        ).click();
    }
}