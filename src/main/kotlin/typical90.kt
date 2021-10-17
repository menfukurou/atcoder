fun main() {
    encyclopediaOfParentheses()
}

fun encyclopediaOfParentheses() {
    // https://atcoder.jp/contests/typical90/tasks/typical90_b
    val n = readLine()!!.toInt()

    if (n % 2 == 1) return

    val init = mutableListOf<String>()
    repeat(n / 2) {
        init.add("(")
        init.add(")")
    }
    val cases = init.permutationWithoutRepetition(n).toMutableList().distinct()
    val ans = cases.filter { case ->
        isValid(case.toMutableList())
    }.map {
        it.joinToString("")
    }.sorted()
    ans.forEach {
        println(it)
    }
}

private fun isValid(list: MutableList<String>): Boolean {
    do {
        if (list.first() == ")") return false
        list.removeAt(0)
        list.removeAt(list.indexOf(")"))
    } while (list.isNotEmpty())
    return true
}

fun yokanParty() {
    // https://atcoder.jp/contests/typical90/tasks/typical90_a
    val (n, l) = readLine()!!.trim().split(" ").map { it.toInt() }
    val k = readLine()!!.trim().toInt()
    val a = readLine()!!.trim().split(" ").map { it.toInt() }

    val ans = mutableListOf<Int>()
    val permutation = a.permutationWithoutRepetition(k)
    permutation.forEach { list ->
        var score = Int.MAX_VALUE
        val temp = list.toMutableList()
        temp.add(l)
        temp.forEachIndexed { index, i ->
            if (index == 0) {
                if (i < score) {
                    score = i
                }
            } else {
                val diff = i - temp[index - 1]
                if (diff < score) {
                    score = diff
                }
            }

        }
        ans.add(score)
    }
    println(ans.max())
}

/** 重複なしの順列 */
private fun <T> List<T>.permutationWithoutRepetition(k: Int): Sequence<List<T>> {
    require(k in 0..size) { "引数 k は 0 以上かつ $size 以下でなければなりません。k: $k" }

    return pcSequenceFactory<T> { options, i ->
        options.take(i) + options.drop(i + 1)
    }(this, k)
}

/** 重複なしの組み合わせ */
private fun <T> List<T>.combinationWithoutRepetition(k: Int): Sequence<List<T>> {
    require(k in 0..size) { "引数 k は 0 以上かつ $size 以下でなければなりません。k: $k" }

    return pcSequenceFactory<T> { options, i ->
        options.drop(i + 1)
    }(this, k)
}

/** 重複ありの順列 */
private fun <T> List<T>.permutationWithRepetition(k: Int): Sequence<List<T>> {
    require(k >= 0) { "引数 k は 0 以上でなければなりません。k: $k" }

    return pcSequenceFactory<T> { options, i ->
        options
    }(this, k)
}

/** 重複ありの組み合わせ */
private fun <T> List<T>.combinationWithRepetition(k: Int): Sequence<List<T>> {
    require(k >= 0) { "引数 k は 0 以上でなければなりません。k: $k" }

    return pcSequenceFactory<T> { options, i ->
        options.drop(i)
    }(this, k)
}

private fun <T> pcSequenceFactory(
    selecteds: List<T> = emptyList(),
    filter: (options: List<T>, i: Int) -> List<T>
): (options: List<T>, k: Int) -> Sequence<List<T>> =
    { options, k ->
        sequence {
            if (k == 0) {
                yield(selecteds)
                return@sequence
            }

            options.forEachIndexed { i, option ->
                pcSequenceFactory(selecteds + option, filter).let {
                    it(filter(options, i), k - 1)
                }.forEach {
                    yield(it)
                }
            }
        }
    }
