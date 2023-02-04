package takeawaynow

/**
 * 
 * TODO
 * 
 */
class BeneficiosCumpleanios {

    static constraints = {
    }

    Date diaDeCumpleanios
    Date diaDeCanjeDePuntos = null
    Date diaDeCompraConBeneficios = null
    PuntosDeConfianza PUNTOS_POR_CUMPLEANIOS = new PuntosDeConfianza(100)

    /**
     * 
     * TODO
     * 
     */
    BeneficiosCumpleanios(Date diaDeCumpleanios) {
        this.diaDeCumpleanios = diaDeCumpleanios
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosSegunFecha(Date dia, PuntosDeConfianza puntosDeConfianza) {
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
    Dinero obtenerPrecioDePedidoSegunFecha(Date dia, Pedido pedido) {
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
    boolean esDiaDeCumpleanios(Date dia) {
        dia.date == this.diaDeCumpleanios.date && dia.month == this.diaDeCumpleanios.month
    }

}
