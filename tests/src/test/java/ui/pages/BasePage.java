package ui.pages;

import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasePage {
    protected Page page;
}

