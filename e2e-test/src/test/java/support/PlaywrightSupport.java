package support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightSupport {

    private static final Playwright playwright = Playwright.create();
    private static final Browser browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(200)
    );

    private static BrowserContext context;
    private static Page page;

    public static Page page() {
        if (page == null) {
            context = browser.newContext();
            page = context.newPage();
        }
        return page;
    }

    public static void closePage() {
        if (page != null) {
            page.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
    }
}
