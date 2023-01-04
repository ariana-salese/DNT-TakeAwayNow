package takeawaynow

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        // "/negocio"(resources: "negocio") {
        //     "/login"(controller:"negocio", action:["login"])
        // }

        // "/cliente"(resources: "cliente") {
        //     "/login"(controller:"cliente", action:"login")
        // }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
