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
	
	public static final String OPERATION_USERLIST = "userList";			//lista de usuarios
	public static final String OPERATION_USERLIST_OK = "userListOk";	//mensaje bien
	public static final String OPERATION_USERLIST_FAIL = "userListFail";//mensaje de fallo
	
	//mejoras del servidor
	public static final String OPERATION_REGISTER_FILESERVER = "registerFileServer"; //mensaje de registro
	public static final String OPERATION_REGISTER_FILESERVER_OK = "registerFileServerOk"; //mensaje bien
	public static final String OPERATION_REGISTER_FILESERVER_FAIL = "registerFileServerFail"; //mensaje de fallo

	public static final String OPERATION_UNREGISTER_FILESERVER = "unregisterFileServer"; //mensaje de desregistro
	public static final String OPERATION_UNREGISTER_FILESERVER_OK = "unregisterFileServerOk"; //mensaje bien
	public static final String OPERATION_UNREGISTER_FILESERVER_FAIL = "unregisterFileServerFail"; //mensaje de fallo

	public static final String OPERATION_USERSTATUS = "userStatus"; //mensaje de estado de usuario
	public static final String OPERATION_USERSTATUS_OK = "userStatusOk"; //mensaje bien
	public static final String OPERATION_USERSTATUS_FAIL = "userStatusFail"; //mensaje de fallo

	public static final String OPERATION_LOOKUPSERVER = "lookupServerByUsername"; //mensaje de busqueda de servidor por nombre
	public static final String OPERATION_LOOKUPSERVER_OK = "lookupServerByUsernameOk"; //mensaje bien
	public static final String OPERATION_LOOKUPSERVER_FAIL = "lookupServerByUsernameFail"; //mensaje de fallo
	
}
