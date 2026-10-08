package pages;

import java.math.BigDecimal;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By precioProducto =
            By.cssSelector(".current-price span");

    private final By cantidad =
            By.id("quantity_wanted");

    private final By agregarCarrito =
            By.cssSelector("button.add-to-cart");

    private final By popup =
            By.id("blockcart-modal");

    private final By totalPopup =
            By.cssSelector(
                    "#blockcart-modal .product-total .value"
            );

    private final By finalizarCompra =
            By.cssSelector(
                    "#blockcart-modal .cart-content-btn a.btn-primary"
            );


    public ProductPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }


    public BigDecimal obtenerPrecioUnitario() {

        WebElement elemento =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                precioProducto
                        )
                );

        return convertirPrecio(
                elemento.getText()
        );
    }


    public void establecerCantidad(int unidades) {

        WebElement campoCantidad =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                cantidad
                        )
                );

        campoCantidad.click();

        campoCantidad.sendKeys(
                Keys.CONTROL,
                "a"
        );

        campoCantidad.sendKeys(
                String.valueOf(unidades)
        );

        campoCantidad.sendKeys(
                Keys.TAB
        );
    }


    public void agregarAlCarrito() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        agregarCarrito
                )
        ).click();
    }


    public boolean popupVisible() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        popup
                )
        ).isDisplayed();
    }


    public boolean productoAgregadoCorrectamente() {

        WebElement modal =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                popup
                        )
                );

        String texto =
                modal.getText()
                        .toLowerCase();

        return texto.contains(
                "producto añadido correctamente"
        );
    }


    public BigDecimal obtenerTotalPopup() {

        WebElement total =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                totalPopup
                        )
                );

        return convertirPrecio(
                total.getText()
        );
    }


    public void finalizarCompra() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        finalizarCompra
                )
        ).click();
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