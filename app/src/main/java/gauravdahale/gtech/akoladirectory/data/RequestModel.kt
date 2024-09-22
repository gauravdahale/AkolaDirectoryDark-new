package gauravdahale.gtech.akoladirectory.data

class RequestModel {
    var n: String? = null
    var a: String? = null
    var p: String? = null
    var i: String? = null
    var d: String? = null
    var t: String? = null

    internal constructor() {

    }

    constructor(n: String, a: String, p: String, i: String, d: String, t: String) {
        this.n = n
        this.a = a
        this.p = p
        this.i = i
        this.d = d
        this.t = t
    }
constructor(n:String,a:String,p:String)
}