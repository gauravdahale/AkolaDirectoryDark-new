package gauravdahale.gtech.akoladirectory.models

/**
 * Created by Gaurav on 6/25/2018.
 */
class UserModel {
    var userName: String? = null
    var userOccupation: String? = null
    var userNumber: String? = null
    var userCity: String? = null
    var token: String? = null
    var date: String? = null
     var datetime: String? = null

    constructor() {}

    constructor(userName: String?, userOccupation: String?, userNumber: String?, userCity: String?, token: String?, date: String?) {
        this.userName = userName
        this.userOccupation = userOccupation
        this.userNumber = userNumber
        this.userCity = userCity
        this.token = token
        this.date = date
    }

    constructor(userName: String?, userOccupation: String?, userNumber: String?, userCity: String?, token: String?) {
        this.userName = userName
        this.userOccupation = userOccupation
        this.userNumber = userNumber
        this.userCity = userCity
        this.token = token
    }

    constructor(userName: String?, userOccupation: String?, userNumber: String?, userCity: String?) {
        this.userName = userName
        this.userOccupation = userOccupation
        this.userNumber = userNumber
        this.userCity = userCity
    }

}