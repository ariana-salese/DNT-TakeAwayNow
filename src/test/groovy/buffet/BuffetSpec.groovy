package buffet

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDateTime

class BuffetSpec extends Specification implements DomainUnitTest<Buffet> {

    def setup() {
    }

    def cleanup() {
    }

    void "un buffet puede agregar una serie de productos"() {
        given: "un buffet que quiere agregar productos"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)
            def precioDona = new Dinero(5)
            def dona = new Producto("dona", 5, precioDona)

        when: "el buffet registra los productos"
            buffet.registrarProducto(pancho)
            buffet.registrarProducto(dona)

        then: "hay stock del productos registrados y sus precios don correctos"
            def inventario = buffet.almacen.inventario
            //chequea precio
            inventario["dona"].precio == precioDona
            inventario["pancho"].precio == precioPancho
            //chequea stock
            buffet.hayStock("dona") == true
            buffet.hayStock("pancho") == true
            inventario.size() == 2
    }

    void "un buffet puede actualizar el precio de un producto"() {
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)

        
        when: "el buffet actualiza el precio"
            buffet.registrarProducto(pancho)
            def nuevoPrecioPancho = new Dinero(15)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio fue actualizado"
            buffet.almacen.inventario["pancho"].precio == nuevoPrecioPancho
    }

    void "un buffet no puede actualizar el precio de un producto como igual a cero"() {
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)

        
        when: "el buffet actualiza el precio como cero"
            buffet.registrarProducto(pancho)
            def nuevoPrecioPancho = new Dinero(0)
            buffet.actualizarPrecio("pancho", nuevoPrecioPancho)

        then: "el precio no fue actualizado y se lanzo error"
            IllegalStateException exception = thrown()
            buffet.almacen.inventario["pancho"].precio == precioPancho
    }

    void "un buffet no puede actualizar el precio de un producto que no tiene"() {
        given: "un buffet que quiere actualizar el precio de un producto"
            def buffet = new Buffet()

        
        when: "el buffet actualiza el precio que no tiene"
            buffet.actualizarPrecio("pancho", new Dinero(15))

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet puede ingresar nuevo stock de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        
        when: "el buffet actualiza el stock"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", 5)

        then: "el stock se incremento"
            buffet.almacen.inventario["pancho"].cantidad == 15
    }

    void "un buffet no puede ingresar nuevo stock de un producto que no tiene"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
        
        when: "el buffet ingresa stock de un producto que no tiene"
            buffet.ingresarStock("pancho", 5)

        then: "se lanzo error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede ingresar stock negativo de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        
        when: "el buffet ingresa stock negativo"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", -1)

        then: "se lanzo un error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede ingresar stock igual a cero de un producto"() {
        given: "un buffet que quiere actualizar el stock de un producto"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)

        when: "el buffet ingresa stock cero del producto"
            buffet.registrarProducto(pancho)
            buffet.ingresarStock("pancho", 0)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet puede agregar un producto a un pedido si hay stock"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 10, precioPancho)
            def pedido = new Pedido()

        when: "el buffet agrega el producto al pedido"
            buffet.registrarProducto(pancho)
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "el pedido tiene el producto y el stock se actualizo"
            pedido.cantidadDeProductos() == 2
            buffet.almacen.inventario["pancho"].cantidad == 8
    }

    void "un buffet no puede agregar un producto a un pedido si no hay suficiente stock"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def precioPancho = new Dinero(10)
            def pancho = new Producto("pancho", 1, precioPancho)
            def pedido = new Pedido()

        when: "el buffet intenta agregar un producto sin stock al pedido"
            buffet.registrarProducto(pancho)
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet no puede agregar un producto a un pedido si no lo tiene"() {
        given: "un buffet que quiere agregar un producto a un pedido"
            def buffet = new Buffet()
            def pedido = new Pedido()

        when: "el buffet intenta agregar un producto que no tiene al pedido"
            buffet.agregarAlPedido("pancho", 2, pedido)

        then: "se lanza error"
            IllegalStateException exception = thrown()
    }

    void "un buffet puede ver las compras realizadas correctamente"() {
        given: "varios clientes y varios productos"
            def buffet = new Buffet()
            def cliente_1 = new Cliente()
            def cliente_2 = new Cliente()
            cliente_1.cargarSaldo(new Dinero(16))
            cliente_2.cargarSaldo(new Dinero(16))
            cliente_1.ingresarBuffet(buffet)
            cliente_2.ingresarBuffet(buffet)
            def pancho = new Producto("pancho", 10, new Dinero(5))
            def coca = new Producto("coca", 10, new Dinero(6))
            buffet.registrarProducto(coca)
            buffet.registrarProducto(pancho)

        when: "los clientes realizan compras"
            cliente_1.agregarAlPedido("pancho", 2)
            cliente_1.agregarAlPedido("coca", 1)
            cliente_2.agregarAlPedido("pancho", 1)
            cliente_1.comprar()
            LocalDateTime fechaDeCompraCliente1 = LocalDateTime.now()
            cliente_2.comprar()
            LocalDateTime fechaDeCompraCliente2 = LocalDateTime.now()

        then: "el buffet puede verlas y sus ids son los correctos"
            List<Compra> historial = buffet.getComprasRegistradas()
            Pedido compraCliente1 = historial.first().pedido()
            Pedido compraCliente2 = historial.last().pedido()
            historial.size() == 2
            historial.first().id_compra == 0
            historial.last().id_compra == 1

            compraCliente1.cantidadDeProductos() == 3
            compraCliente1.precio() == new Dinero(16)
            compraCliente2.cantidadDeProductos() == 1
            compraCliente2.precio() == new Dinero(5)


            historial.first().fecha().getHour() == fechaDeCompraCliente1.getHour()
            historial.first().fecha().getMinute() == fechaDeCompraCliente1.getMinute()
            
            historial.last().fecha().getHour() == fechaDeCompraCliente2.getHour()
            historial.last().fecha().getMinute() == fechaDeCompraCliente2.getMinute()
    }
}
