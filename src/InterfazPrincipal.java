import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class InterfazPrincipal  extends JFrame implements ActionListener  {
	private Simulaciones mundo;
	private JTextField txtSencilla;
	private JLabel lblSencilla;
	private JButton btnSencilla;
	
	private JButton btnVecinos;
	private JLabel lblVecinos;
	public InterfazPrincipal(){
		setTitle("Simulaciones Moviplus");
		setSize(450,205);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		mundo=new Simulaciones();
		
		JPanel panelSimSencilla=new JPanel();
		panelSimSencilla.setLayout(new GridLayout(2,2));
		panelSimSencilla.setPreferredSize(new Dimension(0,75));
		TitledBorder border=BorderFactory.createTitledBorder("Realizar simulación con 1 k");
		border.setTitleColor(Color.GRAY);
		panelSimSencilla.setBorder(border);
		
		JPanel panelSimVecinos=new JPanel();
		panelSimVecinos.setLayout(new GridLayout(1,2));
		panelSimVecinos.setPreferredSize(new Dimension(0,50));
		TitledBorder border3=BorderFactory.createTitledBorder("Realizar simulaciones con k: 1->79");
		border3.setTitleColor(Color.GRAY);
		panelSimVecinos.setBorder(border3);
		
		txtSencilla=new JTextField("5");
		lblSencilla=new JLabel("Ingrese el numero k:") ;
		btnSencilla=new JButton("Comenzar!");
		btnSencilla.setActionCommand("SENCILLA");
		btnSencilla.addActionListener(this);
		panelSimSencilla.add(lblSencilla);
		panelSimSencilla.add(txtSencilla);
		panelSimSencilla.add(new JLabel(""));
		panelSimSencilla.add(btnSencilla);
		
		lblVecinos=new JLabel("Simular multiples k (1->79):");
		btnVecinos=new JButton("Comenzar!");
		btnVecinos.setActionCommand("MULTIPLE");
		btnVecinos.addActionListener(this);
		panelSimVecinos.add(lblVecinos);
		panelSimVecinos.add(btnVecinos);
		
		
		add(panelSimSencilla,BorderLayout.NORTH);
		add(panelSimVecinos,BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) {
		InterfazPrincipal interfaz=new InterfazPrincipal();
		interfaz.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		if(comando.equals("SENCILLA")){
			try{
			int t=Integer.parseInt(txtSencilla.getText().trim());
			if(t<=1||t>80){
				JOptionPane.showMessageDialog (null, "k no debe ser menor o igual a uno, ni mayor a 80. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}else{
			mundo.simulacion1K(t-1);
			
			}
			}catch(Exception ex){
				JOptionPane.showMessageDialog (null, "Debe ingresar un número. Vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}
			
		}
		else if(comando.equals("MULTIPLE")){
			mundo.simulacionTodasLasK();
		}
	}
}
