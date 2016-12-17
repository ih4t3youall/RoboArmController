package ar.com.sourcesistemas.arduino;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Conexion {

	
	/** The output stream to the port */
	private OutputStream output = null;

	SerialPort serialPort;
	private  String PORT_NAME = "/dev/ttyACM0";
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	
	private void sendData(String data){

		try {
			output.write(data.getBytes());
		} catch (IOException e) {
			//TODO mostrar error
		}
	}
	
	public void initializeConnection(){

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		String showInputDialog = JOptionPane.showInputDialog("ingrese el nombre del puerto",PORT_NAME);
		PORT_NAME = showInputDialog;
		
		
		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier)
					portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				portId = currPortId;
				break;
			}
		}

		if (portId == null) {
			//TODO mostrar error
			
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass()
					.getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			output = serialPort.getOutputStream();

		} catch (Exception e) {
			//TODO mostrar error
			
		}

	}
	

	
}
