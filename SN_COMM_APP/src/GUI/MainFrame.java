package GUI;

import at.htlklu.elektronik.schnittstellen.SerielleSchnittstelle;
import at.htlklu.elektronik.schnittstellen.StringEvent;
import at.htlklu.elektronik.schnittstellen.StringListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import serial.SerialComm;

public class MainFrame extends javax.swing.JFrame implements StringListener {
    // Commands to be send to RF-Modul (Coordinator)
    //----------------------------------------------------

    public static final String GETDATA = "#getdata";
    public static final String GETECHO = "#getecho";
    public static final String GETNODES = "#getnodes";
    public static final String GETCONNECTEDNODE = "#getnodeaddress";
    public static final int COMMAND_GETDATA = 0;
    public static final int COMMAND_GETECHO = 1;
    public static final int COMMAND_GETNODES = 2;
    public static final int COMMAND_CLOSE = 3;
    public static final int COMMAND_GETCONNECTEDNODE = 4;
    //---------------------------------------------------
    // Commands reveived from RF-Modul (Coordinator)
    //----------------------------------------------------
    //beginn of data protocol 
    private static final String BODATA = "#BOData";
    //end of data protocol 
    private static final String EODATA = "#EOData";
    // beginn of node list
    private static final String BONL = "#BONL";
    // nodelistentry startswith
    private static final String NODEENTRY = "#shortaddr";
    // private static final String NODEENTRYRO = "#shortaddrRo";
    // end of node list
    private static final String EONL = "#EONL";
    //response received after GETECHO
    private static final String ECHORESPONSE = "#pingresponse";
    //---------------------------------------------------
    // states for implement protocol logic
    public static final int STATE_IDLE = 0;
    public static final int STATE_LISTENING = 1;
    public static final int STATE_LISTENING_NL = 2;
    private SerielleSchnittstelle com;
    private String strR;
    private int state;
    private ArrayList<String> dataString = new ArrayList<String>();
    private ArrayList<String> nodes = new ArrayList<String>();
    private int connectedNode;

    /** Creates new form MainFrame */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();

        getRootPane().setDefaultButton(jbtnSend);

        jbtnGetIntervall.setVisible(false);
        jbtnSetIntervall.setVisible(false);
        jbtnGetTime.setVisible(false);
        jbtnSetTime.setVisible(false);

        ArrayList<String> ports = SerielleSchnittstelle.listPorts();
        for (int i = 0; i < ports.size(); i++) {
            jcbCom.addItem(ports.get(i));
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnConnect = new javax.swing.JButton();
        jcbCom = new javax.swing.JComboBox();
        jcbBaud = new javax.swing.JComboBox();
        jtxtSend = new javax.swing.JTextField();
        jbtnSend = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListNodes = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jbtnReloadNodeList = new javax.swing.JButton();
        jbtnGetData = new javax.swing.JButton();
        jbtnGetEcho = new javax.swing.JButton();
        jbtnSetTime = new javax.swing.JButton();
        jbtnGetTime = new javax.swing.JButton();
        jbtnGetIntervall = new javax.swing.JButton();
        jbtnSetIntervall = new javax.swing.JButton();
        jlblConnected = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtxtOut = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client-Modul Testanwendung");

        jbtnConnect.setText("Connect");
        jbtnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnConnectActionPerformed(evt);
            }
        });

        jcbBaud.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "9600", "19200", "38400", "115200" }));

        jbtnSend.setText("send");
        jbtnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSendActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane1.setViewportView(jListNodes);

        jLabel1.setText("Nodes");

        jbtnReloadNodeList.setText("reload nodes");
        jbtnReloadNodeList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnReloadNodeListActionPerformed(evt);
            }
        });

        jbtnGetData.setText("getdata");
        jbtnGetData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGetDataActionPerformed(evt);
            }
        });

        jbtnGetEcho.setText("getecho");
        jbtnGetEcho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGetEchoActionPerformed(evt);
            }
        });

        jbtnSetTime.setText("settime");

        jbtnGetTime.setText("gettime");

        jbtnGetIntervall.setText("getintervall");

        jbtnSetIntervall.setText("setintervall");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnGetIntervall, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jbtnGetData, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jbtnReloadNodeList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnGetEcho, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jbtnSetTime, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jbtnGetTime, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jbtnSetIntervall, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jbtnGetData, jbtnGetEcho, jbtnReloadNodeList});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtnSetIntervall)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnGetIntervall)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnGetTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnSetTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnGetEcho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnGetData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnReloadNodeList))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                .addContainerGap())
        );

        jlblConnected.setText("disconnected:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        jtxtOut.setColumns(20);
        jtxtOut.setEditable(false);
        jtxtOut.setLineWrap(true);
        jtxtOut.setRows(5);
        jtxtOut.setAutoscrolls(true);
        jtxtOut.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jtxtOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtxtOutMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtxtOut);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlblConnected, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtxtSend, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbBaud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbCom, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnConnect)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnSend)
                    .addComponent(jcbBaud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbCom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnConnect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblConnected)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnConnectActionPerformed
        if (com != null) {
            com.removeStringListener(this);
            com.disconnect();

        } else {
            try {
                com = new SerielleSchnittstelle((String) jcbCom.getSelectedItem(), Integer.parseInt(jcbBaud.getSelectedItem().toString()), 8, 1, 0);
                com.addStringListener(this);
                com.setStringDelimiter(SerielleSchnittstelle.DELIMITER_LF);
                sendCommand(COMMAND_GETNODES, 0);
                sendCommand(COMMAND_GETCONNECTEDNODE, 0);
                jlblConnected.setText("Connected to: shortAddress: " + connectedNode);
            } catch (Exception ex) {
                jlblConnected.setText("unable to connect");
            }

        }


    }//GEN-LAST:event_jbtnConnectActionPerformed

    private void jbtnGetDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGetDataActionPerformed
      
        try{if (com.isConnected()) {
            System.out.println(Integer.parseInt(jListNodes.getSelectedValue().toString()));
            sendCommand(COMMAND_GETDATA, Integer.parseInt(jListNodes.getSelectedValue().toString()));
        } else {
            JOptionPane.showMessageDialog(this, "not connected");
        }
        }catch(NullPointerException ex){
            JOptionPane.showMessageDialog(this, "Wählen Sie eine Node bevor Sie einen Befehl ausführen", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jbtnGetDataActionPerformed

    private void jbtnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSendActionPerformed
        if (com != null) {
            sendString_d(jtxtSend.getText());
        }
    }//GEN-LAST:event_jbtnSendActionPerformed

    private void jbtnGetEchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGetEchoActionPerformed
        if (com != null) {
            sendCommand(COMMAND_GETECHO, Integer.parseInt(jListNodes.getSelectedValue().toString()));
        }
    }//GEN-LAST:event_jbtnGetEchoActionPerformed

    private void jbtnReloadNodeListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReloadNodeListActionPerformed
        sendCommand(COMMAND_GETNODES, 0);

    }//GEN-LAST:event_jbtnReloadNodeListActionPerformed

    private void jtxtOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtxtOutMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            jtxtOut.setText("");
        }
    }//GEN-LAST:event_jtxtOutMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    public void sendCommand(int command, int nodeAddr) {
        if (com.isConnected()) {
            switch (command) {
                case COMMAND_GETDATA:
                    sendString_d(GETDATA + " " + nodeAddr);
                    break;
                case COMMAND_GETECHO:
                    sendString_d(GETECHO + " " + nodeAddr);
                    break;
                case COMMAND_GETNODES:
                    nodes.clear();
                    sendString_d(GETNODES);
                    break;
                case COMMAND_GETCONNECTEDNODE:
                    sendString_d(GETCONNECTEDNODE);
                    break;
                default:
                    System.out.println("unkown command");
            }
        } else {
        }
    }
    /*
     * sends the String with a delay between every charackter
     * This needs to be done to avoid timing problems on the micro controller
     */

    public void sendString_d(String s) {
        try {
            char[] ca = s.toCharArray();
            com.sendByte(' ');
            Thread.sleep(10);
            for (int j = 0; j < ca.length; j++) {
                com.sendByte(ca[j]);
                Thread.sleep(2);
            }
            com.sendByte('\n');
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stringReceived(StringEvent se) {
        strR = se.getStringReceived();
        if (strR.startsWith(BODATA)) {
            state = STATE_LISTENING;
        }
        if (strR.startsWith(EODATA)) {
        }

        if (state == STATE_LISTENING) {
            state = STATE_IDLE;
        }

        if (strR.startsWith("#address")) {
            connectedNode = Integer.parseInt(strR.split(" ")[1]);
        }
        if (strR.startsWith(BONL)) {
            state = STATE_LISTENING_NL;
        }
        if (strR.startsWith(NODEENTRY) && state == STATE_LISTENING_NL) {
            nodes.add(strR.substring(11).split(" ")[0]);
        }
       
        if (strR.startsWith(EONL)) {
            state = STATE_IDLE;
        }
        if (strR.startsWith("Associated")) {
            sendCommand(COMMAND_GETNODES, 0);
        }
        jListNodes.setListData(nodes.toArray());
        jtxtOut.append(strR);
        jtxtOut.append("\r\n");
        jtxtOut.setCaretPosition(jtxtOut.getDocument().getLength());

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListNodes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtnConnect;
    private javax.swing.JButton jbtnGetData;
    private javax.swing.JButton jbtnGetEcho;
    private javax.swing.JButton jbtnGetIntervall;
    private javax.swing.JButton jbtnGetTime;
    private javax.swing.JButton jbtnReloadNodeList;
    private javax.swing.JButton jbtnSend;
    private javax.swing.JButton jbtnSetIntervall;
    private javax.swing.JButton jbtnSetTime;
    private javax.swing.JComboBox jcbBaud;
    private javax.swing.JComboBox jcbCom;
    private javax.swing.JLabel jlblConnected;
    private javax.swing.JTextArea jtxtOut;
    private javax.swing.JTextField jtxtSend;
    // End of variables declaration//GEN-END:variables
}
