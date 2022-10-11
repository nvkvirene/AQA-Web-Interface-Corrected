package ru.netology.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class CardOrderTest {

    public WebDriver driver;

    @BeforeAll
    public static void setDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void quit() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldHappyTest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Новикова Ирина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFailTestLastnameFirstName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Novikova Irina");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();

        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldFailTestMobilePhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Новикова Ирина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7 111 1111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();

        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldFailTestCheckboxNotPressed() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Новикова Ирина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71111111111");
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).getText();

        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }

    @Test
    void shouldFailTestFieldNameEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();

        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldFailTestFieldPhoneEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Новикова Ирина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();

        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }
}

