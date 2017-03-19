package com.yegor.outputNews;

import com.yegor.cachedNews.CachedNewsImpl;
import com.yegor.news.ReadNewsImpl;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
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
public class OutputUkrNewsTest {

    private final ByteArrayOutputStream outForNews = new ByteArrayOutputStream();

    @Autowired
    private CachedNewsImpl cachedNews;

    @Autowired
    @Qualifier("fileForOutput")
    private File file;

    @AfterClass
    public static void tearDown() {
        System.setOut(null);
    }

    @Test
    public void outputFileTest() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        URLConnection urlConnection = PowerMockito.mock(URLConnection.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openConnection()).thenReturn(urlConnection);
        PowerMockito.when(urlConnection.getInputStream()).thenReturn(new FileInputStream("src/test/resources/rssTest.xml"));

        cachedNews.cache();
        if (file.canRead()) {
            try (FileReader reader = new FileReader(file);
                 BufferedReader buff = new BufferedReader(reader)){
                String actual;
                if ((actual = buff.readLine()) != null) {
                    assertTrue(actual.contains("Test news 1"));
                } else {
                    assertTrue(false);
                }
            } finally {
                if (file.exists()){
                    assert file.delete();
                }
            }
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void outputConsoleTest() throws Exception {
        System.setOut(new PrintStream(outForNews));

        URL url = PowerMockito.mock(URL.class);
        URLConnection urlConnection = PowerMockito.mock(URLConnection.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openConnection()).thenReturn(urlConnection);
        PowerMockito.when(urlConnection.getInputStream()).thenReturn(new FileInputStream("src/test/resources/rssTest.xml"));

        cachedNews.cache();
        assertTrue(outForNews.toString().contains("Test news 1"));
    }
}