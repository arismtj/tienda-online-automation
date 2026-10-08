package steps;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;

public class StoreSteps {

    private WebDriver driver;

    private LoginPage loginPage;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    private BigDecimal precioUnitario;
    private BigDecimal totalEsperado;

    private static final String URL_TIENDA =
            "https://qalab.bensg.com/store";

    private static final String URL_LOGIN =
            "https://qalab.bensg.com/store/pe/iniciar-sesion?back=my-account";


    // ==========================================================
    // INGRESO A LA TIENDA
    // ==========================================================

    @Given("estoy en la página de la tienda")
    public void estoyEnLaPaginaDeLaTienda() {

        driver = DriverManager.getDriver();

        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(30)
                );

        /*
         * Primero ingresamos a la tienda.
         */
        driver.get(URL_TIENDA);

        /*
         * Si quedó una sesión iniciada de un escenario anterior,
         * cerramos sesión.
         */
        By logout =
                By.cssSelector("a.logout");

        if (!driver.findElements(logout).isEmpty()) {

            driver.findElement(logout).click();

            wait.until(
                    ExpectedConditions.urlContains(
                            "/store"
                    )
            );
        }

        /*
         * Abrimos directamente el login.
         */
        driver.get(URL_LOGIN);

        /*
         * Confirmamos que realmente cargó el formulario.
         */
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("field-email")
                )
        );

        System.out.println(
                "Página de login disponible."
        );
    }


    // ==========================================================
    // LOGIN
    // ==========================================================

    @Given("me logueo con mi usuario {string} y clave {string}")
    public void login(
            String usuarioFeature,
            String claveFeature) {

        boolean escenarioValido =
                "USUARIO_VALIDO".equals(usuarioFeature)
                        &&
                        "CLAVE_VALIDA".equals(claveFeature);


        String usuario;
        String clave;


        /*
         * HAPPY PATH:
         * Obtiene las credenciales reales desde IntelliJ.
         */
        if (escenarioValido) {

            usuario =
                    System.getenv("STORE_USER");

            clave =
                    System.getenv("STORE_PASSWORD");


            if (usuario == null ||
                    usuario.trim().isEmpty()) {

                throw new IllegalStateException(
                        "STORE_USER no está configurado en TestRunner."
                );
            }


            if (clave == null ||
                    clave.trim().isEmpty()) {

                throw new IllegalStateException(
                        "STORE_PASSWORD no está configurado en TestRunner."
                );
            }

        } else {

            /*
             * UNHAPPY PATH:
             * Usa literalmente los valores del Examples.
             */
            usuario = usuarioFeature;
            clave = claveFeature;
        }


        loginPage =
                new LoginPage(driver);


        loginPage.iniciarSesion(
                usuario,
                clave
        );


        /*
         * Esperamos el resultado del login.
         */
        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                );


        if (escenarioValido) {

            /*
             * Para considerar el login exitoso aceptamos:
             *
             * - que aparezca "Cerrar sesión"
             * - o que lleguemos a la página Mi Cuenta
             */
            boolean loginCorrecto;

            try {

                loginCorrecto =
                        wait.until(webDriver -> {

                            boolean logoutVisible =
                                    !webDriver
                                            .findElements(
                                                    By.cssSelector(
                                                            "a.logout"
                                                    )
                                            )
                                            .isEmpty();


                            boolean paginaCuenta =
                                    webDriver
                                            .getCurrentUrl()
                                            .contains(
                                                    "mi-cuenta"
                                            )
                                            ||
                                            webDriver
                                                    .getCurrentUrl()
                                                    .contains(
                                                            "my-account"
                                                    );


                            return logoutVisible ||
                                    paginaCuenta;
                        });

            } catch (Exception e) {

                loginCorrecto = false;
            }


            Assertions.assertTrue(
                    loginCorrecto,
                    "No se pudo iniciar sesión con las credenciales válidas."
            );


            homePage =
                    new HomePage(driver);


            System.out.println(
                    "LOGIN VÁLIDO: OK"
            );

        } else {

            /*
             * Para el caso negativo esperamos que el usuario
             * siga en la pantalla de autenticación.
             */
            boolean sigueEnLogin;

            try {

                sigueEnLogin =
                        wait.until(webDriver ->

                                !webDriver
                                        .findElements(
                                                By.id(
                                                        "field-email"
                                                )
                                        )
                                        .isEmpty()
                        );

            } catch (Exception e) {

                sigueEnLogin = false;
            }


            if (sigueEnLogin) {

                throw new AssertionError(
                        "UNHAPPY PATH: credenciales inválidas rechazadas. " +
                                "El flujo se detiene como se esperaba."
                );

            } else {

                throw new AssertionError(
                        "DEFECTO: el sistema permitió ingresar " +
                                "con credenciales inválidas."
                );
            }
        }
    }


    // ==========================================================
    // CATEGORÍA
    // ==========================================================

    @When("navego a la categoria {string} y subcategoria {string}")
    public void navegarCategoria(
            String categoria,
            String subcategoria) {

        if (homePage == null) {

            homePage =
                    new HomePage(driver);
        }


        homePage
                .navegarCategoriaYSubcategoria(
                        categoria,
                        subcategoria
                );


        System.out.println(
                "Categoría seleccionada: "
                        + categoria
                        + " / "
                        + subcategoria
        );
    }


    // ==========================================================
    // PRODUCTO
    // ==========================================================

    @When("agrego 2 unidades del primer producto al carrito")
    public void agregarProducto() {

        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(20)
                );


        By primerProducto =
                By.cssSelector(
                        ".product-miniature .product-title a"
                );


        wait.until(
                ExpectedConditions.elementToBeClickable(
                        primerProducto
                )
        ).click();


        productPage =
                new ProductPage(driver);


        precioUnitario =
                productPage
                        .obtenerPrecioUnitario();


        productPage
                .establecerCantidad(2);


        totalEsperado =
                precioUnitario.multiply(
                        BigDecimal.valueOf(2)
                );


        System.out.println(
                "Precio unitario: S/ "
                        + precioUnitario
        );

        System.out.println(
                "Cantidad: 2"
        );

        System.out.println(
                "Total esperado: S/ "
                        + totalEsperado
        );


        productPage
                .agregarAlCarrito();
    }


    // ==========================================================
    // POPUP
    // ==========================================================

    @Then("valido en el popup la confirmación del producto agregado")
    public void validarPopup() {

        Assertions.assertTrue(
                productPage.popupVisible(),
                "No se visualizó el popup."
        );


        Assertions.assertTrue(
                productPage
                        .productoAgregadoCorrectamente(),
                "No apareció la confirmación del producto."
        );


        System.out.println(
                "Popup de confirmación: OK"
        );
    }


    // ==========================================================
    // TOTAL POPUP
    // ==========================================================

    @Then("valido en el popup que el monto total sea calculado correctamente")
    public void validarTotalPopup() {

        BigDecimal totalPopup =
                productPage
                        .obtenerTotalPopup();


        Assertions.assertEquals(
                0,
                totalEsperado.compareTo(
                        totalPopup
                ),
                "El total del popup no coincide."
        );


        System.out.println(
                "Total popup: S/ "
                        + totalPopup
        );
    }


    // ==========================================================
    // IR AL CARRITO
    // ==========================================================

    @When("finalizo la compra")
    public void finalizarCompra() {

        productPage
                .finalizarCompra();


        cartPage =
                new CartPage(driver);
    }


    // ==========================================================
    // TÍTULO CARRITO
    // ==========================================================

    @Then("valido el titulo de la pagina del carrito")
    public void validarTituloCarrito() {

        Assertions.assertTrue(
                cartPage
                        .validarTituloCarrito(),
                "No se encontró el título CARRITO."
        );


        System.out.println(
                "Título CARRITO: OK"
        );
    }


    // ==========================================================
    // TOTAL FINAL
    // ==========================================================

    @Then("vuelvo a validar el calculo de precios en el carrito")
    public void validarTotalCarrito() {

        BigDecimal totalCarrito =
                cartPage
                        .obtenerTotalCarrito();


        Assertions.assertEquals(
                0,
                totalEsperado.compareTo(
                        totalCarrito
                ),
                "El total del carrito no coincide."
        );


        System.out.println(
                "Total carrito: S/ "
                        + totalCarrito
        );


        System.out.println(
                "HAPPY PATH FINALIZADO CORRECTAMENTE"
        );
    }
}