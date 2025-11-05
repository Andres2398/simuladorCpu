package ejercicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class App {

	public static void start() {

		// Inicializamos componentes principales
		Planificador planificador = new Planificador();
		ColaBloqueados colaBloq = new ColaBloqueados();
		ColaTerminados colaTerm = new ColaTerminados();

		Despachador despachador = new Despachador(colaBloq, colaTerm, planificador);

		// Crear procesos iniciales con quantums aleatorios

		for (int i = 0; i < 30; i++) {

			Proceso p = new Proceso(i + 1);
			planificador.agregar(p);
		}

		// Crear CPU y rutina de E/S
		Cpu cpu = new Cpu(planificador, colaBloq, despachador);
		RutinaES rutinaES = new RutinaES(cpu, planificador, colaBloq, despachador);

		// Lanzar los hilos
		Thread hiloCPU = new Thread(cpu, "CPU");
		Thread hiloES = new Thread(rutinaES, "RutinaES");

		hiloCPU.start();
		hiloES.start();

		// Esperar a que termine la CPU
		try {
			hiloCPU.join();
		} catch (InterruptedException e) {
			System.out.println(e);
		}

		rutinaES.detener();

		try {
			hiloES.join();
		} catch (InterruptedException e) {
			System.out.println(e);
		}

		System.out.println("\nSimulación finalizada.");

		System.out.println("Datos: ");

		ordenFinalizacion(colaTerm);

	}

	private static void ordenFinalizacion(ColaTerminados colaTerm) {
		System.out.println("Orden de Terminacion de los procesos");
		for (Proceso p : colaTerm.getTerminados()) {
			System.out.print(p.getId() + " ");
		}

	}

	// TESTER
	public static void main(String[] args) {
		long tiempos[] = new long[4];

		for (int i = 0; i < tiempos.length; i++) {
			long inicio = System.currentTimeMillis();
			start();
			long fin = System.currentTimeMillis();
			tiempos[i] = fin - inicio;
			System.out.println("tiempo de la vuela " + i + tiempos[i]);

		}
		double media = media(tiempos);
		double mediana = mediana(tiempos);
		Long moda = moda(tiempos);

		File f = new File("./SimuladorCpu/src/Fichero.txt/");
		File fichero = new File(f.getAbsolutePath());

		try (FileWriter fw = new FileWriter(fichero,true); 
			BufferedWriter bw = new BufferedWriter(fw);) {

			String s = Double.toString(media);
			bw.write("media: ");
			bw.write(s);
			bw.newLine();

			if (moda != null) {
				s = Long.toString(moda);

				bw.write("moda: ");
				bw.write(s);
				bw.newLine();
			} else {

				bw.write("moda: ");
				bw.write("No se encontro");
				bw.newLine();
			}
			s = Double.toString(mediana);
			bw.write("mediana: ");
			bw.write(s);
			bw.newLine();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static Long moda(long[] t) {

		HashMap<Long, Integer> moda = new HashMap<Long, Integer>();

		for (long l : t) {
			if (moda.containsKey(l)) {
				Integer valor = moda.get(l);
				valor++;
				moda.put(l, valor);
			} else {

				moda.put(l, 1);
			}

		}
		Long modaKey = null;
		int maxCount = 0;

		for (Map.Entry<Long, Integer> entry : moda.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxCount = entry.getValue();
				modaKey = entry.getKey();
			}
		}

		return (maxCount > 1) ? modaKey : null;
	}

	private static long mediana(long[] t) {
		ordenar(t);

		return (t[t.length / 2] + t[t.length / 2 - 1]) / 2;

	}

	private static void ordenar(long[] t) {
		boolean cambio = true;
		while (cambio) {
			cambio = false;
			for (int i = 0; i < t.length - 1; i++) {
				if (t[i] > t[i + 1]) {
					long aux = t[i];
					t[i] = t[i + 1];
					t[i + 1] = aux;
					cambio = true;

				}
			}
		}

	}

	private static double media(long[] t) {
		long suma = 0;
		for (long x : t)
			suma += x;
		return (double) suma / t.length;

	}
}