package es.um.redes.nanoFiles.udp.message;

public class DirMessageOps {

	/*
	 * TODO: Añadir aquí todas las constantes que definen los diferentes tipos de
	 * mensajes del protocolo de comunicación con el directorio.
	 */
	public static final String OPERATION_INVALID = "invalid_operation";	//mensaje invalido
	public static final String OPERATION_LOGIN = "login";				//mensaje de conexion
	public static final String OPERATION_LOGIN_OK = "loginOk"; 			//mensaje bien
	public static final String OPERATION_LOGIN_FAIL = "loginFail"; 		//cuando hay un usuario repetido
	public static final String OPERATION_LOGOUT = "logout";				//mensaje de desconexion
	public static final String OPERATION_LOGOUT_OK = "logoutOk"; 		//mensaje bien
	public static final String OPERATION_LOGOUT_FAIL = "logoutFail";	//mensaje de desconexion fallida





}
