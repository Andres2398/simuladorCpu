package ejercicio;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Clase para introducir los procesos en una cola de ebloqueados
 */
public class ColaBloqueados {
	private final Queue<Proceso> cola = new ConcurrentLinkedQueue<>();
	private Random r = new Random();

	/**
	 * Bloqueamos el proceso
	 * 
	 * @param p proceso a bloquear
	 */
	public void bloquear(Proceso p) {
		p.setEstado(Estado.BLOQUEADO);
		cola.add(p);
		System.out.println(Color.VERDE + "E/S: bloqueado " + p.getId() + Color.RESET);
	}

	/**
	 * desbloqueamos un proceso aleatorio de la cola
	 * 
	 * @return proceso a desbloquear
	 */
	public Proceso desbloquearAleatorio() {
		if (cola.isEmpty())
			return null;

		int tam = cola.size();
		int indice = r.nextInt(tam);
		int i = 0;
		Proceso elegido = null;
		for (Proceso p : cola) {
			if (i == indice) {
				elegido = p;
			}
			i++;
		}

		if (elegido != null) {
			cola.remove(elegido);
			System.out.println(Color.VERDE + "E/S: desbloqueado " + elegido.getId() + Color.RESET);
			return elegido;
		}
		return null;
	}

	/**
	 * comprobar si la cola esta vacia
	 * 
	 * @return true si esta vacia, false si no esta vacia
	 */
	public boolean estaVacia() {
		return cola.isEmpty();
	}
}
