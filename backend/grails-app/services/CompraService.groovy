package buffet

import grails.gorm.transactions.Transactional

@Transactional
class CompraService {

    def comprar(Long productoId, Long clienteId) {
        // no puede ir logica de negocio acá.
        // aca exclusivamente deberia ir el inicio de la historia y la delegacion en las entidades
        def producto = Producto.get(productoId)
        def cliente = Cliente.get(clienteId)
        // if (!cliente.puedeComprar(producto)) ... // esto está MAL
        cliente.comprar(producto)
    }
}
