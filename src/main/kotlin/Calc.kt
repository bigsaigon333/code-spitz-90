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

    fun f(): Double {
        val token = next()
        val cur = token.toDouble()
        val op = try {
            next()
        } catch (_: Throwable) {
            var result = cur
            do {
                val (prev, pop) = env.pop()
                result = eval(pop, prev, result)
            } while (!env.isEmpty())

            return result
        }


        if (env.isEmpty()) {
            env.push(cur to op)
        } else {
            val (prev, pop) = env.first

            when (pop) {
                "+" -> {
                    when (op) {
                        "*", "/" -> {
                            env.push(cur to op)
                        }
                        "+" -> {
                            env.pop()
                            env.push(eval(pop, prev, cur) to op)
                        }
                        else -> throw Throwable("Invalid operator $op")
                    }
                }
                "*", "/" -> {
                    when (op) {
                        "*", "/", "+" -> {
                            env.pop()
                            env.push(eval(pop, prev, cur) to op)
                        }
                        else -> throw Throwable("Invalid operator $op")
                    }
                }
                else -> throw Throwable("Invalid operator $pop")
            }
        }

        return f()
    }

    return f()
}