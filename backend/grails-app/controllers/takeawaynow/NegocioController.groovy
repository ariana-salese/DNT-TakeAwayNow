package takeawaynow

class NegocioController {

    static scaffold = Negocio

    def index() { 
        def lista = Negocio.list()
        render(view: "/negocio/index", model: [negocioList: lista])
    }

    def ingreso(String nombreNegocio) {
        new Negocio(nombreNegocio, new Horario(9, 0), new Horario(18, 0)).save()
        render "Hola negocio ${nombreNegocio}"
    }

    def registro(String nombreNegocio, String horarioApertura, String horarioCierre) {
        render "Bienvenido ${nombreNegocio} con horario de apertura ${horarioApertura} y horario de cierre ${horarioCierre}"
    }

    def login() {
        render(view: "/negocio/login")
    }

    def acceder() {
        def nombre = params.usuario
        def negocio = Negocio.findByNombre(nombre)
        if (!negocio) {
            render "No existe el negocio ${nombre}"
        } else {
            def id_negocio = negocio.id
            def horario_apertura = negocio.horario_apertura
            def horario_cierre = negocio.horario_cierre
            render "Bievenido ${nombre} con horario de apertura ${horario_apertura} y horario de cierre ${horario_cierre}"
        }
    }

    def almacen() {

        // render(view: "/negocio/almacen")
    }

}
