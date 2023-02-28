package takeawaynow

class ClienteController {

    ClienteService service = new ClienteService() // vemos que en algunos repositorios 
                                    // esto = new ClienteService() no lo incluyen pero sino nos lanza error
    static scaffold = Cliente

    def index() {
        def lista = Cliente.list()
        render(view: "/cliente/index", model: [clienteList: lista])
    }

    def login() {
        render(view: "/cliente/login")
    }

    def acceder() {
        def nombre = params.usuario
        Cliente cliente = Cliente.findByNombre(nombre)
        if (!cliente) {
            render "No existe el cliente ${nombre}"
        } else {
            String dniString = cliente.dni as String
            response.setCookie('dni', dniString)
            render(view:"/cliente/perfil", model:[cliente: cliente])
        }
    }

    def entrarACargarSaldo() {
        def dni = request.getCookie('dni')
        Cliente cliente = Cliente.findByDni(dni as Long)
        render(view: "/cliente/cargarSaldo", model:[cliente: cliente])
    }

    def listarNegocios() {
        def lista = Negocio.list()
        render(template:"/cliente/negociosDisponibles", model:[negocios: lista])
    }

    def verSaldo() {
        def lista = Cliente.list()
        render(template:"/cliente/saldoDisponible", model:[clientes: lista])
    }

    def cargarSaldo() {
        def dni = request.getCookie('dni')
        Cliente cliente = Cliente.findByDni(dni as Long)
        def saldoACargar = params.saldoACargar as Integer
        cliente.cargarSaldo(new Dinero(saldoACargar))
        service.guardarCliente(cliente)
        render(view:"/cliente/perfil", model:[cliente: cliente])
    }
}
