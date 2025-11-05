package ejercicio;

import java.util.Random;

public class App {
	public static void main(String[] args) {

		// Inicializamos componentes principales
		Planificador planificador = new Planificador();
		ColaBloqueados colaBloq = new ColaBloqueados();
		ColaTerminados colaTerm = new ColaTerminados();
		// Crear procesos iniciales con quantums aleatorios
		
		for (int i = 0; i < 10; i++) {
			
			Proceso p = new Proceso(i+1);
			planificador.agregar(p);
		}

		// Crear CPU y rutina de E/S
		Cpu cpu = new Cpu(planificador, colaBloq, colaTerm);
		RutinaES rutinaES = new RutinaES(cpu, planificador, colaBloq);

		// Lanzar los hilos
		Thread hiloCPU = new Thread(cpu, "CPU");
		Thread hiloES = new Thread(rutinaES, "RutinaES");

		hiloCPU.start();
		hiloES.start();

		// Esperar a que termine la CPU
		try {
			hiloCPU.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Detener la rutina de E/S cuando la CPU haya finalizado
		rutinaES.detener();

		try {
			hiloES.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		System.out.println("\nSimulación finalizada.");
		
		System.out.println("Datos: ");
		
		ordenFinalizacion(colaTerm);
		
		
		
		
		
	}

	private static void ordenFinalizacion(ColaTerminados colaTerm) {
		System.out.println("Orden de Terminacion de los procesos");
		for (Proceso p : colaTerm.getTerminados()) {
			System.out.print(p.getId() +" ");
		}
		
	}
}