package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
 private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";


    //arraylist to store question objects we got from api inside of it
    ArrayList<Question>questionArrayList=new ArrayList<>();




    //method to return question objects (with api data inside it)
    public List<Question>getQuestion(final AnswerListAsyncResponse callBack){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0 ;i<response.length() ;i++)
                {
                    Question question=new Question();
                    try {
                        question.setAnswer(response.getJSONArray(i).getString(0));
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                        questionArrayList.add(question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (callBack != null)callBack.processFinished(questionArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
    //end of the method


}
