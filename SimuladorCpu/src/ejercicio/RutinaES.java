package ejercicio;

import java.util.Random;
/**
 * Clase que se dedica a simular la Rutina entrada/saldia de un ordenador, la simulamos mediante numeros aleatorios
 */
public class RutinaES implements Runnable {

	private  Cpu cpu;
	
	private  ColaBloqueados colaBloq;
	private boolean running = true;
	private  Random r = new Random();
	private Despachador despachador;

	public RutinaES(Cpu cpu, ColaBloqueados colaBloq, Despachador despachador) {
		this.cpu = cpu;
		this.colaBloq = colaBloq;
		this.despachador=despachador;
	}

	public void detener() {
		running = false;
	}

	/**
	 * Crea numeros aleatorios entre 0/6000 y si el numero es 0 bloquea el proceso
	 * Ademas crea otro numero aleatorio entre 0/2000 y si es 0 desbloquea uno
	 */
	@Override
	public void run() {
		while (running) {
			int valor = r.nextInt(30000); // número entre 0–1000

			if (valor == 0) {
				cpu.interrumpirPorES();
			}

			
		

			// desbloquea aleatoriamente
			if (r.nextInt(20000) == 3 && !colaBloq.estaVacia()) {
				Proceso desbloq = colaBloq.desbloquearAleatorio();
				if (desbloq != null)
					despachador.agregarAcolaListos(desbloq);
			}
		}

		System.out.println(Color.VERDE+"Rutina E/S: terminada."+Color.RESET);
	}
}