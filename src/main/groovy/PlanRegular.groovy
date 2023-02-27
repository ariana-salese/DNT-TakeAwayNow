package takeawaynow

/**
 * 
 * Representa el plan basico para el cliente. No cuenta con beneficios.
 * 
 */
class PlanRegular implements PlanDeCliente {

    /**
     * 
     * Retorna slado actual actualizado segun precio dado.
     * 
    */
    Dinero obtenerSaldoActualizadoPorCompra(Dinero precio, Dinero saldoCliente) {
        saldoCliente - precio
    }

    /**
     * 
     * Retorna puntos de confianza actualizados segun la compra confirmada. 
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraConfirmada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente - compra.pedido.puntos()
    }

    /**
     * 
     * Retorna un plan prime. Si no es posible subscribirse al plan por falta de saldo entonces
     * se lanza un error.
     * 
    */
    PlanPrime subscribirseAPlanPrime(Dinero saldoCliente) {
        new PlanPrime(saldoCliente)
    }

    /**
     * 
     * Retorna puntos de confianza actualizados segun la compra realizada.
     * 
    */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosPorCompraRetirada(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.agregarPuntosPorCompra(compra)
    }

    /**
     * 
     * Retorna los puntos de confianza recibidos disminuidos por los puntos dados por la compra.
     * 
    */
    PuntosDeConfianza eliminarPuntosPorCompra(Compra compra, PuntosDeConfianza puntosDeConfianzaCliente) {
        puntosDeConfianzaCliente.eliminarPuntosPorCompra(compra)
    }


    /**
     * 
     * Retorna si el plan prime esta vigente. Siempre en false.
     * 
    */
    boolean planPrimeVigente() {
        false
    }
}
