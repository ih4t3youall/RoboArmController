
package ar.com.sourcesistemas.arduino;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.sourcesistemas.arduino.constantes.Constantes;
import gnu.io.SerialPort;

public class Botonera extends JPanel implements ActionListener {

	private Conexion conn;


	

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
	private String PORT_NAME = "/dev/ttyACM0";
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public  Botonera() {

		conn = new Conexion();
		
		new Server(conn);

		GridLayout gridLayout = new GridLayout(0, 3);
		this.setLayout(new GridLayout(0,3));
		labelPrecision = new JLabel("1000");
		
		encenderLed = new JButton(Constantes.ENCENDER_LED);

		buttonIzquierda = new JButton(Constantes.IZQUIERDA);
		buttonDerecha = new JButton(Constantes.DERECHA);

		buttonArriba = new JButton(Constantes.ARRIBA);
		buttonAbajo = new JButton(Constantes.ABAJO);

		abrirPinza = new JButton(Constantes.PINZA_ABRIR);
		cerrarPinza = new JButton(Constantes.PINZA_CERRAR);

		muniecaAbajo = new JButton(Constantes.MUNIECA_ABAJO);
		muniecaArriba = new JButton(Constantes.MUNIECA_ARRIBA);

		moverAdelante = new JButton(Constantes.ADELANTE);
		moverAtras = new JButton(Constantes.ATRAS);

		precisionMas = new JButton(Constantes.PRECISION_MAS);
		precisionMenos = new JButton(Constantes.PRECISION_MENOS);

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

		
		this.add(label);
		this.add(new Label(""));
		this.add(new Label(""));

		this.add(buttonArriba);
		this.add(muniecaArriba);
		this.add(moverAdelante);

		this.add(buttonAbajo);
		this.add(muniecaAbajo);
		this.add(moverAtras);

		this.add(abrirPinza);
		this.add(new JLabel(""));
		this.add(cerrarPinza);

		this.add(buttonIzquierda);
		this.add(new JLabel(""));
		this.add(buttonDerecha);

		this.add(new JLabel(""));
		this.add(encenderLed);
		this.add(new JLabel(""));

		this.add(new JLabel(""));
		this.add(new JLabel(""));
		this.add(new JLabel(""));

		this.add(precisionMas);
		this.add(labelPrecision);
		this.add(precisionMenos);


	}

	@Override
	public void actionPerformed(ActionEvent e) {

		
		conn.codeSender(e.getActionCommand());
		

	}

}
