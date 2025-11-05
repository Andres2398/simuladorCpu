package ejercicio;

import java.util.Random;

public class RutinaES implements Runnable {

	private final Cpu cpu;
	private final Planificador planificador;
	private final ColaBloqueados colaBloq;
	private volatile boolean running = true;
	private final Random r = new Random();

	public RutinaES(Cpu cpu, Planificador planificador, ColaBloqueados colaBloq) {
		this.cpu = cpu;
		this.planificador = planificador;
		this.colaBloq = colaBloq;
	}

	public void detener() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			int valor = r.nextInt(1001); // número entre 0–1000

			if (valor == 0) {
				cpu.interrumpirPorES();
			}

			
		

			// desbloquea aleatoriamente
			if (r.nextInt(500) == 0 && !colaBloq.estaVacia()) {
				Proceso desbloq = colaBloq.desbloquearAleatorio();
				if (desbloq != null)
					planificador.agregar(desbloq);
			}
		}

		System.out.println("Rutina E/S: terminada.");
	}
}