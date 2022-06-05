val trim = """[^.\d-+*/]""".toRegex()

fun trim(v: String): String = v.replace(trim, "")

fun repMtoPM(v: String): String = v.replace("-", "+-")

fun cut(v: String): String = if (v[0] == '+') {
    v.substring(1)
} else {
    v
}

val expression = """^\+?(-?[.\d]+)""".toRegex()

fun parseExpression(v: String) = expression.find(v)?.groupValues?.get(1)

val operator = """^[+/*]""".toRegex()

fun parseOperator(v: String) = operator.find(v)?.value

fun eval(op: String, leftValue: Double, rightValue: Double): Double =
    when (op) {
        "+" -> leftValue + rightValue
        "*" -> leftValue * rightValue
        "/" -> leftValue / rightValue
        else -> throw Throwable("Invalid operator $op")
    }

fun calc(v: String): Double = _calc(cut(repMtoPM(trim(v))))

fun _calc(v: String): Double {
    var str = v

    val exp = parseExpression(str) ?: return 0.0
    str = str.substring(exp.length)
    val exp1 = exp.toDouble()

    if (str == "") return exp1

    val op = parseOperator(str) ?: throw Throwable("Invalid operator $str")
    str = str.substring(op.length)

    val _exp = parseExpression(str) ?: throw Throwable("Invalid expression $str")
    str = str.substring(exp.length)
    val exp2 = _exp.toDouble()

    println("$op $exp1 $exp2")

    return eval(op, exp1, exp2)
}