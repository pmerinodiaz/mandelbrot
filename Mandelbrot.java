import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Mandelbrot extends JFrame
{
	public final int PREDETERMINADOS = 1;
	public final int ALTO_CONTRASTE = 2;
	public final int RANDOM = 3;
	
	private int anchoFrame, altoFrame;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private FrameCierre cierre = new FrameCierre();
	private MouseCoordenadas mouse = new MouseCoordenadas();
	private Container contenedor;
	private Pizarra panel;
	private JMenuBar menubar;
	private JMenu fractal, parametros, zoom, iteraciones, trasladar, rotar, colores, ayuda;
	private JMenuItem fractalNuevo, fractalSalir,
	                  parametrosZInicial, parametrosIntervalo, parametrosCota,
	                  zoomIn, zoomOut, zoomFactor,
										iteracionesPredeterminada, iteracionesIngresar,
										trasladarCentrar, trasladarPuntos,
										rotar45, rotar90, rotar180, rotarAngulo,
										coloresPredeterminados, coloresAltoContraste, coloresRandom,
										ayudaAcercaDe, ayudaEcMandelbrot, ayudaManual;
	private double z1, z2;
	private double a1, a2, b1, b2;
	private double factorIn, factorOut;
	private long nIteraciones;
	private double angulo;
	private int tipoColores;
	private int cota;
	
	public static void main(String[] args)
	{
		Mandelbrot frame = new Mandelbrot();
		frame.show();
	}	
	
	public Mandelbrot()
	{
		anchoFrame = toolkit.getScreenSize().width*3/4;
		altoFrame = toolkit.getScreenSize().height*3/4;
		
		setValoresDefault();		
		setMenu();
		setPropiedades();
		setEventos();
	}
	
	private void setValoresDefault()
	{
		z1 = 0.0;
		z2 = 0.0;
		a1 = -2.5;
		a2 = 1.5;
		b1 = 1.5;
		b2 = -1.5;
		factorIn = 2.0;
		factorOut = 2.0;
		nIteraciones = 50;
		angulo = 0;
		tipoColores = 1;
		cota = 5;
	}	
	
	private void setPropiedades()
	{
		setTitle("Fractal de Mandelbrot");
		setSize(anchoFrame, altoFrame);
		setLocation((toolkit.getScreenSize().width - anchoFrame)/2, (toolkit.getScreenSize().height - altoFrame)/2);
		setVisible(true);
		setIconImage(toolkit.getImage("Icono.gif"));
		contenedor = getContentPane();
		panel = new Pizarra(z1, z2, a1, a2, b1, b2, nIteraciones, angulo, tipoColores, cota);
		contenedor.add(panel);
	}
	
	private void setMenu()
	{
		menubar = new JMenuBar();
		menubar.add(fractal = new JMenu("Fractal"));
		fractal.add(fractalNuevo = new JMenuItem("Nuevo"));
		fractal.addSeparator();
		fractal.add(fractalSalir = new JMenuItem("Salir"));
		menubar.add(parametros = new JMenu("Parámetros"));
		parametros.add(parametrosZInicial = new JMenuItem("Z Inicial"));
		parametros.add(parametrosIntervalo = new JMenuItem("Intervalo"));
		parametros.add(parametrosCota = new JMenuItem("Cota"));
		menubar.add(zoom = new JMenu("Zomm"));
		zoom.add(zoomIn = new JMenuItem("Zoom In"));
		zoom.add(zoomOut = new JMenuItem("Zoom Out"));
		zoom.add(zoomFactor = new JMenuItem("Factor Zoom"));
		menubar.add(iteraciones = new JMenu("Iteraciones"));
		iteraciones.add(iteracionesPredeterminada = new JMenuItem("Predeterminada"));
		iteraciones.add(iteracionesIngresar = new JMenuItem("Ingresar Iteraciones"));
		menubar.add(trasladar = new JMenu("Trasladar"));
		trasladar.add(trasladarCentrar = new JMenuItem("Centrar"));
		trasladar.add(trasladarPuntos = new JMenuItem("Ingresar Puntos"));
		menubar.add(rotar = new JMenu("Rotar"));
		rotar.add(rotar45 = new JMenuItem("45º"));
		rotar.add(rotar90 = new JMenuItem("90º"));
		rotar.add(rotar180 = new JMenuItem("180º"));
		rotar.add(rotarAngulo = new JMenuItem("Ingresar Angulo"));
		menubar.add(colores = new JMenu("Colores"));
		colores.add(coloresPredeterminados = new JMenuItem("Predeterminados"));
		colores.add(coloresAltoContraste = new JMenuItem("Alto Contraste"));
		colores.add(coloresRandom = new JMenuItem("Random"));
		menubar.add(ayuda = new JMenu("Ayuda"));
		ayuda.add(ayudaAcercaDe = new JMenuItem("Acerca de..."));
		ayuda.add(ayudaEcMandelbrot = new JMenuItem("Ecuación Mandelbrot"));
		ayuda.addSeparator();
		ayuda.add(ayudaManual = new JMenuItem("Manual"));
		
		setJMenuBar(menubar);
	}
	
	private void setEventos()
	{
		addWindowListener(cierre);
		addMouseListener(mouse);
		fractalNuevo.addActionListener(new MenuTrato());
		fractalSalir.addActionListener(new MenuTrato());
		parametrosZInicial.addActionListener(new MenuTrato());
		parametrosIntervalo.addActionListener(new MenuTrato());
		parametrosCota.addActionListener(new MenuTrato());
		zoomIn.addActionListener(new MenuTrato());
		zoomOut.addActionListener(new MenuTrato());
		zoomFactor.addActionListener(new MenuTrato());
		iteracionesPredeterminada.addActionListener(new MenuTrato());
		iteracionesIngresar.addActionListener(new MenuTrato());
		trasladarCentrar.addActionListener(new MenuTrato());
		trasladarPuntos.addActionListener(new MenuTrato());
		rotar45.addActionListener(new MenuTrato());
		rotar90.addActionListener(new MenuTrato());
		rotar180.addActionListener(new MenuTrato());
		rotarAngulo.addActionListener(new MenuTrato());
		coloresPredeterminados.addActionListener(new MenuTrato());
		coloresAltoContraste.addActionListener(new MenuTrato());
		coloresRandom.addActionListener(new MenuTrato());
		ayudaAcercaDe.addActionListener(new MenuTrato());
		ayudaEcMandelbrot.addActionListener(new MenuTrato());
		ayudaManual.addActionListener(new MenuTrato());
	}
	
	class FrameCierre extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	}
	
	public class MouseCoordenadas extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			int px = e.getX();
			int py = e.getY();
			
			if (e.getClickCount()==2 && !e.isMetaDown())
			{
				int opcion = JOptionPane.showConfirmDialog(null, "Se procederá a iterar sobre el cuadro que se indica", "Zoom In", 2);
				if (opcion == JOptionPane.YES_OPTION)
 				{
					int ancho = getSize().width;
					int alto = getSize().height;
				
					a1 = a1 + ((b1-a1)*(px-(ancho/factorIn)/2.0))/ancho;
					a2 = a2 + ((b2-a2)*(py-(alto/factorIn)/2.0))/alto;
					b1 = a1 + (b1-a1)/factorIn;
					b2 = a2 + (b2-a2)/factorIn;
				
					panel.setIntervalo(a1, a2, b1, b2);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
			}
			else
			if (e.getClickCount()==2 && e.isMetaDown())
			{
				int opcion = JOptionPane.showConfirmDialog(null, "Se procederá a iterar sobre el cuadro que se indica", "Zoom Out", 2);
				if (opcion == JOptionPane.YES_OPTION)
 				{
					int ancho = getSize().width;
					int alto = getSize().height;
				
					a1 = a1 + ((b1-a1)*(px-(ancho*factorOut)/2.0))/ancho;
					a2 = a2 + ((b2-a2)*(py-(alto*factorOut)/2.0))/alto;
					b1 = a1 + (b1-a1)*factorOut;
					b2 = a2 + (b2-a2)*factorOut;
				
					panel.setIntervalo(a1, a2, b1, b2);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
			}
	 }
	}
	
	class MenuTrato implements ActionListener
  {
  public void actionPerformed(ActionEvent e)
	{
	  JMenuItem item = (JMenuItem) e.getSource();
			
		if (item.getText().equals("Nuevo"))   
		{
			setValoresDefault();
			panel.setIntervalo(a1, a2, b1, b2);
			panel.setZInicial(z1, z2);
			panel.setCota(cota);
			panel.setTipoColores(tipoColores);
			panel.setIteraciones(nIteraciones);
			panel.setAngulo(angulo);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Salir"))
		{
			System.exit(0);
		}
		if (item.getText().equals("Z Inicial"))
		{
			new DialogZInicial();
			return;
		}
		if (item.getText().equals("Intervalo"))
		{
			new DialogIntervalo();
			return;
		}
		if (item.getText().equals("Cota"))
		{
			new DialogCota();
			return;
		}
		if (item.getText().equals("Zoom In"))
		{
			panel.setOpcionDibujo(panel.ZOOM_IN_PROPORCION);
			int opcion = JOptionPane.showConfirmDialog(null, "Se procederá a iterar sobre el cuadro que se indica", "Zoom In", 2);
			if (opcion == JOptionPane.YES_OPTION)
 			{
				int ancho = getSize().width;
				int alto = getSize().height;
				
				int pm_x = ancho/2;
				int pm_y = alto/2;
				
				a1 = a1 + ((b1-a1)*(pm_x-(ancho/factorIn)/2.0))/ancho;
				a2 = a2 + ((b2-a2)*(pm_y-(alto/factorIn)/2.0))/alto;
				b1 = a1 + (b1-a1)/factorIn;
				b2 = a2 + (b2-a2)/factorIn;
				
				panel.setIntervalo(a1, a2, b1, b2);
				panel.setOpcionDibujo(panel.FRACTAL);
			}
			else panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Zoom Out"))
		{
			panel.setOpcionDibujo(panel.ZOOM_OUT_PROPORCION);
			int opcion = JOptionPane.showConfirmDialog(null, "Se procederá a iterar sobre el cuadro que se indica", "Zoom In", 2);
			if (opcion == JOptionPane.YES_OPTION)
 			{
				int ancho = getSize().width;
				int alto = getSize().height;
				
				int pm_x = ancho/2;
				int pm_y = alto/2;
				
				a1 = a1 + ((b1-a1)*(pm_x-(ancho*factorOut)/2.0))/ancho;
				a2 = a2 + ((b2-a2)*(pm_y-(alto*factorOut)/2.0))/alto;
				b1 = a1 + (b1-a1)*factorOut;
				b2 = a2 + (b2-a2)*factorOut;
				
				panel.setIntervalo(a1, a2, b1, b2);
				panel.setOpcionDibujo(panel.FRACTAL);
			}
			else panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Factor Zoom"))
		{
			new DialogFactorZoom();
			return;
		}
		if (item.getText().equals("Predeterminada"))
		{
			nIteraciones = 50;
			panel.setIteraciones(nIteraciones);
			return;
		}
		if (item.getText().equals("Ingresar Iteraciones"))
		{
		  new DialogIngresarIteraciones();
			return;
		}
		if (item.getText().equals("Centrar"))
		{
		  return;
		}
		if (item.getText().equals("Ingresar Puntos"))
		{
		  return;
		}
		if (item.getText().equals("45º"))
		{
		  angulo = 45.0;
			panel.setAngulo(angulo);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("90º"))
		{
		  angulo = 90.0;
			panel.setAngulo(angulo);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("180º"))
		{
		  angulo = 180.0;
			panel.setAngulo(angulo);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Ingresar Angulo"))
		{
		  new DialogIngresarAngulo();
			return;
		}	
		if (item.getText().equals("Predeterminados"))
		{
			tipoColores = PREDETERMINADOS;
			panel.setTipoColores(tipoColores);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Alto Contraste"))
		{
			tipoColores = ALTO_CONTRASTE;
			panel.setTipoColores(tipoColores);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Random"))
		{
			tipoColores = RANDOM;
			panel.setTipoColores(tipoColores);
			panel.setOpcionDibujo(panel.FRACTAL);
			return;
		}
		if (item.getText().equals("Acerca de..."))        { return; }
		if (item.getText().equals("Ecuación Mandelbrot")) { return; }
		if (item.getText().equals("Manual"))              { return; }	
		}
	}
	
	public class DialogZInicial extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 170;
		private JLabel lbZ1, lbZ2, lbReal, lbImag;
		private JTextField tfZ1, tfZ2;
		private JButton btAceptar, btCancelar;
			
		public DialogZInicial()
		{
			lbZ1 = new JLabel("Z1 = ");
			lbZ2 = new JLabel("Z2 = ");
			lbReal = new JLabel("(Parte Real)");
			lbImag = new JLabel("(Parte Imaginaria)");
			tfZ1 = new JTextField(""+z1);
			tfZ2 = new JTextField(""+z2);
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbZ1.setBounds(10, 20, 40, 25);
			lbZ2.setBounds(10, 50, 40, 25);
			tfZ1.setBounds(50, 20, 150, 25);
			tfZ2.setBounds(50, 50, 150, 25);
			lbReal.setBounds(210, 20, 110, 25);
			lbImag.setBounds(210, 50, 110, 25);
			btAceptar.setBounds(110, 100, 100, 25);
			btCancelar.setBounds(220, 100, 100, 25);
			panel.add(lbZ1);
			panel.add(lbZ2);
			panel.add(tfZ1);
			panel.add(tfZ2);
			panel.add(lbReal);
			panel.add(lbImag);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Z Inicial");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					z1 = new Double(tfZ1.getText()).doubleValue();
					z2 = new Double(tfZ2.getText()).doubleValue();
					setVisible(false);
					panel.setZInicial(z1, z2);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
	
	public class DialogIntervalo extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 180;
		private JLabel lbA, lbB, lbReal, lbImag, lbComaA, lbPareA, lbComaB, lbPareB;
		private JTextField tfA1, tfA2, tfB1, tfB2;
		private JButton btAceptar, btCancelar;
			
		public DialogIntervalo()
		{
			lbA = new JLabel("A = (");
			lbB = new JLabel("B = (");
			lbReal = new JLabel("Parte Real");
			lbImag = new JLabel("Parte Imaginaria");
			lbComaA = new JLabel(" , ");
			lbPareA = new JLabel(")");
			lbComaB = new JLabel(" , ");
			lbPareB = new JLabel(")");
			tfA1 = new JTextField(""+a1);
			tfA2 = new JTextField(""+a2);
			tfB1 = new JTextField(""+b1);
			tfB2 = new JTextField(""+b2);
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbReal.setBounds(50, 10, 100, 25);
			lbImag.setBounds(170, 10, 100, 25);
			
			lbA.setBounds(10, 40, 40, 25);
			tfA1.setBounds(50, 40, 100, 25);
			lbComaA.setBounds(155, 40, 10, 25);
			tfA2.setBounds(170, 40, 100, 25);
			lbPareA.setBounds(280, 40, 20, 25);
			
			lbB.setBounds(10, 70, 40, 25);
			tfB1.setBounds(50, 70, 100, 25);
			tfB2.setBounds(170, 70, 100, 25);
			lbComaB.setBounds(155, 70, 10, 25);
			lbPareB.setBounds(280, 70, 20, 25);
			
			btAceptar.setBounds(110, 120, 100, 25);
			btCancelar.setBounds(220, 120, 100, 25);
			
			panel.add(lbA);
			panel.add(lbB);
			panel.add(lbReal);
			panel.add(lbImag);
			panel.add(tfA1);
			panel.add(tfA2);
			panel.add(tfB1);
			panel.add(tfB2);
			panel.add(lbComaA);
			panel.add(lbPareA);
			panel.add(lbComaB);
			panel.add(lbPareB);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Intervalo");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					a1 = new Double(tfA1.getText()).doubleValue();
					a2 = new Double(tfA2.getText()).doubleValue();
					b1 = new Double(tfB1.getText()).doubleValue();
					b2 = new Double(tfB2.getText()).doubleValue();
					setVisible(false);
					panel.setIntervalo(a1, a2, b1, b2);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
	
	public class DialogCota extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 150;
		private JLabel lbCota;
		private JTextField tfCota;
		private JButton btAceptar, btCancelar;
			
		public DialogCota()
		{
			lbCota = new JLabel("Cota = ");
			tfCota = new JTextField(""+cota);
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbCota.setBounds(10, 20, 150, 25);
			tfCota.setBounds(160, 20, 140, 25);
			btAceptar.setBounds(110, 80, 100, 25);
			btCancelar.setBounds(220, 80, 100, 25);
			panel.add(lbCota);
			panel.add(tfCota);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Cota");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					cota = new Float(tfCota.getText()).intValue();
					setVisible(false);
					panel.setCota(cota);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
	
	public class DialogFactorZoom extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 170;
		private JLabel lbIn, lbOut;
		private JTextField tfIn, tfOut;
		private JButton btAceptar, btCancelar;
			
		public DialogFactorZoom()
		{
			lbIn = new JLabel("Factor Zoom In = ");
			lbOut = new JLabel("Factor Zoom Out = ");
			tfIn = new JTextField(""+factorIn);
			tfOut = new JTextField(""+factorOut);
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbIn.setBounds(10, 20, 120, 25);
			lbOut.setBounds(10, 50, 120, 25);
			tfIn.setBounds(130, 20, 170, 25);
			tfOut.setBounds(130, 50, 170, 25);
			btAceptar.setBounds(110, 100, 100, 25);
			btCancelar.setBounds(220, 100, 100, 25);
			panel.add(lbIn);
			panel.add(lbOut);
			panel.add(tfIn);
			panel.add(tfOut);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Factor Zoom");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					factorIn = new Double(tfIn.getText()).doubleValue();
					factorOut = new Double(tfOut.getText()).doubleValue();
					setVisible(false);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
	
	public class DialogIngresarIteraciones extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 150;
		private JLabel lbIter;
		private JTextField tfIter;
		private JButton btAceptar, btCancelar;
			
		public DialogIngresarIteraciones()
		{
			lbIter = new JLabel("Número de Iteraciones = ");
			tfIter = new JTextField(""+nIteraciones);
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbIter.setBounds(10, 20, 150, 25);
			tfIter.setBounds(160, 20, 140, 25);
			btAceptar.setBounds(110, 80, 100, 25);
			btCancelar.setBounds(220, 80, 100, 25);
			panel.add(lbIter);
			panel.add(tfIter);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Ingresar Iteraciones");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					nIteraciones = new Long(tfIter.getText()).longValue();
					setVisible(false);
					panel.setIteraciones(nIteraciones);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
	
	public class DialogIngresarAngulo extends JDialog implements ActionListener
	{
		private final int ANCHO = 330;
		private final int ALTO = 150;
		private JLabel lbAng, lbGrad;
		private JTextField tfAng;
		private JButton btAceptar, btCancelar;
			
		public DialogIngresarAngulo()
		{
			lbAng = new JLabel("Angulo = ");
			lbGrad = new JLabel("º (Grados)");
			tfAng = new JTextField();
			btAceptar = new JButton("Aceptar");
			btCancelar = new JButton("Cancelar");
			Container c = getContentPane();
			JPanel panel = new JPanel();
			panel.setLayout(null);
			lbAng.setBounds(10, 20, 100, 25);
			tfAng.setBounds(70, 20, 160, 25);
			lbGrad.setBounds(235, 20, 70, 25);
			btAceptar.setBounds(110, 80, 100, 25);
			btCancelar.setBounds(220, 80, 100, 25);
			panel.add(lbAng);
			panel.add(lbGrad);
			panel.add(tfAng);
			panel.add(btAceptar);
			panel.add(btCancelar);
			c.add(panel);
			setTitle("Ingresar Angulo");
			setSize(ANCHO, ALTO);
			setLocation((toolkit.getScreenSize().width-ANCHO)/2, (toolkit.getScreenSize().height-ALTO)/2);
			setResizable(false);
			setVisible(true);
			btAceptar.addActionListener(this);
			btCancelar.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JButton boton = (JButton) e.getSource();
			
			if (boton == btAceptar)
			{
				try
				{
					angulo = new Double(tfAng.getText()).doubleValue();
					setVisible(false);
					panel.setAngulo(angulo);
					panel.setOpcionDibujo(panel.FRACTAL);
				}
				catch (NumberFormatException excepcion)
				{
					JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error de datos", 0);
				}
			}	
			if (boton == btCancelar) setVisible(false);
		}
	}
}
