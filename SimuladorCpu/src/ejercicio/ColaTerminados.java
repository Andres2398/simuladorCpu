package ejercicio;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase para introducir los procesos en una cola de procesos terminados
 */
public class ColaTerminados {

	private final Queue<Proceso> terminados = new LinkedList<>();

	/**
	 * Metodo para agregar un proceso a la cola
	 * 
	 * @param p proceso terminado a encolar
	 */
	public void agregar(Proceso p) {
		p.setEstado(Estado.TERMINADO);
		// Hay una posibilidad de que se introduzcan a la vez el mismo proceso, se
		// intentara evitar con esto
		if (!terminados.contains(p))
			terminados.add(p);
	}

	public boolean estaVacia() {
		return terminados.isEmpty();
	}

	public int cantidad() {
		return terminados.size();
	}

	public Queue<Proceso> getTerminados() {
		return terminados;
	}

}
