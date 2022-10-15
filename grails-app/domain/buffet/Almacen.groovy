package buffet

class Almacen {

    static constraints = {
    }
    
    def inventario = [:]

    void agregar(Producto producto, int nuevoStock) {
        this.inventario[producto.getNombreDelProducto()] = nuevoStock
    }

    boolean hayStock(String nombreDelProducto) {
        return (this.inventario[nombreDelProducto] > 0)
    }

    boolean retirarProducto(String nombreProducto, int cantidadARetirar, Pedido pedido, Dinero precio) {
        Integer stock = this.inventario[nombreProducto]

        if (stock >= cantidadARetirar) {
            for (int i = 0; i < cantidadARetirar; i++) {
                pedido.agregar(new Producto(nombreProducto), precio, cantidadARetirar)
            }
            inventario[nombreProducto] -= cantidadARetirar
            return true
        }
        return false
    }

}
