package ejercicio;

import java.util.Random;

/**
 * Clase Cpu que se dedicara a consumir el quantum de los procesos
 */
public class Cpu implements Runnable {
	private  Planificador planificador;
	private  ColaBloqueados colaBloq;

	private Random r = new Random();
	private boolean running = true;
	private Proceso procesoActual = null;
	private Despachador despachador;

	public Cpu(Planificador planificador, ColaBloqueados colaBloq, Despachador despachador) {
		this.planificador = planificador;
		this.colaBloq = colaBloq;

		this.despachador = despachador;
	}

	public void detener() {
		running = false;
	}

	/**
	 * Metodo para sacar bloquear el proceso actual llamado desde el hilo de E/S
	 */
	public void interrumpirPorES() {
		if (procesoActual != null && procesoActual.getEstado() == Estado.EJECUTANDO) {
			System.out.println(Color.VERDE+"E/S interrumpe proceso "+ procesoActual.getId()+Color.RESET);
			// importante try catch ya que puede que el proceso cambie a null ya que los 2
			// hilos interactuan sobre el
			// doble verficacion
			try {
				if (procesoActual != null) {
					procesoActual.setEstado(Estado.BLOQUEADO);
					despachador.agregarAcolaBloqueados(procesoActual);
					procesoActual = null;
				} else
					throw new NullPointerException();
			} catch (NullPointerException e) {

			}

		}
	}

	/**
	 * run del hilo Cpu donde que se dedica a consumir recursos de la cpu
	 */
	@Override
	public void run() {
		while (running) {
			Proceso p = planificador.siguiente();

			if (p == null) {
				if (colaBloq.estaVacia() && planificador.estaVacia()) {
					System.out.println(Color.AZUL+"CPU: no hay procesos pendientes. Finalizando."+Color.RESET);
					detener();
					// importante el return cuando no quedan procesos para evitar el null point
					// exception
					return;
				}
			}

			procesoActual = p;
			// comprobamos primero que su quantum sea 0 ya que puede que se bloquee
			// cuando su quantum es 0 y entrar en bucle infinito, forzamos a salir
			if (p.getQuantum() == 0) {
				p.setEstado(Estado.TERMINADO);
				p.finProceso();
				despachador.agregarAcolaTerminados(p);

			} else {

				p.setEstado(Estado.EJECUTANDO);
				System.out.println(Color.AZUL+"Ejecutando proceso" + p.getId() + " (quantum actual= " + p.getQuantum() + ")"+Color.RESET);

				int ejecutadas = 0;
				int ciclosAleatorios = r.nextInt(200, 900);

				// si los ciclos asignados son mayor al quantum restante, igualos ciclos al
				// quantum
				if (ciclosAleatorios > p.getQuantum())
					ciclosAleatorios = p.getQuantum();

				while (ciclosAleatorios > 0 && p.getEstado() != Estado.BLOQUEADO) {
					// simular trabajo
					for (int j = 0; j < 1000; j++)
						;

					ejecutadas++;
					ciclosAleatorios--;
				}

				p.consumirQuantum(ejecutadas);
				System.out.println(Color.AZUL+"Proceso " + p.getId() + " ejecutó " + ejecutadas + " ciclos (restante="
						+ p.getQuantum() + ")"+Color.RESET);

				if (p.getEstado() == Estado.EJECUTANDO) {
					if (p.getQuantum() > 0) {
						p.setEstado(Estado.LISTO);
						despachador.agregarAcolaListos(p);
						System.out.println(Color.AZUL+
								"Proceso " + p.getId() + " reencolado ciclos (restante=" + p.getQuantum() + ")"+Color.RESET);
					} else {
						p.setEstado(Estado.TERMINADO);
						p.finProceso();
						despachador.agregarAcolaTerminados(p);

						System.out.println(Color.AZUL+"Proceso " + p.getId() + " terminó completamente."+Color.RESET);
					}
				}
			}
			procesoActual = null;

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}

		System.out.println(Color.AZUL+"CPU detenida."+Color.RESET);
	}
}
