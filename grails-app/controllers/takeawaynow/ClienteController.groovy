package takeawaynow

class ClienteController {

    ClienteService service
    static scaffold = Cliente

    def index() {
        def lista = Cliente.list()
        render(view: "/cliente/index", model: [clienteList: lista])
    }

    // def home() {
    //     render "<h1>Real Programmers do not eat Quiche</h1>"
    // }

    def login() {
        render(view: "/cliente/login")
    }

    def acceder() {
        def nombre = params.usuario
        Cliente cliente = Cliente.findByNombre(nombre)
        if (!cliente) {
            render "No existe el cliente ${nombre}"
        } else {
            //def lista = Cliente.list()
            String dniString = cliente.dni as String
            response.setCookie('dni', dniString)
            render(view:"/cliente/perfil", model:[cliente: cliente])
        }
    }

    def entrarACargarSaldo() {
        def dni = request.getCookie('dni')
        // print "!!!! DNI: ${dni}\n"
        Cliente cliente = Cliente.findByDni(dni as Long)
        // print "!!!! Nombre del cliente: ${cliente.nombre}\n"
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
        print "!!!! cargar saldo dni: ${dni}\n"
        Cliente cliente = Cliente.findByDni(dni as Long)
        print "!!!! Nombre del cliente cargar salgo: ${cliente.nombre}\n"
        def saldoACargar = params.saldoACargar as Integer
        print "!!!! Saldo: ${saldoACargar}\n"
        cliente.cargarSaldo(new Dinero(saldoACargar))
        cliente.save()
        render(view:"/cliente/perfil", model:[cliente: cliente])
    }
}
