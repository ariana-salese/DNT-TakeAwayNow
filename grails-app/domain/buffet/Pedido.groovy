package buffet

class Pedido {

    static constraints = {
    }

    List<Producto> productosDeseados = []

    void agregar(Producto producto) {
        this.productosDeseados.add(producto)
    }

    int cantidadDeProductos() {
        this.productosDeseados.size()
    }
}
