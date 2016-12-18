package ar.com.sourcesistemas.arduino;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Inicio extends JFrame{

	public Inicio(){
//	JFrame frame = new JFrame("Turn On/Off LED example");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//Create and set up the content pane.
	
	Botonera botonera = new Botonera();
//    newContentPane.setOpaque(true); //content panes must be opaque
//    this.add(botonera);
    botonera.setOpaque(true); //content panes must be opaque
    this.setContentPane(botonera);
    
    //Display the window.
    this.pack();
    setSize(600, 300);
    this.setVisible(true);
	}
}
