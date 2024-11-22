package org.unibl.etf.mdp.discoveryserver.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.discoveryserver.logger.FileLogger;
import org.unibl.etf.mdp.discoveryserver.properties.AppConfig;
import org.unibl.etf.mdp.model.Message;

public class ServerThread extends Thread {
	public static final AppConfig conf = new AppConfig();
	private static final Logger logger = FileLogger.getLogger(ServerThread.class.getName());
	private static final String DISCOVER_SUPPLIER_MSG = conf.getDiscoveryMsg();
	private static final String END_MSG = conf.getEndMsg();
	private static final String OK_MSG = conf.getOkMsg();
	private static final String DISCOVER_ALL_SUPPLIERS_MSG = conf.getDiscoverAllMsg();
	private static final String DISCOVER_USER_MSG = conf.getDiscoverUserMsg();
	private static final String DISCOVER_ALL_USERS_MSG = conf.getDiscoverAllUsersMsg();

	private Socket sock;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private static Map<String, String> servers = new HashMap<>();
	private static Map<String, String> users = new HashMap<>();

	public ServerThread(Socket sock) {
		this.sock = sock;
		try {
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Failed to initialize input/output streams.", ex);
		}
		start();
	}

	public void run() {
		try {
			Message request;
			while (true) {
				try {
					request = (Message) in.readObject();
					if (DISCOVER_SUPPLIER_MSG.equals(request.getType())) {
						int port;
						try {
							String temp = (String) request.getBody();
							port = Integer.parseInt(temp);
						} catch (Exception e) {
							logger.log(Level.SEVERE, "Error parsing port.", e);
							break;
						}
						servers.put((String) request.getUsername(), (String) request.getBody());
						System.out.println("Registered server: " + request.getUsername() + " on port: " + port);

						Message response = new Message(OK_MSG);
						out.writeObject(response);
						out.flush();
					} else if (DISCOVER_ALL_SUPPLIERS_MSG.equals(request.getType())) {
						Message response = new Message(DISCOVER_ALL_SUPPLIERS_MSG, "DiscoveryServer");
						response.setBody(servers.toString());
						out.writeObject(response);
						out.flush();
					} else if (DISCOVER_USER_MSG.equals(request.getType())) {
						int port;
						try {
							String temp = (String) request.getBody();
							port = Integer.parseInt(temp);
						} catch (Exception e) {
							logger.log(Level.SEVERE, "Error parsing port.", e);
							break;
						}
						users.put((String) request.getUsername(), (String) request.getBody());
						System.out.println("Registered user: " + request.getUsername() + " on port: " + port);
						Message response = new Message(OK_MSG);
						out.writeObject(response);
						out.flush();
					} else if (DISCOVER_ALL_USERS_MSG.equals(request.getType())) {
						Message msg = new Message(DISCOVER_ALL_SUPPLIERS_MSG, "DiscoveryServer");
						msg.setBody(users.toString());
						out.writeObject(msg);
						out.flush();
					} else if (END_MSG.equals(request.getType())) {
						System.out.println("Ending connection for: " + request.getUsername());
						break;
					} else {
						logger.log(Level.SEVERE, "Unknown message type: " + request.getType());
					}
				} catch (ClassNotFoundException e) {
					logger.log(Level.SEVERE, "Error reading object. Class not found.", e);
					break;
				} catch (IOException e) {
					logger.log(Level.WARNING, "Client disconnected.", e);
					break;
				}
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unexpected error during run.", ex);
		} finally {
			cleanup();
		}
	}

	private void cleanup() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (sock != null && !sock.isClosed())
				sock.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error closing resources.", e);
		}
	}

}
