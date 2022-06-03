val trim = """[^.\d-+*/]""".toRegex()

fun trim(v: String): String = v.replace(trim, "")

fun repMtoPM(v: String): String = v.replace("-", "+-")

val groupMD = """((?:\+-?)?[.\d]+)([*/])((?:\+-?)?[.\d]+)""".toRegex()

fun cut(v: String): String = if (v[0] == '+') {
    v.substring(1)
} else {
    v
}


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
    val str = cut(repMtoPM(trim(v)))

//    println(str)



    foldGroup(str)
}