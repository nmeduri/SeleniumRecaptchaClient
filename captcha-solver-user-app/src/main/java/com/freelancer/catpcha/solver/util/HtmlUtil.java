package com.freelancer.catpcha.solver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.StringJoiner;

@Component
public class HtmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUtil.class.getName());

    public boolean createCaptchaFile(String siteKey, String id) {
        try {
            InputStream stream = new ClassPathResource("html-resource.txt").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringJoiner joiner = new StringJoiner("\n");
            String line;
            while((line = reader.readLine()) != null) {
                joiner.add(line);
            }
            reader.close();
            String fileContents = joiner.toString().replaceAll("%KEY%", siteKey).replaceAll("%ID%", id);
            File folder = new File(System.getProperty("user.dir"), "static");
            if(!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, "index.html");
            LOGGER.info("Create index file at location {}", file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write(fileContents);
            writer.close();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Exception", ex);
        }
        return false;
    }

}
