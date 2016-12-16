package ar.com.sourcesistemas.arduino;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JPanelTurnOnOffLED extends JPanel
	implements ActionListener {

	private static final String IZQUIERDA = "Izquierda";
	private static final String DERECHA = "Derecha";
	private static final int PULSO_IZQUIERDA = 2;
	private static final int PULSO_DERECHA = 1;
	
	private static final String ARRIBA ="CAMA ARRIBA";
	private static final String ABAJO ="CAMA ABAJO";
	private static final int PULSO_ARRIBA = 3;
	private static final int PULSO_ABAJO = 4;
	
	private static final String ENCENDER_LED = "Encender led";
	private static final int PULSO_LED =9;

	
	private JButton encenderLed;
	private JButton switchOnButton;
	private JButton switchOffButton;
	private JButton buttonArriba;
	private JButton buttonAbajo;
	
	private JLabel label;
	private JFrame frame;

	/** The output stream to the port */
	private OutputStream output = null;

	SerialPort serialPort;
	private  String PORT_NAME = "/dev/ttyACM0";
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public JPanelTurnOnOffLED(){
		encenderLed = new JButton(ENCENDER_LED);
		
		switchOnButton = new JButton(IZQUIERDA);
		switchOffButton = new JButton(DERECHA);

		buttonArriba = new JButton(ARRIBA);
		buttonAbajo = new JButton(ABAJO);

		label = new JLabel("Turn On/Off LED:");

		switchOnButton.addActionListener(this);
		switchOffButton.addActionListener(this);
		buttonAbajo.addActionListener(this);
		buttonArriba.addActionListener(this);
		encenderLed.addActionListener(this);

		add(label);
		add(switchOnButton);
		add(switchOffButton);
		add(buttonAbajo);
		add(buttonArriba);
		add(encenderLed);

		initializeArduinoConnection();
	}

	public void initializeArduinoConnection(){

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
			showError("Could not find COM port.");
			System.exit(ERROR);
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
			showError(e.getMessage());
			System.exit(ERROR);
		}

	}

	public static void createAndShowGUI(){

		JFrame frame = new JFrame("Turn On/Off LED example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		JPanelTurnOnOffLED newContentPane = new JPanelTurnOnOffLED();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String args[]){

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch (e.getActionCommand()){
		
		case  "Izquierda":{
			
//			switchOnButton.setEnabled(false);
//			switchOffButton.setEnabled(true);
			sendData(String.valueOf(PULSO_IZQUIERDA));
			break;
		}
		
		case "Derecha":{
			sendData(String.valueOf(PULSO_DERECHA));
			break;
		}
		
		case ARRIBA:{
			sendData(String.valueOf(PULSO_ARRIBA));
			break;
			
		}
		case ABAJO:{
			sendData(String.valueOf(PULSO_ABAJO));
			break;
			
		}
		
		case ENCENDER_LED:{
			sendData(String.valueOf(PULSO_LED));
			break;
			
		}
		
		
		}
		
//		if(PULSO_IZQUIERDA.equals(e.getActionCommand())){
//			
//
//		}else{
//			switchOnButton.setEnabled(true);
//			switchOffButton.setEnabled(false);
//			sendData(PULSO_DERECHA);
//		}

	}

	private void sendData(String data){

		try {
			output.write(data.getBytes());
		} catch (IOException e) {
			showError("Error sending data");
			System.exit(ERROR);
		}
	}

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(frame,
				errorMessage,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}

}