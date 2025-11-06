package ejercicio;

public class Despachador {
	/**
	 * Clase dedicada a meter los procesos en las distintas colas 
	 */
	
	private ColaBloqueados colaBloq;
	private ColaTerminados colaTerm;
	private Planificador planificador;
	
	
	public Despachador(ColaBloqueados colaBloq, ColaTerminados colaTerm, Planificador planificador) {

		this.colaBloq=colaBloq;
		this.colaTerm=colaTerm;
		this.planificador=planificador;
		
	}
	
	/**
	 * Agraga el proceso a la lista de bloqueados
	 * @param p proceso a encolar
	 */
	public void agregarAcolaBloqueados(Proceso p) {
		colaBloq.bloquear(p);
	}
	/**
	 * Agraga el proceso a la lista de terminados
	 * @param p proceso a encolar
	 */
	public void agregarAcolaTerminados(Proceso p) {
		colaTerm.agregar(p);
	}
	/**
	 * Agraga el proceso a la lista de listos
	 * @param p proceso a encolar
	 */
	public void agregarAcolaListos(Proceso P) {
		planificador.agregar(P);
	}
}
