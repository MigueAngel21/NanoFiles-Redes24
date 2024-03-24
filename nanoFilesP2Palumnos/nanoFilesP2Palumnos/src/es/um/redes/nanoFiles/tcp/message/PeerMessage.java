package es.um.redes.nanoFiles.tcp.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import es.um.redes.nanoFiles.util.FileInfo;

public class PeerMessage {




	private byte opcode;
	private byte hashlength;
	private String filehash;
	private int filelength;		//al ser un int permite en lugar de un byte que eran 127 bytes, 2^31-1 bytes, 2GB
	private byte[] filedata;
	/*
	 * TODO: Añadir atributos y crear otros constructores específicos para crear
	 * mensajes con otros campos (tipos de datos)
	 * 
	 */




	public PeerMessage() {
		opcode = PeerMessageOps.OPCODE_INVALID_CODE;
	}

	public PeerMessage(byte op) {  //filenotfound
		opcode = op;
	}

	public PeerMessage(byte op, byte hashlen, String hash) {  //download
		this.opcode = op;
		this.hashlength = hashlen;
		this.filehash = hash;
	}

	public PeerMessage(byte op, int filelen, byte[] filedata) {  //filedata
		this.opcode = op;
		this.filelength = filelen;
		this.filedata = filedata;
	}
	
	/*
	 * TODO: Crear métodos getter y setter para obtener valores de nuevos atributos,
	 * comprobando previamente que dichos atributos han sido establecidos por el
	 * constructor (sanity checks)
	 */
	public byte getOpcode() {
		return opcode;
	}

	public byte getHashlength() {
		return hashlength;
	}

	public String getFilehash() {
		return filehash;
	}

	public void setHashlength(byte hashlength) {
		assert(hashlength > 0);
		this.hashlength = hashlength;
	}
	
	public void setFilehash(String filehash) {
		assert(filehash!= null);
		this.filehash = new String(filehash);
	}

	public int getFilelength() {
		return filelength;
	}

	public void setFilelength(int filelength) {
		assert(filelength > 0);
		this.filelength = filelength;
	}

	public byte[] getFiledata() {
		return filedata;
	}

	public void setFiledata(byte[] filedata) {
		assert(filedata != null);
		this.filedata = filedata;
	}


	/**
	 * Método de clase para parsear los campos de un mensaje y construir el objeto
	 * DirMessage que contiene los datos del mensaje recibido
	 * 
	 * @param data El array de bytes recibido
	 * @return Un objeto de esta clase cuyos atributos contienen los datos del
	 *         mensaje recibido.
	 * @throws IOException
	 */
	public static PeerMessage readMessageFromInputStream(DataInputStream dis) throws IOException {
		/*
		 * TODO: En función del tipo de mensaje, leer del socket a través del "dis" el
		 * resto de campos para ir extrayendo con los valores y establecer los atributos
		 * del un objeto DirMessage que contendrá toda la información del mensaje, y que
		 * será devuelto como resultado. NOTA: Usar dis.readFully para leer un array de
		 * bytes, dis.readInt para leer un entero, etc.
		 */
		PeerMessage message = new PeerMessage();
		byte opcode = dis.readByte();
		switch (opcode) {
		case PeerMessageOps.OPCODE_FILENOTFOUND:
			message = new PeerMessage(opcode);
			break;
		case PeerMessageOps.OPCODE_DOWNLOADFROM:
			byte hashlength = dis.readByte();
			byte[] hash = new byte[hashlength];
			dis.readFully(hash);
			message = new PeerMessage(opcode, hashlength, new String(hash));
			break;
		
		case PeerMessageOps.OPCODE_FILEDATA:
			int filelength = dis.readInt();
			byte[] filedata = new byte[filelength];
			dis.readFully(filedata);
			message = new PeerMessage(opcode, filelength, filedata);
			break;
		
		default:
			System.err.println("PeerMessage.readMessageFromInputStream doesn't know how to parse this message opcode: "
					+ PeerMessageOps.opcodeToOperation(opcode));
			System.exit(-1);
		}
		return message;
	}

	public void writeMessageToOutputStream(DataOutputStream dos) throws IOException {
		/*
		 * TODO: Escribir los bytes en los que se codifica el mensaje en el socket a
		 * través del "dos", teniendo en cuenta opcode del mensaje del que se trata y
		 * los campos relevantes en cada caso. NOTA: Usar dos.write para leer un array
		 * de bytes, dos.writeInt para escribir un entero, etc.
		 */

		dos.writeByte(opcode);
		switch (opcode) {
		case PeerMessageOps.OPCODE_FILENOTFOUND:
			break;
		case PeerMessageOps.OPCODE_DOWNLOADFROM:
			assert ((filehash.length()==hashlength) && (hashlength > 0));
			dos.writeByte(hashlength);
			dos.write(filehash.getBytes());
			break;

		case PeerMessageOps.OPCODE_FILEDATA:
			assert ((filedata.length==filelength) && (filelength > 0));
			dos.writeInt(filelength);
			dos.write(filedata);
			break;

		default:
			System.err.println("PeerMessage.writeMessageToOutputStream found unexpected message opcode " + opcode + "("
					+ PeerMessageOps.opcodeToOperation(opcode) + ")");
		}
	}





}
