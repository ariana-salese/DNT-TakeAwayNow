package takeawaynow

// Place your Spring DSL code here
beans = {
    Buffet pc = new Buffet("Paseo Colon");
    Buffet lh = new Buffet("Las Heras");
    Buffet av = new Buffet("CBC Avellaneda");
    // pc.save()
    // lh.save()
    // av.save()

    Cliente lauti = new Cliente()
    lauti.ingresarBuffet(pc)

    Cliente santi = new Cliente()
    santi.ingresarBuffet(lh)

    Cliente ari = new Cliente()
    ari.ingresarBuffet(av)

    // lauti.save()
    // santi.save()
    // ari.save()
}
