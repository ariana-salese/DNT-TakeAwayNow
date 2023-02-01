package takeawaynow

class ClienteController {

    ClienteService service
    static scaffold = Cliente

    def index() {
        // Horario horario_apertura = new Horario(9,0)
        // Horario horario_cierre = new Horario(18,0)
        // Negocio pc = new Negocio("Paseo Colon", horario_apertura, horario_cierre);
        // Negocio lh = new Negocio("Las Heras", horario_apertura, horario_cierre);
        // Negocio av = new Negocio("CBC Avellaneda", horario_apertura, horario_cierre);
        // def negocios = [pc, lh, av]
        // for (negocio in negocios) {
        //     negocio.save()
        //     render(template: "NegociosDisponibles", model: [negocio:negocio])
        // }
        // // service.listarNegocios([pc, lh, av])
        def lista = Cliente.list()
        render(view: "/cliente/index", model: [clienteList: lista])
    }

    def home() {
        render "<h1>Real Programmers do not eat Quiche</h1>"
    }

    def login() {
        render(view: "/cliente/login")
    }

    def acceder() {
        def nombre = params.usuario
        def cliente = Cliente.findByNombre(nombre)
        if (!cliente) {
            render "No existe el cliente ${nombre}"
        } else {
            render "Bienvenido ${nombre}"
        }
    }

    def listarNegocios() {
        def lista = Negocio.list()
        render(template:"/cliente/negociosDisponibles", model:[negocios: lista])
    }

    def verSaldo() {
        def lista = Cliente.list()
        render(template:"/cliente/saldoDisponible", model:[clientes: lista])
    }

    // def ingreso(String nombreCliente) {
    //     new Cliente(nombreCliente).save()

    //     render "Hello again ${nombreCliente}"
    // }

    // def registro(String nombreCliente) {
    //     new Cliente(nombreCliente).save()
    //     render "Welcome ${nombreCliente}"
    // }

    // def crear() {
    //     (1..10).each { it ->
    //         new Negocio("HOLACOMOVA").save()
    //     }

    //     (1..10).each { it ->
    //         def cliente = new Cliente()
    //         cliente.cargarSaldo(new Dinero(1000))
    //         cliente.save()
    //     }
    //     render "listo"
    // }
}
