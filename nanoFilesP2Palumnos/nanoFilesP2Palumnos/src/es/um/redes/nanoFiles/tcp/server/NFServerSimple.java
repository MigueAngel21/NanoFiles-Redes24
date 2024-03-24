package es.um.redes.nanoFiles.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class NFServerSimple {

	private static final int SERVERSOCKET_ACCEPT_TIMEOUT_MILISECS = 1000;
	private static final String STOP_SERVER_COMMAND = "fgstop";
	private static final int PORT = 10000;
	private ServerSocket serverSocket = null;

	public NFServerSimple() throws IOException {
		/*
		 * TODO: Crear una direción de socket a partir del puerto especificado
		 */
		
		/*
		 * TODO: Crear un socket servidor y ligarlo a la dirección de socket anterior
		 */
		InetSocketAddress serverAdress = new InetSocketAddress(PORT);
		
		serverSocket = new ServerSocket();
		try {
			serverSocket.bind(serverAdress);
		} catch (BindException e) {
			System.err.println("* Error: Port " + PORT + " is already in use.");
			Random random = new Random();
			int newPort = random.nextInt(55535)+10000;
			System.out.println("Trying to bind to port " + newPort + "...");
			serverSocket.bind(new InetSocketAddress(newPort));

		}
		serverSocket.setReuseAddress(true);
		serverSocket.setSoTimeout(SERVERSOCKET_ACCEPT_TIMEOUT_MILISECS);
	}

	/**
	 * Método para ejecutar el servidor de ficheros en primer plano. Sólo es capaz
	 * de atender una conexión de un cliente. Una vez se lanza, ya no es posible
	 * interactuar con la aplicación a menos que se implemente la funcionalidad de
	 * detectar el comando STOP_SERVER_COMMAND (opcional)
	 * 
	 */
	public void run() {
		/*
		 * TODO: Comprobar que el socket servidor está creado y ligado
		 */
		if (serverSocket == null) {
			System.err.println("* Error: Server socket is not created and bound");
			return;
		} else {
				System.out.println("Server is listening on port " + serverSocket.getLocalSocketAddress() + ".");
		}
		/*
		 * TODO: Usar el socket servidor para esperar conexiones de otros peers que
		 * soliciten descargar ficheros
		 */
		Socket socket = null;
		boolean stopserver = false;
		while (!stopserver){
			try {
				socket = serverSocket.accept();
				System.out.println("New client connected: " + socket.getInetAddress().toString() + ":" + socket.getPort());
			} catch (SocketTimeoutException e) {
				//System.out.println("Server timeout. Waiting for new connections...");
				//haz que pueda leer el comando de parada fgstop
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					if (br.ready()) {
						String command = br.readLine();
						if (command.equals(STOP_SERVER_COMMAND)) {
							stopserver = true;
						}
					}
				} catch (IOException e1) {
					System.err.println("* Error: Problem reading the stop command. " + e1.getMessage());
					e1.printStackTrace();
				}
			} catch (IOException e) {
				System.err.println("* Error: Problem accepting a connection. " + e.getMessage());
				e.printStackTrace();
				socket = null;
			}

			



			if(socket != null) {
				NFServerComm.serveFilesToClient(socket);
			}	
		}
			

		/*
		 * TODO: Al establecerse la conexión con un peer, la comunicación con dicho
		 * cliente se hace en el método NFServerComm.serveFilesToClient(socket), al cual
		 * hay que pasarle el socket devuelto por accept
		 */
		


		System.out.println("NFServerSimple stopped. Returning to the nanoFiles shell...");
	}
}
