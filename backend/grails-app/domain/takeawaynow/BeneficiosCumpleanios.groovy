package takeawaynow

import java.time.LocalDateTime

/**
 * 
 * TODO
 * 
 */
class BeneficiosCumpleanios {

    static constraints = {
    }

    LocalDateTime diaDeCumpleanios
    LocalDateTime diaDeCanjeDePuntos = null
    LocalDateTime diaDeCompraConBeneficios = null
    PuntosDeConfianza PUNTOS_POR_CUMPLEANIOS = new PuntosDeConfianza(100)

    /**
     * 
     * TODO
     * 
     */
    BeneficiosCumpleanios(LocalDateTime diaDeCumpleanios) {
        this.diaDeCumpleanios = diaDeCumpleanios
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosSegunFecha(LocalDateTime dia, PuntosDeConfianza puntosDeConfianza) {
        if (!this.esDiaDeCumpleanios(dia)) return puntosDeConfianza
        if (diaDeCanjeDePuntos != null && diaDeCanjeDePuntos.year == dia.year) return puntosDeConfianza

        this.diaDeCanjeDePuntos = dia
        puntosDeConfianza + PUNTOS_POR_CUMPLEANIOS
    }

    /**
     * 
     * TODO
     * 
     */
    Dinero obtenerPrecioDePedidoSegunFecha(LocalDateTime dia, Pedido pedido) {
        if (!this.esDiaDeCumpleanios(dia)) return pedido.precio()
        if (this.diaDeCompraConBeneficios != null && this.diaDeCompraConBeneficios.year == dia.year) return puntosDeConfianza

        this.diaDeCompraConBeneficios = dia
        pedido.precioDescontandoProductoDeMenorValor()
    }

    /**
     * 
     * TODO
     * 
     */
    boolean esDiaDeCumpleanios(LocalDateTime dia) {
        dia.date == this.diaDeCumpleanios.date && dia.month == this.diaDeCumpleanios.month
    }

}
