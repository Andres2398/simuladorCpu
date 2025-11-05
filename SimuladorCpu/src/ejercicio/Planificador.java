package ejercicio;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Planificador {
    private final Queue<Proceso> colaListos = new ConcurrentLinkedQueue<>();

    public void agregar(Proceso p) {
        p.setEstado(Estado.LISTO);
        colaListos.add(p);
        System.out.printf("Planificador: añadido %s%n", p.getId());
    }

    public Proceso siguiente() {
        return colaListos.poll();
    }

    public boolean estaVacia() {
        return colaListos.isEmpty();
    }
}