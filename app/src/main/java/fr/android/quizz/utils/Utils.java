package fr.android.quizz.utils;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    /**
     * Get random index in a list
     * @param size
     * @param nbIndex
     * @return
     */
    public static ArrayList<Integer> getRandomIndex(int size, int nbIndex) {
        ArrayList<Integer> indexList = new ArrayList<>();
        Random rdm = new Random();

        while (indexList.size() < nbIndex) {
            Integer rdmIndex = rdm.nextInt(size);

            if(!indexList.contains(rdmIndex)) {
                indexList.add(rdmIndex);
            }
        }

        return indexList;
    }

}
