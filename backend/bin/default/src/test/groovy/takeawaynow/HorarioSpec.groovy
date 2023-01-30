package takeawaynow

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class HorarioSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "crear un horario con horas negativas lanza un error"() {
        when: "un horario con horas negativas"
            def horario = new Horario(-9,0)

        then: "se lanza un error"
            IllegalStateException exception = thrown()
    }

    void "crear un horario con minutos negativos lanza un error"() {
        when: "un horario con minutos negativos"
            def horario = new Horario(9,-10)

        then: "se lanza un error"
            IllegalStateException exception = thrown()
    }

    void "crear un horario con hora mayor a 24 lanza un error"() {
        when: "un horario con hora 25"
            def horario = new Horario(25,0)

        then: "se lanza un error"
            IllegalStateException exception = thrown()
    }

    void "crear un horario con minutos mayores a 59 lanza un error"() {
        when: "un horario con minutos negativos"
            def horario = new Horario(9,70)

        then: "se lanza un error"
            IllegalStateException exception = thrown()
    }

    void "crear un horario con minutos iguales a 60 lanza un error"() {
        when: "un horario con minutos negativos"
            def horario = new Horario(9,60)

        then: "se lanza un error"
            IllegalStateException exception = thrown()
    }

    void "dos horarios con la misma hora y minutos son iguales"() {
        when: "dos horarios son nueve en punto"
            def horarioNueve1 = new Horario(9,0)
            def horarioNueve2 = new Horario(9,0)

        then: "los horarios son iguales"
            horarioNueve1 == horarioNueve2
    }

    void "dos horarios con la misma hora y distintos minutos no son iguales"() {
        when: "dos horarios son nueve en punto"
            def horarioNueve1 = new Horario(9,20)
            def horarioNueve2 = new Horario(9,10)

        then: "los horarios no son iguales"
            horarioNueve1 != horarioNueve2
    }

    void "un horario con hora igual a 9 es menor a otro con hora igual a 10"() {
        when: "un horarios es nueve en punto y otro 10 en punto"
            def horarioNueve = new Horario(9,0)
            def horarioDiez = new Horario(10,0)

        then: "el horarios con hora igual a nueve es menor que el horarios con hora igual a 10"
            horarioNueve < horarioDiez
    }

    void "un horario con hora igual a 9:30 es menor a otro con hora igual a 10:20"() {
        when: "un horarios es nueve en punto y otro 10 en punto"
            def horarioNueve = new Horario(9,30)
            def horarioDiez = new Horario(10,20)

        then: "el horarios con hora igual a 9:30 es menor que el horarios con hora igual a 10:20"
            horarioNueve < horarioDiez
    }

    void "un horario con hora igual a 10 es menor a otro con hora igual a 9"() {
        when: "un horarios es nueve en punto y otro 10 en punto"
            def horarioNueve = new Horario(9,0)
            def horarioDiez = new Horario(10,0)

        then: "el horarios con hora igual a nueve es menor que el horarios con hora igual a 10"
            horarioDiez > horarioNueve
    }
}
