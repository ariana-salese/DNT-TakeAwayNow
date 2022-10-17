package buffet

class Cliente {

    static constraints = {
    }

    static embedded = ['saldo', 'pedido', 'buffet']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Buffet buffet
    List<Compra> historialDeCompras = []

    void cargarSaldo(Dinero monto) {
        this.setSaldo(this.saldo + monto)
    }

    void ingresarBuffet(Buffet buffet) {
        this.setBuffet(buffet)
    }

    void agregarAlPedido(String nombreProducto, int cantidad) {
        buffet.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

    int cantidadDeProductos() {
        this.pedido.cantidadDeProductos()
    }

    void comprar() {
        Dinero precioPedido = pedido.precio()

        if (this.saldo < precioPedido) throw new IllegalStateException("no te alcanza papá") 
        this.setSaldo(this.saldo - precioPedido)
        this.pedido = new Pedido()
    }
}
