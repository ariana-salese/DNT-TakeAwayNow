package takeawaynow

/**
 * 
 * TODO
 * 
 */
class PlanRegular extends PlanDeCliente {

    /**
     * 
     * TODO
     * 
    */
    @Override
    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {
        saldoCliente - compra.pedido.precio()
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.pedido.puntos()
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        new PlanPrime(saldoCliente)
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosPorCompra(compra)
    }

    /**
     * 
     * TODO
     * 
    */
    @Override
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra)
    }
}
