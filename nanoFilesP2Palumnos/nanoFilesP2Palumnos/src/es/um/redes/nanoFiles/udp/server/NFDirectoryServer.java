package es.um.redes.nanoFiles.udp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

import es.um.redes.nanoFiles.application.NanoFiles;
import es.um.redes.nanoFiles.udp.message.DirMessage;
import es.um.redes.nanoFiles.udp.message.DirMessageOps;
import es.um.redes.nanoFiles.util.FileInfo;

public class NFDirectoryServer {
	/**
	 * Número de puerto UDP en el que escucha el directorio
	 */
	public static final int DIRECTORY_PORT = 6868;

	/**
	 * Socket de comunicación UDP con el cliente UDP (DirectoryConnector)
	 */
	private DatagramSocket socket = null;
	/**
	 * Estructura para guardar los nicks de usuarios registrados, y clave de sesión
	 * 
	 */
	private HashMap<String, Integer> nicks;
	/**
	 * Estructura para guardar las claves de sesión y sus nicks de usuario asociados
	 * 
	 */
	private HashMap<Integer, String> sessionKeys;
	/*
	 * TODO: Añadir aquí como atributos las estructuras de datos que sean necesarias
	 * para mantener en el directorio cualquier información necesaria para la
	 * funcionalidad del sistema nanoFilesP2P: ficheros publicados, servidores
	 * registrados, etc.
	 */
	private HashMap<String, InetSocketAddress> servers;



	/**
	 * Generador de claves de sesión aleatorias (sessionKeys)
	 */
	Random random = new Random();
	/**
	 * Probabilidad de descartar un mensaje recibido en el directorio (para simular
	 * enlace no confiable y testear el código de retransmisión)
	 */
	private double messageDiscardProbability;

	public NFDirectoryServer(double corruptionProbability) throws SocketException {	//Ejercicio 4(Constructor)
		/*
		 * Guardar la probabilidad de pérdida de datagramas (simular enlace no
		 * confiable)
		 */
		messageDiscardProbability = corruptionProbability;
		/*
		 * TODO: (Boletín UDP) Inicializar el atributo socket: Crear un socket UDP
		 * ligado al puerto especificado por el argumento directoryPort en la máquina
		 * local,
		 */
		socket = new DatagramSocket(DIRECTORY_PORT);
		System.out.println("Server listening on socket addresss " + socket.getLocalSocketAddress());	//remover mas tarde(Utilidad para debuggear)
		/*
		 * TODO: (Boletín UDP) Inicializar el resto de atributos de esta clase
		 * (estructuras de datos que mantiene el servidor: nicks, sessionKeys, etc.)
		 */
		this.nicks=new HashMap<String,Integer>();
		this.sessionKeys=new HashMap<Integer,String>();
		this.servers=new HashMap<String,InetSocketAddress>();


		if (NanoFiles.testMode) {
			if (socket == null || nicks == null || sessionKeys == null) {
				System.err.println("[testMode] NFDirectoryServer: code not yet fully functional.\n"
						+ "Check that all TODOs in its constructor and 'run' methods have been correctly addressed!");
				System.exit(-1);
			}
		}
	}

	public void run() throws IOException {
		
		InetSocketAddress clientAddr = null;
		int dataLength = -1;
		/*
		 * TODO: (Boletín UDP) Crear un búfer para recibir datagramas y un datagrama
		 * asociado al búfer
		 */
		
		
		System.out.println("Directory starting...");
		
		while (true) { // Bucle principal del servidor de directorio
			byte[] receptionBuffer = null;
			receptionBuffer = new byte[DirMessage.PACKET_MAX_SIZE];
			DatagramPacket packetFromClient = new DatagramPacket(receptionBuffer, receptionBuffer.length);
			
			// TODO: (Boletín UDP) Recibimos a través del socket un datagrama

			System.out.println("Waiting to receive datagram...");
			socket.receive(packetFromClient);

			// TODO: (Boletín UDP) Establecemos dataLength con longitud del datagrama
			// recibido

			dataLength=packetFromClient.getLength();
			System.out.println(" Datagram size: " + dataLength + " bytes");

			// TODO: (Boletín UDP) Establecemos 'clientAddr' con la dirección del cliente,
			// obtenida del
			// datagrama recibido

			clientAddr=(InetSocketAddress) packetFromClient.getSocketAddress();




			if (NanoFiles.testMode) {
				if (receptionBuffer == null || clientAddr == null || dataLength < 0) {
					System.err.println("NFDirectoryServer.run: code not yet fully functional.\n"
							+ "Check that all TODOs have been correctly addressed!");
					System.exit(-1);
				}
			}
			System.out.println("Directory received datagram from " + clientAddr + " of size " + dataLength + " bytes");

			// Analizamos la solicitud y la procesamos
			if (dataLength > 0) {
				String messageFromClient = null;
				/*
				 * TODO: (Boletín UDP) Construir una cadena a partir de los datos recibidos en
				 * el buffer de recepción
				 */
				messageFromClient = new String(receptionBuffer,0,packetFromClient.getLength());	//login&nickname en string

				DirMessage mensajeServer = DirMessage.fromString(messageFromClient);
				

				 // Servidor funcionando en modo producción (mensajes bien formados)

					// Vemos si el mensaje debe ser ignorado por la probabilidad de descarte
					double rand = Math.random();
					if (rand < messageDiscardProbability) {
						System.err.println("Directory DISCARDED datagram from " + clientAddr);
						continue;
					}

					String mensaje = buildResponseFromRequest(mensajeServer, clientAddr).toString();	//Llama a la funcion que devuelve el mensaje de respuesta
					byte[] respuesta = mensaje.getBytes();
					DatagramPacket packetToClient = new DatagramPacket(respuesta,respuesta.length,clientAddr);
					socket.send(packetToClient);

					/*
					 * TODO: Construir String partir de los datos recibidos en el datagrama. A
					 * continuación, imprimir por pantalla dicha cadena a modo de depuración.
					 * Después, usar la cadena para construir un objeto DirMessage que contenga en
					 * sus atributos los valores del mensaje (fromString).
					 */
					/*
					 * TODO: Llamar a buildResponseFromRequest para construir, a partir del objeto
					 * DirMessage con los valores del mensaje de petición recibido, un nuevo objeto
					 * DirMessage con el mensaje de respuesta a enviar. Los atributos del objeto
					 * DirMessage de respuesta deben haber sido establecidos con los valores
					 * adecuados para los diferentes campos del mensaje (operation, etc.)
					 */
					/*
					 * TODO: Convertir en string el objeto DirMessage con el mensaje de respuesta a
					 * enviar, extraer los bytes en que se codifica el string (getBytes), y
					 * finalmente enviarlos en un datagrama
					 */



				
			} else {
				System.err.println("Directory ignores EMPTY datagram from " + clientAddr);
			}

		}
	}

	private DirMessage buildResponseFromRequest(DirMessage msg, InetSocketAddress clientAddr) {
		/*
		 * TODO: Construir un DirMessage con la respuesta en función del tipo de mensaje
		 * recibido, leyendo/modificando según sea necesario los atributos de esta clase
		 * (el "estado" guardado en el directorio: nicks, sessionKeys, servers,
		 * files...)
		 */
		String operation = msg.getOperation();

		DirMessage response = null;




		switch (operation) {
		case DirMessageOps.OPERATION_LOGIN: {
			String username = msg.getNickname();
			
			/*
			 * TODO: Comprobamos si tenemos dicho usuario registrado (atributo "nicks"). Si
			 * no está, generamos su sessionKey (número aleatorio entre 0 y 1000) y añadimos
			 * el nick y su sessionKey asociada. NOTA: Puedes usar random.nextInt(10000)
			 * para generar la session key
			 */

			 if (nicks.containsKey(username)) {
				response = new DirMessage(DirMessageOps.OPERATION_LOGIN_FAIL);
				
			} else{
				int clave = random.nextInt(10000);
				while (sessionKeys.containsKey(clave)) {
					clave = random.nextInt(10000);
				}
				nicks.put(username, clave);
				sessionKeys.put(clave, username);
				response = new DirMessage(DirMessageOps.OPERATION_LOGIN_OK);
				response.setSessionKey(Integer.toString(clave));
			}

			/*
			 * TODO: Construimos un mensaje de respuesta que indique el éxito/fracaso del
			 * login y contenga la sessionKey en caso de éxito, y lo devolvemos como
			 * resultado del método.
			 */
			/*
			 * TODO: Imprimimos por pantalla el resultado de procesar la petición recibida
			 * (éxito o fracaso) con los datos relevantes, a modo de depuración en el
			 * servidor
			 */
			System.out.println("Login: " + username + " " + response.getOperation());
			break;
		}
		case DirMessageOps.OPERATION_LOGOUT: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_LOGOUT_FAIL);
			} else {
				String username = sessionKeys.get(clave);
				nicks.remove(username);
				sessionKeys.remove(clave);
				response = new DirMessage(DirMessageOps.OPERATION_LOGOUT_OK);
			}

			break;
		}
		case DirMessageOps.OPERATION_USERLIST: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_USERLIST_FAIL);
				System.err.println("Userlist failed: session key not found");
			} else {
				System.out.println("Recieve userlist request from " + clientAddr);
				System.out.println("Client " + clientAddr + "successfully obteined userlist");
				response = new DirMessage(DirMessageOps.OPERATION_USERLIST_OK);
				String[] userlist = new String[nicks.size()];
				for (int i = 0; i < nicks.size(); i++) {
					userlist[i] = (String) nicks.keySet().toArray()[i];
				}
				response.setUserList(userlist);
				
				System.out.println("Sent userlist reponse to " + clientAddr);
			}
			break;
		}
		
		case DirMessageOps.OPERATION_USERSTATUS: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_USERSTATUS_FAIL);
				System.err.println("Userstatus failed: session key not found");
			} else {
				System.out.println("Recieve userstatus request from " + clientAddr);
				System.out.println("Client " + clientAddr + "successfully obteined userstatus");
				response = new DirMessage(DirMessageOps.OPERATION_USERSTATUS_OK);
				String[] userlist = new String[nicks.size()];
				String[] userstatus = new String[nicks.size()];
				for (int i = 0; i < nicks.size(); i++) {
					userlist[i] = (String) nicks.keySet().toArray()[i];
				}
				for (int i = 0; i < nicks.size(); i++){
					if (servers.containsKey(userlist[i])) {
						userstatus[i] = "true";
					} else {
						userstatus[i] = "false";
					}
				}
				response.setUserStatus(userstatus);
				System.out.println("Sent userstatus reponse to " + clientAddr);
			}
			break;
		}

		case DirMessageOps.OPERATION_REGISTER_FILESERVER: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_REGISTER_FILESERVER_FAIL);
				System.err.println("Register fileserver failed: session key not found");
			} else {
				String username = sessionKeys.get(clave);
				String port = msg.getPort();
				int portInt = Integer.parseInt(port);
				InetSocketAddress bgServerAddress = new InetSocketAddress(clientAddr.getAddress(), portInt);
				servers.put(username, bgServerAddress);
				response = new DirMessage(DirMessageOps.OPERATION_REGISTER_FILESERVER_OK);
				System.out.println("Fileserver " + username + " registered");
			}
			break;
		}

		case DirMessageOps.OPERATION_UNREGISTER_FILESERVER: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_UNREGISTER_FILESERVER_FAIL);
				System.err.println("Unregister fileserver failed: session key not found");
			} else if (!(servers.containsKey(sessionKeys.get(clave))) ) {
				response = new DirMessage(DirMessageOps.OPERATION_UNREGISTER_FILESERVER_FAIL);
				System.err.println("Unregister fileserver failed: fileserver not found");
			} else {
				String username = sessionKeys.get(clave);
				servers.remove(username);
				response = new DirMessage(DirMessageOps.OPERATION_UNREGISTER_FILESERVER_OK);
				System.out.println("Fileserver " + username + " unregistered");
			}
			break;
		}

		case DirMessageOps.OPERATION_LOOKUPSERVER: {
			int clave = Integer.parseInt(msg.getSessionKey());
			if (!(sessionKeys.containsKey(clave))) {
				response = new DirMessage(DirMessageOps.OPERATION_LOOKUPSERVER_FAIL);
				System.err.println("Lookup server failed: session key not found");
			} else {
				String username = msg.getNickname();
				if (!(servers.containsKey(username))) {
					response = new DirMessage(DirMessageOps.OPERATION_LOOKUPSERVER_FAIL);
					System.err.println("Lookup server failed: fileserver not found");
				} else {
					InetSocketAddress serverAddress = servers.get(username);
					response = new DirMessage(DirMessageOps.OPERATION_LOOKUPSERVER_OK);
					response.setServer(serverAddress.toString().substring(1));
					System.out.println("Lookup server success: " + username + " at " + serverAddress);
				}
			}
			break;
		}

		default:
			System.out.println("Unexpected message operation: \"" + operation + "\"");
		}
		return response;

	}
}
