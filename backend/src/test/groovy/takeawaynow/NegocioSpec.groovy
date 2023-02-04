package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDateTime

class NegocioSpec extends Specification implements DomainUnitTest<Negocio> {

    Negocio negocio
    Cliente messi
    Cliente dibu
    Dinero precioPancho
    Dinero precioDona
    Dinero precioCoca
    Producto pancho
    Producto dona
    Producto coca
    Horario horario_apertura, horario_cierre
    Date dia

    def setup() {
        horario_apertura = new Horario(9,0)
        horario_cierre = new Horario(18,0)
        Date diaDeCumpleanios = new Date(2001, 5, 27, 0, 0, 0)
        negocio = new Negocio("Buffet Paseo Colón", horario_apertura, horario_cierre)
        messi = new Cliente("Messi", "campeondelmundo", diaDeCumpleanios)
        dibu = new Cliente("Dibu", "if***youtwice", diaDeCumpleanios)
        precioPancho = new Dinero(10)
        precioDona = new Dinero(5)
        precioCoca = new Dinero(6)

        pancho = new Producto("pancho", 10, precioPancho)
        dona = new Producto("dona", 5, precioDona)
        coca = new Producto("coca", 10, precioCoca)
        // year: 2022, 
        // month: 5, 
        // dayOfMonth: 27, 
        // hourOfDay: 12,
        // minute: 0,
        // second: 0
        dia = new Date(2022, 5, 27, 12, 0, 0)
        messi.ingresarNegocio(negocio, dia)
        messi.cargarSaldo(new Dinero(16))
        dibu.ingresarNegocio(negocio, dia)
        dibu.cargarSaldo(new Dinero(16))
    }

    def cleanup() {
    }

    void "crear un negocio con el horario de apertura mayor al de cierre lanza error"() {
        when: "un negocio tiene horario de apertura mayor al de cierre"
            Negocio negocio_invalido = new Negocio("Buffet Paseo Colón", horario_cierre, horario_apertura)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un negocio puede agregar una serie de productos"() {
        when: "el negocio registra los productos"
            negocio.registrarProducto(pancho)
            negocio.registrarProducto(dona)

        then: "hay stock del productos registrados y sus precios son los correctos"
            def inventario = negocio.almacen.inventario
            //chequea precio
            inventario["dona"].precio == precioDona
            inventario["pancho"].precio == precioPancho
            //chequea stock
            negocio.hayStock("dona") == true
            negocio.hayStock("pancho") == true
            inventario.size() == 2
    }

    void "un negocio puede actualizar el precio de un producto ya registrado"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)
        
        when: "el negocio actualiza el precio"
            def nuevoPrecioPancho = new Dinero(15)
            negocio.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio fue actualizado"
            negocio.almacen.inventario["pancho"].precio == nuevoPrecioPancho
    }

    void "un negocio no puede actualizar el precio de un producto como igual a cero"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)
     
        when: "el negocio actualiza el precio de un producto ya registrado con precio cero"
            def nuevoPrecioPancho = new Dinero(0)
            negocio.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio no fue actualizado y se lanzo error"
            Exception e = thrown()
            e.message == "No se puede actualizar el precio a un precio menor o igual a cero."
            negocio.almacen.inventario["pancho"].precio == precioPancho
    }

    void "un negocio no puede actualizar el precio de un producto que no tiene registrado"() {    
        when: "el negocio actualiza el precio que no tiene"
            negocio.actualizarPrecio("pancho", new Dinero(15))

        then: "se lanza error"
            Exception e = thrown()
            e.message == "El producto al cual se busca actualizar el precio no se encuentra registrado."
    }

    void "un negocio puede ingresar nuevo stock de un producto ya registrado"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)
        
        when: "el negocio actualiza el stock"
            negocio.ingresarStock("pancho", 5)

        then: "el stock se incremento"
            negocio.almacen.inventario["pancho"].cantidad == 15
    }

    void "un negocio no puede ingresar nuevo stock de un producto que no tiene registrado"() {
        when: "el negocio ingresa stock de un producto que no tiene registrado"
            negocio.ingresarStock("chipa", 5)

        then: "se lanzo error"
            Exception e = thrown()
            e.message == "El producto al cual se busca actualizar el stock no se encuentra registrado."
    }

    void "un negocio no puede ingresar stock negativo de un producto ya registrado"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)
        
        when: "el negocio ingresa stock negativo"
            negocio.ingresarStock("pancho", -1)

        then: "se lanzo un error"
            Exception e = thrown()
            e.message == "No se puede ingresar un stock menor o igual a cero."
    }

    void "un negocio no puede ingresar stock igual a cero de un producto ya registrado"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)

        when: "el negocio ingresa stock cero del producto"
            negocio.ingresarStock("pancho", 0)

        then: "se lanza error"
            Exception e = thrown()
            e.message == "No se puede ingresar un stock menor o igual a cero."
    }

    void "un negocio puede agregar un producto a un pedido si hay stock"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)

        when: "el negocio agrega el producto al pedido"
            def pedido = new Pedido()
            negocio.agregarAlPedido("pancho", 2, pedido)

        then: "el pedido tiene el producto y el stock se actualizo"
            pedido.cantidadDeProductos() == 2
            negocio.almacen.inventario["pancho"].cantidad == 8
    }

    void "un negocio no puede agregar un producto a un pedido si no hay suficiente stock"() {
        given: "un negocio que registra un producto"
            negocio.registrarProducto(pancho)

        when: "el negocio intenta agregar un producto sin stock al pedido"
            def pedido = new Pedido()
            negocio.agregarAlPedido("pancho", 11, pedido)

        then: "se lanza error"
            Exception e = thrown()
    }

    void "un negocio no puede agregar un producto a un pedido si no lo tiene registrado"() {
        when: "el negocio intenta agregar un producto que no tiene registrado al pedido"
            def pedido = new Pedido()
            negocio.agregarAlPedido("pancho", 1, pedido)

        then: "se lanza error"
            Exception e = thrown()
            e.message == "El producto que se busca retirar no se encuentra registrado."
    }

    void "un negocio puede ver las compras que sus clientes realizaron de forma correcta"() {
        given: "varios clientes y varios productos registrados"

            //def pancho = new Producto("pancho", 10, new Dinero(5))
            //def coca = new Producto("coca", 10, new Dinero(6))
            negocio.registrarProducto(coca)
            negocio.registrarProducto(pancho)

        when: "los clientes confirman la compra de sus pedidos"
            messi.agregarAlPedido("pancho", 1)
            messi.agregarAlPedido("coca", 1)
            messi.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraMessi = LocalDateTime.now()
            
            dibu.agregarAlPedido("pancho", 1)
            dibu.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraDibu = LocalDateTime.now()

        then: "el negocio puede ver las compras realizadas, tanto sus ids como horarios y estados son los correctos"
            Map<Integer, Compra> historialDeCompras = negocio.getComprasRegistradas()
            historialDeCompras.size() == 2
            historialDeCompras[0].getId() == 0
            historialDeCompras[1].getId() == 1

            Pedido pedidoMessi = historialDeCompras[0].getPedido()
            pedidoMessi.cantidadDeProductos() == 2
            pedidoMessi.precio() == new Dinero(16)
            
            Pedido pedidoDibu = historialDeCompras[1].getPedido()
            pedidoDibu.cantidadDeProductos() == 1
            pedidoDibu.precio() == new Dinero(10)

            historialDeCompras[0].getFecha().getHour() == fechaDeCompraMessi.getHour()
            historialDeCompras[0].getFecha().getMinute() == fechaDeCompraMessi.getMinute()
            historialDeCompras[0].getEstado() == Compra.EstadoDeCompra.AGUARDANDO_PREPARACION

            historialDeCompras[1].getFecha().getHour() == fechaDeCompraDibu.getHour()
            historialDeCompras[1].getFecha().getMinute() == fechaDeCompraDibu.getMinute()
            historialDeCompras[1].getEstado() == Compra.EstadoDeCompra.AGUARDANDO_PREPARACION
    }

    void "un negocio no puede preparar una compra no registrada'"() {
        when: "un negocio el cual registra no registra compras intenta preparar una"
            negocio.prepararCompra(0)

        then: "se lanza el error correspondiente"
            Exception e = thrown()
            e.message == "No se encuentra registrada una compra con el ID indicado."
    }

    void "un negocio puede marcar una compra recién registrada con estado 'EN_PREPARACION' ya que su estado actual es 'AGUARDANDO_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio intenta cambiar de un estado de 'AGUARDANDO_PREPARACION' a 'EN_PREPARACION'"
            negocio.prepararCompra(0)

        then: "la compra ahora tiene como estado 'EN_PREPARACION'"
            negocio.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.EN_PREPARACION
    }
    
    void "un negocio solo puede marcar una compra recién registrada con estado 'EN_PREPARACION' si y solo si la misma tiene como estado 'AGUARDANDO_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como LISTA_PARA_RETIRAR sin que la misma tenga como estado 'EN_PREPARACION'"
            negocio.marcarCompraListaParaRetirar(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION."
    }

    void "un negocio puede marcar una compra con estado 'LISTA_PARA_RETIRAR' ya que su estado actual es 'EN_PREPARACION'"() {
        given: "un negocio el cual registra una compra y posteriormente comienza a prepararla"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio prepara la compra y luego quiere marcarla con estado de 'LISTA_PARA_RETIRAR'"
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)

        then: "la compra ahora tiene como estado 'LISTA_PARA_RETIRAR'"
            negocio.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.LISTA_PARA_RETIRAR
    }

    void "un negocio solo puede marcar una compra con estado 'LISTA_PARA_RETIRAR' si y solo si la misma tiene como estado 'EN_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como LISTA_PARA_RETIRAR sin que la misma tenga como estado 'EN_PREPARACION'"
            negocio.marcarCompraListaParaRetirar(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION."
    }

    void "un negocio puede marcar una compra con estado 'RETIRADA' ya que su estado actual es 'LISTA_PARA_RETIRAR'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio prepara la compra, la marca lista para retirar y luego quiere marcarla con estado 'RETIRADA'"
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            negocio.marcarCompraRetirada(0)

        then: "la compra ahora tiene como estado 'RETIRADA'"
            negocio.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.RETIRADA
    }

    void "un negocio solo puede marcar una compra con estado 'RETIRADA' si y solo si la misma tiene como estado 'LISTA_PARA_RETIRAR'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como RETIRADA sin que la misma tenga como estado 'LISTA_PARA_RETIRAR'"
            negocio.marcarCompraRetirada(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como RETIRADA ya que la misma no se encontraba LISTA_PARA_RETIRAR."
    }

    void "un negocio puede marcar una compra con estado 'DEVUELTA' ya que su estado actual es 'RETIRADA'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio prepara la compra, la marca lista para retirar, la marcar como retirada y posteriormente se ejecuta una devolución'"
            negocio.prepararCompra(0)
            negocio.marcarCompraListaParaRetirar(0)
            negocio.marcarCompraRetirada(0)
            negocio.marcarCompraDevuelta(0)

        then: "la compra ahora tiene como estado 'DEVUELTA'"
            negocio.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.DEVUELTA
    }

    void "un negocio solo puede marcar una compra con estado 'DEVUELTA' si y solo si la misma tiene como estado 'RETIRADA'"() {
        given: "un negocio el cual registra una compra"
            negocio.registrarProducto(pancho)
            messi.agregarAlPedido("pancho", 1)
            messi.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como DEVUELTA sin que la misma tenga como estado 'RETIRADA'"
            negocio.marcarCompraDevuelta(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como DEVUELTA ya que la misma no se encontraba RETIRADA."
    }
}
