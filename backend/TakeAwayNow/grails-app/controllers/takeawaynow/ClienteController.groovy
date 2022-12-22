package takeawaynow

class ClienteController {

    ClienteService service

    def index() {
        Negocio pc = new Negocio("Paseo Colon");
        Negocio lh = new Negocio("Las Heras");
        Negocio av = new Negocio("CBC Avellaneda");
        def negocios = [pc, lh, av]
        for (negocio in negocios) {
            negocio.save()
            render(template: "NegociosDisponibles", model: [negocio:negocio])
        }
        // service.listarNegocios([pc, lh, av])
    }

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
