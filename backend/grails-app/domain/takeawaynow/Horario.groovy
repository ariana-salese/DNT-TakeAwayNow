package takeawaynow

/**
 * 
 * Horario representa un momento del dia por horas y minutos.
 * 
 */
class Horario implements Comparable<Horario> {

    static constraints = {
    }

    int hora
    int minutos

    /**
     * 
     * Retorna un horario. Se lanza error si:
     * - La hora indicada es menor a 0.
     * - La hora indicada es mayor o igual a 24.
     * - Los minutos indicados son menores a 0.
     * - Los minutos indicados son mayores a 59.
     * 
     */
    Horario(int hora, int minutos) {
        if (hora < 0) throw new IllegalStateException("La hora debe ser mayor a 0.")
        if (hora > 24) throw new IllegalStateException("La hora debe ser menor a 24.")
        if (minutos < 0) throw new IllegalStateException("Los minutos deben ser mayores a 0.")
        if (minutos > 59) throw new IllegalStateException("Los minutos deben ser menores a 60.")

        this.hora = hora
        this.minutos = minutos
    }

    /**
     * 
     * Compara dos horarios segun sus horas y minutos.
     * 
     */
    int compareTo(Horario otro) {
        (this.hora <=> otro.hora) <=> (this.minutos <=> otro.minutos)
    }
}
