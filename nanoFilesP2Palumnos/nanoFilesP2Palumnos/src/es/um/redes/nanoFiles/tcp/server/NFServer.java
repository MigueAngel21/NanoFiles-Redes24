package es.um.redes.nanoFiles.tcp.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 * Servidor que se ejecuta en un hilo propio. Creará objetos
 * {@link NFServerThread} cada vez que se conecte un cliente.
 */
public class NFServer implements Runnable {

	private ServerSocket serverSocket = null;
	private boolean stopServer = false;
	private static final int SERVERSOCKET_ACCEPT_TIMEOUT_MILISECS = 1000;
	private static final int PORT = 10000;

	public NFServer() throws IOException {
		/*
		 * TODO: Crear un socket servidor y ligarlo a cualquier puerto disponible
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
	 * Método que crea un socket servidor y ejecuta el hilo principal del servidor,
	 * esperando conexiones de clientes.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/*
		 * TODO: Usar el socket servidor para esperar conexiones de otros peers que
		 * soliciten descargar ficheros
		 */
		if (serverSocket == null) {
			System.err.println("* Error: Server socket is not created and bound");
			return;
		} else {
				System.out.println("Server is listening on port " + serverSocket.getLocalSocketAddress() + ".");
		}
		
		Socket socket = null;
		while (!stopServer) {
			try {
				socket = serverSocket.accept();
				System.out.println("New client connected: " + socket.getInetAddress().toString() + ":" + socket.getPort());
			} catch (SocketTimeoutException e) {
				// No pasa nada, se vuelve a intentar
			} catch (IOException e) {
				System.err.println("* Error: Server socket is not created and bound");
				socket = null;
			}

			if(socket != null) {
				//NFServerComm.serveFilesToClient(socket);
				NFServerThread serverThread = new NFServerThread(socket);
				serverThread.start();
				socket = null;
			}
		}
		
		/*
		 * TODO: Al establecerse la conexión con un peer, la comunicación con dicho
		 * cliente se hace en el método NFServerComm.serveFilesToClient(socket), al cual
		 * hay que pasarle el socket devuelto por accept
		 */
		/*
		 * TODO: (Opcional) Crear un hilo nuevo de la clase NFServerThread, que llevará
		 * a cabo la comunicación con el cliente que se acaba de conectar, mientras este
		 * hilo vuelve a quedar a la escucha de conexiones de nuevos clientes (para
		 * soportar múltiples clientes). Si este hilo es el que se encarga de atender al
		 * cliente conectado, no podremos tener más de un cliente conectado a este
		 * servidor.
		 */
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//System.out.println("Bgserver stopped.");
	}

	/**
	 * TODO: Añadir métodos a esta clase para: 1) Arrancar el servidor en un hilo
	 * nuevo que se ejecutará en segundo plano 2) Detener el servidor (stopserver)
	 * 3) Obtener el puerto de escucha del servidor etc.
	 */
	public void start() { //arrancar el servidor en un hilo nuevo ( NFSeverThread)
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
		this.stopServer = true;
	}

	public int getPort() { //obetener el puerto de escucha del servidor
		return serverSocket.getLocalPort();
	}
	




}
