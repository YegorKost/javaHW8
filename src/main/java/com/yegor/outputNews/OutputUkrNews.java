package com.yegor.outputNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by YegorKost on 16.03.2017.
 */
@Component
public class OutputUkrNews implements OutputNews {
    @Qualifier("fileForOutput")
    private File file;

    @Autowired
    public OutputUkrNews(File file) {
        this.file = file;
    }

    @Override
    public boolean output(List<String> list) {
        boolean result;
        if ((result = !list.isEmpty())){
            outputToConsole(list);
            outputToFile(list);
        }
        return result;
    }

    private void outputToFile(List<String> list) {
        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter buff = new BufferedWriter(fileWriter)){

            if (file.canWrite()) {
                for (String news: list) {
                    buff.write(news);
                    buff.newLine();
                    buff.flush();
                }
            }
        } catch (IOException e) {
            String err = "File cannot be write!";
            System.out.printf("%s (%s)\n", err, e.getMessage());
        }
    }

    private void outputToConsole(List<String> list) {
        for (String s: list) {
            System.out.println(s);
        }
    }
}
