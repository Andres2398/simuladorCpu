package ejercicio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CLASE QUE SIRVE PARA INICIALIZAR EL PROGRAMA (TESTER)
 */
public class App {

	/**
	 * 
	 * 
	 * LOS COLORES QUE SALNDRAN POR CONSOLA PARA DIFRENCIAR QUE HILO ESTA
	 * ESCRIBIENDO HAN SIDO ELEGIDOS PARA ECLIPSE EN MODO OSCURO
	 * 
	 * 
	 * 
	 * 
	 * 
	 * EN CASO DE BORRAR EL FICHERO PRINCIPAL SE RECOMIENDA BORRAR EL FICHERO
	 * INDICES PARA QUE SE ESTEN SINCRONIZADOS
	 * 
	 * 
	 */

	public static ColaTerminados start() {

		Planificador planificador = new Planificador();
		ColaBloqueados colaBloq = new ColaBloqueados();
		ColaTerminados colaTerm = new ColaTerminados();

		Despachador despachador = new Despachador(colaBloq, colaTerm, planificador);

		for (int i = 0; i < 30; i++) {

			Proceso p = new Proceso(i + 1);
			planificador.agregar(p);
		}

		Cpu cpu = new Cpu(planificador, colaBloq, despachador);
		RutinaES rutinaES = new RutinaES(cpu, colaBloq, despachador);

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

		
		//detener E/S
		rutinaES.detener();

		try {
			hiloES.join();
		} catch (InterruptedException e) {
			System.out.println(e);
		}

		System.out.println("\nSimulación finalizada.");

		return colaTerm;
	}

	// TESTER
	public static void main(String[] args) {

		File f = new File("./src/Fichero.txt");
		File fichero = new File(f.getAbsolutePath());

		// true para no sobreescribir fichero

		try (FileWriter fw = new FileWriter(fichero, true); BufferedWriter bw = new BufferedWriter(fw)) {

			// cuantas vueltas dara el programa
			long tiempos[] = new long[10];

			int contador = contadorEjecuciones();
			bw.write("Ejecución número: " + contador);
			bw.newLine();
			bw.newLine();
			for (int i = 0; i < tiempos.length; i++) {

				long inicio = System.currentTimeMillis();
				ColaTerminados cola = start();
				long fin = System.currentTimeMillis();
				tiempos[i] = fin - inicio;

				String s = "Tanda " + (i + 1) + " de procesos, orden de finalización: ";
				bw.write(s);
				bw.newLine();
				s = "";
				for (Proceso p : cola.getTerminados()) {
					s += p.toString();
				}
				bw.write(s);
				bw.newLine();
				bw.newLine();
			}

			double media = media(tiempos);
			double mediana = mediana(tiempos);
			Long moda = moda(tiempos);

			String s = Double.toString(media);
			bw.write("media: ");
			bw.write(s + " ms");
			bw.newLine();

			if (moda != null) {
				s = Long.toString(moda);

				bw.write("moda: ");
				bw.write(s + " ms");
				bw.newLine();
			} else {

				bw.write("moda: ");
				bw.write("No se encontró");
				bw.newLine();
			}
			s = Double.toString(mediana);
			bw.write("mediana: ");
			bw.write(s + " ms");
			bw.newLine();
			bw.newLine();
			bw.newLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static int contadorEjecuciones() {
		int contador = 0;
		File fichero = new File("./src/indices.txt");

		try {
			// Se comprueba si existe el fichero para que en caso de que no exista,
			// inicializarlo con el indice 0
			if (!fichero.exists()) {
				try (FileWriter fw = new FileWriter(fichero)) {
					fw.write("0");
				}
			}

			try (Scanner sc = new Scanner(fichero)) {
				if (sc.hasNextInt()) {
					contador = sc.nextInt();
				}
			}

			contador++;
			try (FileWriter fw = new FileWriter(fichero)) {
				fw.write(String.valueOf(contador));
			}

		} catch (IOException e) {
			System.out.println(e);
		}

		return contador;
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
		// al ser par la longitud del array solo tenemos que comprobar la mediana de
		// esta manera
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