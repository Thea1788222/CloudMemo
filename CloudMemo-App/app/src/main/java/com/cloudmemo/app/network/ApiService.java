package com.cloudmemo.app.network;

import com.cloudmemo.app.network.dto.NoteDTO;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("upload")
    Call<Integer> uploadNotes(@Body List<NoteDTO> notes);

    @GET("download")
    Call<List<NoteDTO>> downloadNotes(@Query("since") long since);
}
