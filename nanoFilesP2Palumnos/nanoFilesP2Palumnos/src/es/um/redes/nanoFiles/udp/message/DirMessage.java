package es.um.redes.nanoFiles.udp.message;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import es.um.redes.nanoFiles.util.FileInfo;

/**
 * Clase que modela los mensajes del protocolo de comunicación entre pares para
 * implementar el explorador de ficheros remoto (servidor de ficheros). Estos
 * mensajes son intercambiados entre las clases DirectoryServer y
 * DirectoryConnector, y se codifican como texto en formato "campo:valor".
 * 
 * @author rtitos
 *
 */
public class DirMessage {
	public static final int PACKET_MAX_SIZE = 65507; // 65535 - 8 (UDP header) - 20 (IP header)

	private static final char DELIMITER = ':'; // Define el delimitador
	private static final char END_LINE = '\n'; // Define el carácter de fin de línea
	private static final String END_LINE_STR = "\n"; // Define el carácter de fin de línea
	/**
	 * Nombre del campo que define el tipo de mensaje (primera línea)
	 */
	private static final String FIELDNAME_OPERATION = "operation";
	/*
	 * TODO: Definir de manera simbólica los nombres de todos los campos que pueden
	 * aparecer en los mensajes de este protocolo (formato campo:valor)
	 */
	private static final String FIELDNAME_NICKNAME = "nickname";
	private static final String FIELDNAME_SESSIONKEY = "sessionkey";
	private static final String FIELDNAME_USERLIST = "users";
	private static final String FIELDNAME_PORT = "port";
	private static final String FIELDNAME_USERSTATUS = "userstatus";
	private static final String FIELDNAME_SERVER = "server";
	/**
	 * Tipo del mensaje, de entre los tipos definidos en PeerMessageOps.
	 */
	private String operation = DirMessageOps.OPERATION_INVALID;
	/*
	 * TODO: Crear un atributo correspondiente a cada uno de los campos de los
	 * diferentes mensajes de este protocolo.
	 */
	private String nickname;
	private String sessionkey; //parsear a int cuando haya que meter la clave al mapa
	private String[] users;
	private String port;
	private String[] userStatus;
	private String server;
	public DirMessage(String op) {
		operation = op;
	}

	

	/*
	 * TODO: Crear diferentes constructores adecuados para construir mensajes de
	 * diferentes tipos con sus correspondientes argumentos (campos del mensaje)
	 */

	public String getOperation() {
		return operation;
	}

	public void setNickname(String nick) {
		assert(operation.equals(DirMessageOps.OPERATION_LOGIN));
		nickname = nick;
	}

	public String getNickname() {
		return nickname;
	}

	public String getSessionKey() {
		return sessionkey;
	}

	public void setSessionKey(String key) {
		assert(operation.equals(DirMessageOps.OPERATION_LOGIN));
		sessionkey = key;
	}

	public String[] getUserList() {
		return users;
	}

	public void setUserList(String[] list) {
		assert(operation.equals(DirMessageOps.OPERATION_USERLIST_OK));
		users = list;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String[] getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String[] userStatus) {
		this.userStatus = userStatus;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * Método que convierte un mensaje codificado como una cadena de caracteres, a
	 * un objeto de la clase PeerMessage, en el cual los atributos correspondientes
	 * han sido establecidos con el valor de los campos del mensaje.
	 * 
	 * @param message El mensaje recibido por el socket, como cadena de caracteres
	 * @return Un objeto PeerMessage que modela el mensaje recibido (tipo, valores,
	 *         etc.)
	 */
	public static DirMessage fromString(String message) {
		/*
		 * TODO: Usar un bucle para parsear el mensaje línea a línea, extrayendo para
		 * cada línea el nombre del campo y el valor, usando el delimitador DELIMITER, y
		 * guardarlo en variables locales.
		 */
		

		// System.out.println("DirMessage read from socket:");
		// System.out.println(message);
		String[] lines = message.split(END_LINE + "");
		// Local variables to save data during parsing
		DirMessage m = null;



		for (String line : lines) {
			int idx = line.indexOf(DELIMITER); // Posición del delimitador(buscar el delimitador)
			String fieldName = line.substring(0, idx).toLowerCase(); // minúsculas (operation)
			String value = line.substring(idx + 1).trim(); //login

			switch (fieldName) {
			case FIELDNAME_OPERATION: {
				assert (m == null);
				m = new DirMessage(value);
				break;
			}
			case FIELDNAME_NICKNAME:{
				assert (m != null);
				m.setNickname(value);
				break;
			}
			case FIELDNAME_SESSIONKEY:{
				assert (m != null);
				m.setSessionKey(value);
				break;
			}
			case FIELDNAME_USERLIST:{
				assert (m != null);
				String[] users = value.split(",");
				String[] userList = new String[users.length];
				for (int i = 0; i < users.length; i++) {
					userList[i] = users[i];
				}
				m.setUserList(userList);
				break;
			}

			case FIELDNAME_USERSTATUS:{
				assert (m != null);
				String[] isServing = value.split(",");
				String[] userStatus = new String[isServing.length];
				for (int i = 0; i < isServing.length; i++) {
					userStatus[i] = isServing[i];
				}
				m.setUserStatus(userStatus);
				break;
			}

			case FIELDNAME_PORT:{
				assert (m != null);
				m.setPort(value);
				break;
			}

			case FIELDNAME_SERVER:{
				assert (m != null);
				m.setServer(value);
				break;
			}
			
			case END_LINE_STR: // Ignoramos las líneas en blanco
				break;
			default:
				System.err.println("PANIC: DirMessage.fromString - message with unknown field name " + fieldName);
				System.err.println("Message was:\n" + message);
				System.exit(-1);
			}
		}




		return m;
	}

	/**
	 * Método que devuelve una cadena de caracteres con la codificación del mensaje
	 * según el formato campo:valor, a partir del tipo y los valores almacenados en
	 * los atributos.
	 * 
	 * @return La cadena de caracteres con el mensaje a enviar por el socket.
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(FIELDNAME_OPERATION + DELIMITER + operation + END_LINE); // Construimos el campo operation(el mensaje en una linea)
		/*
		 * TODO: En función del tipo de mensaje, crear una cadena con el tipo y
		 * concatenar el resto de campos necesarios usando los valores de los atributos
		 * del objeto.
		 */
		switch (operation) {
			case DirMessageOps.OPERATION_LOGIN:
				sb.append(FIELDNAME_NICKNAME + DELIMITER + nickname + END_LINE);
				break;
			case DirMessageOps.OPERATION_LOGIN_OK:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				break;
			case DirMessageOps.OPERATION_LOGIN_FAIL:	//no hay nada que hacer
				break;
			case DirMessageOps.OPERATION_LOGOUT:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				break;
			case DirMessageOps.OPERATION_LOGOUT_OK:		//no hay nada que hacer
				break;
			case DirMessageOps.OPERATION_LOGOUT_FAIL:	//no hay nada que hacer
				break;
			
			case DirMessageOps.OPERATION_USERLIST:		//no hay nada que hacer
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				break;
			case DirMessageOps.OPERATION_USERLIST_OK:
				String usuarios = "";
				for (int i = 0; i < users.length; i++) {
					if (i == users.length - 1) {
						usuarios += users[i];
					} else {
						usuarios += users[i] + ",";
					}
				}
				sb.append(FIELDNAME_USERLIST + DELIMITER + usuarios + END_LINE);
				break;
			
			case DirMessageOps.OPERATION_USERLIST_FAIL:
				break;

			case DirMessageOps.OPERATION_USERSTATUS:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				break;

			case DirMessageOps.OPERATION_USERSTATUS_OK:
				String isServing = "";
				for (int i = 0; i < userStatus.length; i++) {
					if (i == userStatus.length - 1) {
						isServing += userStatus[i];
					} else {
						isServing += userStatus[i] + ",";
					}
				}
				sb.append(FIELDNAME_USERSTATUS + DELIMITER + isServing + END_LINE);
				break;

			case DirMessageOps.OPERATION_USERSTATUS_FAIL:
				break;
			
			case DirMessageOps.OPERATION_REGISTER_FILESERVER:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				sb.append(FIELDNAME_PORT + DELIMITER + port + END_LINE);
				break;
			
			case DirMessageOps.OPERATION_REGISTER_FILESERVER_OK:	//no hay nada que hacer
				break;
			
			case DirMessageOps.OPERATION_REGISTER_FILESERVER_FAIL:	//no hay nada que hacer
				break;
			case DirMessageOps.OPERATION_UNREGISTER_FILESERVER:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				break;
			case DirMessageOps.OPERATION_UNREGISTER_FILESERVER_OK:	//no hay nada que hacer
				break;

			case DirMessageOps.OPERATION_UNREGISTER_FILESERVER_FAIL:	//no hay nada que hacer
				break;

			case DirMessageOps.OPERATION_LOOKUPSERVER:
				sb.append(FIELDNAME_SESSIONKEY + DELIMITER + sessionkey + END_LINE);
				sb.append(FIELDNAME_NICKNAME + DELIMITER + nickname + END_LINE);
				sb.append(FIELDNAME_SERVER + DELIMITER + server + END_LINE);
				break;

			case DirMessageOps.OPERATION_LOOKUPSERVER_OK:
				sb.append(FIELDNAME_SERVER + DELIMITER + server + END_LINE);
				break;

			case DirMessageOps.OPERATION_LOOKUPSERVER_FAIL:	//no hay nada que hacer
				break;
			
			default:	//los break salen por el default
				break;
			
			}


		sb.append(END_LINE); // Marcamos el final del mensaje
		return sb.toString(); //lo pasamos a string
	}
}
