package ejercicio;

import java.util.Random;

public class Cpu implements Runnable {
	private final Planificador planificador;
	private final ColaBloqueados colaBloq;
	private final ColaTerminados colaTerm;
	private final Random r = new Random();
	private volatile boolean running = true;
	private Proceso procesoActual = null;

	public Cpu(Planificador planificador, ColaBloqueados colaBloq, ColaTerminados colaTerm) {
		this.planificador = planificador;
		this.colaBloq = colaBloq;
		this.colaTerm = colaTerm;
	}

	public void detener() {
		running = false;
	}

	public void interrumpirPorES() {
		if (procesoActual != null && procesoActual.getEstado() == Estado.EJECUTANDO) {
			System.out.printf("⚡ E/S interrumpe proceso %d%n", procesoActual.getId());
			procesoActual.setEstado(Estado.BLOQUEADO);
			colaBloq.bloquear(procesoActual);
			procesoActual = null;
		}
	}

	@Override
	public void run() {
		while (running) {
			Proceso p = planificador.siguiente();

			if (p == null) {
				if (colaBloq.estaVacia() && planificador.estaVacia()) {
					System.out.println("CPU: no hay procesos pendientes. Finalizando.");
					detener();
					//importante el return cuando no quedan procesos para evitar el null point exception
					return;
				} 
			}

			procesoActual = p;
			//comprobamos primero que su quantum sea 0 ya que puede que se bloquee
			//cuando su quantum es 0 y entrar en bucle infinito, forzamos a salir
			if (procesoActual.getQuantum() == 0) {
				p.setEstado(Estado.TERMINADO);
				p.finProceso();
				colaTerm.agregar(p);
			} else {

				p.setEstado(Estado.EJECUTANDO);
				System.out.printf("➡️ Ejecutando proceso %d (quantum actual=%d)%n", p.getId(), p.getQuantum());

				int ejecutadas = 0;
				int ciclosAleatorios = r.nextInt(200, 900);

				while (ciclosAleatorios > 0 && p.getEstado() != Estado.BLOQUEADO) {
					 // simula trabajo
					for (int j = 0; j < 1000; j++);
						
					ejecutadas++;
					ciclosAleatorios--;
				}

				p.consumirQuantum(ejecutadas);
				System.out.printf("⏱️ Proceso %d ejecutó %d ciclos (restante=%d)%n", p.getId(), ejecutadas,
						p.getQuantum());

				if (p.getEstado() == Estado.EJECUTANDO) {
					if (p.getQuantum() > 0) {
						p.setEstado(Estado.LISTO);
						planificador.agregar(p);
						System.out.printf("🔁 Proceso %d reencolado (restante=%d)%n", p.getId(), p.getQuantum());
					} else {
						p.setEstado(Estado.TERMINADO);
						p.finProceso();
						colaTerm.agregar(p);
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
