package takeawaynow

/**
 * 
 * TODO
 * 
 */
class PlanRegular implements PlanDeCliente {

    /**
     * 
     * TODO
     * 
    */
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - compra.pedido.precio()
    }

    /**
     * 
     * TODO
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.pedido.puntos()
    }

    /**
     * 
     * TODO
     * 
    */
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        new PlanPrime(saldoCliente)
    }

    /**
     * 
     * TODO
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosPorCompra(compra)
    }

    /**
     * 
     * TODO
     * 
    */
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra)
    }

    /**
     * 
     * TODO
     * 
     */
    int diasRestantesDePlanPrime() {
        0
    }
}
