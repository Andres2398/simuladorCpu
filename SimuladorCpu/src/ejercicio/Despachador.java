package ejercicio;

public class Despachador {
	private ColaBloqueados colaBloq;
	private ColaTerminados colaTerm;
	private Planificador planificador;
	
	
	public Despachador(ColaBloqueados colaBloq, ColaTerminados colaTerm, Planificador planificador) {

		this.colaBloq=colaBloq;
		this.colaTerm=colaTerm;
		this.planificador=planificador;
		
	}

	public void agregarAcolaBloqueados(Proceso p) {
		colaBloq.bloquear(p);
	}

	public void agregarAcolaTerminados(Proceso p) {
		colaTerm.agregar(p);
	}

	public void agregarAcolaListos(Proceso P) {
		planificador.agregar(P);
	}
}
