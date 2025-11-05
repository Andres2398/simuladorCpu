package ejercicio;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ColaBloqueados{
	 private final Queue<Proceso> cola = new ConcurrentLinkedQueue<>();
	 private Random r = new Random();
	 
	    public void bloquear(Proceso p) {
	        p.setEstado(Estado.BLOQUEADO);
	        cola.add(p);
	        System.out.printf("E/S: bloqueado %s%n", p.getId());
	    }

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
	            System.out.printf("E/S: desbloqueado %s%n", elegido.getId());
	            return elegido;
	        }
	        return null;
	    }

	    public boolean estaVacia() {
	        return cola.isEmpty();
	    }
}
