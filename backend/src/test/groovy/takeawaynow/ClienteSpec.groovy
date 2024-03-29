package takeawaynow


import spock.lang.Specification
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class ClienteSpec extends Specification{

    Negocio negocio
    Cliente cliente
    LocalTime horario_apertura, horario_cierre
    LocalDateTime dia
    LocalDate diaDeCumpleanios

    def setup() {
        horario_apertura = LocalTime.of(9,0)
        horario_cierre = LocalTime.of(18,0)
        negocio = new Negocio("Buffet Paseo Colón", horario_apertura, horario_cierre)
        // year: 2001, 
        // month: 5, 
        // dayOfMonth: 27
        diaDeCumpleanios = LocalDate.of(2022, Month.MAY, 27) //TODO probar sin 0 0 0 
        cliente = new Cliente("Messi", "campeondelmundo", diaDeCumpleanios)
        // year: 2022, 
        // month: 5, 
        // dayOfMonth: 27, 
        // hourOfDay: 12,
        // minute: 0,
        // second: 0
        dia = LocalDateTime.of(2022, Month.MAY, 26, 12, 0, 0)
        cliente.ingresarNegocio(negocio, dia)
        cliente.darPuntosDeConfianza(new PuntosDeConfianza(30))
    }

    def cleanup() {
    }

    // INGRESAR A NEGOCIO

    void "un cliente no puede ingresar a un negocio que este cerrado"() {
        given: "un horario pasado el horario de cierre"
            // year: 2022, 
            // month: 5, 
            // dayOfMonth: 27, 
            // hourOfDay: 19,
            // minute: 0,
            // second: 0
            LocalDateTime dia_con_horario_pasado_el_cierre = LocalDateTime.of(2022, Month.MAY, 27, 19, 0, 0)

        when: "un cliente ingresa al negocio"
            cliente.ingresarNegocio(negocio, dia_con_horario_pasado_el_cierre)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    // PEDIDO Y STOCK DE PRODUCTOS 

    void "un cliente puede agregar un producto con stock al pedido"() {
        given: "un cliente que quiere un producto con stock"
            def pancho = new Producto("pancho", 1, new Dinero(10))
            negocio.registrarProducto(pancho)

        when: "el cliente agrega el producto al pedido"
            cliente.agregarAlPedido("pancho", 1)

        then: "el pedido tiene un producto"
            cliente.cantidadDeProductosEnElPedido() == 1
    }

    void "un cliente puede agregar y quitar productos con stock al pedido"() {
        given: "un cliente que quiere un producto con stock"
            def pancho = new Producto("pancho", 5, new Dinero(10 as BigDecimal))
            negocio.registrarProducto(pancho)

        when: "el cliente agrega dos productos al pedido"
            cliente.agregarAlPedido("pancho", 5)
            cliente.quitarDelPedido("pancho", 3)

        then: "el pedido tiene la cantidad de productos adecuados, y el inventario del negocio vuelve a actualizarse"
            cliente.valorDelPedidoEnDinero() == new Dinero(20 as BigDecimal)
            cliente.cantidadDeProductosEnElPedido() == 2
            negocio.hayStock("pancho")
            negocio.almacen.obtenerCantidadDe("pancho") == 3
    }

    void "un cliente no puede quitar productos de su pedido los cuales no haya agregado previamente"() {
        when: "un cliente intenta quitar un producto del pedido, pero el mismo está vacío"
            cliente.quitarDelPedido("pancho", 3)

        then:
            Exception e = thrown()
            e.message == "No se pueden quitar 3 panchos del pedido ya que no hay productos en el mismo."
            cliente.valorDelPedidoEnDinero() == new Dinero(0)
            cliente.cantidadDeProductosEnElPedido() == 0
    }

    void "un cliente no puede agregar un producto sin stock sufuciente al pedido"() {
        when: "el cliente intenta agrega el producto sin stock suficiente al pedido"
            def pancho = new Producto("pancho", 1, new Dinero(10))
            negocio.registrarProducto(pancho)
            cliente.agregarAlPedido("pancho", 2)

        then: "el pedido no tiene productos"
            Exception e = thrown()
            e.message == "La cantidad que se desea retirar es mayor al stock actual del producto"
            cliente.pedido.cantidadDeProductos() == 0
    }

    // CONFIMAR COMPRA

    void "un cliente que no agregó productos a su pedido no puede confirmar la compra del mismo"() {
        when: "el cliente confirma la compra de su pedido"
            cliente.confirmarCompraDelPedido()

        then: "se lanza un error"
            Exception e = thrown()
            e.message == "No se puede confirmar la compra del pedido ya que el mismo no tiene productos agregados."
    }

    void "dado un cliente que tiene 10 pesos de saldo y compró un pancho de 5 pesos le terminan quedando de saldo 5 pesos"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)

        when: "el cliente compra el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "el saldo del cliente es de 5 pesos"
            cliente.saldo == new Dinero(5)
    }

    void "dado un cliente que tiene 10 pesos de saldo y compró un pancho de 15 pesos, no puede confirmar correctamente la compra de su pedido porque no tiene saldo suficiente"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(15))
            negocio.registrarProducto(pancho)

        when: "el cliente intenta comprar el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "se lanza una excepcion ya que no le alcanza el saldo"
            Exception e = thrown()
            e.message == "No se puede confirmar la compra del pedido ya que el saldo es insuficiente."
    }

    void "dado un cliente que realizó una compra, luego puede verla"() {
        given: "dado un cliente que tiene 10 pesos de saldo y un producto pancho de 5 pesos"
            cliente.cargarSaldo(new Dinero(10))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)

        when: "el cliente compra el pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "el cliente puede ver su compra"
            Set<Compra> comprasRealizadasDelCliente = cliente.comprasRealizadas
            Pedido compraDeUnPancho = comprasRealizadasDelCliente[0].getPedido()

            compraDeUnPancho.cantidadDeProductos() == 1
            compraDeUnPancho.precio() == new Dinero(5)
    }

    void "dado un cliente que realizó varias compras, luego puede verlas todas"() {
        given: "dado un cliente que tiene 10 pesos de saldo, un producto pancho de 5 pesos y un producto coca de 6 pesos"
            cliente.cargarSaldo(new Dinero(11))
            def pancho = new Producto("pancho", 10, new Dinero(5))
            def coca = new Producto("coca", 10, new Dinero(6))
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(coca)

        when: "el cliente compra el pancho y luego la coca"
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraDelPancho = LocalDateTime.now()

            cliente.agregarAlPedido("coca", 1)
            cliente.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraDeLaCoca = LocalDateTime.now()

        then: "el cliente puede ver sus compras y los ids son los correctos"
            Set<Compra> comprasRealizadasDelCliente = cliente.comprasRealizadas
            
            Pedido compraDeUnPancho = comprasRealizadasDelCliente[0].getPedido()
            compraDeUnPancho.cantidadDeProductos() == 1
            compraDeUnPancho.precio() == new Dinero(5)
            comprasRealizadasDelCliente[0].getInstanteDeCompra().getHour() == fechaDeCompraDelPancho.getHour()
            comprasRealizadasDelCliente[0].getInstanteDeCompra().getMinute() == fechaDeCompraDelPancho.getMinute()
            comprasRealizadasDelCliente[0].getId() == 0
            
            Pedido compraDeUnaCoca = comprasRealizadasDelCliente[1].getPedido()
            compraDeUnaCoca.cantidadDeProductos() == 1
            compraDeUnaCoca.precio() == new Dinero(6)
            comprasRealizadasDelCliente[1].getInstanteDeCompra().getHour() == fechaDeCompraDeLaCoca.getHour()
            comprasRealizadasDelCliente[1].getInstanteDeCompra().getMinute() == fechaDeCompraDeLaCoca.getMinute()
            comprasRealizadasDelCliente[1].getId() == 1
    }

    // RETIRAR COMPRA

    void "dado un cliente que no realizó compras, el mismo no puede retirar ninguna compra"() {
        when: "el cliente quiere retirar una compra"
            cliente.retirarCompra(0)

        then: "se lanza el error correspondiente"
            Exception e = thrown()
            e.message == "No se encuentra una compra realizada con el ID indicado."
    }

    void "dado un cliente que realizó una compra y la misma aún no está lista para retirar, cuando el cliente intenta retirarla obtiene un error"() {
        given: "un cliente que confirma la compra de su pedido"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            cliente.confirmarCompraDelPedido()

            negocio.prepararCompra(0)
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

        when: "intenta retirar dicha compra"
            cliente.retirarCompra(0)

        then: "obtiene el error correspondiente y sus puntos de confianza siguen igual que antes"
            Exception e = thrown()
            e.message == "La compra aún no está lista para retirar, su estado actual es EN_PREPARACION."

            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente
    }

    void "dado un cliente que realizó una compra y la misma está lista para retirar, tras retirarla sus puntos de confianza se actualizan y el id de la compra corresponde a una compra retirada"() {
        given: "un cliente que confirma la compra de su pedido"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()
            cliente.cargarSaldo(new Dinero(5))
            cliente.agregarAlPedido("pancho", 1)
            cliente.confirmarCompraDelPedido()

        when: "la misma está lista para retirar y el mismo la quiere retirar"
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        then: "la compra se retiró con éxito, los puntos de confianza se actualizan y el id de la compra corresponde a una compra retirada"
            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente + 1
            cliente.getComprasRetiradas().contains(0) == true
    }

    void "dado un cliente que realizó una compra y la retiró, obtiene un error tras intentar retirarla nuevamente"() {
        given: "un cliente que confirma la compra de su pedido y luego lo retira"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            cliente.confirmarCompraDelPedido()

            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

        when: "intenta retirar dicha compra nuevamente"
            cliente.retirarCompra(0)

        then: "obtiene el error correspondiente y sus puntos de confianza siguen igual que antes (luego de retirar su pedido la primera vez)"
            Exception e = thrown()
            e.message == "Dicha compra ya fue retirada previamente."

            cliente.getPuntosDeConfianza() == puntosObtenidosPreviamente
    }

    // CANCELAR COMPRA

        void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra aguardando preparación entonces pierde un punto de confianza, obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido (compra 2 panchos, el stock de los panchos post compra es 8)"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(10))
            cliente.agregarAlPedido("pancho", 2)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()

        when: "lo cancela inmediatamente (tiene estado de aguardando preparación)"
            cliente.cancelarCompra(0)

        then: "pierde un punto de confianza, obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.getPuntosDeConfianza().cantidad == puntosObtenidosPreviamente.cantidad - 1
            cliente.getSaldo().getMonto() == saldoPreCompra.getMonto()
            negocio.almacen.obtenerCantidadDe("pancho") == 10
    }

    void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra en preparación entonces pierde varios punto de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido y el negocio lo comienza a preparar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)

        when: "cancela la compra cuando el pedido esta en preparación"
            cliente.cancelarCompra(0)

        then: "pierde puntos de confianza (cantidad total de productos en la compra), no obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.puntosDeConfianza.cantidad == puntosObtenidosPreviamente.cantidad - 4
            cliente.saldo.monto == saldoPreCompra.monto - cliente.comprasRealizadas[0].pedido.precio().monto
            negocio.almacen.obtenerCantidadDe("pancho") == 10
    }

    void "dado un cliente que realizó una compra y la cancela cuando la misma se encuentra lista para retirar entonces pierde todos sus puntos de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido, el negocio lo comienza a preparar y luego la compra está lista para retirar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.getSaldo()
            PuntosDeConfianza puntosObtenidosPreviamente = cliente.getPuntosDeConfianza()

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)

        when: "cancela la compra cuando el pedido esta listo para retirar"
            cliente.cancelarCompra(0)

        then: "pierde todos sus puntos de confianza, no obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10)"
            cliente.getPuntosDeConfianza().cantidad == 0
            cliente.getSaldo().getMonto() == saldoPreCompra.getMonto() - cliente.getComprasRealizadas()[0].getPedido().precio().getMonto()
            negocio.almacen.obtenerCantidadDe("pancho") == 10
    }

    // DEVOLUCION DE COMPRA

    void "dado un cliente que realizó una compra pero hubo algún error en la entrega del pedido por parte del negocio y pasaron menos de 5 minutos desde que se retiró, el cliente solicita la devolución de la compra y obtiene nuevamente su dinero y el stock de dichos productos se actualiza"() {
        given: "un cliente que confirma la compra de su pedido, el negocio lo comienza a preparar y luego la compra está lista para retirar"
            def pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)
            
            cliente.cargarSaldo(new Dinero(20))
            cliente.agregarAlPedido("pancho", 4)
            
            Dinero saldoPreCompra = cliente.saldo
            int puntosObtenidosPreviamente = cliente.puntosDeConfianza.cantidad

            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        when: "el cliente solicita la devolución de la compra"
            cliente.solicitarDevolucionDeLaCompra(0)

        then: "obtiene nuevamente su dinero y el stock de dichos productos se actualiza (el stock de los panchos post cancelación es 10) y sus puntos de confianza no se modifican"
            cliente.puntosDeConfianza.cantidad == puntosObtenidosPreviamente
            cliente.saldo == new Dinero(20)
            negocio.almacen.obtenerCantidadDe("pancho") == 10
    }

    // COMPRAS CON CANJE PUNTOS DE CONFIANZA

    void "dado un cliente que tiene 30 puntos de confianza y confirma compra con canje de pancho por 10 le quedan 20"() {
        given: "dado un cliente que tiene 30 puntos de confianza y un pancho canjeable por 10"
            Producto pancho = new Producto("pancho", 10, new Dinero(5), new PuntosDeConfianza(10))
            negocio.registrarProducto(pancho)

        when: "el cliente confirma compra con el pancho por puntos"
            cliente.agregarAlPedidoPorPuntoDeConfianza("pancho", 1)
            cliente.confirmarCompraDelPedido()

        then: "los puntos de confianza resultantes son correctos"
            cliente.puntosDeConfianza == new PuntosDeConfianza(20)
    }

    void "dado un cliente que tiene 30 puntos de confianza y retira compra con canje de pancho por 10 le quedan 20"() {
        given: "dado un cliente que tiene 30 puntos de confianza y un pancho canjeable por 10"
            Producto pancho = new Producto("pancho", 10, new Dinero(5), new PuntosDeConfianza(10))
            negocio.registrarProducto(pancho)

        when: "el cliente compra y retira el pancho por puntos"
            cliente.agregarAlPedidoPorPuntoDeConfianza("pancho", 1)
            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        then: "los puntos de confianza resultantes son correctos"
            cliente.puntosDeConfianza == new PuntosDeConfianza(20)
    }

    void "dado un cliente que tiene 30 puntos de confianza y retira compra con canje de pancho por 10 y afajor por 50 pesos entonces el saldo y puntos finales son correctos"() {
        given: "dado un cliente que tiene 30 puntos de confianza, un pancho canjeable por 10 y un alfajor que valo 50 pesos"
            Producto pancho = new Producto("pancho", 10, new Dinero(5), new PuntosDeConfianza(10))
            Producto alfajor = new Producto("alfajor", 10, new Dinero(50))
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(alfajor)

        when: "el cliente compra el pancho por puntos"
            cliente.cargarSaldo(new Dinero(100))
            cliente.agregarAlPedidoPorPuntoDeConfianza("pancho", 1)
            cliente.agregarAlPedido("alfajor", 1)
            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        then: "los puntos de confianza y sald0 resultantes son correctos"
            cliente.puntosDeConfianza == new PuntosDeConfianza(21)
            cliente.saldo == new Dinero(50)
    }

    void "dado un cliente que agrega a su pedido a cambio de puntos un producto no canjeable por puntos entonces se lanza error"() {
        given: "dado un cliente que tiene 30 puntos de confianza y un pancho no canjeable por puntos"
            Producto pancho = new Producto("pancho", 10, new Dinero(5))
            negocio.registrarProducto(pancho)

        when: "el agrega por puntos el pancho no canjeable por puntos"
            cliente.agregarAlPedidoPorPuntoDeConfianza("pancho", 1)

        then: "se lanza error"
            IllegalStateException exception = thrown()
            cliente.cantidadDeProductosEnElPedido() == 0
    }

    void "dado un cliente que tiene 30 puntos de confianza y retira compra con canje de pancho por 10 y afajor por 50 pesos entonces el saldo y puntos finales son correctos"() {
        given: "dado un cliente que tiene 30 puntos de confianza, un pancho canjeable por 10 y un alfajor que valo 50 pesos"
            Producto pancho = new Producto("pancho", 10, new Dinero(5), new PuntosDeConfianza(10))
            Producto alfajor = new Producto("alfajor", 10, new Dinero(50))
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(alfajor)

        when: "el cliente compra el pancho por puntos"
            cliente.cargarSaldo(new Dinero(100))
            cliente.agregarAlPedidoPorPuntoDeConfianza("pancho", 1)
            cliente.agregarAlPedido("alfajor", 1)
            cliente.confirmarCompraDelPedido()
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            cliente.retirarCompra(0)

        then: "los puntos de confianza y sald0 resultantes son correctos"
            cliente.puntosDeConfianza == new PuntosDeConfianza(21)
            cliente.saldo == new Dinero(50)
    }
    
    // BENEFICIOS DE CUMPLEANIOS

    void "dado un cliente que cumpleanios tiene 100 puntos de confianza extra"() {
        when: "el cliente consulta puntos en dia que no es su cumpleanios"
            PuntosDeConfianza puntosDeConfianzaPrevios = cliente.puntosDeConfianza(dia)

        then: "el dia de su cumpleanios tiene 100 puntos mas"
            cliente.puntosDeConfianza(diaDeCumpleanios.atStartOfDay()) == puntosDeConfianzaPrevios + 100
    }

    void "dado un cliente que realizó una compra en el dia de su cumpleanios entonces el producto de menor precio se desconto"() {
        given: "dado un cliente que tiene 50 pesos de saldo, un pancho de 20 pesos y una agua de 15"
            cliente.cargarSaldo(new Dinero(50))
            def pancho = new Producto("pancho", 10, new Dinero(20))
            def agua = new Producto("agua", 10, new Dinero(15))
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(agua)

        when: "el cliente agrega al pedido un agua y un pancho"
            cliente.agregarAlPedido("pancho", 1)
            cliente.agregarAlPedido("agua", 1)
            cliente.confirmarCompraDelPedido(diaDeCumpleanios.atStartOfDay())

        then: "el precio de la compra es 20 entonces el saldo actual es 30"
            Set<Compra> comprasRealizadasDelCliente = cliente.comprasRealizadas
            Pedido pedido = comprasRealizadasDelCliente[0].pedido

            pedido.cantidadDeProductos() == 2
            cliente.saldo == new Dinero(30)
    }

    // PLAN PRIME

    void "dado un cliente con plan regular se subscribe a plan prime con fondos suficientes y tiene plan prime"() {
        given: "dado un cliente que fondos suficientes para abonar plan prime"
            cliente.cargarSaldo(new Dinero(500))

        when: "el cliente se subscribe a plan prime"
            cliente.subscribirseAPlanPrime()

        then: "el cliente tiene plan prime"
            cliente.tienePlanPrime() == true
    }

    void "dado un cliente con plan regular se subscribe a plan prime con fondos insuficientes se lanza error"() {
        when: "el cliente se subscribe a plan prime con fondos insuficientes"
            cliente.subscribirseAPlanPrime()

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "dado un cliente con plan regular y la cantidad de dias restantes de prime son 0"() {
        when: "un cliente que fondos suficientes para abonar plan prime"
            cliente.cargarSaldo(new Dinero(500))

        then: "el cliente no tiene plan prime"
            cliente.tienePlanPrime() == false
    }

    // TODO tests el resto de cosas prime, Lauti lo ibas a hacer vos no?

}
