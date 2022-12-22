package takeawaynow

// Place your Spring DSL code here
beans = {
    Negocio pc = new Negocio("Paseo Colon");
    Negocio lh = new Negocio("Las Heras");
    Negocio av = new Negocio("CBC Avellaneda");
    // pc.save()
    // lh.save()
    // av.save()

    Cliente lauti = new Cliente()
    lauti.ingresarNegocio(pc)

    Cliente santi = new Cliente()
    santi.ingresarNegocio(lh)

    Cliente ari = new Cliente()
    ari.ingresarNegocio(av)

    // lauti.save()
    // santi.save()
    // ari.save()
}
