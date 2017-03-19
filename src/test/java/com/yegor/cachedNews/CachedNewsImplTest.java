package com.yegor.cachedNews;

import com.yegor.news.ReadNewsImpl;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;

/**
 * Created by YegorKost on 18.03.2017.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/springTest.xml")
@PrepareForTest({URL.class, ReadNewsImpl.class})
public class CachedNewsImplTest {
    private final ByteArrayOutputStream outEmptyListOfReadNews = new ByteArrayOutputStream();

    @Autowired
    private CachedNews cachedNews;

    @After
    public void tearDown() {
        System.setOut(null);
    }

    @Test
    public void cacheEmptyListTest() throws Exception {
        System.setOut(new PrintStream(outEmptyListOfReadNews));
        URL url = PowerMockito.mock(URL.class);
        URLConnection urlConnection = PowerMockito.mock(URLConnection.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openConnection()).thenReturn(urlConnection);
        PowerMockito.when(urlConnection.getInputStream()).thenReturn(new FileInputStream("src/test/resources/rssTestEmpty.xml"));

        cachedNews.cache();
        System.out.println(outEmptyListOfReadNews);
        assertTrue(outEmptyListOfReadNews.toString().contains("The reading list of news is empty!"));
    }

    @Test
    public void cacheNullListTest() throws Exception {
        System.setOut(new PrintStream(outEmptyListOfReadNews));

        cachedNews.cache();
        System.out.println(outEmptyListOfReadNews);
        assertTrue(outEmptyListOfReadNews.toString().contains("The reading list of news is empty!"));
    }
}