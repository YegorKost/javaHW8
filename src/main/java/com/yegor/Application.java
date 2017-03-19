package com.yegor;

import com.yegor.app.StartAppImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by YegorKost on 17.03.2017.
 */
public class Application {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        StartAppImpl startApp = (StartAppImpl) context.getBean("startApp");
        startApp.start();
    }
}
