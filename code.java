package io.batch.zabbixbatch.batch.job.tasklet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class scraping {

    private WebDriver webDriver;
    public static final String WEB_DRIVER_ID = webdriver.chrome.driver;  드라이버 ID
    public static final String WEB_DRIVER_PATH = System.getProperty(user.dir) + chromedriver.exe;  드라이버 경로
    public static final String TARGET_SITE = String.format(httpswww.youtube.com@motemote_tv);

    public void getData() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments(headless);  브라우저 열지 않게
        options.addArguments(--disable-gpu);  GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요
        options.addArguments(--no-sandbox);  Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요
        options.addArguments(--disable-popup-blocking);  팝업 안 띄움
        options.addArguments(--remote-allow-origins=);  교차 출처 요청 허용, 없을 시 403에러

        webDriver = new ChromeDriver(options);
        webDriver.get(TARGET_SITE);  해당 주소의 데이터 얻어오기

        WebElement element = webDriver.findElements(By.cssSelector(body  script)).get(14);
        String scriptText = (String) ((JavascriptExecutor) webDriver).executeScript(return arguments[0].innerHTML, element);
        scriptText = scriptText.replace(var ytInitialData = , ).replace(;, );

        JSONObject jsonObj = new JSONObject(scriptText);
        JSONArray contents = jsonObj.getJSONObject(contents)
                .getJSONObject(twoColumnBrowseResultsRenderer)
                .getJSONArray(tabs)
                .getJSONObject(1)
                .getJSONObject(tabRenderer)
                .getJSONObject(content)
                .getJSONObject(richGridRenderer)
                .getJSONArray(contents);

        contents.remove(contents.length() - 1);  마지막 데이터는 필요 없음

        for (Object content  contents) {

            JSONObject videoRenderer = ((JSONObject) content).getJSONObject(richItemRenderer)
                    .getJSONObject(content)
                    .getJSONObject(videoRenderer);

            String thumbnail = videoRenderer.getJSONObject(thumbnail)
                    .getJSONArray(thumbnails)
                    .getJSONObject(3)
                    .getString(url);

            String title = videoRenderer.getJSONObject(title)
                    .getJSONArray(runs)
                    .getJSONObject(0)
                    .getString(text)
                    .split(])[1]
                    .trim();

            String publishedTime = videoRenderer.getJSONObject(publishedTimeText)
                    .getString(simpleText);

            String link = videoRenderer.getJSONObject(navigationEndpoint)
                    .getJSONObject(commandMetadata)
                    .getJSONObject(webCommandMetadata)
                    .getString(url);

            String uploader = videoRenderer.getJSONObject(title)
                    .getJSONObject(accessibility)
                    .getJSONObject(accessibilityData)
                    .getString(label)
                    .split(게시자 )[1]
                    .split(d+)[0]
                    .trim();

            System.out.println(제목   + title + , 게시자   + uploader + , 게시일   + publishedTime + , 썸네일주소   + thumbnail + , 영상 주소   + link);
        }
    }
}
