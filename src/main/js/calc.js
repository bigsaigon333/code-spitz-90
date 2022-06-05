const calc = (op, curr, next) => eval(`${curr}${op}${next}`)


const f1 = (arr, curr, op) => {
  const next = arr.shift()

  if (arr.length === 0) {
    return calc(op, curr, next)
  }

  const nop = arr.shift()

  if (op === "+") {
    if (nop === "+") {
      return f1(arr, calc(op, curr, next), nop);
    } else {
			return calc(op, curr, f1(arr, next, nop));
    }
  } else {
    if (nop === "+") {
      return f1(arr, calc(op, curr, next), nop);
    } else {
      return f1(arr, calc(op, curr, next), nop);
    }
  }
}

let i = 0;

const f = (arr, curr, op) => {
  let next = arr.shift()

  if (arr.length === 0)  {
    console.log(`[${i++}]`, curr, op, next);
    return calc(op, curr, next)
  }

  const nop = arr.shift()
  console.log(`[${i++}]`,curr, op, next, nop);

  if (op === "+") {
    if (nop === "+") {
      curr = calc(op, curr, next);
      return f(arr, curr, nop);
    } else {
      next = f(arr, next, nop);
      console.log(`[${i++}]`,curr, op, next, nop);
			return calc(op, curr, next);
    }
  } else {
    if (nop === "+") {
      curr = calc(op, curr, next)
      return f(arr, curr, nop);
    } else {
      curr = calc(op, curr, next)
      return f(arr, curr, nop);
    }
  }
}

const g = (arr) => {
  return f1(arr, arr.shift(), arr.shift())
}


const toArr =str => str.split("").map(c => /^\d+$/.test(c) ?  Number(c) : c)

console.assert((v = g(toArr("1+2*3/4+7")) === 9.5), v )
console.assert((v = g(toArr("1*2+3/4+7")) === 9.75), v )
console.assert((v = g(toArr("1*2+3/4*7")) ) === 7.25, v )
