package takeawaynow

import grails.gorm.transactions.Transactional

@Transactional
class ClienteService {

    def listarBuffets(List<Buffet> buffets) {
        for (buffet in buffets) {
            buffet.save()
            render(template: "BuffetsDisponibles", model: [buffet:buffet])
        }
    }
}
