fun main() {
    println(calc("-2 * -3 + 0.4 / - 0.2") == 4.0)
    println(calc("2 * 3 + 0.4 / 0.2") == 8.0)
    println(calc("-2 -3 + 0.4") == -4.6)
    println(calc("1 + 2 * 3 / 4 + 7") == 9.5)
    println(calc("-1 -2 * -3 / -4  -7") == -9.5)
    println(calc("1 * 2 + 3 / 4 + 7") == 9.75)
    println(calc("-1 * -2 -3 / -4 -7") == -4.25)
    println(calc("-1 * 2 + 3 / -4 * 7") == -7.25)
    println(calc("-1 * -2 -3 / -4 * -7") == -3.25)
    println(calc("-2 *(-3 + 0.4)/ -0.2")  /* == -26.0 */)
    println(calc("-2 +(-3 * 0.4)/ -0.2") /* == 4.0 */)
//    println(calc("-2 +(-3 * (-5 + 0.4) )/ -0.2") /* == -71.0 */) // TODO
//    println(calc("-2 +((-3 +9) * (-5 + 0.4) )/ -0.2")) // TODO
}
