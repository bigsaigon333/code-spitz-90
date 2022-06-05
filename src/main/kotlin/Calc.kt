val trim = """[^.\d-+*/]""".toRegex()

fun trim(v: String): String = v.replace(trim, "")

fun repMtoPM(v: String): String = v.replace("-", "+-").replace("*+", "*").replace("/+", "/")

fun cut(v: String): String = if (v[0] == '+') v.substring(1) else v

val re = """^(-?[.\d]+|[+/*])""".toRegex()
fun parse(v: String) = re.find(v)

fun eval(op: String, leftValue: Double, rightValue: Double): Double = when (op) {
    "+" -> leftValue + rightValue
    "*" -> leftValue * rightValue
    "/" -> leftValue / rightValue
    else -> throw Throwable("Invalid operator $op")
}

fun calc(v: String): Double {
    var str = cut(repMtoPM(trim(v)))
    val next = {
        val (all, value) = parse(str)?.groupValues ?: throw Throwable("Invalid string $str")
        str = str.substring(all.length)
        value
    }

    return f(next, next().toDouble(), next())
}

fun f(next: () -> String, curr: Double, op: String): Double {
    val n = next().toDouble()

    val nextOp: String
    try {
        nextOp = next()
    } catch (e: Throwable) {
        return eval(op, curr, n)
    }

    return if (op == "+" && nextOp != "+") {
        eval(op, curr, f(next, n, nextOp))
    } else {
        f(next, eval(op, curr, n), nextOp)
    }
}