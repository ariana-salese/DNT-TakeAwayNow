package takeawaynow

/**
 * 
 * TODO
 * 
 */
class Horario implements Comparable<Horario> {

    static constraints = {
    }

    int hora
    int minutos

    /**
     * 
     * TODO
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
     * TODO
     * 
     */
    int compareTo(Horario otro) {
        (this.hora <=> otro.hora) <=> (this.minutos <=> otro.minutos)
    }
}
