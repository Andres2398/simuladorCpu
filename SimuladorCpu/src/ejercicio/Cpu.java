package ejercicio;

import java.util.Random;

/**
 * Clase Cpu
 */
public class Cpu implements Runnable {
	private final Planificador planificador;
	private final ColaBloqueados colaBloq;

	private final Random r = new Random();
	private volatile boolean running = true;
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
			System.out.printf("⚡ E/S interrumpe proceso %d%n", procesoActual.getId());
			//importante try catch ya que puede que el proceso cambie a null ya que los 2 hilos interactuan sobre el
			//doble verficacion
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
					System.out.println("CPU: no hay procesos pendientes. Finalizando.");
					detener();
					// importante el return cuando no quedan procesos para evitar el null point
					// exception
					return;
				}
			}

			procesoActual = p;
			// comprobamos primero que su quantum sea 0 ya que puede que se bloquee
			// cuando su quantum es 0 y entrar en bucle infinito, forzamos a salir
			if (procesoActual.getQuantum() == 0) {
				p.setEstado(Estado.TERMINADO);
				p.finProceso();
				despachador.agregarAcolaTerminados(p);

			} else {

				p.setEstado(Estado.EJECUTANDO);
				System.out.printf("➡️ Ejecutando proceso %d (quantum actual=%d)%n", p.getId(), p.getQuantum());

				int ejecutadas = 0;
				int ciclosAleatorios = r.nextInt(200, 900);

				while (ciclosAleatorios > 0 && p.getEstado() != Estado.BLOQUEADO) {
					// simula trabajo
					for (int j = 0; j < 1000; j++)
						;

					ejecutadas++;
					ciclosAleatorios--;
				}

				p.consumirQuantum(ejecutadas);
				System.out.printf("⏱️ Proceso %d ejecutó %d ciclos (restante=%d)%n", p.getId(), ejecutadas,
						p.getQuantum());

				if (p.getEstado() == Estado.EJECUTANDO) {
					if (p.getQuantum() > 0) {
						p.setEstado(Estado.LISTO);
						despachador.agregarAcolaListos(procesoActual);
						System.out.printf("🔁 Proceso %d reencolado (restante=%d)%n", p.getId(), p.getQuantum());
					} else {
						p.setEstado(Estado.TERMINADO);
						p.finProceso();
						despachador.agregarAcolaTerminados(procesoActual);

						System.out.printf("✅ Proceso %d terminó completamente.%n", p.getId());
					}
				}
			}
			procesoActual = null;

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}

		System.out.println("🧠 CPU detenida.");
	}
}
