package buffet

class Cliente {

    static constraints = {
    }

    static embedded = ['saldo', 'pedido', 'buffetIngresado']

    Dinero saldo = new Dinero(0)
    Pedido pedido = new Pedido()
    Buffet buffetIngresado
    List<Compra> historialDeCompras = []

    void ingresarBuffet(Buffet buffet) {
        this.setBuffetIngresado(buffet)
    }

    void cargarSaldo(Dinero monto) {
        this.setSaldo(this.saldo + monto)
    }

    void agregarAlPedido(String nombreProducto, int cantidad) {
        buffetIngresado.agregarAlPedido(nombreProducto, cantidad, this.pedido)
    }

    int cantidadDeProductos() {
        this.pedido.cantidadDeProductos()
    }

    Dinero valorDelPedido(){
        this.pedido.precio()
    }

    void comprar() {
        Dinero precioPedido = pedido.precio()
        if (this.saldo < precioPedido) throw new IllegalStateException() 

        this.setSaldo(this.saldo - precioPedido)
        Compra compra = buffetIngresado.registrarCompra(this.pedido)
        this.historialDeCompras.add(compra)
        this.pedido = new Pedido()
    }
}
