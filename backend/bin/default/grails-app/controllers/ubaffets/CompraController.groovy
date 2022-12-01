package buffet

class CompraController {

    def compraService

    def index() {
        [
            clientes: Cliente.list(),
            productos: Producto.list(),
        ]
    }

    def crear() {
        (1..10).each { it ->
            new Producto(new Dinero(it), it).save()
        }

        (1..10).each { it ->
            def cliente = new Cliente()
            cliente.cargarSaldo(new Dinero(1_000))
            cliente.save()
        }
        render "listo"
    }

    def comprar(Long productoId, Long clienteId) {
        compraService.comprar(productoId, clienteId)
        render "hola"
    }
}
