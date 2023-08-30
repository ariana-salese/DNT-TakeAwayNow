package takeawaynow

import java.time.LocalTime

class NegocioController {

    static scaffold = Negocio

    def index() { 
        def lista = Negocio.list()
        render(view: "/negocio/index", model: [negocioList: lista])
    }

    def ingreso(String nombreNegocio) {
        new Negocio(nombreNegocio, LocalTime.of(9, 0), LocalTime.of(18, 0)).save()
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
            render "Bienvenido ${nombre}"
        }
    }
}
