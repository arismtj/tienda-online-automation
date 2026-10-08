package pages;

import java.math.BigDecimal;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By titulo =
            By.xpath(
                    "//h1[contains(" +
                            "translate(normalize-space(.)," +
                            "'abcdefghijklmnopqrstuvwxyz'," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ')," +
                            "'CARRITO')]"
            );

    private final By total =
            By.cssSelector(
                    ".cart-total .value"
            );


    public CartPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }


    public boolean validarTituloCarrito() {

        WebElement elemento =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                titulo
                        )
                );

        return elemento
                .getText()
                .trim()
                .equalsIgnoreCase(
                        "CARRITO"
                );
    }


    public BigDecimal obtenerTotalCarrito() {

        WebElement elemento =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                total
                        )
                );

        return convertirPrecio(
                elemento.getText()
        );
    }


    private BigDecimal convertirPrecio(
            String texto) {

        String limpio =
                texto.replace("S/", "")
                        .replace(",", ".")
                        .replaceAll(
                                "[^0-9.]",
                                ""
                        )
                        .trim();

        return new BigDecimal(limpio);
    }
}