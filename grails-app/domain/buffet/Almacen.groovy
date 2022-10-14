package buffet

class Almacen {

    static constraints = {
    }
    
    Map<String, int> inventario = [:]

    void agregar(Producto producto, int nuevoStock) {
        this.inventario[producto.getNombreDelProducto()] = nuevoStock
    }

    boolean retirarProducto(String nombreProducto, int cantidadARetirar, Pedido pedido) {
        int stock = this.inventario[nombreProducto]

        if (stock >= cantidadARetirar) {
            for (int i = 0; i < cantidadARetirar; i++) {
                pedido.agregar(new Producto(nombreProducto))
            }
            inventario[nombreProducto] -= cantidadARetirar
            return true
        }
        return false
    }

}
