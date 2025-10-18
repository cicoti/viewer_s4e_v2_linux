package com.s4etech.util;
import java.io.*;
import java.net.*;

public class RTSPClient {
    private Socket rtspSocket;
    private PrintWriter rtspWriter;
    private BufferedReader rtspReader;
    private String sessionID;

    public RTSPClient(String host, int port) throws IOException {
        rtspSocket = new Socket(host, port);
        rtspSocket.setSoTimeout(3000); // Timeout de 5 segundos
        rtspWriter = new PrintWriter(rtspSocket.getOutputStream(), true);
        rtspReader = new BufferedReader(new InputStreamReader(rtspSocket.getInputStream()));
    }

    public String sendSetup() throws IOException {
    	
        String setupRequest = "SETUP rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
                              "CSeq: 2\r\n" +
                              "Transport: RTP/AVP;unicast;client_port=4588-4589\r\n" +
                              "\r\n";
        
        rtspWriter.println(setupRequest);

        String responseLine;
        while ((responseLine = rtspReader.readLine()) != null) {
            System.out.println("Server response: " + responseLine);
            if (responseLine.startsWith("Session:")) {
                sessionID = responseLine.split(":")[1].trim().split(";")[0];
                break;
            } else if (responseLine.startsWith("RTSP/1.0 500")) {
                System.err.println("Erro interno do servidor ao tentar configurar a sessão.");
                break;
            }
        }
        
        return sessionID;
    }
    
    public void sendDescribe() throws IOException {
    	
        String describeRequest = "DESCRIBE rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
                                 "CSeq: 2\r\n" +
                                 "Accept: application/sdp\r\n" +
                                 "\r\n";

        rtspWriter.println(describeRequest);

        String responseLine;
        boolean endOfResponse = false;
        while (!endOfResponse && (responseLine = rtspReader.readLine()) != null) {
            System.out.println("Server response: " + responseLine);
            if (responseLine.isEmpty()) {
                endOfResponse = true;
            }
        }
    }
    
    public void sendOptions() throws IOException {
    	
        String optionsRequest = "OPTIONS rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
                                "CSeq: 1\r\n" +
                                "\r\n";

        rtspWriter.println(optionsRequest);

        String responseLine;
        while ((responseLine = rtspReader.readLine()) != null) {
            System.out.println("Server response: " + responseLine);
            if (responseLine.startsWith("RTSP/1.0 200 OK")) {
                break;
            }
        }
    }

    public void sendTeardown() throws IOException {
    	
        String request = "TEARDOWN rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
                         "CSeq: 4\r\n" +
                         "Session: " + sessionID + "\r\n" +
                         "\r\n";

        rtspWriter.println(request);

        try {
            String responseLine;
            while ((responseLine = rtspReader.readLine()) != null) {
                System.out.println("Server response: " + responseLine);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout esperado após TEARDOWN, não há mais dados a serem recebidos.");
        }
        
        System.out.println(sessionID);
    }
    
 public void sendTeardown(String sessionID) throws IOException {
    	
        String request = "TEARDOWN rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
                         "CSeq: 4\r\n" +
                         "Session: " + sessionID + "\r\n" +
                         "\r\n";

        rtspWriter.println(request);

        try {
            String responseLine;
            while ((responseLine = rtspReader.readLine()) != null) {
                //System.out.println("Server response: " + responseLine);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout esperado após TEARDOWN, não há mais dados a serem recebidos.");
        }
                
    }
 
 public void checkSession(String sessionID) throws IOException {
	    String request = "OPTIONS rtsp://admin:admin@192.168.15.88:554/cam1/mainstream RTSP/1.0\r\n" +
	                     "CSeq: 5\r\n" +
	                     "Session: " + sessionID + "\r\n" +
	                     "\r\n";

	    rtspWriter.println(request);
	    String responseLine;
	    while ((responseLine = rtspReader.readLine()) != null) {
	        System.out.println("Server response: " + responseLine);
	        if (responseLine.contains("454 Session Not Found")) {
	            System.out.println("Session " + sessionID + " has been terminated.");
	            break;
	        }
	        if (responseLine.startsWith("RTSP/1.0 200 OK")) {
	            System.out.println("Session " + sessionID + " is still active.");
	            break;
	        }
	    }
	}



    public static void main(String[] args) {
        try {
            RTSPClient client = new RTSPClient("192.168.15.88", 554);
            client.sendOptions();
            client.sendDescribe();
            String sessionID = client.sendSetup(); // Primeiro envia o SETUP para obter o sessionID
            client.sendTeardown(); // Depois envia o TEARDOWN usando o sessionID obtido
            client.checkSession(sessionID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
