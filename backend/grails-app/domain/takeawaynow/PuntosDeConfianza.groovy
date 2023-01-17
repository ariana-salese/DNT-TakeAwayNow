package takeawaynow

class PuntosDeConfianza implements Comparable<PuntosDeConfianza> {

    static constraints = {
    }

    int cantidad

    /**
     * 
     * Crea puntos de confianza con cantidad indicada
     * 
     */
    PuntosDeConfianza(int cantidad) {
        if (cantidad < 0) throw new IllegalStateException("La cantidad de puntos de confianza no puede ser negativa")
        this.cantidad = cantidad
    }

    /**
     * 
     * Compara la cantidad actual con la cantidad de puntos de confianza recibidos
     * 
     */
    int compareTo(PuntosDeConfianza otro) {
        this.cantidad <=> otro.cantidad
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza plus(int cantidad) {
        if (cantidad < 0) throw new IllegalStateException("La cantidad a agregar no puede ser negativa")
        new PuntosDeConfianza(this.cantidad + cantidad)
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza minus(int cantidad) {
        if (this.cantidad - cantidad < 0) return new PuntosDeConfianza(0)
        new PuntosDeConfianza(this.cantidad - cantidad)
    }

    /**
     * 
     * Suma la cantidad actual con la cantidad de los puntos de confianza recibidos y retorna
     * nuevos puntos de confianza con el valor resultante
     * 
     */
    PuntosDeConfianza plus(PuntosDeConfianza otro) {
        new PuntosDeConfianza(this.cantidad + otro.cantidad)
    }

    /**
     * 
     * Resta la cantidad actual con la cantidad de puntos de confianza recibidos y retorna
     * nuevos puntos de confianza con el valor resultante. Si el valor resultante de la resta
     * es menor a cero la nueva cantidad sera cero.
     * 
     */
    PuntosDeConfianza minus(PuntosDeConfianza otro) {
        if (this.cantidad - otro.cantidad < 0) return new PuntosDeConfianza(0)
        new PuntosDeConfianza(this.cantidad - otro.cantidad)
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza multiply(int cantidad) {
        new PuntosDeConfianza(this.cantidad * cantidad)
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, int multiplicador = 1) {
        this - this.calcularPuntosPorCompra(compra) * multiplicador
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza agregarPuntosPorCompra(Compra compra, int multiplicador = 1) {
        this + this.calcularPuntosPorCompra(compra) * multiplicador
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza calcularPuntosPorCompra(Compra compra) {
        new PuntosDeConfianza(compra.cantidadDeProductosPorDinero())
    }

}
