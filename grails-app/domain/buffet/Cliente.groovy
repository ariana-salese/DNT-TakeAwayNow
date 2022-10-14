package buffet

class Cliente {

    static constraints = {
    }

    static embedded = ['saldo', 'pedido', 'buffet']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Buffet buffet 

    void cargarSaldo(Dinero monto) {
        this.setSaldo(this.saldo + monto)
    }

    void ingresarBuffet(Buffet buffet) {
        this.buffet = buffet
    }

    void agregarAlPedido(String nombreProducto, int cantidad) {
        buffet.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

//     void comprar(Producto producto, int cantidad = 1) {
//         if (this.saldo < producto.precio) throw new IllegalStateException("no te alcanza papá")
//         this.setSaldo(this.saldo - producto.precio)
//         //producto.reducirStock(cantidad, cliente)
//         producto.reducirStock(cantidad, this)

//         // Compra c = new Compra(producto, this)
//         // this.compras << c
//         // c
//         //TODO retornar compra
//     }
}
