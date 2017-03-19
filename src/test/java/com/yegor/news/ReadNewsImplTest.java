package com.yegor.news;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by YegorKost on 15.03.2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/springTest.xml")
@PrepareForTest({URL.class, ReadNewsImpl.class})
public class ReadNewsImplTest {

    private static List<String> testNews = new ArrayList<>();
    private final ByteArrayOutputStream outResourcesNotAvailable = new ByteArrayOutputStream();
    private final ByteArrayOutputStream outDocumentNotParsed = new ByteArrayOutputStream();


    @Autowired
    private ReadNewsImpl readNews;

    @BeforeClass
    public static void setList() {
        Collections.addAll(testNews, "Test news 1", "Test news 2", "Test news 3");
    }

    @After
    public void tearDown() {
        System.setOut(null);
    }

    @Test
    public void readNewsTest() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        URLConnection urlConnection = PowerMockito.mock(URLConnection.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openConnection()).thenReturn(urlConnection);
        PowerMockito.when(urlConnection.getInputStream()).thenReturn(new FileInputStream("src/test/resources/rssTest.xml"));

        assertEquals(testNews, readNews.readNews());

    }

    @Test
    public void readNewsWithoutResourcesTest() throws Exception {
        System.setOut(new PrintStream(outResourcesNotAvailable));

        readNews.readNews();
        assertTrue(outResourcesNotAvailable.toString().contains("Resource is not available!"));
    }

    @Test
    public void readNewsWithNotParsedResourcesTest() throws Exception {
        System.setOut(new PrintStream(outDocumentNotParsed));
        URL url = PowerMockito.mock(URL.class);
        URLConnection urlConnection = PowerMockito.mock(URLConnection.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openConnection()).thenReturn(urlConnection);
        PowerMockito.when(urlConnection.getInputStream()).thenReturn(new FileInputStream("src/test/resources/rssTestNotParsed.xml"));

        readNews.readNews();
        assertTrue(outDocumentNotParsed.toString().contains("Resource cannot be parsed!"));
    }

}