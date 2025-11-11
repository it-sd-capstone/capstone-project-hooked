package edu.cvtc.hooked.controller;

import edu.cvtc.hooked.util.DbUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppBootstrap implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent sce) {
        DbUtil.ensureSchema();
    }
}
