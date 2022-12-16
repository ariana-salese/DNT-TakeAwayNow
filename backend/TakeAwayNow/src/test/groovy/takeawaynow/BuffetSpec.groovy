package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDateTime

class BuffetSpec extends Specification implements DomainUnitTest<Buffet> {

    Buffet buffet

    def precioPancho
    def precioDona

    Producto pancho
    Producto dona

    def setup() {
        buffet = new Buffet("Buffet Paseo Colón")

        precioPancho = new Dinero(10)
        precioDona = new Dinero(5)

        pancho = new Producto("pancho", 10, precioPancho)
        dona = new Producto("dona", 5, precioDona)
    }

    def cleanup() {
    }

    void "un buffet puede agregar una serie de productos"() {
        when: "el buffet registra los productos"
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(dona)

        then: "hay stock del productos registrados y sus precios son los correctos"
            def inventario = buffet.almacen.inventario
            //chequea precio
            inventario["dona"].precio == precioDona
            inventario["pancho"].precio == precioPancho
            //chequea stock
            buffet.hayStock("dona") == true
            buffet.hayStock("pancho") == true
            inventario.size() == 2
    }

    void "un buffet puede actualizar el precio de un producto ya registrado"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)
        
        when: "el buffet actualiza el precio"
            def nuevoPrecioPancho = new Dinero(15)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio fue actualizado"
            buffet.almacen.inventario["pancho"].precio == nuevoPrecioPancho
    }

    void "un buffet no puede actualizar el precio de un producto como igual a cero"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)
     
        when: "el buffet actualiza el precio de un producto ya registrado con precio cero"
            def nuevoPrecioPancho = new Dinero(0)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio no fue actualizado y se lanzo error"
            Exception e = thrown()
            e.message == "No se puede actualizar el precio a un precio menor o igual a cero."
            buffet.almacen.inventario["pancho"].precio == precioPancho
    }

    void "un buffet no puede actualizar el precio de un producto que no tiene registrado"() {    
        when: "el buffet actualiza el precio que no tiene"
            buffet.actualizarPrecio("pancho", new Dinero(15))

        then: "se lanza error"
            Exception e = thrown()
            e.message == "El producto al cual se busca actualizar el precio no se encuentra registrado."
    }

    void "un buffet puede ingresar nuevo stock de un producto ya registrado"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)
        
        when: "el buffet actualiza el stock"
            buffet.ingresarStock("pancho", 5)

        then: "el stock se incremento"
            buffet.almacen.inventario["pancho"].cantidad == 15
    }

    void "un buffet no puede ingresar nuevo stock de un producto que no tiene registrado"() {
        when: "el buffet ingresa stock de un producto que no tiene registrado"
            buffet.ingresarStock("chipa", 5)

        then: "se lanzo error"
            Exception e = thrown()
            e.message == "El producto al cual se busca actualizar el stock no se encuentra registrado."
    }

    void "un buffet no puede ingresar stock negativo de un producto ya registrado"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)
        
        when: "el buffet ingresa stock negativo"
            buffet.ingresarStock("pancho", -1)

        then: "se lanzo un error"
            Exception e = thrown()
            e.message == "No se puede ingresar un stock menor o igual a cero."
    }

    void "un buffet no puede ingresar stock igual a cero de un producto ya registrado"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)

        when: "el buffet ingresa stock cero del producto"
            buffet.ingresarStock("pancho", 0)

        then: "se lanza error"
            Exception e = thrown()
            e.message == "No se puede ingresar un stock menor o igual a cero."
    }

    void "un buffet puede agregar un producto a un pedido si hay stock"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)

        when: "el buffet agrega el producto al pedido"
            def pedido = new Pedido()
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "el pedido tiene el producto y el stock se actualizo"
            pedido.cantidadDeProductos() == 2
            buffet.almacen.inventario["pancho"].cantidad == 8
    }

    void "un buffet no puede agregar un producto a un pedido si no hay suficiente stock"() {
        given: "un buffet que registra un producto"
            buffet.registrarProducto(pancho)

        when: "el buffet intenta agregar un producto sin stock al pedido"
            def pedido = new Pedido()
            buffet.agregarAlPedido("pancho", 11, pedido)

        then: "se lanza error"
            Exception e = thrown()
    }

    void "un buffet no puede agregar un producto a un pedido si no lo tiene registrado"() {
        when: "el buffet intenta agregar un producto que no tiene registrado al pedido"
            def pedido = new Pedido()
            buffet.agregarAlPedido("pancho", 1, pedido)

        then: "se lanza error"
            Exception e = thrown()
            e.message == "El producto que se busca retirar no se encuentra registrado."
    }

    void "un buffet puede ver las compras que sus clientes realizaron de forma correcta"() {
        given: "varios clientes y varios productos registrados"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            def ariana = new Cliente()
            ariana.cargarSaldo(new Dinero(16))
            ariana.ingresarBuffet(buffet)

            def pancho = new Producto("pancho", 10, new Dinero(5))
            def coca = new Producto("coca", 10, new Dinero(6))
            buffet.registrarProducto(coca)
            buffet.registrarProducto(pancho)

        when: "los clientes confirman la compra de sus pedidos"
            lautaro.agregarAlPedido("pancho", 2)
            lautaro.agregarAlPedido("coca", 1)
            lautaro.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraLautaro = LocalDateTime.now()
            
            ariana.agregarAlPedido("pancho", 1)
            ariana.confirmarCompraDelPedido()
            LocalDateTime fechaDeCompraAriana = LocalDateTime.now()

        then: "el buffet puede ver las compras realizadas, tanto sus ids como horarios y estados son los correctos"
            Map<Integer, Compra> historialDeCompras = buffet.getComprasRegistradas()
            historialDeCompras.size() == 2
            historialDeCompras[0].getId() == 0
            historialDeCompras[1].getId() == 1

            Pedido pedidoLautaro = historialDeCompras[0].getPedido()
            pedidoLautaro.cantidadDeProductos() == 3
            pedidoLautaro.precio() == new Dinero(16)
            
            Pedido pedidoAriana = historialDeCompras[1].getPedido()
            pedidoAriana.cantidadDeProductos() == 1
            pedidoAriana.precio() == new Dinero(5)

            historialDeCompras[0].getFecha().getHour() == fechaDeCompraLautaro.getHour()
            historialDeCompras[0].getFecha().getMinute() == fechaDeCompraLautaro.getMinute()
            historialDeCompras[0].getEstado() == Compra.EstadoDeCompra.AGUARDANDO_PREPARACION

            historialDeCompras[1].getFecha().getHour() == fechaDeCompraAriana.getHour()
            historialDeCompras[1].getFecha().getMinute() == fechaDeCompraAriana.getMinute()
            historialDeCompras[1].getEstado() == Compra.EstadoDeCompra.AGUARDANDO_PREPARACION
    }

    void "un negocio no puede preparar una compra no registrada'"() {
        when: "un negocio el cual registra no registra compras intenta preparar una"
            buffet.prepararCompra(0)

        then: "se lanza el error correspondiente"
            Exception e = thrown()
            e.message == "No se encuentra registrada una compra con el ID indicado."
    }

    void "un negocio puede marcar una compra recién registrada con estado 'EN_PREPARACION' ya que su estado actual es 'AGUARDANDO_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio intenta cambiar de un estado de 'AGUARDANDO_PREPARACION' a 'EN_PREPARACION'"
            buffet.prepararCompra(0)

        then: "la compra ahora tiene como estado 'EN_PREPARACION'"
            buffet.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.EN_PREPARACION
    }
    
    void "un negocio solo puede marcar una compra recién registrada con estado 'EN_PREPARACION' si y solo si la misma tiene como estado 'AGUARDANDO_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como LISTA_PARA_RETIRAR sin que la misma tenga como estado 'EN_PREPARACION'"
            buffet.marcarCompraListaParaRetirar(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION."
    }

    void "un negocio puede marcar una compra con estado 'LISTA_PARA_RETIRAR' ya que su estado actual es 'EN_PREPARACION'"() {
        given: "un negocio el cual registra una compra y posteriormente comienza a prepararla"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio prepara la compra y luego quiere marcarla con estado de 'LISTA_PARA_RETIRAR'"
            buffet.prepararCompra(0)
            buffet.marcarCompraListaParaRetirar(0)

        then: "la compra ahora tiene como estado 'LISTA_PARA_RETIRAR'"
            buffet.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.LISTA_PARA_RETIRAR
    }

    void "un negocio solo puede marcar una compra con estado 'LISTA_PARA_RETIRAR' si y solo si la misma tiene como estado 'EN_PREPARACION'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como LISTA_PARA_RETIRAR sin que la misma tenga como estado 'EN_PREPARACION'"
            buffet.marcarCompraListaParaRetirar(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como LISTA_PARA_RETIRAR ya que la misma no se encontraba EN_PREPARACION."
    }

    void "un negocio puede marcar una compra con estado 'RETIRADA' ya que su estado actual es 'LISTA_PARA_RETIRAR'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio prepara la compra, la marca lista para retirar y luego quiere marcarla con estado 'RETIRADA'"
            buffet.prepararCompra(0)
            buffet.marcarCompraListaParaRetirar(0)
            buffet.marcarCompraRetirada(0)

        then: "la compra ahora tiene como estado 'RETIRADA'"
            buffet.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.RETIRADA
    }

    void "un negocio solo puede marcar una compra con estado 'RETIRADA' si y solo si la misma tiene como estado 'LISTA_PARA_RETIRAR'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como RETIRADA sin que la misma tenga como estado 'LISTA_PARA_RETIRAR'"
            buffet.marcarCompraRetirada(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como RETIRADA ya que la misma no se encontraba LISTA_PARA_RETIRAR."
    }

    void "un negocio puede marcar una compra con estado 'DEVUELTA' ya que su estado actual es 'RETIRADA'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio prepara la compra, la marca lista para retirar, la marcar como retirada y posteriormente se ejecuta una devolución'"
            buffet.prepararCompra(0)
            buffet.marcarCompraListaParaRetirar(0)
            buffet.marcarCompraRetirada(0)
            buffet.marcarCompraDevuelta(0)

        then: "la compra ahora tiene como estado 'DEVUELTA'"
            buffet.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.DEVUELTA
    }

    void "un negocio solo puede marcar una compra con estado 'DEVUELTA' si y solo si la misma tiene como estado 'RETIRADA'"() {
        given: "un negocio el cual registra una compra"
            def lautaro = new Cliente()
            lautaro.cargarSaldo(new Dinero(16))
            lautaro.ingresarBuffet(buffet)

            buffet.registrarProducto(pancho)
            lautaro.agregarAlPedido("pancho", 1)
            lautaro.confirmarCompraDelPedido()

        when: "el negocio intenta marcar la compra como DEVUELTA sin que la misma tenga como estado 'RETIRADA'"
            buffet.marcarCompraDevuelta(0)

        then: "se lanzan el respectivo error"
            Exception e = thrown()
            e.message == "No se puede marcar dicha compra como DEVUELTA ya que la misma no se encontraba RETIRADA."
    }

    // void "un negocio puede marcar una compra con estado 'CANCELADA' ya que su estado actual es 'RETIRADA'"() {
    //     given: "un negocio el cual registra una compra"
    //         def lautaro = new Cliente()
    //         lautaro.cargarSaldo(new Dinero(16))
    //         lautaro.ingresarBuffet(buffet)

    //         buffet.registrarProducto(pancho)
    //         lautaro.agregarAlPedido("pancho", 1)
    //         lautaro.confirmarCompraDelPedido()

    //     when: "el negocio prepara la compra, la marca lista para retirar y luego quiere marcarla con estado 'RETIRADA'"
    //         buffet.prepararCompra(0)
    //         buffet.marcarCompraListaParaRetirar(0)
    //         buffet.marcarCompraRetirada(0)

    //     then: "la compra ahora tiene como estado 'RETIRADA'"
    //         buffet.getComprasRegistradas()[0].getEstado() == Compra.EstadoDeCompra.RETIRADA
    // }
}
