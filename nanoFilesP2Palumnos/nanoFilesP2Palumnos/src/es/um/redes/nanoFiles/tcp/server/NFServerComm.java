package es.um.redes.nanoFiles.tcp.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;

import es.um.redes.nanoFiles.application.NanoFiles;
import es.um.redes.nanoFiles.tcp.message.PeerMessage;
import es.um.redes.nanoFiles.tcp.message.PeerMessageOps;
import es.um.redes.nanoFiles.util.FileInfo;
import javafx.scene.chart.PieChart.Data;

public class NFServerComm {

	public static void serveFilesToClient(Socket socket) {
		/*
		 * TODO: Crear dis/dos a partir del socket
		 */
		/*
		 * TODO: Mientras el cliente esté conectado, leer mensajes de socket,
		 * convertirlo a un objeto PeerMessage y luego actuar en función del tipo de
		 * mensaje recibido, enviando los correspondientes mensajes de respuesta.
		 */
		/*
		 * TODO: Para servir un fichero, hay que localizarlo a partir de su hash (o
		 * subcadena) en nuestra base de datos de ficheros compartidos. Los ficheros
		 * compartidos se pueden obtener con NanoFiles.db.getFiles(). El método
		 * FileInfo.lookupHashSubstring es útil para buscar coincidencias de una
		 * subcadena del hash. El método NanoFiles.db.lookupFilePath(targethash)
		 * devuelve la ruta al fichero a partir de su hash completo.
		 */
		//Foto de la clase
		DataInputStream dis = null;
		DataOutputStream dos = null;
		
		
		try{
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			PeerMessage msgIn = PeerMessage.readMessageFromInputStream(dis);
			switch (msgIn.getOpcode()) {
				case PeerMessageOps.OPCODE_DOWNLOADFROM:										//Si el mensaje es de descarga
					String hash = msgIn.getFilehash();											//Obtenemos el hash del fichero
					FileInfo[] file = FileInfo.loadFilesFromFolder(NanoFiles.sharedDirname);	//Cargamos los ficheros compartidos
					FileInfo[] infoFich = FileInfo.lookupHashSubstring(file, hash);				//Buscamos el fichero por el hash
					if (infoFich.length == 0){
						String vacio = "";
						dos.writeUTF(vacio);														//Si no se encuentra el fichero
						PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILENOTFOUND);	
						msgOut.writeMessageToOutputStream(dos);										//Enviamos un mensaje de error
					} else {
						String targetHash = infoFich[0].fileHash;									//Si se encuentra el fichero
						dos.writeUTF(targetHash);
						String path = NanoFiles.db.lookupFilePath(targetHash);						//Obtenemos la ruta del fichero
						File fichero = new File(path);
						int length = (int) fichero.length();
						byte[] filedata = new byte[length];
						DataInputStream disFich = new DataInputStream(new FileInputStream(fichero)); //Leemos el fichero
						disFich.readFully(filedata);
						disFich.close();															//Cerramos el fichero
						PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILEDATA, (int)filedata.length, filedata);
						msgOut.writeMessageToOutputStream(dos);										//Enviamos el fichero
					}
					break;
				default:
					break;
			}
						
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		try {
			socket.close();
			System.out.println("Server closed, client disconnected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




}
