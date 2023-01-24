package takeawaynow

/**
 * 
 * TODO
 * 
 */
interface PlanDeCliente {

    Dinero obtenerSaldoActualizadoPorCompra(Dinero precio, Dinero saldoCliente)

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) 

    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente)

    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) 

    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente)

    int diasRestantesDePlanPrime()
}
