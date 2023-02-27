package takeawaynow

class ProductoController {

    static scaffold = Producto

    def index() { 
        def lista = Producto.list()
        render(view: "/producto/index", model: [productoList: lista])
    }
}
