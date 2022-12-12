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
        buffet = new Buffet()

        precioPancho = new Dinero(10)
        precioDona = new Dinero(5)

        pancho = new Producto("pancho", 10, new Dinero(10))
        dona = new Producto("dona", 5, new Dinero(5))
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

        then: "el buffet puede ver las compras de los pedidos y tanto sus ids como horarios son los correctos"
            List<Compra> historialDeCompras = buffet.getComprasRegistradas()
            historialDeCompras.size() == 2
            historialDeCompras.first().id_compra == 0
            historialDeCompras.last().id_compra == 1

            Pedido pedidoLautaro = historialDeCompras.first().pedido()
            pedidoLautaro.cantidadDeProductos() == 3
            pedidoLautaro.precio() == new Dinero(16)
            
            Pedido pedidoAriana = historialDeCompras.last().pedido()
            pedidoAriana.cantidadDeProductos() == 1
            pedidoAriana.precio() == new Dinero(5)

            historialDeCompras.first().fecha().getHour() == fechaDeCompraLautaro.getHour()
            historialDeCompras.first().fecha().getMinute() == fechaDeCompraLautaro.getMinute()

            historialDeCompras.last().fecha().getHour() == fechaDeCompraAriana.getHour()
            historialDeCompras.last().fecha().getMinute() == fechaDeCompraAriana.getMinute()
    }
}
