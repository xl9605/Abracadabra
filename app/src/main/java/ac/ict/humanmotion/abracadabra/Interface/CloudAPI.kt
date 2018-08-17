package ac.ict.humanmotion.abracadabra.Interface

import ac.ict.humanmotion.abracadabra.Bean.Operation
import ac.ict.humanmotion.abracadabra.Bean.User
import ac.ict.humanmotion.abracadabra.Bean.Worktable
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/11.
 */

interface CloudAPI {

    //get all User
    @GET("user")
    fun getUser(@Query("offset") offset: Int = 0,
                @Query("query") query: String? = null,
                @Query("sortby") sortby: String? = null,
                @Query("order") order: String? = null): Observable<List<User>>

    //get User by id
    @GET("user/{id}")
    fun getUserById(@Path("id") UserId: Int): Observable<User>

    //create User
    @POST("user")
    fun postUser(@Body User: User): Observable<User>

    //update the User
    @PUT("user/{id}")
    fun putUserById(@Path("id") UserId: Int, @Body User: User): Observable<User>

    //delete the User
    @DELETE("user/{id}")
    fun deleteUserById(@Path("id") UserId: Int): Observable<User>

    //--------------------------------------------------------------------------------------------//

    //get all operation
    @GET("operation")
    fun getOperation(@Query("limit") limit: Int = 10,
                     @Query("offset") offset: Int = 0,
                     @Query("query") query: String? = null,
                     @Query("sortby") sortby: String? = null,
                     @Query("order") order: String? = null): Observable<List<Operation>>

    //get enrollment by id
    @GET("operation/{id}")
    fun getOperationById(@Path("id") enrollmentId: Int): Observable<Operation>

    //create Enrollment
    @POST("operation")
    fun postOperation(@Body enrollment: Operation): Observable<String>

    //update the Enrollment
    @PUT("operation/{id}")
    fun putOperationById(@Path("id") enrollmentId: Int, @Body enrollment: Operation): Observable<String>

    //delete the Enrollment
    @DELETE("operation/{id}")
    fun deleteOperationById(@Path("id") enrollmentId: Int): Observable<String>

    //--------------------------------------------------------------------------------------------//

    //get all worktable
    @GET("worktable")
    fun getWorkTable(@Query("offset") offset: Int = 0,
                     @Query("query") query: String? = null,
                     @Query("sortby") sortby: String? = null,
                     @Query("order") order: String? = null): Observable<List<Worktable>>

    //get course by id
    @GET("worktable/{id}")
    fun getWorkTableById(@Path("id") courseId: Int): Observable<Worktable>

    //create course
    @POST("worktable")
    fun postWorkTable(@Body course: Worktable): Observable<String>

    //update the course
    @PUT("worktable/{id}")
    fun putWorkTableById(@Path("id") courseId: Int, @Body course: Worktable): Observable<String>

    //delete the course
    @DELETE("worktable/{id}")
    fun deleteWorkTableById(@Path("id") courseId: Int): Observable<String>

    // TODO: F1-> POST IMAGE TO SERVER

    // TODO: F2-> POST IMAGE TO SERVER and GET CAMPARE RESULT

    // TODO: F3-> GET OCR RESULT

    // TODO: F4-> POST LMPSB2 DATA
}
