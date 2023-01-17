package takeawaynow

class NegocioController {

    static scaffold = Negocio

    def index() { 
        // render "hola"
    }

    def ingreso(String nombreNegocio) {
        new Negocio(nombreNegocio, new Horario(9, 0), new Horario(18, 0)).save()
        render "Hola negocio ${nombreNegocio}"
    }

    def registro(String nombreNegocio, String horarioApertura, String horarioCierre) {
        render "Bienvenido ${nombreNegocio} con horario de apertura ${horarioApertura} y horario de cierre ${horarioCierre}"
    }
}
