package buffet

class Producto {

    static constraints = {
    }

    static embedded = ['precio']

    String nombre
    Dinero precio
    int cantidad

    Producto(String nombre, int cantidad, Dinero precio) {
        if (cantidad <= 0) throw new IllegalStateException() 
        if (precio == 0) throw new IllegalStateException() 

        this.nombre = nombre
        this.precio = precio
        this.cantidad = cantidad
    }

    Dinero precioSegunCantidad() {
        this.precio * this.cantidad
    }

    Producto retirar(int cantidad) {
        if (this.cantidad < cantidad) throw new IllegalStateException()
        this.cantidad -= cantidad
        new Producto(this.nombre, cantidad, this.precio)
    }
}
