package takeawaynow

/**
 * 
 * TODO
 * 
 */
abstract class PlanDeCliente {

    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente) {}

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {}

    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {}

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {}

    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {}
}
