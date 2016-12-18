package ar.com.sourcesistemas.arduino;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ar.com.sourcesistemas.arduino.constantes.Constantes;
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
	
	private JLabel labelPrecision;
	public void sendData(String data){

		try {
			output.write(data.getBytes());
		} catch (IOException e) {
			//TODO mostrar error
		}
	}
	
	public void usePresicion(){
		
		labelPrecision = new JLabel("1000");
		
	}
	
	
	public  Conexion(){

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
			System.out.println("error 1");
			
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
			System.out.println("error 2");
			
		}

	}

	public void codeSender(String actionCommand) {


		switch (actionCommand) {

		case Constantes.IZQUIERDA: {

			sendData(String.valueOf(Constantes.PULSO_IZQUIERDA));
			break;
		}

		case Constantes.DERECHA: {
			sendData(String.valueOf(Constantes.PULSO_DERECHA));
			break;
		}

		case Constantes.ARRIBA: {
			sendData(String.valueOf(Constantes.PULSO_ARRIBA));
			break;

		}
		case Constantes.ABAJO: {
			sendData(String.valueOf(Constantes.PULSO_ABAJO));
			break;

		}

		case Constantes.PINZA_ABRIR: {
			sendData(String.valueOf(Constantes.PULSO_ABRIR));
			break;

		}

		case Constantes.PINZA_CERRAR: {

			sendData(String.valueOf(Constantes.PULSO_CERRAR));
			break;

		}

		case Constantes.MUNIECA_ABAJO: {

			sendData(String.valueOf(Constantes.PULSO_MUNIECA_ABAJO));
			break;

		}

		case Constantes.MUNIECA_ARRIBA: {
			sendData(String.valueOf(Constantes.PULSO_MUNIECA_ARRIBA));
			break;

		}

		case Constantes.ENCENDER_LED: {
			sendData(String.valueOf(Constantes.PULSO_LED));
			break;

		}

		case Constantes.PRECISION_MAS: {
			sendData(Constantes.PULSO_PRECISION_MAS);
			labelPrecision.setText(String.valueOf(Integer.parseInt(labelPrecision.getText()) - 50));
			break;

		}

		case Constantes.PRECISION_MENOS: {
			sendData(Constantes.PULSO_PRECISION_MENOS);
			labelPrecision.setText(String.valueOf(Integer.parseInt(labelPrecision.getText()) + 50));
			break;

		}

		case Constantes.ADELANTE: {

			sendData(Constantes.PULSO_ADELANTE);
			break;

		}

		case Constantes.ATRAS: {

			sendData(Constantes.PULSO_ATRAS);
			break;

		}

		}
		
		
	}
	

	
}
