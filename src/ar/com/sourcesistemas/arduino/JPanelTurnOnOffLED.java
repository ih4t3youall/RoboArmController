package ar.com.sourcesistemas.arduino;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

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
	
	private static final String PINZA_ABRIR ="Abrir pinza";
	private static final String PINZA_CERRAR ="cerrar pinza";
	private static final int PULSO_CERRAR =7;
	private static final int PULSO_ABRIR = 8;
	
	private static final String MUNIECA_ARRIBA="munieca arriba";
	private static final String MUNIECA_ABAJO="munieca abajo";
	private static final int PULSO_MUNIECA_ARRIBA = 6;
	private static final int PULSO_MUNIECA_ABAJO = 5;
	
	private static final String ADELANTE = "Adelante";
	private static final String ATRAS ="Atras";
	private static final String PULSO_ADELANTE="w";
	private static final String PULSO_ATRAS="s";
	
	
	
	private static final String ENCENDER_LED = "Encender led";
	private static final int PULSO_LED =9;
	
	private static final String PRECISION_MAS ="Precicion mas";
	private static final String PRECISION_MENOS ="Precicion menos";
	private static final String PULSO_PRECISION_MAS="z";
	private static final String PULSO_PRECISION_MENOS="x";
	
	private JLabel labelPrecision;

	
	private JButton encenderLed;
	private JButton buttonIzquierda;
	private JButton buttonDerecha;
	private JButton buttonArriba;
	private JButton buttonAbajo;
	
	private JButton abrirPinza;
	private JButton cerrarPinza;
	private JButton muniecaArriba;
	private JButton muniecaAbajo;
	
	private JButton precisionMas;
	private JButton precisionMenos;
	
	private JButton moverAdelante;
	private JButton moverAtras;
	
	
	
	
	
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
		
		
		GridLayout gridLayout = new GridLayout(0,3);
		
		labelPrecision =new JLabel("1000");
		
		encenderLed = new JButton(ENCENDER_LED);
		
		buttonIzquierda = new JButton(IZQUIERDA);
		buttonDerecha = new JButton(DERECHA);

		buttonArriba = new JButton(ARRIBA);
		buttonAbajo = new JButton(ABAJO);
		
		abrirPinza = new JButton(PINZA_ABRIR);
		cerrarPinza =new JButton(PINZA_CERRAR);
		
		muniecaAbajo =  new JButton(MUNIECA_ABAJO);
		muniecaArriba = new JButton(MUNIECA_ARRIBA);
		
		moverAdelante = new JButton(ADELANTE);
		moverAtras = new JButton(ATRAS);
		
		precisionMas = new JButton(PRECISION_MAS);
		precisionMenos = new JButton(PRECISION_MENOS);
		

		label = new JLabel("Turn On/Off LED:");

		buttonDerecha.addActionListener(this);
		buttonIzquierda.addActionListener(this);
		buttonAbajo.addActionListener(this);
		buttonArriba.addActionListener(this);
		encenderLed.addActionListener(this);
		abrirPinza.addActionListener(this);
		cerrarPinza.addActionListener(this);
		muniecaAbajo.addActionListener(this);
		muniecaArriba.addActionListener(this);
		precisionMas.addActionListener(this);
		precisionMenos.addActionListener(this);
		moverAdelante.addActionListener(this);
		moverAtras.addActionListener(this);

		setLayout(gridLayout);
		add(label);
		add(new Label(""));
		add(new Label(""));
		
		add(buttonArriba);
		add(muniecaArriba);
		add(moverAdelante);
		
		add(buttonAbajo);
		add(muniecaAbajo);
		add(moverAtras);
		
		add(abrirPinza);
		add(new JLabel(""));
		add(cerrarPinza);
		
		add(buttonIzquierda);
		add(new JLabel(""));
		add(buttonDerecha);
		
		add(new JLabel(""));
		add(encenderLed);
		add(new JLabel(""));
		
		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));
		
		
		add(precisionMas);
		add(labelPrecision);
		add(precisionMenos);
	
		
		setVisible(true);
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
		
		case PINZA_ABRIR:{
			sendData(String.valueOf(PULSO_ABRIR));
			break;
			
			
		}
		
		case PINZA_CERRAR:{
			
			sendData(String.valueOf(PULSO_CERRAR));
			break;
			
		}
		
		case MUNIECA_ABAJO:{
			
			sendData(String.valueOf(PULSO_MUNIECA_ABAJO));
			break;
			
		}
		
		case MUNIECA_ARRIBA:{
			sendData(String.valueOf(PULSO_MUNIECA_ARRIBA));
			break;
			
		}
		
		case ENCENDER_LED:{
			sendData(String.valueOf(PULSO_LED));
			break;
			
		}
		
		case PRECISION_MAS:{
			sendData(PULSO_PRECISION_MAS);
			labelPrecision.setText(String.valueOf(Integer.parseInt(labelPrecision.getText())-50));
			break;
			
		}
		
		case PRECISION_MENOS:{
			sendData(PULSO_PRECISION_MENOS);
			labelPrecision.setText(String.valueOf(Integer.parseInt(labelPrecision.getText())+50));
			break;
			
		}
		
		case ADELANTE:{
			
			sendData(PULSO_ADELANTE);
			break;
			
		}
		
		case ATRAS:{
			
			sendData(PULSO_ATRAS);
			break;
			
		}
		
		
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