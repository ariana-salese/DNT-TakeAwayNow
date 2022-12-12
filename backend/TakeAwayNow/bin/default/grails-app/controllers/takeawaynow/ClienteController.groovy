package takeawaynow

class ClienteController {

    ClienteService service

    def index() {
        Buffet pc = new Buffet("Paseo Colon");
        Buffet lh = new Buffet("Las Heras");
        Buffet av = new Buffet("CBC Avellaneda");
        def buffets = [pc, lh, av]
        for (buffet in buffets) {
            buffet.save()
            render(template: "BuffetsDisponibles", model: [buffet:buffet])
        }
        // service.listarBuffets([pc, lh, av])
    }

    // def crear() {
    //     (1..10).each { it ->
    //         new Buffet("HOLACOMOVA").save()
    //     }

    //     (1..10).each { it ->
    //         def cliente = new Cliente()
    //         cliente.cargarSaldo(new Dinero(1000))
    //         cliente.save()
    //     }
    //     render "listo"
    // }
}
