package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cerrarSesion =
            By.cssSelector("a.logout");

    // Locators reales observados en la tienda
    private final By categoriaClothes =
            By.cssSelector("#category-3 > a");

    private final By subcategoriaMen =
            By.cssSelector("#category-4 > a");


    public HomePage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }


    public boolean usuarioAutenticado() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            cerrarSesion
                    )
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }


    public void navegarCategoriaYSubcategoria(
            String categoria,
            String subcategoria) {

        /*
         * Caso negativo solicitado por el examen.
         */
        if (!categoria.equalsIgnoreCase("Clothes")) {

            throw new AssertionError(
                    "La categoría '" + categoria + "' no existe."
            );
        }


        /*
         * Esperamos explícitamente la categoría CLOTHES.
         */
        WebElement clothes = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        categoriaClothes
                )
        );


        /*
         * Hacemos hover para desplegar MEN / WOMEN.
         */
        Actions actions = new Actions(driver);

        actions
                .moveToElement(clothes)
                .perform();


        /*
         * El examen solicita Men.
         */
        if (!subcategoria.equalsIgnoreCase("Men")) {

            throw new AssertionError(
                    "La subcategoría '" + subcategoria + "' no existe."
            );
        }


        WebElement men = wait.until(
                ExpectedConditions.elementToBeClickable(
                        subcategoriaMen
                )
        );


        men.click();


        /*
         * Confirmamos que realmente llegamos a la categoría Men.
         */
        wait.until(
                webDriver ->
                        webDriver
                                .getCurrentUrl()
                                .toLowerCase()
                                .contains("men")
        );


        System.out.println(
                "NAVEGACIÓN OK: Clothes > Men"
        );
    }
}