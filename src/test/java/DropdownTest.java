import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DropdownTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testSelectOption1() {
        page.navigate("https://the-internet.herokuapp.com/dropdown");

        page.locator("#dropdown").selectOption("1");

        String selected = page.locator("#dropdown").inputValue();
        assertEquals("1", selected, "Option 1 should be selected!");
    }

    @Test
    void testSelectOption2() {
        page.navigate("https://the-internet.herokuapp.com/dropdown");

        page.locator("#dropdown").selectOption("2");

        String selected = page.locator("#dropdown").inputValue();
        assertEquals("2", selected, "Option 2 should be selected!");
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}