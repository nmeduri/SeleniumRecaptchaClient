package com.freelancer.catpcha.solver.ui;

import com.freelancer.catpcha.solver.dto.CaptchaRequest;
import com.freelancer.catpcha.solver.dto.Response;
import com.freelancer.catpcha.solver.model.CaptchaModel;
import com.freelancer.catpcha.solver.task.PollingTask;
import com.freelancer.catpcha.solver.ui.communicator.Communicator;
import com.freelancer.catpcha.solver.util.HostUtil;
import com.freelancer.catpcha.solver.util.HtmlUtil;
import com.freelancer.catpcha.solver.util.RestUtil;
import com.freelancer.catpcha.solver.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class Dashboard extends JFrame implements Communicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dashboard.class.getName());

    private Timer timer;

    @Autowired
    private ApplicationContext context;

    private final DefaultListModel<CaptchaModel> listModel;

    private final JButton buttonAccept;

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private HtmlUtil htmlUtil;

    @Autowired
    private HostUtil hostUtil;

    private final SystemUtil systemUtil;

    @Autowired
    public Dashboard(SystemUtil util, Timer timer) {
        super("Captcha Solver User");
        this.systemUtil = util;
        setSize(600, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        LOGGER.info("Period {} ms", systemUtil.getPeriod());
        LOGGER.info("delay {} ms", systemUtil.getDelay());
        JPanel panelCaptchaServer = new JPanel();
        panelCaptchaServer.setBounds(10, 10, 550, 280);
        panelCaptchaServer.setBorder(new TitledBorder("Server details"));
        panelCaptchaServer.setLayout(null);
        add(panelCaptchaServer);
        JPanel panelCaptchaChallenge = new JPanel();
        panelCaptchaChallenge.setBounds(10, 300, 550, 280);
        panelCaptchaChallenge.setBorder(new TitledBorder("Captcha Challenge"));
        panelCaptchaChallenge.setLayout(null);
        add(panelCaptchaChallenge);

        JButton buttonGetChallenge = new JButton("Get Captcha Challenge");
        buttonGetChallenge.setBounds(50, 50, 400, 50);
        buttonGetChallenge.addActionListener(this::getChallenge);
        panelCaptchaServer.add(buttonGetChallenge);

        JButton buttonReloadChallenge = new JButton("Auto Reload Captcha Challenge");
        buttonReloadChallenge.setBounds(50, 120, 400, 50);
        buttonReloadChallenge.addActionListener(this::schedule);
        buttonReloadChallenge.setEnabled(false);
        panelCaptchaServer.add(buttonReloadChallenge);

        JButton buttonCancelReloadChallenge = new JButton("Cancel Auto Reload Captcha Challenge");
        buttonCancelReloadChallenge.setBounds(50, 190, 400, 50);
        buttonCancelReloadChallenge.addActionListener(this::cancelAutoReload);
        buttonCancelReloadChallenge.setEnabled(false);
        panelCaptchaServer.add(buttonCancelReloadChallenge);

        listModel = new DefaultListModel<>();
        JList<CaptchaModel> list = new JList<>(listModel);
        list.setBounds(10, 30, 300, 200);
        panelCaptchaChallenge.add(list);

        buttonAccept = new JButton("Accept Challenge");
        buttonAccept.setBounds(320, 30, 200, 50);
        buttonAccept.addActionListener(this::acceptChallenge);
        panelCaptchaChallenge.add(buttonAccept);

        this.timer = timer;
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(systemUtil.isAutomate()) {
                    schedule(null);
                }
            }
        }, systemUtil.getDelay());

    }

    private void acceptChallenge(ActionEvent event) {
        CaptchaModel request = listModel.get(0);
        String id = request.getId();
        Response response = restUtil.acceptChallenge(id);
        if(response == null) {
            showError("Something went wrong.");
            return;
        }
        if(response.getStatus() == 200) {
            if(!htmlUtil.createCaptchaFile(request.getSiteKey(), request.getId())) {
                showError("Failed to create sample captcha html page.");
                return;
            }
            if(!hostUtil.addHostEntry(request.getHost())) {
                showError("Failed to create host entry.");
                return;
            }
            showMessage(response.getMessage());
            openBrowser(systemUtil.getDomainName(request.getHost()));
            startRobot();
        } else {
            showError("Failed " + response.getMessage());
        }
    }

    private void openBrowser(String host) {
        try {
            String[] command = systemUtil.getCommand(host);
            Runtime.getRuntime().exec(command);
        } catch (Exception ex ) {
            LOGGER.error("Error in opening browser.", ex);
        }
    }

    private void startRobot() {
        try {
            Thread.sleep(20 * 1000);
            Robot robot = new Robot();
            //TODO 2) this is the location of captcha checkbox appear on web page.
            //TODO 2) you have to update this location according to your screen
           // robot.mouseMove(100, 180);
            robot.mouseMove(50, 150);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(250);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception ex) {
            LOGGER.error("Exception in clicking from robot", ex);
        }
    }

    private void showMessage(String message) {
        if(!systemUtil.isAutomate()) {
            JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showError(String error) {
        if(!systemUtil.isAutomate()) {
            JOptionPane.showMessageDialog(this, error, "Information", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getChallenge(ActionEvent event) {
        PollingTask task = context.getBean(PollingTask.class);
        timer.schedule(task, systemUtil.getDelay());
    }

    private void schedule(ActionEvent event) {
        PollingTask task = context.getBean(PollingTask.class);
        timer.scheduleAtFixedRate(task, systemUtil.getDelay(), systemUtil.getPeriod());
    }

    private void cancelAutoReload(ActionEvent event) {
        timer.cancel();
        timer = context.getBean(Timer.class);
    }

    @Override
    public void addRequest(CaptchaRequest request) {
        if(request == null) {
            showMessage("No captcha challenge present.");
            return;
        }
        buttonAccept.setEnabled(false);
        listModel.clear();
        listModel.addElement(new CaptchaModel(request.getRequestId(), request.getHostName(), request.getSiteKey()));
        buttonAccept.setEnabled(true);
        if(systemUtil.isAutomate()) {
            buttonAccept.doClick();
        }
    }

    @Override
    public void disableEverything() {
        buttonAccept.setEnabled(false);
    }

    @Override
    public void enableEverything() {
        buttonAccept.setEnabled(true);
    }

    @Override
    public void solutionSubmitted() {
        listModel.clear();
    }

}
