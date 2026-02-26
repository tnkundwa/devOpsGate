import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SortingTest {
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
    void testSortingPriceLowToHigh() {
        page.navigate("https://practicesoftwaretesting.com/");

        page.locator(".form-select").selectOption("price,desc");

        Locator pricelist = page.locator("[data-test=\"product-price\"]");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<String> priceTexts = pricelist.allInnerTexts().stream().map(i -> i.substring(1)).toList();
        List<Double> prices = priceTexts.stream().map(Double::parseDouble).toList();

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices, Collections.reverseOrder());
        
        Assertions.assertEquals(sortedPrices, prices, "The prices are not sorted correctly!");
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }
}