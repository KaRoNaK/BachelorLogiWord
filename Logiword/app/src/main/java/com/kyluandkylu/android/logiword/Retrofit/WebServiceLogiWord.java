package com.kyluandkylu.android.logiword.Retrofit;

import com.kyluandkylu.android.logiword.FriendList.FriendPairModel;
import com.kyluandkylu.android.logiword.FriendList.FriendResponseModel;
import com.kyluandkylu.android.logiword.Game.DailyChallengeAttemptModel;
import com.kyluandkylu.android.logiword.Game.GameResultsModel;
import com.kyluandkylu.android.logiword.GlobalScore.ScoreModel;
import com.kyluandkylu.android.logiword.LocalScore.LocalScoreModel;
import com.kyluandkylu.android.logiword.Profile.ChangeProfileInformationModel;
import com.kyluandkylu.android.logiword.Profile.ProfileModel;
import com.kyluandkylu.android.logiword.Profile.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceLogiWord {

    @GET("singleplayer")
    Call<List<ScoreModel>> getTopPlayersInSinglePlayer();

    @GET("singleplayer/{myPlayerID}")
    Call<List<LocalScoreModel>> getLocalScoreTable(@Path("myPlayerID") int myPlayerID);

    @POST("account")
    Call<ResponseBody> registerUser(@Body User user);

    @GET("account/{userName}/{password}")
    Call<Integer> logIn(@Path("userName") String userName, @Path("password") String password);

    @POST("singleplayer")
    Call<ResponseBody> sendGameResults(@Body GameResultsModel gameResultsModel);

    @GET("account/{myPlayerID}")
    Call<ProfileModel> getMyProfile(@Path("myPlayerID") int myPlayerID);

    @PUT("account")
    Call<ChangeProfileInformationModel> setNewUserName(@Body ChangeProfileInformationModel changeProfileInformationModel);

    @GET("dailyword")
    Call<ResponseBody> getDailyChallengeForToday();

    @POST("challengeattempt")
    Call<ResponseBody> sendDailyChallengeAttempt(@Body DailyChallengeAttemptModel dailyChallengeAttemptModel);

    @GET("friends/{playerId}")
    Call<String[]> getFriendList(@Path("playerId") int playerId);

    @GET("friends/requests/{playerId}")
    Call<String[]> getFriendRequests(@Path("playerId") int playerId);

    @POST("friends")
    Call<ResponseBody> sendFriendRequest(@Body FriendPairModel friendPairModel);

    @PUT("friends")
    Call<ResponseBody> respondToFriendRequest(@Body FriendResponseModel friendResponseModel);

    @HTTP(method = "DELETE", path = "friends", hasBody = true)
    Call<ResponseBody> removeFriend(@Body FriendPairModel friendPairModel);

}
