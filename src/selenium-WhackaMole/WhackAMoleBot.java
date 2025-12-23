package es.urjc.etsii.metodologias.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WhackAMoleBot {
    public static void main(String[] args) throws InterruptedException {
        // Configurar GeckoDriver con la ruta correcta
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Juan_\\Desktop\\CIBER 2024-2025\\2ndo Cuatrimestre\\METODOLOGIAS DE DESARROLLO SEGURO\\PRACTICA_2\\geckodriver-v0.36.0-win32\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        WebDriver driver = new FirefoxDriver(options);

        // Abrir el juego en localhost(esta dirección varia)
        driver.get("http://localhost:63343/Whack-a-Mole/index.html?_ijt=qifi1ckjir5fsirs3t0c3ieqa8");


        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        int score = 0;

        // Bucle hasta alcanzar 10,000 puntos
        while (score < 10000) {
            try {
                // Buscar los topos activos
                List<WebElement> moles = driver.findElements(By.className("mole"));

                for (WebElement mole : moles) {
                    if (mole.isDisplayed()) {
                        mole.click();
                        score++;
                    }
                }
                
                if (score % 1000 == 0) {
                    System.out.println("Progreso: " + score + " puntos");
                }

                Thread.sleep(3);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        System.out.println("Esperando a que aparezca la flag...");
        Thread.sleep(5000);

        try {
            String pageText = driver.findElement(By.tagName("body")).getText();

            System.out.println("Texto completo de la página:");
            System.out.println(pageText);

            if (pageText.contains("URJC{")) {
                System.out.println("FLAG DETECTADA: " + pageText.substring(pageText.indexOf("URJC{"), pageText.indexOf("}") + 1));
            } else {
                System.out.println("No se encontró la flag automáticamente, revisa el texto.");
            }

        } catch (Exception e) {
            System.out.println("Error al buscar la flag: " + e.getMessage());
        }

        driver.quit();
    }
}//flag: URJC{W1111111N333RRR}
