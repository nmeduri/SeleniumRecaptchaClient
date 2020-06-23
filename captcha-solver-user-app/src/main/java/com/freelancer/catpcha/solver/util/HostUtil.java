package com.freelancer.catpcha.solver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HostUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostUtil.class.getName());

    //TODO 1) give read and write permission this hosts file
    private static final String HOSTS_FILE_PATH_FOR_UNIX = "/etc/hosts";
    private static final String HOSTS_FILE_PATH_FOR_WINDOWS = "C:/Windows/System32/drivers/etc/hosts";

    @Autowired
    private SystemUtil systemUtil;

    public boolean addHostEntry(String host) {
        File hostsFile = getHostsFilePath();
        if(isAccessible(hostsFile)) {
            try {
                String entry = "\n127.0.1.1\t" + systemUtil.getDomainName(host) + "\n";
                LOGGER.info("Entry need to be added is {}", entry);
                FileWriter writer = new FileWriter(hostsFile, true);
                writer.write(entry);
                writer.close();
                LOGGER.info("File write success");
                return true;
            } catch (Exception ex) {
                LOGGER.error("Exception", ex);
            }
        }
        return false;
    }

    private boolean isAccessible(File hostsFile) {
        if(!hostsFile.exists()) {
            LOGGER.info("Hosts file doesn't exists");
            return false;
        }
        if(!hostsFile.canWrite()) {
            LOGGER.info("User does not have write access");
            return false;
        }
        return true;
    }

    public boolean removeHostEntry(String host) {
        File hostsFile = getHostsFilePath();
        if(isAccessible(hostsFile)) {
            try {
                String entry = "127.0.1.1\t" + systemUtil.getDomainName(host);
                LOGGER.info("Entry need to be deleted is {}", entry);
                List<String> lines = Files.lines(hostsFile.toPath()).filter(line -> !line.equalsIgnoreCase(entry)).collect(Collectors.toList());
                FileWriter writer = new FileWriter(hostsFile, false);
                lines.forEach((line) -> {
                    try {
                        writer.write(line + "\n");
                    } catch (Exception ex) {
                        LOGGER.error("Exception in deleting entry in hosts file", ex);
                    }
                });
                LOGGER.info("File write success");
                writer.close();
                return true;
            } catch (Exception ex) {
                LOGGER.error("Exception", ex);
            }
        }
        return false;
    }

    private File getHostsFilePath() {
        String osName = systemUtil.getOSName();
        if("linux".equalsIgnoreCase(osName) || "mac".equalsIgnoreCase(osName)) {
            return new File(HOSTS_FILE_PATH_FOR_UNIX);
        } else {
            return new File(HOSTS_FILE_PATH_FOR_WINDOWS);
        }
    }

}
