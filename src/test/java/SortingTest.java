import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

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
        page.setDefaultTimeout(60000);
    }

    @Test
    void testSortingPriceLowToHigh() {
        page.navigate("https://practicesoftwaretesting.com/", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        Locator acceptCookies = page.locator("button:has-text('Accept')");
        if (acceptCookies.isVisible()) {
            acceptCookies.click();
        }

        // Locator sortDropdown = page.locator(".form-select");
        Locator sortDropdown = page.locator("[data-test='sort']");

        sortDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000));

        sortDropdown.selectOption("price,desc");

        page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(60000));
        
        Locator pricelist = page.locator("[data-test=\"product-price\"]");

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
        if (browser != null) {
        browser.close();
    }
    if (playwright != null) {
        playwright.close();
    }
    }
}