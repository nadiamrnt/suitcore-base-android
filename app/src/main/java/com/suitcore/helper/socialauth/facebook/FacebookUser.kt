package com.suitcore.helper.socialauth.facebook

import org.json.JSONObject

/**
 * Created by dodydmw19 on 12/14/18.
 */

class FacebookUser {
    var name: String? = null

    var email: String? = null

    var facebookID: String? = null

    var gender: String? = null

    var about: String? = null

    var bio: String? = null

    var coverPicUrl: String? = null

    var profilePic: String? = null

    /**
     * JSON response received. If you want to parse more fields.
     */
    var response: JSONObject? = null

}
