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

	private static final String ON = "On";
	private static final String OFF = "Off";
	private static final String TURN_ON = "1";
	private static final String TURN_OFF = "0";

	private JButton switchOnButton;
	private JButton switchOffButton;
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
		switchOnButton = new JButton(ON);

		switchOffButton = new JButton(OFF);
		switchOffButton.setEnabled(false);

		label = new JLabel("Turn On/Off LED:");

		switchOnButton.addActionListener(this);
		switchOffButton.addActionListener(this);

		add(label);
		add(switchOnButton);
		add(switchOffButton);

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
		if(ON.equals(e.getActionCommand())){
			switchOnButton.setEnabled(false);
			switchOffButton.setEnabled(true);
			sendData(TURN_ON);

		}else{
			switchOnButton.setEnabled(true);
			switchOffButton.setEnabled(false);
			sendData(TURN_OFF);
		}

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