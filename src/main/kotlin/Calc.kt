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

        if (token == "(") {
            env.push(0.0 to "(")
            return f()
        }

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

        if (op == ")") {
            var result = cur
            do {
                val (prev, pop) = env.pop()
                result = eval(pop, prev, result)
            } while (!env.isEmpty() && env.first.second != "(")

            env.pop()

            val nextOp =  try {
                next()
            } catch (_: Throwable) {
                do {
                    val (prev, pop) = env.pop()
                    result = eval(pop, prev, result)
                } while (!env.isEmpty())

                return result
            }

            env.push(result to nextOp)

            return f()
        }

        if (env.isEmpty()) {
            env.push(cur to op)
        } else {
            val (prev, pop) = env.first

            when (pop) {
                "(" -> {
                    env.push(cur to op)
                }
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