package buffet

class Producto {

    static constraints = {
    }

    static embedded = ['precio']

    String nombreDelProducto

    Producto(String nombreDelProducto) {
        this.nombreDelProducto = nombreDelProducto
    }

    boolean puedeComprar(Cliente cliente) {
        this.stock > 0
    }

    void reducirStock(int cantidad, Cliente cliente) {
        // throw new exception si el stock es 0
        // if (!puedeComprar) throw new .....
        this.setStock(this.stock - cantidad)
        // Movimiento m = new Movimiento(VENTA, cantidad, cliente)
        // this.movimientos << m
        // m    
        // TODO Retornar movimieto?
    }
}
