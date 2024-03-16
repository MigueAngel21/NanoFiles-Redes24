package es.um.redes.nanoFiles.tcp.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PeerMessageTest {

	public static void main(String[] args) throws IOException {
		//haz que la consola pueda escribir caracteres en español y tildes
		System.setProperty("file.encoding", "UTF-8");
		String nombreArchivo = "buffer.txt";
		DataOutputStream fos = new DataOutputStream(new FileOutputStream(nombreArchivo));

		/*
		 * TODO: Probar a crear diferentes tipos de mensajes (con los opcodes válidos
		 * definidos en PeerMessageOps), estableciendo los atributos adecuados a cada
		 * tipo de mensaje. Luego, escribir el mensaje a un fichero con
		 * writeMessageToOutputStream para comprobar que readMessageFromInputStream
		 * construye un mensaje idéntico al original.
		 */
		//PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_DOWNLOAD, (byte) 7, "hf7838g");
		//PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILENOTFOUND);
		File file = new File("C:\\Users\\Benja\\Documents\\GitHub\\NanoFiles-Redes24\\nf-shared\\Fichero.txt");
		byte[] filedata = new byte[(int)file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(filedata);
		dis.close();
		PeerMessage msgOut = new PeerMessage(PeerMessageOps.OPCODE_FILEDATA, (int)filedata.length,filedata);
		msgOut.writeMessageToOutputStream(fos);
	
		DataInputStream fis = new DataInputStream(new FileInputStream(nombreArchivo));
		PeerMessage msgIn = PeerMessage.readMessageFromInputStream((DataInputStream) fis);
		/*
		 * TODO: Comprobar que coinciden los valores de los atributos relevantes al tipo
		 * de mensaje en ambos mensajes (msgOut y msgIn), empezando por el opcode.
		 */
		if (msgOut.getOpcode() != msgIn.getOpcode()) {
			System.err.println("Opcode does not match!");
		}else{
			switch (msgIn.getOpcode()) {
				case PeerMessageOps.OPCODE_DOWNLOAD:
					int hashlen = (int) msgIn.getHashlength();
					String hash = msgIn.getFilehash();
					System.out.println("Hash Length: " + hashlen + " Hash: " + hash);
					break;
				case PeerMessageOps.OPCODE_FILENOTFOUND:
					System.out.println("File not found!");
					break;
				case PeerMessageOps.OPCODE_FILEDATA:
					int filelen = (int) msgIn.getFilelength();
					byte[] fileData = msgIn.getFiledata();
					String fileDataStr = new String(fileData);
					System.out.println("File Length: " + filelen); 
					System.out.println("File Data: " + fileDataStr);
					break;
				default:
					System.out.println("KYS!");
			}
			
		}

	}

}

