import java.util.*

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

    val env = LinkedList<Pair<Double, String>>()

    fun f(curr: Double, op: String): Double {
        val c = next()
        val n = if (c == "(") f(next().toDouble(), next()) else c.toDouble()

        val nextOp: String
        try {
            nextOp = next()
        } catch (e: Throwable) {
            var result = eval(op, curr, n)
            while (!env.isEmpty()) {
                val (_curr, _op) = env.pop()
                result = eval(_op, _curr, result)
            }
            return result
        }

        return when (op) {
            "+" -> {
                when (nextOp) {
                    "*", "/" -> {
                        env.add(curr to op)
                        f(n, nextOp)
                    }
                    ")" -> {
                        var result = eval(op, curr, n)
                        while (!env.isEmpty()) {
                            val (_curr, _op) = env.pop()
                            result = eval(_op, _curr, result)
                        }
                        result
                    }
                    "+" -> {
                        var result = eval(op, curr, n)
                        while (!env.isEmpty()) {
                            val (_curr, _op) = env.pop()
                            result = eval(_op, _curr, result)
                        }

                        f(result, nextOp)
                    }
                    else -> throw Throwable("Invalid operator $nextOp")
                }
            }
            "*", "/" -> {
                when (nextOp) {
                    "*", "/" -> f(eval(op, curr, n), nextOp)
                    "+" -> {
                        var result = eval(op, curr, n)
                        while (!env.isEmpty()) {
                            val (_curr, _op) = env.pop()
                            result = eval(_op, _curr, result)
                        }

                        f(result, nextOp)
                    }
                    ")" -> {
                        var result = eval(op, curr, n)
                        while (!env.isEmpty()) {
                            val (_curr, _op) = env.pop()
                            result = eval(_op, _curr, result)
                        }
                        result
                    }
                    else -> throw Throwable("Invalid operator $nextOp")
                }
            }
            else -> throw Throwable("Invalid operator $op")
        }
    }

    return f(next().toDouble(), next())

}