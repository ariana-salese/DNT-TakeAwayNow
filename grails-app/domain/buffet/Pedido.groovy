package buffet

class Pedido {

    static constraints = {
    }

    List<Producto> productosDeseados = []
    List<Dinero> precioProducto = []
    List<Integer> cantidadProducto = []

    void agregar(Producto producto, Dinero precio, int cantidad) {
        this.productosDeseados.add(producto)
        this.precioProducto.add(precio)
        this.cantidadProducto.add(cantidad)
    }

    int cantidadDeProductos() {
        this.productosDeseados.size()
    }

    Dinero precioTotal() {
        Dinero precioTotal = new Dinero(0)
        for (int i = 0; i < this.productosDeseados.size(); i++) {
            precioTotal += this.precioProducto[i] * new Dinero(this.cantidadProducto[i]) //TODO No debería tener que crear un Dinero para multiplicarlo
        }
        precioTotal
    }
}
