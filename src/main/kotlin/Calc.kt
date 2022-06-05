val trim = """[^.\d-+*()/]""".toRegex()

fun trim(v: String): String = v.replace(trim, "")

val MtoPM = """(?<!^|[*/(])-""".toRegex()

fun repMtoPM(v: String): String = v.replace(MtoPM, "+-")

val re = """^(-?[.\d]+|[+/*()])""".toRegex()

fun parse(v: String) = re.find(v)

fun eval(op: String, leftValue: Double, rightValue: Double): Double = when (op) {
    "+" -> leftValue + rightValue
    "*" -> leftValue * rightValue
    "/" -> leftValue / rightValue
    else -> throw Throwable("Invalid operator $op")
}

fun calc(v: String): Double {
    var str = repMtoPM(trim(v))
    val next = {
        val (all, value) = parse(str)?.groupValues ?: throw Throwable("Invalid string $str")
        str = str.substring(all.length)
        value
    }

    fun f(curr: Double, op: String): Double {
        val c = next()
        val n = if (c == "(") f(next().toDouble(), next()) else c.toDouble()

        val nextOp: String
        try {
            nextOp = next()
        } catch (e: Throwable) {
            return eval(op, curr, n)
        }

        return if (op == "+") {
            when (nextOp) {
                "*", "/" -> eval(op, curr, f(n, nextOp))
                ")" -> eval(op, curr, n)
                "+" -> f(eval(op, curr, n), nextOp)
                else -> throw Throwable("Invalid operator $nextOp")
            }
        } else {
            when (nextOp) {
                "*", "/", "+" -> f(eval(op, curr, n), nextOp)
                ")" -> eval(op, curr, n)
                else -> throw Throwable("Invalid operator $nextOp")
            }
        }
    }

    return f(next().toDouble(), next())

}