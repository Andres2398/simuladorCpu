package ejercicio;

import java.util.Random;
/**
 * Clase proceso que simula un proceso
 */
public class Proceso {

	private final int id;
	private Estado estado;
	private int quantum;// Quantum aleatorio 2000–9000
	
	
	private Random r = new Random();

	public Proceso(int id) {
		
		this.id = id;
		this.estado = Estado.LISTO;
		this.quantum = r.nextInt(2000, 9000);

	}
	/**
	 * Consume el quantum
	 * @param cantidad a restar del quantum total
	 */
	public void consumirQuantum(int cantidad) {
		quantum -= cantidad;
		if (quantum < 0)
			quantum = 0;
	}

	public int getId() {
		return id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getQuantum() {
		return quantum;
	}
	/**
	 * establecemos el estado a terminado
	 */
	public void finProceso() {
		if (getEstado() == Estado.TERMINADO) {
			
			
		}

	}

	
	
	@Override
	public String toString() {
		
		return "Proceso: "+Integer.toString(id) + ", ";
	}

}