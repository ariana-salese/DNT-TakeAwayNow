package takeawaynow

class Producto {

    static constraints = {
    }

    static embedded = ['precio']

    String nombre
    Dinero precio
    int cantidad

    Producto(String nombre, int cantidad, Dinero precio) {
        if (cantidad <= 0) throw new IllegalStateException("No se puede inicializar un producto con cantidad igual a cero.") 
        if ((int)precio.monto <= 0) throw new IllegalStateException("No se puede inicializar un producto con precio menor o igual a cero.") 

        this.nombre = nombre
        this.precio = precio
        this.cantidad = cantidad
    }

    Dinero precioSegunCantidad() {
        this.precio * this.cantidad
    }

    Producto retirar(int cantidad) {
        if (this.cantidad < cantidad) throw new IllegalStateException("La cantidad que se desea retirar es mayor al stock actual del producto")
        this.cantidad -= cantidad
        new Producto(this.nombre, cantidad, this.precio)
    }
}