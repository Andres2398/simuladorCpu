package ejercicio;

import java.util.Random;

public class Proceso {

	private final int id;
	private Estado estado;
	private int quantum;// Quantum aleatorio 2000–9000
	private long inicio;
	private long fin;
	private long tiempo;
	private Random r = new Random();

	public Proceso(int id) {
		inicio = System.currentTimeMillis();
		this.id = id;
		this.estado = Estado.LISTO;
		this.quantum = r.nextInt(2000, 9000);

	}

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

	public void finProceso() {
		if (getEstado() == Estado.TERMINADO) {
			fin = System.currentTimeMillis();
			tiempoTotal();
		}

	}

	private void tiempoTotal() {
		tiempo = fin - inicio;

	}

}