/*
 * Copyright (C) 2014 beatsleigher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package eu.m4gkbeatz.androidtoolkit.ui;


import JDroidLib.android.controllers.*;
import JDroidLib.android.device.*;

import eu.m4gkbeatz.androidtoolkit.*;
import eu.m4gkbeatz.androidtoolkit.language.*;
import eu.m4gkbeatz.androidtoolkit.logging.*;
import static eu.m4gkbeatz.androidtoolkit.logging.Logger.Level;
import eu.m4gkbeatz.androidtoolkit.settings.*;
import eu.m4gkbeatz.androidtoolkit.ui.customui.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


/**
 *
 * @author beatsleigher
 */
public class UAT extends javax.swing.JFrame {
    
    //# =============== Instance Variables =============== #\\
    //========== Data types ==========
    private boolean debug;
    private boolean getLogcat = true;
    private boolean getDMESG = true;
    //========== Class instances ==========
    private Logger logger;
    private Logger.Level level;
    private SettingsManager settings;
    private ADBController adbController;
    private Devices deviceManager = null;
    //# =============== Static variables =============== #\\
    private static boolean ALREADY_ACTIVATED = false;
    
    public Device selectedDevice = null;

    public UAT(boolean debug, Logger logger, Level lvl, SettingsManager settings, ADBController adbController) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/eu/m4gkbeatz/androidtoolkit/resources/UniversalAndroidToolkit_logo.png")).getImage());
        this.debug = debug;
        this.logger = logger;
        this.level = lvl;
        this.settings = settings;
        this.adbController = adbController;
        deviceManager = new Devices(debug, logger, level, adbController, settings, this);
        deviceManager.setVisible(true);
        jTabbedPane2.setUI(new PPTTabbedPaneUI());
        try {
            loadTranslation();
        } catch (IOException | LanguageNotFoundException ex) {
            logger.log(level.ERROR, "Error occurred while applying translation: " + ex.toString() + "\n"
                    + "The error stack trace will be printed to the console! You can still use UAT.");
            ex.printStackTrace(System.err);
        }
    }
    
    private void loadTranslation() throws IOException, LanguageNotFoundException {
        LangFileParser parser = new LangFileParser();
        parser.parse(settings.getLanguage(), logger, debug);
        //# =============== Title Translations =============== #\\
        setTitle(parser.parse("title") + System.getProperty("user.name") + " | " + Main.VERSION_NO);
        //# =============== Translate Buttons and Labels =============== #\\
        // Android tab
        installApplication_androidButton.setText(parser.parse("installButton"));
        uninstallApplication_androidButton.setText(parser.parse("uninstallButton"));
        fileManager_androidButton.setText(parser.parse("fileManagerButton"));
        backupDevice_androidButton.setText(parser.parse("backupButton"));
        restoreDevice_androidButton.setText(parser.parse("restoreButton"));
        exploreRoot_androidButton.setText(parser.parse("exploreRootButton"));
        connectDevice_androidButton.setText(parser.parse("connectButton"));
        disconnectDevice_androidButton.setText(parser.parse("disconnectButton"));
        more_androidButton.setText(parser.parse("moreButton"));
        // Fastboot tab
        formatPartition_fastbootButton.setText(parser.parse("formatPartitionButton"));
        erasePartition_fastbootButton.setText(parser.parse("erasePartitionButton"));
        flashPartition_fastbootButton.setText(parser.parse("flashPartitionButton"));
        cleanFlashPartition_fastbootButton.setText(parser.parse("cleanFlashPartitionButton"));
        bootKernel_fastbootButton.setText(parser.parse("bootKernelButton"));
        unlockBootloader_fastbootButton.setText(parser.parse("unlockBootloaderButton"));
        relockBootloader_fastbootButton.setText(parser.parse("relockBootloaderButton"));
        updateDevice_fastbootButton.setText(parser.parse("updateDeviceButton"));
        // Device tab
            // Battery tab
        batteryLevel_batteryLabel.setText(parser.parse("batteryLevelLabel"));
        batteryHealth_batteryLabel.setText(parser.parse("batteryHealthLabel"));
        isInserted_batteryLabel.setText(parser.parse("isInsertedLabel"));
        isPoweredBy_batteryLabel.setText(parser.parse("isPoweredByLabel"));
        batteryStatus_batteryLabel.setText(parser.parse("batteryStatusLabel"));
        batteryScale_batteryLabel.setText(parser.parse("batteryScaleLabel"));
        batteryVoltage_batteryLabel.setText(parser.parse("batteryVoltageLabel"));
        batteryCurrent_batteryLabel.setText(parser.parse("batteryCurrentLabel"));
        batteryTemp_batteryLabel.setText(parser.parse("batteryTempLabel"));
        batteryTech_batteryLabel.setText(parser.parse("batteryTechLabel"));
            // Root tab
        superUserStatus_rootLabel.setText(parser.parse("superUserLabel"));
        superUserVersion_rootLabel.setText(parser.parse("suVersionLabel"));
        busyboxStatus_rootLabel.setText(parser.parse("busyboxLabel"));
        busyboxVersion_rootLabel.setText(parser.parse("busyboxVersionLabel"));
        cpuUsage_rootLabel.setText(parser.parse("cpuUsageLabel"));
            // Build Prop tab
        reload_buildPropButton.setText(parser.parse("reloadButton"));
        saveAs_buildPropButton.setText(parser.parse("saveAsButton"));
        saveToDevice_buildPropButton.setText(parser.parse("saveToDeviceButton"));
        // Toolkit tab
            // Settings tab
        refreshDevices_settingsButton.setText(parser.parse("refreshButton"));
        refreshInterval_settingsLabel.setText(parser.parse("intervalLabel"));
        intervalSeconds_settingsLabel.setText(parser.parse("secondsLabel"));
        checkForUpdates_settingsButton.setText(parser.parse("checkForUpdatesButton"));
        autoUpdate_settingsButton.setText(parser.parse("autoUpdateButton"));
        sendLogs_settingsButton.setText(parser.parse("sendLogsButton"));
        showLog_settingsButton.setText(parser.parse("showLogButton"));
            // Updates tab
        changelog_updatesLabel.setText(parser.parse("changelogLabel"));
        downloadJar_updatesButton.setText(parser.parse("downloadJarButton"));
        downloadRPM_updatesButton.setText(parser.parse("downloadRPMButton"));
        downloadDEB_updatesButton.setText(parser.parse("downloadDEBButton"));
        downloadEXE_updatesButton.setText(parser.parse("downloadEXEButton"));
        // Toolbar
        showDevices_toolbarButton.setText(parser.parse("showDevicesButton"));
        //# =============== Menu Translations =============== #\\
            // ADB
        startServer_adbMenu.setText(parser.parse("startServerItem"));
        stopServer_adbMenu.setText(parser.parse("stopServerItem"));
        restartServer_adbMenu.setText(parser.parse("restartServerItem"));
        connectToDevice_adbMenu.setText(parser.parse("connectDeviceItem"));
        reboot_adbMenu.setText(parser.parse("rebootItem"));
        toAndroid_adbMenu.setText(parser.parse("toAndroidItem"));
        toFastboot_adbMenu.setText(parser.parse("toFastbootItem"));
        toRecovery_adbMenu.setText(parser.parse("toRecoveryItem"));
            // Fastboot
        reboot_fastbootMenu.setText(parser.parse("rebootItem"));
        toAndroid_fastbootMenu.setText(parser.parse("toAndroidItem"));
        toFastboot_fastbootMenu.setText(parser.parse("toFastbootItem"));
            // UAT
        checkForUpdates_uatMenu.setText(parser.parse("checkForUpdatesItem"));
        installADB_uatMenu.setText(parser.parse("installADBItem"));
        about_uatMenu.setText(parser.parse("aboutItem"));
        exit_uatMenu.setText(parser.parse("exitItem"));
        //# =============== Panel Translations =============== #\\
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        showDevices_toolbarButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        androidTab = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        installApplication_androidButton = new javax.swing.JButton();
        uninstallApplication_androidButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        fileManager_androidButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        backupDevice_androidButton = new javax.swing.JButton();
        restoreDevice_androidButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        exploreRoot_androidButton = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logcat_androidTextArea = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dmesg_androidTextArea = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        connectDevice_androidButton = new javax.swing.JButton();
        disconnectDevice_androidButton = new javax.swing.JButton();
        more_androidButton = new javax.swing.JButton();
        fastbootTab = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        formatPartition_fastbootButton = new javax.swing.JButton();
        erasePartition_fastbootButton = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        flashPartition_fastbootButton = new javax.swing.JButton();
        cleanFlashPartition_fastbootButton = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        bootKernel_fastbootButton = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        unlockBootloader_fastbootButton = new javax.swing.JButton();
        relockBootloader_fastbootButton = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        updateDevice_fastbootButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        batteryTab = new javax.swing.JPanel();
        batteryLevel_batteryLabel = new javax.swing.JLabel();
        batteryHealth_batteryLabel = new javax.swing.JLabel();
        isInserted_batteryLabel = new javax.swing.JLabel();
        isPoweredBy_batteryLabel = new javax.swing.JLabel();
        batteryStatus_batteryLabel = new javax.swing.JLabel();
        batteryScale_batteryLabel = new javax.swing.JLabel();
        batteryVoltage_batteryLabel = new javax.swing.JLabel();
        batteryCurrent_batteryLabel = new javax.swing.JLabel();
        batteryTemp_batteryLabel = new javax.swing.JLabel();
        batteryTech_batteryLabel = new javax.swing.JLabel();
        rootTab = new javax.swing.JPanel();
        superUserStatus_rootLabel = new javax.swing.JLabel();
        superUserVersion_rootLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        busyboxStatus_rootLabel = new javax.swing.JLabel();
        busyboxVersion_rootLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cpuUsage_rootLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        buildPropTab = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        reload_buildPropButton = new javax.swing.JButton();
        saveAs_buildPropButton = new javax.swing.JButton();
        saveToDevice_buildPropButton = new javax.swing.JButton();
        fileManagerTab = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        preferenceTab = new javax.swing.JPanel();
        devices_settingsPanel = new javax.swing.JPanel();
        refreshDevices_settingsButton = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        refreshInterval_settingsLabel = new javax.swing.JLabel();
        intervalSeconds_settingsLabel = new javax.swing.JLabel();
        updates_settingsPanel = new javax.swing.JPanel();
        checkForUpdates_settingsButton = new javax.swing.JToggleButton();
        autoUpdate_settingsButton = new javax.swing.JToggleButton();
        logs_settingsPanel = new javax.swing.JPanel();
        sendLogs_settingsButton = new javax.swing.JToggleButton();
        showLog_settingsButton = new javax.swing.JToggleButton();
        lookAndFeel_settingsPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        updatesTab = new javax.swing.JPanel();
        updateStatus_updatesLabel = new javax.swing.JLabel();
        jProgressBar2 = new javax.swing.JProgressBar();
        downloadJar_updatesButton = new javax.swing.JButton();
        downloadRPM_updatesButton = new javax.swing.JButton();
        downloadDEB_updatesButton = new javax.swing.JButton();
        downloadEXE_updatesButton = new javax.swing.JButton();
        changelog_updatesLabel = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        adbMenu = new javax.swing.JMenu();
        startServer_adbMenu = new javax.swing.JMenuItem();
        stopServer_adbMenu = new javax.swing.JMenuItem();
        restartServer_adbMenu = new javax.swing.JMenuItem();
        connectToDevice_adbMenu = new javax.swing.JMenuItem();
        reboot_adbMenu = new javax.swing.JMenu();
        toAndroid_adbMenu = new javax.swing.JMenuItem();
        toFastboot_adbMenu = new javax.swing.JMenuItem();
        toRecovery_adbMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        reboot_fastbootMenu = new javax.swing.JMenu();
        toAndroid_fastbootMenu = new javax.swing.JMenuItem();
        toFastboot_fastbootMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        checkForUpdates_uatMenu = new javax.swing.JMenuItem();
        installADB_uatMenu = new javax.swing.JMenuItem();
        about_uatMenu = new javax.swing.JMenuItem();
        exit_uatMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jToolBar1.setFloatable(false);

        showDevices_toolbarButton.setText("Show Devices");
        showDevices_toolbarButton.setFocusable(false);
        showDevices_toolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showDevices_toolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(showDevices_toolbarButton);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Applications"));

        installApplication_androidButton.setText("Install");

        uninstallApplication_androidButton.setText("Uninstall");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(installApplication_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(uninstallApplication_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(installApplication_androidButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uninstallApplication_androidButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Files"));

        fileManager_androidButton.setText("File Manager");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileManager_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileManager_androidButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Backups"));

        backupDevice_androidButton.setText("Backup");

        restoreDevice_androidButton.setText("Restore");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(backupDevice_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(restoreDevice_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backupDevice_androidButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(restoreDevice_androidButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Rooting"));

        exploreRoot_androidButton.setText("Explore!");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exploreRoot_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exploreRoot_androidButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logcat_androidTextArea.setColumns(20);
        logcat_androidTextArea.setRows(5);
        jScrollPane1.setViewportView(logcat_androidTextArea);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Logcat", null, jPanel9, "CTRL+S = Save Log...\nCTRL+C = Clear Log...\nCTRL+S = Start/Stop Logging\n");

        dmesg_androidTextArea.setColumns(20);
        dmesg_androidTextArea.setRows(5);
        jScrollPane2.setViewportView(dmesg_androidTextArea);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("DMESG", null, jPanel10, "CTRL+S = Save Log...\nCTRL+C = Clear Log...\nCTRL+S = Start/Stop Logging\n");

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("ADB via TCP"));

        connectDevice_androidButton.setText("Connect...");

        disconnectDevice_androidButton.setText("Disconnect...");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(disconnectDevice_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectDevice_androidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectDevice_androidButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(disconnectDevice_androidButton)
                .addContainerGap())
        );

        more_androidButton.setText("More...");

        javax.swing.GroupLayout androidTabLayout = new javax.swing.GroupLayout(androidTab);
        androidTab.setLayout(androidTabLayout);
        androidTabLayout.setHorizontalGroup(
            androidTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(androidTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(androidTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(androidTabLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, androidTabLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(more_androidButton)))
                .addContainerGap())
        );
        androidTabLayout.setVerticalGroup(
            androidTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(androidTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(androidTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(androidTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(more_androidButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Android", androidTab);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Formatting"));

        formatPartition_fastbootButton.setText("Format Partition");

        erasePartition_fastbootButton.setText("Erase Partition");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(formatPartition_fastbootButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(erasePartition_fastbootButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(formatPartition_fastbootButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(erasePartition_fastbootButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Flashing"));

        flashPartition_fastbootButton.setText("Flash Partition");

        cleanFlashPartition_fastbootButton.setText("Clean Flash Partition");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cleanFlashPartition_fastbootButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(flashPartition_fastbootButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(flashPartition_fastbootButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cleanFlashPartition_fastbootButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Boot"));

        bootKernel_fastbootButton.setText("Boot Kernel");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bootKernel_fastbootButton)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bootKernel_fastbootButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Lock State"));

        unlockBootloader_fastbootButton.setText("Unlock Bootloader");

        relockBootloader_fastbootButton.setText("Relock Bootloader");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(unlockBootloader_fastbootButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(relockBootloader_fastbootButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unlockBootloader_fastbootButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relockBootloader_fastbootButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Update"));

        updateDevice_fastbootButton.setText("Update Device");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateDevice_fastbootButton)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateDevice_fastbootButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout fastbootTabLayout = new javax.swing.GroupLayout(fastbootTab);
        fastbootTab.setLayout(fastbootTabLayout);
        fastbootTabLayout.setHorizontalGroup(
            fastbootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fastbootTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fastbootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fastbootTabLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        fastbootTabLayout.setVerticalGroup(
            fastbootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fastbootTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fastbootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fastbootTabLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(fastbootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Fastboot", fastbootTab);

        batteryLevel_batteryLabel.setText("Battery Level: 000%");

        batteryHealth_batteryLabel.setText("Battery Health: GOOD");

        isInserted_batteryLabel.setText("Is Inserted:  true");

        isPoweredBy_batteryLabel.setText("Is Powered By: USB");

        batteryStatus_batteryLabel.setText("Battery Status: CHARGING");

        batteryScale_batteryLabel.setText("Battery Scale: 100");

        batteryVoltage_batteryLabel.setText("Battery Voltage: 4207");

        batteryCurrent_batteryLabel.setText("Battery Current (MAmps): 400");

        batteryTemp_batteryLabel.setText("Battery Temperature: 30");

        batteryTech_batteryLabel.setText("Battery Technology: LI-ION");

        javax.swing.GroupLayout batteryTabLayout = new javax.swing.GroupLayout(batteryTab);
        batteryTab.setLayout(batteryTabLayout);
        batteryTabLayout.setHorizontalGroup(
            batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(batteryTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(batteryLevel_batteryLabel)
                    .addComponent(batteryHealth_batteryLabel))
                .addGap(22, 22, 22)
                .addGroup(batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(isInserted_batteryLabel)
                    .addComponent(isPoweredBy_batteryLabel))
                .addGap(26, 26, 26)
                .addGroup(batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(batteryTech_batteryLabel)
                    .addComponent(batteryTemp_batteryLabel)
                    .addComponent(batteryCurrent_batteryLabel)
                    .addComponent(batteryVoltage_batteryLabel)
                    .addComponent(batteryScale_batteryLabel)
                    .addComponent(batteryStatus_batteryLabel))
                .addContainerGap(167, Short.MAX_VALUE))
        );
        batteryTabLayout.setVerticalGroup(
            batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(batteryTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batteryLevel_batteryLabel)
                    .addComponent(isInserted_batteryLabel)
                    .addComponent(batteryStatus_batteryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(batteryTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batteryHealth_batteryLabel)
                    .addComponent(isPoweredBy_batteryLabel)
                    .addComponent(batteryScale_batteryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batteryVoltage_batteryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batteryCurrent_batteryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batteryTemp_batteryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batteryTech_batteryLabel)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Battery", batteryTab);

        superUserStatus_rootLabel.setText("Super User: Installed");

        superUserVersion_rootLabel.setText("SU Version: 0.0.0.0");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        busyboxStatus_rootLabel.setText("Busybox: Installed");

        busyboxVersion_rootLabel.setText("Busybox Version: 0.0.0.0");

        cpuUsage_rootLabel.setText("CPU Usage: 1000 / 1000 / 1000");

        jScrollPane3.setViewportView(jList1);

        javax.swing.GroupLayout rootTabLayout = new javax.swing.GroupLayout(rootTab);
        rootTab.setLayout(rootTabLayout);
        rootTabLayout.setHorizontalGroup(
            rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(cpuUsage_rootLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(rootTabLayout.createSequentialGroup()
                        .addGroup(rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(superUserStatus_rootLabel)
                            .addComponent(superUserVersion_rootLabel))
                        .addGap(183, 183, 183)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(busyboxStatus_rootLabel)
                            .addComponent(busyboxVersion_rootLabel))
                        .addGap(0, 152, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        rootTabLayout.setVerticalGroup(
            rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootTabLayout.createSequentialGroup()
                        .addComponent(busyboxStatus_rootLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(busyboxVersion_rootLabel))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rootTabLayout.createSequentialGroup()
                        .addComponent(superUserStatus_rootLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(superUserVersion_rootLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cpuUsage_rootLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Root and CPU", rootTab);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        reload_buildPropButton.setText("Reload");

        saveAs_buildPropButton.setText("Save As...");

        saveToDevice_buildPropButton.setText("Save To Device");

        javax.swing.GroupLayout buildPropTabLayout = new javax.swing.GroupLayout(buildPropTab);
        buildPropTab.setLayout(buildPropTabLayout);
        buildPropTabLayout.setHorizontalGroup(
            buildPropTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buildPropTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(buildPropTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reload_buildPropButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveAs_buildPropButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveToDevice_buildPropButton, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                .addContainerGap())
        );
        buildPropTabLayout.setVerticalGroup(
            buildPropTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buildPropTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buildPropTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buildPropTabLayout.createSequentialGroup()
                        .addComponent(reload_buildPropButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAs_buildPropButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveToDevice_buildPropButton)
                        .addGap(0, 183, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        jTabbedPane3.addTab("Build Prop Manager", buildPropTab);

        jScrollPane5.setViewportView(jList2);

        jButton4.setText(">>");

        jButton5.setText("<<");

        jButton6.setText("⌂ ");

        jProgressBar1.setOrientation(1);
        jProgressBar1.setIndeterminate(true);

        jScrollPane6.setViewportView(jList3);

        javax.swing.GroupLayout fileManagerTabLayout = new javax.swing.GroupLayout(fileManagerTab);
        fileManagerTab.setLayout(fileManagerTabLayout);
        fileManagerTabLayout.setHorizontalGroup(
            fileManagerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileManagerTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fileManagerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addContainerGap())
        );
        fileManagerTabLayout.setVerticalGroup(
            fileManagerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileManagerTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fileManagerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(fileManagerTabLayout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );

        jTabbedPane3.addTab("File Manager", fileManagerTab);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Device", jPanel3);

        preferenceTab.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        devices_settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Devices"));

        refreshDevices_settingsButton.setSelected(true);
        refreshDevices_settingsButton.setText("Refresh");

        jTextField1.setText("30");

        refreshInterval_settingsLabel.setText("Interval:");

        intervalSeconds_settingsLabel.setText("s");

        javax.swing.GroupLayout devices_settingsPanelLayout = new javax.swing.GroupLayout(devices_settingsPanel);
        devices_settingsPanel.setLayout(devices_settingsPanelLayout);
        devices_settingsPanelLayout.setHorizontalGroup(
            devices_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devices_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(devices_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(devices_settingsPanelLayout.createSequentialGroup()
                        .addGroup(devices_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(refreshInterval_settingsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(intervalSeconds_settingsLabel)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, devices_settingsPanelLayout.createSequentialGroup()
                        .addComponent(refreshDevices_settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        devices_settingsPanelLayout.setVerticalGroup(
            devices_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devices_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(refreshDevices_settingsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(refreshInterval_settingsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(devices_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalSeconds_settingsLabel))
                .addContainerGap())
        );

        updates_settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Updates"));

        checkForUpdates_settingsButton.setSelected(true);
        checkForUpdates_settingsButton.setText("Check...");

        autoUpdate_settingsButton.setText("Auto-Update");

        javax.swing.GroupLayout updates_settingsPanelLayout = new javax.swing.GroupLayout(updates_settingsPanel);
        updates_settingsPanel.setLayout(updates_settingsPanelLayout);
        updates_settingsPanelLayout.setHorizontalGroup(
            updates_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updates_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updates_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkForUpdates_settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(updates_settingsPanelLayout.createSequentialGroup()
                        .addComponent(autoUpdate_settingsButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        updates_settingsPanelLayout.setVerticalGroup(
            updates_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updates_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkForUpdates_settingsButton)
                .addGap(18, 18, 18)
                .addComponent(autoUpdate_settingsButton)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        logs_settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));

        sendLogs_settingsButton.setSelected(true);
        sendLogs_settingsButton.setText("Send Logs");

        showLog_settingsButton.setSelected(true);
        showLog_settingsButton.setText("Show Log");

        javax.swing.GroupLayout logs_settingsPanelLayout = new javax.swing.GroupLayout(logs_settingsPanel);
        logs_settingsPanel.setLayout(logs_settingsPanelLayout);
        logs_settingsPanelLayout.setHorizontalGroup(
            logs_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logs_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logs_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendLogs_settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(showLog_settingsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        logs_settingsPanelLayout.setVerticalGroup(
            logs_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logs_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sendLogs_settingsButton)
                .addGap(18, 18, 18)
                .addComponent(showLog_settingsButton)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lookAndFeel_settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Theme"));

        jScrollPane7.setViewportView(jList4);

        javax.swing.GroupLayout lookAndFeel_settingsPanelLayout = new javax.swing.GroupLayout(lookAndFeel_settingsPanel);
        lookAndFeel_settingsPanel.setLayout(lookAndFeel_settingsPanelLayout);
        lookAndFeel_settingsPanelLayout.setHorizontalGroup(
            lookAndFeel_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lookAndFeel_settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addContainerGap())
        );
        lookAndFeel_settingsPanelLayout.setVerticalGroup(
            lookAndFeel_settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lookAndFeel_settingsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Language"));

        jScrollPane8.setViewportView(jList5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout preferenceTabLayout = new javax.swing.GroupLayout(preferenceTab);
        preferenceTab.setLayout(preferenceTabLayout);
        preferenceTabLayout.setHorizontalGroup(
            preferenceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferenceTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(preferenceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(preferenceTabLayout.createSequentialGroup()
                        .addComponent(devices_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updates_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logs_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lookAndFeel_settingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        preferenceTabLayout.setVerticalGroup(
            preferenceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferenceTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(preferenceTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lookAndFeel_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updates_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(devices_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logs_settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Preferences", preferenceTab);

        updateStatus_updatesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eu/m4gkbeatz/androidtoolkit/resources/updates/no-update.png"))); // NOI18N
        updateStatus_updatesLabel.setText("No updates found...");

        downloadJar_updatesButton.setText("Download Jar File(s)...");

        downloadRPM_updatesButton.setText("Download Installer (.RPM)");

        downloadDEB_updatesButton.setText("Download Installer (.DEB)");

        downloadEXE_updatesButton.setText("Download Bin (.EXE)");

        changelog_updatesLabel.setText("Changes:");

        jScrollPane9.setViewportView(jList6);

        javax.swing.GroupLayout updatesTabLayout = new javax.swing.GroupLayout(updatesTab);
        updatesTab.setLayout(updatesTabLayout);
        updatesTabLayout.setHorizontalGroup(
            updatesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updatesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(updatesTabLayout.createSequentialGroup()
                        .addGroup(updatesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updateStatus_updatesLabel)
                            .addComponent(changelog_updatesLabel)
                            .addGroup(updatesTabLayout.createSequentialGroup()
                                .addComponent(downloadJar_updatesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadRPM_updatesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadDEB_updatesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadEXE_updatesButton)))
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addComponent(jScrollPane9))
                .addContainerGap())
        );
        updatesTabLayout.setVerticalGroup(
            updatesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateStatus_updatesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changelog_updatesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(updatesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(downloadJar_updatesButton)
                    .addComponent(downloadRPM_updatesButton)
                    .addComponent(downloadDEB_updatesButton)
                    .addComponent(downloadEXE_updatesButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Updates", updatesTab);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Toolkit", jPanel4);

        adbMenu.setText("ADB");

        startServer_adbMenu.setText("Start Server");
        adbMenu.add(startServer_adbMenu);

        stopServer_adbMenu.setText("Stop Server");
        adbMenu.add(stopServer_adbMenu);

        restartServer_adbMenu.setText("Restart Server");
        adbMenu.add(restartServer_adbMenu);

        connectToDevice_adbMenu.setText("Connect to Device");
        adbMenu.add(connectToDevice_adbMenu);

        reboot_adbMenu.setText("Reboot");

        toAndroid_adbMenu.setText("to Android");
        reboot_adbMenu.add(toAndroid_adbMenu);

        toFastboot_adbMenu.setText("to Fastboot");
        reboot_adbMenu.add(toFastboot_adbMenu);

        toRecovery_adbMenu.setText("to Recovery");
        reboot_adbMenu.add(toRecovery_adbMenu);

        adbMenu.add(reboot_adbMenu);

        jMenuBar1.add(adbMenu);

        jMenu2.setText("Fastboot");

        reboot_fastbootMenu.setText("Reboot");

        toAndroid_fastbootMenu.setText("to Android");
        reboot_fastbootMenu.add(toAndroid_fastbootMenu);

        toFastboot_fastbootMenu.setText("to Fastboot");
        reboot_fastbootMenu.add(toFastboot_fastbootMenu);

        jMenu2.add(reboot_fastbootMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Universal Android Toolkit");

        checkForUpdates_uatMenu.setText("Check for Updates");
        jMenu3.add(checkForUpdates_uatMenu);

        installADB_uatMenu.setText("Install ADB to Custom Location...");
        jMenu3.add(installADB_uatMenu);

        about_uatMenu.setText("About");
        jMenu3.add(about_uatMenu);

        exit_uatMenu.setText("Exit");
        jMenu3.add(exit_uatMenu);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //# =============== Event Listeners =============== #\\
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (!ALREADY_ACTIVATED) {
            if (debug)
                logger.log(level.DEBUG, "First-time activation: Starting logcat and DMESG.");
            if (selectedDevice != null) {
                getLogcat(selectedDevice);
                getDMESG(selectedDevice);
            }
            
            ALREADY_ACTIVATED = true;
        }
    }//GEN-LAST:event_formWindowActivated

    //# =============== Logcat and DMESG =============== #\\
    private void getLogcat(final Device device) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final File ADB = adbController.getADB();
                    ProcessBuilder process = new ProcessBuilder();
                    List<String> args = new ArrayList<>();
                    args.add(ADB.getAbsolutePath());
                    args.add("-s");
                    args.add(device.getSerial());
                    args.add("logcat");
                    process.command(args);
                    process.redirectErrorStream(true);
                    Process pr = process.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    String line = null;
                    logcat_androidTextArea.setText("");
                    while (getLogcat && (line = reader.readLine()) != null) {
                        logcat_androidTextArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    logger.log(level.ERROR, "Error occurred while reading device's (" + device.getSerial() + ") logcat!\n\tError stack trace will be printed to console!");
                    JOptionPane.showMessageDialog(null, "ERROR: An error occurred while reading the device's (" + device.getSerial() + ") logcat.\n"
                            + "Please read the program's output and send the stack trace to the developer.", "Error Reading Logcat", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(System.err);
                    interrupt();
                }
                interrupt();
            }
        }.start();
    }
    
    private void getDMESG(final Device device) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final File ADB = adbController.getADB();
                    ProcessBuilder process = new ProcessBuilder();
                    List<String> args = new ArrayList<>();
                    args.add(ADB.getAbsolutePath());
                    args.add("-s");
                    args.add(device.getSerial());
                    args.add("shell");
                    args.add("dmesg");
                    process.command(args);
                    process.redirectErrorStream(true);
                    Process pr = process.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    String line = null;
                    dmesg_androidTextArea.setText("");
                    while (getLogcat && (line = reader.readLine()) != null) {
                        dmesg_androidTextArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    logger.log(level.ERROR, "Error occurred while reading device's (" + device.getSerial() + ") logcat!\n\tError stack trace will be printed to console!");
                    JOptionPane.showMessageDialog(null, "ERROR: An error occurred while reading the device's (" + device.getSerial() + ") logcat.\n"
                            + "Please read the program's output and send the stack trace to the developer.", "Error Reading Logcat", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(System.err);
                    interrupt();
                }
                interrupt();
            }
        }.start();
    }
    
    public void stopLogcat() {
        getLogcat = false;
        for (int i = 0; i < 500; i++) {}
    }
    
    public void stopDMESG() {
        getDMESG = true;
        for (int i = 0; i < 500; i++) {} 
    }
    
    public void startLogcat(final Device device) {
        getLogcat = true;
        getLogcat(device);
    }
    
    public void startDMESG(final Device device) {
        getDMESG = true;
        getDMESG(device);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem about_uatMenu;
    private javax.swing.JMenu adbMenu;
    private javax.swing.JPanel androidTab;
    private javax.swing.JToggleButton autoUpdate_settingsButton;
    private javax.swing.JButton backupDevice_androidButton;
    private javax.swing.JLabel batteryCurrent_batteryLabel;
    private javax.swing.JLabel batteryHealth_batteryLabel;
    private javax.swing.JLabel batteryLevel_batteryLabel;
    private javax.swing.JLabel batteryScale_batteryLabel;
    private javax.swing.JLabel batteryStatus_batteryLabel;
    private javax.swing.JPanel batteryTab;
    private javax.swing.JLabel batteryTech_batteryLabel;
    private javax.swing.JLabel batteryTemp_batteryLabel;
    private javax.swing.JLabel batteryVoltage_batteryLabel;
    private javax.swing.JButton bootKernel_fastbootButton;
    private javax.swing.JPanel buildPropTab;
    private javax.swing.JLabel busyboxStatus_rootLabel;
    private javax.swing.JLabel busyboxVersion_rootLabel;
    private javax.swing.JLabel changelog_updatesLabel;
    private javax.swing.JToggleButton checkForUpdates_settingsButton;
    private javax.swing.JMenuItem checkForUpdates_uatMenu;
    private javax.swing.JButton cleanFlashPartition_fastbootButton;
    private javax.swing.JButton connectDevice_androidButton;
    private javax.swing.JMenuItem connectToDevice_adbMenu;
    private javax.swing.JLabel cpuUsage_rootLabel;
    private javax.swing.JPanel devices_settingsPanel;
    private javax.swing.JButton disconnectDevice_androidButton;
    private javax.swing.JTextArea dmesg_androidTextArea;
    private javax.swing.JButton downloadDEB_updatesButton;
    private javax.swing.JButton downloadEXE_updatesButton;
    private javax.swing.JButton downloadJar_updatesButton;
    private javax.swing.JButton downloadRPM_updatesButton;
    private javax.swing.JButton erasePartition_fastbootButton;
    private javax.swing.JMenuItem exit_uatMenu;
    private javax.swing.JButton exploreRoot_androidButton;
    private javax.swing.JPanel fastbootTab;
    private javax.swing.JPanel fileManagerTab;
    private javax.swing.JButton fileManager_androidButton;
    private javax.swing.JButton flashPartition_fastbootButton;
    private javax.swing.JButton formatPartition_fastbootButton;
    private javax.swing.JMenuItem installADB_uatMenu;
    private javax.swing.JButton installApplication_androidButton;
    private javax.swing.JLabel intervalSeconds_settingsLabel;
    private javax.swing.JLabel isInserted_batteryLabel;
    private javax.swing.JLabel isPoweredBy_batteryLabel;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JList jList5;
    private javax.swing.JList jList6;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea logcat_androidTextArea;
    private javax.swing.JPanel logs_settingsPanel;
    private javax.swing.JPanel lookAndFeel_settingsPanel;
    private javax.swing.JButton more_androidButton;
    private javax.swing.JPanel preferenceTab;
    private javax.swing.JMenu reboot_adbMenu;
    private javax.swing.JMenu reboot_fastbootMenu;
    private javax.swing.JToggleButton refreshDevices_settingsButton;
    private javax.swing.JLabel refreshInterval_settingsLabel;
    private javax.swing.JButton reload_buildPropButton;
    private javax.swing.JButton relockBootloader_fastbootButton;
    private javax.swing.JMenuItem restartServer_adbMenu;
    private javax.swing.JButton restoreDevice_androidButton;
    private javax.swing.JPanel rootTab;
    private javax.swing.JButton saveAs_buildPropButton;
    private javax.swing.JButton saveToDevice_buildPropButton;
    private javax.swing.JToggleButton sendLogs_settingsButton;
    private javax.swing.JButton showDevices_toolbarButton;
    private javax.swing.JToggleButton showLog_settingsButton;
    private javax.swing.JMenuItem startServer_adbMenu;
    private javax.swing.JMenuItem stopServer_adbMenu;
    private javax.swing.JLabel superUserStatus_rootLabel;
    private javax.swing.JLabel superUserVersion_rootLabel;
    private javax.swing.JMenuItem toAndroid_adbMenu;
    private javax.swing.JMenuItem toAndroid_fastbootMenu;
    private javax.swing.JMenuItem toFastboot_adbMenu;
    private javax.swing.JMenuItem toFastboot_fastbootMenu;
    private javax.swing.JMenuItem toRecovery_adbMenu;
    private javax.swing.JButton uninstallApplication_androidButton;
    private javax.swing.JButton unlockBootloader_fastbootButton;
    private javax.swing.JButton updateDevice_fastbootButton;
    private javax.swing.JLabel updateStatus_updatesLabel;
    private javax.swing.JPanel updatesTab;
    private javax.swing.JPanel updates_settingsPanel;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
    
    //# =============== Custom Classes =============== #\\
    
    private class LangFileParser {
        
        //# =============== Variables =============== #\\
        String translationFile = null;
        
        public void parse(Language lang, Logger logger, boolean debug) throws IOException {
            URL languageFile = this.getClass().getResource("/eu/m4gkbeatz/androidtoolkit/resources/langs/" + lang + ".lang");
            if (debug)
                logger.log(Level.DEBUG, "Attempting translation from file: " + languageFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(languageFile.openStream()));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                strBuilder.append(line + "\n");
            reader.close();
            translationFile = strBuilder.toString();
        }
        
        public String parse(String item) throws IOException {
            BufferedReader reader = new BufferedReader(new StringReader(translationFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(item)) {
                    String[] arr = line.split("=");
                    return arr[1];
                }
            }
            return "???";
        }
        
    }
    
    private class LanguageNotFoundException extends Exception {
        
        public LanguageNotFoundException() {}
        
        public LanguageNotFoundException(String msg) { super(msg); }
        
    }
}
