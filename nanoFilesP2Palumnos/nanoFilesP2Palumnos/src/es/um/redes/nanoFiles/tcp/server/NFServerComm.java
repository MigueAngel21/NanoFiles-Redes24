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
			int numberToSend = PeerMessageOps.OPCODE_FILEDATA;
			int numberToReceive = 0;
			numberToReceive = dis.readInt();
			System.out.println("Received: " + Integer.toString(numberToReceive));
			if (numberToReceive == PeerMessageOps.OPCODE_DOWNLOAD) {
				PeerMessage msgIn = PeerMessage.readMessageFromInputStream(dis);
				String targetFileHashSubstr = msgIn.getFilehash();
				/*
				FileInfo fileInfo = new FileInfo();
				File file = new File(NanoFiles.db.lookupFilePath(targetFileHashSubstr));

				if (file.exists()) {
					byte[] filedata = new byte[(int)file.length()];
					DataInputStream disFile = new DataInputStream(new FileInputStream(file));
					disFile.readFully(filedata);
					disFile.close();
					PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILEDATA, (int)filedata.length,filedata);
					msgOut.writeMessageToOutputStream(dos);
				} else {
					PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILENOTFOUND);
					msgOut.writeMessageToOutputStream(dos);
				}
				
				byte[] filedata = new byte[(int)file.length()];
				DataInputStream disFile = new DataInputStream(new FileInputStream(file));
				disFile.readFully(filedata);
				disFile.close();
				PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILEDATA, (int)filedata.length,filedata);
				msgOut.writeMessageToOutputStream(dos);
				*/
			}
			dos.writeInt(numberToSend);
			System.out.println("Sent: " + Integer.toString(numberToSend));
			socket.close();
		} catch (IOException e) {
			System.err.println("* Error al crear los streams de entrada/salida");//Unable to start the server
			e.printStackTrace();
		}
	}




}
