/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serial;

import at.htlklu.elektronik.schnittstellen.SerielleSchnittstelle;
import at.htlklu.elektronik.schnittstellen.StringEvent;
import at.htlklu.elektronik.schnittstellen.StringListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon
 */
public class SerialComm implements StringListener {

    private static final SerialComm instance = new SerialComm();
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
    private static final String BODATA = "#BOData";
    private static final String EODATA = "#EOData";
    private static final String ADDRESS = "#address";
    private static final String ECHORESPONSE = "#pingresponse";
    //---------------------------------------------------
    public static final int STATE_IDLE = 0;
    public static final int STATE_LISTENING = 1;
    private SerielleSchnittstelle com;
    private String strR;
    private int state;
    private ArrayList<String> dataString = new ArrayList<String>();


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
            System.out.println("string sent");
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SerialComm() {
        com = new SerielleSchnittstelle();
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

    public void sendCommand(int command) {
        sendCommand(command, 0);
    }

    public void disconnect() {
        com.disconnect();
    }

    @Override
    public void stringReceived(StringEvent se) {
        strR = se.getStringReceived();

        if (strR.equals(BODATA)) {
            dataString.add("got command " + BODATA);
            state = STATE_LISTENING;
            return;
        }
        if (strR.equals(EODATA)) {
            dataString.add("got command " + EODATA);
            return;
        }
        if (strR.startsWith(ADDRESS)) {
            dataString.add("connected node has address: " + strR.split(" ")[1] + "\r\n");
            return;
        }

        if (state == STATE_LISTENING) {
            dataString.add("reveived DATA from node: " + strR);
            state = STATE_IDLE;
            return;
        }

        if (strR.startsWith(ECHORESPONSE)) {
            dataString.add("Ping took: " + strR.split(" ")[1] + "ms\r\n");
            return;
        }
        System.out.println(strR);
    }

    public static SerialComm getInstance() {
        return instance;
    }

    public void connect(String portName, int baud) {
        if (com.isConnected()) {
            com.removeStringListener(this);
            com.disconnect();
        }
        com = new SerielleSchnittstelle(portName, baud, 8, 1, 0);
        com.addStringListener(this);
    }

    public ArrayList<String> getPortNames() {
        return SerielleSchnittstelle.listPorts();
    }

    public int getBaud() {
        if (com.isConnected()) {
            return com.getBaudRate();
        }
        return 115200;
    }

    public String getConsoleOutput() {
        ListIterator<String> iter = dataString.listIterator();
        StringBuilder s = new StringBuilder();
        while (iter.hasNext()) {
            s.append("\r\n").append(iter.next());
        }
        return s.toString();
    }

    public void setDataString(String s) {
        dataString.clear();
    }
}
