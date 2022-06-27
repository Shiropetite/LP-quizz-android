package fr.android.quizz.utils;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.android.quizz.model.Question;
import fr.android.quizz.model.User;

public class Database {

    /**
     * Get 5 random questions from database
     * @return
     */
    public static MutableLiveData<ArrayList<Question>> getRandomQuestions() {
        FirebaseFirestore database = FirebaseFirestore.getInstance(); // Get database
        MutableLiveData<ArrayList<Question>> questions = new MutableLiveData<ArrayList<Question>>(new ArrayList<Question>());


        database.collection("questions").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            // Get 5 random index
                            ArrayList<Integer> indexList = Utils.getRandomIndex(list.size(), 5);

                            // Get the 5 question with the random indexes
                            for(Integer i = 0; i < indexList.size(); i++) {
                                Map<String, Object> data = list.get(indexList.get(i)).getData();
                                questions.getValue().add(new Question(
                                                data.get("statement").toString(),
                                                data.get("image").toString(),
                                                data.get("goodAnswer").toString().equals("true"),
                                                data.get("explanation").toString())
                                );
                            }

                            // Update value
                            questions.setValue(questions.getValue());
                        }
                    }
                });

        return questions;
    }

    /**
     * Update user score
     * @param user
     * @return
     */
    public static MutableLiveData<Boolean> updateScore(User user) {
        FirebaseFirestore database = FirebaseFirestore.getInstance(); // Get database
        MutableLiveData<Boolean> success = new MutableLiveData(false);

        database.collection("users").document(user.getId()).update("score", user.getScore())
                .addOnSuccessListener(new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                        success.setValue(true);
                    }

                });

        return success;
    }

    /**
     * Get user by name with his friends
     * @param username
     * @return
     */
    public static MutableLiveData<User> getUserByNameWithFriends(String username) {
        FirebaseFirestore database = FirebaseFirestore.getInstance(); // Get database
        MutableLiveData<User> user = new MutableLiveData<>(new User());

        database.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            ArrayList<String> dataFriends = new ArrayList<String>();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                String dataUsername = d.getData().get("username").toString();
                                String dataAvatar = d.getData().get("avatar").toString();
                                int dataScore = Integer.parseInt(d.getData().get("score").toString());

                                if (username.equals(dataUsername)) {
                                    user.setValue(new User(d.getId(), dataUsername, dataAvatar, dataScore));
                                    dataFriends = (ArrayList) d.getData().get("friends");
                                }

                            }

                            for(String friendId: dataFriends) {
                                for(DocumentSnapshot d : list) {
                                    if(d.getId().equals(friendId)) {
                                        String dataUsername = d.getData().get("username").toString();
                                        String dataAvatar = d.getData().get("avatar").toString();
                                        int dataScore = Integer.parseInt(d.getData().get("score").toString());

                                        user.getValue().getFriends().add(new User(d.getId(), dataUsername, dataAvatar, dataScore));
                                    }
                                }
                            }
                            user.setValue(user.getValue());

                        }
                    }
                });

        return user;
    }

    /**
     * Verify the login and password
     * @param username
     * @param password
     * @return
     */
    public static MutableLiveData<Integer> login(String username, String password) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        MutableLiveData<Integer> exist = new MutableLiveData<Integer>(-1);

        database.collection("users").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            String dataUsername = d.getData().get("username").toString();
                            String dataPassword = d.getData().get("password").toString();

                            if (username.equals(dataUsername) && password.equals(dataPassword)) {
                                exist.setValue(0);
                            }

                        }

                        if(exist.getValue() == -1) {
                            exist.setValue(1);
                        }
                    }
                }
            });

        return exist;
    }

}
