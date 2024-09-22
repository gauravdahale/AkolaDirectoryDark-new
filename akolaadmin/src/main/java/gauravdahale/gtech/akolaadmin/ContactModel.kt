package gauravdahale.gtech.akolaadmin

class ContactModel {
    var d: String? = null
    var p: String? = null
    var a: String? = null
    var i: String? = null
    var n: String? = null
    var t: String? = null
    var o: String? = null
    var ii: String? = null
    var iii: String? = null

    constructor() {}

    constructor(
        d: String,
        p: String,
        a: String,
        i: String,
        n: String,
        t: String,
        o: String,
        ii: String,
        iii: String
    ) {
        this.d = d
        this.p = p
        this.a = a
        this.i = i
        this.n = n
        this.t = t
        this.o = o
        this.ii = ii
        this.iii = iii
    }

    constructor(
        d: String,
        p: String,
        a: String,
        i: String,
        n: String,
        t: String,
        o: String,
        ii: String
    ) {
        this.d = d
        this.p = p
        this.a = a
        this.i = i
        this.n = n
        this.t = t
        this.o = o
        this.ii = ii
    }

    constructor(n: String, a: String, i: String, p: String, d: String) {
        this.n = n
        this.a = a
        this.i = i
        this.p = p
        this.d = d
    }

    constructor(n: String, a: String, p: String) {
        this.n = n
        this.a = a
        this.p = p

    }

    constructor(n: String, a: String, p: String, t: String) {
        this.n = n
        this.a = a

        this.p = p
        this.t = t

    }

    constructor(d: String, p: String, a: String, i: String, n: String, t: String, o: String) {
        this.d = d
        this.p = p
        this.a = a
        this.i = i
        this.n = n
        this.t = t
        this.o = o
    }
}
