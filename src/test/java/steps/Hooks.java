package steps;

import io.cucumber.java.After;

public class Hooks {

    @After
    public void cerrarNavegador() {

        DriverManager.cerrarDriver();
    }
}