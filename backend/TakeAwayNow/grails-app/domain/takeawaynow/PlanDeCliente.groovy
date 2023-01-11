package takeawaynow

/**
 * 
 * TODO
 * 
 */
interface PlanDeCliente {

    Dinero obtenerSaldoActualizadoPorCompra(Compra compra, Dinero saldoCliente)

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente)

    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente)

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente)
}