val trim = """[^.\d-+*/]""".toRegex()

fun trim(v: String): String = v.replace(trim, "")

fun repMtoPM(v: String): String = v.replace("-", "+-")


fun cut(v: String): String = if (v[0] == '+') {
    v.substring(1)
} else {
    v
}

val expression = """^(-?[.\d]+)""".toRegex()

fun parseExpression(v: String) = expression.find(v)?.value

val operator = """^[+/*]""".toRegex()

fun parseOperator(v:String) = operator.find(v)?.value

val groupMD = """((?:\+-?)?[.\d]+)([*/])((?:\+-?)?[.\d]+)""".toRegex()


fun foldGroup(v: String): Double = groupMD.findAll(v).fold(0.0) { acc, curr ->
    val (_, left, op, right) = curr.groupValues
    val leftValue = left.replace("+", "").toDouble()
    val rightValue = right.replace("+", "").toDouble()
    val result = when (op) {
        "*" -> leftValue * rightValue
        "/" -> leftValue / rightValue
        else -> throw Throwable("Invalid operator $op")
    }

    acc + result
}


fun calc(v: String) {
    var str = cut(repMtoPM(trim(v)))
    val exp1 = parseExpression(str) ?: return
    str = str.substring(exp1.length)

    val op = parseOperator(str) ?: return
    str = str.substring(op.length)

    val exp2 = parseExpression(str) ?: return
    str = str.substring(exp2.length)


    println(exp1)
    println(op)
    println(exp2)



    foldGroup(str)
}