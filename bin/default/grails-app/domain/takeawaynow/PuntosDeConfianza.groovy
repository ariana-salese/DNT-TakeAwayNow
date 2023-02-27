package takeawaynow

class PuntosDeConfianza implements Comparable<PuntosDeConfianza> {

    static constraints = {
        cliente nullable: true
    }

    static belongsTo = [cliente: Cliente]

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
     * Suma puntos de confianza con un entero. Retorna puntos de confianza
     * con la cantidad restultante de sumar la actual con la indicada. Si la cantidad 
     * indicada es negativa se lanza un error.
     * 
     */
    PuntosDeConfianza plus(int cantidad) {
        if (cantidad < 0) throw new IllegalStateException("La cantidad a agregar no puede ser negativa")
        new PuntosDeConfianza(this.cantidad + cantidad)
    }

    /**
     * 
     * Resta puntos de confianza con un entero. Retorna puntos de confianza
     * con la cantidad restultante de restar la actual con la indicada por parametro.
     * Si resta da menor a cero la cantidad restultante sera cero. Si la cantidad
     * indicada por parametro es negativa se lanza un error.
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
     * Multiplica la cantidad actual por la cantidad indicada por parametro. Retorna
     * puntos de confianza con la cantidad resultante. Si esta es negativa se lanza un
     * error.
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
