import com.microsoft.playwright.*;

import org.junit.jupiter.api.*;
import java.util.List;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Disabled;

@Disabled
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

    try {
        page.navigate("https://practicesoftwaretesting.com/");

        Locator sortDropdown = page.locator(".form-select");

        sortDropdown.selectOption("price,desc");
        
        Locator pricelist = page.locator("[data-test=\"product-price\"]");

        List<String> priceTexts = pricelist.allInnerTexts().stream().map(i -> i.substring(1)).toList();
        List<Double> prices = priceTexts.stream().map(Double::parseDouble).toList();

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices, Collections.reverseOrder());
        Assertions.assertEquals(sortedPrices, prices, "The prices are not sorted correctly!");

    } catch (Exception e) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get("target/screenshots/failure-sorting.png"))
            .setFullPage(true));
        throw e;
    }
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

/*

yml code
    - name: Upload Screenshots on Failure
      if: failure()
      uses: actions/upload-artifact@v4
      with:
        name: failure-screenshots
        path: target/screenshots/

*/