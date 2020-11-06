package operations

import com.google.gson.Gson
import data.otp.Response
import org.apache.http.client.fluent.Request

fun requestRoot(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Response {
    /** http kérés összeállítása és végrehajtása **/
    val s: String = Request.Get(
        "http://localhost:8000/otp/routers/default/plan"
                + "?fromPlace=${lat1},${lon1}"
                + "&toPlace=${lat2},${lon2}"
                + "&time=3:50pm&date=2-25-2019&mode=CAR,WALK&maxWalkDistance=6000&arriveBy=false"
    ).execute().returnContent().asString()

    println(s)

    val response: Response = Gson().fromJson(s, Response::class.java)

    return response
}