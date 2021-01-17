class Ecuacion
{
	private double real, imag;
	private double x1, y1, x2, y2;
	private int cota;
	private long iteraciones;
	public double a, bi;
	private int contador;
  			
	public Ecuacion(double real, double imag, double x1, double y1, double x2, double y2, int cota, long iteraciones)
	{
		this.real = real;
		this.imag = imag;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.cota = cota;
		this.iteraciones = iteraciones;
		
		this.bi = 0.0;
		this.a = 0.0;
		this.contador = 0;
	}
	
	public double itera()
	{
	  // Inicialización de variables.
		contador = 0;
				
		// Componentes del número complejo Z.
		double c1 = real;
		double c2 = imag;
					
		// Variables auxiliares.
		double c_aux;
		double norma_z = 0.0;
					
		// Bucle en el que se itera Z(n+1) = Z(n)*Z(n)+C.
		while (norma_z<cota && ++contador<iteraciones)
		{
		  c_aux = c1;
			c1 = c1*c1 - c2*c2 + a;
			c2 = 2.*c_aux*c2 + bi;
						
			// Norma del número complejo.
			norma_z = c1*c1 + c2*c2;
		}
		
		return (norma_z);
	}
	
	public boolean pertenece(double norma_z)
	{
		if (norma_z>=cota)
			return (true);
		return (false);
	}
	
	public int getIndice()
	{
		return (contador%(Pizarra.NUM_COLORES-1));
	}
}	
