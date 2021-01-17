import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;

class Pizarra extends JPanel
{
	public final int NADA = 0;
	public final int FRACTAL = 1;
	public final int ZOOM_IN_PROPORCION = 2;
	public final int ZOOM_OUT_PROPORCION = 3;
	public final int ZOOM = 4;
	
	public static int NUM_COLORES = 17;
	
	private int opcionDibujo;
	private double a1, a2, b1, b2;
	private double z1, z2;
	private int cota;
	private int tipoColores;
	private long nIteraciones;
	private double angulo;
	private double x0, y0, x1, y1;
	
	Color[] colores = new Color[NUM_COLORES];
		
	public Pizarra(double z1, double z2, double a1, double a2, double b1, double b2, long nIteraciones, double angulo, int tipoColores, int cota)
	{
		setOpcionDibujo(FRACTAL);
		
		setZInicial(z1, z2);
		setIntervalo(a1, a2, b1, b2);
		setIteraciones(nIteraciones);
		setAngulo(angulo);
		setTipoColores(tipoColores);
		setCota(cota);
		setIntervaloZoom(0.0, 0.0, 0.0, 0.0);
	}
	
	public void setZInicial(double z1, double z2)
	{
		this.z1 = z1;
		this.z2 = z2;
	}	
	
	public void setCota(int cota)
	{
		this.cota = cota;
	}
	
	public void setTipoColores(int tipoColores)
	{
		this.tipoColores = tipoColores;
		
		switch (tipoColores)
		{
			case 1:
			{
				colores[0] = Color.gray;
				colores[1] = new Color(209,225,17);
				colores[2] = Color.blue;
				colores[3] = Color.cyan;
				colores[4] = Color.darkGray;
				colores[5] = Color.pink;
				colores[6] = new Color(155,90,47);
				colores[7] = Color.green;
				colores[8] = Color.lightGray;
				colores[9] = Color.magenta;
				colores[10] = new Color(58,184,62);
				colores[11] = Color.yellow;
				colores[12] = Color.orange;
				colores[13] = Color.red;
				colores[14] = new Color(186,35,207);
				colores[15] = new Color(134,253,70);
				colores[16] = Color.black;
			} break;
			case 2:
			{
				colores[0] = Color.white;
				colores[1] = Color.blue;
				colores[2] = Color.cyan;
				colores[3] = Color.gray;
				colores[4] = Color.darkGray;
				colores[5] = Color.lightGray;
				colores[6] = Color.green;
				colores[7] = Color.magenta;
				colores[8] = Color.orange;
				colores[9] = Color.pink;
				colores[10] = Color.yellow;
				colores[11] = new Color(209,225,17);
				colores[12] = new Color(58,184,62);
				colores[13] = new Color(155,90,47);
				colores[14] = new Color(186,35,207);
				colores[15] = new Color(134,253,70);
				colores[16] = Color.black;
			} break;
			
			case 3:
			{
				Random random = new Random();
				colores[0] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[1] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[2] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[3] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[4] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[5] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[6] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[7] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[8] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[9] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[10] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[11] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[12] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[13] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[14] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[15] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colores[16] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
			} break;
			default: break;
		}	
	}
	
	public void setIteraciones(long nIteraciones)
	{
		this.nIteraciones = nIteraciones;
	}	
	
	public void setAngulo(double angulo)
	{
		this.angulo = angulo;
	}
	
	public void setIntervalo(double a1, double a2, double b1, double b2)
	{
		this.a1 = a1;
		this.a2 = a2;
		this.b1 = b1;
		this.b2 = b2;
	}
	
	public void setIntervaloZoom(double x0, double y0, double x1, double y1)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		drawFondo(g2);
		
		drawMandelbrot(g2);
		
		if (opcionDibujo == ZOOM_IN_PROPORCION)
			drawRecuadroProporcion(g2);
		
		if (opcionDibujo == ZOOM_OUT_PROPORCION)
			drawRecuadroProporcion(g2);
		
		if (opcionDibujo == ZOOM)
			drawRecuadro(g2);
	}
	
	public void setOpcionDibujo(int opcion)
	{
		opcionDibujo = opcion;
		repaint();
	}
	
	public void setOpcionDibujo(int opcion, int x0, int y0, int x1, int y1)
	{
		opcionDibujo = opcion;
		setIntervaloZoom(x0, y0, x1, y1);
		repaint();
	}
	
	private void drawMandelbrot(Graphics2D g2)
	{
		Ecuacion ecuacion = new Ecuacion(this.z1, this.z2, this.a1, this.a2, this.b1, this.b2, this.cota, this.nIteraciones);
		
		g2.rotate(Math.toRadians(angulo));
				
		for (int y=0; y<getSize().height; y++)
		{
			ecuacion.bi = transformaY(y);
			for (int x=0; x<getSize().width; x++)
			{
				ecuacion.a = transformaX(x);
				if (ecuacion.pertenece(ecuacion.itera()))
				{
					g2.setColor(colores[ecuacion.getIndice()]);
					g2.drawLine(x, y, x, y);
				}	
			}
		}
	}
	
	private double transformaY(int y)
	{
		return (this.a2 + (y/((double)getSize().height)) * (this.b2 - this.a2));
	}	
	
	private double transformaX(int x)
	{
		return (this.a1 + (x/((double)getSize().width)) * (this.b1 - this.a1));
	}
	
	private void drawFondo(Graphics2D g2)
	{
		g2.setColor(Color.black);
		g2.fill(new Rectangle2D.Double(0, 0, getSize().width, getSize().height));
	}
	
	private void drawRecuadro(Graphics2D g2)
	{
		g2.setColor(Color.white);
		g2.draw(new Line2D.Double(x0, y0, x1, y0));
		g2.draw(new Line2D.Double(x0, y1, x1, y1));
		g2.draw(new Line2D.Double(x0, y0, x0, y1));
		g2.draw(new Line2D.Double(x1, y0, x1, y1));
	}
	
	private void drawRecuadroProporcion(Graphics2D g2)
	{
		int ancho_cuadro = getSize().width/2;
		int alto_cuadro = getSize().height/2;
		
		int punto_medio_x = getSize().width/2;
		int punto_medio_y = getSize().height/2;
		
		g2.setColor(Color.white);
		g2.draw(new Rectangle2D.Double(punto_medio_x - ancho_cuadro/2, punto_medio_y - alto_cuadro/2, ancho_cuadro, alto_cuadro));
	}
}
