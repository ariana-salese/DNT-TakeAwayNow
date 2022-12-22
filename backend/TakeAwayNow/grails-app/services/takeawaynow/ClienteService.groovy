package takeawaynow

import grails.gorm.transactions.Transactional

@Transactional
class ClienteService {

    def listarNegocios(List<Negocio> negocios) {
        for (negocio in negocios) {
            negocio.save()
            render(template: "NegociosDisponibles", model: [negocio:negocio])
        }
    }
}
