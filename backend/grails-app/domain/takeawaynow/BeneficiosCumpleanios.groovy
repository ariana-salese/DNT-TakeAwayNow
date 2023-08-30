package takeawaynow

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 
 * TODO
 * 
 */
class BeneficiosCumpleanios {

    static constraints = {
    }

    LocalDate diaDeCumpleanios
    LocalDateTime diaDeCanjeDePuntos = null
    LocalDateTime diaDeCompraConBeneficios = null
    PuntosDeConfianza PUNTOS_POR_CUMPLEANIOS = new PuntosDeConfianza(100)

    /**
     * 
     * TODO
     * 
     */
    BeneficiosCumpleanios(LocalDate diaDeCumpleanios) {
        this.diaDeCumpleanios = diaDeCumpleanios
    }

    /**
     * 
     * TODO
     * 
     */
    PuntosDeConfianza obtenerPuntosDeConfianzaActualizadosSegunFecha(LocalDateTime dia, PuntosDeConfianza puntosDeConfianza) {
        if (!this.esDiaDeCumpleanios(dia.toLocalDate())) return puntosDeConfianza
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
        if (!this.esDiaDeCumpleanios(dia.toLocalDate())) return pedido.precio()
        if (this.diaDeCompraConBeneficios != null && this.diaDeCompraConBeneficios.year == dia.year) return puntosDeConfianza

        this.diaDeCompraConBeneficios = dia
        pedido.precioDescontandoProductoDeMenorValor()
    }

    /**
     * 
     * TODO
     * 
     */
    boolean esDiaDeCumpleanios(LocalDate dia) {
        dia.dayOfMonth == this.diaDeCumpleanios.dayOfMonth && dia.month == this.diaDeCumpleanios.month 
        //dia.date == this.diaDeCumpleanios.date && dia.month == this.diaDeCumpleanios.month
    }

}
